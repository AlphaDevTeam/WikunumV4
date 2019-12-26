package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.WikunumApp;
import com.alphadevs.sales.domain.InvoiceDetails;
import com.alphadevs.sales.domain.Items;
import com.alphadevs.sales.domain.Invoice;
import com.alphadevs.sales.repository.InvoiceDetailsRepository;
import com.alphadevs.sales.service.InvoiceDetailsService;
import com.alphadevs.sales.web.rest.errors.ExceptionTranslator;
import com.alphadevs.sales.service.dto.InvoiceDetailsCriteria;
import com.alphadevs.sales.service.InvoiceDetailsQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;

import static com.alphadevs.sales.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link InvoiceDetailsResource} REST controller.
 */
@SpringBootTest(classes = WikunumApp.class)
public class InvoiceDetailsResourceIT {

    private static final Double DEFAULT_INV_QTY = 1D;
    private static final Double UPDATED_INV_QTY = 2D;
    private static final Double SMALLER_INV_QTY = 1D - 1D;

    private static final BigDecimal DEFAULT_REVISED_ITEM_SALES_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_REVISED_ITEM_SALES_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_REVISED_ITEM_SALES_PRICE = new BigDecimal(1 - 1);

    @Autowired
    private InvoiceDetailsRepository invoiceDetailsRepository;

    @Autowired
    private InvoiceDetailsService invoiceDetailsService;

    @Autowired
    private InvoiceDetailsQueryService invoiceDetailsQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restInvoiceDetailsMockMvc;

    private InvoiceDetails invoiceDetails;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InvoiceDetailsResource invoiceDetailsResource = new InvoiceDetailsResource(invoiceDetailsService, invoiceDetailsQueryService);
        this.restInvoiceDetailsMockMvc = MockMvcBuilders.standaloneSetup(invoiceDetailsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvoiceDetails createEntity(EntityManager em) {
        InvoiceDetails invoiceDetails = new InvoiceDetails()
            .invQty(DEFAULT_INV_QTY)
            .revisedItemSalesPrice(DEFAULT_REVISED_ITEM_SALES_PRICE);
        // Add required entity
        Items items;
        if (TestUtil.findAll(em, Items.class).isEmpty()) {
            items = ItemsResourceIT.createEntity(em);
            em.persist(items);
            em.flush();
        } else {
            items = TestUtil.findAll(em, Items.class).get(0);
        }
        invoiceDetails.setItem(items);
        // Add required entity
        Invoice invoice;
        if (TestUtil.findAll(em, Invoice.class).isEmpty()) {
            invoice = InvoiceResourceIT.createEntity(em);
            em.persist(invoice);
            em.flush();
        } else {
            invoice = TestUtil.findAll(em, Invoice.class).get(0);
        }
        invoiceDetails.setInv(invoice);
        return invoiceDetails;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvoiceDetails createUpdatedEntity(EntityManager em) {
        InvoiceDetails invoiceDetails = new InvoiceDetails()
            .invQty(UPDATED_INV_QTY)
            .revisedItemSalesPrice(UPDATED_REVISED_ITEM_SALES_PRICE);
        // Add required entity
        Items items;
        if (TestUtil.findAll(em, Items.class).isEmpty()) {
            items = ItemsResourceIT.createUpdatedEntity(em);
            em.persist(items);
            em.flush();
        } else {
            items = TestUtil.findAll(em, Items.class).get(0);
        }
        invoiceDetails.setItem(items);
        // Add required entity
        Invoice invoice;
        if (TestUtil.findAll(em, Invoice.class).isEmpty()) {
            invoice = InvoiceResourceIT.createUpdatedEntity(em);
            em.persist(invoice);
            em.flush();
        } else {
            invoice = TestUtil.findAll(em, Invoice.class).get(0);
        }
        invoiceDetails.setInv(invoice);
        return invoiceDetails;
    }

    @BeforeEach
    public void initTest() {
        invoiceDetails = createEntity(em);
    }

    @Test
    @Transactional
    public void createInvoiceDetails() throws Exception {
        int databaseSizeBeforeCreate = invoiceDetailsRepository.findAll().size();

        // Create the InvoiceDetails
        restInvoiceDetailsMockMvc.perform(post("/api/invoice-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDetails)))
            .andExpect(status().isCreated());

        // Validate the InvoiceDetails in the database
        List<InvoiceDetails> invoiceDetailsList = invoiceDetailsRepository.findAll();
        assertThat(invoiceDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        InvoiceDetails testInvoiceDetails = invoiceDetailsList.get(invoiceDetailsList.size() - 1);
        assertThat(testInvoiceDetails.getInvQty()).isEqualTo(DEFAULT_INV_QTY);
        assertThat(testInvoiceDetails.getRevisedItemSalesPrice()).isEqualTo(DEFAULT_REVISED_ITEM_SALES_PRICE);
    }

    @Test
    @Transactional
    public void createInvoiceDetailsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = invoiceDetailsRepository.findAll().size();

        // Create the InvoiceDetails with an existing ID
        invoiceDetails.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvoiceDetailsMockMvc.perform(post("/api/invoice-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDetails)))
            .andExpect(status().isBadRequest());

        // Validate the InvoiceDetails in the database
        List<InvoiceDetails> invoiceDetailsList = invoiceDetailsRepository.findAll();
        assertThat(invoiceDetailsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkInvQtyIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceDetailsRepository.findAll().size();
        // set the field null
        invoiceDetails.setInvQty(null);

        // Create the InvoiceDetails, which fails.

        restInvoiceDetailsMockMvc.perform(post("/api/invoice-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDetails)))
            .andExpect(status().isBadRequest());

        List<InvoiceDetails> invoiceDetailsList = invoiceDetailsRepository.findAll();
        assertThat(invoiceDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInvoiceDetails() throws Exception {
        // Initialize the database
        invoiceDetailsRepository.saveAndFlush(invoiceDetails);

        // Get all the invoiceDetailsList
        restInvoiceDetailsMockMvc.perform(get("/api/invoice-details?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoiceDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].invQty").value(hasItem(DEFAULT_INV_QTY.doubleValue())))
            .andExpect(jsonPath("$.[*].revisedItemSalesPrice").value(hasItem(DEFAULT_REVISED_ITEM_SALES_PRICE.intValue())));
    }
    
    @Test
    @Transactional
    public void getInvoiceDetails() throws Exception {
        // Initialize the database
        invoiceDetailsRepository.saveAndFlush(invoiceDetails);

        // Get the invoiceDetails
        restInvoiceDetailsMockMvc.perform(get("/api/invoice-details/{id}", invoiceDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(invoiceDetails.getId().intValue()))
            .andExpect(jsonPath("$.invQty").value(DEFAULT_INV_QTY.doubleValue()))
            .andExpect(jsonPath("$.revisedItemSalesPrice").value(DEFAULT_REVISED_ITEM_SALES_PRICE.intValue()));
    }


    @Test
    @Transactional
    public void getInvoiceDetailsByIdFiltering() throws Exception {
        // Initialize the database
        invoiceDetailsRepository.saveAndFlush(invoiceDetails);

        Long id = invoiceDetails.getId();

        defaultInvoiceDetailsShouldBeFound("id.equals=" + id);
        defaultInvoiceDetailsShouldNotBeFound("id.notEquals=" + id);

        defaultInvoiceDetailsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultInvoiceDetailsShouldNotBeFound("id.greaterThan=" + id);

        defaultInvoiceDetailsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultInvoiceDetailsShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllInvoiceDetailsByInvQtyIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceDetailsRepository.saveAndFlush(invoiceDetails);

        // Get all the invoiceDetailsList where invQty equals to DEFAULT_INV_QTY
        defaultInvoiceDetailsShouldBeFound("invQty.equals=" + DEFAULT_INV_QTY);

        // Get all the invoiceDetailsList where invQty equals to UPDATED_INV_QTY
        defaultInvoiceDetailsShouldNotBeFound("invQty.equals=" + UPDATED_INV_QTY);
    }

    @Test
    @Transactional
    public void getAllInvoiceDetailsByInvQtyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        invoiceDetailsRepository.saveAndFlush(invoiceDetails);

        // Get all the invoiceDetailsList where invQty not equals to DEFAULT_INV_QTY
        defaultInvoiceDetailsShouldNotBeFound("invQty.notEquals=" + DEFAULT_INV_QTY);

        // Get all the invoiceDetailsList where invQty not equals to UPDATED_INV_QTY
        defaultInvoiceDetailsShouldBeFound("invQty.notEquals=" + UPDATED_INV_QTY);
    }

    @Test
    @Transactional
    public void getAllInvoiceDetailsByInvQtyIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceDetailsRepository.saveAndFlush(invoiceDetails);

        // Get all the invoiceDetailsList where invQty in DEFAULT_INV_QTY or UPDATED_INV_QTY
        defaultInvoiceDetailsShouldBeFound("invQty.in=" + DEFAULT_INV_QTY + "," + UPDATED_INV_QTY);

        // Get all the invoiceDetailsList where invQty equals to UPDATED_INV_QTY
        defaultInvoiceDetailsShouldNotBeFound("invQty.in=" + UPDATED_INV_QTY);
    }

    @Test
    @Transactional
    public void getAllInvoiceDetailsByInvQtyIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceDetailsRepository.saveAndFlush(invoiceDetails);

        // Get all the invoiceDetailsList where invQty is not null
        defaultInvoiceDetailsShouldBeFound("invQty.specified=true");

        // Get all the invoiceDetailsList where invQty is null
        defaultInvoiceDetailsShouldNotBeFound("invQty.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvoiceDetailsByInvQtyIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceDetailsRepository.saveAndFlush(invoiceDetails);

        // Get all the invoiceDetailsList where invQty is greater than or equal to DEFAULT_INV_QTY
        defaultInvoiceDetailsShouldBeFound("invQty.greaterThanOrEqual=" + DEFAULT_INV_QTY);

        // Get all the invoiceDetailsList where invQty is greater than or equal to UPDATED_INV_QTY
        defaultInvoiceDetailsShouldNotBeFound("invQty.greaterThanOrEqual=" + UPDATED_INV_QTY);
    }

    @Test
    @Transactional
    public void getAllInvoiceDetailsByInvQtyIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceDetailsRepository.saveAndFlush(invoiceDetails);

        // Get all the invoiceDetailsList where invQty is less than or equal to DEFAULT_INV_QTY
        defaultInvoiceDetailsShouldBeFound("invQty.lessThanOrEqual=" + DEFAULT_INV_QTY);

        // Get all the invoiceDetailsList where invQty is less than or equal to SMALLER_INV_QTY
        defaultInvoiceDetailsShouldNotBeFound("invQty.lessThanOrEqual=" + SMALLER_INV_QTY);
    }

    @Test
    @Transactional
    public void getAllInvoiceDetailsByInvQtyIsLessThanSomething() throws Exception {
        // Initialize the database
        invoiceDetailsRepository.saveAndFlush(invoiceDetails);

        // Get all the invoiceDetailsList where invQty is less than DEFAULT_INV_QTY
        defaultInvoiceDetailsShouldNotBeFound("invQty.lessThan=" + DEFAULT_INV_QTY);

        // Get all the invoiceDetailsList where invQty is less than UPDATED_INV_QTY
        defaultInvoiceDetailsShouldBeFound("invQty.lessThan=" + UPDATED_INV_QTY);
    }

    @Test
    @Transactional
    public void getAllInvoiceDetailsByInvQtyIsGreaterThanSomething() throws Exception {
        // Initialize the database
        invoiceDetailsRepository.saveAndFlush(invoiceDetails);

        // Get all the invoiceDetailsList where invQty is greater than DEFAULT_INV_QTY
        defaultInvoiceDetailsShouldNotBeFound("invQty.greaterThan=" + DEFAULT_INV_QTY);

        // Get all the invoiceDetailsList where invQty is greater than SMALLER_INV_QTY
        defaultInvoiceDetailsShouldBeFound("invQty.greaterThan=" + SMALLER_INV_QTY);
    }


    @Test
    @Transactional
    public void getAllInvoiceDetailsByRevisedItemSalesPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceDetailsRepository.saveAndFlush(invoiceDetails);

        // Get all the invoiceDetailsList where revisedItemSalesPrice equals to DEFAULT_REVISED_ITEM_SALES_PRICE
        defaultInvoiceDetailsShouldBeFound("revisedItemSalesPrice.equals=" + DEFAULT_REVISED_ITEM_SALES_PRICE);

        // Get all the invoiceDetailsList where revisedItemSalesPrice equals to UPDATED_REVISED_ITEM_SALES_PRICE
        defaultInvoiceDetailsShouldNotBeFound("revisedItemSalesPrice.equals=" + UPDATED_REVISED_ITEM_SALES_PRICE);
    }

    @Test
    @Transactional
    public void getAllInvoiceDetailsByRevisedItemSalesPriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        invoiceDetailsRepository.saveAndFlush(invoiceDetails);

        // Get all the invoiceDetailsList where revisedItemSalesPrice not equals to DEFAULT_REVISED_ITEM_SALES_PRICE
        defaultInvoiceDetailsShouldNotBeFound("revisedItemSalesPrice.notEquals=" + DEFAULT_REVISED_ITEM_SALES_PRICE);

        // Get all the invoiceDetailsList where revisedItemSalesPrice not equals to UPDATED_REVISED_ITEM_SALES_PRICE
        defaultInvoiceDetailsShouldBeFound("revisedItemSalesPrice.notEquals=" + UPDATED_REVISED_ITEM_SALES_PRICE);
    }

    @Test
    @Transactional
    public void getAllInvoiceDetailsByRevisedItemSalesPriceIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceDetailsRepository.saveAndFlush(invoiceDetails);

        // Get all the invoiceDetailsList where revisedItemSalesPrice in DEFAULT_REVISED_ITEM_SALES_PRICE or UPDATED_REVISED_ITEM_SALES_PRICE
        defaultInvoiceDetailsShouldBeFound("revisedItemSalesPrice.in=" + DEFAULT_REVISED_ITEM_SALES_PRICE + "," + UPDATED_REVISED_ITEM_SALES_PRICE);

        // Get all the invoiceDetailsList where revisedItemSalesPrice equals to UPDATED_REVISED_ITEM_SALES_PRICE
        defaultInvoiceDetailsShouldNotBeFound("revisedItemSalesPrice.in=" + UPDATED_REVISED_ITEM_SALES_PRICE);
    }

    @Test
    @Transactional
    public void getAllInvoiceDetailsByRevisedItemSalesPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceDetailsRepository.saveAndFlush(invoiceDetails);

        // Get all the invoiceDetailsList where revisedItemSalesPrice is not null
        defaultInvoiceDetailsShouldBeFound("revisedItemSalesPrice.specified=true");

        // Get all the invoiceDetailsList where revisedItemSalesPrice is null
        defaultInvoiceDetailsShouldNotBeFound("revisedItemSalesPrice.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvoiceDetailsByRevisedItemSalesPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceDetailsRepository.saveAndFlush(invoiceDetails);

        // Get all the invoiceDetailsList where revisedItemSalesPrice is greater than or equal to DEFAULT_REVISED_ITEM_SALES_PRICE
        defaultInvoiceDetailsShouldBeFound("revisedItemSalesPrice.greaterThanOrEqual=" + DEFAULT_REVISED_ITEM_SALES_PRICE);

        // Get all the invoiceDetailsList where revisedItemSalesPrice is greater than or equal to UPDATED_REVISED_ITEM_SALES_PRICE
        defaultInvoiceDetailsShouldNotBeFound("revisedItemSalesPrice.greaterThanOrEqual=" + UPDATED_REVISED_ITEM_SALES_PRICE);
    }

    @Test
    @Transactional
    public void getAllInvoiceDetailsByRevisedItemSalesPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceDetailsRepository.saveAndFlush(invoiceDetails);

        // Get all the invoiceDetailsList where revisedItemSalesPrice is less than or equal to DEFAULT_REVISED_ITEM_SALES_PRICE
        defaultInvoiceDetailsShouldBeFound("revisedItemSalesPrice.lessThanOrEqual=" + DEFAULT_REVISED_ITEM_SALES_PRICE);

        // Get all the invoiceDetailsList where revisedItemSalesPrice is less than or equal to SMALLER_REVISED_ITEM_SALES_PRICE
        defaultInvoiceDetailsShouldNotBeFound("revisedItemSalesPrice.lessThanOrEqual=" + SMALLER_REVISED_ITEM_SALES_PRICE);
    }

    @Test
    @Transactional
    public void getAllInvoiceDetailsByRevisedItemSalesPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        invoiceDetailsRepository.saveAndFlush(invoiceDetails);

        // Get all the invoiceDetailsList where revisedItemSalesPrice is less than DEFAULT_REVISED_ITEM_SALES_PRICE
        defaultInvoiceDetailsShouldNotBeFound("revisedItemSalesPrice.lessThan=" + DEFAULT_REVISED_ITEM_SALES_PRICE);

        // Get all the invoiceDetailsList where revisedItemSalesPrice is less than UPDATED_REVISED_ITEM_SALES_PRICE
        defaultInvoiceDetailsShouldBeFound("revisedItemSalesPrice.lessThan=" + UPDATED_REVISED_ITEM_SALES_PRICE);
    }

    @Test
    @Transactional
    public void getAllInvoiceDetailsByRevisedItemSalesPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        invoiceDetailsRepository.saveAndFlush(invoiceDetails);

        // Get all the invoiceDetailsList where revisedItemSalesPrice is greater than DEFAULT_REVISED_ITEM_SALES_PRICE
        defaultInvoiceDetailsShouldNotBeFound("revisedItemSalesPrice.greaterThan=" + DEFAULT_REVISED_ITEM_SALES_PRICE);

        // Get all the invoiceDetailsList where revisedItemSalesPrice is greater than SMALLER_REVISED_ITEM_SALES_PRICE
        defaultInvoiceDetailsShouldBeFound("revisedItemSalesPrice.greaterThan=" + SMALLER_REVISED_ITEM_SALES_PRICE);
    }


    @Test
    @Transactional
    public void getAllInvoiceDetailsByItemIsEqualToSomething() throws Exception {
        // Get already existing entity
        Items item = invoiceDetails.getItem();
        invoiceDetailsRepository.saveAndFlush(invoiceDetails);
        Long itemId = item.getId();

        // Get all the invoiceDetailsList where item equals to itemId
        defaultInvoiceDetailsShouldBeFound("itemId.equals=" + itemId);

        // Get all the invoiceDetailsList where item equals to itemId + 1
        defaultInvoiceDetailsShouldNotBeFound("itemId.equals=" + (itemId + 1));
    }


    @Test
    @Transactional
    public void getAllInvoiceDetailsByInvIsEqualToSomething() throws Exception {
        // Get already existing entity
        Invoice inv = invoiceDetails.getInv();
        invoiceDetailsRepository.saveAndFlush(invoiceDetails);
        Long invId = inv.getId();

        // Get all the invoiceDetailsList where inv equals to invId
        defaultInvoiceDetailsShouldBeFound("invId.equals=" + invId);

        // Get all the invoiceDetailsList where inv equals to invId + 1
        defaultInvoiceDetailsShouldNotBeFound("invId.equals=" + (invId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInvoiceDetailsShouldBeFound(String filter) throws Exception {
        restInvoiceDetailsMockMvc.perform(get("/api/invoice-details?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoiceDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].invQty").value(hasItem(DEFAULT_INV_QTY.doubleValue())))
            .andExpect(jsonPath("$.[*].revisedItemSalesPrice").value(hasItem(DEFAULT_REVISED_ITEM_SALES_PRICE.intValue())));

        // Check, that the count call also returns 1
        restInvoiceDetailsMockMvc.perform(get("/api/invoice-details/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInvoiceDetailsShouldNotBeFound(String filter) throws Exception {
        restInvoiceDetailsMockMvc.perform(get("/api/invoice-details?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInvoiceDetailsMockMvc.perform(get("/api/invoice-details/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingInvoiceDetails() throws Exception {
        // Get the invoiceDetails
        restInvoiceDetailsMockMvc.perform(get("/api/invoice-details/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInvoiceDetails() throws Exception {
        // Initialize the database
        invoiceDetailsService.save(invoiceDetails);

        int databaseSizeBeforeUpdate = invoiceDetailsRepository.findAll().size();

        // Update the invoiceDetails
        InvoiceDetails updatedInvoiceDetails = invoiceDetailsRepository.findById(invoiceDetails.getId()).get();
        // Disconnect from session so that the updates on updatedInvoiceDetails are not directly saved in db
        em.detach(updatedInvoiceDetails);
        updatedInvoiceDetails
            .invQty(UPDATED_INV_QTY)
            .revisedItemSalesPrice(UPDATED_REVISED_ITEM_SALES_PRICE);

        restInvoiceDetailsMockMvc.perform(put("/api/invoice-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedInvoiceDetails)))
            .andExpect(status().isOk());

        // Validate the InvoiceDetails in the database
        List<InvoiceDetails> invoiceDetailsList = invoiceDetailsRepository.findAll();
        assertThat(invoiceDetailsList).hasSize(databaseSizeBeforeUpdate);
        InvoiceDetails testInvoiceDetails = invoiceDetailsList.get(invoiceDetailsList.size() - 1);
        assertThat(testInvoiceDetails.getInvQty()).isEqualTo(UPDATED_INV_QTY);
        assertThat(testInvoiceDetails.getRevisedItemSalesPrice()).isEqualTo(UPDATED_REVISED_ITEM_SALES_PRICE);
    }

    @Test
    @Transactional
    public void updateNonExistingInvoiceDetails() throws Exception {
        int databaseSizeBeforeUpdate = invoiceDetailsRepository.findAll().size();

        // Create the InvoiceDetails

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceDetailsMockMvc.perform(put("/api/invoice-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDetails)))
            .andExpect(status().isBadRequest());

        // Validate the InvoiceDetails in the database
        List<InvoiceDetails> invoiceDetailsList = invoiceDetailsRepository.findAll();
        assertThat(invoiceDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteInvoiceDetails() throws Exception {
        // Initialize the database
        invoiceDetailsService.save(invoiceDetails);

        int databaseSizeBeforeDelete = invoiceDetailsRepository.findAll().size();

        // Delete the invoiceDetails
        restInvoiceDetailsMockMvc.perform(delete("/api/invoice-details/{id}", invoiceDetails.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InvoiceDetails> invoiceDetailsList = invoiceDetailsRepository.findAll();
        assertThat(invoiceDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

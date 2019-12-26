package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.WikunumApp;
import com.alphadevs.sales.domain.GoodsReceiptDetails;
import com.alphadevs.sales.domain.Items;
import com.alphadevs.sales.domain.StorageBin;
import com.alphadevs.sales.domain.GoodsReceipt;
import com.alphadevs.sales.repository.GoodsReceiptDetailsRepository;
import com.alphadevs.sales.service.GoodsReceiptDetailsService;
import com.alphadevs.sales.web.rest.errors.ExceptionTranslator;
import com.alphadevs.sales.service.dto.GoodsReceiptDetailsCriteria;
import com.alphadevs.sales.service.GoodsReceiptDetailsQueryService;

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
 * Integration tests for the {@link GoodsReceiptDetailsResource} REST controller.
 */
@SpringBootTest(classes = WikunumApp.class)
public class GoodsReceiptDetailsResourceIT {

    private static final Double DEFAULT_GRN_QTY = 1D;
    private static final Double UPDATED_GRN_QTY = 2D;
    private static final Double SMALLER_GRN_QTY = 1D - 1D;

    private static final BigDecimal DEFAULT_REVISED_ITEM_COST = new BigDecimal(1);
    private static final BigDecimal UPDATED_REVISED_ITEM_COST = new BigDecimal(2);
    private static final BigDecimal SMALLER_REVISED_ITEM_COST = new BigDecimal(1 - 1);

    @Autowired
    private GoodsReceiptDetailsRepository goodsReceiptDetailsRepository;

    @Autowired
    private GoodsReceiptDetailsService goodsReceiptDetailsService;

    @Autowired
    private GoodsReceiptDetailsQueryService goodsReceiptDetailsQueryService;

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

    private MockMvc restGoodsReceiptDetailsMockMvc;

    private GoodsReceiptDetails goodsReceiptDetails;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GoodsReceiptDetailsResource goodsReceiptDetailsResource = new GoodsReceiptDetailsResource(goodsReceiptDetailsService, goodsReceiptDetailsQueryService);
        this.restGoodsReceiptDetailsMockMvc = MockMvcBuilders.standaloneSetup(goodsReceiptDetailsResource)
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
    public static GoodsReceiptDetails createEntity(EntityManager em) {
        GoodsReceiptDetails goodsReceiptDetails = new GoodsReceiptDetails()
            .grnQty(DEFAULT_GRN_QTY)
            .revisedItemCost(DEFAULT_REVISED_ITEM_COST);
        // Add required entity
        Items items;
        if (TestUtil.findAll(em, Items.class).isEmpty()) {
            items = ItemsResourceIT.createEntity(em);
            em.persist(items);
            em.flush();
        } else {
            items = TestUtil.findAll(em, Items.class).get(0);
        }
        goodsReceiptDetails.setItem(items);
        // Add required entity
        GoodsReceipt goodsReceipt;
        if (TestUtil.findAll(em, GoodsReceipt.class).isEmpty()) {
            goodsReceipt = GoodsReceiptResourceIT.createEntity(em);
            em.persist(goodsReceipt);
            em.flush();
        } else {
            goodsReceipt = TestUtil.findAll(em, GoodsReceipt.class).get(0);
        }
        goodsReceiptDetails.setGrn(goodsReceipt);
        return goodsReceiptDetails;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GoodsReceiptDetails createUpdatedEntity(EntityManager em) {
        GoodsReceiptDetails goodsReceiptDetails = new GoodsReceiptDetails()
            .grnQty(UPDATED_GRN_QTY)
            .revisedItemCost(UPDATED_REVISED_ITEM_COST);
        // Add required entity
        Items items;
        if (TestUtil.findAll(em, Items.class).isEmpty()) {
            items = ItemsResourceIT.createUpdatedEntity(em);
            em.persist(items);
            em.flush();
        } else {
            items = TestUtil.findAll(em, Items.class).get(0);
        }
        goodsReceiptDetails.setItem(items);
        // Add required entity
        GoodsReceipt goodsReceipt;
        if (TestUtil.findAll(em, GoodsReceipt.class).isEmpty()) {
            goodsReceipt = GoodsReceiptResourceIT.createUpdatedEntity(em);
            em.persist(goodsReceipt);
            em.flush();
        } else {
            goodsReceipt = TestUtil.findAll(em, GoodsReceipt.class).get(0);
        }
        goodsReceiptDetails.setGrn(goodsReceipt);
        return goodsReceiptDetails;
    }

    @BeforeEach
    public void initTest() {
        goodsReceiptDetails = createEntity(em);
    }

    @Test
    @Transactional
    public void createGoodsReceiptDetails() throws Exception {
        int databaseSizeBeforeCreate = goodsReceiptDetailsRepository.findAll().size();

        // Create the GoodsReceiptDetails
        restGoodsReceiptDetailsMockMvc.perform(post("/api/goods-receipt-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(goodsReceiptDetails)))
            .andExpect(status().isCreated());

        // Validate the GoodsReceiptDetails in the database
        List<GoodsReceiptDetails> goodsReceiptDetailsList = goodsReceiptDetailsRepository.findAll();
        assertThat(goodsReceiptDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        GoodsReceiptDetails testGoodsReceiptDetails = goodsReceiptDetailsList.get(goodsReceiptDetailsList.size() - 1);
        assertThat(testGoodsReceiptDetails.getGrnQty()).isEqualTo(DEFAULT_GRN_QTY);
        assertThat(testGoodsReceiptDetails.getRevisedItemCost()).isEqualTo(DEFAULT_REVISED_ITEM_COST);
    }

    @Test
    @Transactional
    public void createGoodsReceiptDetailsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = goodsReceiptDetailsRepository.findAll().size();

        // Create the GoodsReceiptDetails with an existing ID
        goodsReceiptDetails.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGoodsReceiptDetailsMockMvc.perform(post("/api/goods-receipt-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(goodsReceiptDetails)))
            .andExpect(status().isBadRequest());

        // Validate the GoodsReceiptDetails in the database
        List<GoodsReceiptDetails> goodsReceiptDetailsList = goodsReceiptDetailsRepository.findAll();
        assertThat(goodsReceiptDetailsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkGrnQtyIsRequired() throws Exception {
        int databaseSizeBeforeTest = goodsReceiptDetailsRepository.findAll().size();
        // set the field null
        goodsReceiptDetails.setGrnQty(null);

        // Create the GoodsReceiptDetails, which fails.

        restGoodsReceiptDetailsMockMvc.perform(post("/api/goods-receipt-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(goodsReceiptDetails)))
            .andExpect(status().isBadRequest());

        List<GoodsReceiptDetails> goodsReceiptDetailsList = goodsReceiptDetailsRepository.findAll();
        assertThat(goodsReceiptDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptDetails() throws Exception {
        // Initialize the database
        goodsReceiptDetailsRepository.saveAndFlush(goodsReceiptDetails);

        // Get all the goodsReceiptDetailsList
        restGoodsReceiptDetailsMockMvc.perform(get("/api/goods-receipt-details?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(goodsReceiptDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].grnQty").value(hasItem(DEFAULT_GRN_QTY.doubleValue())))
            .andExpect(jsonPath("$.[*].revisedItemCost").value(hasItem(DEFAULT_REVISED_ITEM_COST.intValue())));
    }
    
    @Test
    @Transactional
    public void getGoodsReceiptDetails() throws Exception {
        // Initialize the database
        goodsReceiptDetailsRepository.saveAndFlush(goodsReceiptDetails);

        // Get the goodsReceiptDetails
        restGoodsReceiptDetailsMockMvc.perform(get("/api/goods-receipt-details/{id}", goodsReceiptDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(goodsReceiptDetails.getId().intValue()))
            .andExpect(jsonPath("$.grnQty").value(DEFAULT_GRN_QTY.doubleValue()))
            .andExpect(jsonPath("$.revisedItemCost").value(DEFAULT_REVISED_ITEM_COST.intValue()));
    }


    @Test
    @Transactional
    public void getGoodsReceiptDetailsByIdFiltering() throws Exception {
        // Initialize the database
        goodsReceiptDetailsRepository.saveAndFlush(goodsReceiptDetails);

        Long id = goodsReceiptDetails.getId();

        defaultGoodsReceiptDetailsShouldBeFound("id.equals=" + id);
        defaultGoodsReceiptDetailsShouldNotBeFound("id.notEquals=" + id);

        defaultGoodsReceiptDetailsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultGoodsReceiptDetailsShouldNotBeFound("id.greaterThan=" + id);

        defaultGoodsReceiptDetailsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultGoodsReceiptDetailsShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllGoodsReceiptDetailsByGrnQtyIsEqualToSomething() throws Exception {
        // Initialize the database
        goodsReceiptDetailsRepository.saveAndFlush(goodsReceiptDetails);

        // Get all the goodsReceiptDetailsList where grnQty equals to DEFAULT_GRN_QTY
        defaultGoodsReceiptDetailsShouldBeFound("grnQty.equals=" + DEFAULT_GRN_QTY);

        // Get all the goodsReceiptDetailsList where grnQty equals to UPDATED_GRN_QTY
        defaultGoodsReceiptDetailsShouldNotBeFound("grnQty.equals=" + UPDATED_GRN_QTY);
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptDetailsByGrnQtyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        goodsReceiptDetailsRepository.saveAndFlush(goodsReceiptDetails);

        // Get all the goodsReceiptDetailsList where grnQty not equals to DEFAULT_GRN_QTY
        defaultGoodsReceiptDetailsShouldNotBeFound("grnQty.notEquals=" + DEFAULT_GRN_QTY);

        // Get all the goodsReceiptDetailsList where grnQty not equals to UPDATED_GRN_QTY
        defaultGoodsReceiptDetailsShouldBeFound("grnQty.notEquals=" + UPDATED_GRN_QTY);
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptDetailsByGrnQtyIsInShouldWork() throws Exception {
        // Initialize the database
        goodsReceiptDetailsRepository.saveAndFlush(goodsReceiptDetails);

        // Get all the goodsReceiptDetailsList where grnQty in DEFAULT_GRN_QTY or UPDATED_GRN_QTY
        defaultGoodsReceiptDetailsShouldBeFound("grnQty.in=" + DEFAULT_GRN_QTY + "," + UPDATED_GRN_QTY);

        // Get all the goodsReceiptDetailsList where grnQty equals to UPDATED_GRN_QTY
        defaultGoodsReceiptDetailsShouldNotBeFound("grnQty.in=" + UPDATED_GRN_QTY);
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptDetailsByGrnQtyIsNullOrNotNull() throws Exception {
        // Initialize the database
        goodsReceiptDetailsRepository.saveAndFlush(goodsReceiptDetails);

        // Get all the goodsReceiptDetailsList where grnQty is not null
        defaultGoodsReceiptDetailsShouldBeFound("grnQty.specified=true");

        // Get all the goodsReceiptDetailsList where grnQty is null
        defaultGoodsReceiptDetailsShouldNotBeFound("grnQty.specified=false");
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptDetailsByGrnQtyIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        goodsReceiptDetailsRepository.saveAndFlush(goodsReceiptDetails);

        // Get all the goodsReceiptDetailsList where grnQty is greater than or equal to DEFAULT_GRN_QTY
        defaultGoodsReceiptDetailsShouldBeFound("grnQty.greaterThanOrEqual=" + DEFAULT_GRN_QTY);

        // Get all the goodsReceiptDetailsList where grnQty is greater than or equal to UPDATED_GRN_QTY
        defaultGoodsReceiptDetailsShouldNotBeFound("grnQty.greaterThanOrEqual=" + UPDATED_GRN_QTY);
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptDetailsByGrnQtyIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        goodsReceiptDetailsRepository.saveAndFlush(goodsReceiptDetails);

        // Get all the goodsReceiptDetailsList where grnQty is less than or equal to DEFAULT_GRN_QTY
        defaultGoodsReceiptDetailsShouldBeFound("grnQty.lessThanOrEqual=" + DEFAULT_GRN_QTY);

        // Get all the goodsReceiptDetailsList where grnQty is less than or equal to SMALLER_GRN_QTY
        defaultGoodsReceiptDetailsShouldNotBeFound("grnQty.lessThanOrEqual=" + SMALLER_GRN_QTY);
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptDetailsByGrnQtyIsLessThanSomething() throws Exception {
        // Initialize the database
        goodsReceiptDetailsRepository.saveAndFlush(goodsReceiptDetails);

        // Get all the goodsReceiptDetailsList where grnQty is less than DEFAULT_GRN_QTY
        defaultGoodsReceiptDetailsShouldNotBeFound("grnQty.lessThan=" + DEFAULT_GRN_QTY);

        // Get all the goodsReceiptDetailsList where grnQty is less than UPDATED_GRN_QTY
        defaultGoodsReceiptDetailsShouldBeFound("grnQty.lessThan=" + UPDATED_GRN_QTY);
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptDetailsByGrnQtyIsGreaterThanSomething() throws Exception {
        // Initialize the database
        goodsReceiptDetailsRepository.saveAndFlush(goodsReceiptDetails);

        // Get all the goodsReceiptDetailsList where grnQty is greater than DEFAULT_GRN_QTY
        defaultGoodsReceiptDetailsShouldNotBeFound("grnQty.greaterThan=" + DEFAULT_GRN_QTY);

        // Get all the goodsReceiptDetailsList where grnQty is greater than SMALLER_GRN_QTY
        defaultGoodsReceiptDetailsShouldBeFound("grnQty.greaterThan=" + SMALLER_GRN_QTY);
    }


    @Test
    @Transactional
    public void getAllGoodsReceiptDetailsByRevisedItemCostIsEqualToSomething() throws Exception {
        // Initialize the database
        goodsReceiptDetailsRepository.saveAndFlush(goodsReceiptDetails);

        // Get all the goodsReceiptDetailsList where revisedItemCost equals to DEFAULT_REVISED_ITEM_COST
        defaultGoodsReceiptDetailsShouldBeFound("revisedItemCost.equals=" + DEFAULT_REVISED_ITEM_COST);

        // Get all the goodsReceiptDetailsList where revisedItemCost equals to UPDATED_REVISED_ITEM_COST
        defaultGoodsReceiptDetailsShouldNotBeFound("revisedItemCost.equals=" + UPDATED_REVISED_ITEM_COST);
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptDetailsByRevisedItemCostIsNotEqualToSomething() throws Exception {
        // Initialize the database
        goodsReceiptDetailsRepository.saveAndFlush(goodsReceiptDetails);

        // Get all the goodsReceiptDetailsList where revisedItemCost not equals to DEFAULT_REVISED_ITEM_COST
        defaultGoodsReceiptDetailsShouldNotBeFound("revisedItemCost.notEquals=" + DEFAULT_REVISED_ITEM_COST);

        // Get all the goodsReceiptDetailsList where revisedItemCost not equals to UPDATED_REVISED_ITEM_COST
        defaultGoodsReceiptDetailsShouldBeFound("revisedItemCost.notEquals=" + UPDATED_REVISED_ITEM_COST);
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptDetailsByRevisedItemCostIsInShouldWork() throws Exception {
        // Initialize the database
        goodsReceiptDetailsRepository.saveAndFlush(goodsReceiptDetails);

        // Get all the goodsReceiptDetailsList where revisedItemCost in DEFAULT_REVISED_ITEM_COST or UPDATED_REVISED_ITEM_COST
        defaultGoodsReceiptDetailsShouldBeFound("revisedItemCost.in=" + DEFAULT_REVISED_ITEM_COST + "," + UPDATED_REVISED_ITEM_COST);

        // Get all the goodsReceiptDetailsList where revisedItemCost equals to UPDATED_REVISED_ITEM_COST
        defaultGoodsReceiptDetailsShouldNotBeFound("revisedItemCost.in=" + UPDATED_REVISED_ITEM_COST);
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptDetailsByRevisedItemCostIsNullOrNotNull() throws Exception {
        // Initialize the database
        goodsReceiptDetailsRepository.saveAndFlush(goodsReceiptDetails);

        // Get all the goodsReceiptDetailsList where revisedItemCost is not null
        defaultGoodsReceiptDetailsShouldBeFound("revisedItemCost.specified=true");

        // Get all the goodsReceiptDetailsList where revisedItemCost is null
        defaultGoodsReceiptDetailsShouldNotBeFound("revisedItemCost.specified=false");
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptDetailsByRevisedItemCostIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        goodsReceiptDetailsRepository.saveAndFlush(goodsReceiptDetails);

        // Get all the goodsReceiptDetailsList where revisedItemCost is greater than or equal to DEFAULT_REVISED_ITEM_COST
        defaultGoodsReceiptDetailsShouldBeFound("revisedItemCost.greaterThanOrEqual=" + DEFAULT_REVISED_ITEM_COST);

        // Get all the goodsReceiptDetailsList where revisedItemCost is greater than or equal to UPDATED_REVISED_ITEM_COST
        defaultGoodsReceiptDetailsShouldNotBeFound("revisedItemCost.greaterThanOrEqual=" + UPDATED_REVISED_ITEM_COST);
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptDetailsByRevisedItemCostIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        goodsReceiptDetailsRepository.saveAndFlush(goodsReceiptDetails);

        // Get all the goodsReceiptDetailsList where revisedItemCost is less than or equal to DEFAULT_REVISED_ITEM_COST
        defaultGoodsReceiptDetailsShouldBeFound("revisedItemCost.lessThanOrEqual=" + DEFAULT_REVISED_ITEM_COST);

        // Get all the goodsReceiptDetailsList where revisedItemCost is less than or equal to SMALLER_REVISED_ITEM_COST
        defaultGoodsReceiptDetailsShouldNotBeFound("revisedItemCost.lessThanOrEqual=" + SMALLER_REVISED_ITEM_COST);
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptDetailsByRevisedItemCostIsLessThanSomething() throws Exception {
        // Initialize the database
        goodsReceiptDetailsRepository.saveAndFlush(goodsReceiptDetails);

        // Get all the goodsReceiptDetailsList where revisedItemCost is less than DEFAULT_REVISED_ITEM_COST
        defaultGoodsReceiptDetailsShouldNotBeFound("revisedItemCost.lessThan=" + DEFAULT_REVISED_ITEM_COST);

        // Get all the goodsReceiptDetailsList where revisedItemCost is less than UPDATED_REVISED_ITEM_COST
        defaultGoodsReceiptDetailsShouldBeFound("revisedItemCost.lessThan=" + UPDATED_REVISED_ITEM_COST);
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptDetailsByRevisedItemCostIsGreaterThanSomething() throws Exception {
        // Initialize the database
        goodsReceiptDetailsRepository.saveAndFlush(goodsReceiptDetails);

        // Get all the goodsReceiptDetailsList where revisedItemCost is greater than DEFAULT_REVISED_ITEM_COST
        defaultGoodsReceiptDetailsShouldNotBeFound("revisedItemCost.greaterThan=" + DEFAULT_REVISED_ITEM_COST);

        // Get all the goodsReceiptDetailsList where revisedItemCost is greater than SMALLER_REVISED_ITEM_COST
        defaultGoodsReceiptDetailsShouldBeFound("revisedItemCost.greaterThan=" + SMALLER_REVISED_ITEM_COST);
    }


    @Test
    @Transactional
    public void getAllGoodsReceiptDetailsByItemIsEqualToSomething() throws Exception {
        // Get already existing entity
        Items item = goodsReceiptDetails.getItem();
        goodsReceiptDetailsRepository.saveAndFlush(goodsReceiptDetails);
        Long itemId = item.getId();

        // Get all the goodsReceiptDetailsList where item equals to itemId
        defaultGoodsReceiptDetailsShouldBeFound("itemId.equals=" + itemId);

        // Get all the goodsReceiptDetailsList where item equals to itemId + 1
        defaultGoodsReceiptDetailsShouldNotBeFound("itemId.equals=" + (itemId + 1));
    }


    @Test
    @Transactional
    public void getAllGoodsReceiptDetailsByStorageBinIsEqualToSomething() throws Exception {
        // Initialize the database
        goodsReceiptDetailsRepository.saveAndFlush(goodsReceiptDetails);
        StorageBin storageBin = StorageBinResourceIT.createEntity(em);
        em.persist(storageBin);
        em.flush();
        goodsReceiptDetails.setStorageBin(storageBin);
        goodsReceiptDetailsRepository.saveAndFlush(goodsReceiptDetails);
        Long storageBinId = storageBin.getId();

        // Get all the goodsReceiptDetailsList where storageBin equals to storageBinId
        defaultGoodsReceiptDetailsShouldBeFound("storageBinId.equals=" + storageBinId);

        // Get all the goodsReceiptDetailsList where storageBin equals to storageBinId + 1
        defaultGoodsReceiptDetailsShouldNotBeFound("storageBinId.equals=" + (storageBinId + 1));
    }


    @Test
    @Transactional
    public void getAllGoodsReceiptDetailsByGrnIsEqualToSomething() throws Exception {
        // Get already existing entity
        GoodsReceipt grn = goodsReceiptDetails.getGrn();
        goodsReceiptDetailsRepository.saveAndFlush(goodsReceiptDetails);
        Long grnId = grn.getId();

        // Get all the goodsReceiptDetailsList where grn equals to grnId
        defaultGoodsReceiptDetailsShouldBeFound("grnId.equals=" + grnId);

        // Get all the goodsReceiptDetailsList where grn equals to grnId + 1
        defaultGoodsReceiptDetailsShouldNotBeFound("grnId.equals=" + (grnId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGoodsReceiptDetailsShouldBeFound(String filter) throws Exception {
        restGoodsReceiptDetailsMockMvc.perform(get("/api/goods-receipt-details?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(goodsReceiptDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].grnQty").value(hasItem(DEFAULT_GRN_QTY.doubleValue())))
            .andExpect(jsonPath("$.[*].revisedItemCost").value(hasItem(DEFAULT_REVISED_ITEM_COST.intValue())));

        // Check, that the count call also returns 1
        restGoodsReceiptDetailsMockMvc.perform(get("/api/goods-receipt-details/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGoodsReceiptDetailsShouldNotBeFound(String filter) throws Exception {
        restGoodsReceiptDetailsMockMvc.perform(get("/api/goods-receipt-details?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGoodsReceiptDetailsMockMvc.perform(get("/api/goods-receipt-details/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingGoodsReceiptDetails() throws Exception {
        // Get the goodsReceiptDetails
        restGoodsReceiptDetailsMockMvc.perform(get("/api/goods-receipt-details/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGoodsReceiptDetails() throws Exception {
        // Initialize the database
        goodsReceiptDetailsService.save(goodsReceiptDetails);

        int databaseSizeBeforeUpdate = goodsReceiptDetailsRepository.findAll().size();

        // Update the goodsReceiptDetails
        GoodsReceiptDetails updatedGoodsReceiptDetails = goodsReceiptDetailsRepository.findById(goodsReceiptDetails.getId()).get();
        // Disconnect from session so that the updates on updatedGoodsReceiptDetails are not directly saved in db
        em.detach(updatedGoodsReceiptDetails);
        updatedGoodsReceiptDetails
            .grnQty(UPDATED_GRN_QTY)
            .revisedItemCost(UPDATED_REVISED_ITEM_COST);

        restGoodsReceiptDetailsMockMvc.perform(put("/api/goods-receipt-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedGoodsReceiptDetails)))
            .andExpect(status().isOk());

        // Validate the GoodsReceiptDetails in the database
        List<GoodsReceiptDetails> goodsReceiptDetailsList = goodsReceiptDetailsRepository.findAll();
        assertThat(goodsReceiptDetailsList).hasSize(databaseSizeBeforeUpdate);
        GoodsReceiptDetails testGoodsReceiptDetails = goodsReceiptDetailsList.get(goodsReceiptDetailsList.size() - 1);
        assertThat(testGoodsReceiptDetails.getGrnQty()).isEqualTo(UPDATED_GRN_QTY);
        assertThat(testGoodsReceiptDetails.getRevisedItemCost()).isEqualTo(UPDATED_REVISED_ITEM_COST);
    }

    @Test
    @Transactional
    public void updateNonExistingGoodsReceiptDetails() throws Exception {
        int databaseSizeBeforeUpdate = goodsReceiptDetailsRepository.findAll().size();

        // Create the GoodsReceiptDetails

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGoodsReceiptDetailsMockMvc.perform(put("/api/goods-receipt-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(goodsReceiptDetails)))
            .andExpect(status().isBadRequest());

        // Validate the GoodsReceiptDetails in the database
        List<GoodsReceiptDetails> goodsReceiptDetailsList = goodsReceiptDetailsRepository.findAll();
        assertThat(goodsReceiptDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteGoodsReceiptDetails() throws Exception {
        // Initialize the database
        goodsReceiptDetailsService.save(goodsReceiptDetails);

        int databaseSizeBeforeDelete = goodsReceiptDetailsRepository.findAll().size();

        // Delete the goodsReceiptDetails
        restGoodsReceiptDetailsMockMvc.perform(delete("/api/goods-receipt-details/{id}", goodsReceiptDetails.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GoodsReceiptDetails> goodsReceiptDetailsList = goodsReceiptDetailsRepository.findAll();
        assertThat(goodsReceiptDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

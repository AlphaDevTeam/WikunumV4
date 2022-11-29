package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.WikunumApp;
import com.alphadevs.sales.domain.QuotationDetails;
import com.alphadevs.sales.domain.Items;
import com.alphadevs.sales.domain.Quotation;
import com.alphadevs.sales.repository.QuotationDetailsRepository;
import com.alphadevs.sales.service.QuotationDetailsService;
import com.alphadevs.sales.web.rest.errors.ExceptionTranslator;
import com.alphadevs.sales.service.dto.QuotationDetailsCriteria;
import com.alphadevs.sales.service.QuotationDetailsQueryService;

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
import java.util.List;

import static com.alphadevs.sales.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link QuotationDetailsResource} REST controller.
 */
@SpringBootTest(classes = WikunumApp.class)
public class QuotationDetailsResourceIT {

    private static final Double DEFAULT_RATE = 1D;
    private static final Double UPDATED_RATE = 2D;
    private static final Double SMALLER_RATE = 1D - 1D;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private QuotationDetailsRepository quotationDetailsRepository;

    @Autowired
    private QuotationDetailsService quotationDetailsService;

    @Autowired
    private QuotationDetailsQueryService quotationDetailsQueryService;

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

    private MockMvc restQuotationDetailsMockMvc;

    private QuotationDetails quotationDetails;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final QuotationDetailsResource quotationDetailsResource = new QuotationDetailsResource(quotationDetailsService, quotationDetailsQueryService);
        this.restQuotationDetailsMockMvc = MockMvcBuilders.standaloneSetup(quotationDetailsResource)
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
    public static QuotationDetails createEntity(EntityManager em) {
        QuotationDetails quotationDetails = new QuotationDetails()
            .rate(DEFAULT_RATE)
            .description(DEFAULT_DESCRIPTION);
        // Add required entity
        Items items;
        if (TestUtil.findAll(em, Items.class).isEmpty()) {
            items = ItemsResourceIT.createEntity(em);
            em.persist(items);
            em.flush();
        } else {
            items = TestUtil.findAll(em, Items.class).get(0);
        }
        quotationDetails.setItem(items);
        // Add required entity
        Quotation quotation;
        if (TestUtil.findAll(em, Quotation.class).isEmpty()) {
            quotation = QuotationResourceIT.createEntity(em);
            em.persist(quotation);
            em.flush();
        } else {
            quotation = TestUtil.findAll(em, Quotation.class).get(0);
        }
        quotationDetails.setQuote(quotation);
        return quotationDetails;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuotationDetails createUpdatedEntity(EntityManager em) {
        QuotationDetails quotationDetails = new QuotationDetails()
            .rate(UPDATED_RATE)
            .description(UPDATED_DESCRIPTION);
        // Add required entity
        Items items;
        if (TestUtil.findAll(em, Items.class).isEmpty()) {
            items = ItemsResourceIT.createUpdatedEntity(em);
            em.persist(items);
            em.flush();
        } else {
            items = TestUtil.findAll(em, Items.class).get(0);
        }
        quotationDetails.setItem(items);
        // Add required entity
        Quotation quotation;
        if (TestUtil.findAll(em, Quotation.class).isEmpty()) {
            quotation = QuotationResourceIT.createUpdatedEntity(em);
            em.persist(quotation);
            em.flush();
        } else {
            quotation = TestUtil.findAll(em, Quotation.class).get(0);
        }
        quotationDetails.setQuote(quotation);
        return quotationDetails;
    }

    @BeforeEach
    public void initTest() {
        quotationDetails = createEntity(em);
    }

    @Test
    @Transactional
    public void createQuotationDetails() throws Exception {
        int databaseSizeBeforeCreate = quotationDetailsRepository.findAll().size();

        // Create the QuotationDetails
        restQuotationDetailsMockMvc.perform(post("/api/quotation-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quotationDetails)))
            .andExpect(status().isCreated());

        // Validate the QuotationDetails in the database
        List<QuotationDetails> quotationDetailsList = quotationDetailsRepository.findAll();
        assertThat(quotationDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        QuotationDetails testQuotationDetails = quotationDetailsList.get(quotationDetailsList.size() - 1);
        assertThat(testQuotationDetails.getRate()).isEqualTo(DEFAULT_RATE);
        assertThat(testQuotationDetails.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createQuotationDetailsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = quotationDetailsRepository.findAll().size();

        // Create the QuotationDetails with an existing ID
        quotationDetails.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuotationDetailsMockMvc.perform(post("/api/quotation-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quotationDetails)))
            .andExpect(status().isBadRequest());

        // Validate the QuotationDetails in the database
        List<QuotationDetails> quotationDetailsList = quotationDetailsRepository.findAll();
        assertThat(quotationDetailsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkRateIsRequired() throws Exception {
        int databaseSizeBeforeTest = quotationDetailsRepository.findAll().size();
        // set the field null
        quotationDetails.setRate(null);

        // Create the QuotationDetails, which fails.

        restQuotationDetailsMockMvc.perform(post("/api/quotation-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quotationDetails)))
            .andExpect(status().isBadRequest());

        List<QuotationDetails> quotationDetailsList = quotationDetailsRepository.findAll();
        assertThat(quotationDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllQuotationDetails() throws Exception {
        // Initialize the database
        quotationDetailsRepository.saveAndFlush(quotationDetails);

        // Get all the quotationDetailsList
        restQuotationDetailsMockMvc.perform(get("/api/quotation-details?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quotationDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].rate").value(hasItem(DEFAULT_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
    
    @Test
    @Transactional
    public void getQuotationDetails() throws Exception {
        // Initialize the database
        quotationDetailsRepository.saveAndFlush(quotationDetails);

        // Get the quotationDetails
        restQuotationDetailsMockMvc.perform(get("/api/quotation-details/{id}", quotationDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(quotationDetails.getId().intValue()))
            .andExpect(jsonPath("$.rate").value(DEFAULT_RATE.doubleValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }


    @Test
    @Transactional
    public void getQuotationDetailsByIdFiltering() throws Exception {
        // Initialize the database
        quotationDetailsRepository.saveAndFlush(quotationDetails);

        Long id = quotationDetails.getId();

        defaultQuotationDetailsShouldBeFound("id.equals=" + id);
        defaultQuotationDetailsShouldNotBeFound("id.notEquals=" + id);

        defaultQuotationDetailsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultQuotationDetailsShouldNotBeFound("id.greaterThan=" + id);

        defaultQuotationDetailsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultQuotationDetailsShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllQuotationDetailsByRateIsEqualToSomething() throws Exception {
        // Initialize the database
        quotationDetailsRepository.saveAndFlush(quotationDetails);

        // Get all the quotationDetailsList where rate equals to DEFAULT_RATE
        defaultQuotationDetailsShouldBeFound("rate.equals=" + DEFAULT_RATE);

        // Get all the quotationDetailsList where rate equals to UPDATED_RATE
        defaultQuotationDetailsShouldNotBeFound("rate.equals=" + UPDATED_RATE);
    }

    @Test
    @Transactional
    public void getAllQuotationDetailsByRateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        quotationDetailsRepository.saveAndFlush(quotationDetails);

        // Get all the quotationDetailsList where rate not equals to DEFAULT_RATE
        defaultQuotationDetailsShouldNotBeFound("rate.notEquals=" + DEFAULT_RATE);

        // Get all the quotationDetailsList where rate not equals to UPDATED_RATE
        defaultQuotationDetailsShouldBeFound("rate.notEquals=" + UPDATED_RATE);
    }

    @Test
    @Transactional
    public void getAllQuotationDetailsByRateIsInShouldWork() throws Exception {
        // Initialize the database
        quotationDetailsRepository.saveAndFlush(quotationDetails);

        // Get all the quotationDetailsList where rate in DEFAULT_RATE or UPDATED_RATE
        defaultQuotationDetailsShouldBeFound("rate.in=" + DEFAULT_RATE + "," + UPDATED_RATE);

        // Get all the quotationDetailsList where rate equals to UPDATED_RATE
        defaultQuotationDetailsShouldNotBeFound("rate.in=" + UPDATED_RATE);
    }

    @Test
    @Transactional
    public void getAllQuotationDetailsByRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        quotationDetailsRepository.saveAndFlush(quotationDetails);

        // Get all the quotationDetailsList where rate is not null
        defaultQuotationDetailsShouldBeFound("rate.specified=true");

        // Get all the quotationDetailsList where rate is null
        defaultQuotationDetailsShouldNotBeFound("rate.specified=false");
    }

    @Test
    @Transactional
    public void getAllQuotationDetailsByRateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        quotationDetailsRepository.saveAndFlush(quotationDetails);

        // Get all the quotationDetailsList where rate is greater than or equal to DEFAULT_RATE
        defaultQuotationDetailsShouldBeFound("rate.greaterThanOrEqual=" + DEFAULT_RATE);

        // Get all the quotationDetailsList where rate is greater than or equal to UPDATED_RATE
        defaultQuotationDetailsShouldNotBeFound("rate.greaterThanOrEqual=" + UPDATED_RATE);
    }

    @Test
    @Transactional
    public void getAllQuotationDetailsByRateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        quotationDetailsRepository.saveAndFlush(quotationDetails);

        // Get all the quotationDetailsList where rate is less than or equal to DEFAULT_RATE
        defaultQuotationDetailsShouldBeFound("rate.lessThanOrEqual=" + DEFAULT_RATE);

        // Get all the quotationDetailsList where rate is less than or equal to SMALLER_RATE
        defaultQuotationDetailsShouldNotBeFound("rate.lessThanOrEqual=" + SMALLER_RATE);
    }

    @Test
    @Transactional
    public void getAllQuotationDetailsByRateIsLessThanSomething() throws Exception {
        // Initialize the database
        quotationDetailsRepository.saveAndFlush(quotationDetails);

        // Get all the quotationDetailsList where rate is less than DEFAULT_RATE
        defaultQuotationDetailsShouldNotBeFound("rate.lessThan=" + DEFAULT_RATE);

        // Get all the quotationDetailsList where rate is less than UPDATED_RATE
        defaultQuotationDetailsShouldBeFound("rate.lessThan=" + UPDATED_RATE);
    }

    @Test
    @Transactional
    public void getAllQuotationDetailsByRateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        quotationDetailsRepository.saveAndFlush(quotationDetails);

        // Get all the quotationDetailsList where rate is greater than DEFAULT_RATE
        defaultQuotationDetailsShouldNotBeFound("rate.greaterThan=" + DEFAULT_RATE);

        // Get all the quotationDetailsList where rate is greater than SMALLER_RATE
        defaultQuotationDetailsShouldBeFound("rate.greaterThan=" + SMALLER_RATE);
    }


    @Test
    @Transactional
    public void getAllQuotationDetailsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        quotationDetailsRepository.saveAndFlush(quotationDetails);

        // Get all the quotationDetailsList where description equals to DEFAULT_DESCRIPTION
        defaultQuotationDetailsShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the quotationDetailsList where description equals to UPDATED_DESCRIPTION
        defaultQuotationDetailsShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllQuotationDetailsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        quotationDetailsRepository.saveAndFlush(quotationDetails);

        // Get all the quotationDetailsList where description not equals to DEFAULT_DESCRIPTION
        defaultQuotationDetailsShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the quotationDetailsList where description not equals to UPDATED_DESCRIPTION
        defaultQuotationDetailsShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllQuotationDetailsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        quotationDetailsRepository.saveAndFlush(quotationDetails);

        // Get all the quotationDetailsList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultQuotationDetailsShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the quotationDetailsList where description equals to UPDATED_DESCRIPTION
        defaultQuotationDetailsShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllQuotationDetailsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        quotationDetailsRepository.saveAndFlush(quotationDetails);

        // Get all the quotationDetailsList where description is not null
        defaultQuotationDetailsShouldBeFound("description.specified=true");

        // Get all the quotationDetailsList where description is null
        defaultQuotationDetailsShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllQuotationDetailsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        quotationDetailsRepository.saveAndFlush(quotationDetails);

        // Get all the quotationDetailsList where description contains DEFAULT_DESCRIPTION
        defaultQuotationDetailsShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the quotationDetailsList where description contains UPDATED_DESCRIPTION
        defaultQuotationDetailsShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllQuotationDetailsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        quotationDetailsRepository.saveAndFlush(quotationDetails);

        // Get all the quotationDetailsList where description does not contain DEFAULT_DESCRIPTION
        defaultQuotationDetailsShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the quotationDetailsList where description does not contain UPDATED_DESCRIPTION
        defaultQuotationDetailsShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllQuotationDetailsByItemIsEqualToSomething() throws Exception {
        // Get already existing entity
        Items item = quotationDetails.getItem();
        quotationDetailsRepository.saveAndFlush(quotationDetails);
        Long itemId = item.getId();

        // Get all the quotationDetailsList where item equals to itemId
        defaultQuotationDetailsShouldBeFound("itemId.equals=" + itemId);

        // Get all the quotationDetailsList where item equals to itemId + 1
        defaultQuotationDetailsShouldNotBeFound("itemId.equals=" + (itemId + 1));
    }


    @Test
    @Transactional
    public void getAllQuotationDetailsByQuoteIsEqualToSomething() throws Exception {
        // Get already existing entity
        Quotation quote = quotationDetails.getQuote();
        quotationDetailsRepository.saveAndFlush(quotationDetails);
        Long quoteId = quote.getId();

        // Get all the quotationDetailsList where quote equals to quoteId
        defaultQuotationDetailsShouldBeFound("quoteId.equals=" + quoteId);

        // Get all the quotationDetailsList where quote equals to quoteId + 1
        defaultQuotationDetailsShouldNotBeFound("quoteId.equals=" + (quoteId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultQuotationDetailsShouldBeFound(String filter) throws Exception {
        restQuotationDetailsMockMvc.perform(get("/api/quotation-details?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quotationDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].rate").value(hasItem(DEFAULT_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restQuotationDetailsMockMvc.perform(get("/api/quotation-details/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultQuotationDetailsShouldNotBeFound(String filter) throws Exception {
        restQuotationDetailsMockMvc.perform(get("/api/quotation-details?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restQuotationDetailsMockMvc.perform(get("/api/quotation-details/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingQuotationDetails() throws Exception {
        // Get the quotationDetails
        restQuotationDetailsMockMvc.perform(get("/api/quotation-details/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateQuotationDetails() throws Exception {
        // Initialize the database
        quotationDetailsService.save(quotationDetails);

        int databaseSizeBeforeUpdate = quotationDetailsRepository.findAll().size();

        // Update the quotationDetails
        QuotationDetails updatedQuotationDetails = quotationDetailsRepository.findById(quotationDetails.getId()).get();
        // Disconnect from session so that the updates on updatedQuotationDetails are not directly saved in db
        em.detach(updatedQuotationDetails);
        updatedQuotationDetails
            .rate(UPDATED_RATE)
            .description(UPDATED_DESCRIPTION);

        restQuotationDetailsMockMvc.perform(put("/api/quotation-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedQuotationDetails)))
            .andExpect(status().isOk());

        // Validate the QuotationDetails in the database
        List<QuotationDetails> quotationDetailsList = quotationDetailsRepository.findAll();
        assertThat(quotationDetailsList).hasSize(databaseSizeBeforeUpdate);
        QuotationDetails testQuotationDetails = quotationDetailsList.get(quotationDetailsList.size() - 1);
        assertThat(testQuotationDetails.getRate()).isEqualTo(UPDATED_RATE);
        assertThat(testQuotationDetails.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingQuotationDetails() throws Exception {
        int databaseSizeBeforeUpdate = quotationDetailsRepository.findAll().size();

        // Create the QuotationDetails

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuotationDetailsMockMvc.perform(put("/api/quotation-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quotationDetails)))
            .andExpect(status().isBadRequest());

        // Validate the QuotationDetails in the database
        List<QuotationDetails> quotationDetailsList = quotationDetailsRepository.findAll();
        assertThat(quotationDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteQuotationDetails() throws Exception {
        // Initialize the database
        quotationDetailsService.save(quotationDetails);

        int databaseSizeBeforeDelete = quotationDetailsRepository.findAll().size();

        // Delete the quotationDetails
        restQuotationDetailsMockMvc.perform(delete("/api/quotation-details/{id}", quotationDetails.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<QuotationDetails> quotationDetailsList = quotationDetailsRepository.findAll();
        assertThat(quotationDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

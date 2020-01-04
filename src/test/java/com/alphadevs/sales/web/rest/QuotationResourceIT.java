package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.WikunumApp;
import com.alphadevs.sales.domain.Quotation;
import com.alphadevs.sales.domain.QuotationDetails;
import com.alphadevs.sales.domain.Location;
import com.alphadevs.sales.repository.QuotationRepository;
import com.alphadevs.sales.service.QuotationService;
import com.alphadevs.sales.web.rest.errors.ExceptionTranslator;
import com.alphadevs.sales.service.dto.QuotationCriteria;
import com.alphadevs.sales.service.QuotationQueryService;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.alphadevs.sales.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link QuotationResource} REST controller.
 */
@SpringBootTest(classes = WikunumApp.class)
public class QuotationResourceIT {

    private static final String DEFAULT_QUOTATION_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_QUOTATION_NUMBER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_QUOTATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_QUOTATION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_QUOTATION_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_QUOTATIONEXPIRE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_QUOTATIONEXPIRE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_QUOTATIONEXPIRE_DATE = LocalDate.ofEpochDay(-1L);

    private static final BigDecimal DEFAULT_QUOTATION_TOTAL_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_QUOTATION_TOTAL_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_QUOTATION_TOTAL_AMOUNT = new BigDecimal(1 - 1);

    private static final String DEFAULT_QUOTATION_TO = "AAAAAAAAAA";
    private static final String UPDATED_QUOTATION_TO = "BBBBBBBBBB";

    private static final String DEFAULT_QUOTATION_FROM = "AAAAAAAAAA";
    private static final String UPDATED_QUOTATION_FROM = "BBBBBBBBBB";

    private static final String DEFAULT_PROJECT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PROJECT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_QUOTATION_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_QUOTATION_NOTE = "BBBBBBBBBB";

    @Autowired
    private QuotationRepository quotationRepository;

    @Autowired
    private QuotationService quotationService;

    @Autowired
    private QuotationQueryService quotationQueryService;

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

    private MockMvc restQuotationMockMvc;

    private Quotation quotation;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final QuotationResource quotationResource = new QuotationResource(quotationService, quotationQueryService);
        this.restQuotationMockMvc = MockMvcBuilders.standaloneSetup(quotationResource)
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
    public static Quotation createEntity(EntityManager em) {
        Quotation quotation = new Quotation()
            .quotationNumber(DEFAULT_QUOTATION_NUMBER)
            .quotationDate(DEFAULT_QUOTATION_DATE)
            .quotationexpireDate(DEFAULT_QUOTATIONEXPIRE_DATE)
            .quotationTotalAmount(DEFAULT_QUOTATION_TOTAL_AMOUNT)
            .quotationTo(DEFAULT_QUOTATION_TO)
            .quotationFrom(DEFAULT_QUOTATION_FROM)
            .projectNumber(DEFAULT_PROJECT_NUMBER)
            .quotationNote(DEFAULT_QUOTATION_NOTE);
        // Add required entity
        QuotationDetails quotationDetails;
        if (TestUtil.findAll(em, QuotationDetails.class).isEmpty()) {
            quotationDetails = QuotationDetailsResourceIT.createEntity(em);
            em.persist(quotationDetails);
            em.flush();
        } else {
            quotationDetails = TestUtil.findAll(em, QuotationDetails.class).get(0);
        }
        quotation.getDetails().add(quotationDetails);
        // Add required entity
        Location location;
        if (TestUtil.findAll(em, Location.class).isEmpty()) {
            location = LocationResourceIT.createEntity(em);
            em.persist(location);
            em.flush();
        } else {
            location = TestUtil.findAll(em, Location.class).get(0);
        }
        quotation.setLocation(location);
        return quotation;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Quotation createUpdatedEntity(EntityManager em) {
        Quotation quotation = new Quotation()
            .quotationNumber(UPDATED_QUOTATION_NUMBER)
            .quotationDate(UPDATED_QUOTATION_DATE)
            .quotationexpireDate(UPDATED_QUOTATIONEXPIRE_DATE)
            .quotationTotalAmount(UPDATED_QUOTATION_TOTAL_AMOUNT)
            .quotationTo(UPDATED_QUOTATION_TO)
            .quotationFrom(UPDATED_QUOTATION_FROM)
            .projectNumber(UPDATED_PROJECT_NUMBER)
            .quotationNote(UPDATED_QUOTATION_NOTE);
        // Add required entity
        QuotationDetails quotationDetails;
        if (TestUtil.findAll(em, QuotationDetails.class).isEmpty()) {
            quotationDetails = QuotationDetailsResourceIT.createUpdatedEntity(em);
            em.persist(quotationDetails);
            em.flush();
        } else {
            quotationDetails = TestUtil.findAll(em, QuotationDetails.class).get(0);
        }
        quotation.getDetails().add(quotationDetails);
        // Add required entity
        Location location;
        if (TestUtil.findAll(em, Location.class).isEmpty()) {
            location = LocationResourceIT.createUpdatedEntity(em);
            em.persist(location);
            em.flush();
        } else {
            location = TestUtil.findAll(em, Location.class).get(0);
        }
        quotation.setLocation(location);
        return quotation;
    }

    @BeforeEach
    public void initTest() {
        quotation = createEntity(em);
    }

    @Test
    @Transactional
    public void createQuotation() throws Exception {
        int databaseSizeBeforeCreate = quotationRepository.findAll().size();

        // Create the Quotation
        restQuotationMockMvc.perform(post("/api/quotations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quotation)))
            .andExpect(status().isCreated());

        // Validate the Quotation in the database
        List<Quotation> quotationList = quotationRepository.findAll();
        assertThat(quotationList).hasSize(databaseSizeBeforeCreate + 1);
        Quotation testQuotation = quotationList.get(quotationList.size() - 1);
        assertThat(testQuotation.getQuotationNumber()).isEqualTo(DEFAULT_QUOTATION_NUMBER);
        assertThat(testQuotation.getQuotationDate()).isEqualTo(DEFAULT_QUOTATION_DATE);
        assertThat(testQuotation.getQuotationexpireDate()).isEqualTo(DEFAULT_QUOTATIONEXPIRE_DATE);
        assertThat(testQuotation.getQuotationTotalAmount()).isEqualTo(DEFAULT_QUOTATION_TOTAL_AMOUNT);
        assertThat(testQuotation.getQuotationTo()).isEqualTo(DEFAULT_QUOTATION_TO);
        assertThat(testQuotation.getQuotationFrom()).isEqualTo(DEFAULT_QUOTATION_FROM);
        assertThat(testQuotation.getProjectNumber()).isEqualTo(DEFAULT_PROJECT_NUMBER);
        assertThat(testQuotation.getQuotationNote()).isEqualTo(DEFAULT_QUOTATION_NOTE);
    }

    @Test
    @Transactional
    public void createQuotationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = quotationRepository.findAll().size();

        // Create the Quotation with an existing ID
        quotation.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuotationMockMvc.perform(post("/api/quotations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quotation)))
            .andExpect(status().isBadRequest());

        // Validate the Quotation in the database
        List<Quotation> quotationList = quotationRepository.findAll();
        assertThat(quotationList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkQuotationNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = quotationRepository.findAll().size();
        // set the field null
        quotation.setQuotationNumber(null);

        // Create the Quotation, which fails.

        restQuotationMockMvc.perform(post("/api/quotations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quotation)))
            .andExpect(status().isBadRequest());

        List<Quotation> quotationList = quotationRepository.findAll();
        assertThat(quotationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQuotationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = quotationRepository.findAll().size();
        // set the field null
        quotation.setQuotationDate(null);

        // Create the Quotation, which fails.

        restQuotationMockMvc.perform(post("/api/quotations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quotation)))
            .andExpect(status().isBadRequest());

        List<Quotation> quotationList = quotationRepository.findAll();
        assertThat(quotationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQuotationexpireDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = quotationRepository.findAll().size();
        // set the field null
        quotation.setQuotationexpireDate(null);

        // Create the Quotation, which fails.

        restQuotationMockMvc.perform(post("/api/quotations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quotation)))
            .andExpect(status().isBadRequest());

        List<Quotation> quotationList = quotationRepository.findAll();
        assertThat(quotationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQuotationToIsRequired() throws Exception {
        int databaseSizeBeforeTest = quotationRepository.findAll().size();
        // set the field null
        quotation.setQuotationTo(null);

        // Create the Quotation, which fails.

        restQuotationMockMvc.perform(post("/api/quotations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quotation)))
            .andExpect(status().isBadRequest());

        List<Quotation> quotationList = quotationRepository.findAll();
        assertThat(quotationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQuotationFromIsRequired() throws Exception {
        int databaseSizeBeforeTest = quotationRepository.findAll().size();
        // set the field null
        quotation.setQuotationFrom(null);

        // Create the Quotation, which fails.

        restQuotationMockMvc.perform(post("/api/quotations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quotation)))
            .andExpect(status().isBadRequest());

        List<Quotation> quotationList = quotationRepository.findAll();
        assertThat(quotationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProjectNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = quotationRepository.findAll().size();
        // set the field null
        quotation.setProjectNumber(null);

        // Create the Quotation, which fails.

        restQuotationMockMvc.perform(post("/api/quotations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quotation)))
            .andExpect(status().isBadRequest());

        List<Quotation> quotationList = quotationRepository.findAll();
        assertThat(quotationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllQuotations() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList
        restQuotationMockMvc.perform(get("/api/quotations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quotation.getId().intValue())))
            .andExpect(jsonPath("$.[*].quotationNumber").value(hasItem(DEFAULT_QUOTATION_NUMBER)))
            .andExpect(jsonPath("$.[*].quotationDate").value(hasItem(DEFAULT_QUOTATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].quotationexpireDate").value(hasItem(DEFAULT_QUOTATIONEXPIRE_DATE.toString())))
            .andExpect(jsonPath("$.[*].quotationTotalAmount").value(hasItem(DEFAULT_QUOTATION_TOTAL_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].quotationTo").value(hasItem(DEFAULT_QUOTATION_TO)))
            .andExpect(jsonPath("$.[*].quotationFrom").value(hasItem(DEFAULT_QUOTATION_FROM)))
            .andExpect(jsonPath("$.[*].projectNumber").value(hasItem(DEFAULT_PROJECT_NUMBER)))
            .andExpect(jsonPath("$.[*].quotationNote").value(hasItem(DEFAULT_QUOTATION_NOTE)));
    }
    
    @Test
    @Transactional
    public void getQuotation() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get the quotation
        restQuotationMockMvc.perform(get("/api/quotations/{id}", quotation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(quotation.getId().intValue()))
            .andExpect(jsonPath("$.quotationNumber").value(DEFAULT_QUOTATION_NUMBER))
            .andExpect(jsonPath("$.quotationDate").value(DEFAULT_QUOTATION_DATE.toString()))
            .andExpect(jsonPath("$.quotationexpireDate").value(DEFAULT_QUOTATIONEXPIRE_DATE.toString()))
            .andExpect(jsonPath("$.quotationTotalAmount").value(DEFAULT_QUOTATION_TOTAL_AMOUNT.intValue()))
            .andExpect(jsonPath("$.quotationTo").value(DEFAULT_QUOTATION_TO))
            .andExpect(jsonPath("$.quotationFrom").value(DEFAULT_QUOTATION_FROM))
            .andExpect(jsonPath("$.projectNumber").value(DEFAULT_PROJECT_NUMBER))
            .andExpect(jsonPath("$.quotationNote").value(DEFAULT_QUOTATION_NOTE));
    }


    @Test
    @Transactional
    public void getQuotationsByIdFiltering() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        Long id = quotation.getId();

        defaultQuotationShouldBeFound("id.equals=" + id);
        defaultQuotationShouldNotBeFound("id.notEquals=" + id);

        defaultQuotationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultQuotationShouldNotBeFound("id.greaterThan=" + id);

        defaultQuotationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultQuotationShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllQuotationsByQuotationNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where quotationNumber equals to DEFAULT_QUOTATION_NUMBER
        defaultQuotationShouldBeFound("quotationNumber.equals=" + DEFAULT_QUOTATION_NUMBER);

        // Get all the quotationList where quotationNumber equals to UPDATED_QUOTATION_NUMBER
        defaultQuotationShouldNotBeFound("quotationNumber.equals=" + UPDATED_QUOTATION_NUMBER);
    }

    @Test
    @Transactional
    public void getAllQuotationsByQuotationNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where quotationNumber not equals to DEFAULT_QUOTATION_NUMBER
        defaultQuotationShouldNotBeFound("quotationNumber.notEquals=" + DEFAULT_QUOTATION_NUMBER);

        // Get all the quotationList where quotationNumber not equals to UPDATED_QUOTATION_NUMBER
        defaultQuotationShouldBeFound("quotationNumber.notEquals=" + UPDATED_QUOTATION_NUMBER);
    }

    @Test
    @Transactional
    public void getAllQuotationsByQuotationNumberIsInShouldWork() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where quotationNumber in DEFAULT_QUOTATION_NUMBER or UPDATED_QUOTATION_NUMBER
        defaultQuotationShouldBeFound("quotationNumber.in=" + DEFAULT_QUOTATION_NUMBER + "," + UPDATED_QUOTATION_NUMBER);

        // Get all the quotationList where quotationNumber equals to UPDATED_QUOTATION_NUMBER
        defaultQuotationShouldNotBeFound("quotationNumber.in=" + UPDATED_QUOTATION_NUMBER);
    }

    @Test
    @Transactional
    public void getAllQuotationsByQuotationNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where quotationNumber is not null
        defaultQuotationShouldBeFound("quotationNumber.specified=true");

        // Get all the quotationList where quotationNumber is null
        defaultQuotationShouldNotBeFound("quotationNumber.specified=false");
    }
                @Test
    @Transactional
    public void getAllQuotationsByQuotationNumberContainsSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where quotationNumber contains DEFAULT_QUOTATION_NUMBER
        defaultQuotationShouldBeFound("quotationNumber.contains=" + DEFAULT_QUOTATION_NUMBER);

        // Get all the quotationList where quotationNumber contains UPDATED_QUOTATION_NUMBER
        defaultQuotationShouldNotBeFound("quotationNumber.contains=" + UPDATED_QUOTATION_NUMBER);
    }

    @Test
    @Transactional
    public void getAllQuotationsByQuotationNumberNotContainsSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where quotationNumber does not contain DEFAULT_QUOTATION_NUMBER
        defaultQuotationShouldNotBeFound("quotationNumber.doesNotContain=" + DEFAULT_QUOTATION_NUMBER);

        // Get all the quotationList where quotationNumber does not contain UPDATED_QUOTATION_NUMBER
        defaultQuotationShouldBeFound("quotationNumber.doesNotContain=" + UPDATED_QUOTATION_NUMBER);
    }


    @Test
    @Transactional
    public void getAllQuotationsByQuotationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where quotationDate equals to DEFAULT_QUOTATION_DATE
        defaultQuotationShouldBeFound("quotationDate.equals=" + DEFAULT_QUOTATION_DATE);

        // Get all the quotationList where quotationDate equals to UPDATED_QUOTATION_DATE
        defaultQuotationShouldNotBeFound("quotationDate.equals=" + UPDATED_QUOTATION_DATE);
    }

    @Test
    @Transactional
    public void getAllQuotationsByQuotationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where quotationDate not equals to DEFAULT_QUOTATION_DATE
        defaultQuotationShouldNotBeFound("quotationDate.notEquals=" + DEFAULT_QUOTATION_DATE);

        // Get all the quotationList where quotationDate not equals to UPDATED_QUOTATION_DATE
        defaultQuotationShouldBeFound("quotationDate.notEquals=" + UPDATED_QUOTATION_DATE);
    }

    @Test
    @Transactional
    public void getAllQuotationsByQuotationDateIsInShouldWork() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where quotationDate in DEFAULT_QUOTATION_DATE or UPDATED_QUOTATION_DATE
        defaultQuotationShouldBeFound("quotationDate.in=" + DEFAULT_QUOTATION_DATE + "," + UPDATED_QUOTATION_DATE);

        // Get all the quotationList where quotationDate equals to UPDATED_QUOTATION_DATE
        defaultQuotationShouldNotBeFound("quotationDate.in=" + UPDATED_QUOTATION_DATE);
    }

    @Test
    @Transactional
    public void getAllQuotationsByQuotationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where quotationDate is not null
        defaultQuotationShouldBeFound("quotationDate.specified=true");

        // Get all the quotationList where quotationDate is null
        defaultQuotationShouldNotBeFound("quotationDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllQuotationsByQuotationDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where quotationDate is greater than or equal to DEFAULT_QUOTATION_DATE
        defaultQuotationShouldBeFound("quotationDate.greaterThanOrEqual=" + DEFAULT_QUOTATION_DATE);

        // Get all the quotationList where quotationDate is greater than or equal to UPDATED_QUOTATION_DATE
        defaultQuotationShouldNotBeFound("quotationDate.greaterThanOrEqual=" + UPDATED_QUOTATION_DATE);
    }

    @Test
    @Transactional
    public void getAllQuotationsByQuotationDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where quotationDate is less than or equal to DEFAULT_QUOTATION_DATE
        defaultQuotationShouldBeFound("quotationDate.lessThanOrEqual=" + DEFAULT_QUOTATION_DATE);

        // Get all the quotationList where quotationDate is less than or equal to SMALLER_QUOTATION_DATE
        defaultQuotationShouldNotBeFound("quotationDate.lessThanOrEqual=" + SMALLER_QUOTATION_DATE);
    }

    @Test
    @Transactional
    public void getAllQuotationsByQuotationDateIsLessThanSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where quotationDate is less than DEFAULT_QUOTATION_DATE
        defaultQuotationShouldNotBeFound("quotationDate.lessThan=" + DEFAULT_QUOTATION_DATE);

        // Get all the quotationList where quotationDate is less than UPDATED_QUOTATION_DATE
        defaultQuotationShouldBeFound("quotationDate.lessThan=" + UPDATED_QUOTATION_DATE);
    }

    @Test
    @Transactional
    public void getAllQuotationsByQuotationDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where quotationDate is greater than DEFAULT_QUOTATION_DATE
        defaultQuotationShouldNotBeFound("quotationDate.greaterThan=" + DEFAULT_QUOTATION_DATE);

        // Get all the quotationList where quotationDate is greater than SMALLER_QUOTATION_DATE
        defaultQuotationShouldBeFound("quotationDate.greaterThan=" + SMALLER_QUOTATION_DATE);
    }


    @Test
    @Transactional
    public void getAllQuotationsByQuotationexpireDateIsEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where quotationexpireDate equals to DEFAULT_QUOTATIONEXPIRE_DATE
        defaultQuotationShouldBeFound("quotationexpireDate.equals=" + DEFAULT_QUOTATIONEXPIRE_DATE);

        // Get all the quotationList where quotationexpireDate equals to UPDATED_QUOTATIONEXPIRE_DATE
        defaultQuotationShouldNotBeFound("quotationexpireDate.equals=" + UPDATED_QUOTATIONEXPIRE_DATE);
    }

    @Test
    @Transactional
    public void getAllQuotationsByQuotationexpireDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where quotationexpireDate not equals to DEFAULT_QUOTATIONEXPIRE_DATE
        defaultQuotationShouldNotBeFound("quotationexpireDate.notEquals=" + DEFAULT_QUOTATIONEXPIRE_DATE);

        // Get all the quotationList where quotationexpireDate not equals to UPDATED_QUOTATIONEXPIRE_DATE
        defaultQuotationShouldBeFound("quotationexpireDate.notEquals=" + UPDATED_QUOTATIONEXPIRE_DATE);
    }

    @Test
    @Transactional
    public void getAllQuotationsByQuotationexpireDateIsInShouldWork() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where quotationexpireDate in DEFAULT_QUOTATIONEXPIRE_DATE or UPDATED_QUOTATIONEXPIRE_DATE
        defaultQuotationShouldBeFound("quotationexpireDate.in=" + DEFAULT_QUOTATIONEXPIRE_DATE + "," + UPDATED_QUOTATIONEXPIRE_DATE);

        // Get all the quotationList where quotationexpireDate equals to UPDATED_QUOTATIONEXPIRE_DATE
        defaultQuotationShouldNotBeFound("quotationexpireDate.in=" + UPDATED_QUOTATIONEXPIRE_DATE);
    }

    @Test
    @Transactional
    public void getAllQuotationsByQuotationexpireDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where quotationexpireDate is not null
        defaultQuotationShouldBeFound("quotationexpireDate.specified=true");

        // Get all the quotationList where quotationexpireDate is null
        defaultQuotationShouldNotBeFound("quotationexpireDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllQuotationsByQuotationexpireDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where quotationexpireDate is greater than or equal to DEFAULT_QUOTATIONEXPIRE_DATE
        defaultQuotationShouldBeFound("quotationexpireDate.greaterThanOrEqual=" + DEFAULT_QUOTATIONEXPIRE_DATE);

        // Get all the quotationList where quotationexpireDate is greater than or equal to UPDATED_QUOTATIONEXPIRE_DATE
        defaultQuotationShouldNotBeFound("quotationexpireDate.greaterThanOrEqual=" + UPDATED_QUOTATIONEXPIRE_DATE);
    }

    @Test
    @Transactional
    public void getAllQuotationsByQuotationexpireDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where quotationexpireDate is less than or equal to DEFAULT_QUOTATIONEXPIRE_DATE
        defaultQuotationShouldBeFound("quotationexpireDate.lessThanOrEqual=" + DEFAULT_QUOTATIONEXPIRE_DATE);

        // Get all the quotationList where quotationexpireDate is less than or equal to SMALLER_QUOTATIONEXPIRE_DATE
        defaultQuotationShouldNotBeFound("quotationexpireDate.lessThanOrEqual=" + SMALLER_QUOTATIONEXPIRE_DATE);
    }

    @Test
    @Transactional
    public void getAllQuotationsByQuotationexpireDateIsLessThanSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where quotationexpireDate is less than DEFAULT_QUOTATIONEXPIRE_DATE
        defaultQuotationShouldNotBeFound("quotationexpireDate.lessThan=" + DEFAULT_QUOTATIONEXPIRE_DATE);

        // Get all the quotationList where quotationexpireDate is less than UPDATED_QUOTATIONEXPIRE_DATE
        defaultQuotationShouldBeFound("quotationexpireDate.lessThan=" + UPDATED_QUOTATIONEXPIRE_DATE);
    }

    @Test
    @Transactional
    public void getAllQuotationsByQuotationexpireDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where quotationexpireDate is greater than DEFAULT_QUOTATIONEXPIRE_DATE
        defaultQuotationShouldNotBeFound("quotationexpireDate.greaterThan=" + DEFAULT_QUOTATIONEXPIRE_DATE);

        // Get all the quotationList where quotationexpireDate is greater than SMALLER_QUOTATIONEXPIRE_DATE
        defaultQuotationShouldBeFound("quotationexpireDate.greaterThan=" + SMALLER_QUOTATIONEXPIRE_DATE);
    }


    @Test
    @Transactional
    public void getAllQuotationsByQuotationTotalAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where quotationTotalAmount equals to DEFAULT_QUOTATION_TOTAL_AMOUNT
        defaultQuotationShouldBeFound("quotationTotalAmount.equals=" + DEFAULT_QUOTATION_TOTAL_AMOUNT);

        // Get all the quotationList where quotationTotalAmount equals to UPDATED_QUOTATION_TOTAL_AMOUNT
        defaultQuotationShouldNotBeFound("quotationTotalAmount.equals=" + UPDATED_QUOTATION_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllQuotationsByQuotationTotalAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where quotationTotalAmount not equals to DEFAULT_QUOTATION_TOTAL_AMOUNT
        defaultQuotationShouldNotBeFound("quotationTotalAmount.notEquals=" + DEFAULT_QUOTATION_TOTAL_AMOUNT);

        // Get all the quotationList where quotationTotalAmount not equals to UPDATED_QUOTATION_TOTAL_AMOUNT
        defaultQuotationShouldBeFound("quotationTotalAmount.notEquals=" + UPDATED_QUOTATION_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllQuotationsByQuotationTotalAmountIsInShouldWork() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where quotationTotalAmount in DEFAULT_QUOTATION_TOTAL_AMOUNT or UPDATED_QUOTATION_TOTAL_AMOUNT
        defaultQuotationShouldBeFound("quotationTotalAmount.in=" + DEFAULT_QUOTATION_TOTAL_AMOUNT + "," + UPDATED_QUOTATION_TOTAL_AMOUNT);

        // Get all the quotationList where quotationTotalAmount equals to UPDATED_QUOTATION_TOTAL_AMOUNT
        defaultQuotationShouldNotBeFound("quotationTotalAmount.in=" + UPDATED_QUOTATION_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllQuotationsByQuotationTotalAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where quotationTotalAmount is not null
        defaultQuotationShouldBeFound("quotationTotalAmount.specified=true");

        // Get all the quotationList where quotationTotalAmount is null
        defaultQuotationShouldNotBeFound("quotationTotalAmount.specified=false");
    }

    @Test
    @Transactional
    public void getAllQuotationsByQuotationTotalAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where quotationTotalAmount is greater than or equal to DEFAULT_QUOTATION_TOTAL_AMOUNT
        defaultQuotationShouldBeFound("quotationTotalAmount.greaterThanOrEqual=" + DEFAULT_QUOTATION_TOTAL_AMOUNT);

        // Get all the quotationList where quotationTotalAmount is greater than or equal to UPDATED_QUOTATION_TOTAL_AMOUNT
        defaultQuotationShouldNotBeFound("quotationTotalAmount.greaterThanOrEqual=" + UPDATED_QUOTATION_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllQuotationsByQuotationTotalAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where quotationTotalAmount is less than or equal to DEFAULT_QUOTATION_TOTAL_AMOUNT
        defaultQuotationShouldBeFound("quotationTotalAmount.lessThanOrEqual=" + DEFAULT_QUOTATION_TOTAL_AMOUNT);

        // Get all the quotationList where quotationTotalAmount is less than or equal to SMALLER_QUOTATION_TOTAL_AMOUNT
        defaultQuotationShouldNotBeFound("quotationTotalAmount.lessThanOrEqual=" + SMALLER_QUOTATION_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllQuotationsByQuotationTotalAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where quotationTotalAmount is less than DEFAULT_QUOTATION_TOTAL_AMOUNT
        defaultQuotationShouldNotBeFound("quotationTotalAmount.lessThan=" + DEFAULT_QUOTATION_TOTAL_AMOUNT);

        // Get all the quotationList where quotationTotalAmount is less than UPDATED_QUOTATION_TOTAL_AMOUNT
        defaultQuotationShouldBeFound("quotationTotalAmount.lessThan=" + UPDATED_QUOTATION_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllQuotationsByQuotationTotalAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where quotationTotalAmount is greater than DEFAULT_QUOTATION_TOTAL_AMOUNT
        defaultQuotationShouldNotBeFound("quotationTotalAmount.greaterThan=" + DEFAULT_QUOTATION_TOTAL_AMOUNT);

        // Get all the quotationList where quotationTotalAmount is greater than SMALLER_QUOTATION_TOTAL_AMOUNT
        defaultQuotationShouldBeFound("quotationTotalAmount.greaterThan=" + SMALLER_QUOTATION_TOTAL_AMOUNT);
    }


    @Test
    @Transactional
    public void getAllQuotationsByQuotationToIsEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where quotationTo equals to DEFAULT_QUOTATION_TO
        defaultQuotationShouldBeFound("quotationTo.equals=" + DEFAULT_QUOTATION_TO);

        // Get all the quotationList where quotationTo equals to UPDATED_QUOTATION_TO
        defaultQuotationShouldNotBeFound("quotationTo.equals=" + UPDATED_QUOTATION_TO);
    }

    @Test
    @Transactional
    public void getAllQuotationsByQuotationToIsNotEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where quotationTo not equals to DEFAULT_QUOTATION_TO
        defaultQuotationShouldNotBeFound("quotationTo.notEquals=" + DEFAULT_QUOTATION_TO);

        // Get all the quotationList where quotationTo not equals to UPDATED_QUOTATION_TO
        defaultQuotationShouldBeFound("quotationTo.notEquals=" + UPDATED_QUOTATION_TO);
    }

    @Test
    @Transactional
    public void getAllQuotationsByQuotationToIsInShouldWork() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where quotationTo in DEFAULT_QUOTATION_TO or UPDATED_QUOTATION_TO
        defaultQuotationShouldBeFound("quotationTo.in=" + DEFAULT_QUOTATION_TO + "," + UPDATED_QUOTATION_TO);

        // Get all the quotationList where quotationTo equals to UPDATED_QUOTATION_TO
        defaultQuotationShouldNotBeFound("quotationTo.in=" + UPDATED_QUOTATION_TO);
    }

    @Test
    @Transactional
    public void getAllQuotationsByQuotationToIsNullOrNotNull() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where quotationTo is not null
        defaultQuotationShouldBeFound("quotationTo.specified=true");

        // Get all the quotationList where quotationTo is null
        defaultQuotationShouldNotBeFound("quotationTo.specified=false");
    }
                @Test
    @Transactional
    public void getAllQuotationsByQuotationToContainsSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where quotationTo contains DEFAULT_QUOTATION_TO
        defaultQuotationShouldBeFound("quotationTo.contains=" + DEFAULT_QUOTATION_TO);

        // Get all the quotationList where quotationTo contains UPDATED_QUOTATION_TO
        defaultQuotationShouldNotBeFound("quotationTo.contains=" + UPDATED_QUOTATION_TO);
    }

    @Test
    @Transactional
    public void getAllQuotationsByQuotationToNotContainsSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where quotationTo does not contain DEFAULT_QUOTATION_TO
        defaultQuotationShouldNotBeFound("quotationTo.doesNotContain=" + DEFAULT_QUOTATION_TO);

        // Get all the quotationList where quotationTo does not contain UPDATED_QUOTATION_TO
        defaultQuotationShouldBeFound("quotationTo.doesNotContain=" + UPDATED_QUOTATION_TO);
    }


    @Test
    @Transactional
    public void getAllQuotationsByQuotationFromIsEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where quotationFrom equals to DEFAULT_QUOTATION_FROM
        defaultQuotationShouldBeFound("quotationFrom.equals=" + DEFAULT_QUOTATION_FROM);

        // Get all the quotationList where quotationFrom equals to UPDATED_QUOTATION_FROM
        defaultQuotationShouldNotBeFound("quotationFrom.equals=" + UPDATED_QUOTATION_FROM);
    }

    @Test
    @Transactional
    public void getAllQuotationsByQuotationFromIsNotEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where quotationFrom not equals to DEFAULT_QUOTATION_FROM
        defaultQuotationShouldNotBeFound("quotationFrom.notEquals=" + DEFAULT_QUOTATION_FROM);

        // Get all the quotationList where quotationFrom not equals to UPDATED_QUOTATION_FROM
        defaultQuotationShouldBeFound("quotationFrom.notEquals=" + UPDATED_QUOTATION_FROM);
    }

    @Test
    @Transactional
    public void getAllQuotationsByQuotationFromIsInShouldWork() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where quotationFrom in DEFAULT_QUOTATION_FROM or UPDATED_QUOTATION_FROM
        defaultQuotationShouldBeFound("quotationFrom.in=" + DEFAULT_QUOTATION_FROM + "," + UPDATED_QUOTATION_FROM);

        // Get all the quotationList where quotationFrom equals to UPDATED_QUOTATION_FROM
        defaultQuotationShouldNotBeFound("quotationFrom.in=" + UPDATED_QUOTATION_FROM);
    }

    @Test
    @Transactional
    public void getAllQuotationsByQuotationFromIsNullOrNotNull() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where quotationFrom is not null
        defaultQuotationShouldBeFound("quotationFrom.specified=true");

        // Get all the quotationList where quotationFrom is null
        defaultQuotationShouldNotBeFound("quotationFrom.specified=false");
    }
                @Test
    @Transactional
    public void getAllQuotationsByQuotationFromContainsSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where quotationFrom contains DEFAULT_QUOTATION_FROM
        defaultQuotationShouldBeFound("quotationFrom.contains=" + DEFAULT_QUOTATION_FROM);

        // Get all the quotationList where quotationFrom contains UPDATED_QUOTATION_FROM
        defaultQuotationShouldNotBeFound("quotationFrom.contains=" + UPDATED_QUOTATION_FROM);
    }

    @Test
    @Transactional
    public void getAllQuotationsByQuotationFromNotContainsSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where quotationFrom does not contain DEFAULT_QUOTATION_FROM
        defaultQuotationShouldNotBeFound("quotationFrom.doesNotContain=" + DEFAULT_QUOTATION_FROM);

        // Get all the quotationList where quotationFrom does not contain UPDATED_QUOTATION_FROM
        defaultQuotationShouldBeFound("quotationFrom.doesNotContain=" + UPDATED_QUOTATION_FROM);
    }


    @Test
    @Transactional
    public void getAllQuotationsByProjectNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where projectNumber equals to DEFAULT_PROJECT_NUMBER
        defaultQuotationShouldBeFound("projectNumber.equals=" + DEFAULT_PROJECT_NUMBER);

        // Get all the quotationList where projectNumber equals to UPDATED_PROJECT_NUMBER
        defaultQuotationShouldNotBeFound("projectNumber.equals=" + UPDATED_PROJECT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllQuotationsByProjectNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where projectNumber not equals to DEFAULT_PROJECT_NUMBER
        defaultQuotationShouldNotBeFound("projectNumber.notEquals=" + DEFAULT_PROJECT_NUMBER);

        // Get all the quotationList where projectNumber not equals to UPDATED_PROJECT_NUMBER
        defaultQuotationShouldBeFound("projectNumber.notEquals=" + UPDATED_PROJECT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllQuotationsByProjectNumberIsInShouldWork() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where projectNumber in DEFAULT_PROJECT_NUMBER or UPDATED_PROJECT_NUMBER
        defaultQuotationShouldBeFound("projectNumber.in=" + DEFAULT_PROJECT_NUMBER + "," + UPDATED_PROJECT_NUMBER);

        // Get all the quotationList where projectNumber equals to UPDATED_PROJECT_NUMBER
        defaultQuotationShouldNotBeFound("projectNumber.in=" + UPDATED_PROJECT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllQuotationsByProjectNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where projectNumber is not null
        defaultQuotationShouldBeFound("projectNumber.specified=true");

        // Get all the quotationList where projectNumber is null
        defaultQuotationShouldNotBeFound("projectNumber.specified=false");
    }
                @Test
    @Transactional
    public void getAllQuotationsByProjectNumberContainsSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where projectNumber contains DEFAULT_PROJECT_NUMBER
        defaultQuotationShouldBeFound("projectNumber.contains=" + DEFAULT_PROJECT_NUMBER);

        // Get all the quotationList where projectNumber contains UPDATED_PROJECT_NUMBER
        defaultQuotationShouldNotBeFound("projectNumber.contains=" + UPDATED_PROJECT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllQuotationsByProjectNumberNotContainsSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where projectNumber does not contain DEFAULT_PROJECT_NUMBER
        defaultQuotationShouldNotBeFound("projectNumber.doesNotContain=" + DEFAULT_PROJECT_NUMBER);

        // Get all the quotationList where projectNumber does not contain UPDATED_PROJECT_NUMBER
        defaultQuotationShouldBeFound("projectNumber.doesNotContain=" + UPDATED_PROJECT_NUMBER);
    }


    @Test
    @Transactional
    public void getAllQuotationsByQuotationNoteIsEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where quotationNote equals to DEFAULT_QUOTATION_NOTE
        defaultQuotationShouldBeFound("quotationNote.equals=" + DEFAULT_QUOTATION_NOTE);

        // Get all the quotationList where quotationNote equals to UPDATED_QUOTATION_NOTE
        defaultQuotationShouldNotBeFound("quotationNote.equals=" + UPDATED_QUOTATION_NOTE);
    }

    @Test
    @Transactional
    public void getAllQuotationsByQuotationNoteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where quotationNote not equals to DEFAULT_QUOTATION_NOTE
        defaultQuotationShouldNotBeFound("quotationNote.notEquals=" + DEFAULT_QUOTATION_NOTE);

        // Get all the quotationList where quotationNote not equals to UPDATED_QUOTATION_NOTE
        defaultQuotationShouldBeFound("quotationNote.notEquals=" + UPDATED_QUOTATION_NOTE);
    }

    @Test
    @Transactional
    public void getAllQuotationsByQuotationNoteIsInShouldWork() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where quotationNote in DEFAULT_QUOTATION_NOTE or UPDATED_QUOTATION_NOTE
        defaultQuotationShouldBeFound("quotationNote.in=" + DEFAULT_QUOTATION_NOTE + "," + UPDATED_QUOTATION_NOTE);

        // Get all the quotationList where quotationNote equals to UPDATED_QUOTATION_NOTE
        defaultQuotationShouldNotBeFound("quotationNote.in=" + UPDATED_QUOTATION_NOTE);
    }

    @Test
    @Transactional
    public void getAllQuotationsByQuotationNoteIsNullOrNotNull() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where quotationNote is not null
        defaultQuotationShouldBeFound("quotationNote.specified=true");

        // Get all the quotationList where quotationNote is null
        defaultQuotationShouldNotBeFound("quotationNote.specified=false");
    }
                @Test
    @Transactional
    public void getAllQuotationsByQuotationNoteContainsSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where quotationNote contains DEFAULT_QUOTATION_NOTE
        defaultQuotationShouldBeFound("quotationNote.contains=" + DEFAULT_QUOTATION_NOTE);

        // Get all the quotationList where quotationNote contains UPDATED_QUOTATION_NOTE
        defaultQuotationShouldNotBeFound("quotationNote.contains=" + UPDATED_QUOTATION_NOTE);
    }

    @Test
    @Transactional
    public void getAllQuotationsByQuotationNoteNotContainsSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where quotationNote does not contain DEFAULT_QUOTATION_NOTE
        defaultQuotationShouldNotBeFound("quotationNote.doesNotContain=" + DEFAULT_QUOTATION_NOTE);

        // Get all the quotationList where quotationNote does not contain UPDATED_QUOTATION_NOTE
        defaultQuotationShouldBeFound("quotationNote.doesNotContain=" + UPDATED_QUOTATION_NOTE);
    }


    @Test
    @Transactional
    public void getAllQuotationsByDetailsIsEqualToSomething() throws Exception {
        // Get already existing entity
        QuotationDetails details = quotation.getDetails();
        quotationRepository.saveAndFlush(quotation);
        Long detailsId = details.getId();

        // Get all the quotationList where details equals to detailsId
        defaultQuotationShouldBeFound("detailsId.equals=" + detailsId);

        // Get all the quotationList where details equals to detailsId + 1
        defaultQuotationShouldNotBeFound("detailsId.equals=" + (detailsId + 1));
    }


    @Test
    @Transactional
    public void getAllQuotationsByLocationIsEqualToSomething() throws Exception {
        // Get already existing entity
        Location location = quotation.getLocation();
        quotationRepository.saveAndFlush(quotation);
        Long locationId = location.getId();

        // Get all the quotationList where location equals to locationId
        defaultQuotationShouldBeFound("locationId.equals=" + locationId);

        // Get all the quotationList where location equals to locationId + 1
        defaultQuotationShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultQuotationShouldBeFound(String filter) throws Exception {
        restQuotationMockMvc.perform(get("/api/quotations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quotation.getId().intValue())))
            .andExpect(jsonPath("$.[*].quotationNumber").value(hasItem(DEFAULT_QUOTATION_NUMBER)))
            .andExpect(jsonPath("$.[*].quotationDate").value(hasItem(DEFAULT_QUOTATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].quotationexpireDate").value(hasItem(DEFAULT_QUOTATIONEXPIRE_DATE.toString())))
            .andExpect(jsonPath("$.[*].quotationTotalAmount").value(hasItem(DEFAULT_QUOTATION_TOTAL_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].quotationTo").value(hasItem(DEFAULT_QUOTATION_TO)))
            .andExpect(jsonPath("$.[*].quotationFrom").value(hasItem(DEFAULT_QUOTATION_FROM)))
            .andExpect(jsonPath("$.[*].projectNumber").value(hasItem(DEFAULT_PROJECT_NUMBER)))
            .andExpect(jsonPath("$.[*].quotationNote").value(hasItem(DEFAULT_QUOTATION_NOTE)));

        // Check, that the count call also returns 1
        restQuotationMockMvc.perform(get("/api/quotations/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultQuotationShouldNotBeFound(String filter) throws Exception {
        restQuotationMockMvc.perform(get("/api/quotations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restQuotationMockMvc.perform(get("/api/quotations/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingQuotation() throws Exception {
        // Get the quotation
        restQuotationMockMvc.perform(get("/api/quotations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateQuotation() throws Exception {
        // Initialize the database
        quotationService.save(quotation);

        int databaseSizeBeforeUpdate = quotationRepository.findAll().size();

        // Update the quotation
        Quotation updatedQuotation = quotationRepository.findById(quotation.getId()).get();
        // Disconnect from session so that the updates on updatedQuotation are not directly saved in db
        em.detach(updatedQuotation);
        updatedQuotation
            .quotationNumber(UPDATED_QUOTATION_NUMBER)
            .quotationDate(UPDATED_QUOTATION_DATE)
            .quotationexpireDate(UPDATED_QUOTATIONEXPIRE_DATE)
            .quotationTotalAmount(UPDATED_QUOTATION_TOTAL_AMOUNT)
            .quotationTo(UPDATED_QUOTATION_TO)
            .quotationFrom(UPDATED_QUOTATION_FROM)
            .projectNumber(UPDATED_PROJECT_NUMBER)
            .quotationNote(UPDATED_QUOTATION_NOTE);

        restQuotationMockMvc.perform(put("/api/quotations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedQuotation)))
            .andExpect(status().isOk());

        // Validate the Quotation in the database
        List<Quotation> quotationList = quotationRepository.findAll();
        assertThat(quotationList).hasSize(databaseSizeBeforeUpdate);
        Quotation testQuotation = quotationList.get(quotationList.size() - 1);
        assertThat(testQuotation.getQuotationNumber()).isEqualTo(UPDATED_QUOTATION_NUMBER);
        assertThat(testQuotation.getQuotationDate()).isEqualTo(UPDATED_QUOTATION_DATE);
        assertThat(testQuotation.getQuotationexpireDate()).isEqualTo(UPDATED_QUOTATIONEXPIRE_DATE);
        assertThat(testQuotation.getQuotationTotalAmount()).isEqualTo(UPDATED_QUOTATION_TOTAL_AMOUNT);
        assertThat(testQuotation.getQuotationTo()).isEqualTo(UPDATED_QUOTATION_TO);
        assertThat(testQuotation.getQuotationFrom()).isEqualTo(UPDATED_QUOTATION_FROM);
        assertThat(testQuotation.getProjectNumber()).isEqualTo(UPDATED_PROJECT_NUMBER);
        assertThat(testQuotation.getQuotationNote()).isEqualTo(UPDATED_QUOTATION_NOTE);
    }

    @Test
    @Transactional
    public void updateNonExistingQuotation() throws Exception {
        int databaseSizeBeforeUpdate = quotationRepository.findAll().size();

        // Create the Quotation

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuotationMockMvc.perform(put("/api/quotations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quotation)))
            .andExpect(status().isBadRequest());

        // Validate the Quotation in the database
        List<Quotation> quotationList = quotationRepository.findAll();
        assertThat(quotationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteQuotation() throws Exception {
        // Initialize the database
        quotationService.save(quotation);

        int databaseSizeBeforeDelete = quotationRepository.findAll().size();

        // Delete the quotation
        restQuotationMockMvc.perform(delete("/api/quotations/{id}", quotation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Quotation> quotationList = quotationRepository.findAll();
        assertThat(quotationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

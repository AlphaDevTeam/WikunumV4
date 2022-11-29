package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.WikunumApp;
import com.alphadevs.sales.domain.CashPaymentVoucherSupplier;
import com.alphadevs.sales.domain.Location;
import com.alphadevs.sales.domain.TransactionType;
import com.alphadevs.sales.domain.Supplier;
import com.alphadevs.sales.repository.CashPaymentVoucherSupplierRepository;
import com.alphadevs.sales.service.CashPaymentVoucherSupplierService;
import com.alphadevs.sales.web.rest.errors.ExceptionTranslator;
import com.alphadevs.sales.service.dto.CashPaymentVoucherSupplierCriteria;
import com.alphadevs.sales.service.CashPaymentVoucherSupplierQueryService;

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
 * Integration tests for the {@link CashPaymentVoucherSupplierResource} REST controller.
 */
@SpringBootTest(classes = WikunumApp.class)
public class CashPaymentVoucherSupplierResourceIT {

    private static final String DEFAULT_TRANSACTION_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_TRANSACTION_NUMBER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_TRANSACTION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TRANSACTION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_TRANSACTION_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_TRANSACTION_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_TRANSACTION_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_TRANSACTION_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_TRANSACTION_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_TRANSACTION_AMOUNT = new BigDecimal(1 - 1);

    @Autowired
    private CashPaymentVoucherSupplierRepository cashPaymentVoucherSupplierRepository;

    @Autowired
    private CashPaymentVoucherSupplierService cashPaymentVoucherSupplierService;

    @Autowired
    private CashPaymentVoucherSupplierQueryService cashPaymentVoucherSupplierQueryService;

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

    private MockMvc restCashPaymentVoucherSupplierMockMvc;

    private CashPaymentVoucherSupplier cashPaymentVoucherSupplier;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CashPaymentVoucherSupplierResource cashPaymentVoucherSupplierResource = new CashPaymentVoucherSupplierResource(cashPaymentVoucherSupplierService, cashPaymentVoucherSupplierQueryService);
        this.restCashPaymentVoucherSupplierMockMvc = MockMvcBuilders.standaloneSetup(cashPaymentVoucherSupplierResource)
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
    public static CashPaymentVoucherSupplier createEntity(EntityManager em) {
        CashPaymentVoucherSupplier cashPaymentVoucherSupplier = new CashPaymentVoucherSupplier()
            .transactionNumber(DEFAULT_TRANSACTION_NUMBER)
            .transactionDate(DEFAULT_TRANSACTION_DATE)
            .transactionDescription(DEFAULT_TRANSACTION_DESCRIPTION)
            .transactionAmount(DEFAULT_TRANSACTION_AMOUNT);
        // Add required entity
        Location location;
        if (TestUtil.findAll(em, Location.class).isEmpty()) {
            location = LocationResourceIT.createEntity(em);
            em.persist(location);
            em.flush();
        } else {
            location = TestUtil.findAll(em, Location.class).get(0);
        }
        cashPaymentVoucherSupplier.setLocation(location);
        // Add required entity
        TransactionType transactionType;
        if (TestUtil.findAll(em, TransactionType.class).isEmpty()) {
            transactionType = TransactionTypeResourceIT.createEntity(em);
            em.persist(transactionType);
            em.flush();
        } else {
            transactionType = TestUtil.findAll(em, TransactionType.class).get(0);
        }
        cashPaymentVoucherSupplier.setTransactionType(transactionType);
        // Add required entity
        Supplier supplier;
        if (TestUtil.findAll(em, Supplier.class).isEmpty()) {
            supplier = SupplierResourceIT.createEntity(em);
            em.persist(supplier);
            em.flush();
        } else {
            supplier = TestUtil.findAll(em, Supplier.class).get(0);
        }
        cashPaymentVoucherSupplier.setSupplier(supplier);
        return cashPaymentVoucherSupplier;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CashPaymentVoucherSupplier createUpdatedEntity(EntityManager em) {
        CashPaymentVoucherSupplier cashPaymentVoucherSupplier = new CashPaymentVoucherSupplier()
            .transactionNumber(UPDATED_TRANSACTION_NUMBER)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .transactionDescription(UPDATED_TRANSACTION_DESCRIPTION)
            .transactionAmount(UPDATED_TRANSACTION_AMOUNT);
        // Add required entity
        Location location;
        if (TestUtil.findAll(em, Location.class).isEmpty()) {
            location = LocationResourceIT.createUpdatedEntity(em);
            em.persist(location);
            em.flush();
        } else {
            location = TestUtil.findAll(em, Location.class).get(0);
        }
        cashPaymentVoucherSupplier.setLocation(location);
        // Add required entity
        TransactionType transactionType;
        if (TestUtil.findAll(em, TransactionType.class).isEmpty()) {
            transactionType = TransactionTypeResourceIT.createUpdatedEntity(em);
            em.persist(transactionType);
            em.flush();
        } else {
            transactionType = TestUtil.findAll(em, TransactionType.class).get(0);
        }
        cashPaymentVoucherSupplier.setTransactionType(transactionType);
        // Add required entity
        Supplier supplier;
        if (TestUtil.findAll(em, Supplier.class).isEmpty()) {
            supplier = SupplierResourceIT.createUpdatedEntity(em);
            em.persist(supplier);
            em.flush();
        } else {
            supplier = TestUtil.findAll(em, Supplier.class).get(0);
        }
        cashPaymentVoucherSupplier.setSupplier(supplier);
        return cashPaymentVoucherSupplier;
    }

    @BeforeEach
    public void initTest() {
        cashPaymentVoucherSupplier = createEntity(em);
    }

    @Test
    @Transactional
    public void createCashPaymentVoucherSupplier() throws Exception {
        int databaseSizeBeforeCreate = cashPaymentVoucherSupplierRepository.findAll().size();

        // Create the CashPaymentVoucherSupplier
        restCashPaymentVoucherSupplierMockMvc.perform(post("/api/cash-payment-voucher-suppliers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashPaymentVoucherSupplier)))
            .andExpect(status().isCreated());

        // Validate the CashPaymentVoucherSupplier in the database
        List<CashPaymentVoucherSupplier> cashPaymentVoucherSupplierList = cashPaymentVoucherSupplierRepository.findAll();
        assertThat(cashPaymentVoucherSupplierList).hasSize(databaseSizeBeforeCreate + 1);
        CashPaymentVoucherSupplier testCashPaymentVoucherSupplier = cashPaymentVoucherSupplierList.get(cashPaymentVoucherSupplierList.size() - 1);
        assertThat(testCashPaymentVoucherSupplier.getTransactionNumber()).isEqualTo(DEFAULT_TRANSACTION_NUMBER);
        assertThat(testCashPaymentVoucherSupplier.getTransactionDate()).isEqualTo(DEFAULT_TRANSACTION_DATE);
        assertThat(testCashPaymentVoucherSupplier.getTransactionDescription()).isEqualTo(DEFAULT_TRANSACTION_DESCRIPTION);
        assertThat(testCashPaymentVoucherSupplier.getTransactionAmount()).isEqualTo(DEFAULT_TRANSACTION_AMOUNT);
    }

    @Test
    @Transactional
    public void createCashPaymentVoucherSupplierWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cashPaymentVoucherSupplierRepository.findAll().size();

        // Create the CashPaymentVoucherSupplier with an existing ID
        cashPaymentVoucherSupplier.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCashPaymentVoucherSupplierMockMvc.perform(post("/api/cash-payment-voucher-suppliers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashPaymentVoucherSupplier)))
            .andExpect(status().isBadRequest());

        // Validate the CashPaymentVoucherSupplier in the database
        List<CashPaymentVoucherSupplier> cashPaymentVoucherSupplierList = cashPaymentVoucherSupplierRepository.findAll();
        assertThat(cashPaymentVoucherSupplierList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTransactionNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = cashPaymentVoucherSupplierRepository.findAll().size();
        // set the field null
        cashPaymentVoucherSupplier.setTransactionNumber(null);

        // Create the CashPaymentVoucherSupplier, which fails.

        restCashPaymentVoucherSupplierMockMvc.perform(post("/api/cash-payment-voucher-suppliers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashPaymentVoucherSupplier)))
            .andExpect(status().isBadRequest());

        List<CashPaymentVoucherSupplier> cashPaymentVoucherSupplierList = cashPaymentVoucherSupplierRepository.findAll();
        assertThat(cashPaymentVoucherSupplierList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTransactionDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = cashPaymentVoucherSupplierRepository.findAll().size();
        // set the field null
        cashPaymentVoucherSupplier.setTransactionDate(null);

        // Create the CashPaymentVoucherSupplier, which fails.

        restCashPaymentVoucherSupplierMockMvc.perform(post("/api/cash-payment-voucher-suppliers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashPaymentVoucherSupplier)))
            .andExpect(status().isBadRequest());

        List<CashPaymentVoucherSupplier> cashPaymentVoucherSupplierList = cashPaymentVoucherSupplierRepository.findAll();
        assertThat(cashPaymentVoucherSupplierList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTransactionDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = cashPaymentVoucherSupplierRepository.findAll().size();
        // set the field null
        cashPaymentVoucherSupplier.setTransactionDescription(null);

        // Create the CashPaymentVoucherSupplier, which fails.

        restCashPaymentVoucherSupplierMockMvc.perform(post("/api/cash-payment-voucher-suppliers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashPaymentVoucherSupplier)))
            .andExpect(status().isBadRequest());

        List<CashPaymentVoucherSupplier> cashPaymentVoucherSupplierList = cashPaymentVoucherSupplierRepository.findAll();
        assertThat(cashPaymentVoucherSupplierList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTransactionAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = cashPaymentVoucherSupplierRepository.findAll().size();
        // set the field null
        cashPaymentVoucherSupplier.setTransactionAmount(null);

        // Create the CashPaymentVoucherSupplier, which fails.

        restCashPaymentVoucherSupplierMockMvc.perform(post("/api/cash-payment-voucher-suppliers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashPaymentVoucherSupplier)))
            .andExpect(status().isBadRequest());

        List<CashPaymentVoucherSupplier> cashPaymentVoucherSupplierList = cashPaymentVoucherSupplierRepository.findAll();
        assertThat(cashPaymentVoucherSupplierList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherSuppliers() throws Exception {
        // Initialize the database
        cashPaymentVoucherSupplierRepository.saveAndFlush(cashPaymentVoucherSupplier);

        // Get all the cashPaymentVoucherSupplierList
        restCashPaymentVoucherSupplierMockMvc.perform(get("/api/cash-payment-voucher-suppliers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cashPaymentVoucherSupplier.getId().intValue())))
            .andExpect(jsonPath("$.[*].transactionNumber").value(hasItem(DEFAULT_TRANSACTION_NUMBER)))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].transactionDescription").value(hasItem(DEFAULT_TRANSACTION_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].transactionAmount").value(hasItem(DEFAULT_TRANSACTION_AMOUNT.intValue())));
    }
    
    @Test
    @Transactional
    public void getCashPaymentVoucherSupplier() throws Exception {
        // Initialize the database
        cashPaymentVoucherSupplierRepository.saveAndFlush(cashPaymentVoucherSupplier);

        // Get the cashPaymentVoucherSupplier
        restCashPaymentVoucherSupplierMockMvc.perform(get("/api/cash-payment-voucher-suppliers/{id}", cashPaymentVoucherSupplier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cashPaymentVoucherSupplier.getId().intValue()))
            .andExpect(jsonPath("$.transactionNumber").value(DEFAULT_TRANSACTION_NUMBER))
            .andExpect(jsonPath("$.transactionDate").value(DEFAULT_TRANSACTION_DATE.toString()))
            .andExpect(jsonPath("$.transactionDescription").value(DEFAULT_TRANSACTION_DESCRIPTION))
            .andExpect(jsonPath("$.transactionAmount").value(DEFAULT_TRANSACTION_AMOUNT.intValue()));
    }


    @Test
    @Transactional
    public void getCashPaymentVoucherSuppliersByIdFiltering() throws Exception {
        // Initialize the database
        cashPaymentVoucherSupplierRepository.saveAndFlush(cashPaymentVoucherSupplier);

        Long id = cashPaymentVoucherSupplier.getId();

        defaultCashPaymentVoucherSupplierShouldBeFound("id.equals=" + id);
        defaultCashPaymentVoucherSupplierShouldNotBeFound("id.notEquals=" + id);

        defaultCashPaymentVoucherSupplierShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCashPaymentVoucherSupplierShouldNotBeFound("id.greaterThan=" + id);

        defaultCashPaymentVoucherSupplierShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCashPaymentVoucherSupplierShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllCashPaymentVoucherSuppliersByTransactionNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        cashPaymentVoucherSupplierRepository.saveAndFlush(cashPaymentVoucherSupplier);

        // Get all the cashPaymentVoucherSupplierList where transactionNumber equals to DEFAULT_TRANSACTION_NUMBER
        defaultCashPaymentVoucherSupplierShouldBeFound("transactionNumber.equals=" + DEFAULT_TRANSACTION_NUMBER);

        // Get all the cashPaymentVoucherSupplierList where transactionNumber equals to UPDATED_TRANSACTION_NUMBER
        defaultCashPaymentVoucherSupplierShouldNotBeFound("transactionNumber.equals=" + UPDATED_TRANSACTION_NUMBER);
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherSuppliersByTransactionNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cashPaymentVoucherSupplierRepository.saveAndFlush(cashPaymentVoucherSupplier);

        // Get all the cashPaymentVoucherSupplierList where transactionNumber not equals to DEFAULT_TRANSACTION_NUMBER
        defaultCashPaymentVoucherSupplierShouldNotBeFound("transactionNumber.notEquals=" + DEFAULT_TRANSACTION_NUMBER);

        // Get all the cashPaymentVoucherSupplierList where transactionNumber not equals to UPDATED_TRANSACTION_NUMBER
        defaultCashPaymentVoucherSupplierShouldBeFound("transactionNumber.notEquals=" + UPDATED_TRANSACTION_NUMBER);
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherSuppliersByTransactionNumberIsInShouldWork() throws Exception {
        // Initialize the database
        cashPaymentVoucherSupplierRepository.saveAndFlush(cashPaymentVoucherSupplier);

        // Get all the cashPaymentVoucherSupplierList where transactionNumber in DEFAULT_TRANSACTION_NUMBER or UPDATED_TRANSACTION_NUMBER
        defaultCashPaymentVoucherSupplierShouldBeFound("transactionNumber.in=" + DEFAULT_TRANSACTION_NUMBER + "," + UPDATED_TRANSACTION_NUMBER);

        // Get all the cashPaymentVoucherSupplierList where transactionNumber equals to UPDATED_TRANSACTION_NUMBER
        defaultCashPaymentVoucherSupplierShouldNotBeFound("transactionNumber.in=" + UPDATED_TRANSACTION_NUMBER);
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherSuppliersByTransactionNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        cashPaymentVoucherSupplierRepository.saveAndFlush(cashPaymentVoucherSupplier);

        // Get all the cashPaymentVoucherSupplierList where transactionNumber is not null
        defaultCashPaymentVoucherSupplierShouldBeFound("transactionNumber.specified=true");

        // Get all the cashPaymentVoucherSupplierList where transactionNumber is null
        defaultCashPaymentVoucherSupplierShouldNotBeFound("transactionNumber.specified=false");
    }
                @Test
    @Transactional
    public void getAllCashPaymentVoucherSuppliersByTransactionNumberContainsSomething() throws Exception {
        // Initialize the database
        cashPaymentVoucherSupplierRepository.saveAndFlush(cashPaymentVoucherSupplier);

        // Get all the cashPaymentVoucherSupplierList where transactionNumber contains DEFAULT_TRANSACTION_NUMBER
        defaultCashPaymentVoucherSupplierShouldBeFound("transactionNumber.contains=" + DEFAULT_TRANSACTION_NUMBER);

        // Get all the cashPaymentVoucherSupplierList where transactionNumber contains UPDATED_TRANSACTION_NUMBER
        defaultCashPaymentVoucherSupplierShouldNotBeFound("transactionNumber.contains=" + UPDATED_TRANSACTION_NUMBER);
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherSuppliersByTransactionNumberNotContainsSomething() throws Exception {
        // Initialize the database
        cashPaymentVoucherSupplierRepository.saveAndFlush(cashPaymentVoucherSupplier);

        // Get all the cashPaymentVoucherSupplierList where transactionNumber does not contain DEFAULT_TRANSACTION_NUMBER
        defaultCashPaymentVoucherSupplierShouldNotBeFound("transactionNumber.doesNotContain=" + DEFAULT_TRANSACTION_NUMBER);

        // Get all the cashPaymentVoucherSupplierList where transactionNumber does not contain UPDATED_TRANSACTION_NUMBER
        defaultCashPaymentVoucherSupplierShouldBeFound("transactionNumber.doesNotContain=" + UPDATED_TRANSACTION_NUMBER);
    }


    @Test
    @Transactional
    public void getAllCashPaymentVoucherSuppliersByTransactionDateIsEqualToSomething() throws Exception {
        // Initialize the database
        cashPaymentVoucherSupplierRepository.saveAndFlush(cashPaymentVoucherSupplier);

        // Get all the cashPaymentVoucherSupplierList where transactionDate equals to DEFAULT_TRANSACTION_DATE
        defaultCashPaymentVoucherSupplierShouldBeFound("transactionDate.equals=" + DEFAULT_TRANSACTION_DATE);

        // Get all the cashPaymentVoucherSupplierList where transactionDate equals to UPDATED_TRANSACTION_DATE
        defaultCashPaymentVoucherSupplierShouldNotBeFound("transactionDate.equals=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherSuppliersByTransactionDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cashPaymentVoucherSupplierRepository.saveAndFlush(cashPaymentVoucherSupplier);

        // Get all the cashPaymentVoucherSupplierList where transactionDate not equals to DEFAULT_TRANSACTION_DATE
        defaultCashPaymentVoucherSupplierShouldNotBeFound("transactionDate.notEquals=" + DEFAULT_TRANSACTION_DATE);

        // Get all the cashPaymentVoucherSupplierList where transactionDate not equals to UPDATED_TRANSACTION_DATE
        defaultCashPaymentVoucherSupplierShouldBeFound("transactionDate.notEquals=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherSuppliersByTransactionDateIsInShouldWork() throws Exception {
        // Initialize the database
        cashPaymentVoucherSupplierRepository.saveAndFlush(cashPaymentVoucherSupplier);

        // Get all the cashPaymentVoucherSupplierList where transactionDate in DEFAULT_TRANSACTION_DATE or UPDATED_TRANSACTION_DATE
        defaultCashPaymentVoucherSupplierShouldBeFound("transactionDate.in=" + DEFAULT_TRANSACTION_DATE + "," + UPDATED_TRANSACTION_DATE);

        // Get all the cashPaymentVoucherSupplierList where transactionDate equals to UPDATED_TRANSACTION_DATE
        defaultCashPaymentVoucherSupplierShouldNotBeFound("transactionDate.in=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherSuppliersByTransactionDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        cashPaymentVoucherSupplierRepository.saveAndFlush(cashPaymentVoucherSupplier);

        // Get all the cashPaymentVoucherSupplierList where transactionDate is not null
        defaultCashPaymentVoucherSupplierShouldBeFound("transactionDate.specified=true");

        // Get all the cashPaymentVoucherSupplierList where transactionDate is null
        defaultCashPaymentVoucherSupplierShouldNotBeFound("transactionDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherSuppliersByTransactionDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cashPaymentVoucherSupplierRepository.saveAndFlush(cashPaymentVoucherSupplier);

        // Get all the cashPaymentVoucherSupplierList where transactionDate is greater than or equal to DEFAULT_TRANSACTION_DATE
        defaultCashPaymentVoucherSupplierShouldBeFound("transactionDate.greaterThanOrEqual=" + DEFAULT_TRANSACTION_DATE);

        // Get all the cashPaymentVoucherSupplierList where transactionDate is greater than or equal to UPDATED_TRANSACTION_DATE
        defaultCashPaymentVoucherSupplierShouldNotBeFound("transactionDate.greaterThanOrEqual=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherSuppliersByTransactionDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cashPaymentVoucherSupplierRepository.saveAndFlush(cashPaymentVoucherSupplier);

        // Get all the cashPaymentVoucherSupplierList where transactionDate is less than or equal to DEFAULT_TRANSACTION_DATE
        defaultCashPaymentVoucherSupplierShouldBeFound("transactionDate.lessThanOrEqual=" + DEFAULT_TRANSACTION_DATE);

        // Get all the cashPaymentVoucherSupplierList where transactionDate is less than or equal to SMALLER_TRANSACTION_DATE
        defaultCashPaymentVoucherSupplierShouldNotBeFound("transactionDate.lessThanOrEqual=" + SMALLER_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherSuppliersByTransactionDateIsLessThanSomething() throws Exception {
        // Initialize the database
        cashPaymentVoucherSupplierRepository.saveAndFlush(cashPaymentVoucherSupplier);

        // Get all the cashPaymentVoucherSupplierList where transactionDate is less than DEFAULT_TRANSACTION_DATE
        defaultCashPaymentVoucherSupplierShouldNotBeFound("transactionDate.lessThan=" + DEFAULT_TRANSACTION_DATE);

        // Get all the cashPaymentVoucherSupplierList where transactionDate is less than UPDATED_TRANSACTION_DATE
        defaultCashPaymentVoucherSupplierShouldBeFound("transactionDate.lessThan=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherSuppliersByTransactionDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cashPaymentVoucherSupplierRepository.saveAndFlush(cashPaymentVoucherSupplier);

        // Get all the cashPaymentVoucherSupplierList where transactionDate is greater than DEFAULT_TRANSACTION_DATE
        defaultCashPaymentVoucherSupplierShouldNotBeFound("transactionDate.greaterThan=" + DEFAULT_TRANSACTION_DATE);

        // Get all the cashPaymentVoucherSupplierList where transactionDate is greater than SMALLER_TRANSACTION_DATE
        defaultCashPaymentVoucherSupplierShouldBeFound("transactionDate.greaterThan=" + SMALLER_TRANSACTION_DATE);
    }


    @Test
    @Transactional
    public void getAllCashPaymentVoucherSuppliersByTransactionDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        cashPaymentVoucherSupplierRepository.saveAndFlush(cashPaymentVoucherSupplier);

        // Get all the cashPaymentVoucherSupplierList where transactionDescription equals to DEFAULT_TRANSACTION_DESCRIPTION
        defaultCashPaymentVoucherSupplierShouldBeFound("transactionDescription.equals=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the cashPaymentVoucherSupplierList where transactionDescription equals to UPDATED_TRANSACTION_DESCRIPTION
        defaultCashPaymentVoucherSupplierShouldNotBeFound("transactionDescription.equals=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherSuppliersByTransactionDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cashPaymentVoucherSupplierRepository.saveAndFlush(cashPaymentVoucherSupplier);

        // Get all the cashPaymentVoucherSupplierList where transactionDescription not equals to DEFAULT_TRANSACTION_DESCRIPTION
        defaultCashPaymentVoucherSupplierShouldNotBeFound("transactionDescription.notEquals=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the cashPaymentVoucherSupplierList where transactionDescription not equals to UPDATED_TRANSACTION_DESCRIPTION
        defaultCashPaymentVoucherSupplierShouldBeFound("transactionDescription.notEquals=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherSuppliersByTransactionDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        cashPaymentVoucherSupplierRepository.saveAndFlush(cashPaymentVoucherSupplier);

        // Get all the cashPaymentVoucherSupplierList where transactionDescription in DEFAULT_TRANSACTION_DESCRIPTION or UPDATED_TRANSACTION_DESCRIPTION
        defaultCashPaymentVoucherSupplierShouldBeFound("transactionDescription.in=" + DEFAULT_TRANSACTION_DESCRIPTION + "," + UPDATED_TRANSACTION_DESCRIPTION);

        // Get all the cashPaymentVoucherSupplierList where transactionDescription equals to UPDATED_TRANSACTION_DESCRIPTION
        defaultCashPaymentVoucherSupplierShouldNotBeFound("transactionDescription.in=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherSuppliersByTransactionDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        cashPaymentVoucherSupplierRepository.saveAndFlush(cashPaymentVoucherSupplier);

        // Get all the cashPaymentVoucherSupplierList where transactionDescription is not null
        defaultCashPaymentVoucherSupplierShouldBeFound("transactionDescription.specified=true");

        // Get all the cashPaymentVoucherSupplierList where transactionDescription is null
        defaultCashPaymentVoucherSupplierShouldNotBeFound("transactionDescription.specified=false");
    }
                @Test
    @Transactional
    public void getAllCashPaymentVoucherSuppliersByTransactionDescriptionContainsSomething() throws Exception {
        // Initialize the database
        cashPaymentVoucherSupplierRepository.saveAndFlush(cashPaymentVoucherSupplier);

        // Get all the cashPaymentVoucherSupplierList where transactionDescription contains DEFAULT_TRANSACTION_DESCRIPTION
        defaultCashPaymentVoucherSupplierShouldBeFound("transactionDescription.contains=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the cashPaymentVoucherSupplierList where transactionDescription contains UPDATED_TRANSACTION_DESCRIPTION
        defaultCashPaymentVoucherSupplierShouldNotBeFound("transactionDescription.contains=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherSuppliersByTransactionDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        cashPaymentVoucherSupplierRepository.saveAndFlush(cashPaymentVoucherSupplier);

        // Get all the cashPaymentVoucherSupplierList where transactionDescription does not contain DEFAULT_TRANSACTION_DESCRIPTION
        defaultCashPaymentVoucherSupplierShouldNotBeFound("transactionDescription.doesNotContain=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the cashPaymentVoucherSupplierList where transactionDescription does not contain UPDATED_TRANSACTION_DESCRIPTION
        defaultCashPaymentVoucherSupplierShouldBeFound("transactionDescription.doesNotContain=" + UPDATED_TRANSACTION_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllCashPaymentVoucherSuppliersByTransactionAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        cashPaymentVoucherSupplierRepository.saveAndFlush(cashPaymentVoucherSupplier);

        // Get all the cashPaymentVoucherSupplierList where transactionAmount equals to DEFAULT_TRANSACTION_AMOUNT
        defaultCashPaymentVoucherSupplierShouldBeFound("transactionAmount.equals=" + DEFAULT_TRANSACTION_AMOUNT);

        // Get all the cashPaymentVoucherSupplierList where transactionAmount equals to UPDATED_TRANSACTION_AMOUNT
        defaultCashPaymentVoucherSupplierShouldNotBeFound("transactionAmount.equals=" + UPDATED_TRANSACTION_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherSuppliersByTransactionAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cashPaymentVoucherSupplierRepository.saveAndFlush(cashPaymentVoucherSupplier);

        // Get all the cashPaymentVoucherSupplierList where transactionAmount not equals to DEFAULT_TRANSACTION_AMOUNT
        defaultCashPaymentVoucherSupplierShouldNotBeFound("transactionAmount.notEquals=" + DEFAULT_TRANSACTION_AMOUNT);

        // Get all the cashPaymentVoucherSupplierList where transactionAmount not equals to UPDATED_TRANSACTION_AMOUNT
        defaultCashPaymentVoucherSupplierShouldBeFound("transactionAmount.notEquals=" + UPDATED_TRANSACTION_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherSuppliersByTransactionAmountIsInShouldWork() throws Exception {
        // Initialize the database
        cashPaymentVoucherSupplierRepository.saveAndFlush(cashPaymentVoucherSupplier);

        // Get all the cashPaymentVoucherSupplierList where transactionAmount in DEFAULT_TRANSACTION_AMOUNT or UPDATED_TRANSACTION_AMOUNT
        defaultCashPaymentVoucherSupplierShouldBeFound("transactionAmount.in=" + DEFAULT_TRANSACTION_AMOUNT + "," + UPDATED_TRANSACTION_AMOUNT);

        // Get all the cashPaymentVoucherSupplierList where transactionAmount equals to UPDATED_TRANSACTION_AMOUNT
        defaultCashPaymentVoucherSupplierShouldNotBeFound("transactionAmount.in=" + UPDATED_TRANSACTION_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherSuppliersByTransactionAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        cashPaymentVoucherSupplierRepository.saveAndFlush(cashPaymentVoucherSupplier);

        // Get all the cashPaymentVoucherSupplierList where transactionAmount is not null
        defaultCashPaymentVoucherSupplierShouldBeFound("transactionAmount.specified=true");

        // Get all the cashPaymentVoucherSupplierList where transactionAmount is null
        defaultCashPaymentVoucherSupplierShouldNotBeFound("transactionAmount.specified=false");
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherSuppliersByTransactionAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cashPaymentVoucherSupplierRepository.saveAndFlush(cashPaymentVoucherSupplier);

        // Get all the cashPaymentVoucherSupplierList where transactionAmount is greater than or equal to DEFAULT_TRANSACTION_AMOUNT
        defaultCashPaymentVoucherSupplierShouldBeFound("transactionAmount.greaterThanOrEqual=" + DEFAULT_TRANSACTION_AMOUNT);

        // Get all the cashPaymentVoucherSupplierList where transactionAmount is greater than or equal to UPDATED_TRANSACTION_AMOUNT
        defaultCashPaymentVoucherSupplierShouldNotBeFound("transactionAmount.greaterThanOrEqual=" + UPDATED_TRANSACTION_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherSuppliersByTransactionAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cashPaymentVoucherSupplierRepository.saveAndFlush(cashPaymentVoucherSupplier);

        // Get all the cashPaymentVoucherSupplierList where transactionAmount is less than or equal to DEFAULT_TRANSACTION_AMOUNT
        defaultCashPaymentVoucherSupplierShouldBeFound("transactionAmount.lessThanOrEqual=" + DEFAULT_TRANSACTION_AMOUNT);

        // Get all the cashPaymentVoucherSupplierList where transactionAmount is less than or equal to SMALLER_TRANSACTION_AMOUNT
        defaultCashPaymentVoucherSupplierShouldNotBeFound("transactionAmount.lessThanOrEqual=" + SMALLER_TRANSACTION_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherSuppliersByTransactionAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        cashPaymentVoucherSupplierRepository.saveAndFlush(cashPaymentVoucherSupplier);

        // Get all the cashPaymentVoucherSupplierList where transactionAmount is less than DEFAULT_TRANSACTION_AMOUNT
        defaultCashPaymentVoucherSupplierShouldNotBeFound("transactionAmount.lessThan=" + DEFAULT_TRANSACTION_AMOUNT);

        // Get all the cashPaymentVoucherSupplierList where transactionAmount is less than UPDATED_TRANSACTION_AMOUNT
        defaultCashPaymentVoucherSupplierShouldBeFound("transactionAmount.lessThan=" + UPDATED_TRANSACTION_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherSuppliersByTransactionAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cashPaymentVoucherSupplierRepository.saveAndFlush(cashPaymentVoucherSupplier);

        // Get all the cashPaymentVoucherSupplierList where transactionAmount is greater than DEFAULT_TRANSACTION_AMOUNT
        defaultCashPaymentVoucherSupplierShouldNotBeFound("transactionAmount.greaterThan=" + DEFAULT_TRANSACTION_AMOUNT);

        // Get all the cashPaymentVoucherSupplierList where transactionAmount is greater than SMALLER_TRANSACTION_AMOUNT
        defaultCashPaymentVoucherSupplierShouldBeFound("transactionAmount.greaterThan=" + SMALLER_TRANSACTION_AMOUNT);
    }


    @Test
    @Transactional
    public void getAllCashPaymentVoucherSuppliersByLocationIsEqualToSomething() throws Exception {
        // Get already existing entity
        Location location = cashPaymentVoucherSupplier.getLocation();
        cashPaymentVoucherSupplierRepository.saveAndFlush(cashPaymentVoucherSupplier);
        Long locationId = location.getId();

        // Get all the cashPaymentVoucherSupplierList where location equals to locationId
        defaultCashPaymentVoucherSupplierShouldBeFound("locationId.equals=" + locationId);

        // Get all the cashPaymentVoucherSupplierList where location equals to locationId + 1
        defaultCashPaymentVoucherSupplierShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }


    @Test
    @Transactional
    public void getAllCashPaymentVoucherSuppliersByTransactionTypeIsEqualToSomething() throws Exception {
        // Get already existing entity
        TransactionType transactionType = cashPaymentVoucherSupplier.getTransactionType();
        cashPaymentVoucherSupplierRepository.saveAndFlush(cashPaymentVoucherSupplier);
        Long transactionTypeId = transactionType.getId();

        // Get all the cashPaymentVoucherSupplierList where transactionType equals to transactionTypeId
        defaultCashPaymentVoucherSupplierShouldBeFound("transactionTypeId.equals=" + transactionTypeId);

        // Get all the cashPaymentVoucherSupplierList where transactionType equals to transactionTypeId + 1
        defaultCashPaymentVoucherSupplierShouldNotBeFound("transactionTypeId.equals=" + (transactionTypeId + 1));
    }


    @Test
    @Transactional
    public void getAllCashPaymentVoucherSuppliersBySupplierIsEqualToSomething() throws Exception {
        // Get already existing entity
        Supplier supplier = cashPaymentVoucherSupplier.getSupplier();
        cashPaymentVoucherSupplierRepository.saveAndFlush(cashPaymentVoucherSupplier);
        Long supplierId = supplier.getId();

        // Get all the cashPaymentVoucherSupplierList where supplier equals to supplierId
        defaultCashPaymentVoucherSupplierShouldBeFound("supplierId.equals=" + supplierId);

        // Get all the cashPaymentVoucherSupplierList where supplier equals to supplierId + 1
        defaultCashPaymentVoucherSupplierShouldNotBeFound("supplierId.equals=" + (supplierId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCashPaymentVoucherSupplierShouldBeFound(String filter) throws Exception {
        restCashPaymentVoucherSupplierMockMvc.perform(get("/api/cash-payment-voucher-suppliers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cashPaymentVoucherSupplier.getId().intValue())))
            .andExpect(jsonPath("$.[*].transactionNumber").value(hasItem(DEFAULT_TRANSACTION_NUMBER)))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].transactionDescription").value(hasItem(DEFAULT_TRANSACTION_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].transactionAmount").value(hasItem(DEFAULT_TRANSACTION_AMOUNT.intValue())));

        // Check, that the count call also returns 1
        restCashPaymentVoucherSupplierMockMvc.perform(get("/api/cash-payment-voucher-suppliers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCashPaymentVoucherSupplierShouldNotBeFound(String filter) throws Exception {
        restCashPaymentVoucherSupplierMockMvc.perform(get("/api/cash-payment-voucher-suppliers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCashPaymentVoucherSupplierMockMvc.perform(get("/api/cash-payment-voucher-suppliers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingCashPaymentVoucherSupplier() throws Exception {
        // Get the cashPaymentVoucherSupplier
        restCashPaymentVoucherSupplierMockMvc.perform(get("/api/cash-payment-voucher-suppliers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCashPaymentVoucherSupplier() throws Exception {
        // Initialize the database
        cashPaymentVoucherSupplierService.save(cashPaymentVoucherSupplier);

        int databaseSizeBeforeUpdate = cashPaymentVoucherSupplierRepository.findAll().size();

        // Update the cashPaymentVoucherSupplier
        CashPaymentVoucherSupplier updatedCashPaymentVoucherSupplier = cashPaymentVoucherSupplierRepository.findById(cashPaymentVoucherSupplier.getId()).get();
        // Disconnect from session so that the updates on updatedCashPaymentVoucherSupplier are not directly saved in db
        em.detach(updatedCashPaymentVoucherSupplier);
        updatedCashPaymentVoucherSupplier
            .transactionNumber(UPDATED_TRANSACTION_NUMBER)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .transactionDescription(UPDATED_TRANSACTION_DESCRIPTION)
            .transactionAmount(UPDATED_TRANSACTION_AMOUNT);

        restCashPaymentVoucherSupplierMockMvc.perform(put("/api/cash-payment-voucher-suppliers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCashPaymentVoucherSupplier)))
            .andExpect(status().isOk());

        // Validate the CashPaymentVoucherSupplier in the database
        List<CashPaymentVoucherSupplier> cashPaymentVoucherSupplierList = cashPaymentVoucherSupplierRepository.findAll();
        assertThat(cashPaymentVoucherSupplierList).hasSize(databaseSizeBeforeUpdate);
        CashPaymentVoucherSupplier testCashPaymentVoucherSupplier = cashPaymentVoucherSupplierList.get(cashPaymentVoucherSupplierList.size() - 1);
        assertThat(testCashPaymentVoucherSupplier.getTransactionNumber()).isEqualTo(UPDATED_TRANSACTION_NUMBER);
        assertThat(testCashPaymentVoucherSupplier.getTransactionDate()).isEqualTo(UPDATED_TRANSACTION_DATE);
        assertThat(testCashPaymentVoucherSupplier.getTransactionDescription()).isEqualTo(UPDATED_TRANSACTION_DESCRIPTION);
        assertThat(testCashPaymentVoucherSupplier.getTransactionAmount()).isEqualTo(UPDATED_TRANSACTION_AMOUNT);
    }

    @Test
    @Transactional
    public void updateNonExistingCashPaymentVoucherSupplier() throws Exception {
        int databaseSizeBeforeUpdate = cashPaymentVoucherSupplierRepository.findAll().size();

        // Create the CashPaymentVoucherSupplier

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCashPaymentVoucherSupplierMockMvc.perform(put("/api/cash-payment-voucher-suppliers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashPaymentVoucherSupplier)))
            .andExpect(status().isBadRequest());

        // Validate the CashPaymentVoucherSupplier in the database
        List<CashPaymentVoucherSupplier> cashPaymentVoucherSupplierList = cashPaymentVoucherSupplierRepository.findAll();
        assertThat(cashPaymentVoucherSupplierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCashPaymentVoucherSupplier() throws Exception {
        // Initialize the database
        cashPaymentVoucherSupplierService.save(cashPaymentVoucherSupplier);

        int databaseSizeBeforeDelete = cashPaymentVoucherSupplierRepository.findAll().size();

        // Delete the cashPaymentVoucherSupplier
        restCashPaymentVoucherSupplierMockMvc.perform(delete("/api/cash-payment-voucher-suppliers/{id}", cashPaymentVoucherSupplier.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CashPaymentVoucherSupplier> cashPaymentVoucherSupplierList = cashPaymentVoucherSupplierRepository.findAll();
        assertThat(cashPaymentVoucherSupplierList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

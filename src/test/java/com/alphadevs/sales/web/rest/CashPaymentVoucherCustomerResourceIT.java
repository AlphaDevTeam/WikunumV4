package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.WikunumApp;
import com.alphadevs.sales.domain.CashPaymentVoucherCustomer;
import com.alphadevs.sales.domain.DocumentHistory;
import com.alphadevs.sales.domain.Location;
import com.alphadevs.sales.domain.TransactionType;
import com.alphadevs.sales.domain.Customer;
import com.alphadevs.sales.repository.CashPaymentVoucherCustomerRepository;
import com.alphadevs.sales.service.CashPaymentVoucherCustomerService;
import com.alphadevs.sales.web.rest.errors.ExceptionTranslator;
import com.alphadevs.sales.service.dto.CashPaymentVoucherCustomerCriteria;
import com.alphadevs.sales.service.CashPaymentVoucherCustomerQueryService;

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
 * Integration tests for the {@link CashPaymentVoucherCustomerResource} REST controller.
 */
@SpringBootTest(classes = WikunumApp.class)
public class CashPaymentVoucherCustomerResourceIT {

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
    private CashPaymentVoucherCustomerRepository cashPaymentVoucherCustomerRepository;

    @Autowired
    private CashPaymentVoucherCustomerService cashPaymentVoucherCustomerService;

    @Autowired
    private CashPaymentVoucherCustomerQueryService cashPaymentVoucherCustomerQueryService;

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

    private MockMvc restCashPaymentVoucherCustomerMockMvc;

    private CashPaymentVoucherCustomer cashPaymentVoucherCustomer;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CashPaymentVoucherCustomerResource cashPaymentVoucherCustomerResource = new CashPaymentVoucherCustomerResource(cashPaymentVoucherCustomerService, cashPaymentVoucherCustomerQueryService);
        this.restCashPaymentVoucherCustomerMockMvc = MockMvcBuilders.standaloneSetup(cashPaymentVoucherCustomerResource)
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
    public static CashPaymentVoucherCustomer createEntity(EntityManager em) {
        CashPaymentVoucherCustomer cashPaymentVoucherCustomer = new CashPaymentVoucherCustomer()
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
        cashPaymentVoucherCustomer.setLocation(location);
        // Add required entity
        TransactionType transactionType;
        if (TestUtil.findAll(em, TransactionType.class).isEmpty()) {
            transactionType = TransactionTypeResourceIT.createEntity(em);
            em.persist(transactionType);
            em.flush();
        } else {
            transactionType = TestUtil.findAll(em, TransactionType.class).get(0);
        }
        cashPaymentVoucherCustomer.setTransactionType(transactionType);
        // Add required entity
        Customer customer;
        if (TestUtil.findAll(em, Customer.class).isEmpty()) {
            customer = CustomerResourceIT.createEntity(em);
            em.persist(customer);
            em.flush();
        } else {
            customer = TestUtil.findAll(em, Customer.class).get(0);
        }
        cashPaymentVoucherCustomer.setCustomer(customer);
        return cashPaymentVoucherCustomer;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CashPaymentVoucherCustomer createUpdatedEntity(EntityManager em) {
        CashPaymentVoucherCustomer cashPaymentVoucherCustomer = new CashPaymentVoucherCustomer()
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
        cashPaymentVoucherCustomer.setLocation(location);
        // Add required entity
        TransactionType transactionType;
        if (TestUtil.findAll(em, TransactionType.class).isEmpty()) {
            transactionType = TransactionTypeResourceIT.createUpdatedEntity(em);
            em.persist(transactionType);
            em.flush();
        } else {
            transactionType = TestUtil.findAll(em, TransactionType.class).get(0);
        }
        cashPaymentVoucherCustomer.setTransactionType(transactionType);
        // Add required entity
        Customer customer;
        if (TestUtil.findAll(em, Customer.class).isEmpty()) {
            customer = CustomerResourceIT.createUpdatedEntity(em);
            em.persist(customer);
            em.flush();
        } else {
            customer = TestUtil.findAll(em, Customer.class).get(0);
        }
        cashPaymentVoucherCustomer.setCustomer(customer);
        return cashPaymentVoucherCustomer;
    }

    @BeforeEach
    public void initTest() {
        cashPaymentVoucherCustomer = createEntity(em);
    }

    @Test
    @Transactional
    public void createCashPaymentVoucherCustomer() throws Exception {
        int databaseSizeBeforeCreate = cashPaymentVoucherCustomerRepository.findAll().size();

        // Create the CashPaymentVoucherCustomer
        restCashPaymentVoucherCustomerMockMvc.perform(post("/api/cash-payment-voucher-customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashPaymentVoucherCustomer)))
            .andExpect(status().isCreated());

        // Validate the CashPaymentVoucherCustomer in the database
        List<CashPaymentVoucherCustomer> cashPaymentVoucherCustomerList = cashPaymentVoucherCustomerRepository.findAll();
        assertThat(cashPaymentVoucherCustomerList).hasSize(databaseSizeBeforeCreate + 1);
        CashPaymentVoucherCustomer testCashPaymentVoucherCustomer = cashPaymentVoucherCustomerList.get(cashPaymentVoucherCustomerList.size() - 1);
        assertThat(testCashPaymentVoucherCustomer.getTransactionNumber()).isEqualTo(DEFAULT_TRANSACTION_NUMBER);
        assertThat(testCashPaymentVoucherCustomer.getTransactionDate()).isEqualTo(DEFAULT_TRANSACTION_DATE);
        assertThat(testCashPaymentVoucherCustomer.getTransactionDescription()).isEqualTo(DEFAULT_TRANSACTION_DESCRIPTION);
        assertThat(testCashPaymentVoucherCustomer.getTransactionAmount()).isEqualTo(DEFAULT_TRANSACTION_AMOUNT);
    }

    @Test
    @Transactional
    public void createCashPaymentVoucherCustomerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cashPaymentVoucherCustomerRepository.findAll().size();

        // Create the CashPaymentVoucherCustomer with an existing ID
        cashPaymentVoucherCustomer.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCashPaymentVoucherCustomerMockMvc.perform(post("/api/cash-payment-voucher-customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashPaymentVoucherCustomer)))
            .andExpect(status().isBadRequest());

        // Validate the CashPaymentVoucherCustomer in the database
        List<CashPaymentVoucherCustomer> cashPaymentVoucherCustomerList = cashPaymentVoucherCustomerRepository.findAll();
        assertThat(cashPaymentVoucherCustomerList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTransactionNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = cashPaymentVoucherCustomerRepository.findAll().size();
        // set the field null
        cashPaymentVoucherCustomer.setTransactionNumber(null);

        // Create the CashPaymentVoucherCustomer, which fails.

        restCashPaymentVoucherCustomerMockMvc.perform(post("/api/cash-payment-voucher-customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashPaymentVoucherCustomer)))
            .andExpect(status().isBadRequest());

        List<CashPaymentVoucherCustomer> cashPaymentVoucherCustomerList = cashPaymentVoucherCustomerRepository.findAll();
        assertThat(cashPaymentVoucherCustomerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTransactionDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = cashPaymentVoucherCustomerRepository.findAll().size();
        // set the field null
        cashPaymentVoucherCustomer.setTransactionDate(null);

        // Create the CashPaymentVoucherCustomer, which fails.

        restCashPaymentVoucherCustomerMockMvc.perform(post("/api/cash-payment-voucher-customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashPaymentVoucherCustomer)))
            .andExpect(status().isBadRequest());

        List<CashPaymentVoucherCustomer> cashPaymentVoucherCustomerList = cashPaymentVoucherCustomerRepository.findAll();
        assertThat(cashPaymentVoucherCustomerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTransactionDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = cashPaymentVoucherCustomerRepository.findAll().size();
        // set the field null
        cashPaymentVoucherCustomer.setTransactionDescription(null);

        // Create the CashPaymentVoucherCustomer, which fails.

        restCashPaymentVoucherCustomerMockMvc.perform(post("/api/cash-payment-voucher-customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashPaymentVoucherCustomer)))
            .andExpect(status().isBadRequest());

        List<CashPaymentVoucherCustomer> cashPaymentVoucherCustomerList = cashPaymentVoucherCustomerRepository.findAll();
        assertThat(cashPaymentVoucherCustomerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTransactionAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = cashPaymentVoucherCustomerRepository.findAll().size();
        // set the field null
        cashPaymentVoucherCustomer.setTransactionAmount(null);

        // Create the CashPaymentVoucherCustomer, which fails.

        restCashPaymentVoucherCustomerMockMvc.perform(post("/api/cash-payment-voucher-customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashPaymentVoucherCustomer)))
            .andExpect(status().isBadRequest());

        List<CashPaymentVoucherCustomer> cashPaymentVoucherCustomerList = cashPaymentVoucherCustomerRepository.findAll();
        assertThat(cashPaymentVoucherCustomerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherCustomers() throws Exception {
        // Initialize the database
        cashPaymentVoucherCustomerRepository.saveAndFlush(cashPaymentVoucherCustomer);

        // Get all the cashPaymentVoucherCustomerList
        restCashPaymentVoucherCustomerMockMvc.perform(get("/api/cash-payment-voucher-customers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cashPaymentVoucherCustomer.getId().intValue())))
            .andExpect(jsonPath("$.[*].transactionNumber").value(hasItem(DEFAULT_TRANSACTION_NUMBER)))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].transactionDescription").value(hasItem(DEFAULT_TRANSACTION_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].transactionAmount").value(hasItem(DEFAULT_TRANSACTION_AMOUNT.intValue())));
    }
    
    @Test
    @Transactional
    public void getCashPaymentVoucherCustomer() throws Exception {
        // Initialize the database
        cashPaymentVoucherCustomerRepository.saveAndFlush(cashPaymentVoucherCustomer);

        // Get the cashPaymentVoucherCustomer
        restCashPaymentVoucherCustomerMockMvc.perform(get("/api/cash-payment-voucher-customers/{id}", cashPaymentVoucherCustomer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cashPaymentVoucherCustomer.getId().intValue()))
            .andExpect(jsonPath("$.transactionNumber").value(DEFAULT_TRANSACTION_NUMBER))
            .andExpect(jsonPath("$.transactionDate").value(DEFAULT_TRANSACTION_DATE.toString()))
            .andExpect(jsonPath("$.transactionDescription").value(DEFAULT_TRANSACTION_DESCRIPTION))
            .andExpect(jsonPath("$.transactionAmount").value(DEFAULT_TRANSACTION_AMOUNT.intValue()));
    }


    @Test
    @Transactional
    public void getCashPaymentVoucherCustomersByIdFiltering() throws Exception {
        // Initialize the database
        cashPaymentVoucherCustomerRepository.saveAndFlush(cashPaymentVoucherCustomer);

        Long id = cashPaymentVoucherCustomer.getId();

        defaultCashPaymentVoucherCustomerShouldBeFound("id.equals=" + id);
        defaultCashPaymentVoucherCustomerShouldNotBeFound("id.notEquals=" + id);

        defaultCashPaymentVoucherCustomerShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCashPaymentVoucherCustomerShouldNotBeFound("id.greaterThan=" + id);

        defaultCashPaymentVoucherCustomerShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCashPaymentVoucherCustomerShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllCashPaymentVoucherCustomersByTransactionNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        cashPaymentVoucherCustomerRepository.saveAndFlush(cashPaymentVoucherCustomer);

        // Get all the cashPaymentVoucherCustomerList where transactionNumber equals to DEFAULT_TRANSACTION_NUMBER
        defaultCashPaymentVoucherCustomerShouldBeFound("transactionNumber.equals=" + DEFAULT_TRANSACTION_NUMBER);

        // Get all the cashPaymentVoucherCustomerList where transactionNumber equals to UPDATED_TRANSACTION_NUMBER
        defaultCashPaymentVoucherCustomerShouldNotBeFound("transactionNumber.equals=" + UPDATED_TRANSACTION_NUMBER);
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherCustomersByTransactionNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cashPaymentVoucherCustomerRepository.saveAndFlush(cashPaymentVoucherCustomer);

        // Get all the cashPaymentVoucherCustomerList where transactionNumber not equals to DEFAULT_TRANSACTION_NUMBER
        defaultCashPaymentVoucherCustomerShouldNotBeFound("transactionNumber.notEquals=" + DEFAULT_TRANSACTION_NUMBER);

        // Get all the cashPaymentVoucherCustomerList where transactionNumber not equals to UPDATED_TRANSACTION_NUMBER
        defaultCashPaymentVoucherCustomerShouldBeFound("transactionNumber.notEquals=" + UPDATED_TRANSACTION_NUMBER);
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherCustomersByTransactionNumberIsInShouldWork() throws Exception {
        // Initialize the database
        cashPaymentVoucherCustomerRepository.saveAndFlush(cashPaymentVoucherCustomer);

        // Get all the cashPaymentVoucherCustomerList where transactionNumber in DEFAULT_TRANSACTION_NUMBER or UPDATED_TRANSACTION_NUMBER
        defaultCashPaymentVoucherCustomerShouldBeFound("transactionNumber.in=" + DEFAULT_TRANSACTION_NUMBER + "," + UPDATED_TRANSACTION_NUMBER);

        // Get all the cashPaymentVoucherCustomerList where transactionNumber equals to UPDATED_TRANSACTION_NUMBER
        defaultCashPaymentVoucherCustomerShouldNotBeFound("transactionNumber.in=" + UPDATED_TRANSACTION_NUMBER);
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherCustomersByTransactionNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        cashPaymentVoucherCustomerRepository.saveAndFlush(cashPaymentVoucherCustomer);

        // Get all the cashPaymentVoucherCustomerList where transactionNumber is not null
        defaultCashPaymentVoucherCustomerShouldBeFound("transactionNumber.specified=true");

        // Get all the cashPaymentVoucherCustomerList where transactionNumber is null
        defaultCashPaymentVoucherCustomerShouldNotBeFound("transactionNumber.specified=false");
    }
                @Test
    @Transactional
    public void getAllCashPaymentVoucherCustomersByTransactionNumberContainsSomething() throws Exception {
        // Initialize the database
        cashPaymentVoucherCustomerRepository.saveAndFlush(cashPaymentVoucherCustomer);

        // Get all the cashPaymentVoucherCustomerList where transactionNumber contains DEFAULT_TRANSACTION_NUMBER
        defaultCashPaymentVoucherCustomerShouldBeFound("transactionNumber.contains=" + DEFAULT_TRANSACTION_NUMBER);

        // Get all the cashPaymentVoucherCustomerList where transactionNumber contains UPDATED_TRANSACTION_NUMBER
        defaultCashPaymentVoucherCustomerShouldNotBeFound("transactionNumber.contains=" + UPDATED_TRANSACTION_NUMBER);
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherCustomersByTransactionNumberNotContainsSomething() throws Exception {
        // Initialize the database
        cashPaymentVoucherCustomerRepository.saveAndFlush(cashPaymentVoucherCustomer);

        // Get all the cashPaymentVoucherCustomerList where transactionNumber does not contain DEFAULT_TRANSACTION_NUMBER
        defaultCashPaymentVoucherCustomerShouldNotBeFound("transactionNumber.doesNotContain=" + DEFAULT_TRANSACTION_NUMBER);

        // Get all the cashPaymentVoucherCustomerList where transactionNumber does not contain UPDATED_TRANSACTION_NUMBER
        defaultCashPaymentVoucherCustomerShouldBeFound("transactionNumber.doesNotContain=" + UPDATED_TRANSACTION_NUMBER);
    }


    @Test
    @Transactional
    public void getAllCashPaymentVoucherCustomersByTransactionDateIsEqualToSomething() throws Exception {
        // Initialize the database
        cashPaymentVoucherCustomerRepository.saveAndFlush(cashPaymentVoucherCustomer);

        // Get all the cashPaymentVoucherCustomerList where transactionDate equals to DEFAULT_TRANSACTION_DATE
        defaultCashPaymentVoucherCustomerShouldBeFound("transactionDate.equals=" + DEFAULT_TRANSACTION_DATE);

        // Get all the cashPaymentVoucherCustomerList where transactionDate equals to UPDATED_TRANSACTION_DATE
        defaultCashPaymentVoucherCustomerShouldNotBeFound("transactionDate.equals=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherCustomersByTransactionDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cashPaymentVoucherCustomerRepository.saveAndFlush(cashPaymentVoucherCustomer);

        // Get all the cashPaymentVoucherCustomerList where transactionDate not equals to DEFAULT_TRANSACTION_DATE
        defaultCashPaymentVoucherCustomerShouldNotBeFound("transactionDate.notEquals=" + DEFAULT_TRANSACTION_DATE);

        // Get all the cashPaymentVoucherCustomerList where transactionDate not equals to UPDATED_TRANSACTION_DATE
        defaultCashPaymentVoucherCustomerShouldBeFound("transactionDate.notEquals=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherCustomersByTransactionDateIsInShouldWork() throws Exception {
        // Initialize the database
        cashPaymentVoucherCustomerRepository.saveAndFlush(cashPaymentVoucherCustomer);

        // Get all the cashPaymentVoucherCustomerList where transactionDate in DEFAULT_TRANSACTION_DATE or UPDATED_TRANSACTION_DATE
        defaultCashPaymentVoucherCustomerShouldBeFound("transactionDate.in=" + DEFAULT_TRANSACTION_DATE + "," + UPDATED_TRANSACTION_DATE);

        // Get all the cashPaymentVoucherCustomerList where transactionDate equals to UPDATED_TRANSACTION_DATE
        defaultCashPaymentVoucherCustomerShouldNotBeFound("transactionDate.in=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherCustomersByTransactionDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        cashPaymentVoucherCustomerRepository.saveAndFlush(cashPaymentVoucherCustomer);

        // Get all the cashPaymentVoucherCustomerList where transactionDate is not null
        defaultCashPaymentVoucherCustomerShouldBeFound("transactionDate.specified=true");

        // Get all the cashPaymentVoucherCustomerList where transactionDate is null
        defaultCashPaymentVoucherCustomerShouldNotBeFound("transactionDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherCustomersByTransactionDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cashPaymentVoucherCustomerRepository.saveAndFlush(cashPaymentVoucherCustomer);

        // Get all the cashPaymentVoucherCustomerList where transactionDate is greater than or equal to DEFAULT_TRANSACTION_DATE
        defaultCashPaymentVoucherCustomerShouldBeFound("transactionDate.greaterThanOrEqual=" + DEFAULT_TRANSACTION_DATE);

        // Get all the cashPaymentVoucherCustomerList where transactionDate is greater than or equal to UPDATED_TRANSACTION_DATE
        defaultCashPaymentVoucherCustomerShouldNotBeFound("transactionDate.greaterThanOrEqual=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherCustomersByTransactionDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cashPaymentVoucherCustomerRepository.saveAndFlush(cashPaymentVoucherCustomer);

        // Get all the cashPaymentVoucherCustomerList where transactionDate is less than or equal to DEFAULT_TRANSACTION_DATE
        defaultCashPaymentVoucherCustomerShouldBeFound("transactionDate.lessThanOrEqual=" + DEFAULT_TRANSACTION_DATE);

        // Get all the cashPaymentVoucherCustomerList where transactionDate is less than or equal to SMALLER_TRANSACTION_DATE
        defaultCashPaymentVoucherCustomerShouldNotBeFound("transactionDate.lessThanOrEqual=" + SMALLER_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherCustomersByTransactionDateIsLessThanSomething() throws Exception {
        // Initialize the database
        cashPaymentVoucherCustomerRepository.saveAndFlush(cashPaymentVoucherCustomer);

        // Get all the cashPaymentVoucherCustomerList where transactionDate is less than DEFAULT_TRANSACTION_DATE
        defaultCashPaymentVoucherCustomerShouldNotBeFound("transactionDate.lessThan=" + DEFAULT_TRANSACTION_DATE);

        // Get all the cashPaymentVoucherCustomerList where transactionDate is less than UPDATED_TRANSACTION_DATE
        defaultCashPaymentVoucherCustomerShouldBeFound("transactionDate.lessThan=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherCustomersByTransactionDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cashPaymentVoucherCustomerRepository.saveAndFlush(cashPaymentVoucherCustomer);

        // Get all the cashPaymentVoucherCustomerList where transactionDate is greater than DEFAULT_TRANSACTION_DATE
        defaultCashPaymentVoucherCustomerShouldNotBeFound("transactionDate.greaterThan=" + DEFAULT_TRANSACTION_DATE);

        // Get all the cashPaymentVoucherCustomerList where transactionDate is greater than SMALLER_TRANSACTION_DATE
        defaultCashPaymentVoucherCustomerShouldBeFound("transactionDate.greaterThan=" + SMALLER_TRANSACTION_DATE);
    }


    @Test
    @Transactional
    public void getAllCashPaymentVoucherCustomersByTransactionDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        cashPaymentVoucherCustomerRepository.saveAndFlush(cashPaymentVoucherCustomer);

        // Get all the cashPaymentVoucherCustomerList where transactionDescription equals to DEFAULT_TRANSACTION_DESCRIPTION
        defaultCashPaymentVoucherCustomerShouldBeFound("transactionDescription.equals=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the cashPaymentVoucherCustomerList where transactionDescription equals to UPDATED_TRANSACTION_DESCRIPTION
        defaultCashPaymentVoucherCustomerShouldNotBeFound("transactionDescription.equals=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherCustomersByTransactionDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cashPaymentVoucherCustomerRepository.saveAndFlush(cashPaymentVoucherCustomer);

        // Get all the cashPaymentVoucherCustomerList where transactionDescription not equals to DEFAULT_TRANSACTION_DESCRIPTION
        defaultCashPaymentVoucherCustomerShouldNotBeFound("transactionDescription.notEquals=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the cashPaymentVoucherCustomerList where transactionDescription not equals to UPDATED_TRANSACTION_DESCRIPTION
        defaultCashPaymentVoucherCustomerShouldBeFound("transactionDescription.notEquals=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherCustomersByTransactionDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        cashPaymentVoucherCustomerRepository.saveAndFlush(cashPaymentVoucherCustomer);

        // Get all the cashPaymentVoucherCustomerList where transactionDescription in DEFAULT_TRANSACTION_DESCRIPTION or UPDATED_TRANSACTION_DESCRIPTION
        defaultCashPaymentVoucherCustomerShouldBeFound("transactionDescription.in=" + DEFAULT_TRANSACTION_DESCRIPTION + "," + UPDATED_TRANSACTION_DESCRIPTION);

        // Get all the cashPaymentVoucherCustomerList where transactionDescription equals to UPDATED_TRANSACTION_DESCRIPTION
        defaultCashPaymentVoucherCustomerShouldNotBeFound("transactionDescription.in=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherCustomersByTransactionDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        cashPaymentVoucherCustomerRepository.saveAndFlush(cashPaymentVoucherCustomer);

        // Get all the cashPaymentVoucherCustomerList where transactionDescription is not null
        defaultCashPaymentVoucherCustomerShouldBeFound("transactionDescription.specified=true");

        // Get all the cashPaymentVoucherCustomerList where transactionDescription is null
        defaultCashPaymentVoucherCustomerShouldNotBeFound("transactionDescription.specified=false");
    }
                @Test
    @Transactional
    public void getAllCashPaymentVoucherCustomersByTransactionDescriptionContainsSomething() throws Exception {
        // Initialize the database
        cashPaymentVoucherCustomerRepository.saveAndFlush(cashPaymentVoucherCustomer);

        // Get all the cashPaymentVoucherCustomerList where transactionDescription contains DEFAULT_TRANSACTION_DESCRIPTION
        defaultCashPaymentVoucherCustomerShouldBeFound("transactionDescription.contains=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the cashPaymentVoucherCustomerList where transactionDescription contains UPDATED_TRANSACTION_DESCRIPTION
        defaultCashPaymentVoucherCustomerShouldNotBeFound("transactionDescription.contains=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherCustomersByTransactionDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        cashPaymentVoucherCustomerRepository.saveAndFlush(cashPaymentVoucherCustomer);

        // Get all the cashPaymentVoucherCustomerList where transactionDescription does not contain DEFAULT_TRANSACTION_DESCRIPTION
        defaultCashPaymentVoucherCustomerShouldNotBeFound("transactionDescription.doesNotContain=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the cashPaymentVoucherCustomerList where transactionDescription does not contain UPDATED_TRANSACTION_DESCRIPTION
        defaultCashPaymentVoucherCustomerShouldBeFound("transactionDescription.doesNotContain=" + UPDATED_TRANSACTION_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllCashPaymentVoucherCustomersByTransactionAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        cashPaymentVoucherCustomerRepository.saveAndFlush(cashPaymentVoucherCustomer);

        // Get all the cashPaymentVoucherCustomerList where transactionAmount equals to DEFAULT_TRANSACTION_AMOUNT
        defaultCashPaymentVoucherCustomerShouldBeFound("transactionAmount.equals=" + DEFAULT_TRANSACTION_AMOUNT);

        // Get all the cashPaymentVoucherCustomerList where transactionAmount equals to UPDATED_TRANSACTION_AMOUNT
        defaultCashPaymentVoucherCustomerShouldNotBeFound("transactionAmount.equals=" + UPDATED_TRANSACTION_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherCustomersByTransactionAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cashPaymentVoucherCustomerRepository.saveAndFlush(cashPaymentVoucherCustomer);

        // Get all the cashPaymentVoucherCustomerList where transactionAmount not equals to DEFAULT_TRANSACTION_AMOUNT
        defaultCashPaymentVoucherCustomerShouldNotBeFound("transactionAmount.notEquals=" + DEFAULT_TRANSACTION_AMOUNT);

        // Get all the cashPaymentVoucherCustomerList where transactionAmount not equals to UPDATED_TRANSACTION_AMOUNT
        defaultCashPaymentVoucherCustomerShouldBeFound("transactionAmount.notEquals=" + UPDATED_TRANSACTION_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherCustomersByTransactionAmountIsInShouldWork() throws Exception {
        // Initialize the database
        cashPaymentVoucherCustomerRepository.saveAndFlush(cashPaymentVoucherCustomer);

        // Get all the cashPaymentVoucherCustomerList where transactionAmount in DEFAULT_TRANSACTION_AMOUNT or UPDATED_TRANSACTION_AMOUNT
        defaultCashPaymentVoucherCustomerShouldBeFound("transactionAmount.in=" + DEFAULT_TRANSACTION_AMOUNT + "," + UPDATED_TRANSACTION_AMOUNT);

        // Get all the cashPaymentVoucherCustomerList where transactionAmount equals to UPDATED_TRANSACTION_AMOUNT
        defaultCashPaymentVoucherCustomerShouldNotBeFound("transactionAmount.in=" + UPDATED_TRANSACTION_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherCustomersByTransactionAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        cashPaymentVoucherCustomerRepository.saveAndFlush(cashPaymentVoucherCustomer);

        // Get all the cashPaymentVoucherCustomerList where transactionAmount is not null
        defaultCashPaymentVoucherCustomerShouldBeFound("transactionAmount.specified=true");

        // Get all the cashPaymentVoucherCustomerList where transactionAmount is null
        defaultCashPaymentVoucherCustomerShouldNotBeFound("transactionAmount.specified=false");
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherCustomersByTransactionAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cashPaymentVoucherCustomerRepository.saveAndFlush(cashPaymentVoucherCustomer);

        // Get all the cashPaymentVoucherCustomerList where transactionAmount is greater than or equal to DEFAULT_TRANSACTION_AMOUNT
        defaultCashPaymentVoucherCustomerShouldBeFound("transactionAmount.greaterThanOrEqual=" + DEFAULT_TRANSACTION_AMOUNT);

        // Get all the cashPaymentVoucherCustomerList where transactionAmount is greater than or equal to UPDATED_TRANSACTION_AMOUNT
        defaultCashPaymentVoucherCustomerShouldNotBeFound("transactionAmount.greaterThanOrEqual=" + UPDATED_TRANSACTION_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherCustomersByTransactionAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cashPaymentVoucherCustomerRepository.saveAndFlush(cashPaymentVoucherCustomer);

        // Get all the cashPaymentVoucherCustomerList where transactionAmount is less than or equal to DEFAULT_TRANSACTION_AMOUNT
        defaultCashPaymentVoucherCustomerShouldBeFound("transactionAmount.lessThanOrEqual=" + DEFAULT_TRANSACTION_AMOUNT);

        // Get all the cashPaymentVoucherCustomerList where transactionAmount is less than or equal to SMALLER_TRANSACTION_AMOUNT
        defaultCashPaymentVoucherCustomerShouldNotBeFound("transactionAmount.lessThanOrEqual=" + SMALLER_TRANSACTION_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherCustomersByTransactionAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        cashPaymentVoucherCustomerRepository.saveAndFlush(cashPaymentVoucherCustomer);

        // Get all the cashPaymentVoucherCustomerList where transactionAmount is less than DEFAULT_TRANSACTION_AMOUNT
        defaultCashPaymentVoucherCustomerShouldNotBeFound("transactionAmount.lessThan=" + DEFAULT_TRANSACTION_AMOUNT);

        // Get all the cashPaymentVoucherCustomerList where transactionAmount is less than UPDATED_TRANSACTION_AMOUNT
        defaultCashPaymentVoucherCustomerShouldBeFound("transactionAmount.lessThan=" + UPDATED_TRANSACTION_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherCustomersByTransactionAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cashPaymentVoucherCustomerRepository.saveAndFlush(cashPaymentVoucherCustomer);

        // Get all the cashPaymentVoucherCustomerList where transactionAmount is greater than DEFAULT_TRANSACTION_AMOUNT
        defaultCashPaymentVoucherCustomerShouldNotBeFound("transactionAmount.greaterThan=" + DEFAULT_TRANSACTION_AMOUNT);

        // Get all the cashPaymentVoucherCustomerList where transactionAmount is greater than SMALLER_TRANSACTION_AMOUNT
        defaultCashPaymentVoucherCustomerShouldBeFound("transactionAmount.greaterThan=" + SMALLER_TRANSACTION_AMOUNT);
    }


    @Test
    @Transactional
    public void getAllCashPaymentVoucherCustomersByHistoryIsEqualToSomething() throws Exception {
        // Initialize the database
        cashPaymentVoucherCustomerRepository.saveAndFlush(cashPaymentVoucherCustomer);
        DocumentHistory history = DocumentHistoryResourceIT.createEntity(em);
        em.persist(history);
        em.flush();
        cashPaymentVoucherCustomer.setHistory(history);
        cashPaymentVoucherCustomerRepository.saveAndFlush(cashPaymentVoucherCustomer);
        Long historyId = history.getId();

        // Get all the cashPaymentVoucherCustomerList where history equals to historyId
        defaultCashPaymentVoucherCustomerShouldBeFound("historyId.equals=" + historyId);

        // Get all the cashPaymentVoucherCustomerList where history equals to historyId + 1
        defaultCashPaymentVoucherCustomerShouldNotBeFound("historyId.equals=" + (historyId + 1));
    }


    @Test
    @Transactional
    public void getAllCashPaymentVoucherCustomersByLocationIsEqualToSomething() throws Exception {
        // Get already existing entity
        Location location = cashPaymentVoucherCustomer.getLocation();
        cashPaymentVoucherCustomerRepository.saveAndFlush(cashPaymentVoucherCustomer);
        Long locationId = location.getId();

        // Get all the cashPaymentVoucherCustomerList where location equals to locationId
        defaultCashPaymentVoucherCustomerShouldBeFound("locationId.equals=" + locationId);

        // Get all the cashPaymentVoucherCustomerList where location equals to locationId + 1
        defaultCashPaymentVoucherCustomerShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }


    @Test
    @Transactional
    public void getAllCashPaymentVoucherCustomersByTransactionTypeIsEqualToSomething() throws Exception {
        // Get already existing entity
        TransactionType transactionType = cashPaymentVoucherCustomer.getTransactionType();
        cashPaymentVoucherCustomerRepository.saveAndFlush(cashPaymentVoucherCustomer);
        Long transactionTypeId = transactionType.getId();

        // Get all the cashPaymentVoucherCustomerList where transactionType equals to transactionTypeId
        defaultCashPaymentVoucherCustomerShouldBeFound("transactionTypeId.equals=" + transactionTypeId);

        // Get all the cashPaymentVoucherCustomerList where transactionType equals to transactionTypeId + 1
        defaultCashPaymentVoucherCustomerShouldNotBeFound("transactionTypeId.equals=" + (transactionTypeId + 1));
    }


    @Test
    @Transactional
    public void getAllCashPaymentVoucherCustomersByCustomerIsEqualToSomething() throws Exception {
        // Get already existing entity
        Customer customer = cashPaymentVoucherCustomer.getCustomer();
        cashPaymentVoucherCustomerRepository.saveAndFlush(cashPaymentVoucherCustomer);
        Long customerId = customer.getId();

        // Get all the cashPaymentVoucherCustomerList where customer equals to customerId
        defaultCashPaymentVoucherCustomerShouldBeFound("customerId.equals=" + customerId);

        // Get all the cashPaymentVoucherCustomerList where customer equals to customerId + 1
        defaultCashPaymentVoucherCustomerShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCashPaymentVoucherCustomerShouldBeFound(String filter) throws Exception {
        restCashPaymentVoucherCustomerMockMvc.perform(get("/api/cash-payment-voucher-customers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cashPaymentVoucherCustomer.getId().intValue())))
            .andExpect(jsonPath("$.[*].transactionNumber").value(hasItem(DEFAULT_TRANSACTION_NUMBER)))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].transactionDescription").value(hasItem(DEFAULT_TRANSACTION_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].transactionAmount").value(hasItem(DEFAULT_TRANSACTION_AMOUNT.intValue())));

        // Check, that the count call also returns 1
        restCashPaymentVoucherCustomerMockMvc.perform(get("/api/cash-payment-voucher-customers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCashPaymentVoucherCustomerShouldNotBeFound(String filter) throws Exception {
        restCashPaymentVoucherCustomerMockMvc.perform(get("/api/cash-payment-voucher-customers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCashPaymentVoucherCustomerMockMvc.perform(get("/api/cash-payment-voucher-customers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingCashPaymentVoucherCustomer() throws Exception {
        // Get the cashPaymentVoucherCustomer
        restCashPaymentVoucherCustomerMockMvc.perform(get("/api/cash-payment-voucher-customers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCashPaymentVoucherCustomer() throws Exception {
        // Initialize the database
        cashPaymentVoucherCustomerService.save(cashPaymentVoucherCustomer);

        int databaseSizeBeforeUpdate = cashPaymentVoucherCustomerRepository.findAll().size();

        // Update the cashPaymentVoucherCustomer
        CashPaymentVoucherCustomer updatedCashPaymentVoucherCustomer = cashPaymentVoucherCustomerRepository.findById(cashPaymentVoucherCustomer.getId()).get();
        // Disconnect from session so that the updates on updatedCashPaymentVoucherCustomer are not directly saved in db
        em.detach(updatedCashPaymentVoucherCustomer);
        updatedCashPaymentVoucherCustomer
            .transactionNumber(UPDATED_TRANSACTION_NUMBER)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .transactionDescription(UPDATED_TRANSACTION_DESCRIPTION)
            .transactionAmount(UPDATED_TRANSACTION_AMOUNT);

        restCashPaymentVoucherCustomerMockMvc.perform(put("/api/cash-payment-voucher-customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCashPaymentVoucherCustomer)))
            .andExpect(status().isOk());

        // Validate the CashPaymentVoucherCustomer in the database
        List<CashPaymentVoucherCustomer> cashPaymentVoucherCustomerList = cashPaymentVoucherCustomerRepository.findAll();
        assertThat(cashPaymentVoucherCustomerList).hasSize(databaseSizeBeforeUpdate);
        CashPaymentVoucherCustomer testCashPaymentVoucherCustomer = cashPaymentVoucherCustomerList.get(cashPaymentVoucherCustomerList.size() - 1);
        assertThat(testCashPaymentVoucherCustomer.getTransactionNumber()).isEqualTo(UPDATED_TRANSACTION_NUMBER);
        assertThat(testCashPaymentVoucherCustomer.getTransactionDate()).isEqualTo(UPDATED_TRANSACTION_DATE);
        assertThat(testCashPaymentVoucherCustomer.getTransactionDescription()).isEqualTo(UPDATED_TRANSACTION_DESCRIPTION);
        assertThat(testCashPaymentVoucherCustomer.getTransactionAmount()).isEqualTo(UPDATED_TRANSACTION_AMOUNT);
    }

    @Test
    @Transactional
    public void updateNonExistingCashPaymentVoucherCustomer() throws Exception {
        int databaseSizeBeforeUpdate = cashPaymentVoucherCustomerRepository.findAll().size();

        // Create the CashPaymentVoucherCustomer

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCashPaymentVoucherCustomerMockMvc.perform(put("/api/cash-payment-voucher-customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashPaymentVoucherCustomer)))
            .andExpect(status().isBadRequest());

        // Validate the CashPaymentVoucherCustomer in the database
        List<CashPaymentVoucherCustomer> cashPaymentVoucherCustomerList = cashPaymentVoucherCustomerRepository.findAll();
        assertThat(cashPaymentVoucherCustomerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCashPaymentVoucherCustomer() throws Exception {
        // Initialize the database
        cashPaymentVoucherCustomerService.save(cashPaymentVoucherCustomer);

        int databaseSizeBeforeDelete = cashPaymentVoucherCustomerRepository.findAll().size();

        // Delete the cashPaymentVoucherCustomer
        restCashPaymentVoucherCustomerMockMvc.perform(delete("/api/cash-payment-voucher-customers/{id}", cashPaymentVoucherCustomer.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CashPaymentVoucherCustomer> cashPaymentVoucherCustomerList = cashPaymentVoucherCustomerRepository.findAll();
        assertThat(cashPaymentVoucherCustomerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

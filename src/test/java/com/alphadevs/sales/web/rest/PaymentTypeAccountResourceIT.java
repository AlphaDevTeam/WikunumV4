package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.WikunumApp;
import com.alphadevs.sales.domain.PaymentTypeAccount;
import com.alphadevs.sales.domain.Location;
import com.alphadevs.sales.domain.TransactionType;
import com.alphadevs.sales.domain.PaymentTypes;
import com.alphadevs.sales.repository.PaymentTypeAccountRepository;
import com.alphadevs.sales.service.PaymentTypeAccountService;
import com.alphadevs.sales.web.rest.errors.ExceptionTranslator;
import com.alphadevs.sales.service.dto.PaymentTypeAccountCriteria;
import com.alphadevs.sales.service.PaymentTypeAccountQueryService;

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
 * Integration tests for the {@link PaymentTypeAccountResource} REST controller.
 */
@SpringBootTest(classes = WikunumApp.class)
public class PaymentTypeAccountResourceIT {

    private static final LocalDate DEFAULT_TRANSACTION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TRANSACTION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_TRANSACTION_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_TRANSACTION_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_TRANSACTION_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_TRANSACTION_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_TRANSACTION_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_TRANSACTION_AMOUNT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_TRANSACTION_BALANCE = new BigDecimal(1);
    private static final BigDecimal UPDATED_TRANSACTION_BALANCE = new BigDecimal(2);
    private static final BigDecimal SMALLER_TRANSACTION_BALANCE = new BigDecimal(1 - 1);

    @Autowired
    private PaymentTypeAccountRepository paymentTypeAccountRepository;

    @Autowired
    private PaymentTypeAccountService paymentTypeAccountService;

    @Autowired
    private PaymentTypeAccountQueryService paymentTypeAccountQueryService;

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

    private MockMvc restPaymentTypeAccountMockMvc;

    private PaymentTypeAccount paymentTypeAccount;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PaymentTypeAccountResource paymentTypeAccountResource = new PaymentTypeAccountResource(paymentTypeAccountService, paymentTypeAccountQueryService);
        this.restPaymentTypeAccountMockMvc = MockMvcBuilders.standaloneSetup(paymentTypeAccountResource)
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
    public static PaymentTypeAccount createEntity(EntityManager em) {
        PaymentTypeAccount paymentTypeAccount = new PaymentTypeAccount()
            .transactionDate(DEFAULT_TRANSACTION_DATE)
            .transactionDescription(DEFAULT_TRANSACTION_DESCRIPTION)
            .transactionAmount(DEFAULT_TRANSACTION_AMOUNT)
            .transactionBalance(DEFAULT_TRANSACTION_BALANCE);
        // Add required entity
        Location location;
        if (TestUtil.findAll(em, Location.class).isEmpty()) {
            location = LocationResourceIT.createEntity(em);
            em.persist(location);
            em.flush();
        } else {
            location = TestUtil.findAll(em, Location.class).get(0);
        }
        paymentTypeAccount.setLocation(location);
        // Add required entity
        TransactionType transactionType;
        if (TestUtil.findAll(em, TransactionType.class).isEmpty()) {
            transactionType = TransactionTypeResourceIT.createEntity(em);
            em.persist(transactionType);
            em.flush();
        } else {
            transactionType = TestUtil.findAll(em, TransactionType.class).get(0);
        }
        paymentTypeAccount.setTransactionType(transactionType);
        // Add required entity
        PaymentTypes paymentTypes;
        if (TestUtil.findAll(em, PaymentTypes.class).isEmpty()) {
            paymentTypes = PaymentTypesResourceIT.createEntity(em);
            em.persist(paymentTypes);
            em.flush();
        } else {
            paymentTypes = TestUtil.findAll(em, PaymentTypes.class).get(0);
        }
        paymentTypeAccount.setPayType(paymentTypes);
        return paymentTypeAccount;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentTypeAccount createUpdatedEntity(EntityManager em) {
        PaymentTypeAccount paymentTypeAccount = new PaymentTypeAccount()
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .transactionDescription(UPDATED_TRANSACTION_DESCRIPTION)
            .transactionAmount(UPDATED_TRANSACTION_AMOUNT)
            .transactionBalance(UPDATED_TRANSACTION_BALANCE);
        // Add required entity
        Location location;
        if (TestUtil.findAll(em, Location.class).isEmpty()) {
            location = LocationResourceIT.createUpdatedEntity(em);
            em.persist(location);
            em.flush();
        } else {
            location = TestUtil.findAll(em, Location.class).get(0);
        }
        paymentTypeAccount.setLocation(location);
        // Add required entity
        TransactionType transactionType;
        if (TestUtil.findAll(em, TransactionType.class).isEmpty()) {
            transactionType = TransactionTypeResourceIT.createUpdatedEntity(em);
            em.persist(transactionType);
            em.flush();
        } else {
            transactionType = TestUtil.findAll(em, TransactionType.class).get(0);
        }
        paymentTypeAccount.setTransactionType(transactionType);
        // Add required entity
        PaymentTypes paymentTypes;
        if (TestUtil.findAll(em, PaymentTypes.class).isEmpty()) {
            paymentTypes = PaymentTypesResourceIT.createUpdatedEntity(em);
            em.persist(paymentTypes);
            em.flush();
        } else {
            paymentTypes = TestUtil.findAll(em, PaymentTypes.class).get(0);
        }
        paymentTypeAccount.setPayType(paymentTypes);
        return paymentTypeAccount;
    }

    @BeforeEach
    public void initTest() {
        paymentTypeAccount = createEntity(em);
    }

    @Test
    @Transactional
    public void createPaymentTypeAccount() throws Exception {
        int databaseSizeBeforeCreate = paymentTypeAccountRepository.findAll().size();

        // Create the PaymentTypeAccount
        restPaymentTypeAccountMockMvc.perform(post("/api/payment-type-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentTypeAccount)))
            .andExpect(status().isCreated());

        // Validate the PaymentTypeAccount in the database
        List<PaymentTypeAccount> paymentTypeAccountList = paymentTypeAccountRepository.findAll();
        assertThat(paymentTypeAccountList).hasSize(databaseSizeBeforeCreate + 1);
        PaymentTypeAccount testPaymentTypeAccount = paymentTypeAccountList.get(paymentTypeAccountList.size() - 1);
        assertThat(testPaymentTypeAccount.getTransactionDate()).isEqualTo(DEFAULT_TRANSACTION_DATE);
        assertThat(testPaymentTypeAccount.getTransactionDescription()).isEqualTo(DEFAULT_TRANSACTION_DESCRIPTION);
        assertThat(testPaymentTypeAccount.getTransactionAmount()).isEqualTo(DEFAULT_TRANSACTION_AMOUNT);
        assertThat(testPaymentTypeAccount.getTransactionBalance()).isEqualTo(DEFAULT_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void createPaymentTypeAccountWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = paymentTypeAccountRepository.findAll().size();

        // Create the PaymentTypeAccount with an existing ID
        paymentTypeAccount.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentTypeAccountMockMvc.perform(post("/api/payment-type-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentTypeAccount)))
            .andExpect(status().isBadRequest());

        // Validate the PaymentTypeAccount in the database
        List<PaymentTypeAccount> paymentTypeAccountList = paymentTypeAccountRepository.findAll();
        assertThat(paymentTypeAccountList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTransactionDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentTypeAccountRepository.findAll().size();
        // set the field null
        paymentTypeAccount.setTransactionDate(null);

        // Create the PaymentTypeAccount, which fails.

        restPaymentTypeAccountMockMvc.perform(post("/api/payment-type-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentTypeAccount)))
            .andExpect(status().isBadRequest());

        List<PaymentTypeAccount> paymentTypeAccountList = paymentTypeAccountRepository.findAll();
        assertThat(paymentTypeAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTransactionDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentTypeAccountRepository.findAll().size();
        // set the field null
        paymentTypeAccount.setTransactionDescription(null);

        // Create the PaymentTypeAccount, which fails.

        restPaymentTypeAccountMockMvc.perform(post("/api/payment-type-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentTypeAccount)))
            .andExpect(status().isBadRequest());

        List<PaymentTypeAccount> paymentTypeAccountList = paymentTypeAccountRepository.findAll();
        assertThat(paymentTypeAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTransactionAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentTypeAccountRepository.findAll().size();
        // set the field null
        paymentTypeAccount.setTransactionAmount(null);

        // Create the PaymentTypeAccount, which fails.

        restPaymentTypeAccountMockMvc.perform(post("/api/payment-type-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentTypeAccount)))
            .andExpect(status().isBadRequest());

        List<PaymentTypeAccount> paymentTypeAccountList = paymentTypeAccountRepository.findAll();
        assertThat(paymentTypeAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTransactionBalanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentTypeAccountRepository.findAll().size();
        // set the field null
        paymentTypeAccount.setTransactionBalance(null);

        // Create the PaymentTypeAccount, which fails.

        restPaymentTypeAccountMockMvc.perform(post("/api/payment-type-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentTypeAccount)))
            .andExpect(status().isBadRequest());

        List<PaymentTypeAccount> paymentTypeAccountList = paymentTypeAccountRepository.findAll();
        assertThat(paymentTypeAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPaymentTypeAccounts() throws Exception {
        // Initialize the database
        paymentTypeAccountRepository.saveAndFlush(paymentTypeAccount);

        // Get all the paymentTypeAccountList
        restPaymentTypeAccountMockMvc.perform(get("/api/payment-type-accounts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentTypeAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].transactionDescription").value(hasItem(DEFAULT_TRANSACTION_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].transactionAmount").value(hasItem(DEFAULT_TRANSACTION_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].transactionBalance").value(hasItem(DEFAULT_TRANSACTION_BALANCE.intValue())));
    }
    
    @Test
    @Transactional
    public void getPaymentTypeAccount() throws Exception {
        // Initialize the database
        paymentTypeAccountRepository.saveAndFlush(paymentTypeAccount);

        // Get the paymentTypeAccount
        restPaymentTypeAccountMockMvc.perform(get("/api/payment-type-accounts/{id}", paymentTypeAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(paymentTypeAccount.getId().intValue()))
            .andExpect(jsonPath("$.transactionDate").value(DEFAULT_TRANSACTION_DATE.toString()))
            .andExpect(jsonPath("$.transactionDescription").value(DEFAULT_TRANSACTION_DESCRIPTION))
            .andExpect(jsonPath("$.transactionAmount").value(DEFAULT_TRANSACTION_AMOUNT.intValue()))
            .andExpect(jsonPath("$.transactionBalance").value(DEFAULT_TRANSACTION_BALANCE.intValue()));
    }


    @Test
    @Transactional
    public void getPaymentTypeAccountsByIdFiltering() throws Exception {
        // Initialize the database
        paymentTypeAccountRepository.saveAndFlush(paymentTypeAccount);

        Long id = paymentTypeAccount.getId();

        defaultPaymentTypeAccountShouldBeFound("id.equals=" + id);
        defaultPaymentTypeAccountShouldNotBeFound("id.notEquals=" + id);

        defaultPaymentTypeAccountShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPaymentTypeAccountShouldNotBeFound("id.greaterThan=" + id);

        defaultPaymentTypeAccountShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPaymentTypeAccountShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllPaymentTypeAccountsByTransactionDateIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentTypeAccountRepository.saveAndFlush(paymentTypeAccount);

        // Get all the paymentTypeAccountList where transactionDate equals to DEFAULT_TRANSACTION_DATE
        defaultPaymentTypeAccountShouldBeFound("transactionDate.equals=" + DEFAULT_TRANSACTION_DATE);

        // Get all the paymentTypeAccountList where transactionDate equals to UPDATED_TRANSACTION_DATE
        defaultPaymentTypeAccountShouldNotBeFound("transactionDate.equals=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllPaymentTypeAccountsByTransactionDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentTypeAccountRepository.saveAndFlush(paymentTypeAccount);

        // Get all the paymentTypeAccountList where transactionDate not equals to DEFAULT_TRANSACTION_DATE
        defaultPaymentTypeAccountShouldNotBeFound("transactionDate.notEquals=" + DEFAULT_TRANSACTION_DATE);

        // Get all the paymentTypeAccountList where transactionDate not equals to UPDATED_TRANSACTION_DATE
        defaultPaymentTypeAccountShouldBeFound("transactionDate.notEquals=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllPaymentTypeAccountsByTransactionDateIsInShouldWork() throws Exception {
        // Initialize the database
        paymentTypeAccountRepository.saveAndFlush(paymentTypeAccount);

        // Get all the paymentTypeAccountList where transactionDate in DEFAULT_TRANSACTION_DATE or UPDATED_TRANSACTION_DATE
        defaultPaymentTypeAccountShouldBeFound("transactionDate.in=" + DEFAULT_TRANSACTION_DATE + "," + UPDATED_TRANSACTION_DATE);

        // Get all the paymentTypeAccountList where transactionDate equals to UPDATED_TRANSACTION_DATE
        defaultPaymentTypeAccountShouldNotBeFound("transactionDate.in=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllPaymentTypeAccountsByTransactionDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentTypeAccountRepository.saveAndFlush(paymentTypeAccount);

        // Get all the paymentTypeAccountList where transactionDate is not null
        defaultPaymentTypeAccountShouldBeFound("transactionDate.specified=true");

        // Get all the paymentTypeAccountList where transactionDate is null
        defaultPaymentTypeAccountShouldNotBeFound("transactionDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllPaymentTypeAccountsByTransactionDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentTypeAccountRepository.saveAndFlush(paymentTypeAccount);

        // Get all the paymentTypeAccountList where transactionDate is greater than or equal to DEFAULT_TRANSACTION_DATE
        defaultPaymentTypeAccountShouldBeFound("transactionDate.greaterThanOrEqual=" + DEFAULT_TRANSACTION_DATE);

        // Get all the paymentTypeAccountList where transactionDate is greater than or equal to UPDATED_TRANSACTION_DATE
        defaultPaymentTypeAccountShouldNotBeFound("transactionDate.greaterThanOrEqual=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllPaymentTypeAccountsByTransactionDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentTypeAccountRepository.saveAndFlush(paymentTypeAccount);

        // Get all the paymentTypeAccountList where transactionDate is less than or equal to DEFAULT_TRANSACTION_DATE
        defaultPaymentTypeAccountShouldBeFound("transactionDate.lessThanOrEqual=" + DEFAULT_TRANSACTION_DATE);

        // Get all the paymentTypeAccountList where transactionDate is less than or equal to SMALLER_TRANSACTION_DATE
        defaultPaymentTypeAccountShouldNotBeFound("transactionDate.lessThanOrEqual=" + SMALLER_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllPaymentTypeAccountsByTransactionDateIsLessThanSomething() throws Exception {
        // Initialize the database
        paymentTypeAccountRepository.saveAndFlush(paymentTypeAccount);

        // Get all the paymentTypeAccountList where transactionDate is less than DEFAULT_TRANSACTION_DATE
        defaultPaymentTypeAccountShouldNotBeFound("transactionDate.lessThan=" + DEFAULT_TRANSACTION_DATE);

        // Get all the paymentTypeAccountList where transactionDate is less than UPDATED_TRANSACTION_DATE
        defaultPaymentTypeAccountShouldBeFound("transactionDate.lessThan=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllPaymentTypeAccountsByTransactionDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        paymentTypeAccountRepository.saveAndFlush(paymentTypeAccount);

        // Get all the paymentTypeAccountList where transactionDate is greater than DEFAULT_TRANSACTION_DATE
        defaultPaymentTypeAccountShouldNotBeFound("transactionDate.greaterThan=" + DEFAULT_TRANSACTION_DATE);

        // Get all the paymentTypeAccountList where transactionDate is greater than SMALLER_TRANSACTION_DATE
        defaultPaymentTypeAccountShouldBeFound("transactionDate.greaterThan=" + SMALLER_TRANSACTION_DATE);
    }


    @Test
    @Transactional
    public void getAllPaymentTypeAccountsByTransactionDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentTypeAccountRepository.saveAndFlush(paymentTypeAccount);

        // Get all the paymentTypeAccountList where transactionDescription equals to DEFAULT_TRANSACTION_DESCRIPTION
        defaultPaymentTypeAccountShouldBeFound("transactionDescription.equals=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the paymentTypeAccountList where transactionDescription equals to UPDATED_TRANSACTION_DESCRIPTION
        defaultPaymentTypeAccountShouldNotBeFound("transactionDescription.equals=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllPaymentTypeAccountsByTransactionDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentTypeAccountRepository.saveAndFlush(paymentTypeAccount);

        // Get all the paymentTypeAccountList where transactionDescription not equals to DEFAULT_TRANSACTION_DESCRIPTION
        defaultPaymentTypeAccountShouldNotBeFound("transactionDescription.notEquals=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the paymentTypeAccountList where transactionDescription not equals to UPDATED_TRANSACTION_DESCRIPTION
        defaultPaymentTypeAccountShouldBeFound("transactionDescription.notEquals=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllPaymentTypeAccountsByTransactionDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        paymentTypeAccountRepository.saveAndFlush(paymentTypeAccount);

        // Get all the paymentTypeAccountList where transactionDescription in DEFAULT_TRANSACTION_DESCRIPTION or UPDATED_TRANSACTION_DESCRIPTION
        defaultPaymentTypeAccountShouldBeFound("transactionDescription.in=" + DEFAULT_TRANSACTION_DESCRIPTION + "," + UPDATED_TRANSACTION_DESCRIPTION);

        // Get all the paymentTypeAccountList where transactionDescription equals to UPDATED_TRANSACTION_DESCRIPTION
        defaultPaymentTypeAccountShouldNotBeFound("transactionDescription.in=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllPaymentTypeAccountsByTransactionDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentTypeAccountRepository.saveAndFlush(paymentTypeAccount);

        // Get all the paymentTypeAccountList where transactionDescription is not null
        defaultPaymentTypeAccountShouldBeFound("transactionDescription.specified=true");

        // Get all the paymentTypeAccountList where transactionDescription is null
        defaultPaymentTypeAccountShouldNotBeFound("transactionDescription.specified=false");
    }
                @Test
    @Transactional
    public void getAllPaymentTypeAccountsByTransactionDescriptionContainsSomething() throws Exception {
        // Initialize the database
        paymentTypeAccountRepository.saveAndFlush(paymentTypeAccount);

        // Get all the paymentTypeAccountList where transactionDescription contains DEFAULT_TRANSACTION_DESCRIPTION
        defaultPaymentTypeAccountShouldBeFound("transactionDescription.contains=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the paymentTypeAccountList where transactionDescription contains UPDATED_TRANSACTION_DESCRIPTION
        defaultPaymentTypeAccountShouldNotBeFound("transactionDescription.contains=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllPaymentTypeAccountsByTransactionDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        paymentTypeAccountRepository.saveAndFlush(paymentTypeAccount);

        // Get all the paymentTypeAccountList where transactionDescription does not contain DEFAULT_TRANSACTION_DESCRIPTION
        defaultPaymentTypeAccountShouldNotBeFound("transactionDescription.doesNotContain=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the paymentTypeAccountList where transactionDescription does not contain UPDATED_TRANSACTION_DESCRIPTION
        defaultPaymentTypeAccountShouldBeFound("transactionDescription.doesNotContain=" + UPDATED_TRANSACTION_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllPaymentTypeAccountsByTransactionAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentTypeAccountRepository.saveAndFlush(paymentTypeAccount);

        // Get all the paymentTypeAccountList where transactionAmount equals to DEFAULT_TRANSACTION_AMOUNT
        defaultPaymentTypeAccountShouldBeFound("transactionAmount.equals=" + DEFAULT_TRANSACTION_AMOUNT);

        // Get all the paymentTypeAccountList where transactionAmount equals to UPDATED_TRANSACTION_AMOUNT
        defaultPaymentTypeAccountShouldNotBeFound("transactionAmount.equals=" + UPDATED_TRANSACTION_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllPaymentTypeAccountsByTransactionAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentTypeAccountRepository.saveAndFlush(paymentTypeAccount);

        // Get all the paymentTypeAccountList where transactionAmount not equals to DEFAULT_TRANSACTION_AMOUNT
        defaultPaymentTypeAccountShouldNotBeFound("transactionAmount.notEquals=" + DEFAULT_TRANSACTION_AMOUNT);

        // Get all the paymentTypeAccountList where transactionAmount not equals to UPDATED_TRANSACTION_AMOUNT
        defaultPaymentTypeAccountShouldBeFound("transactionAmount.notEquals=" + UPDATED_TRANSACTION_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllPaymentTypeAccountsByTransactionAmountIsInShouldWork() throws Exception {
        // Initialize the database
        paymentTypeAccountRepository.saveAndFlush(paymentTypeAccount);

        // Get all the paymentTypeAccountList where transactionAmount in DEFAULT_TRANSACTION_AMOUNT or UPDATED_TRANSACTION_AMOUNT
        defaultPaymentTypeAccountShouldBeFound("transactionAmount.in=" + DEFAULT_TRANSACTION_AMOUNT + "," + UPDATED_TRANSACTION_AMOUNT);

        // Get all the paymentTypeAccountList where transactionAmount equals to UPDATED_TRANSACTION_AMOUNT
        defaultPaymentTypeAccountShouldNotBeFound("transactionAmount.in=" + UPDATED_TRANSACTION_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllPaymentTypeAccountsByTransactionAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentTypeAccountRepository.saveAndFlush(paymentTypeAccount);

        // Get all the paymentTypeAccountList where transactionAmount is not null
        defaultPaymentTypeAccountShouldBeFound("transactionAmount.specified=true");

        // Get all the paymentTypeAccountList where transactionAmount is null
        defaultPaymentTypeAccountShouldNotBeFound("transactionAmount.specified=false");
    }

    @Test
    @Transactional
    public void getAllPaymentTypeAccountsByTransactionAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentTypeAccountRepository.saveAndFlush(paymentTypeAccount);

        // Get all the paymentTypeAccountList where transactionAmount is greater than or equal to DEFAULT_TRANSACTION_AMOUNT
        defaultPaymentTypeAccountShouldBeFound("transactionAmount.greaterThanOrEqual=" + DEFAULT_TRANSACTION_AMOUNT);

        // Get all the paymentTypeAccountList where transactionAmount is greater than or equal to UPDATED_TRANSACTION_AMOUNT
        defaultPaymentTypeAccountShouldNotBeFound("transactionAmount.greaterThanOrEqual=" + UPDATED_TRANSACTION_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllPaymentTypeAccountsByTransactionAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentTypeAccountRepository.saveAndFlush(paymentTypeAccount);

        // Get all the paymentTypeAccountList where transactionAmount is less than or equal to DEFAULT_TRANSACTION_AMOUNT
        defaultPaymentTypeAccountShouldBeFound("transactionAmount.lessThanOrEqual=" + DEFAULT_TRANSACTION_AMOUNT);

        // Get all the paymentTypeAccountList where transactionAmount is less than or equal to SMALLER_TRANSACTION_AMOUNT
        defaultPaymentTypeAccountShouldNotBeFound("transactionAmount.lessThanOrEqual=" + SMALLER_TRANSACTION_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllPaymentTypeAccountsByTransactionAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        paymentTypeAccountRepository.saveAndFlush(paymentTypeAccount);

        // Get all the paymentTypeAccountList where transactionAmount is less than DEFAULT_TRANSACTION_AMOUNT
        defaultPaymentTypeAccountShouldNotBeFound("transactionAmount.lessThan=" + DEFAULT_TRANSACTION_AMOUNT);

        // Get all the paymentTypeAccountList where transactionAmount is less than UPDATED_TRANSACTION_AMOUNT
        defaultPaymentTypeAccountShouldBeFound("transactionAmount.lessThan=" + UPDATED_TRANSACTION_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllPaymentTypeAccountsByTransactionAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        paymentTypeAccountRepository.saveAndFlush(paymentTypeAccount);

        // Get all the paymentTypeAccountList where transactionAmount is greater than DEFAULT_TRANSACTION_AMOUNT
        defaultPaymentTypeAccountShouldNotBeFound("transactionAmount.greaterThan=" + DEFAULT_TRANSACTION_AMOUNT);

        // Get all the paymentTypeAccountList where transactionAmount is greater than SMALLER_TRANSACTION_AMOUNT
        defaultPaymentTypeAccountShouldBeFound("transactionAmount.greaterThan=" + SMALLER_TRANSACTION_AMOUNT);
    }


    @Test
    @Transactional
    public void getAllPaymentTypeAccountsByTransactionBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentTypeAccountRepository.saveAndFlush(paymentTypeAccount);

        // Get all the paymentTypeAccountList where transactionBalance equals to DEFAULT_TRANSACTION_BALANCE
        defaultPaymentTypeAccountShouldBeFound("transactionBalance.equals=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the paymentTypeAccountList where transactionBalance equals to UPDATED_TRANSACTION_BALANCE
        defaultPaymentTypeAccountShouldNotBeFound("transactionBalance.equals=" + UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void getAllPaymentTypeAccountsByTransactionBalanceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentTypeAccountRepository.saveAndFlush(paymentTypeAccount);

        // Get all the paymentTypeAccountList where transactionBalance not equals to DEFAULT_TRANSACTION_BALANCE
        defaultPaymentTypeAccountShouldNotBeFound("transactionBalance.notEquals=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the paymentTypeAccountList where transactionBalance not equals to UPDATED_TRANSACTION_BALANCE
        defaultPaymentTypeAccountShouldBeFound("transactionBalance.notEquals=" + UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void getAllPaymentTypeAccountsByTransactionBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        paymentTypeAccountRepository.saveAndFlush(paymentTypeAccount);

        // Get all the paymentTypeAccountList where transactionBalance in DEFAULT_TRANSACTION_BALANCE or UPDATED_TRANSACTION_BALANCE
        defaultPaymentTypeAccountShouldBeFound("transactionBalance.in=" + DEFAULT_TRANSACTION_BALANCE + "," + UPDATED_TRANSACTION_BALANCE);

        // Get all the paymentTypeAccountList where transactionBalance equals to UPDATED_TRANSACTION_BALANCE
        defaultPaymentTypeAccountShouldNotBeFound("transactionBalance.in=" + UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void getAllPaymentTypeAccountsByTransactionBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentTypeAccountRepository.saveAndFlush(paymentTypeAccount);

        // Get all the paymentTypeAccountList where transactionBalance is not null
        defaultPaymentTypeAccountShouldBeFound("transactionBalance.specified=true");

        // Get all the paymentTypeAccountList where transactionBalance is null
        defaultPaymentTypeAccountShouldNotBeFound("transactionBalance.specified=false");
    }

    @Test
    @Transactional
    public void getAllPaymentTypeAccountsByTransactionBalanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentTypeAccountRepository.saveAndFlush(paymentTypeAccount);

        // Get all the paymentTypeAccountList where transactionBalance is greater than or equal to DEFAULT_TRANSACTION_BALANCE
        defaultPaymentTypeAccountShouldBeFound("transactionBalance.greaterThanOrEqual=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the paymentTypeAccountList where transactionBalance is greater than or equal to UPDATED_TRANSACTION_BALANCE
        defaultPaymentTypeAccountShouldNotBeFound("transactionBalance.greaterThanOrEqual=" + UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void getAllPaymentTypeAccountsByTransactionBalanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentTypeAccountRepository.saveAndFlush(paymentTypeAccount);

        // Get all the paymentTypeAccountList where transactionBalance is less than or equal to DEFAULT_TRANSACTION_BALANCE
        defaultPaymentTypeAccountShouldBeFound("transactionBalance.lessThanOrEqual=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the paymentTypeAccountList where transactionBalance is less than or equal to SMALLER_TRANSACTION_BALANCE
        defaultPaymentTypeAccountShouldNotBeFound("transactionBalance.lessThanOrEqual=" + SMALLER_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void getAllPaymentTypeAccountsByTransactionBalanceIsLessThanSomething() throws Exception {
        // Initialize the database
        paymentTypeAccountRepository.saveAndFlush(paymentTypeAccount);

        // Get all the paymentTypeAccountList where transactionBalance is less than DEFAULT_TRANSACTION_BALANCE
        defaultPaymentTypeAccountShouldNotBeFound("transactionBalance.lessThan=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the paymentTypeAccountList where transactionBalance is less than UPDATED_TRANSACTION_BALANCE
        defaultPaymentTypeAccountShouldBeFound("transactionBalance.lessThan=" + UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void getAllPaymentTypeAccountsByTransactionBalanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        paymentTypeAccountRepository.saveAndFlush(paymentTypeAccount);

        // Get all the paymentTypeAccountList where transactionBalance is greater than DEFAULT_TRANSACTION_BALANCE
        defaultPaymentTypeAccountShouldNotBeFound("transactionBalance.greaterThan=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the paymentTypeAccountList where transactionBalance is greater than SMALLER_TRANSACTION_BALANCE
        defaultPaymentTypeAccountShouldBeFound("transactionBalance.greaterThan=" + SMALLER_TRANSACTION_BALANCE);
    }


    @Test
    @Transactional
    public void getAllPaymentTypeAccountsByLocationIsEqualToSomething() throws Exception {
        // Get already existing entity
        Location location = paymentTypeAccount.getLocation();
        paymentTypeAccountRepository.saveAndFlush(paymentTypeAccount);
        Long locationId = location.getId();

        // Get all the paymentTypeAccountList where location equals to locationId
        defaultPaymentTypeAccountShouldBeFound("locationId.equals=" + locationId);

        // Get all the paymentTypeAccountList where location equals to locationId + 1
        defaultPaymentTypeAccountShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }


    @Test
    @Transactional
    public void getAllPaymentTypeAccountsByTransactionTypeIsEqualToSomething() throws Exception {
        // Get already existing entity
        TransactionType transactionType = paymentTypeAccount.getTransactionType();
        paymentTypeAccountRepository.saveAndFlush(paymentTypeAccount);
        Long transactionTypeId = transactionType.getId();

        // Get all the paymentTypeAccountList where transactionType equals to transactionTypeId
        defaultPaymentTypeAccountShouldBeFound("transactionTypeId.equals=" + transactionTypeId);

        // Get all the paymentTypeAccountList where transactionType equals to transactionTypeId + 1
        defaultPaymentTypeAccountShouldNotBeFound("transactionTypeId.equals=" + (transactionTypeId + 1));
    }


    @Test
    @Transactional
    public void getAllPaymentTypeAccountsByPayTypeIsEqualToSomething() throws Exception {
        // Get already existing entity
        PaymentTypes payType = paymentTypeAccount.getPayType();
        paymentTypeAccountRepository.saveAndFlush(paymentTypeAccount);
        Long payTypeId = payType.getId();

        // Get all the paymentTypeAccountList where payType equals to payTypeId
        defaultPaymentTypeAccountShouldBeFound("payTypeId.equals=" + payTypeId);

        // Get all the paymentTypeAccountList where payType equals to payTypeId + 1
        defaultPaymentTypeAccountShouldNotBeFound("payTypeId.equals=" + (payTypeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPaymentTypeAccountShouldBeFound(String filter) throws Exception {
        restPaymentTypeAccountMockMvc.perform(get("/api/payment-type-accounts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentTypeAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].transactionDescription").value(hasItem(DEFAULT_TRANSACTION_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].transactionAmount").value(hasItem(DEFAULT_TRANSACTION_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].transactionBalance").value(hasItem(DEFAULT_TRANSACTION_BALANCE.intValue())));

        // Check, that the count call also returns 1
        restPaymentTypeAccountMockMvc.perform(get("/api/payment-type-accounts/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPaymentTypeAccountShouldNotBeFound(String filter) throws Exception {
        restPaymentTypeAccountMockMvc.perform(get("/api/payment-type-accounts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPaymentTypeAccountMockMvc.perform(get("/api/payment-type-accounts/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingPaymentTypeAccount() throws Exception {
        // Get the paymentTypeAccount
        restPaymentTypeAccountMockMvc.perform(get("/api/payment-type-accounts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePaymentTypeAccount() throws Exception {
        // Initialize the database
        paymentTypeAccountService.save(paymentTypeAccount);

        int databaseSizeBeforeUpdate = paymentTypeAccountRepository.findAll().size();

        // Update the paymentTypeAccount
        PaymentTypeAccount updatedPaymentTypeAccount = paymentTypeAccountRepository.findById(paymentTypeAccount.getId()).get();
        // Disconnect from session so that the updates on updatedPaymentTypeAccount are not directly saved in db
        em.detach(updatedPaymentTypeAccount);
        updatedPaymentTypeAccount
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .transactionDescription(UPDATED_TRANSACTION_DESCRIPTION)
            .transactionAmount(UPDATED_TRANSACTION_AMOUNT)
            .transactionBalance(UPDATED_TRANSACTION_BALANCE);

        restPaymentTypeAccountMockMvc.perform(put("/api/payment-type-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPaymentTypeAccount)))
            .andExpect(status().isOk());

        // Validate the PaymentTypeAccount in the database
        List<PaymentTypeAccount> paymentTypeAccountList = paymentTypeAccountRepository.findAll();
        assertThat(paymentTypeAccountList).hasSize(databaseSizeBeforeUpdate);
        PaymentTypeAccount testPaymentTypeAccount = paymentTypeAccountList.get(paymentTypeAccountList.size() - 1);
        assertThat(testPaymentTypeAccount.getTransactionDate()).isEqualTo(UPDATED_TRANSACTION_DATE);
        assertThat(testPaymentTypeAccount.getTransactionDescription()).isEqualTo(UPDATED_TRANSACTION_DESCRIPTION);
        assertThat(testPaymentTypeAccount.getTransactionAmount()).isEqualTo(UPDATED_TRANSACTION_AMOUNT);
        assertThat(testPaymentTypeAccount.getTransactionBalance()).isEqualTo(UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void updateNonExistingPaymentTypeAccount() throws Exception {
        int databaseSizeBeforeUpdate = paymentTypeAccountRepository.findAll().size();

        // Create the PaymentTypeAccount

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentTypeAccountMockMvc.perform(put("/api/payment-type-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentTypeAccount)))
            .andExpect(status().isBadRequest());

        // Validate the PaymentTypeAccount in the database
        List<PaymentTypeAccount> paymentTypeAccountList = paymentTypeAccountRepository.findAll();
        assertThat(paymentTypeAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePaymentTypeAccount() throws Exception {
        // Initialize the database
        paymentTypeAccountService.save(paymentTypeAccount);

        int databaseSizeBeforeDelete = paymentTypeAccountRepository.findAll().size();

        // Delete the paymentTypeAccount
        restPaymentTypeAccountMockMvc.perform(delete("/api/payment-type-accounts/{id}", paymentTypeAccount.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PaymentTypeAccount> paymentTypeAccountList = paymentTypeAccountRepository.findAll();
        assertThat(paymentTypeAccountList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

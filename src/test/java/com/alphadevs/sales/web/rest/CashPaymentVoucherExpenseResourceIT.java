package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.WikunumApp;
import com.alphadevs.sales.domain.CashPaymentVoucherExpense;
import com.alphadevs.sales.domain.Location;
import com.alphadevs.sales.domain.TransactionType;
import com.alphadevs.sales.domain.Expense;
import com.alphadevs.sales.repository.CashPaymentVoucherExpenseRepository;
import com.alphadevs.sales.service.CashPaymentVoucherExpenseService;
import com.alphadevs.sales.web.rest.errors.ExceptionTranslator;
import com.alphadevs.sales.service.dto.CashPaymentVoucherExpenseCriteria;
import com.alphadevs.sales.service.CashPaymentVoucherExpenseQueryService;

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
 * Integration tests for the {@link CashPaymentVoucherExpenseResource} REST controller.
 */
@SpringBootTest(classes = WikunumApp.class)
public class CashPaymentVoucherExpenseResourceIT {

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
    private CashPaymentVoucherExpenseRepository cashPaymentVoucherExpenseRepository;

    @Autowired
    private CashPaymentVoucherExpenseService cashPaymentVoucherExpenseService;

    @Autowired
    private CashPaymentVoucherExpenseQueryService cashPaymentVoucherExpenseQueryService;

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

    private MockMvc restCashPaymentVoucherExpenseMockMvc;

    private CashPaymentVoucherExpense cashPaymentVoucherExpense;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CashPaymentVoucherExpenseResource cashPaymentVoucherExpenseResource = new CashPaymentVoucherExpenseResource(cashPaymentVoucherExpenseService, cashPaymentVoucherExpenseQueryService);
        this.restCashPaymentVoucherExpenseMockMvc = MockMvcBuilders.standaloneSetup(cashPaymentVoucherExpenseResource)
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
    public static CashPaymentVoucherExpense createEntity(EntityManager em) {
        CashPaymentVoucherExpense cashPaymentVoucherExpense = new CashPaymentVoucherExpense()
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
        cashPaymentVoucherExpense.setLocation(location);
        // Add required entity
        TransactionType transactionType;
        if (TestUtil.findAll(em, TransactionType.class).isEmpty()) {
            transactionType = TransactionTypeResourceIT.createEntity(em);
            em.persist(transactionType);
            em.flush();
        } else {
            transactionType = TestUtil.findAll(em, TransactionType.class).get(0);
        }
        cashPaymentVoucherExpense.setTransactionType(transactionType);
        // Add required entity
        Expense expense;
        if (TestUtil.findAll(em, Expense.class).isEmpty()) {
            expense = ExpenseResourceIT.createEntity(em);
            em.persist(expense);
            em.flush();
        } else {
            expense = TestUtil.findAll(em, Expense.class).get(0);
        }
        cashPaymentVoucherExpense.setExpense(expense);
        return cashPaymentVoucherExpense;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CashPaymentVoucherExpense createUpdatedEntity(EntityManager em) {
        CashPaymentVoucherExpense cashPaymentVoucherExpense = new CashPaymentVoucherExpense()
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
        cashPaymentVoucherExpense.setLocation(location);
        // Add required entity
        TransactionType transactionType;
        if (TestUtil.findAll(em, TransactionType.class).isEmpty()) {
            transactionType = TransactionTypeResourceIT.createUpdatedEntity(em);
            em.persist(transactionType);
            em.flush();
        } else {
            transactionType = TestUtil.findAll(em, TransactionType.class).get(0);
        }
        cashPaymentVoucherExpense.setTransactionType(transactionType);
        // Add required entity
        Expense expense;
        if (TestUtil.findAll(em, Expense.class).isEmpty()) {
            expense = ExpenseResourceIT.createUpdatedEntity(em);
            em.persist(expense);
            em.flush();
        } else {
            expense = TestUtil.findAll(em, Expense.class).get(0);
        }
        cashPaymentVoucherExpense.setExpense(expense);
        return cashPaymentVoucherExpense;
    }

    @BeforeEach
    public void initTest() {
        cashPaymentVoucherExpense = createEntity(em);
    }

    @Test
    @Transactional
    public void createCashPaymentVoucherExpense() throws Exception {
        int databaseSizeBeforeCreate = cashPaymentVoucherExpenseRepository.findAll().size();

        // Create the CashPaymentVoucherExpense
        restCashPaymentVoucherExpenseMockMvc.perform(post("/api/cash-payment-voucher-expenses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashPaymentVoucherExpense)))
            .andExpect(status().isCreated());

        // Validate the CashPaymentVoucherExpense in the database
        List<CashPaymentVoucherExpense> cashPaymentVoucherExpenseList = cashPaymentVoucherExpenseRepository.findAll();
        assertThat(cashPaymentVoucherExpenseList).hasSize(databaseSizeBeforeCreate + 1);
        CashPaymentVoucherExpense testCashPaymentVoucherExpense = cashPaymentVoucherExpenseList.get(cashPaymentVoucherExpenseList.size() - 1);
        assertThat(testCashPaymentVoucherExpense.getTransactionNumber()).isEqualTo(DEFAULT_TRANSACTION_NUMBER);
        assertThat(testCashPaymentVoucherExpense.getTransactionDate()).isEqualTo(DEFAULT_TRANSACTION_DATE);
        assertThat(testCashPaymentVoucherExpense.getTransactionDescription()).isEqualTo(DEFAULT_TRANSACTION_DESCRIPTION);
        assertThat(testCashPaymentVoucherExpense.getTransactionAmount()).isEqualTo(DEFAULT_TRANSACTION_AMOUNT);
    }

    @Test
    @Transactional
    public void createCashPaymentVoucherExpenseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cashPaymentVoucherExpenseRepository.findAll().size();

        // Create the CashPaymentVoucherExpense with an existing ID
        cashPaymentVoucherExpense.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCashPaymentVoucherExpenseMockMvc.perform(post("/api/cash-payment-voucher-expenses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashPaymentVoucherExpense)))
            .andExpect(status().isBadRequest());

        // Validate the CashPaymentVoucherExpense in the database
        List<CashPaymentVoucherExpense> cashPaymentVoucherExpenseList = cashPaymentVoucherExpenseRepository.findAll();
        assertThat(cashPaymentVoucherExpenseList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTransactionNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = cashPaymentVoucherExpenseRepository.findAll().size();
        // set the field null
        cashPaymentVoucherExpense.setTransactionNumber(null);

        // Create the CashPaymentVoucherExpense, which fails.

        restCashPaymentVoucherExpenseMockMvc.perform(post("/api/cash-payment-voucher-expenses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashPaymentVoucherExpense)))
            .andExpect(status().isBadRequest());

        List<CashPaymentVoucherExpense> cashPaymentVoucherExpenseList = cashPaymentVoucherExpenseRepository.findAll();
        assertThat(cashPaymentVoucherExpenseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTransactionDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = cashPaymentVoucherExpenseRepository.findAll().size();
        // set the field null
        cashPaymentVoucherExpense.setTransactionDate(null);

        // Create the CashPaymentVoucherExpense, which fails.

        restCashPaymentVoucherExpenseMockMvc.perform(post("/api/cash-payment-voucher-expenses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashPaymentVoucherExpense)))
            .andExpect(status().isBadRequest());

        List<CashPaymentVoucherExpense> cashPaymentVoucherExpenseList = cashPaymentVoucherExpenseRepository.findAll();
        assertThat(cashPaymentVoucherExpenseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTransactionDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = cashPaymentVoucherExpenseRepository.findAll().size();
        // set the field null
        cashPaymentVoucherExpense.setTransactionDescription(null);

        // Create the CashPaymentVoucherExpense, which fails.

        restCashPaymentVoucherExpenseMockMvc.perform(post("/api/cash-payment-voucher-expenses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashPaymentVoucherExpense)))
            .andExpect(status().isBadRequest());

        List<CashPaymentVoucherExpense> cashPaymentVoucherExpenseList = cashPaymentVoucherExpenseRepository.findAll();
        assertThat(cashPaymentVoucherExpenseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTransactionAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = cashPaymentVoucherExpenseRepository.findAll().size();
        // set the field null
        cashPaymentVoucherExpense.setTransactionAmount(null);

        // Create the CashPaymentVoucherExpense, which fails.

        restCashPaymentVoucherExpenseMockMvc.perform(post("/api/cash-payment-voucher-expenses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashPaymentVoucherExpense)))
            .andExpect(status().isBadRequest());

        List<CashPaymentVoucherExpense> cashPaymentVoucherExpenseList = cashPaymentVoucherExpenseRepository.findAll();
        assertThat(cashPaymentVoucherExpenseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherExpenses() throws Exception {
        // Initialize the database
        cashPaymentVoucherExpenseRepository.saveAndFlush(cashPaymentVoucherExpense);

        // Get all the cashPaymentVoucherExpenseList
        restCashPaymentVoucherExpenseMockMvc.perform(get("/api/cash-payment-voucher-expenses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cashPaymentVoucherExpense.getId().intValue())))
            .andExpect(jsonPath("$.[*].transactionNumber").value(hasItem(DEFAULT_TRANSACTION_NUMBER)))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].transactionDescription").value(hasItem(DEFAULT_TRANSACTION_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].transactionAmount").value(hasItem(DEFAULT_TRANSACTION_AMOUNT.intValue())));
    }
    
    @Test
    @Transactional
    public void getCashPaymentVoucherExpense() throws Exception {
        // Initialize the database
        cashPaymentVoucherExpenseRepository.saveAndFlush(cashPaymentVoucherExpense);

        // Get the cashPaymentVoucherExpense
        restCashPaymentVoucherExpenseMockMvc.perform(get("/api/cash-payment-voucher-expenses/{id}", cashPaymentVoucherExpense.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cashPaymentVoucherExpense.getId().intValue()))
            .andExpect(jsonPath("$.transactionNumber").value(DEFAULT_TRANSACTION_NUMBER))
            .andExpect(jsonPath("$.transactionDate").value(DEFAULT_TRANSACTION_DATE.toString()))
            .andExpect(jsonPath("$.transactionDescription").value(DEFAULT_TRANSACTION_DESCRIPTION))
            .andExpect(jsonPath("$.transactionAmount").value(DEFAULT_TRANSACTION_AMOUNT.intValue()));
    }


    @Test
    @Transactional
    public void getCashPaymentVoucherExpensesByIdFiltering() throws Exception {
        // Initialize the database
        cashPaymentVoucherExpenseRepository.saveAndFlush(cashPaymentVoucherExpense);

        Long id = cashPaymentVoucherExpense.getId();

        defaultCashPaymentVoucherExpenseShouldBeFound("id.equals=" + id);
        defaultCashPaymentVoucherExpenseShouldNotBeFound("id.notEquals=" + id);

        defaultCashPaymentVoucherExpenseShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCashPaymentVoucherExpenseShouldNotBeFound("id.greaterThan=" + id);

        defaultCashPaymentVoucherExpenseShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCashPaymentVoucherExpenseShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllCashPaymentVoucherExpensesByTransactionNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        cashPaymentVoucherExpenseRepository.saveAndFlush(cashPaymentVoucherExpense);

        // Get all the cashPaymentVoucherExpenseList where transactionNumber equals to DEFAULT_TRANSACTION_NUMBER
        defaultCashPaymentVoucherExpenseShouldBeFound("transactionNumber.equals=" + DEFAULT_TRANSACTION_NUMBER);

        // Get all the cashPaymentVoucherExpenseList where transactionNumber equals to UPDATED_TRANSACTION_NUMBER
        defaultCashPaymentVoucherExpenseShouldNotBeFound("transactionNumber.equals=" + UPDATED_TRANSACTION_NUMBER);
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherExpensesByTransactionNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cashPaymentVoucherExpenseRepository.saveAndFlush(cashPaymentVoucherExpense);

        // Get all the cashPaymentVoucherExpenseList where transactionNumber not equals to DEFAULT_TRANSACTION_NUMBER
        defaultCashPaymentVoucherExpenseShouldNotBeFound("transactionNumber.notEquals=" + DEFAULT_TRANSACTION_NUMBER);

        // Get all the cashPaymentVoucherExpenseList where transactionNumber not equals to UPDATED_TRANSACTION_NUMBER
        defaultCashPaymentVoucherExpenseShouldBeFound("transactionNumber.notEquals=" + UPDATED_TRANSACTION_NUMBER);
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherExpensesByTransactionNumberIsInShouldWork() throws Exception {
        // Initialize the database
        cashPaymentVoucherExpenseRepository.saveAndFlush(cashPaymentVoucherExpense);

        // Get all the cashPaymentVoucherExpenseList where transactionNumber in DEFAULT_TRANSACTION_NUMBER or UPDATED_TRANSACTION_NUMBER
        defaultCashPaymentVoucherExpenseShouldBeFound("transactionNumber.in=" + DEFAULT_TRANSACTION_NUMBER + "," + UPDATED_TRANSACTION_NUMBER);

        // Get all the cashPaymentVoucherExpenseList where transactionNumber equals to UPDATED_TRANSACTION_NUMBER
        defaultCashPaymentVoucherExpenseShouldNotBeFound("transactionNumber.in=" + UPDATED_TRANSACTION_NUMBER);
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherExpensesByTransactionNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        cashPaymentVoucherExpenseRepository.saveAndFlush(cashPaymentVoucherExpense);

        // Get all the cashPaymentVoucherExpenseList where transactionNumber is not null
        defaultCashPaymentVoucherExpenseShouldBeFound("transactionNumber.specified=true");

        // Get all the cashPaymentVoucherExpenseList where transactionNumber is null
        defaultCashPaymentVoucherExpenseShouldNotBeFound("transactionNumber.specified=false");
    }
                @Test
    @Transactional
    public void getAllCashPaymentVoucherExpensesByTransactionNumberContainsSomething() throws Exception {
        // Initialize the database
        cashPaymentVoucherExpenseRepository.saveAndFlush(cashPaymentVoucherExpense);

        // Get all the cashPaymentVoucherExpenseList where transactionNumber contains DEFAULT_TRANSACTION_NUMBER
        defaultCashPaymentVoucherExpenseShouldBeFound("transactionNumber.contains=" + DEFAULT_TRANSACTION_NUMBER);

        // Get all the cashPaymentVoucherExpenseList where transactionNumber contains UPDATED_TRANSACTION_NUMBER
        defaultCashPaymentVoucherExpenseShouldNotBeFound("transactionNumber.contains=" + UPDATED_TRANSACTION_NUMBER);
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherExpensesByTransactionNumberNotContainsSomething() throws Exception {
        // Initialize the database
        cashPaymentVoucherExpenseRepository.saveAndFlush(cashPaymentVoucherExpense);

        // Get all the cashPaymentVoucherExpenseList where transactionNumber does not contain DEFAULT_TRANSACTION_NUMBER
        defaultCashPaymentVoucherExpenseShouldNotBeFound("transactionNumber.doesNotContain=" + DEFAULT_TRANSACTION_NUMBER);

        // Get all the cashPaymentVoucherExpenseList where transactionNumber does not contain UPDATED_TRANSACTION_NUMBER
        defaultCashPaymentVoucherExpenseShouldBeFound("transactionNumber.doesNotContain=" + UPDATED_TRANSACTION_NUMBER);
    }


    @Test
    @Transactional
    public void getAllCashPaymentVoucherExpensesByTransactionDateIsEqualToSomething() throws Exception {
        // Initialize the database
        cashPaymentVoucherExpenseRepository.saveAndFlush(cashPaymentVoucherExpense);

        // Get all the cashPaymentVoucherExpenseList where transactionDate equals to DEFAULT_TRANSACTION_DATE
        defaultCashPaymentVoucherExpenseShouldBeFound("transactionDate.equals=" + DEFAULT_TRANSACTION_DATE);

        // Get all the cashPaymentVoucherExpenseList where transactionDate equals to UPDATED_TRANSACTION_DATE
        defaultCashPaymentVoucherExpenseShouldNotBeFound("transactionDate.equals=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherExpensesByTransactionDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cashPaymentVoucherExpenseRepository.saveAndFlush(cashPaymentVoucherExpense);

        // Get all the cashPaymentVoucherExpenseList where transactionDate not equals to DEFAULT_TRANSACTION_DATE
        defaultCashPaymentVoucherExpenseShouldNotBeFound("transactionDate.notEquals=" + DEFAULT_TRANSACTION_DATE);

        // Get all the cashPaymentVoucherExpenseList where transactionDate not equals to UPDATED_TRANSACTION_DATE
        defaultCashPaymentVoucherExpenseShouldBeFound("transactionDate.notEquals=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherExpensesByTransactionDateIsInShouldWork() throws Exception {
        // Initialize the database
        cashPaymentVoucherExpenseRepository.saveAndFlush(cashPaymentVoucherExpense);

        // Get all the cashPaymentVoucherExpenseList where transactionDate in DEFAULT_TRANSACTION_DATE or UPDATED_TRANSACTION_DATE
        defaultCashPaymentVoucherExpenseShouldBeFound("transactionDate.in=" + DEFAULT_TRANSACTION_DATE + "," + UPDATED_TRANSACTION_DATE);

        // Get all the cashPaymentVoucherExpenseList where transactionDate equals to UPDATED_TRANSACTION_DATE
        defaultCashPaymentVoucherExpenseShouldNotBeFound("transactionDate.in=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherExpensesByTransactionDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        cashPaymentVoucherExpenseRepository.saveAndFlush(cashPaymentVoucherExpense);

        // Get all the cashPaymentVoucherExpenseList where transactionDate is not null
        defaultCashPaymentVoucherExpenseShouldBeFound("transactionDate.specified=true");

        // Get all the cashPaymentVoucherExpenseList where transactionDate is null
        defaultCashPaymentVoucherExpenseShouldNotBeFound("transactionDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherExpensesByTransactionDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cashPaymentVoucherExpenseRepository.saveAndFlush(cashPaymentVoucherExpense);

        // Get all the cashPaymentVoucherExpenseList where transactionDate is greater than or equal to DEFAULT_TRANSACTION_DATE
        defaultCashPaymentVoucherExpenseShouldBeFound("transactionDate.greaterThanOrEqual=" + DEFAULT_TRANSACTION_DATE);

        // Get all the cashPaymentVoucherExpenseList where transactionDate is greater than or equal to UPDATED_TRANSACTION_DATE
        defaultCashPaymentVoucherExpenseShouldNotBeFound("transactionDate.greaterThanOrEqual=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherExpensesByTransactionDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cashPaymentVoucherExpenseRepository.saveAndFlush(cashPaymentVoucherExpense);

        // Get all the cashPaymentVoucherExpenseList where transactionDate is less than or equal to DEFAULT_TRANSACTION_DATE
        defaultCashPaymentVoucherExpenseShouldBeFound("transactionDate.lessThanOrEqual=" + DEFAULT_TRANSACTION_DATE);

        // Get all the cashPaymentVoucherExpenseList where transactionDate is less than or equal to SMALLER_TRANSACTION_DATE
        defaultCashPaymentVoucherExpenseShouldNotBeFound("transactionDate.lessThanOrEqual=" + SMALLER_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherExpensesByTransactionDateIsLessThanSomething() throws Exception {
        // Initialize the database
        cashPaymentVoucherExpenseRepository.saveAndFlush(cashPaymentVoucherExpense);

        // Get all the cashPaymentVoucherExpenseList where transactionDate is less than DEFAULT_TRANSACTION_DATE
        defaultCashPaymentVoucherExpenseShouldNotBeFound("transactionDate.lessThan=" + DEFAULT_TRANSACTION_DATE);

        // Get all the cashPaymentVoucherExpenseList where transactionDate is less than UPDATED_TRANSACTION_DATE
        defaultCashPaymentVoucherExpenseShouldBeFound("transactionDate.lessThan=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherExpensesByTransactionDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cashPaymentVoucherExpenseRepository.saveAndFlush(cashPaymentVoucherExpense);

        // Get all the cashPaymentVoucherExpenseList where transactionDate is greater than DEFAULT_TRANSACTION_DATE
        defaultCashPaymentVoucherExpenseShouldNotBeFound("transactionDate.greaterThan=" + DEFAULT_TRANSACTION_DATE);

        // Get all the cashPaymentVoucherExpenseList where transactionDate is greater than SMALLER_TRANSACTION_DATE
        defaultCashPaymentVoucherExpenseShouldBeFound("transactionDate.greaterThan=" + SMALLER_TRANSACTION_DATE);
    }


    @Test
    @Transactional
    public void getAllCashPaymentVoucherExpensesByTransactionDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        cashPaymentVoucherExpenseRepository.saveAndFlush(cashPaymentVoucherExpense);

        // Get all the cashPaymentVoucherExpenseList where transactionDescription equals to DEFAULT_TRANSACTION_DESCRIPTION
        defaultCashPaymentVoucherExpenseShouldBeFound("transactionDescription.equals=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the cashPaymentVoucherExpenseList where transactionDescription equals to UPDATED_TRANSACTION_DESCRIPTION
        defaultCashPaymentVoucherExpenseShouldNotBeFound("transactionDescription.equals=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherExpensesByTransactionDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cashPaymentVoucherExpenseRepository.saveAndFlush(cashPaymentVoucherExpense);

        // Get all the cashPaymentVoucherExpenseList where transactionDescription not equals to DEFAULT_TRANSACTION_DESCRIPTION
        defaultCashPaymentVoucherExpenseShouldNotBeFound("transactionDescription.notEquals=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the cashPaymentVoucherExpenseList where transactionDescription not equals to UPDATED_TRANSACTION_DESCRIPTION
        defaultCashPaymentVoucherExpenseShouldBeFound("transactionDescription.notEquals=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherExpensesByTransactionDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        cashPaymentVoucherExpenseRepository.saveAndFlush(cashPaymentVoucherExpense);

        // Get all the cashPaymentVoucherExpenseList where transactionDescription in DEFAULT_TRANSACTION_DESCRIPTION or UPDATED_TRANSACTION_DESCRIPTION
        defaultCashPaymentVoucherExpenseShouldBeFound("transactionDescription.in=" + DEFAULT_TRANSACTION_DESCRIPTION + "," + UPDATED_TRANSACTION_DESCRIPTION);

        // Get all the cashPaymentVoucherExpenseList where transactionDescription equals to UPDATED_TRANSACTION_DESCRIPTION
        defaultCashPaymentVoucherExpenseShouldNotBeFound("transactionDescription.in=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherExpensesByTransactionDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        cashPaymentVoucherExpenseRepository.saveAndFlush(cashPaymentVoucherExpense);

        // Get all the cashPaymentVoucherExpenseList where transactionDescription is not null
        defaultCashPaymentVoucherExpenseShouldBeFound("transactionDescription.specified=true");

        // Get all the cashPaymentVoucherExpenseList where transactionDescription is null
        defaultCashPaymentVoucherExpenseShouldNotBeFound("transactionDescription.specified=false");
    }
                @Test
    @Transactional
    public void getAllCashPaymentVoucherExpensesByTransactionDescriptionContainsSomething() throws Exception {
        // Initialize the database
        cashPaymentVoucherExpenseRepository.saveAndFlush(cashPaymentVoucherExpense);

        // Get all the cashPaymentVoucherExpenseList where transactionDescription contains DEFAULT_TRANSACTION_DESCRIPTION
        defaultCashPaymentVoucherExpenseShouldBeFound("transactionDescription.contains=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the cashPaymentVoucherExpenseList where transactionDescription contains UPDATED_TRANSACTION_DESCRIPTION
        defaultCashPaymentVoucherExpenseShouldNotBeFound("transactionDescription.contains=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherExpensesByTransactionDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        cashPaymentVoucherExpenseRepository.saveAndFlush(cashPaymentVoucherExpense);

        // Get all the cashPaymentVoucherExpenseList where transactionDescription does not contain DEFAULT_TRANSACTION_DESCRIPTION
        defaultCashPaymentVoucherExpenseShouldNotBeFound("transactionDescription.doesNotContain=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the cashPaymentVoucherExpenseList where transactionDescription does not contain UPDATED_TRANSACTION_DESCRIPTION
        defaultCashPaymentVoucherExpenseShouldBeFound("transactionDescription.doesNotContain=" + UPDATED_TRANSACTION_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllCashPaymentVoucherExpensesByTransactionAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        cashPaymentVoucherExpenseRepository.saveAndFlush(cashPaymentVoucherExpense);

        // Get all the cashPaymentVoucherExpenseList where transactionAmount equals to DEFAULT_TRANSACTION_AMOUNT
        defaultCashPaymentVoucherExpenseShouldBeFound("transactionAmount.equals=" + DEFAULT_TRANSACTION_AMOUNT);

        // Get all the cashPaymentVoucherExpenseList where transactionAmount equals to UPDATED_TRANSACTION_AMOUNT
        defaultCashPaymentVoucherExpenseShouldNotBeFound("transactionAmount.equals=" + UPDATED_TRANSACTION_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherExpensesByTransactionAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cashPaymentVoucherExpenseRepository.saveAndFlush(cashPaymentVoucherExpense);

        // Get all the cashPaymentVoucherExpenseList where transactionAmount not equals to DEFAULT_TRANSACTION_AMOUNT
        defaultCashPaymentVoucherExpenseShouldNotBeFound("transactionAmount.notEquals=" + DEFAULT_TRANSACTION_AMOUNT);

        // Get all the cashPaymentVoucherExpenseList where transactionAmount not equals to UPDATED_TRANSACTION_AMOUNT
        defaultCashPaymentVoucherExpenseShouldBeFound("transactionAmount.notEquals=" + UPDATED_TRANSACTION_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherExpensesByTransactionAmountIsInShouldWork() throws Exception {
        // Initialize the database
        cashPaymentVoucherExpenseRepository.saveAndFlush(cashPaymentVoucherExpense);

        // Get all the cashPaymentVoucherExpenseList where transactionAmount in DEFAULT_TRANSACTION_AMOUNT or UPDATED_TRANSACTION_AMOUNT
        defaultCashPaymentVoucherExpenseShouldBeFound("transactionAmount.in=" + DEFAULT_TRANSACTION_AMOUNT + "," + UPDATED_TRANSACTION_AMOUNT);

        // Get all the cashPaymentVoucherExpenseList where transactionAmount equals to UPDATED_TRANSACTION_AMOUNT
        defaultCashPaymentVoucherExpenseShouldNotBeFound("transactionAmount.in=" + UPDATED_TRANSACTION_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherExpensesByTransactionAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        cashPaymentVoucherExpenseRepository.saveAndFlush(cashPaymentVoucherExpense);

        // Get all the cashPaymentVoucherExpenseList where transactionAmount is not null
        defaultCashPaymentVoucherExpenseShouldBeFound("transactionAmount.specified=true");

        // Get all the cashPaymentVoucherExpenseList where transactionAmount is null
        defaultCashPaymentVoucherExpenseShouldNotBeFound("transactionAmount.specified=false");
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherExpensesByTransactionAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cashPaymentVoucherExpenseRepository.saveAndFlush(cashPaymentVoucherExpense);

        // Get all the cashPaymentVoucherExpenseList where transactionAmount is greater than or equal to DEFAULT_TRANSACTION_AMOUNT
        defaultCashPaymentVoucherExpenseShouldBeFound("transactionAmount.greaterThanOrEqual=" + DEFAULT_TRANSACTION_AMOUNT);

        // Get all the cashPaymentVoucherExpenseList where transactionAmount is greater than or equal to UPDATED_TRANSACTION_AMOUNT
        defaultCashPaymentVoucherExpenseShouldNotBeFound("transactionAmount.greaterThanOrEqual=" + UPDATED_TRANSACTION_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherExpensesByTransactionAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cashPaymentVoucherExpenseRepository.saveAndFlush(cashPaymentVoucherExpense);

        // Get all the cashPaymentVoucherExpenseList where transactionAmount is less than or equal to DEFAULT_TRANSACTION_AMOUNT
        defaultCashPaymentVoucherExpenseShouldBeFound("transactionAmount.lessThanOrEqual=" + DEFAULT_TRANSACTION_AMOUNT);

        // Get all the cashPaymentVoucherExpenseList where transactionAmount is less than or equal to SMALLER_TRANSACTION_AMOUNT
        defaultCashPaymentVoucherExpenseShouldNotBeFound("transactionAmount.lessThanOrEqual=" + SMALLER_TRANSACTION_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherExpensesByTransactionAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        cashPaymentVoucherExpenseRepository.saveAndFlush(cashPaymentVoucherExpense);

        // Get all the cashPaymentVoucherExpenseList where transactionAmount is less than DEFAULT_TRANSACTION_AMOUNT
        defaultCashPaymentVoucherExpenseShouldNotBeFound("transactionAmount.lessThan=" + DEFAULT_TRANSACTION_AMOUNT);

        // Get all the cashPaymentVoucherExpenseList where transactionAmount is less than UPDATED_TRANSACTION_AMOUNT
        defaultCashPaymentVoucherExpenseShouldBeFound("transactionAmount.lessThan=" + UPDATED_TRANSACTION_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllCashPaymentVoucherExpensesByTransactionAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cashPaymentVoucherExpenseRepository.saveAndFlush(cashPaymentVoucherExpense);

        // Get all the cashPaymentVoucherExpenseList where transactionAmount is greater than DEFAULT_TRANSACTION_AMOUNT
        defaultCashPaymentVoucherExpenseShouldNotBeFound("transactionAmount.greaterThan=" + DEFAULT_TRANSACTION_AMOUNT);

        // Get all the cashPaymentVoucherExpenseList where transactionAmount is greater than SMALLER_TRANSACTION_AMOUNT
        defaultCashPaymentVoucherExpenseShouldBeFound("transactionAmount.greaterThan=" + SMALLER_TRANSACTION_AMOUNT);
    }


    @Test
    @Transactional
    public void getAllCashPaymentVoucherExpensesByLocationIsEqualToSomething() throws Exception {
        // Get already existing entity
        Location location = cashPaymentVoucherExpense.getLocation();
        cashPaymentVoucherExpenseRepository.saveAndFlush(cashPaymentVoucherExpense);
        Long locationId = location.getId();

        // Get all the cashPaymentVoucherExpenseList where location equals to locationId
        defaultCashPaymentVoucherExpenseShouldBeFound("locationId.equals=" + locationId);

        // Get all the cashPaymentVoucherExpenseList where location equals to locationId + 1
        defaultCashPaymentVoucherExpenseShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }


    @Test
    @Transactional
    public void getAllCashPaymentVoucherExpensesByTransactionTypeIsEqualToSomething() throws Exception {
        // Get already existing entity
        TransactionType transactionType = cashPaymentVoucherExpense.getTransactionType();
        cashPaymentVoucherExpenseRepository.saveAndFlush(cashPaymentVoucherExpense);
        Long transactionTypeId = transactionType.getId();

        // Get all the cashPaymentVoucherExpenseList where transactionType equals to transactionTypeId
        defaultCashPaymentVoucherExpenseShouldBeFound("transactionTypeId.equals=" + transactionTypeId);

        // Get all the cashPaymentVoucherExpenseList where transactionType equals to transactionTypeId + 1
        defaultCashPaymentVoucherExpenseShouldNotBeFound("transactionTypeId.equals=" + (transactionTypeId + 1));
    }


    @Test
    @Transactional
    public void getAllCashPaymentVoucherExpensesByExpenseIsEqualToSomething() throws Exception {
        // Get already existing entity
        Expense expense = cashPaymentVoucherExpense.getExpense();
        cashPaymentVoucherExpenseRepository.saveAndFlush(cashPaymentVoucherExpense);
        Long expenseId = expense.getId();

        // Get all the cashPaymentVoucherExpenseList where expense equals to expenseId
        defaultCashPaymentVoucherExpenseShouldBeFound("expenseId.equals=" + expenseId);

        // Get all the cashPaymentVoucherExpenseList where expense equals to expenseId + 1
        defaultCashPaymentVoucherExpenseShouldNotBeFound("expenseId.equals=" + (expenseId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCashPaymentVoucherExpenseShouldBeFound(String filter) throws Exception {
        restCashPaymentVoucherExpenseMockMvc.perform(get("/api/cash-payment-voucher-expenses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cashPaymentVoucherExpense.getId().intValue())))
            .andExpect(jsonPath("$.[*].transactionNumber").value(hasItem(DEFAULT_TRANSACTION_NUMBER)))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].transactionDescription").value(hasItem(DEFAULT_TRANSACTION_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].transactionAmount").value(hasItem(DEFAULT_TRANSACTION_AMOUNT.intValue())));

        // Check, that the count call also returns 1
        restCashPaymentVoucherExpenseMockMvc.perform(get("/api/cash-payment-voucher-expenses/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCashPaymentVoucherExpenseShouldNotBeFound(String filter) throws Exception {
        restCashPaymentVoucherExpenseMockMvc.perform(get("/api/cash-payment-voucher-expenses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCashPaymentVoucherExpenseMockMvc.perform(get("/api/cash-payment-voucher-expenses/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingCashPaymentVoucherExpense() throws Exception {
        // Get the cashPaymentVoucherExpense
        restCashPaymentVoucherExpenseMockMvc.perform(get("/api/cash-payment-voucher-expenses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCashPaymentVoucherExpense() throws Exception {
        // Initialize the database
        cashPaymentVoucherExpenseService.save(cashPaymentVoucherExpense);

        int databaseSizeBeforeUpdate = cashPaymentVoucherExpenseRepository.findAll().size();

        // Update the cashPaymentVoucherExpense
        CashPaymentVoucherExpense updatedCashPaymentVoucherExpense = cashPaymentVoucherExpenseRepository.findById(cashPaymentVoucherExpense.getId()).get();
        // Disconnect from session so that the updates on updatedCashPaymentVoucherExpense are not directly saved in db
        em.detach(updatedCashPaymentVoucherExpense);
        updatedCashPaymentVoucherExpense
            .transactionNumber(UPDATED_TRANSACTION_NUMBER)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .transactionDescription(UPDATED_TRANSACTION_DESCRIPTION)
            .transactionAmount(UPDATED_TRANSACTION_AMOUNT);

        restCashPaymentVoucherExpenseMockMvc.perform(put("/api/cash-payment-voucher-expenses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCashPaymentVoucherExpense)))
            .andExpect(status().isOk());

        // Validate the CashPaymentVoucherExpense in the database
        List<CashPaymentVoucherExpense> cashPaymentVoucherExpenseList = cashPaymentVoucherExpenseRepository.findAll();
        assertThat(cashPaymentVoucherExpenseList).hasSize(databaseSizeBeforeUpdate);
        CashPaymentVoucherExpense testCashPaymentVoucherExpense = cashPaymentVoucherExpenseList.get(cashPaymentVoucherExpenseList.size() - 1);
        assertThat(testCashPaymentVoucherExpense.getTransactionNumber()).isEqualTo(UPDATED_TRANSACTION_NUMBER);
        assertThat(testCashPaymentVoucherExpense.getTransactionDate()).isEqualTo(UPDATED_TRANSACTION_DATE);
        assertThat(testCashPaymentVoucherExpense.getTransactionDescription()).isEqualTo(UPDATED_TRANSACTION_DESCRIPTION);
        assertThat(testCashPaymentVoucherExpense.getTransactionAmount()).isEqualTo(UPDATED_TRANSACTION_AMOUNT);
    }

    @Test
    @Transactional
    public void updateNonExistingCashPaymentVoucherExpense() throws Exception {
        int databaseSizeBeforeUpdate = cashPaymentVoucherExpenseRepository.findAll().size();

        // Create the CashPaymentVoucherExpense

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCashPaymentVoucherExpenseMockMvc.perform(put("/api/cash-payment-voucher-expenses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashPaymentVoucherExpense)))
            .andExpect(status().isBadRequest());

        // Validate the CashPaymentVoucherExpense in the database
        List<CashPaymentVoucherExpense> cashPaymentVoucherExpenseList = cashPaymentVoucherExpenseRepository.findAll();
        assertThat(cashPaymentVoucherExpenseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCashPaymentVoucherExpense() throws Exception {
        // Initialize the database
        cashPaymentVoucherExpenseService.save(cashPaymentVoucherExpense);

        int databaseSizeBeforeDelete = cashPaymentVoucherExpenseRepository.findAll().size();

        // Delete the cashPaymentVoucherExpense
        restCashPaymentVoucherExpenseMockMvc.perform(delete("/api/cash-payment-voucher-expenses/{id}", cashPaymentVoucherExpense.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CashPaymentVoucherExpense> cashPaymentVoucherExpenseList = cashPaymentVoucherExpenseRepository.findAll();
        assertThat(cashPaymentVoucherExpenseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

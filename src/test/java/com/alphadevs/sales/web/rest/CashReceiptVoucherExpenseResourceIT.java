package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.WikunumApp;
import com.alphadevs.sales.domain.CashReceiptVoucherExpense;
import com.alphadevs.sales.domain.Location;
import com.alphadevs.sales.domain.TransactionType;
import com.alphadevs.sales.domain.Expense;
import com.alphadevs.sales.repository.CashReceiptVoucherExpenseRepository;
import com.alphadevs.sales.service.CashReceiptVoucherExpenseService;
import com.alphadevs.sales.web.rest.errors.ExceptionTranslator;
import com.alphadevs.sales.service.dto.CashReceiptVoucherExpenseCriteria;
import com.alphadevs.sales.service.CashReceiptVoucherExpenseQueryService;

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
 * Integration tests for the {@link CashReceiptVoucherExpenseResource} REST controller.
 */
@SpringBootTest(classes = WikunumApp.class)
public class CashReceiptVoucherExpenseResourceIT {

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
    private CashReceiptVoucherExpenseRepository cashReceiptVoucherExpenseRepository;

    @Autowired
    private CashReceiptVoucherExpenseService cashReceiptVoucherExpenseService;

    @Autowired
    private CashReceiptVoucherExpenseQueryService cashReceiptVoucherExpenseQueryService;

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

    private MockMvc restCashReceiptVoucherExpenseMockMvc;

    private CashReceiptVoucherExpense cashReceiptVoucherExpense;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CashReceiptVoucherExpenseResource cashReceiptVoucherExpenseResource = new CashReceiptVoucherExpenseResource(cashReceiptVoucherExpenseService, cashReceiptVoucherExpenseQueryService);
        this.restCashReceiptVoucherExpenseMockMvc = MockMvcBuilders.standaloneSetup(cashReceiptVoucherExpenseResource)
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
    public static CashReceiptVoucherExpense createEntity(EntityManager em) {
        CashReceiptVoucherExpense cashReceiptVoucherExpense = new CashReceiptVoucherExpense()
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
        cashReceiptVoucherExpense.setLocation(location);
        // Add required entity
        TransactionType transactionType;
        if (TestUtil.findAll(em, TransactionType.class).isEmpty()) {
            transactionType = TransactionTypeResourceIT.createEntity(em);
            em.persist(transactionType);
            em.flush();
        } else {
            transactionType = TestUtil.findAll(em, TransactionType.class).get(0);
        }
        cashReceiptVoucherExpense.setTransactionType(transactionType);
        // Add required entity
        Expense expense;
        if (TestUtil.findAll(em, Expense.class).isEmpty()) {
            expense = ExpenseResourceIT.createEntity(em);
            em.persist(expense);
            em.flush();
        } else {
            expense = TestUtil.findAll(em, Expense.class).get(0);
        }
        cashReceiptVoucherExpense.setExpense(expense);
        return cashReceiptVoucherExpense;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CashReceiptVoucherExpense createUpdatedEntity(EntityManager em) {
        CashReceiptVoucherExpense cashReceiptVoucherExpense = new CashReceiptVoucherExpense()
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
        cashReceiptVoucherExpense.setLocation(location);
        // Add required entity
        TransactionType transactionType;
        if (TestUtil.findAll(em, TransactionType.class).isEmpty()) {
            transactionType = TransactionTypeResourceIT.createUpdatedEntity(em);
            em.persist(transactionType);
            em.flush();
        } else {
            transactionType = TestUtil.findAll(em, TransactionType.class).get(0);
        }
        cashReceiptVoucherExpense.setTransactionType(transactionType);
        // Add required entity
        Expense expense;
        if (TestUtil.findAll(em, Expense.class).isEmpty()) {
            expense = ExpenseResourceIT.createUpdatedEntity(em);
            em.persist(expense);
            em.flush();
        } else {
            expense = TestUtil.findAll(em, Expense.class).get(0);
        }
        cashReceiptVoucherExpense.setExpense(expense);
        return cashReceiptVoucherExpense;
    }

    @BeforeEach
    public void initTest() {
        cashReceiptVoucherExpense = createEntity(em);
    }

    @Test
    @Transactional
    public void createCashReceiptVoucherExpense() throws Exception {
        int databaseSizeBeforeCreate = cashReceiptVoucherExpenseRepository.findAll().size();

        // Create the CashReceiptVoucherExpense
        restCashReceiptVoucherExpenseMockMvc.perform(post("/api/cash-receipt-voucher-expenses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashReceiptVoucherExpense)))
            .andExpect(status().isCreated());

        // Validate the CashReceiptVoucherExpense in the database
        List<CashReceiptVoucherExpense> cashReceiptVoucherExpenseList = cashReceiptVoucherExpenseRepository.findAll();
        assertThat(cashReceiptVoucherExpenseList).hasSize(databaseSizeBeforeCreate + 1);
        CashReceiptVoucherExpense testCashReceiptVoucherExpense = cashReceiptVoucherExpenseList.get(cashReceiptVoucherExpenseList.size() - 1);
        assertThat(testCashReceiptVoucherExpense.getTransactionNumber()).isEqualTo(DEFAULT_TRANSACTION_NUMBER);
        assertThat(testCashReceiptVoucherExpense.getTransactionDate()).isEqualTo(DEFAULT_TRANSACTION_DATE);
        assertThat(testCashReceiptVoucherExpense.getTransactionDescription()).isEqualTo(DEFAULT_TRANSACTION_DESCRIPTION);
        assertThat(testCashReceiptVoucherExpense.getTransactionAmount()).isEqualTo(DEFAULT_TRANSACTION_AMOUNT);
    }

    @Test
    @Transactional
    public void createCashReceiptVoucherExpenseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cashReceiptVoucherExpenseRepository.findAll().size();

        // Create the CashReceiptVoucherExpense with an existing ID
        cashReceiptVoucherExpense.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCashReceiptVoucherExpenseMockMvc.perform(post("/api/cash-receipt-voucher-expenses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashReceiptVoucherExpense)))
            .andExpect(status().isBadRequest());

        // Validate the CashReceiptVoucherExpense in the database
        List<CashReceiptVoucherExpense> cashReceiptVoucherExpenseList = cashReceiptVoucherExpenseRepository.findAll();
        assertThat(cashReceiptVoucherExpenseList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTransactionNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = cashReceiptVoucherExpenseRepository.findAll().size();
        // set the field null
        cashReceiptVoucherExpense.setTransactionNumber(null);

        // Create the CashReceiptVoucherExpense, which fails.

        restCashReceiptVoucherExpenseMockMvc.perform(post("/api/cash-receipt-voucher-expenses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashReceiptVoucherExpense)))
            .andExpect(status().isBadRequest());

        List<CashReceiptVoucherExpense> cashReceiptVoucherExpenseList = cashReceiptVoucherExpenseRepository.findAll();
        assertThat(cashReceiptVoucherExpenseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTransactionDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = cashReceiptVoucherExpenseRepository.findAll().size();
        // set the field null
        cashReceiptVoucherExpense.setTransactionDate(null);

        // Create the CashReceiptVoucherExpense, which fails.

        restCashReceiptVoucherExpenseMockMvc.perform(post("/api/cash-receipt-voucher-expenses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashReceiptVoucherExpense)))
            .andExpect(status().isBadRequest());

        List<CashReceiptVoucherExpense> cashReceiptVoucherExpenseList = cashReceiptVoucherExpenseRepository.findAll();
        assertThat(cashReceiptVoucherExpenseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTransactionDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = cashReceiptVoucherExpenseRepository.findAll().size();
        // set the field null
        cashReceiptVoucherExpense.setTransactionDescription(null);

        // Create the CashReceiptVoucherExpense, which fails.

        restCashReceiptVoucherExpenseMockMvc.perform(post("/api/cash-receipt-voucher-expenses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashReceiptVoucherExpense)))
            .andExpect(status().isBadRequest());

        List<CashReceiptVoucherExpense> cashReceiptVoucherExpenseList = cashReceiptVoucherExpenseRepository.findAll();
        assertThat(cashReceiptVoucherExpenseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTransactionAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = cashReceiptVoucherExpenseRepository.findAll().size();
        // set the field null
        cashReceiptVoucherExpense.setTransactionAmount(null);

        // Create the CashReceiptVoucherExpense, which fails.

        restCashReceiptVoucherExpenseMockMvc.perform(post("/api/cash-receipt-voucher-expenses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashReceiptVoucherExpense)))
            .andExpect(status().isBadRequest());

        List<CashReceiptVoucherExpense> cashReceiptVoucherExpenseList = cashReceiptVoucherExpenseRepository.findAll();
        assertThat(cashReceiptVoucherExpenseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCashReceiptVoucherExpenses() throws Exception {
        // Initialize the database
        cashReceiptVoucherExpenseRepository.saveAndFlush(cashReceiptVoucherExpense);

        // Get all the cashReceiptVoucherExpenseList
        restCashReceiptVoucherExpenseMockMvc.perform(get("/api/cash-receipt-voucher-expenses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cashReceiptVoucherExpense.getId().intValue())))
            .andExpect(jsonPath("$.[*].transactionNumber").value(hasItem(DEFAULT_TRANSACTION_NUMBER)))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].transactionDescription").value(hasItem(DEFAULT_TRANSACTION_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].transactionAmount").value(hasItem(DEFAULT_TRANSACTION_AMOUNT.intValue())));
    }
    
    @Test
    @Transactional
    public void getCashReceiptVoucherExpense() throws Exception {
        // Initialize the database
        cashReceiptVoucherExpenseRepository.saveAndFlush(cashReceiptVoucherExpense);

        // Get the cashReceiptVoucherExpense
        restCashReceiptVoucherExpenseMockMvc.perform(get("/api/cash-receipt-voucher-expenses/{id}", cashReceiptVoucherExpense.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cashReceiptVoucherExpense.getId().intValue()))
            .andExpect(jsonPath("$.transactionNumber").value(DEFAULT_TRANSACTION_NUMBER))
            .andExpect(jsonPath("$.transactionDate").value(DEFAULT_TRANSACTION_DATE.toString()))
            .andExpect(jsonPath("$.transactionDescription").value(DEFAULT_TRANSACTION_DESCRIPTION))
            .andExpect(jsonPath("$.transactionAmount").value(DEFAULT_TRANSACTION_AMOUNT.intValue()));
    }


    @Test
    @Transactional
    public void getCashReceiptVoucherExpensesByIdFiltering() throws Exception {
        // Initialize the database
        cashReceiptVoucherExpenseRepository.saveAndFlush(cashReceiptVoucherExpense);

        Long id = cashReceiptVoucherExpense.getId();

        defaultCashReceiptVoucherExpenseShouldBeFound("id.equals=" + id);
        defaultCashReceiptVoucherExpenseShouldNotBeFound("id.notEquals=" + id);

        defaultCashReceiptVoucherExpenseShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCashReceiptVoucherExpenseShouldNotBeFound("id.greaterThan=" + id);

        defaultCashReceiptVoucherExpenseShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCashReceiptVoucherExpenseShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllCashReceiptVoucherExpensesByTransactionNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        cashReceiptVoucherExpenseRepository.saveAndFlush(cashReceiptVoucherExpense);

        // Get all the cashReceiptVoucherExpenseList where transactionNumber equals to DEFAULT_TRANSACTION_NUMBER
        defaultCashReceiptVoucherExpenseShouldBeFound("transactionNumber.equals=" + DEFAULT_TRANSACTION_NUMBER);

        // Get all the cashReceiptVoucherExpenseList where transactionNumber equals to UPDATED_TRANSACTION_NUMBER
        defaultCashReceiptVoucherExpenseShouldNotBeFound("transactionNumber.equals=" + UPDATED_TRANSACTION_NUMBER);
    }

    @Test
    @Transactional
    public void getAllCashReceiptVoucherExpensesByTransactionNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cashReceiptVoucherExpenseRepository.saveAndFlush(cashReceiptVoucherExpense);

        // Get all the cashReceiptVoucherExpenseList where transactionNumber not equals to DEFAULT_TRANSACTION_NUMBER
        defaultCashReceiptVoucherExpenseShouldNotBeFound("transactionNumber.notEquals=" + DEFAULT_TRANSACTION_NUMBER);

        // Get all the cashReceiptVoucherExpenseList where transactionNumber not equals to UPDATED_TRANSACTION_NUMBER
        defaultCashReceiptVoucherExpenseShouldBeFound("transactionNumber.notEquals=" + UPDATED_TRANSACTION_NUMBER);
    }

    @Test
    @Transactional
    public void getAllCashReceiptVoucherExpensesByTransactionNumberIsInShouldWork() throws Exception {
        // Initialize the database
        cashReceiptVoucherExpenseRepository.saveAndFlush(cashReceiptVoucherExpense);

        // Get all the cashReceiptVoucherExpenseList where transactionNumber in DEFAULT_TRANSACTION_NUMBER or UPDATED_TRANSACTION_NUMBER
        defaultCashReceiptVoucherExpenseShouldBeFound("transactionNumber.in=" + DEFAULT_TRANSACTION_NUMBER + "," + UPDATED_TRANSACTION_NUMBER);

        // Get all the cashReceiptVoucherExpenseList where transactionNumber equals to UPDATED_TRANSACTION_NUMBER
        defaultCashReceiptVoucherExpenseShouldNotBeFound("transactionNumber.in=" + UPDATED_TRANSACTION_NUMBER);
    }

    @Test
    @Transactional
    public void getAllCashReceiptVoucherExpensesByTransactionNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        cashReceiptVoucherExpenseRepository.saveAndFlush(cashReceiptVoucherExpense);

        // Get all the cashReceiptVoucherExpenseList where transactionNumber is not null
        defaultCashReceiptVoucherExpenseShouldBeFound("transactionNumber.specified=true");

        // Get all the cashReceiptVoucherExpenseList where transactionNumber is null
        defaultCashReceiptVoucherExpenseShouldNotBeFound("transactionNumber.specified=false");
    }
                @Test
    @Transactional
    public void getAllCashReceiptVoucherExpensesByTransactionNumberContainsSomething() throws Exception {
        // Initialize the database
        cashReceiptVoucherExpenseRepository.saveAndFlush(cashReceiptVoucherExpense);

        // Get all the cashReceiptVoucherExpenseList where transactionNumber contains DEFAULT_TRANSACTION_NUMBER
        defaultCashReceiptVoucherExpenseShouldBeFound("transactionNumber.contains=" + DEFAULT_TRANSACTION_NUMBER);

        // Get all the cashReceiptVoucherExpenseList where transactionNumber contains UPDATED_TRANSACTION_NUMBER
        defaultCashReceiptVoucherExpenseShouldNotBeFound("transactionNumber.contains=" + UPDATED_TRANSACTION_NUMBER);
    }

    @Test
    @Transactional
    public void getAllCashReceiptVoucherExpensesByTransactionNumberNotContainsSomething() throws Exception {
        // Initialize the database
        cashReceiptVoucherExpenseRepository.saveAndFlush(cashReceiptVoucherExpense);

        // Get all the cashReceiptVoucherExpenseList where transactionNumber does not contain DEFAULT_TRANSACTION_NUMBER
        defaultCashReceiptVoucherExpenseShouldNotBeFound("transactionNumber.doesNotContain=" + DEFAULT_TRANSACTION_NUMBER);

        // Get all the cashReceiptVoucherExpenseList where transactionNumber does not contain UPDATED_TRANSACTION_NUMBER
        defaultCashReceiptVoucherExpenseShouldBeFound("transactionNumber.doesNotContain=" + UPDATED_TRANSACTION_NUMBER);
    }


    @Test
    @Transactional
    public void getAllCashReceiptVoucherExpensesByTransactionDateIsEqualToSomething() throws Exception {
        // Initialize the database
        cashReceiptVoucherExpenseRepository.saveAndFlush(cashReceiptVoucherExpense);

        // Get all the cashReceiptVoucherExpenseList where transactionDate equals to DEFAULT_TRANSACTION_DATE
        defaultCashReceiptVoucherExpenseShouldBeFound("transactionDate.equals=" + DEFAULT_TRANSACTION_DATE);

        // Get all the cashReceiptVoucherExpenseList where transactionDate equals to UPDATED_TRANSACTION_DATE
        defaultCashReceiptVoucherExpenseShouldNotBeFound("transactionDate.equals=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllCashReceiptVoucherExpensesByTransactionDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cashReceiptVoucherExpenseRepository.saveAndFlush(cashReceiptVoucherExpense);

        // Get all the cashReceiptVoucherExpenseList where transactionDate not equals to DEFAULT_TRANSACTION_DATE
        defaultCashReceiptVoucherExpenseShouldNotBeFound("transactionDate.notEquals=" + DEFAULT_TRANSACTION_DATE);

        // Get all the cashReceiptVoucherExpenseList where transactionDate not equals to UPDATED_TRANSACTION_DATE
        defaultCashReceiptVoucherExpenseShouldBeFound("transactionDate.notEquals=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllCashReceiptVoucherExpensesByTransactionDateIsInShouldWork() throws Exception {
        // Initialize the database
        cashReceiptVoucherExpenseRepository.saveAndFlush(cashReceiptVoucherExpense);

        // Get all the cashReceiptVoucherExpenseList where transactionDate in DEFAULT_TRANSACTION_DATE or UPDATED_TRANSACTION_DATE
        defaultCashReceiptVoucherExpenseShouldBeFound("transactionDate.in=" + DEFAULT_TRANSACTION_DATE + "," + UPDATED_TRANSACTION_DATE);

        // Get all the cashReceiptVoucherExpenseList where transactionDate equals to UPDATED_TRANSACTION_DATE
        defaultCashReceiptVoucherExpenseShouldNotBeFound("transactionDate.in=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllCashReceiptVoucherExpensesByTransactionDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        cashReceiptVoucherExpenseRepository.saveAndFlush(cashReceiptVoucherExpense);

        // Get all the cashReceiptVoucherExpenseList where transactionDate is not null
        defaultCashReceiptVoucherExpenseShouldBeFound("transactionDate.specified=true");

        // Get all the cashReceiptVoucherExpenseList where transactionDate is null
        defaultCashReceiptVoucherExpenseShouldNotBeFound("transactionDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllCashReceiptVoucherExpensesByTransactionDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cashReceiptVoucherExpenseRepository.saveAndFlush(cashReceiptVoucherExpense);

        // Get all the cashReceiptVoucherExpenseList where transactionDate is greater than or equal to DEFAULT_TRANSACTION_DATE
        defaultCashReceiptVoucherExpenseShouldBeFound("transactionDate.greaterThanOrEqual=" + DEFAULT_TRANSACTION_DATE);

        // Get all the cashReceiptVoucherExpenseList where transactionDate is greater than or equal to UPDATED_TRANSACTION_DATE
        defaultCashReceiptVoucherExpenseShouldNotBeFound("transactionDate.greaterThanOrEqual=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllCashReceiptVoucherExpensesByTransactionDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cashReceiptVoucherExpenseRepository.saveAndFlush(cashReceiptVoucherExpense);

        // Get all the cashReceiptVoucherExpenseList where transactionDate is less than or equal to DEFAULT_TRANSACTION_DATE
        defaultCashReceiptVoucherExpenseShouldBeFound("transactionDate.lessThanOrEqual=" + DEFAULT_TRANSACTION_DATE);

        // Get all the cashReceiptVoucherExpenseList where transactionDate is less than or equal to SMALLER_TRANSACTION_DATE
        defaultCashReceiptVoucherExpenseShouldNotBeFound("transactionDate.lessThanOrEqual=" + SMALLER_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllCashReceiptVoucherExpensesByTransactionDateIsLessThanSomething() throws Exception {
        // Initialize the database
        cashReceiptVoucherExpenseRepository.saveAndFlush(cashReceiptVoucherExpense);

        // Get all the cashReceiptVoucherExpenseList where transactionDate is less than DEFAULT_TRANSACTION_DATE
        defaultCashReceiptVoucherExpenseShouldNotBeFound("transactionDate.lessThan=" + DEFAULT_TRANSACTION_DATE);

        // Get all the cashReceiptVoucherExpenseList where transactionDate is less than UPDATED_TRANSACTION_DATE
        defaultCashReceiptVoucherExpenseShouldBeFound("transactionDate.lessThan=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllCashReceiptVoucherExpensesByTransactionDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cashReceiptVoucherExpenseRepository.saveAndFlush(cashReceiptVoucherExpense);

        // Get all the cashReceiptVoucherExpenseList where transactionDate is greater than DEFAULT_TRANSACTION_DATE
        defaultCashReceiptVoucherExpenseShouldNotBeFound("transactionDate.greaterThan=" + DEFAULT_TRANSACTION_DATE);

        // Get all the cashReceiptVoucherExpenseList where transactionDate is greater than SMALLER_TRANSACTION_DATE
        defaultCashReceiptVoucherExpenseShouldBeFound("transactionDate.greaterThan=" + SMALLER_TRANSACTION_DATE);
    }


    @Test
    @Transactional
    public void getAllCashReceiptVoucherExpensesByTransactionDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        cashReceiptVoucherExpenseRepository.saveAndFlush(cashReceiptVoucherExpense);

        // Get all the cashReceiptVoucherExpenseList where transactionDescription equals to DEFAULT_TRANSACTION_DESCRIPTION
        defaultCashReceiptVoucherExpenseShouldBeFound("transactionDescription.equals=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the cashReceiptVoucherExpenseList where transactionDescription equals to UPDATED_TRANSACTION_DESCRIPTION
        defaultCashReceiptVoucherExpenseShouldNotBeFound("transactionDescription.equals=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCashReceiptVoucherExpensesByTransactionDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cashReceiptVoucherExpenseRepository.saveAndFlush(cashReceiptVoucherExpense);

        // Get all the cashReceiptVoucherExpenseList where transactionDescription not equals to DEFAULT_TRANSACTION_DESCRIPTION
        defaultCashReceiptVoucherExpenseShouldNotBeFound("transactionDescription.notEquals=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the cashReceiptVoucherExpenseList where transactionDescription not equals to UPDATED_TRANSACTION_DESCRIPTION
        defaultCashReceiptVoucherExpenseShouldBeFound("transactionDescription.notEquals=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCashReceiptVoucherExpensesByTransactionDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        cashReceiptVoucherExpenseRepository.saveAndFlush(cashReceiptVoucherExpense);

        // Get all the cashReceiptVoucherExpenseList where transactionDescription in DEFAULT_TRANSACTION_DESCRIPTION or UPDATED_TRANSACTION_DESCRIPTION
        defaultCashReceiptVoucherExpenseShouldBeFound("transactionDescription.in=" + DEFAULT_TRANSACTION_DESCRIPTION + "," + UPDATED_TRANSACTION_DESCRIPTION);

        // Get all the cashReceiptVoucherExpenseList where transactionDescription equals to UPDATED_TRANSACTION_DESCRIPTION
        defaultCashReceiptVoucherExpenseShouldNotBeFound("transactionDescription.in=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCashReceiptVoucherExpensesByTransactionDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        cashReceiptVoucherExpenseRepository.saveAndFlush(cashReceiptVoucherExpense);

        // Get all the cashReceiptVoucherExpenseList where transactionDescription is not null
        defaultCashReceiptVoucherExpenseShouldBeFound("transactionDescription.specified=true");

        // Get all the cashReceiptVoucherExpenseList where transactionDescription is null
        defaultCashReceiptVoucherExpenseShouldNotBeFound("transactionDescription.specified=false");
    }
                @Test
    @Transactional
    public void getAllCashReceiptVoucherExpensesByTransactionDescriptionContainsSomething() throws Exception {
        // Initialize the database
        cashReceiptVoucherExpenseRepository.saveAndFlush(cashReceiptVoucherExpense);

        // Get all the cashReceiptVoucherExpenseList where transactionDescription contains DEFAULT_TRANSACTION_DESCRIPTION
        defaultCashReceiptVoucherExpenseShouldBeFound("transactionDescription.contains=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the cashReceiptVoucherExpenseList where transactionDescription contains UPDATED_TRANSACTION_DESCRIPTION
        defaultCashReceiptVoucherExpenseShouldNotBeFound("transactionDescription.contains=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCashReceiptVoucherExpensesByTransactionDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        cashReceiptVoucherExpenseRepository.saveAndFlush(cashReceiptVoucherExpense);

        // Get all the cashReceiptVoucherExpenseList where transactionDescription does not contain DEFAULT_TRANSACTION_DESCRIPTION
        defaultCashReceiptVoucherExpenseShouldNotBeFound("transactionDescription.doesNotContain=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the cashReceiptVoucherExpenseList where transactionDescription does not contain UPDATED_TRANSACTION_DESCRIPTION
        defaultCashReceiptVoucherExpenseShouldBeFound("transactionDescription.doesNotContain=" + UPDATED_TRANSACTION_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllCashReceiptVoucherExpensesByTransactionAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        cashReceiptVoucherExpenseRepository.saveAndFlush(cashReceiptVoucherExpense);

        // Get all the cashReceiptVoucherExpenseList where transactionAmount equals to DEFAULT_TRANSACTION_AMOUNT
        defaultCashReceiptVoucherExpenseShouldBeFound("transactionAmount.equals=" + DEFAULT_TRANSACTION_AMOUNT);

        // Get all the cashReceiptVoucherExpenseList where transactionAmount equals to UPDATED_TRANSACTION_AMOUNT
        defaultCashReceiptVoucherExpenseShouldNotBeFound("transactionAmount.equals=" + UPDATED_TRANSACTION_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllCashReceiptVoucherExpensesByTransactionAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cashReceiptVoucherExpenseRepository.saveAndFlush(cashReceiptVoucherExpense);

        // Get all the cashReceiptVoucherExpenseList where transactionAmount not equals to DEFAULT_TRANSACTION_AMOUNT
        defaultCashReceiptVoucherExpenseShouldNotBeFound("transactionAmount.notEquals=" + DEFAULT_TRANSACTION_AMOUNT);

        // Get all the cashReceiptVoucherExpenseList where transactionAmount not equals to UPDATED_TRANSACTION_AMOUNT
        defaultCashReceiptVoucherExpenseShouldBeFound("transactionAmount.notEquals=" + UPDATED_TRANSACTION_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllCashReceiptVoucherExpensesByTransactionAmountIsInShouldWork() throws Exception {
        // Initialize the database
        cashReceiptVoucherExpenseRepository.saveAndFlush(cashReceiptVoucherExpense);

        // Get all the cashReceiptVoucherExpenseList where transactionAmount in DEFAULT_TRANSACTION_AMOUNT or UPDATED_TRANSACTION_AMOUNT
        defaultCashReceiptVoucherExpenseShouldBeFound("transactionAmount.in=" + DEFAULT_TRANSACTION_AMOUNT + "," + UPDATED_TRANSACTION_AMOUNT);

        // Get all the cashReceiptVoucherExpenseList where transactionAmount equals to UPDATED_TRANSACTION_AMOUNT
        defaultCashReceiptVoucherExpenseShouldNotBeFound("transactionAmount.in=" + UPDATED_TRANSACTION_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllCashReceiptVoucherExpensesByTransactionAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        cashReceiptVoucherExpenseRepository.saveAndFlush(cashReceiptVoucherExpense);

        // Get all the cashReceiptVoucherExpenseList where transactionAmount is not null
        defaultCashReceiptVoucherExpenseShouldBeFound("transactionAmount.specified=true");

        // Get all the cashReceiptVoucherExpenseList where transactionAmount is null
        defaultCashReceiptVoucherExpenseShouldNotBeFound("transactionAmount.specified=false");
    }

    @Test
    @Transactional
    public void getAllCashReceiptVoucherExpensesByTransactionAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cashReceiptVoucherExpenseRepository.saveAndFlush(cashReceiptVoucherExpense);

        // Get all the cashReceiptVoucherExpenseList where transactionAmount is greater than or equal to DEFAULT_TRANSACTION_AMOUNT
        defaultCashReceiptVoucherExpenseShouldBeFound("transactionAmount.greaterThanOrEqual=" + DEFAULT_TRANSACTION_AMOUNT);

        // Get all the cashReceiptVoucherExpenseList where transactionAmount is greater than or equal to UPDATED_TRANSACTION_AMOUNT
        defaultCashReceiptVoucherExpenseShouldNotBeFound("transactionAmount.greaterThanOrEqual=" + UPDATED_TRANSACTION_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllCashReceiptVoucherExpensesByTransactionAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cashReceiptVoucherExpenseRepository.saveAndFlush(cashReceiptVoucherExpense);

        // Get all the cashReceiptVoucherExpenseList where transactionAmount is less than or equal to DEFAULT_TRANSACTION_AMOUNT
        defaultCashReceiptVoucherExpenseShouldBeFound("transactionAmount.lessThanOrEqual=" + DEFAULT_TRANSACTION_AMOUNT);

        // Get all the cashReceiptVoucherExpenseList where transactionAmount is less than or equal to SMALLER_TRANSACTION_AMOUNT
        defaultCashReceiptVoucherExpenseShouldNotBeFound("transactionAmount.lessThanOrEqual=" + SMALLER_TRANSACTION_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllCashReceiptVoucherExpensesByTransactionAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        cashReceiptVoucherExpenseRepository.saveAndFlush(cashReceiptVoucherExpense);

        // Get all the cashReceiptVoucherExpenseList where transactionAmount is less than DEFAULT_TRANSACTION_AMOUNT
        defaultCashReceiptVoucherExpenseShouldNotBeFound("transactionAmount.lessThan=" + DEFAULT_TRANSACTION_AMOUNT);

        // Get all the cashReceiptVoucherExpenseList where transactionAmount is less than UPDATED_TRANSACTION_AMOUNT
        defaultCashReceiptVoucherExpenseShouldBeFound("transactionAmount.lessThan=" + UPDATED_TRANSACTION_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllCashReceiptVoucherExpensesByTransactionAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cashReceiptVoucherExpenseRepository.saveAndFlush(cashReceiptVoucherExpense);

        // Get all the cashReceiptVoucherExpenseList where transactionAmount is greater than DEFAULT_TRANSACTION_AMOUNT
        defaultCashReceiptVoucherExpenseShouldNotBeFound("transactionAmount.greaterThan=" + DEFAULT_TRANSACTION_AMOUNT);

        // Get all the cashReceiptVoucherExpenseList where transactionAmount is greater than SMALLER_TRANSACTION_AMOUNT
        defaultCashReceiptVoucherExpenseShouldBeFound("transactionAmount.greaterThan=" + SMALLER_TRANSACTION_AMOUNT);
    }


    @Test
    @Transactional
    public void getAllCashReceiptVoucherExpensesByLocationIsEqualToSomething() throws Exception {
        // Get already existing entity
        Location location = cashReceiptVoucherExpense.getLocation();
        cashReceiptVoucherExpenseRepository.saveAndFlush(cashReceiptVoucherExpense);
        Long locationId = location.getId();

        // Get all the cashReceiptVoucherExpenseList where location equals to locationId
        defaultCashReceiptVoucherExpenseShouldBeFound("locationId.equals=" + locationId);

        // Get all the cashReceiptVoucherExpenseList where location equals to locationId + 1
        defaultCashReceiptVoucherExpenseShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }


    @Test
    @Transactional
    public void getAllCashReceiptVoucherExpensesByTransactionTypeIsEqualToSomething() throws Exception {
        // Get already existing entity
        TransactionType transactionType = cashReceiptVoucherExpense.getTransactionType();
        cashReceiptVoucherExpenseRepository.saveAndFlush(cashReceiptVoucherExpense);
        Long transactionTypeId = transactionType.getId();

        // Get all the cashReceiptVoucherExpenseList where transactionType equals to transactionTypeId
        defaultCashReceiptVoucherExpenseShouldBeFound("transactionTypeId.equals=" + transactionTypeId);

        // Get all the cashReceiptVoucherExpenseList where transactionType equals to transactionTypeId + 1
        defaultCashReceiptVoucherExpenseShouldNotBeFound("transactionTypeId.equals=" + (transactionTypeId + 1));
    }


    @Test
    @Transactional
    public void getAllCashReceiptVoucherExpensesByExpenseIsEqualToSomething() throws Exception {
        // Get already existing entity
        Expense expense = cashReceiptVoucherExpense.getExpense();
        cashReceiptVoucherExpenseRepository.saveAndFlush(cashReceiptVoucherExpense);
        Long expenseId = expense.getId();

        // Get all the cashReceiptVoucherExpenseList where expense equals to expenseId
        defaultCashReceiptVoucherExpenseShouldBeFound("expenseId.equals=" + expenseId);

        // Get all the cashReceiptVoucherExpenseList where expense equals to expenseId + 1
        defaultCashReceiptVoucherExpenseShouldNotBeFound("expenseId.equals=" + (expenseId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCashReceiptVoucherExpenseShouldBeFound(String filter) throws Exception {
        restCashReceiptVoucherExpenseMockMvc.perform(get("/api/cash-receipt-voucher-expenses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cashReceiptVoucherExpense.getId().intValue())))
            .andExpect(jsonPath("$.[*].transactionNumber").value(hasItem(DEFAULT_TRANSACTION_NUMBER)))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].transactionDescription").value(hasItem(DEFAULT_TRANSACTION_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].transactionAmount").value(hasItem(DEFAULT_TRANSACTION_AMOUNT.intValue())));

        // Check, that the count call also returns 1
        restCashReceiptVoucherExpenseMockMvc.perform(get("/api/cash-receipt-voucher-expenses/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCashReceiptVoucherExpenseShouldNotBeFound(String filter) throws Exception {
        restCashReceiptVoucherExpenseMockMvc.perform(get("/api/cash-receipt-voucher-expenses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCashReceiptVoucherExpenseMockMvc.perform(get("/api/cash-receipt-voucher-expenses/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingCashReceiptVoucherExpense() throws Exception {
        // Get the cashReceiptVoucherExpense
        restCashReceiptVoucherExpenseMockMvc.perform(get("/api/cash-receipt-voucher-expenses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCashReceiptVoucherExpense() throws Exception {
        // Initialize the database
        cashReceiptVoucherExpenseService.save(cashReceiptVoucherExpense);

        int databaseSizeBeforeUpdate = cashReceiptVoucherExpenseRepository.findAll().size();

        // Update the cashReceiptVoucherExpense
        CashReceiptVoucherExpense updatedCashReceiptVoucherExpense = cashReceiptVoucherExpenseRepository.findById(cashReceiptVoucherExpense.getId()).get();
        // Disconnect from session so that the updates on updatedCashReceiptVoucherExpense are not directly saved in db
        em.detach(updatedCashReceiptVoucherExpense);
        updatedCashReceiptVoucherExpense
            .transactionNumber(UPDATED_TRANSACTION_NUMBER)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .transactionDescription(UPDATED_TRANSACTION_DESCRIPTION)
            .transactionAmount(UPDATED_TRANSACTION_AMOUNT);

        restCashReceiptVoucherExpenseMockMvc.perform(put("/api/cash-receipt-voucher-expenses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCashReceiptVoucherExpense)))
            .andExpect(status().isOk());

        // Validate the CashReceiptVoucherExpense in the database
        List<CashReceiptVoucherExpense> cashReceiptVoucherExpenseList = cashReceiptVoucherExpenseRepository.findAll();
        assertThat(cashReceiptVoucherExpenseList).hasSize(databaseSizeBeforeUpdate);
        CashReceiptVoucherExpense testCashReceiptVoucherExpense = cashReceiptVoucherExpenseList.get(cashReceiptVoucherExpenseList.size() - 1);
        assertThat(testCashReceiptVoucherExpense.getTransactionNumber()).isEqualTo(UPDATED_TRANSACTION_NUMBER);
        assertThat(testCashReceiptVoucherExpense.getTransactionDate()).isEqualTo(UPDATED_TRANSACTION_DATE);
        assertThat(testCashReceiptVoucherExpense.getTransactionDescription()).isEqualTo(UPDATED_TRANSACTION_DESCRIPTION);
        assertThat(testCashReceiptVoucherExpense.getTransactionAmount()).isEqualTo(UPDATED_TRANSACTION_AMOUNT);
    }

    @Test
    @Transactional
    public void updateNonExistingCashReceiptVoucherExpense() throws Exception {
        int databaseSizeBeforeUpdate = cashReceiptVoucherExpenseRepository.findAll().size();

        // Create the CashReceiptVoucherExpense

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCashReceiptVoucherExpenseMockMvc.perform(put("/api/cash-receipt-voucher-expenses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashReceiptVoucherExpense)))
            .andExpect(status().isBadRequest());

        // Validate the CashReceiptVoucherExpense in the database
        List<CashReceiptVoucherExpense> cashReceiptVoucherExpenseList = cashReceiptVoucherExpenseRepository.findAll();
        assertThat(cashReceiptVoucherExpenseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCashReceiptVoucherExpense() throws Exception {
        // Initialize the database
        cashReceiptVoucherExpenseService.save(cashReceiptVoucherExpense);

        int databaseSizeBeforeDelete = cashReceiptVoucherExpenseRepository.findAll().size();

        // Delete the cashReceiptVoucherExpense
        restCashReceiptVoucherExpenseMockMvc.perform(delete("/api/cash-receipt-voucher-expenses/{id}", cashReceiptVoucherExpense.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CashReceiptVoucherExpense> cashReceiptVoucherExpenseList = cashReceiptVoucherExpenseRepository.findAll();
        assertThat(cashReceiptVoucherExpenseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

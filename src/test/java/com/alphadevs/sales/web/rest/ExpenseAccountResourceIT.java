package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.WikunumApp;
import com.alphadevs.sales.domain.ExpenseAccount;
import com.alphadevs.sales.domain.Location;
import com.alphadevs.sales.domain.TransactionType;
import com.alphadevs.sales.domain.Expense;
import com.alphadevs.sales.repository.ExpenseAccountRepository;
import com.alphadevs.sales.service.ExpenseAccountService;
import com.alphadevs.sales.web.rest.errors.ExceptionTranslator;
import com.alphadevs.sales.service.dto.ExpenseAccountCriteria;
import com.alphadevs.sales.service.ExpenseAccountQueryService;

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
 * Integration tests for the {@link ExpenseAccountResource} REST controller.
 */
@SpringBootTest(classes = WikunumApp.class)
public class ExpenseAccountResourceIT {

    private static final LocalDate DEFAULT_TRANSACTION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TRANSACTION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_TRANSACTION_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_TRANSACTION_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_TRANSACTION_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_TRANSACTION_AMOUNT_DR = new BigDecimal(1);
    private static final BigDecimal UPDATED_TRANSACTION_AMOUNT_DR = new BigDecimal(2);
    private static final BigDecimal SMALLER_TRANSACTION_AMOUNT_DR = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_TRANSACTION_AMOUNT_CR = new BigDecimal(1);
    private static final BigDecimal UPDATED_TRANSACTION_AMOUNT_CR = new BigDecimal(2);
    private static final BigDecimal SMALLER_TRANSACTION_AMOUNT_CR = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_TRANSACTION_BALANCE = new BigDecimal(1);
    private static final BigDecimal UPDATED_TRANSACTION_BALANCE = new BigDecimal(2);
    private static final BigDecimal SMALLER_TRANSACTION_BALANCE = new BigDecimal(1 - 1);

    @Autowired
    private ExpenseAccountRepository expenseAccountRepository;

    @Autowired
    private ExpenseAccountService expenseAccountService;

    @Autowired
    private ExpenseAccountQueryService expenseAccountQueryService;

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

    private MockMvc restExpenseAccountMockMvc;

    private ExpenseAccount expenseAccount;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ExpenseAccountResource expenseAccountResource = new ExpenseAccountResource(expenseAccountService, expenseAccountQueryService);
        this.restExpenseAccountMockMvc = MockMvcBuilders.standaloneSetup(expenseAccountResource)
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
    public static ExpenseAccount createEntity(EntityManager em) {
        ExpenseAccount expenseAccount = new ExpenseAccount()
            .transactionDate(DEFAULT_TRANSACTION_DATE)
            .transactionDescription(DEFAULT_TRANSACTION_DESCRIPTION)
            .transactionAmountDR(DEFAULT_TRANSACTION_AMOUNT_DR)
            .transactionAmountCR(DEFAULT_TRANSACTION_AMOUNT_CR)
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
        expenseAccount.setLocation(location);
        // Add required entity
        TransactionType transactionType;
        if (TestUtil.findAll(em, TransactionType.class).isEmpty()) {
            transactionType = TransactionTypeResourceIT.createEntity(em);
            em.persist(transactionType);
            em.flush();
        } else {
            transactionType = TestUtil.findAll(em, TransactionType.class).get(0);
        }
        expenseAccount.setTransactionType(transactionType);
        // Add required entity
        Expense expense;
        if (TestUtil.findAll(em, Expense.class).isEmpty()) {
            expense = ExpenseResourceIT.createEntity(em);
            em.persist(expense);
            em.flush();
        } else {
            expense = TestUtil.findAll(em, Expense.class).get(0);
        }
        expenseAccount.setExpense(expense);
        return expenseAccount;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExpenseAccount createUpdatedEntity(EntityManager em) {
        ExpenseAccount expenseAccount = new ExpenseAccount()
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .transactionDescription(UPDATED_TRANSACTION_DESCRIPTION)
            .transactionAmountDR(UPDATED_TRANSACTION_AMOUNT_DR)
            .transactionAmountCR(UPDATED_TRANSACTION_AMOUNT_CR)
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
        expenseAccount.setLocation(location);
        // Add required entity
        TransactionType transactionType;
        if (TestUtil.findAll(em, TransactionType.class).isEmpty()) {
            transactionType = TransactionTypeResourceIT.createUpdatedEntity(em);
            em.persist(transactionType);
            em.flush();
        } else {
            transactionType = TestUtil.findAll(em, TransactionType.class).get(0);
        }
        expenseAccount.setTransactionType(transactionType);
        // Add required entity
        Expense expense;
        if (TestUtil.findAll(em, Expense.class).isEmpty()) {
            expense = ExpenseResourceIT.createUpdatedEntity(em);
            em.persist(expense);
            em.flush();
        } else {
            expense = TestUtil.findAll(em, Expense.class).get(0);
        }
        expenseAccount.setExpense(expense);
        return expenseAccount;
    }

    @BeforeEach
    public void initTest() {
        expenseAccount = createEntity(em);
    }

    @Test
    @Transactional
    public void createExpenseAccount() throws Exception {
        int databaseSizeBeforeCreate = expenseAccountRepository.findAll().size();

        // Create the ExpenseAccount
        restExpenseAccountMockMvc.perform(post("/api/expense-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(expenseAccount)))
            .andExpect(status().isCreated());

        // Validate the ExpenseAccount in the database
        List<ExpenseAccount> expenseAccountList = expenseAccountRepository.findAll();
        assertThat(expenseAccountList).hasSize(databaseSizeBeforeCreate + 1);
        ExpenseAccount testExpenseAccount = expenseAccountList.get(expenseAccountList.size() - 1);
        assertThat(testExpenseAccount.getTransactionDate()).isEqualTo(DEFAULT_TRANSACTION_DATE);
        assertThat(testExpenseAccount.getTransactionDescription()).isEqualTo(DEFAULT_TRANSACTION_DESCRIPTION);
        assertThat(testExpenseAccount.getTransactionAmountDR()).isEqualTo(DEFAULT_TRANSACTION_AMOUNT_DR);
        assertThat(testExpenseAccount.getTransactionAmountCR()).isEqualTo(DEFAULT_TRANSACTION_AMOUNT_CR);
        assertThat(testExpenseAccount.getTransactionBalance()).isEqualTo(DEFAULT_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void createExpenseAccountWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = expenseAccountRepository.findAll().size();

        // Create the ExpenseAccount with an existing ID
        expenseAccount.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restExpenseAccountMockMvc.perform(post("/api/expense-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(expenseAccount)))
            .andExpect(status().isBadRequest());

        // Validate the ExpenseAccount in the database
        List<ExpenseAccount> expenseAccountList = expenseAccountRepository.findAll();
        assertThat(expenseAccountList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTransactionDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = expenseAccountRepository.findAll().size();
        // set the field null
        expenseAccount.setTransactionDate(null);

        // Create the ExpenseAccount, which fails.

        restExpenseAccountMockMvc.perform(post("/api/expense-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(expenseAccount)))
            .andExpect(status().isBadRequest());

        List<ExpenseAccount> expenseAccountList = expenseAccountRepository.findAll();
        assertThat(expenseAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTransactionDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = expenseAccountRepository.findAll().size();
        // set the field null
        expenseAccount.setTransactionDescription(null);

        // Create the ExpenseAccount, which fails.

        restExpenseAccountMockMvc.perform(post("/api/expense-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(expenseAccount)))
            .andExpect(status().isBadRequest());

        List<ExpenseAccount> expenseAccountList = expenseAccountRepository.findAll();
        assertThat(expenseAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTransactionAmountDRIsRequired() throws Exception {
        int databaseSizeBeforeTest = expenseAccountRepository.findAll().size();
        // set the field null
        expenseAccount.setTransactionAmountDR(null);

        // Create the ExpenseAccount, which fails.

        restExpenseAccountMockMvc.perform(post("/api/expense-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(expenseAccount)))
            .andExpect(status().isBadRequest());

        List<ExpenseAccount> expenseAccountList = expenseAccountRepository.findAll();
        assertThat(expenseAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTransactionAmountCRIsRequired() throws Exception {
        int databaseSizeBeforeTest = expenseAccountRepository.findAll().size();
        // set the field null
        expenseAccount.setTransactionAmountCR(null);

        // Create the ExpenseAccount, which fails.

        restExpenseAccountMockMvc.perform(post("/api/expense-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(expenseAccount)))
            .andExpect(status().isBadRequest());

        List<ExpenseAccount> expenseAccountList = expenseAccountRepository.findAll();
        assertThat(expenseAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTransactionBalanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = expenseAccountRepository.findAll().size();
        // set the field null
        expenseAccount.setTransactionBalance(null);

        // Create the ExpenseAccount, which fails.

        restExpenseAccountMockMvc.perform(post("/api/expense-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(expenseAccount)))
            .andExpect(status().isBadRequest());

        List<ExpenseAccount> expenseAccountList = expenseAccountRepository.findAll();
        assertThat(expenseAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllExpenseAccounts() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList
        restExpenseAccountMockMvc.perform(get("/api/expense-accounts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(expenseAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].transactionDescription").value(hasItem(DEFAULT_TRANSACTION_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].transactionAmountDR").value(hasItem(DEFAULT_TRANSACTION_AMOUNT_DR.intValue())))
            .andExpect(jsonPath("$.[*].transactionAmountCR").value(hasItem(DEFAULT_TRANSACTION_AMOUNT_CR.intValue())))
            .andExpect(jsonPath("$.[*].transactionBalance").value(hasItem(DEFAULT_TRANSACTION_BALANCE.intValue())));
    }
    
    @Test
    @Transactional
    public void getExpenseAccount() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get the expenseAccount
        restExpenseAccountMockMvc.perform(get("/api/expense-accounts/{id}", expenseAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(expenseAccount.getId().intValue()))
            .andExpect(jsonPath("$.transactionDate").value(DEFAULT_TRANSACTION_DATE.toString()))
            .andExpect(jsonPath("$.transactionDescription").value(DEFAULT_TRANSACTION_DESCRIPTION))
            .andExpect(jsonPath("$.transactionAmountDR").value(DEFAULT_TRANSACTION_AMOUNT_DR.intValue()))
            .andExpect(jsonPath("$.transactionAmountCR").value(DEFAULT_TRANSACTION_AMOUNT_CR.intValue()))
            .andExpect(jsonPath("$.transactionBalance").value(DEFAULT_TRANSACTION_BALANCE.intValue()));
    }


    @Test
    @Transactional
    public void getExpenseAccountsByIdFiltering() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        Long id = expenseAccount.getId();

        defaultExpenseAccountShouldBeFound("id.equals=" + id);
        defaultExpenseAccountShouldNotBeFound("id.notEquals=" + id);

        defaultExpenseAccountShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultExpenseAccountShouldNotBeFound("id.greaterThan=" + id);

        defaultExpenseAccountShouldBeFound("id.lessThanOrEqual=" + id);
        defaultExpenseAccountShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllExpenseAccountsByTransactionDateIsEqualToSomething() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionDate equals to DEFAULT_TRANSACTION_DATE
        defaultExpenseAccountShouldBeFound("transactionDate.equals=" + DEFAULT_TRANSACTION_DATE);

        // Get all the expenseAccountList where transactionDate equals to UPDATED_TRANSACTION_DATE
        defaultExpenseAccountShouldNotBeFound("transactionDate.equals=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllExpenseAccountsByTransactionDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionDate not equals to DEFAULT_TRANSACTION_DATE
        defaultExpenseAccountShouldNotBeFound("transactionDate.notEquals=" + DEFAULT_TRANSACTION_DATE);

        // Get all the expenseAccountList where transactionDate not equals to UPDATED_TRANSACTION_DATE
        defaultExpenseAccountShouldBeFound("transactionDate.notEquals=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllExpenseAccountsByTransactionDateIsInShouldWork() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionDate in DEFAULT_TRANSACTION_DATE or UPDATED_TRANSACTION_DATE
        defaultExpenseAccountShouldBeFound("transactionDate.in=" + DEFAULT_TRANSACTION_DATE + "," + UPDATED_TRANSACTION_DATE);

        // Get all the expenseAccountList where transactionDate equals to UPDATED_TRANSACTION_DATE
        defaultExpenseAccountShouldNotBeFound("transactionDate.in=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllExpenseAccountsByTransactionDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionDate is not null
        defaultExpenseAccountShouldBeFound("transactionDate.specified=true");

        // Get all the expenseAccountList where transactionDate is null
        defaultExpenseAccountShouldNotBeFound("transactionDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllExpenseAccountsByTransactionDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionDate is greater than or equal to DEFAULT_TRANSACTION_DATE
        defaultExpenseAccountShouldBeFound("transactionDate.greaterThanOrEqual=" + DEFAULT_TRANSACTION_DATE);

        // Get all the expenseAccountList where transactionDate is greater than or equal to UPDATED_TRANSACTION_DATE
        defaultExpenseAccountShouldNotBeFound("transactionDate.greaterThanOrEqual=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllExpenseAccountsByTransactionDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionDate is less than or equal to DEFAULT_TRANSACTION_DATE
        defaultExpenseAccountShouldBeFound("transactionDate.lessThanOrEqual=" + DEFAULT_TRANSACTION_DATE);

        // Get all the expenseAccountList where transactionDate is less than or equal to SMALLER_TRANSACTION_DATE
        defaultExpenseAccountShouldNotBeFound("transactionDate.lessThanOrEqual=" + SMALLER_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllExpenseAccountsByTransactionDateIsLessThanSomething() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionDate is less than DEFAULT_TRANSACTION_DATE
        defaultExpenseAccountShouldNotBeFound("transactionDate.lessThan=" + DEFAULT_TRANSACTION_DATE);

        // Get all the expenseAccountList where transactionDate is less than UPDATED_TRANSACTION_DATE
        defaultExpenseAccountShouldBeFound("transactionDate.lessThan=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllExpenseAccountsByTransactionDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionDate is greater than DEFAULT_TRANSACTION_DATE
        defaultExpenseAccountShouldNotBeFound("transactionDate.greaterThan=" + DEFAULT_TRANSACTION_DATE);

        // Get all the expenseAccountList where transactionDate is greater than SMALLER_TRANSACTION_DATE
        defaultExpenseAccountShouldBeFound("transactionDate.greaterThan=" + SMALLER_TRANSACTION_DATE);
    }


    @Test
    @Transactional
    public void getAllExpenseAccountsByTransactionDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionDescription equals to DEFAULT_TRANSACTION_DESCRIPTION
        defaultExpenseAccountShouldBeFound("transactionDescription.equals=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the expenseAccountList where transactionDescription equals to UPDATED_TRANSACTION_DESCRIPTION
        defaultExpenseAccountShouldNotBeFound("transactionDescription.equals=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllExpenseAccountsByTransactionDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionDescription not equals to DEFAULT_TRANSACTION_DESCRIPTION
        defaultExpenseAccountShouldNotBeFound("transactionDescription.notEquals=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the expenseAccountList where transactionDescription not equals to UPDATED_TRANSACTION_DESCRIPTION
        defaultExpenseAccountShouldBeFound("transactionDescription.notEquals=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllExpenseAccountsByTransactionDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionDescription in DEFAULT_TRANSACTION_DESCRIPTION or UPDATED_TRANSACTION_DESCRIPTION
        defaultExpenseAccountShouldBeFound("transactionDescription.in=" + DEFAULT_TRANSACTION_DESCRIPTION + "," + UPDATED_TRANSACTION_DESCRIPTION);

        // Get all the expenseAccountList where transactionDescription equals to UPDATED_TRANSACTION_DESCRIPTION
        defaultExpenseAccountShouldNotBeFound("transactionDescription.in=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllExpenseAccountsByTransactionDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionDescription is not null
        defaultExpenseAccountShouldBeFound("transactionDescription.specified=true");

        // Get all the expenseAccountList where transactionDescription is null
        defaultExpenseAccountShouldNotBeFound("transactionDescription.specified=false");
    }
                @Test
    @Transactional
    public void getAllExpenseAccountsByTransactionDescriptionContainsSomething() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionDescription contains DEFAULT_TRANSACTION_DESCRIPTION
        defaultExpenseAccountShouldBeFound("transactionDescription.contains=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the expenseAccountList where transactionDescription contains UPDATED_TRANSACTION_DESCRIPTION
        defaultExpenseAccountShouldNotBeFound("transactionDescription.contains=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllExpenseAccountsByTransactionDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionDescription does not contain DEFAULT_TRANSACTION_DESCRIPTION
        defaultExpenseAccountShouldNotBeFound("transactionDescription.doesNotContain=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the expenseAccountList where transactionDescription does not contain UPDATED_TRANSACTION_DESCRIPTION
        defaultExpenseAccountShouldBeFound("transactionDescription.doesNotContain=" + UPDATED_TRANSACTION_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllExpenseAccountsByTransactionAmountDRIsEqualToSomething() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionAmountDR equals to DEFAULT_TRANSACTION_AMOUNT_DR
        defaultExpenseAccountShouldBeFound("transactionAmountDR.equals=" + DEFAULT_TRANSACTION_AMOUNT_DR);

        // Get all the expenseAccountList where transactionAmountDR equals to UPDATED_TRANSACTION_AMOUNT_DR
        defaultExpenseAccountShouldNotBeFound("transactionAmountDR.equals=" + UPDATED_TRANSACTION_AMOUNT_DR);
    }

    @Test
    @Transactional
    public void getAllExpenseAccountsByTransactionAmountDRIsNotEqualToSomething() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionAmountDR not equals to DEFAULT_TRANSACTION_AMOUNT_DR
        defaultExpenseAccountShouldNotBeFound("transactionAmountDR.notEquals=" + DEFAULT_TRANSACTION_AMOUNT_DR);

        // Get all the expenseAccountList where transactionAmountDR not equals to UPDATED_TRANSACTION_AMOUNT_DR
        defaultExpenseAccountShouldBeFound("transactionAmountDR.notEquals=" + UPDATED_TRANSACTION_AMOUNT_DR);
    }

    @Test
    @Transactional
    public void getAllExpenseAccountsByTransactionAmountDRIsInShouldWork() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionAmountDR in DEFAULT_TRANSACTION_AMOUNT_DR or UPDATED_TRANSACTION_AMOUNT_DR
        defaultExpenseAccountShouldBeFound("transactionAmountDR.in=" + DEFAULT_TRANSACTION_AMOUNT_DR + "," + UPDATED_TRANSACTION_AMOUNT_DR);

        // Get all the expenseAccountList where transactionAmountDR equals to UPDATED_TRANSACTION_AMOUNT_DR
        defaultExpenseAccountShouldNotBeFound("transactionAmountDR.in=" + UPDATED_TRANSACTION_AMOUNT_DR);
    }

    @Test
    @Transactional
    public void getAllExpenseAccountsByTransactionAmountDRIsNullOrNotNull() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionAmountDR is not null
        defaultExpenseAccountShouldBeFound("transactionAmountDR.specified=true");

        // Get all the expenseAccountList where transactionAmountDR is null
        defaultExpenseAccountShouldNotBeFound("transactionAmountDR.specified=false");
    }

    @Test
    @Transactional
    public void getAllExpenseAccountsByTransactionAmountDRIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionAmountDR is greater than or equal to DEFAULT_TRANSACTION_AMOUNT_DR
        defaultExpenseAccountShouldBeFound("transactionAmountDR.greaterThanOrEqual=" + DEFAULT_TRANSACTION_AMOUNT_DR);

        // Get all the expenseAccountList where transactionAmountDR is greater than or equal to UPDATED_TRANSACTION_AMOUNT_DR
        defaultExpenseAccountShouldNotBeFound("transactionAmountDR.greaterThanOrEqual=" + UPDATED_TRANSACTION_AMOUNT_DR);
    }

    @Test
    @Transactional
    public void getAllExpenseAccountsByTransactionAmountDRIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionAmountDR is less than or equal to DEFAULT_TRANSACTION_AMOUNT_DR
        defaultExpenseAccountShouldBeFound("transactionAmountDR.lessThanOrEqual=" + DEFAULT_TRANSACTION_AMOUNT_DR);

        // Get all the expenseAccountList where transactionAmountDR is less than or equal to SMALLER_TRANSACTION_AMOUNT_DR
        defaultExpenseAccountShouldNotBeFound("transactionAmountDR.lessThanOrEqual=" + SMALLER_TRANSACTION_AMOUNT_DR);
    }

    @Test
    @Transactional
    public void getAllExpenseAccountsByTransactionAmountDRIsLessThanSomething() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionAmountDR is less than DEFAULT_TRANSACTION_AMOUNT_DR
        defaultExpenseAccountShouldNotBeFound("transactionAmountDR.lessThan=" + DEFAULT_TRANSACTION_AMOUNT_DR);

        // Get all the expenseAccountList where transactionAmountDR is less than UPDATED_TRANSACTION_AMOUNT_DR
        defaultExpenseAccountShouldBeFound("transactionAmountDR.lessThan=" + UPDATED_TRANSACTION_AMOUNT_DR);
    }

    @Test
    @Transactional
    public void getAllExpenseAccountsByTransactionAmountDRIsGreaterThanSomething() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionAmountDR is greater than DEFAULT_TRANSACTION_AMOUNT_DR
        defaultExpenseAccountShouldNotBeFound("transactionAmountDR.greaterThan=" + DEFAULT_TRANSACTION_AMOUNT_DR);

        // Get all the expenseAccountList where transactionAmountDR is greater than SMALLER_TRANSACTION_AMOUNT_DR
        defaultExpenseAccountShouldBeFound("transactionAmountDR.greaterThan=" + SMALLER_TRANSACTION_AMOUNT_DR);
    }


    @Test
    @Transactional
    public void getAllExpenseAccountsByTransactionAmountCRIsEqualToSomething() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionAmountCR equals to DEFAULT_TRANSACTION_AMOUNT_CR
        defaultExpenseAccountShouldBeFound("transactionAmountCR.equals=" + DEFAULT_TRANSACTION_AMOUNT_CR);

        // Get all the expenseAccountList where transactionAmountCR equals to UPDATED_TRANSACTION_AMOUNT_CR
        defaultExpenseAccountShouldNotBeFound("transactionAmountCR.equals=" + UPDATED_TRANSACTION_AMOUNT_CR);
    }

    @Test
    @Transactional
    public void getAllExpenseAccountsByTransactionAmountCRIsNotEqualToSomething() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionAmountCR not equals to DEFAULT_TRANSACTION_AMOUNT_CR
        defaultExpenseAccountShouldNotBeFound("transactionAmountCR.notEquals=" + DEFAULT_TRANSACTION_AMOUNT_CR);

        // Get all the expenseAccountList where transactionAmountCR not equals to UPDATED_TRANSACTION_AMOUNT_CR
        defaultExpenseAccountShouldBeFound("transactionAmountCR.notEquals=" + UPDATED_TRANSACTION_AMOUNT_CR);
    }

    @Test
    @Transactional
    public void getAllExpenseAccountsByTransactionAmountCRIsInShouldWork() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionAmountCR in DEFAULT_TRANSACTION_AMOUNT_CR or UPDATED_TRANSACTION_AMOUNT_CR
        defaultExpenseAccountShouldBeFound("transactionAmountCR.in=" + DEFAULT_TRANSACTION_AMOUNT_CR + "," + UPDATED_TRANSACTION_AMOUNT_CR);

        // Get all the expenseAccountList where transactionAmountCR equals to UPDATED_TRANSACTION_AMOUNT_CR
        defaultExpenseAccountShouldNotBeFound("transactionAmountCR.in=" + UPDATED_TRANSACTION_AMOUNT_CR);
    }

    @Test
    @Transactional
    public void getAllExpenseAccountsByTransactionAmountCRIsNullOrNotNull() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionAmountCR is not null
        defaultExpenseAccountShouldBeFound("transactionAmountCR.specified=true");

        // Get all the expenseAccountList where transactionAmountCR is null
        defaultExpenseAccountShouldNotBeFound("transactionAmountCR.specified=false");
    }

    @Test
    @Transactional
    public void getAllExpenseAccountsByTransactionAmountCRIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionAmountCR is greater than or equal to DEFAULT_TRANSACTION_AMOUNT_CR
        defaultExpenseAccountShouldBeFound("transactionAmountCR.greaterThanOrEqual=" + DEFAULT_TRANSACTION_AMOUNT_CR);

        // Get all the expenseAccountList where transactionAmountCR is greater than or equal to UPDATED_TRANSACTION_AMOUNT_CR
        defaultExpenseAccountShouldNotBeFound("transactionAmountCR.greaterThanOrEqual=" + UPDATED_TRANSACTION_AMOUNT_CR);
    }

    @Test
    @Transactional
    public void getAllExpenseAccountsByTransactionAmountCRIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionAmountCR is less than or equal to DEFAULT_TRANSACTION_AMOUNT_CR
        defaultExpenseAccountShouldBeFound("transactionAmountCR.lessThanOrEqual=" + DEFAULT_TRANSACTION_AMOUNT_CR);

        // Get all the expenseAccountList where transactionAmountCR is less than or equal to SMALLER_TRANSACTION_AMOUNT_CR
        defaultExpenseAccountShouldNotBeFound("transactionAmountCR.lessThanOrEqual=" + SMALLER_TRANSACTION_AMOUNT_CR);
    }

    @Test
    @Transactional
    public void getAllExpenseAccountsByTransactionAmountCRIsLessThanSomething() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionAmountCR is less than DEFAULT_TRANSACTION_AMOUNT_CR
        defaultExpenseAccountShouldNotBeFound("transactionAmountCR.lessThan=" + DEFAULT_TRANSACTION_AMOUNT_CR);

        // Get all the expenseAccountList where transactionAmountCR is less than UPDATED_TRANSACTION_AMOUNT_CR
        defaultExpenseAccountShouldBeFound("transactionAmountCR.lessThan=" + UPDATED_TRANSACTION_AMOUNT_CR);
    }

    @Test
    @Transactional
    public void getAllExpenseAccountsByTransactionAmountCRIsGreaterThanSomething() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionAmountCR is greater than DEFAULT_TRANSACTION_AMOUNT_CR
        defaultExpenseAccountShouldNotBeFound("transactionAmountCR.greaterThan=" + DEFAULT_TRANSACTION_AMOUNT_CR);

        // Get all the expenseAccountList where transactionAmountCR is greater than SMALLER_TRANSACTION_AMOUNT_CR
        defaultExpenseAccountShouldBeFound("transactionAmountCR.greaterThan=" + SMALLER_TRANSACTION_AMOUNT_CR);
    }


    @Test
    @Transactional
    public void getAllExpenseAccountsByTransactionBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionBalance equals to DEFAULT_TRANSACTION_BALANCE
        defaultExpenseAccountShouldBeFound("transactionBalance.equals=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the expenseAccountList where transactionBalance equals to UPDATED_TRANSACTION_BALANCE
        defaultExpenseAccountShouldNotBeFound("transactionBalance.equals=" + UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void getAllExpenseAccountsByTransactionBalanceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionBalance not equals to DEFAULT_TRANSACTION_BALANCE
        defaultExpenseAccountShouldNotBeFound("transactionBalance.notEquals=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the expenseAccountList where transactionBalance not equals to UPDATED_TRANSACTION_BALANCE
        defaultExpenseAccountShouldBeFound("transactionBalance.notEquals=" + UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void getAllExpenseAccountsByTransactionBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionBalance in DEFAULT_TRANSACTION_BALANCE or UPDATED_TRANSACTION_BALANCE
        defaultExpenseAccountShouldBeFound("transactionBalance.in=" + DEFAULT_TRANSACTION_BALANCE + "," + UPDATED_TRANSACTION_BALANCE);

        // Get all the expenseAccountList where transactionBalance equals to UPDATED_TRANSACTION_BALANCE
        defaultExpenseAccountShouldNotBeFound("transactionBalance.in=" + UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void getAllExpenseAccountsByTransactionBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionBalance is not null
        defaultExpenseAccountShouldBeFound("transactionBalance.specified=true");

        // Get all the expenseAccountList where transactionBalance is null
        defaultExpenseAccountShouldNotBeFound("transactionBalance.specified=false");
    }

    @Test
    @Transactional
    public void getAllExpenseAccountsByTransactionBalanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionBalance is greater than or equal to DEFAULT_TRANSACTION_BALANCE
        defaultExpenseAccountShouldBeFound("transactionBalance.greaterThanOrEqual=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the expenseAccountList where transactionBalance is greater than or equal to UPDATED_TRANSACTION_BALANCE
        defaultExpenseAccountShouldNotBeFound("transactionBalance.greaterThanOrEqual=" + UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void getAllExpenseAccountsByTransactionBalanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionBalance is less than or equal to DEFAULT_TRANSACTION_BALANCE
        defaultExpenseAccountShouldBeFound("transactionBalance.lessThanOrEqual=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the expenseAccountList where transactionBalance is less than or equal to SMALLER_TRANSACTION_BALANCE
        defaultExpenseAccountShouldNotBeFound("transactionBalance.lessThanOrEqual=" + SMALLER_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void getAllExpenseAccountsByTransactionBalanceIsLessThanSomething() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionBalance is less than DEFAULT_TRANSACTION_BALANCE
        defaultExpenseAccountShouldNotBeFound("transactionBalance.lessThan=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the expenseAccountList where transactionBalance is less than UPDATED_TRANSACTION_BALANCE
        defaultExpenseAccountShouldBeFound("transactionBalance.lessThan=" + UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void getAllExpenseAccountsByTransactionBalanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        expenseAccountRepository.saveAndFlush(expenseAccount);

        // Get all the expenseAccountList where transactionBalance is greater than DEFAULT_TRANSACTION_BALANCE
        defaultExpenseAccountShouldNotBeFound("transactionBalance.greaterThan=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the expenseAccountList where transactionBalance is greater than SMALLER_TRANSACTION_BALANCE
        defaultExpenseAccountShouldBeFound("transactionBalance.greaterThan=" + SMALLER_TRANSACTION_BALANCE);
    }


    @Test
    @Transactional
    public void getAllExpenseAccountsByLocationIsEqualToSomething() throws Exception {
        // Get already existing entity
        Location location = expenseAccount.getLocation();
        expenseAccountRepository.saveAndFlush(expenseAccount);
        Long locationId = location.getId();

        // Get all the expenseAccountList where location equals to locationId
        defaultExpenseAccountShouldBeFound("locationId.equals=" + locationId);

        // Get all the expenseAccountList where location equals to locationId + 1
        defaultExpenseAccountShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }


    @Test
    @Transactional
    public void getAllExpenseAccountsByTransactionTypeIsEqualToSomething() throws Exception {
        // Get already existing entity
        TransactionType transactionType = expenseAccount.getTransactionType();
        expenseAccountRepository.saveAndFlush(expenseAccount);
        Long transactionTypeId = transactionType.getId();

        // Get all the expenseAccountList where transactionType equals to transactionTypeId
        defaultExpenseAccountShouldBeFound("transactionTypeId.equals=" + transactionTypeId);

        // Get all the expenseAccountList where transactionType equals to transactionTypeId + 1
        defaultExpenseAccountShouldNotBeFound("transactionTypeId.equals=" + (transactionTypeId + 1));
    }


    @Test
    @Transactional
    public void getAllExpenseAccountsByExpenseIsEqualToSomething() throws Exception {
        // Get already existing entity
        Expense expense = expenseAccount.getExpense();
        expenseAccountRepository.saveAndFlush(expenseAccount);
        Long expenseId = expense.getId();

        // Get all the expenseAccountList where expense equals to expenseId
        defaultExpenseAccountShouldBeFound("expenseId.equals=" + expenseId);

        // Get all the expenseAccountList where expense equals to expenseId + 1
        defaultExpenseAccountShouldNotBeFound("expenseId.equals=" + (expenseId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultExpenseAccountShouldBeFound(String filter) throws Exception {
        restExpenseAccountMockMvc.perform(get("/api/expense-accounts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(expenseAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].transactionDescription").value(hasItem(DEFAULT_TRANSACTION_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].transactionAmountDR").value(hasItem(DEFAULT_TRANSACTION_AMOUNT_DR.intValue())))
            .andExpect(jsonPath("$.[*].transactionAmountCR").value(hasItem(DEFAULT_TRANSACTION_AMOUNT_CR.intValue())))
            .andExpect(jsonPath("$.[*].transactionBalance").value(hasItem(DEFAULT_TRANSACTION_BALANCE.intValue())));

        // Check, that the count call also returns 1
        restExpenseAccountMockMvc.perform(get("/api/expense-accounts/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultExpenseAccountShouldNotBeFound(String filter) throws Exception {
        restExpenseAccountMockMvc.perform(get("/api/expense-accounts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restExpenseAccountMockMvc.perform(get("/api/expense-accounts/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingExpenseAccount() throws Exception {
        // Get the expenseAccount
        restExpenseAccountMockMvc.perform(get("/api/expense-accounts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExpenseAccount() throws Exception {
        // Initialize the database
        expenseAccountService.save(expenseAccount);

        int databaseSizeBeforeUpdate = expenseAccountRepository.findAll().size();

        // Update the expenseAccount
        ExpenseAccount updatedExpenseAccount = expenseAccountRepository.findById(expenseAccount.getId()).get();
        // Disconnect from session so that the updates on updatedExpenseAccount are not directly saved in db
        em.detach(updatedExpenseAccount);
        updatedExpenseAccount
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .transactionDescription(UPDATED_TRANSACTION_DESCRIPTION)
            .transactionAmountDR(UPDATED_TRANSACTION_AMOUNT_DR)
            .transactionAmountCR(UPDATED_TRANSACTION_AMOUNT_CR)
            .transactionBalance(UPDATED_TRANSACTION_BALANCE);

        restExpenseAccountMockMvc.perform(put("/api/expense-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedExpenseAccount)))
            .andExpect(status().isOk());

        // Validate the ExpenseAccount in the database
        List<ExpenseAccount> expenseAccountList = expenseAccountRepository.findAll();
        assertThat(expenseAccountList).hasSize(databaseSizeBeforeUpdate);
        ExpenseAccount testExpenseAccount = expenseAccountList.get(expenseAccountList.size() - 1);
        assertThat(testExpenseAccount.getTransactionDate()).isEqualTo(UPDATED_TRANSACTION_DATE);
        assertThat(testExpenseAccount.getTransactionDescription()).isEqualTo(UPDATED_TRANSACTION_DESCRIPTION);
        assertThat(testExpenseAccount.getTransactionAmountDR()).isEqualTo(UPDATED_TRANSACTION_AMOUNT_DR);
        assertThat(testExpenseAccount.getTransactionAmountCR()).isEqualTo(UPDATED_TRANSACTION_AMOUNT_CR);
        assertThat(testExpenseAccount.getTransactionBalance()).isEqualTo(UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void updateNonExistingExpenseAccount() throws Exception {
        int databaseSizeBeforeUpdate = expenseAccountRepository.findAll().size();

        // Create the ExpenseAccount

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExpenseAccountMockMvc.perform(put("/api/expense-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(expenseAccount)))
            .andExpect(status().isBadRequest());

        // Validate the ExpenseAccount in the database
        List<ExpenseAccount> expenseAccountList = expenseAccountRepository.findAll();
        assertThat(expenseAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteExpenseAccount() throws Exception {
        // Initialize the database
        expenseAccountService.save(expenseAccount);

        int databaseSizeBeforeDelete = expenseAccountRepository.findAll().size();

        // Delete the expenseAccount
        restExpenseAccountMockMvc.perform(delete("/api/expense-accounts/{id}", expenseAccount.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ExpenseAccount> expenseAccountList = expenseAccountRepository.findAll();
        assertThat(expenseAccountList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

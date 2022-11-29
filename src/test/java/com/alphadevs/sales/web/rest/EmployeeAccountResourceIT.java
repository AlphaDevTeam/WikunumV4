package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.WikunumApp;
import com.alphadevs.sales.domain.EmployeeAccount;
import com.alphadevs.sales.domain.Location;
import com.alphadevs.sales.domain.TransactionType;
import com.alphadevs.sales.domain.Employee;
import com.alphadevs.sales.repository.EmployeeAccountRepository;
import com.alphadevs.sales.service.EmployeeAccountService;
import com.alphadevs.sales.web.rest.errors.ExceptionTranslator;
import com.alphadevs.sales.service.dto.EmployeeAccountCriteria;
import com.alphadevs.sales.service.EmployeeAccountQueryService;

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
 * Integration tests for the {@link EmployeeAccountResource} REST controller.
 */
@SpringBootTest(classes = WikunumApp.class)
public class EmployeeAccountResourceIT {

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
    private EmployeeAccountRepository employeeAccountRepository;

    @Autowired
    private EmployeeAccountService employeeAccountService;

    @Autowired
    private EmployeeAccountQueryService employeeAccountQueryService;

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

    private MockMvc restEmployeeAccountMockMvc;

    private EmployeeAccount employeeAccount;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EmployeeAccountResource employeeAccountResource = new EmployeeAccountResource(employeeAccountService, employeeAccountQueryService);
        this.restEmployeeAccountMockMvc = MockMvcBuilders.standaloneSetup(employeeAccountResource)
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
    public static EmployeeAccount createEntity(EntityManager em) {
        EmployeeAccount employeeAccount = new EmployeeAccount()
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
        employeeAccount.setLocation(location);
        // Add required entity
        TransactionType transactionType;
        if (TestUtil.findAll(em, TransactionType.class).isEmpty()) {
            transactionType = TransactionTypeResourceIT.createEntity(em);
            em.persist(transactionType);
            em.flush();
        } else {
            transactionType = TestUtil.findAll(em, TransactionType.class).get(0);
        }
        employeeAccount.setTransactionType(transactionType);
        // Add required entity
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        employeeAccount.setEmployee(employee);
        return employeeAccount;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmployeeAccount createUpdatedEntity(EntityManager em) {
        EmployeeAccount employeeAccount = new EmployeeAccount()
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
        employeeAccount.setLocation(location);
        // Add required entity
        TransactionType transactionType;
        if (TestUtil.findAll(em, TransactionType.class).isEmpty()) {
            transactionType = TransactionTypeResourceIT.createUpdatedEntity(em);
            em.persist(transactionType);
            em.flush();
        } else {
            transactionType = TestUtil.findAll(em, TransactionType.class).get(0);
        }
        employeeAccount.setTransactionType(transactionType);
        // Add required entity
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createUpdatedEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        employeeAccount.setEmployee(employee);
        return employeeAccount;
    }

    @BeforeEach
    public void initTest() {
        employeeAccount = createEntity(em);
    }

    @Test
    @Transactional
    public void createEmployeeAccount() throws Exception {
        int databaseSizeBeforeCreate = employeeAccountRepository.findAll().size();

        // Create the EmployeeAccount
        restEmployeeAccountMockMvc.perform(post("/api/employee-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employeeAccount)))
            .andExpect(status().isCreated());

        // Validate the EmployeeAccount in the database
        List<EmployeeAccount> employeeAccountList = employeeAccountRepository.findAll();
        assertThat(employeeAccountList).hasSize(databaseSizeBeforeCreate + 1);
        EmployeeAccount testEmployeeAccount = employeeAccountList.get(employeeAccountList.size() - 1);
        assertThat(testEmployeeAccount.getTransactionDate()).isEqualTo(DEFAULT_TRANSACTION_DATE);
        assertThat(testEmployeeAccount.getTransactionDescription()).isEqualTo(DEFAULT_TRANSACTION_DESCRIPTION);
        assertThat(testEmployeeAccount.getTransactionAmountDR()).isEqualTo(DEFAULT_TRANSACTION_AMOUNT_DR);
        assertThat(testEmployeeAccount.getTransactionAmountCR()).isEqualTo(DEFAULT_TRANSACTION_AMOUNT_CR);
        assertThat(testEmployeeAccount.getTransactionBalance()).isEqualTo(DEFAULT_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void createEmployeeAccountWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = employeeAccountRepository.findAll().size();

        // Create the EmployeeAccount with an existing ID
        employeeAccount.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmployeeAccountMockMvc.perform(post("/api/employee-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employeeAccount)))
            .andExpect(status().isBadRequest());

        // Validate the EmployeeAccount in the database
        List<EmployeeAccount> employeeAccountList = employeeAccountRepository.findAll();
        assertThat(employeeAccountList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTransactionDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeAccountRepository.findAll().size();
        // set the field null
        employeeAccount.setTransactionDate(null);

        // Create the EmployeeAccount, which fails.

        restEmployeeAccountMockMvc.perform(post("/api/employee-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employeeAccount)))
            .andExpect(status().isBadRequest());

        List<EmployeeAccount> employeeAccountList = employeeAccountRepository.findAll();
        assertThat(employeeAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTransactionDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeAccountRepository.findAll().size();
        // set the field null
        employeeAccount.setTransactionDescription(null);

        // Create the EmployeeAccount, which fails.

        restEmployeeAccountMockMvc.perform(post("/api/employee-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employeeAccount)))
            .andExpect(status().isBadRequest());

        List<EmployeeAccount> employeeAccountList = employeeAccountRepository.findAll();
        assertThat(employeeAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTransactionAmountDRIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeAccountRepository.findAll().size();
        // set the field null
        employeeAccount.setTransactionAmountDR(null);

        // Create the EmployeeAccount, which fails.

        restEmployeeAccountMockMvc.perform(post("/api/employee-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employeeAccount)))
            .andExpect(status().isBadRequest());

        List<EmployeeAccount> employeeAccountList = employeeAccountRepository.findAll();
        assertThat(employeeAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTransactionAmountCRIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeAccountRepository.findAll().size();
        // set the field null
        employeeAccount.setTransactionAmountCR(null);

        // Create the EmployeeAccount, which fails.

        restEmployeeAccountMockMvc.perform(post("/api/employee-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employeeAccount)))
            .andExpect(status().isBadRequest());

        List<EmployeeAccount> employeeAccountList = employeeAccountRepository.findAll();
        assertThat(employeeAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTransactionBalanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeAccountRepository.findAll().size();
        // set the field null
        employeeAccount.setTransactionBalance(null);

        // Create the EmployeeAccount, which fails.

        restEmployeeAccountMockMvc.perform(post("/api/employee-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employeeAccount)))
            .andExpect(status().isBadRequest());

        List<EmployeeAccount> employeeAccountList = employeeAccountRepository.findAll();
        assertThat(employeeAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEmployeeAccounts() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList
        restEmployeeAccountMockMvc.perform(get("/api/employee-accounts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employeeAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].transactionDescription").value(hasItem(DEFAULT_TRANSACTION_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].transactionAmountDR").value(hasItem(DEFAULT_TRANSACTION_AMOUNT_DR.intValue())))
            .andExpect(jsonPath("$.[*].transactionAmountCR").value(hasItem(DEFAULT_TRANSACTION_AMOUNT_CR.intValue())))
            .andExpect(jsonPath("$.[*].transactionBalance").value(hasItem(DEFAULT_TRANSACTION_BALANCE.intValue())));
    }
    
    @Test
    @Transactional
    public void getEmployeeAccount() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get the employeeAccount
        restEmployeeAccountMockMvc.perform(get("/api/employee-accounts/{id}", employeeAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(employeeAccount.getId().intValue()))
            .andExpect(jsonPath("$.transactionDate").value(DEFAULT_TRANSACTION_DATE.toString()))
            .andExpect(jsonPath("$.transactionDescription").value(DEFAULT_TRANSACTION_DESCRIPTION))
            .andExpect(jsonPath("$.transactionAmountDR").value(DEFAULT_TRANSACTION_AMOUNT_DR.intValue()))
            .andExpect(jsonPath("$.transactionAmountCR").value(DEFAULT_TRANSACTION_AMOUNT_CR.intValue()))
            .andExpect(jsonPath("$.transactionBalance").value(DEFAULT_TRANSACTION_BALANCE.intValue()));
    }


    @Test
    @Transactional
    public void getEmployeeAccountsByIdFiltering() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        Long id = employeeAccount.getId();

        defaultEmployeeAccountShouldBeFound("id.equals=" + id);
        defaultEmployeeAccountShouldNotBeFound("id.notEquals=" + id);

        defaultEmployeeAccountShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEmployeeAccountShouldNotBeFound("id.greaterThan=" + id);

        defaultEmployeeAccountShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEmployeeAccountShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllEmployeeAccountsByTransactionDateIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionDate equals to DEFAULT_TRANSACTION_DATE
        defaultEmployeeAccountShouldBeFound("transactionDate.equals=" + DEFAULT_TRANSACTION_DATE);

        // Get all the employeeAccountList where transactionDate equals to UPDATED_TRANSACTION_DATE
        defaultEmployeeAccountShouldNotBeFound("transactionDate.equals=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllEmployeeAccountsByTransactionDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionDate not equals to DEFAULT_TRANSACTION_DATE
        defaultEmployeeAccountShouldNotBeFound("transactionDate.notEquals=" + DEFAULT_TRANSACTION_DATE);

        // Get all the employeeAccountList where transactionDate not equals to UPDATED_TRANSACTION_DATE
        defaultEmployeeAccountShouldBeFound("transactionDate.notEquals=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllEmployeeAccountsByTransactionDateIsInShouldWork() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionDate in DEFAULT_TRANSACTION_DATE or UPDATED_TRANSACTION_DATE
        defaultEmployeeAccountShouldBeFound("transactionDate.in=" + DEFAULT_TRANSACTION_DATE + "," + UPDATED_TRANSACTION_DATE);

        // Get all the employeeAccountList where transactionDate equals to UPDATED_TRANSACTION_DATE
        defaultEmployeeAccountShouldNotBeFound("transactionDate.in=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllEmployeeAccountsByTransactionDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionDate is not null
        defaultEmployeeAccountShouldBeFound("transactionDate.specified=true");

        // Get all the employeeAccountList where transactionDate is null
        defaultEmployeeAccountShouldNotBeFound("transactionDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllEmployeeAccountsByTransactionDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionDate is greater than or equal to DEFAULT_TRANSACTION_DATE
        defaultEmployeeAccountShouldBeFound("transactionDate.greaterThanOrEqual=" + DEFAULT_TRANSACTION_DATE);

        // Get all the employeeAccountList where transactionDate is greater than or equal to UPDATED_TRANSACTION_DATE
        defaultEmployeeAccountShouldNotBeFound("transactionDate.greaterThanOrEqual=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllEmployeeAccountsByTransactionDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionDate is less than or equal to DEFAULT_TRANSACTION_DATE
        defaultEmployeeAccountShouldBeFound("transactionDate.lessThanOrEqual=" + DEFAULT_TRANSACTION_DATE);

        // Get all the employeeAccountList where transactionDate is less than or equal to SMALLER_TRANSACTION_DATE
        defaultEmployeeAccountShouldNotBeFound("transactionDate.lessThanOrEqual=" + SMALLER_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllEmployeeAccountsByTransactionDateIsLessThanSomething() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionDate is less than DEFAULT_TRANSACTION_DATE
        defaultEmployeeAccountShouldNotBeFound("transactionDate.lessThan=" + DEFAULT_TRANSACTION_DATE);

        // Get all the employeeAccountList where transactionDate is less than UPDATED_TRANSACTION_DATE
        defaultEmployeeAccountShouldBeFound("transactionDate.lessThan=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllEmployeeAccountsByTransactionDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionDate is greater than DEFAULT_TRANSACTION_DATE
        defaultEmployeeAccountShouldNotBeFound("transactionDate.greaterThan=" + DEFAULT_TRANSACTION_DATE);

        // Get all the employeeAccountList where transactionDate is greater than SMALLER_TRANSACTION_DATE
        defaultEmployeeAccountShouldBeFound("transactionDate.greaterThan=" + SMALLER_TRANSACTION_DATE);
    }


    @Test
    @Transactional
    public void getAllEmployeeAccountsByTransactionDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionDescription equals to DEFAULT_TRANSACTION_DESCRIPTION
        defaultEmployeeAccountShouldBeFound("transactionDescription.equals=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the employeeAccountList where transactionDescription equals to UPDATED_TRANSACTION_DESCRIPTION
        defaultEmployeeAccountShouldNotBeFound("transactionDescription.equals=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllEmployeeAccountsByTransactionDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionDescription not equals to DEFAULT_TRANSACTION_DESCRIPTION
        defaultEmployeeAccountShouldNotBeFound("transactionDescription.notEquals=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the employeeAccountList where transactionDescription not equals to UPDATED_TRANSACTION_DESCRIPTION
        defaultEmployeeAccountShouldBeFound("transactionDescription.notEquals=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllEmployeeAccountsByTransactionDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionDescription in DEFAULT_TRANSACTION_DESCRIPTION or UPDATED_TRANSACTION_DESCRIPTION
        defaultEmployeeAccountShouldBeFound("transactionDescription.in=" + DEFAULT_TRANSACTION_DESCRIPTION + "," + UPDATED_TRANSACTION_DESCRIPTION);

        // Get all the employeeAccountList where transactionDescription equals to UPDATED_TRANSACTION_DESCRIPTION
        defaultEmployeeAccountShouldNotBeFound("transactionDescription.in=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllEmployeeAccountsByTransactionDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionDescription is not null
        defaultEmployeeAccountShouldBeFound("transactionDescription.specified=true");

        // Get all the employeeAccountList where transactionDescription is null
        defaultEmployeeAccountShouldNotBeFound("transactionDescription.specified=false");
    }
                @Test
    @Transactional
    public void getAllEmployeeAccountsByTransactionDescriptionContainsSomething() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionDescription contains DEFAULT_TRANSACTION_DESCRIPTION
        defaultEmployeeAccountShouldBeFound("transactionDescription.contains=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the employeeAccountList where transactionDescription contains UPDATED_TRANSACTION_DESCRIPTION
        defaultEmployeeAccountShouldNotBeFound("transactionDescription.contains=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllEmployeeAccountsByTransactionDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionDescription does not contain DEFAULT_TRANSACTION_DESCRIPTION
        defaultEmployeeAccountShouldNotBeFound("transactionDescription.doesNotContain=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the employeeAccountList where transactionDescription does not contain UPDATED_TRANSACTION_DESCRIPTION
        defaultEmployeeAccountShouldBeFound("transactionDescription.doesNotContain=" + UPDATED_TRANSACTION_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllEmployeeAccountsByTransactionAmountDRIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionAmountDR equals to DEFAULT_TRANSACTION_AMOUNT_DR
        defaultEmployeeAccountShouldBeFound("transactionAmountDR.equals=" + DEFAULT_TRANSACTION_AMOUNT_DR);

        // Get all the employeeAccountList where transactionAmountDR equals to UPDATED_TRANSACTION_AMOUNT_DR
        defaultEmployeeAccountShouldNotBeFound("transactionAmountDR.equals=" + UPDATED_TRANSACTION_AMOUNT_DR);
    }

    @Test
    @Transactional
    public void getAllEmployeeAccountsByTransactionAmountDRIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionAmountDR not equals to DEFAULT_TRANSACTION_AMOUNT_DR
        defaultEmployeeAccountShouldNotBeFound("transactionAmountDR.notEquals=" + DEFAULT_TRANSACTION_AMOUNT_DR);

        // Get all the employeeAccountList where transactionAmountDR not equals to UPDATED_TRANSACTION_AMOUNT_DR
        defaultEmployeeAccountShouldBeFound("transactionAmountDR.notEquals=" + UPDATED_TRANSACTION_AMOUNT_DR);
    }

    @Test
    @Transactional
    public void getAllEmployeeAccountsByTransactionAmountDRIsInShouldWork() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionAmountDR in DEFAULT_TRANSACTION_AMOUNT_DR or UPDATED_TRANSACTION_AMOUNT_DR
        defaultEmployeeAccountShouldBeFound("transactionAmountDR.in=" + DEFAULT_TRANSACTION_AMOUNT_DR + "," + UPDATED_TRANSACTION_AMOUNT_DR);

        // Get all the employeeAccountList where transactionAmountDR equals to UPDATED_TRANSACTION_AMOUNT_DR
        defaultEmployeeAccountShouldNotBeFound("transactionAmountDR.in=" + UPDATED_TRANSACTION_AMOUNT_DR);
    }

    @Test
    @Transactional
    public void getAllEmployeeAccountsByTransactionAmountDRIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionAmountDR is not null
        defaultEmployeeAccountShouldBeFound("transactionAmountDR.specified=true");

        // Get all the employeeAccountList where transactionAmountDR is null
        defaultEmployeeAccountShouldNotBeFound("transactionAmountDR.specified=false");
    }

    @Test
    @Transactional
    public void getAllEmployeeAccountsByTransactionAmountDRIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionAmountDR is greater than or equal to DEFAULT_TRANSACTION_AMOUNT_DR
        defaultEmployeeAccountShouldBeFound("transactionAmountDR.greaterThanOrEqual=" + DEFAULT_TRANSACTION_AMOUNT_DR);

        // Get all the employeeAccountList where transactionAmountDR is greater than or equal to UPDATED_TRANSACTION_AMOUNT_DR
        defaultEmployeeAccountShouldNotBeFound("transactionAmountDR.greaterThanOrEqual=" + UPDATED_TRANSACTION_AMOUNT_DR);
    }

    @Test
    @Transactional
    public void getAllEmployeeAccountsByTransactionAmountDRIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionAmountDR is less than or equal to DEFAULT_TRANSACTION_AMOUNT_DR
        defaultEmployeeAccountShouldBeFound("transactionAmountDR.lessThanOrEqual=" + DEFAULT_TRANSACTION_AMOUNT_DR);

        // Get all the employeeAccountList where transactionAmountDR is less than or equal to SMALLER_TRANSACTION_AMOUNT_DR
        defaultEmployeeAccountShouldNotBeFound("transactionAmountDR.lessThanOrEqual=" + SMALLER_TRANSACTION_AMOUNT_DR);
    }

    @Test
    @Transactional
    public void getAllEmployeeAccountsByTransactionAmountDRIsLessThanSomething() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionAmountDR is less than DEFAULT_TRANSACTION_AMOUNT_DR
        defaultEmployeeAccountShouldNotBeFound("transactionAmountDR.lessThan=" + DEFAULT_TRANSACTION_AMOUNT_DR);

        // Get all the employeeAccountList where transactionAmountDR is less than UPDATED_TRANSACTION_AMOUNT_DR
        defaultEmployeeAccountShouldBeFound("transactionAmountDR.lessThan=" + UPDATED_TRANSACTION_AMOUNT_DR);
    }

    @Test
    @Transactional
    public void getAllEmployeeAccountsByTransactionAmountDRIsGreaterThanSomething() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionAmountDR is greater than DEFAULT_TRANSACTION_AMOUNT_DR
        defaultEmployeeAccountShouldNotBeFound("transactionAmountDR.greaterThan=" + DEFAULT_TRANSACTION_AMOUNT_DR);

        // Get all the employeeAccountList where transactionAmountDR is greater than SMALLER_TRANSACTION_AMOUNT_DR
        defaultEmployeeAccountShouldBeFound("transactionAmountDR.greaterThan=" + SMALLER_TRANSACTION_AMOUNT_DR);
    }


    @Test
    @Transactional
    public void getAllEmployeeAccountsByTransactionAmountCRIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionAmountCR equals to DEFAULT_TRANSACTION_AMOUNT_CR
        defaultEmployeeAccountShouldBeFound("transactionAmountCR.equals=" + DEFAULT_TRANSACTION_AMOUNT_CR);

        // Get all the employeeAccountList where transactionAmountCR equals to UPDATED_TRANSACTION_AMOUNT_CR
        defaultEmployeeAccountShouldNotBeFound("transactionAmountCR.equals=" + UPDATED_TRANSACTION_AMOUNT_CR);
    }

    @Test
    @Transactional
    public void getAllEmployeeAccountsByTransactionAmountCRIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionAmountCR not equals to DEFAULT_TRANSACTION_AMOUNT_CR
        defaultEmployeeAccountShouldNotBeFound("transactionAmountCR.notEquals=" + DEFAULT_TRANSACTION_AMOUNT_CR);

        // Get all the employeeAccountList where transactionAmountCR not equals to UPDATED_TRANSACTION_AMOUNT_CR
        defaultEmployeeAccountShouldBeFound("transactionAmountCR.notEquals=" + UPDATED_TRANSACTION_AMOUNT_CR);
    }

    @Test
    @Transactional
    public void getAllEmployeeAccountsByTransactionAmountCRIsInShouldWork() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionAmountCR in DEFAULT_TRANSACTION_AMOUNT_CR or UPDATED_TRANSACTION_AMOUNT_CR
        defaultEmployeeAccountShouldBeFound("transactionAmountCR.in=" + DEFAULT_TRANSACTION_AMOUNT_CR + "," + UPDATED_TRANSACTION_AMOUNT_CR);

        // Get all the employeeAccountList where transactionAmountCR equals to UPDATED_TRANSACTION_AMOUNT_CR
        defaultEmployeeAccountShouldNotBeFound("transactionAmountCR.in=" + UPDATED_TRANSACTION_AMOUNT_CR);
    }

    @Test
    @Transactional
    public void getAllEmployeeAccountsByTransactionAmountCRIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionAmountCR is not null
        defaultEmployeeAccountShouldBeFound("transactionAmountCR.specified=true");

        // Get all the employeeAccountList where transactionAmountCR is null
        defaultEmployeeAccountShouldNotBeFound("transactionAmountCR.specified=false");
    }

    @Test
    @Transactional
    public void getAllEmployeeAccountsByTransactionAmountCRIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionAmountCR is greater than or equal to DEFAULT_TRANSACTION_AMOUNT_CR
        defaultEmployeeAccountShouldBeFound("transactionAmountCR.greaterThanOrEqual=" + DEFAULT_TRANSACTION_AMOUNT_CR);

        // Get all the employeeAccountList where transactionAmountCR is greater than or equal to UPDATED_TRANSACTION_AMOUNT_CR
        defaultEmployeeAccountShouldNotBeFound("transactionAmountCR.greaterThanOrEqual=" + UPDATED_TRANSACTION_AMOUNT_CR);
    }

    @Test
    @Transactional
    public void getAllEmployeeAccountsByTransactionAmountCRIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionAmountCR is less than or equal to DEFAULT_TRANSACTION_AMOUNT_CR
        defaultEmployeeAccountShouldBeFound("transactionAmountCR.lessThanOrEqual=" + DEFAULT_TRANSACTION_AMOUNT_CR);

        // Get all the employeeAccountList where transactionAmountCR is less than or equal to SMALLER_TRANSACTION_AMOUNT_CR
        defaultEmployeeAccountShouldNotBeFound("transactionAmountCR.lessThanOrEqual=" + SMALLER_TRANSACTION_AMOUNT_CR);
    }

    @Test
    @Transactional
    public void getAllEmployeeAccountsByTransactionAmountCRIsLessThanSomething() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionAmountCR is less than DEFAULT_TRANSACTION_AMOUNT_CR
        defaultEmployeeAccountShouldNotBeFound("transactionAmountCR.lessThan=" + DEFAULT_TRANSACTION_AMOUNT_CR);

        // Get all the employeeAccountList where transactionAmountCR is less than UPDATED_TRANSACTION_AMOUNT_CR
        defaultEmployeeAccountShouldBeFound("transactionAmountCR.lessThan=" + UPDATED_TRANSACTION_AMOUNT_CR);
    }

    @Test
    @Transactional
    public void getAllEmployeeAccountsByTransactionAmountCRIsGreaterThanSomething() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionAmountCR is greater than DEFAULT_TRANSACTION_AMOUNT_CR
        defaultEmployeeAccountShouldNotBeFound("transactionAmountCR.greaterThan=" + DEFAULT_TRANSACTION_AMOUNT_CR);

        // Get all the employeeAccountList where transactionAmountCR is greater than SMALLER_TRANSACTION_AMOUNT_CR
        defaultEmployeeAccountShouldBeFound("transactionAmountCR.greaterThan=" + SMALLER_TRANSACTION_AMOUNT_CR);
    }


    @Test
    @Transactional
    public void getAllEmployeeAccountsByTransactionBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionBalance equals to DEFAULT_TRANSACTION_BALANCE
        defaultEmployeeAccountShouldBeFound("transactionBalance.equals=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the employeeAccountList where transactionBalance equals to UPDATED_TRANSACTION_BALANCE
        defaultEmployeeAccountShouldNotBeFound("transactionBalance.equals=" + UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void getAllEmployeeAccountsByTransactionBalanceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionBalance not equals to DEFAULT_TRANSACTION_BALANCE
        defaultEmployeeAccountShouldNotBeFound("transactionBalance.notEquals=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the employeeAccountList where transactionBalance not equals to UPDATED_TRANSACTION_BALANCE
        defaultEmployeeAccountShouldBeFound("transactionBalance.notEquals=" + UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void getAllEmployeeAccountsByTransactionBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionBalance in DEFAULT_TRANSACTION_BALANCE or UPDATED_TRANSACTION_BALANCE
        defaultEmployeeAccountShouldBeFound("transactionBalance.in=" + DEFAULT_TRANSACTION_BALANCE + "," + UPDATED_TRANSACTION_BALANCE);

        // Get all the employeeAccountList where transactionBalance equals to UPDATED_TRANSACTION_BALANCE
        defaultEmployeeAccountShouldNotBeFound("transactionBalance.in=" + UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void getAllEmployeeAccountsByTransactionBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionBalance is not null
        defaultEmployeeAccountShouldBeFound("transactionBalance.specified=true");

        // Get all the employeeAccountList where transactionBalance is null
        defaultEmployeeAccountShouldNotBeFound("transactionBalance.specified=false");
    }

    @Test
    @Transactional
    public void getAllEmployeeAccountsByTransactionBalanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionBalance is greater than or equal to DEFAULT_TRANSACTION_BALANCE
        defaultEmployeeAccountShouldBeFound("transactionBalance.greaterThanOrEqual=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the employeeAccountList where transactionBalance is greater than or equal to UPDATED_TRANSACTION_BALANCE
        defaultEmployeeAccountShouldNotBeFound("transactionBalance.greaterThanOrEqual=" + UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void getAllEmployeeAccountsByTransactionBalanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionBalance is less than or equal to DEFAULT_TRANSACTION_BALANCE
        defaultEmployeeAccountShouldBeFound("transactionBalance.lessThanOrEqual=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the employeeAccountList where transactionBalance is less than or equal to SMALLER_TRANSACTION_BALANCE
        defaultEmployeeAccountShouldNotBeFound("transactionBalance.lessThanOrEqual=" + SMALLER_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void getAllEmployeeAccountsByTransactionBalanceIsLessThanSomething() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionBalance is less than DEFAULT_TRANSACTION_BALANCE
        defaultEmployeeAccountShouldNotBeFound("transactionBalance.lessThan=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the employeeAccountList where transactionBalance is less than UPDATED_TRANSACTION_BALANCE
        defaultEmployeeAccountShouldBeFound("transactionBalance.lessThan=" + UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void getAllEmployeeAccountsByTransactionBalanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where transactionBalance is greater than DEFAULT_TRANSACTION_BALANCE
        defaultEmployeeAccountShouldNotBeFound("transactionBalance.greaterThan=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the employeeAccountList where transactionBalance is greater than SMALLER_TRANSACTION_BALANCE
        defaultEmployeeAccountShouldBeFound("transactionBalance.greaterThan=" + SMALLER_TRANSACTION_BALANCE);
    }


    @Test
    @Transactional
    public void getAllEmployeeAccountsByLocationIsEqualToSomething() throws Exception {
        // Get already existing entity
        Location location = employeeAccount.getLocation();
        employeeAccountRepository.saveAndFlush(employeeAccount);
        Long locationId = location.getId();

        // Get all the employeeAccountList where location equals to locationId
        defaultEmployeeAccountShouldBeFound("locationId.equals=" + locationId);

        // Get all the employeeAccountList where location equals to locationId + 1
        defaultEmployeeAccountShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }


    @Test
    @Transactional
    public void getAllEmployeeAccountsByTransactionTypeIsEqualToSomething() throws Exception {
        // Get already existing entity
        TransactionType transactionType = employeeAccount.getTransactionType();
        employeeAccountRepository.saveAndFlush(employeeAccount);
        Long transactionTypeId = transactionType.getId();

        // Get all the employeeAccountList where transactionType equals to transactionTypeId
        defaultEmployeeAccountShouldBeFound("transactionTypeId.equals=" + transactionTypeId);

        // Get all the employeeAccountList where transactionType equals to transactionTypeId + 1
        defaultEmployeeAccountShouldNotBeFound("transactionTypeId.equals=" + (transactionTypeId + 1));
    }


    @Test
    @Transactional
    public void getAllEmployeeAccountsByEmployeeIsEqualToSomething() throws Exception {
        // Get already existing entity
        Employee employee = employeeAccount.getEmployee();
        employeeAccountRepository.saveAndFlush(employeeAccount);
        Long employeeId = employee.getId();

        // Get all the employeeAccountList where employee equals to employeeId
        defaultEmployeeAccountShouldBeFound("employeeId.equals=" + employeeId);

        // Get all the employeeAccountList where employee equals to employeeId + 1
        defaultEmployeeAccountShouldNotBeFound("employeeId.equals=" + (employeeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEmployeeAccountShouldBeFound(String filter) throws Exception {
        restEmployeeAccountMockMvc.perform(get("/api/employee-accounts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employeeAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].transactionDescription").value(hasItem(DEFAULT_TRANSACTION_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].transactionAmountDR").value(hasItem(DEFAULT_TRANSACTION_AMOUNT_DR.intValue())))
            .andExpect(jsonPath("$.[*].transactionAmountCR").value(hasItem(DEFAULT_TRANSACTION_AMOUNT_CR.intValue())))
            .andExpect(jsonPath("$.[*].transactionBalance").value(hasItem(DEFAULT_TRANSACTION_BALANCE.intValue())));

        // Check, that the count call also returns 1
        restEmployeeAccountMockMvc.perform(get("/api/employee-accounts/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEmployeeAccountShouldNotBeFound(String filter) throws Exception {
        restEmployeeAccountMockMvc.perform(get("/api/employee-accounts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEmployeeAccountMockMvc.perform(get("/api/employee-accounts/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingEmployeeAccount() throws Exception {
        // Get the employeeAccount
        restEmployeeAccountMockMvc.perform(get("/api/employee-accounts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEmployeeAccount() throws Exception {
        // Initialize the database
        employeeAccountService.save(employeeAccount);

        int databaseSizeBeforeUpdate = employeeAccountRepository.findAll().size();

        // Update the employeeAccount
        EmployeeAccount updatedEmployeeAccount = employeeAccountRepository.findById(employeeAccount.getId()).get();
        // Disconnect from session so that the updates on updatedEmployeeAccount are not directly saved in db
        em.detach(updatedEmployeeAccount);
        updatedEmployeeAccount
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .transactionDescription(UPDATED_TRANSACTION_DESCRIPTION)
            .transactionAmountDR(UPDATED_TRANSACTION_AMOUNT_DR)
            .transactionAmountCR(UPDATED_TRANSACTION_AMOUNT_CR)
            .transactionBalance(UPDATED_TRANSACTION_BALANCE);

        restEmployeeAccountMockMvc.perform(put("/api/employee-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEmployeeAccount)))
            .andExpect(status().isOk());

        // Validate the EmployeeAccount in the database
        List<EmployeeAccount> employeeAccountList = employeeAccountRepository.findAll();
        assertThat(employeeAccountList).hasSize(databaseSizeBeforeUpdate);
        EmployeeAccount testEmployeeAccount = employeeAccountList.get(employeeAccountList.size() - 1);
        assertThat(testEmployeeAccount.getTransactionDate()).isEqualTo(UPDATED_TRANSACTION_DATE);
        assertThat(testEmployeeAccount.getTransactionDescription()).isEqualTo(UPDATED_TRANSACTION_DESCRIPTION);
        assertThat(testEmployeeAccount.getTransactionAmountDR()).isEqualTo(UPDATED_TRANSACTION_AMOUNT_DR);
        assertThat(testEmployeeAccount.getTransactionAmountCR()).isEqualTo(UPDATED_TRANSACTION_AMOUNT_CR);
        assertThat(testEmployeeAccount.getTransactionBalance()).isEqualTo(UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void updateNonExistingEmployeeAccount() throws Exception {
        int databaseSizeBeforeUpdate = employeeAccountRepository.findAll().size();

        // Create the EmployeeAccount

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeAccountMockMvc.perform(put("/api/employee-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employeeAccount)))
            .andExpect(status().isBadRequest());

        // Validate the EmployeeAccount in the database
        List<EmployeeAccount> employeeAccountList = employeeAccountRepository.findAll();
        assertThat(employeeAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEmployeeAccount() throws Exception {
        // Initialize the database
        employeeAccountService.save(employeeAccount);

        int databaseSizeBeforeDelete = employeeAccountRepository.findAll().size();

        // Delete the employeeAccount
        restEmployeeAccountMockMvc.perform(delete("/api/employee-accounts/{id}", employeeAccount.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EmployeeAccount> employeeAccountList = employeeAccountRepository.findAll();
        assertThat(employeeAccountList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

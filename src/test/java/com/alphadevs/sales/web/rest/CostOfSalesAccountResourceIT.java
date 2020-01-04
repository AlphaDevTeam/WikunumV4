package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.WikunumApp;
import com.alphadevs.sales.domain.CostOfSalesAccount;
import com.alphadevs.sales.domain.Location;
import com.alphadevs.sales.domain.TransactionType;
import com.alphadevs.sales.repository.CostOfSalesAccountRepository;
import com.alphadevs.sales.service.CostOfSalesAccountService;
import com.alphadevs.sales.web.rest.errors.ExceptionTranslator;
import com.alphadevs.sales.service.dto.CostOfSalesAccountCriteria;
import com.alphadevs.sales.service.CostOfSalesAccountQueryService;

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
 * Integration tests for the {@link CostOfSalesAccountResource} REST controller.
 */
@SpringBootTest(classes = WikunumApp.class)
public class CostOfSalesAccountResourceIT {

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
    private CostOfSalesAccountRepository costOfSalesAccountRepository;

    @Autowired
    private CostOfSalesAccountService costOfSalesAccountService;

    @Autowired
    private CostOfSalesAccountQueryService costOfSalesAccountQueryService;

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

    private MockMvc restCostOfSalesAccountMockMvc;

    private CostOfSalesAccount costOfSalesAccount;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CostOfSalesAccountResource costOfSalesAccountResource = new CostOfSalesAccountResource(costOfSalesAccountService, costOfSalesAccountQueryService);
        this.restCostOfSalesAccountMockMvc = MockMvcBuilders.standaloneSetup(costOfSalesAccountResource)
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
    public static CostOfSalesAccount createEntity(EntityManager em) {
        CostOfSalesAccount costOfSalesAccount = new CostOfSalesAccount()
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
        costOfSalesAccount.setLocation(location);
        // Add required entity
        TransactionType transactionType;
        if (TestUtil.findAll(em, TransactionType.class).isEmpty()) {
            transactionType = TransactionTypeResourceIT.createEntity(em);
            em.persist(transactionType);
            em.flush();
        } else {
            transactionType = TestUtil.findAll(em, TransactionType.class).get(0);
        }
        costOfSalesAccount.setTransactionType(transactionType);
        return costOfSalesAccount;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CostOfSalesAccount createUpdatedEntity(EntityManager em) {
        CostOfSalesAccount costOfSalesAccount = new CostOfSalesAccount()
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
        costOfSalesAccount.setLocation(location);
        // Add required entity
        TransactionType transactionType;
        if (TestUtil.findAll(em, TransactionType.class).isEmpty()) {
            transactionType = TransactionTypeResourceIT.createUpdatedEntity(em);
            em.persist(transactionType);
            em.flush();
        } else {
            transactionType = TestUtil.findAll(em, TransactionType.class).get(0);
        }
        costOfSalesAccount.setTransactionType(transactionType);
        return costOfSalesAccount;
    }

    @BeforeEach
    public void initTest() {
        costOfSalesAccount = createEntity(em);
    }

    @Test
    @Transactional
    public void createCostOfSalesAccount() throws Exception {
        int databaseSizeBeforeCreate = costOfSalesAccountRepository.findAll().size();

        // Create the CostOfSalesAccount
        restCostOfSalesAccountMockMvc.perform(post("/api/cost-of-sales-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(costOfSalesAccount)))
            .andExpect(status().isCreated());

        // Validate the CostOfSalesAccount in the database
        List<CostOfSalesAccount> costOfSalesAccountList = costOfSalesAccountRepository.findAll();
        assertThat(costOfSalesAccountList).hasSize(databaseSizeBeforeCreate + 1);
        CostOfSalesAccount testCostOfSalesAccount = costOfSalesAccountList.get(costOfSalesAccountList.size() - 1);
        assertThat(testCostOfSalesAccount.getTransactionDate()).isEqualTo(DEFAULT_TRANSACTION_DATE);
        assertThat(testCostOfSalesAccount.getTransactionDescription()).isEqualTo(DEFAULT_TRANSACTION_DESCRIPTION);
        assertThat(testCostOfSalesAccount.getTransactionAmountDR()).isEqualTo(DEFAULT_TRANSACTION_AMOUNT_DR);
        assertThat(testCostOfSalesAccount.getTransactionAmountCR()).isEqualTo(DEFAULT_TRANSACTION_AMOUNT_CR);
        assertThat(testCostOfSalesAccount.getTransactionBalance()).isEqualTo(DEFAULT_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void createCostOfSalesAccountWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = costOfSalesAccountRepository.findAll().size();

        // Create the CostOfSalesAccount with an existing ID
        costOfSalesAccount.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCostOfSalesAccountMockMvc.perform(post("/api/cost-of-sales-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(costOfSalesAccount)))
            .andExpect(status().isBadRequest());

        // Validate the CostOfSalesAccount in the database
        List<CostOfSalesAccount> costOfSalesAccountList = costOfSalesAccountRepository.findAll();
        assertThat(costOfSalesAccountList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTransactionDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = costOfSalesAccountRepository.findAll().size();
        // set the field null
        costOfSalesAccount.setTransactionDate(null);

        // Create the CostOfSalesAccount, which fails.

        restCostOfSalesAccountMockMvc.perform(post("/api/cost-of-sales-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(costOfSalesAccount)))
            .andExpect(status().isBadRequest());

        List<CostOfSalesAccount> costOfSalesAccountList = costOfSalesAccountRepository.findAll();
        assertThat(costOfSalesAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTransactionDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = costOfSalesAccountRepository.findAll().size();
        // set the field null
        costOfSalesAccount.setTransactionDescription(null);

        // Create the CostOfSalesAccount, which fails.

        restCostOfSalesAccountMockMvc.perform(post("/api/cost-of-sales-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(costOfSalesAccount)))
            .andExpect(status().isBadRequest());

        List<CostOfSalesAccount> costOfSalesAccountList = costOfSalesAccountRepository.findAll();
        assertThat(costOfSalesAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTransactionAmountDRIsRequired() throws Exception {
        int databaseSizeBeforeTest = costOfSalesAccountRepository.findAll().size();
        // set the field null
        costOfSalesAccount.setTransactionAmountDR(null);

        // Create the CostOfSalesAccount, which fails.

        restCostOfSalesAccountMockMvc.perform(post("/api/cost-of-sales-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(costOfSalesAccount)))
            .andExpect(status().isBadRequest());

        List<CostOfSalesAccount> costOfSalesAccountList = costOfSalesAccountRepository.findAll();
        assertThat(costOfSalesAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTransactionAmountCRIsRequired() throws Exception {
        int databaseSizeBeforeTest = costOfSalesAccountRepository.findAll().size();
        // set the field null
        costOfSalesAccount.setTransactionAmountCR(null);

        // Create the CostOfSalesAccount, which fails.

        restCostOfSalesAccountMockMvc.perform(post("/api/cost-of-sales-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(costOfSalesAccount)))
            .andExpect(status().isBadRequest());

        List<CostOfSalesAccount> costOfSalesAccountList = costOfSalesAccountRepository.findAll();
        assertThat(costOfSalesAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTransactionBalanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = costOfSalesAccountRepository.findAll().size();
        // set the field null
        costOfSalesAccount.setTransactionBalance(null);

        // Create the CostOfSalesAccount, which fails.

        restCostOfSalesAccountMockMvc.perform(post("/api/cost-of-sales-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(costOfSalesAccount)))
            .andExpect(status().isBadRequest());

        List<CostOfSalesAccount> costOfSalesAccountList = costOfSalesAccountRepository.findAll();
        assertThat(costOfSalesAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCostOfSalesAccounts() throws Exception {
        // Initialize the database
        costOfSalesAccountRepository.saveAndFlush(costOfSalesAccount);

        // Get all the costOfSalesAccountList
        restCostOfSalesAccountMockMvc.perform(get("/api/cost-of-sales-accounts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(costOfSalesAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].transactionDescription").value(hasItem(DEFAULT_TRANSACTION_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].transactionAmountDR").value(hasItem(DEFAULT_TRANSACTION_AMOUNT_DR.intValue())))
            .andExpect(jsonPath("$.[*].transactionAmountCR").value(hasItem(DEFAULT_TRANSACTION_AMOUNT_CR.intValue())))
            .andExpect(jsonPath("$.[*].transactionBalance").value(hasItem(DEFAULT_TRANSACTION_BALANCE.intValue())));
    }
    
    @Test
    @Transactional
    public void getCostOfSalesAccount() throws Exception {
        // Initialize the database
        costOfSalesAccountRepository.saveAndFlush(costOfSalesAccount);

        // Get the costOfSalesAccount
        restCostOfSalesAccountMockMvc.perform(get("/api/cost-of-sales-accounts/{id}", costOfSalesAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(costOfSalesAccount.getId().intValue()))
            .andExpect(jsonPath("$.transactionDate").value(DEFAULT_TRANSACTION_DATE.toString()))
            .andExpect(jsonPath("$.transactionDescription").value(DEFAULT_TRANSACTION_DESCRIPTION))
            .andExpect(jsonPath("$.transactionAmountDR").value(DEFAULT_TRANSACTION_AMOUNT_DR.intValue()))
            .andExpect(jsonPath("$.transactionAmountCR").value(DEFAULT_TRANSACTION_AMOUNT_CR.intValue()))
            .andExpect(jsonPath("$.transactionBalance").value(DEFAULT_TRANSACTION_BALANCE.intValue()));
    }


    @Test
    @Transactional
    public void getCostOfSalesAccountsByIdFiltering() throws Exception {
        // Initialize the database
        costOfSalesAccountRepository.saveAndFlush(costOfSalesAccount);

        Long id = costOfSalesAccount.getId();

        defaultCostOfSalesAccountShouldBeFound("id.equals=" + id);
        defaultCostOfSalesAccountShouldNotBeFound("id.notEquals=" + id);

        defaultCostOfSalesAccountShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCostOfSalesAccountShouldNotBeFound("id.greaterThan=" + id);

        defaultCostOfSalesAccountShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCostOfSalesAccountShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllCostOfSalesAccountsByTransactionDateIsEqualToSomething() throws Exception {
        // Initialize the database
        costOfSalesAccountRepository.saveAndFlush(costOfSalesAccount);

        // Get all the costOfSalesAccountList where transactionDate equals to DEFAULT_TRANSACTION_DATE
        defaultCostOfSalesAccountShouldBeFound("transactionDate.equals=" + DEFAULT_TRANSACTION_DATE);

        // Get all the costOfSalesAccountList where transactionDate equals to UPDATED_TRANSACTION_DATE
        defaultCostOfSalesAccountShouldNotBeFound("transactionDate.equals=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllCostOfSalesAccountsByTransactionDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        costOfSalesAccountRepository.saveAndFlush(costOfSalesAccount);

        // Get all the costOfSalesAccountList where transactionDate not equals to DEFAULT_TRANSACTION_DATE
        defaultCostOfSalesAccountShouldNotBeFound("transactionDate.notEquals=" + DEFAULT_TRANSACTION_DATE);

        // Get all the costOfSalesAccountList where transactionDate not equals to UPDATED_TRANSACTION_DATE
        defaultCostOfSalesAccountShouldBeFound("transactionDate.notEquals=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllCostOfSalesAccountsByTransactionDateIsInShouldWork() throws Exception {
        // Initialize the database
        costOfSalesAccountRepository.saveAndFlush(costOfSalesAccount);

        // Get all the costOfSalesAccountList where transactionDate in DEFAULT_TRANSACTION_DATE or UPDATED_TRANSACTION_DATE
        defaultCostOfSalesAccountShouldBeFound("transactionDate.in=" + DEFAULT_TRANSACTION_DATE + "," + UPDATED_TRANSACTION_DATE);

        // Get all the costOfSalesAccountList where transactionDate equals to UPDATED_TRANSACTION_DATE
        defaultCostOfSalesAccountShouldNotBeFound("transactionDate.in=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllCostOfSalesAccountsByTransactionDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        costOfSalesAccountRepository.saveAndFlush(costOfSalesAccount);

        // Get all the costOfSalesAccountList where transactionDate is not null
        defaultCostOfSalesAccountShouldBeFound("transactionDate.specified=true");

        // Get all the costOfSalesAccountList where transactionDate is null
        defaultCostOfSalesAccountShouldNotBeFound("transactionDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllCostOfSalesAccountsByTransactionDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        costOfSalesAccountRepository.saveAndFlush(costOfSalesAccount);

        // Get all the costOfSalesAccountList where transactionDate is greater than or equal to DEFAULT_TRANSACTION_DATE
        defaultCostOfSalesAccountShouldBeFound("transactionDate.greaterThanOrEqual=" + DEFAULT_TRANSACTION_DATE);

        // Get all the costOfSalesAccountList where transactionDate is greater than or equal to UPDATED_TRANSACTION_DATE
        defaultCostOfSalesAccountShouldNotBeFound("transactionDate.greaterThanOrEqual=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllCostOfSalesAccountsByTransactionDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        costOfSalesAccountRepository.saveAndFlush(costOfSalesAccount);

        // Get all the costOfSalesAccountList where transactionDate is less than or equal to DEFAULT_TRANSACTION_DATE
        defaultCostOfSalesAccountShouldBeFound("transactionDate.lessThanOrEqual=" + DEFAULT_TRANSACTION_DATE);

        // Get all the costOfSalesAccountList where transactionDate is less than or equal to SMALLER_TRANSACTION_DATE
        defaultCostOfSalesAccountShouldNotBeFound("transactionDate.lessThanOrEqual=" + SMALLER_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllCostOfSalesAccountsByTransactionDateIsLessThanSomething() throws Exception {
        // Initialize the database
        costOfSalesAccountRepository.saveAndFlush(costOfSalesAccount);

        // Get all the costOfSalesAccountList where transactionDate is less than DEFAULT_TRANSACTION_DATE
        defaultCostOfSalesAccountShouldNotBeFound("transactionDate.lessThan=" + DEFAULT_TRANSACTION_DATE);

        // Get all the costOfSalesAccountList where transactionDate is less than UPDATED_TRANSACTION_DATE
        defaultCostOfSalesAccountShouldBeFound("transactionDate.lessThan=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllCostOfSalesAccountsByTransactionDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        costOfSalesAccountRepository.saveAndFlush(costOfSalesAccount);

        // Get all the costOfSalesAccountList where transactionDate is greater than DEFAULT_TRANSACTION_DATE
        defaultCostOfSalesAccountShouldNotBeFound("transactionDate.greaterThan=" + DEFAULT_TRANSACTION_DATE);

        // Get all the costOfSalesAccountList where transactionDate is greater than SMALLER_TRANSACTION_DATE
        defaultCostOfSalesAccountShouldBeFound("transactionDate.greaterThan=" + SMALLER_TRANSACTION_DATE);
    }


    @Test
    @Transactional
    public void getAllCostOfSalesAccountsByTransactionDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        costOfSalesAccountRepository.saveAndFlush(costOfSalesAccount);

        // Get all the costOfSalesAccountList where transactionDescription equals to DEFAULT_TRANSACTION_DESCRIPTION
        defaultCostOfSalesAccountShouldBeFound("transactionDescription.equals=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the costOfSalesAccountList where transactionDescription equals to UPDATED_TRANSACTION_DESCRIPTION
        defaultCostOfSalesAccountShouldNotBeFound("transactionDescription.equals=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCostOfSalesAccountsByTransactionDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        costOfSalesAccountRepository.saveAndFlush(costOfSalesAccount);

        // Get all the costOfSalesAccountList where transactionDescription not equals to DEFAULT_TRANSACTION_DESCRIPTION
        defaultCostOfSalesAccountShouldNotBeFound("transactionDescription.notEquals=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the costOfSalesAccountList where transactionDescription not equals to UPDATED_TRANSACTION_DESCRIPTION
        defaultCostOfSalesAccountShouldBeFound("transactionDescription.notEquals=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCostOfSalesAccountsByTransactionDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        costOfSalesAccountRepository.saveAndFlush(costOfSalesAccount);

        // Get all the costOfSalesAccountList where transactionDescription in DEFAULT_TRANSACTION_DESCRIPTION or UPDATED_TRANSACTION_DESCRIPTION
        defaultCostOfSalesAccountShouldBeFound("transactionDescription.in=" + DEFAULT_TRANSACTION_DESCRIPTION + "," + UPDATED_TRANSACTION_DESCRIPTION);

        // Get all the costOfSalesAccountList where transactionDescription equals to UPDATED_TRANSACTION_DESCRIPTION
        defaultCostOfSalesAccountShouldNotBeFound("transactionDescription.in=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCostOfSalesAccountsByTransactionDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        costOfSalesAccountRepository.saveAndFlush(costOfSalesAccount);

        // Get all the costOfSalesAccountList where transactionDescription is not null
        defaultCostOfSalesAccountShouldBeFound("transactionDescription.specified=true");

        // Get all the costOfSalesAccountList where transactionDescription is null
        defaultCostOfSalesAccountShouldNotBeFound("transactionDescription.specified=false");
    }
                @Test
    @Transactional
    public void getAllCostOfSalesAccountsByTransactionDescriptionContainsSomething() throws Exception {
        // Initialize the database
        costOfSalesAccountRepository.saveAndFlush(costOfSalesAccount);

        // Get all the costOfSalesAccountList where transactionDescription contains DEFAULT_TRANSACTION_DESCRIPTION
        defaultCostOfSalesAccountShouldBeFound("transactionDescription.contains=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the costOfSalesAccountList where transactionDescription contains UPDATED_TRANSACTION_DESCRIPTION
        defaultCostOfSalesAccountShouldNotBeFound("transactionDescription.contains=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCostOfSalesAccountsByTransactionDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        costOfSalesAccountRepository.saveAndFlush(costOfSalesAccount);

        // Get all the costOfSalesAccountList where transactionDescription does not contain DEFAULT_TRANSACTION_DESCRIPTION
        defaultCostOfSalesAccountShouldNotBeFound("transactionDescription.doesNotContain=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the costOfSalesAccountList where transactionDescription does not contain UPDATED_TRANSACTION_DESCRIPTION
        defaultCostOfSalesAccountShouldBeFound("transactionDescription.doesNotContain=" + UPDATED_TRANSACTION_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllCostOfSalesAccountsByTransactionAmountDRIsEqualToSomething() throws Exception {
        // Initialize the database
        costOfSalesAccountRepository.saveAndFlush(costOfSalesAccount);

        // Get all the costOfSalesAccountList where transactionAmountDR equals to DEFAULT_TRANSACTION_AMOUNT_DR
        defaultCostOfSalesAccountShouldBeFound("transactionAmountDR.equals=" + DEFAULT_TRANSACTION_AMOUNT_DR);

        // Get all the costOfSalesAccountList where transactionAmountDR equals to UPDATED_TRANSACTION_AMOUNT_DR
        defaultCostOfSalesAccountShouldNotBeFound("transactionAmountDR.equals=" + UPDATED_TRANSACTION_AMOUNT_DR);
    }

    @Test
    @Transactional
    public void getAllCostOfSalesAccountsByTransactionAmountDRIsNotEqualToSomething() throws Exception {
        // Initialize the database
        costOfSalesAccountRepository.saveAndFlush(costOfSalesAccount);

        // Get all the costOfSalesAccountList where transactionAmountDR not equals to DEFAULT_TRANSACTION_AMOUNT_DR
        defaultCostOfSalesAccountShouldNotBeFound("transactionAmountDR.notEquals=" + DEFAULT_TRANSACTION_AMOUNT_DR);

        // Get all the costOfSalesAccountList where transactionAmountDR not equals to UPDATED_TRANSACTION_AMOUNT_DR
        defaultCostOfSalesAccountShouldBeFound("transactionAmountDR.notEquals=" + UPDATED_TRANSACTION_AMOUNT_DR);
    }

    @Test
    @Transactional
    public void getAllCostOfSalesAccountsByTransactionAmountDRIsInShouldWork() throws Exception {
        // Initialize the database
        costOfSalesAccountRepository.saveAndFlush(costOfSalesAccount);

        // Get all the costOfSalesAccountList where transactionAmountDR in DEFAULT_TRANSACTION_AMOUNT_DR or UPDATED_TRANSACTION_AMOUNT_DR
        defaultCostOfSalesAccountShouldBeFound("transactionAmountDR.in=" + DEFAULT_TRANSACTION_AMOUNT_DR + "," + UPDATED_TRANSACTION_AMOUNT_DR);

        // Get all the costOfSalesAccountList where transactionAmountDR equals to UPDATED_TRANSACTION_AMOUNT_DR
        defaultCostOfSalesAccountShouldNotBeFound("transactionAmountDR.in=" + UPDATED_TRANSACTION_AMOUNT_DR);
    }

    @Test
    @Transactional
    public void getAllCostOfSalesAccountsByTransactionAmountDRIsNullOrNotNull() throws Exception {
        // Initialize the database
        costOfSalesAccountRepository.saveAndFlush(costOfSalesAccount);

        // Get all the costOfSalesAccountList where transactionAmountDR is not null
        defaultCostOfSalesAccountShouldBeFound("transactionAmountDR.specified=true");

        // Get all the costOfSalesAccountList where transactionAmountDR is null
        defaultCostOfSalesAccountShouldNotBeFound("transactionAmountDR.specified=false");
    }

    @Test
    @Transactional
    public void getAllCostOfSalesAccountsByTransactionAmountDRIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        costOfSalesAccountRepository.saveAndFlush(costOfSalesAccount);

        // Get all the costOfSalesAccountList where transactionAmountDR is greater than or equal to DEFAULT_TRANSACTION_AMOUNT_DR
        defaultCostOfSalesAccountShouldBeFound("transactionAmountDR.greaterThanOrEqual=" + DEFAULT_TRANSACTION_AMOUNT_DR);

        // Get all the costOfSalesAccountList where transactionAmountDR is greater than or equal to UPDATED_TRANSACTION_AMOUNT_DR
        defaultCostOfSalesAccountShouldNotBeFound("transactionAmountDR.greaterThanOrEqual=" + UPDATED_TRANSACTION_AMOUNT_DR);
    }

    @Test
    @Transactional
    public void getAllCostOfSalesAccountsByTransactionAmountDRIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        costOfSalesAccountRepository.saveAndFlush(costOfSalesAccount);

        // Get all the costOfSalesAccountList where transactionAmountDR is less than or equal to DEFAULT_TRANSACTION_AMOUNT_DR
        defaultCostOfSalesAccountShouldBeFound("transactionAmountDR.lessThanOrEqual=" + DEFAULT_TRANSACTION_AMOUNT_DR);

        // Get all the costOfSalesAccountList where transactionAmountDR is less than or equal to SMALLER_TRANSACTION_AMOUNT_DR
        defaultCostOfSalesAccountShouldNotBeFound("transactionAmountDR.lessThanOrEqual=" + SMALLER_TRANSACTION_AMOUNT_DR);
    }

    @Test
    @Transactional
    public void getAllCostOfSalesAccountsByTransactionAmountDRIsLessThanSomething() throws Exception {
        // Initialize the database
        costOfSalesAccountRepository.saveAndFlush(costOfSalesAccount);

        // Get all the costOfSalesAccountList where transactionAmountDR is less than DEFAULT_TRANSACTION_AMOUNT_DR
        defaultCostOfSalesAccountShouldNotBeFound("transactionAmountDR.lessThan=" + DEFAULT_TRANSACTION_AMOUNT_DR);

        // Get all the costOfSalesAccountList where transactionAmountDR is less than UPDATED_TRANSACTION_AMOUNT_DR
        defaultCostOfSalesAccountShouldBeFound("transactionAmountDR.lessThan=" + UPDATED_TRANSACTION_AMOUNT_DR);
    }

    @Test
    @Transactional
    public void getAllCostOfSalesAccountsByTransactionAmountDRIsGreaterThanSomething() throws Exception {
        // Initialize the database
        costOfSalesAccountRepository.saveAndFlush(costOfSalesAccount);

        // Get all the costOfSalesAccountList where transactionAmountDR is greater than DEFAULT_TRANSACTION_AMOUNT_DR
        defaultCostOfSalesAccountShouldNotBeFound("transactionAmountDR.greaterThan=" + DEFAULT_TRANSACTION_AMOUNT_DR);

        // Get all the costOfSalesAccountList where transactionAmountDR is greater than SMALLER_TRANSACTION_AMOUNT_DR
        defaultCostOfSalesAccountShouldBeFound("transactionAmountDR.greaterThan=" + SMALLER_TRANSACTION_AMOUNT_DR);
    }


    @Test
    @Transactional
    public void getAllCostOfSalesAccountsByTransactionAmountCRIsEqualToSomething() throws Exception {
        // Initialize the database
        costOfSalesAccountRepository.saveAndFlush(costOfSalesAccount);

        // Get all the costOfSalesAccountList where transactionAmountCR equals to DEFAULT_TRANSACTION_AMOUNT_CR
        defaultCostOfSalesAccountShouldBeFound("transactionAmountCR.equals=" + DEFAULT_TRANSACTION_AMOUNT_CR);

        // Get all the costOfSalesAccountList where transactionAmountCR equals to UPDATED_TRANSACTION_AMOUNT_CR
        defaultCostOfSalesAccountShouldNotBeFound("transactionAmountCR.equals=" + UPDATED_TRANSACTION_AMOUNT_CR);
    }

    @Test
    @Transactional
    public void getAllCostOfSalesAccountsByTransactionAmountCRIsNotEqualToSomething() throws Exception {
        // Initialize the database
        costOfSalesAccountRepository.saveAndFlush(costOfSalesAccount);

        // Get all the costOfSalesAccountList where transactionAmountCR not equals to DEFAULT_TRANSACTION_AMOUNT_CR
        defaultCostOfSalesAccountShouldNotBeFound("transactionAmountCR.notEquals=" + DEFAULT_TRANSACTION_AMOUNT_CR);

        // Get all the costOfSalesAccountList where transactionAmountCR not equals to UPDATED_TRANSACTION_AMOUNT_CR
        defaultCostOfSalesAccountShouldBeFound("transactionAmountCR.notEquals=" + UPDATED_TRANSACTION_AMOUNT_CR);
    }

    @Test
    @Transactional
    public void getAllCostOfSalesAccountsByTransactionAmountCRIsInShouldWork() throws Exception {
        // Initialize the database
        costOfSalesAccountRepository.saveAndFlush(costOfSalesAccount);

        // Get all the costOfSalesAccountList where transactionAmountCR in DEFAULT_TRANSACTION_AMOUNT_CR or UPDATED_TRANSACTION_AMOUNT_CR
        defaultCostOfSalesAccountShouldBeFound("transactionAmountCR.in=" + DEFAULT_TRANSACTION_AMOUNT_CR + "," + UPDATED_TRANSACTION_AMOUNT_CR);

        // Get all the costOfSalesAccountList where transactionAmountCR equals to UPDATED_TRANSACTION_AMOUNT_CR
        defaultCostOfSalesAccountShouldNotBeFound("transactionAmountCR.in=" + UPDATED_TRANSACTION_AMOUNT_CR);
    }

    @Test
    @Transactional
    public void getAllCostOfSalesAccountsByTransactionAmountCRIsNullOrNotNull() throws Exception {
        // Initialize the database
        costOfSalesAccountRepository.saveAndFlush(costOfSalesAccount);

        // Get all the costOfSalesAccountList where transactionAmountCR is not null
        defaultCostOfSalesAccountShouldBeFound("transactionAmountCR.specified=true");

        // Get all the costOfSalesAccountList where transactionAmountCR is null
        defaultCostOfSalesAccountShouldNotBeFound("transactionAmountCR.specified=false");
    }

    @Test
    @Transactional
    public void getAllCostOfSalesAccountsByTransactionAmountCRIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        costOfSalesAccountRepository.saveAndFlush(costOfSalesAccount);

        // Get all the costOfSalesAccountList where transactionAmountCR is greater than or equal to DEFAULT_TRANSACTION_AMOUNT_CR
        defaultCostOfSalesAccountShouldBeFound("transactionAmountCR.greaterThanOrEqual=" + DEFAULT_TRANSACTION_AMOUNT_CR);

        // Get all the costOfSalesAccountList where transactionAmountCR is greater than or equal to UPDATED_TRANSACTION_AMOUNT_CR
        defaultCostOfSalesAccountShouldNotBeFound("transactionAmountCR.greaterThanOrEqual=" + UPDATED_TRANSACTION_AMOUNT_CR);
    }

    @Test
    @Transactional
    public void getAllCostOfSalesAccountsByTransactionAmountCRIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        costOfSalesAccountRepository.saveAndFlush(costOfSalesAccount);

        // Get all the costOfSalesAccountList where transactionAmountCR is less than or equal to DEFAULT_TRANSACTION_AMOUNT_CR
        defaultCostOfSalesAccountShouldBeFound("transactionAmountCR.lessThanOrEqual=" + DEFAULT_TRANSACTION_AMOUNT_CR);

        // Get all the costOfSalesAccountList where transactionAmountCR is less than or equal to SMALLER_TRANSACTION_AMOUNT_CR
        defaultCostOfSalesAccountShouldNotBeFound("transactionAmountCR.lessThanOrEqual=" + SMALLER_TRANSACTION_AMOUNT_CR);
    }

    @Test
    @Transactional
    public void getAllCostOfSalesAccountsByTransactionAmountCRIsLessThanSomething() throws Exception {
        // Initialize the database
        costOfSalesAccountRepository.saveAndFlush(costOfSalesAccount);

        // Get all the costOfSalesAccountList where transactionAmountCR is less than DEFAULT_TRANSACTION_AMOUNT_CR
        defaultCostOfSalesAccountShouldNotBeFound("transactionAmountCR.lessThan=" + DEFAULT_TRANSACTION_AMOUNT_CR);

        // Get all the costOfSalesAccountList where transactionAmountCR is less than UPDATED_TRANSACTION_AMOUNT_CR
        defaultCostOfSalesAccountShouldBeFound("transactionAmountCR.lessThan=" + UPDATED_TRANSACTION_AMOUNT_CR);
    }

    @Test
    @Transactional
    public void getAllCostOfSalesAccountsByTransactionAmountCRIsGreaterThanSomething() throws Exception {
        // Initialize the database
        costOfSalesAccountRepository.saveAndFlush(costOfSalesAccount);

        // Get all the costOfSalesAccountList where transactionAmountCR is greater than DEFAULT_TRANSACTION_AMOUNT_CR
        defaultCostOfSalesAccountShouldNotBeFound("transactionAmountCR.greaterThan=" + DEFAULT_TRANSACTION_AMOUNT_CR);

        // Get all the costOfSalesAccountList where transactionAmountCR is greater than SMALLER_TRANSACTION_AMOUNT_CR
        defaultCostOfSalesAccountShouldBeFound("transactionAmountCR.greaterThan=" + SMALLER_TRANSACTION_AMOUNT_CR);
    }


    @Test
    @Transactional
    public void getAllCostOfSalesAccountsByTransactionBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        costOfSalesAccountRepository.saveAndFlush(costOfSalesAccount);

        // Get all the costOfSalesAccountList where transactionBalance equals to DEFAULT_TRANSACTION_BALANCE
        defaultCostOfSalesAccountShouldBeFound("transactionBalance.equals=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the costOfSalesAccountList where transactionBalance equals to UPDATED_TRANSACTION_BALANCE
        defaultCostOfSalesAccountShouldNotBeFound("transactionBalance.equals=" + UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void getAllCostOfSalesAccountsByTransactionBalanceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        costOfSalesAccountRepository.saveAndFlush(costOfSalesAccount);

        // Get all the costOfSalesAccountList where transactionBalance not equals to DEFAULT_TRANSACTION_BALANCE
        defaultCostOfSalesAccountShouldNotBeFound("transactionBalance.notEquals=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the costOfSalesAccountList where transactionBalance not equals to UPDATED_TRANSACTION_BALANCE
        defaultCostOfSalesAccountShouldBeFound("transactionBalance.notEquals=" + UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void getAllCostOfSalesAccountsByTransactionBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        costOfSalesAccountRepository.saveAndFlush(costOfSalesAccount);

        // Get all the costOfSalesAccountList where transactionBalance in DEFAULT_TRANSACTION_BALANCE or UPDATED_TRANSACTION_BALANCE
        defaultCostOfSalesAccountShouldBeFound("transactionBalance.in=" + DEFAULT_TRANSACTION_BALANCE + "," + UPDATED_TRANSACTION_BALANCE);

        // Get all the costOfSalesAccountList where transactionBalance equals to UPDATED_TRANSACTION_BALANCE
        defaultCostOfSalesAccountShouldNotBeFound("transactionBalance.in=" + UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void getAllCostOfSalesAccountsByTransactionBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        costOfSalesAccountRepository.saveAndFlush(costOfSalesAccount);

        // Get all the costOfSalesAccountList where transactionBalance is not null
        defaultCostOfSalesAccountShouldBeFound("transactionBalance.specified=true");

        // Get all the costOfSalesAccountList where transactionBalance is null
        defaultCostOfSalesAccountShouldNotBeFound("transactionBalance.specified=false");
    }

    @Test
    @Transactional
    public void getAllCostOfSalesAccountsByTransactionBalanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        costOfSalesAccountRepository.saveAndFlush(costOfSalesAccount);

        // Get all the costOfSalesAccountList where transactionBalance is greater than or equal to DEFAULT_TRANSACTION_BALANCE
        defaultCostOfSalesAccountShouldBeFound("transactionBalance.greaterThanOrEqual=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the costOfSalesAccountList where transactionBalance is greater than or equal to UPDATED_TRANSACTION_BALANCE
        defaultCostOfSalesAccountShouldNotBeFound("transactionBalance.greaterThanOrEqual=" + UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void getAllCostOfSalesAccountsByTransactionBalanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        costOfSalesAccountRepository.saveAndFlush(costOfSalesAccount);

        // Get all the costOfSalesAccountList where transactionBalance is less than or equal to DEFAULT_TRANSACTION_BALANCE
        defaultCostOfSalesAccountShouldBeFound("transactionBalance.lessThanOrEqual=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the costOfSalesAccountList where transactionBalance is less than or equal to SMALLER_TRANSACTION_BALANCE
        defaultCostOfSalesAccountShouldNotBeFound("transactionBalance.lessThanOrEqual=" + SMALLER_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void getAllCostOfSalesAccountsByTransactionBalanceIsLessThanSomething() throws Exception {
        // Initialize the database
        costOfSalesAccountRepository.saveAndFlush(costOfSalesAccount);

        // Get all the costOfSalesAccountList where transactionBalance is less than DEFAULT_TRANSACTION_BALANCE
        defaultCostOfSalesAccountShouldNotBeFound("transactionBalance.lessThan=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the costOfSalesAccountList where transactionBalance is less than UPDATED_TRANSACTION_BALANCE
        defaultCostOfSalesAccountShouldBeFound("transactionBalance.lessThan=" + UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void getAllCostOfSalesAccountsByTransactionBalanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        costOfSalesAccountRepository.saveAndFlush(costOfSalesAccount);

        // Get all the costOfSalesAccountList where transactionBalance is greater than DEFAULT_TRANSACTION_BALANCE
        defaultCostOfSalesAccountShouldNotBeFound("transactionBalance.greaterThan=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the costOfSalesAccountList where transactionBalance is greater than SMALLER_TRANSACTION_BALANCE
        defaultCostOfSalesAccountShouldBeFound("transactionBalance.greaterThan=" + SMALLER_TRANSACTION_BALANCE);
    }


    @Test
    @Transactional
    public void getAllCostOfSalesAccountsByLocationIsEqualToSomething() throws Exception {
        // Get already existing entity
        Location location = costOfSalesAccount.getLocation();
        costOfSalesAccountRepository.saveAndFlush(costOfSalesAccount);
        Long locationId = location.getId();

        // Get all the costOfSalesAccountList where location equals to locationId
        defaultCostOfSalesAccountShouldBeFound("locationId.equals=" + locationId);

        // Get all the costOfSalesAccountList where location equals to locationId + 1
        defaultCostOfSalesAccountShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }


    @Test
    @Transactional
    public void getAllCostOfSalesAccountsByTransactionTypeIsEqualToSomething() throws Exception {
        // Get already existing entity
        TransactionType transactionType = costOfSalesAccount.getTransactionType();
        costOfSalesAccountRepository.saveAndFlush(costOfSalesAccount);
        Long transactionTypeId = transactionType.getId();

        // Get all the costOfSalesAccountList where transactionType equals to transactionTypeId
        defaultCostOfSalesAccountShouldBeFound("transactionTypeId.equals=" + transactionTypeId);

        // Get all the costOfSalesAccountList where transactionType equals to transactionTypeId + 1
        defaultCostOfSalesAccountShouldNotBeFound("transactionTypeId.equals=" + (transactionTypeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCostOfSalesAccountShouldBeFound(String filter) throws Exception {
        restCostOfSalesAccountMockMvc.perform(get("/api/cost-of-sales-accounts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(costOfSalesAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].transactionDescription").value(hasItem(DEFAULT_TRANSACTION_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].transactionAmountDR").value(hasItem(DEFAULT_TRANSACTION_AMOUNT_DR.intValue())))
            .andExpect(jsonPath("$.[*].transactionAmountCR").value(hasItem(DEFAULT_TRANSACTION_AMOUNT_CR.intValue())))
            .andExpect(jsonPath("$.[*].transactionBalance").value(hasItem(DEFAULT_TRANSACTION_BALANCE.intValue())));

        // Check, that the count call also returns 1
        restCostOfSalesAccountMockMvc.perform(get("/api/cost-of-sales-accounts/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCostOfSalesAccountShouldNotBeFound(String filter) throws Exception {
        restCostOfSalesAccountMockMvc.perform(get("/api/cost-of-sales-accounts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCostOfSalesAccountMockMvc.perform(get("/api/cost-of-sales-accounts/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingCostOfSalesAccount() throws Exception {
        // Get the costOfSalesAccount
        restCostOfSalesAccountMockMvc.perform(get("/api/cost-of-sales-accounts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCostOfSalesAccount() throws Exception {
        // Initialize the database
        costOfSalesAccountService.save(costOfSalesAccount);

        int databaseSizeBeforeUpdate = costOfSalesAccountRepository.findAll().size();

        // Update the costOfSalesAccount
        CostOfSalesAccount updatedCostOfSalesAccount = costOfSalesAccountRepository.findById(costOfSalesAccount.getId()).get();
        // Disconnect from session so that the updates on updatedCostOfSalesAccount are not directly saved in db
        em.detach(updatedCostOfSalesAccount);
        updatedCostOfSalesAccount
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .transactionDescription(UPDATED_TRANSACTION_DESCRIPTION)
            .transactionAmountDR(UPDATED_TRANSACTION_AMOUNT_DR)
            .transactionAmountCR(UPDATED_TRANSACTION_AMOUNT_CR)
            .transactionBalance(UPDATED_TRANSACTION_BALANCE);

        restCostOfSalesAccountMockMvc.perform(put("/api/cost-of-sales-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCostOfSalesAccount)))
            .andExpect(status().isOk());

        // Validate the CostOfSalesAccount in the database
        List<CostOfSalesAccount> costOfSalesAccountList = costOfSalesAccountRepository.findAll();
        assertThat(costOfSalesAccountList).hasSize(databaseSizeBeforeUpdate);
        CostOfSalesAccount testCostOfSalesAccount = costOfSalesAccountList.get(costOfSalesAccountList.size() - 1);
        assertThat(testCostOfSalesAccount.getTransactionDate()).isEqualTo(UPDATED_TRANSACTION_DATE);
        assertThat(testCostOfSalesAccount.getTransactionDescription()).isEqualTo(UPDATED_TRANSACTION_DESCRIPTION);
        assertThat(testCostOfSalesAccount.getTransactionAmountDR()).isEqualTo(UPDATED_TRANSACTION_AMOUNT_DR);
        assertThat(testCostOfSalesAccount.getTransactionAmountCR()).isEqualTo(UPDATED_TRANSACTION_AMOUNT_CR);
        assertThat(testCostOfSalesAccount.getTransactionBalance()).isEqualTo(UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void updateNonExistingCostOfSalesAccount() throws Exception {
        int databaseSizeBeforeUpdate = costOfSalesAccountRepository.findAll().size();

        // Create the CostOfSalesAccount

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCostOfSalesAccountMockMvc.perform(put("/api/cost-of-sales-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(costOfSalesAccount)))
            .andExpect(status().isBadRequest());

        // Validate the CostOfSalesAccount in the database
        List<CostOfSalesAccount> costOfSalesAccountList = costOfSalesAccountRepository.findAll();
        assertThat(costOfSalesAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCostOfSalesAccount() throws Exception {
        // Initialize the database
        costOfSalesAccountService.save(costOfSalesAccount);

        int databaseSizeBeforeDelete = costOfSalesAccountRepository.findAll().size();

        // Delete the costOfSalesAccount
        restCostOfSalesAccountMockMvc.perform(delete("/api/cost-of-sales-accounts/{id}", costOfSalesAccount.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CostOfSalesAccount> costOfSalesAccountList = costOfSalesAccountRepository.findAll();
        assertThat(costOfSalesAccountList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

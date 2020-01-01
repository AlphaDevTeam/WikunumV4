package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.WikunumApp;
import com.alphadevs.sales.domain.ExpenseAccountBalance;
import com.alphadevs.sales.domain.Location;
import com.alphadevs.sales.domain.TransactionType;
import com.alphadevs.sales.domain.Expense;
import com.alphadevs.sales.repository.ExpenseAccountBalanceRepository;
import com.alphadevs.sales.service.ExpenseAccountBalanceService;
import com.alphadevs.sales.web.rest.errors.ExceptionTranslator;
import com.alphadevs.sales.service.dto.ExpenseAccountBalanceCriteria;
import com.alphadevs.sales.service.ExpenseAccountBalanceQueryService;

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
import java.util.List;

import static com.alphadevs.sales.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ExpenseAccountBalanceResource} REST controller.
 */
@SpringBootTest(classes = WikunumApp.class)
public class ExpenseAccountBalanceResourceIT {

    private static final BigDecimal DEFAULT_BALANCE = new BigDecimal(1);
    private static final BigDecimal UPDATED_BALANCE = new BigDecimal(2);
    private static final BigDecimal SMALLER_BALANCE = new BigDecimal(1 - 1);

    @Autowired
    private ExpenseAccountBalanceRepository expenseAccountBalanceRepository;

    @Autowired
    private ExpenseAccountBalanceService expenseAccountBalanceService;

    @Autowired
    private ExpenseAccountBalanceQueryService expenseAccountBalanceQueryService;

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

    private MockMvc restExpenseAccountBalanceMockMvc;

    private ExpenseAccountBalance expenseAccountBalance;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ExpenseAccountBalanceResource expenseAccountBalanceResource = new ExpenseAccountBalanceResource(expenseAccountBalanceService, expenseAccountBalanceQueryService);
        this.restExpenseAccountBalanceMockMvc = MockMvcBuilders.standaloneSetup(expenseAccountBalanceResource)
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
    public static ExpenseAccountBalance createEntity(EntityManager em) {
        ExpenseAccountBalance expenseAccountBalance = new ExpenseAccountBalance()
            .balance(DEFAULT_BALANCE);
        // Add required entity
        Location location;
        if (TestUtil.findAll(em, Location.class).isEmpty()) {
            location = LocationResourceIT.createEntity(em);
            em.persist(location);
            em.flush();
        } else {
            location = TestUtil.findAll(em, Location.class).get(0);
        }
        expenseAccountBalance.setLocation(location);
        // Add required entity
        TransactionType transactionType;
        if (TestUtil.findAll(em, TransactionType.class).isEmpty()) {
            transactionType = TransactionTypeResourceIT.createEntity(em);
            em.persist(transactionType);
            em.flush();
        } else {
            transactionType = TestUtil.findAll(em, TransactionType.class).get(0);
        }
        expenseAccountBalance.setTransactionType(transactionType);
        // Add required entity
        Expense expense;
        if (TestUtil.findAll(em, Expense.class).isEmpty()) {
            expense = ExpenseResourceIT.createEntity(em);
            em.persist(expense);
            em.flush();
        } else {
            expense = TestUtil.findAll(em, Expense.class).get(0);
        }
        expenseAccountBalance.setExpense(expense);
        return expenseAccountBalance;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExpenseAccountBalance createUpdatedEntity(EntityManager em) {
        ExpenseAccountBalance expenseAccountBalance = new ExpenseAccountBalance()
            .balance(UPDATED_BALANCE);
        // Add required entity
        Location location;
        if (TestUtil.findAll(em, Location.class).isEmpty()) {
            location = LocationResourceIT.createUpdatedEntity(em);
            em.persist(location);
            em.flush();
        } else {
            location = TestUtil.findAll(em, Location.class).get(0);
        }
        expenseAccountBalance.setLocation(location);
        // Add required entity
        TransactionType transactionType;
        if (TestUtil.findAll(em, TransactionType.class).isEmpty()) {
            transactionType = TransactionTypeResourceIT.createUpdatedEntity(em);
            em.persist(transactionType);
            em.flush();
        } else {
            transactionType = TestUtil.findAll(em, TransactionType.class).get(0);
        }
        expenseAccountBalance.setTransactionType(transactionType);
        // Add required entity
        Expense expense;
        if (TestUtil.findAll(em, Expense.class).isEmpty()) {
            expense = ExpenseResourceIT.createUpdatedEntity(em);
            em.persist(expense);
            em.flush();
        } else {
            expense = TestUtil.findAll(em, Expense.class).get(0);
        }
        expenseAccountBalance.setExpense(expense);
        return expenseAccountBalance;
    }

    @BeforeEach
    public void initTest() {
        expenseAccountBalance = createEntity(em);
    }

    @Test
    @Transactional
    public void createExpenseAccountBalance() throws Exception {
        int databaseSizeBeforeCreate = expenseAccountBalanceRepository.findAll().size();

        // Create the ExpenseAccountBalance
        restExpenseAccountBalanceMockMvc.perform(post("/api/expense-account-balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(expenseAccountBalance)))
            .andExpect(status().isCreated());

        // Validate the ExpenseAccountBalance in the database
        List<ExpenseAccountBalance> expenseAccountBalanceList = expenseAccountBalanceRepository.findAll();
        assertThat(expenseAccountBalanceList).hasSize(databaseSizeBeforeCreate + 1);
        ExpenseAccountBalance testExpenseAccountBalance = expenseAccountBalanceList.get(expenseAccountBalanceList.size() - 1);
        assertThat(testExpenseAccountBalance.getBalance()).isEqualTo(DEFAULT_BALANCE);
    }

    @Test
    @Transactional
    public void createExpenseAccountBalanceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = expenseAccountBalanceRepository.findAll().size();

        // Create the ExpenseAccountBalance with an existing ID
        expenseAccountBalance.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restExpenseAccountBalanceMockMvc.perform(post("/api/expense-account-balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(expenseAccountBalance)))
            .andExpect(status().isBadRequest());

        // Validate the ExpenseAccountBalance in the database
        List<ExpenseAccountBalance> expenseAccountBalanceList = expenseAccountBalanceRepository.findAll();
        assertThat(expenseAccountBalanceList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkBalanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = expenseAccountBalanceRepository.findAll().size();
        // set the field null
        expenseAccountBalance.setBalance(null);

        // Create the ExpenseAccountBalance, which fails.

        restExpenseAccountBalanceMockMvc.perform(post("/api/expense-account-balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(expenseAccountBalance)))
            .andExpect(status().isBadRequest());

        List<ExpenseAccountBalance> expenseAccountBalanceList = expenseAccountBalanceRepository.findAll();
        assertThat(expenseAccountBalanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllExpenseAccountBalances() throws Exception {
        // Initialize the database
        expenseAccountBalanceRepository.saveAndFlush(expenseAccountBalance);

        // Get all the expenseAccountBalanceList
        restExpenseAccountBalanceMockMvc.perform(get("/api/expense-account-balances?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(expenseAccountBalance.getId().intValue())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.intValue())));
    }
    
    @Test
    @Transactional
    public void getExpenseAccountBalance() throws Exception {
        // Initialize the database
        expenseAccountBalanceRepository.saveAndFlush(expenseAccountBalance);

        // Get the expenseAccountBalance
        restExpenseAccountBalanceMockMvc.perform(get("/api/expense-account-balances/{id}", expenseAccountBalance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(expenseAccountBalance.getId().intValue()))
            .andExpect(jsonPath("$.balance").value(DEFAULT_BALANCE.intValue()));
    }


    @Test
    @Transactional
    public void getExpenseAccountBalancesByIdFiltering() throws Exception {
        // Initialize the database
        expenseAccountBalanceRepository.saveAndFlush(expenseAccountBalance);

        Long id = expenseAccountBalance.getId();

        defaultExpenseAccountBalanceShouldBeFound("id.equals=" + id);
        defaultExpenseAccountBalanceShouldNotBeFound("id.notEquals=" + id);

        defaultExpenseAccountBalanceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultExpenseAccountBalanceShouldNotBeFound("id.greaterThan=" + id);

        defaultExpenseAccountBalanceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultExpenseAccountBalanceShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllExpenseAccountBalancesByBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        expenseAccountBalanceRepository.saveAndFlush(expenseAccountBalance);

        // Get all the expenseAccountBalanceList where balance equals to DEFAULT_BALANCE
        defaultExpenseAccountBalanceShouldBeFound("balance.equals=" + DEFAULT_BALANCE);

        // Get all the expenseAccountBalanceList where balance equals to UPDATED_BALANCE
        defaultExpenseAccountBalanceShouldNotBeFound("balance.equals=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllExpenseAccountBalancesByBalanceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        expenseAccountBalanceRepository.saveAndFlush(expenseAccountBalance);

        // Get all the expenseAccountBalanceList where balance not equals to DEFAULT_BALANCE
        defaultExpenseAccountBalanceShouldNotBeFound("balance.notEquals=" + DEFAULT_BALANCE);

        // Get all the expenseAccountBalanceList where balance not equals to UPDATED_BALANCE
        defaultExpenseAccountBalanceShouldBeFound("balance.notEquals=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllExpenseAccountBalancesByBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        expenseAccountBalanceRepository.saveAndFlush(expenseAccountBalance);

        // Get all the expenseAccountBalanceList where balance in DEFAULT_BALANCE or UPDATED_BALANCE
        defaultExpenseAccountBalanceShouldBeFound("balance.in=" + DEFAULT_BALANCE + "," + UPDATED_BALANCE);

        // Get all the expenseAccountBalanceList where balance equals to UPDATED_BALANCE
        defaultExpenseAccountBalanceShouldNotBeFound("balance.in=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllExpenseAccountBalancesByBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        expenseAccountBalanceRepository.saveAndFlush(expenseAccountBalance);

        // Get all the expenseAccountBalanceList where balance is not null
        defaultExpenseAccountBalanceShouldBeFound("balance.specified=true");

        // Get all the expenseAccountBalanceList where balance is null
        defaultExpenseAccountBalanceShouldNotBeFound("balance.specified=false");
    }

    @Test
    @Transactional
    public void getAllExpenseAccountBalancesByBalanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        expenseAccountBalanceRepository.saveAndFlush(expenseAccountBalance);

        // Get all the expenseAccountBalanceList where balance is greater than or equal to DEFAULT_BALANCE
        defaultExpenseAccountBalanceShouldBeFound("balance.greaterThanOrEqual=" + DEFAULT_BALANCE);

        // Get all the expenseAccountBalanceList where balance is greater than or equal to UPDATED_BALANCE
        defaultExpenseAccountBalanceShouldNotBeFound("balance.greaterThanOrEqual=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllExpenseAccountBalancesByBalanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        expenseAccountBalanceRepository.saveAndFlush(expenseAccountBalance);

        // Get all the expenseAccountBalanceList where balance is less than or equal to DEFAULT_BALANCE
        defaultExpenseAccountBalanceShouldBeFound("balance.lessThanOrEqual=" + DEFAULT_BALANCE);

        // Get all the expenseAccountBalanceList where balance is less than or equal to SMALLER_BALANCE
        defaultExpenseAccountBalanceShouldNotBeFound("balance.lessThanOrEqual=" + SMALLER_BALANCE);
    }

    @Test
    @Transactional
    public void getAllExpenseAccountBalancesByBalanceIsLessThanSomething() throws Exception {
        // Initialize the database
        expenseAccountBalanceRepository.saveAndFlush(expenseAccountBalance);

        // Get all the expenseAccountBalanceList where balance is less than DEFAULT_BALANCE
        defaultExpenseAccountBalanceShouldNotBeFound("balance.lessThan=" + DEFAULT_BALANCE);

        // Get all the expenseAccountBalanceList where balance is less than UPDATED_BALANCE
        defaultExpenseAccountBalanceShouldBeFound("balance.lessThan=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllExpenseAccountBalancesByBalanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        expenseAccountBalanceRepository.saveAndFlush(expenseAccountBalance);

        // Get all the expenseAccountBalanceList where balance is greater than DEFAULT_BALANCE
        defaultExpenseAccountBalanceShouldNotBeFound("balance.greaterThan=" + DEFAULT_BALANCE);

        // Get all the expenseAccountBalanceList where balance is greater than SMALLER_BALANCE
        defaultExpenseAccountBalanceShouldBeFound("balance.greaterThan=" + SMALLER_BALANCE);
    }


    @Test
    @Transactional
    public void getAllExpenseAccountBalancesByLocationIsEqualToSomething() throws Exception {
        // Get already existing entity
        Location location = expenseAccountBalance.getLocation();
        expenseAccountBalanceRepository.saveAndFlush(expenseAccountBalance);
        Long locationId = location.getId();

        // Get all the expenseAccountBalanceList where location equals to locationId
        defaultExpenseAccountBalanceShouldBeFound("locationId.equals=" + locationId);

        // Get all the expenseAccountBalanceList where location equals to locationId + 1
        defaultExpenseAccountBalanceShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }


    @Test
    @Transactional
    public void getAllExpenseAccountBalancesByTransactionTypeIsEqualToSomething() throws Exception {
        // Get already existing entity
        TransactionType transactionType = expenseAccountBalance.getTransactionType();
        expenseAccountBalanceRepository.saveAndFlush(expenseAccountBalance);
        Long transactionTypeId = transactionType.getId();

        // Get all the expenseAccountBalanceList where transactionType equals to transactionTypeId
        defaultExpenseAccountBalanceShouldBeFound("transactionTypeId.equals=" + transactionTypeId);

        // Get all the expenseAccountBalanceList where transactionType equals to transactionTypeId + 1
        defaultExpenseAccountBalanceShouldNotBeFound("transactionTypeId.equals=" + (transactionTypeId + 1));
    }


    @Test
    @Transactional
    public void getAllExpenseAccountBalancesByExpenseIsEqualToSomething() throws Exception {
        // Get already existing entity
        Expense expense = expenseAccountBalance.getExpense();
        expenseAccountBalanceRepository.saveAndFlush(expenseAccountBalance);
        Long expenseId = expense.getId();

        // Get all the expenseAccountBalanceList where expense equals to expenseId
        defaultExpenseAccountBalanceShouldBeFound("expenseId.equals=" + expenseId);

        // Get all the expenseAccountBalanceList where expense equals to expenseId + 1
        defaultExpenseAccountBalanceShouldNotBeFound("expenseId.equals=" + (expenseId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultExpenseAccountBalanceShouldBeFound(String filter) throws Exception {
        restExpenseAccountBalanceMockMvc.perform(get("/api/expense-account-balances?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(expenseAccountBalance.getId().intValue())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.intValue())));

        // Check, that the count call also returns 1
        restExpenseAccountBalanceMockMvc.perform(get("/api/expense-account-balances/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultExpenseAccountBalanceShouldNotBeFound(String filter) throws Exception {
        restExpenseAccountBalanceMockMvc.perform(get("/api/expense-account-balances?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restExpenseAccountBalanceMockMvc.perform(get("/api/expense-account-balances/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingExpenseAccountBalance() throws Exception {
        // Get the expenseAccountBalance
        restExpenseAccountBalanceMockMvc.perform(get("/api/expense-account-balances/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExpenseAccountBalance() throws Exception {
        // Initialize the database
        expenseAccountBalanceService.save(expenseAccountBalance);

        int databaseSizeBeforeUpdate = expenseAccountBalanceRepository.findAll().size();

        // Update the expenseAccountBalance
        ExpenseAccountBalance updatedExpenseAccountBalance = expenseAccountBalanceRepository.findById(expenseAccountBalance.getId()).get();
        // Disconnect from session so that the updates on updatedExpenseAccountBalance are not directly saved in db
        em.detach(updatedExpenseAccountBalance);
        updatedExpenseAccountBalance
            .balance(UPDATED_BALANCE);

        restExpenseAccountBalanceMockMvc.perform(put("/api/expense-account-balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedExpenseAccountBalance)))
            .andExpect(status().isOk());

        // Validate the ExpenseAccountBalance in the database
        List<ExpenseAccountBalance> expenseAccountBalanceList = expenseAccountBalanceRepository.findAll();
        assertThat(expenseAccountBalanceList).hasSize(databaseSizeBeforeUpdate);
        ExpenseAccountBalance testExpenseAccountBalance = expenseAccountBalanceList.get(expenseAccountBalanceList.size() - 1);
        assertThat(testExpenseAccountBalance.getBalance()).isEqualTo(UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void updateNonExistingExpenseAccountBalance() throws Exception {
        int databaseSizeBeforeUpdate = expenseAccountBalanceRepository.findAll().size();

        // Create the ExpenseAccountBalance

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExpenseAccountBalanceMockMvc.perform(put("/api/expense-account-balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(expenseAccountBalance)))
            .andExpect(status().isBadRequest());

        // Validate the ExpenseAccountBalance in the database
        List<ExpenseAccountBalance> expenseAccountBalanceList = expenseAccountBalanceRepository.findAll();
        assertThat(expenseAccountBalanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteExpenseAccountBalance() throws Exception {
        // Initialize the database
        expenseAccountBalanceService.save(expenseAccountBalance);

        int databaseSizeBeforeDelete = expenseAccountBalanceRepository.findAll().size();

        // Delete the expenseAccountBalance
        restExpenseAccountBalanceMockMvc.perform(delete("/api/expense-account-balances/{id}", expenseAccountBalance.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ExpenseAccountBalance> expenseAccountBalanceList = expenseAccountBalanceRepository.findAll();
        assertThat(expenseAccountBalanceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

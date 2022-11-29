package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.WikunumApp;
import com.alphadevs.sales.domain.Expense;
import com.alphadevs.sales.domain.Location;
import com.alphadevs.sales.repository.ExpenseRepository;
import com.alphadevs.sales.service.ExpenseService;
import com.alphadevs.sales.web.rest.errors.ExceptionTranslator;
import com.alphadevs.sales.service.dto.ExpenseCriteria;
import com.alphadevs.sales.service.ExpenseQueryService;

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
 * Integration tests for the {@link ExpenseResource} REST controller.
 */
@SpringBootTest(classes = WikunumApp.class)
public class ExpenseResourceIT {

    private static final String DEFAULT_EXPENSE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_EXPENSE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_EXPENSE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_EXPENSE_NAME = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_EXPENSE_LIMIT = new BigDecimal(1);
    private static final BigDecimal UPDATED_EXPENSE_LIMIT = new BigDecimal(2);
    private static final BigDecimal SMALLER_EXPENSE_LIMIT = new BigDecimal(1 - 1);

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private ExpenseQueryService expenseQueryService;

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

    private MockMvc restExpenseMockMvc;

    private Expense expense;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ExpenseResource expenseResource = new ExpenseResource(expenseService, expenseQueryService);
        this.restExpenseMockMvc = MockMvcBuilders.standaloneSetup(expenseResource)
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
    public static Expense createEntity(EntityManager em) {
        Expense expense = new Expense()
            .expenseCode(DEFAULT_EXPENSE_CODE)
            .expenseName(DEFAULT_EXPENSE_NAME)
            .expenseLimit(DEFAULT_EXPENSE_LIMIT)
            .isActive(DEFAULT_IS_ACTIVE);
        // Add required entity
        Location location;
        if (TestUtil.findAll(em, Location.class).isEmpty()) {
            location = LocationResourceIT.createEntity(em);
            em.persist(location);
            em.flush();
        } else {
            location = TestUtil.findAll(em, Location.class).get(0);
        }
        expense.setLocation(location);
        return expense;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Expense createUpdatedEntity(EntityManager em) {
        Expense expense = new Expense()
            .expenseCode(UPDATED_EXPENSE_CODE)
            .expenseName(UPDATED_EXPENSE_NAME)
            .expenseLimit(UPDATED_EXPENSE_LIMIT)
            .isActive(UPDATED_IS_ACTIVE);
        // Add required entity
        Location location;
        if (TestUtil.findAll(em, Location.class).isEmpty()) {
            location = LocationResourceIT.createUpdatedEntity(em);
            em.persist(location);
            em.flush();
        } else {
            location = TestUtil.findAll(em, Location.class).get(0);
        }
        expense.setLocation(location);
        return expense;
    }

    @BeforeEach
    public void initTest() {
        expense = createEntity(em);
    }

    @Test
    @Transactional
    public void createExpense() throws Exception {
        int databaseSizeBeforeCreate = expenseRepository.findAll().size();

        // Create the Expense
        restExpenseMockMvc.perform(post("/api/expenses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(expense)))
            .andExpect(status().isCreated());

        // Validate the Expense in the database
        List<Expense> expenseList = expenseRepository.findAll();
        assertThat(expenseList).hasSize(databaseSizeBeforeCreate + 1);
        Expense testExpense = expenseList.get(expenseList.size() - 1);
        assertThat(testExpense.getExpenseCode()).isEqualTo(DEFAULT_EXPENSE_CODE);
        assertThat(testExpense.getExpenseName()).isEqualTo(DEFAULT_EXPENSE_NAME);
        assertThat(testExpense.getExpenseLimit()).isEqualTo(DEFAULT_EXPENSE_LIMIT);
        assertThat(testExpense.isIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void createExpenseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = expenseRepository.findAll().size();

        // Create the Expense with an existing ID
        expense.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restExpenseMockMvc.perform(post("/api/expenses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(expense)))
            .andExpect(status().isBadRequest());

        // Validate the Expense in the database
        List<Expense> expenseList = expenseRepository.findAll();
        assertThat(expenseList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkExpenseCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = expenseRepository.findAll().size();
        // set the field null
        expense.setExpenseCode(null);

        // Create the Expense, which fails.

        restExpenseMockMvc.perform(post("/api/expenses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(expense)))
            .andExpect(status().isBadRequest());

        List<Expense> expenseList = expenseRepository.findAll();
        assertThat(expenseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkExpenseNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = expenseRepository.findAll().size();
        // set the field null
        expense.setExpenseName(null);

        // Create the Expense, which fails.

        restExpenseMockMvc.perform(post("/api/expenses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(expense)))
            .andExpect(status().isBadRequest());

        List<Expense> expenseList = expenseRepository.findAll();
        assertThat(expenseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkExpenseLimitIsRequired() throws Exception {
        int databaseSizeBeforeTest = expenseRepository.findAll().size();
        // set the field null
        expense.setExpenseLimit(null);

        // Create the Expense, which fails.

        restExpenseMockMvc.perform(post("/api/expenses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(expense)))
            .andExpect(status().isBadRequest());

        List<Expense> expenseList = expenseRepository.findAll();
        assertThat(expenseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllExpenses() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList
        restExpenseMockMvc.perform(get("/api/expenses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(expense.getId().intValue())))
            .andExpect(jsonPath("$.[*].expenseCode").value(hasItem(DEFAULT_EXPENSE_CODE)))
            .andExpect(jsonPath("$.[*].expenseName").value(hasItem(DEFAULT_EXPENSE_NAME)))
            .andExpect(jsonPath("$.[*].expenseLimit").value(hasItem(DEFAULT_EXPENSE_LIMIT.intValue())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getExpense() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get the expense
        restExpenseMockMvc.perform(get("/api/expenses/{id}", expense.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(expense.getId().intValue()))
            .andExpect(jsonPath("$.expenseCode").value(DEFAULT_EXPENSE_CODE))
            .andExpect(jsonPath("$.expenseName").value(DEFAULT_EXPENSE_NAME))
            .andExpect(jsonPath("$.expenseLimit").value(DEFAULT_EXPENSE_LIMIT.intValue()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }


    @Test
    @Transactional
    public void getExpensesByIdFiltering() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        Long id = expense.getId();

        defaultExpenseShouldBeFound("id.equals=" + id);
        defaultExpenseShouldNotBeFound("id.notEquals=" + id);

        defaultExpenseShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultExpenseShouldNotBeFound("id.greaterThan=" + id);

        defaultExpenseShouldBeFound("id.lessThanOrEqual=" + id);
        defaultExpenseShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllExpensesByExpenseCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where expenseCode equals to DEFAULT_EXPENSE_CODE
        defaultExpenseShouldBeFound("expenseCode.equals=" + DEFAULT_EXPENSE_CODE);

        // Get all the expenseList where expenseCode equals to UPDATED_EXPENSE_CODE
        defaultExpenseShouldNotBeFound("expenseCode.equals=" + UPDATED_EXPENSE_CODE);
    }

    @Test
    @Transactional
    public void getAllExpensesByExpenseCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where expenseCode not equals to DEFAULT_EXPENSE_CODE
        defaultExpenseShouldNotBeFound("expenseCode.notEquals=" + DEFAULT_EXPENSE_CODE);

        // Get all the expenseList where expenseCode not equals to UPDATED_EXPENSE_CODE
        defaultExpenseShouldBeFound("expenseCode.notEquals=" + UPDATED_EXPENSE_CODE);
    }

    @Test
    @Transactional
    public void getAllExpensesByExpenseCodeIsInShouldWork() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where expenseCode in DEFAULT_EXPENSE_CODE or UPDATED_EXPENSE_CODE
        defaultExpenseShouldBeFound("expenseCode.in=" + DEFAULT_EXPENSE_CODE + "," + UPDATED_EXPENSE_CODE);

        // Get all the expenseList where expenseCode equals to UPDATED_EXPENSE_CODE
        defaultExpenseShouldNotBeFound("expenseCode.in=" + UPDATED_EXPENSE_CODE);
    }

    @Test
    @Transactional
    public void getAllExpensesByExpenseCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where expenseCode is not null
        defaultExpenseShouldBeFound("expenseCode.specified=true");

        // Get all the expenseList where expenseCode is null
        defaultExpenseShouldNotBeFound("expenseCode.specified=false");
    }
                @Test
    @Transactional
    public void getAllExpensesByExpenseCodeContainsSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where expenseCode contains DEFAULT_EXPENSE_CODE
        defaultExpenseShouldBeFound("expenseCode.contains=" + DEFAULT_EXPENSE_CODE);

        // Get all the expenseList where expenseCode contains UPDATED_EXPENSE_CODE
        defaultExpenseShouldNotBeFound("expenseCode.contains=" + UPDATED_EXPENSE_CODE);
    }

    @Test
    @Transactional
    public void getAllExpensesByExpenseCodeNotContainsSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where expenseCode does not contain DEFAULT_EXPENSE_CODE
        defaultExpenseShouldNotBeFound("expenseCode.doesNotContain=" + DEFAULT_EXPENSE_CODE);

        // Get all the expenseList where expenseCode does not contain UPDATED_EXPENSE_CODE
        defaultExpenseShouldBeFound("expenseCode.doesNotContain=" + UPDATED_EXPENSE_CODE);
    }


    @Test
    @Transactional
    public void getAllExpensesByExpenseNameIsEqualToSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where expenseName equals to DEFAULT_EXPENSE_NAME
        defaultExpenseShouldBeFound("expenseName.equals=" + DEFAULT_EXPENSE_NAME);

        // Get all the expenseList where expenseName equals to UPDATED_EXPENSE_NAME
        defaultExpenseShouldNotBeFound("expenseName.equals=" + UPDATED_EXPENSE_NAME);
    }

    @Test
    @Transactional
    public void getAllExpensesByExpenseNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where expenseName not equals to DEFAULT_EXPENSE_NAME
        defaultExpenseShouldNotBeFound("expenseName.notEquals=" + DEFAULT_EXPENSE_NAME);

        // Get all the expenseList where expenseName not equals to UPDATED_EXPENSE_NAME
        defaultExpenseShouldBeFound("expenseName.notEquals=" + UPDATED_EXPENSE_NAME);
    }

    @Test
    @Transactional
    public void getAllExpensesByExpenseNameIsInShouldWork() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where expenseName in DEFAULT_EXPENSE_NAME or UPDATED_EXPENSE_NAME
        defaultExpenseShouldBeFound("expenseName.in=" + DEFAULT_EXPENSE_NAME + "," + UPDATED_EXPENSE_NAME);

        // Get all the expenseList where expenseName equals to UPDATED_EXPENSE_NAME
        defaultExpenseShouldNotBeFound("expenseName.in=" + UPDATED_EXPENSE_NAME);
    }

    @Test
    @Transactional
    public void getAllExpensesByExpenseNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where expenseName is not null
        defaultExpenseShouldBeFound("expenseName.specified=true");

        // Get all the expenseList where expenseName is null
        defaultExpenseShouldNotBeFound("expenseName.specified=false");
    }
                @Test
    @Transactional
    public void getAllExpensesByExpenseNameContainsSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where expenseName contains DEFAULT_EXPENSE_NAME
        defaultExpenseShouldBeFound("expenseName.contains=" + DEFAULT_EXPENSE_NAME);

        // Get all the expenseList where expenseName contains UPDATED_EXPENSE_NAME
        defaultExpenseShouldNotBeFound("expenseName.contains=" + UPDATED_EXPENSE_NAME);
    }

    @Test
    @Transactional
    public void getAllExpensesByExpenseNameNotContainsSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where expenseName does not contain DEFAULT_EXPENSE_NAME
        defaultExpenseShouldNotBeFound("expenseName.doesNotContain=" + DEFAULT_EXPENSE_NAME);

        // Get all the expenseList where expenseName does not contain UPDATED_EXPENSE_NAME
        defaultExpenseShouldBeFound("expenseName.doesNotContain=" + UPDATED_EXPENSE_NAME);
    }


    @Test
    @Transactional
    public void getAllExpensesByExpenseLimitIsEqualToSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where expenseLimit equals to DEFAULT_EXPENSE_LIMIT
        defaultExpenseShouldBeFound("expenseLimit.equals=" + DEFAULT_EXPENSE_LIMIT);

        // Get all the expenseList where expenseLimit equals to UPDATED_EXPENSE_LIMIT
        defaultExpenseShouldNotBeFound("expenseLimit.equals=" + UPDATED_EXPENSE_LIMIT);
    }

    @Test
    @Transactional
    public void getAllExpensesByExpenseLimitIsNotEqualToSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where expenseLimit not equals to DEFAULT_EXPENSE_LIMIT
        defaultExpenseShouldNotBeFound("expenseLimit.notEquals=" + DEFAULT_EXPENSE_LIMIT);

        // Get all the expenseList where expenseLimit not equals to UPDATED_EXPENSE_LIMIT
        defaultExpenseShouldBeFound("expenseLimit.notEquals=" + UPDATED_EXPENSE_LIMIT);
    }

    @Test
    @Transactional
    public void getAllExpensesByExpenseLimitIsInShouldWork() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where expenseLimit in DEFAULT_EXPENSE_LIMIT or UPDATED_EXPENSE_LIMIT
        defaultExpenseShouldBeFound("expenseLimit.in=" + DEFAULT_EXPENSE_LIMIT + "," + UPDATED_EXPENSE_LIMIT);

        // Get all the expenseList where expenseLimit equals to UPDATED_EXPENSE_LIMIT
        defaultExpenseShouldNotBeFound("expenseLimit.in=" + UPDATED_EXPENSE_LIMIT);
    }

    @Test
    @Transactional
    public void getAllExpensesByExpenseLimitIsNullOrNotNull() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where expenseLimit is not null
        defaultExpenseShouldBeFound("expenseLimit.specified=true");

        // Get all the expenseList where expenseLimit is null
        defaultExpenseShouldNotBeFound("expenseLimit.specified=false");
    }

    @Test
    @Transactional
    public void getAllExpensesByExpenseLimitIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where expenseLimit is greater than or equal to DEFAULT_EXPENSE_LIMIT
        defaultExpenseShouldBeFound("expenseLimit.greaterThanOrEqual=" + DEFAULT_EXPENSE_LIMIT);

        // Get all the expenseList where expenseLimit is greater than or equal to UPDATED_EXPENSE_LIMIT
        defaultExpenseShouldNotBeFound("expenseLimit.greaterThanOrEqual=" + UPDATED_EXPENSE_LIMIT);
    }

    @Test
    @Transactional
    public void getAllExpensesByExpenseLimitIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where expenseLimit is less than or equal to DEFAULT_EXPENSE_LIMIT
        defaultExpenseShouldBeFound("expenseLimit.lessThanOrEqual=" + DEFAULT_EXPENSE_LIMIT);

        // Get all the expenseList where expenseLimit is less than or equal to SMALLER_EXPENSE_LIMIT
        defaultExpenseShouldNotBeFound("expenseLimit.lessThanOrEqual=" + SMALLER_EXPENSE_LIMIT);
    }

    @Test
    @Transactional
    public void getAllExpensesByExpenseLimitIsLessThanSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where expenseLimit is less than DEFAULT_EXPENSE_LIMIT
        defaultExpenseShouldNotBeFound("expenseLimit.lessThan=" + DEFAULT_EXPENSE_LIMIT);

        // Get all the expenseList where expenseLimit is less than UPDATED_EXPENSE_LIMIT
        defaultExpenseShouldBeFound("expenseLimit.lessThan=" + UPDATED_EXPENSE_LIMIT);
    }

    @Test
    @Transactional
    public void getAllExpensesByExpenseLimitIsGreaterThanSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where expenseLimit is greater than DEFAULT_EXPENSE_LIMIT
        defaultExpenseShouldNotBeFound("expenseLimit.greaterThan=" + DEFAULT_EXPENSE_LIMIT);

        // Get all the expenseList where expenseLimit is greater than SMALLER_EXPENSE_LIMIT
        defaultExpenseShouldBeFound("expenseLimit.greaterThan=" + SMALLER_EXPENSE_LIMIT);
    }


    @Test
    @Transactional
    public void getAllExpensesByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where isActive equals to DEFAULT_IS_ACTIVE
        defaultExpenseShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the expenseList where isActive equals to UPDATED_IS_ACTIVE
        defaultExpenseShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllExpensesByIsActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where isActive not equals to DEFAULT_IS_ACTIVE
        defaultExpenseShouldNotBeFound("isActive.notEquals=" + DEFAULT_IS_ACTIVE);

        // Get all the expenseList where isActive not equals to UPDATED_IS_ACTIVE
        defaultExpenseShouldBeFound("isActive.notEquals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllExpensesByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultExpenseShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the expenseList where isActive equals to UPDATED_IS_ACTIVE
        defaultExpenseShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllExpensesByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where isActive is not null
        defaultExpenseShouldBeFound("isActive.specified=true");

        // Get all the expenseList where isActive is null
        defaultExpenseShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    public void getAllExpensesByLocationIsEqualToSomething() throws Exception {
        // Get already existing entity
        Location location = expense.getLocation();
        expenseRepository.saveAndFlush(expense);
        Long locationId = location.getId();

        // Get all the expenseList where location equals to locationId
        defaultExpenseShouldBeFound("locationId.equals=" + locationId);

        // Get all the expenseList where location equals to locationId + 1
        defaultExpenseShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultExpenseShouldBeFound(String filter) throws Exception {
        restExpenseMockMvc.perform(get("/api/expenses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(expense.getId().intValue())))
            .andExpect(jsonPath("$.[*].expenseCode").value(hasItem(DEFAULT_EXPENSE_CODE)))
            .andExpect(jsonPath("$.[*].expenseName").value(hasItem(DEFAULT_EXPENSE_NAME)))
            .andExpect(jsonPath("$.[*].expenseLimit").value(hasItem(DEFAULT_EXPENSE_LIMIT.intValue())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restExpenseMockMvc.perform(get("/api/expenses/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultExpenseShouldNotBeFound(String filter) throws Exception {
        restExpenseMockMvc.perform(get("/api/expenses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restExpenseMockMvc.perform(get("/api/expenses/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingExpense() throws Exception {
        // Get the expense
        restExpenseMockMvc.perform(get("/api/expenses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExpense() throws Exception {
        // Initialize the database
        expenseService.save(expense);

        int databaseSizeBeforeUpdate = expenseRepository.findAll().size();

        // Update the expense
        Expense updatedExpense = expenseRepository.findById(expense.getId()).get();
        // Disconnect from session so that the updates on updatedExpense are not directly saved in db
        em.detach(updatedExpense);
        updatedExpense
            .expenseCode(UPDATED_EXPENSE_CODE)
            .expenseName(UPDATED_EXPENSE_NAME)
            .expenseLimit(UPDATED_EXPENSE_LIMIT)
            .isActive(UPDATED_IS_ACTIVE);

        restExpenseMockMvc.perform(put("/api/expenses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedExpense)))
            .andExpect(status().isOk());

        // Validate the Expense in the database
        List<Expense> expenseList = expenseRepository.findAll();
        assertThat(expenseList).hasSize(databaseSizeBeforeUpdate);
        Expense testExpense = expenseList.get(expenseList.size() - 1);
        assertThat(testExpense.getExpenseCode()).isEqualTo(UPDATED_EXPENSE_CODE);
        assertThat(testExpense.getExpenseName()).isEqualTo(UPDATED_EXPENSE_NAME);
        assertThat(testExpense.getExpenseLimit()).isEqualTo(UPDATED_EXPENSE_LIMIT);
        assertThat(testExpense.isIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void updateNonExistingExpense() throws Exception {
        int databaseSizeBeforeUpdate = expenseRepository.findAll().size();

        // Create the Expense

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExpenseMockMvc.perform(put("/api/expenses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(expense)))
            .andExpect(status().isBadRequest());

        // Validate the Expense in the database
        List<Expense> expenseList = expenseRepository.findAll();
        assertThat(expenseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteExpense() throws Exception {
        // Initialize the database
        expenseService.save(expense);

        int databaseSizeBeforeDelete = expenseRepository.findAll().size();

        // Delete the expense
        restExpenseMockMvc.perform(delete("/api/expenses/{id}", expense.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Expense> expenseList = expenseRepository.findAll();
        assertThat(expenseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

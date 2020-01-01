package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.WikunumApp;
import com.alphadevs.sales.domain.EmployeeAccountBalance;
import com.alphadevs.sales.domain.Location;
import com.alphadevs.sales.domain.TransactionType;
import com.alphadevs.sales.domain.Employee;
import com.alphadevs.sales.repository.EmployeeAccountBalanceRepository;
import com.alphadevs.sales.service.EmployeeAccountBalanceService;
import com.alphadevs.sales.web.rest.errors.ExceptionTranslator;
import com.alphadevs.sales.service.dto.EmployeeAccountBalanceCriteria;
import com.alphadevs.sales.service.EmployeeAccountBalanceQueryService;

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
 * Integration tests for the {@link EmployeeAccountBalanceResource} REST controller.
 */
@SpringBootTest(classes = WikunumApp.class)
public class EmployeeAccountBalanceResourceIT {

    private static final BigDecimal DEFAULT_BALANCE = new BigDecimal(1);
    private static final BigDecimal UPDATED_BALANCE = new BigDecimal(2);
    private static final BigDecimal SMALLER_BALANCE = new BigDecimal(1 - 1);

    @Autowired
    private EmployeeAccountBalanceRepository employeeAccountBalanceRepository;

    @Autowired
    private EmployeeAccountBalanceService employeeAccountBalanceService;

    @Autowired
    private EmployeeAccountBalanceQueryService employeeAccountBalanceQueryService;

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

    private MockMvc restEmployeeAccountBalanceMockMvc;

    private EmployeeAccountBalance employeeAccountBalance;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EmployeeAccountBalanceResource employeeAccountBalanceResource = new EmployeeAccountBalanceResource(employeeAccountBalanceService, employeeAccountBalanceQueryService);
        this.restEmployeeAccountBalanceMockMvc = MockMvcBuilders.standaloneSetup(employeeAccountBalanceResource)
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
    public static EmployeeAccountBalance createEntity(EntityManager em) {
        EmployeeAccountBalance employeeAccountBalance = new EmployeeAccountBalance()
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
        employeeAccountBalance.setLocation(location);
        // Add required entity
        TransactionType transactionType;
        if (TestUtil.findAll(em, TransactionType.class).isEmpty()) {
            transactionType = TransactionTypeResourceIT.createEntity(em);
            em.persist(transactionType);
            em.flush();
        } else {
            transactionType = TestUtil.findAll(em, TransactionType.class).get(0);
        }
        employeeAccountBalance.setTransactionType(transactionType);
        // Add required entity
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        employeeAccountBalance.setEmployee(employee);
        return employeeAccountBalance;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmployeeAccountBalance createUpdatedEntity(EntityManager em) {
        EmployeeAccountBalance employeeAccountBalance = new EmployeeAccountBalance()
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
        employeeAccountBalance.setLocation(location);
        // Add required entity
        TransactionType transactionType;
        if (TestUtil.findAll(em, TransactionType.class).isEmpty()) {
            transactionType = TransactionTypeResourceIT.createUpdatedEntity(em);
            em.persist(transactionType);
            em.flush();
        } else {
            transactionType = TestUtil.findAll(em, TransactionType.class).get(0);
        }
        employeeAccountBalance.setTransactionType(transactionType);
        // Add required entity
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createUpdatedEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        employeeAccountBalance.setEmployee(employee);
        return employeeAccountBalance;
    }

    @BeforeEach
    public void initTest() {
        employeeAccountBalance = createEntity(em);
    }

    @Test
    @Transactional
    public void createEmployeeAccountBalance() throws Exception {
        int databaseSizeBeforeCreate = employeeAccountBalanceRepository.findAll().size();

        // Create the EmployeeAccountBalance
        restEmployeeAccountBalanceMockMvc.perform(post("/api/employee-account-balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employeeAccountBalance)))
            .andExpect(status().isCreated());

        // Validate the EmployeeAccountBalance in the database
        List<EmployeeAccountBalance> employeeAccountBalanceList = employeeAccountBalanceRepository.findAll();
        assertThat(employeeAccountBalanceList).hasSize(databaseSizeBeforeCreate + 1);
        EmployeeAccountBalance testEmployeeAccountBalance = employeeAccountBalanceList.get(employeeAccountBalanceList.size() - 1);
        assertThat(testEmployeeAccountBalance.getBalance()).isEqualTo(DEFAULT_BALANCE);
    }

    @Test
    @Transactional
    public void createEmployeeAccountBalanceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = employeeAccountBalanceRepository.findAll().size();

        // Create the EmployeeAccountBalance with an existing ID
        employeeAccountBalance.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmployeeAccountBalanceMockMvc.perform(post("/api/employee-account-balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employeeAccountBalance)))
            .andExpect(status().isBadRequest());

        // Validate the EmployeeAccountBalance in the database
        List<EmployeeAccountBalance> employeeAccountBalanceList = employeeAccountBalanceRepository.findAll();
        assertThat(employeeAccountBalanceList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkBalanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeAccountBalanceRepository.findAll().size();
        // set the field null
        employeeAccountBalance.setBalance(null);

        // Create the EmployeeAccountBalance, which fails.

        restEmployeeAccountBalanceMockMvc.perform(post("/api/employee-account-balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employeeAccountBalance)))
            .andExpect(status().isBadRequest());

        List<EmployeeAccountBalance> employeeAccountBalanceList = employeeAccountBalanceRepository.findAll();
        assertThat(employeeAccountBalanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEmployeeAccountBalances() throws Exception {
        // Initialize the database
        employeeAccountBalanceRepository.saveAndFlush(employeeAccountBalance);

        // Get all the employeeAccountBalanceList
        restEmployeeAccountBalanceMockMvc.perform(get("/api/employee-account-balances?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employeeAccountBalance.getId().intValue())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.intValue())));
    }
    
    @Test
    @Transactional
    public void getEmployeeAccountBalance() throws Exception {
        // Initialize the database
        employeeAccountBalanceRepository.saveAndFlush(employeeAccountBalance);

        // Get the employeeAccountBalance
        restEmployeeAccountBalanceMockMvc.perform(get("/api/employee-account-balances/{id}", employeeAccountBalance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(employeeAccountBalance.getId().intValue()))
            .andExpect(jsonPath("$.balance").value(DEFAULT_BALANCE.intValue()));
    }


    @Test
    @Transactional
    public void getEmployeeAccountBalancesByIdFiltering() throws Exception {
        // Initialize the database
        employeeAccountBalanceRepository.saveAndFlush(employeeAccountBalance);

        Long id = employeeAccountBalance.getId();

        defaultEmployeeAccountBalanceShouldBeFound("id.equals=" + id);
        defaultEmployeeAccountBalanceShouldNotBeFound("id.notEquals=" + id);

        defaultEmployeeAccountBalanceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEmployeeAccountBalanceShouldNotBeFound("id.greaterThan=" + id);

        defaultEmployeeAccountBalanceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEmployeeAccountBalanceShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllEmployeeAccountBalancesByBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeAccountBalanceRepository.saveAndFlush(employeeAccountBalance);

        // Get all the employeeAccountBalanceList where balance equals to DEFAULT_BALANCE
        defaultEmployeeAccountBalanceShouldBeFound("balance.equals=" + DEFAULT_BALANCE);

        // Get all the employeeAccountBalanceList where balance equals to UPDATED_BALANCE
        defaultEmployeeAccountBalanceShouldNotBeFound("balance.equals=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllEmployeeAccountBalancesByBalanceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeAccountBalanceRepository.saveAndFlush(employeeAccountBalance);

        // Get all the employeeAccountBalanceList where balance not equals to DEFAULT_BALANCE
        defaultEmployeeAccountBalanceShouldNotBeFound("balance.notEquals=" + DEFAULT_BALANCE);

        // Get all the employeeAccountBalanceList where balance not equals to UPDATED_BALANCE
        defaultEmployeeAccountBalanceShouldBeFound("balance.notEquals=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllEmployeeAccountBalancesByBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        employeeAccountBalanceRepository.saveAndFlush(employeeAccountBalance);

        // Get all the employeeAccountBalanceList where balance in DEFAULT_BALANCE or UPDATED_BALANCE
        defaultEmployeeAccountBalanceShouldBeFound("balance.in=" + DEFAULT_BALANCE + "," + UPDATED_BALANCE);

        // Get all the employeeAccountBalanceList where balance equals to UPDATED_BALANCE
        defaultEmployeeAccountBalanceShouldNotBeFound("balance.in=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllEmployeeAccountBalancesByBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeAccountBalanceRepository.saveAndFlush(employeeAccountBalance);

        // Get all the employeeAccountBalanceList where balance is not null
        defaultEmployeeAccountBalanceShouldBeFound("balance.specified=true");

        // Get all the employeeAccountBalanceList where balance is null
        defaultEmployeeAccountBalanceShouldNotBeFound("balance.specified=false");
    }

    @Test
    @Transactional
    public void getAllEmployeeAccountBalancesByBalanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeAccountBalanceRepository.saveAndFlush(employeeAccountBalance);

        // Get all the employeeAccountBalanceList where balance is greater than or equal to DEFAULT_BALANCE
        defaultEmployeeAccountBalanceShouldBeFound("balance.greaterThanOrEqual=" + DEFAULT_BALANCE);

        // Get all the employeeAccountBalanceList where balance is greater than or equal to UPDATED_BALANCE
        defaultEmployeeAccountBalanceShouldNotBeFound("balance.greaterThanOrEqual=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllEmployeeAccountBalancesByBalanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeAccountBalanceRepository.saveAndFlush(employeeAccountBalance);

        // Get all the employeeAccountBalanceList where balance is less than or equal to DEFAULT_BALANCE
        defaultEmployeeAccountBalanceShouldBeFound("balance.lessThanOrEqual=" + DEFAULT_BALANCE);

        // Get all the employeeAccountBalanceList where balance is less than or equal to SMALLER_BALANCE
        defaultEmployeeAccountBalanceShouldNotBeFound("balance.lessThanOrEqual=" + SMALLER_BALANCE);
    }

    @Test
    @Transactional
    public void getAllEmployeeAccountBalancesByBalanceIsLessThanSomething() throws Exception {
        // Initialize the database
        employeeAccountBalanceRepository.saveAndFlush(employeeAccountBalance);

        // Get all the employeeAccountBalanceList where balance is less than DEFAULT_BALANCE
        defaultEmployeeAccountBalanceShouldNotBeFound("balance.lessThan=" + DEFAULT_BALANCE);

        // Get all the employeeAccountBalanceList where balance is less than UPDATED_BALANCE
        defaultEmployeeAccountBalanceShouldBeFound("balance.lessThan=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllEmployeeAccountBalancesByBalanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        employeeAccountBalanceRepository.saveAndFlush(employeeAccountBalance);

        // Get all the employeeAccountBalanceList where balance is greater than DEFAULT_BALANCE
        defaultEmployeeAccountBalanceShouldNotBeFound("balance.greaterThan=" + DEFAULT_BALANCE);

        // Get all the employeeAccountBalanceList where balance is greater than SMALLER_BALANCE
        defaultEmployeeAccountBalanceShouldBeFound("balance.greaterThan=" + SMALLER_BALANCE);
    }


    @Test
    @Transactional
    public void getAllEmployeeAccountBalancesByLocationIsEqualToSomething() throws Exception {
        // Get already existing entity
        Location location = employeeAccountBalance.getLocation();
        employeeAccountBalanceRepository.saveAndFlush(employeeAccountBalance);
        Long locationId = location.getId();

        // Get all the employeeAccountBalanceList where location equals to locationId
        defaultEmployeeAccountBalanceShouldBeFound("locationId.equals=" + locationId);

        // Get all the employeeAccountBalanceList where location equals to locationId + 1
        defaultEmployeeAccountBalanceShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }


    @Test
    @Transactional
    public void getAllEmployeeAccountBalancesByTransactionTypeIsEqualToSomething() throws Exception {
        // Get already existing entity
        TransactionType transactionType = employeeAccountBalance.getTransactionType();
        employeeAccountBalanceRepository.saveAndFlush(employeeAccountBalance);
        Long transactionTypeId = transactionType.getId();

        // Get all the employeeAccountBalanceList where transactionType equals to transactionTypeId
        defaultEmployeeAccountBalanceShouldBeFound("transactionTypeId.equals=" + transactionTypeId);

        // Get all the employeeAccountBalanceList where transactionType equals to transactionTypeId + 1
        defaultEmployeeAccountBalanceShouldNotBeFound("transactionTypeId.equals=" + (transactionTypeId + 1));
    }


    @Test
    @Transactional
    public void getAllEmployeeAccountBalancesByEmployeeIsEqualToSomething() throws Exception {
        // Get already existing entity
        Employee employee = employeeAccountBalance.getEmployee();
        employeeAccountBalanceRepository.saveAndFlush(employeeAccountBalance);
        Long employeeId = employee.getId();

        // Get all the employeeAccountBalanceList where employee equals to employeeId
        defaultEmployeeAccountBalanceShouldBeFound("employeeId.equals=" + employeeId);

        // Get all the employeeAccountBalanceList where employee equals to employeeId + 1
        defaultEmployeeAccountBalanceShouldNotBeFound("employeeId.equals=" + (employeeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEmployeeAccountBalanceShouldBeFound(String filter) throws Exception {
        restEmployeeAccountBalanceMockMvc.perform(get("/api/employee-account-balances?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employeeAccountBalance.getId().intValue())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.intValue())));

        // Check, that the count call also returns 1
        restEmployeeAccountBalanceMockMvc.perform(get("/api/employee-account-balances/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEmployeeAccountBalanceShouldNotBeFound(String filter) throws Exception {
        restEmployeeAccountBalanceMockMvc.perform(get("/api/employee-account-balances?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEmployeeAccountBalanceMockMvc.perform(get("/api/employee-account-balances/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingEmployeeAccountBalance() throws Exception {
        // Get the employeeAccountBalance
        restEmployeeAccountBalanceMockMvc.perform(get("/api/employee-account-balances/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEmployeeAccountBalance() throws Exception {
        // Initialize the database
        employeeAccountBalanceService.save(employeeAccountBalance);

        int databaseSizeBeforeUpdate = employeeAccountBalanceRepository.findAll().size();

        // Update the employeeAccountBalance
        EmployeeAccountBalance updatedEmployeeAccountBalance = employeeAccountBalanceRepository.findById(employeeAccountBalance.getId()).get();
        // Disconnect from session so that the updates on updatedEmployeeAccountBalance are not directly saved in db
        em.detach(updatedEmployeeAccountBalance);
        updatedEmployeeAccountBalance
            .balance(UPDATED_BALANCE);

        restEmployeeAccountBalanceMockMvc.perform(put("/api/employee-account-balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEmployeeAccountBalance)))
            .andExpect(status().isOk());

        // Validate the EmployeeAccountBalance in the database
        List<EmployeeAccountBalance> employeeAccountBalanceList = employeeAccountBalanceRepository.findAll();
        assertThat(employeeAccountBalanceList).hasSize(databaseSizeBeforeUpdate);
        EmployeeAccountBalance testEmployeeAccountBalance = employeeAccountBalanceList.get(employeeAccountBalanceList.size() - 1);
        assertThat(testEmployeeAccountBalance.getBalance()).isEqualTo(UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void updateNonExistingEmployeeAccountBalance() throws Exception {
        int databaseSizeBeforeUpdate = employeeAccountBalanceRepository.findAll().size();

        // Create the EmployeeAccountBalance

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeAccountBalanceMockMvc.perform(put("/api/employee-account-balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employeeAccountBalance)))
            .andExpect(status().isBadRequest());

        // Validate the EmployeeAccountBalance in the database
        List<EmployeeAccountBalance> employeeAccountBalanceList = employeeAccountBalanceRepository.findAll();
        assertThat(employeeAccountBalanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEmployeeAccountBalance() throws Exception {
        // Initialize the database
        employeeAccountBalanceService.save(employeeAccountBalance);

        int databaseSizeBeforeDelete = employeeAccountBalanceRepository.findAll().size();

        // Delete the employeeAccountBalance
        restEmployeeAccountBalanceMockMvc.perform(delete("/api/employee-account-balances/{id}", employeeAccountBalance.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EmployeeAccountBalance> employeeAccountBalanceList = employeeAccountBalanceRepository.findAll();
        assertThat(employeeAccountBalanceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.WikunumApp;
import com.alphadevs.sales.domain.CashBookBalance;
import com.alphadevs.sales.domain.Location;
import com.alphadevs.sales.domain.TransactionType;
import com.alphadevs.sales.repository.CashBookBalanceRepository;
import com.alphadevs.sales.service.CashBookBalanceService;
import com.alphadevs.sales.web.rest.errors.ExceptionTranslator;
import com.alphadevs.sales.service.dto.CashBookBalanceCriteria;
import com.alphadevs.sales.service.CashBookBalanceQueryService;

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
 * Integration tests for the {@link CashBookBalanceResource} REST controller.
 */
@SpringBootTest(classes = WikunumApp.class)
public class CashBookBalanceResourceIT {

    private static final BigDecimal DEFAULT_BALANCE = new BigDecimal(1);
    private static final BigDecimal UPDATED_BALANCE = new BigDecimal(2);
    private static final BigDecimal SMALLER_BALANCE = new BigDecimal(1 - 1);

    @Autowired
    private CashBookBalanceRepository cashBookBalanceRepository;

    @Autowired
    private CashBookBalanceService cashBookBalanceService;

    @Autowired
    private CashBookBalanceQueryService cashBookBalanceQueryService;

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

    private MockMvc restCashBookBalanceMockMvc;

    private CashBookBalance cashBookBalance;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CashBookBalanceResource cashBookBalanceResource = new CashBookBalanceResource(cashBookBalanceService, cashBookBalanceQueryService);
        this.restCashBookBalanceMockMvc = MockMvcBuilders.standaloneSetup(cashBookBalanceResource)
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
    public static CashBookBalance createEntity(EntityManager em) {
        CashBookBalance cashBookBalance = new CashBookBalance()
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
        cashBookBalance.setLocation(location);
        // Add required entity
        TransactionType transactionType;
        if (TestUtil.findAll(em, TransactionType.class).isEmpty()) {
            transactionType = TransactionTypeResourceIT.createEntity(em);
            em.persist(transactionType);
            em.flush();
        } else {
            transactionType = TestUtil.findAll(em, TransactionType.class).get(0);
        }
        cashBookBalance.setTransactionType(transactionType);
        return cashBookBalance;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CashBookBalance createUpdatedEntity(EntityManager em) {
        CashBookBalance cashBookBalance = new CashBookBalance()
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
        cashBookBalance.setLocation(location);
        // Add required entity
        TransactionType transactionType;
        if (TestUtil.findAll(em, TransactionType.class).isEmpty()) {
            transactionType = TransactionTypeResourceIT.createUpdatedEntity(em);
            em.persist(transactionType);
            em.flush();
        } else {
            transactionType = TestUtil.findAll(em, TransactionType.class).get(0);
        }
        cashBookBalance.setTransactionType(transactionType);
        return cashBookBalance;
    }

    @BeforeEach
    public void initTest() {
        cashBookBalance = createEntity(em);
    }

    @Test
    @Transactional
    public void createCashBookBalance() throws Exception {
        int databaseSizeBeforeCreate = cashBookBalanceRepository.findAll().size();

        // Create the CashBookBalance
        restCashBookBalanceMockMvc.perform(post("/api/cash-book-balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashBookBalance)))
            .andExpect(status().isCreated());

        // Validate the CashBookBalance in the database
        List<CashBookBalance> cashBookBalanceList = cashBookBalanceRepository.findAll();
        assertThat(cashBookBalanceList).hasSize(databaseSizeBeforeCreate + 1);
        CashBookBalance testCashBookBalance = cashBookBalanceList.get(cashBookBalanceList.size() - 1);
        assertThat(testCashBookBalance.getBalance()).isEqualTo(DEFAULT_BALANCE);
    }

    @Test
    @Transactional
    public void createCashBookBalanceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cashBookBalanceRepository.findAll().size();

        // Create the CashBookBalance with an existing ID
        cashBookBalance.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCashBookBalanceMockMvc.perform(post("/api/cash-book-balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashBookBalance)))
            .andExpect(status().isBadRequest());

        // Validate the CashBookBalance in the database
        List<CashBookBalance> cashBookBalanceList = cashBookBalanceRepository.findAll();
        assertThat(cashBookBalanceList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkBalanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = cashBookBalanceRepository.findAll().size();
        // set the field null
        cashBookBalance.setBalance(null);

        // Create the CashBookBalance, which fails.

        restCashBookBalanceMockMvc.perform(post("/api/cash-book-balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashBookBalance)))
            .andExpect(status().isBadRequest());

        List<CashBookBalance> cashBookBalanceList = cashBookBalanceRepository.findAll();
        assertThat(cashBookBalanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCashBookBalances() throws Exception {
        // Initialize the database
        cashBookBalanceRepository.saveAndFlush(cashBookBalance);

        // Get all the cashBookBalanceList
        restCashBookBalanceMockMvc.perform(get("/api/cash-book-balances?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cashBookBalance.getId().intValue())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.intValue())));
    }
    
    @Test
    @Transactional
    public void getCashBookBalance() throws Exception {
        // Initialize the database
        cashBookBalanceRepository.saveAndFlush(cashBookBalance);

        // Get the cashBookBalance
        restCashBookBalanceMockMvc.perform(get("/api/cash-book-balances/{id}", cashBookBalance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cashBookBalance.getId().intValue()))
            .andExpect(jsonPath("$.balance").value(DEFAULT_BALANCE.intValue()));
    }


    @Test
    @Transactional
    public void getCashBookBalancesByIdFiltering() throws Exception {
        // Initialize the database
        cashBookBalanceRepository.saveAndFlush(cashBookBalance);

        Long id = cashBookBalance.getId();

        defaultCashBookBalanceShouldBeFound("id.equals=" + id);
        defaultCashBookBalanceShouldNotBeFound("id.notEquals=" + id);

        defaultCashBookBalanceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCashBookBalanceShouldNotBeFound("id.greaterThan=" + id);

        defaultCashBookBalanceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCashBookBalanceShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllCashBookBalancesByBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        cashBookBalanceRepository.saveAndFlush(cashBookBalance);

        // Get all the cashBookBalanceList where balance equals to DEFAULT_BALANCE
        defaultCashBookBalanceShouldBeFound("balance.equals=" + DEFAULT_BALANCE);

        // Get all the cashBookBalanceList where balance equals to UPDATED_BALANCE
        defaultCashBookBalanceShouldNotBeFound("balance.equals=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllCashBookBalancesByBalanceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cashBookBalanceRepository.saveAndFlush(cashBookBalance);

        // Get all the cashBookBalanceList where balance not equals to DEFAULT_BALANCE
        defaultCashBookBalanceShouldNotBeFound("balance.notEquals=" + DEFAULT_BALANCE);

        // Get all the cashBookBalanceList where balance not equals to UPDATED_BALANCE
        defaultCashBookBalanceShouldBeFound("balance.notEquals=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllCashBookBalancesByBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        cashBookBalanceRepository.saveAndFlush(cashBookBalance);

        // Get all the cashBookBalanceList where balance in DEFAULT_BALANCE or UPDATED_BALANCE
        defaultCashBookBalanceShouldBeFound("balance.in=" + DEFAULT_BALANCE + "," + UPDATED_BALANCE);

        // Get all the cashBookBalanceList where balance equals to UPDATED_BALANCE
        defaultCashBookBalanceShouldNotBeFound("balance.in=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllCashBookBalancesByBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        cashBookBalanceRepository.saveAndFlush(cashBookBalance);

        // Get all the cashBookBalanceList where balance is not null
        defaultCashBookBalanceShouldBeFound("balance.specified=true");

        // Get all the cashBookBalanceList where balance is null
        defaultCashBookBalanceShouldNotBeFound("balance.specified=false");
    }

    @Test
    @Transactional
    public void getAllCashBookBalancesByBalanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cashBookBalanceRepository.saveAndFlush(cashBookBalance);

        // Get all the cashBookBalanceList where balance is greater than or equal to DEFAULT_BALANCE
        defaultCashBookBalanceShouldBeFound("balance.greaterThanOrEqual=" + DEFAULT_BALANCE);

        // Get all the cashBookBalanceList where balance is greater than or equal to UPDATED_BALANCE
        defaultCashBookBalanceShouldNotBeFound("balance.greaterThanOrEqual=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllCashBookBalancesByBalanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cashBookBalanceRepository.saveAndFlush(cashBookBalance);

        // Get all the cashBookBalanceList where balance is less than or equal to DEFAULT_BALANCE
        defaultCashBookBalanceShouldBeFound("balance.lessThanOrEqual=" + DEFAULT_BALANCE);

        // Get all the cashBookBalanceList where balance is less than or equal to SMALLER_BALANCE
        defaultCashBookBalanceShouldNotBeFound("balance.lessThanOrEqual=" + SMALLER_BALANCE);
    }

    @Test
    @Transactional
    public void getAllCashBookBalancesByBalanceIsLessThanSomething() throws Exception {
        // Initialize the database
        cashBookBalanceRepository.saveAndFlush(cashBookBalance);

        // Get all the cashBookBalanceList where balance is less than DEFAULT_BALANCE
        defaultCashBookBalanceShouldNotBeFound("balance.lessThan=" + DEFAULT_BALANCE);

        // Get all the cashBookBalanceList where balance is less than UPDATED_BALANCE
        defaultCashBookBalanceShouldBeFound("balance.lessThan=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllCashBookBalancesByBalanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cashBookBalanceRepository.saveAndFlush(cashBookBalance);

        // Get all the cashBookBalanceList where balance is greater than DEFAULT_BALANCE
        defaultCashBookBalanceShouldNotBeFound("balance.greaterThan=" + DEFAULT_BALANCE);

        // Get all the cashBookBalanceList where balance is greater than SMALLER_BALANCE
        defaultCashBookBalanceShouldBeFound("balance.greaterThan=" + SMALLER_BALANCE);
    }


    @Test
    @Transactional
    public void getAllCashBookBalancesByLocationIsEqualToSomething() throws Exception {
        // Get already existing entity
        Location location = cashBookBalance.getLocation();
        cashBookBalanceRepository.saveAndFlush(cashBookBalance);
        Long locationId = location.getId();

        // Get all the cashBookBalanceList where location equals to locationId
        defaultCashBookBalanceShouldBeFound("locationId.equals=" + locationId);

        // Get all the cashBookBalanceList where location equals to locationId + 1
        defaultCashBookBalanceShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }


    @Test
    @Transactional
    public void getAllCashBookBalancesByTransactionTypeIsEqualToSomething() throws Exception {
        // Get already existing entity
        TransactionType transactionType = cashBookBalance.getTransactionType();
        cashBookBalanceRepository.saveAndFlush(cashBookBalance);
        Long transactionTypeId = transactionType.getId();

        // Get all the cashBookBalanceList where transactionType equals to transactionTypeId
        defaultCashBookBalanceShouldBeFound("transactionTypeId.equals=" + transactionTypeId);

        // Get all the cashBookBalanceList where transactionType equals to transactionTypeId + 1
        defaultCashBookBalanceShouldNotBeFound("transactionTypeId.equals=" + (transactionTypeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCashBookBalanceShouldBeFound(String filter) throws Exception {
        restCashBookBalanceMockMvc.perform(get("/api/cash-book-balances?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cashBookBalance.getId().intValue())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.intValue())));

        // Check, that the count call also returns 1
        restCashBookBalanceMockMvc.perform(get("/api/cash-book-balances/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCashBookBalanceShouldNotBeFound(String filter) throws Exception {
        restCashBookBalanceMockMvc.perform(get("/api/cash-book-balances?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCashBookBalanceMockMvc.perform(get("/api/cash-book-balances/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingCashBookBalance() throws Exception {
        // Get the cashBookBalance
        restCashBookBalanceMockMvc.perform(get("/api/cash-book-balances/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCashBookBalance() throws Exception {
        // Initialize the database
        cashBookBalanceService.save(cashBookBalance);

        int databaseSizeBeforeUpdate = cashBookBalanceRepository.findAll().size();

        // Update the cashBookBalance
        CashBookBalance updatedCashBookBalance = cashBookBalanceRepository.findById(cashBookBalance.getId()).get();
        // Disconnect from session so that the updates on updatedCashBookBalance are not directly saved in db
        em.detach(updatedCashBookBalance);
        updatedCashBookBalance
            .balance(UPDATED_BALANCE);

        restCashBookBalanceMockMvc.perform(put("/api/cash-book-balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCashBookBalance)))
            .andExpect(status().isOk());

        // Validate the CashBookBalance in the database
        List<CashBookBalance> cashBookBalanceList = cashBookBalanceRepository.findAll();
        assertThat(cashBookBalanceList).hasSize(databaseSizeBeforeUpdate);
        CashBookBalance testCashBookBalance = cashBookBalanceList.get(cashBookBalanceList.size() - 1);
        assertThat(testCashBookBalance.getBalance()).isEqualTo(UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void updateNonExistingCashBookBalance() throws Exception {
        int databaseSizeBeforeUpdate = cashBookBalanceRepository.findAll().size();

        // Create the CashBookBalance

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCashBookBalanceMockMvc.perform(put("/api/cash-book-balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashBookBalance)))
            .andExpect(status().isBadRequest());

        // Validate the CashBookBalance in the database
        List<CashBookBalance> cashBookBalanceList = cashBookBalanceRepository.findAll();
        assertThat(cashBookBalanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCashBookBalance() throws Exception {
        // Initialize the database
        cashBookBalanceService.save(cashBookBalance);

        int databaseSizeBeforeDelete = cashBookBalanceRepository.findAll().size();

        // Delete the cashBookBalance
        restCashBookBalanceMockMvc.perform(delete("/api/cash-book-balances/{id}", cashBookBalance.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CashBookBalance> cashBookBalanceList = cashBookBalanceRepository.findAll();
        assertThat(cashBookBalanceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

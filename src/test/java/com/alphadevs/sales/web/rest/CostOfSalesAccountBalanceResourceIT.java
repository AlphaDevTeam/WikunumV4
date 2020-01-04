package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.WikunumApp;
import com.alphadevs.sales.domain.CostOfSalesAccountBalance;
import com.alphadevs.sales.domain.Location;
import com.alphadevs.sales.domain.TransactionType;
import com.alphadevs.sales.repository.CostOfSalesAccountBalanceRepository;
import com.alphadevs.sales.service.CostOfSalesAccountBalanceService;
import com.alphadevs.sales.web.rest.errors.ExceptionTranslator;
import com.alphadevs.sales.service.dto.CostOfSalesAccountBalanceCriteria;
import com.alphadevs.sales.service.CostOfSalesAccountBalanceQueryService;

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
 * Integration tests for the {@link CostOfSalesAccountBalanceResource} REST controller.
 */
@SpringBootTest(classes = WikunumApp.class)
public class CostOfSalesAccountBalanceResourceIT {

    private static final BigDecimal DEFAULT_BALANCE = new BigDecimal(1);
    private static final BigDecimal UPDATED_BALANCE = new BigDecimal(2);
    private static final BigDecimal SMALLER_BALANCE = new BigDecimal(1 - 1);

    @Autowired
    private CostOfSalesAccountBalanceRepository costOfSalesAccountBalanceRepository;

    @Autowired
    private CostOfSalesAccountBalanceService costOfSalesAccountBalanceService;

    @Autowired
    private CostOfSalesAccountBalanceQueryService costOfSalesAccountBalanceQueryService;

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

    private MockMvc restCostOfSalesAccountBalanceMockMvc;

    private CostOfSalesAccountBalance costOfSalesAccountBalance;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CostOfSalesAccountBalanceResource costOfSalesAccountBalanceResource = new CostOfSalesAccountBalanceResource(costOfSalesAccountBalanceService, costOfSalesAccountBalanceQueryService);
        this.restCostOfSalesAccountBalanceMockMvc = MockMvcBuilders.standaloneSetup(costOfSalesAccountBalanceResource)
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
    public static CostOfSalesAccountBalance createEntity(EntityManager em) {
        CostOfSalesAccountBalance costOfSalesAccountBalance = new CostOfSalesAccountBalance()
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
        costOfSalesAccountBalance.setLocation(location);
        // Add required entity
        TransactionType transactionType;
        if (TestUtil.findAll(em, TransactionType.class).isEmpty()) {
            transactionType = TransactionTypeResourceIT.createEntity(em);
            em.persist(transactionType);
            em.flush();
        } else {
            transactionType = TestUtil.findAll(em, TransactionType.class).get(0);
        }
        costOfSalesAccountBalance.setTransactionType(transactionType);
        return costOfSalesAccountBalance;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CostOfSalesAccountBalance createUpdatedEntity(EntityManager em) {
        CostOfSalesAccountBalance costOfSalesAccountBalance = new CostOfSalesAccountBalance()
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
        costOfSalesAccountBalance.setLocation(location);
        // Add required entity
        TransactionType transactionType;
        if (TestUtil.findAll(em, TransactionType.class).isEmpty()) {
            transactionType = TransactionTypeResourceIT.createUpdatedEntity(em);
            em.persist(transactionType);
            em.flush();
        } else {
            transactionType = TestUtil.findAll(em, TransactionType.class).get(0);
        }
        costOfSalesAccountBalance.setTransactionType(transactionType);
        return costOfSalesAccountBalance;
    }

    @BeforeEach
    public void initTest() {
        costOfSalesAccountBalance = createEntity(em);
    }

    @Test
    @Transactional
    public void createCostOfSalesAccountBalance() throws Exception {
        int databaseSizeBeforeCreate = costOfSalesAccountBalanceRepository.findAll().size();

        // Create the CostOfSalesAccountBalance
        restCostOfSalesAccountBalanceMockMvc.perform(post("/api/cost-of-sales-account-balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(costOfSalesAccountBalance)))
            .andExpect(status().isCreated());

        // Validate the CostOfSalesAccountBalance in the database
        List<CostOfSalesAccountBalance> costOfSalesAccountBalanceList = costOfSalesAccountBalanceRepository.findAll();
        assertThat(costOfSalesAccountBalanceList).hasSize(databaseSizeBeforeCreate + 1);
        CostOfSalesAccountBalance testCostOfSalesAccountBalance = costOfSalesAccountBalanceList.get(costOfSalesAccountBalanceList.size() - 1);
        assertThat(testCostOfSalesAccountBalance.getBalance()).isEqualTo(DEFAULT_BALANCE);
    }

    @Test
    @Transactional
    public void createCostOfSalesAccountBalanceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = costOfSalesAccountBalanceRepository.findAll().size();

        // Create the CostOfSalesAccountBalance with an existing ID
        costOfSalesAccountBalance.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCostOfSalesAccountBalanceMockMvc.perform(post("/api/cost-of-sales-account-balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(costOfSalesAccountBalance)))
            .andExpect(status().isBadRequest());

        // Validate the CostOfSalesAccountBalance in the database
        List<CostOfSalesAccountBalance> costOfSalesAccountBalanceList = costOfSalesAccountBalanceRepository.findAll();
        assertThat(costOfSalesAccountBalanceList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkBalanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = costOfSalesAccountBalanceRepository.findAll().size();
        // set the field null
        costOfSalesAccountBalance.setBalance(null);

        // Create the CostOfSalesAccountBalance, which fails.

        restCostOfSalesAccountBalanceMockMvc.perform(post("/api/cost-of-sales-account-balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(costOfSalesAccountBalance)))
            .andExpect(status().isBadRequest());

        List<CostOfSalesAccountBalance> costOfSalesAccountBalanceList = costOfSalesAccountBalanceRepository.findAll();
        assertThat(costOfSalesAccountBalanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCostOfSalesAccountBalances() throws Exception {
        // Initialize the database
        costOfSalesAccountBalanceRepository.saveAndFlush(costOfSalesAccountBalance);

        // Get all the costOfSalesAccountBalanceList
        restCostOfSalesAccountBalanceMockMvc.perform(get("/api/cost-of-sales-account-balances?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(costOfSalesAccountBalance.getId().intValue())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.intValue())));
    }
    
    @Test
    @Transactional
    public void getCostOfSalesAccountBalance() throws Exception {
        // Initialize the database
        costOfSalesAccountBalanceRepository.saveAndFlush(costOfSalesAccountBalance);

        // Get the costOfSalesAccountBalance
        restCostOfSalesAccountBalanceMockMvc.perform(get("/api/cost-of-sales-account-balances/{id}", costOfSalesAccountBalance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(costOfSalesAccountBalance.getId().intValue()))
            .andExpect(jsonPath("$.balance").value(DEFAULT_BALANCE.intValue()));
    }


    @Test
    @Transactional
    public void getCostOfSalesAccountBalancesByIdFiltering() throws Exception {
        // Initialize the database
        costOfSalesAccountBalanceRepository.saveAndFlush(costOfSalesAccountBalance);

        Long id = costOfSalesAccountBalance.getId();

        defaultCostOfSalesAccountBalanceShouldBeFound("id.equals=" + id);
        defaultCostOfSalesAccountBalanceShouldNotBeFound("id.notEquals=" + id);

        defaultCostOfSalesAccountBalanceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCostOfSalesAccountBalanceShouldNotBeFound("id.greaterThan=" + id);

        defaultCostOfSalesAccountBalanceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCostOfSalesAccountBalanceShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllCostOfSalesAccountBalancesByBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        costOfSalesAccountBalanceRepository.saveAndFlush(costOfSalesAccountBalance);

        // Get all the costOfSalesAccountBalanceList where balance equals to DEFAULT_BALANCE
        defaultCostOfSalesAccountBalanceShouldBeFound("balance.equals=" + DEFAULT_BALANCE);

        // Get all the costOfSalesAccountBalanceList where balance equals to UPDATED_BALANCE
        defaultCostOfSalesAccountBalanceShouldNotBeFound("balance.equals=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllCostOfSalesAccountBalancesByBalanceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        costOfSalesAccountBalanceRepository.saveAndFlush(costOfSalesAccountBalance);

        // Get all the costOfSalesAccountBalanceList where balance not equals to DEFAULT_BALANCE
        defaultCostOfSalesAccountBalanceShouldNotBeFound("balance.notEquals=" + DEFAULT_BALANCE);

        // Get all the costOfSalesAccountBalanceList where balance not equals to UPDATED_BALANCE
        defaultCostOfSalesAccountBalanceShouldBeFound("balance.notEquals=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllCostOfSalesAccountBalancesByBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        costOfSalesAccountBalanceRepository.saveAndFlush(costOfSalesAccountBalance);

        // Get all the costOfSalesAccountBalanceList where balance in DEFAULT_BALANCE or UPDATED_BALANCE
        defaultCostOfSalesAccountBalanceShouldBeFound("balance.in=" + DEFAULT_BALANCE + "," + UPDATED_BALANCE);

        // Get all the costOfSalesAccountBalanceList where balance equals to UPDATED_BALANCE
        defaultCostOfSalesAccountBalanceShouldNotBeFound("balance.in=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllCostOfSalesAccountBalancesByBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        costOfSalesAccountBalanceRepository.saveAndFlush(costOfSalesAccountBalance);

        // Get all the costOfSalesAccountBalanceList where balance is not null
        defaultCostOfSalesAccountBalanceShouldBeFound("balance.specified=true");

        // Get all the costOfSalesAccountBalanceList where balance is null
        defaultCostOfSalesAccountBalanceShouldNotBeFound("balance.specified=false");
    }

    @Test
    @Transactional
    public void getAllCostOfSalesAccountBalancesByBalanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        costOfSalesAccountBalanceRepository.saveAndFlush(costOfSalesAccountBalance);

        // Get all the costOfSalesAccountBalanceList where balance is greater than or equal to DEFAULT_BALANCE
        defaultCostOfSalesAccountBalanceShouldBeFound("balance.greaterThanOrEqual=" + DEFAULT_BALANCE);

        // Get all the costOfSalesAccountBalanceList where balance is greater than or equal to UPDATED_BALANCE
        defaultCostOfSalesAccountBalanceShouldNotBeFound("balance.greaterThanOrEqual=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllCostOfSalesAccountBalancesByBalanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        costOfSalesAccountBalanceRepository.saveAndFlush(costOfSalesAccountBalance);

        // Get all the costOfSalesAccountBalanceList where balance is less than or equal to DEFAULT_BALANCE
        defaultCostOfSalesAccountBalanceShouldBeFound("balance.lessThanOrEqual=" + DEFAULT_BALANCE);

        // Get all the costOfSalesAccountBalanceList where balance is less than or equal to SMALLER_BALANCE
        defaultCostOfSalesAccountBalanceShouldNotBeFound("balance.lessThanOrEqual=" + SMALLER_BALANCE);
    }

    @Test
    @Transactional
    public void getAllCostOfSalesAccountBalancesByBalanceIsLessThanSomething() throws Exception {
        // Initialize the database
        costOfSalesAccountBalanceRepository.saveAndFlush(costOfSalesAccountBalance);

        // Get all the costOfSalesAccountBalanceList where balance is less than DEFAULT_BALANCE
        defaultCostOfSalesAccountBalanceShouldNotBeFound("balance.lessThan=" + DEFAULT_BALANCE);

        // Get all the costOfSalesAccountBalanceList where balance is less than UPDATED_BALANCE
        defaultCostOfSalesAccountBalanceShouldBeFound("balance.lessThan=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllCostOfSalesAccountBalancesByBalanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        costOfSalesAccountBalanceRepository.saveAndFlush(costOfSalesAccountBalance);

        // Get all the costOfSalesAccountBalanceList where balance is greater than DEFAULT_BALANCE
        defaultCostOfSalesAccountBalanceShouldNotBeFound("balance.greaterThan=" + DEFAULT_BALANCE);

        // Get all the costOfSalesAccountBalanceList where balance is greater than SMALLER_BALANCE
        defaultCostOfSalesAccountBalanceShouldBeFound("balance.greaterThan=" + SMALLER_BALANCE);
    }


    @Test
    @Transactional
    public void getAllCostOfSalesAccountBalancesByLocationIsEqualToSomething() throws Exception {
        // Get already existing entity
        Location location = costOfSalesAccountBalance.getLocation();
        costOfSalesAccountBalanceRepository.saveAndFlush(costOfSalesAccountBalance);
        Long locationId = location.getId();

        // Get all the costOfSalesAccountBalanceList where location equals to locationId
        defaultCostOfSalesAccountBalanceShouldBeFound("locationId.equals=" + locationId);

        // Get all the costOfSalesAccountBalanceList where location equals to locationId + 1
        defaultCostOfSalesAccountBalanceShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }


    @Test
    @Transactional
    public void getAllCostOfSalesAccountBalancesByTransactionTypeIsEqualToSomething() throws Exception {
        // Get already existing entity
        TransactionType transactionType = costOfSalesAccountBalance.getTransactionType();
        costOfSalesAccountBalanceRepository.saveAndFlush(costOfSalesAccountBalance);
        Long transactionTypeId = transactionType.getId();

        // Get all the costOfSalesAccountBalanceList where transactionType equals to transactionTypeId
        defaultCostOfSalesAccountBalanceShouldBeFound("transactionTypeId.equals=" + transactionTypeId);

        // Get all the costOfSalesAccountBalanceList where transactionType equals to transactionTypeId + 1
        defaultCostOfSalesAccountBalanceShouldNotBeFound("transactionTypeId.equals=" + (transactionTypeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCostOfSalesAccountBalanceShouldBeFound(String filter) throws Exception {
        restCostOfSalesAccountBalanceMockMvc.perform(get("/api/cost-of-sales-account-balances?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(costOfSalesAccountBalance.getId().intValue())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.intValue())));

        // Check, that the count call also returns 1
        restCostOfSalesAccountBalanceMockMvc.perform(get("/api/cost-of-sales-account-balances/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCostOfSalesAccountBalanceShouldNotBeFound(String filter) throws Exception {
        restCostOfSalesAccountBalanceMockMvc.perform(get("/api/cost-of-sales-account-balances?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCostOfSalesAccountBalanceMockMvc.perform(get("/api/cost-of-sales-account-balances/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingCostOfSalesAccountBalance() throws Exception {
        // Get the costOfSalesAccountBalance
        restCostOfSalesAccountBalanceMockMvc.perform(get("/api/cost-of-sales-account-balances/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCostOfSalesAccountBalance() throws Exception {
        // Initialize the database
        costOfSalesAccountBalanceService.save(costOfSalesAccountBalance);

        int databaseSizeBeforeUpdate = costOfSalesAccountBalanceRepository.findAll().size();

        // Update the costOfSalesAccountBalance
        CostOfSalesAccountBalance updatedCostOfSalesAccountBalance = costOfSalesAccountBalanceRepository.findById(costOfSalesAccountBalance.getId()).get();
        // Disconnect from session so that the updates on updatedCostOfSalesAccountBalance are not directly saved in db
        em.detach(updatedCostOfSalesAccountBalance);
        updatedCostOfSalesAccountBalance
            .balance(UPDATED_BALANCE);

        restCostOfSalesAccountBalanceMockMvc.perform(put("/api/cost-of-sales-account-balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCostOfSalesAccountBalance)))
            .andExpect(status().isOk());

        // Validate the CostOfSalesAccountBalance in the database
        List<CostOfSalesAccountBalance> costOfSalesAccountBalanceList = costOfSalesAccountBalanceRepository.findAll();
        assertThat(costOfSalesAccountBalanceList).hasSize(databaseSizeBeforeUpdate);
        CostOfSalesAccountBalance testCostOfSalesAccountBalance = costOfSalesAccountBalanceList.get(costOfSalesAccountBalanceList.size() - 1);
        assertThat(testCostOfSalesAccountBalance.getBalance()).isEqualTo(UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void updateNonExistingCostOfSalesAccountBalance() throws Exception {
        int databaseSizeBeforeUpdate = costOfSalesAccountBalanceRepository.findAll().size();

        // Create the CostOfSalesAccountBalance

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCostOfSalesAccountBalanceMockMvc.perform(put("/api/cost-of-sales-account-balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(costOfSalesAccountBalance)))
            .andExpect(status().isBadRequest());

        // Validate the CostOfSalesAccountBalance in the database
        List<CostOfSalesAccountBalance> costOfSalesAccountBalanceList = costOfSalesAccountBalanceRepository.findAll();
        assertThat(costOfSalesAccountBalanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCostOfSalesAccountBalance() throws Exception {
        // Initialize the database
        costOfSalesAccountBalanceService.save(costOfSalesAccountBalance);

        int databaseSizeBeforeDelete = costOfSalesAccountBalanceRepository.findAll().size();

        // Delete the costOfSalesAccountBalance
        restCostOfSalesAccountBalanceMockMvc.perform(delete("/api/cost-of-sales-account-balances/{id}", costOfSalesAccountBalance.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CostOfSalesAccountBalance> costOfSalesAccountBalanceList = costOfSalesAccountBalanceRepository.findAll();
        assertThat(costOfSalesAccountBalanceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.WikunumApp;
import com.alphadevs.sales.domain.PaymentTypeBalance;
import com.alphadevs.sales.domain.Location;
import com.alphadevs.sales.domain.TransactionType;
import com.alphadevs.sales.domain.PaymentTypes;
import com.alphadevs.sales.repository.PaymentTypeBalanceRepository;
import com.alphadevs.sales.service.PaymentTypeBalanceService;
import com.alphadevs.sales.web.rest.errors.ExceptionTranslator;
import com.alphadevs.sales.service.dto.PaymentTypeBalanceCriteria;
import com.alphadevs.sales.service.PaymentTypeBalanceQueryService;

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
 * Integration tests for the {@link PaymentTypeBalanceResource} REST controller.
 */
@SpringBootTest(classes = WikunumApp.class)
public class PaymentTypeBalanceResourceIT {

    private static final BigDecimal DEFAULT_BALANCE = new BigDecimal(1);
    private static final BigDecimal UPDATED_BALANCE = new BigDecimal(2);
    private static final BigDecimal SMALLER_BALANCE = new BigDecimal(1 - 1);

    @Autowired
    private PaymentTypeBalanceRepository paymentTypeBalanceRepository;

    @Autowired
    private PaymentTypeBalanceService paymentTypeBalanceService;

    @Autowired
    private PaymentTypeBalanceQueryService paymentTypeBalanceQueryService;

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

    private MockMvc restPaymentTypeBalanceMockMvc;

    private PaymentTypeBalance paymentTypeBalance;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PaymentTypeBalanceResource paymentTypeBalanceResource = new PaymentTypeBalanceResource(paymentTypeBalanceService, paymentTypeBalanceQueryService);
        this.restPaymentTypeBalanceMockMvc = MockMvcBuilders.standaloneSetup(paymentTypeBalanceResource)
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
    public static PaymentTypeBalance createEntity(EntityManager em) {
        PaymentTypeBalance paymentTypeBalance = new PaymentTypeBalance()
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
        paymentTypeBalance.setLocation(location);
        // Add required entity
        TransactionType transactionType;
        if (TestUtil.findAll(em, TransactionType.class).isEmpty()) {
            transactionType = TransactionTypeResourceIT.createEntity(em);
            em.persist(transactionType);
            em.flush();
        } else {
            transactionType = TestUtil.findAll(em, TransactionType.class).get(0);
        }
        paymentTypeBalance.setTransactionType(transactionType);
        // Add required entity
        PaymentTypes paymentTypes;
        if (TestUtil.findAll(em, PaymentTypes.class).isEmpty()) {
            paymentTypes = PaymentTypesResourceIT.createEntity(em);
            em.persist(paymentTypes);
            em.flush();
        } else {
            paymentTypes = TestUtil.findAll(em, PaymentTypes.class).get(0);
        }
        paymentTypeBalance.setPayType(paymentTypes);
        return paymentTypeBalance;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentTypeBalance createUpdatedEntity(EntityManager em) {
        PaymentTypeBalance paymentTypeBalance = new PaymentTypeBalance()
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
        paymentTypeBalance.setLocation(location);
        // Add required entity
        TransactionType transactionType;
        if (TestUtil.findAll(em, TransactionType.class).isEmpty()) {
            transactionType = TransactionTypeResourceIT.createUpdatedEntity(em);
            em.persist(transactionType);
            em.flush();
        } else {
            transactionType = TestUtil.findAll(em, TransactionType.class).get(0);
        }
        paymentTypeBalance.setTransactionType(transactionType);
        // Add required entity
        PaymentTypes paymentTypes;
        if (TestUtil.findAll(em, PaymentTypes.class).isEmpty()) {
            paymentTypes = PaymentTypesResourceIT.createUpdatedEntity(em);
            em.persist(paymentTypes);
            em.flush();
        } else {
            paymentTypes = TestUtil.findAll(em, PaymentTypes.class).get(0);
        }
        paymentTypeBalance.setPayType(paymentTypes);
        return paymentTypeBalance;
    }

    @BeforeEach
    public void initTest() {
        paymentTypeBalance = createEntity(em);
    }

    @Test
    @Transactional
    public void createPaymentTypeBalance() throws Exception {
        int databaseSizeBeforeCreate = paymentTypeBalanceRepository.findAll().size();

        // Create the PaymentTypeBalance
        restPaymentTypeBalanceMockMvc.perform(post("/api/payment-type-balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentTypeBalance)))
            .andExpect(status().isCreated());

        // Validate the PaymentTypeBalance in the database
        List<PaymentTypeBalance> paymentTypeBalanceList = paymentTypeBalanceRepository.findAll();
        assertThat(paymentTypeBalanceList).hasSize(databaseSizeBeforeCreate + 1);
        PaymentTypeBalance testPaymentTypeBalance = paymentTypeBalanceList.get(paymentTypeBalanceList.size() - 1);
        assertThat(testPaymentTypeBalance.getBalance()).isEqualTo(DEFAULT_BALANCE);
    }

    @Test
    @Transactional
    public void createPaymentTypeBalanceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = paymentTypeBalanceRepository.findAll().size();

        // Create the PaymentTypeBalance with an existing ID
        paymentTypeBalance.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentTypeBalanceMockMvc.perform(post("/api/payment-type-balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentTypeBalance)))
            .andExpect(status().isBadRequest());

        // Validate the PaymentTypeBalance in the database
        List<PaymentTypeBalance> paymentTypeBalanceList = paymentTypeBalanceRepository.findAll();
        assertThat(paymentTypeBalanceList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkBalanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentTypeBalanceRepository.findAll().size();
        // set the field null
        paymentTypeBalance.setBalance(null);

        // Create the PaymentTypeBalance, which fails.

        restPaymentTypeBalanceMockMvc.perform(post("/api/payment-type-balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentTypeBalance)))
            .andExpect(status().isBadRequest());

        List<PaymentTypeBalance> paymentTypeBalanceList = paymentTypeBalanceRepository.findAll();
        assertThat(paymentTypeBalanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPaymentTypeBalances() throws Exception {
        // Initialize the database
        paymentTypeBalanceRepository.saveAndFlush(paymentTypeBalance);

        // Get all the paymentTypeBalanceList
        restPaymentTypeBalanceMockMvc.perform(get("/api/payment-type-balances?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentTypeBalance.getId().intValue())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.intValue())));
    }
    
    @Test
    @Transactional
    public void getPaymentTypeBalance() throws Exception {
        // Initialize the database
        paymentTypeBalanceRepository.saveAndFlush(paymentTypeBalance);

        // Get the paymentTypeBalance
        restPaymentTypeBalanceMockMvc.perform(get("/api/payment-type-balances/{id}", paymentTypeBalance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(paymentTypeBalance.getId().intValue()))
            .andExpect(jsonPath("$.balance").value(DEFAULT_BALANCE.intValue()));
    }


    @Test
    @Transactional
    public void getPaymentTypeBalancesByIdFiltering() throws Exception {
        // Initialize the database
        paymentTypeBalanceRepository.saveAndFlush(paymentTypeBalance);

        Long id = paymentTypeBalance.getId();

        defaultPaymentTypeBalanceShouldBeFound("id.equals=" + id);
        defaultPaymentTypeBalanceShouldNotBeFound("id.notEquals=" + id);

        defaultPaymentTypeBalanceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPaymentTypeBalanceShouldNotBeFound("id.greaterThan=" + id);

        defaultPaymentTypeBalanceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPaymentTypeBalanceShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllPaymentTypeBalancesByBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentTypeBalanceRepository.saveAndFlush(paymentTypeBalance);

        // Get all the paymentTypeBalanceList where balance equals to DEFAULT_BALANCE
        defaultPaymentTypeBalanceShouldBeFound("balance.equals=" + DEFAULT_BALANCE);

        // Get all the paymentTypeBalanceList where balance equals to UPDATED_BALANCE
        defaultPaymentTypeBalanceShouldNotBeFound("balance.equals=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllPaymentTypeBalancesByBalanceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentTypeBalanceRepository.saveAndFlush(paymentTypeBalance);

        // Get all the paymentTypeBalanceList where balance not equals to DEFAULT_BALANCE
        defaultPaymentTypeBalanceShouldNotBeFound("balance.notEquals=" + DEFAULT_BALANCE);

        // Get all the paymentTypeBalanceList where balance not equals to UPDATED_BALANCE
        defaultPaymentTypeBalanceShouldBeFound("balance.notEquals=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllPaymentTypeBalancesByBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        paymentTypeBalanceRepository.saveAndFlush(paymentTypeBalance);

        // Get all the paymentTypeBalanceList where balance in DEFAULT_BALANCE or UPDATED_BALANCE
        defaultPaymentTypeBalanceShouldBeFound("balance.in=" + DEFAULT_BALANCE + "," + UPDATED_BALANCE);

        // Get all the paymentTypeBalanceList where balance equals to UPDATED_BALANCE
        defaultPaymentTypeBalanceShouldNotBeFound("balance.in=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllPaymentTypeBalancesByBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentTypeBalanceRepository.saveAndFlush(paymentTypeBalance);

        // Get all the paymentTypeBalanceList where balance is not null
        defaultPaymentTypeBalanceShouldBeFound("balance.specified=true");

        // Get all the paymentTypeBalanceList where balance is null
        defaultPaymentTypeBalanceShouldNotBeFound("balance.specified=false");
    }

    @Test
    @Transactional
    public void getAllPaymentTypeBalancesByBalanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentTypeBalanceRepository.saveAndFlush(paymentTypeBalance);

        // Get all the paymentTypeBalanceList where balance is greater than or equal to DEFAULT_BALANCE
        defaultPaymentTypeBalanceShouldBeFound("balance.greaterThanOrEqual=" + DEFAULT_BALANCE);

        // Get all the paymentTypeBalanceList where balance is greater than or equal to UPDATED_BALANCE
        defaultPaymentTypeBalanceShouldNotBeFound("balance.greaterThanOrEqual=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllPaymentTypeBalancesByBalanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentTypeBalanceRepository.saveAndFlush(paymentTypeBalance);

        // Get all the paymentTypeBalanceList where balance is less than or equal to DEFAULT_BALANCE
        defaultPaymentTypeBalanceShouldBeFound("balance.lessThanOrEqual=" + DEFAULT_BALANCE);

        // Get all the paymentTypeBalanceList where balance is less than or equal to SMALLER_BALANCE
        defaultPaymentTypeBalanceShouldNotBeFound("balance.lessThanOrEqual=" + SMALLER_BALANCE);
    }

    @Test
    @Transactional
    public void getAllPaymentTypeBalancesByBalanceIsLessThanSomething() throws Exception {
        // Initialize the database
        paymentTypeBalanceRepository.saveAndFlush(paymentTypeBalance);

        // Get all the paymentTypeBalanceList where balance is less than DEFAULT_BALANCE
        defaultPaymentTypeBalanceShouldNotBeFound("balance.lessThan=" + DEFAULT_BALANCE);

        // Get all the paymentTypeBalanceList where balance is less than UPDATED_BALANCE
        defaultPaymentTypeBalanceShouldBeFound("balance.lessThan=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllPaymentTypeBalancesByBalanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        paymentTypeBalanceRepository.saveAndFlush(paymentTypeBalance);

        // Get all the paymentTypeBalanceList where balance is greater than DEFAULT_BALANCE
        defaultPaymentTypeBalanceShouldNotBeFound("balance.greaterThan=" + DEFAULT_BALANCE);

        // Get all the paymentTypeBalanceList where balance is greater than SMALLER_BALANCE
        defaultPaymentTypeBalanceShouldBeFound("balance.greaterThan=" + SMALLER_BALANCE);
    }


    @Test
    @Transactional
    public void getAllPaymentTypeBalancesByLocationIsEqualToSomething() throws Exception {
        // Get already existing entity
        Location location = paymentTypeBalance.getLocation();
        paymentTypeBalanceRepository.saveAndFlush(paymentTypeBalance);
        Long locationId = location.getId();

        // Get all the paymentTypeBalanceList where location equals to locationId
        defaultPaymentTypeBalanceShouldBeFound("locationId.equals=" + locationId);

        // Get all the paymentTypeBalanceList where location equals to locationId + 1
        defaultPaymentTypeBalanceShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }


    @Test
    @Transactional
    public void getAllPaymentTypeBalancesByTransactionTypeIsEqualToSomething() throws Exception {
        // Get already existing entity
        TransactionType transactionType = paymentTypeBalance.getTransactionType();
        paymentTypeBalanceRepository.saveAndFlush(paymentTypeBalance);
        Long transactionTypeId = transactionType.getId();

        // Get all the paymentTypeBalanceList where transactionType equals to transactionTypeId
        defaultPaymentTypeBalanceShouldBeFound("transactionTypeId.equals=" + transactionTypeId);

        // Get all the paymentTypeBalanceList where transactionType equals to transactionTypeId + 1
        defaultPaymentTypeBalanceShouldNotBeFound("transactionTypeId.equals=" + (transactionTypeId + 1));
    }


    @Test
    @Transactional
    public void getAllPaymentTypeBalancesByPayTypeIsEqualToSomething() throws Exception {
        // Get already existing entity
        PaymentTypes payType = paymentTypeBalance.getPayType();
        paymentTypeBalanceRepository.saveAndFlush(paymentTypeBalance);
        Long payTypeId = payType.getId();

        // Get all the paymentTypeBalanceList where payType equals to payTypeId
        defaultPaymentTypeBalanceShouldBeFound("payTypeId.equals=" + payTypeId);

        // Get all the paymentTypeBalanceList where payType equals to payTypeId + 1
        defaultPaymentTypeBalanceShouldNotBeFound("payTypeId.equals=" + (payTypeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPaymentTypeBalanceShouldBeFound(String filter) throws Exception {
        restPaymentTypeBalanceMockMvc.perform(get("/api/payment-type-balances?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentTypeBalance.getId().intValue())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.intValue())));

        // Check, that the count call also returns 1
        restPaymentTypeBalanceMockMvc.perform(get("/api/payment-type-balances/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPaymentTypeBalanceShouldNotBeFound(String filter) throws Exception {
        restPaymentTypeBalanceMockMvc.perform(get("/api/payment-type-balances?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPaymentTypeBalanceMockMvc.perform(get("/api/payment-type-balances/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingPaymentTypeBalance() throws Exception {
        // Get the paymentTypeBalance
        restPaymentTypeBalanceMockMvc.perform(get("/api/payment-type-balances/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePaymentTypeBalance() throws Exception {
        // Initialize the database
        paymentTypeBalanceService.save(paymentTypeBalance);

        int databaseSizeBeforeUpdate = paymentTypeBalanceRepository.findAll().size();

        // Update the paymentTypeBalance
        PaymentTypeBalance updatedPaymentTypeBalance = paymentTypeBalanceRepository.findById(paymentTypeBalance.getId()).get();
        // Disconnect from session so that the updates on updatedPaymentTypeBalance are not directly saved in db
        em.detach(updatedPaymentTypeBalance);
        updatedPaymentTypeBalance
            .balance(UPDATED_BALANCE);

        restPaymentTypeBalanceMockMvc.perform(put("/api/payment-type-balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPaymentTypeBalance)))
            .andExpect(status().isOk());

        // Validate the PaymentTypeBalance in the database
        List<PaymentTypeBalance> paymentTypeBalanceList = paymentTypeBalanceRepository.findAll();
        assertThat(paymentTypeBalanceList).hasSize(databaseSizeBeforeUpdate);
        PaymentTypeBalance testPaymentTypeBalance = paymentTypeBalanceList.get(paymentTypeBalanceList.size() - 1);
        assertThat(testPaymentTypeBalance.getBalance()).isEqualTo(UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void updateNonExistingPaymentTypeBalance() throws Exception {
        int databaseSizeBeforeUpdate = paymentTypeBalanceRepository.findAll().size();

        // Create the PaymentTypeBalance

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentTypeBalanceMockMvc.perform(put("/api/payment-type-balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentTypeBalance)))
            .andExpect(status().isBadRequest());

        // Validate the PaymentTypeBalance in the database
        List<PaymentTypeBalance> paymentTypeBalanceList = paymentTypeBalanceRepository.findAll();
        assertThat(paymentTypeBalanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePaymentTypeBalance() throws Exception {
        // Initialize the database
        paymentTypeBalanceService.save(paymentTypeBalance);

        int databaseSizeBeforeDelete = paymentTypeBalanceRepository.findAll().size();

        // Delete the paymentTypeBalance
        restPaymentTypeBalanceMockMvc.perform(delete("/api/payment-type-balances/{id}", paymentTypeBalance.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PaymentTypeBalance> paymentTypeBalanceList = paymentTypeBalanceRepository.findAll();
        assertThat(paymentTypeBalanceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

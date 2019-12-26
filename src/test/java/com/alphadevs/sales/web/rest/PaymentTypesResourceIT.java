package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.WikunumApp;
import com.alphadevs.sales.domain.PaymentTypes;
import com.alphadevs.sales.domain.Location;
import com.alphadevs.sales.domain.Invoice;
import com.alphadevs.sales.repository.PaymentTypesRepository;
import com.alphadevs.sales.service.PaymentTypesService;
import com.alphadevs.sales.web.rest.errors.ExceptionTranslator;
import com.alphadevs.sales.service.dto.PaymentTypesCriteria;
import com.alphadevs.sales.service.PaymentTypesQueryService;

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
import java.util.List;

import static com.alphadevs.sales.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PaymentTypesResource} REST controller.
 */
@SpringBootTest(classes = WikunumApp.class)
public class PaymentTypesResourceIT {

    private static final String DEFAULT_PAYMENT_TYPES_CODE = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_TYPES_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_PAYMENT_TYPES = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_TYPES = "BBBBBBBBBB";

    private static final Double DEFAULT_PAYMENT_TYPES_CHARGE_PER = 1D;
    private static final Double UPDATED_PAYMENT_TYPES_CHARGE_PER = 2D;
    private static final Double SMALLER_PAYMENT_TYPES_CHARGE_PER = 1D - 1D;

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    @Autowired
    private PaymentTypesRepository paymentTypesRepository;

    @Autowired
    private PaymentTypesService paymentTypesService;

    @Autowired
    private PaymentTypesQueryService paymentTypesQueryService;

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

    private MockMvc restPaymentTypesMockMvc;

    private PaymentTypes paymentTypes;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PaymentTypesResource paymentTypesResource = new PaymentTypesResource(paymentTypesService, paymentTypesQueryService);
        this.restPaymentTypesMockMvc = MockMvcBuilders.standaloneSetup(paymentTypesResource)
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
    public static PaymentTypes createEntity(EntityManager em) {
        PaymentTypes paymentTypes = new PaymentTypes()
            .paymentTypesCode(DEFAULT_PAYMENT_TYPES_CODE)
            .paymentTypes(DEFAULT_PAYMENT_TYPES)
            .paymentTypesChargePer(DEFAULT_PAYMENT_TYPES_CHARGE_PER)
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
        paymentTypes.setLocation(location);
        return paymentTypes;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentTypes createUpdatedEntity(EntityManager em) {
        PaymentTypes paymentTypes = new PaymentTypes()
            .paymentTypesCode(UPDATED_PAYMENT_TYPES_CODE)
            .paymentTypes(UPDATED_PAYMENT_TYPES)
            .paymentTypesChargePer(UPDATED_PAYMENT_TYPES_CHARGE_PER)
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
        paymentTypes.setLocation(location);
        return paymentTypes;
    }

    @BeforeEach
    public void initTest() {
        paymentTypes = createEntity(em);
    }

    @Test
    @Transactional
    public void createPaymentTypes() throws Exception {
        int databaseSizeBeforeCreate = paymentTypesRepository.findAll().size();

        // Create the PaymentTypes
        restPaymentTypesMockMvc.perform(post("/api/payment-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentTypes)))
            .andExpect(status().isCreated());

        // Validate the PaymentTypes in the database
        List<PaymentTypes> paymentTypesList = paymentTypesRepository.findAll();
        assertThat(paymentTypesList).hasSize(databaseSizeBeforeCreate + 1);
        PaymentTypes testPaymentTypes = paymentTypesList.get(paymentTypesList.size() - 1);
        assertThat(testPaymentTypes.getPaymentTypesCode()).isEqualTo(DEFAULT_PAYMENT_TYPES_CODE);
        assertThat(testPaymentTypes.getPaymentTypes()).isEqualTo(DEFAULT_PAYMENT_TYPES);
        assertThat(testPaymentTypes.getPaymentTypesChargePer()).isEqualTo(DEFAULT_PAYMENT_TYPES_CHARGE_PER);
        assertThat(testPaymentTypes.isIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void createPaymentTypesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = paymentTypesRepository.findAll().size();

        // Create the PaymentTypes with an existing ID
        paymentTypes.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentTypesMockMvc.perform(post("/api/payment-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentTypes)))
            .andExpect(status().isBadRequest());

        // Validate the PaymentTypes in the database
        List<PaymentTypes> paymentTypesList = paymentTypesRepository.findAll();
        assertThat(paymentTypesList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkPaymentTypesCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentTypesRepository.findAll().size();
        // set the field null
        paymentTypes.setPaymentTypesCode(null);

        // Create the PaymentTypes, which fails.

        restPaymentTypesMockMvc.perform(post("/api/payment-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentTypes)))
            .andExpect(status().isBadRequest());

        List<PaymentTypes> paymentTypesList = paymentTypesRepository.findAll();
        assertThat(paymentTypesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPaymentTypesIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentTypesRepository.findAll().size();
        // set the field null
        paymentTypes.setPaymentTypes(null);

        // Create the PaymentTypes, which fails.

        restPaymentTypesMockMvc.perform(post("/api/payment-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentTypes)))
            .andExpect(status().isBadRequest());

        List<PaymentTypes> paymentTypesList = paymentTypesRepository.findAll();
        assertThat(paymentTypesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPaymentTypes() throws Exception {
        // Initialize the database
        paymentTypesRepository.saveAndFlush(paymentTypes);

        // Get all the paymentTypesList
        restPaymentTypesMockMvc.perform(get("/api/payment-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentTypes.getId().intValue())))
            .andExpect(jsonPath("$.[*].paymentTypesCode").value(hasItem(DEFAULT_PAYMENT_TYPES_CODE)))
            .andExpect(jsonPath("$.[*].paymentTypes").value(hasItem(DEFAULT_PAYMENT_TYPES)))
            .andExpect(jsonPath("$.[*].paymentTypesChargePer").value(hasItem(DEFAULT_PAYMENT_TYPES_CHARGE_PER.doubleValue())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getPaymentTypes() throws Exception {
        // Initialize the database
        paymentTypesRepository.saveAndFlush(paymentTypes);

        // Get the paymentTypes
        restPaymentTypesMockMvc.perform(get("/api/payment-types/{id}", paymentTypes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(paymentTypes.getId().intValue()))
            .andExpect(jsonPath("$.paymentTypesCode").value(DEFAULT_PAYMENT_TYPES_CODE))
            .andExpect(jsonPath("$.paymentTypes").value(DEFAULT_PAYMENT_TYPES))
            .andExpect(jsonPath("$.paymentTypesChargePer").value(DEFAULT_PAYMENT_TYPES_CHARGE_PER.doubleValue()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }


    @Test
    @Transactional
    public void getPaymentTypesByIdFiltering() throws Exception {
        // Initialize the database
        paymentTypesRepository.saveAndFlush(paymentTypes);

        Long id = paymentTypes.getId();

        defaultPaymentTypesShouldBeFound("id.equals=" + id);
        defaultPaymentTypesShouldNotBeFound("id.notEquals=" + id);

        defaultPaymentTypesShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPaymentTypesShouldNotBeFound("id.greaterThan=" + id);

        defaultPaymentTypesShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPaymentTypesShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllPaymentTypesByPaymentTypesCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentTypesRepository.saveAndFlush(paymentTypes);

        // Get all the paymentTypesList where paymentTypesCode equals to DEFAULT_PAYMENT_TYPES_CODE
        defaultPaymentTypesShouldBeFound("paymentTypesCode.equals=" + DEFAULT_PAYMENT_TYPES_CODE);

        // Get all the paymentTypesList where paymentTypesCode equals to UPDATED_PAYMENT_TYPES_CODE
        defaultPaymentTypesShouldNotBeFound("paymentTypesCode.equals=" + UPDATED_PAYMENT_TYPES_CODE);
    }

    @Test
    @Transactional
    public void getAllPaymentTypesByPaymentTypesCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentTypesRepository.saveAndFlush(paymentTypes);

        // Get all the paymentTypesList where paymentTypesCode not equals to DEFAULT_PAYMENT_TYPES_CODE
        defaultPaymentTypesShouldNotBeFound("paymentTypesCode.notEquals=" + DEFAULT_PAYMENT_TYPES_CODE);

        // Get all the paymentTypesList where paymentTypesCode not equals to UPDATED_PAYMENT_TYPES_CODE
        defaultPaymentTypesShouldBeFound("paymentTypesCode.notEquals=" + UPDATED_PAYMENT_TYPES_CODE);
    }

    @Test
    @Transactional
    public void getAllPaymentTypesByPaymentTypesCodeIsInShouldWork() throws Exception {
        // Initialize the database
        paymentTypesRepository.saveAndFlush(paymentTypes);

        // Get all the paymentTypesList where paymentTypesCode in DEFAULT_PAYMENT_TYPES_CODE or UPDATED_PAYMENT_TYPES_CODE
        defaultPaymentTypesShouldBeFound("paymentTypesCode.in=" + DEFAULT_PAYMENT_TYPES_CODE + "," + UPDATED_PAYMENT_TYPES_CODE);

        // Get all the paymentTypesList where paymentTypesCode equals to UPDATED_PAYMENT_TYPES_CODE
        defaultPaymentTypesShouldNotBeFound("paymentTypesCode.in=" + UPDATED_PAYMENT_TYPES_CODE);
    }

    @Test
    @Transactional
    public void getAllPaymentTypesByPaymentTypesCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentTypesRepository.saveAndFlush(paymentTypes);

        // Get all the paymentTypesList where paymentTypesCode is not null
        defaultPaymentTypesShouldBeFound("paymentTypesCode.specified=true");

        // Get all the paymentTypesList where paymentTypesCode is null
        defaultPaymentTypesShouldNotBeFound("paymentTypesCode.specified=false");
    }
                @Test
    @Transactional
    public void getAllPaymentTypesByPaymentTypesCodeContainsSomething() throws Exception {
        // Initialize the database
        paymentTypesRepository.saveAndFlush(paymentTypes);

        // Get all the paymentTypesList where paymentTypesCode contains DEFAULT_PAYMENT_TYPES_CODE
        defaultPaymentTypesShouldBeFound("paymentTypesCode.contains=" + DEFAULT_PAYMENT_TYPES_CODE);

        // Get all the paymentTypesList where paymentTypesCode contains UPDATED_PAYMENT_TYPES_CODE
        defaultPaymentTypesShouldNotBeFound("paymentTypesCode.contains=" + UPDATED_PAYMENT_TYPES_CODE);
    }

    @Test
    @Transactional
    public void getAllPaymentTypesByPaymentTypesCodeNotContainsSomething() throws Exception {
        // Initialize the database
        paymentTypesRepository.saveAndFlush(paymentTypes);

        // Get all the paymentTypesList where paymentTypesCode does not contain DEFAULT_PAYMENT_TYPES_CODE
        defaultPaymentTypesShouldNotBeFound("paymentTypesCode.doesNotContain=" + DEFAULT_PAYMENT_TYPES_CODE);

        // Get all the paymentTypesList where paymentTypesCode does not contain UPDATED_PAYMENT_TYPES_CODE
        defaultPaymentTypesShouldBeFound("paymentTypesCode.doesNotContain=" + UPDATED_PAYMENT_TYPES_CODE);
    }


    @Test
    @Transactional
    public void getAllPaymentTypesByPaymentTypesIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentTypesRepository.saveAndFlush(paymentTypes);

        // Get all the paymentTypesList where paymentTypes equals to DEFAULT_PAYMENT_TYPES
        defaultPaymentTypesShouldBeFound("paymentTypes.equals=" + DEFAULT_PAYMENT_TYPES);

        // Get all the paymentTypesList where paymentTypes equals to UPDATED_PAYMENT_TYPES
        defaultPaymentTypesShouldNotBeFound("paymentTypes.equals=" + UPDATED_PAYMENT_TYPES);
    }

    @Test
    @Transactional
    public void getAllPaymentTypesByPaymentTypesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentTypesRepository.saveAndFlush(paymentTypes);

        // Get all the paymentTypesList where paymentTypes not equals to DEFAULT_PAYMENT_TYPES
        defaultPaymentTypesShouldNotBeFound("paymentTypes.notEquals=" + DEFAULT_PAYMENT_TYPES);

        // Get all the paymentTypesList where paymentTypes not equals to UPDATED_PAYMENT_TYPES
        defaultPaymentTypesShouldBeFound("paymentTypes.notEquals=" + UPDATED_PAYMENT_TYPES);
    }

    @Test
    @Transactional
    public void getAllPaymentTypesByPaymentTypesIsInShouldWork() throws Exception {
        // Initialize the database
        paymentTypesRepository.saveAndFlush(paymentTypes);

        // Get all the paymentTypesList where paymentTypes in DEFAULT_PAYMENT_TYPES or UPDATED_PAYMENT_TYPES
        defaultPaymentTypesShouldBeFound("paymentTypes.in=" + DEFAULT_PAYMENT_TYPES + "," + UPDATED_PAYMENT_TYPES);

        // Get all the paymentTypesList where paymentTypes equals to UPDATED_PAYMENT_TYPES
        defaultPaymentTypesShouldNotBeFound("paymentTypes.in=" + UPDATED_PAYMENT_TYPES);
    }

    @Test
    @Transactional
    public void getAllPaymentTypesByPaymentTypesIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentTypesRepository.saveAndFlush(paymentTypes);

        // Get all the paymentTypesList where paymentTypes is not null
        defaultPaymentTypesShouldBeFound("paymentTypes.specified=true");

        // Get all the paymentTypesList where paymentTypes is null
        defaultPaymentTypesShouldNotBeFound("paymentTypes.specified=false");
    }
                @Test
    @Transactional
    public void getAllPaymentTypesByPaymentTypesContainsSomething() throws Exception {
        // Initialize the database
        paymentTypesRepository.saveAndFlush(paymentTypes);

        // Get all the paymentTypesList where paymentTypes contains DEFAULT_PAYMENT_TYPES
        defaultPaymentTypesShouldBeFound("paymentTypes.contains=" + DEFAULT_PAYMENT_TYPES);

        // Get all the paymentTypesList where paymentTypes contains UPDATED_PAYMENT_TYPES
        defaultPaymentTypesShouldNotBeFound("paymentTypes.contains=" + UPDATED_PAYMENT_TYPES);
    }

    @Test
    @Transactional
    public void getAllPaymentTypesByPaymentTypesNotContainsSomething() throws Exception {
        // Initialize the database
        paymentTypesRepository.saveAndFlush(paymentTypes);

        // Get all the paymentTypesList where paymentTypes does not contain DEFAULT_PAYMENT_TYPES
        defaultPaymentTypesShouldNotBeFound("paymentTypes.doesNotContain=" + DEFAULT_PAYMENT_TYPES);

        // Get all the paymentTypesList where paymentTypes does not contain UPDATED_PAYMENT_TYPES
        defaultPaymentTypesShouldBeFound("paymentTypes.doesNotContain=" + UPDATED_PAYMENT_TYPES);
    }


    @Test
    @Transactional
    public void getAllPaymentTypesByPaymentTypesChargePerIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentTypesRepository.saveAndFlush(paymentTypes);

        // Get all the paymentTypesList where paymentTypesChargePer equals to DEFAULT_PAYMENT_TYPES_CHARGE_PER
        defaultPaymentTypesShouldBeFound("paymentTypesChargePer.equals=" + DEFAULT_PAYMENT_TYPES_CHARGE_PER);

        // Get all the paymentTypesList where paymentTypesChargePer equals to UPDATED_PAYMENT_TYPES_CHARGE_PER
        defaultPaymentTypesShouldNotBeFound("paymentTypesChargePer.equals=" + UPDATED_PAYMENT_TYPES_CHARGE_PER);
    }

    @Test
    @Transactional
    public void getAllPaymentTypesByPaymentTypesChargePerIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentTypesRepository.saveAndFlush(paymentTypes);

        // Get all the paymentTypesList where paymentTypesChargePer not equals to DEFAULT_PAYMENT_TYPES_CHARGE_PER
        defaultPaymentTypesShouldNotBeFound("paymentTypesChargePer.notEquals=" + DEFAULT_PAYMENT_TYPES_CHARGE_PER);

        // Get all the paymentTypesList where paymentTypesChargePer not equals to UPDATED_PAYMENT_TYPES_CHARGE_PER
        defaultPaymentTypesShouldBeFound("paymentTypesChargePer.notEquals=" + UPDATED_PAYMENT_TYPES_CHARGE_PER);
    }

    @Test
    @Transactional
    public void getAllPaymentTypesByPaymentTypesChargePerIsInShouldWork() throws Exception {
        // Initialize the database
        paymentTypesRepository.saveAndFlush(paymentTypes);

        // Get all the paymentTypesList where paymentTypesChargePer in DEFAULT_PAYMENT_TYPES_CHARGE_PER or UPDATED_PAYMENT_TYPES_CHARGE_PER
        defaultPaymentTypesShouldBeFound("paymentTypesChargePer.in=" + DEFAULT_PAYMENT_TYPES_CHARGE_PER + "," + UPDATED_PAYMENT_TYPES_CHARGE_PER);

        // Get all the paymentTypesList where paymentTypesChargePer equals to UPDATED_PAYMENT_TYPES_CHARGE_PER
        defaultPaymentTypesShouldNotBeFound("paymentTypesChargePer.in=" + UPDATED_PAYMENT_TYPES_CHARGE_PER);
    }

    @Test
    @Transactional
    public void getAllPaymentTypesByPaymentTypesChargePerIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentTypesRepository.saveAndFlush(paymentTypes);

        // Get all the paymentTypesList where paymentTypesChargePer is not null
        defaultPaymentTypesShouldBeFound("paymentTypesChargePer.specified=true");

        // Get all the paymentTypesList where paymentTypesChargePer is null
        defaultPaymentTypesShouldNotBeFound("paymentTypesChargePer.specified=false");
    }

    @Test
    @Transactional
    public void getAllPaymentTypesByPaymentTypesChargePerIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentTypesRepository.saveAndFlush(paymentTypes);

        // Get all the paymentTypesList where paymentTypesChargePer is greater than or equal to DEFAULT_PAYMENT_TYPES_CHARGE_PER
        defaultPaymentTypesShouldBeFound("paymentTypesChargePer.greaterThanOrEqual=" + DEFAULT_PAYMENT_TYPES_CHARGE_PER);

        // Get all the paymentTypesList where paymentTypesChargePer is greater than or equal to UPDATED_PAYMENT_TYPES_CHARGE_PER
        defaultPaymentTypesShouldNotBeFound("paymentTypesChargePer.greaterThanOrEqual=" + UPDATED_PAYMENT_TYPES_CHARGE_PER);
    }

    @Test
    @Transactional
    public void getAllPaymentTypesByPaymentTypesChargePerIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentTypesRepository.saveAndFlush(paymentTypes);

        // Get all the paymentTypesList where paymentTypesChargePer is less than or equal to DEFAULT_PAYMENT_TYPES_CHARGE_PER
        defaultPaymentTypesShouldBeFound("paymentTypesChargePer.lessThanOrEqual=" + DEFAULT_PAYMENT_TYPES_CHARGE_PER);

        // Get all the paymentTypesList where paymentTypesChargePer is less than or equal to SMALLER_PAYMENT_TYPES_CHARGE_PER
        defaultPaymentTypesShouldNotBeFound("paymentTypesChargePer.lessThanOrEqual=" + SMALLER_PAYMENT_TYPES_CHARGE_PER);
    }

    @Test
    @Transactional
    public void getAllPaymentTypesByPaymentTypesChargePerIsLessThanSomething() throws Exception {
        // Initialize the database
        paymentTypesRepository.saveAndFlush(paymentTypes);

        // Get all the paymentTypesList where paymentTypesChargePer is less than DEFAULT_PAYMENT_TYPES_CHARGE_PER
        defaultPaymentTypesShouldNotBeFound("paymentTypesChargePer.lessThan=" + DEFAULT_PAYMENT_TYPES_CHARGE_PER);

        // Get all the paymentTypesList where paymentTypesChargePer is less than UPDATED_PAYMENT_TYPES_CHARGE_PER
        defaultPaymentTypesShouldBeFound("paymentTypesChargePer.lessThan=" + UPDATED_PAYMENT_TYPES_CHARGE_PER);
    }

    @Test
    @Transactional
    public void getAllPaymentTypesByPaymentTypesChargePerIsGreaterThanSomething() throws Exception {
        // Initialize the database
        paymentTypesRepository.saveAndFlush(paymentTypes);

        // Get all the paymentTypesList where paymentTypesChargePer is greater than DEFAULT_PAYMENT_TYPES_CHARGE_PER
        defaultPaymentTypesShouldNotBeFound("paymentTypesChargePer.greaterThan=" + DEFAULT_PAYMENT_TYPES_CHARGE_PER);

        // Get all the paymentTypesList where paymentTypesChargePer is greater than SMALLER_PAYMENT_TYPES_CHARGE_PER
        defaultPaymentTypesShouldBeFound("paymentTypesChargePer.greaterThan=" + SMALLER_PAYMENT_TYPES_CHARGE_PER);
    }


    @Test
    @Transactional
    public void getAllPaymentTypesByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentTypesRepository.saveAndFlush(paymentTypes);

        // Get all the paymentTypesList where isActive equals to DEFAULT_IS_ACTIVE
        defaultPaymentTypesShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the paymentTypesList where isActive equals to UPDATED_IS_ACTIVE
        defaultPaymentTypesShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllPaymentTypesByIsActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentTypesRepository.saveAndFlush(paymentTypes);

        // Get all the paymentTypesList where isActive not equals to DEFAULT_IS_ACTIVE
        defaultPaymentTypesShouldNotBeFound("isActive.notEquals=" + DEFAULT_IS_ACTIVE);

        // Get all the paymentTypesList where isActive not equals to UPDATED_IS_ACTIVE
        defaultPaymentTypesShouldBeFound("isActive.notEquals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllPaymentTypesByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        paymentTypesRepository.saveAndFlush(paymentTypes);

        // Get all the paymentTypesList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultPaymentTypesShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the paymentTypesList where isActive equals to UPDATED_IS_ACTIVE
        defaultPaymentTypesShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllPaymentTypesByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentTypesRepository.saveAndFlush(paymentTypes);

        // Get all the paymentTypesList where isActive is not null
        defaultPaymentTypesShouldBeFound("isActive.specified=true");

        // Get all the paymentTypesList where isActive is null
        defaultPaymentTypesShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    public void getAllPaymentTypesByLocationIsEqualToSomething() throws Exception {
        // Get already existing entity
        Location location = paymentTypes.getLocation();
        paymentTypesRepository.saveAndFlush(paymentTypes);
        Long locationId = location.getId();

        // Get all the paymentTypesList where location equals to locationId
        defaultPaymentTypesShouldBeFound("locationId.equals=" + locationId);

        // Get all the paymentTypesList where location equals to locationId + 1
        defaultPaymentTypesShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }


    @Test
    @Transactional
    public void getAllPaymentTypesByInvicePayIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentTypesRepository.saveAndFlush(paymentTypes);
        Invoice invicePay = InvoiceResourceIT.createEntity(em);
        em.persist(invicePay);
        em.flush();
        paymentTypes.setInvicePay(invicePay);
        paymentTypesRepository.saveAndFlush(paymentTypes);
        Long invicePayId = invicePay.getId();

        // Get all the paymentTypesList where invicePay equals to invicePayId
        defaultPaymentTypesShouldBeFound("invicePayId.equals=" + invicePayId);

        // Get all the paymentTypesList where invicePay equals to invicePayId + 1
        defaultPaymentTypesShouldNotBeFound("invicePayId.equals=" + (invicePayId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPaymentTypesShouldBeFound(String filter) throws Exception {
        restPaymentTypesMockMvc.perform(get("/api/payment-types?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentTypes.getId().intValue())))
            .andExpect(jsonPath("$.[*].paymentTypesCode").value(hasItem(DEFAULT_PAYMENT_TYPES_CODE)))
            .andExpect(jsonPath("$.[*].paymentTypes").value(hasItem(DEFAULT_PAYMENT_TYPES)))
            .andExpect(jsonPath("$.[*].paymentTypesChargePer").value(hasItem(DEFAULT_PAYMENT_TYPES_CHARGE_PER.doubleValue())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restPaymentTypesMockMvc.perform(get("/api/payment-types/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPaymentTypesShouldNotBeFound(String filter) throws Exception {
        restPaymentTypesMockMvc.perform(get("/api/payment-types?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPaymentTypesMockMvc.perform(get("/api/payment-types/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingPaymentTypes() throws Exception {
        // Get the paymentTypes
        restPaymentTypesMockMvc.perform(get("/api/payment-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePaymentTypes() throws Exception {
        // Initialize the database
        paymentTypesService.save(paymentTypes);

        int databaseSizeBeforeUpdate = paymentTypesRepository.findAll().size();

        // Update the paymentTypes
        PaymentTypes updatedPaymentTypes = paymentTypesRepository.findById(paymentTypes.getId()).get();
        // Disconnect from session so that the updates on updatedPaymentTypes are not directly saved in db
        em.detach(updatedPaymentTypes);
        updatedPaymentTypes
            .paymentTypesCode(UPDATED_PAYMENT_TYPES_CODE)
            .paymentTypes(UPDATED_PAYMENT_TYPES)
            .paymentTypesChargePer(UPDATED_PAYMENT_TYPES_CHARGE_PER)
            .isActive(UPDATED_IS_ACTIVE);

        restPaymentTypesMockMvc.perform(put("/api/payment-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPaymentTypes)))
            .andExpect(status().isOk());

        // Validate the PaymentTypes in the database
        List<PaymentTypes> paymentTypesList = paymentTypesRepository.findAll();
        assertThat(paymentTypesList).hasSize(databaseSizeBeforeUpdate);
        PaymentTypes testPaymentTypes = paymentTypesList.get(paymentTypesList.size() - 1);
        assertThat(testPaymentTypes.getPaymentTypesCode()).isEqualTo(UPDATED_PAYMENT_TYPES_CODE);
        assertThat(testPaymentTypes.getPaymentTypes()).isEqualTo(UPDATED_PAYMENT_TYPES);
        assertThat(testPaymentTypes.getPaymentTypesChargePer()).isEqualTo(UPDATED_PAYMENT_TYPES_CHARGE_PER);
        assertThat(testPaymentTypes.isIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void updateNonExistingPaymentTypes() throws Exception {
        int databaseSizeBeforeUpdate = paymentTypesRepository.findAll().size();

        // Create the PaymentTypes

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentTypesMockMvc.perform(put("/api/payment-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentTypes)))
            .andExpect(status().isBadRequest());

        // Validate the PaymentTypes in the database
        List<PaymentTypes> paymentTypesList = paymentTypesRepository.findAll();
        assertThat(paymentTypesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePaymentTypes() throws Exception {
        // Initialize the database
        paymentTypesService.save(paymentTypes);

        int databaseSizeBeforeDelete = paymentTypesRepository.findAll().size();

        // Delete the paymentTypes
        restPaymentTypesMockMvc.perform(delete("/api/payment-types/{id}", paymentTypes.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PaymentTypes> paymentTypesList = paymentTypesRepository.findAll();
        assertThat(paymentTypesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

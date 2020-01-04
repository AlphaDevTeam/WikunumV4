package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.WikunumApp;
import com.alphadevs.sales.domain.Customer;
import com.alphadevs.sales.domain.Location;
import com.alphadevs.sales.repository.CustomerRepository;
import com.alphadevs.sales.service.CustomerService;
import com.alphadevs.sales.web.rest.errors.ExceptionTranslator;
import com.alphadevs.sales.service.dto.CustomerCriteria;
import com.alphadevs.sales.service.CustomerQueryService;

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
import org.springframework.util.Base64Utils;
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
 * Integration tests for the {@link CustomerResource} REST controller.
 */
@SpringBootTest(classes = WikunumApp.class)
public class CustomerResourceIT {

    private static final String DEFAULT_CUSTOMER_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CUSTOMER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_NAME = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_CUSTOMER_CREDIT_LIMIT = new BigDecimal(1);
    private static final BigDecimal UPDATED_CUSTOMER_CREDIT_LIMIT = new BigDecimal(2);
    private static final BigDecimal SMALLER_CUSTOMER_CREDIT_LIMIT = new BigDecimal(1 - 1);

    private static final String DEFAULT_EMAIL = "8LRX@g.U!";
    private static final String UPDATED_EMAIL = "Hz{~cx@H~<**+.\"D&iH%";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Double DEFAULT_RATING = 1D;
    private static final Double UPDATED_RATING = 2D;
    private static final Double SMALLER_RATING = 1D - 1D;

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_LINE_1 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_LINE_1 = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_LINE_2 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_LINE_2 = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerQueryService customerQueryService;

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

    private MockMvc restCustomerMockMvc;

    private Customer customer;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CustomerResource customerResource = new CustomerResource(customerService, customerQueryService);
        this.restCustomerMockMvc = MockMvcBuilders.standaloneSetup(customerResource)
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
    public static Customer createEntity(EntityManager em) {
        Customer customer = new Customer()
            .customerCode(DEFAULT_CUSTOMER_CODE)
            .customerName(DEFAULT_CUSTOMER_NAME)
            .customerCreditLimit(DEFAULT_CUSTOMER_CREDIT_LIMIT)
            .email(DEFAULT_EMAIL)
            .isActive(DEFAULT_IS_ACTIVE)
            .rating(DEFAULT_RATING)
            .phone(DEFAULT_PHONE)
            .addressLine1(DEFAULT_ADDRESS_LINE_1)
            .addressLine2(DEFAULT_ADDRESS_LINE_2)
            .city(DEFAULT_CITY)
            .country(DEFAULT_COUNTRY)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE);
        // Add required entity
        Location location;
        if (TestUtil.findAll(em, Location.class).isEmpty()) {
            location = LocationResourceIT.createEntity(em);
            em.persist(location);
            em.flush();
        } else {
            location = TestUtil.findAll(em, Location.class).get(0);
        }
        customer.setLocation(location);
        return customer;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Customer createUpdatedEntity(EntityManager em) {
        Customer customer = new Customer()
            .customerCode(UPDATED_CUSTOMER_CODE)
            .customerName(UPDATED_CUSTOMER_NAME)
            .customerCreditLimit(UPDATED_CUSTOMER_CREDIT_LIMIT)
            .email(UPDATED_EMAIL)
            .isActive(UPDATED_IS_ACTIVE)
            .rating(UPDATED_RATING)
            .phone(UPDATED_PHONE)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        // Add required entity
        Location location;
        if (TestUtil.findAll(em, Location.class).isEmpty()) {
            location = LocationResourceIT.createUpdatedEntity(em);
            em.persist(location);
            em.flush();
        } else {
            location = TestUtil.findAll(em, Location.class).get(0);
        }
        customer.setLocation(location);
        return customer;
    }

    @BeforeEach
    public void initTest() {
        customer = createEntity(em);
    }

    @Test
    @Transactional
    public void createCustomer() throws Exception {
        int databaseSizeBeforeCreate = customerRepository.findAll().size();

        // Create the Customer
        restCustomerMockMvc.perform(post("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customer)))
            .andExpect(status().isCreated());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeCreate + 1);
        Customer testCustomer = customerList.get(customerList.size() - 1);
        assertThat(testCustomer.getCustomerCode()).isEqualTo(DEFAULT_CUSTOMER_CODE);
        assertThat(testCustomer.getCustomerName()).isEqualTo(DEFAULT_CUSTOMER_NAME);
        assertThat(testCustomer.getCustomerCreditLimit()).isEqualTo(DEFAULT_CUSTOMER_CREDIT_LIMIT);
        assertThat(testCustomer.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testCustomer.isIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testCustomer.getRating()).isEqualTo(DEFAULT_RATING);
        assertThat(testCustomer.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testCustomer.getAddressLine1()).isEqualTo(DEFAULT_ADDRESS_LINE_1);
        assertThat(testCustomer.getAddressLine2()).isEqualTo(DEFAULT_ADDRESS_LINE_2);
        assertThat(testCustomer.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testCustomer.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testCustomer.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testCustomer.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createCustomerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = customerRepository.findAll().size();

        // Create the Customer with an existing ID
        customer.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerMockMvc.perform(post("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customer)))
            .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCustomerCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().size();
        // set the field null
        customer.setCustomerCode(null);

        // Create the Customer, which fails.

        restCustomerMockMvc.perform(post("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customer)))
            .andExpect(status().isBadRequest());

        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCustomerNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().size();
        // set the field null
        customer.setCustomerName(null);

        // Create the Customer, which fails.

        restCustomerMockMvc.perform(post("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customer)))
            .andExpect(status().isBadRequest());

        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCustomerCreditLimitIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().size();
        // set the field null
        customer.setCustomerCreditLimit(null);

        // Create the Customer, which fails.

        restCustomerMockMvc.perform(post("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customer)))
            .andExpect(status().isBadRequest());

        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().size();
        // set the field null
        customer.setEmail(null);

        // Create the Customer, which fails.

        restCustomerMockMvc.perform(post("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customer)))
            .andExpect(status().isBadRequest());

        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCustomers() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList
        restCustomerMockMvc.perform(get("/api/customers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customer.getId().intValue())))
            .andExpect(jsonPath("$.[*].customerCode").value(hasItem(DEFAULT_CUSTOMER_CODE)))
            .andExpect(jsonPath("$.[*].customerName").value(hasItem(DEFAULT_CUSTOMER_NAME)))
            .andExpect(jsonPath("$.[*].customerCreditLimit").value(hasItem(DEFAULT_CUSTOMER_CREDIT_LIMIT.intValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING.doubleValue())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].addressLine1").value(hasItem(DEFAULT_ADDRESS_LINE_1)))
            .andExpect(jsonPath("$.[*].addressLine2").value(hasItem(DEFAULT_ADDRESS_LINE_2)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));
    }
    
    @Test
    @Transactional
    public void getCustomer() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get the customer
        restCustomerMockMvc.perform(get("/api/customers/{id}", customer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(customer.getId().intValue()))
            .andExpect(jsonPath("$.customerCode").value(DEFAULT_CUSTOMER_CODE))
            .andExpect(jsonPath("$.customerName").value(DEFAULT_CUSTOMER_NAME))
            .andExpect(jsonPath("$.customerCreditLimit").value(DEFAULT_CUSTOMER_CREDIT_LIMIT.intValue()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING.doubleValue()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.addressLine1").value(DEFAULT_ADDRESS_LINE_1))
            .andExpect(jsonPath("$.addressLine2").value(DEFAULT_ADDRESS_LINE_2))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)));
    }


    @Test
    @Transactional
    public void getCustomersByIdFiltering() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        Long id = customer.getId();

        defaultCustomerShouldBeFound("id.equals=" + id);
        defaultCustomerShouldNotBeFound("id.notEquals=" + id);

        defaultCustomerShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCustomerShouldNotBeFound("id.greaterThan=" + id);

        defaultCustomerShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCustomerShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllCustomersByCustomerCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where customerCode equals to DEFAULT_CUSTOMER_CODE
        defaultCustomerShouldBeFound("customerCode.equals=" + DEFAULT_CUSTOMER_CODE);

        // Get all the customerList where customerCode equals to UPDATED_CUSTOMER_CODE
        defaultCustomerShouldNotBeFound("customerCode.equals=" + UPDATED_CUSTOMER_CODE);
    }

    @Test
    @Transactional
    public void getAllCustomersByCustomerCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where customerCode not equals to DEFAULT_CUSTOMER_CODE
        defaultCustomerShouldNotBeFound("customerCode.notEquals=" + DEFAULT_CUSTOMER_CODE);

        // Get all the customerList where customerCode not equals to UPDATED_CUSTOMER_CODE
        defaultCustomerShouldBeFound("customerCode.notEquals=" + UPDATED_CUSTOMER_CODE);
    }

    @Test
    @Transactional
    public void getAllCustomersByCustomerCodeIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where customerCode in DEFAULT_CUSTOMER_CODE or UPDATED_CUSTOMER_CODE
        defaultCustomerShouldBeFound("customerCode.in=" + DEFAULT_CUSTOMER_CODE + "," + UPDATED_CUSTOMER_CODE);

        // Get all the customerList where customerCode equals to UPDATED_CUSTOMER_CODE
        defaultCustomerShouldNotBeFound("customerCode.in=" + UPDATED_CUSTOMER_CODE);
    }

    @Test
    @Transactional
    public void getAllCustomersByCustomerCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where customerCode is not null
        defaultCustomerShouldBeFound("customerCode.specified=true");

        // Get all the customerList where customerCode is null
        defaultCustomerShouldNotBeFound("customerCode.specified=false");
    }
                @Test
    @Transactional
    public void getAllCustomersByCustomerCodeContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where customerCode contains DEFAULT_CUSTOMER_CODE
        defaultCustomerShouldBeFound("customerCode.contains=" + DEFAULT_CUSTOMER_CODE);

        // Get all the customerList where customerCode contains UPDATED_CUSTOMER_CODE
        defaultCustomerShouldNotBeFound("customerCode.contains=" + UPDATED_CUSTOMER_CODE);
    }

    @Test
    @Transactional
    public void getAllCustomersByCustomerCodeNotContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where customerCode does not contain DEFAULT_CUSTOMER_CODE
        defaultCustomerShouldNotBeFound("customerCode.doesNotContain=" + DEFAULT_CUSTOMER_CODE);

        // Get all the customerList where customerCode does not contain UPDATED_CUSTOMER_CODE
        defaultCustomerShouldBeFound("customerCode.doesNotContain=" + UPDATED_CUSTOMER_CODE);
    }


    @Test
    @Transactional
    public void getAllCustomersByCustomerNameIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where customerName equals to DEFAULT_CUSTOMER_NAME
        defaultCustomerShouldBeFound("customerName.equals=" + DEFAULT_CUSTOMER_NAME);

        // Get all the customerList where customerName equals to UPDATED_CUSTOMER_NAME
        defaultCustomerShouldNotBeFound("customerName.equals=" + UPDATED_CUSTOMER_NAME);
    }

    @Test
    @Transactional
    public void getAllCustomersByCustomerNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where customerName not equals to DEFAULT_CUSTOMER_NAME
        defaultCustomerShouldNotBeFound("customerName.notEquals=" + DEFAULT_CUSTOMER_NAME);

        // Get all the customerList where customerName not equals to UPDATED_CUSTOMER_NAME
        defaultCustomerShouldBeFound("customerName.notEquals=" + UPDATED_CUSTOMER_NAME);
    }

    @Test
    @Transactional
    public void getAllCustomersByCustomerNameIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where customerName in DEFAULT_CUSTOMER_NAME or UPDATED_CUSTOMER_NAME
        defaultCustomerShouldBeFound("customerName.in=" + DEFAULT_CUSTOMER_NAME + "," + UPDATED_CUSTOMER_NAME);

        // Get all the customerList where customerName equals to UPDATED_CUSTOMER_NAME
        defaultCustomerShouldNotBeFound("customerName.in=" + UPDATED_CUSTOMER_NAME);
    }

    @Test
    @Transactional
    public void getAllCustomersByCustomerNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where customerName is not null
        defaultCustomerShouldBeFound("customerName.specified=true");

        // Get all the customerList where customerName is null
        defaultCustomerShouldNotBeFound("customerName.specified=false");
    }
                @Test
    @Transactional
    public void getAllCustomersByCustomerNameContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where customerName contains DEFAULT_CUSTOMER_NAME
        defaultCustomerShouldBeFound("customerName.contains=" + DEFAULT_CUSTOMER_NAME);

        // Get all the customerList where customerName contains UPDATED_CUSTOMER_NAME
        defaultCustomerShouldNotBeFound("customerName.contains=" + UPDATED_CUSTOMER_NAME);
    }

    @Test
    @Transactional
    public void getAllCustomersByCustomerNameNotContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where customerName does not contain DEFAULT_CUSTOMER_NAME
        defaultCustomerShouldNotBeFound("customerName.doesNotContain=" + DEFAULT_CUSTOMER_NAME);

        // Get all the customerList where customerName does not contain UPDATED_CUSTOMER_NAME
        defaultCustomerShouldBeFound("customerName.doesNotContain=" + UPDATED_CUSTOMER_NAME);
    }


    @Test
    @Transactional
    public void getAllCustomersByCustomerCreditLimitIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where customerCreditLimit equals to DEFAULT_CUSTOMER_CREDIT_LIMIT
        defaultCustomerShouldBeFound("customerCreditLimit.equals=" + DEFAULT_CUSTOMER_CREDIT_LIMIT);

        // Get all the customerList where customerCreditLimit equals to UPDATED_CUSTOMER_CREDIT_LIMIT
        defaultCustomerShouldNotBeFound("customerCreditLimit.equals=" + UPDATED_CUSTOMER_CREDIT_LIMIT);
    }

    @Test
    @Transactional
    public void getAllCustomersByCustomerCreditLimitIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where customerCreditLimit not equals to DEFAULT_CUSTOMER_CREDIT_LIMIT
        defaultCustomerShouldNotBeFound("customerCreditLimit.notEquals=" + DEFAULT_CUSTOMER_CREDIT_LIMIT);

        // Get all the customerList where customerCreditLimit not equals to UPDATED_CUSTOMER_CREDIT_LIMIT
        defaultCustomerShouldBeFound("customerCreditLimit.notEquals=" + UPDATED_CUSTOMER_CREDIT_LIMIT);
    }

    @Test
    @Transactional
    public void getAllCustomersByCustomerCreditLimitIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where customerCreditLimit in DEFAULT_CUSTOMER_CREDIT_LIMIT or UPDATED_CUSTOMER_CREDIT_LIMIT
        defaultCustomerShouldBeFound("customerCreditLimit.in=" + DEFAULT_CUSTOMER_CREDIT_LIMIT + "," + UPDATED_CUSTOMER_CREDIT_LIMIT);

        // Get all the customerList where customerCreditLimit equals to UPDATED_CUSTOMER_CREDIT_LIMIT
        defaultCustomerShouldNotBeFound("customerCreditLimit.in=" + UPDATED_CUSTOMER_CREDIT_LIMIT);
    }

    @Test
    @Transactional
    public void getAllCustomersByCustomerCreditLimitIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where customerCreditLimit is not null
        defaultCustomerShouldBeFound("customerCreditLimit.specified=true");

        // Get all the customerList where customerCreditLimit is null
        defaultCustomerShouldNotBeFound("customerCreditLimit.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersByCustomerCreditLimitIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where customerCreditLimit is greater than or equal to DEFAULT_CUSTOMER_CREDIT_LIMIT
        defaultCustomerShouldBeFound("customerCreditLimit.greaterThanOrEqual=" + DEFAULT_CUSTOMER_CREDIT_LIMIT);

        // Get all the customerList where customerCreditLimit is greater than or equal to UPDATED_CUSTOMER_CREDIT_LIMIT
        defaultCustomerShouldNotBeFound("customerCreditLimit.greaterThanOrEqual=" + UPDATED_CUSTOMER_CREDIT_LIMIT);
    }

    @Test
    @Transactional
    public void getAllCustomersByCustomerCreditLimitIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where customerCreditLimit is less than or equal to DEFAULT_CUSTOMER_CREDIT_LIMIT
        defaultCustomerShouldBeFound("customerCreditLimit.lessThanOrEqual=" + DEFAULT_CUSTOMER_CREDIT_LIMIT);

        // Get all the customerList where customerCreditLimit is less than or equal to SMALLER_CUSTOMER_CREDIT_LIMIT
        defaultCustomerShouldNotBeFound("customerCreditLimit.lessThanOrEqual=" + SMALLER_CUSTOMER_CREDIT_LIMIT);
    }

    @Test
    @Transactional
    public void getAllCustomersByCustomerCreditLimitIsLessThanSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where customerCreditLimit is less than DEFAULT_CUSTOMER_CREDIT_LIMIT
        defaultCustomerShouldNotBeFound("customerCreditLimit.lessThan=" + DEFAULT_CUSTOMER_CREDIT_LIMIT);

        // Get all the customerList where customerCreditLimit is less than UPDATED_CUSTOMER_CREDIT_LIMIT
        defaultCustomerShouldBeFound("customerCreditLimit.lessThan=" + UPDATED_CUSTOMER_CREDIT_LIMIT);
    }

    @Test
    @Transactional
    public void getAllCustomersByCustomerCreditLimitIsGreaterThanSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where customerCreditLimit is greater than DEFAULT_CUSTOMER_CREDIT_LIMIT
        defaultCustomerShouldNotBeFound("customerCreditLimit.greaterThan=" + DEFAULT_CUSTOMER_CREDIT_LIMIT);

        // Get all the customerList where customerCreditLimit is greater than SMALLER_CUSTOMER_CREDIT_LIMIT
        defaultCustomerShouldBeFound("customerCreditLimit.greaterThan=" + SMALLER_CUSTOMER_CREDIT_LIMIT);
    }


    @Test
    @Transactional
    public void getAllCustomersByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where email equals to DEFAULT_EMAIL
        defaultCustomerShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the customerList where email equals to UPDATED_EMAIL
        defaultCustomerShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllCustomersByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where email not equals to DEFAULT_EMAIL
        defaultCustomerShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the customerList where email not equals to UPDATED_EMAIL
        defaultCustomerShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllCustomersByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultCustomerShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the customerList where email equals to UPDATED_EMAIL
        defaultCustomerShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllCustomersByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where email is not null
        defaultCustomerShouldBeFound("email.specified=true");

        // Get all the customerList where email is null
        defaultCustomerShouldNotBeFound("email.specified=false");
    }
                @Test
    @Transactional
    public void getAllCustomersByEmailContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where email contains DEFAULT_EMAIL
        defaultCustomerShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the customerList where email contains UPDATED_EMAIL
        defaultCustomerShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllCustomersByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where email does not contain DEFAULT_EMAIL
        defaultCustomerShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the customerList where email does not contain UPDATED_EMAIL
        defaultCustomerShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }


    @Test
    @Transactional
    public void getAllCustomersByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where isActive equals to DEFAULT_IS_ACTIVE
        defaultCustomerShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the customerList where isActive equals to UPDATED_IS_ACTIVE
        defaultCustomerShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllCustomersByIsActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where isActive not equals to DEFAULT_IS_ACTIVE
        defaultCustomerShouldNotBeFound("isActive.notEquals=" + DEFAULT_IS_ACTIVE);

        // Get all the customerList where isActive not equals to UPDATED_IS_ACTIVE
        defaultCustomerShouldBeFound("isActive.notEquals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllCustomersByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultCustomerShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the customerList where isActive equals to UPDATED_IS_ACTIVE
        defaultCustomerShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllCustomersByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where isActive is not null
        defaultCustomerShouldBeFound("isActive.specified=true");

        // Get all the customerList where isActive is null
        defaultCustomerShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersByRatingIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where rating equals to DEFAULT_RATING
        defaultCustomerShouldBeFound("rating.equals=" + DEFAULT_RATING);

        // Get all the customerList where rating equals to UPDATED_RATING
        defaultCustomerShouldNotBeFound("rating.equals=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    public void getAllCustomersByRatingIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where rating not equals to DEFAULT_RATING
        defaultCustomerShouldNotBeFound("rating.notEquals=" + DEFAULT_RATING);

        // Get all the customerList where rating not equals to UPDATED_RATING
        defaultCustomerShouldBeFound("rating.notEquals=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    public void getAllCustomersByRatingIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where rating in DEFAULT_RATING or UPDATED_RATING
        defaultCustomerShouldBeFound("rating.in=" + DEFAULT_RATING + "," + UPDATED_RATING);

        // Get all the customerList where rating equals to UPDATED_RATING
        defaultCustomerShouldNotBeFound("rating.in=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    public void getAllCustomersByRatingIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where rating is not null
        defaultCustomerShouldBeFound("rating.specified=true");

        // Get all the customerList where rating is null
        defaultCustomerShouldNotBeFound("rating.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersByRatingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where rating is greater than or equal to DEFAULT_RATING
        defaultCustomerShouldBeFound("rating.greaterThanOrEqual=" + DEFAULT_RATING);

        // Get all the customerList where rating is greater than or equal to UPDATED_RATING
        defaultCustomerShouldNotBeFound("rating.greaterThanOrEqual=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    public void getAllCustomersByRatingIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where rating is less than or equal to DEFAULT_RATING
        defaultCustomerShouldBeFound("rating.lessThanOrEqual=" + DEFAULT_RATING);

        // Get all the customerList where rating is less than or equal to SMALLER_RATING
        defaultCustomerShouldNotBeFound("rating.lessThanOrEqual=" + SMALLER_RATING);
    }

    @Test
    @Transactional
    public void getAllCustomersByRatingIsLessThanSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where rating is less than DEFAULT_RATING
        defaultCustomerShouldNotBeFound("rating.lessThan=" + DEFAULT_RATING);

        // Get all the customerList where rating is less than UPDATED_RATING
        defaultCustomerShouldBeFound("rating.lessThan=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    public void getAllCustomersByRatingIsGreaterThanSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where rating is greater than DEFAULT_RATING
        defaultCustomerShouldNotBeFound("rating.greaterThan=" + DEFAULT_RATING);

        // Get all the customerList where rating is greater than SMALLER_RATING
        defaultCustomerShouldBeFound("rating.greaterThan=" + SMALLER_RATING);
    }


    @Test
    @Transactional
    public void getAllCustomersByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where phone equals to DEFAULT_PHONE
        defaultCustomerShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the customerList where phone equals to UPDATED_PHONE
        defaultCustomerShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllCustomersByPhoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where phone not equals to DEFAULT_PHONE
        defaultCustomerShouldNotBeFound("phone.notEquals=" + DEFAULT_PHONE);

        // Get all the customerList where phone not equals to UPDATED_PHONE
        defaultCustomerShouldBeFound("phone.notEquals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllCustomersByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultCustomerShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the customerList where phone equals to UPDATED_PHONE
        defaultCustomerShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllCustomersByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where phone is not null
        defaultCustomerShouldBeFound("phone.specified=true");

        // Get all the customerList where phone is null
        defaultCustomerShouldNotBeFound("phone.specified=false");
    }
                @Test
    @Transactional
    public void getAllCustomersByPhoneContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where phone contains DEFAULT_PHONE
        defaultCustomerShouldBeFound("phone.contains=" + DEFAULT_PHONE);

        // Get all the customerList where phone contains UPDATED_PHONE
        defaultCustomerShouldNotBeFound("phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllCustomersByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where phone does not contain DEFAULT_PHONE
        defaultCustomerShouldNotBeFound("phone.doesNotContain=" + DEFAULT_PHONE);

        // Get all the customerList where phone does not contain UPDATED_PHONE
        defaultCustomerShouldBeFound("phone.doesNotContain=" + UPDATED_PHONE);
    }


    @Test
    @Transactional
    public void getAllCustomersByAddressLine1IsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where addressLine1 equals to DEFAULT_ADDRESS_LINE_1
        defaultCustomerShouldBeFound("addressLine1.equals=" + DEFAULT_ADDRESS_LINE_1);

        // Get all the customerList where addressLine1 equals to UPDATED_ADDRESS_LINE_1
        defaultCustomerShouldNotBeFound("addressLine1.equals=" + UPDATED_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    public void getAllCustomersByAddressLine1IsNotEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where addressLine1 not equals to DEFAULT_ADDRESS_LINE_1
        defaultCustomerShouldNotBeFound("addressLine1.notEquals=" + DEFAULT_ADDRESS_LINE_1);

        // Get all the customerList where addressLine1 not equals to UPDATED_ADDRESS_LINE_1
        defaultCustomerShouldBeFound("addressLine1.notEquals=" + UPDATED_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    public void getAllCustomersByAddressLine1IsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where addressLine1 in DEFAULT_ADDRESS_LINE_1 or UPDATED_ADDRESS_LINE_1
        defaultCustomerShouldBeFound("addressLine1.in=" + DEFAULT_ADDRESS_LINE_1 + "," + UPDATED_ADDRESS_LINE_1);

        // Get all the customerList where addressLine1 equals to UPDATED_ADDRESS_LINE_1
        defaultCustomerShouldNotBeFound("addressLine1.in=" + UPDATED_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    public void getAllCustomersByAddressLine1IsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where addressLine1 is not null
        defaultCustomerShouldBeFound("addressLine1.specified=true");

        // Get all the customerList where addressLine1 is null
        defaultCustomerShouldNotBeFound("addressLine1.specified=false");
    }
                @Test
    @Transactional
    public void getAllCustomersByAddressLine1ContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where addressLine1 contains DEFAULT_ADDRESS_LINE_1
        defaultCustomerShouldBeFound("addressLine1.contains=" + DEFAULT_ADDRESS_LINE_1);

        // Get all the customerList where addressLine1 contains UPDATED_ADDRESS_LINE_1
        defaultCustomerShouldNotBeFound("addressLine1.contains=" + UPDATED_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    public void getAllCustomersByAddressLine1NotContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where addressLine1 does not contain DEFAULT_ADDRESS_LINE_1
        defaultCustomerShouldNotBeFound("addressLine1.doesNotContain=" + DEFAULT_ADDRESS_LINE_1);

        // Get all the customerList where addressLine1 does not contain UPDATED_ADDRESS_LINE_1
        defaultCustomerShouldBeFound("addressLine1.doesNotContain=" + UPDATED_ADDRESS_LINE_1);
    }


    @Test
    @Transactional
    public void getAllCustomersByAddressLine2IsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where addressLine2 equals to DEFAULT_ADDRESS_LINE_2
        defaultCustomerShouldBeFound("addressLine2.equals=" + DEFAULT_ADDRESS_LINE_2);

        // Get all the customerList where addressLine2 equals to UPDATED_ADDRESS_LINE_2
        defaultCustomerShouldNotBeFound("addressLine2.equals=" + UPDATED_ADDRESS_LINE_2);
    }

    @Test
    @Transactional
    public void getAllCustomersByAddressLine2IsNotEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where addressLine2 not equals to DEFAULT_ADDRESS_LINE_2
        defaultCustomerShouldNotBeFound("addressLine2.notEquals=" + DEFAULT_ADDRESS_LINE_2);

        // Get all the customerList where addressLine2 not equals to UPDATED_ADDRESS_LINE_2
        defaultCustomerShouldBeFound("addressLine2.notEquals=" + UPDATED_ADDRESS_LINE_2);
    }

    @Test
    @Transactional
    public void getAllCustomersByAddressLine2IsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where addressLine2 in DEFAULT_ADDRESS_LINE_2 or UPDATED_ADDRESS_LINE_2
        defaultCustomerShouldBeFound("addressLine2.in=" + DEFAULT_ADDRESS_LINE_2 + "," + UPDATED_ADDRESS_LINE_2);

        // Get all the customerList where addressLine2 equals to UPDATED_ADDRESS_LINE_2
        defaultCustomerShouldNotBeFound("addressLine2.in=" + UPDATED_ADDRESS_LINE_2);
    }

    @Test
    @Transactional
    public void getAllCustomersByAddressLine2IsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where addressLine2 is not null
        defaultCustomerShouldBeFound("addressLine2.specified=true");

        // Get all the customerList where addressLine2 is null
        defaultCustomerShouldNotBeFound("addressLine2.specified=false");
    }
                @Test
    @Transactional
    public void getAllCustomersByAddressLine2ContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where addressLine2 contains DEFAULT_ADDRESS_LINE_2
        defaultCustomerShouldBeFound("addressLine2.contains=" + DEFAULT_ADDRESS_LINE_2);

        // Get all the customerList where addressLine2 contains UPDATED_ADDRESS_LINE_2
        defaultCustomerShouldNotBeFound("addressLine2.contains=" + UPDATED_ADDRESS_LINE_2);
    }

    @Test
    @Transactional
    public void getAllCustomersByAddressLine2NotContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where addressLine2 does not contain DEFAULT_ADDRESS_LINE_2
        defaultCustomerShouldNotBeFound("addressLine2.doesNotContain=" + DEFAULT_ADDRESS_LINE_2);

        // Get all the customerList where addressLine2 does not contain UPDATED_ADDRESS_LINE_2
        defaultCustomerShouldBeFound("addressLine2.doesNotContain=" + UPDATED_ADDRESS_LINE_2);
    }


    @Test
    @Transactional
    public void getAllCustomersByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where city equals to DEFAULT_CITY
        defaultCustomerShouldBeFound("city.equals=" + DEFAULT_CITY);

        // Get all the customerList where city equals to UPDATED_CITY
        defaultCustomerShouldNotBeFound("city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllCustomersByCityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where city not equals to DEFAULT_CITY
        defaultCustomerShouldNotBeFound("city.notEquals=" + DEFAULT_CITY);

        // Get all the customerList where city not equals to UPDATED_CITY
        defaultCustomerShouldBeFound("city.notEquals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllCustomersByCityIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where city in DEFAULT_CITY or UPDATED_CITY
        defaultCustomerShouldBeFound("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY);

        // Get all the customerList where city equals to UPDATED_CITY
        defaultCustomerShouldNotBeFound("city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllCustomersByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where city is not null
        defaultCustomerShouldBeFound("city.specified=true");

        // Get all the customerList where city is null
        defaultCustomerShouldNotBeFound("city.specified=false");
    }
                @Test
    @Transactional
    public void getAllCustomersByCityContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where city contains DEFAULT_CITY
        defaultCustomerShouldBeFound("city.contains=" + DEFAULT_CITY);

        // Get all the customerList where city contains UPDATED_CITY
        defaultCustomerShouldNotBeFound("city.contains=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllCustomersByCityNotContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where city does not contain DEFAULT_CITY
        defaultCustomerShouldNotBeFound("city.doesNotContain=" + DEFAULT_CITY);

        // Get all the customerList where city does not contain UPDATED_CITY
        defaultCustomerShouldBeFound("city.doesNotContain=" + UPDATED_CITY);
    }


    @Test
    @Transactional
    public void getAllCustomersByCountryIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where country equals to DEFAULT_COUNTRY
        defaultCustomerShouldBeFound("country.equals=" + DEFAULT_COUNTRY);

        // Get all the customerList where country equals to UPDATED_COUNTRY
        defaultCustomerShouldNotBeFound("country.equals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllCustomersByCountryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where country not equals to DEFAULT_COUNTRY
        defaultCustomerShouldNotBeFound("country.notEquals=" + DEFAULT_COUNTRY);

        // Get all the customerList where country not equals to UPDATED_COUNTRY
        defaultCustomerShouldBeFound("country.notEquals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllCustomersByCountryIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where country in DEFAULT_COUNTRY or UPDATED_COUNTRY
        defaultCustomerShouldBeFound("country.in=" + DEFAULT_COUNTRY + "," + UPDATED_COUNTRY);

        // Get all the customerList where country equals to UPDATED_COUNTRY
        defaultCustomerShouldNotBeFound("country.in=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllCustomersByCountryIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where country is not null
        defaultCustomerShouldBeFound("country.specified=true");

        // Get all the customerList where country is null
        defaultCustomerShouldNotBeFound("country.specified=false");
    }
                @Test
    @Transactional
    public void getAllCustomersByCountryContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where country contains DEFAULT_COUNTRY
        defaultCustomerShouldBeFound("country.contains=" + DEFAULT_COUNTRY);

        // Get all the customerList where country contains UPDATED_COUNTRY
        defaultCustomerShouldNotBeFound("country.contains=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllCustomersByCountryNotContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where country does not contain DEFAULT_COUNTRY
        defaultCustomerShouldNotBeFound("country.doesNotContain=" + DEFAULT_COUNTRY);

        // Get all the customerList where country does not contain UPDATED_COUNTRY
        defaultCustomerShouldBeFound("country.doesNotContain=" + UPDATED_COUNTRY);
    }


    @Test
    @Transactional
    public void getAllCustomersByLocationIsEqualToSomething() throws Exception {
        // Get already existing entity
        Location location = customer.getLocation();
        customerRepository.saveAndFlush(customer);
        Long locationId = location.getId();

        // Get all the customerList where location equals to locationId
        defaultCustomerShouldBeFound("locationId.equals=" + locationId);

        // Get all the customerList where location equals to locationId + 1
        defaultCustomerShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCustomerShouldBeFound(String filter) throws Exception {
        restCustomerMockMvc.perform(get("/api/customers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customer.getId().intValue())))
            .andExpect(jsonPath("$.[*].customerCode").value(hasItem(DEFAULT_CUSTOMER_CODE)))
            .andExpect(jsonPath("$.[*].customerName").value(hasItem(DEFAULT_CUSTOMER_NAME)))
            .andExpect(jsonPath("$.[*].customerCreditLimit").value(hasItem(DEFAULT_CUSTOMER_CREDIT_LIMIT.intValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING.doubleValue())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].addressLine1").value(hasItem(DEFAULT_ADDRESS_LINE_1)))
            .andExpect(jsonPath("$.[*].addressLine2").value(hasItem(DEFAULT_ADDRESS_LINE_2)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));

        // Check, that the count call also returns 1
        restCustomerMockMvc.perform(get("/api/customers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCustomerShouldNotBeFound(String filter) throws Exception {
        restCustomerMockMvc.perform(get("/api/customers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCustomerMockMvc.perform(get("/api/customers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingCustomer() throws Exception {
        // Get the customer
        restCustomerMockMvc.perform(get("/api/customers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCustomer() throws Exception {
        // Initialize the database
        customerService.save(customer);

        int databaseSizeBeforeUpdate = customerRepository.findAll().size();

        // Update the customer
        Customer updatedCustomer = customerRepository.findById(customer.getId()).get();
        // Disconnect from session so that the updates on updatedCustomer are not directly saved in db
        em.detach(updatedCustomer);
        updatedCustomer
            .customerCode(UPDATED_CUSTOMER_CODE)
            .customerName(UPDATED_CUSTOMER_NAME)
            .customerCreditLimit(UPDATED_CUSTOMER_CREDIT_LIMIT)
            .email(UPDATED_EMAIL)
            .isActive(UPDATED_IS_ACTIVE)
            .rating(UPDATED_RATING)
            .phone(UPDATED_PHONE)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restCustomerMockMvc.perform(put("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCustomer)))
            .andExpect(status().isOk());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
        Customer testCustomer = customerList.get(customerList.size() - 1);
        assertThat(testCustomer.getCustomerCode()).isEqualTo(UPDATED_CUSTOMER_CODE);
        assertThat(testCustomer.getCustomerName()).isEqualTo(UPDATED_CUSTOMER_NAME);
        assertThat(testCustomer.getCustomerCreditLimit()).isEqualTo(UPDATED_CUSTOMER_CREDIT_LIMIT);
        assertThat(testCustomer.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCustomer.isIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testCustomer.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testCustomer.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testCustomer.getAddressLine1()).isEqualTo(UPDATED_ADDRESS_LINE_1);
        assertThat(testCustomer.getAddressLine2()).isEqualTo(UPDATED_ADDRESS_LINE_2);
        assertThat(testCustomer.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testCustomer.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testCustomer.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testCustomer.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();

        // Create the Customer

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerMockMvc.perform(put("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customer)))
            .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCustomer() throws Exception {
        // Initialize the database
        customerService.save(customer);

        int databaseSizeBeforeDelete = customerRepository.findAll().size();

        // Delete the customer
        restCustomerMockMvc.perform(delete("/api/customers/{id}", customer.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

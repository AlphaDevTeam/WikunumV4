package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.WikunumApp;
import com.alphadevs.sales.domain.Company;
import com.alphadevs.sales.domain.LicenseType;
import com.alphadevs.sales.repository.CompanyRepository;
import com.alphadevs.sales.service.CompanyService;
import com.alphadevs.sales.web.rest.errors.ExceptionTranslator;
import com.alphadevs.sales.service.dto.CompanyCriteria;
import com.alphadevs.sales.service.CompanyQueryService;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.alphadevs.sales.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link CompanyResource} REST controller.
 */
@SpringBootTest(classes = WikunumApp.class)
public class CompanyResourceIT {

    private static final String DEFAULT_COMPANY_CODE = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_COMPANY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COMPANY_REG_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_REG_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "o@ei.5";
    private static final String UPDATED_EMAIL = ";~uv1@0]oQmb.)}^xLV";

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

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String DEFAULT_API_KEY = "AAAAAAAAAA";
    private static final String UPDATED_API_KEY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_EXPIRE_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EXPIRE_ON = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_EXPIRE_ON = LocalDate.ofEpochDay(-1L);

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CompanyQueryService companyQueryService;

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

    private MockMvc restCompanyMockMvc;

    private Company company;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CompanyResource companyResource = new CompanyResource(companyService, companyQueryService);
        this.restCompanyMockMvc = MockMvcBuilders.standaloneSetup(companyResource)
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
    public static Company createEntity(EntityManager em) {
        Company company = new Company()
            .companyCode(DEFAULT_COMPANY_CODE)
            .companyName(DEFAULT_COMPANY_NAME)
            .companyRegNumber(DEFAULT_COMPANY_REG_NUMBER)
            .email(DEFAULT_EMAIL)
            .rating(DEFAULT_RATING)
            .phone(DEFAULT_PHONE)
            .addressLine1(DEFAULT_ADDRESS_LINE_1)
            .addressLine2(DEFAULT_ADDRESS_LINE_2)
            .city(DEFAULT_CITY)
            .country(DEFAULT_COUNTRY)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .isActive(DEFAULT_IS_ACTIVE)
            .apiKey(DEFAULT_API_KEY)
            .expireOn(DEFAULT_EXPIRE_ON);
        // Add required entity
        LicenseType licenseType;
        if (TestUtil.findAll(em, LicenseType.class).isEmpty()) {
            licenseType = LicenseTypeResourceIT.createEntity(em);
            em.persist(licenseType);
            em.flush();
        } else {
            licenseType = TestUtil.findAll(em, LicenseType.class).get(0);
        }
        company.setLicenseType(licenseType);
        return company;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Company createUpdatedEntity(EntityManager em) {
        Company company = new Company()
            .companyCode(UPDATED_COMPANY_CODE)
            .companyName(UPDATED_COMPANY_NAME)
            .companyRegNumber(UPDATED_COMPANY_REG_NUMBER)
            .email(UPDATED_EMAIL)
            .rating(UPDATED_RATING)
            .phone(UPDATED_PHONE)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .isActive(UPDATED_IS_ACTIVE)
            .apiKey(UPDATED_API_KEY)
            .expireOn(UPDATED_EXPIRE_ON);
        // Add required entity
        LicenseType licenseType;
        if (TestUtil.findAll(em, LicenseType.class).isEmpty()) {
            licenseType = LicenseTypeResourceIT.createUpdatedEntity(em);
            em.persist(licenseType);
            em.flush();
        } else {
            licenseType = TestUtil.findAll(em, LicenseType.class).get(0);
        }
        company.setLicenseType(licenseType);
        return company;
    }

    @BeforeEach
    public void initTest() {
        company = createEntity(em);
    }

    @Test
    @Transactional
    public void createCompany() throws Exception {
        int databaseSizeBeforeCreate = companyRepository.findAll().size();

        // Create the Company
        restCompanyMockMvc.perform(post("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(company)))
            .andExpect(status().isCreated());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeCreate + 1);
        Company testCompany = companyList.get(companyList.size() - 1);
        assertThat(testCompany.getCompanyCode()).isEqualTo(DEFAULT_COMPANY_CODE);
        assertThat(testCompany.getCompanyName()).isEqualTo(DEFAULT_COMPANY_NAME);
        assertThat(testCompany.getCompanyRegNumber()).isEqualTo(DEFAULT_COMPANY_REG_NUMBER);
        assertThat(testCompany.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testCompany.getRating()).isEqualTo(DEFAULT_RATING);
        assertThat(testCompany.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testCompany.getAddressLine1()).isEqualTo(DEFAULT_ADDRESS_LINE_1);
        assertThat(testCompany.getAddressLine2()).isEqualTo(DEFAULT_ADDRESS_LINE_2);
        assertThat(testCompany.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testCompany.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testCompany.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testCompany.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testCompany.isIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testCompany.getApiKey()).isEqualTo(DEFAULT_API_KEY);
        assertThat(testCompany.getExpireOn()).isEqualTo(DEFAULT_EXPIRE_ON);
    }

    @Test
    @Transactional
    public void createCompanyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = companyRepository.findAll().size();

        // Create the Company with an existing ID
        company.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompanyMockMvc.perform(post("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(company)))
            .andExpect(status().isBadRequest());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCompanyCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyRepository.findAll().size();
        // set the field null
        company.setCompanyCode(null);

        // Create the Company, which fails.

        restCompanyMockMvc.perform(post("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(company)))
            .andExpect(status().isBadRequest());

        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCompanyNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyRepository.findAll().size();
        // set the field null
        company.setCompanyName(null);

        // Create the Company, which fails.

        restCompanyMockMvc.perform(post("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(company)))
            .andExpect(status().isBadRequest());

        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCompanyRegNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyRepository.findAll().size();
        // set the field null
        company.setCompanyRegNumber(null);

        // Create the Company, which fails.

        restCompanyMockMvc.perform(post("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(company)))
            .andExpect(status().isBadRequest());

        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyRepository.findAll().size();
        // set the field null
        company.setEmail(null);

        // Create the Company, which fails.

        restCompanyMockMvc.perform(post("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(company)))
            .andExpect(status().isBadRequest());

        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCompanies() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList
        restCompanyMockMvc.perform(get("/api/companies?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(company.getId().intValue())))
            .andExpect(jsonPath("$.[*].companyCode").value(hasItem(DEFAULT_COMPANY_CODE)))
            .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME)))
            .andExpect(jsonPath("$.[*].companyRegNumber").value(hasItem(DEFAULT_COMPANY_REG_NUMBER)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING.doubleValue())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].addressLine1").value(hasItem(DEFAULT_ADDRESS_LINE_1)))
            .andExpect(jsonPath("$.[*].addressLine2").value(hasItem(DEFAULT_ADDRESS_LINE_2)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].apiKey").value(hasItem(DEFAULT_API_KEY)))
            .andExpect(jsonPath("$.[*].expireOn").value(hasItem(DEFAULT_EXPIRE_ON.toString())));
    }
    
    @Test
    @Transactional
    public void getCompany() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get the company
        restCompanyMockMvc.perform(get("/api/companies/{id}", company.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(company.getId().intValue()))
            .andExpect(jsonPath("$.companyCode").value(DEFAULT_COMPANY_CODE))
            .andExpect(jsonPath("$.companyName").value(DEFAULT_COMPANY_NAME))
            .andExpect(jsonPath("$.companyRegNumber").value(DEFAULT_COMPANY_REG_NUMBER))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING.doubleValue()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.addressLine1").value(DEFAULT_ADDRESS_LINE_1))
            .andExpect(jsonPath("$.addressLine2").value(DEFAULT_ADDRESS_LINE_2))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.apiKey").value(DEFAULT_API_KEY))
            .andExpect(jsonPath("$.expireOn").value(DEFAULT_EXPIRE_ON.toString()));
    }


    @Test
    @Transactional
    public void getCompaniesByIdFiltering() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        Long id = company.getId();

        defaultCompanyShouldBeFound("id.equals=" + id);
        defaultCompanyShouldNotBeFound("id.notEquals=" + id);

        defaultCompanyShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCompanyShouldNotBeFound("id.greaterThan=" + id);

        defaultCompanyShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCompanyShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllCompaniesByCompanyCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where companyCode equals to DEFAULT_COMPANY_CODE
        defaultCompanyShouldBeFound("companyCode.equals=" + DEFAULT_COMPANY_CODE);

        // Get all the companyList where companyCode equals to UPDATED_COMPANY_CODE
        defaultCompanyShouldNotBeFound("companyCode.equals=" + UPDATED_COMPANY_CODE);
    }

    @Test
    @Transactional
    public void getAllCompaniesByCompanyCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where companyCode not equals to DEFAULT_COMPANY_CODE
        defaultCompanyShouldNotBeFound("companyCode.notEquals=" + DEFAULT_COMPANY_CODE);

        // Get all the companyList where companyCode not equals to UPDATED_COMPANY_CODE
        defaultCompanyShouldBeFound("companyCode.notEquals=" + UPDATED_COMPANY_CODE);
    }

    @Test
    @Transactional
    public void getAllCompaniesByCompanyCodeIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where companyCode in DEFAULT_COMPANY_CODE or UPDATED_COMPANY_CODE
        defaultCompanyShouldBeFound("companyCode.in=" + DEFAULT_COMPANY_CODE + "," + UPDATED_COMPANY_CODE);

        // Get all the companyList where companyCode equals to UPDATED_COMPANY_CODE
        defaultCompanyShouldNotBeFound("companyCode.in=" + UPDATED_COMPANY_CODE);
    }

    @Test
    @Transactional
    public void getAllCompaniesByCompanyCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where companyCode is not null
        defaultCompanyShouldBeFound("companyCode.specified=true");

        // Get all the companyList where companyCode is null
        defaultCompanyShouldNotBeFound("companyCode.specified=false");
    }
                @Test
    @Transactional
    public void getAllCompaniesByCompanyCodeContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where companyCode contains DEFAULT_COMPANY_CODE
        defaultCompanyShouldBeFound("companyCode.contains=" + DEFAULT_COMPANY_CODE);

        // Get all the companyList where companyCode contains UPDATED_COMPANY_CODE
        defaultCompanyShouldNotBeFound("companyCode.contains=" + UPDATED_COMPANY_CODE);
    }

    @Test
    @Transactional
    public void getAllCompaniesByCompanyCodeNotContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where companyCode does not contain DEFAULT_COMPANY_CODE
        defaultCompanyShouldNotBeFound("companyCode.doesNotContain=" + DEFAULT_COMPANY_CODE);

        // Get all the companyList where companyCode does not contain UPDATED_COMPANY_CODE
        defaultCompanyShouldBeFound("companyCode.doesNotContain=" + UPDATED_COMPANY_CODE);
    }


    @Test
    @Transactional
    public void getAllCompaniesByCompanyNameIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where companyName equals to DEFAULT_COMPANY_NAME
        defaultCompanyShouldBeFound("companyName.equals=" + DEFAULT_COMPANY_NAME);

        // Get all the companyList where companyName equals to UPDATED_COMPANY_NAME
        defaultCompanyShouldNotBeFound("companyName.equals=" + UPDATED_COMPANY_NAME);
    }

    @Test
    @Transactional
    public void getAllCompaniesByCompanyNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where companyName not equals to DEFAULT_COMPANY_NAME
        defaultCompanyShouldNotBeFound("companyName.notEquals=" + DEFAULT_COMPANY_NAME);

        // Get all the companyList where companyName not equals to UPDATED_COMPANY_NAME
        defaultCompanyShouldBeFound("companyName.notEquals=" + UPDATED_COMPANY_NAME);
    }

    @Test
    @Transactional
    public void getAllCompaniesByCompanyNameIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where companyName in DEFAULT_COMPANY_NAME or UPDATED_COMPANY_NAME
        defaultCompanyShouldBeFound("companyName.in=" + DEFAULT_COMPANY_NAME + "," + UPDATED_COMPANY_NAME);

        // Get all the companyList where companyName equals to UPDATED_COMPANY_NAME
        defaultCompanyShouldNotBeFound("companyName.in=" + UPDATED_COMPANY_NAME);
    }

    @Test
    @Transactional
    public void getAllCompaniesByCompanyNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where companyName is not null
        defaultCompanyShouldBeFound("companyName.specified=true");

        // Get all the companyList where companyName is null
        defaultCompanyShouldNotBeFound("companyName.specified=false");
    }
                @Test
    @Transactional
    public void getAllCompaniesByCompanyNameContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where companyName contains DEFAULT_COMPANY_NAME
        defaultCompanyShouldBeFound("companyName.contains=" + DEFAULT_COMPANY_NAME);

        // Get all the companyList where companyName contains UPDATED_COMPANY_NAME
        defaultCompanyShouldNotBeFound("companyName.contains=" + UPDATED_COMPANY_NAME);
    }

    @Test
    @Transactional
    public void getAllCompaniesByCompanyNameNotContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where companyName does not contain DEFAULT_COMPANY_NAME
        defaultCompanyShouldNotBeFound("companyName.doesNotContain=" + DEFAULT_COMPANY_NAME);

        // Get all the companyList where companyName does not contain UPDATED_COMPANY_NAME
        defaultCompanyShouldBeFound("companyName.doesNotContain=" + UPDATED_COMPANY_NAME);
    }


    @Test
    @Transactional
    public void getAllCompaniesByCompanyRegNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where companyRegNumber equals to DEFAULT_COMPANY_REG_NUMBER
        defaultCompanyShouldBeFound("companyRegNumber.equals=" + DEFAULT_COMPANY_REG_NUMBER);

        // Get all the companyList where companyRegNumber equals to UPDATED_COMPANY_REG_NUMBER
        defaultCompanyShouldNotBeFound("companyRegNumber.equals=" + UPDATED_COMPANY_REG_NUMBER);
    }

    @Test
    @Transactional
    public void getAllCompaniesByCompanyRegNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where companyRegNumber not equals to DEFAULT_COMPANY_REG_NUMBER
        defaultCompanyShouldNotBeFound("companyRegNumber.notEquals=" + DEFAULT_COMPANY_REG_NUMBER);

        // Get all the companyList where companyRegNumber not equals to UPDATED_COMPANY_REG_NUMBER
        defaultCompanyShouldBeFound("companyRegNumber.notEquals=" + UPDATED_COMPANY_REG_NUMBER);
    }

    @Test
    @Transactional
    public void getAllCompaniesByCompanyRegNumberIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where companyRegNumber in DEFAULT_COMPANY_REG_NUMBER or UPDATED_COMPANY_REG_NUMBER
        defaultCompanyShouldBeFound("companyRegNumber.in=" + DEFAULT_COMPANY_REG_NUMBER + "," + UPDATED_COMPANY_REG_NUMBER);

        // Get all the companyList where companyRegNumber equals to UPDATED_COMPANY_REG_NUMBER
        defaultCompanyShouldNotBeFound("companyRegNumber.in=" + UPDATED_COMPANY_REG_NUMBER);
    }

    @Test
    @Transactional
    public void getAllCompaniesByCompanyRegNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where companyRegNumber is not null
        defaultCompanyShouldBeFound("companyRegNumber.specified=true");

        // Get all the companyList where companyRegNumber is null
        defaultCompanyShouldNotBeFound("companyRegNumber.specified=false");
    }
                @Test
    @Transactional
    public void getAllCompaniesByCompanyRegNumberContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where companyRegNumber contains DEFAULT_COMPANY_REG_NUMBER
        defaultCompanyShouldBeFound("companyRegNumber.contains=" + DEFAULT_COMPANY_REG_NUMBER);

        // Get all the companyList where companyRegNumber contains UPDATED_COMPANY_REG_NUMBER
        defaultCompanyShouldNotBeFound("companyRegNumber.contains=" + UPDATED_COMPANY_REG_NUMBER);
    }

    @Test
    @Transactional
    public void getAllCompaniesByCompanyRegNumberNotContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where companyRegNumber does not contain DEFAULT_COMPANY_REG_NUMBER
        defaultCompanyShouldNotBeFound("companyRegNumber.doesNotContain=" + DEFAULT_COMPANY_REG_NUMBER);

        // Get all the companyList where companyRegNumber does not contain UPDATED_COMPANY_REG_NUMBER
        defaultCompanyShouldBeFound("companyRegNumber.doesNotContain=" + UPDATED_COMPANY_REG_NUMBER);
    }


    @Test
    @Transactional
    public void getAllCompaniesByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where email equals to DEFAULT_EMAIL
        defaultCompanyShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the companyList where email equals to UPDATED_EMAIL
        defaultCompanyShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllCompaniesByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where email not equals to DEFAULT_EMAIL
        defaultCompanyShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the companyList where email not equals to UPDATED_EMAIL
        defaultCompanyShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllCompaniesByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultCompanyShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the companyList where email equals to UPDATED_EMAIL
        defaultCompanyShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllCompaniesByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where email is not null
        defaultCompanyShouldBeFound("email.specified=true");

        // Get all the companyList where email is null
        defaultCompanyShouldNotBeFound("email.specified=false");
    }
                @Test
    @Transactional
    public void getAllCompaniesByEmailContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where email contains DEFAULT_EMAIL
        defaultCompanyShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the companyList where email contains UPDATED_EMAIL
        defaultCompanyShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllCompaniesByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where email does not contain DEFAULT_EMAIL
        defaultCompanyShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the companyList where email does not contain UPDATED_EMAIL
        defaultCompanyShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }


    @Test
    @Transactional
    public void getAllCompaniesByRatingIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where rating equals to DEFAULT_RATING
        defaultCompanyShouldBeFound("rating.equals=" + DEFAULT_RATING);

        // Get all the companyList where rating equals to UPDATED_RATING
        defaultCompanyShouldNotBeFound("rating.equals=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    public void getAllCompaniesByRatingIsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where rating not equals to DEFAULT_RATING
        defaultCompanyShouldNotBeFound("rating.notEquals=" + DEFAULT_RATING);

        // Get all the companyList where rating not equals to UPDATED_RATING
        defaultCompanyShouldBeFound("rating.notEquals=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    public void getAllCompaniesByRatingIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where rating in DEFAULT_RATING or UPDATED_RATING
        defaultCompanyShouldBeFound("rating.in=" + DEFAULT_RATING + "," + UPDATED_RATING);

        // Get all the companyList where rating equals to UPDATED_RATING
        defaultCompanyShouldNotBeFound("rating.in=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    public void getAllCompaniesByRatingIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where rating is not null
        defaultCompanyShouldBeFound("rating.specified=true");

        // Get all the companyList where rating is null
        defaultCompanyShouldNotBeFound("rating.specified=false");
    }

    @Test
    @Transactional
    public void getAllCompaniesByRatingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where rating is greater than or equal to DEFAULT_RATING
        defaultCompanyShouldBeFound("rating.greaterThanOrEqual=" + DEFAULT_RATING);

        // Get all the companyList where rating is greater than or equal to UPDATED_RATING
        defaultCompanyShouldNotBeFound("rating.greaterThanOrEqual=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    public void getAllCompaniesByRatingIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where rating is less than or equal to DEFAULT_RATING
        defaultCompanyShouldBeFound("rating.lessThanOrEqual=" + DEFAULT_RATING);

        // Get all the companyList where rating is less than or equal to SMALLER_RATING
        defaultCompanyShouldNotBeFound("rating.lessThanOrEqual=" + SMALLER_RATING);
    }

    @Test
    @Transactional
    public void getAllCompaniesByRatingIsLessThanSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where rating is less than DEFAULT_RATING
        defaultCompanyShouldNotBeFound("rating.lessThan=" + DEFAULT_RATING);

        // Get all the companyList where rating is less than UPDATED_RATING
        defaultCompanyShouldBeFound("rating.lessThan=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    public void getAllCompaniesByRatingIsGreaterThanSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where rating is greater than DEFAULT_RATING
        defaultCompanyShouldNotBeFound("rating.greaterThan=" + DEFAULT_RATING);

        // Get all the companyList where rating is greater than SMALLER_RATING
        defaultCompanyShouldBeFound("rating.greaterThan=" + SMALLER_RATING);
    }


    @Test
    @Transactional
    public void getAllCompaniesByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where phone equals to DEFAULT_PHONE
        defaultCompanyShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the companyList where phone equals to UPDATED_PHONE
        defaultCompanyShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllCompaniesByPhoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where phone not equals to DEFAULT_PHONE
        defaultCompanyShouldNotBeFound("phone.notEquals=" + DEFAULT_PHONE);

        // Get all the companyList where phone not equals to UPDATED_PHONE
        defaultCompanyShouldBeFound("phone.notEquals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllCompaniesByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultCompanyShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the companyList where phone equals to UPDATED_PHONE
        defaultCompanyShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllCompaniesByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where phone is not null
        defaultCompanyShouldBeFound("phone.specified=true");

        // Get all the companyList where phone is null
        defaultCompanyShouldNotBeFound("phone.specified=false");
    }
                @Test
    @Transactional
    public void getAllCompaniesByPhoneContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where phone contains DEFAULT_PHONE
        defaultCompanyShouldBeFound("phone.contains=" + DEFAULT_PHONE);

        // Get all the companyList where phone contains UPDATED_PHONE
        defaultCompanyShouldNotBeFound("phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllCompaniesByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where phone does not contain DEFAULT_PHONE
        defaultCompanyShouldNotBeFound("phone.doesNotContain=" + DEFAULT_PHONE);

        // Get all the companyList where phone does not contain UPDATED_PHONE
        defaultCompanyShouldBeFound("phone.doesNotContain=" + UPDATED_PHONE);
    }


    @Test
    @Transactional
    public void getAllCompaniesByAddressLine1IsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where addressLine1 equals to DEFAULT_ADDRESS_LINE_1
        defaultCompanyShouldBeFound("addressLine1.equals=" + DEFAULT_ADDRESS_LINE_1);

        // Get all the companyList where addressLine1 equals to UPDATED_ADDRESS_LINE_1
        defaultCompanyShouldNotBeFound("addressLine1.equals=" + UPDATED_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    public void getAllCompaniesByAddressLine1IsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where addressLine1 not equals to DEFAULT_ADDRESS_LINE_1
        defaultCompanyShouldNotBeFound("addressLine1.notEquals=" + DEFAULT_ADDRESS_LINE_1);

        // Get all the companyList where addressLine1 not equals to UPDATED_ADDRESS_LINE_1
        defaultCompanyShouldBeFound("addressLine1.notEquals=" + UPDATED_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    public void getAllCompaniesByAddressLine1IsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where addressLine1 in DEFAULT_ADDRESS_LINE_1 or UPDATED_ADDRESS_LINE_1
        defaultCompanyShouldBeFound("addressLine1.in=" + DEFAULT_ADDRESS_LINE_1 + "," + UPDATED_ADDRESS_LINE_1);

        // Get all the companyList where addressLine1 equals to UPDATED_ADDRESS_LINE_1
        defaultCompanyShouldNotBeFound("addressLine1.in=" + UPDATED_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    public void getAllCompaniesByAddressLine1IsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where addressLine1 is not null
        defaultCompanyShouldBeFound("addressLine1.specified=true");

        // Get all the companyList where addressLine1 is null
        defaultCompanyShouldNotBeFound("addressLine1.specified=false");
    }
                @Test
    @Transactional
    public void getAllCompaniesByAddressLine1ContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where addressLine1 contains DEFAULT_ADDRESS_LINE_1
        defaultCompanyShouldBeFound("addressLine1.contains=" + DEFAULT_ADDRESS_LINE_1);

        // Get all the companyList where addressLine1 contains UPDATED_ADDRESS_LINE_1
        defaultCompanyShouldNotBeFound("addressLine1.contains=" + UPDATED_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    public void getAllCompaniesByAddressLine1NotContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where addressLine1 does not contain DEFAULT_ADDRESS_LINE_1
        defaultCompanyShouldNotBeFound("addressLine1.doesNotContain=" + DEFAULT_ADDRESS_LINE_1);

        // Get all the companyList where addressLine1 does not contain UPDATED_ADDRESS_LINE_1
        defaultCompanyShouldBeFound("addressLine1.doesNotContain=" + UPDATED_ADDRESS_LINE_1);
    }


    @Test
    @Transactional
    public void getAllCompaniesByAddressLine2IsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where addressLine2 equals to DEFAULT_ADDRESS_LINE_2
        defaultCompanyShouldBeFound("addressLine2.equals=" + DEFAULT_ADDRESS_LINE_2);

        // Get all the companyList where addressLine2 equals to UPDATED_ADDRESS_LINE_2
        defaultCompanyShouldNotBeFound("addressLine2.equals=" + UPDATED_ADDRESS_LINE_2);
    }

    @Test
    @Transactional
    public void getAllCompaniesByAddressLine2IsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where addressLine2 not equals to DEFAULT_ADDRESS_LINE_2
        defaultCompanyShouldNotBeFound("addressLine2.notEquals=" + DEFAULT_ADDRESS_LINE_2);

        // Get all the companyList where addressLine2 not equals to UPDATED_ADDRESS_LINE_2
        defaultCompanyShouldBeFound("addressLine2.notEquals=" + UPDATED_ADDRESS_LINE_2);
    }

    @Test
    @Transactional
    public void getAllCompaniesByAddressLine2IsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where addressLine2 in DEFAULT_ADDRESS_LINE_2 or UPDATED_ADDRESS_LINE_2
        defaultCompanyShouldBeFound("addressLine2.in=" + DEFAULT_ADDRESS_LINE_2 + "," + UPDATED_ADDRESS_LINE_2);

        // Get all the companyList where addressLine2 equals to UPDATED_ADDRESS_LINE_2
        defaultCompanyShouldNotBeFound("addressLine2.in=" + UPDATED_ADDRESS_LINE_2);
    }

    @Test
    @Transactional
    public void getAllCompaniesByAddressLine2IsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where addressLine2 is not null
        defaultCompanyShouldBeFound("addressLine2.specified=true");

        // Get all the companyList where addressLine2 is null
        defaultCompanyShouldNotBeFound("addressLine2.specified=false");
    }
                @Test
    @Transactional
    public void getAllCompaniesByAddressLine2ContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where addressLine2 contains DEFAULT_ADDRESS_LINE_2
        defaultCompanyShouldBeFound("addressLine2.contains=" + DEFAULT_ADDRESS_LINE_2);

        // Get all the companyList where addressLine2 contains UPDATED_ADDRESS_LINE_2
        defaultCompanyShouldNotBeFound("addressLine2.contains=" + UPDATED_ADDRESS_LINE_2);
    }

    @Test
    @Transactional
    public void getAllCompaniesByAddressLine2NotContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where addressLine2 does not contain DEFAULT_ADDRESS_LINE_2
        defaultCompanyShouldNotBeFound("addressLine2.doesNotContain=" + DEFAULT_ADDRESS_LINE_2);

        // Get all the companyList where addressLine2 does not contain UPDATED_ADDRESS_LINE_2
        defaultCompanyShouldBeFound("addressLine2.doesNotContain=" + UPDATED_ADDRESS_LINE_2);
    }


    @Test
    @Transactional
    public void getAllCompaniesByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where city equals to DEFAULT_CITY
        defaultCompanyShouldBeFound("city.equals=" + DEFAULT_CITY);

        // Get all the companyList where city equals to UPDATED_CITY
        defaultCompanyShouldNotBeFound("city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllCompaniesByCityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where city not equals to DEFAULT_CITY
        defaultCompanyShouldNotBeFound("city.notEquals=" + DEFAULT_CITY);

        // Get all the companyList where city not equals to UPDATED_CITY
        defaultCompanyShouldBeFound("city.notEquals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllCompaniesByCityIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where city in DEFAULT_CITY or UPDATED_CITY
        defaultCompanyShouldBeFound("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY);

        // Get all the companyList where city equals to UPDATED_CITY
        defaultCompanyShouldNotBeFound("city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllCompaniesByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where city is not null
        defaultCompanyShouldBeFound("city.specified=true");

        // Get all the companyList where city is null
        defaultCompanyShouldNotBeFound("city.specified=false");
    }
                @Test
    @Transactional
    public void getAllCompaniesByCityContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where city contains DEFAULT_CITY
        defaultCompanyShouldBeFound("city.contains=" + DEFAULT_CITY);

        // Get all the companyList where city contains UPDATED_CITY
        defaultCompanyShouldNotBeFound("city.contains=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllCompaniesByCityNotContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where city does not contain DEFAULT_CITY
        defaultCompanyShouldNotBeFound("city.doesNotContain=" + DEFAULT_CITY);

        // Get all the companyList where city does not contain UPDATED_CITY
        defaultCompanyShouldBeFound("city.doesNotContain=" + UPDATED_CITY);
    }


    @Test
    @Transactional
    public void getAllCompaniesByCountryIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where country equals to DEFAULT_COUNTRY
        defaultCompanyShouldBeFound("country.equals=" + DEFAULT_COUNTRY);

        // Get all the companyList where country equals to UPDATED_COUNTRY
        defaultCompanyShouldNotBeFound("country.equals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllCompaniesByCountryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where country not equals to DEFAULT_COUNTRY
        defaultCompanyShouldNotBeFound("country.notEquals=" + DEFAULT_COUNTRY);

        // Get all the companyList where country not equals to UPDATED_COUNTRY
        defaultCompanyShouldBeFound("country.notEquals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllCompaniesByCountryIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where country in DEFAULT_COUNTRY or UPDATED_COUNTRY
        defaultCompanyShouldBeFound("country.in=" + DEFAULT_COUNTRY + "," + UPDATED_COUNTRY);

        // Get all the companyList where country equals to UPDATED_COUNTRY
        defaultCompanyShouldNotBeFound("country.in=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllCompaniesByCountryIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where country is not null
        defaultCompanyShouldBeFound("country.specified=true");

        // Get all the companyList where country is null
        defaultCompanyShouldNotBeFound("country.specified=false");
    }
                @Test
    @Transactional
    public void getAllCompaniesByCountryContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where country contains DEFAULT_COUNTRY
        defaultCompanyShouldBeFound("country.contains=" + DEFAULT_COUNTRY);

        // Get all the companyList where country contains UPDATED_COUNTRY
        defaultCompanyShouldNotBeFound("country.contains=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllCompaniesByCountryNotContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where country does not contain DEFAULT_COUNTRY
        defaultCompanyShouldNotBeFound("country.doesNotContain=" + DEFAULT_COUNTRY);

        // Get all the companyList where country does not contain UPDATED_COUNTRY
        defaultCompanyShouldBeFound("country.doesNotContain=" + UPDATED_COUNTRY);
    }


    @Test
    @Transactional
    public void getAllCompaniesByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where isActive equals to DEFAULT_IS_ACTIVE
        defaultCompanyShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the companyList where isActive equals to UPDATED_IS_ACTIVE
        defaultCompanyShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllCompaniesByIsActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where isActive not equals to DEFAULT_IS_ACTIVE
        defaultCompanyShouldNotBeFound("isActive.notEquals=" + DEFAULT_IS_ACTIVE);

        // Get all the companyList where isActive not equals to UPDATED_IS_ACTIVE
        defaultCompanyShouldBeFound("isActive.notEquals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllCompaniesByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultCompanyShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the companyList where isActive equals to UPDATED_IS_ACTIVE
        defaultCompanyShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllCompaniesByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where isActive is not null
        defaultCompanyShouldBeFound("isActive.specified=true");

        // Get all the companyList where isActive is null
        defaultCompanyShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    public void getAllCompaniesByApiKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where apiKey equals to DEFAULT_API_KEY
        defaultCompanyShouldBeFound("apiKey.equals=" + DEFAULT_API_KEY);

        // Get all the companyList where apiKey equals to UPDATED_API_KEY
        defaultCompanyShouldNotBeFound("apiKey.equals=" + UPDATED_API_KEY);
    }

    @Test
    @Transactional
    public void getAllCompaniesByApiKeyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where apiKey not equals to DEFAULT_API_KEY
        defaultCompanyShouldNotBeFound("apiKey.notEquals=" + DEFAULT_API_KEY);

        // Get all the companyList where apiKey not equals to UPDATED_API_KEY
        defaultCompanyShouldBeFound("apiKey.notEquals=" + UPDATED_API_KEY);
    }

    @Test
    @Transactional
    public void getAllCompaniesByApiKeyIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where apiKey in DEFAULT_API_KEY or UPDATED_API_KEY
        defaultCompanyShouldBeFound("apiKey.in=" + DEFAULT_API_KEY + "," + UPDATED_API_KEY);

        // Get all the companyList where apiKey equals to UPDATED_API_KEY
        defaultCompanyShouldNotBeFound("apiKey.in=" + UPDATED_API_KEY);
    }

    @Test
    @Transactional
    public void getAllCompaniesByApiKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where apiKey is not null
        defaultCompanyShouldBeFound("apiKey.specified=true");

        // Get all the companyList where apiKey is null
        defaultCompanyShouldNotBeFound("apiKey.specified=false");
    }
                @Test
    @Transactional
    public void getAllCompaniesByApiKeyContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where apiKey contains DEFAULT_API_KEY
        defaultCompanyShouldBeFound("apiKey.contains=" + DEFAULT_API_KEY);

        // Get all the companyList where apiKey contains UPDATED_API_KEY
        defaultCompanyShouldNotBeFound("apiKey.contains=" + UPDATED_API_KEY);
    }

    @Test
    @Transactional
    public void getAllCompaniesByApiKeyNotContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where apiKey does not contain DEFAULT_API_KEY
        defaultCompanyShouldNotBeFound("apiKey.doesNotContain=" + DEFAULT_API_KEY);

        // Get all the companyList where apiKey does not contain UPDATED_API_KEY
        defaultCompanyShouldBeFound("apiKey.doesNotContain=" + UPDATED_API_KEY);
    }


    @Test
    @Transactional
    public void getAllCompaniesByExpireOnIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where expireOn equals to DEFAULT_EXPIRE_ON
        defaultCompanyShouldBeFound("expireOn.equals=" + DEFAULT_EXPIRE_ON);

        // Get all the companyList where expireOn equals to UPDATED_EXPIRE_ON
        defaultCompanyShouldNotBeFound("expireOn.equals=" + UPDATED_EXPIRE_ON);
    }

    @Test
    @Transactional
    public void getAllCompaniesByExpireOnIsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where expireOn not equals to DEFAULT_EXPIRE_ON
        defaultCompanyShouldNotBeFound("expireOn.notEquals=" + DEFAULT_EXPIRE_ON);

        // Get all the companyList where expireOn not equals to UPDATED_EXPIRE_ON
        defaultCompanyShouldBeFound("expireOn.notEquals=" + UPDATED_EXPIRE_ON);
    }

    @Test
    @Transactional
    public void getAllCompaniesByExpireOnIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where expireOn in DEFAULT_EXPIRE_ON or UPDATED_EXPIRE_ON
        defaultCompanyShouldBeFound("expireOn.in=" + DEFAULT_EXPIRE_ON + "," + UPDATED_EXPIRE_ON);

        // Get all the companyList where expireOn equals to UPDATED_EXPIRE_ON
        defaultCompanyShouldNotBeFound("expireOn.in=" + UPDATED_EXPIRE_ON);
    }

    @Test
    @Transactional
    public void getAllCompaniesByExpireOnIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where expireOn is not null
        defaultCompanyShouldBeFound("expireOn.specified=true");

        // Get all the companyList where expireOn is null
        defaultCompanyShouldNotBeFound("expireOn.specified=false");
    }

    @Test
    @Transactional
    public void getAllCompaniesByExpireOnIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where expireOn is greater than or equal to DEFAULT_EXPIRE_ON
        defaultCompanyShouldBeFound("expireOn.greaterThanOrEqual=" + DEFAULT_EXPIRE_ON);

        // Get all the companyList where expireOn is greater than or equal to UPDATED_EXPIRE_ON
        defaultCompanyShouldNotBeFound("expireOn.greaterThanOrEqual=" + UPDATED_EXPIRE_ON);
    }

    @Test
    @Transactional
    public void getAllCompaniesByExpireOnIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where expireOn is less than or equal to DEFAULT_EXPIRE_ON
        defaultCompanyShouldBeFound("expireOn.lessThanOrEqual=" + DEFAULT_EXPIRE_ON);

        // Get all the companyList where expireOn is less than or equal to SMALLER_EXPIRE_ON
        defaultCompanyShouldNotBeFound("expireOn.lessThanOrEqual=" + SMALLER_EXPIRE_ON);
    }

    @Test
    @Transactional
    public void getAllCompaniesByExpireOnIsLessThanSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where expireOn is less than DEFAULT_EXPIRE_ON
        defaultCompanyShouldNotBeFound("expireOn.lessThan=" + DEFAULT_EXPIRE_ON);

        // Get all the companyList where expireOn is less than UPDATED_EXPIRE_ON
        defaultCompanyShouldBeFound("expireOn.lessThan=" + UPDATED_EXPIRE_ON);
    }

    @Test
    @Transactional
    public void getAllCompaniesByExpireOnIsGreaterThanSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where expireOn is greater than DEFAULT_EXPIRE_ON
        defaultCompanyShouldNotBeFound("expireOn.greaterThan=" + DEFAULT_EXPIRE_ON);

        // Get all the companyList where expireOn is greater than SMALLER_EXPIRE_ON
        defaultCompanyShouldBeFound("expireOn.greaterThan=" + SMALLER_EXPIRE_ON);
    }


    @Test
    @Transactional
    public void getAllCompaniesByLicenseTypeIsEqualToSomething() throws Exception {
        // Get already existing entity
        LicenseType licenseType = company.getLicenseType();
        companyRepository.saveAndFlush(company);
        Long licenseTypeId = licenseType.getId();

        // Get all the companyList where licenseType equals to licenseTypeId
        defaultCompanyShouldBeFound("licenseTypeId.equals=" + licenseTypeId);

        // Get all the companyList where licenseType equals to licenseTypeId + 1
        defaultCompanyShouldNotBeFound("licenseTypeId.equals=" + (licenseTypeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCompanyShouldBeFound(String filter) throws Exception {
        restCompanyMockMvc.perform(get("/api/companies?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(company.getId().intValue())))
            .andExpect(jsonPath("$.[*].companyCode").value(hasItem(DEFAULT_COMPANY_CODE)))
            .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME)))
            .andExpect(jsonPath("$.[*].companyRegNumber").value(hasItem(DEFAULT_COMPANY_REG_NUMBER)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING.doubleValue())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].addressLine1").value(hasItem(DEFAULT_ADDRESS_LINE_1)))
            .andExpect(jsonPath("$.[*].addressLine2").value(hasItem(DEFAULT_ADDRESS_LINE_2)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].apiKey").value(hasItem(DEFAULT_API_KEY)))
            .andExpect(jsonPath("$.[*].expireOn").value(hasItem(DEFAULT_EXPIRE_ON.toString())));

        // Check, that the count call also returns 1
        restCompanyMockMvc.perform(get("/api/companies/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCompanyShouldNotBeFound(String filter) throws Exception {
        restCompanyMockMvc.perform(get("/api/companies?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCompanyMockMvc.perform(get("/api/companies/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingCompany() throws Exception {
        // Get the company
        restCompanyMockMvc.perform(get("/api/companies/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCompany() throws Exception {
        // Initialize the database
        companyService.save(company);

        int databaseSizeBeforeUpdate = companyRepository.findAll().size();

        // Update the company
        Company updatedCompany = companyRepository.findById(company.getId()).get();
        // Disconnect from session so that the updates on updatedCompany are not directly saved in db
        em.detach(updatedCompany);
        updatedCompany
            .companyCode(UPDATED_COMPANY_CODE)
            .companyName(UPDATED_COMPANY_NAME)
            .companyRegNumber(UPDATED_COMPANY_REG_NUMBER)
            .email(UPDATED_EMAIL)
            .rating(UPDATED_RATING)
            .phone(UPDATED_PHONE)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .isActive(UPDATED_IS_ACTIVE)
            .apiKey(UPDATED_API_KEY)
            .expireOn(UPDATED_EXPIRE_ON);

        restCompanyMockMvc.perform(put("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCompany)))
            .andExpect(status().isOk());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);
        Company testCompany = companyList.get(companyList.size() - 1);
        assertThat(testCompany.getCompanyCode()).isEqualTo(UPDATED_COMPANY_CODE);
        assertThat(testCompany.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testCompany.getCompanyRegNumber()).isEqualTo(UPDATED_COMPANY_REG_NUMBER);
        assertThat(testCompany.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCompany.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testCompany.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testCompany.getAddressLine1()).isEqualTo(UPDATED_ADDRESS_LINE_1);
        assertThat(testCompany.getAddressLine2()).isEqualTo(UPDATED_ADDRESS_LINE_2);
        assertThat(testCompany.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testCompany.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testCompany.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testCompany.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testCompany.isIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testCompany.getApiKey()).isEqualTo(UPDATED_API_KEY);
        assertThat(testCompany.getExpireOn()).isEqualTo(UPDATED_EXPIRE_ON);
    }

    @Test
    @Transactional
    public void updateNonExistingCompany() throws Exception {
        int databaseSizeBeforeUpdate = companyRepository.findAll().size();

        // Create the Company

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompanyMockMvc.perform(put("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(company)))
            .andExpect(status().isBadRequest());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCompany() throws Exception {
        // Initialize the database
        companyService.save(company);

        int databaseSizeBeforeDelete = companyRepository.findAll().size();

        // Delete the company
        restCompanyMockMvc.perform(delete("/api/companies/{id}", company.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

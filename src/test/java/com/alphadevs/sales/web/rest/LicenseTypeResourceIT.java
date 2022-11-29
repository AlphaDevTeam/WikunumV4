package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.WikunumApp;
import com.alphadevs.sales.domain.LicenseType;
import com.alphadevs.sales.repository.LicenseTypeRepository;
import com.alphadevs.sales.service.LicenseTypeService;
import com.alphadevs.sales.web.rest.errors.ExceptionTranslator;
import com.alphadevs.sales.service.dto.LicenseTypeCriteria;
import com.alphadevs.sales.service.LicenseTypeQueryService;

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
 * Integration tests for the {@link LicenseTypeResource} REST controller.
 */
@SpringBootTest(classes = WikunumApp.class)
public class LicenseTypeResourceIT {

    private static final String DEFAULT_LICENSE_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_LICENSE_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_LICENSE_TYPE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LICENSE_TYPE_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_VALIDITY_DAYS = 1D;
    private static final Double UPDATED_VALIDITY_DAYS = 2D;
    private static final Double SMALLER_VALIDITY_DAYS = 1D - 1D;

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    @Autowired
    private LicenseTypeRepository licenseTypeRepository;

    @Autowired
    private LicenseTypeService licenseTypeService;

    @Autowired
    private LicenseTypeQueryService licenseTypeQueryService;

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

    private MockMvc restLicenseTypeMockMvc;

    private LicenseType licenseType;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LicenseTypeResource licenseTypeResource = new LicenseTypeResource(licenseTypeService, licenseTypeQueryService);
        this.restLicenseTypeMockMvc = MockMvcBuilders.standaloneSetup(licenseTypeResource)
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
    public static LicenseType createEntity(EntityManager em) {
        LicenseType licenseType = new LicenseType()
            .licenseTypeCode(DEFAULT_LICENSE_TYPE_CODE)
            .licenseTypeName(DEFAULT_LICENSE_TYPE_NAME)
            .validityDays(DEFAULT_VALIDITY_DAYS)
            .isActive(DEFAULT_IS_ACTIVE);
        return licenseType;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LicenseType createUpdatedEntity(EntityManager em) {
        LicenseType licenseType = new LicenseType()
            .licenseTypeCode(UPDATED_LICENSE_TYPE_CODE)
            .licenseTypeName(UPDATED_LICENSE_TYPE_NAME)
            .validityDays(UPDATED_VALIDITY_DAYS)
            .isActive(UPDATED_IS_ACTIVE);
        return licenseType;
    }

    @BeforeEach
    public void initTest() {
        licenseType = createEntity(em);
    }

    @Test
    @Transactional
    public void createLicenseType() throws Exception {
        int databaseSizeBeforeCreate = licenseTypeRepository.findAll().size();

        // Create the LicenseType
        restLicenseTypeMockMvc.perform(post("/api/license-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(licenseType)))
            .andExpect(status().isCreated());

        // Validate the LicenseType in the database
        List<LicenseType> licenseTypeList = licenseTypeRepository.findAll();
        assertThat(licenseTypeList).hasSize(databaseSizeBeforeCreate + 1);
        LicenseType testLicenseType = licenseTypeList.get(licenseTypeList.size() - 1);
        assertThat(testLicenseType.getLicenseTypeCode()).isEqualTo(DEFAULT_LICENSE_TYPE_CODE);
        assertThat(testLicenseType.getLicenseTypeName()).isEqualTo(DEFAULT_LICENSE_TYPE_NAME);
        assertThat(testLicenseType.getValidityDays()).isEqualTo(DEFAULT_VALIDITY_DAYS);
        assertThat(testLicenseType.isIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void createLicenseTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = licenseTypeRepository.findAll().size();

        // Create the LicenseType with an existing ID
        licenseType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLicenseTypeMockMvc.perform(post("/api/license-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(licenseType)))
            .andExpect(status().isBadRequest());

        // Validate the LicenseType in the database
        List<LicenseType> licenseTypeList = licenseTypeRepository.findAll();
        assertThat(licenseTypeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkLicenseTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = licenseTypeRepository.findAll().size();
        // set the field null
        licenseType.setLicenseTypeCode(null);

        // Create the LicenseType, which fails.

        restLicenseTypeMockMvc.perform(post("/api/license-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(licenseType)))
            .andExpect(status().isBadRequest());

        List<LicenseType> licenseTypeList = licenseTypeRepository.findAll();
        assertThat(licenseTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLicenseTypeNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = licenseTypeRepository.findAll().size();
        // set the field null
        licenseType.setLicenseTypeName(null);

        // Create the LicenseType, which fails.

        restLicenseTypeMockMvc.perform(post("/api/license-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(licenseType)))
            .andExpect(status().isBadRequest());

        List<LicenseType> licenseTypeList = licenseTypeRepository.findAll();
        assertThat(licenseTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLicenseTypes() throws Exception {
        // Initialize the database
        licenseTypeRepository.saveAndFlush(licenseType);

        // Get all the licenseTypeList
        restLicenseTypeMockMvc.perform(get("/api/license-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(licenseType.getId().intValue())))
            .andExpect(jsonPath("$.[*].licenseTypeCode").value(hasItem(DEFAULT_LICENSE_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].licenseTypeName").value(hasItem(DEFAULT_LICENSE_TYPE_NAME)))
            .andExpect(jsonPath("$.[*].validityDays").value(hasItem(DEFAULT_VALIDITY_DAYS.doubleValue())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getLicenseType() throws Exception {
        // Initialize the database
        licenseTypeRepository.saveAndFlush(licenseType);

        // Get the licenseType
        restLicenseTypeMockMvc.perform(get("/api/license-types/{id}", licenseType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(licenseType.getId().intValue()))
            .andExpect(jsonPath("$.licenseTypeCode").value(DEFAULT_LICENSE_TYPE_CODE))
            .andExpect(jsonPath("$.licenseTypeName").value(DEFAULT_LICENSE_TYPE_NAME))
            .andExpect(jsonPath("$.validityDays").value(DEFAULT_VALIDITY_DAYS.doubleValue()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }


    @Test
    @Transactional
    public void getLicenseTypesByIdFiltering() throws Exception {
        // Initialize the database
        licenseTypeRepository.saveAndFlush(licenseType);

        Long id = licenseType.getId();

        defaultLicenseTypeShouldBeFound("id.equals=" + id);
        defaultLicenseTypeShouldNotBeFound("id.notEquals=" + id);

        defaultLicenseTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLicenseTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultLicenseTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLicenseTypeShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllLicenseTypesByLicenseTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        licenseTypeRepository.saveAndFlush(licenseType);

        // Get all the licenseTypeList where licenseTypeCode equals to DEFAULT_LICENSE_TYPE_CODE
        defaultLicenseTypeShouldBeFound("licenseTypeCode.equals=" + DEFAULT_LICENSE_TYPE_CODE);

        // Get all the licenseTypeList where licenseTypeCode equals to UPDATED_LICENSE_TYPE_CODE
        defaultLicenseTypeShouldNotBeFound("licenseTypeCode.equals=" + UPDATED_LICENSE_TYPE_CODE);
    }

    @Test
    @Transactional
    public void getAllLicenseTypesByLicenseTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        licenseTypeRepository.saveAndFlush(licenseType);

        // Get all the licenseTypeList where licenseTypeCode not equals to DEFAULT_LICENSE_TYPE_CODE
        defaultLicenseTypeShouldNotBeFound("licenseTypeCode.notEquals=" + DEFAULT_LICENSE_TYPE_CODE);

        // Get all the licenseTypeList where licenseTypeCode not equals to UPDATED_LICENSE_TYPE_CODE
        defaultLicenseTypeShouldBeFound("licenseTypeCode.notEquals=" + UPDATED_LICENSE_TYPE_CODE);
    }

    @Test
    @Transactional
    public void getAllLicenseTypesByLicenseTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        licenseTypeRepository.saveAndFlush(licenseType);

        // Get all the licenseTypeList where licenseTypeCode in DEFAULT_LICENSE_TYPE_CODE or UPDATED_LICENSE_TYPE_CODE
        defaultLicenseTypeShouldBeFound("licenseTypeCode.in=" + DEFAULT_LICENSE_TYPE_CODE + "," + UPDATED_LICENSE_TYPE_CODE);

        // Get all the licenseTypeList where licenseTypeCode equals to UPDATED_LICENSE_TYPE_CODE
        defaultLicenseTypeShouldNotBeFound("licenseTypeCode.in=" + UPDATED_LICENSE_TYPE_CODE);
    }

    @Test
    @Transactional
    public void getAllLicenseTypesByLicenseTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        licenseTypeRepository.saveAndFlush(licenseType);

        // Get all the licenseTypeList where licenseTypeCode is not null
        defaultLicenseTypeShouldBeFound("licenseTypeCode.specified=true");

        // Get all the licenseTypeList where licenseTypeCode is null
        defaultLicenseTypeShouldNotBeFound("licenseTypeCode.specified=false");
    }
                @Test
    @Transactional
    public void getAllLicenseTypesByLicenseTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        licenseTypeRepository.saveAndFlush(licenseType);

        // Get all the licenseTypeList where licenseTypeCode contains DEFAULT_LICENSE_TYPE_CODE
        defaultLicenseTypeShouldBeFound("licenseTypeCode.contains=" + DEFAULT_LICENSE_TYPE_CODE);

        // Get all the licenseTypeList where licenseTypeCode contains UPDATED_LICENSE_TYPE_CODE
        defaultLicenseTypeShouldNotBeFound("licenseTypeCode.contains=" + UPDATED_LICENSE_TYPE_CODE);
    }

    @Test
    @Transactional
    public void getAllLicenseTypesByLicenseTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        licenseTypeRepository.saveAndFlush(licenseType);

        // Get all the licenseTypeList where licenseTypeCode does not contain DEFAULT_LICENSE_TYPE_CODE
        defaultLicenseTypeShouldNotBeFound("licenseTypeCode.doesNotContain=" + DEFAULT_LICENSE_TYPE_CODE);

        // Get all the licenseTypeList where licenseTypeCode does not contain UPDATED_LICENSE_TYPE_CODE
        defaultLicenseTypeShouldBeFound("licenseTypeCode.doesNotContain=" + UPDATED_LICENSE_TYPE_CODE);
    }


    @Test
    @Transactional
    public void getAllLicenseTypesByLicenseTypeNameIsEqualToSomething() throws Exception {
        // Initialize the database
        licenseTypeRepository.saveAndFlush(licenseType);

        // Get all the licenseTypeList where licenseTypeName equals to DEFAULT_LICENSE_TYPE_NAME
        defaultLicenseTypeShouldBeFound("licenseTypeName.equals=" + DEFAULT_LICENSE_TYPE_NAME);

        // Get all the licenseTypeList where licenseTypeName equals to UPDATED_LICENSE_TYPE_NAME
        defaultLicenseTypeShouldNotBeFound("licenseTypeName.equals=" + UPDATED_LICENSE_TYPE_NAME);
    }

    @Test
    @Transactional
    public void getAllLicenseTypesByLicenseTypeNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        licenseTypeRepository.saveAndFlush(licenseType);

        // Get all the licenseTypeList where licenseTypeName not equals to DEFAULT_LICENSE_TYPE_NAME
        defaultLicenseTypeShouldNotBeFound("licenseTypeName.notEquals=" + DEFAULT_LICENSE_TYPE_NAME);

        // Get all the licenseTypeList where licenseTypeName not equals to UPDATED_LICENSE_TYPE_NAME
        defaultLicenseTypeShouldBeFound("licenseTypeName.notEquals=" + UPDATED_LICENSE_TYPE_NAME);
    }

    @Test
    @Transactional
    public void getAllLicenseTypesByLicenseTypeNameIsInShouldWork() throws Exception {
        // Initialize the database
        licenseTypeRepository.saveAndFlush(licenseType);

        // Get all the licenseTypeList where licenseTypeName in DEFAULT_LICENSE_TYPE_NAME or UPDATED_LICENSE_TYPE_NAME
        defaultLicenseTypeShouldBeFound("licenseTypeName.in=" + DEFAULT_LICENSE_TYPE_NAME + "," + UPDATED_LICENSE_TYPE_NAME);

        // Get all the licenseTypeList where licenseTypeName equals to UPDATED_LICENSE_TYPE_NAME
        defaultLicenseTypeShouldNotBeFound("licenseTypeName.in=" + UPDATED_LICENSE_TYPE_NAME);
    }

    @Test
    @Transactional
    public void getAllLicenseTypesByLicenseTypeNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        licenseTypeRepository.saveAndFlush(licenseType);

        // Get all the licenseTypeList where licenseTypeName is not null
        defaultLicenseTypeShouldBeFound("licenseTypeName.specified=true");

        // Get all the licenseTypeList where licenseTypeName is null
        defaultLicenseTypeShouldNotBeFound("licenseTypeName.specified=false");
    }
                @Test
    @Transactional
    public void getAllLicenseTypesByLicenseTypeNameContainsSomething() throws Exception {
        // Initialize the database
        licenseTypeRepository.saveAndFlush(licenseType);

        // Get all the licenseTypeList where licenseTypeName contains DEFAULT_LICENSE_TYPE_NAME
        defaultLicenseTypeShouldBeFound("licenseTypeName.contains=" + DEFAULT_LICENSE_TYPE_NAME);

        // Get all the licenseTypeList where licenseTypeName contains UPDATED_LICENSE_TYPE_NAME
        defaultLicenseTypeShouldNotBeFound("licenseTypeName.contains=" + UPDATED_LICENSE_TYPE_NAME);
    }

    @Test
    @Transactional
    public void getAllLicenseTypesByLicenseTypeNameNotContainsSomething() throws Exception {
        // Initialize the database
        licenseTypeRepository.saveAndFlush(licenseType);

        // Get all the licenseTypeList where licenseTypeName does not contain DEFAULT_LICENSE_TYPE_NAME
        defaultLicenseTypeShouldNotBeFound("licenseTypeName.doesNotContain=" + DEFAULT_LICENSE_TYPE_NAME);

        // Get all the licenseTypeList where licenseTypeName does not contain UPDATED_LICENSE_TYPE_NAME
        defaultLicenseTypeShouldBeFound("licenseTypeName.doesNotContain=" + UPDATED_LICENSE_TYPE_NAME);
    }


    @Test
    @Transactional
    public void getAllLicenseTypesByValidityDaysIsEqualToSomething() throws Exception {
        // Initialize the database
        licenseTypeRepository.saveAndFlush(licenseType);

        // Get all the licenseTypeList where validityDays equals to DEFAULT_VALIDITY_DAYS
        defaultLicenseTypeShouldBeFound("validityDays.equals=" + DEFAULT_VALIDITY_DAYS);

        // Get all the licenseTypeList where validityDays equals to UPDATED_VALIDITY_DAYS
        defaultLicenseTypeShouldNotBeFound("validityDays.equals=" + UPDATED_VALIDITY_DAYS);
    }

    @Test
    @Transactional
    public void getAllLicenseTypesByValidityDaysIsNotEqualToSomething() throws Exception {
        // Initialize the database
        licenseTypeRepository.saveAndFlush(licenseType);

        // Get all the licenseTypeList where validityDays not equals to DEFAULT_VALIDITY_DAYS
        defaultLicenseTypeShouldNotBeFound("validityDays.notEquals=" + DEFAULT_VALIDITY_DAYS);

        // Get all the licenseTypeList where validityDays not equals to UPDATED_VALIDITY_DAYS
        defaultLicenseTypeShouldBeFound("validityDays.notEquals=" + UPDATED_VALIDITY_DAYS);
    }

    @Test
    @Transactional
    public void getAllLicenseTypesByValidityDaysIsInShouldWork() throws Exception {
        // Initialize the database
        licenseTypeRepository.saveAndFlush(licenseType);

        // Get all the licenseTypeList where validityDays in DEFAULT_VALIDITY_DAYS or UPDATED_VALIDITY_DAYS
        defaultLicenseTypeShouldBeFound("validityDays.in=" + DEFAULT_VALIDITY_DAYS + "," + UPDATED_VALIDITY_DAYS);

        // Get all the licenseTypeList where validityDays equals to UPDATED_VALIDITY_DAYS
        defaultLicenseTypeShouldNotBeFound("validityDays.in=" + UPDATED_VALIDITY_DAYS);
    }

    @Test
    @Transactional
    public void getAllLicenseTypesByValidityDaysIsNullOrNotNull() throws Exception {
        // Initialize the database
        licenseTypeRepository.saveAndFlush(licenseType);

        // Get all the licenseTypeList where validityDays is not null
        defaultLicenseTypeShouldBeFound("validityDays.specified=true");

        // Get all the licenseTypeList where validityDays is null
        defaultLicenseTypeShouldNotBeFound("validityDays.specified=false");
    }

    @Test
    @Transactional
    public void getAllLicenseTypesByValidityDaysIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        licenseTypeRepository.saveAndFlush(licenseType);

        // Get all the licenseTypeList where validityDays is greater than or equal to DEFAULT_VALIDITY_DAYS
        defaultLicenseTypeShouldBeFound("validityDays.greaterThanOrEqual=" + DEFAULT_VALIDITY_DAYS);

        // Get all the licenseTypeList where validityDays is greater than or equal to UPDATED_VALIDITY_DAYS
        defaultLicenseTypeShouldNotBeFound("validityDays.greaterThanOrEqual=" + UPDATED_VALIDITY_DAYS);
    }

    @Test
    @Transactional
    public void getAllLicenseTypesByValidityDaysIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        licenseTypeRepository.saveAndFlush(licenseType);

        // Get all the licenseTypeList where validityDays is less than or equal to DEFAULT_VALIDITY_DAYS
        defaultLicenseTypeShouldBeFound("validityDays.lessThanOrEqual=" + DEFAULT_VALIDITY_DAYS);

        // Get all the licenseTypeList where validityDays is less than or equal to SMALLER_VALIDITY_DAYS
        defaultLicenseTypeShouldNotBeFound("validityDays.lessThanOrEqual=" + SMALLER_VALIDITY_DAYS);
    }

    @Test
    @Transactional
    public void getAllLicenseTypesByValidityDaysIsLessThanSomething() throws Exception {
        // Initialize the database
        licenseTypeRepository.saveAndFlush(licenseType);

        // Get all the licenseTypeList where validityDays is less than DEFAULT_VALIDITY_DAYS
        defaultLicenseTypeShouldNotBeFound("validityDays.lessThan=" + DEFAULT_VALIDITY_DAYS);

        // Get all the licenseTypeList where validityDays is less than UPDATED_VALIDITY_DAYS
        defaultLicenseTypeShouldBeFound("validityDays.lessThan=" + UPDATED_VALIDITY_DAYS);
    }

    @Test
    @Transactional
    public void getAllLicenseTypesByValidityDaysIsGreaterThanSomething() throws Exception {
        // Initialize the database
        licenseTypeRepository.saveAndFlush(licenseType);

        // Get all the licenseTypeList where validityDays is greater than DEFAULT_VALIDITY_DAYS
        defaultLicenseTypeShouldNotBeFound("validityDays.greaterThan=" + DEFAULT_VALIDITY_DAYS);

        // Get all the licenseTypeList where validityDays is greater than SMALLER_VALIDITY_DAYS
        defaultLicenseTypeShouldBeFound("validityDays.greaterThan=" + SMALLER_VALIDITY_DAYS);
    }


    @Test
    @Transactional
    public void getAllLicenseTypesByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        licenseTypeRepository.saveAndFlush(licenseType);

        // Get all the licenseTypeList where isActive equals to DEFAULT_IS_ACTIVE
        defaultLicenseTypeShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the licenseTypeList where isActive equals to UPDATED_IS_ACTIVE
        defaultLicenseTypeShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllLicenseTypesByIsActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        licenseTypeRepository.saveAndFlush(licenseType);

        // Get all the licenseTypeList where isActive not equals to DEFAULT_IS_ACTIVE
        defaultLicenseTypeShouldNotBeFound("isActive.notEquals=" + DEFAULT_IS_ACTIVE);

        // Get all the licenseTypeList where isActive not equals to UPDATED_IS_ACTIVE
        defaultLicenseTypeShouldBeFound("isActive.notEquals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllLicenseTypesByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        licenseTypeRepository.saveAndFlush(licenseType);

        // Get all the licenseTypeList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultLicenseTypeShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the licenseTypeList where isActive equals to UPDATED_IS_ACTIVE
        defaultLicenseTypeShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllLicenseTypesByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        licenseTypeRepository.saveAndFlush(licenseType);

        // Get all the licenseTypeList where isActive is not null
        defaultLicenseTypeShouldBeFound("isActive.specified=true");

        // Get all the licenseTypeList where isActive is null
        defaultLicenseTypeShouldNotBeFound("isActive.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLicenseTypeShouldBeFound(String filter) throws Exception {
        restLicenseTypeMockMvc.perform(get("/api/license-types?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(licenseType.getId().intValue())))
            .andExpect(jsonPath("$.[*].licenseTypeCode").value(hasItem(DEFAULT_LICENSE_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].licenseTypeName").value(hasItem(DEFAULT_LICENSE_TYPE_NAME)))
            .andExpect(jsonPath("$.[*].validityDays").value(hasItem(DEFAULT_VALIDITY_DAYS.doubleValue())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restLicenseTypeMockMvc.perform(get("/api/license-types/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLicenseTypeShouldNotBeFound(String filter) throws Exception {
        restLicenseTypeMockMvc.perform(get("/api/license-types?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLicenseTypeMockMvc.perform(get("/api/license-types/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingLicenseType() throws Exception {
        // Get the licenseType
        restLicenseTypeMockMvc.perform(get("/api/license-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLicenseType() throws Exception {
        // Initialize the database
        licenseTypeService.save(licenseType);

        int databaseSizeBeforeUpdate = licenseTypeRepository.findAll().size();

        // Update the licenseType
        LicenseType updatedLicenseType = licenseTypeRepository.findById(licenseType.getId()).get();
        // Disconnect from session so that the updates on updatedLicenseType are not directly saved in db
        em.detach(updatedLicenseType);
        updatedLicenseType
            .licenseTypeCode(UPDATED_LICENSE_TYPE_CODE)
            .licenseTypeName(UPDATED_LICENSE_TYPE_NAME)
            .validityDays(UPDATED_VALIDITY_DAYS)
            .isActive(UPDATED_IS_ACTIVE);

        restLicenseTypeMockMvc.perform(put("/api/license-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedLicenseType)))
            .andExpect(status().isOk());

        // Validate the LicenseType in the database
        List<LicenseType> licenseTypeList = licenseTypeRepository.findAll();
        assertThat(licenseTypeList).hasSize(databaseSizeBeforeUpdate);
        LicenseType testLicenseType = licenseTypeList.get(licenseTypeList.size() - 1);
        assertThat(testLicenseType.getLicenseTypeCode()).isEqualTo(UPDATED_LICENSE_TYPE_CODE);
        assertThat(testLicenseType.getLicenseTypeName()).isEqualTo(UPDATED_LICENSE_TYPE_NAME);
        assertThat(testLicenseType.getValidityDays()).isEqualTo(UPDATED_VALIDITY_DAYS);
        assertThat(testLicenseType.isIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void updateNonExistingLicenseType() throws Exception {
        int databaseSizeBeforeUpdate = licenseTypeRepository.findAll().size();

        // Create the LicenseType

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLicenseTypeMockMvc.perform(put("/api/license-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(licenseType)))
            .andExpect(status().isBadRequest());

        // Validate the LicenseType in the database
        List<LicenseType> licenseTypeList = licenseTypeRepository.findAll();
        assertThat(licenseTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteLicenseType() throws Exception {
        // Initialize the database
        licenseTypeService.save(licenseType);

        int databaseSizeBeforeDelete = licenseTypeRepository.findAll().size();

        // Delete the licenseType
        restLicenseTypeMockMvc.perform(delete("/api/license-types/{id}", licenseType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LicenseType> licenseTypeList = licenseTypeRepository.findAll();
        assertThat(licenseTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.WikunumApp;
import com.alphadevs.sales.domain.EmployeeType;
import com.alphadevs.sales.repository.EmployeeTypeRepository;
import com.alphadevs.sales.service.EmployeeTypeService;
import com.alphadevs.sales.web.rest.errors.ExceptionTranslator;
import com.alphadevs.sales.service.dto.EmployeeTypeCriteria;
import com.alphadevs.sales.service.EmployeeTypeQueryService;

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
 * Integration tests for the {@link EmployeeTypeResource} REST controller.
 */
@SpringBootTest(classes = WikunumApp.class)
public class EmployeeTypeResourceIT {

    private static final String DEFAULT_EMPLOYEE_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_EMPLOYEE_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_EMPLOYEE_TYPE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_EMPLOYEE_TYPE_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    @Autowired
    private EmployeeTypeRepository employeeTypeRepository;

    @Autowired
    private EmployeeTypeService employeeTypeService;

    @Autowired
    private EmployeeTypeQueryService employeeTypeQueryService;

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

    private MockMvc restEmployeeTypeMockMvc;

    private EmployeeType employeeType;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EmployeeTypeResource employeeTypeResource = new EmployeeTypeResource(employeeTypeService, employeeTypeQueryService);
        this.restEmployeeTypeMockMvc = MockMvcBuilders.standaloneSetup(employeeTypeResource)
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
    public static EmployeeType createEntity(EntityManager em) {
        EmployeeType employeeType = new EmployeeType()
            .employeeTypeCode(DEFAULT_EMPLOYEE_TYPE_CODE)
            .employeeTypeName(DEFAULT_EMPLOYEE_TYPE_NAME)
            .isActive(DEFAULT_IS_ACTIVE);
        return employeeType;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmployeeType createUpdatedEntity(EntityManager em) {
        EmployeeType employeeType = new EmployeeType()
            .employeeTypeCode(UPDATED_EMPLOYEE_TYPE_CODE)
            .employeeTypeName(UPDATED_EMPLOYEE_TYPE_NAME)
            .isActive(UPDATED_IS_ACTIVE);
        return employeeType;
    }

    @BeforeEach
    public void initTest() {
        employeeType = createEntity(em);
    }

    @Test
    @Transactional
    public void createEmployeeType() throws Exception {
        int databaseSizeBeforeCreate = employeeTypeRepository.findAll().size();

        // Create the EmployeeType
        restEmployeeTypeMockMvc.perform(post("/api/employee-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employeeType)))
            .andExpect(status().isCreated());

        // Validate the EmployeeType in the database
        List<EmployeeType> employeeTypeList = employeeTypeRepository.findAll();
        assertThat(employeeTypeList).hasSize(databaseSizeBeforeCreate + 1);
        EmployeeType testEmployeeType = employeeTypeList.get(employeeTypeList.size() - 1);
        assertThat(testEmployeeType.getEmployeeTypeCode()).isEqualTo(DEFAULT_EMPLOYEE_TYPE_CODE);
        assertThat(testEmployeeType.getEmployeeTypeName()).isEqualTo(DEFAULT_EMPLOYEE_TYPE_NAME);
        assertThat(testEmployeeType.isIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void createEmployeeTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = employeeTypeRepository.findAll().size();

        // Create the EmployeeType with an existing ID
        employeeType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmployeeTypeMockMvc.perform(post("/api/employee-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employeeType)))
            .andExpect(status().isBadRequest());

        // Validate the EmployeeType in the database
        List<EmployeeType> employeeTypeList = employeeTypeRepository.findAll();
        assertThat(employeeTypeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkEmployeeTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeTypeRepository.findAll().size();
        // set the field null
        employeeType.setEmployeeTypeCode(null);

        // Create the EmployeeType, which fails.

        restEmployeeTypeMockMvc.perform(post("/api/employee-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employeeType)))
            .andExpect(status().isBadRequest());

        List<EmployeeType> employeeTypeList = employeeTypeRepository.findAll();
        assertThat(employeeTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmployeeTypeNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeTypeRepository.findAll().size();
        // set the field null
        employeeType.setEmployeeTypeName(null);

        // Create the EmployeeType, which fails.

        restEmployeeTypeMockMvc.perform(post("/api/employee-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employeeType)))
            .andExpect(status().isBadRequest());

        List<EmployeeType> employeeTypeList = employeeTypeRepository.findAll();
        assertThat(employeeTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEmployeeTypes() throws Exception {
        // Initialize the database
        employeeTypeRepository.saveAndFlush(employeeType);

        // Get all the employeeTypeList
        restEmployeeTypeMockMvc.perform(get("/api/employee-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employeeType.getId().intValue())))
            .andExpect(jsonPath("$.[*].employeeTypeCode").value(hasItem(DEFAULT_EMPLOYEE_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].employeeTypeName").value(hasItem(DEFAULT_EMPLOYEE_TYPE_NAME)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getEmployeeType() throws Exception {
        // Initialize the database
        employeeTypeRepository.saveAndFlush(employeeType);

        // Get the employeeType
        restEmployeeTypeMockMvc.perform(get("/api/employee-types/{id}", employeeType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(employeeType.getId().intValue()))
            .andExpect(jsonPath("$.employeeTypeCode").value(DEFAULT_EMPLOYEE_TYPE_CODE))
            .andExpect(jsonPath("$.employeeTypeName").value(DEFAULT_EMPLOYEE_TYPE_NAME))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }


    @Test
    @Transactional
    public void getEmployeeTypesByIdFiltering() throws Exception {
        // Initialize the database
        employeeTypeRepository.saveAndFlush(employeeType);

        Long id = employeeType.getId();

        defaultEmployeeTypeShouldBeFound("id.equals=" + id);
        defaultEmployeeTypeShouldNotBeFound("id.notEquals=" + id);

        defaultEmployeeTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEmployeeTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultEmployeeTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEmployeeTypeShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllEmployeeTypesByEmployeeTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeTypeRepository.saveAndFlush(employeeType);

        // Get all the employeeTypeList where employeeTypeCode equals to DEFAULT_EMPLOYEE_TYPE_CODE
        defaultEmployeeTypeShouldBeFound("employeeTypeCode.equals=" + DEFAULT_EMPLOYEE_TYPE_CODE);

        // Get all the employeeTypeList where employeeTypeCode equals to UPDATED_EMPLOYEE_TYPE_CODE
        defaultEmployeeTypeShouldNotBeFound("employeeTypeCode.equals=" + UPDATED_EMPLOYEE_TYPE_CODE);
    }

    @Test
    @Transactional
    public void getAllEmployeeTypesByEmployeeTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeTypeRepository.saveAndFlush(employeeType);

        // Get all the employeeTypeList where employeeTypeCode not equals to DEFAULT_EMPLOYEE_TYPE_CODE
        defaultEmployeeTypeShouldNotBeFound("employeeTypeCode.notEquals=" + DEFAULT_EMPLOYEE_TYPE_CODE);

        // Get all the employeeTypeList where employeeTypeCode not equals to UPDATED_EMPLOYEE_TYPE_CODE
        defaultEmployeeTypeShouldBeFound("employeeTypeCode.notEquals=" + UPDATED_EMPLOYEE_TYPE_CODE);
    }

    @Test
    @Transactional
    public void getAllEmployeeTypesByEmployeeTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        employeeTypeRepository.saveAndFlush(employeeType);

        // Get all the employeeTypeList where employeeTypeCode in DEFAULT_EMPLOYEE_TYPE_CODE or UPDATED_EMPLOYEE_TYPE_CODE
        defaultEmployeeTypeShouldBeFound("employeeTypeCode.in=" + DEFAULT_EMPLOYEE_TYPE_CODE + "," + UPDATED_EMPLOYEE_TYPE_CODE);

        // Get all the employeeTypeList where employeeTypeCode equals to UPDATED_EMPLOYEE_TYPE_CODE
        defaultEmployeeTypeShouldNotBeFound("employeeTypeCode.in=" + UPDATED_EMPLOYEE_TYPE_CODE);
    }

    @Test
    @Transactional
    public void getAllEmployeeTypesByEmployeeTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeTypeRepository.saveAndFlush(employeeType);

        // Get all the employeeTypeList where employeeTypeCode is not null
        defaultEmployeeTypeShouldBeFound("employeeTypeCode.specified=true");

        // Get all the employeeTypeList where employeeTypeCode is null
        defaultEmployeeTypeShouldNotBeFound("employeeTypeCode.specified=false");
    }
                @Test
    @Transactional
    public void getAllEmployeeTypesByEmployeeTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        employeeTypeRepository.saveAndFlush(employeeType);

        // Get all the employeeTypeList where employeeTypeCode contains DEFAULT_EMPLOYEE_TYPE_CODE
        defaultEmployeeTypeShouldBeFound("employeeTypeCode.contains=" + DEFAULT_EMPLOYEE_TYPE_CODE);

        // Get all the employeeTypeList where employeeTypeCode contains UPDATED_EMPLOYEE_TYPE_CODE
        defaultEmployeeTypeShouldNotBeFound("employeeTypeCode.contains=" + UPDATED_EMPLOYEE_TYPE_CODE);
    }

    @Test
    @Transactional
    public void getAllEmployeeTypesByEmployeeTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        employeeTypeRepository.saveAndFlush(employeeType);

        // Get all the employeeTypeList where employeeTypeCode does not contain DEFAULT_EMPLOYEE_TYPE_CODE
        defaultEmployeeTypeShouldNotBeFound("employeeTypeCode.doesNotContain=" + DEFAULT_EMPLOYEE_TYPE_CODE);

        // Get all the employeeTypeList where employeeTypeCode does not contain UPDATED_EMPLOYEE_TYPE_CODE
        defaultEmployeeTypeShouldBeFound("employeeTypeCode.doesNotContain=" + UPDATED_EMPLOYEE_TYPE_CODE);
    }


    @Test
    @Transactional
    public void getAllEmployeeTypesByEmployeeTypeNameIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeTypeRepository.saveAndFlush(employeeType);

        // Get all the employeeTypeList where employeeTypeName equals to DEFAULT_EMPLOYEE_TYPE_NAME
        defaultEmployeeTypeShouldBeFound("employeeTypeName.equals=" + DEFAULT_EMPLOYEE_TYPE_NAME);

        // Get all the employeeTypeList where employeeTypeName equals to UPDATED_EMPLOYEE_TYPE_NAME
        defaultEmployeeTypeShouldNotBeFound("employeeTypeName.equals=" + UPDATED_EMPLOYEE_TYPE_NAME);
    }

    @Test
    @Transactional
    public void getAllEmployeeTypesByEmployeeTypeNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeTypeRepository.saveAndFlush(employeeType);

        // Get all the employeeTypeList where employeeTypeName not equals to DEFAULT_EMPLOYEE_TYPE_NAME
        defaultEmployeeTypeShouldNotBeFound("employeeTypeName.notEquals=" + DEFAULT_EMPLOYEE_TYPE_NAME);

        // Get all the employeeTypeList where employeeTypeName not equals to UPDATED_EMPLOYEE_TYPE_NAME
        defaultEmployeeTypeShouldBeFound("employeeTypeName.notEquals=" + UPDATED_EMPLOYEE_TYPE_NAME);
    }

    @Test
    @Transactional
    public void getAllEmployeeTypesByEmployeeTypeNameIsInShouldWork() throws Exception {
        // Initialize the database
        employeeTypeRepository.saveAndFlush(employeeType);

        // Get all the employeeTypeList where employeeTypeName in DEFAULT_EMPLOYEE_TYPE_NAME or UPDATED_EMPLOYEE_TYPE_NAME
        defaultEmployeeTypeShouldBeFound("employeeTypeName.in=" + DEFAULT_EMPLOYEE_TYPE_NAME + "," + UPDATED_EMPLOYEE_TYPE_NAME);

        // Get all the employeeTypeList where employeeTypeName equals to UPDATED_EMPLOYEE_TYPE_NAME
        defaultEmployeeTypeShouldNotBeFound("employeeTypeName.in=" + UPDATED_EMPLOYEE_TYPE_NAME);
    }

    @Test
    @Transactional
    public void getAllEmployeeTypesByEmployeeTypeNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeTypeRepository.saveAndFlush(employeeType);

        // Get all the employeeTypeList where employeeTypeName is not null
        defaultEmployeeTypeShouldBeFound("employeeTypeName.specified=true");

        // Get all the employeeTypeList where employeeTypeName is null
        defaultEmployeeTypeShouldNotBeFound("employeeTypeName.specified=false");
    }
                @Test
    @Transactional
    public void getAllEmployeeTypesByEmployeeTypeNameContainsSomething() throws Exception {
        // Initialize the database
        employeeTypeRepository.saveAndFlush(employeeType);

        // Get all the employeeTypeList where employeeTypeName contains DEFAULT_EMPLOYEE_TYPE_NAME
        defaultEmployeeTypeShouldBeFound("employeeTypeName.contains=" + DEFAULT_EMPLOYEE_TYPE_NAME);

        // Get all the employeeTypeList where employeeTypeName contains UPDATED_EMPLOYEE_TYPE_NAME
        defaultEmployeeTypeShouldNotBeFound("employeeTypeName.contains=" + UPDATED_EMPLOYEE_TYPE_NAME);
    }

    @Test
    @Transactional
    public void getAllEmployeeTypesByEmployeeTypeNameNotContainsSomething() throws Exception {
        // Initialize the database
        employeeTypeRepository.saveAndFlush(employeeType);

        // Get all the employeeTypeList where employeeTypeName does not contain DEFAULT_EMPLOYEE_TYPE_NAME
        defaultEmployeeTypeShouldNotBeFound("employeeTypeName.doesNotContain=" + DEFAULT_EMPLOYEE_TYPE_NAME);

        // Get all the employeeTypeList where employeeTypeName does not contain UPDATED_EMPLOYEE_TYPE_NAME
        defaultEmployeeTypeShouldBeFound("employeeTypeName.doesNotContain=" + UPDATED_EMPLOYEE_TYPE_NAME);
    }


    @Test
    @Transactional
    public void getAllEmployeeTypesByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeTypeRepository.saveAndFlush(employeeType);

        // Get all the employeeTypeList where isActive equals to DEFAULT_IS_ACTIVE
        defaultEmployeeTypeShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the employeeTypeList where isActive equals to UPDATED_IS_ACTIVE
        defaultEmployeeTypeShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllEmployeeTypesByIsActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeTypeRepository.saveAndFlush(employeeType);

        // Get all the employeeTypeList where isActive not equals to DEFAULT_IS_ACTIVE
        defaultEmployeeTypeShouldNotBeFound("isActive.notEquals=" + DEFAULT_IS_ACTIVE);

        // Get all the employeeTypeList where isActive not equals to UPDATED_IS_ACTIVE
        defaultEmployeeTypeShouldBeFound("isActive.notEquals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllEmployeeTypesByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        employeeTypeRepository.saveAndFlush(employeeType);

        // Get all the employeeTypeList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultEmployeeTypeShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the employeeTypeList where isActive equals to UPDATED_IS_ACTIVE
        defaultEmployeeTypeShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllEmployeeTypesByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeTypeRepository.saveAndFlush(employeeType);

        // Get all the employeeTypeList where isActive is not null
        defaultEmployeeTypeShouldBeFound("isActive.specified=true");

        // Get all the employeeTypeList where isActive is null
        defaultEmployeeTypeShouldNotBeFound("isActive.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEmployeeTypeShouldBeFound(String filter) throws Exception {
        restEmployeeTypeMockMvc.perform(get("/api/employee-types?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employeeType.getId().intValue())))
            .andExpect(jsonPath("$.[*].employeeTypeCode").value(hasItem(DEFAULT_EMPLOYEE_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].employeeTypeName").value(hasItem(DEFAULT_EMPLOYEE_TYPE_NAME)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restEmployeeTypeMockMvc.perform(get("/api/employee-types/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEmployeeTypeShouldNotBeFound(String filter) throws Exception {
        restEmployeeTypeMockMvc.perform(get("/api/employee-types?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEmployeeTypeMockMvc.perform(get("/api/employee-types/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingEmployeeType() throws Exception {
        // Get the employeeType
        restEmployeeTypeMockMvc.perform(get("/api/employee-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEmployeeType() throws Exception {
        // Initialize the database
        employeeTypeService.save(employeeType);

        int databaseSizeBeforeUpdate = employeeTypeRepository.findAll().size();

        // Update the employeeType
        EmployeeType updatedEmployeeType = employeeTypeRepository.findById(employeeType.getId()).get();
        // Disconnect from session so that the updates on updatedEmployeeType are not directly saved in db
        em.detach(updatedEmployeeType);
        updatedEmployeeType
            .employeeTypeCode(UPDATED_EMPLOYEE_TYPE_CODE)
            .employeeTypeName(UPDATED_EMPLOYEE_TYPE_NAME)
            .isActive(UPDATED_IS_ACTIVE);

        restEmployeeTypeMockMvc.perform(put("/api/employee-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEmployeeType)))
            .andExpect(status().isOk());

        // Validate the EmployeeType in the database
        List<EmployeeType> employeeTypeList = employeeTypeRepository.findAll();
        assertThat(employeeTypeList).hasSize(databaseSizeBeforeUpdate);
        EmployeeType testEmployeeType = employeeTypeList.get(employeeTypeList.size() - 1);
        assertThat(testEmployeeType.getEmployeeTypeCode()).isEqualTo(UPDATED_EMPLOYEE_TYPE_CODE);
        assertThat(testEmployeeType.getEmployeeTypeName()).isEqualTo(UPDATED_EMPLOYEE_TYPE_NAME);
        assertThat(testEmployeeType.isIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void updateNonExistingEmployeeType() throws Exception {
        int databaseSizeBeforeUpdate = employeeTypeRepository.findAll().size();

        // Create the EmployeeType

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeTypeMockMvc.perform(put("/api/employee-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employeeType)))
            .andExpect(status().isBadRequest());

        // Validate the EmployeeType in the database
        List<EmployeeType> employeeTypeList = employeeTypeRepository.findAll();
        assertThat(employeeTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEmployeeType() throws Exception {
        // Initialize the database
        employeeTypeService.save(employeeType);

        int databaseSizeBeforeDelete = employeeTypeRepository.findAll().size();

        // Delete the employeeType
        restEmployeeTypeMockMvc.perform(delete("/api/employee-types/{id}", employeeType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EmployeeType> employeeTypeList = employeeTypeRepository.findAll();
        assertThat(employeeTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

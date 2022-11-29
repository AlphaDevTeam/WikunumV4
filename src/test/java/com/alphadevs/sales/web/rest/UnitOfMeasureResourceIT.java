package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.WikunumApp;
import com.alphadevs.sales.domain.UnitOfMeasure;
import com.alphadevs.sales.repository.UnitOfMeasureRepository;
import com.alphadevs.sales.service.UnitOfMeasureService;
import com.alphadevs.sales.web.rest.errors.ExceptionTranslator;
import com.alphadevs.sales.service.dto.UnitOfMeasureCriteria;
import com.alphadevs.sales.service.UnitOfMeasureQueryService;

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
 * Integration tests for the {@link UnitOfMeasureResource} REST controller.
 */
@SpringBootTest(classes = WikunumApp.class)
public class UnitOfMeasureResourceIT {

    private static final String DEFAULT_UNIT_OF_MEASURE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_UNIT_OF_MEASURE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_UNIT_OF_MEASURE_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_UNIT_OF_MEASURE_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    @Autowired
    private UnitOfMeasureRepository unitOfMeasureRepository;

    @Autowired
    private UnitOfMeasureService unitOfMeasureService;

    @Autowired
    private UnitOfMeasureQueryService unitOfMeasureQueryService;

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

    private MockMvc restUnitOfMeasureMockMvc;

    private UnitOfMeasure unitOfMeasure;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UnitOfMeasureResource unitOfMeasureResource = new UnitOfMeasureResource(unitOfMeasureService, unitOfMeasureQueryService);
        this.restUnitOfMeasureMockMvc = MockMvcBuilders.standaloneSetup(unitOfMeasureResource)
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
    public static UnitOfMeasure createEntity(EntityManager em) {
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure()
            .unitOfMeasureCode(DEFAULT_UNIT_OF_MEASURE_CODE)
            .unitOfMeasureDescription(DEFAULT_UNIT_OF_MEASURE_DESCRIPTION)
            .isActive(DEFAULT_IS_ACTIVE);
        return unitOfMeasure;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UnitOfMeasure createUpdatedEntity(EntityManager em) {
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure()
            .unitOfMeasureCode(UPDATED_UNIT_OF_MEASURE_CODE)
            .unitOfMeasureDescription(UPDATED_UNIT_OF_MEASURE_DESCRIPTION)
            .isActive(UPDATED_IS_ACTIVE);
        return unitOfMeasure;
    }

    @BeforeEach
    public void initTest() {
        unitOfMeasure = createEntity(em);
    }

    @Test
    @Transactional
    public void createUnitOfMeasure() throws Exception {
        int databaseSizeBeforeCreate = unitOfMeasureRepository.findAll().size();

        // Create the UnitOfMeasure
        restUnitOfMeasureMockMvc.perform(post("/api/unit-of-measures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(unitOfMeasure)))
            .andExpect(status().isCreated());

        // Validate the UnitOfMeasure in the database
        List<UnitOfMeasure> unitOfMeasureList = unitOfMeasureRepository.findAll();
        assertThat(unitOfMeasureList).hasSize(databaseSizeBeforeCreate + 1);
        UnitOfMeasure testUnitOfMeasure = unitOfMeasureList.get(unitOfMeasureList.size() - 1);
        assertThat(testUnitOfMeasure.getUnitOfMeasureCode()).isEqualTo(DEFAULT_UNIT_OF_MEASURE_CODE);
        assertThat(testUnitOfMeasure.getUnitOfMeasureDescription()).isEqualTo(DEFAULT_UNIT_OF_MEASURE_DESCRIPTION);
        assertThat(testUnitOfMeasure.isIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void createUnitOfMeasureWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = unitOfMeasureRepository.findAll().size();

        // Create the UnitOfMeasure with an existing ID
        unitOfMeasure.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUnitOfMeasureMockMvc.perform(post("/api/unit-of-measures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(unitOfMeasure)))
            .andExpect(status().isBadRequest());

        // Validate the UnitOfMeasure in the database
        List<UnitOfMeasure> unitOfMeasureList = unitOfMeasureRepository.findAll();
        assertThat(unitOfMeasureList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkUnitOfMeasureCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = unitOfMeasureRepository.findAll().size();
        // set the field null
        unitOfMeasure.setUnitOfMeasureCode(null);

        // Create the UnitOfMeasure, which fails.

        restUnitOfMeasureMockMvc.perform(post("/api/unit-of-measures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(unitOfMeasure)))
            .andExpect(status().isBadRequest());

        List<UnitOfMeasure> unitOfMeasureList = unitOfMeasureRepository.findAll();
        assertThat(unitOfMeasureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUnitOfMeasureDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = unitOfMeasureRepository.findAll().size();
        // set the field null
        unitOfMeasure.setUnitOfMeasureDescription(null);

        // Create the UnitOfMeasure, which fails.

        restUnitOfMeasureMockMvc.perform(post("/api/unit-of-measures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(unitOfMeasure)))
            .andExpect(status().isBadRequest());

        List<UnitOfMeasure> unitOfMeasureList = unitOfMeasureRepository.findAll();
        assertThat(unitOfMeasureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUnitOfMeasures() throws Exception {
        // Initialize the database
        unitOfMeasureRepository.saveAndFlush(unitOfMeasure);

        // Get all the unitOfMeasureList
        restUnitOfMeasureMockMvc.perform(get("/api/unit-of-measures?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(unitOfMeasure.getId().intValue())))
            .andExpect(jsonPath("$.[*].unitOfMeasureCode").value(hasItem(DEFAULT_UNIT_OF_MEASURE_CODE)))
            .andExpect(jsonPath("$.[*].unitOfMeasureDescription").value(hasItem(DEFAULT_UNIT_OF_MEASURE_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getUnitOfMeasure() throws Exception {
        // Initialize the database
        unitOfMeasureRepository.saveAndFlush(unitOfMeasure);

        // Get the unitOfMeasure
        restUnitOfMeasureMockMvc.perform(get("/api/unit-of-measures/{id}", unitOfMeasure.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(unitOfMeasure.getId().intValue()))
            .andExpect(jsonPath("$.unitOfMeasureCode").value(DEFAULT_UNIT_OF_MEASURE_CODE))
            .andExpect(jsonPath("$.unitOfMeasureDescription").value(DEFAULT_UNIT_OF_MEASURE_DESCRIPTION))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }


    @Test
    @Transactional
    public void getUnitOfMeasuresByIdFiltering() throws Exception {
        // Initialize the database
        unitOfMeasureRepository.saveAndFlush(unitOfMeasure);

        Long id = unitOfMeasure.getId();

        defaultUnitOfMeasureShouldBeFound("id.equals=" + id);
        defaultUnitOfMeasureShouldNotBeFound("id.notEquals=" + id);

        defaultUnitOfMeasureShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUnitOfMeasureShouldNotBeFound("id.greaterThan=" + id);

        defaultUnitOfMeasureShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUnitOfMeasureShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllUnitOfMeasuresByUnitOfMeasureCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        unitOfMeasureRepository.saveAndFlush(unitOfMeasure);

        // Get all the unitOfMeasureList where unitOfMeasureCode equals to DEFAULT_UNIT_OF_MEASURE_CODE
        defaultUnitOfMeasureShouldBeFound("unitOfMeasureCode.equals=" + DEFAULT_UNIT_OF_MEASURE_CODE);

        // Get all the unitOfMeasureList where unitOfMeasureCode equals to UPDATED_UNIT_OF_MEASURE_CODE
        defaultUnitOfMeasureShouldNotBeFound("unitOfMeasureCode.equals=" + UPDATED_UNIT_OF_MEASURE_CODE);
    }

    @Test
    @Transactional
    public void getAllUnitOfMeasuresByUnitOfMeasureCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        unitOfMeasureRepository.saveAndFlush(unitOfMeasure);

        // Get all the unitOfMeasureList where unitOfMeasureCode not equals to DEFAULT_UNIT_OF_MEASURE_CODE
        defaultUnitOfMeasureShouldNotBeFound("unitOfMeasureCode.notEquals=" + DEFAULT_UNIT_OF_MEASURE_CODE);

        // Get all the unitOfMeasureList where unitOfMeasureCode not equals to UPDATED_UNIT_OF_MEASURE_CODE
        defaultUnitOfMeasureShouldBeFound("unitOfMeasureCode.notEquals=" + UPDATED_UNIT_OF_MEASURE_CODE);
    }

    @Test
    @Transactional
    public void getAllUnitOfMeasuresByUnitOfMeasureCodeIsInShouldWork() throws Exception {
        // Initialize the database
        unitOfMeasureRepository.saveAndFlush(unitOfMeasure);

        // Get all the unitOfMeasureList where unitOfMeasureCode in DEFAULT_UNIT_OF_MEASURE_CODE or UPDATED_UNIT_OF_MEASURE_CODE
        defaultUnitOfMeasureShouldBeFound("unitOfMeasureCode.in=" + DEFAULT_UNIT_OF_MEASURE_CODE + "," + UPDATED_UNIT_OF_MEASURE_CODE);

        // Get all the unitOfMeasureList where unitOfMeasureCode equals to UPDATED_UNIT_OF_MEASURE_CODE
        defaultUnitOfMeasureShouldNotBeFound("unitOfMeasureCode.in=" + UPDATED_UNIT_OF_MEASURE_CODE);
    }

    @Test
    @Transactional
    public void getAllUnitOfMeasuresByUnitOfMeasureCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        unitOfMeasureRepository.saveAndFlush(unitOfMeasure);

        // Get all the unitOfMeasureList where unitOfMeasureCode is not null
        defaultUnitOfMeasureShouldBeFound("unitOfMeasureCode.specified=true");

        // Get all the unitOfMeasureList where unitOfMeasureCode is null
        defaultUnitOfMeasureShouldNotBeFound("unitOfMeasureCode.specified=false");
    }
                @Test
    @Transactional
    public void getAllUnitOfMeasuresByUnitOfMeasureCodeContainsSomething() throws Exception {
        // Initialize the database
        unitOfMeasureRepository.saveAndFlush(unitOfMeasure);

        // Get all the unitOfMeasureList where unitOfMeasureCode contains DEFAULT_UNIT_OF_MEASURE_CODE
        defaultUnitOfMeasureShouldBeFound("unitOfMeasureCode.contains=" + DEFAULT_UNIT_OF_MEASURE_CODE);

        // Get all the unitOfMeasureList where unitOfMeasureCode contains UPDATED_UNIT_OF_MEASURE_CODE
        defaultUnitOfMeasureShouldNotBeFound("unitOfMeasureCode.contains=" + UPDATED_UNIT_OF_MEASURE_CODE);
    }

    @Test
    @Transactional
    public void getAllUnitOfMeasuresByUnitOfMeasureCodeNotContainsSomething() throws Exception {
        // Initialize the database
        unitOfMeasureRepository.saveAndFlush(unitOfMeasure);

        // Get all the unitOfMeasureList where unitOfMeasureCode does not contain DEFAULT_UNIT_OF_MEASURE_CODE
        defaultUnitOfMeasureShouldNotBeFound("unitOfMeasureCode.doesNotContain=" + DEFAULT_UNIT_OF_MEASURE_CODE);

        // Get all the unitOfMeasureList where unitOfMeasureCode does not contain UPDATED_UNIT_OF_MEASURE_CODE
        defaultUnitOfMeasureShouldBeFound("unitOfMeasureCode.doesNotContain=" + UPDATED_UNIT_OF_MEASURE_CODE);
    }


    @Test
    @Transactional
    public void getAllUnitOfMeasuresByUnitOfMeasureDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        unitOfMeasureRepository.saveAndFlush(unitOfMeasure);

        // Get all the unitOfMeasureList where unitOfMeasureDescription equals to DEFAULT_UNIT_OF_MEASURE_DESCRIPTION
        defaultUnitOfMeasureShouldBeFound("unitOfMeasureDescription.equals=" + DEFAULT_UNIT_OF_MEASURE_DESCRIPTION);

        // Get all the unitOfMeasureList where unitOfMeasureDescription equals to UPDATED_UNIT_OF_MEASURE_DESCRIPTION
        defaultUnitOfMeasureShouldNotBeFound("unitOfMeasureDescription.equals=" + UPDATED_UNIT_OF_MEASURE_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllUnitOfMeasuresByUnitOfMeasureDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        unitOfMeasureRepository.saveAndFlush(unitOfMeasure);

        // Get all the unitOfMeasureList where unitOfMeasureDescription not equals to DEFAULT_UNIT_OF_MEASURE_DESCRIPTION
        defaultUnitOfMeasureShouldNotBeFound("unitOfMeasureDescription.notEquals=" + DEFAULT_UNIT_OF_MEASURE_DESCRIPTION);

        // Get all the unitOfMeasureList where unitOfMeasureDescription not equals to UPDATED_UNIT_OF_MEASURE_DESCRIPTION
        defaultUnitOfMeasureShouldBeFound("unitOfMeasureDescription.notEquals=" + UPDATED_UNIT_OF_MEASURE_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllUnitOfMeasuresByUnitOfMeasureDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        unitOfMeasureRepository.saveAndFlush(unitOfMeasure);

        // Get all the unitOfMeasureList where unitOfMeasureDescription in DEFAULT_UNIT_OF_MEASURE_DESCRIPTION or UPDATED_UNIT_OF_MEASURE_DESCRIPTION
        defaultUnitOfMeasureShouldBeFound("unitOfMeasureDescription.in=" + DEFAULT_UNIT_OF_MEASURE_DESCRIPTION + "," + UPDATED_UNIT_OF_MEASURE_DESCRIPTION);

        // Get all the unitOfMeasureList where unitOfMeasureDescription equals to UPDATED_UNIT_OF_MEASURE_DESCRIPTION
        defaultUnitOfMeasureShouldNotBeFound("unitOfMeasureDescription.in=" + UPDATED_UNIT_OF_MEASURE_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllUnitOfMeasuresByUnitOfMeasureDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        unitOfMeasureRepository.saveAndFlush(unitOfMeasure);

        // Get all the unitOfMeasureList where unitOfMeasureDescription is not null
        defaultUnitOfMeasureShouldBeFound("unitOfMeasureDescription.specified=true");

        // Get all the unitOfMeasureList where unitOfMeasureDescription is null
        defaultUnitOfMeasureShouldNotBeFound("unitOfMeasureDescription.specified=false");
    }
                @Test
    @Transactional
    public void getAllUnitOfMeasuresByUnitOfMeasureDescriptionContainsSomething() throws Exception {
        // Initialize the database
        unitOfMeasureRepository.saveAndFlush(unitOfMeasure);

        // Get all the unitOfMeasureList where unitOfMeasureDescription contains DEFAULT_UNIT_OF_MEASURE_DESCRIPTION
        defaultUnitOfMeasureShouldBeFound("unitOfMeasureDescription.contains=" + DEFAULT_UNIT_OF_MEASURE_DESCRIPTION);

        // Get all the unitOfMeasureList where unitOfMeasureDescription contains UPDATED_UNIT_OF_MEASURE_DESCRIPTION
        defaultUnitOfMeasureShouldNotBeFound("unitOfMeasureDescription.contains=" + UPDATED_UNIT_OF_MEASURE_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllUnitOfMeasuresByUnitOfMeasureDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        unitOfMeasureRepository.saveAndFlush(unitOfMeasure);

        // Get all the unitOfMeasureList where unitOfMeasureDescription does not contain DEFAULT_UNIT_OF_MEASURE_DESCRIPTION
        defaultUnitOfMeasureShouldNotBeFound("unitOfMeasureDescription.doesNotContain=" + DEFAULT_UNIT_OF_MEASURE_DESCRIPTION);

        // Get all the unitOfMeasureList where unitOfMeasureDescription does not contain UPDATED_UNIT_OF_MEASURE_DESCRIPTION
        defaultUnitOfMeasureShouldBeFound("unitOfMeasureDescription.doesNotContain=" + UPDATED_UNIT_OF_MEASURE_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllUnitOfMeasuresByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        unitOfMeasureRepository.saveAndFlush(unitOfMeasure);

        // Get all the unitOfMeasureList where isActive equals to DEFAULT_IS_ACTIVE
        defaultUnitOfMeasureShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the unitOfMeasureList where isActive equals to UPDATED_IS_ACTIVE
        defaultUnitOfMeasureShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllUnitOfMeasuresByIsActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        unitOfMeasureRepository.saveAndFlush(unitOfMeasure);

        // Get all the unitOfMeasureList where isActive not equals to DEFAULT_IS_ACTIVE
        defaultUnitOfMeasureShouldNotBeFound("isActive.notEquals=" + DEFAULT_IS_ACTIVE);

        // Get all the unitOfMeasureList where isActive not equals to UPDATED_IS_ACTIVE
        defaultUnitOfMeasureShouldBeFound("isActive.notEquals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllUnitOfMeasuresByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        unitOfMeasureRepository.saveAndFlush(unitOfMeasure);

        // Get all the unitOfMeasureList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultUnitOfMeasureShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the unitOfMeasureList where isActive equals to UPDATED_IS_ACTIVE
        defaultUnitOfMeasureShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllUnitOfMeasuresByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        unitOfMeasureRepository.saveAndFlush(unitOfMeasure);

        // Get all the unitOfMeasureList where isActive is not null
        defaultUnitOfMeasureShouldBeFound("isActive.specified=true");

        // Get all the unitOfMeasureList where isActive is null
        defaultUnitOfMeasureShouldNotBeFound("isActive.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUnitOfMeasureShouldBeFound(String filter) throws Exception {
        restUnitOfMeasureMockMvc.perform(get("/api/unit-of-measures?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(unitOfMeasure.getId().intValue())))
            .andExpect(jsonPath("$.[*].unitOfMeasureCode").value(hasItem(DEFAULT_UNIT_OF_MEASURE_CODE)))
            .andExpect(jsonPath("$.[*].unitOfMeasureDescription").value(hasItem(DEFAULT_UNIT_OF_MEASURE_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restUnitOfMeasureMockMvc.perform(get("/api/unit-of-measures/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUnitOfMeasureShouldNotBeFound(String filter) throws Exception {
        restUnitOfMeasureMockMvc.perform(get("/api/unit-of-measures?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUnitOfMeasureMockMvc.perform(get("/api/unit-of-measures/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingUnitOfMeasure() throws Exception {
        // Get the unitOfMeasure
        restUnitOfMeasureMockMvc.perform(get("/api/unit-of-measures/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUnitOfMeasure() throws Exception {
        // Initialize the database
        unitOfMeasureService.save(unitOfMeasure);

        int databaseSizeBeforeUpdate = unitOfMeasureRepository.findAll().size();

        // Update the unitOfMeasure
        UnitOfMeasure updatedUnitOfMeasure = unitOfMeasureRepository.findById(unitOfMeasure.getId()).get();
        // Disconnect from session so that the updates on updatedUnitOfMeasure are not directly saved in db
        em.detach(updatedUnitOfMeasure);
        updatedUnitOfMeasure
            .unitOfMeasureCode(UPDATED_UNIT_OF_MEASURE_CODE)
            .unitOfMeasureDescription(UPDATED_UNIT_OF_MEASURE_DESCRIPTION)
            .isActive(UPDATED_IS_ACTIVE);

        restUnitOfMeasureMockMvc.perform(put("/api/unit-of-measures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUnitOfMeasure)))
            .andExpect(status().isOk());

        // Validate the UnitOfMeasure in the database
        List<UnitOfMeasure> unitOfMeasureList = unitOfMeasureRepository.findAll();
        assertThat(unitOfMeasureList).hasSize(databaseSizeBeforeUpdate);
        UnitOfMeasure testUnitOfMeasure = unitOfMeasureList.get(unitOfMeasureList.size() - 1);
        assertThat(testUnitOfMeasure.getUnitOfMeasureCode()).isEqualTo(UPDATED_UNIT_OF_MEASURE_CODE);
        assertThat(testUnitOfMeasure.getUnitOfMeasureDescription()).isEqualTo(UPDATED_UNIT_OF_MEASURE_DESCRIPTION);
        assertThat(testUnitOfMeasure.isIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void updateNonExistingUnitOfMeasure() throws Exception {
        int databaseSizeBeforeUpdate = unitOfMeasureRepository.findAll().size();

        // Create the UnitOfMeasure

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUnitOfMeasureMockMvc.perform(put("/api/unit-of-measures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(unitOfMeasure)))
            .andExpect(status().isBadRequest());

        // Validate the UnitOfMeasure in the database
        List<UnitOfMeasure> unitOfMeasureList = unitOfMeasureRepository.findAll();
        assertThat(unitOfMeasureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteUnitOfMeasure() throws Exception {
        // Initialize the database
        unitOfMeasureService.save(unitOfMeasure);

        int databaseSizeBeforeDelete = unitOfMeasureRepository.findAll().size();

        // Delete the unitOfMeasure
        restUnitOfMeasureMockMvc.perform(delete("/api/unit-of-measures/{id}", unitOfMeasure.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UnitOfMeasure> unitOfMeasureList = unitOfMeasureRepository.findAll();
        assertThat(unitOfMeasureList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

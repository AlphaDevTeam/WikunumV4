package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.WikunumApp;
import com.alphadevs.sales.domain.TransactionType;
import com.alphadevs.sales.domain.DocumentHistory;
import com.alphadevs.sales.repository.TransactionTypeRepository;
import com.alphadevs.sales.service.TransactionTypeService;
import com.alphadevs.sales.web.rest.errors.ExceptionTranslator;
import com.alphadevs.sales.service.dto.TransactionTypeCriteria;
import com.alphadevs.sales.service.TransactionTypeQueryService;

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
 * Integration tests for the {@link TransactionTypeResource} REST controller.
 */
@SpringBootTest(classes = WikunumApp.class)
public class TransactionTypeResourceIT {

    private static final String DEFAULT_TRANSACTION_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_TRANSACTION_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_TRANSACTION_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TRANSACTION_TYPE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    @Autowired
    private TransactionTypeRepository transactionTypeRepository;

    @Autowired
    private TransactionTypeService transactionTypeService;

    @Autowired
    private TransactionTypeQueryService transactionTypeQueryService;

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

    private MockMvc restTransactionTypeMockMvc;

    private TransactionType transactionType;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TransactionTypeResource transactionTypeResource = new TransactionTypeResource(transactionTypeService, transactionTypeQueryService);
        this.restTransactionTypeMockMvc = MockMvcBuilders.standaloneSetup(transactionTypeResource)
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
    public static TransactionType createEntity(EntityManager em) {
        TransactionType transactionType = new TransactionType()
            .transactionTypeCode(DEFAULT_TRANSACTION_TYPE_CODE)
            .transactionType(DEFAULT_TRANSACTION_TYPE)
            .isActive(DEFAULT_IS_ACTIVE);
        return transactionType;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransactionType createUpdatedEntity(EntityManager em) {
        TransactionType transactionType = new TransactionType()
            .transactionTypeCode(UPDATED_TRANSACTION_TYPE_CODE)
            .transactionType(UPDATED_TRANSACTION_TYPE)
            .isActive(UPDATED_IS_ACTIVE);
        return transactionType;
    }

    @BeforeEach
    public void initTest() {
        transactionType = createEntity(em);
    }

    @Test
    @Transactional
    public void createTransactionType() throws Exception {
        int databaseSizeBeforeCreate = transactionTypeRepository.findAll().size();

        // Create the TransactionType
        restTransactionTypeMockMvc.perform(post("/api/transaction-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionType)))
            .andExpect(status().isCreated());

        // Validate the TransactionType in the database
        List<TransactionType> transactionTypeList = transactionTypeRepository.findAll();
        assertThat(transactionTypeList).hasSize(databaseSizeBeforeCreate + 1);
        TransactionType testTransactionType = transactionTypeList.get(transactionTypeList.size() - 1);
        assertThat(testTransactionType.getTransactionTypeCode()).isEqualTo(DEFAULT_TRANSACTION_TYPE_CODE);
        assertThat(testTransactionType.getTransactionType()).isEqualTo(DEFAULT_TRANSACTION_TYPE);
        assertThat(testTransactionType.isIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void createTransactionTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = transactionTypeRepository.findAll().size();

        // Create the TransactionType with an existing ID
        transactionType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransactionTypeMockMvc.perform(post("/api/transaction-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionType)))
            .andExpect(status().isBadRequest());

        // Validate the TransactionType in the database
        List<TransactionType> transactionTypeList = transactionTypeRepository.findAll();
        assertThat(transactionTypeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTransactionTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = transactionTypeRepository.findAll().size();
        // set the field null
        transactionType.setTransactionTypeCode(null);

        // Create the TransactionType, which fails.

        restTransactionTypeMockMvc.perform(post("/api/transaction-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionType)))
            .andExpect(status().isBadRequest());

        List<TransactionType> transactionTypeList = transactionTypeRepository.findAll();
        assertThat(transactionTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTransactionTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = transactionTypeRepository.findAll().size();
        // set the field null
        transactionType.setTransactionType(null);

        // Create the TransactionType, which fails.

        restTransactionTypeMockMvc.perform(post("/api/transaction-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionType)))
            .andExpect(status().isBadRequest());

        List<TransactionType> transactionTypeList = transactionTypeRepository.findAll();
        assertThat(transactionTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTransactionTypes() throws Exception {
        // Initialize the database
        transactionTypeRepository.saveAndFlush(transactionType);

        // Get all the transactionTypeList
        restTransactionTypeMockMvc.perform(get("/api/transaction-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactionType.getId().intValue())))
            .andExpect(jsonPath("$.[*].transactionTypeCode").value(hasItem(DEFAULT_TRANSACTION_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].transactionType").value(hasItem(DEFAULT_TRANSACTION_TYPE)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getTransactionType() throws Exception {
        // Initialize the database
        transactionTypeRepository.saveAndFlush(transactionType);

        // Get the transactionType
        restTransactionTypeMockMvc.perform(get("/api/transaction-types/{id}", transactionType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(transactionType.getId().intValue()))
            .andExpect(jsonPath("$.transactionTypeCode").value(DEFAULT_TRANSACTION_TYPE_CODE))
            .andExpect(jsonPath("$.transactionType").value(DEFAULT_TRANSACTION_TYPE))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }


    @Test
    @Transactional
    public void getTransactionTypesByIdFiltering() throws Exception {
        // Initialize the database
        transactionTypeRepository.saveAndFlush(transactionType);

        Long id = transactionType.getId();

        defaultTransactionTypeShouldBeFound("id.equals=" + id);
        defaultTransactionTypeShouldNotBeFound("id.notEquals=" + id);

        defaultTransactionTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTransactionTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultTransactionTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTransactionTypeShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllTransactionTypesByTransactionTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionTypeRepository.saveAndFlush(transactionType);

        // Get all the transactionTypeList where transactionTypeCode equals to DEFAULT_TRANSACTION_TYPE_CODE
        defaultTransactionTypeShouldBeFound("transactionTypeCode.equals=" + DEFAULT_TRANSACTION_TYPE_CODE);

        // Get all the transactionTypeList where transactionTypeCode equals to UPDATED_TRANSACTION_TYPE_CODE
        defaultTransactionTypeShouldNotBeFound("transactionTypeCode.equals=" + UPDATED_TRANSACTION_TYPE_CODE);
    }

    @Test
    @Transactional
    public void getAllTransactionTypesByTransactionTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transactionTypeRepository.saveAndFlush(transactionType);

        // Get all the transactionTypeList where transactionTypeCode not equals to DEFAULT_TRANSACTION_TYPE_CODE
        defaultTransactionTypeShouldNotBeFound("transactionTypeCode.notEquals=" + DEFAULT_TRANSACTION_TYPE_CODE);

        // Get all the transactionTypeList where transactionTypeCode not equals to UPDATED_TRANSACTION_TYPE_CODE
        defaultTransactionTypeShouldBeFound("transactionTypeCode.notEquals=" + UPDATED_TRANSACTION_TYPE_CODE);
    }

    @Test
    @Transactional
    public void getAllTransactionTypesByTransactionTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        transactionTypeRepository.saveAndFlush(transactionType);

        // Get all the transactionTypeList where transactionTypeCode in DEFAULT_TRANSACTION_TYPE_CODE or UPDATED_TRANSACTION_TYPE_CODE
        defaultTransactionTypeShouldBeFound("transactionTypeCode.in=" + DEFAULT_TRANSACTION_TYPE_CODE + "," + UPDATED_TRANSACTION_TYPE_CODE);

        // Get all the transactionTypeList where transactionTypeCode equals to UPDATED_TRANSACTION_TYPE_CODE
        defaultTransactionTypeShouldNotBeFound("transactionTypeCode.in=" + UPDATED_TRANSACTION_TYPE_CODE);
    }

    @Test
    @Transactional
    public void getAllTransactionTypesByTransactionTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionTypeRepository.saveAndFlush(transactionType);

        // Get all the transactionTypeList where transactionTypeCode is not null
        defaultTransactionTypeShouldBeFound("transactionTypeCode.specified=true");

        // Get all the transactionTypeList where transactionTypeCode is null
        defaultTransactionTypeShouldNotBeFound("transactionTypeCode.specified=false");
    }
                @Test
    @Transactional
    public void getAllTransactionTypesByTransactionTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        transactionTypeRepository.saveAndFlush(transactionType);

        // Get all the transactionTypeList where transactionTypeCode contains DEFAULT_TRANSACTION_TYPE_CODE
        defaultTransactionTypeShouldBeFound("transactionTypeCode.contains=" + DEFAULT_TRANSACTION_TYPE_CODE);

        // Get all the transactionTypeList where transactionTypeCode contains UPDATED_TRANSACTION_TYPE_CODE
        defaultTransactionTypeShouldNotBeFound("transactionTypeCode.contains=" + UPDATED_TRANSACTION_TYPE_CODE);
    }

    @Test
    @Transactional
    public void getAllTransactionTypesByTransactionTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        transactionTypeRepository.saveAndFlush(transactionType);

        // Get all the transactionTypeList where transactionTypeCode does not contain DEFAULT_TRANSACTION_TYPE_CODE
        defaultTransactionTypeShouldNotBeFound("transactionTypeCode.doesNotContain=" + DEFAULT_TRANSACTION_TYPE_CODE);

        // Get all the transactionTypeList where transactionTypeCode does not contain UPDATED_TRANSACTION_TYPE_CODE
        defaultTransactionTypeShouldBeFound("transactionTypeCode.doesNotContain=" + UPDATED_TRANSACTION_TYPE_CODE);
    }


    @Test
    @Transactional
    public void getAllTransactionTypesByTransactionTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionTypeRepository.saveAndFlush(transactionType);

        // Get all the transactionTypeList where transactionType equals to DEFAULT_TRANSACTION_TYPE
        defaultTransactionTypeShouldBeFound("transactionType.equals=" + DEFAULT_TRANSACTION_TYPE);

        // Get all the transactionTypeList where transactionType equals to UPDATED_TRANSACTION_TYPE
        defaultTransactionTypeShouldNotBeFound("transactionType.equals=" + UPDATED_TRANSACTION_TYPE);
    }

    @Test
    @Transactional
    public void getAllTransactionTypesByTransactionTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transactionTypeRepository.saveAndFlush(transactionType);

        // Get all the transactionTypeList where transactionType not equals to DEFAULT_TRANSACTION_TYPE
        defaultTransactionTypeShouldNotBeFound("transactionType.notEquals=" + DEFAULT_TRANSACTION_TYPE);

        // Get all the transactionTypeList where transactionType not equals to UPDATED_TRANSACTION_TYPE
        defaultTransactionTypeShouldBeFound("transactionType.notEquals=" + UPDATED_TRANSACTION_TYPE);
    }

    @Test
    @Transactional
    public void getAllTransactionTypesByTransactionTypeIsInShouldWork() throws Exception {
        // Initialize the database
        transactionTypeRepository.saveAndFlush(transactionType);

        // Get all the transactionTypeList where transactionType in DEFAULT_TRANSACTION_TYPE or UPDATED_TRANSACTION_TYPE
        defaultTransactionTypeShouldBeFound("transactionType.in=" + DEFAULT_TRANSACTION_TYPE + "," + UPDATED_TRANSACTION_TYPE);

        // Get all the transactionTypeList where transactionType equals to UPDATED_TRANSACTION_TYPE
        defaultTransactionTypeShouldNotBeFound("transactionType.in=" + UPDATED_TRANSACTION_TYPE);
    }

    @Test
    @Transactional
    public void getAllTransactionTypesByTransactionTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionTypeRepository.saveAndFlush(transactionType);

        // Get all the transactionTypeList where transactionType is not null
        defaultTransactionTypeShouldBeFound("transactionType.specified=true");

        // Get all the transactionTypeList where transactionType is null
        defaultTransactionTypeShouldNotBeFound("transactionType.specified=false");
    }
                @Test
    @Transactional
    public void getAllTransactionTypesByTransactionTypeContainsSomething() throws Exception {
        // Initialize the database
        transactionTypeRepository.saveAndFlush(transactionType);

        // Get all the transactionTypeList where transactionType contains DEFAULT_TRANSACTION_TYPE
        defaultTransactionTypeShouldBeFound("transactionType.contains=" + DEFAULT_TRANSACTION_TYPE);

        // Get all the transactionTypeList where transactionType contains UPDATED_TRANSACTION_TYPE
        defaultTransactionTypeShouldNotBeFound("transactionType.contains=" + UPDATED_TRANSACTION_TYPE);
    }

    @Test
    @Transactional
    public void getAllTransactionTypesByTransactionTypeNotContainsSomething() throws Exception {
        // Initialize the database
        transactionTypeRepository.saveAndFlush(transactionType);

        // Get all the transactionTypeList where transactionType does not contain DEFAULT_TRANSACTION_TYPE
        defaultTransactionTypeShouldNotBeFound("transactionType.doesNotContain=" + DEFAULT_TRANSACTION_TYPE);

        // Get all the transactionTypeList where transactionType does not contain UPDATED_TRANSACTION_TYPE
        defaultTransactionTypeShouldBeFound("transactionType.doesNotContain=" + UPDATED_TRANSACTION_TYPE);
    }


    @Test
    @Transactional
    public void getAllTransactionTypesByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionTypeRepository.saveAndFlush(transactionType);

        // Get all the transactionTypeList where isActive equals to DEFAULT_IS_ACTIVE
        defaultTransactionTypeShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the transactionTypeList where isActive equals to UPDATED_IS_ACTIVE
        defaultTransactionTypeShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllTransactionTypesByIsActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transactionTypeRepository.saveAndFlush(transactionType);

        // Get all the transactionTypeList where isActive not equals to DEFAULT_IS_ACTIVE
        defaultTransactionTypeShouldNotBeFound("isActive.notEquals=" + DEFAULT_IS_ACTIVE);

        // Get all the transactionTypeList where isActive not equals to UPDATED_IS_ACTIVE
        defaultTransactionTypeShouldBeFound("isActive.notEquals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllTransactionTypesByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        transactionTypeRepository.saveAndFlush(transactionType);

        // Get all the transactionTypeList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultTransactionTypeShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the transactionTypeList where isActive equals to UPDATED_IS_ACTIVE
        defaultTransactionTypeShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllTransactionTypesByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionTypeRepository.saveAndFlush(transactionType);

        // Get all the transactionTypeList where isActive is not null
        defaultTransactionTypeShouldBeFound("isActive.specified=true");

        // Get all the transactionTypeList where isActive is null
        defaultTransactionTypeShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    public void getAllTransactionTypesByHistoryIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionTypeRepository.saveAndFlush(transactionType);
        DocumentHistory history = DocumentHistoryResourceIT.createEntity(em);
        em.persist(history);
        em.flush();
        transactionType.setHistory(history);
        transactionTypeRepository.saveAndFlush(transactionType);
        Long historyId = history.getId();

        // Get all the transactionTypeList where history equals to historyId
        defaultTransactionTypeShouldBeFound("historyId.equals=" + historyId);

        // Get all the transactionTypeList where history equals to historyId + 1
        defaultTransactionTypeShouldNotBeFound("historyId.equals=" + (historyId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTransactionTypeShouldBeFound(String filter) throws Exception {
        restTransactionTypeMockMvc.perform(get("/api/transaction-types?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactionType.getId().intValue())))
            .andExpect(jsonPath("$.[*].transactionTypeCode").value(hasItem(DEFAULT_TRANSACTION_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].transactionType").value(hasItem(DEFAULT_TRANSACTION_TYPE)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restTransactionTypeMockMvc.perform(get("/api/transaction-types/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTransactionTypeShouldNotBeFound(String filter) throws Exception {
        restTransactionTypeMockMvc.perform(get("/api/transaction-types?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTransactionTypeMockMvc.perform(get("/api/transaction-types/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingTransactionType() throws Exception {
        // Get the transactionType
        restTransactionTypeMockMvc.perform(get("/api/transaction-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTransactionType() throws Exception {
        // Initialize the database
        transactionTypeService.save(transactionType);

        int databaseSizeBeforeUpdate = transactionTypeRepository.findAll().size();

        // Update the transactionType
        TransactionType updatedTransactionType = transactionTypeRepository.findById(transactionType.getId()).get();
        // Disconnect from session so that the updates on updatedTransactionType are not directly saved in db
        em.detach(updatedTransactionType);
        updatedTransactionType
            .transactionTypeCode(UPDATED_TRANSACTION_TYPE_CODE)
            .transactionType(UPDATED_TRANSACTION_TYPE)
            .isActive(UPDATED_IS_ACTIVE);

        restTransactionTypeMockMvc.perform(put("/api/transaction-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTransactionType)))
            .andExpect(status().isOk());

        // Validate the TransactionType in the database
        List<TransactionType> transactionTypeList = transactionTypeRepository.findAll();
        assertThat(transactionTypeList).hasSize(databaseSizeBeforeUpdate);
        TransactionType testTransactionType = transactionTypeList.get(transactionTypeList.size() - 1);
        assertThat(testTransactionType.getTransactionTypeCode()).isEqualTo(UPDATED_TRANSACTION_TYPE_CODE);
        assertThat(testTransactionType.getTransactionType()).isEqualTo(UPDATED_TRANSACTION_TYPE);
        assertThat(testTransactionType.isIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void updateNonExistingTransactionType() throws Exception {
        int databaseSizeBeforeUpdate = transactionTypeRepository.findAll().size();

        // Create the TransactionType

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransactionTypeMockMvc.perform(put("/api/transaction-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionType)))
            .andExpect(status().isBadRequest());

        // Validate the TransactionType in the database
        List<TransactionType> transactionTypeList = transactionTypeRepository.findAll();
        assertThat(transactionTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTransactionType() throws Exception {
        // Initialize the database
        transactionTypeService.save(transactionType);

        int databaseSizeBeforeDelete = transactionTypeRepository.findAll().size();

        // Delete the transactionType
        restTransactionTypeMockMvc.perform(delete("/api/transaction-types/{id}", transactionType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TransactionType> transactionTypeList = transactionTypeRepository.findAll();
        assertThat(transactionTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

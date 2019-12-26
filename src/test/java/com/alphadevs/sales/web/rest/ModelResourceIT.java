package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.WikunumApp;
import com.alphadevs.sales.domain.Model;
import com.alphadevs.sales.domain.DocumentHistory;
import com.alphadevs.sales.domain.Products;
import com.alphadevs.sales.domain.Location;
import com.alphadevs.sales.repository.ModelRepository;
import com.alphadevs.sales.service.ModelService;
import com.alphadevs.sales.web.rest.errors.ExceptionTranslator;
import com.alphadevs.sales.service.dto.ModelCriteria;
import com.alphadevs.sales.service.ModelQueryService;

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
 * Integration tests for the {@link ModelResource} REST controller.
 */
@SpringBootTest(classes = WikunumApp.class)
public class ModelResourceIT {

    private static final String DEFAULT_MODEL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_MODEL_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_MODEL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MODEL_NAME = "BBBBBBBBBB";

    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private ModelService modelService;

    @Autowired
    private ModelQueryService modelQueryService;

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

    private MockMvc restModelMockMvc;

    private Model model;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ModelResource modelResource = new ModelResource(modelService, modelQueryService);
        this.restModelMockMvc = MockMvcBuilders.standaloneSetup(modelResource)
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
    public static Model createEntity(EntityManager em) {
        Model model = new Model()
            .modelCode(DEFAULT_MODEL_CODE)
            .modelName(DEFAULT_MODEL_NAME);
        // Add required entity
        Products products;
        if (TestUtil.findAll(em, Products.class).isEmpty()) {
            products = ProductsResourceIT.createEntity(em);
            em.persist(products);
            em.flush();
        } else {
            products = TestUtil.findAll(em, Products.class).get(0);
        }
        model.setRelatedProduct(products);
        // Add required entity
        Location location;
        if (TestUtil.findAll(em, Location.class).isEmpty()) {
            location = LocationResourceIT.createEntity(em);
            em.persist(location);
            em.flush();
        } else {
            location = TestUtil.findAll(em, Location.class).get(0);
        }
        model.setLocation(location);
        return model;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Model createUpdatedEntity(EntityManager em) {
        Model model = new Model()
            .modelCode(UPDATED_MODEL_CODE)
            .modelName(UPDATED_MODEL_NAME);
        // Add required entity
        Products products;
        if (TestUtil.findAll(em, Products.class).isEmpty()) {
            products = ProductsResourceIT.createUpdatedEntity(em);
            em.persist(products);
            em.flush();
        } else {
            products = TestUtil.findAll(em, Products.class).get(0);
        }
        model.setRelatedProduct(products);
        // Add required entity
        Location location;
        if (TestUtil.findAll(em, Location.class).isEmpty()) {
            location = LocationResourceIT.createUpdatedEntity(em);
            em.persist(location);
            em.flush();
        } else {
            location = TestUtil.findAll(em, Location.class).get(0);
        }
        model.setLocation(location);
        return model;
    }

    @BeforeEach
    public void initTest() {
        model = createEntity(em);
    }

    @Test
    @Transactional
    public void createModel() throws Exception {
        int databaseSizeBeforeCreate = modelRepository.findAll().size();

        // Create the Model
        restModelMockMvc.perform(post("/api/models")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(model)))
            .andExpect(status().isCreated());

        // Validate the Model in the database
        List<Model> modelList = modelRepository.findAll();
        assertThat(modelList).hasSize(databaseSizeBeforeCreate + 1);
        Model testModel = modelList.get(modelList.size() - 1);
        assertThat(testModel.getModelCode()).isEqualTo(DEFAULT_MODEL_CODE);
        assertThat(testModel.getModelName()).isEqualTo(DEFAULT_MODEL_NAME);
    }

    @Test
    @Transactional
    public void createModelWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = modelRepository.findAll().size();

        // Create the Model with an existing ID
        model.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restModelMockMvc.perform(post("/api/models")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(model)))
            .andExpect(status().isBadRequest());

        // Validate the Model in the database
        List<Model> modelList = modelRepository.findAll();
        assertThat(modelList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkModelCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = modelRepository.findAll().size();
        // set the field null
        model.setModelCode(null);

        // Create the Model, which fails.

        restModelMockMvc.perform(post("/api/models")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(model)))
            .andExpect(status().isBadRequest());

        List<Model> modelList = modelRepository.findAll();
        assertThat(modelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkModelNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = modelRepository.findAll().size();
        // set the field null
        model.setModelName(null);

        // Create the Model, which fails.

        restModelMockMvc.perform(post("/api/models")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(model)))
            .andExpect(status().isBadRequest());

        List<Model> modelList = modelRepository.findAll();
        assertThat(modelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllModels() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        // Get all the modelList
        restModelMockMvc.perform(get("/api/models?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(model.getId().intValue())))
            .andExpect(jsonPath("$.[*].modelCode").value(hasItem(DEFAULT_MODEL_CODE)))
            .andExpect(jsonPath("$.[*].modelName").value(hasItem(DEFAULT_MODEL_NAME)));
    }
    
    @Test
    @Transactional
    public void getModel() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        // Get the model
        restModelMockMvc.perform(get("/api/models/{id}", model.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(model.getId().intValue()))
            .andExpect(jsonPath("$.modelCode").value(DEFAULT_MODEL_CODE))
            .andExpect(jsonPath("$.modelName").value(DEFAULT_MODEL_NAME));
    }


    @Test
    @Transactional
    public void getModelsByIdFiltering() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        Long id = model.getId();

        defaultModelShouldBeFound("id.equals=" + id);
        defaultModelShouldNotBeFound("id.notEquals=" + id);

        defaultModelShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultModelShouldNotBeFound("id.greaterThan=" + id);

        defaultModelShouldBeFound("id.lessThanOrEqual=" + id);
        defaultModelShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllModelsByModelCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        // Get all the modelList where modelCode equals to DEFAULT_MODEL_CODE
        defaultModelShouldBeFound("modelCode.equals=" + DEFAULT_MODEL_CODE);

        // Get all the modelList where modelCode equals to UPDATED_MODEL_CODE
        defaultModelShouldNotBeFound("modelCode.equals=" + UPDATED_MODEL_CODE);
    }

    @Test
    @Transactional
    public void getAllModelsByModelCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        // Get all the modelList where modelCode not equals to DEFAULT_MODEL_CODE
        defaultModelShouldNotBeFound("modelCode.notEquals=" + DEFAULT_MODEL_CODE);

        // Get all the modelList where modelCode not equals to UPDATED_MODEL_CODE
        defaultModelShouldBeFound("modelCode.notEquals=" + UPDATED_MODEL_CODE);
    }

    @Test
    @Transactional
    public void getAllModelsByModelCodeIsInShouldWork() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        // Get all the modelList where modelCode in DEFAULT_MODEL_CODE or UPDATED_MODEL_CODE
        defaultModelShouldBeFound("modelCode.in=" + DEFAULT_MODEL_CODE + "," + UPDATED_MODEL_CODE);

        // Get all the modelList where modelCode equals to UPDATED_MODEL_CODE
        defaultModelShouldNotBeFound("modelCode.in=" + UPDATED_MODEL_CODE);
    }

    @Test
    @Transactional
    public void getAllModelsByModelCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        // Get all the modelList where modelCode is not null
        defaultModelShouldBeFound("modelCode.specified=true");

        // Get all the modelList where modelCode is null
        defaultModelShouldNotBeFound("modelCode.specified=false");
    }
                @Test
    @Transactional
    public void getAllModelsByModelCodeContainsSomething() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        // Get all the modelList where modelCode contains DEFAULT_MODEL_CODE
        defaultModelShouldBeFound("modelCode.contains=" + DEFAULT_MODEL_CODE);

        // Get all the modelList where modelCode contains UPDATED_MODEL_CODE
        defaultModelShouldNotBeFound("modelCode.contains=" + UPDATED_MODEL_CODE);
    }

    @Test
    @Transactional
    public void getAllModelsByModelCodeNotContainsSomething() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        // Get all the modelList where modelCode does not contain DEFAULT_MODEL_CODE
        defaultModelShouldNotBeFound("modelCode.doesNotContain=" + DEFAULT_MODEL_CODE);

        // Get all the modelList where modelCode does not contain UPDATED_MODEL_CODE
        defaultModelShouldBeFound("modelCode.doesNotContain=" + UPDATED_MODEL_CODE);
    }


    @Test
    @Transactional
    public void getAllModelsByModelNameIsEqualToSomething() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        // Get all the modelList where modelName equals to DEFAULT_MODEL_NAME
        defaultModelShouldBeFound("modelName.equals=" + DEFAULT_MODEL_NAME);

        // Get all the modelList where modelName equals to UPDATED_MODEL_NAME
        defaultModelShouldNotBeFound("modelName.equals=" + UPDATED_MODEL_NAME);
    }

    @Test
    @Transactional
    public void getAllModelsByModelNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        // Get all the modelList where modelName not equals to DEFAULT_MODEL_NAME
        defaultModelShouldNotBeFound("modelName.notEquals=" + DEFAULT_MODEL_NAME);

        // Get all the modelList where modelName not equals to UPDATED_MODEL_NAME
        defaultModelShouldBeFound("modelName.notEquals=" + UPDATED_MODEL_NAME);
    }

    @Test
    @Transactional
    public void getAllModelsByModelNameIsInShouldWork() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        // Get all the modelList where modelName in DEFAULT_MODEL_NAME or UPDATED_MODEL_NAME
        defaultModelShouldBeFound("modelName.in=" + DEFAULT_MODEL_NAME + "," + UPDATED_MODEL_NAME);

        // Get all the modelList where modelName equals to UPDATED_MODEL_NAME
        defaultModelShouldNotBeFound("modelName.in=" + UPDATED_MODEL_NAME);
    }

    @Test
    @Transactional
    public void getAllModelsByModelNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        // Get all the modelList where modelName is not null
        defaultModelShouldBeFound("modelName.specified=true");

        // Get all the modelList where modelName is null
        defaultModelShouldNotBeFound("modelName.specified=false");
    }
                @Test
    @Transactional
    public void getAllModelsByModelNameContainsSomething() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        // Get all the modelList where modelName contains DEFAULT_MODEL_NAME
        defaultModelShouldBeFound("modelName.contains=" + DEFAULT_MODEL_NAME);

        // Get all the modelList where modelName contains UPDATED_MODEL_NAME
        defaultModelShouldNotBeFound("modelName.contains=" + UPDATED_MODEL_NAME);
    }

    @Test
    @Transactional
    public void getAllModelsByModelNameNotContainsSomething() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        // Get all the modelList where modelName does not contain DEFAULT_MODEL_NAME
        defaultModelShouldNotBeFound("modelName.doesNotContain=" + DEFAULT_MODEL_NAME);

        // Get all the modelList where modelName does not contain UPDATED_MODEL_NAME
        defaultModelShouldBeFound("modelName.doesNotContain=" + UPDATED_MODEL_NAME);
    }


    @Test
    @Transactional
    public void getAllModelsByHistoryIsEqualToSomething() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);
        DocumentHistory history = DocumentHistoryResourceIT.createEntity(em);
        em.persist(history);
        em.flush();
        model.setHistory(history);
        modelRepository.saveAndFlush(model);
        Long historyId = history.getId();

        // Get all the modelList where history equals to historyId
        defaultModelShouldBeFound("historyId.equals=" + historyId);

        // Get all the modelList where history equals to historyId + 1
        defaultModelShouldNotBeFound("historyId.equals=" + (historyId + 1));
    }


    @Test
    @Transactional
    public void getAllModelsByRelatedProductIsEqualToSomething() throws Exception {
        // Get already existing entity
        Products relatedProduct = model.getRelatedProduct();
        modelRepository.saveAndFlush(model);
        Long relatedProductId = relatedProduct.getId();

        // Get all the modelList where relatedProduct equals to relatedProductId
        defaultModelShouldBeFound("relatedProductId.equals=" + relatedProductId);

        // Get all the modelList where relatedProduct equals to relatedProductId + 1
        defaultModelShouldNotBeFound("relatedProductId.equals=" + (relatedProductId + 1));
    }


    @Test
    @Transactional
    public void getAllModelsByLocationIsEqualToSomething() throws Exception {
        // Get already existing entity
        Location location = model.getLocation();
        modelRepository.saveAndFlush(model);
        Long locationId = location.getId();

        // Get all the modelList where location equals to locationId
        defaultModelShouldBeFound("locationId.equals=" + locationId);

        // Get all the modelList where location equals to locationId + 1
        defaultModelShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultModelShouldBeFound(String filter) throws Exception {
        restModelMockMvc.perform(get("/api/models?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(model.getId().intValue())))
            .andExpect(jsonPath("$.[*].modelCode").value(hasItem(DEFAULT_MODEL_CODE)))
            .andExpect(jsonPath("$.[*].modelName").value(hasItem(DEFAULT_MODEL_NAME)));

        // Check, that the count call also returns 1
        restModelMockMvc.perform(get("/api/models/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultModelShouldNotBeFound(String filter) throws Exception {
        restModelMockMvc.perform(get("/api/models?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restModelMockMvc.perform(get("/api/models/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingModel() throws Exception {
        // Get the model
        restModelMockMvc.perform(get("/api/models/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateModel() throws Exception {
        // Initialize the database
        modelService.save(model);

        int databaseSizeBeforeUpdate = modelRepository.findAll().size();

        // Update the model
        Model updatedModel = modelRepository.findById(model.getId()).get();
        // Disconnect from session so that the updates on updatedModel are not directly saved in db
        em.detach(updatedModel);
        updatedModel
            .modelCode(UPDATED_MODEL_CODE)
            .modelName(UPDATED_MODEL_NAME);

        restModelMockMvc.perform(put("/api/models")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedModel)))
            .andExpect(status().isOk());

        // Validate the Model in the database
        List<Model> modelList = modelRepository.findAll();
        assertThat(modelList).hasSize(databaseSizeBeforeUpdate);
        Model testModel = modelList.get(modelList.size() - 1);
        assertThat(testModel.getModelCode()).isEqualTo(UPDATED_MODEL_CODE);
        assertThat(testModel.getModelName()).isEqualTo(UPDATED_MODEL_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingModel() throws Exception {
        int databaseSizeBeforeUpdate = modelRepository.findAll().size();

        // Create the Model

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restModelMockMvc.perform(put("/api/models")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(model)))
            .andExpect(status().isBadRequest());

        // Validate the Model in the database
        List<Model> modelList = modelRepository.findAll();
        assertThat(modelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteModel() throws Exception {
        // Initialize the database
        modelService.save(model);

        int databaseSizeBeforeDelete = modelRepository.findAll().size();

        // Delete the model
        restModelMockMvc.perform(delete("/api/models/{id}", model.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Model> modelList = modelRepository.findAll();
        assertThat(modelList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

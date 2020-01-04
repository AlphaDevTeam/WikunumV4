package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.WikunumApp;
import com.alphadevs.sales.domain.ConfigurationItems;
import com.alphadevs.sales.domain.Location;
import com.alphadevs.sales.repository.ConfigurationItemsRepository;
import com.alphadevs.sales.service.ConfigurationItemsService;
import com.alphadevs.sales.web.rest.errors.ExceptionTranslator;
import com.alphadevs.sales.service.dto.ConfigurationItemsCriteria;
import com.alphadevs.sales.service.ConfigurationItemsQueryService;

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
 * Integration tests for the {@link ConfigurationItemsResource} REST controller.
 */
@SpringBootTest(classes = WikunumApp.class)
public class ConfigurationItemsResourceIT {

    private static final String DEFAULT_CONFIG_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CONFIG_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CONFIG_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_CONFIG_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_CONFIG_ENABLED = false;
    private static final Boolean UPDATED_CONFIG_ENABLED = true;

    private static final Double DEFAULT_CONFIG_PARAMTER = 1D;
    private static final Double UPDATED_CONFIG_PARAMTER = 2D;
    private static final Double SMALLER_CONFIG_PARAMTER = 1D - 1D;

    @Autowired
    private ConfigurationItemsRepository configurationItemsRepository;

    @Autowired
    private ConfigurationItemsService configurationItemsService;

    @Autowired
    private ConfigurationItemsQueryService configurationItemsQueryService;

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

    private MockMvc restConfigurationItemsMockMvc;

    private ConfigurationItems configurationItems;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ConfigurationItemsResource configurationItemsResource = new ConfigurationItemsResource(configurationItemsService, configurationItemsQueryService);
        this.restConfigurationItemsMockMvc = MockMvcBuilders.standaloneSetup(configurationItemsResource)
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
    public static ConfigurationItems createEntity(EntityManager em) {
        ConfigurationItems configurationItems = new ConfigurationItems()
            .configCode(DEFAULT_CONFIG_CODE)
            .configDescription(DEFAULT_CONFIG_DESCRIPTION)
            .configEnabled(DEFAULT_CONFIG_ENABLED)
            .configParamter(DEFAULT_CONFIG_PARAMTER);
        return configurationItems;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConfigurationItems createUpdatedEntity(EntityManager em) {
        ConfigurationItems configurationItems = new ConfigurationItems()
            .configCode(UPDATED_CONFIG_CODE)
            .configDescription(UPDATED_CONFIG_DESCRIPTION)
            .configEnabled(UPDATED_CONFIG_ENABLED)
            .configParamter(UPDATED_CONFIG_PARAMTER);
        return configurationItems;
    }

    @BeforeEach
    public void initTest() {
        configurationItems = createEntity(em);
    }

    @Test
    @Transactional
    public void createConfigurationItems() throws Exception {
        int databaseSizeBeforeCreate = configurationItemsRepository.findAll().size();

        // Create the ConfigurationItems
        restConfigurationItemsMockMvc.perform(post("/api/configuration-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configurationItems)))
            .andExpect(status().isCreated());

        // Validate the ConfigurationItems in the database
        List<ConfigurationItems> configurationItemsList = configurationItemsRepository.findAll();
        assertThat(configurationItemsList).hasSize(databaseSizeBeforeCreate + 1);
        ConfigurationItems testConfigurationItems = configurationItemsList.get(configurationItemsList.size() - 1);
        assertThat(testConfigurationItems.getConfigCode()).isEqualTo(DEFAULT_CONFIG_CODE);
        assertThat(testConfigurationItems.getConfigDescription()).isEqualTo(DEFAULT_CONFIG_DESCRIPTION);
        assertThat(testConfigurationItems.isConfigEnabled()).isEqualTo(DEFAULT_CONFIG_ENABLED);
        assertThat(testConfigurationItems.getConfigParamter()).isEqualTo(DEFAULT_CONFIG_PARAMTER);
    }

    @Test
    @Transactional
    public void createConfigurationItemsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = configurationItemsRepository.findAll().size();

        // Create the ConfigurationItems with an existing ID
        configurationItems.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restConfigurationItemsMockMvc.perform(post("/api/configuration-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configurationItems)))
            .andExpect(status().isBadRequest());

        // Validate the ConfigurationItems in the database
        List<ConfigurationItems> configurationItemsList = configurationItemsRepository.findAll();
        assertThat(configurationItemsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkConfigCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = configurationItemsRepository.findAll().size();
        // set the field null
        configurationItems.setConfigCode(null);

        // Create the ConfigurationItems, which fails.

        restConfigurationItemsMockMvc.perform(post("/api/configuration-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configurationItems)))
            .andExpect(status().isBadRequest());

        List<ConfigurationItems> configurationItemsList = configurationItemsRepository.findAll();
        assertThat(configurationItemsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkConfigDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = configurationItemsRepository.findAll().size();
        // set the field null
        configurationItems.setConfigDescription(null);

        // Create the ConfigurationItems, which fails.

        restConfigurationItemsMockMvc.perform(post("/api/configuration-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configurationItems)))
            .andExpect(status().isBadRequest());

        List<ConfigurationItems> configurationItemsList = configurationItemsRepository.findAll();
        assertThat(configurationItemsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllConfigurationItems() throws Exception {
        // Initialize the database
        configurationItemsRepository.saveAndFlush(configurationItems);

        // Get all the configurationItemsList
        restConfigurationItemsMockMvc.perform(get("/api/configuration-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(configurationItems.getId().intValue())))
            .andExpect(jsonPath("$.[*].configCode").value(hasItem(DEFAULT_CONFIG_CODE)))
            .andExpect(jsonPath("$.[*].configDescription").value(hasItem(DEFAULT_CONFIG_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].configEnabled").value(hasItem(DEFAULT_CONFIG_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].configParamter").value(hasItem(DEFAULT_CONFIG_PARAMTER.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getConfigurationItems() throws Exception {
        // Initialize the database
        configurationItemsRepository.saveAndFlush(configurationItems);

        // Get the configurationItems
        restConfigurationItemsMockMvc.perform(get("/api/configuration-items/{id}", configurationItems.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(configurationItems.getId().intValue()))
            .andExpect(jsonPath("$.configCode").value(DEFAULT_CONFIG_CODE))
            .andExpect(jsonPath("$.configDescription").value(DEFAULT_CONFIG_DESCRIPTION))
            .andExpect(jsonPath("$.configEnabled").value(DEFAULT_CONFIG_ENABLED.booleanValue()))
            .andExpect(jsonPath("$.configParamter").value(DEFAULT_CONFIG_PARAMTER.doubleValue()));
    }


    @Test
    @Transactional
    public void getConfigurationItemsByIdFiltering() throws Exception {
        // Initialize the database
        configurationItemsRepository.saveAndFlush(configurationItems);

        Long id = configurationItems.getId();

        defaultConfigurationItemsShouldBeFound("id.equals=" + id);
        defaultConfigurationItemsShouldNotBeFound("id.notEquals=" + id);

        defaultConfigurationItemsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultConfigurationItemsShouldNotBeFound("id.greaterThan=" + id);

        defaultConfigurationItemsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultConfigurationItemsShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllConfigurationItemsByConfigCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        configurationItemsRepository.saveAndFlush(configurationItems);

        // Get all the configurationItemsList where configCode equals to DEFAULT_CONFIG_CODE
        defaultConfigurationItemsShouldBeFound("configCode.equals=" + DEFAULT_CONFIG_CODE);

        // Get all the configurationItemsList where configCode equals to UPDATED_CONFIG_CODE
        defaultConfigurationItemsShouldNotBeFound("configCode.equals=" + UPDATED_CONFIG_CODE);
    }

    @Test
    @Transactional
    public void getAllConfigurationItemsByConfigCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        configurationItemsRepository.saveAndFlush(configurationItems);

        // Get all the configurationItemsList where configCode not equals to DEFAULT_CONFIG_CODE
        defaultConfigurationItemsShouldNotBeFound("configCode.notEquals=" + DEFAULT_CONFIG_CODE);

        // Get all the configurationItemsList where configCode not equals to UPDATED_CONFIG_CODE
        defaultConfigurationItemsShouldBeFound("configCode.notEquals=" + UPDATED_CONFIG_CODE);
    }

    @Test
    @Transactional
    public void getAllConfigurationItemsByConfigCodeIsInShouldWork() throws Exception {
        // Initialize the database
        configurationItemsRepository.saveAndFlush(configurationItems);

        // Get all the configurationItemsList where configCode in DEFAULT_CONFIG_CODE or UPDATED_CONFIG_CODE
        defaultConfigurationItemsShouldBeFound("configCode.in=" + DEFAULT_CONFIG_CODE + "," + UPDATED_CONFIG_CODE);

        // Get all the configurationItemsList where configCode equals to UPDATED_CONFIG_CODE
        defaultConfigurationItemsShouldNotBeFound("configCode.in=" + UPDATED_CONFIG_CODE);
    }

    @Test
    @Transactional
    public void getAllConfigurationItemsByConfigCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        configurationItemsRepository.saveAndFlush(configurationItems);

        // Get all the configurationItemsList where configCode is not null
        defaultConfigurationItemsShouldBeFound("configCode.specified=true");

        // Get all the configurationItemsList where configCode is null
        defaultConfigurationItemsShouldNotBeFound("configCode.specified=false");
    }
                @Test
    @Transactional
    public void getAllConfigurationItemsByConfigCodeContainsSomething() throws Exception {
        // Initialize the database
        configurationItemsRepository.saveAndFlush(configurationItems);

        // Get all the configurationItemsList where configCode contains DEFAULT_CONFIG_CODE
        defaultConfigurationItemsShouldBeFound("configCode.contains=" + DEFAULT_CONFIG_CODE);

        // Get all the configurationItemsList where configCode contains UPDATED_CONFIG_CODE
        defaultConfigurationItemsShouldNotBeFound("configCode.contains=" + UPDATED_CONFIG_CODE);
    }

    @Test
    @Transactional
    public void getAllConfigurationItemsByConfigCodeNotContainsSomething() throws Exception {
        // Initialize the database
        configurationItemsRepository.saveAndFlush(configurationItems);

        // Get all the configurationItemsList where configCode does not contain DEFAULT_CONFIG_CODE
        defaultConfigurationItemsShouldNotBeFound("configCode.doesNotContain=" + DEFAULT_CONFIG_CODE);

        // Get all the configurationItemsList where configCode does not contain UPDATED_CONFIG_CODE
        defaultConfigurationItemsShouldBeFound("configCode.doesNotContain=" + UPDATED_CONFIG_CODE);
    }


    @Test
    @Transactional
    public void getAllConfigurationItemsByConfigDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        configurationItemsRepository.saveAndFlush(configurationItems);

        // Get all the configurationItemsList where configDescription equals to DEFAULT_CONFIG_DESCRIPTION
        defaultConfigurationItemsShouldBeFound("configDescription.equals=" + DEFAULT_CONFIG_DESCRIPTION);

        // Get all the configurationItemsList where configDescription equals to UPDATED_CONFIG_DESCRIPTION
        defaultConfigurationItemsShouldNotBeFound("configDescription.equals=" + UPDATED_CONFIG_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllConfigurationItemsByConfigDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        configurationItemsRepository.saveAndFlush(configurationItems);

        // Get all the configurationItemsList where configDescription not equals to DEFAULT_CONFIG_DESCRIPTION
        defaultConfigurationItemsShouldNotBeFound("configDescription.notEquals=" + DEFAULT_CONFIG_DESCRIPTION);

        // Get all the configurationItemsList where configDescription not equals to UPDATED_CONFIG_DESCRIPTION
        defaultConfigurationItemsShouldBeFound("configDescription.notEquals=" + UPDATED_CONFIG_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllConfigurationItemsByConfigDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        configurationItemsRepository.saveAndFlush(configurationItems);

        // Get all the configurationItemsList where configDescription in DEFAULT_CONFIG_DESCRIPTION or UPDATED_CONFIG_DESCRIPTION
        defaultConfigurationItemsShouldBeFound("configDescription.in=" + DEFAULT_CONFIG_DESCRIPTION + "," + UPDATED_CONFIG_DESCRIPTION);

        // Get all the configurationItemsList where configDescription equals to UPDATED_CONFIG_DESCRIPTION
        defaultConfigurationItemsShouldNotBeFound("configDescription.in=" + UPDATED_CONFIG_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllConfigurationItemsByConfigDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        configurationItemsRepository.saveAndFlush(configurationItems);

        // Get all the configurationItemsList where configDescription is not null
        defaultConfigurationItemsShouldBeFound("configDescription.specified=true");

        // Get all the configurationItemsList where configDescription is null
        defaultConfigurationItemsShouldNotBeFound("configDescription.specified=false");
    }
                @Test
    @Transactional
    public void getAllConfigurationItemsByConfigDescriptionContainsSomething() throws Exception {
        // Initialize the database
        configurationItemsRepository.saveAndFlush(configurationItems);

        // Get all the configurationItemsList where configDescription contains DEFAULT_CONFIG_DESCRIPTION
        defaultConfigurationItemsShouldBeFound("configDescription.contains=" + DEFAULT_CONFIG_DESCRIPTION);

        // Get all the configurationItemsList where configDescription contains UPDATED_CONFIG_DESCRIPTION
        defaultConfigurationItemsShouldNotBeFound("configDescription.contains=" + UPDATED_CONFIG_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllConfigurationItemsByConfigDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        configurationItemsRepository.saveAndFlush(configurationItems);

        // Get all the configurationItemsList where configDescription does not contain DEFAULT_CONFIG_DESCRIPTION
        defaultConfigurationItemsShouldNotBeFound("configDescription.doesNotContain=" + DEFAULT_CONFIG_DESCRIPTION);

        // Get all the configurationItemsList where configDescription does not contain UPDATED_CONFIG_DESCRIPTION
        defaultConfigurationItemsShouldBeFound("configDescription.doesNotContain=" + UPDATED_CONFIG_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllConfigurationItemsByConfigEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        configurationItemsRepository.saveAndFlush(configurationItems);

        // Get all the configurationItemsList where configEnabled equals to DEFAULT_CONFIG_ENABLED
        defaultConfigurationItemsShouldBeFound("configEnabled.equals=" + DEFAULT_CONFIG_ENABLED);

        // Get all the configurationItemsList where configEnabled equals to UPDATED_CONFIG_ENABLED
        defaultConfigurationItemsShouldNotBeFound("configEnabled.equals=" + UPDATED_CONFIG_ENABLED);
    }

    @Test
    @Transactional
    public void getAllConfigurationItemsByConfigEnabledIsNotEqualToSomething() throws Exception {
        // Initialize the database
        configurationItemsRepository.saveAndFlush(configurationItems);

        // Get all the configurationItemsList where configEnabled not equals to DEFAULT_CONFIG_ENABLED
        defaultConfigurationItemsShouldNotBeFound("configEnabled.notEquals=" + DEFAULT_CONFIG_ENABLED);

        // Get all the configurationItemsList where configEnabled not equals to UPDATED_CONFIG_ENABLED
        defaultConfigurationItemsShouldBeFound("configEnabled.notEquals=" + UPDATED_CONFIG_ENABLED);
    }

    @Test
    @Transactional
    public void getAllConfigurationItemsByConfigEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        configurationItemsRepository.saveAndFlush(configurationItems);

        // Get all the configurationItemsList where configEnabled in DEFAULT_CONFIG_ENABLED or UPDATED_CONFIG_ENABLED
        defaultConfigurationItemsShouldBeFound("configEnabled.in=" + DEFAULT_CONFIG_ENABLED + "," + UPDATED_CONFIG_ENABLED);

        // Get all the configurationItemsList where configEnabled equals to UPDATED_CONFIG_ENABLED
        defaultConfigurationItemsShouldNotBeFound("configEnabled.in=" + UPDATED_CONFIG_ENABLED);
    }

    @Test
    @Transactional
    public void getAllConfigurationItemsByConfigEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        configurationItemsRepository.saveAndFlush(configurationItems);

        // Get all the configurationItemsList where configEnabled is not null
        defaultConfigurationItemsShouldBeFound("configEnabled.specified=true");

        // Get all the configurationItemsList where configEnabled is null
        defaultConfigurationItemsShouldNotBeFound("configEnabled.specified=false");
    }

    @Test
    @Transactional
    public void getAllConfigurationItemsByConfigParamterIsEqualToSomething() throws Exception {
        // Initialize the database
        configurationItemsRepository.saveAndFlush(configurationItems);

        // Get all the configurationItemsList where configParamter equals to DEFAULT_CONFIG_PARAMTER
        defaultConfigurationItemsShouldBeFound("configParamter.equals=" + DEFAULT_CONFIG_PARAMTER);

        // Get all the configurationItemsList where configParamter equals to UPDATED_CONFIG_PARAMTER
        defaultConfigurationItemsShouldNotBeFound("configParamter.equals=" + UPDATED_CONFIG_PARAMTER);
    }

    @Test
    @Transactional
    public void getAllConfigurationItemsByConfigParamterIsNotEqualToSomething() throws Exception {
        // Initialize the database
        configurationItemsRepository.saveAndFlush(configurationItems);

        // Get all the configurationItemsList where configParamter not equals to DEFAULT_CONFIG_PARAMTER
        defaultConfigurationItemsShouldNotBeFound("configParamter.notEquals=" + DEFAULT_CONFIG_PARAMTER);

        // Get all the configurationItemsList where configParamter not equals to UPDATED_CONFIG_PARAMTER
        defaultConfigurationItemsShouldBeFound("configParamter.notEquals=" + UPDATED_CONFIG_PARAMTER);
    }

    @Test
    @Transactional
    public void getAllConfigurationItemsByConfigParamterIsInShouldWork() throws Exception {
        // Initialize the database
        configurationItemsRepository.saveAndFlush(configurationItems);

        // Get all the configurationItemsList where configParamter in DEFAULT_CONFIG_PARAMTER or UPDATED_CONFIG_PARAMTER
        defaultConfigurationItemsShouldBeFound("configParamter.in=" + DEFAULT_CONFIG_PARAMTER + "," + UPDATED_CONFIG_PARAMTER);

        // Get all the configurationItemsList where configParamter equals to UPDATED_CONFIG_PARAMTER
        defaultConfigurationItemsShouldNotBeFound("configParamter.in=" + UPDATED_CONFIG_PARAMTER);
    }

    @Test
    @Transactional
    public void getAllConfigurationItemsByConfigParamterIsNullOrNotNull() throws Exception {
        // Initialize the database
        configurationItemsRepository.saveAndFlush(configurationItems);

        // Get all the configurationItemsList where configParamter is not null
        defaultConfigurationItemsShouldBeFound("configParamter.specified=true");

        // Get all the configurationItemsList where configParamter is null
        defaultConfigurationItemsShouldNotBeFound("configParamter.specified=false");
    }

    @Test
    @Transactional
    public void getAllConfigurationItemsByConfigParamterIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        configurationItemsRepository.saveAndFlush(configurationItems);

        // Get all the configurationItemsList where configParamter is greater than or equal to DEFAULT_CONFIG_PARAMTER
        defaultConfigurationItemsShouldBeFound("configParamter.greaterThanOrEqual=" + DEFAULT_CONFIG_PARAMTER);

        // Get all the configurationItemsList where configParamter is greater than or equal to UPDATED_CONFIG_PARAMTER
        defaultConfigurationItemsShouldNotBeFound("configParamter.greaterThanOrEqual=" + UPDATED_CONFIG_PARAMTER);
    }

    @Test
    @Transactional
    public void getAllConfigurationItemsByConfigParamterIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        configurationItemsRepository.saveAndFlush(configurationItems);

        // Get all the configurationItemsList where configParamter is less than or equal to DEFAULT_CONFIG_PARAMTER
        defaultConfigurationItemsShouldBeFound("configParamter.lessThanOrEqual=" + DEFAULT_CONFIG_PARAMTER);

        // Get all the configurationItemsList where configParamter is less than or equal to SMALLER_CONFIG_PARAMTER
        defaultConfigurationItemsShouldNotBeFound("configParamter.lessThanOrEqual=" + SMALLER_CONFIG_PARAMTER);
    }

    @Test
    @Transactional
    public void getAllConfigurationItemsByConfigParamterIsLessThanSomething() throws Exception {
        // Initialize the database
        configurationItemsRepository.saveAndFlush(configurationItems);

        // Get all the configurationItemsList where configParamter is less than DEFAULT_CONFIG_PARAMTER
        defaultConfigurationItemsShouldNotBeFound("configParamter.lessThan=" + DEFAULT_CONFIG_PARAMTER);

        // Get all the configurationItemsList where configParamter is less than UPDATED_CONFIG_PARAMTER
        defaultConfigurationItemsShouldBeFound("configParamter.lessThan=" + UPDATED_CONFIG_PARAMTER);
    }

    @Test
    @Transactional
    public void getAllConfigurationItemsByConfigParamterIsGreaterThanSomething() throws Exception {
        // Initialize the database
        configurationItemsRepository.saveAndFlush(configurationItems);

        // Get all the configurationItemsList where configParamter is greater than DEFAULT_CONFIG_PARAMTER
        defaultConfigurationItemsShouldNotBeFound("configParamter.greaterThan=" + DEFAULT_CONFIG_PARAMTER);

        // Get all the configurationItemsList where configParamter is greater than SMALLER_CONFIG_PARAMTER
        defaultConfigurationItemsShouldBeFound("configParamter.greaterThan=" + SMALLER_CONFIG_PARAMTER);
    }


    @Test
    @Transactional
    public void getAllConfigurationItemsByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        configurationItemsRepository.saveAndFlush(configurationItems);
        Location location = LocationResourceIT.createEntity(em);
        em.persist(location);
        em.flush();
        configurationItems.addLocation(location);
        configurationItemsRepository.saveAndFlush(configurationItems);
        Long locationId = location.getId();

        // Get all the configurationItemsList where location equals to locationId
        defaultConfigurationItemsShouldBeFound("locationId.equals=" + locationId);

        // Get all the configurationItemsList where location equals to locationId + 1
        defaultConfigurationItemsShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultConfigurationItemsShouldBeFound(String filter) throws Exception {
        restConfigurationItemsMockMvc.perform(get("/api/configuration-items?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(configurationItems.getId().intValue())))
            .andExpect(jsonPath("$.[*].configCode").value(hasItem(DEFAULT_CONFIG_CODE)))
            .andExpect(jsonPath("$.[*].configDescription").value(hasItem(DEFAULT_CONFIG_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].configEnabled").value(hasItem(DEFAULT_CONFIG_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].configParamter").value(hasItem(DEFAULT_CONFIG_PARAMTER.doubleValue())));

        // Check, that the count call also returns 1
        restConfigurationItemsMockMvc.perform(get("/api/configuration-items/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultConfigurationItemsShouldNotBeFound(String filter) throws Exception {
        restConfigurationItemsMockMvc.perform(get("/api/configuration-items?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restConfigurationItemsMockMvc.perform(get("/api/configuration-items/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingConfigurationItems() throws Exception {
        // Get the configurationItems
        restConfigurationItemsMockMvc.perform(get("/api/configuration-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConfigurationItems() throws Exception {
        // Initialize the database
        configurationItemsService.save(configurationItems);

        int databaseSizeBeforeUpdate = configurationItemsRepository.findAll().size();

        // Update the configurationItems
        ConfigurationItems updatedConfigurationItems = configurationItemsRepository.findById(configurationItems.getId()).get();
        // Disconnect from session so that the updates on updatedConfigurationItems are not directly saved in db
        em.detach(updatedConfigurationItems);
        updatedConfigurationItems
            .configCode(UPDATED_CONFIG_CODE)
            .configDescription(UPDATED_CONFIG_DESCRIPTION)
            .configEnabled(UPDATED_CONFIG_ENABLED)
            .configParamter(UPDATED_CONFIG_PARAMTER);

        restConfigurationItemsMockMvc.perform(put("/api/configuration-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedConfigurationItems)))
            .andExpect(status().isOk());

        // Validate the ConfigurationItems in the database
        List<ConfigurationItems> configurationItemsList = configurationItemsRepository.findAll();
        assertThat(configurationItemsList).hasSize(databaseSizeBeforeUpdate);
        ConfigurationItems testConfigurationItems = configurationItemsList.get(configurationItemsList.size() - 1);
        assertThat(testConfigurationItems.getConfigCode()).isEqualTo(UPDATED_CONFIG_CODE);
        assertThat(testConfigurationItems.getConfigDescription()).isEqualTo(UPDATED_CONFIG_DESCRIPTION);
        assertThat(testConfigurationItems.isConfigEnabled()).isEqualTo(UPDATED_CONFIG_ENABLED);
        assertThat(testConfigurationItems.getConfigParamter()).isEqualTo(UPDATED_CONFIG_PARAMTER);
    }

    @Test
    @Transactional
    public void updateNonExistingConfigurationItems() throws Exception {
        int databaseSizeBeforeUpdate = configurationItemsRepository.findAll().size();

        // Create the ConfigurationItems

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConfigurationItemsMockMvc.perform(put("/api/configuration-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configurationItems)))
            .andExpect(status().isBadRequest());

        // Validate the ConfigurationItems in the database
        List<ConfigurationItems> configurationItemsList = configurationItemsRepository.findAll();
        assertThat(configurationItemsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteConfigurationItems() throws Exception {
        // Initialize the database
        configurationItemsService.save(configurationItems);

        int databaseSizeBeforeDelete = configurationItemsRepository.findAll().size();

        // Delete the configurationItems
        restConfigurationItemsMockMvc.perform(delete("/api/configuration-items/{id}", configurationItems.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ConfigurationItems> configurationItemsList = configurationItemsRepository.findAll();
        assertThat(configurationItemsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

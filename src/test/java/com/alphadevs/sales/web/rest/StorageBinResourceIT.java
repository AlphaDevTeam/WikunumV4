package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.WikunumApp;
import com.alphadevs.sales.domain.StorageBin;
import com.alphadevs.sales.domain.DocumentHistory;
import com.alphadevs.sales.repository.StorageBinRepository;
import com.alphadevs.sales.service.StorageBinService;
import com.alphadevs.sales.web.rest.errors.ExceptionTranslator;
import com.alphadevs.sales.service.dto.StorageBinCriteria;
import com.alphadevs.sales.service.StorageBinQueryService;

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
 * Integration tests for the {@link StorageBinResource} REST controller.
 */
@SpringBootTest(classes = WikunumApp.class)
public class StorageBinResourceIT {

    private static final String DEFAULT_BIN_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_BIN_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_BIN_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_BIN_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private StorageBinRepository storageBinRepository;

    @Autowired
    private StorageBinService storageBinService;

    @Autowired
    private StorageBinQueryService storageBinQueryService;

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

    private MockMvc restStorageBinMockMvc;

    private StorageBin storageBin;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final StorageBinResource storageBinResource = new StorageBinResource(storageBinService, storageBinQueryService);
        this.restStorageBinMockMvc = MockMvcBuilders.standaloneSetup(storageBinResource)
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
    public static StorageBin createEntity(EntityManager em) {
        StorageBin storageBin = new StorageBin()
            .binNumber(DEFAULT_BIN_NUMBER)
            .binDescription(DEFAULT_BIN_DESCRIPTION);
        return storageBin;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StorageBin createUpdatedEntity(EntityManager em) {
        StorageBin storageBin = new StorageBin()
            .binNumber(UPDATED_BIN_NUMBER)
            .binDescription(UPDATED_BIN_DESCRIPTION);
        return storageBin;
    }

    @BeforeEach
    public void initTest() {
        storageBin = createEntity(em);
    }

    @Test
    @Transactional
    public void createStorageBin() throws Exception {
        int databaseSizeBeforeCreate = storageBinRepository.findAll().size();

        // Create the StorageBin
        restStorageBinMockMvc.perform(post("/api/storage-bins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storageBin)))
            .andExpect(status().isCreated());

        // Validate the StorageBin in the database
        List<StorageBin> storageBinList = storageBinRepository.findAll();
        assertThat(storageBinList).hasSize(databaseSizeBeforeCreate + 1);
        StorageBin testStorageBin = storageBinList.get(storageBinList.size() - 1);
        assertThat(testStorageBin.getBinNumber()).isEqualTo(DEFAULT_BIN_NUMBER);
        assertThat(testStorageBin.getBinDescription()).isEqualTo(DEFAULT_BIN_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createStorageBinWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = storageBinRepository.findAll().size();

        // Create the StorageBin with an existing ID
        storageBin.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStorageBinMockMvc.perform(post("/api/storage-bins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storageBin)))
            .andExpect(status().isBadRequest());

        // Validate the StorageBin in the database
        List<StorageBin> storageBinList = storageBinRepository.findAll();
        assertThat(storageBinList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkBinNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = storageBinRepository.findAll().size();
        // set the field null
        storageBin.setBinNumber(null);

        // Create the StorageBin, which fails.

        restStorageBinMockMvc.perform(post("/api/storage-bins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storageBin)))
            .andExpect(status().isBadRequest());

        List<StorageBin> storageBinList = storageBinRepository.findAll();
        assertThat(storageBinList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBinDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = storageBinRepository.findAll().size();
        // set the field null
        storageBin.setBinDescription(null);

        // Create the StorageBin, which fails.

        restStorageBinMockMvc.perform(post("/api/storage-bins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storageBin)))
            .andExpect(status().isBadRequest());

        List<StorageBin> storageBinList = storageBinRepository.findAll();
        assertThat(storageBinList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStorageBins() throws Exception {
        // Initialize the database
        storageBinRepository.saveAndFlush(storageBin);

        // Get all the storageBinList
        restStorageBinMockMvc.perform(get("/api/storage-bins?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(storageBin.getId().intValue())))
            .andExpect(jsonPath("$.[*].binNumber").value(hasItem(DEFAULT_BIN_NUMBER)))
            .andExpect(jsonPath("$.[*].binDescription").value(hasItem(DEFAULT_BIN_DESCRIPTION)));
    }
    
    @Test
    @Transactional
    public void getStorageBin() throws Exception {
        // Initialize the database
        storageBinRepository.saveAndFlush(storageBin);

        // Get the storageBin
        restStorageBinMockMvc.perform(get("/api/storage-bins/{id}", storageBin.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(storageBin.getId().intValue()))
            .andExpect(jsonPath("$.binNumber").value(DEFAULT_BIN_NUMBER))
            .andExpect(jsonPath("$.binDescription").value(DEFAULT_BIN_DESCRIPTION));
    }


    @Test
    @Transactional
    public void getStorageBinsByIdFiltering() throws Exception {
        // Initialize the database
        storageBinRepository.saveAndFlush(storageBin);

        Long id = storageBin.getId();

        defaultStorageBinShouldBeFound("id.equals=" + id);
        defaultStorageBinShouldNotBeFound("id.notEquals=" + id);

        defaultStorageBinShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultStorageBinShouldNotBeFound("id.greaterThan=" + id);

        defaultStorageBinShouldBeFound("id.lessThanOrEqual=" + id);
        defaultStorageBinShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllStorageBinsByBinNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        storageBinRepository.saveAndFlush(storageBin);

        // Get all the storageBinList where binNumber equals to DEFAULT_BIN_NUMBER
        defaultStorageBinShouldBeFound("binNumber.equals=" + DEFAULT_BIN_NUMBER);

        // Get all the storageBinList where binNumber equals to UPDATED_BIN_NUMBER
        defaultStorageBinShouldNotBeFound("binNumber.equals=" + UPDATED_BIN_NUMBER);
    }

    @Test
    @Transactional
    public void getAllStorageBinsByBinNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        storageBinRepository.saveAndFlush(storageBin);

        // Get all the storageBinList where binNumber not equals to DEFAULT_BIN_NUMBER
        defaultStorageBinShouldNotBeFound("binNumber.notEquals=" + DEFAULT_BIN_NUMBER);

        // Get all the storageBinList where binNumber not equals to UPDATED_BIN_NUMBER
        defaultStorageBinShouldBeFound("binNumber.notEquals=" + UPDATED_BIN_NUMBER);
    }

    @Test
    @Transactional
    public void getAllStorageBinsByBinNumberIsInShouldWork() throws Exception {
        // Initialize the database
        storageBinRepository.saveAndFlush(storageBin);

        // Get all the storageBinList where binNumber in DEFAULT_BIN_NUMBER or UPDATED_BIN_NUMBER
        defaultStorageBinShouldBeFound("binNumber.in=" + DEFAULT_BIN_NUMBER + "," + UPDATED_BIN_NUMBER);

        // Get all the storageBinList where binNumber equals to UPDATED_BIN_NUMBER
        defaultStorageBinShouldNotBeFound("binNumber.in=" + UPDATED_BIN_NUMBER);
    }

    @Test
    @Transactional
    public void getAllStorageBinsByBinNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        storageBinRepository.saveAndFlush(storageBin);

        // Get all the storageBinList where binNumber is not null
        defaultStorageBinShouldBeFound("binNumber.specified=true");

        // Get all the storageBinList where binNumber is null
        defaultStorageBinShouldNotBeFound("binNumber.specified=false");
    }
                @Test
    @Transactional
    public void getAllStorageBinsByBinNumberContainsSomething() throws Exception {
        // Initialize the database
        storageBinRepository.saveAndFlush(storageBin);

        // Get all the storageBinList where binNumber contains DEFAULT_BIN_NUMBER
        defaultStorageBinShouldBeFound("binNumber.contains=" + DEFAULT_BIN_NUMBER);

        // Get all the storageBinList where binNumber contains UPDATED_BIN_NUMBER
        defaultStorageBinShouldNotBeFound("binNumber.contains=" + UPDATED_BIN_NUMBER);
    }

    @Test
    @Transactional
    public void getAllStorageBinsByBinNumberNotContainsSomething() throws Exception {
        // Initialize the database
        storageBinRepository.saveAndFlush(storageBin);

        // Get all the storageBinList where binNumber does not contain DEFAULT_BIN_NUMBER
        defaultStorageBinShouldNotBeFound("binNumber.doesNotContain=" + DEFAULT_BIN_NUMBER);

        // Get all the storageBinList where binNumber does not contain UPDATED_BIN_NUMBER
        defaultStorageBinShouldBeFound("binNumber.doesNotContain=" + UPDATED_BIN_NUMBER);
    }


    @Test
    @Transactional
    public void getAllStorageBinsByBinDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        storageBinRepository.saveAndFlush(storageBin);

        // Get all the storageBinList where binDescription equals to DEFAULT_BIN_DESCRIPTION
        defaultStorageBinShouldBeFound("binDescription.equals=" + DEFAULT_BIN_DESCRIPTION);

        // Get all the storageBinList where binDescription equals to UPDATED_BIN_DESCRIPTION
        defaultStorageBinShouldNotBeFound("binDescription.equals=" + UPDATED_BIN_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllStorageBinsByBinDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        storageBinRepository.saveAndFlush(storageBin);

        // Get all the storageBinList where binDescription not equals to DEFAULT_BIN_DESCRIPTION
        defaultStorageBinShouldNotBeFound("binDescription.notEquals=" + DEFAULT_BIN_DESCRIPTION);

        // Get all the storageBinList where binDescription not equals to UPDATED_BIN_DESCRIPTION
        defaultStorageBinShouldBeFound("binDescription.notEquals=" + UPDATED_BIN_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllStorageBinsByBinDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        storageBinRepository.saveAndFlush(storageBin);

        // Get all the storageBinList where binDescription in DEFAULT_BIN_DESCRIPTION or UPDATED_BIN_DESCRIPTION
        defaultStorageBinShouldBeFound("binDescription.in=" + DEFAULT_BIN_DESCRIPTION + "," + UPDATED_BIN_DESCRIPTION);

        // Get all the storageBinList where binDescription equals to UPDATED_BIN_DESCRIPTION
        defaultStorageBinShouldNotBeFound("binDescription.in=" + UPDATED_BIN_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllStorageBinsByBinDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        storageBinRepository.saveAndFlush(storageBin);

        // Get all the storageBinList where binDescription is not null
        defaultStorageBinShouldBeFound("binDescription.specified=true");

        // Get all the storageBinList where binDescription is null
        defaultStorageBinShouldNotBeFound("binDescription.specified=false");
    }
                @Test
    @Transactional
    public void getAllStorageBinsByBinDescriptionContainsSomething() throws Exception {
        // Initialize the database
        storageBinRepository.saveAndFlush(storageBin);

        // Get all the storageBinList where binDescription contains DEFAULT_BIN_DESCRIPTION
        defaultStorageBinShouldBeFound("binDescription.contains=" + DEFAULT_BIN_DESCRIPTION);

        // Get all the storageBinList where binDescription contains UPDATED_BIN_DESCRIPTION
        defaultStorageBinShouldNotBeFound("binDescription.contains=" + UPDATED_BIN_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllStorageBinsByBinDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        storageBinRepository.saveAndFlush(storageBin);

        // Get all the storageBinList where binDescription does not contain DEFAULT_BIN_DESCRIPTION
        defaultStorageBinShouldNotBeFound("binDescription.doesNotContain=" + DEFAULT_BIN_DESCRIPTION);

        // Get all the storageBinList where binDescription does not contain UPDATED_BIN_DESCRIPTION
        defaultStorageBinShouldBeFound("binDescription.doesNotContain=" + UPDATED_BIN_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllStorageBinsByHistoryIsEqualToSomething() throws Exception {
        // Initialize the database
        storageBinRepository.saveAndFlush(storageBin);
        DocumentHistory history = DocumentHistoryResourceIT.createEntity(em);
        em.persist(history);
        em.flush();
        storageBin.setHistory(history);
        storageBinRepository.saveAndFlush(storageBin);
        Long historyId = history.getId();

        // Get all the storageBinList where history equals to historyId
        defaultStorageBinShouldBeFound("historyId.equals=" + historyId);

        // Get all the storageBinList where history equals to historyId + 1
        defaultStorageBinShouldNotBeFound("historyId.equals=" + (historyId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultStorageBinShouldBeFound(String filter) throws Exception {
        restStorageBinMockMvc.perform(get("/api/storage-bins?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(storageBin.getId().intValue())))
            .andExpect(jsonPath("$.[*].binNumber").value(hasItem(DEFAULT_BIN_NUMBER)))
            .andExpect(jsonPath("$.[*].binDescription").value(hasItem(DEFAULT_BIN_DESCRIPTION)));

        // Check, that the count call also returns 1
        restStorageBinMockMvc.perform(get("/api/storage-bins/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultStorageBinShouldNotBeFound(String filter) throws Exception {
        restStorageBinMockMvc.perform(get("/api/storage-bins?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStorageBinMockMvc.perform(get("/api/storage-bins/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingStorageBin() throws Exception {
        // Get the storageBin
        restStorageBinMockMvc.perform(get("/api/storage-bins/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStorageBin() throws Exception {
        // Initialize the database
        storageBinService.save(storageBin);

        int databaseSizeBeforeUpdate = storageBinRepository.findAll().size();

        // Update the storageBin
        StorageBin updatedStorageBin = storageBinRepository.findById(storageBin.getId()).get();
        // Disconnect from session so that the updates on updatedStorageBin are not directly saved in db
        em.detach(updatedStorageBin);
        updatedStorageBin
            .binNumber(UPDATED_BIN_NUMBER)
            .binDescription(UPDATED_BIN_DESCRIPTION);

        restStorageBinMockMvc.perform(put("/api/storage-bins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedStorageBin)))
            .andExpect(status().isOk());

        // Validate the StorageBin in the database
        List<StorageBin> storageBinList = storageBinRepository.findAll();
        assertThat(storageBinList).hasSize(databaseSizeBeforeUpdate);
        StorageBin testStorageBin = storageBinList.get(storageBinList.size() - 1);
        assertThat(testStorageBin.getBinNumber()).isEqualTo(UPDATED_BIN_NUMBER);
        assertThat(testStorageBin.getBinDescription()).isEqualTo(UPDATED_BIN_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingStorageBin() throws Exception {
        int databaseSizeBeforeUpdate = storageBinRepository.findAll().size();

        // Create the StorageBin

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStorageBinMockMvc.perform(put("/api/storage-bins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storageBin)))
            .andExpect(status().isBadRequest());

        // Validate the StorageBin in the database
        List<StorageBin> storageBinList = storageBinRepository.findAll();
        assertThat(storageBinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteStorageBin() throws Exception {
        // Initialize the database
        storageBinService.save(storageBin);

        int databaseSizeBeforeDelete = storageBinRepository.findAll().size();

        // Delete the storageBin
        restStorageBinMockMvc.perform(delete("/api/storage-bins/{id}", storageBin.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<StorageBin> storageBinList = storageBinRepository.findAll();
        assertThat(storageBinList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

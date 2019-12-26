package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.WikunumApp;
import com.alphadevs.sales.domain.ChangeLog;
import com.alphadevs.sales.domain.DocumentHistory;
import com.alphadevs.sales.repository.ChangeLogRepository;
import com.alphadevs.sales.service.ChangeLogService;
import com.alphadevs.sales.web.rest.errors.ExceptionTranslator;
import com.alphadevs.sales.service.dto.ChangeLogCriteria;
import com.alphadevs.sales.service.ChangeLogQueryService;

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
 * Integration tests for the {@link ChangeLogResource} REST controller.
 */
@SpringBootTest(classes = WikunumApp.class)
public class ChangeLogResourceIT {

    private static final String DEFAULT_CHANGE_KEY = "AAAAAAAAAA";
    private static final String UPDATED_CHANGE_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_CHANGE_FROM = "AAAAAAAAAA";
    private static final String UPDATED_CHANGE_FROM = "BBBBBBBBBB";

    private static final String DEFAULT_CHANGE_TO = "AAAAAAAAAA";
    private static final String UPDATED_CHANGE_TO = "BBBBBBBBBB";

    @Autowired
    private ChangeLogRepository changeLogRepository;

    @Autowired
    private ChangeLogService changeLogService;

    @Autowired
    private ChangeLogQueryService changeLogQueryService;

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

    private MockMvc restChangeLogMockMvc;

    private ChangeLog changeLog;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ChangeLogResource changeLogResource = new ChangeLogResource(changeLogService, changeLogQueryService);
        this.restChangeLogMockMvc = MockMvcBuilders.standaloneSetup(changeLogResource)
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
    public static ChangeLog createEntity(EntityManager em) {
        ChangeLog changeLog = new ChangeLog()
            .changeKey(DEFAULT_CHANGE_KEY)
            .changeFrom(DEFAULT_CHANGE_FROM)
            .changeTo(DEFAULT_CHANGE_TO);
        return changeLog;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChangeLog createUpdatedEntity(EntityManager em) {
        ChangeLog changeLog = new ChangeLog()
            .changeKey(UPDATED_CHANGE_KEY)
            .changeFrom(UPDATED_CHANGE_FROM)
            .changeTo(UPDATED_CHANGE_TO);
        return changeLog;
    }

    @BeforeEach
    public void initTest() {
        changeLog = createEntity(em);
    }

    @Test
    @Transactional
    public void createChangeLog() throws Exception {
        int databaseSizeBeforeCreate = changeLogRepository.findAll().size();

        // Create the ChangeLog
        restChangeLogMockMvc.perform(post("/api/change-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(changeLog)))
            .andExpect(status().isCreated());

        // Validate the ChangeLog in the database
        List<ChangeLog> changeLogList = changeLogRepository.findAll();
        assertThat(changeLogList).hasSize(databaseSizeBeforeCreate + 1);
        ChangeLog testChangeLog = changeLogList.get(changeLogList.size() - 1);
        assertThat(testChangeLog.getChangeKey()).isEqualTo(DEFAULT_CHANGE_KEY);
        assertThat(testChangeLog.getChangeFrom()).isEqualTo(DEFAULT_CHANGE_FROM);
        assertThat(testChangeLog.getChangeTo()).isEqualTo(DEFAULT_CHANGE_TO);
    }

    @Test
    @Transactional
    public void createChangeLogWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = changeLogRepository.findAll().size();

        // Create the ChangeLog with an existing ID
        changeLog.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restChangeLogMockMvc.perform(post("/api/change-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(changeLog)))
            .andExpect(status().isBadRequest());

        // Validate the ChangeLog in the database
        List<ChangeLog> changeLogList = changeLogRepository.findAll();
        assertThat(changeLogList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkChangeKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = changeLogRepository.findAll().size();
        // set the field null
        changeLog.setChangeKey(null);

        // Create the ChangeLog, which fails.

        restChangeLogMockMvc.perform(post("/api/change-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(changeLog)))
            .andExpect(status().isBadRequest());

        List<ChangeLog> changeLogList = changeLogRepository.findAll();
        assertThat(changeLogList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkChangeFromIsRequired() throws Exception {
        int databaseSizeBeforeTest = changeLogRepository.findAll().size();
        // set the field null
        changeLog.setChangeFrom(null);

        // Create the ChangeLog, which fails.

        restChangeLogMockMvc.perform(post("/api/change-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(changeLog)))
            .andExpect(status().isBadRequest());

        List<ChangeLog> changeLogList = changeLogRepository.findAll();
        assertThat(changeLogList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkChangeToIsRequired() throws Exception {
        int databaseSizeBeforeTest = changeLogRepository.findAll().size();
        // set the field null
        changeLog.setChangeTo(null);

        // Create the ChangeLog, which fails.

        restChangeLogMockMvc.perform(post("/api/change-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(changeLog)))
            .andExpect(status().isBadRequest());

        List<ChangeLog> changeLogList = changeLogRepository.findAll();
        assertThat(changeLogList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllChangeLogs() throws Exception {
        // Initialize the database
        changeLogRepository.saveAndFlush(changeLog);

        // Get all the changeLogList
        restChangeLogMockMvc.perform(get("/api/change-logs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(changeLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].changeKey").value(hasItem(DEFAULT_CHANGE_KEY)))
            .andExpect(jsonPath("$.[*].changeFrom").value(hasItem(DEFAULT_CHANGE_FROM)))
            .andExpect(jsonPath("$.[*].changeTo").value(hasItem(DEFAULT_CHANGE_TO)));
    }
    
    @Test
    @Transactional
    public void getChangeLog() throws Exception {
        // Initialize the database
        changeLogRepository.saveAndFlush(changeLog);

        // Get the changeLog
        restChangeLogMockMvc.perform(get("/api/change-logs/{id}", changeLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(changeLog.getId().intValue()))
            .andExpect(jsonPath("$.changeKey").value(DEFAULT_CHANGE_KEY))
            .andExpect(jsonPath("$.changeFrom").value(DEFAULT_CHANGE_FROM))
            .andExpect(jsonPath("$.changeTo").value(DEFAULT_CHANGE_TO));
    }


    @Test
    @Transactional
    public void getChangeLogsByIdFiltering() throws Exception {
        // Initialize the database
        changeLogRepository.saveAndFlush(changeLog);

        Long id = changeLog.getId();

        defaultChangeLogShouldBeFound("id.equals=" + id);
        defaultChangeLogShouldNotBeFound("id.notEquals=" + id);

        defaultChangeLogShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultChangeLogShouldNotBeFound("id.greaterThan=" + id);

        defaultChangeLogShouldBeFound("id.lessThanOrEqual=" + id);
        defaultChangeLogShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllChangeLogsByChangeKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        changeLogRepository.saveAndFlush(changeLog);

        // Get all the changeLogList where changeKey equals to DEFAULT_CHANGE_KEY
        defaultChangeLogShouldBeFound("changeKey.equals=" + DEFAULT_CHANGE_KEY);

        // Get all the changeLogList where changeKey equals to UPDATED_CHANGE_KEY
        defaultChangeLogShouldNotBeFound("changeKey.equals=" + UPDATED_CHANGE_KEY);
    }

    @Test
    @Transactional
    public void getAllChangeLogsByChangeKeyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        changeLogRepository.saveAndFlush(changeLog);

        // Get all the changeLogList where changeKey not equals to DEFAULT_CHANGE_KEY
        defaultChangeLogShouldNotBeFound("changeKey.notEquals=" + DEFAULT_CHANGE_KEY);

        // Get all the changeLogList where changeKey not equals to UPDATED_CHANGE_KEY
        defaultChangeLogShouldBeFound("changeKey.notEquals=" + UPDATED_CHANGE_KEY);
    }

    @Test
    @Transactional
    public void getAllChangeLogsByChangeKeyIsInShouldWork() throws Exception {
        // Initialize the database
        changeLogRepository.saveAndFlush(changeLog);

        // Get all the changeLogList where changeKey in DEFAULT_CHANGE_KEY or UPDATED_CHANGE_KEY
        defaultChangeLogShouldBeFound("changeKey.in=" + DEFAULT_CHANGE_KEY + "," + UPDATED_CHANGE_KEY);

        // Get all the changeLogList where changeKey equals to UPDATED_CHANGE_KEY
        defaultChangeLogShouldNotBeFound("changeKey.in=" + UPDATED_CHANGE_KEY);
    }

    @Test
    @Transactional
    public void getAllChangeLogsByChangeKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        changeLogRepository.saveAndFlush(changeLog);

        // Get all the changeLogList where changeKey is not null
        defaultChangeLogShouldBeFound("changeKey.specified=true");

        // Get all the changeLogList where changeKey is null
        defaultChangeLogShouldNotBeFound("changeKey.specified=false");
    }
                @Test
    @Transactional
    public void getAllChangeLogsByChangeKeyContainsSomething() throws Exception {
        // Initialize the database
        changeLogRepository.saveAndFlush(changeLog);

        // Get all the changeLogList where changeKey contains DEFAULT_CHANGE_KEY
        defaultChangeLogShouldBeFound("changeKey.contains=" + DEFAULT_CHANGE_KEY);

        // Get all the changeLogList where changeKey contains UPDATED_CHANGE_KEY
        defaultChangeLogShouldNotBeFound("changeKey.contains=" + UPDATED_CHANGE_KEY);
    }

    @Test
    @Transactional
    public void getAllChangeLogsByChangeKeyNotContainsSomething() throws Exception {
        // Initialize the database
        changeLogRepository.saveAndFlush(changeLog);

        // Get all the changeLogList where changeKey does not contain DEFAULT_CHANGE_KEY
        defaultChangeLogShouldNotBeFound("changeKey.doesNotContain=" + DEFAULT_CHANGE_KEY);

        // Get all the changeLogList where changeKey does not contain UPDATED_CHANGE_KEY
        defaultChangeLogShouldBeFound("changeKey.doesNotContain=" + UPDATED_CHANGE_KEY);
    }


    @Test
    @Transactional
    public void getAllChangeLogsByChangeFromIsEqualToSomething() throws Exception {
        // Initialize the database
        changeLogRepository.saveAndFlush(changeLog);

        // Get all the changeLogList where changeFrom equals to DEFAULT_CHANGE_FROM
        defaultChangeLogShouldBeFound("changeFrom.equals=" + DEFAULT_CHANGE_FROM);

        // Get all the changeLogList where changeFrom equals to UPDATED_CHANGE_FROM
        defaultChangeLogShouldNotBeFound("changeFrom.equals=" + UPDATED_CHANGE_FROM);
    }

    @Test
    @Transactional
    public void getAllChangeLogsByChangeFromIsNotEqualToSomething() throws Exception {
        // Initialize the database
        changeLogRepository.saveAndFlush(changeLog);

        // Get all the changeLogList where changeFrom not equals to DEFAULT_CHANGE_FROM
        defaultChangeLogShouldNotBeFound("changeFrom.notEquals=" + DEFAULT_CHANGE_FROM);

        // Get all the changeLogList where changeFrom not equals to UPDATED_CHANGE_FROM
        defaultChangeLogShouldBeFound("changeFrom.notEquals=" + UPDATED_CHANGE_FROM);
    }

    @Test
    @Transactional
    public void getAllChangeLogsByChangeFromIsInShouldWork() throws Exception {
        // Initialize the database
        changeLogRepository.saveAndFlush(changeLog);

        // Get all the changeLogList where changeFrom in DEFAULT_CHANGE_FROM or UPDATED_CHANGE_FROM
        defaultChangeLogShouldBeFound("changeFrom.in=" + DEFAULT_CHANGE_FROM + "," + UPDATED_CHANGE_FROM);

        // Get all the changeLogList where changeFrom equals to UPDATED_CHANGE_FROM
        defaultChangeLogShouldNotBeFound("changeFrom.in=" + UPDATED_CHANGE_FROM);
    }

    @Test
    @Transactional
    public void getAllChangeLogsByChangeFromIsNullOrNotNull() throws Exception {
        // Initialize the database
        changeLogRepository.saveAndFlush(changeLog);

        // Get all the changeLogList where changeFrom is not null
        defaultChangeLogShouldBeFound("changeFrom.specified=true");

        // Get all the changeLogList where changeFrom is null
        defaultChangeLogShouldNotBeFound("changeFrom.specified=false");
    }
                @Test
    @Transactional
    public void getAllChangeLogsByChangeFromContainsSomething() throws Exception {
        // Initialize the database
        changeLogRepository.saveAndFlush(changeLog);

        // Get all the changeLogList where changeFrom contains DEFAULT_CHANGE_FROM
        defaultChangeLogShouldBeFound("changeFrom.contains=" + DEFAULT_CHANGE_FROM);

        // Get all the changeLogList where changeFrom contains UPDATED_CHANGE_FROM
        defaultChangeLogShouldNotBeFound("changeFrom.contains=" + UPDATED_CHANGE_FROM);
    }

    @Test
    @Transactional
    public void getAllChangeLogsByChangeFromNotContainsSomething() throws Exception {
        // Initialize the database
        changeLogRepository.saveAndFlush(changeLog);

        // Get all the changeLogList where changeFrom does not contain DEFAULT_CHANGE_FROM
        defaultChangeLogShouldNotBeFound("changeFrom.doesNotContain=" + DEFAULT_CHANGE_FROM);

        // Get all the changeLogList where changeFrom does not contain UPDATED_CHANGE_FROM
        defaultChangeLogShouldBeFound("changeFrom.doesNotContain=" + UPDATED_CHANGE_FROM);
    }


    @Test
    @Transactional
    public void getAllChangeLogsByChangeToIsEqualToSomething() throws Exception {
        // Initialize the database
        changeLogRepository.saveAndFlush(changeLog);

        // Get all the changeLogList where changeTo equals to DEFAULT_CHANGE_TO
        defaultChangeLogShouldBeFound("changeTo.equals=" + DEFAULT_CHANGE_TO);

        // Get all the changeLogList where changeTo equals to UPDATED_CHANGE_TO
        defaultChangeLogShouldNotBeFound("changeTo.equals=" + UPDATED_CHANGE_TO);
    }

    @Test
    @Transactional
    public void getAllChangeLogsByChangeToIsNotEqualToSomething() throws Exception {
        // Initialize the database
        changeLogRepository.saveAndFlush(changeLog);

        // Get all the changeLogList where changeTo not equals to DEFAULT_CHANGE_TO
        defaultChangeLogShouldNotBeFound("changeTo.notEquals=" + DEFAULT_CHANGE_TO);

        // Get all the changeLogList where changeTo not equals to UPDATED_CHANGE_TO
        defaultChangeLogShouldBeFound("changeTo.notEquals=" + UPDATED_CHANGE_TO);
    }

    @Test
    @Transactional
    public void getAllChangeLogsByChangeToIsInShouldWork() throws Exception {
        // Initialize the database
        changeLogRepository.saveAndFlush(changeLog);

        // Get all the changeLogList where changeTo in DEFAULT_CHANGE_TO or UPDATED_CHANGE_TO
        defaultChangeLogShouldBeFound("changeTo.in=" + DEFAULT_CHANGE_TO + "," + UPDATED_CHANGE_TO);

        // Get all the changeLogList where changeTo equals to UPDATED_CHANGE_TO
        defaultChangeLogShouldNotBeFound("changeTo.in=" + UPDATED_CHANGE_TO);
    }

    @Test
    @Transactional
    public void getAllChangeLogsByChangeToIsNullOrNotNull() throws Exception {
        // Initialize the database
        changeLogRepository.saveAndFlush(changeLog);

        // Get all the changeLogList where changeTo is not null
        defaultChangeLogShouldBeFound("changeTo.specified=true");

        // Get all the changeLogList where changeTo is null
        defaultChangeLogShouldNotBeFound("changeTo.specified=false");
    }
                @Test
    @Transactional
    public void getAllChangeLogsByChangeToContainsSomething() throws Exception {
        // Initialize the database
        changeLogRepository.saveAndFlush(changeLog);

        // Get all the changeLogList where changeTo contains DEFAULT_CHANGE_TO
        defaultChangeLogShouldBeFound("changeTo.contains=" + DEFAULT_CHANGE_TO);

        // Get all the changeLogList where changeTo contains UPDATED_CHANGE_TO
        defaultChangeLogShouldNotBeFound("changeTo.contains=" + UPDATED_CHANGE_TO);
    }

    @Test
    @Transactional
    public void getAllChangeLogsByChangeToNotContainsSomething() throws Exception {
        // Initialize the database
        changeLogRepository.saveAndFlush(changeLog);

        // Get all the changeLogList where changeTo does not contain DEFAULT_CHANGE_TO
        defaultChangeLogShouldNotBeFound("changeTo.doesNotContain=" + DEFAULT_CHANGE_TO);

        // Get all the changeLogList where changeTo does not contain UPDATED_CHANGE_TO
        defaultChangeLogShouldBeFound("changeTo.doesNotContain=" + UPDATED_CHANGE_TO);
    }


    @Test
    @Transactional
    public void getAllChangeLogsByDocumentHistoryIsEqualToSomething() throws Exception {
        // Initialize the database
        changeLogRepository.saveAndFlush(changeLog);
        DocumentHistory documentHistory = DocumentHistoryResourceIT.createEntity(em);
        em.persist(documentHistory);
        em.flush();
        changeLog.addDocumentHistory(documentHistory);
        changeLogRepository.saveAndFlush(changeLog);
        Long documentHistoryId = documentHistory.getId();

        // Get all the changeLogList where documentHistory equals to documentHistoryId
        defaultChangeLogShouldBeFound("documentHistoryId.equals=" + documentHistoryId);

        // Get all the changeLogList where documentHistory equals to documentHistoryId + 1
        defaultChangeLogShouldNotBeFound("documentHistoryId.equals=" + (documentHistoryId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultChangeLogShouldBeFound(String filter) throws Exception {
        restChangeLogMockMvc.perform(get("/api/change-logs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(changeLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].changeKey").value(hasItem(DEFAULT_CHANGE_KEY)))
            .andExpect(jsonPath("$.[*].changeFrom").value(hasItem(DEFAULT_CHANGE_FROM)))
            .andExpect(jsonPath("$.[*].changeTo").value(hasItem(DEFAULT_CHANGE_TO)));

        // Check, that the count call also returns 1
        restChangeLogMockMvc.perform(get("/api/change-logs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultChangeLogShouldNotBeFound(String filter) throws Exception {
        restChangeLogMockMvc.perform(get("/api/change-logs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restChangeLogMockMvc.perform(get("/api/change-logs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingChangeLog() throws Exception {
        // Get the changeLog
        restChangeLogMockMvc.perform(get("/api/change-logs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChangeLog() throws Exception {
        // Initialize the database
        changeLogService.save(changeLog);

        int databaseSizeBeforeUpdate = changeLogRepository.findAll().size();

        // Update the changeLog
        ChangeLog updatedChangeLog = changeLogRepository.findById(changeLog.getId()).get();
        // Disconnect from session so that the updates on updatedChangeLog are not directly saved in db
        em.detach(updatedChangeLog);
        updatedChangeLog
            .changeKey(UPDATED_CHANGE_KEY)
            .changeFrom(UPDATED_CHANGE_FROM)
            .changeTo(UPDATED_CHANGE_TO);

        restChangeLogMockMvc.perform(put("/api/change-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedChangeLog)))
            .andExpect(status().isOk());

        // Validate the ChangeLog in the database
        List<ChangeLog> changeLogList = changeLogRepository.findAll();
        assertThat(changeLogList).hasSize(databaseSizeBeforeUpdate);
        ChangeLog testChangeLog = changeLogList.get(changeLogList.size() - 1);
        assertThat(testChangeLog.getChangeKey()).isEqualTo(UPDATED_CHANGE_KEY);
        assertThat(testChangeLog.getChangeFrom()).isEqualTo(UPDATED_CHANGE_FROM);
        assertThat(testChangeLog.getChangeTo()).isEqualTo(UPDATED_CHANGE_TO);
    }

    @Test
    @Transactional
    public void updateNonExistingChangeLog() throws Exception {
        int databaseSizeBeforeUpdate = changeLogRepository.findAll().size();

        // Create the ChangeLog

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChangeLogMockMvc.perform(put("/api/change-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(changeLog)))
            .andExpect(status().isBadRequest());

        // Validate the ChangeLog in the database
        List<ChangeLog> changeLogList = changeLogRepository.findAll();
        assertThat(changeLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteChangeLog() throws Exception {
        // Initialize the database
        changeLogService.save(changeLog);

        int databaseSizeBeforeDelete = changeLogRepository.findAll().size();

        // Delete the changeLog
        restChangeLogMockMvc.perform(delete("/api/change-logs/{id}", changeLog.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ChangeLog> changeLogList = changeLogRepository.findAll();
        assertThat(changeLogList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

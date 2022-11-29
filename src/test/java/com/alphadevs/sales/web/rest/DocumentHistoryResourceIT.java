package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.WikunumApp;
import com.alphadevs.sales.domain.DocumentHistory;
import com.alphadevs.sales.domain.DocumentType;
import com.alphadevs.sales.domain.ExUser;
import com.alphadevs.sales.domain.ChangeLog;
import com.alphadevs.sales.repository.DocumentHistoryRepository;
import com.alphadevs.sales.service.DocumentHistoryService;
import com.alphadevs.sales.web.rest.errors.ExceptionTranslator;
import com.alphadevs.sales.service.dto.DocumentHistoryCriteria;
import com.alphadevs.sales.service.DocumentHistoryQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static com.alphadevs.sales.web.rest.TestUtil.sameInstant;
import static com.alphadevs.sales.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link DocumentHistoryResource} REST controller.
 */
@SpringBootTest(classes = WikunumApp.class)
public class DocumentHistoryResourceIT {

    private static final String DEFAULT_HISTORY_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_HISTORY_DESCRIPTION = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_HISTORY_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_HISTORY_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_HISTORY_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    @Autowired
    private DocumentHistoryRepository documentHistoryRepository;

    @Mock
    private DocumentHistoryRepository documentHistoryRepositoryMock;

    @Mock
    private DocumentHistoryService documentHistoryServiceMock;

    @Autowired
    private DocumentHistoryService documentHistoryService;

    @Autowired
    private DocumentHistoryQueryService documentHistoryQueryService;

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

    private MockMvc restDocumentHistoryMockMvc;

    private DocumentHistory documentHistory;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DocumentHistoryResource documentHistoryResource = new DocumentHistoryResource(documentHistoryService, documentHistoryQueryService);
        this.restDocumentHistoryMockMvc = MockMvcBuilders.standaloneSetup(documentHistoryResource)
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
    public static DocumentHistory createEntity(EntityManager em) {
        DocumentHistory documentHistory = new DocumentHistory()
            .historyDescription(DEFAULT_HISTORY_DESCRIPTION)
            .historyDate(DEFAULT_HISTORY_DATE);
        // Add required entity
        DocumentType documentType;
        if (TestUtil.findAll(em, DocumentType.class).isEmpty()) {
            documentType = DocumentTypeResourceIT.createEntity(em);
            em.persist(documentType);
            em.flush();
        } else {
            documentType = TestUtil.findAll(em, DocumentType.class).get(0);
        }
        documentHistory.setType(documentType);
        // Add required entity
        ExUser exUser;
        if (TestUtil.findAll(em, ExUser.class).isEmpty()) {
            exUser = ExUserResourceIT.createEntity(em);
            em.persist(exUser);
            em.flush();
        } else {
            exUser = TestUtil.findAll(em, ExUser.class).get(0);
        }
        documentHistory.setLastModifiedUser(exUser);
        // Add required entity
        documentHistory.setCreatedUser(exUser);
        return documentHistory;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentHistory createUpdatedEntity(EntityManager em) {
        DocumentHistory documentHistory = new DocumentHistory()
            .historyDescription(UPDATED_HISTORY_DESCRIPTION)
            .historyDate(UPDATED_HISTORY_DATE);
        // Add required entity
        DocumentType documentType;
        if (TestUtil.findAll(em, DocumentType.class).isEmpty()) {
            documentType = DocumentTypeResourceIT.createUpdatedEntity(em);
            em.persist(documentType);
            em.flush();
        } else {
            documentType = TestUtil.findAll(em, DocumentType.class).get(0);
        }
        documentHistory.setType(documentType);
        // Add required entity
        ExUser exUser;
        if (TestUtil.findAll(em, ExUser.class).isEmpty()) {
            exUser = ExUserResourceIT.createUpdatedEntity(em);
            em.persist(exUser);
            em.flush();
        } else {
            exUser = TestUtil.findAll(em, ExUser.class).get(0);
        }
        documentHistory.setLastModifiedUser(exUser);
        // Add required entity
        documentHistory.setCreatedUser(exUser);
        return documentHistory;
    }

    @BeforeEach
    public void initTest() {
        documentHistory = createEntity(em);
    }

    @Test
    @Transactional
    public void createDocumentHistory() throws Exception {
        int databaseSizeBeforeCreate = documentHistoryRepository.findAll().size();

        // Create the DocumentHistory
        restDocumentHistoryMockMvc.perform(post("/api/document-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documentHistory)))
            .andExpect(status().isCreated());

        // Validate the DocumentHistory in the database
        List<DocumentHistory> documentHistoryList = documentHistoryRepository.findAll();
        assertThat(documentHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        DocumentHistory testDocumentHistory = documentHistoryList.get(documentHistoryList.size() - 1);
        assertThat(testDocumentHistory.getHistoryDescription()).isEqualTo(DEFAULT_HISTORY_DESCRIPTION);
        assertThat(testDocumentHistory.getHistoryDate()).isEqualTo(DEFAULT_HISTORY_DATE);
    }

    @Test
    @Transactional
    public void createDocumentHistoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = documentHistoryRepository.findAll().size();

        // Create the DocumentHistory with an existing ID
        documentHistory.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentHistoryMockMvc.perform(post("/api/document-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documentHistory)))
            .andExpect(status().isBadRequest());

        // Validate the DocumentHistory in the database
        List<DocumentHistory> documentHistoryList = documentHistoryRepository.findAll();
        assertThat(documentHistoryList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkHistoryDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = documentHistoryRepository.findAll().size();
        // set the field null
        documentHistory.setHistoryDescription(null);

        // Create the DocumentHistory, which fails.

        restDocumentHistoryMockMvc.perform(post("/api/document-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documentHistory)))
            .andExpect(status().isBadRequest());

        List<DocumentHistory> documentHistoryList = documentHistoryRepository.findAll();
        assertThat(documentHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkHistoryDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = documentHistoryRepository.findAll().size();
        // set the field null
        documentHistory.setHistoryDate(null);

        // Create the DocumentHistory, which fails.

        restDocumentHistoryMockMvc.perform(post("/api/document-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documentHistory)))
            .andExpect(status().isBadRequest());

        List<DocumentHistory> documentHistoryList = documentHistoryRepository.findAll();
        assertThat(documentHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDocumentHistories() throws Exception {
        // Initialize the database
        documentHistoryRepository.saveAndFlush(documentHistory);

        // Get all the documentHistoryList
        restDocumentHistoryMockMvc.perform(get("/api/document-histories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].historyDescription").value(hasItem(DEFAULT_HISTORY_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].historyDate").value(hasItem(sameInstant(DEFAULT_HISTORY_DATE))));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllDocumentHistoriesWithEagerRelationshipsIsEnabled() throws Exception {
        DocumentHistoryResource documentHistoryResource = new DocumentHistoryResource(documentHistoryServiceMock, documentHistoryQueryService);
        when(documentHistoryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restDocumentHistoryMockMvc = MockMvcBuilders.standaloneSetup(documentHistoryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restDocumentHistoryMockMvc.perform(get("/api/document-histories?eagerload=true"))
        .andExpect(status().isOk());

        verify(documentHistoryServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllDocumentHistoriesWithEagerRelationshipsIsNotEnabled() throws Exception {
        DocumentHistoryResource documentHistoryResource = new DocumentHistoryResource(documentHistoryServiceMock, documentHistoryQueryService);
            when(documentHistoryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restDocumentHistoryMockMvc = MockMvcBuilders.standaloneSetup(documentHistoryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restDocumentHistoryMockMvc.perform(get("/api/document-histories?eagerload=true"))
        .andExpect(status().isOk());

            verify(documentHistoryServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getDocumentHistory() throws Exception {
        // Initialize the database
        documentHistoryRepository.saveAndFlush(documentHistory);

        // Get the documentHistory
        restDocumentHistoryMockMvc.perform(get("/api/document-histories/{id}", documentHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(documentHistory.getId().intValue()))
            .andExpect(jsonPath("$.historyDescription").value(DEFAULT_HISTORY_DESCRIPTION))
            .andExpect(jsonPath("$.historyDate").value(sameInstant(DEFAULT_HISTORY_DATE)));
    }


    @Test
    @Transactional
    public void getDocumentHistoriesByIdFiltering() throws Exception {
        // Initialize the database
        documentHistoryRepository.saveAndFlush(documentHistory);

        Long id = documentHistory.getId();

        defaultDocumentHistoryShouldBeFound("id.equals=" + id);
        defaultDocumentHistoryShouldNotBeFound("id.notEquals=" + id);

        defaultDocumentHistoryShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDocumentHistoryShouldNotBeFound("id.greaterThan=" + id);

        defaultDocumentHistoryShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDocumentHistoryShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllDocumentHistoriesByHistoryDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        documentHistoryRepository.saveAndFlush(documentHistory);

        // Get all the documentHistoryList where historyDescription equals to DEFAULT_HISTORY_DESCRIPTION
        defaultDocumentHistoryShouldBeFound("historyDescription.equals=" + DEFAULT_HISTORY_DESCRIPTION);

        // Get all the documentHistoryList where historyDescription equals to UPDATED_HISTORY_DESCRIPTION
        defaultDocumentHistoryShouldNotBeFound("historyDescription.equals=" + UPDATED_HISTORY_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllDocumentHistoriesByHistoryDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        documentHistoryRepository.saveAndFlush(documentHistory);

        // Get all the documentHistoryList where historyDescription not equals to DEFAULT_HISTORY_DESCRIPTION
        defaultDocumentHistoryShouldNotBeFound("historyDescription.notEquals=" + DEFAULT_HISTORY_DESCRIPTION);

        // Get all the documentHistoryList where historyDescription not equals to UPDATED_HISTORY_DESCRIPTION
        defaultDocumentHistoryShouldBeFound("historyDescription.notEquals=" + UPDATED_HISTORY_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllDocumentHistoriesByHistoryDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        documentHistoryRepository.saveAndFlush(documentHistory);

        // Get all the documentHistoryList where historyDescription in DEFAULT_HISTORY_DESCRIPTION or UPDATED_HISTORY_DESCRIPTION
        defaultDocumentHistoryShouldBeFound("historyDescription.in=" + DEFAULT_HISTORY_DESCRIPTION + "," + UPDATED_HISTORY_DESCRIPTION);

        // Get all the documentHistoryList where historyDescription equals to UPDATED_HISTORY_DESCRIPTION
        defaultDocumentHistoryShouldNotBeFound("historyDescription.in=" + UPDATED_HISTORY_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllDocumentHistoriesByHistoryDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        documentHistoryRepository.saveAndFlush(documentHistory);

        // Get all the documentHistoryList where historyDescription is not null
        defaultDocumentHistoryShouldBeFound("historyDescription.specified=true");

        // Get all the documentHistoryList where historyDescription is null
        defaultDocumentHistoryShouldNotBeFound("historyDescription.specified=false");
    }
                @Test
    @Transactional
    public void getAllDocumentHistoriesByHistoryDescriptionContainsSomething() throws Exception {
        // Initialize the database
        documentHistoryRepository.saveAndFlush(documentHistory);

        // Get all the documentHistoryList where historyDescription contains DEFAULT_HISTORY_DESCRIPTION
        defaultDocumentHistoryShouldBeFound("historyDescription.contains=" + DEFAULT_HISTORY_DESCRIPTION);

        // Get all the documentHistoryList where historyDescription contains UPDATED_HISTORY_DESCRIPTION
        defaultDocumentHistoryShouldNotBeFound("historyDescription.contains=" + UPDATED_HISTORY_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllDocumentHistoriesByHistoryDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        documentHistoryRepository.saveAndFlush(documentHistory);

        // Get all the documentHistoryList where historyDescription does not contain DEFAULT_HISTORY_DESCRIPTION
        defaultDocumentHistoryShouldNotBeFound("historyDescription.doesNotContain=" + DEFAULT_HISTORY_DESCRIPTION);

        // Get all the documentHistoryList where historyDescription does not contain UPDATED_HISTORY_DESCRIPTION
        defaultDocumentHistoryShouldBeFound("historyDescription.doesNotContain=" + UPDATED_HISTORY_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllDocumentHistoriesByHistoryDateIsEqualToSomething() throws Exception {
        // Initialize the database
        documentHistoryRepository.saveAndFlush(documentHistory);

        // Get all the documentHistoryList where historyDate equals to DEFAULT_HISTORY_DATE
        defaultDocumentHistoryShouldBeFound("historyDate.equals=" + DEFAULT_HISTORY_DATE);

        // Get all the documentHistoryList where historyDate equals to UPDATED_HISTORY_DATE
        defaultDocumentHistoryShouldNotBeFound("historyDate.equals=" + UPDATED_HISTORY_DATE);
    }

    @Test
    @Transactional
    public void getAllDocumentHistoriesByHistoryDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        documentHistoryRepository.saveAndFlush(documentHistory);

        // Get all the documentHistoryList where historyDate not equals to DEFAULT_HISTORY_DATE
        defaultDocumentHistoryShouldNotBeFound("historyDate.notEquals=" + DEFAULT_HISTORY_DATE);

        // Get all the documentHistoryList where historyDate not equals to UPDATED_HISTORY_DATE
        defaultDocumentHistoryShouldBeFound("historyDate.notEquals=" + UPDATED_HISTORY_DATE);
    }

    @Test
    @Transactional
    public void getAllDocumentHistoriesByHistoryDateIsInShouldWork() throws Exception {
        // Initialize the database
        documentHistoryRepository.saveAndFlush(documentHistory);

        // Get all the documentHistoryList where historyDate in DEFAULT_HISTORY_DATE or UPDATED_HISTORY_DATE
        defaultDocumentHistoryShouldBeFound("historyDate.in=" + DEFAULT_HISTORY_DATE + "," + UPDATED_HISTORY_DATE);

        // Get all the documentHistoryList where historyDate equals to UPDATED_HISTORY_DATE
        defaultDocumentHistoryShouldNotBeFound("historyDate.in=" + UPDATED_HISTORY_DATE);
    }

    @Test
    @Transactional
    public void getAllDocumentHistoriesByHistoryDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        documentHistoryRepository.saveAndFlush(documentHistory);

        // Get all the documentHistoryList where historyDate is not null
        defaultDocumentHistoryShouldBeFound("historyDate.specified=true");

        // Get all the documentHistoryList where historyDate is null
        defaultDocumentHistoryShouldNotBeFound("historyDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllDocumentHistoriesByHistoryDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        documentHistoryRepository.saveAndFlush(documentHistory);

        // Get all the documentHistoryList where historyDate is greater than or equal to DEFAULT_HISTORY_DATE
        defaultDocumentHistoryShouldBeFound("historyDate.greaterThanOrEqual=" + DEFAULT_HISTORY_DATE);

        // Get all the documentHistoryList where historyDate is greater than or equal to UPDATED_HISTORY_DATE
        defaultDocumentHistoryShouldNotBeFound("historyDate.greaterThanOrEqual=" + UPDATED_HISTORY_DATE);
    }

    @Test
    @Transactional
    public void getAllDocumentHistoriesByHistoryDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        documentHistoryRepository.saveAndFlush(documentHistory);

        // Get all the documentHistoryList where historyDate is less than or equal to DEFAULT_HISTORY_DATE
        defaultDocumentHistoryShouldBeFound("historyDate.lessThanOrEqual=" + DEFAULT_HISTORY_DATE);

        // Get all the documentHistoryList where historyDate is less than or equal to SMALLER_HISTORY_DATE
        defaultDocumentHistoryShouldNotBeFound("historyDate.lessThanOrEqual=" + SMALLER_HISTORY_DATE);
    }

    @Test
    @Transactional
    public void getAllDocumentHistoriesByHistoryDateIsLessThanSomething() throws Exception {
        // Initialize the database
        documentHistoryRepository.saveAndFlush(documentHistory);

        // Get all the documentHistoryList where historyDate is less than DEFAULT_HISTORY_DATE
        defaultDocumentHistoryShouldNotBeFound("historyDate.lessThan=" + DEFAULT_HISTORY_DATE);

        // Get all the documentHistoryList where historyDate is less than UPDATED_HISTORY_DATE
        defaultDocumentHistoryShouldBeFound("historyDate.lessThan=" + UPDATED_HISTORY_DATE);
    }

    @Test
    @Transactional
    public void getAllDocumentHistoriesByHistoryDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        documentHistoryRepository.saveAndFlush(documentHistory);

        // Get all the documentHistoryList where historyDate is greater than DEFAULT_HISTORY_DATE
        defaultDocumentHistoryShouldNotBeFound("historyDate.greaterThan=" + DEFAULT_HISTORY_DATE);

        // Get all the documentHistoryList where historyDate is greater than SMALLER_HISTORY_DATE
        defaultDocumentHistoryShouldBeFound("historyDate.greaterThan=" + SMALLER_HISTORY_DATE);
    }


    @Test
    @Transactional
    public void getAllDocumentHistoriesByTypeIsEqualToSomething() throws Exception {
        // Get already existing entity
        DocumentType type = documentHistory.getType();
        documentHistoryRepository.saveAndFlush(documentHistory);
        Long typeId = type.getId();

        // Get all the documentHistoryList where type equals to typeId
        defaultDocumentHistoryShouldBeFound("typeId.equals=" + typeId);

        // Get all the documentHistoryList where type equals to typeId + 1
        defaultDocumentHistoryShouldNotBeFound("typeId.equals=" + (typeId + 1));
    }


    @Test
    @Transactional
    public void getAllDocumentHistoriesByLastModifiedUserIsEqualToSomething() throws Exception {
        // Get already existing entity
        ExUser lastModifiedUser = documentHistory.getLastModifiedUser();
        documentHistoryRepository.saveAndFlush(documentHistory);
        Long lastModifiedUserId = lastModifiedUser.getId();

        // Get all the documentHistoryList where lastModifiedUser equals to lastModifiedUserId
        defaultDocumentHistoryShouldBeFound("lastModifiedUserId.equals=" + lastModifiedUserId);

        // Get all the documentHistoryList where lastModifiedUser equals to lastModifiedUserId + 1
        defaultDocumentHistoryShouldNotBeFound("lastModifiedUserId.equals=" + (lastModifiedUserId + 1));
    }


    @Test
    @Transactional
    public void getAllDocumentHistoriesByCreatedUserIsEqualToSomething() throws Exception {
        // Get already existing entity
        ExUser createdUser = documentHistory.getCreatedUser();
        documentHistoryRepository.saveAndFlush(documentHistory);
        Long createdUserId = createdUser.getId();

        // Get all the documentHistoryList where createdUser equals to createdUserId
        defaultDocumentHistoryShouldBeFound("createdUserId.equals=" + createdUserId);

        // Get all the documentHistoryList where createdUser equals to createdUserId + 1
        defaultDocumentHistoryShouldNotBeFound("createdUserId.equals=" + (createdUserId + 1));
    }


    @Test
    @Transactional
    public void getAllDocumentHistoriesByChangeLogIsEqualToSomething() throws Exception {
        // Initialize the database
        documentHistoryRepository.saveAndFlush(documentHistory);
        ChangeLog changeLog = ChangeLogResourceIT.createEntity(em);
        em.persist(changeLog);
        em.flush();
        documentHistory.addChangeLog(changeLog);
        documentHistoryRepository.saveAndFlush(documentHistory);
        Long changeLogId = changeLog.getId();

        // Get all the documentHistoryList where changeLog equals to changeLogId
        defaultDocumentHistoryShouldBeFound("changeLogId.equals=" + changeLogId);

        // Get all the documentHistoryList where changeLog equals to changeLogId + 1
        defaultDocumentHistoryShouldNotBeFound("changeLogId.equals=" + (changeLogId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDocumentHistoryShouldBeFound(String filter) throws Exception {
        restDocumentHistoryMockMvc.perform(get("/api/document-histories?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].historyDescription").value(hasItem(DEFAULT_HISTORY_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].historyDate").value(hasItem(sameInstant(DEFAULT_HISTORY_DATE))));

        // Check, that the count call also returns 1
        restDocumentHistoryMockMvc.perform(get("/api/document-histories/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDocumentHistoryShouldNotBeFound(String filter) throws Exception {
        restDocumentHistoryMockMvc.perform(get("/api/document-histories?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDocumentHistoryMockMvc.perform(get("/api/document-histories/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingDocumentHistory() throws Exception {
        // Get the documentHistory
        restDocumentHistoryMockMvc.perform(get("/api/document-histories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDocumentHistory() throws Exception {
        // Initialize the database
        documentHistoryService.save(documentHistory);

        int databaseSizeBeforeUpdate = documentHistoryRepository.findAll().size();

        // Update the documentHistory
        DocumentHistory updatedDocumentHistory = documentHistoryRepository.findById(documentHistory.getId()).get();
        // Disconnect from session so that the updates on updatedDocumentHistory are not directly saved in db
        em.detach(updatedDocumentHistory);
        updatedDocumentHistory
            .historyDescription(UPDATED_HISTORY_DESCRIPTION)
            .historyDate(UPDATED_HISTORY_DATE);

        restDocumentHistoryMockMvc.perform(put("/api/document-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDocumentHistory)))
            .andExpect(status().isOk());

        // Validate the DocumentHistory in the database
        List<DocumentHistory> documentHistoryList = documentHistoryRepository.findAll();
        assertThat(documentHistoryList).hasSize(databaseSizeBeforeUpdate);
        DocumentHistory testDocumentHistory = documentHistoryList.get(documentHistoryList.size() - 1);
        assertThat(testDocumentHistory.getHistoryDescription()).isEqualTo(UPDATED_HISTORY_DESCRIPTION);
        assertThat(testDocumentHistory.getHistoryDate()).isEqualTo(UPDATED_HISTORY_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingDocumentHistory() throws Exception {
        int databaseSizeBeforeUpdate = documentHistoryRepository.findAll().size();

        // Create the DocumentHistory

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentHistoryMockMvc.perform(put("/api/document-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documentHistory)))
            .andExpect(status().isBadRequest());

        // Validate the DocumentHistory in the database
        List<DocumentHistory> documentHistoryList = documentHistoryRepository.findAll();
        assertThat(documentHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDocumentHistory() throws Exception {
        // Initialize the database
        documentHistoryService.save(documentHistory);

        int databaseSizeBeforeDelete = documentHistoryRepository.findAll().size();

        // Delete the documentHistory
        restDocumentHistoryMockMvc.perform(delete("/api/document-histories/{id}", documentHistory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DocumentHistory> documentHistoryList = documentHistoryRepository.findAll();
        assertThat(documentHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

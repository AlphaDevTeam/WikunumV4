package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.WikunumApp;
import com.alphadevs.sales.domain.DocumentNumberConfig;
import com.alphadevs.sales.domain.DocumentType;
import com.alphadevs.sales.domain.Location;
import com.alphadevs.sales.domain.TransactionType;
import com.alphadevs.sales.repository.DocumentNumberConfigRepository;
import com.alphadevs.sales.service.DocumentNumberConfigService;
import com.alphadevs.sales.web.rest.errors.ExceptionTranslator;
import com.alphadevs.sales.service.dto.DocumentNumberConfigCriteria;
import com.alphadevs.sales.service.DocumentNumberConfigQueryService;

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
 * Integration tests for the {@link DocumentNumberConfigResource} REST controller.
 */
@SpringBootTest(classes = WikunumApp.class)
public class DocumentNumberConfigResourceIT {

    private static final String DEFAULT_DOCUMENT_PREFIX = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_PREFIX = "BBBBBBBBBB";

    private static final String DEFAULT_DOCUMENT_POSTFIX = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_POSTFIX = "BBBBBBBBBB";

    private static final Double DEFAULT_CURRENT_NUMBER = 1D;
    private static final Double UPDATED_CURRENT_NUMBER = 2D;
    private static final Double SMALLER_CURRENT_NUMBER = 1D - 1D;

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    @Autowired
    private DocumentNumberConfigRepository documentNumberConfigRepository;

    @Autowired
    private DocumentNumberConfigService documentNumberConfigService;

    @Autowired
    private DocumentNumberConfigQueryService documentNumberConfigQueryService;

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

    private MockMvc restDocumentNumberConfigMockMvc;

    private DocumentNumberConfig documentNumberConfig;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DocumentNumberConfigResource documentNumberConfigResource = new DocumentNumberConfigResource(documentNumberConfigService, documentNumberConfigQueryService);
        this.restDocumentNumberConfigMockMvc = MockMvcBuilders.standaloneSetup(documentNumberConfigResource)
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
    public static DocumentNumberConfig createEntity(EntityManager em) {
        DocumentNumberConfig documentNumberConfig = new DocumentNumberConfig()
            .documentPrefix(DEFAULT_DOCUMENT_PREFIX)
            .documentPostfix(DEFAULT_DOCUMENT_POSTFIX)
            .currentNumber(DEFAULT_CURRENT_NUMBER)
            .isActive(DEFAULT_IS_ACTIVE);
        // Add required entity
        DocumentType documentType;
        if (TestUtil.findAll(em, DocumentType.class).isEmpty()) {
            documentType = DocumentTypeResourceIT.createEntity(em);
            em.persist(documentType);
            em.flush();
        } else {
            documentType = TestUtil.findAll(em, DocumentType.class).get(0);
        }
        documentNumberConfig.setDocument(documentType);
        // Add required entity
        Location location;
        if (TestUtil.findAll(em, Location.class).isEmpty()) {
            location = LocationResourceIT.createEntity(em);
            em.persist(location);
            em.flush();
        } else {
            location = TestUtil.findAll(em, Location.class).get(0);
        }
        documentNumberConfig.setLocation(location);
        // Add required entity
        TransactionType transactionType;
        if (TestUtil.findAll(em, TransactionType.class).isEmpty()) {
            transactionType = TransactionTypeResourceIT.createEntity(em);
            em.persist(transactionType);
            em.flush();
        } else {
            transactionType = TestUtil.findAll(em, TransactionType.class).get(0);
        }
        documentNumberConfig.setTransactionType(transactionType);
        return documentNumberConfig;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentNumberConfig createUpdatedEntity(EntityManager em) {
        DocumentNumberConfig documentNumberConfig = new DocumentNumberConfig()
            .documentPrefix(UPDATED_DOCUMENT_PREFIX)
            .documentPostfix(UPDATED_DOCUMENT_POSTFIX)
            .currentNumber(UPDATED_CURRENT_NUMBER)
            .isActive(UPDATED_IS_ACTIVE);
        // Add required entity
        DocumentType documentType;
        if (TestUtil.findAll(em, DocumentType.class).isEmpty()) {
            documentType = DocumentTypeResourceIT.createUpdatedEntity(em);
            em.persist(documentType);
            em.flush();
        } else {
            documentType = TestUtil.findAll(em, DocumentType.class).get(0);
        }
        documentNumberConfig.setDocument(documentType);
        // Add required entity
        Location location;
        if (TestUtil.findAll(em, Location.class).isEmpty()) {
            location = LocationResourceIT.createUpdatedEntity(em);
            em.persist(location);
            em.flush();
        } else {
            location = TestUtil.findAll(em, Location.class).get(0);
        }
        documentNumberConfig.setLocation(location);
        // Add required entity
        TransactionType transactionType;
        if (TestUtil.findAll(em, TransactionType.class).isEmpty()) {
            transactionType = TransactionTypeResourceIT.createUpdatedEntity(em);
            em.persist(transactionType);
            em.flush();
        } else {
            transactionType = TestUtil.findAll(em, TransactionType.class).get(0);
        }
        documentNumberConfig.setTransactionType(transactionType);
        return documentNumberConfig;
    }

    @BeforeEach
    public void initTest() {
        documentNumberConfig = createEntity(em);
    }

    @Test
    @Transactional
    public void createDocumentNumberConfig() throws Exception {
        int databaseSizeBeforeCreate = documentNumberConfigRepository.findAll().size();

        // Create the DocumentNumberConfig
        restDocumentNumberConfigMockMvc.perform(post("/api/document-number-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documentNumberConfig)))
            .andExpect(status().isCreated());

        // Validate the DocumentNumberConfig in the database
        List<DocumentNumberConfig> documentNumberConfigList = documentNumberConfigRepository.findAll();
        assertThat(documentNumberConfigList).hasSize(databaseSizeBeforeCreate + 1);
        DocumentNumberConfig testDocumentNumberConfig = documentNumberConfigList.get(documentNumberConfigList.size() - 1);
        assertThat(testDocumentNumberConfig.getDocumentPrefix()).isEqualTo(DEFAULT_DOCUMENT_PREFIX);
        assertThat(testDocumentNumberConfig.getDocumentPostfix()).isEqualTo(DEFAULT_DOCUMENT_POSTFIX);
        assertThat(testDocumentNumberConfig.getCurrentNumber()).isEqualTo(DEFAULT_CURRENT_NUMBER);
        assertThat(testDocumentNumberConfig.isIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void createDocumentNumberConfigWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = documentNumberConfigRepository.findAll().size();

        // Create the DocumentNumberConfig with an existing ID
        documentNumberConfig.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentNumberConfigMockMvc.perform(post("/api/document-number-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documentNumberConfig)))
            .andExpect(status().isBadRequest());

        // Validate the DocumentNumberConfig in the database
        List<DocumentNumberConfig> documentNumberConfigList = documentNumberConfigRepository.findAll();
        assertThat(documentNumberConfigList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkDocumentPrefixIsRequired() throws Exception {
        int databaseSizeBeforeTest = documentNumberConfigRepository.findAll().size();
        // set the field null
        documentNumberConfig.setDocumentPrefix(null);

        // Create the DocumentNumberConfig, which fails.

        restDocumentNumberConfigMockMvc.perform(post("/api/document-number-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documentNumberConfig)))
            .andExpect(status().isBadRequest());

        List<DocumentNumberConfig> documentNumberConfigList = documentNumberConfigRepository.findAll();
        assertThat(documentNumberConfigList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCurrentNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = documentNumberConfigRepository.findAll().size();
        // set the field null
        documentNumberConfig.setCurrentNumber(null);

        // Create the DocumentNumberConfig, which fails.

        restDocumentNumberConfigMockMvc.perform(post("/api/document-number-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documentNumberConfig)))
            .andExpect(status().isBadRequest());

        List<DocumentNumberConfig> documentNumberConfigList = documentNumberConfigRepository.findAll();
        assertThat(documentNumberConfigList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDocumentNumberConfigs() throws Exception {
        // Initialize the database
        documentNumberConfigRepository.saveAndFlush(documentNumberConfig);

        // Get all the documentNumberConfigList
        restDocumentNumberConfigMockMvc.perform(get("/api/document-number-configs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentNumberConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentPrefix").value(hasItem(DEFAULT_DOCUMENT_PREFIX)))
            .andExpect(jsonPath("$.[*].documentPostfix").value(hasItem(DEFAULT_DOCUMENT_POSTFIX)))
            .andExpect(jsonPath("$.[*].currentNumber").value(hasItem(DEFAULT_CURRENT_NUMBER.doubleValue())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getDocumentNumberConfig() throws Exception {
        // Initialize the database
        documentNumberConfigRepository.saveAndFlush(documentNumberConfig);

        // Get the documentNumberConfig
        restDocumentNumberConfigMockMvc.perform(get("/api/document-number-configs/{id}", documentNumberConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(documentNumberConfig.getId().intValue()))
            .andExpect(jsonPath("$.documentPrefix").value(DEFAULT_DOCUMENT_PREFIX))
            .andExpect(jsonPath("$.documentPostfix").value(DEFAULT_DOCUMENT_POSTFIX))
            .andExpect(jsonPath("$.currentNumber").value(DEFAULT_CURRENT_NUMBER.doubleValue()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }


    @Test
    @Transactional
    public void getDocumentNumberConfigsByIdFiltering() throws Exception {
        // Initialize the database
        documentNumberConfigRepository.saveAndFlush(documentNumberConfig);

        Long id = documentNumberConfig.getId();

        defaultDocumentNumberConfigShouldBeFound("id.equals=" + id);
        defaultDocumentNumberConfigShouldNotBeFound("id.notEquals=" + id);

        defaultDocumentNumberConfigShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDocumentNumberConfigShouldNotBeFound("id.greaterThan=" + id);

        defaultDocumentNumberConfigShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDocumentNumberConfigShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllDocumentNumberConfigsByDocumentPrefixIsEqualToSomething() throws Exception {
        // Initialize the database
        documentNumberConfigRepository.saveAndFlush(documentNumberConfig);

        // Get all the documentNumberConfigList where documentPrefix equals to DEFAULT_DOCUMENT_PREFIX
        defaultDocumentNumberConfigShouldBeFound("documentPrefix.equals=" + DEFAULT_DOCUMENT_PREFIX);

        // Get all the documentNumberConfigList where documentPrefix equals to UPDATED_DOCUMENT_PREFIX
        defaultDocumentNumberConfigShouldNotBeFound("documentPrefix.equals=" + UPDATED_DOCUMENT_PREFIX);
    }

    @Test
    @Transactional
    public void getAllDocumentNumberConfigsByDocumentPrefixIsNotEqualToSomething() throws Exception {
        // Initialize the database
        documentNumberConfigRepository.saveAndFlush(documentNumberConfig);

        // Get all the documentNumberConfigList where documentPrefix not equals to DEFAULT_DOCUMENT_PREFIX
        defaultDocumentNumberConfigShouldNotBeFound("documentPrefix.notEquals=" + DEFAULT_DOCUMENT_PREFIX);

        // Get all the documentNumberConfigList where documentPrefix not equals to UPDATED_DOCUMENT_PREFIX
        defaultDocumentNumberConfigShouldBeFound("documentPrefix.notEquals=" + UPDATED_DOCUMENT_PREFIX);
    }

    @Test
    @Transactional
    public void getAllDocumentNumberConfigsByDocumentPrefixIsInShouldWork() throws Exception {
        // Initialize the database
        documentNumberConfigRepository.saveAndFlush(documentNumberConfig);

        // Get all the documentNumberConfigList where documentPrefix in DEFAULT_DOCUMENT_PREFIX or UPDATED_DOCUMENT_PREFIX
        defaultDocumentNumberConfigShouldBeFound("documentPrefix.in=" + DEFAULT_DOCUMENT_PREFIX + "," + UPDATED_DOCUMENT_PREFIX);

        // Get all the documentNumberConfigList where documentPrefix equals to UPDATED_DOCUMENT_PREFIX
        defaultDocumentNumberConfigShouldNotBeFound("documentPrefix.in=" + UPDATED_DOCUMENT_PREFIX);
    }

    @Test
    @Transactional
    public void getAllDocumentNumberConfigsByDocumentPrefixIsNullOrNotNull() throws Exception {
        // Initialize the database
        documentNumberConfigRepository.saveAndFlush(documentNumberConfig);

        // Get all the documentNumberConfigList where documentPrefix is not null
        defaultDocumentNumberConfigShouldBeFound("documentPrefix.specified=true");

        // Get all the documentNumberConfigList where documentPrefix is null
        defaultDocumentNumberConfigShouldNotBeFound("documentPrefix.specified=false");
    }
                @Test
    @Transactional
    public void getAllDocumentNumberConfigsByDocumentPrefixContainsSomething() throws Exception {
        // Initialize the database
        documentNumberConfigRepository.saveAndFlush(documentNumberConfig);

        // Get all the documentNumberConfigList where documentPrefix contains DEFAULT_DOCUMENT_PREFIX
        defaultDocumentNumberConfigShouldBeFound("documentPrefix.contains=" + DEFAULT_DOCUMENT_PREFIX);

        // Get all the documentNumberConfigList where documentPrefix contains UPDATED_DOCUMENT_PREFIX
        defaultDocumentNumberConfigShouldNotBeFound("documentPrefix.contains=" + UPDATED_DOCUMENT_PREFIX);
    }

    @Test
    @Transactional
    public void getAllDocumentNumberConfigsByDocumentPrefixNotContainsSomething() throws Exception {
        // Initialize the database
        documentNumberConfigRepository.saveAndFlush(documentNumberConfig);

        // Get all the documentNumberConfigList where documentPrefix does not contain DEFAULT_DOCUMENT_PREFIX
        defaultDocumentNumberConfigShouldNotBeFound("documentPrefix.doesNotContain=" + DEFAULT_DOCUMENT_PREFIX);

        // Get all the documentNumberConfigList where documentPrefix does not contain UPDATED_DOCUMENT_PREFIX
        defaultDocumentNumberConfigShouldBeFound("documentPrefix.doesNotContain=" + UPDATED_DOCUMENT_PREFIX);
    }


    @Test
    @Transactional
    public void getAllDocumentNumberConfigsByDocumentPostfixIsEqualToSomething() throws Exception {
        // Initialize the database
        documentNumberConfigRepository.saveAndFlush(documentNumberConfig);

        // Get all the documentNumberConfigList where documentPostfix equals to DEFAULT_DOCUMENT_POSTFIX
        defaultDocumentNumberConfigShouldBeFound("documentPostfix.equals=" + DEFAULT_DOCUMENT_POSTFIX);

        // Get all the documentNumberConfigList where documentPostfix equals to UPDATED_DOCUMENT_POSTFIX
        defaultDocumentNumberConfigShouldNotBeFound("documentPostfix.equals=" + UPDATED_DOCUMENT_POSTFIX);
    }

    @Test
    @Transactional
    public void getAllDocumentNumberConfigsByDocumentPostfixIsNotEqualToSomething() throws Exception {
        // Initialize the database
        documentNumberConfigRepository.saveAndFlush(documentNumberConfig);

        // Get all the documentNumberConfigList where documentPostfix not equals to DEFAULT_DOCUMENT_POSTFIX
        defaultDocumentNumberConfigShouldNotBeFound("documentPostfix.notEquals=" + DEFAULT_DOCUMENT_POSTFIX);

        // Get all the documentNumberConfigList where documentPostfix not equals to UPDATED_DOCUMENT_POSTFIX
        defaultDocumentNumberConfigShouldBeFound("documentPostfix.notEquals=" + UPDATED_DOCUMENT_POSTFIX);
    }

    @Test
    @Transactional
    public void getAllDocumentNumberConfigsByDocumentPostfixIsInShouldWork() throws Exception {
        // Initialize the database
        documentNumberConfigRepository.saveAndFlush(documentNumberConfig);

        // Get all the documentNumberConfigList where documentPostfix in DEFAULT_DOCUMENT_POSTFIX or UPDATED_DOCUMENT_POSTFIX
        defaultDocumentNumberConfigShouldBeFound("documentPostfix.in=" + DEFAULT_DOCUMENT_POSTFIX + "," + UPDATED_DOCUMENT_POSTFIX);

        // Get all the documentNumberConfigList where documentPostfix equals to UPDATED_DOCUMENT_POSTFIX
        defaultDocumentNumberConfigShouldNotBeFound("documentPostfix.in=" + UPDATED_DOCUMENT_POSTFIX);
    }

    @Test
    @Transactional
    public void getAllDocumentNumberConfigsByDocumentPostfixIsNullOrNotNull() throws Exception {
        // Initialize the database
        documentNumberConfigRepository.saveAndFlush(documentNumberConfig);

        // Get all the documentNumberConfigList where documentPostfix is not null
        defaultDocumentNumberConfigShouldBeFound("documentPostfix.specified=true");

        // Get all the documentNumberConfigList where documentPostfix is null
        defaultDocumentNumberConfigShouldNotBeFound("documentPostfix.specified=false");
    }
                @Test
    @Transactional
    public void getAllDocumentNumberConfigsByDocumentPostfixContainsSomething() throws Exception {
        // Initialize the database
        documentNumberConfigRepository.saveAndFlush(documentNumberConfig);

        // Get all the documentNumberConfigList where documentPostfix contains DEFAULT_DOCUMENT_POSTFIX
        defaultDocumentNumberConfigShouldBeFound("documentPostfix.contains=" + DEFAULT_DOCUMENT_POSTFIX);

        // Get all the documentNumberConfigList where documentPostfix contains UPDATED_DOCUMENT_POSTFIX
        defaultDocumentNumberConfigShouldNotBeFound("documentPostfix.contains=" + UPDATED_DOCUMENT_POSTFIX);
    }

    @Test
    @Transactional
    public void getAllDocumentNumberConfigsByDocumentPostfixNotContainsSomething() throws Exception {
        // Initialize the database
        documentNumberConfigRepository.saveAndFlush(documentNumberConfig);

        // Get all the documentNumberConfigList where documentPostfix does not contain DEFAULT_DOCUMENT_POSTFIX
        defaultDocumentNumberConfigShouldNotBeFound("documentPostfix.doesNotContain=" + DEFAULT_DOCUMENT_POSTFIX);

        // Get all the documentNumberConfigList where documentPostfix does not contain UPDATED_DOCUMENT_POSTFIX
        defaultDocumentNumberConfigShouldBeFound("documentPostfix.doesNotContain=" + UPDATED_DOCUMENT_POSTFIX);
    }


    @Test
    @Transactional
    public void getAllDocumentNumberConfigsByCurrentNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        documentNumberConfigRepository.saveAndFlush(documentNumberConfig);

        // Get all the documentNumberConfigList where currentNumber equals to DEFAULT_CURRENT_NUMBER
        defaultDocumentNumberConfigShouldBeFound("currentNumber.equals=" + DEFAULT_CURRENT_NUMBER);

        // Get all the documentNumberConfigList where currentNumber equals to UPDATED_CURRENT_NUMBER
        defaultDocumentNumberConfigShouldNotBeFound("currentNumber.equals=" + UPDATED_CURRENT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllDocumentNumberConfigsByCurrentNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        documentNumberConfigRepository.saveAndFlush(documentNumberConfig);

        // Get all the documentNumberConfigList where currentNumber not equals to DEFAULT_CURRENT_NUMBER
        defaultDocumentNumberConfigShouldNotBeFound("currentNumber.notEquals=" + DEFAULT_CURRENT_NUMBER);

        // Get all the documentNumberConfigList where currentNumber not equals to UPDATED_CURRENT_NUMBER
        defaultDocumentNumberConfigShouldBeFound("currentNumber.notEquals=" + UPDATED_CURRENT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllDocumentNumberConfigsByCurrentNumberIsInShouldWork() throws Exception {
        // Initialize the database
        documentNumberConfigRepository.saveAndFlush(documentNumberConfig);

        // Get all the documentNumberConfigList where currentNumber in DEFAULT_CURRENT_NUMBER or UPDATED_CURRENT_NUMBER
        defaultDocumentNumberConfigShouldBeFound("currentNumber.in=" + DEFAULT_CURRENT_NUMBER + "," + UPDATED_CURRENT_NUMBER);

        // Get all the documentNumberConfigList where currentNumber equals to UPDATED_CURRENT_NUMBER
        defaultDocumentNumberConfigShouldNotBeFound("currentNumber.in=" + UPDATED_CURRENT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllDocumentNumberConfigsByCurrentNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        documentNumberConfigRepository.saveAndFlush(documentNumberConfig);

        // Get all the documentNumberConfigList where currentNumber is not null
        defaultDocumentNumberConfigShouldBeFound("currentNumber.specified=true");

        // Get all the documentNumberConfigList where currentNumber is null
        defaultDocumentNumberConfigShouldNotBeFound("currentNumber.specified=false");
    }

    @Test
    @Transactional
    public void getAllDocumentNumberConfigsByCurrentNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        documentNumberConfigRepository.saveAndFlush(documentNumberConfig);

        // Get all the documentNumberConfigList where currentNumber is greater than or equal to DEFAULT_CURRENT_NUMBER
        defaultDocumentNumberConfigShouldBeFound("currentNumber.greaterThanOrEqual=" + DEFAULT_CURRENT_NUMBER);

        // Get all the documentNumberConfigList where currentNumber is greater than or equal to UPDATED_CURRENT_NUMBER
        defaultDocumentNumberConfigShouldNotBeFound("currentNumber.greaterThanOrEqual=" + UPDATED_CURRENT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllDocumentNumberConfigsByCurrentNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        documentNumberConfigRepository.saveAndFlush(documentNumberConfig);

        // Get all the documentNumberConfigList where currentNumber is less than or equal to DEFAULT_CURRENT_NUMBER
        defaultDocumentNumberConfigShouldBeFound("currentNumber.lessThanOrEqual=" + DEFAULT_CURRENT_NUMBER);

        // Get all the documentNumberConfigList where currentNumber is less than or equal to SMALLER_CURRENT_NUMBER
        defaultDocumentNumberConfigShouldNotBeFound("currentNumber.lessThanOrEqual=" + SMALLER_CURRENT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllDocumentNumberConfigsByCurrentNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        documentNumberConfigRepository.saveAndFlush(documentNumberConfig);

        // Get all the documentNumberConfigList where currentNumber is less than DEFAULT_CURRENT_NUMBER
        defaultDocumentNumberConfigShouldNotBeFound("currentNumber.lessThan=" + DEFAULT_CURRENT_NUMBER);

        // Get all the documentNumberConfigList where currentNumber is less than UPDATED_CURRENT_NUMBER
        defaultDocumentNumberConfigShouldBeFound("currentNumber.lessThan=" + UPDATED_CURRENT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllDocumentNumberConfigsByCurrentNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        documentNumberConfigRepository.saveAndFlush(documentNumberConfig);

        // Get all the documentNumberConfigList where currentNumber is greater than DEFAULT_CURRENT_NUMBER
        defaultDocumentNumberConfigShouldNotBeFound("currentNumber.greaterThan=" + DEFAULT_CURRENT_NUMBER);

        // Get all the documentNumberConfigList where currentNumber is greater than SMALLER_CURRENT_NUMBER
        defaultDocumentNumberConfigShouldBeFound("currentNumber.greaterThan=" + SMALLER_CURRENT_NUMBER);
    }


    @Test
    @Transactional
    public void getAllDocumentNumberConfigsByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        documentNumberConfigRepository.saveAndFlush(documentNumberConfig);

        // Get all the documentNumberConfigList where isActive equals to DEFAULT_IS_ACTIVE
        defaultDocumentNumberConfigShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the documentNumberConfigList where isActive equals to UPDATED_IS_ACTIVE
        defaultDocumentNumberConfigShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllDocumentNumberConfigsByIsActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        documentNumberConfigRepository.saveAndFlush(documentNumberConfig);

        // Get all the documentNumberConfigList where isActive not equals to DEFAULT_IS_ACTIVE
        defaultDocumentNumberConfigShouldNotBeFound("isActive.notEquals=" + DEFAULT_IS_ACTIVE);

        // Get all the documentNumberConfigList where isActive not equals to UPDATED_IS_ACTIVE
        defaultDocumentNumberConfigShouldBeFound("isActive.notEquals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllDocumentNumberConfigsByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        documentNumberConfigRepository.saveAndFlush(documentNumberConfig);

        // Get all the documentNumberConfigList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultDocumentNumberConfigShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the documentNumberConfigList where isActive equals to UPDATED_IS_ACTIVE
        defaultDocumentNumberConfigShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllDocumentNumberConfigsByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        documentNumberConfigRepository.saveAndFlush(documentNumberConfig);

        // Get all the documentNumberConfigList where isActive is not null
        defaultDocumentNumberConfigShouldBeFound("isActive.specified=true");

        // Get all the documentNumberConfigList where isActive is null
        defaultDocumentNumberConfigShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    public void getAllDocumentNumberConfigsByDocumentIsEqualToSomething() throws Exception {
        // Get already existing entity
        DocumentType document = documentNumberConfig.getDocument();
        documentNumberConfigRepository.saveAndFlush(documentNumberConfig);
        Long documentId = document.getId();

        // Get all the documentNumberConfigList where document equals to documentId
        defaultDocumentNumberConfigShouldBeFound("documentId.equals=" + documentId);

        // Get all the documentNumberConfigList where document equals to documentId + 1
        defaultDocumentNumberConfigShouldNotBeFound("documentId.equals=" + (documentId + 1));
    }


    @Test
    @Transactional
    public void getAllDocumentNumberConfigsByLocationIsEqualToSomething() throws Exception {
        // Get already existing entity
        Location location = documentNumberConfig.getLocation();
        documentNumberConfigRepository.saveAndFlush(documentNumberConfig);
        Long locationId = location.getId();

        // Get all the documentNumberConfigList where location equals to locationId
        defaultDocumentNumberConfigShouldBeFound("locationId.equals=" + locationId);

        // Get all the documentNumberConfigList where location equals to locationId + 1
        defaultDocumentNumberConfigShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }


    @Test
    @Transactional
    public void getAllDocumentNumberConfigsByTransactionTypeIsEqualToSomething() throws Exception {
        // Get already existing entity
        TransactionType transactionType = documentNumberConfig.getTransactionType();
        documentNumberConfigRepository.saveAndFlush(documentNumberConfig);
        Long transactionTypeId = transactionType.getId();

        // Get all the documentNumberConfigList where transactionType equals to transactionTypeId
        defaultDocumentNumberConfigShouldBeFound("transactionTypeId.equals=" + transactionTypeId);

        // Get all the documentNumberConfigList where transactionType equals to transactionTypeId + 1
        defaultDocumentNumberConfigShouldNotBeFound("transactionTypeId.equals=" + (transactionTypeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDocumentNumberConfigShouldBeFound(String filter) throws Exception {
        restDocumentNumberConfigMockMvc.perform(get("/api/document-number-configs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentNumberConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentPrefix").value(hasItem(DEFAULT_DOCUMENT_PREFIX)))
            .andExpect(jsonPath("$.[*].documentPostfix").value(hasItem(DEFAULT_DOCUMENT_POSTFIX)))
            .andExpect(jsonPath("$.[*].currentNumber").value(hasItem(DEFAULT_CURRENT_NUMBER.doubleValue())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restDocumentNumberConfigMockMvc.perform(get("/api/document-number-configs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDocumentNumberConfigShouldNotBeFound(String filter) throws Exception {
        restDocumentNumberConfigMockMvc.perform(get("/api/document-number-configs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDocumentNumberConfigMockMvc.perform(get("/api/document-number-configs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingDocumentNumberConfig() throws Exception {
        // Get the documentNumberConfig
        restDocumentNumberConfigMockMvc.perform(get("/api/document-number-configs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDocumentNumberConfig() throws Exception {
        // Initialize the database
        documentNumberConfigService.save(documentNumberConfig);

        int databaseSizeBeforeUpdate = documentNumberConfigRepository.findAll().size();

        // Update the documentNumberConfig
        DocumentNumberConfig updatedDocumentNumberConfig = documentNumberConfigRepository.findById(documentNumberConfig.getId()).get();
        // Disconnect from session so that the updates on updatedDocumentNumberConfig are not directly saved in db
        em.detach(updatedDocumentNumberConfig);
        updatedDocumentNumberConfig
            .documentPrefix(UPDATED_DOCUMENT_PREFIX)
            .documentPostfix(UPDATED_DOCUMENT_POSTFIX)
            .currentNumber(UPDATED_CURRENT_NUMBER)
            .isActive(UPDATED_IS_ACTIVE);

        restDocumentNumberConfigMockMvc.perform(put("/api/document-number-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDocumentNumberConfig)))
            .andExpect(status().isOk());

        // Validate the DocumentNumberConfig in the database
        List<DocumentNumberConfig> documentNumberConfigList = documentNumberConfigRepository.findAll();
        assertThat(documentNumberConfigList).hasSize(databaseSizeBeforeUpdate);
        DocumentNumberConfig testDocumentNumberConfig = documentNumberConfigList.get(documentNumberConfigList.size() - 1);
        assertThat(testDocumentNumberConfig.getDocumentPrefix()).isEqualTo(UPDATED_DOCUMENT_PREFIX);
        assertThat(testDocumentNumberConfig.getDocumentPostfix()).isEqualTo(UPDATED_DOCUMENT_POSTFIX);
        assertThat(testDocumentNumberConfig.getCurrentNumber()).isEqualTo(UPDATED_CURRENT_NUMBER);
        assertThat(testDocumentNumberConfig.isIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void updateNonExistingDocumentNumberConfig() throws Exception {
        int databaseSizeBeforeUpdate = documentNumberConfigRepository.findAll().size();

        // Create the DocumentNumberConfig

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentNumberConfigMockMvc.perform(put("/api/document-number-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documentNumberConfig)))
            .andExpect(status().isBadRequest());

        // Validate the DocumentNumberConfig in the database
        List<DocumentNumberConfig> documentNumberConfigList = documentNumberConfigRepository.findAll();
        assertThat(documentNumberConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDocumentNumberConfig() throws Exception {
        // Initialize the database
        documentNumberConfigService.save(documentNumberConfig);

        int databaseSizeBeforeDelete = documentNumberConfigRepository.findAll().size();

        // Delete the documentNumberConfig
        restDocumentNumberConfigMockMvc.perform(delete("/api/document-number-configs/{id}", documentNumberConfig.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DocumentNumberConfig> documentNumberConfigList = documentNumberConfigRepository.findAll();
        assertThat(documentNumberConfigList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

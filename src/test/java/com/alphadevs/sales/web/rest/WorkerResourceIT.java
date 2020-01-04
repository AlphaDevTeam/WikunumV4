package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.WikunumApp;
import com.alphadevs.sales.domain.Worker;
import com.alphadevs.sales.domain.Location;
import com.alphadevs.sales.domain.Job;
import com.alphadevs.sales.repository.WorkerRepository;
import com.alphadevs.sales.service.WorkerService;
import com.alphadevs.sales.web.rest.errors.ExceptionTranslator;
import com.alphadevs.sales.service.dto.WorkerCriteria;
import com.alphadevs.sales.service.WorkerQueryService;

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
import java.math.BigDecimal;
import java.util.List;

import static com.alphadevs.sales.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link WorkerResource} REST controller.
 */
@SpringBootTest(classes = WikunumApp.class)
public class WorkerResourceIT {

    private static final String DEFAULT_WORKER_CODE = "AAAAAAAAAA";
    private static final String UPDATED_WORKER_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_WORKER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_WORKER_NAME = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_WORKER_LIMIT = new BigDecimal(1);
    private static final BigDecimal UPDATED_WORKER_LIMIT = new BigDecimal(2);
    private static final BigDecimal SMALLER_WORKER_LIMIT = new BigDecimal(1 - 1);

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Double DEFAULT_RATING = 1D;
    private static final Double UPDATED_RATING = 2D;
    private static final Double SMALLER_RATING = 1D - 1D;

    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private WorkerService workerService;

    @Autowired
    private WorkerQueryService workerQueryService;

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

    private MockMvc restWorkerMockMvc;

    private Worker worker;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WorkerResource workerResource = new WorkerResource(workerService, workerQueryService);
        this.restWorkerMockMvc = MockMvcBuilders.standaloneSetup(workerResource)
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
    public static Worker createEntity(EntityManager em) {
        Worker worker = new Worker()
            .workerCode(DEFAULT_WORKER_CODE)
            .workerName(DEFAULT_WORKER_NAME)
            .workerLimit(DEFAULT_WORKER_LIMIT)
            .isActive(DEFAULT_IS_ACTIVE)
            .rating(DEFAULT_RATING);
        // Add required entity
        Location location;
        if (TestUtil.findAll(em, Location.class).isEmpty()) {
            location = LocationResourceIT.createEntity(em);
            em.persist(location);
            em.flush();
        } else {
            location = TestUtil.findAll(em, Location.class).get(0);
        }
        worker.setLocation(location);
        // Add required entity
        Job job;
        if (TestUtil.findAll(em, Job.class).isEmpty()) {
            job = JobResourceIT.createEntity(em);
            em.persist(job);
            em.flush();
        } else {
            job = TestUtil.findAll(em, Job.class).get(0);
        }
        worker.getJobs().add(job);
        return worker;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Worker createUpdatedEntity(EntityManager em) {
        Worker worker = new Worker()
            .workerCode(UPDATED_WORKER_CODE)
            .workerName(UPDATED_WORKER_NAME)
            .workerLimit(UPDATED_WORKER_LIMIT)
            .isActive(UPDATED_IS_ACTIVE)
            .rating(UPDATED_RATING);
        // Add required entity
        Location location;
        if (TestUtil.findAll(em, Location.class).isEmpty()) {
            location = LocationResourceIT.createUpdatedEntity(em);
            em.persist(location);
            em.flush();
        } else {
            location = TestUtil.findAll(em, Location.class).get(0);
        }
        worker.setLocation(location);
        // Add required entity
        Job job;
        if (TestUtil.findAll(em, Job.class).isEmpty()) {
            job = JobResourceIT.createUpdatedEntity(em);
            em.persist(job);
            em.flush();
        } else {
            job = TestUtil.findAll(em, Job.class).get(0);
        }
        worker.getJobs().add(job);
        return worker;
    }

    @BeforeEach
    public void initTest() {
        worker = createEntity(em);
    }

    @Test
    @Transactional
    public void createWorker() throws Exception {
        int databaseSizeBeforeCreate = workerRepository.findAll().size();

        // Create the Worker
        restWorkerMockMvc.perform(post("/api/workers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(worker)))
            .andExpect(status().isCreated());

        // Validate the Worker in the database
        List<Worker> workerList = workerRepository.findAll();
        assertThat(workerList).hasSize(databaseSizeBeforeCreate + 1);
        Worker testWorker = workerList.get(workerList.size() - 1);
        assertThat(testWorker.getWorkerCode()).isEqualTo(DEFAULT_WORKER_CODE);
        assertThat(testWorker.getWorkerName()).isEqualTo(DEFAULT_WORKER_NAME);
        assertThat(testWorker.getWorkerLimit()).isEqualTo(DEFAULT_WORKER_LIMIT);
        assertThat(testWorker.isIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testWorker.getRating()).isEqualTo(DEFAULT_RATING);
    }

    @Test
    @Transactional
    public void createWorkerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = workerRepository.findAll().size();

        // Create the Worker with an existing ID
        worker.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkerMockMvc.perform(post("/api/workers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(worker)))
            .andExpect(status().isBadRequest());

        // Validate the Worker in the database
        List<Worker> workerList = workerRepository.findAll();
        assertThat(workerList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkWorkerCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = workerRepository.findAll().size();
        // set the field null
        worker.setWorkerCode(null);

        // Create the Worker, which fails.

        restWorkerMockMvc.perform(post("/api/workers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(worker)))
            .andExpect(status().isBadRequest());

        List<Worker> workerList = workerRepository.findAll();
        assertThat(workerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkWorkerNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = workerRepository.findAll().size();
        // set the field null
        worker.setWorkerName(null);

        // Create the Worker, which fails.

        restWorkerMockMvc.perform(post("/api/workers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(worker)))
            .andExpect(status().isBadRequest());

        List<Worker> workerList = workerRepository.findAll();
        assertThat(workerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWorkers() throws Exception {
        // Initialize the database
        workerRepository.saveAndFlush(worker);

        // Get all the workerList
        restWorkerMockMvc.perform(get("/api/workers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(worker.getId().intValue())))
            .andExpect(jsonPath("$.[*].workerCode").value(hasItem(DEFAULT_WORKER_CODE)))
            .andExpect(jsonPath("$.[*].workerName").value(hasItem(DEFAULT_WORKER_NAME)))
            .andExpect(jsonPath("$.[*].workerLimit").value(hasItem(DEFAULT_WORKER_LIMIT.intValue())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getWorker() throws Exception {
        // Initialize the database
        workerRepository.saveAndFlush(worker);

        // Get the worker
        restWorkerMockMvc.perform(get("/api/workers/{id}", worker.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(worker.getId().intValue()))
            .andExpect(jsonPath("$.workerCode").value(DEFAULT_WORKER_CODE))
            .andExpect(jsonPath("$.workerName").value(DEFAULT_WORKER_NAME))
            .andExpect(jsonPath("$.workerLimit").value(DEFAULT_WORKER_LIMIT.intValue()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING.doubleValue()));
    }


    @Test
    @Transactional
    public void getWorkersByIdFiltering() throws Exception {
        // Initialize the database
        workerRepository.saveAndFlush(worker);

        Long id = worker.getId();

        defaultWorkerShouldBeFound("id.equals=" + id);
        defaultWorkerShouldNotBeFound("id.notEquals=" + id);

        defaultWorkerShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultWorkerShouldNotBeFound("id.greaterThan=" + id);

        defaultWorkerShouldBeFound("id.lessThanOrEqual=" + id);
        defaultWorkerShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllWorkersByWorkerCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        workerRepository.saveAndFlush(worker);

        // Get all the workerList where workerCode equals to DEFAULT_WORKER_CODE
        defaultWorkerShouldBeFound("workerCode.equals=" + DEFAULT_WORKER_CODE);

        // Get all the workerList where workerCode equals to UPDATED_WORKER_CODE
        defaultWorkerShouldNotBeFound("workerCode.equals=" + UPDATED_WORKER_CODE);
    }

    @Test
    @Transactional
    public void getAllWorkersByWorkerCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        workerRepository.saveAndFlush(worker);

        // Get all the workerList where workerCode not equals to DEFAULT_WORKER_CODE
        defaultWorkerShouldNotBeFound("workerCode.notEquals=" + DEFAULT_WORKER_CODE);

        // Get all the workerList where workerCode not equals to UPDATED_WORKER_CODE
        defaultWorkerShouldBeFound("workerCode.notEquals=" + UPDATED_WORKER_CODE);
    }

    @Test
    @Transactional
    public void getAllWorkersByWorkerCodeIsInShouldWork() throws Exception {
        // Initialize the database
        workerRepository.saveAndFlush(worker);

        // Get all the workerList where workerCode in DEFAULT_WORKER_CODE or UPDATED_WORKER_CODE
        defaultWorkerShouldBeFound("workerCode.in=" + DEFAULT_WORKER_CODE + "," + UPDATED_WORKER_CODE);

        // Get all the workerList where workerCode equals to UPDATED_WORKER_CODE
        defaultWorkerShouldNotBeFound("workerCode.in=" + UPDATED_WORKER_CODE);
    }

    @Test
    @Transactional
    public void getAllWorkersByWorkerCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        workerRepository.saveAndFlush(worker);

        // Get all the workerList where workerCode is not null
        defaultWorkerShouldBeFound("workerCode.specified=true");

        // Get all the workerList where workerCode is null
        defaultWorkerShouldNotBeFound("workerCode.specified=false");
    }
                @Test
    @Transactional
    public void getAllWorkersByWorkerCodeContainsSomething() throws Exception {
        // Initialize the database
        workerRepository.saveAndFlush(worker);

        // Get all the workerList where workerCode contains DEFAULT_WORKER_CODE
        defaultWorkerShouldBeFound("workerCode.contains=" + DEFAULT_WORKER_CODE);

        // Get all the workerList where workerCode contains UPDATED_WORKER_CODE
        defaultWorkerShouldNotBeFound("workerCode.contains=" + UPDATED_WORKER_CODE);
    }

    @Test
    @Transactional
    public void getAllWorkersByWorkerCodeNotContainsSomething() throws Exception {
        // Initialize the database
        workerRepository.saveAndFlush(worker);

        // Get all the workerList where workerCode does not contain DEFAULT_WORKER_CODE
        defaultWorkerShouldNotBeFound("workerCode.doesNotContain=" + DEFAULT_WORKER_CODE);

        // Get all the workerList where workerCode does not contain UPDATED_WORKER_CODE
        defaultWorkerShouldBeFound("workerCode.doesNotContain=" + UPDATED_WORKER_CODE);
    }


    @Test
    @Transactional
    public void getAllWorkersByWorkerNameIsEqualToSomething() throws Exception {
        // Initialize the database
        workerRepository.saveAndFlush(worker);

        // Get all the workerList where workerName equals to DEFAULT_WORKER_NAME
        defaultWorkerShouldBeFound("workerName.equals=" + DEFAULT_WORKER_NAME);

        // Get all the workerList where workerName equals to UPDATED_WORKER_NAME
        defaultWorkerShouldNotBeFound("workerName.equals=" + UPDATED_WORKER_NAME);
    }

    @Test
    @Transactional
    public void getAllWorkersByWorkerNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        workerRepository.saveAndFlush(worker);

        // Get all the workerList where workerName not equals to DEFAULT_WORKER_NAME
        defaultWorkerShouldNotBeFound("workerName.notEquals=" + DEFAULT_WORKER_NAME);

        // Get all the workerList where workerName not equals to UPDATED_WORKER_NAME
        defaultWorkerShouldBeFound("workerName.notEquals=" + UPDATED_WORKER_NAME);
    }

    @Test
    @Transactional
    public void getAllWorkersByWorkerNameIsInShouldWork() throws Exception {
        // Initialize the database
        workerRepository.saveAndFlush(worker);

        // Get all the workerList where workerName in DEFAULT_WORKER_NAME or UPDATED_WORKER_NAME
        defaultWorkerShouldBeFound("workerName.in=" + DEFAULT_WORKER_NAME + "," + UPDATED_WORKER_NAME);

        // Get all the workerList where workerName equals to UPDATED_WORKER_NAME
        defaultWorkerShouldNotBeFound("workerName.in=" + UPDATED_WORKER_NAME);
    }

    @Test
    @Transactional
    public void getAllWorkersByWorkerNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        workerRepository.saveAndFlush(worker);

        // Get all the workerList where workerName is not null
        defaultWorkerShouldBeFound("workerName.specified=true");

        // Get all the workerList where workerName is null
        defaultWorkerShouldNotBeFound("workerName.specified=false");
    }
                @Test
    @Transactional
    public void getAllWorkersByWorkerNameContainsSomething() throws Exception {
        // Initialize the database
        workerRepository.saveAndFlush(worker);

        // Get all the workerList where workerName contains DEFAULT_WORKER_NAME
        defaultWorkerShouldBeFound("workerName.contains=" + DEFAULT_WORKER_NAME);

        // Get all the workerList where workerName contains UPDATED_WORKER_NAME
        defaultWorkerShouldNotBeFound("workerName.contains=" + UPDATED_WORKER_NAME);
    }

    @Test
    @Transactional
    public void getAllWorkersByWorkerNameNotContainsSomething() throws Exception {
        // Initialize the database
        workerRepository.saveAndFlush(worker);

        // Get all the workerList where workerName does not contain DEFAULT_WORKER_NAME
        defaultWorkerShouldNotBeFound("workerName.doesNotContain=" + DEFAULT_WORKER_NAME);

        // Get all the workerList where workerName does not contain UPDATED_WORKER_NAME
        defaultWorkerShouldBeFound("workerName.doesNotContain=" + UPDATED_WORKER_NAME);
    }


    @Test
    @Transactional
    public void getAllWorkersByWorkerLimitIsEqualToSomething() throws Exception {
        // Initialize the database
        workerRepository.saveAndFlush(worker);

        // Get all the workerList where workerLimit equals to DEFAULT_WORKER_LIMIT
        defaultWorkerShouldBeFound("workerLimit.equals=" + DEFAULT_WORKER_LIMIT);

        // Get all the workerList where workerLimit equals to UPDATED_WORKER_LIMIT
        defaultWorkerShouldNotBeFound("workerLimit.equals=" + UPDATED_WORKER_LIMIT);
    }

    @Test
    @Transactional
    public void getAllWorkersByWorkerLimitIsNotEqualToSomething() throws Exception {
        // Initialize the database
        workerRepository.saveAndFlush(worker);

        // Get all the workerList where workerLimit not equals to DEFAULT_WORKER_LIMIT
        defaultWorkerShouldNotBeFound("workerLimit.notEquals=" + DEFAULT_WORKER_LIMIT);

        // Get all the workerList where workerLimit not equals to UPDATED_WORKER_LIMIT
        defaultWorkerShouldBeFound("workerLimit.notEquals=" + UPDATED_WORKER_LIMIT);
    }

    @Test
    @Transactional
    public void getAllWorkersByWorkerLimitIsInShouldWork() throws Exception {
        // Initialize the database
        workerRepository.saveAndFlush(worker);

        // Get all the workerList where workerLimit in DEFAULT_WORKER_LIMIT or UPDATED_WORKER_LIMIT
        defaultWorkerShouldBeFound("workerLimit.in=" + DEFAULT_WORKER_LIMIT + "," + UPDATED_WORKER_LIMIT);

        // Get all the workerList where workerLimit equals to UPDATED_WORKER_LIMIT
        defaultWorkerShouldNotBeFound("workerLimit.in=" + UPDATED_WORKER_LIMIT);
    }

    @Test
    @Transactional
    public void getAllWorkersByWorkerLimitIsNullOrNotNull() throws Exception {
        // Initialize the database
        workerRepository.saveAndFlush(worker);

        // Get all the workerList where workerLimit is not null
        defaultWorkerShouldBeFound("workerLimit.specified=true");

        // Get all the workerList where workerLimit is null
        defaultWorkerShouldNotBeFound("workerLimit.specified=false");
    }

    @Test
    @Transactional
    public void getAllWorkersByWorkerLimitIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        workerRepository.saveAndFlush(worker);

        // Get all the workerList where workerLimit is greater than or equal to DEFAULT_WORKER_LIMIT
        defaultWorkerShouldBeFound("workerLimit.greaterThanOrEqual=" + DEFAULT_WORKER_LIMIT);

        // Get all the workerList where workerLimit is greater than or equal to UPDATED_WORKER_LIMIT
        defaultWorkerShouldNotBeFound("workerLimit.greaterThanOrEqual=" + UPDATED_WORKER_LIMIT);
    }

    @Test
    @Transactional
    public void getAllWorkersByWorkerLimitIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        workerRepository.saveAndFlush(worker);

        // Get all the workerList where workerLimit is less than or equal to DEFAULT_WORKER_LIMIT
        defaultWorkerShouldBeFound("workerLimit.lessThanOrEqual=" + DEFAULT_WORKER_LIMIT);

        // Get all the workerList where workerLimit is less than or equal to SMALLER_WORKER_LIMIT
        defaultWorkerShouldNotBeFound("workerLimit.lessThanOrEqual=" + SMALLER_WORKER_LIMIT);
    }

    @Test
    @Transactional
    public void getAllWorkersByWorkerLimitIsLessThanSomething() throws Exception {
        // Initialize the database
        workerRepository.saveAndFlush(worker);

        // Get all the workerList where workerLimit is less than DEFAULT_WORKER_LIMIT
        defaultWorkerShouldNotBeFound("workerLimit.lessThan=" + DEFAULT_WORKER_LIMIT);

        // Get all the workerList where workerLimit is less than UPDATED_WORKER_LIMIT
        defaultWorkerShouldBeFound("workerLimit.lessThan=" + UPDATED_WORKER_LIMIT);
    }

    @Test
    @Transactional
    public void getAllWorkersByWorkerLimitIsGreaterThanSomething() throws Exception {
        // Initialize the database
        workerRepository.saveAndFlush(worker);

        // Get all the workerList where workerLimit is greater than DEFAULT_WORKER_LIMIT
        defaultWorkerShouldNotBeFound("workerLimit.greaterThan=" + DEFAULT_WORKER_LIMIT);

        // Get all the workerList where workerLimit is greater than SMALLER_WORKER_LIMIT
        defaultWorkerShouldBeFound("workerLimit.greaterThan=" + SMALLER_WORKER_LIMIT);
    }


    @Test
    @Transactional
    public void getAllWorkersByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        workerRepository.saveAndFlush(worker);

        // Get all the workerList where isActive equals to DEFAULT_IS_ACTIVE
        defaultWorkerShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the workerList where isActive equals to UPDATED_IS_ACTIVE
        defaultWorkerShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllWorkersByIsActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        workerRepository.saveAndFlush(worker);

        // Get all the workerList where isActive not equals to DEFAULT_IS_ACTIVE
        defaultWorkerShouldNotBeFound("isActive.notEquals=" + DEFAULT_IS_ACTIVE);

        // Get all the workerList where isActive not equals to UPDATED_IS_ACTIVE
        defaultWorkerShouldBeFound("isActive.notEquals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllWorkersByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        workerRepository.saveAndFlush(worker);

        // Get all the workerList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultWorkerShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the workerList where isActive equals to UPDATED_IS_ACTIVE
        defaultWorkerShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllWorkersByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        workerRepository.saveAndFlush(worker);

        // Get all the workerList where isActive is not null
        defaultWorkerShouldBeFound("isActive.specified=true");

        // Get all the workerList where isActive is null
        defaultWorkerShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    public void getAllWorkersByRatingIsEqualToSomething() throws Exception {
        // Initialize the database
        workerRepository.saveAndFlush(worker);

        // Get all the workerList where rating equals to DEFAULT_RATING
        defaultWorkerShouldBeFound("rating.equals=" + DEFAULT_RATING);

        // Get all the workerList where rating equals to UPDATED_RATING
        defaultWorkerShouldNotBeFound("rating.equals=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    public void getAllWorkersByRatingIsNotEqualToSomething() throws Exception {
        // Initialize the database
        workerRepository.saveAndFlush(worker);

        // Get all the workerList where rating not equals to DEFAULT_RATING
        defaultWorkerShouldNotBeFound("rating.notEquals=" + DEFAULT_RATING);

        // Get all the workerList where rating not equals to UPDATED_RATING
        defaultWorkerShouldBeFound("rating.notEquals=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    public void getAllWorkersByRatingIsInShouldWork() throws Exception {
        // Initialize the database
        workerRepository.saveAndFlush(worker);

        // Get all the workerList where rating in DEFAULT_RATING or UPDATED_RATING
        defaultWorkerShouldBeFound("rating.in=" + DEFAULT_RATING + "," + UPDATED_RATING);

        // Get all the workerList where rating equals to UPDATED_RATING
        defaultWorkerShouldNotBeFound("rating.in=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    public void getAllWorkersByRatingIsNullOrNotNull() throws Exception {
        // Initialize the database
        workerRepository.saveAndFlush(worker);

        // Get all the workerList where rating is not null
        defaultWorkerShouldBeFound("rating.specified=true");

        // Get all the workerList where rating is null
        defaultWorkerShouldNotBeFound("rating.specified=false");
    }

    @Test
    @Transactional
    public void getAllWorkersByRatingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        workerRepository.saveAndFlush(worker);

        // Get all the workerList where rating is greater than or equal to DEFAULT_RATING
        defaultWorkerShouldBeFound("rating.greaterThanOrEqual=" + DEFAULT_RATING);

        // Get all the workerList where rating is greater than or equal to UPDATED_RATING
        defaultWorkerShouldNotBeFound("rating.greaterThanOrEqual=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    public void getAllWorkersByRatingIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        workerRepository.saveAndFlush(worker);

        // Get all the workerList where rating is less than or equal to DEFAULT_RATING
        defaultWorkerShouldBeFound("rating.lessThanOrEqual=" + DEFAULT_RATING);

        // Get all the workerList where rating is less than or equal to SMALLER_RATING
        defaultWorkerShouldNotBeFound("rating.lessThanOrEqual=" + SMALLER_RATING);
    }

    @Test
    @Transactional
    public void getAllWorkersByRatingIsLessThanSomething() throws Exception {
        // Initialize the database
        workerRepository.saveAndFlush(worker);

        // Get all the workerList where rating is less than DEFAULT_RATING
        defaultWorkerShouldNotBeFound("rating.lessThan=" + DEFAULT_RATING);

        // Get all the workerList where rating is less than UPDATED_RATING
        defaultWorkerShouldBeFound("rating.lessThan=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    public void getAllWorkersByRatingIsGreaterThanSomething() throws Exception {
        // Initialize the database
        workerRepository.saveAndFlush(worker);

        // Get all the workerList where rating is greater than DEFAULT_RATING
        defaultWorkerShouldNotBeFound("rating.greaterThan=" + DEFAULT_RATING);

        // Get all the workerList where rating is greater than SMALLER_RATING
        defaultWorkerShouldBeFound("rating.greaterThan=" + SMALLER_RATING);
    }


    @Test
    @Transactional
    public void getAllWorkersByLocationIsEqualToSomething() throws Exception {
        // Get already existing entity
        Location location = worker.getLocation();
        workerRepository.saveAndFlush(worker);
        Long locationId = location.getId();

        // Get all the workerList where location equals to locationId
        defaultWorkerShouldBeFound("locationId.equals=" + locationId);

        // Get all the workerList where location equals to locationId + 1
        defaultWorkerShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }


    @Test
    @Transactional
    public void getAllWorkersByJobsIsEqualToSomething() throws Exception {
        // Get already existing entity
        Job jobs = worker.getJobs();
        workerRepository.saveAndFlush(worker);
        Long jobsId = jobs.getId();

        // Get all the workerList where jobs equals to jobsId
        defaultWorkerShouldBeFound("jobsId.equals=" + jobsId);

        // Get all the workerList where jobs equals to jobsId + 1
        defaultWorkerShouldNotBeFound("jobsId.equals=" + (jobsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultWorkerShouldBeFound(String filter) throws Exception {
        restWorkerMockMvc.perform(get("/api/workers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(worker.getId().intValue())))
            .andExpect(jsonPath("$.[*].workerCode").value(hasItem(DEFAULT_WORKER_CODE)))
            .andExpect(jsonPath("$.[*].workerName").value(hasItem(DEFAULT_WORKER_NAME)))
            .andExpect(jsonPath("$.[*].workerLimit").value(hasItem(DEFAULT_WORKER_LIMIT.intValue())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING.doubleValue())));

        // Check, that the count call also returns 1
        restWorkerMockMvc.perform(get("/api/workers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultWorkerShouldNotBeFound(String filter) throws Exception {
        restWorkerMockMvc.perform(get("/api/workers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restWorkerMockMvc.perform(get("/api/workers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingWorker() throws Exception {
        // Get the worker
        restWorkerMockMvc.perform(get("/api/workers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWorker() throws Exception {
        // Initialize the database
        workerService.save(worker);

        int databaseSizeBeforeUpdate = workerRepository.findAll().size();

        // Update the worker
        Worker updatedWorker = workerRepository.findById(worker.getId()).get();
        // Disconnect from session so that the updates on updatedWorker are not directly saved in db
        em.detach(updatedWorker);
        updatedWorker
            .workerCode(UPDATED_WORKER_CODE)
            .workerName(UPDATED_WORKER_NAME)
            .workerLimit(UPDATED_WORKER_LIMIT)
            .isActive(UPDATED_IS_ACTIVE)
            .rating(UPDATED_RATING);

        restWorkerMockMvc.perform(put("/api/workers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedWorker)))
            .andExpect(status().isOk());

        // Validate the Worker in the database
        List<Worker> workerList = workerRepository.findAll();
        assertThat(workerList).hasSize(databaseSizeBeforeUpdate);
        Worker testWorker = workerList.get(workerList.size() - 1);
        assertThat(testWorker.getWorkerCode()).isEqualTo(UPDATED_WORKER_CODE);
        assertThat(testWorker.getWorkerName()).isEqualTo(UPDATED_WORKER_NAME);
        assertThat(testWorker.getWorkerLimit()).isEqualTo(UPDATED_WORKER_LIMIT);
        assertThat(testWorker.isIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testWorker.getRating()).isEqualTo(UPDATED_RATING);
    }

    @Test
    @Transactional
    public void updateNonExistingWorker() throws Exception {
        int databaseSizeBeforeUpdate = workerRepository.findAll().size();

        // Create the Worker

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkerMockMvc.perform(put("/api/workers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(worker)))
            .andExpect(status().isBadRequest());

        // Validate the Worker in the database
        List<Worker> workerList = workerRepository.findAll();
        assertThat(workerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteWorker() throws Exception {
        // Initialize the database
        workerService.save(worker);

        int databaseSizeBeforeDelete = workerRepository.findAll().size();

        // Delete the worker
        restWorkerMockMvc.perform(delete("/api/workers/{id}", worker.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Worker> workerList = workerRepository.findAll();
        assertThat(workerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

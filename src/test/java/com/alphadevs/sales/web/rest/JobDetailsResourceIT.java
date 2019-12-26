package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.WikunumApp;
import com.alphadevs.sales.domain.JobDetails;
import com.alphadevs.sales.domain.Items;
import com.alphadevs.sales.domain.Job;
import com.alphadevs.sales.repository.JobDetailsRepository;
import com.alphadevs.sales.service.JobDetailsService;
import com.alphadevs.sales.web.rest.errors.ExceptionTranslator;
import com.alphadevs.sales.service.dto.JobDetailsCriteria;
import com.alphadevs.sales.service.JobDetailsQueryService;

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
 * Integration tests for the {@link JobDetailsResource} REST controller.
 */
@SpringBootTest(classes = WikunumApp.class)
public class JobDetailsResourceIT {

    private static final BigDecimal DEFAULT_JOB_ITEM_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_JOB_ITEM_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_JOB_ITEM_PRICE = new BigDecimal(1 - 1);

    private static final Double DEFAULT_JOB_ITEM_QTY = 1D;
    private static final Double UPDATED_JOB_ITEM_QTY = 2D;
    private static final Double SMALLER_JOB_ITEM_QTY = 1D - 1D;

    @Autowired
    private JobDetailsRepository jobDetailsRepository;

    @Autowired
    private JobDetailsService jobDetailsService;

    @Autowired
    private JobDetailsQueryService jobDetailsQueryService;

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

    private MockMvc restJobDetailsMockMvc;

    private JobDetails jobDetails;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final JobDetailsResource jobDetailsResource = new JobDetailsResource(jobDetailsService, jobDetailsQueryService);
        this.restJobDetailsMockMvc = MockMvcBuilders.standaloneSetup(jobDetailsResource)
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
    public static JobDetails createEntity(EntityManager em) {
        JobDetails jobDetails = new JobDetails()
            .jobItemPrice(DEFAULT_JOB_ITEM_PRICE)
            .jobItemQty(DEFAULT_JOB_ITEM_QTY);
        // Add required entity
        Items items;
        if (TestUtil.findAll(em, Items.class).isEmpty()) {
            items = ItemsResourceIT.createEntity(em);
            em.persist(items);
            em.flush();
        } else {
            items = TestUtil.findAll(em, Items.class).get(0);
        }
        jobDetails.setItem(items);
        // Add required entity
        Job job;
        if (TestUtil.findAll(em, Job.class).isEmpty()) {
            job = JobResourceIT.createEntity(em);
            em.persist(job);
            em.flush();
        } else {
            job = TestUtil.findAll(em, Job.class).get(0);
        }
        jobDetails.setJob(job);
        return jobDetails;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JobDetails createUpdatedEntity(EntityManager em) {
        JobDetails jobDetails = new JobDetails()
            .jobItemPrice(UPDATED_JOB_ITEM_PRICE)
            .jobItemQty(UPDATED_JOB_ITEM_QTY);
        // Add required entity
        Items items;
        if (TestUtil.findAll(em, Items.class).isEmpty()) {
            items = ItemsResourceIT.createUpdatedEntity(em);
            em.persist(items);
            em.flush();
        } else {
            items = TestUtil.findAll(em, Items.class).get(0);
        }
        jobDetails.setItem(items);
        // Add required entity
        Job job;
        if (TestUtil.findAll(em, Job.class).isEmpty()) {
            job = JobResourceIT.createUpdatedEntity(em);
            em.persist(job);
            em.flush();
        } else {
            job = TestUtil.findAll(em, Job.class).get(0);
        }
        jobDetails.setJob(job);
        return jobDetails;
    }

    @BeforeEach
    public void initTest() {
        jobDetails = createEntity(em);
    }

    @Test
    @Transactional
    public void createJobDetails() throws Exception {
        int databaseSizeBeforeCreate = jobDetailsRepository.findAll().size();

        // Create the JobDetails
        restJobDetailsMockMvc.perform(post("/api/job-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobDetails)))
            .andExpect(status().isCreated());

        // Validate the JobDetails in the database
        List<JobDetails> jobDetailsList = jobDetailsRepository.findAll();
        assertThat(jobDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        JobDetails testJobDetails = jobDetailsList.get(jobDetailsList.size() - 1);
        assertThat(testJobDetails.getJobItemPrice()).isEqualTo(DEFAULT_JOB_ITEM_PRICE);
        assertThat(testJobDetails.getJobItemQty()).isEqualTo(DEFAULT_JOB_ITEM_QTY);
    }

    @Test
    @Transactional
    public void createJobDetailsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = jobDetailsRepository.findAll().size();

        // Create the JobDetails with an existing ID
        jobDetails.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restJobDetailsMockMvc.perform(post("/api/job-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobDetails)))
            .andExpect(status().isBadRequest());

        // Validate the JobDetails in the database
        List<JobDetails> jobDetailsList = jobDetailsRepository.findAll();
        assertThat(jobDetailsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkJobItemPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = jobDetailsRepository.findAll().size();
        // set the field null
        jobDetails.setJobItemPrice(null);

        // Create the JobDetails, which fails.

        restJobDetailsMockMvc.perform(post("/api/job-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobDetails)))
            .andExpect(status().isBadRequest());

        List<JobDetails> jobDetailsList = jobDetailsRepository.findAll();
        assertThat(jobDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkJobItemQtyIsRequired() throws Exception {
        int databaseSizeBeforeTest = jobDetailsRepository.findAll().size();
        // set the field null
        jobDetails.setJobItemQty(null);

        // Create the JobDetails, which fails.

        restJobDetailsMockMvc.perform(post("/api/job-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobDetails)))
            .andExpect(status().isBadRequest());

        List<JobDetails> jobDetailsList = jobDetailsRepository.findAll();
        assertThat(jobDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllJobDetails() throws Exception {
        // Initialize the database
        jobDetailsRepository.saveAndFlush(jobDetails);

        // Get all the jobDetailsList
        restJobDetailsMockMvc.perform(get("/api/job-details?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].jobItemPrice").value(hasItem(DEFAULT_JOB_ITEM_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].jobItemQty").value(hasItem(DEFAULT_JOB_ITEM_QTY.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getJobDetails() throws Exception {
        // Initialize the database
        jobDetailsRepository.saveAndFlush(jobDetails);

        // Get the jobDetails
        restJobDetailsMockMvc.perform(get("/api/job-details/{id}", jobDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(jobDetails.getId().intValue()))
            .andExpect(jsonPath("$.jobItemPrice").value(DEFAULT_JOB_ITEM_PRICE.intValue()))
            .andExpect(jsonPath("$.jobItemQty").value(DEFAULT_JOB_ITEM_QTY.doubleValue()));
    }


    @Test
    @Transactional
    public void getJobDetailsByIdFiltering() throws Exception {
        // Initialize the database
        jobDetailsRepository.saveAndFlush(jobDetails);

        Long id = jobDetails.getId();

        defaultJobDetailsShouldBeFound("id.equals=" + id);
        defaultJobDetailsShouldNotBeFound("id.notEquals=" + id);

        defaultJobDetailsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultJobDetailsShouldNotBeFound("id.greaterThan=" + id);

        defaultJobDetailsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultJobDetailsShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllJobDetailsByJobItemPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        jobDetailsRepository.saveAndFlush(jobDetails);

        // Get all the jobDetailsList where jobItemPrice equals to DEFAULT_JOB_ITEM_PRICE
        defaultJobDetailsShouldBeFound("jobItemPrice.equals=" + DEFAULT_JOB_ITEM_PRICE);

        // Get all the jobDetailsList where jobItemPrice equals to UPDATED_JOB_ITEM_PRICE
        defaultJobDetailsShouldNotBeFound("jobItemPrice.equals=" + UPDATED_JOB_ITEM_PRICE);
    }

    @Test
    @Transactional
    public void getAllJobDetailsByJobItemPriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jobDetailsRepository.saveAndFlush(jobDetails);

        // Get all the jobDetailsList where jobItemPrice not equals to DEFAULT_JOB_ITEM_PRICE
        defaultJobDetailsShouldNotBeFound("jobItemPrice.notEquals=" + DEFAULT_JOB_ITEM_PRICE);

        // Get all the jobDetailsList where jobItemPrice not equals to UPDATED_JOB_ITEM_PRICE
        defaultJobDetailsShouldBeFound("jobItemPrice.notEquals=" + UPDATED_JOB_ITEM_PRICE);
    }

    @Test
    @Transactional
    public void getAllJobDetailsByJobItemPriceIsInShouldWork() throws Exception {
        // Initialize the database
        jobDetailsRepository.saveAndFlush(jobDetails);

        // Get all the jobDetailsList where jobItemPrice in DEFAULT_JOB_ITEM_PRICE or UPDATED_JOB_ITEM_PRICE
        defaultJobDetailsShouldBeFound("jobItemPrice.in=" + DEFAULT_JOB_ITEM_PRICE + "," + UPDATED_JOB_ITEM_PRICE);

        // Get all the jobDetailsList where jobItemPrice equals to UPDATED_JOB_ITEM_PRICE
        defaultJobDetailsShouldNotBeFound("jobItemPrice.in=" + UPDATED_JOB_ITEM_PRICE);
    }

    @Test
    @Transactional
    public void getAllJobDetailsByJobItemPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobDetailsRepository.saveAndFlush(jobDetails);

        // Get all the jobDetailsList where jobItemPrice is not null
        defaultJobDetailsShouldBeFound("jobItemPrice.specified=true");

        // Get all the jobDetailsList where jobItemPrice is null
        defaultJobDetailsShouldNotBeFound("jobItemPrice.specified=false");
    }

    @Test
    @Transactional
    public void getAllJobDetailsByJobItemPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        jobDetailsRepository.saveAndFlush(jobDetails);

        // Get all the jobDetailsList where jobItemPrice is greater than or equal to DEFAULT_JOB_ITEM_PRICE
        defaultJobDetailsShouldBeFound("jobItemPrice.greaterThanOrEqual=" + DEFAULT_JOB_ITEM_PRICE);

        // Get all the jobDetailsList where jobItemPrice is greater than or equal to UPDATED_JOB_ITEM_PRICE
        defaultJobDetailsShouldNotBeFound("jobItemPrice.greaterThanOrEqual=" + UPDATED_JOB_ITEM_PRICE);
    }

    @Test
    @Transactional
    public void getAllJobDetailsByJobItemPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        jobDetailsRepository.saveAndFlush(jobDetails);

        // Get all the jobDetailsList where jobItemPrice is less than or equal to DEFAULT_JOB_ITEM_PRICE
        defaultJobDetailsShouldBeFound("jobItemPrice.lessThanOrEqual=" + DEFAULT_JOB_ITEM_PRICE);

        // Get all the jobDetailsList where jobItemPrice is less than or equal to SMALLER_JOB_ITEM_PRICE
        defaultJobDetailsShouldNotBeFound("jobItemPrice.lessThanOrEqual=" + SMALLER_JOB_ITEM_PRICE);
    }

    @Test
    @Transactional
    public void getAllJobDetailsByJobItemPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        jobDetailsRepository.saveAndFlush(jobDetails);

        // Get all the jobDetailsList where jobItemPrice is less than DEFAULT_JOB_ITEM_PRICE
        defaultJobDetailsShouldNotBeFound("jobItemPrice.lessThan=" + DEFAULT_JOB_ITEM_PRICE);

        // Get all the jobDetailsList where jobItemPrice is less than UPDATED_JOB_ITEM_PRICE
        defaultJobDetailsShouldBeFound("jobItemPrice.lessThan=" + UPDATED_JOB_ITEM_PRICE);
    }

    @Test
    @Transactional
    public void getAllJobDetailsByJobItemPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        jobDetailsRepository.saveAndFlush(jobDetails);

        // Get all the jobDetailsList where jobItemPrice is greater than DEFAULT_JOB_ITEM_PRICE
        defaultJobDetailsShouldNotBeFound("jobItemPrice.greaterThan=" + DEFAULT_JOB_ITEM_PRICE);

        // Get all the jobDetailsList where jobItemPrice is greater than SMALLER_JOB_ITEM_PRICE
        defaultJobDetailsShouldBeFound("jobItemPrice.greaterThan=" + SMALLER_JOB_ITEM_PRICE);
    }


    @Test
    @Transactional
    public void getAllJobDetailsByJobItemQtyIsEqualToSomething() throws Exception {
        // Initialize the database
        jobDetailsRepository.saveAndFlush(jobDetails);

        // Get all the jobDetailsList where jobItemQty equals to DEFAULT_JOB_ITEM_QTY
        defaultJobDetailsShouldBeFound("jobItemQty.equals=" + DEFAULT_JOB_ITEM_QTY);

        // Get all the jobDetailsList where jobItemQty equals to UPDATED_JOB_ITEM_QTY
        defaultJobDetailsShouldNotBeFound("jobItemQty.equals=" + UPDATED_JOB_ITEM_QTY);
    }

    @Test
    @Transactional
    public void getAllJobDetailsByJobItemQtyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jobDetailsRepository.saveAndFlush(jobDetails);

        // Get all the jobDetailsList where jobItemQty not equals to DEFAULT_JOB_ITEM_QTY
        defaultJobDetailsShouldNotBeFound("jobItemQty.notEquals=" + DEFAULT_JOB_ITEM_QTY);

        // Get all the jobDetailsList where jobItemQty not equals to UPDATED_JOB_ITEM_QTY
        defaultJobDetailsShouldBeFound("jobItemQty.notEquals=" + UPDATED_JOB_ITEM_QTY);
    }

    @Test
    @Transactional
    public void getAllJobDetailsByJobItemQtyIsInShouldWork() throws Exception {
        // Initialize the database
        jobDetailsRepository.saveAndFlush(jobDetails);

        // Get all the jobDetailsList where jobItemQty in DEFAULT_JOB_ITEM_QTY or UPDATED_JOB_ITEM_QTY
        defaultJobDetailsShouldBeFound("jobItemQty.in=" + DEFAULT_JOB_ITEM_QTY + "," + UPDATED_JOB_ITEM_QTY);

        // Get all the jobDetailsList where jobItemQty equals to UPDATED_JOB_ITEM_QTY
        defaultJobDetailsShouldNotBeFound("jobItemQty.in=" + UPDATED_JOB_ITEM_QTY);
    }

    @Test
    @Transactional
    public void getAllJobDetailsByJobItemQtyIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobDetailsRepository.saveAndFlush(jobDetails);

        // Get all the jobDetailsList where jobItemQty is not null
        defaultJobDetailsShouldBeFound("jobItemQty.specified=true");

        // Get all the jobDetailsList where jobItemQty is null
        defaultJobDetailsShouldNotBeFound("jobItemQty.specified=false");
    }

    @Test
    @Transactional
    public void getAllJobDetailsByJobItemQtyIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        jobDetailsRepository.saveAndFlush(jobDetails);

        // Get all the jobDetailsList where jobItemQty is greater than or equal to DEFAULT_JOB_ITEM_QTY
        defaultJobDetailsShouldBeFound("jobItemQty.greaterThanOrEqual=" + DEFAULT_JOB_ITEM_QTY);

        // Get all the jobDetailsList where jobItemQty is greater than or equal to UPDATED_JOB_ITEM_QTY
        defaultJobDetailsShouldNotBeFound("jobItemQty.greaterThanOrEqual=" + UPDATED_JOB_ITEM_QTY);
    }

    @Test
    @Transactional
    public void getAllJobDetailsByJobItemQtyIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        jobDetailsRepository.saveAndFlush(jobDetails);

        // Get all the jobDetailsList where jobItemQty is less than or equal to DEFAULT_JOB_ITEM_QTY
        defaultJobDetailsShouldBeFound("jobItemQty.lessThanOrEqual=" + DEFAULT_JOB_ITEM_QTY);

        // Get all the jobDetailsList where jobItemQty is less than or equal to SMALLER_JOB_ITEM_QTY
        defaultJobDetailsShouldNotBeFound("jobItemQty.lessThanOrEqual=" + SMALLER_JOB_ITEM_QTY);
    }

    @Test
    @Transactional
    public void getAllJobDetailsByJobItemQtyIsLessThanSomething() throws Exception {
        // Initialize the database
        jobDetailsRepository.saveAndFlush(jobDetails);

        // Get all the jobDetailsList where jobItemQty is less than DEFAULT_JOB_ITEM_QTY
        defaultJobDetailsShouldNotBeFound("jobItemQty.lessThan=" + DEFAULT_JOB_ITEM_QTY);

        // Get all the jobDetailsList where jobItemQty is less than UPDATED_JOB_ITEM_QTY
        defaultJobDetailsShouldBeFound("jobItemQty.lessThan=" + UPDATED_JOB_ITEM_QTY);
    }

    @Test
    @Transactional
    public void getAllJobDetailsByJobItemQtyIsGreaterThanSomething() throws Exception {
        // Initialize the database
        jobDetailsRepository.saveAndFlush(jobDetails);

        // Get all the jobDetailsList where jobItemQty is greater than DEFAULT_JOB_ITEM_QTY
        defaultJobDetailsShouldNotBeFound("jobItemQty.greaterThan=" + DEFAULT_JOB_ITEM_QTY);

        // Get all the jobDetailsList where jobItemQty is greater than SMALLER_JOB_ITEM_QTY
        defaultJobDetailsShouldBeFound("jobItemQty.greaterThan=" + SMALLER_JOB_ITEM_QTY);
    }


    @Test
    @Transactional
    public void getAllJobDetailsByItemIsEqualToSomething() throws Exception {
        // Get already existing entity
        Items item = jobDetails.getItem();
        jobDetailsRepository.saveAndFlush(jobDetails);
        Long itemId = item.getId();

        // Get all the jobDetailsList where item equals to itemId
        defaultJobDetailsShouldBeFound("itemId.equals=" + itemId);

        // Get all the jobDetailsList where item equals to itemId + 1
        defaultJobDetailsShouldNotBeFound("itemId.equals=" + (itemId + 1));
    }


    @Test
    @Transactional
    public void getAllJobDetailsByJobIsEqualToSomething() throws Exception {
        // Get already existing entity
        Job job = jobDetails.getJob();
        jobDetailsRepository.saveAndFlush(jobDetails);
        Long jobId = job.getId();

        // Get all the jobDetailsList where job equals to jobId
        defaultJobDetailsShouldBeFound("jobId.equals=" + jobId);

        // Get all the jobDetailsList where job equals to jobId + 1
        defaultJobDetailsShouldNotBeFound("jobId.equals=" + (jobId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultJobDetailsShouldBeFound(String filter) throws Exception {
        restJobDetailsMockMvc.perform(get("/api/job-details?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].jobItemPrice").value(hasItem(DEFAULT_JOB_ITEM_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].jobItemQty").value(hasItem(DEFAULT_JOB_ITEM_QTY.doubleValue())));

        // Check, that the count call also returns 1
        restJobDetailsMockMvc.perform(get("/api/job-details/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultJobDetailsShouldNotBeFound(String filter) throws Exception {
        restJobDetailsMockMvc.perform(get("/api/job-details?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restJobDetailsMockMvc.perform(get("/api/job-details/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingJobDetails() throws Exception {
        // Get the jobDetails
        restJobDetailsMockMvc.perform(get("/api/job-details/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJobDetails() throws Exception {
        // Initialize the database
        jobDetailsService.save(jobDetails);

        int databaseSizeBeforeUpdate = jobDetailsRepository.findAll().size();

        // Update the jobDetails
        JobDetails updatedJobDetails = jobDetailsRepository.findById(jobDetails.getId()).get();
        // Disconnect from session so that the updates on updatedJobDetails are not directly saved in db
        em.detach(updatedJobDetails);
        updatedJobDetails
            .jobItemPrice(UPDATED_JOB_ITEM_PRICE)
            .jobItemQty(UPDATED_JOB_ITEM_QTY);

        restJobDetailsMockMvc.perform(put("/api/job-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedJobDetails)))
            .andExpect(status().isOk());

        // Validate the JobDetails in the database
        List<JobDetails> jobDetailsList = jobDetailsRepository.findAll();
        assertThat(jobDetailsList).hasSize(databaseSizeBeforeUpdate);
        JobDetails testJobDetails = jobDetailsList.get(jobDetailsList.size() - 1);
        assertThat(testJobDetails.getJobItemPrice()).isEqualTo(UPDATED_JOB_ITEM_PRICE);
        assertThat(testJobDetails.getJobItemQty()).isEqualTo(UPDATED_JOB_ITEM_QTY);
    }

    @Test
    @Transactional
    public void updateNonExistingJobDetails() throws Exception {
        int databaseSizeBeforeUpdate = jobDetailsRepository.findAll().size();

        // Create the JobDetails

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJobDetailsMockMvc.perform(put("/api/job-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobDetails)))
            .andExpect(status().isBadRequest());

        // Validate the JobDetails in the database
        List<JobDetails> jobDetailsList = jobDetailsRepository.findAll();
        assertThat(jobDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteJobDetails() throws Exception {
        // Initialize the database
        jobDetailsService.save(jobDetails);

        int databaseSizeBeforeDelete = jobDetailsRepository.findAll().size();

        // Delete the jobDetails
        restJobDetailsMockMvc.perform(delete("/api/job-details/{id}", jobDetails.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<JobDetails> jobDetailsList = jobDetailsRepository.findAll();
        assertThat(jobDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

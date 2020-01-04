package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.domain.JobDetails;
import com.alphadevs.sales.service.JobDetailsService;
import com.alphadevs.sales.web.rest.errors.BadRequestAlertException;
import com.alphadevs.sales.service.dto.JobDetailsCriteria;
import com.alphadevs.sales.service.JobDetailsQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.alphadevs.sales.domain.JobDetails}.
 */
@RestController
@RequestMapping("/api")
public class JobDetailsResource {

    private final Logger log = LoggerFactory.getLogger(JobDetailsResource.class);

    private static final String ENTITY_NAME = "jobDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final JobDetailsService jobDetailsService;

    private final JobDetailsQueryService jobDetailsQueryService;

    public JobDetailsResource(JobDetailsService jobDetailsService, JobDetailsQueryService jobDetailsQueryService) {
        this.jobDetailsService = jobDetailsService;
        this.jobDetailsQueryService = jobDetailsQueryService;
    }

    /**
     * {@code POST  /job-details} : Create a new jobDetails.
     *
     * @param jobDetails the jobDetails to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new jobDetails, or with status {@code 400 (Bad Request)} if the jobDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/job-details")
    public ResponseEntity<JobDetails> createJobDetails(@Valid @RequestBody JobDetails jobDetails) throws URISyntaxException {
        log.debug("REST request to save JobDetails : {}", jobDetails);
        if (jobDetails.getId() != null) {
            throw new BadRequestAlertException("A new jobDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        JobDetails result = jobDetailsService.save(jobDetails);
        return ResponseEntity.created(new URI("/api/job-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /job-details} : Updates an existing jobDetails.
     *
     * @param jobDetails the jobDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated jobDetails,
     * or with status {@code 400 (Bad Request)} if the jobDetails is not valid,
     * or with status {@code 500 (Internal Server Error)} if the jobDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/job-details")
    public ResponseEntity<JobDetails> updateJobDetails(@Valid @RequestBody JobDetails jobDetails) throws URISyntaxException {
        log.debug("REST request to update JobDetails : {}", jobDetails);
        if (jobDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        JobDetails result = jobDetailsService.save(jobDetails);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, jobDetails.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /job-details} : get all the jobDetails.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of jobDetails in body.
     */
    @GetMapping("/job-details")
    public ResponseEntity<List<JobDetails>> getAllJobDetails(JobDetailsCriteria criteria, Pageable pageable) {
        log.debug("REST request to get JobDetails by criteria: {}", criteria);
        Page<JobDetails> page = jobDetailsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /job-details/count} : count all the jobDetails.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/job-details/count")
    public ResponseEntity<Long> countJobDetails(JobDetailsCriteria criteria) {
        log.debug("REST request to count JobDetails by criteria: {}", criteria);
        return ResponseEntity.ok().body(jobDetailsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /job-details/:id} : get the "id" jobDetails.
     *
     * @param id the id of the jobDetails to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the jobDetails, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/job-details/{id}")
    public ResponseEntity<JobDetails> getJobDetails(@PathVariable Long id) {
        log.debug("REST request to get JobDetails : {}", id);
        Optional<JobDetails> jobDetails = jobDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(jobDetails);
    }

    /**
     * {@code DELETE  /job-details/:id} : delete the "id" jobDetails.
     *
     * @param id the id of the jobDetails to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/job-details/{id}")
    public ResponseEntity<Void> deleteJobDetails(@PathVariable Long id) {
        log.debug("REST request to delete JobDetails : {}", id);
        jobDetailsService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}

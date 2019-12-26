package com.alphadevs.sales.service;

import com.alphadevs.sales.domain.JobDetails;
import com.alphadevs.sales.repository.JobDetailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link JobDetails}.
 */
@Service
@Transactional
public class JobDetailsService {

    private final Logger log = LoggerFactory.getLogger(JobDetailsService.class);

    private final JobDetailsRepository jobDetailsRepository;

    public JobDetailsService(JobDetailsRepository jobDetailsRepository) {
        this.jobDetailsRepository = jobDetailsRepository;
    }

    /**
     * Save a jobDetails.
     *
     * @param jobDetails the entity to save.
     * @return the persisted entity.
     */
    public JobDetails save(JobDetails jobDetails) {
        log.debug("Request to save JobDetails : {}", jobDetails);
        return jobDetailsRepository.save(jobDetails);
    }

    /**
     * Get all the jobDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<JobDetails> findAll(Pageable pageable) {
        log.debug("Request to get all JobDetails");
        return jobDetailsRepository.findAll(pageable);
    }


    /**
     * Get one jobDetails by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<JobDetails> findOne(Long id) {
        log.debug("Request to get JobDetails : {}", id);
        return jobDetailsRepository.findById(id);
    }

    /**
     * Delete the jobDetails by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete JobDetails : {}", id);
        jobDetailsRepository.deleteById(id);
    }
}

package com.alphadevs.sales.service;

import com.alphadevs.sales.domain.JobStatus;
import com.alphadevs.sales.repository.JobStatusRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link JobStatus}.
 */
@Service
@Transactional
public class JobStatusService {

    private final Logger log = LoggerFactory.getLogger(JobStatusService.class);

    private final JobStatusRepository jobStatusRepository;

    public JobStatusService(JobStatusRepository jobStatusRepository) {
        this.jobStatusRepository = jobStatusRepository;
    }

    /**
     * Save a jobStatus.
     *
     * @param jobStatus the entity to save.
     * @return the persisted entity.
     */
    public JobStatus save(JobStatus jobStatus) {
        log.debug("Request to save JobStatus : {}", jobStatus);
        return jobStatusRepository.save(jobStatus);
    }

    /**
     * Get all the jobStatuses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<JobStatus> findAll(Pageable pageable) {
        log.debug("Request to get all JobStatuses");
        return jobStatusRepository.findAll(pageable);
    }


    /**
     * Get one jobStatus by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<JobStatus> findOne(Long id) {
        log.debug("Request to get JobStatus : {}", id);
        return jobStatusRepository.findById(id);
    }

    /**
     * Delete the jobStatus by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete JobStatus : {}", id);
        jobStatusRepository.deleteById(id);
    }
}

package com.alphadevs.sales.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.alphadevs.sales.domain.JobDetails;
import com.alphadevs.sales.domain.*; // for static metamodels
import com.alphadevs.sales.repository.JobDetailsRepository;
import com.alphadevs.sales.service.dto.JobDetailsCriteria;

/**
 * Service for executing complex queries for {@link JobDetails} entities in the database.
 * The main input is a {@link JobDetailsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link JobDetails} or a {@link Page} of {@link JobDetails} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class JobDetailsQueryService extends QueryService<JobDetails> {

    private final Logger log = LoggerFactory.getLogger(JobDetailsQueryService.class);

    private final JobDetailsRepository jobDetailsRepository;

    public JobDetailsQueryService(JobDetailsRepository jobDetailsRepository) {
        this.jobDetailsRepository = jobDetailsRepository;
    }

    /**
     * Return a {@link List} of {@link JobDetails} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<JobDetails> findByCriteria(JobDetailsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<JobDetails> specification = createSpecification(criteria);
        return jobDetailsRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link JobDetails} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<JobDetails> findByCriteria(JobDetailsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<JobDetails> specification = createSpecification(criteria);
        return jobDetailsRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(JobDetailsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<JobDetails> specification = createSpecification(criteria);
        return jobDetailsRepository.count(specification);
    }

    /**
     * Function to convert {@link JobDetailsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<JobDetails> createSpecification(JobDetailsCriteria criteria) {
        Specification<JobDetails> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), JobDetails_.id));
            }
            if (criteria.getJobItemPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getJobItemPrice(), JobDetails_.jobItemPrice));
            }
            if (criteria.getJobItemQty() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getJobItemQty(), JobDetails_.jobItemQty));
            }
            if (criteria.getItemId() != null) {
                specification = specification.and(buildSpecification(criteria.getItemId(),
                    root -> root.join(JobDetails_.item, JoinType.LEFT).get(Items_.id)));
            }
            if (criteria.getJobId() != null) {
                specification = specification.and(buildSpecification(criteria.getJobId(),
                    root -> root.join(JobDetails_.job, JoinType.LEFT).get(Job_.id)));
            }
        }
        return specification;
    }
}

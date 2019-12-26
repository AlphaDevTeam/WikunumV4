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

import com.alphadevs.sales.domain.ChangeLog;
import com.alphadevs.sales.domain.*; // for static metamodels
import com.alphadevs.sales.repository.ChangeLogRepository;
import com.alphadevs.sales.service.dto.ChangeLogCriteria;

/**
 * Service for executing complex queries for {@link ChangeLog} entities in the database.
 * The main input is a {@link ChangeLogCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ChangeLog} or a {@link Page} of {@link ChangeLog} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ChangeLogQueryService extends QueryService<ChangeLog> {

    private final Logger log = LoggerFactory.getLogger(ChangeLogQueryService.class);

    private final ChangeLogRepository changeLogRepository;

    public ChangeLogQueryService(ChangeLogRepository changeLogRepository) {
        this.changeLogRepository = changeLogRepository;
    }

    /**
     * Return a {@link List} of {@link ChangeLog} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ChangeLog> findByCriteria(ChangeLogCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ChangeLog> specification = createSpecification(criteria);
        return changeLogRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ChangeLog} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ChangeLog> findByCriteria(ChangeLogCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ChangeLog> specification = createSpecification(criteria);
        return changeLogRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ChangeLogCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ChangeLog> specification = createSpecification(criteria);
        return changeLogRepository.count(specification);
    }

    /**
     * Function to convert {@link ChangeLogCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ChangeLog> createSpecification(ChangeLogCriteria criteria) {
        Specification<ChangeLog> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ChangeLog_.id));
            }
            if (criteria.getChangeKey() != null) {
                specification = specification.and(buildStringSpecification(criteria.getChangeKey(), ChangeLog_.changeKey));
            }
            if (criteria.getChangeFrom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getChangeFrom(), ChangeLog_.changeFrom));
            }
            if (criteria.getChangeTo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getChangeTo(), ChangeLog_.changeTo));
            }
            if (criteria.getDocumentHistoryId() != null) {
                specification = specification.and(buildSpecification(criteria.getDocumentHistoryId(),
                    root -> root.join(ChangeLog_.documentHistories, JoinType.LEFT).get(DocumentHistory_.id)));
            }
        }
        return specification;
    }
}

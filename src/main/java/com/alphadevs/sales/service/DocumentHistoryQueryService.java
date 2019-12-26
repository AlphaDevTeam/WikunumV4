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

import com.alphadevs.sales.domain.DocumentHistory;
import com.alphadevs.sales.domain.*; // for static metamodels
import com.alphadevs.sales.repository.DocumentHistoryRepository;
import com.alphadevs.sales.service.dto.DocumentHistoryCriteria;

/**
 * Service for executing complex queries for {@link DocumentHistory} entities in the database.
 * The main input is a {@link DocumentHistoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DocumentHistory} or a {@link Page} of {@link DocumentHistory} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DocumentHistoryQueryService extends QueryService<DocumentHistory> {

    private final Logger log = LoggerFactory.getLogger(DocumentHistoryQueryService.class);

    private final DocumentHistoryRepository documentHistoryRepository;

    public DocumentHistoryQueryService(DocumentHistoryRepository documentHistoryRepository) {
        this.documentHistoryRepository = documentHistoryRepository;
    }

    /**
     * Return a {@link List} of {@link DocumentHistory} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DocumentHistory> findByCriteria(DocumentHistoryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<DocumentHistory> specification = createSpecification(criteria);
        return documentHistoryRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link DocumentHistory} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DocumentHistory> findByCriteria(DocumentHistoryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DocumentHistory> specification = createSpecification(criteria);
        return documentHistoryRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DocumentHistoryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<DocumentHistory> specification = createSpecification(criteria);
        return documentHistoryRepository.count(specification);
    }

    /**
     * Function to convert {@link DocumentHistoryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DocumentHistory> createSpecification(DocumentHistoryCriteria criteria) {
        Specification<DocumentHistory> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), DocumentHistory_.id));
            }
            if (criteria.getHistoryDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getHistoryDescription(), DocumentHistory_.historyDescription));
            }
            if (criteria.getHistoryDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHistoryDate(), DocumentHistory_.historyDate));
            }
            if (criteria.getTypeId() != null) {
                specification = specification.and(buildSpecification(criteria.getTypeId(),
                    root -> root.join(DocumentHistory_.type, JoinType.LEFT).get(DocumentType_.id)));
            }
            if (criteria.getLastModifiedUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getLastModifiedUserId(),
                    root -> root.join(DocumentHistory_.lastModifiedUser, JoinType.LEFT).get(ExUser_.id)));
            }
            if (criteria.getCreatedUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getCreatedUserId(),
                    root -> root.join(DocumentHistory_.createdUser, JoinType.LEFT).get(ExUser_.id)));
            }
            if (criteria.getChangeLogId() != null) {
                specification = specification.and(buildSpecification(criteria.getChangeLogId(),
                    root -> root.join(DocumentHistory_.changeLogs, JoinType.LEFT).get(ChangeLog_.id)));
            }
        }
        return specification;
    }
}

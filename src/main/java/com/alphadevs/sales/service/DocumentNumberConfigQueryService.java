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

import com.alphadevs.sales.domain.DocumentNumberConfig;
import com.alphadevs.sales.domain.*; // for static metamodels
import com.alphadevs.sales.repository.DocumentNumberConfigRepository;
import com.alphadevs.sales.service.dto.DocumentNumberConfigCriteria;

/**
 * Service for executing complex queries for {@link DocumentNumberConfig} entities in the database.
 * The main input is a {@link DocumentNumberConfigCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DocumentNumberConfig} or a {@link Page} of {@link DocumentNumberConfig} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DocumentNumberConfigQueryService extends QueryService<DocumentNumberConfig> {

    private final Logger log = LoggerFactory.getLogger(DocumentNumberConfigQueryService.class);

    private final DocumentNumberConfigRepository documentNumberConfigRepository;

    public DocumentNumberConfigQueryService(DocumentNumberConfigRepository documentNumberConfigRepository) {
        this.documentNumberConfigRepository = documentNumberConfigRepository;
    }

    /**
     * Return a {@link List} of {@link DocumentNumberConfig} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DocumentNumberConfig> findByCriteria(DocumentNumberConfigCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<DocumentNumberConfig> specification = createSpecification(criteria);
        return documentNumberConfigRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link DocumentNumberConfig} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DocumentNumberConfig> findByCriteria(DocumentNumberConfigCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DocumentNumberConfig> specification = createSpecification(criteria);
        return documentNumberConfigRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DocumentNumberConfigCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<DocumentNumberConfig> specification = createSpecification(criteria);
        return documentNumberConfigRepository.count(specification);
    }

    /**
     * Function to convert {@link DocumentNumberConfigCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DocumentNumberConfig> createSpecification(DocumentNumberConfigCriteria criteria) {
        Specification<DocumentNumberConfig> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), DocumentNumberConfig_.id));
            }
            if (criteria.getDocumentPrefix() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDocumentPrefix(), DocumentNumberConfig_.documentPrefix));
            }
            if (criteria.getDocumentPostfix() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDocumentPostfix(), DocumentNumberConfig_.documentPostfix));
            }
            if (criteria.getCurrentNumber() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCurrentNumber(), DocumentNumberConfig_.currentNumber));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), DocumentNumberConfig_.isActive));
            }
            if (criteria.getDocumentId() != null) {
                specification = specification.and(buildSpecification(criteria.getDocumentId(),
                    root -> root.join(DocumentNumberConfig_.document, JoinType.LEFT).get(DocumentType_.id)));
            }
            if (criteria.getLocationId() != null) {
                specification = specification.and(buildSpecification(criteria.getLocationId(),
                    root -> root.join(DocumentNumberConfig_.location, JoinType.LEFT).get(Location_.id)));
            }
            if (criteria.getTransactionTypeId() != null) {
                specification = specification.and(buildSpecification(criteria.getTransactionTypeId(),
                    root -> root.join(DocumentNumberConfig_.transactionType, JoinType.LEFT).get(TransactionType_.id)));
            }
        }
        return specification;
    }
}

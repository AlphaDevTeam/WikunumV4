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

import com.alphadevs.sales.domain.QuotationDetails;
import com.alphadevs.sales.domain.*; // for static metamodels
import com.alphadevs.sales.repository.QuotationDetailsRepository;
import com.alphadevs.sales.service.dto.QuotationDetailsCriteria;

/**
 * Service for executing complex queries for {@link QuotationDetails} entities in the database.
 * The main input is a {@link QuotationDetailsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link QuotationDetails} or a {@link Page} of {@link QuotationDetails} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class QuotationDetailsQueryService extends QueryService<QuotationDetails> {

    private final Logger log = LoggerFactory.getLogger(QuotationDetailsQueryService.class);

    private final QuotationDetailsRepository quotationDetailsRepository;

    public QuotationDetailsQueryService(QuotationDetailsRepository quotationDetailsRepository) {
        this.quotationDetailsRepository = quotationDetailsRepository;
    }

    /**
     * Return a {@link List} of {@link QuotationDetails} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<QuotationDetails> findByCriteria(QuotationDetailsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<QuotationDetails> specification = createSpecification(criteria);
        return quotationDetailsRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link QuotationDetails} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<QuotationDetails> findByCriteria(QuotationDetailsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<QuotationDetails> specification = createSpecification(criteria);
        return quotationDetailsRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(QuotationDetailsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<QuotationDetails> specification = createSpecification(criteria);
        return quotationDetailsRepository.count(specification);
    }

    /**
     * Function to convert {@link QuotationDetailsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<QuotationDetails> createSpecification(QuotationDetailsCriteria criteria) {
        Specification<QuotationDetails> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), QuotationDetails_.id));
            }
            if (criteria.getRate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRate(), QuotationDetails_.rate));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), QuotationDetails_.description));
            }
            if (criteria.getItemId() != null) {
                specification = specification.and(buildSpecification(criteria.getItemId(),
                    root -> root.join(QuotationDetails_.item, JoinType.LEFT).get(Items_.id)));
            }
            if (criteria.getQuoteId() != null) {
                specification = specification.and(buildSpecification(criteria.getQuoteId(),
                    root -> root.join(QuotationDetails_.quote, JoinType.LEFT).get(Quotation_.id)));
            }
        }
        return specification;
    }
}

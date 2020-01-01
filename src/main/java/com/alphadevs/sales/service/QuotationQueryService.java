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

import com.alphadevs.sales.domain.Quotation;
import com.alphadevs.sales.domain.*; // for static metamodels
import com.alphadevs.sales.repository.QuotationRepository;
import com.alphadevs.sales.service.dto.QuotationCriteria;

/**
 * Service for executing complex queries for {@link Quotation} entities in the database.
 * The main input is a {@link QuotationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Quotation} or a {@link Page} of {@link Quotation} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class QuotationQueryService extends QueryService<Quotation> {

    private final Logger log = LoggerFactory.getLogger(QuotationQueryService.class);

    private final QuotationRepository quotationRepository;

    public QuotationQueryService(QuotationRepository quotationRepository) {
        this.quotationRepository = quotationRepository;
    }

    /**
     * Return a {@link List} of {@link Quotation} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Quotation> findByCriteria(QuotationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Quotation> specification = createSpecification(criteria);
        return quotationRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Quotation} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Quotation> findByCriteria(QuotationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Quotation> specification = createSpecification(criteria);
        return quotationRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(QuotationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Quotation> specification = createSpecification(criteria);
        return quotationRepository.count(specification);
    }

    /**
     * Function to convert {@link QuotationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Quotation> createSpecification(QuotationCriteria criteria) {
        Specification<Quotation> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Quotation_.id));
            }
            if (criteria.getQuotationNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getQuotationNumber(), Quotation_.quotationNumber));
            }
            if (criteria.getQuotationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuotationDate(), Quotation_.quotationDate));
            }
            if (criteria.getQuotationexpireDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuotationexpireDate(), Quotation_.quotationexpireDate));
            }
            if (criteria.getQuotationTotalAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuotationTotalAmount(), Quotation_.quotationTotalAmount));
            }
            if (criteria.getQuotationTo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getQuotationTo(), Quotation_.quotationTo));
            }
            if (criteria.getQuotationFrom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getQuotationFrom(), Quotation_.quotationFrom));
            }
            if (criteria.getProjectNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProjectNumber(), Quotation_.projectNumber));
            }
            if (criteria.getQuotationNote() != null) {
                specification = specification.and(buildStringSpecification(criteria.getQuotationNote(), Quotation_.quotationNote));
            }
            if (criteria.getDetailsId() != null) {
                specification = specification.and(buildSpecification(criteria.getDetailsId(),
                    root -> root.join(Quotation_.details, JoinType.LEFT).get(QuotationDetails_.id)));
            }
            if (criteria.getLocationId() != null) {
                specification = specification.and(buildSpecification(criteria.getLocationId(),
                    root -> root.join(Quotation_.location, JoinType.LEFT).get(Location_.id)));
            }
        }
        return specification;
    }
}

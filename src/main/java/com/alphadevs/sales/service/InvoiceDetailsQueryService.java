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

import com.alphadevs.sales.domain.InvoiceDetails;
import com.alphadevs.sales.domain.*; // for static metamodels
import com.alphadevs.sales.repository.InvoiceDetailsRepository;
import com.alphadevs.sales.service.dto.InvoiceDetailsCriteria;

/**
 * Service for executing complex queries for {@link InvoiceDetails} entities in the database.
 * The main input is a {@link InvoiceDetailsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link InvoiceDetails} or a {@link Page} of {@link InvoiceDetails} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InvoiceDetailsQueryService extends QueryService<InvoiceDetails> {

    private final Logger log = LoggerFactory.getLogger(InvoiceDetailsQueryService.class);

    private final InvoiceDetailsRepository invoiceDetailsRepository;

    public InvoiceDetailsQueryService(InvoiceDetailsRepository invoiceDetailsRepository) {
        this.invoiceDetailsRepository = invoiceDetailsRepository;
    }

    /**
     * Return a {@link List} of {@link InvoiceDetails} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<InvoiceDetails> findByCriteria(InvoiceDetailsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<InvoiceDetails> specification = createSpecification(criteria);
        return invoiceDetailsRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link InvoiceDetails} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InvoiceDetails> findByCriteria(InvoiceDetailsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<InvoiceDetails> specification = createSpecification(criteria);
        return invoiceDetailsRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InvoiceDetailsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<InvoiceDetails> specification = createSpecification(criteria);
        return invoiceDetailsRepository.count(specification);
    }

    /**
     * Function to convert {@link InvoiceDetailsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<InvoiceDetails> createSpecification(InvoiceDetailsCriteria criteria) {
        Specification<InvoiceDetails> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), InvoiceDetails_.id));
            }
            if (criteria.getInvQty() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getInvQty(), InvoiceDetails_.invQty));
            }
            if (criteria.getRevisedItemSalesPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRevisedItemSalesPrice(), InvoiceDetails_.revisedItemSalesPrice));
            }
            if (criteria.getItemId() != null) {
                specification = specification.and(buildSpecification(criteria.getItemId(),
                    root -> root.join(InvoiceDetails_.item, JoinType.LEFT).get(Items_.id)));
            }
            if (criteria.getInvId() != null) {
                specification = specification.and(buildSpecification(criteria.getInvId(),
                    root -> root.join(InvoiceDetails_.inv, JoinType.LEFT).get(Invoice_.id)));
            }
        }
        return specification;
    }
}

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

import com.alphadevs.sales.domain.Invoice;
import com.alphadevs.sales.domain.*; // for static metamodels
import com.alphadevs.sales.repository.InvoiceRepository;
import com.alphadevs.sales.service.dto.InvoiceCriteria;

/**
 * Service for executing complex queries for {@link Invoice} entities in the database.
 * The main input is a {@link InvoiceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Invoice} or a {@link Page} of {@link Invoice} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InvoiceQueryService extends QueryService<Invoice> {

    private final Logger log = LoggerFactory.getLogger(InvoiceQueryService.class);

    private final InvoiceRepository invoiceRepository;

    public InvoiceQueryService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    /**
     * Return a {@link List} of {@link Invoice} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Invoice> findByCriteria(InvoiceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Invoice> specification = createSpecification(criteria);
        return invoiceRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Invoice} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Invoice> findByCriteria(InvoiceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Invoice> specification = createSpecification(criteria);
        return invoiceRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InvoiceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Invoice> specification = createSpecification(criteria);
        return invoiceRepository.count(specification);
    }

    /**
     * Function to convert {@link InvoiceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Invoice> createSpecification(InvoiceCriteria criteria) {
        Specification<Invoice> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Invoice_.id));
            }
            if (criteria.getInvNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getInvNumber(), Invoice_.invNumber));
            }
            if (criteria.getInvDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getInvDate(), Invoice_.invDate));
            }
            if (criteria.getInvAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getInvAmount(), Invoice_.invAmount));
            }
            if (criteria.getHistoryId() != null) {
                specification = specification.and(buildSpecification(criteria.getHistoryId(),
                    root -> root.join(Invoice_.history, JoinType.LEFT).get(DocumentHistory_.id)));
            }
            if (criteria.getDetailsId() != null) {
                specification = specification.and(buildSpecification(criteria.getDetailsId(),
                    root -> root.join(Invoice_.details, JoinType.LEFT).get(InvoiceDetails_.id)));
            }
            if (criteria.getPayTypeId() != null) {
                specification = specification.and(buildSpecification(criteria.getPayTypeId(),
                    root -> root.join(Invoice_.payTypes, JoinType.LEFT).get(PaymentTypes_.id)));
            }
            if (criteria.getPayTypeId() != null) {
                specification = specification.and(buildSpecification(criteria.getPayTypeId(),
                    root -> root.join(Invoice_.payType, JoinType.LEFT).get(PaymentTypes_.id)));
            }
            if (criteria.getCustomerId() != null) {
                specification = specification.and(buildSpecification(criteria.getCustomerId(),
                    root -> root.join(Invoice_.customer, JoinType.LEFT).get(Customer_.id)));
            }
            if (criteria.getTransactionTypeId() != null) {
                specification = specification.and(buildSpecification(criteria.getTransactionTypeId(),
                    root -> root.join(Invoice_.transactionType, JoinType.LEFT).get(TransactionType_.id)));
            }
            if (criteria.getLocationId() != null) {
                specification = specification.and(buildSpecification(criteria.getLocationId(),
                    root -> root.join(Invoice_.location, JoinType.LEFT).get(Location_.id)));
            }
        }
        return specification;
    }
}

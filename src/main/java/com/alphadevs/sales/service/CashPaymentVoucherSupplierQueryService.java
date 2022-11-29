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

import com.alphadevs.sales.domain.CashPaymentVoucherSupplier;
import com.alphadevs.sales.domain.*; // for static metamodels
import com.alphadevs.sales.repository.CashPaymentVoucherSupplierRepository;
import com.alphadevs.sales.service.dto.CashPaymentVoucherSupplierCriteria;

/**
 * Service for executing complex queries for {@link CashPaymentVoucherSupplier} entities in the database.
 * The main input is a {@link CashPaymentVoucherSupplierCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CashPaymentVoucherSupplier} or a {@link Page} of {@link CashPaymentVoucherSupplier} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CashPaymentVoucherSupplierQueryService extends QueryService<CashPaymentVoucherSupplier> {

    private final Logger log = LoggerFactory.getLogger(CashPaymentVoucherSupplierQueryService.class);

    private final CashPaymentVoucherSupplierRepository cashPaymentVoucherSupplierRepository;

    public CashPaymentVoucherSupplierQueryService(CashPaymentVoucherSupplierRepository cashPaymentVoucherSupplierRepository) {
        this.cashPaymentVoucherSupplierRepository = cashPaymentVoucherSupplierRepository;
    }

    /**
     * Return a {@link List} of {@link CashPaymentVoucherSupplier} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CashPaymentVoucherSupplier> findByCriteria(CashPaymentVoucherSupplierCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CashPaymentVoucherSupplier> specification = createSpecification(criteria);
        return cashPaymentVoucherSupplierRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link CashPaymentVoucherSupplier} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CashPaymentVoucherSupplier> findByCriteria(CashPaymentVoucherSupplierCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CashPaymentVoucherSupplier> specification = createSpecification(criteria);
        return cashPaymentVoucherSupplierRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CashPaymentVoucherSupplierCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CashPaymentVoucherSupplier> specification = createSpecification(criteria);
        return cashPaymentVoucherSupplierRepository.count(specification);
    }

    /**
     * Function to convert {@link CashPaymentVoucherSupplierCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CashPaymentVoucherSupplier> createSpecification(CashPaymentVoucherSupplierCriteria criteria) {
        Specification<CashPaymentVoucherSupplier> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CashPaymentVoucherSupplier_.id));
            }
            if (criteria.getTransactionNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTransactionNumber(), CashPaymentVoucherSupplier_.transactionNumber));
            }
            if (criteria.getTransactionDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTransactionDate(), CashPaymentVoucherSupplier_.transactionDate));
            }
            if (criteria.getTransactionDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTransactionDescription(), CashPaymentVoucherSupplier_.transactionDescription));
            }
            if (criteria.getTransactionAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTransactionAmount(), CashPaymentVoucherSupplier_.transactionAmount));
            }
            if (criteria.getHistoryId() != null) {
                specification = specification.and(buildSpecification(criteria.getHistoryId(),
                    root -> root.join(CashPaymentVoucherSupplier_.history, JoinType.LEFT).get(DocumentHistory_.id)));
            }
            if (criteria.getLocationId() != null) {
                specification = specification.and(buildSpecification(criteria.getLocationId(),
                    root -> root.join(CashPaymentVoucherSupplier_.location, JoinType.LEFT).get(Location_.id)));
            }
            if (criteria.getTransactionTypeId() != null) {
                specification = specification.and(buildSpecification(criteria.getTransactionTypeId(),
                    root -> root.join(CashPaymentVoucherSupplier_.transactionType, JoinType.LEFT).get(TransactionType_.id)));
            }
            if (criteria.getSupplierId() != null) {
                specification = specification.and(buildSpecification(criteria.getSupplierId(),
                    root -> root.join(CashPaymentVoucherSupplier_.supplier, JoinType.LEFT).get(Supplier_.id)));
            }
        }
        return specification;
    }
}

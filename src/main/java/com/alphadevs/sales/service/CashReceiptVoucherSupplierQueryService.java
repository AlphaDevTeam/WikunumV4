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

import com.alphadevs.sales.domain.CashReceiptVoucherSupplier;
import com.alphadevs.sales.domain.*; // for static metamodels
import com.alphadevs.sales.repository.CashReceiptVoucherSupplierRepository;
import com.alphadevs.sales.service.dto.CashReceiptVoucherSupplierCriteria;

/**
 * Service for executing complex queries for {@link CashReceiptVoucherSupplier} entities in the database.
 * The main input is a {@link CashReceiptVoucherSupplierCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CashReceiptVoucherSupplier} or a {@link Page} of {@link CashReceiptVoucherSupplier} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CashReceiptVoucherSupplierQueryService extends QueryService<CashReceiptVoucherSupplier> {

    private final Logger log = LoggerFactory.getLogger(CashReceiptVoucherSupplierQueryService.class);

    private final CashReceiptVoucherSupplierRepository cashReceiptVoucherSupplierRepository;

    public CashReceiptVoucherSupplierQueryService(CashReceiptVoucherSupplierRepository cashReceiptVoucherSupplierRepository) {
        this.cashReceiptVoucherSupplierRepository = cashReceiptVoucherSupplierRepository;
    }

    /**
     * Return a {@link List} of {@link CashReceiptVoucherSupplier} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CashReceiptVoucherSupplier> findByCriteria(CashReceiptVoucherSupplierCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CashReceiptVoucherSupplier> specification = createSpecification(criteria);
        return cashReceiptVoucherSupplierRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link CashReceiptVoucherSupplier} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CashReceiptVoucherSupplier> findByCriteria(CashReceiptVoucherSupplierCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CashReceiptVoucherSupplier> specification = createSpecification(criteria);
        return cashReceiptVoucherSupplierRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CashReceiptVoucherSupplierCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CashReceiptVoucherSupplier> specification = createSpecification(criteria);
        return cashReceiptVoucherSupplierRepository.count(specification);
    }

    /**
     * Function to convert {@link CashReceiptVoucherSupplierCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CashReceiptVoucherSupplier> createSpecification(CashReceiptVoucherSupplierCriteria criteria) {
        Specification<CashReceiptVoucherSupplier> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CashReceiptVoucherSupplier_.id));
            }
            if (criteria.getTransactionNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTransactionNumber(), CashReceiptVoucherSupplier_.transactionNumber));
            }
            if (criteria.getTransactionDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTransactionDate(), CashReceiptVoucherSupplier_.transactionDate));
            }
            if (criteria.getTransactionDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTransactionDescription(), CashReceiptVoucherSupplier_.transactionDescription));
            }
            if (criteria.getTransactionAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTransactionAmount(), CashReceiptVoucherSupplier_.transactionAmount));
            }
            if (criteria.getLocationId() != null) {
                specification = specification.and(buildSpecification(criteria.getLocationId(),
                    root -> root.join(CashReceiptVoucherSupplier_.location, JoinType.LEFT).get(Location_.id)));
            }
            if (criteria.getTransactionTypeId() != null) {
                specification = specification.and(buildSpecification(criteria.getTransactionTypeId(),
                    root -> root.join(CashReceiptVoucherSupplier_.transactionType, JoinType.LEFT).get(TransactionType_.id)));
            }
            if (criteria.getSupplierId() != null) {
                specification = specification.and(buildSpecification(criteria.getSupplierId(),
                    root -> root.join(CashReceiptVoucherSupplier_.supplier, JoinType.LEFT).get(Supplier_.id)));
            }
        }
        return specification;
    }
}

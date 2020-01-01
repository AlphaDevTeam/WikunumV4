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

import com.alphadevs.sales.domain.CashReceiptVoucherCustomer;
import com.alphadevs.sales.domain.*; // for static metamodels
import com.alphadevs.sales.repository.CashReceiptVoucherCustomerRepository;
import com.alphadevs.sales.service.dto.CashReceiptVoucherCustomerCriteria;

/**
 * Service for executing complex queries for {@link CashReceiptVoucherCustomer} entities in the database.
 * The main input is a {@link CashReceiptVoucherCustomerCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CashReceiptVoucherCustomer} or a {@link Page} of {@link CashReceiptVoucherCustomer} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CashReceiptVoucherCustomerQueryService extends QueryService<CashReceiptVoucherCustomer> {

    private final Logger log = LoggerFactory.getLogger(CashReceiptVoucherCustomerQueryService.class);

    private final CashReceiptVoucherCustomerRepository cashReceiptVoucherCustomerRepository;

    public CashReceiptVoucherCustomerQueryService(CashReceiptVoucherCustomerRepository cashReceiptVoucherCustomerRepository) {
        this.cashReceiptVoucherCustomerRepository = cashReceiptVoucherCustomerRepository;
    }

    /**
     * Return a {@link List} of {@link CashReceiptVoucherCustomer} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CashReceiptVoucherCustomer> findByCriteria(CashReceiptVoucherCustomerCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CashReceiptVoucherCustomer> specification = createSpecification(criteria);
        return cashReceiptVoucherCustomerRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link CashReceiptVoucherCustomer} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CashReceiptVoucherCustomer> findByCriteria(CashReceiptVoucherCustomerCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CashReceiptVoucherCustomer> specification = createSpecification(criteria);
        return cashReceiptVoucherCustomerRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CashReceiptVoucherCustomerCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CashReceiptVoucherCustomer> specification = createSpecification(criteria);
        return cashReceiptVoucherCustomerRepository.count(specification);
    }

    /**
     * Function to convert {@link CashReceiptVoucherCustomerCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CashReceiptVoucherCustomer> createSpecification(CashReceiptVoucherCustomerCriteria criteria) {
        Specification<CashReceiptVoucherCustomer> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CashReceiptVoucherCustomer_.id));
            }
            if (criteria.getTransactionNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTransactionNumber(), CashReceiptVoucherCustomer_.transactionNumber));
            }
            if (criteria.getTransactionDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTransactionDate(), CashReceiptVoucherCustomer_.transactionDate));
            }
            if (criteria.getTransactionDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTransactionDescription(), CashReceiptVoucherCustomer_.transactionDescription));
            }
            if (criteria.getTransactionAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTransactionAmount(), CashReceiptVoucherCustomer_.transactionAmount));
            }
            if (criteria.getLocationId() != null) {
                specification = specification.and(buildSpecification(criteria.getLocationId(),
                    root -> root.join(CashReceiptVoucherCustomer_.location, JoinType.LEFT).get(Location_.id)));
            }
            if (criteria.getTransactionTypeId() != null) {
                specification = specification.and(buildSpecification(criteria.getTransactionTypeId(),
                    root -> root.join(CashReceiptVoucherCustomer_.transactionType, JoinType.LEFT).get(TransactionType_.id)));
            }
            if (criteria.getCustomerId() != null) {
                specification = specification.and(buildSpecification(criteria.getCustomerId(),
                    root -> root.join(CashReceiptVoucherCustomer_.customer, JoinType.LEFT).get(Customer_.id)));
            }
        }
        return specification;
    }
}

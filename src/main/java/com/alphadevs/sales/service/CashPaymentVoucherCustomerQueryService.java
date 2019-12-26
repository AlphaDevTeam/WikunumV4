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

import com.alphadevs.sales.domain.CashPaymentVoucherCustomer;
import com.alphadevs.sales.domain.*; // for static metamodels
import com.alphadevs.sales.repository.CashPaymentVoucherCustomerRepository;
import com.alphadevs.sales.service.dto.CashPaymentVoucherCustomerCriteria;

/**
 * Service for executing complex queries for {@link CashPaymentVoucherCustomer} entities in the database.
 * The main input is a {@link CashPaymentVoucherCustomerCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CashPaymentVoucherCustomer} or a {@link Page} of {@link CashPaymentVoucherCustomer} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CashPaymentVoucherCustomerQueryService extends QueryService<CashPaymentVoucherCustomer> {

    private final Logger log = LoggerFactory.getLogger(CashPaymentVoucherCustomerQueryService.class);

    private final CashPaymentVoucherCustomerRepository cashPaymentVoucherCustomerRepository;

    public CashPaymentVoucherCustomerQueryService(CashPaymentVoucherCustomerRepository cashPaymentVoucherCustomerRepository) {
        this.cashPaymentVoucherCustomerRepository = cashPaymentVoucherCustomerRepository;
    }

    /**
     * Return a {@link List} of {@link CashPaymentVoucherCustomer} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CashPaymentVoucherCustomer> findByCriteria(CashPaymentVoucherCustomerCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CashPaymentVoucherCustomer> specification = createSpecification(criteria);
        return cashPaymentVoucherCustomerRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link CashPaymentVoucherCustomer} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CashPaymentVoucherCustomer> findByCriteria(CashPaymentVoucherCustomerCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CashPaymentVoucherCustomer> specification = createSpecification(criteria);
        return cashPaymentVoucherCustomerRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CashPaymentVoucherCustomerCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CashPaymentVoucherCustomer> specification = createSpecification(criteria);
        return cashPaymentVoucherCustomerRepository.count(specification);
    }

    /**
     * Function to convert {@link CashPaymentVoucherCustomerCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CashPaymentVoucherCustomer> createSpecification(CashPaymentVoucherCustomerCriteria criteria) {
        Specification<CashPaymentVoucherCustomer> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CashPaymentVoucherCustomer_.id));
            }
            if (criteria.getTransactionNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTransactionNumber(), CashPaymentVoucherCustomer_.transactionNumber));
            }
            if (criteria.getTransactionDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTransactionDate(), CashPaymentVoucherCustomer_.transactionDate));
            }
            if (criteria.getTransactionDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTransactionDescription(), CashPaymentVoucherCustomer_.transactionDescription));
            }
            if (criteria.getTransactionAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTransactionAmount(), CashPaymentVoucherCustomer_.transactionAmount));
            }
            if (criteria.getHistoryId() != null) {
                specification = specification.and(buildSpecification(criteria.getHistoryId(),
                    root -> root.join(CashPaymentVoucherCustomer_.history, JoinType.LEFT).get(DocumentHistory_.id)));
            }
            if (criteria.getLocationId() != null) {
                specification = specification.and(buildSpecification(criteria.getLocationId(),
                    root -> root.join(CashPaymentVoucherCustomer_.location, JoinType.LEFT).get(Location_.id)));
            }
            if (criteria.getTransactionTypeId() != null) {
                specification = specification.and(buildSpecification(criteria.getTransactionTypeId(),
                    root -> root.join(CashPaymentVoucherCustomer_.transactionType, JoinType.LEFT).get(TransactionType_.id)));
            }
            if (criteria.getCustomerId() != null) {
                specification = specification.and(buildSpecification(criteria.getCustomerId(),
                    root -> root.join(CashPaymentVoucherCustomer_.customer, JoinType.LEFT).get(Customer_.id)));
            }
        }
        return specification;
    }
}

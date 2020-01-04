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

import com.alphadevs.sales.domain.CashPaymentVoucherExpense;
import com.alphadevs.sales.domain.*; // for static metamodels
import com.alphadevs.sales.repository.CashPaymentVoucherExpenseRepository;
import com.alphadevs.sales.service.dto.CashPaymentVoucherExpenseCriteria;

/**
 * Service for executing complex queries for {@link CashPaymentVoucherExpense} entities in the database.
 * The main input is a {@link CashPaymentVoucherExpenseCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CashPaymentVoucherExpense} or a {@link Page} of {@link CashPaymentVoucherExpense} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CashPaymentVoucherExpenseQueryService extends QueryService<CashPaymentVoucherExpense> {

    private final Logger log = LoggerFactory.getLogger(CashPaymentVoucherExpenseQueryService.class);

    private final CashPaymentVoucherExpenseRepository cashPaymentVoucherExpenseRepository;

    public CashPaymentVoucherExpenseQueryService(CashPaymentVoucherExpenseRepository cashPaymentVoucherExpenseRepository) {
        this.cashPaymentVoucherExpenseRepository = cashPaymentVoucherExpenseRepository;
    }

    /**
     * Return a {@link List} of {@link CashPaymentVoucherExpense} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CashPaymentVoucherExpense> findByCriteria(CashPaymentVoucherExpenseCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CashPaymentVoucherExpense> specification = createSpecification(criteria);
        return cashPaymentVoucherExpenseRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link CashPaymentVoucherExpense} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CashPaymentVoucherExpense> findByCriteria(CashPaymentVoucherExpenseCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CashPaymentVoucherExpense> specification = createSpecification(criteria);
        return cashPaymentVoucherExpenseRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CashPaymentVoucherExpenseCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CashPaymentVoucherExpense> specification = createSpecification(criteria);
        return cashPaymentVoucherExpenseRepository.count(specification);
    }

    /**
     * Function to convert {@link CashPaymentVoucherExpenseCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CashPaymentVoucherExpense> createSpecification(CashPaymentVoucherExpenseCriteria criteria) {
        Specification<CashPaymentVoucherExpense> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CashPaymentVoucherExpense_.id));
            }
            if (criteria.getTransactionNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTransactionNumber(), CashPaymentVoucherExpense_.transactionNumber));
            }
            if (criteria.getTransactionDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTransactionDate(), CashPaymentVoucherExpense_.transactionDate));
            }
            if (criteria.getTransactionDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTransactionDescription(), CashPaymentVoucherExpense_.transactionDescription));
            }
            if (criteria.getTransactionAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTransactionAmount(), CashPaymentVoucherExpense_.transactionAmount));
            }
            if (criteria.getLocationId() != null) {
                specification = specification.and(buildSpecification(criteria.getLocationId(),
                    root -> root.join(CashPaymentVoucherExpense_.location, JoinType.LEFT).get(Location_.id)));
            }
            if (criteria.getTransactionTypeId() != null) {
                specification = specification.and(buildSpecification(criteria.getTransactionTypeId(),
                    root -> root.join(CashPaymentVoucherExpense_.transactionType, JoinType.LEFT).get(TransactionType_.id)));
            }
            if (criteria.getExpenseId() != null) {
                specification = specification.and(buildSpecification(criteria.getExpenseId(),
                    root -> root.join(CashPaymentVoucherExpense_.expense, JoinType.LEFT).get(Expense_.id)));
            }
        }
        return specification;
    }
}

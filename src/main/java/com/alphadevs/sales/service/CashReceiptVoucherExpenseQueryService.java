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

import com.alphadevs.sales.domain.CashReceiptVoucherExpense;
import com.alphadevs.sales.domain.*; // for static metamodels
import com.alphadevs.sales.repository.CashReceiptVoucherExpenseRepository;
import com.alphadevs.sales.service.dto.CashReceiptVoucherExpenseCriteria;

/**
 * Service for executing complex queries for {@link CashReceiptVoucherExpense} entities in the database.
 * The main input is a {@link CashReceiptVoucherExpenseCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CashReceiptVoucherExpense} or a {@link Page} of {@link CashReceiptVoucherExpense} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CashReceiptVoucherExpenseQueryService extends QueryService<CashReceiptVoucherExpense> {

    private final Logger log = LoggerFactory.getLogger(CashReceiptVoucherExpenseQueryService.class);

    private final CashReceiptVoucherExpenseRepository cashReceiptVoucherExpenseRepository;

    public CashReceiptVoucherExpenseQueryService(CashReceiptVoucherExpenseRepository cashReceiptVoucherExpenseRepository) {
        this.cashReceiptVoucherExpenseRepository = cashReceiptVoucherExpenseRepository;
    }

    /**
     * Return a {@link List} of {@link CashReceiptVoucherExpense} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CashReceiptVoucherExpense> findByCriteria(CashReceiptVoucherExpenseCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CashReceiptVoucherExpense> specification = createSpecification(criteria);
        return cashReceiptVoucherExpenseRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link CashReceiptVoucherExpense} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CashReceiptVoucherExpense> findByCriteria(CashReceiptVoucherExpenseCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CashReceiptVoucherExpense> specification = createSpecification(criteria);
        return cashReceiptVoucherExpenseRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CashReceiptVoucherExpenseCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CashReceiptVoucherExpense> specification = createSpecification(criteria);
        return cashReceiptVoucherExpenseRepository.count(specification);
    }

    /**
     * Function to convert {@link CashReceiptVoucherExpenseCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CashReceiptVoucherExpense> createSpecification(CashReceiptVoucherExpenseCriteria criteria) {
        Specification<CashReceiptVoucherExpense> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CashReceiptVoucherExpense_.id));
            }
            if (criteria.getTransactionNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTransactionNumber(), CashReceiptVoucherExpense_.transactionNumber));
            }
            if (criteria.getTransactionDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTransactionDate(), CashReceiptVoucherExpense_.transactionDate));
            }
            if (criteria.getTransactionDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTransactionDescription(), CashReceiptVoucherExpense_.transactionDescription));
            }
            if (criteria.getTransactionAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTransactionAmount(), CashReceiptVoucherExpense_.transactionAmount));
            }
            if (criteria.getLocationId() != null) {
                specification = specification.and(buildSpecification(criteria.getLocationId(),
                    root -> root.join(CashReceiptVoucherExpense_.location, JoinType.LEFT).get(Location_.id)));
            }
            if (criteria.getTransactionTypeId() != null) {
                specification = specification.and(buildSpecification(criteria.getTransactionTypeId(),
                    root -> root.join(CashReceiptVoucherExpense_.transactionType, JoinType.LEFT).get(TransactionType_.id)));
            }
            if (criteria.getExpenseId() != null) {
                specification = specification.and(buildSpecification(criteria.getExpenseId(),
                    root -> root.join(CashReceiptVoucherExpense_.expense, JoinType.LEFT).get(Expense_.id)));
            }
        }
        return specification;
    }
}

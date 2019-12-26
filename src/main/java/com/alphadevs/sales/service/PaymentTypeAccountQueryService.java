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

import com.alphadevs.sales.domain.PaymentTypeAccount;
import com.alphadevs.sales.domain.*; // for static metamodels
import com.alphadevs.sales.repository.PaymentTypeAccountRepository;
import com.alphadevs.sales.service.dto.PaymentTypeAccountCriteria;

/**
 * Service for executing complex queries for {@link PaymentTypeAccount} entities in the database.
 * The main input is a {@link PaymentTypeAccountCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PaymentTypeAccount} or a {@link Page} of {@link PaymentTypeAccount} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PaymentTypeAccountQueryService extends QueryService<PaymentTypeAccount> {

    private final Logger log = LoggerFactory.getLogger(PaymentTypeAccountQueryService.class);

    private final PaymentTypeAccountRepository paymentTypeAccountRepository;

    public PaymentTypeAccountQueryService(PaymentTypeAccountRepository paymentTypeAccountRepository) {
        this.paymentTypeAccountRepository = paymentTypeAccountRepository;
    }

    /**
     * Return a {@link List} of {@link PaymentTypeAccount} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PaymentTypeAccount> findByCriteria(PaymentTypeAccountCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PaymentTypeAccount> specification = createSpecification(criteria);
        return paymentTypeAccountRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link PaymentTypeAccount} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PaymentTypeAccount> findByCriteria(PaymentTypeAccountCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PaymentTypeAccount> specification = createSpecification(criteria);
        return paymentTypeAccountRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PaymentTypeAccountCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PaymentTypeAccount> specification = createSpecification(criteria);
        return paymentTypeAccountRepository.count(specification);
    }

    /**
     * Function to convert {@link PaymentTypeAccountCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PaymentTypeAccount> createSpecification(PaymentTypeAccountCriteria criteria) {
        Specification<PaymentTypeAccount> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PaymentTypeAccount_.id));
            }
            if (criteria.getTransactionDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTransactionDate(), PaymentTypeAccount_.transactionDate));
            }
            if (criteria.getTransactionDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTransactionDescription(), PaymentTypeAccount_.transactionDescription));
            }
            if (criteria.getTransactionAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTransactionAmount(), PaymentTypeAccount_.transactionAmount));
            }
            if (criteria.getTransactionBalance() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTransactionBalance(), PaymentTypeAccount_.transactionBalance));
            }
            if (criteria.getLocationId() != null) {
                specification = specification.and(buildSpecification(criteria.getLocationId(),
                    root -> root.join(PaymentTypeAccount_.location, JoinType.LEFT).get(Location_.id)));
            }
            if (criteria.getTransactionTypeId() != null) {
                specification = specification.and(buildSpecification(criteria.getTransactionTypeId(),
                    root -> root.join(PaymentTypeAccount_.transactionType, JoinType.LEFT).get(TransactionType_.id)));
            }
            if (criteria.getPayTypeId() != null) {
                specification = specification.and(buildSpecification(criteria.getPayTypeId(),
                    root -> root.join(PaymentTypeAccount_.payType, JoinType.LEFT).get(PaymentTypes_.id)));
            }
        }
        return specification;
    }
}

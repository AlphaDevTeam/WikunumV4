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

import com.alphadevs.sales.domain.PaymentTypeBalance;
import com.alphadevs.sales.domain.*; // for static metamodels
import com.alphadevs.sales.repository.PaymentTypeBalanceRepository;
import com.alphadevs.sales.service.dto.PaymentTypeBalanceCriteria;

/**
 * Service for executing complex queries for {@link PaymentTypeBalance} entities in the database.
 * The main input is a {@link PaymentTypeBalanceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PaymentTypeBalance} or a {@link Page} of {@link PaymentTypeBalance} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PaymentTypeBalanceQueryService extends QueryService<PaymentTypeBalance> {

    private final Logger log = LoggerFactory.getLogger(PaymentTypeBalanceQueryService.class);

    private final PaymentTypeBalanceRepository paymentTypeBalanceRepository;

    public PaymentTypeBalanceQueryService(PaymentTypeBalanceRepository paymentTypeBalanceRepository) {
        this.paymentTypeBalanceRepository = paymentTypeBalanceRepository;
    }

    /**
     * Return a {@link List} of {@link PaymentTypeBalance} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PaymentTypeBalance> findByCriteria(PaymentTypeBalanceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PaymentTypeBalance> specification = createSpecification(criteria);
        return paymentTypeBalanceRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link PaymentTypeBalance} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PaymentTypeBalance> findByCriteria(PaymentTypeBalanceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PaymentTypeBalance> specification = createSpecification(criteria);
        return paymentTypeBalanceRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PaymentTypeBalanceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PaymentTypeBalance> specification = createSpecification(criteria);
        return paymentTypeBalanceRepository.count(specification);
    }

    /**
     * Function to convert {@link PaymentTypeBalanceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PaymentTypeBalance> createSpecification(PaymentTypeBalanceCriteria criteria) {
        Specification<PaymentTypeBalance> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PaymentTypeBalance_.id));
            }
            if (criteria.getBalance() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBalance(), PaymentTypeBalance_.balance));
            }
            if (criteria.getLocationId() != null) {
                specification = specification.and(buildSpecification(criteria.getLocationId(),
                    root -> root.join(PaymentTypeBalance_.location, JoinType.LEFT).get(Location_.id)));
            }
            if (criteria.getTransactionTypeId() != null) {
                specification = specification.and(buildSpecification(criteria.getTransactionTypeId(),
                    root -> root.join(PaymentTypeBalance_.transactionType, JoinType.LEFT).get(TransactionType_.id)));
            }
            if (criteria.getPayTypeId() != null) {
                specification = specification.and(buildSpecification(criteria.getPayTypeId(),
                    root -> root.join(PaymentTypeBalance_.payType, JoinType.LEFT).get(PaymentTypes_.id)));
            }
        }
        return specification;
    }
}

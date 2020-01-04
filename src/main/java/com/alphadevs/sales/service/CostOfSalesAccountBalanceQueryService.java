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

import com.alphadevs.sales.domain.CostOfSalesAccountBalance;
import com.alphadevs.sales.domain.*; // for static metamodels
import com.alphadevs.sales.repository.CostOfSalesAccountBalanceRepository;
import com.alphadevs.sales.service.dto.CostOfSalesAccountBalanceCriteria;

/**
 * Service for executing complex queries for {@link CostOfSalesAccountBalance} entities in the database.
 * The main input is a {@link CostOfSalesAccountBalanceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CostOfSalesAccountBalance} or a {@link Page} of {@link CostOfSalesAccountBalance} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CostOfSalesAccountBalanceQueryService extends QueryService<CostOfSalesAccountBalance> {

    private final Logger log = LoggerFactory.getLogger(CostOfSalesAccountBalanceQueryService.class);

    private final CostOfSalesAccountBalanceRepository costOfSalesAccountBalanceRepository;

    public CostOfSalesAccountBalanceQueryService(CostOfSalesAccountBalanceRepository costOfSalesAccountBalanceRepository) {
        this.costOfSalesAccountBalanceRepository = costOfSalesAccountBalanceRepository;
    }

    /**
     * Return a {@link List} of {@link CostOfSalesAccountBalance} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CostOfSalesAccountBalance> findByCriteria(CostOfSalesAccountBalanceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CostOfSalesAccountBalance> specification = createSpecification(criteria);
        return costOfSalesAccountBalanceRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link CostOfSalesAccountBalance} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CostOfSalesAccountBalance> findByCriteria(CostOfSalesAccountBalanceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CostOfSalesAccountBalance> specification = createSpecification(criteria);
        return costOfSalesAccountBalanceRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CostOfSalesAccountBalanceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CostOfSalesAccountBalance> specification = createSpecification(criteria);
        return costOfSalesAccountBalanceRepository.count(specification);
    }

    /**
     * Function to convert {@link CostOfSalesAccountBalanceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CostOfSalesAccountBalance> createSpecification(CostOfSalesAccountBalanceCriteria criteria) {
        Specification<CostOfSalesAccountBalance> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CostOfSalesAccountBalance_.id));
            }
            if (criteria.getBalance() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBalance(), CostOfSalesAccountBalance_.balance));
            }
            if (criteria.getLocationId() != null) {
                specification = specification.and(buildSpecification(criteria.getLocationId(),
                    root -> root.join(CostOfSalesAccountBalance_.location, JoinType.LEFT).get(Location_.id)));
            }
            if (criteria.getTransactionTypeId() != null) {
                specification = specification.and(buildSpecification(criteria.getTransactionTypeId(),
                    root -> root.join(CostOfSalesAccountBalance_.transactionType, JoinType.LEFT).get(TransactionType_.id)));
            }
        }
        return specification;
    }
}

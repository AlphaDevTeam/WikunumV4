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

import com.alphadevs.sales.domain.CostOfSalesAccount;
import com.alphadevs.sales.domain.*; // for static metamodels
import com.alphadevs.sales.repository.CostOfSalesAccountRepository;
import com.alphadevs.sales.service.dto.CostOfSalesAccountCriteria;

/**
 * Service for executing complex queries for {@link CostOfSalesAccount} entities in the database.
 * The main input is a {@link CostOfSalesAccountCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CostOfSalesAccount} or a {@link Page} of {@link CostOfSalesAccount} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CostOfSalesAccountQueryService extends QueryService<CostOfSalesAccount> {

    private final Logger log = LoggerFactory.getLogger(CostOfSalesAccountQueryService.class);

    private final CostOfSalesAccountRepository costOfSalesAccountRepository;

    public CostOfSalesAccountQueryService(CostOfSalesAccountRepository costOfSalesAccountRepository) {
        this.costOfSalesAccountRepository = costOfSalesAccountRepository;
    }

    /**
     * Return a {@link List} of {@link CostOfSalesAccount} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CostOfSalesAccount> findByCriteria(CostOfSalesAccountCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CostOfSalesAccount> specification = createSpecification(criteria);
        return costOfSalesAccountRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link CostOfSalesAccount} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CostOfSalesAccount> findByCriteria(CostOfSalesAccountCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CostOfSalesAccount> specification = createSpecification(criteria);
        return costOfSalesAccountRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CostOfSalesAccountCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CostOfSalesAccount> specification = createSpecification(criteria);
        return costOfSalesAccountRepository.count(specification);
    }

    /**
     * Function to convert {@link CostOfSalesAccountCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CostOfSalesAccount> createSpecification(CostOfSalesAccountCriteria criteria) {
        Specification<CostOfSalesAccount> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CostOfSalesAccount_.id));
            }
            if (criteria.getTransactionDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTransactionDate(), CostOfSalesAccount_.transactionDate));
            }
            if (criteria.getTransactionDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTransactionDescription(), CostOfSalesAccount_.transactionDescription));
            }
            if (criteria.getTransactionAmountDR() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTransactionAmountDR(), CostOfSalesAccount_.transactionAmountDR));
            }
            if (criteria.getTransactionAmountCR() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTransactionAmountCR(), CostOfSalesAccount_.transactionAmountCR));
            }
            if (criteria.getTransactionBalance() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTransactionBalance(), CostOfSalesAccount_.transactionBalance));
            }
            if (criteria.getLocationId() != null) {
                specification = specification.and(buildSpecification(criteria.getLocationId(),
                    root -> root.join(CostOfSalesAccount_.location, JoinType.LEFT).get(Location_.id)));
            }
            if (criteria.getTransactionTypeId() != null) {
                specification = specification.and(buildSpecification(criteria.getTransactionTypeId(),
                    root -> root.join(CostOfSalesAccount_.transactionType, JoinType.LEFT).get(TransactionType_.id)));
            }
        }
        return specification;
    }
}

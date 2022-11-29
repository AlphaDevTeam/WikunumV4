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

import com.alphadevs.sales.domain.StockTransfer;
import com.alphadevs.sales.domain.*; // for static metamodels
import com.alphadevs.sales.repository.StockTransferRepository;
import com.alphadevs.sales.service.dto.StockTransferCriteria;

/**
 * Service for executing complex queries for {@link StockTransfer} entities in the database.
 * The main input is a {@link StockTransferCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link StockTransfer} or a {@link Page} of {@link StockTransfer} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class StockTransferQueryService extends QueryService<StockTransfer> {

    private final Logger log = LoggerFactory.getLogger(StockTransferQueryService.class);

    private final StockTransferRepository stockTransferRepository;

    public StockTransferQueryService(StockTransferRepository stockTransferRepository) {
        this.stockTransferRepository = stockTransferRepository;
    }

    /**
     * Return a {@link List} of {@link StockTransfer} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<StockTransfer> findByCriteria(StockTransferCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<StockTransfer> specification = createSpecification(criteria);
        return stockTransferRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link StockTransfer} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<StockTransfer> findByCriteria(StockTransferCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<StockTransfer> specification = createSpecification(criteria);
        return stockTransferRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(StockTransferCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<StockTransfer> specification = createSpecification(criteria);
        return stockTransferRepository.count(specification);
    }

    /**
     * Function to convert {@link StockTransferCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<StockTransfer> createSpecification(StockTransferCriteria criteria) {
        Specification<StockTransfer> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), StockTransfer_.id));
            }
            if (criteria.getTransactionNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTransactionNumber(), StockTransfer_.transactionNumber));
            }
            if (criteria.getTransactionDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTransactionDate(), StockTransfer_.transactionDate));
            }
            if (criteria.getTransactionDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTransactionDescription(), StockTransfer_.transactionDescription));
            }
            if (criteria.getTransactionQty() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTransactionQty(), StockTransfer_.transactionQty));
            }
            if (criteria.getItemId() != null) {
                specification = specification.and(buildSpecification(criteria.getItemId(),
                    root -> root.join(StockTransfer_.item, JoinType.LEFT).get(Items_.id)));
            }
            if (criteria.getLocationFromId() != null) {
                specification = specification.and(buildSpecification(criteria.getLocationFromId(),
                    root -> root.join(StockTransfer_.locationFrom, JoinType.LEFT).get(Location_.id)));
            }
            if (criteria.getLocationToId() != null) {
                specification = specification.and(buildSpecification(criteria.getLocationToId(),
                    root -> root.join(StockTransfer_.locationTo, JoinType.LEFT).get(Location_.id)));
            }
        }
        return specification;
    }
}

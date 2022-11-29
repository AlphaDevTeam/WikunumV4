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

import com.alphadevs.sales.domain.Items;
import com.alphadevs.sales.domain.*; // for static metamodels
import com.alphadevs.sales.repository.ItemsRepository;
import com.alphadevs.sales.service.dto.ItemsCriteria;

/**
 * Service for executing complex queries for {@link Items} entities in the database.
 * The main input is a {@link ItemsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Items} or a {@link Page} of {@link Items} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ItemsQueryService extends QueryService<Items> {

    private final Logger log = LoggerFactory.getLogger(ItemsQueryService.class);

    private final ItemsRepository itemsRepository;

    public ItemsQueryService(ItemsRepository itemsRepository) {
        this.itemsRepository = itemsRepository;
    }

    /**
     * Return a {@link List} of {@link Items} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Items> findByCriteria(ItemsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Items> specification = createSpecification(criteria);
        return itemsRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Items} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Items> findByCriteria(ItemsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Items> specification = createSpecification(criteria);
        return itemsRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ItemsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Items> specification = createSpecification(criteria);
        return itemsRepository.count(specification);
    }

    /**
     * Function to convert {@link ItemsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Items> createSpecification(ItemsCriteria criteria) {
        Specification<Items> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Items_.id));
            }
            if (criteria.getItemCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getItemCode(), Items_.itemCode));
            }
            if (criteria.getItemName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getItemName(), Items_.itemName));
            }
            if (criteria.getItemDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getItemDescription(), Items_.itemDescription));
            }
            if (criteria.getItemPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getItemPrice(), Items_.itemPrice));
            }
            if (criteria.getItemSerial() != null) {
                specification = specification.and(buildStringSpecification(criteria.getItemSerial(), Items_.itemSerial));
            }
            if (criteria.getItemSupplierSerial() != null) {
                specification = specification.and(buildStringSpecification(criteria.getItemSupplierSerial(), Items_.itemSupplierSerial));
            }
            if (criteria.getItemPromotionalPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getItemPromotionalPrice(), Items_.itemPromotionalPrice));
            }
            if (criteria.getItemPromotionalPercentage() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getItemPromotionalPercentage(), Items_.itemPromotionalPercentage));
            }
            if (criteria.getItemCost() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getItemCost(), Items_.itemCost));
            }
            if (criteria.getOriginalStockDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOriginalStockDate(), Items_.originalStockDate));
            }
            if (criteria.getModifiedStockDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getModifiedStockDate(), Items_.modifiedStockDate));
            }
            if (criteria.getHistoryId() != null) {
                specification = specification.and(buildSpecification(criteria.getHistoryId(),
                    root -> root.join(Items_.history, JoinType.LEFT).get(DocumentHistory_.id)));
            }
            if (criteria.getRelatedModelId() != null) {
                specification = specification.and(buildSpecification(criteria.getRelatedModelId(),
                    root -> root.join(Items_.relatedModel, JoinType.LEFT).get(Model_.id)));
            }
            if (criteria.getRelatedProductId() != null) {
                specification = specification.and(buildSpecification(criteria.getRelatedProductId(),
                    root -> root.join(Items_.relatedProduct, JoinType.LEFT).get(Products_.id)));
            }
            if (criteria.getLocationId() != null) {
                specification = specification.and(buildSpecification(criteria.getLocationId(),
                    root -> root.join(Items_.location, JoinType.LEFT).get(Location_.id)));
            }
            if (criteria.getUnitOfMeasureId() != null) {
                specification = specification.and(buildSpecification(criteria.getUnitOfMeasureId(),
                    root -> root.join(Items_.unitOfMeasure, JoinType.LEFT).get(UnitOfMeasure_.id)));
            }
            if (criteria.getCurrencyId() != null) {
                specification = specification.and(buildSpecification(criteria.getCurrencyId(),
                    root -> root.join(Items_.currency, JoinType.LEFT).get(Currency_.id)));
            }
            if (criteria.getAddonsId() != null) {
                specification = specification.and(buildSpecification(criteria.getAddonsId(),
                    root -> root.join(Items_.addons, JoinType.LEFT).get(ItemAddOns_.id)));
            }
        }
        return specification;
    }
}

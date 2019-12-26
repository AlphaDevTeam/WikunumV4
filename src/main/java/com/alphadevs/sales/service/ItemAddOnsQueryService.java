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

import com.alphadevs.sales.domain.ItemAddOns;
import com.alphadevs.sales.domain.*; // for static metamodels
import com.alphadevs.sales.repository.ItemAddOnsRepository;
import com.alphadevs.sales.service.dto.ItemAddOnsCriteria;

/**
 * Service for executing complex queries for {@link ItemAddOns} entities in the database.
 * The main input is a {@link ItemAddOnsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ItemAddOns} or a {@link Page} of {@link ItemAddOns} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ItemAddOnsQueryService extends QueryService<ItemAddOns> {

    private final Logger log = LoggerFactory.getLogger(ItemAddOnsQueryService.class);

    private final ItemAddOnsRepository itemAddOnsRepository;

    public ItemAddOnsQueryService(ItemAddOnsRepository itemAddOnsRepository) {
        this.itemAddOnsRepository = itemAddOnsRepository;
    }

    /**
     * Return a {@link List} of {@link ItemAddOns} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ItemAddOns> findByCriteria(ItemAddOnsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ItemAddOns> specification = createSpecification(criteria);
        return itemAddOnsRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ItemAddOns} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ItemAddOns> findByCriteria(ItemAddOnsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ItemAddOns> specification = createSpecification(criteria);
        return itemAddOnsRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ItemAddOnsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ItemAddOns> specification = createSpecification(criteria);
        return itemAddOnsRepository.count(specification);
    }

    /**
     * Function to convert {@link ItemAddOnsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ItemAddOns> createSpecification(ItemAddOnsCriteria criteria) {
        Specification<ItemAddOns> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ItemAddOns_.id));
            }
            if (criteria.getAddonCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddonCode(), ItemAddOns_.addonCode));
            }
            if (criteria.getAddonName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddonName(), ItemAddOns_.addonName));
            }
            if (criteria.getAddonDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddonDescription(), ItemAddOns_.addonDescription));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), ItemAddOns_.isActive));
            }
            if (criteria.getAllowSubstract() != null) {
                specification = specification.and(buildSpecification(criteria.getAllowSubstract(), ItemAddOns_.allowSubstract));
            }
            if (criteria.getAddonPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAddonPrice(), ItemAddOns_.addonPrice));
            }
            if (criteria.getSubstractPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSubstractPrice(), ItemAddOns_.substractPrice));
            }
            if (criteria.getLocationId() != null) {
                specification = specification.and(buildSpecification(criteria.getLocationId(),
                    root -> root.join(ItemAddOns_.location, JoinType.LEFT).get(Location_.id)));
            }
            if (criteria.getItemsId() != null) {
                specification = specification.and(buildSpecification(criteria.getItemsId(),
                    root -> root.join(ItemAddOns_.items, JoinType.LEFT).get(Items_.id)));
            }
        }
        return specification;
    }
}

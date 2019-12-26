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

import com.alphadevs.sales.domain.MenuItems;
import com.alphadevs.sales.domain.*; // for static metamodels
import com.alphadevs.sales.repository.MenuItemsRepository;
import com.alphadevs.sales.service.dto.MenuItemsCriteria;

/**
 * Service for executing complex queries for {@link MenuItems} entities in the database.
 * The main input is a {@link MenuItemsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MenuItems} or a {@link Page} of {@link MenuItems} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MenuItemsQueryService extends QueryService<MenuItems> {

    private final Logger log = LoggerFactory.getLogger(MenuItemsQueryService.class);

    private final MenuItemsRepository menuItemsRepository;

    public MenuItemsQueryService(MenuItemsRepository menuItemsRepository) {
        this.menuItemsRepository = menuItemsRepository;
    }

    /**
     * Return a {@link List} of {@link MenuItems} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MenuItems> findByCriteria(MenuItemsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<MenuItems> specification = createSpecification(criteria);
        return menuItemsRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link MenuItems} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MenuItems> findByCriteria(MenuItemsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MenuItems> specification = createSpecification(criteria);
        return menuItemsRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MenuItemsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<MenuItems> specification = createSpecification(criteria);
        return menuItemsRepository.count(specification);
    }

    /**
     * Function to convert {@link MenuItemsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MenuItems> createSpecification(MenuItemsCriteria criteria) {
        Specification<MenuItems> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), MenuItems_.id));
            }
            if (criteria.getMenuName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMenuName(), MenuItems_.menuName));
            }
            if (criteria.getMenuURL() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMenuURL(), MenuItems_.menuURL));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), MenuItems_.isActive));
            }
            if (criteria.getHistoryId() != null) {
                specification = specification.and(buildSpecification(criteria.getHistoryId(),
                    root -> root.join(MenuItems_.history, JoinType.LEFT).get(DocumentHistory_.id)));
            }
            if (criteria.getUserPermissionId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserPermissionId(),
                    root -> root.join(MenuItems_.userPermissions, JoinType.LEFT).get(UserPermissions_.id)));
            }
        }
        return specification;
    }
}

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

import com.alphadevs.sales.domain.UserPermissions;
import com.alphadevs.sales.domain.*; // for static metamodels
import com.alphadevs.sales.repository.UserPermissionsRepository;
import com.alphadevs.sales.service.dto.UserPermissionsCriteria;

/**
 * Service for executing complex queries for {@link UserPermissions} entities in the database.
 * The main input is a {@link UserPermissionsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UserPermissions} or a {@link Page} of {@link UserPermissions} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UserPermissionsQueryService extends QueryService<UserPermissions> {

    private final Logger log = LoggerFactory.getLogger(UserPermissionsQueryService.class);

    private final UserPermissionsRepository userPermissionsRepository;

    public UserPermissionsQueryService(UserPermissionsRepository userPermissionsRepository) {
        this.userPermissionsRepository = userPermissionsRepository;
    }

    /**
     * Return a {@link List} of {@link UserPermissions} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UserPermissions> findByCriteria(UserPermissionsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<UserPermissions> specification = createSpecification(criteria);
        return userPermissionsRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link UserPermissions} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UserPermissions> findByCriteria(UserPermissionsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<UserPermissions> specification = createSpecification(criteria);
        return userPermissionsRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UserPermissionsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<UserPermissions> specification = createSpecification(criteria);
        return userPermissionsRepository.count(specification);
    }

    /**
     * Function to convert {@link UserPermissionsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<UserPermissions> createSpecification(UserPermissionsCriteria criteria) {
        Specification<UserPermissions> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), UserPermissions_.id));
            }
            if (criteria.getUserPermKey() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUserPermKey(), UserPermissions_.userPermKey));
            }
            if (criteria.getUserPermDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUserPermDescription(), UserPermissions_.userPermDescription));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), UserPermissions_.isActive));
            }
            if (criteria.getMenuItemsId() != null) {
                specification = specification.and(buildSpecification(criteria.getMenuItemsId(),
                    root -> root.join(UserPermissions_.menuItems, JoinType.LEFT).get(MenuItems_.id)));
            }
            if (criteria.getUsersId() != null) {
                specification = specification.and(buildSpecification(criteria.getUsersId(),
                    root -> root.join(UserPermissions_.users, JoinType.LEFT).get(ExUser_.id)));
            }
            if (criteria.getUserGroupId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserGroupId(),
                    root -> root.join(UserPermissions_.userGroups, JoinType.LEFT).get(UserGroup_.id)));
            }
        }
        return specification;
    }
}

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

import com.alphadevs.sales.domain.UserGroup;
import com.alphadevs.sales.domain.*; // for static metamodels
import com.alphadevs.sales.repository.UserGroupRepository;
import com.alphadevs.sales.service.dto.UserGroupCriteria;

/**
 * Service for executing complex queries for {@link UserGroup} entities in the database.
 * The main input is a {@link UserGroupCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UserGroup} or a {@link Page} of {@link UserGroup} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UserGroupQueryService extends QueryService<UserGroup> {

    private final Logger log = LoggerFactory.getLogger(UserGroupQueryService.class);

    private final UserGroupRepository userGroupRepository;

    public UserGroupQueryService(UserGroupRepository userGroupRepository) {
        this.userGroupRepository = userGroupRepository;
    }

    /**
     * Return a {@link List} of {@link UserGroup} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UserGroup> findByCriteria(UserGroupCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<UserGroup> specification = createSpecification(criteria);
        return userGroupRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link UserGroup} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UserGroup> findByCriteria(UserGroupCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<UserGroup> specification = createSpecification(criteria);
        return userGroupRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UserGroupCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<UserGroup> specification = createSpecification(criteria);
        return userGroupRepository.count(specification);
    }

    /**
     * Function to convert {@link UserGroupCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<UserGroup> createSpecification(UserGroupCriteria criteria) {
        Specification<UserGroup> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), UserGroup_.id));
            }
            if (criteria.getGroupName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGroupName(), UserGroup_.groupName));
            }
            if (criteria.getHistoryId() != null) {
                specification = specification.and(buildSpecification(criteria.getHistoryId(),
                    root -> root.join(UserGroup_.history, JoinType.LEFT).get(DocumentHistory_.id)));
            }
            if (criteria.getUserPermissionsId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserPermissionsId(),
                    root -> root.join(UserGroup_.userPermissions, JoinType.LEFT).get(UserPermissions_.id)));
            }
            if (criteria.getUsersId() != null) {
                specification = specification.and(buildSpecification(criteria.getUsersId(),
                    root -> root.join(UserGroup_.users, JoinType.LEFT).get(ExUser_.id)));
            }
        }
        return specification;
    }
}

package com.alphadevs.sales.service;

import com.alphadevs.sales.domain.UserGroup;
import com.alphadevs.sales.repository.UserGroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link UserGroup}.
 */
@Service
@Transactional
public class UserGroupService {

    private final Logger log = LoggerFactory.getLogger(UserGroupService.class);

    private final UserGroupRepository userGroupRepository;

    public UserGroupService(UserGroupRepository userGroupRepository) {
        this.userGroupRepository = userGroupRepository;
    }

    /**
     * Save a userGroup.
     *
     * @param userGroup the entity to save.
     * @return the persisted entity.
     */
    public UserGroup save(UserGroup userGroup) {
        log.debug("Request to save UserGroup : {}", userGroup);
        return userGroupRepository.save(userGroup);
    }

    /**
     * Get all the userGroups.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<UserGroup> findAll(Pageable pageable) {
        log.debug("Request to get all UserGroups");
        return userGroupRepository.findAll(pageable);
    }

    /**
     * Get all the userGroups with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<UserGroup> findAllWithEagerRelationships(Pageable pageable) {
        return userGroupRepository.findAllWithEagerRelationships(pageable);
    }
    

    /**
     * Get one userGroup by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UserGroup> findOne(Long id) {
        log.debug("Request to get UserGroup : {}", id);
        return userGroupRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the userGroup by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete UserGroup : {}", id);
        userGroupRepository.deleteById(id);
    }
}

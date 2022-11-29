package com.alphadevs.sales.service;

import com.alphadevs.sales.domain.UserPermissions;
import com.alphadevs.sales.repository.UserPermissionsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link UserPermissions}.
 */
@Service
@Transactional
public class UserPermissionsService {

    private final Logger log = LoggerFactory.getLogger(UserPermissionsService.class);

    private final UserPermissionsRepository userPermissionsRepository;

    public UserPermissionsService(UserPermissionsRepository userPermissionsRepository) {
        this.userPermissionsRepository = userPermissionsRepository;
    }

    /**
     * Save a userPermissions.
     *
     * @param userPermissions the entity to save.
     * @return the persisted entity.
     */
    public UserPermissions save(UserPermissions userPermissions) {
        log.debug("Request to save UserPermissions : {}", userPermissions);
        return userPermissionsRepository.save(userPermissions);
    }

    /**
     * Get all the userPermissions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<UserPermissions> findAll(Pageable pageable) {
        log.debug("Request to get all UserPermissions");
        return userPermissionsRepository.findAll(pageable);
    }

    /**
     * Get all the userPermissions with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<UserPermissions> findAllWithEagerRelationships(Pageable pageable) {
        return userPermissionsRepository.findAllWithEagerRelationships(pageable);
    }
    

    /**
     * Get one userPermissions by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UserPermissions> findOne(Long id) {
        log.debug("Request to get UserPermissions : {}", id);
        return userPermissionsRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the userPermissions by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete UserPermissions : {}", id);
        userPermissionsRepository.deleteById(id);
    }
}

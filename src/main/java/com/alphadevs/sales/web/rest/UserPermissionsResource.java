package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.domain.UserPermissions;
import com.alphadevs.sales.service.UserPermissionsService;
import com.alphadevs.sales.web.rest.errors.BadRequestAlertException;
import com.alphadevs.sales.service.dto.UserPermissionsCriteria;
import com.alphadevs.sales.service.UserPermissionsQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.alphadevs.sales.domain.UserPermissions}.
 */
@RestController
@RequestMapping("/api")
public class UserPermissionsResource {

    private final Logger log = LoggerFactory.getLogger(UserPermissionsResource.class);

    private static final String ENTITY_NAME = "userPermissions";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserPermissionsService userPermissionsService;

    private final UserPermissionsQueryService userPermissionsQueryService;

    public UserPermissionsResource(UserPermissionsService userPermissionsService, UserPermissionsQueryService userPermissionsQueryService) {
        this.userPermissionsService = userPermissionsService;
        this.userPermissionsQueryService = userPermissionsQueryService;
    }

    /**
     * {@code POST  /user-permissions} : Create a new userPermissions.
     *
     * @param userPermissions the userPermissions to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userPermissions, or with status {@code 400 (Bad Request)} if the userPermissions has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-permissions")
    public ResponseEntity<UserPermissions> createUserPermissions(@Valid @RequestBody UserPermissions userPermissions) throws URISyntaxException {
        log.debug("REST request to save UserPermissions : {}", userPermissions);
        if (userPermissions.getId() != null) {
            throw new BadRequestAlertException("A new userPermissions cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserPermissions result = userPermissionsService.save(userPermissions);
        return ResponseEntity.created(new URI("/api/user-permissions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-permissions} : Updates an existing userPermissions.
     *
     * @param userPermissions the userPermissions to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userPermissions,
     * or with status {@code 400 (Bad Request)} if the userPermissions is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userPermissions couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-permissions")
    public ResponseEntity<UserPermissions> updateUserPermissions(@Valid @RequestBody UserPermissions userPermissions) throws URISyntaxException {
        log.debug("REST request to update UserPermissions : {}", userPermissions);
        if (userPermissions.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UserPermissions result = userPermissionsService.save(userPermissions);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userPermissions.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /user-permissions} : get all the userPermissions.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userPermissions in body.
     */
    @GetMapping("/user-permissions")
    public ResponseEntity<List<UserPermissions>> getAllUserPermissions(UserPermissionsCriteria criteria, Pageable pageable) {
        log.debug("REST request to get UserPermissions by criteria: {}", criteria);
        Page<UserPermissions> page = userPermissionsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /user-permissions/count} : count all the userPermissions.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/user-permissions/count")
    public ResponseEntity<Long> countUserPermissions(UserPermissionsCriteria criteria) {
        log.debug("REST request to count UserPermissions by criteria: {}", criteria);
        return ResponseEntity.ok().body(userPermissionsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /user-permissions/:id} : get the "id" userPermissions.
     *
     * @param id the id of the userPermissions to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userPermissions, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-permissions/{id}")
    public ResponseEntity<UserPermissions> getUserPermissions(@PathVariable Long id) {
        log.debug("REST request to get UserPermissions : {}", id);
        Optional<UserPermissions> userPermissions = userPermissionsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userPermissions);
    }

    /**
     * {@code DELETE  /user-permissions/:id} : delete the "id" userPermissions.
     *
     * @param id the id of the userPermissions to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-permissions/{id}")
    public ResponseEntity<Void> deleteUserPermissions(@PathVariable Long id) {
        log.debug("REST request to delete UserPermissions : {}", id);
        userPermissionsService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}

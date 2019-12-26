package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.domain.ExUser;
import com.alphadevs.sales.service.ExUserService;
import com.alphadevs.sales.web.rest.errors.BadRequestAlertException;
import com.alphadevs.sales.service.dto.ExUserCriteria;
import com.alphadevs.sales.service.ExUserQueryService;

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
 * REST controller for managing {@link com.alphadevs.sales.domain.ExUser}.
 */
@RestController
@RequestMapping("/api")
public class ExUserResource {

    private final Logger log = LoggerFactory.getLogger(ExUserResource.class);

    private static final String ENTITY_NAME = "exUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExUserService exUserService;

    private final ExUserQueryService exUserQueryService;

    public ExUserResource(ExUserService exUserService, ExUserQueryService exUserQueryService) {
        this.exUserService = exUserService;
        this.exUserQueryService = exUserQueryService;
    }

    /**
     * {@code POST  /ex-users} : Create a new exUser.
     *
     * @param exUser the exUser to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new exUser, or with status {@code 400 (Bad Request)} if the exUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ex-users")
    public ResponseEntity<ExUser> createExUser(@Valid @RequestBody ExUser exUser) throws URISyntaxException {
        log.debug("REST request to save ExUser : {}", exUser);
        if (exUser.getId() != null) {
            throw new BadRequestAlertException("A new exUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExUser result = exUserService.save(exUser);
        return ResponseEntity.created(new URI("/api/ex-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ex-users} : Updates an existing exUser.
     *
     * @param exUser the exUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated exUser,
     * or with status {@code 400 (Bad Request)} if the exUser is not valid,
     * or with status {@code 500 (Internal Server Error)} if the exUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ex-users")
    public ResponseEntity<ExUser> updateExUser(@Valid @RequestBody ExUser exUser) throws URISyntaxException {
        log.debug("REST request to update ExUser : {}", exUser);
        if (exUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ExUser result = exUserService.save(exUser);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, exUser.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /ex-users} : get all the exUsers.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of exUsers in body.
     */
    @GetMapping("/ex-users")
    public ResponseEntity<List<ExUser>> getAllExUsers(ExUserCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ExUsers by criteria: {}", criteria);
        Page<ExUser> page = exUserQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /ex-users/count} : count all the exUsers.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/ex-users/count")
    public ResponseEntity<Long> countExUsers(ExUserCriteria criteria) {
        log.debug("REST request to count ExUsers by criteria: {}", criteria);
        return ResponseEntity.ok().body(exUserQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /ex-users/:id} : get the "id" exUser.
     *
     * @param id the id of the exUser to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the exUser, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ex-users/{id}")
    public ResponseEntity<ExUser> getExUser(@PathVariable Long id) {
        log.debug("REST request to get ExUser : {}", id);
        Optional<ExUser> exUser = exUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(exUser);
    }

    /**
     * {@code DELETE  /ex-users/:id} : delete the "id" exUser.
     *
     * @param id the id of the exUser to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ex-users/{id}")
    public ResponseEntity<Void> deleteExUser(@PathVariable Long id) {
        log.debug("REST request to delete ExUser : {}", id);
        exUserService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}

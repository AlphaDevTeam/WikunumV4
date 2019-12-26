package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.domain.ChangeLog;
import com.alphadevs.sales.service.ChangeLogService;
import com.alphadevs.sales.web.rest.errors.BadRequestAlertException;
import com.alphadevs.sales.service.dto.ChangeLogCriteria;
import com.alphadevs.sales.service.ChangeLogQueryService;

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
 * REST controller for managing {@link com.alphadevs.sales.domain.ChangeLog}.
 */
@RestController
@RequestMapping("/api")
public class ChangeLogResource {

    private final Logger log = LoggerFactory.getLogger(ChangeLogResource.class);

    private static final String ENTITY_NAME = "changeLog";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ChangeLogService changeLogService;

    private final ChangeLogQueryService changeLogQueryService;

    public ChangeLogResource(ChangeLogService changeLogService, ChangeLogQueryService changeLogQueryService) {
        this.changeLogService = changeLogService;
        this.changeLogQueryService = changeLogQueryService;
    }

    /**
     * {@code POST  /change-logs} : Create a new changeLog.
     *
     * @param changeLog the changeLog to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new changeLog, or with status {@code 400 (Bad Request)} if the changeLog has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/change-logs")
    public ResponseEntity<ChangeLog> createChangeLog(@Valid @RequestBody ChangeLog changeLog) throws URISyntaxException {
        log.debug("REST request to save ChangeLog : {}", changeLog);
        if (changeLog.getId() != null) {
            throw new BadRequestAlertException("A new changeLog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ChangeLog result = changeLogService.save(changeLog);
        return ResponseEntity.created(new URI("/api/change-logs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /change-logs} : Updates an existing changeLog.
     *
     * @param changeLog the changeLog to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated changeLog,
     * or with status {@code 400 (Bad Request)} if the changeLog is not valid,
     * or with status {@code 500 (Internal Server Error)} if the changeLog couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/change-logs")
    public ResponseEntity<ChangeLog> updateChangeLog(@Valid @RequestBody ChangeLog changeLog) throws URISyntaxException {
        log.debug("REST request to update ChangeLog : {}", changeLog);
        if (changeLog.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ChangeLog result = changeLogService.save(changeLog);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, changeLog.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /change-logs} : get all the changeLogs.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of changeLogs in body.
     */
    @GetMapping("/change-logs")
    public ResponseEntity<List<ChangeLog>> getAllChangeLogs(ChangeLogCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ChangeLogs by criteria: {}", criteria);
        Page<ChangeLog> page = changeLogQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /change-logs/count} : count all the changeLogs.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/change-logs/count")
    public ResponseEntity<Long> countChangeLogs(ChangeLogCriteria criteria) {
        log.debug("REST request to count ChangeLogs by criteria: {}", criteria);
        return ResponseEntity.ok().body(changeLogQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /change-logs/:id} : get the "id" changeLog.
     *
     * @param id the id of the changeLog to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the changeLog, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/change-logs/{id}")
    public ResponseEntity<ChangeLog> getChangeLog(@PathVariable Long id) {
        log.debug("REST request to get ChangeLog : {}", id);
        Optional<ChangeLog> changeLog = changeLogService.findOne(id);
        return ResponseUtil.wrapOrNotFound(changeLog);
    }

    /**
     * {@code DELETE  /change-logs/:id} : delete the "id" changeLog.
     *
     * @param id the id of the changeLog to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/change-logs/{id}")
    public ResponseEntity<Void> deleteChangeLog(@PathVariable Long id) {
        log.debug("REST request to delete ChangeLog : {}", id);
        changeLogService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}

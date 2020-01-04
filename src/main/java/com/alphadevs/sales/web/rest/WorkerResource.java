package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.domain.Worker;
import com.alphadevs.sales.service.WorkerService;
import com.alphadevs.sales.web.rest.errors.BadRequestAlertException;
import com.alphadevs.sales.service.dto.WorkerCriteria;
import com.alphadevs.sales.service.WorkerQueryService;

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
 * REST controller for managing {@link com.alphadevs.sales.domain.Worker}.
 */
@RestController
@RequestMapping("/api")
public class WorkerResource {

    private final Logger log = LoggerFactory.getLogger(WorkerResource.class);

    private static final String ENTITY_NAME = "worker";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkerService workerService;

    private final WorkerQueryService workerQueryService;

    public WorkerResource(WorkerService workerService, WorkerQueryService workerQueryService) {
        this.workerService = workerService;
        this.workerQueryService = workerQueryService;
    }

    /**
     * {@code POST  /workers} : Create a new worker.
     *
     * @param worker the worker to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new worker, or with status {@code 400 (Bad Request)} if the worker has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/workers")
    public ResponseEntity<Worker> createWorker(@Valid @RequestBody Worker worker) throws URISyntaxException {
        log.debug("REST request to save Worker : {}", worker);
        if (worker.getId() != null) {
            throw new BadRequestAlertException("A new worker cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Worker result = workerService.save(worker);
        return ResponseEntity.created(new URI("/api/workers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /workers} : Updates an existing worker.
     *
     * @param worker the worker to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated worker,
     * or with status {@code 400 (Bad Request)} if the worker is not valid,
     * or with status {@code 500 (Internal Server Error)} if the worker couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/workers")
    public ResponseEntity<Worker> updateWorker(@Valid @RequestBody Worker worker) throws URISyntaxException {
        log.debug("REST request to update Worker : {}", worker);
        if (worker.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Worker result = workerService.save(worker);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, worker.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /workers} : get all the workers.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workers in body.
     */
    @GetMapping("/workers")
    public ResponseEntity<List<Worker>> getAllWorkers(WorkerCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Workers by criteria: {}", criteria);
        Page<Worker> page = workerQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /workers/count} : count all the workers.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/workers/count")
    public ResponseEntity<Long> countWorkers(WorkerCriteria criteria) {
        log.debug("REST request to count Workers by criteria: {}", criteria);
        return ResponseEntity.ok().body(workerQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /workers/:id} : get the "id" worker.
     *
     * @param id the id of the worker to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the worker, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/workers/{id}")
    public ResponseEntity<Worker> getWorker(@PathVariable Long id) {
        log.debug("REST request to get Worker : {}", id);
        Optional<Worker> worker = workerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(worker);
    }

    /**
     * {@code DELETE  /workers/:id} : delete the "id" worker.
     *
     * @param id the id of the worker to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/workers/{id}")
    public ResponseEntity<Void> deleteWorker(@PathVariable Long id) {
        log.debug("REST request to delete Worker : {}", id);
        workerService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}

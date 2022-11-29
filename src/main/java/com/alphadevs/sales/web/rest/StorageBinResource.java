package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.domain.StorageBin;
import com.alphadevs.sales.service.StorageBinService;
import com.alphadevs.sales.web.rest.errors.BadRequestAlertException;
import com.alphadevs.sales.service.dto.StorageBinCriteria;
import com.alphadevs.sales.service.StorageBinQueryService;

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
 * REST controller for managing {@link com.alphadevs.sales.domain.StorageBin}.
 */
@RestController
@RequestMapping("/api")
public class StorageBinResource {

    private final Logger log = LoggerFactory.getLogger(StorageBinResource.class);

    private static final String ENTITY_NAME = "storageBin";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StorageBinService storageBinService;

    private final StorageBinQueryService storageBinQueryService;

    public StorageBinResource(StorageBinService storageBinService, StorageBinQueryService storageBinQueryService) {
        this.storageBinService = storageBinService;
        this.storageBinQueryService = storageBinQueryService;
    }

    /**
     * {@code POST  /storage-bins} : Create a new storageBin.
     *
     * @param storageBin the storageBin to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new storageBin, or with status {@code 400 (Bad Request)} if the storageBin has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/storage-bins")
    public ResponseEntity<StorageBin> createStorageBin(@Valid @RequestBody StorageBin storageBin) throws URISyntaxException {
        log.debug("REST request to save StorageBin : {}", storageBin);
        if (storageBin.getId() != null) {
            throw new BadRequestAlertException("A new storageBin cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StorageBin result = storageBinService.save(storageBin);
        return ResponseEntity.created(new URI("/api/storage-bins/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /storage-bins} : Updates an existing storageBin.
     *
     * @param storageBin the storageBin to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated storageBin,
     * or with status {@code 400 (Bad Request)} if the storageBin is not valid,
     * or with status {@code 500 (Internal Server Error)} if the storageBin couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/storage-bins")
    public ResponseEntity<StorageBin> updateStorageBin(@Valid @RequestBody StorageBin storageBin) throws URISyntaxException {
        log.debug("REST request to update StorageBin : {}", storageBin);
        if (storageBin.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        StorageBin result = storageBinService.save(storageBin);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, storageBin.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /storage-bins} : get all the storageBins.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of storageBins in body.
     */
    @GetMapping("/storage-bins")
    public ResponseEntity<List<StorageBin>> getAllStorageBins(StorageBinCriteria criteria, Pageable pageable) {
        log.debug("REST request to get StorageBins by criteria: {}", criteria);
        Page<StorageBin> page = storageBinQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /storage-bins/count} : count all the storageBins.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/storage-bins/count")
    public ResponseEntity<Long> countStorageBins(StorageBinCriteria criteria) {
        log.debug("REST request to count StorageBins by criteria: {}", criteria);
        return ResponseEntity.ok().body(storageBinQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /storage-bins/:id} : get the "id" storageBin.
     *
     * @param id the id of the storageBin to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the storageBin, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/storage-bins/{id}")
    public ResponseEntity<StorageBin> getStorageBin(@PathVariable Long id) {
        log.debug("REST request to get StorageBin : {}", id);
        Optional<StorageBin> storageBin = storageBinService.findOne(id);
        return ResponseUtil.wrapOrNotFound(storageBin);
    }

    /**
     * {@code DELETE  /storage-bins/:id} : delete the "id" storageBin.
     *
     * @param id the id of the storageBin to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/storage-bins/{id}")
    public ResponseEntity<Void> deleteStorageBin(@PathVariable Long id) {
        log.debug("REST request to delete StorageBin : {}", id);
        storageBinService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}

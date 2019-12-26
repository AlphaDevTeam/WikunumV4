package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.domain.DocumentHistory;
import com.alphadevs.sales.service.DocumentHistoryService;
import com.alphadevs.sales.web.rest.errors.BadRequestAlertException;
import com.alphadevs.sales.service.dto.DocumentHistoryCriteria;
import com.alphadevs.sales.service.DocumentHistoryQueryService;

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
 * REST controller for managing {@link com.alphadevs.sales.domain.DocumentHistory}.
 */
@RestController
@RequestMapping("/api")
public class DocumentHistoryResource {

    private final Logger log = LoggerFactory.getLogger(DocumentHistoryResource.class);

    private static final String ENTITY_NAME = "documentHistory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DocumentHistoryService documentHistoryService;

    private final DocumentHistoryQueryService documentHistoryQueryService;

    public DocumentHistoryResource(DocumentHistoryService documentHistoryService, DocumentHistoryQueryService documentHistoryQueryService) {
        this.documentHistoryService = documentHistoryService;
        this.documentHistoryQueryService = documentHistoryQueryService;
    }

    /**
     * {@code POST  /document-histories} : Create a new documentHistory.
     *
     * @param documentHistory the documentHistory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new documentHistory, or with status {@code 400 (Bad Request)} if the documentHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/document-histories")
    public ResponseEntity<DocumentHistory> createDocumentHistory(@Valid @RequestBody DocumentHistory documentHistory) throws URISyntaxException {
        log.debug("REST request to save DocumentHistory : {}", documentHistory);
        if (documentHistory.getId() != null) {
            throw new BadRequestAlertException("A new documentHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DocumentHistory result = documentHistoryService.save(documentHistory);
        return ResponseEntity.created(new URI("/api/document-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /document-histories} : Updates an existing documentHistory.
     *
     * @param documentHistory the documentHistory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentHistory,
     * or with status {@code 400 (Bad Request)} if the documentHistory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the documentHistory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/document-histories")
    public ResponseEntity<DocumentHistory> updateDocumentHistory(@Valid @RequestBody DocumentHistory documentHistory) throws URISyntaxException {
        log.debug("REST request to update DocumentHistory : {}", documentHistory);
        if (documentHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DocumentHistory result = documentHistoryService.save(documentHistory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, documentHistory.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /document-histories} : get all the documentHistories.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of documentHistories in body.
     */
    @GetMapping("/document-histories")
    public ResponseEntity<List<DocumentHistory>> getAllDocumentHistories(DocumentHistoryCriteria criteria, Pageable pageable) {
        log.debug("REST request to get DocumentHistories by criteria: {}", criteria);
        Page<DocumentHistory> page = documentHistoryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /document-histories/count} : count all the documentHistories.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/document-histories/count")
    public ResponseEntity<Long> countDocumentHistories(DocumentHistoryCriteria criteria) {
        log.debug("REST request to count DocumentHistories by criteria: {}", criteria);
        return ResponseEntity.ok().body(documentHistoryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /document-histories/:id} : get the "id" documentHistory.
     *
     * @param id the id of the documentHistory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the documentHistory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/document-histories/{id}")
    public ResponseEntity<DocumentHistory> getDocumentHistory(@PathVariable Long id) {
        log.debug("REST request to get DocumentHistory : {}", id);
        Optional<DocumentHistory> documentHistory = documentHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(documentHistory);
    }

    /**
     * {@code DELETE  /document-histories/:id} : delete the "id" documentHistory.
     *
     * @param id the id of the documentHistory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/document-histories/{id}")
    public ResponseEntity<Void> deleteDocumentHistory(@PathVariable Long id) {
        log.debug("REST request to delete DocumentHistory : {}", id);
        documentHistoryService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}

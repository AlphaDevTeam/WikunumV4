package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.domain.DocumentNumberConfig;
import com.alphadevs.sales.service.DocumentNumberConfigService;
import com.alphadevs.sales.web.rest.errors.BadRequestAlertException;
import com.alphadevs.sales.service.dto.DocumentNumberConfigCriteria;
import com.alphadevs.sales.service.DocumentNumberConfigQueryService;

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
 * REST controller for managing {@link com.alphadevs.sales.domain.DocumentNumberConfig}.
 */
@RestController
@RequestMapping("/api")
public class DocumentNumberConfigResource {

    private final Logger log = LoggerFactory.getLogger(DocumentNumberConfigResource.class);

    private static final String ENTITY_NAME = "documentNumberConfig";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DocumentNumberConfigService documentNumberConfigService;

    private final DocumentNumberConfigQueryService documentNumberConfigQueryService;

    public DocumentNumberConfigResource(DocumentNumberConfigService documentNumberConfigService, DocumentNumberConfigQueryService documentNumberConfigQueryService) {
        this.documentNumberConfigService = documentNumberConfigService;
        this.documentNumberConfigQueryService = documentNumberConfigQueryService;
    }

    /**
     * {@code POST  /document-number-configs} : Create a new documentNumberConfig.
     *
     * @param documentNumberConfig the documentNumberConfig to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new documentNumberConfig, or with status {@code 400 (Bad Request)} if the documentNumberConfig has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/document-number-configs")
    public ResponseEntity<DocumentNumberConfig> createDocumentNumberConfig(@Valid @RequestBody DocumentNumberConfig documentNumberConfig) throws URISyntaxException {
        log.debug("REST request to save DocumentNumberConfig : {}", documentNumberConfig);
        if (documentNumberConfig.getId() != null) {
            throw new BadRequestAlertException("A new documentNumberConfig cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DocumentNumberConfig result = documentNumberConfigService.save(documentNumberConfig);
        return ResponseEntity.created(new URI("/api/document-number-configs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /document-number-configs} : Updates an existing documentNumberConfig.
     *
     * @param documentNumberConfig the documentNumberConfig to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentNumberConfig,
     * or with status {@code 400 (Bad Request)} if the documentNumberConfig is not valid,
     * or with status {@code 500 (Internal Server Error)} if the documentNumberConfig couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/document-number-configs")
    public ResponseEntity<DocumentNumberConfig> updateDocumentNumberConfig(@Valid @RequestBody DocumentNumberConfig documentNumberConfig) throws URISyntaxException {
        log.debug("REST request to update DocumentNumberConfig : {}", documentNumberConfig);
        if (documentNumberConfig.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DocumentNumberConfig result = documentNumberConfigService.save(documentNumberConfig);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, documentNumberConfig.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /document-number-configs} : get all the documentNumberConfigs.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of documentNumberConfigs in body.
     */
    @GetMapping("/document-number-configs")
    public ResponseEntity<List<DocumentNumberConfig>> getAllDocumentNumberConfigs(DocumentNumberConfigCriteria criteria, Pageable pageable) {
        log.debug("REST request to get DocumentNumberConfigs by criteria: {}", criteria);
        Page<DocumentNumberConfig> page = documentNumberConfigQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /document-number-configs/count} : count all the documentNumberConfigs.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/document-number-configs/count")
    public ResponseEntity<Long> countDocumentNumberConfigs(DocumentNumberConfigCriteria criteria) {
        log.debug("REST request to count DocumentNumberConfigs by criteria: {}", criteria);
        return ResponseEntity.ok().body(documentNumberConfigQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /document-number-configs/:id} : get the "id" documentNumberConfig.
     *
     * @param id the id of the documentNumberConfig to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the documentNumberConfig, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/document-number-configs/{id}")
    public ResponseEntity<DocumentNumberConfig> getDocumentNumberConfig(@PathVariable Long id) {
        log.debug("REST request to get DocumentNumberConfig : {}", id);
        Optional<DocumentNumberConfig> documentNumberConfig = documentNumberConfigService.findOne(id);
        return ResponseUtil.wrapOrNotFound(documentNumberConfig);
    }

    /**
     * {@code DELETE  /document-number-configs/:id} : delete the "id" documentNumberConfig.
     *
     * @param id the id of the documentNumberConfig to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/document-number-configs/{id}")
    public ResponseEntity<Void> deleteDocumentNumberConfig(@PathVariable Long id) {
        log.debug("REST request to delete DocumentNumberConfig : {}", id);
        documentNumberConfigService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}

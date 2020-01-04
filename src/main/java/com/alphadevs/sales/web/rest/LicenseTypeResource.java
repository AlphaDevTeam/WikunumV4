package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.domain.LicenseType;
import com.alphadevs.sales.service.LicenseTypeService;
import com.alphadevs.sales.web.rest.errors.BadRequestAlertException;
import com.alphadevs.sales.service.dto.LicenseTypeCriteria;
import com.alphadevs.sales.service.LicenseTypeQueryService;

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
 * REST controller for managing {@link com.alphadevs.sales.domain.LicenseType}.
 */
@RestController
@RequestMapping("/api")
public class LicenseTypeResource {

    private final Logger log = LoggerFactory.getLogger(LicenseTypeResource.class);

    private static final String ENTITY_NAME = "licenseType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LicenseTypeService licenseTypeService;

    private final LicenseTypeQueryService licenseTypeQueryService;

    public LicenseTypeResource(LicenseTypeService licenseTypeService, LicenseTypeQueryService licenseTypeQueryService) {
        this.licenseTypeService = licenseTypeService;
        this.licenseTypeQueryService = licenseTypeQueryService;
    }

    /**
     * {@code POST  /license-types} : Create a new licenseType.
     *
     * @param licenseType the licenseType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new licenseType, or with status {@code 400 (Bad Request)} if the licenseType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/license-types")
    public ResponseEntity<LicenseType> createLicenseType(@Valid @RequestBody LicenseType licenseType) throws URISyntaxException {
        log.debug("REST request to save LicenseType : {}", licenseType);
        if (licenseType.getId() != null) {
            throw new BadRequestAlertException("A new licenseType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LicenseType result = licenseTypeService.save(licenseType);
        return ResponseEntity.created(new URI("/api/license-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /license-types} : Updates an existing licenseType.
     *
     * @param licenseType the licenseType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated licenseType,
     * or with status {@code 400 (Bad Request)} if the licenseType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the licenseType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/license-types")
    public ResponseEntity<LicenseType> updateLicenseType(@Valid @RequestBody LicenseType licenseType) throws URISyntaxException {
        log.debug("REST request to update LicenseType : {}", licenseType);
        if (licenseType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        LicenseType result = licenseTypeService.save(licenseType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, licenseType.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /license-types} : get all the licenseTypes.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of licenseTypes in body.
     */
    @GetMapping("/license-types")
    public ResponseEntity<List<LicenseType>> getAllLicenseTypes(LicenseTypeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get LicenseTypes by criteria: {}", criteria);
        Page<LicenseType> page = licenseTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /license-types/count} : count all the licenseTypes.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/license-types/count")
    public ResponseEntity<Long> countLicenseTypes(LicenseTypeCriteria criteria) {
        log.debug("REST request to count LicenseTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(licenseTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /license-types/:id} : get the "id" licenseType.
     *
     * @param id the id of the licenseType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the licenseType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/license-types/{id}")
    public ResponseEntity<LicenseType> getLicenseType(@PathVariable Long id) {
        log.debug("REST request to get LicenseType : {}", id);
        Optional<LicenseType> licenseType = licenseTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(licenseType);
    }

    /**
     * {@code DELETE  /license-types/:id} : delete the "id" licenseType.
     *
     * @param id the id of the licenseType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/license-types/{id}")
    public ResponseEntity<Void> deleteLicenseType(@PathVariable Long id) {
        log.debug("REST request to delete LicenseType : {}", id);
        licenseTypeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}

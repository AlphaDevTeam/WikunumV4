package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.domain.EmployeeType;
import com.alphadevs.sales.service.EmployeeTypeService;
import com.alphadevs.sales.web.rest.errors.BadRequestAlertException;
import com.alphadevs.sales.service.dto.EmployeeTypeCriteria;
import com.alphadevs.sales.service.EmployeeTypeQueryService;

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
 * REST controller for managing {@link com.alphadevs.sales.domain.EmployeeType}.
 */
@RestController
@RequestMapping("/api")
public class EmployeeTypeResource {

    private final Logger log = LoggerFactory.getLogger(EmployeeTypeResource.class);

    private static final String ENTITY_NAME = "employeeType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmployeeTypeService employeeTypeService;

    private final EmployeeTypeQueryService employeeTypeQueryService;

    public EmployeeTypeResource(EmployeeTypeService employeeTypeService, EmployeeTypeQueryService employeeTypeQueryService) {
        this.employeeTypeService = employeeTypeService;
        this.employeeTypeQueryService = employeeTypeQueryService;
    }

    /**
     * {@code POST  /employee-types} : Create a new employeeType.
     *
     * @param employeeType the employeeType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new employeeType, or with status {@code 400 (Bad Request)} if the employeeType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/employee-types")
    public ResponseEntity<EmployeeType> createEmployeeType(@Valid @RequestBody EmployeeType employeeType) throws URISyntaxException {
        log.debug("REST request to save EmployeeType : {}", employeeType);
        if (employeeType.getId() != null) {
            throw new BadRequestAlertException("A new employeeType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EmployeeType result = employeeTypeService.save(employeeType);
        return ResponseEntity.created(new URI("/api/employee-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /employee-types} : Updates an existing employeeType.
     *
     * @param employeeType the employeeType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employeeType,
     * or with status {@code 400 (Bad Request)} if the employeeType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the employeeType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/employee-types")
    public ResponseEntity<EmployeeType> updateEmployeeType(@Valid @RequestBody EmployeeType employeeType) throws URISyntaxException {
        log.debug("REST request to update EmployeeType : {}", employeeType);
        if (employeeType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        EmployeeType result = employeeTypeService.save(employeeType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, employeeType.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /employee-types} : get all the employeeTypes.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of employeeTypes in body.
     */
    @GetMapping("/employee-types")
    public ResponseEntity<List<EmployeeType>> getAllEmployeeTypes(EmployeeTypeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get EmployeeTypes by criteria: {}", criteria);
        Page<EmployeeType> page = employeeTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /employee-types/count} : count all the employeeTypes.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/employee-types/count")
    public ResponseEntity<Long> countEmployeeTypes(EmployeeTypeCriteria criteria) {
        log.debug("REST request to count EmployeeTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(employeeTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /employee-types/:id} : get the "id" employeeType.
     *
     * @param id the id of the employeeType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the employeeType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/employee-types/{id}")
    public ResponseEntity<EmployeeType> getEmployeeType(@PathVariable Long id) {
        log.debug("REST request to get EmployeeType : {}", id);
        Optional<EmployeeType> employeeType = employeeTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(employeeType);
    }

    /**
     * {@code DELETE  /employee-types/:id} : delete the "id" employeeType.
     *
     * @param id the id of the employeeType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/employee-types/{id}")
    public ResponseEntity<Void> deleteEmployeeType(@PathVariable Long id) {
        log.debug("REST request to delete EmployeeType : {}", id);
        employeeTypeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}

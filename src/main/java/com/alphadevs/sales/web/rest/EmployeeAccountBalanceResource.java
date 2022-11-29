package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.domain.EmployeeAccountBalance;
import com.alphadevs.sales.service.EmployeeAccountBalanceService;
import com.alphadevs.sales.web.rest.errors.BadRequestAlertException;
import com.alphadevs.sales.service.dto.EmployeeAccountBalanceCriteria;
import com.alphadevs.sales.service.EmployeeAccountBalanceQueryService;

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
 * REST controller for managing {@link com.alphadevs.sales.domain.EmployeeAccountBalance}.
 */
@RestController
@RequestMapping("/api")
public class EmployeeAccountBalanceResource {

    private final Logger log = LoggerFactory.getLogger(EmployeeAccountBalanceResource.class);

    private static final String ENTITY_NAME = "employeeAccountBalance";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmployeeAccountBalanceService employeeAccountBalanceService;

    private final EmployeeAccountBalanceQueryService employeeAccountBalanceQueryService;

    public EmployeeAccountBalanceResource(EmployeeAccountBalanceService employeeAccountBalanceService, EmployeeAccountBalanceQueryService employeeAccountBalanceQueryService) {
        this.employeeAccountBalanceService = employeeAccountBalanceService;
        this.employeeAccountBalanceQueryService = employeeAccountBalanceQueryService;
    }

    /**
     * {@code POST  /employee-account-balances} : Create a new employeeAccountBalance.
     *
     * @param employeeAccountBalance the employeeAccountBalance to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new employeeAccountBalance, or with status {@code 400 (Bad Request)} if the employeeAccountBalance has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/employee-account-balances")
    public ResponseEntity<EmployeeAccountBalance> createEmployeeAccountBalance(@Valid @RequestBody EmployeeAccountBalance employeeAccountBalance) throws URISyntaxException {
        log.debug("REST request to save EmployeeAccountBalance : {}", employeeAccountBalance);
        if (employeeAccountBalance.getId() != null) {
            throw new BadRequestAlertException("A new employeeAccountBalance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EmployeeAccountBalance result = employeeAccountBalanceService.save(employeeAccountBalance);
        return ResponseEntity.created(new URI("/api/employee-account-balances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /employee-account-balances} : Updates an existing employeeAccountBalance.
     *
     * @param employeeAccountBalance the employeeAccountBalance to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employeeAccountBalance,
     * or with status {@code 400 (Bad Request)} if the employeeAccountBalance is not valid,
     * or with status {@code 500 (Internal Server Error)} if the employeeAccountBalance couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/employee-account-balances")
    public ResponseEntity<EmployeeAccountBalance> updateEmployeeAccountBalance(@Valid @RequestBody EmployeeAccountBalance employeeAccountBalance) throws URISyntaxException {
        log.debug("REST request to update EmployeeAccountBalance : {}", employeeAccountBalance);
        if (employeeAccountBalance.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        EmployeeAccountBalance result = employeeAccountBalanceService.save(employeeAccountBalance);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, employeeAccountBalance.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /employee-account-balances} : get all the employeeAccountBalances.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of employeeAccountBalances in body.
     */
    @GetMapping("/employee-account-balances")
    public ResponseEntity<List<EmployeeAccountBalance>> getAllEmployeeAccountBalances(EmployeeAccountBalanceCriteria criteria, Pageable pageable) {
        log.debug("REST request to get EmployeeAccountBalances by criteria: {}", criteria);
        Page<EmployeeAccountBalance> page = employeeAccountBalanceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /employee-account-balances/count} : count all the employeeAccountBalances.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/employee-account-balances/count")
    public ResponseEntity<Long> countEmployeeAccountBalances(EmployeeAccountBalanceCriteria criteria) {
        log.debug("REST request to count EmployeeAccountBalances by criteria: {}", criteria);
        return ResponseEntity.ok().body(employeeAccountBalanceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /employee-account-balances/:id} : get the "id" employeeAccountBalance.
     *
     * @param id the id of the employeeAccountBalance to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the employeeAccountBalance, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/employee-account-balances/{id}")
    public ResponseEntity<EmployeeAccountBalance> getEmployeeAccountBalance(@PathVariable Long id) {
        log.debug("REST request to get EmployeeAccountBalance : {}", id);
        Optional<EmployeeAccountBalance> employeeAccountBalance = employeeAccountBalanceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(employeeAccountBalance);
    }

    /**
     * {@code DELETE  /employee-account-balances/:id} : delete the "id" employeeAccountBalance.
     *
     * @param id the id of the employeeAccountBalance to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/employee-account-balances/{id}")
    public ResponseEntity<Void> deleteEmployeeAccountBalance(@PathVariable Long id) {
        log.debug("REST request to delete EmployeeAccountBalance : {}", id);
        employeeAccountBalanceService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}

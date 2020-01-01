package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.domain.CostOfSalesAccount;
import com.alphadevs.sales.service.CostOfSalesAccountService;
import com.alphadevs.sales.web.rest.errors.BadRequestAlertException;
import com.alphadevs.sales.service.dto.CostOfSalesAccountCriteria;
import com.alphadevs.sales.service.CostOfSalesAccountQueryService;

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
 * REST controller for managing {@link com.alphadevs.sales.domain.CostOfSalesAccount}.
 */
@RestController
@RequestMapping("/api")
public class CostOfSalesAccountResource {

    private final Logger log = LoggerFactory.getLogger(CostOfSalesAccountResource.class);

    private static final String ENTITY_NAME = "costOfSalesAccount";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CostOfSalesAccountService costOfSalesAccountService;

    private final CostOfSalesAccountQueryService costOfSalesAccountQueryService;

    public CostOfSalesAccountResource(CostOfSalesAccountService costOfSalesAccountService, CostOfSalesAccountQueryService costOfSalesAccountQueryService) {
        this.costOfSalesAccountService = costOfSalesAccountService;
        this.costOfSalesAccountQueryService = costOfSalesAccountQueryService;
    }

    /**
     * {@code POST  /cost-of-sales-accounts} : Create a new costOfSalesAccount.
     *
     * @param costOfSalesAccount the costOfSalesAccount to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new costOfSalesAccount, or with status {@code 400 (Bad Request)} if the costOfSalesAccount has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cost-of-sales-accounts")
    public ResponseEntity<CostOfSalesAccount> createCostOfSalesAccount(@Valid @RequestBody CostOfSalesAccount costOfSalesAccount) throws URISyntaxException {
        log.debug("REST request to save CostOfSalesAccount : {}", costOfSalesAccount);
        if (costOfSalesAccount.getId() != null) {
            throw new BadRequestAlertException("A new costOfSalesAccount cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CostOfSalesAccount result = costOfSalesAccountService.save(costOfSalesAccount);
        return ResponseEntity.created(new URI("/api/cost-of-sales-accounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cost-of-sales-accounts} : Updates an existing costOfSalesAccount.
     *
     * @param costOfSalesAccount the costOfSalesAccount to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated costOfSalesAccount,
     * or with status {@code 400 (Bad Request)} if the costOfSalesAccount is not valid,
     * or with status {@code 500 (Internal Server Error)} if the costOfSalesAccount couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cost-of-sales-accounts")
    public ResponseEntity<CostOfSalesAccount> updateCostOfSalesAccount(@Valid @RequestBody CostOfSalesAccount costOfSalesAccount) throws URISyntaxException {
        log.debug("REST request to update CostOfSalesAccount : {}", costOfSalesAccount);
        if (costOfSalesAccount.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CostOfSalesAccount result = costOfSalesAccountService.save(costOfSalesAccount);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, costOfSalesAccount.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /cost-of-sales-accounts} : get all the costOfSalesAccounts.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of costOfSalesAccounts in body.
     */
    @GetMapping("/cost-of-sales-accounts")
    public ResponseEntity<List<CostOfSalesAccount>> getAllCostOfSalesAccounts(CostOfSalesAccountCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CostOfSalesAccounts by criteria: {}", criteria);
        Page<CostOfSalesAccount> page = costOfSalesAccountQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /cost-of-sales-accounts/count} : count all the costOfSalesAccounts.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/cost-of-sales-accounts/count")
    public ResponseEntity<Long> countCostOfSalesAccounts(CostOfSalesAccountCriteria criteria) {
        log.debug("REST request to count CostOfSalesAccounts by criteria: {}", criteria);
        return ResponseEntity.ok().body(costOfSalesAccountQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cost-of-sales-accounts/:id} : get the "id" costOfSalesAccount.
     *
     * @param id the id of the costOfSalesAccount to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the costOfSalesAccount, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cost-of-sales-accounts/{id}")
    public ResponseEntity<CostOfSalesAccount> getCostOfSalesAccount(@PathVariable Long id) {
        log.debug("REST request to get CostOfSalesAccount : {}", id);
        Optional<CostOfSalesAccount> costOfSalesAccount = costOfSalesAccountService.findOne(id);
        return ResponseUtil.wrapOrNotFound(costOfSalesAccount);
    }

    /**
     * {@code DELETE  /cost-of-sales-accounts/:id} : delete the "id" costOfSalesAccount.
     *
     * @param id the id of the costOfSalesAccount to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cost-of-sales-accounts/{id}")
    public ResponseEntity<Void> deleteCostOfSalesAccount(@PathVariable Long id) {
        log.debug("REST request to delete CostOfSalesAccount : {}", id);
        costOfSalesAccountService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}

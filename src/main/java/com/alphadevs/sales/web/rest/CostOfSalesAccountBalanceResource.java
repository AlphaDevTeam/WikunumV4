package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.domain.CostOfSalesAccountBalance;
import com.alphadevs.sales.service.CostOfSalesAccountBalanceService;
import com.alphadevs.sales.web.rest.errors.BadRequestAlertException;
import com.alphadevs.sales.service.dto.CostOfSalesAccountBalanceCriteria;
import com.alphadevs.sales.service.CostOfSalesAccountBalanceQueryService;

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
 * REST controller for managing {@link com.alphadevs.sales.domain.CostOfSalesAccountBalance}.
 */
@RestController
@RequestMapping("/api")
public class CostOfSalesAccountBalanceResource {

    private final Logger log = LoggerFactory.getLogger(CostOfSalesAccountBalanceResource.class);

    private static final String ENTITY_NAME = "costOfSalesAccountBalance";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CostOfSalesAccountBalanceService costOfSalesAccountBalanceService;

    private final CostOfSalesAccountBalanceQueryService costOfSalesAccountBalanceQueryService;

    public CostOfSalesAccountBalanceResource(CostOfSalesAccountBalanceService costOfSalesAccountBalanceService, CostOfSalesAccountBalanceQueryService costOfSalesAccountBalanceQueryService) {
        this.costOfSalesAccountBalanceService = costOfSalesAccountBalanceService;
        this.costOfSalesAccountBalanceQueryService = costOfSalesAccountBalanceQueryService;
    }

    /**
     * {@code POST  /cost-of-sales-account-balances} : Create a new costOfSalesAccountBalance.
     *
     * @param costOfSalesAccountBalance the costOfSalesAccountBalance to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new costOfSalesAccountBalance, or with status {@code 400 (Bad Request)} if the costOfSalesAccountBalance has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cost-of-sales-account-balances")
    public ResponseEntity<CostOfSalesAccountBalance> createCostOfSalesAccountBalance(@Valid @RequestBody CostOfSalesAccountBalance costOfSalesAccountBalance) throws URISyntaxException {
        log.debug("REST request to save CostOfSalesAccountBalance : {}", costOfSalesAccountBalance);
        if (costOfSalesAccountBalance.getId() != null) {
            throw new BadRequestAlertException("A new costOfSalesAccountBalance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CostOfSalesAccountBalance result = costOfSalesAccountBalanceService.save(costOfSalesAccountBalance);
        return ResponseEntity.created(new URI("/api/cost-of-sales-account-balances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cost-of-sales-account-balances} : Updates an existing costOfSalesAccountBalance.
     *
     * @param costOfSalesAccountBalance the costOfSalesAccountBalance to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated costOfSalesAccountBalance,
     * or with status {@code 400 (Bad Request)} if the costOfSalesAccountBalance is not valid,
     * or with status {@code 500 (Internal Server Error)} if the costOfSalesAccountBalance couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cost-of-sales-account-balances")
    public ResponseEntity<CostOfSalesAccountBalance> updateCostOfSalesAccountBalance(@Valid @RequestBody CostOfSalesAccountBalance costOfSalesAccountBalance) throws URISyntaxException {
        log.debug("REST request to update CostOfSalesAccountBalance : {}", costOfSalesAccountBalance);
        if (costOfSalesAccountBalance.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CostOfSalesAccountBalance result = costOfSalesAccountBalanceService.save(costOfSalesAccountBalance);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, costOfSalesAccountBalance.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /cost-of-sales-account-balances} : get all the costOfSalesAccountBalances.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of costOfSalesAccountBalances in body.
     */
    @GetMapping("/cost-of-sales-account-balances")
    public ResponseEntity<List<CostOfSalesAccountBalance>> getAllCostOfSalesAccountBalances(CostOfSalesAccountBalanceCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CostOfSalesAccountBalances by criteria: {}", criteria);
        Page<CostOfSalesAccountBalance> page = costOfSalesAccountBalanceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /cost-of-sales-account-balances/count} : count all the costOfSalesAccountBalances.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/cost-of-sales-account-balances/count")
    public ResponseEntity<Long> countCostOfSalesAccountBalances(CostOfSalesAccountBalanceCriteria criteria) {
        log.debug("REST request to count CostOfSalesAccountBalances by criteria: {}", criteria);
        return ResponseEntity.ok().body(costOfSalesAccountBalanceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cost-of-sales-account-balances/:id} : get the "id" costOfSalesAccountBalance.
     *
     * @param id the id of the costOfSalesAccountBalance to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the costOfSalesAccountBalance, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cost-of-sales-account-balances/{id}")
    public ResponseEntity<CostOfSalesAccountBalance> getCostOfSalesAccountBalance(@PathVariable Long id) {
        log.debug("REST request to get CostOfSalesAccountBalance : {}", id);
        Optional<CostOfSalesAccountBalance> costOfSalesAccountBalance = costOfSalesAccountBalanceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(costOfSalesAccountBalance);
    }

    /**
     * {@code DELETE  /cost-of-sales-account-balances/:id} : delete the "id" costOfSalesAccountBalance.
     *
     * @param id the id of the costOfSalesAccountBalance to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cost-of-sales-account-balances/{id}")
    public ResponseEntity<Void> deleteCostOfSalesAccountBalance(@PathVariable Long id) {
        log.debug("REST request to delete CostOfSalesAccountBalance : {}", id);
        costOfSalesAccountBalanceService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}

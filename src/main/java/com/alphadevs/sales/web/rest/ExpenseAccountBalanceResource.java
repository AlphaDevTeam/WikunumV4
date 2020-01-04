package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.domain.ExpenseAccountBalance;
import com.alphadevs.sales.service.ExpenseAccountBalanceService;
import com.alphadevs.sales.web.rest.errors.BadRequestAlertException;
import com.alphadevs.sales.service.dto.ExpenseAccountBalanceCriteria;
import com.alphadevs.sales.service.ExpenseAccountBalanceQueryService;

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
 * REST controller for managing {@link com.alphadevs.sales.domain.ExpenseAccountBalance}.
 */
@RestController
@RequestMapping("/api")
public class ExpenseAccountBalanceResource {

    private final Logger log = LoggerFactory.getLogger(ExpenseAccountBalanceResource.class);

    private static final String ENTITY_NAME = "expenseAccountBalance";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExpenseAccountBalanceService expenseAccountBalanceService;

    private final ExpenseAccountBalanceQueryService expenseAccountBalanceQueryService;

    public ExpenseAccountBalanceResource(ExpenseAccountBalanceService expenseAccountBalanceService, ExpenseAccountBalanceQueryService expenseAccountBalanceQueryService) {
        this.expenseAccountBalanceService = expenseAccountBalanceService;
        this.expenseAccountBalanceQueryService = expenseAccountBalanceQueryService;
    }

    /**
     * {@code POST  /expense-account-balances} : Create a new expenseAccountBalance.
     *
     * @param expenseAccountBalance the expenseAccountBalance to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new expenseAccountBalance, or with status {@code 400 (Bad Request)} if the expenseAccountBalance has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/expense-account-balances")
    public ResponseEntity<ExpenseAccountBalance> createExpenseAccountBalance(@Valid @RequestBody ExpenseAccountBalance expenseAccountBalance) throws URISyntaxException {
        log.debug("REST request to save ExpenseAccountBalance : {}", expenseAccountBalance);
        if (expenseAccountBalance.getId() != null) {
            throw new BadRequestAlertException("A new expenseAccountBalance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExpenseAccountBalance result = expenseAccountBalanceService.save(expenseAccountBalance);
        return ResponseEntity.created(new URI("/api/expense-account-balances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /expense-account-balances} : Updates an existing expenseAccountBalance.
     *
     * @param expenseAccountBalance the expenseAccountBalance to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated expenseAccountBalance,
     * or with status {@code 400 (Bad Request)} if the expenseAccountBalance is not valid,
     * or with status {@code 500 (Internal Server Error)} if the expenseAccountBalance couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/expense-account-balances")
    public ResponseEntity<ExpenseAccountBalance> updateExpenseAccountBalance(@Valid @RequestBody ExpenseAccountBalance expenseAccountBalance) throws URISyntaxException {
        log.debug("REST request to update ExpenseAccountBalance : {}", expenseAccountBalance);
        if (expenseAccountBalance.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ExpenseAccountBalance result = expenseAccountBalanceService.save(expenseAccountBalance);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, expenseAccountBalance.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /expense-account-balances} : get all the expenseAccountBalances.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of expenseAccountBalances in body.
     */
    @GetMapping("/expense-account-balances")
    public ResponseEntity<List<ExpenseAccountBalance>> getAllExpenseAccountBalances(ExpenseAccountBalanceCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ExpenseAccountBalances by criteria: {}", criteria);
        Page<ExpenseAccountBalance> page = expenseAccountBalanceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /expense-account-balances/count} : count all the expenseAccountBalances.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/expense-account-balances/count")
    public ResponseEntity<Long> countExpenseAccountBalances(ExpenseAccountBalanceCriteria criteria) {
        log.debug("REST request to count ExpenseAccountBalances by criteria: {}", criteria);
        return ResponseEntity.ok().body(expenseAccountBalanceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /expense-account-balances/:id} : get the "id" expenseAccountBalance.
     *
     * @param id the id of the expenseAccountBalance to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the expenseAccountBalance, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/expense-account-balances/{id}")
    public ResponseEntity<ExpenseAccountBalance> getExpenseAccountBalance(@PathVariable Long id) {
        log.debug("REST request to get ExpenseAccountBalance : {}", id);
        Optional<ExpenseAccountBalance> expenseAccountBalance = expenseAccountBalanceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(expenseAccountBalance);
    }

    /**
     * {@code DELETE  /expense-account-balances/:id} : delete the "id" expenseAccountBalance.
     *
     * @param id the id of the expenseAccountBalance to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/expense-account-balances/{id}")
    public ResponseEntity<Void> deleteExpenseAccountBalance(@PathVariable Long id) {
        log.debug("REST request to delete ExpenseAccountBalance : {}", id);
        expenseAccountBalanceService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}

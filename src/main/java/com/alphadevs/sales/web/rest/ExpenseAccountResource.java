package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.domain.ExpenseAccount;
import com.alphadevs.sales.service.ExpenseAccountService;
import com.alphadevs.sales.web.rest.errors.BadRequestAlertException;
import com.alphadevs.sales.service.dto.ExpenseAccountCriteria;
import com.alphadevs.sales.service.ExpenseAccountQueryService;

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
 * REST controller for managing {@link com.alphadevs.sales.domain.ExpenseAccount}.
 */
@RestController
@RequestMapping("/api")
public class ExpenseAccountResource {

    private final Logger log = LoggerFactory.getLogger(ExpenseAccountResource.class);

    private static final String ENTITY_NAME = "expenseAccount";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExpenseAccountService expenseAccountService;

    private final ExpenseAccountQueryService expenseAccountQueryService;

    public ExpenseAccountResource(ExpenseAccountService expenseAccountService, ExpenseAccountQueryService expenseAccountQueryService) {
        this.expenseAccountService = expenseAccountService;
        this.expenseAccountQueryService = expenseAccountQueryService;
    }

    /**
     * {@code POST  /expense-accounts} : Create a new expenseAccount.
     *
     * @param expenseAccount the expenseAccount to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new expenseAccount, or with status {@code 400 (Bad Request)} if the expenseAccount has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/expense-accounts")
    public ResponseEntity<ExpenseAccount> createExpenseAccount(@Valid @RequestBody ExpenseAccount expenseAccount) throws URISyntaxException {
        log.debug("REST request to save ExpenseAccount : {}", expenseAccount);
        if (expenseAccount.getId() != null) {
            throw new BadRequestAlertException("A new expenseAccount cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExpenseAccount result = expenseAccountService.save(expenseAccount);
        return ResponseEntity.created(new URI("/api/expense-accounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /expense-accounts} : Updates an existing expenseAccount.
     *
     * @param expenseAccount the expenseAccount to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated expenseAccount,
     * or with status {@code 400 (Bad Request)} if the expenseAccount is not valid,
     * or with status {@code 500 (Internal Server Error)} if the expenseAccount couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/expense-accounts")
    public ResponseEntity<ExpenseAccount> updateExpenseAccount(@Valid @RequestBody ExpenseAccount expenseAccount) throws URISyntaxException {
        log.debug("REST request to update ExpenseAccount : {}", expenseAccount);
        if (expenseAccount.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ExpenseAccount result = expenseAccountService.save(expenseAccount);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, expenseAccount.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /expense-accounts} : get all the expenseAccounts.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of expenseAccounts in body.
     */
    @GetMapping("/expense-accounts")
    public ResponseEntity<List<ExpenseAccount>> getAllExpenseAccounts(ExpenseAccountCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ExpenseAccounts by criteria: {}", criteria);
        Page<ExpenseAccount> page = expenseAccountQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /expense-accounts/count} : count all the expenseAccounts.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/expense-accounts/count")
    public ResponseEntity<Long> countExpenseAccounts(ExpenseAccountCriteria criteria) {
        log.debug("REST request to count ExpenseAccounts by criteria: {}", criteria);
        return ResponseEntity.ok().body(expenseAccountQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /expense-accounts/:id} : get the "id" expenseAccount.
     *
     * @param id the id of the expenseAccount to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the expenseAccount, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/expense-accounts/{id}")
    public ResponseEntity<ExpenseAccount> getExpenseAccount(@PathVariable Long id) {
        log.debug("REST request to get ExpenseAccount : {}", id);
        Optional<ExpenseAccount> expenseAccount = expenseAccountService.findOne(id);
        return ResponseUtil.wrapOrNotFound(expenseAccount);
    }

    /**
     * {@code DELETE  /expense-accounts/:id} : delete the "id" expenseAccount.
     *
     * @param id the id of the expenseAccount to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/expense-accounts/{id}")
    public ResponseEntity<Void> deleteExpenseAccount(@PathVariable Long id) {
        log.debug("REST request to delete ExpenseAccount : {}", id);
        expenseAccountService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}

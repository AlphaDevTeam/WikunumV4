package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.domain.CashPaymentVoucherExpense;
import com.alphadevs.sales.service.CashPaymentVoucherExpenseService;
import com.alphadevs.sales.web.rest.errors.BadRequestAlertException;
import com.alphadevs.sales.service.dto.CashPaymentVoucherExpenseCriteria;
import com.alphadevs.sales.service.CashPaymentVoucherExpenseQueryService;

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
 * REST controller for managing {@link com.alphadevs.sales.domain.CashPaymentVoucherExpense}.
 */
@RestController
@RequestMapping("/api")
public class CashPaymentVoucherExpenseResource {

    private final Logger log = LoggerFactory.getLogger(CashPaymentVoucherExpenseResource.class);

    private static final String ENTITY_NAME = "cashPaymentVoucherExpense";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CashPaymentVoucherExpenseService cashPaymentVoucherExpenseService;

    private final CashPaymentVoucherExpenseQueryService cashPaymentVoucherExpenseQueryService;

    public CashPaymentVoucherExpenseResource(CashPaymentVoucherExpenseService cashPaymentVoucherExpenseService, CashPaymentVoucherExpenseQueryService cashPaymentVoucherExpenseQueryService) {
        this.cashPaymentVoucherExpenseService = cashPaymentVoucherExpenseService;
        this.cashPaymentVoucherExpenseQueryService = cashPaymentVoucherExpenseQueryService;
    }

    /**
     * {@code POST  /cash-payment-voucher-expenses} : Create a new cashPaymentVoucherExpense.
     *
     * @param cashPaymentVoucherExpense the cashPaymentVoucherExpense to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cashPaymentVoucherExpense, or with status {@code 400 (Bad Request)} if the cashPaymentVoucherExpense has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cash-payment-voucher-expenses")
    public ResponseEntity<CashPaymentVoucherExpense> createCashPaymentVoucherExpense(@Valid @RequestBody CashPaymentVoucherExpense cashPaymentVoucherExpense) throws URISyntaxException {
        log.debug("REST request to save CashPaymentVoucherExpense : {}", cashPaymentVoucherExpense);
        if (cashPaymentVoucherExpense.getId() != null) {
            throw new BadRequestAlertException("A new cashPaymentVoucherExpense cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CashPaymentVoucherExpense result = cashPaymentVoucherExpenseService.save(cashPaymentVoucherExpense);
        return ResponseEntity.created(new URI("/api/cash-payment-voucher-expenses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cash-payment-voucher-expenses} : Updates an existing cashPaymentVoucherExpense.
     *
     * @param cashPaymentVoucherExpense the cashPaymentVoucherExpense to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cashPaymentVoucherExpense,
     * or with status {@code 400 (Bad Request)} if the cashPaymentVoucherExpense is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cashPaymentVoucherExpense couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cash-payment-voucher-expenses")
    public ResponseEntity<CashPaymentVoucherExpense> updateCashPaymentVoucherExpense(@Valid @RequestBody CashPaymentVoucherExpense cashPaymentVoucherExpense) throws URISyntaxException {
        log.debug("REST request to update CashPaymentVoucherExpense : {}", cashPaymentVoucherExpense);
        if (cashPaymentVoucherExpense.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CashPaymentVoucherExpense result = cashPaymentVoucherExpenseService.save(cashPaymentVoucherExpense);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cashPaymentVoucherExpense.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /cash-payment-voucher-expenses} : get all the cashPaymentVoucherExpenses.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cashPaymentVoucherExpenses in body.
     */
    @GetMapping("/cash-payment-voucher-expenses")
    public ResponseEntity<List<CashPaymentVoucherExpense>> getAllCashPaymentVoucherExpenses(CashPaymentVoucherExpenseCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CashPaymentVoucherExpenses by criteria: {}", criteria);
        Page<CashPaymentVoucherExpense> page = cashPaymentVoucherExpenseQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /cash-payment-voucher-expenses/count} : count all the cashPaymentVoucherExpenses.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/cash-payment-voucher-expenses/count")
    public ResponseEntity<Long> countCashPaymentVoucherExpenses(CashPaymentVoucherExpenseCriteria criteria) {
        log.debug("REST request to count CashPaymentVoucherExpenses by criteria: {}", criteria);
        return ResponseEntity.ok().body(cashPaymentVoucherExpenseQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cash-payment-voucher-expenses/:id} : get the "id" cashPaymentVoucherExpense.
     *
     * @param id the id of the cashPaymentVoucherExpense to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cashPaymentVoucherExpense, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cash-payment-voucher-expenses/{id}")
    public ResponseEntity<CashPaymentVoucherExpense> getCashPaymentVoucherExpense(@PathVariable Long id) {
        log.debug("REST request to get CashPaymentVoucherExpense : {}", id);
        Optional<CashPaymentVoucherExpense> cashPaymentVoucherExpense = cashPaymentVoucherExpenseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cashPaymentVoucherExpense);
    }

    /**
     * {@code DELETE  /cash-payment-voucher-expenses/:id} : delete the "id" cashPaymentVoucherExpense.
     *
     * @param id the id of the cashPaymentVoucherExpense to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cash-payment-voucher-expenses/{id}")
    public ResponseEntity<Void> deleteCashPaymentVoucherExpense(@PathVariable Long id) {
        log.debug("REST request to delete CashPaymentVoucherExpense : {}", id);
        cashPaymentVoucherExpenseService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}

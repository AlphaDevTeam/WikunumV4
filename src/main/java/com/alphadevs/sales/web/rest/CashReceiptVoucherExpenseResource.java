package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.domain.CashReceiptVoucherExpense;
import com.alphadevs.sales.service.CashReceiptVoucherExpenseService;
import com.alphadevs.sales.web.rest.errors.BadRequestAlertException;
import com.alphadevs.sales.service.dto.CashReceiptVoucherExpenseCriteria;
import com.alphadevs.sales.service.CashReceiptVoucherExpenseQueryService;

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
 * REST controller for managing {@link com.alphadevs.sales.domain.CashReceiptVoucherExpense}.
 */
@RestController
@RequestMapping("/api")
public class CashReceiptVoucherExpenseResource {

    private final Logger log = LoggerFactory.getLogger(CashReceiptVoucherExpenseResource.class);

    private static final String ENTITY_NAME = "cashReceiptVoucherExpense";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CashReceiptVoucherExpenseService cashReceiptVoucherExpenseService;

    private final CashReceiptVoucherExpenseQueryService cashReceiptVoucherExpenseQueryService;

    public CashReceiptVoucherExpenseResource(CashReceiptVoucherExpenseService cashReceiptVoucherExpenseService, CashReceiptVoucherExpenseQueryService cashReceiptVoucherExpenseQueryService) {
        this.cashReceiptVoucherExpenseService = cashReceiptVoucherExpenseService;
        this.cashReceiptVoucherExpenseQueryService = cashReceiptVoucherExpenseQueryService;
    }

    /**
     * {@code POST  /cash-receipt-voucher-expenses} : Create a new cashReceiptVoucherExpense.
     *
     * @param cashReceiptVoucherExpense the cashReceiptVoucherExpense to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cashReceiptVoucherExpense, or with status {@code 400 (Bad Request)} if the cashReceiptVoucherExpense has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cash-receipt-voucher-expenses")
    public ResponseEntity<CashReceiptVoucherExpense> createCashReceiptVoucherExpense(@Valid @RequestBody CashReceiptVoucherExpense cashReceiptVoucherExpense) throws URISyntaxException {
        log.debug("REST request to save CashReceiptVoucherExpense : {}", cashReceiptVoucherExpense);
        if (cashReceiptVoucherExpense.getId() != null) {
            throw new BadRequestAlertException("A new cashReceiptVoucherExpense cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CashReceiptVoucherExpense result = cashReceiptVoucherExpenseService.save(cashReceiptVoucherExpense);
        return ResponseEntity.created(new URI("/api/cash-receipt-voucher-expenses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cash-receipt-voucher-expenses} : Updates an existing cashReceiptVoucherExpense.
     *
     * @param cashReceiptVoucherExpense the cashReceiptVoucherExpense to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cashReceiptVoucherExpense,
     * or with status {@code 400 (Bad Request)} if the cashReceiptVoucherExpense is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cashReceiptVoucherExpense couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cash-receipt-voucher-expenses")
    public ResponseEntity<CashReceiptVoucherExpense> updateCashReceiptVoucherExpense(@Valid @RequestBody CashReceiptVoucherExpense cashReceiptVoucherExpense) throws URISyntaxException {
        log.debug("REST request to update CashReceiptVoucherExpense : {}", cashReceiptVoucherExpense);
        if (cashReceiptVoucherExpense.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CashReceiptVoucherExpense result = cashReceiptVoucherExpenseService.save(cashReceiptVoucherExpense);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cashReceiptVoucherExpense.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /cash-receipt-voucher-expenses} : get all the cashReceiptVoucherExpenses.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cashReceiptVoucherExpenses in body.
     */
    @GetMapping("/cash-receipt-voucher-expenses")
    public ResponseEntity<List<CashReceiptVoucherExpense>> getAllCashReceiptVoucherExpenses(CashReceiptVoucherExpenseCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CashReceiptVoucherExpenses by criteria: {}", criteria);
        Page<CashReceiptVoucherExpense> page = cashReceiptVoucherExpenseQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /cash-receipt-voucher-expenses/count} : count all the cashReceiptVoucherExpenses.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/cash-receipt-voucher-expenses/count")
    public ResponseEntity<Long> countCashReceiptVoucherExpenses(CashReceiptVoucherExpenseCriteria criteria) {
        log.debug("REST request to count CashReceiptVoucherExpenses by criteria: {}", criteria);
        return ResponseEntity.ok().body(cashReceiptVoucherExpenseQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cash-receipt-voucher-expenses/:id} : get the "id" cashReceiptVoucherExpense.
     *
     * @param id the id of the cashReceiptVoucherExpense to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cashReceiptVoucherExpense, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cash-receipt-voucher-expenses/{id}")
    public ResponseEntity<CashReceiptVoucherExpense> getCashReceiptVoucherExpense(@PathVariable Long id) {
        log.debug("REST request to get CashReceiptVoucherExpense : {}", id);
        Optional<CashReceiptVoucherExpense> cashReceiptVoucherExpense = cashReceiptVoucherExpenseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cashReceiptVoucherExpense);
    }

    /**
     * {@code DELETE  /cash-receipt-voucher-expenses/:id} : delete the "id" cashReceiptVoucherExpense.
     *
     * @param id the id of the cashReceiptVoucherExpense to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cash-receipt-voucher-expenses/{id}")
    public ResponseEntity<Void> deleteCashReceiptVoucherExpense(@PathVariable Long id) {
        log.debug("REST request to delete CashReceiptVoucherExpense : {}", id);
        cashReceiptVoucherExpenseService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}

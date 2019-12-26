package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.domain.CashReceiptVoucherSupplier;
import com.alphadevs.sales.service.CashReceiptVoucherSupplierService;
import com.alphadevs.sales.web.rest.errors.BadRequestAlertException;
import com.alphadevs.sales.service.dto.CashReceiptVoucherSupplierCriteria;
import com.alphadevs.sales.service.CashReceiptVoucherSupplierQueryService;

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
 * REST controller for managing {@link com.alphadevs.sales.domain.CashReceiptVoucherSupplier}.
 */
@RestController
@RequestMapping("/api")
public class CashReceiptVoucherSupplierResource {

    private final Logger log = LoggerFactory.getLogger(CashReceiptVoucherSupplierResource.class);

    private static final String ENTITY_NAME = "cashReceiptVoucherSupplier";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CashReceiptVoucherSupplierService cashReceiptVoucherSupplierService;

    private final CashReceiptVoucherSupplierQueryService cashReceiptVoucherSupplierQueryService;

    public CashReceiptVoucherSupplierResource(CashReceiptVoucherSupplierService cashReceiptVoucherSupplierService, CashReceiptVoucherSupplierQueryService cashReceiptVoucherSupplierQueryService) {
        this.cashReceiptVoucherSupplierService = cashReceiptVoucherSupplierService;
        this.cashReceiptVoucherSupplierQueryService = cashReceiptVoucherSupplierQueryService;
    }

    /**
     * {@code POST  /cash-receipt-voucher-suppliers} : Create a new cashReceiptVoucherSupplier.
     *
     * @param cashReceiptVoucherSupplier the cashReceiptVoucherSupplier to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cashReceiptVoucherSupplier, or with status {@code 400 (Bad Request)} if the cashReceiptVoucherSupplier has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cash-receipt-voucher-suppliers")
    public ResponseEntity<CashReceiptVoucherSupplier> createCashReceiptVoucherSupplier(@Valid @RequestBody CashReceiptVoucherSupplier cashReceiptVoucherSupplier) throws URISyntaxException {
        log.debug("REST request to save CashReceiptVoucherSupplier : {}", cashReceiptVoucherSupplier);
        if (cashReceiptVoucherSupplier.getId() != null) {
            throw new BadRequestAlertException("A new cashReceiptVoucherSupplier cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CashReceiptVoucherSupplier result = cashReceiptVoucherSupplierService.save(cashReceiptVoucherSupplier);
        return ResponseEntity.created(new URI("/api/cash-receipt-voucher-suppliers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cash-receipt-voucher-suppliers} : Updates an existing cashReceiptVoucherSupplier.
     *
     * @param cashReceiptVoucherSupplier the cashReceiptVoucherSupplier to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cashReceiptVoucherSupplier,
     * or with status {@code 400 (Bad Request)} if the cashReceiptVoucherSupplier is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cashReceiptVoucherSupplier couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cash-receipt-voucher-suppliers")
    public ResponseEntity<CashReceiptVoucherSupplier> updateCashReceiptVoucherSupplier(@Valid @RequestBody CashReceiptVoucherSupplier cashReceiptVoucherSupplier) throws URISyntaxException {
        log.debug("REST request to update CashReceiptVoucherSupplier : {}", cashReceiptVoucherSupplier);
        if (cashReceiptVoucherSupplier.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CashReceiptVoucherSupplier result = cashReceiptVoucherSupplierService.save(cashReceiptVoucherSupplier);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cashReceiptVoucherSupplier.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /cash-receipt-voucher-suppliers} : get all the cashReceiptVoucherSuppliers.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cashReceiptVoucherSuppliers in body.
     */
    @GetMapping("/cash-receipt-voucher-suppliers")
    public ResponseEntity<List<CashReceiptVoucherSupplier>> getAllCashReceiptVoucherSuppliers(CashReceiptVoucherSupplierCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CashReceiptVoucherSuppliers by criteria: {}", criteria);
        Page<CashReceiptVoucherSupplier> page = cashReceiptVoucherSupplierQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /cash-receipt-voucher-suppliers/count} : count all the cashReceiptVoucherSuppliers.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/cash-receipt-voucher-suppliers/count")
    public ResponseEntity<Long> countCashReceiptVoucherSuppliers(CashReceiptVoucherSupplierCriteria criteria) {
        log.debug("REST request to count CashReceiptVoucherSuppliers by criteria: {}", criteria);
        return ResponseEntity.ok().body(cashReceiptVoucherSupplierQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cash-receipt-voucher-suppliers/:id} : get the "id" cashReceiptVoucherSupplier.
     *
     * @param id the id of the cashReceiptVoucherSupplier to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cashReceiptVoucherSupplier, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cash-receipt-voucher-suppliers/{id}")
    public ResponseEntity<CashReceiptVoucherSupplier> getCashReceiptVoucherSupplier(@PathVariable Long id) {
        log.debug("REST request to get CashReceiptVoucherSupplier : {}", id);
        Optional<CashReceiptVoucherSupplier> cashReceiptVoucherSupplier = cashReceiptVoucherSupplierService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cashReceiptVoucherSupplier);
    }

    /**
     * {@code DELETE  /cash-receipt-voucher-suppliers/:id} : delete the "id" cashReceiptVoucherSupplier.
     *
     * @param id the id of the cashReceiptVoucherSupplier to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cash-receipt-voucher-suppliers/{id}")
    public ResponseEntity<Void> deleteCashReceiptVoucherSupplier(@PathVariable Long id) {
        log.debug("REST request to delete CashReceiptVoucherSupplier : {}", id);
        cashReceiptVoucherSupplierService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}

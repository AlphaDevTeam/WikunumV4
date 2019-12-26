package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.domain.CashPaymentVoucherSupplier;
import com.alphadevs.sales.service.CashPaymentVoucherSupplierService;
import com.alphadevs.sales.web.rest.errors.BadRequestAlertException;
import com.alphadevs.sales.service.dto.CashPaymentVoucherSupplierCriteria;
import com.alphadevs.sales.service.CashPaymentVoucherSupplierQueryService;

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
 * REST controller for managing {@link com.alphadevs.sales.domain.CashPaymentVoucherSupplier}.
 */
@RestController
@RequestMapping("/api")
public class CashPaymentVoucherSupplierResource {

    private final Logger log = LoggerFactory.getLogger(CashPaymentVoucherSupplierResource.class);

    private static final String ENTITY_NAME = "cashPaymentVoucherSupplier";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CashPaymentVoucherSupplierService cashPaymentVoucherSupplierService;

    private final CashPaymentVoucherSupplierQueryService cashPaymentVoucherSupplierQueryService;

    public CashPaymentVoucherSupplierResource(CashPaymentVoucherSupplierService cashPaymentVoucherSupplierService, CashPaymentVoucherSupplierQueryService cashPaymentVoucherSupplierQueryService) {
        this.cashPaymentVoucherSupplierService = cashPaymentVoucherSupplierService;
        this.cashPaymentVoucherSupplierQueryService = cashPaymentVoucherSupplierQueryService;
    }

    /**
     * {@code POST  /cash-payment-voucher-suppliers} : Create a new cashPaymentVoucherSupplier.
     *
     * @param cashPaymentVoucherSupplier the cashPaymentVoucherSupplier to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cashPaymentVoucherSupplier, or with status {@code 400 (Bad Request)} if the cashPaymentVoucherSupplier has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cash-payment-voucher-suppliers")
    public ResponseEntity<CashPaymentVoucherSupplier> createCashPaymentVoucherSupplier(@Valid @RequestBody CashPaymentVoucherSupplier cashPaymentVoucherSupplier) throws URISyntaxException {
        log.debug("REST request to save CashPaymentVoucherSupplier : {}", cashPaymentVoucherSupplier);
        if (cashPaymentVoucherSupplier.getId() != null) {
            throw new BadRequestAlertException("A new cashPaymentVoucherSupplier cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CashPaymentVoucherSupplier result = cashPaymentVoucherSupplierService.save(cashPaymentVoucherSupplier);
        return ResponseEntity.created(new URI("/api/cash-payment-voucher-suppliers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cash-payment-voucher-suppliers} : Updates an existing cashPaymentVoucherSupplier.
     *
     * @param cashPaymentVoucherSupplier the cashPaymentVoucherSupplier to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cashPaymentVoucherSupplier,
     * or with status {@code 400 (Bad Request)} if the cashPaymentVoucherSupplier is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cashPaymentVoucherSupplier couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cash-payment-voucher-suppliers")
    public ResponseEntity<CashPaymentVoucherSupplier> updateCashPaymentVoucherSupplier(@Valid @RequestBody CashPaymentVoucherSupplier cashPaymentVoucherSupplier) throws URISyntaxException {
        log.debug("REST request to update CashPaymentVoucherSupplier : {}", cashPaymentVoucherSupplier);
        if (cashPaymentVoucherSupplier.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CashPaymentVoucherSupplier result = cashPaymentVoucherSupplierService.save(cashPaymentVoucherSupplier);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cashPaymentVoucherSupplier.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /cash-payment-voucher-suppliers} : get all the cashPaymentVoucherSuppliers.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cashPaymentVoucherSuppliers in body.
     */
    @GetMapping("/cash-payment-voucher-suppliers")
    public ResponseEntity<List<CashPaymentVoucherSupplier>> getAllCashPaymentVoucherSuppliers(CashPaymentVoucherSupplierCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CashPaymentVoucherSuppliers by criteria: {}", criteria);
        Page<CashPaymentVoucherSupplier> page = cashPaymentVoucherSupplierQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /cash-payment-voucher-suppliers/count} : count all the cashPaymentVoucherSuppliers.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/cash-payment-voucher-suppliers/count")
    public ResponseEntity<Long> countCashPaymentVoucherSuppliers(CashPaymentVoucherSupplierCriteria criteria) {
        log.debug("REST request to count CashPaymentVoucherSuppliers by criteria: {}", criteria);
        return ResponseEntity.ok().body(cashPaymentVoucherSupplierQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cash-payment-voucher-suppliers/:id} : get the "id" cashPaymentVoucherSupplier.
     *
     * @param id the id of the cashPaymentVoucherSupplier to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cashPaymentVoucherSupplier, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cash-payment-voucher-suppliers/{id}")
    public ResponseEntity<CashPaymentVoucherSupplier> getCashPaymentVoucherSupplier(@PathVariable Long id) {
        log.debug("REST request to get CashPaymentVoucherSupplier : {}", id);
        Optional<CashPaymentVoucherSupplier> cashPaymentVoucherSupplier = cashPaymentVoucherSupplierService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cashPaymentVoucherSupplier);
    }

    /**
     * {@code DELETE  /cash-payment-voucher-suppliers/:id} : delete the "id" cashPaymentVoucherSupplier.
     *
     * @param id the id of the cashPaymentVoucherSupplier to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cash-payment-voucher-suppliers/{id}")
    public ResponseEntity<Void> deleteCashPaymentVoucherSupplier(@PathVariable Long id) {
        log.debug("REST request to delete CashPaymentVoucherSupplier : {}", id);
        cashPaymentVoucherSupplierService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}

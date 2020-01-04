package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.domain.CashReceiptVoucherCustomer;
import com.alphadevs.sales.service.CashReceiptVoucherCustomerService;
import com.alphadevs.sales.web.rest.errors.BadRequestAlertException;
import com.alphadevs.sales.service.dto.CashReceiptVoucherCustomerCriteria;
import com.alphadevs.sales.service.CashReceiptVoucherCustomerQueryService;

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
 * REST controller for managing {@link com.alphadevs.sales.domain.CashReceiptVoucherCustomer}.
 */
@RestController
@RequestMapping("/api")
public class CashReceiptVoucherCustomerResource {

    private final Logger log = LoggerFactory.getLogger(CashReceiptVoucherCustomerResource.class);

    private static final String ENTITY_NAME = "cashReceiptVoucherCustomer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CashReceiptVoucherCustomerService cashReceiptVoucherCustomerService;

    private final CashReceiptVoucherCustomerQueryService cashReceiptVoucherCustomerQueryService;

    public CashReceiptVoucherCustomerResource(CashReceiptVoucherCustomerService cashReceiptVoucherCustomerService, CashReceiptVoucherCustomerQueryService cashReceiptVoucherCustomerQueryService) {
        this.cashReceiptVoucherCustomerService = cashReceiptVoucherCustomerService;
        this.cashReceiptVoucherCustomerQueryService = cashReceiptVoucherCustomerQueryService;
    }

    /**
     * {@code POST  /cash-receipt-voucher-customers} : Create a new cashReceiptVoucherCustomer.
     *
     * @param cashReceiptVoucherCustomer the cashReceiptVoucherCustomer to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cashReceiptVoucherCustomer, or with status {@code 400 (Bad Request)} if the cashReceiptVoucherCustomer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cash-receipt-voucher-customers")
    public ResponseEntity<CashReceiptVoucherCustomer> createCashReceiptVoucherCustomer(@Valid @RequestBody CashReceiptVoucherCustomer cashReceiptVoucherCustomer) throws URISyntaxException {
        log.debug("REST request to save CashReceiptVoucherCustomer : {}", cashReceiptVoucherCustomer);
        if (cashReceiptVoucherCustomer.getId() != null) {
            throw new BadRequestAlertException("A new cashReceiptVoucherCustomer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CashReceiptVoucherCustomer result = cashReceiptVoucherCustomerService.save(cashReceiptVoucherCustomer);
        return ResponseEntity.created(new URI("/api/cash-receipt-voucher-customers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cash-receipt-voucher-customers} : Updates an existing cashReceiptVoucherCustomer.
     *
     * @param cashReceiptVoucherCustomer the cashReceiptVoucherCustomer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cashReceiptVoucherCustomer,
     * or with status {@code 400 (Bad Request)} if the cashReceiptVoucherCustomer is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cashReceiptVoucherCustomer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cash-receipt-voucher-customers")
    public ResponseEntity<CashReceiptVoucherCustomer> updateCashReceiptVoucherCustomer(@Valid @RequestBody CashReceiptVoucherCustomer cashReceiptVoucherCustomer) throws URISyntaxException {
        log.debug("REST request to update CashReceiptVoucherCustomer : {}", cashReceiptVoucherCustomer);
        if (cashReceiptVoucherCustomer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CashReceiptVoucherCustomer result = cashReceiptVoucherCustomerService.save(cashReceiptVoucherCustomer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cashReceiptVoucherCustomer.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /cash-receipt-voucher-customers} : get all the cashReceiptVoucherCustomers.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cashReceiptVoucherCustomers in body.
     */
    @GetMapping("/cash-receipt-voucher-customers")
    public ResponseEntity<List<CashReceiptVoucherCustomer>> getAllCashReceiptVoucherCustomers(CashReceiptVoucherCustomerCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CashReceiptVoucherCustomers by criteria: {}", criteria);
        Page<CashReceiptVoucherCustomer> page = cashReceiptVoucherCustomerQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /cash-receipt-voucher-customers/count} : count all the cashReceiptVoucherCustomers.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/cash-receipt-voucher-customers/count")
    public ResponseEntity<Long> countCashReceiptVoucherCustomers(CashReceiptVoucherCustomerCriteria criteria) {
        log.debug("REST request to count CashReceiptVoucherCustomers by criteria: {}", criteria);
        return ResponseEntity.ok().body(cashReceiptVoucherCustomerQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cash-receipt-voucher-customers/:id} : get the "id" cashReceiptVoucherCustomer.
     *
     * @param id the id of the cashReceiptVoucherCustomer to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cashReceiptVoucherCustomer, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cash-receipt-voucher-customers/{id}")
    public ResponseEntity<CashReceiptVoucherCustomer> getCashReceiptVoucherCustomer(@PathVariable Long id) {
        log.debug("REST request to get CashReceiptVoucherCustomer : {}", id);
        Optional<CashReceiptVoucherCustomer> cashReceiptVoucherCustomer = cashReceiptVoucherCustomerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cashReceiptVoucherCustomer);
    }

    /**
     * {@code DELETE  /cash-receipt-voucher-customers/:id} : delete the "id" cashReceiptVoucherCustomer.
     *
     * @param id the id of the cashReceiptVoucherCustomer to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cash-receipt-voucher-customers/{id}")
    public ResponseEntity<Void> deleteCashReceiptVoucherCustomer(@PathVariable Long id) {
        log.debug("REST request to delete CashReceiptVoucherCustomer : {}", id);
        cashReceiptVoucherCustomerService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}

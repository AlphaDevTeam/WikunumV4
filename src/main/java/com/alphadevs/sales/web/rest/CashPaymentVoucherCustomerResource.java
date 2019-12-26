package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.domain.CashPaymentVoucherCustomer;
import com.alphadevs.sales.service.CashPaymentVoucherCustomerService;
import com.alphadevs.sales.web.rest.errors.BadRequestAlertException;
import com.alphadevs.sales.service.dto.CashPaymentVoucherCustomerCriteria;
import com.alphadevs.sales.service.CashPaymentVoucherCustomerQueryService;

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
 * REST controller for managing {@link com.alphadevs.sales.domain.CashPaymentVoucherCustomer}.
 */
@RestController
@RequestMapping("/api")
public class CashPaymentVoucherCustomerResource {

    private final Logger log = LoggerFactory.getLogger(CashPaymentVoucherCustomerResource.class);

    private static final String ENTITY_NAME = "cashPaymentVoucherCustomer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CashPaymentVoucherCustomerService cashPaymentVoucherCustomerService;

    private final CashPaymentVoucherCustomerQueryService cashPaymentVoucherCustomerQueryService;

    public CashPaymentVoucherCustomerResource(CashPaymentVoucherCustomerService cashPaymentVoucherCustomerService, CashPaymentVoucherCustomerQueryService cashPaymentVoucherCustomerQueryService) {
        this.cashPaymentVoucherCustomerService = cashPaymentVoucherCustomerService;
        this.cashPaymentVoucherCustomerQueryService = cashPaymentVoucherCustomerQueryService;
    }

    /**
     * {@code POST  /cash-payment-voucher-customers} : Create a new cashPaymentVoucherCustomer.
     *
     * @param cashPaymentVoucherCustomer the cashPaymentVoucherCustomer to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cashPaymentVoucherCustomer, or with status {@code 400 (Bad Request)} if the cashPaymentVoucherCustomer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cash-payment-voucher-customers")
    public ResponseEntity<CashPaymentVoucherCustomer> createCashPaymentVoucherCustomer(@Valid @RequestBody CashPaymentVoucherCustomer cashPaymentVoucherCustomer) throws URISyntaxException {
        log.debug("REST request to save CashPaymentVoucherCustomer : {}", cashPaymentVoucherCustomer);
        if (cashPaymentVoucherCustomer.getId() != null) {
            throw new BadRequestAlertException("A new cashPaymentVoucherCustomer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CashPaymentVoucherCustomer result = cashPaymentVoucherCustomerService.save(cashPaymentVoucherCustomer);
        return ResponseEntity.created(new URI("/api/cash-payment-voucher-customers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cash-payment-voucher-customers} : Updates an existing cashPaymentVoucherCustomer.
     *
     * @param cashPaymentVoucherCustomer the cashPaymentVoucherCustomer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cashPaymentVoucherCustomer,
     * or with status {@code 400 (Bad Request)} if the cashPaymentVoucherCustomer is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cashPaymentVoucherCustomer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cash-payment-voucher-customers")
    public ResponseEntity<CashPaymentVoucherCustomer> updateCashPaymentVoucherCustomer(@Valid @RequestBody CashPaymentVoucherCustomer cashPaymentVoucherCustomer) throws URISyntaxException {
        log.debug("REST request to update CashPaymentVoucherCustomer : {}", cashPaymentVoucherCustomer);
        if (cashPaymentVoucherCustomer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CashPaymentVoucherCustomer result = cashPaymentVoucherCustomerService.save(cashPaymentVoucherCustomer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cashPaymentVoucherCustomer.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /cash-payment-voucher-customers} : get all the cashPaymentVoucherCustomers.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cashPaymentVoucherCustomers in body.
     */
    @GetMapping("/cash-payment-voucher-customers")
    public ResponseEntity<List<CashPaymentVoucherCustomer>> getAllCashPaymentVoucherCustomers(CashPaymentVoucherCustomerCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CashPaymentVoucherCustomers by criteria: {}", criteria);
        Page<CashPaymentVoucherCustomer> page = cashPaymentVoucherCustomerQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /cash-payment-voucher-customers/count} : count all the cashPaymentVoucherCustomers.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/cash-payment-voucher-customers/count")
    public ResponseEntity<Long> countCashPaymentVoucherCustomers(CashPaymentVoucherCustomerCriteria criteria) {
        log.debug("REST request to count CashPaymentVoucherCustomers by criteria: {}", criteria);
        return ResponseEntity.ok().body(cashPaymentVoucherCustomerQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cash-payment-voucher-customers/:id} : get the "id" cashPaymentVoucherCustomer.
     *
     * @param id the id of the cashPaymentVoucherCustomer to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cashPaymentVoucherCustomer, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cash-payment-voucher-customers/{id}")
    public ResponseEntity<CashPaymentVoucherCustomer> getCashPaymentVoucherCustomer(@PathVariable Long id) {
        log.debug("REST request to get CashPaymentVoucherCustomer : {}", id);
        Optional<CashPaymentVoucherCustomer> cashPaymentVoucherCustomer = cashPaymentVoucherCustomerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cashPaymentVoucherCustomer);
    }

    /**
     * {@code DELETE  /cash-payment-voucher-customers/:id} : delete the "id" cashPaymentVoucherCustomer.
     *
     * @param id the id of the cashPaymentVoucherCustomer to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cash-payment-voucher-customers/{id}")
    public ResponseEntity<Void> deleteCashPaymentVoucherCustomer(@PathVariable Long id) {
        log.debug("REST request to delete CashPaymentVoucherCustomer : {}", id);
        cashPaymentVoucherCustomerService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}

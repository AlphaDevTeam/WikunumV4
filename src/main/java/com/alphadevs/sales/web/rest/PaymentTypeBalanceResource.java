package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.domain.PaymentTypeBalance;
import com.alphadevs.sales.service.PaymentTypeBalanceService;
import com.alphadevs.sales.web.rest.errors.BadRequestAlertException;
import com.alphadevs.sales.service.dto.PaymentTypeBalanceCriteria;
import com.alphadevs.sales.service.PaymentTypeBalanceQueryService;

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
 * REST controller for managing {@link com.alphadevs.sales.domain.PaymentTypeBalance}.
 */
@RestController
@RequestMapping("/api")
public class PaymentTypeBalanceResource {

    private final Logger log = LoggerFactory.getLogger(PaymentTypeBalanceResource.class);

    private static final String ENTITY_NAME = "paymentTypeBalance";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaymentTypeBalanceService paymentTypeBalanceService;

    private final PaymentTypeBalanceQueryService paymentTypeBalanceQueryService;

    public PaymentTypeBalanceResource(PaymentTypeBalanceService paymentTypeBalanceService, PaymentTypeBalanceQueryService paymentTypeBalanceQueryService) {
        this.paymentTypeBalanceService = paymentTypeBalanceService;
        this.paymentTypeBalanceQueryService = paymentTypeBalanceQueryService;
    }

    /**
     * {@code POST  /payment-type-balances} : Create a new paymentTypeBalance.
     *
     * @param paymentTypeBalance the paymentTypeBalance to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paymentTypeBalance, or with status {@code 400 (Bad Request)} if the paymentTypeBalance has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/payment-type-balances")
    public ResponseEntity<PaymentTypeBalance> createPaymentTypeBalance(@Valid @RequestBody PaymentTypeBalance paymentTypeBalance) throws URISyntaxException {
        log.debug("REST request to save PaymentTypeBalance : {}", paymentTypeBalance);
        if (paymentTypeBalance.getId() != null) {
            throw new BadRequestAlertException("A new paymentTypeBalance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PaymentTypeBalance result = paymentTypeBalanceService.save(paymentTypeBalance);
        return ResponseEntity.created(new URI("/api/payment-type-balances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /payment-type-balances} : Updates an existing paymentTypeBalance.
     *
     * @param paymentTypeBalance the paymentTypeBalance to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentTypeBalance,
     * or with status {@code 400 (Bad Request)} if the paymentTypeBalance is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paymentTypeBalance couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/payment-type-balances")
    public ResponseEntity<PaymentTypeBalance> updatePaymentTypeBalance(@Valid @RequestBody PaymentTypeBalance paymentTypeBalance) throws URISyntaxException {
        log.debug("REST request to update PaymentTypeBalance : {}", paymentTypeBalance);
        if (paymentTypeBalance.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PaymentTypeBalance result = paymentTypeBalanceService.save(paymentTypeBalance);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, paymentTypeBalance.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /payment-type-balances} : get all the paymentTypeBalances.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paymentTypeBalances in body.
     */
    @GetMapping("/payment-type-balances")
    public ResponseEntity<List<PaymentTypeBalance>> getAllPaymentTypeBalances(PaymentTypeBalanceCriteria criteria, Pageable pageable) {
        log.debug("REST request to get PaymentTypeBalances by criteria: {}", criteria);
        Page<PaymentTypeBalance> page = paymentTypeBalanceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /payment-type-balances/count} : count all the paymentTypeBalances.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/payment-type-balances/count")
    public ResponseEntity<Long> countPaymentTypeBalances(PaymentTypeBalanceCriteria criteria) {
        log.debug("REST request to count PaymentTypeBalances by criteria: {}", criteria);
        return ResponseEntity.ok().body(paymentTypeBalanceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /payment-type-balances/:id} : get the "id" paymentTypeBalance.
     *
     * @param id the id of the paymentTypeBalance to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paymentTypeBalance, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/payment-type-balances/{id}")
    public ResponseEntity<PaymentTypeBalance> getPaymentTypeBalance(@PathVariable Long id) {
        log.debug("REST request to get PaymentTypeBalance : {}", id);
        Optional<PaymentTypeBalance> paymentTypeBalance = paymentTypeBalanceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paymentTypeBalance);
    }

    /**
     * {@code DELETE  /payment-type-balances/:id} : delete the "id" paymentTypeBalance.
     *
     * @param id the id of the paymentTypeBalance to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/payment-type-balances/{id}")
    public ResponseEntity<Void> deletePaymentTypeBalance(@PathVariable Long id) {
        log.debug("REST request to delete PaymentTypeBalance : {}", id);
        paymentTypeBalanceService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}

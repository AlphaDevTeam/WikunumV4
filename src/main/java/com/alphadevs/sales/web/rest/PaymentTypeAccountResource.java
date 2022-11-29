package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.domain.PaymentTypeAccount;
import com.alphadevs.sales.service.PaymentTypeAccountService;
import com.alphadevs.sales.web.rest.errors.BadRequestAlertException;
import com.alphadevs.sales.service.dto.PaymentTypeAccountCriteria;
import com.alphadevs.sales.service.PaymentTypeAccountQueryService;

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
 * REST controller for managing {@link com.alphadevs.sales.domain.PaymentTypeAccount}.
 */
@RestController
@RequestMapping("/api")
public class PaymentTypeAccountResource {

    private final Logger log = LoggerFactory.getLogger(PaymentTypeAccountResource.class);

    private static final String ENTITY_NAME = "paymentTypeAccount";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaymentTypeAccountService paymentTypeAccountService;

    private final PaymentTypeAccountQueryService paymentTypeAccountQueryService;

    public PaymentTypeAccountResource(PaymentTypeAccountService paymentTypeAccountService, PaymentTypeAccountQueryService paymentTypeAccountQueryService) {
        this.paymentTypeAccountService = paymentTypeAccountService;
        this.paymentTypeAccountQueryService = paymentTypeAccountQueryService;
    }

    /**
     * {@code POST  /payment-type-accounts} : Create a new paymentTypeAccount.
     *
     * @param paymentTypeAccount the paymentTypeAccount to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paymentTypeAccount, or with status {@code 400 (Bad Request)} if the paymentTypeAccount has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/payment-type-accounts")
    public ResponseEntity<PaymentTypeAccount> createPaymentTypeAccount(@Valid @RequestBody PaymentTypeAccount paymentTypeAccount) throws URISyntaxException {
        log.debug("REST request to save PaymentTypeAccount : {}", paymentTypeAccount);
        if (paymentTypeAccount.getId() != null) {
            throw new BadRequestAlertException("A new paymentTypeAccount cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PaymentTypeAccount result = paymentTypeAccountService.save(paymentTypeAccount);
        return ResponseEntity.created(new URI("/api/payment-type-accounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /payment-type-accounts} : Updates an existing paymentTypeAccount.
     *
     * @param paymentTypeAccount the paymentTypeAccount to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentTypeAccount,
     * or with status {@code 400 (Bad Request)} if the paymentTypeAccount is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paymentTypeAccount couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/payment-type-accounts")
    public ResponseEntity<PaymentTypeAccount> updatePaymentTypeAccount(@Valid @RequestBody PaymentTypeAccount paymentTypeAccount) throws URISyntaxException {
        log.debug("REST request to update PaymentTypeAccount : {}", paymentTypeAccount);
        if (paymentTypeAccount.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PaymentTypeAccount result = paymentTypeAccountService.save(paymentTypeAccount);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, paymentTypeAccount.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /payment-type-accounts} : get all the paymentTypeAccounts.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paymentTypeAccounts in body.
     */
    @GetMapping("/payment-type-accounts")
    public ResponseEntity<List<PaymentTypeAccount>> getAllPaymentTypeAccounts(PaymentTypeAccountCriteria criteria, Pageable pageable) {
        log.debug("REST request to get PaymentTypeAccounts by criteria: {}", criteria);
        Page<PaymentTypeAccount> page = paymentTypeAccountQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /payment-type-accounts/count} : count all the paymentTypeAccounts.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/payment-type-accounts/count")
    public ResponseEntity<Long> countPaymentTypeAccounts(PaymentTypeAccountCriteria criteria) {
        log.debug("REST request to count PaymentTypeAccounts by criteria: {}", criteria);
        return ResponseEntity.ok().body(paymentTypeAccountQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /payment-type-accounts/:id} : get the "id" paymentTypeAccount.
     *
     * @param id the id of the paymentTypeAccount to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paymentTypeAccount, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/payment-type-accounts/{id}")
    public ResponseEntity<PaymentTypeAccount> getPaymentTypeAccount(@PathVariable Long id) {
        log.debug("REST request to get PaymentTypeAccount : {}", id);
        Optional<PaymentTypeAccount> paymentTypeAccount = paymentTypeAccountService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paymentTypeAccount);
    }

    /**
     * {@code DELETE  /payment-type-accounts/:id} : delete the "id" paymentTypeAccount.
     *
     * @param id the id of the paymentTypeAccount to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/payment-type-accounts/{id}")
    public ResponseEntity<Void> deletePaymentTypeAccount(@PathVariable Long id) {
        log.debug("REST request to delete PaymentTypeAccount : {}", id);
        paymentTypeAccountService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}

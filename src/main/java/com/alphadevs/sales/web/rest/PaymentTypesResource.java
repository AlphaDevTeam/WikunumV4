package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.domain.PaymentTypes;
import com.alphadevs.sales.service.PaymentTypesService;
import com.alphadevs.sales.web.rest.errors.BadRequestAlertException;
import com.alphadevs.sales.service.dto.PaymentTypesCriteria;
import com.alphadevs.sales.service.PaymentTypesQueryService;

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
 * REST controller for managing {@link com.alphadevs.sales.domain.PaymentTypes}.
 */
@RestController
@RequestMapping("/api")
public class PaymentTypesResource {

    private final Logger log = LoggerFactory.getLogger(PaymentTypesResource.class);

    private static final String ENTITY_NAME = "paymentTypes";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaymentTypesService paymentTypesService;

    private final PaymentTypesQueryService paymentTypesQueryService;

    public PaymentTypesResource(PaymentTypesService paymentTypesService, PaymentTypesQueryService paymentTypesQueryService) {
        this.paymentTypesService = paymentTypesService;
        this.paymentTypesQueryService = paymentTypesQueryService;
    }

    /**
     * {@code POST  /payment-types} : Create a new paymentTypes.
     *
     * @param paymentTypes the paymentTypes to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paymentTypes, or with status {@code 400 (Bad Request)} if the paymentTypes has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/payment-types")
    public ResponseEntity<PaymentTypes> createPaymentTypes(@Valid @RequestBody PaymentTypes paymentTypes) throws URISyntaxException {
        log.debug("REST request to save PaymentTypes : {}", paymentTypes);
        if (paymentTypes.getId() != null) {
            throw new BadRequestAlertException("A new paymentTypes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PaymentTypes result = paymentTypesService.save(paymentTypes);
        return ResponseEntity.created(new URI("/api/payment-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /payment-types} : Updates an existing paymentTypes.
     *
     * @param paymentTypes the paymentTypes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentTypes,
     * or with status {@code 400 (Bad Request)} if the paymentTypes is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paymentTypes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/payment-types")
    public ResponseEntity<PaymentTypes> updatePaymentTypes(@Valid @RequestBody PaymentTypes paymentTypes) throws URISyntaxException {
        log.debug("REST request to update PaymentTypes : {}", paymentTypes);
        if (paymentTypes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PaymentTypes result = paymentTypesService.save(paymentTypes);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, paymentTypes.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /payment-types} : get all the paymentTypes.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paymentTypes in body.
     */
    @GetMapping("/payment-types")
    public ResponseEntity<List<PaymentTypes>> getAllPaymentTypes(PaymentTypesCriteria criteria, Pageable pageable) {
        log.debug("REST request to get PaymentTypes by criteria: {}", criteria);
        Page<PaymentTypes> page = paymentTypesQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /payment-types/count} : count all the paymentTypes.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/payment-types/count")
    public ResponseEntity<Long> countPaymentTypes(PaymentTypesCriteria criteria) {
        log.debug("REST request to count PaymentTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(paymentTypesQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /payment-types/:id} : get the "id" paymentTypes.
     *
     * @param id the id of the paymentTypes to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paymentTypes, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/payment-types/{id}")
    public ResponseEntity<PaymentTypes> getPaymentTypes(@PathVariable Long id) {
        log.debug("REST request to get PaymentTypes : {}", id);
        Optional<PaymentTypes> paymentTypes = paymentTypesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paymentTypes);
    }

    /**
     * {@code DELETE  /payment-types/:id} : delete the "id" paymentTypes.
     *
     * @param id the id of the paymentTypes to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/payment-types/{id}")
    public ResponseEntity<Void> deletePaymentTypes(@PathVariable Long id) {
        log.debug("REST request to delete PaymentTypes : {}", id);
        paymentTypesService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}

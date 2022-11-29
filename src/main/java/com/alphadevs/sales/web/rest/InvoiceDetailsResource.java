package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.domain.InvoiceDetails;
import com.alphadevs.sales.service.InvoiceDetailsService;
import com.alphadevs.sales.web.rest.errors.BadRequestAlertException;
import com.alphadevs.sales.service.dto.InvoiceDetailsCriteria;
import com.alphadevs.sales.service.InvoiceDetailsQueryService;

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
 * REST controller for managing {@link com.alphadevs.sales.domain.InvoiceDetails}.
 */
@RestController
@RequestMapping("/api")
public class InvoiceDetailsResource {

    private final Logger log = LoggerFactory.getLogger(InvoiceDetailsResource.class);

    private static final String ENTITY_NAME = "invoiceDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InvoiceDetailsService invoiceDetailsService;

    private final InvoiceDetailsQueryService invoiceDetailsQueryService;

    public InvoiceDetailsResource(InvoiceDetailsService invoiceDetailsService, InvoiceDetailsQueryService invoiceDetailsQueryService) {
        this.invoiceDetailsService = invoiceDetailsService;
        this.invoiceDetailsQueryService = invoiceDetailsQueryService;
    }

    /**
     * {@code POST  /invoice-details} : Create a new invoiceDetails.
     *
     * @param invoiceDetails the invoiceDetails to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new invoiceDetails, or with status {@code 400 (Bad Request)} if the invoiceDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/invoice-details")
    public ResponseEntity<InvoiceDetails> createInvoiceDetails(@Valid @RequestBody InvoiceDetails invoiceDetails) throws URISyntaxException {
        log.debug("REST request to save InvoiceDetails : {}", invoiceDetails);
        if (invoiceDetails.getId() != null) {
            throw new BadRequestAlertException("A new invoiceDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InvoiceDetails result = invoiceDetailsService.save(invoiceDetails);
        return ResponseEntity.created(new URI("/api/invoice-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /invoice-details} : Updates an existing invoiceDetails.
     *
     * @param invoiceDetails the invoiceDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoiceDetails,
     * or with status {@code 400 (Bad Request)} if the invoiceDetails is not valid,
     * or with status {@code 500 (Internal Server Error)} if the invoiceDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/invoice-details")
    public ResponseEntity<InvoiceDetails> updateInvoiceDetails(@Valid @RequestBody InvoiceDetails invoiceDetails) throws URISyntaxException {
        log.debug("REST request to update InvoiceDetails : {}", invoiceDetails);
        if (invoiceDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        InvoiceDetails result = invoiceDetailsService.save(invoiceDetails);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, invoiceDetails.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /invoice-details} : get all the invoiceDetails.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of invoiceDetails in body.
     */
    @GetMapping("/invoice-details")
    public ResponseEntity<List<InvoiceDetails>> getAllInvoiceDetails(InvoiceDetailsCriteria criteria, Pageable pageable) {
        log.debug("REST request to get InvoiceDetails by criteria: {}", criteria);
        Page<InvoiceDetails> page = invoiceDetailsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /invoice-details/count} : count all the invoiceDetails.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/invoice-details/count")
    public ResponseEntity<Long> countInvoiceDetails(InvoiceDetailsCriteria criteria) {
        log.debug("REST request to count InvoiceDetails by criteria: {}", criteria);
        return ResponseEntity.ok().body(invoiceDetailsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /invoice-details/:id} : get the "id" invoiceDetails.
     *
     * @param id the id of the invoiceDetails to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the invoiceDetails, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/invoice-details/{id}")
    public ResponseEntity<InvoiceDetails> getInvoiceDetails(@PathVariable Long id) {
        log.debug("REST request to get InvoiceDetails : {}", id);
        Optional<InvoiceDetails> invoiceDetails = invoiceDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(invoiceDetails);
    }

    /**
     * {@code DELETE  /invoice-details/:id} : delete the "id" invoiceDetails.
     *
     * @param id the id of the invoiceDetails to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/invoice-details/{id}")
    public ResponseEntity<Void> deleteInvoiceDetails(@PathVariable Long id) {
        log.debug("REST request to delete InvoiceDetails : {}", id);
        invoiceDetailsService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}

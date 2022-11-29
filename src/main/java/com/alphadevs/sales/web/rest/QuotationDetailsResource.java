package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.domain.QuotationDetails;
import com.alphadevs.sales.service.QuotationDetailsService;
import com.alphadevs.sales.web.rest.errors.BadRequestAlertException;
import com.alphadevs.sales.service.dto.QuotationDetailsCriteria;
import com.alphadevs.sales.service.QuotationDetailsQueryService;

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
 * REST controller for managing {@link com.alphadevs.sales.domain.QuotationDetails}.
 */
@RestController
@RequestMapping("/api")
public class QuotationDetailsResource {

    private final Logger log = LoggerFactory.getLogger(QuotationDetailsResource.class);

    private static final String ENTITY_NAME = "quotationDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QuotationDetailsService quotationDetailsService;

    private final QuotationDetailsQueryService quotationDetailsQueryService;

    public QuotationDetailsResource(QuotationDetailsService quotationDetailsService, QuotationDetailsQueryService quotationDetailsQueryService) {
        this.quotationDetailsService = quotationDetailsService;
        this.quotationDetailsQueryService = quotationDetailsQueryService;
    }

    /**
     * {@code POST  /quotation-details} : Create a new quotationDetails.
     *
     * @param quotationDetails the quotationDetails to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new quotationDetails, or with status {@code 400 (Bad Request)} if the quotationDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/quotation-details")
    public ResponseEntity<QuotationDetails> createQuotationDetails(@Valid @RequestBody QuotationDetails quotationDetails) throws URISyntaxException {
        log.debug("REST request to save QuotationDetails : {}", quotationDetails);
        if (quotationDetails.getId() != null) {
            throw new BadRequestAlertException("A new quotationDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        QuotationDetails result = quotationDetailsService.save(quotationDetails);
        return ResponseEntity.created(new URI("/api/quotation-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /quotation-details} : Updates an existing quotationDetails.
     *
     * @param quotationDetails the quotationDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quotationDetails,
     * or with status {@code 400 (Bad Request)} if the quotationDetails is not valid,
     * or with status {@code 500 (Internal Server Error)} if the quotationDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/quotation-details")
    public ResponseEntity<QuotationDetails> updateQuotationDetails(@Valid @RequestBody QuotationDetails quotationDetails) throws URISyntaxException {
        log.debug("REST request to update QuotationDetails : {}", quotationDetails);
        if (quotationDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        QuotationDetails result = quotationDetailsService.save(quotationDetails);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, quotationDetails.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /quotation-details} : get all the quotationDetails.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of quotationDetails in body.
     */
    @GetMapping("/quotation-details")
    public ResponseEntity<List<QuotationDetails>> getAllQuotationDetails(QuotationDetailsCriteria criteria, Pageable pageable) {
        log.debug("REST request to get QuotationDetails by criteria: {}", criteria);
        Page<QuotationDetails> page = quotationDetailsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /quotation-details/count} : count all the quotationDetails.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/quotation-details/count")
    public ResponseEntity<Long> countQuotationDetails(QuotationDetailsCriteria criteria) {
        log.debug("REST request to count QuotationDetails by criteria: {}", criteria);
        return ResponseEntity.ok().body(quotationDetailsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /quotation-details/:id} : get the "id" quotationDetails.
     *
     * @param id the id of the quotationDetails to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the quotationDetails, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/quotation-details/{id}")
    public ResponseEntity<QuotationDetails> getQuotationDetails(@PathVariable Long id) {
        log.debug("REST request to get QuotationDetails : {}", id);
        Optional<QuotationDetails> quotationDetails = quotationDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(quotationDetails);
    }

    /**
     * {@code DELETE  /quotation-details/:id} : delete the "id" quotationDetails.
     *
     * @param id the id of the quotationDetails to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/quotation-details/{id}")
    public ResponseEntity<Void> deleteQuotationDetails(@PathVariable Long id) {
        log.debug("REST request to delete QuotationDetails : {}", id);
        quotationDetailsService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}

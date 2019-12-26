package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.domain.SupplierAccount;
import com.alphadevs.sales.service.SupplierAccountService;
import com.alphadevs.sales.web.rest.errors.BadRequestAlertException;
import com.alphadevs.sales.service.dto.SupplierAccountCriteria;
import com.alphadevs.sales.service.SupplierAccountQueryService;

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
 * REST controller for managing {@link com.alphadevs.sales.domain.SupplierAccount}.
 */
@RestController
@RequestMapping("/api")
public class SupplierAccountResource {

    private final Logger log = LoggerFactory.getLogger(SupplierAccountResource.class);

    private static final String ENTITY_NAME = "supplierAccount";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SupplierAccountService supplierAccountService;

    private final SupplierAccountQueryService supplierAccountQueryService;

    public SupplierAccountResource(SupplierAccountService supplierAccountService, SupplierAccountQueryService supplierAccountQueryService) {
        this.supplierAccountService = supplierAccountService;
        this.supplierAccountQueryService = supplierAccountQueryService;
    }

    /**
     * {@code POST  /supplier-accounts} : Create a new supplierAccount.
     *
     * @param supplierAccount the supplierAccount to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new supplierAccount, or with status {@code 400 (Bad Request)} if the supplierAccount has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/supplier-accounts")
    public ResponseEntity<SupplierAccount> createSupplierAccount(@Valid @RequestBody SupplierAccount supplierAccount) throws URISyntaxException {
        log.debug("REST request to save SupplierAccount : {}", supplierAccount);
        if (supplierAccount.getId() != null) {
            throw new BadRequestAlertException("A new supplierAccount cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SupplierAccount result = supplierAccountService.save(supplierAccount);
        return ResponseEntity.created(new URI("/api/supplier-accounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /supplier-accounts} : Updates an existing supplierAccount.
     *
     * @param supplierAccount the supplierAccount to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated supplierAccount,
     * or with status {@code 400 (Bad Request)} if the supplierAccount is not valid,
     * or with status {@code 500 (Internal Server Error)} if the supplierAccount couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/supplier-accounts")
    public ResponseEntity<SupplierAccount> updateSupplierAccount(@Valid @RequestBody SupplierAccount supplierAccount) throws URISyntaxException {
        log.debug("REST request to update SupplierAccount : {}", supplierAccount);
        if (supplierAccount.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SupplierAccount result = supplierAccountService.save(supplierAccount);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, supplierAccount.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /supplier-accounts} : get all the supplierAccounts.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of supplierAccounts in body.
     */
    @GetMapping("/supplier-accounts")
    public ResponseEntity<List<SupplierAccount>> getAllSupplierAccounts(SupplierAccountCriteria criteria, Pageable pageable) {
        log.debug("REST request to get SupplierAccounts by criteria: {}", criteria);
        Page<SupplierAccount> page = supplierAccountQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /supplier-accounts/count} : count all the supplierAccounts.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/supplier-accounts/count")
    public ResponseEntity<Long> countSupplierAccounts(SupplierAccountCriteria criteria) {
        log.debug("REST request to count SupplierAccounts by criteria: {}", criteria);
        return ResponseEntity.ok().body(supplierAccountQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /supplier-accounts/:id} : get the "id" supplierAccount.
     *
     * @param id the id of the supplierAccount to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the supplierAccount, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/supplier-accounts/{id}")
    public ResponseEntity<SupplierAccount> getSupplierAccount(@PathVariable Long id) {
        log.debug("REST request to get SupplierAccount : {}", id);
        Optional<SupplierAccount> supplierAccount = supplierAccountService.findOne(id);
        return ResponseUtil.wrapOrNotFound(supplierAccount);
    }

    /**
     * {@code DELETE  /supplier-accounts/:id} : delete the "id" supplierAccount.
     *
     * @param id the id of the supplierAccount to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/supplier-accounts/{id}")
    public ResponseEntity<Void> deleteSupplierAccount(@PathVariable Long id) {
        log.debug("REST request to delete SupplierAccount : {}", id);
        supplierAccountService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}

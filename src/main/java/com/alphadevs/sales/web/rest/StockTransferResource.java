package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.domain.StockTransfer;
import com.alphadevs.sales.service.StockTransferService;
import com.alphadevs.sales.web.rest.errors.BadRequestAlertException;
import com.alphadevs.sales.service.dto.StockTransferCriteria;
import com.alphadevs.sales.service.StockTransferQueryService;

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
 * REST controller for managing {@link com.alphadevs.sales.domain.StockTransfer}.
 */
@RestController
@RequestMapping("/api")
public class StockTransferResource {

    private final Logger log = LoggerFactory.getLogger(StockTransferResource.class);

    private static final String ENTITY_NAME = "stockTransfer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StockTransferService stockTransferService;

    private final StockTransferQueryService stockTransferQueryService;

    public StockTransferResource(StockTransferService stockTransferService, StockTransferQueryService stockTransferQueryService) {
        this.stockTransferService = stockTransferService;
        this.stockTransferQueryService = stockTransferQueryService;
    }

    /**
     * {@code POST  /stock-transfers} : Create a new stockTransfer.
     *
     * @param stockTransfer the stockTransfer to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new stockTransfer, or with status {@code 400 (Bad Request)} if the stockTransfer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/stock-transfers")
    public ResponseEntity<StockTransfer> createStockTransfer(@Valid @RequestBody StockTransfer stockTransfer) throws URISyntaxException {
        log.debug("REST request to save StockTransfer : {}", stockTransfer);
        if (stockTransfer.getId() != null) {
            throw new BadRequestAlertException("A new stockTransfer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StockTransfer result = stockTransferService.save(stockTransfer);
        return ResponseEntity.created(new URI("/api/stock-transfers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /stock-transfers} : Updates an existing stockTransfer.
     *
     * @param stockTransfer the stockTransfer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stockTransfer,
     * or with status {@code 400 (Bad Request)} if the stockTransfer is not valid,
     * or with status {@code 500 (Internal Server Error)} if the stockTransfer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/stock-transfers")
    public ResponseEntity<StockTransfer> updateStockTransfer(@Valid @RequestBody StockTransfer stockTransfer) throws URISyntaxException {
        log.debug("REST request to update StockTransfer : {}", stockTransfer);
        if (stockTransfer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        StockTransfer result = stockTransferService.save(stockTransfer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, stockTransfer.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /stock-transfers} : get all the stockTransfers.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of stockTransfers in body.
     */
    @GetMapping("/stock-transfers")
    public ResponseEntity<List<StockTransfer>> getAllStockTransfers(StockTransferCriteria criteria, Pageable pageable) {
        log.debug("REST request to get StockTransfers by criteria: {}", criteria);
        Page<StockTransfer> page = stockTransferQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /stock-transfers/count} : count all the stockTransfers.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/stock-transfers/count")
    public ResponseEntity<Long> countStockTransfers(StockTransferCriteria criteria) {
        log.debug("REST request to count StockTransfers by criteria: {}", criteria);
        return ResponseEntity.ok().body(stockTransferQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /stock-transfers/:id} : get the "id" stockTransfer.
     *
     * @param id the id of the stockTransfer to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the stockTransfer, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/stock-transfers/{id}")
    public ResponseEntity<StockTransfer> getStockTransfer(@PathVariable Long id) {
        log.debug("REST request to get StockTransfer : {}", id);
        Optional<StockTransfer> stockTransfer = stockTransferService.findOne(id);
        return ResponseUtil.wrapOrNotFound(stockTransfer);
    }

    /**
     * {@code DELETE  /stock-transfers/:id} : delete the "id" stockTransfer.
     *
     * @param id the id of the stockTransfer to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/stock-transfers/{id}")
    public ResponseEntity<Void> deleteStockTransfer(@PathVariable Long id) {
        log.debug("REST request to delete StockTransfer : {}", id);
        stockTransferService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}

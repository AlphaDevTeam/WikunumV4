package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.domain.ItemAddOns;
import com.alphadevs.sales.service.ItemAddOnsService;
import com.alphadevs.sales.web.rest.errors.BadRequestAlertException;
import com.alphadevs.sales.service.dto.ItemAddOnsCriteria;
import com.alphadevs.sales.service.ItemAddOnsQueryService;

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
 * REST controller for managing {@link com.alphadevs.sales.domain.ItemAddOns}.
 */
@RestController
@RequestMapping("/api")
public class ItemAddOnsResource {

    private final Logger log = LoggerFactory.getLogger(ItemAddOnsResource.class);

    private static final String ENTITY_NAME = "itemAddOns";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ItemAddOnsService itemAddOnsService;

    private final ItemAddOnsQueryService itemAddOnsQueryService;

    public ItemAddOnsResource(ItemAddOnsService itemAddOnsService, ItemAddOnsQueryService itemAddOnsQueryService) {
        this.itemAddOnsService = itemAddOnsService;
        this.itemAddOnsQueryService = itemAddOnsQueryService;
    }

    /**
     * {@code POST  /item-add-ons} : Create a new itemAddOns.
     *
     * @param itemAddOns the itemAddOns to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new itemAddOns, or with status {@code 400 (Bad Request)} if the itemAddOns has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/item-add-ons")
    public ResponseEntity<ItemAddOns> createItemAddOns(@Valid @RequestBody ItemAddOns itemAddOns) throws URISyntaxException {
        log.debug("REST request to save ItemAddOns : {}", itemAddOns);
        if (itemAddOns.getId() != null) {
            throw new BadRequestAlertException("A new itemAddOns cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ItemAddOns result = itemAddOnsService.save(itemAddOns);
        return ResponseEntity.created(new URI("/api/item-add-ons/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /item-add-ons} : Updates an existing itemAddOns.
     *
     * @param itemAddOns the itemAddOns to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemAddOns,
     * or with status {@code 400 (Bad Request)} if the itemAddOns is not valid,
     * or with status {@code 500 (Internal Server Error)} if the itemAddOns couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/item-add-ons")
    public ResponseEntity<ItemAddOns> updateItemAddOns(@Valid @RequestBody ItemAddOns itemAddOns) throws URISyntaxException {
        log.debug("REST request to update ItemAddOns : {}", itemAddOns);
        if (itemAddOns.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ItemAddOns result = itemAddOnsService.save(itemAddOns);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, itemAddOns.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /item-add-ons} : get all the itemAddOns.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of itemAddOns in body.
     */
    @GetMapping("/item-add-ons")
    public ResponseEntity<List<ItemAddOns>> getAllItemAddOns(ItemAddOnsCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ItemAddOns by criteria: {}", criteria);
        Page<ItemAddOns> page = itemAddOnsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /item-add-ons/count} : count all the itemAddOns.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/item-add-ons/count")
    public ResponseEntity<Long> countItemAddOns(ItemAddOnsCriteria criteria) {
        log.debug("REST request to count ItemAddOns by criteria: {}", criteria);
        return ResponseEntity.ok().body(itemAddOnsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /item-add-ons/:id} : get the "id" itemAddOns.
     *
     * @param id the id of the itemAddOns to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the itemAddOns, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/item-add-ons/{id}")
    public ResponseEntity<ItemAddOns> getItemAddOns(@PathVariable Long id) {
        log.debug("REST request to get ItemAddOns : {}", id);
        Optional<ItemAddOns> itemAddOns = itemAddOnsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(itemAddOns);
    }

    /**
     * {@code DELETE  /item-add-ons/:id} : delete the "id" itemAddOns.
     *
     * @param id the id of the itemAddOns to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/item-add-ons/{id}")
    public ResponseEntity<Void> deleteItemAddOns(@PathVariable Long id) {
        log.debug("REST request to delete ItemAddOns : {}", id);
        itemAddOnsService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}

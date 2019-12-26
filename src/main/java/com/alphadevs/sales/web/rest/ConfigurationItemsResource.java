package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.domain.ConfigurationItems;
import com.alphadevs.sales.service.ConfigurationItemsService;
import com.alphadevs.sales.web.rest.errors.BadRequestAlertException;
import com.alphadevs.sales.service.dto.ConfigurationItemsCriteria;
import com.alphadevs.sales.service.ConfigurationItemsQueryService;

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
 * REST controller for managing {@link com.alphadevs.sales.domain.ConfigurationItems}.
 */
@RestController
@RequestMapping("/api")
public class ConfigurationItemsResource {

    private final Logger log = LoggerFactory.getLogger(ConfigurationItemsResource.class);

    private static final String ENTITY_NAME = "configurationItems";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConfigurationItemsService configurationItemsService;

    private final ConfigurationItemsQueryService configurationItemsQueryService;

    public ConfigurationItemsResource(ConfigurationItemsService configurationItemsService, ConfigurationItemsQueryService configurationItemsQueryService) {
        this.configurationItemsService = configurationItemsService;
        this.configurationItemsQueryService = configurationItemsQueryService;
    }

    /**
     * {@code POST  /configuration-items} : Create a new configurationItems.
     *
     * @param configurationItems the configurationItems to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new configurationItems, or with status {@code 400 (Bad Request)} if the configurationItems has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/configuration-items")
    public ResponseEntity<ConfigurationItems> createConfigurationItems(@Valid @RequestBody ConfigurationItems configurationItems) throws URISyntaxException {
        log.debug("REST request to save ConfigurationItems : {}", configurationItems);
        if (configurationItems.getId() != null) {
            throw new BadRequestAlertException("A new configurationItems cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ConfigurationItems result = configurationItemsService.save(configurationItems);
        return ResponseEntity.created(new URI("/api/configuration-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /configuration-items} : Updates an existing configurationItems.
     *
     * @param configurationItems the configurationItems to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated configurationItems,
     * or with status {@code 400 (Bad Request)} if the configurationItems is not valid,
     * or with status {@code 500 (Internal Server Error)} if the configurationItems couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/configuration-items")
    public ResponseEntity<ConfigurationItems> updateConfigurationItems(@Valid @RequestBody ConfigurationItems configurationItems) throws URISyntaxException {
        log.debug("REST request to update ConfigurationItems : {}", configurationItems);
        if (configurationItems.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ConfigurationItems result = configurationItemsService.save(configurationItems);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, configurationItems.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /configuration-items} : get all the configurationItems.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of configurationItems in body.
     */
    @GetMapping("/configuration-items")
    public ResponseEntity<List<ConfigurationItems>> getAllConfigurationItems(ConfigurationItemsCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ConfigurationItems by criteria: {}", criteria);
        Page<ConfigurationItems> page = configurationItemsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /configuration-items/count} : count all the configurationItems.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/configuration-items/count")
    public ResponseEntity<Long> countConfigurationItems(ConfigurationItemsCriteria criteria) {
        log.debug("REST request to count ConfigurationItems by criteria: {}", criteria);
        return ResponseEntity.ok().body(configurationItemsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /configuration-items/:id} : get the "id" configurationItems.
     *
     * @param id the id of the configurationItems to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the configurationItems, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/configuration-items/{id}")
    public ResponseEntity<ConfigurationItems> getConfigurationItems(@PathVariable Long id) {
        log.debug("REST request to get ConfigurationItems : {}", id);
        Optional<ConfigurationItems> configurationItems = configurationItemsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(configurationItems);
    }

    /**
     * {@code DELETE  /configuration-items/:id} : delete the "id" configurationItems.
     *
     * @param id the id of the configurationItems to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/configuration-items/{id}")
    public ResponseEntity<Void> deleteConfigurationItems(@PathVariable Long id) {
        log.debug("REST request to delete ConfigurationItems : {}", id);
        configurationItemsService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}

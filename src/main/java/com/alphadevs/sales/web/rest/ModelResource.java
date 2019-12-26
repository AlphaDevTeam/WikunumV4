package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.domain.Model;
import com.alphadevs.sales.service.ModelService;
import com.alphadevs.sales.web.rest.errors.BadRequestAlertException;
import com.alphadevs.sales.service.dto.ModelCriteria;
import com.alphadevs.sales.service.ModelQueryService;

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
 * REST controller for managing {@link com.alphadevs.sales.domain.Model}.
 */
@RestController
@RequestMapping("/api")
public class ModelResource {

    private final Logger log = LoggerFactory.getLogger(ModelResource.class);

    private static final String ENTITY_NAME = "model";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ModelService modelService;

    private final ModelQueryService modelQueryService;

    public ModelResource(ModelService modelService, ModelQueryService modelQueryService) {
        this.modelService = modelService;
        this.modelQueryService = modelQueryService;
    }

    /**
     * {@code POST  /models} : Create a new model.
     *
     * @param model the model to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new model, or with status {@code 400 (Bad Request)} if the model has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/models")
    public ResponseEntity<Model> createModel(@Valid @RequestBody Model model) throws URISyntaxException {
        log.debug("REST request to save Model : {}", model);
        if (model.getId() != null) {
            throw new BadRequestAlertException("A new model cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Model result = modelService.save(model);
        return ResponseEntity.created(new URI("/api/models/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /models} : Updates an existing model.
     *
     * @param model the model to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated model,
     * or with status {@code 400 (Bad Request)} if the model is not valid,
     * or with status {@code 500 (Internal Server Error)} if the model couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/models")
    public ResponseEntity<Model> updateModel(@Valid @RequestBody Model model) throws URISyntaxException {
        log.debug("REST request to update Model : {}", model);
        if (model.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Model result = modelService.save(model);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, model.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /models} : get all the models.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of models in body.
     */
    @GetMapping("/models")
    public ResponseEntity<List<Model>> getAllModels(ModelCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Models by criteria: {}", criteria);
        Page<Model> page = modelQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /models/count} : count all the models.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/models/count")
    public ResponseEntity<Long> countModels(ModelCriteria criteria) {
        log.debug("REST request to count Models by criteria: {}", criteria);
        return ResponseEntity.ok().body(modelQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /models/:id} : get the "id" model.
     *
     * @param id the id of the model to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the model, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/models/{id}")
    public ResponseEntity<Model> getModel(@PathVariable Long id) {
        log.debug("REST request to get Model : {}", id);
        Optional<Model> model = modelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(model);
    }

    /**
     * {@code DELETE  /models/:id} : delete the "id" model.
     *
     * @param id the id of the model to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/models/{id}")
    public ResponseEntity<Void> deleteModel(@PathVariable Long id) {
        log.debug("REST request to delete Model : {}", id);
        modelService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}

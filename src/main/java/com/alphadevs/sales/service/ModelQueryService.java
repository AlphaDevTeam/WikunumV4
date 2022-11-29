package com.alphadevs.sales.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.alphadevs.sales.domain.Model;
import com.alphadevs.sales.domain.*; // for static metamodels
import com.alphadevs.sales.repository.ModelRepository;
import com.alphadevs.sales.service.dto.ModelCriteria;

/**
 * Service for executing complex queries for {@link Model} entities in the database.
 * The main input is a {@link ModelCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Model} or a {@link Page} of {@link Model} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ModelQueryService extends QueryService<Model> {

    private final Logger log = LoggerFactory.getLogger(ModelQueryService.class);

    private final ModelRepository modelRepository;

    public ModelQueryService(ModelRepository modelRepository) {
        this.modelRepository = modelRepository;
    }

    /**
     * Return a {@link List} of {@link Model} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Model> findByCriteria(ModelCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Model> specification = createSpecification(criteria);
        return modelRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Model} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Model> findByCriteria(ModelCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Model> specification = createSpecification(criteria);
        return modelRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ModelCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Model> specification = createSpecification(criteria);
        return modelRepository.count(specification);
    }

    /**
     * Function to convert {@link ModelCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Model> createSpecification(ModelCriteria criteria) {
        Specification<Model> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Model_.id));
            }
            if (criteria.getModelCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getModelCode(), Model_.modelCode));
            }
            if (criteria.getModelName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getModelName(), Model_.modelName));
            }
            if (criteria.getRelatedProductId() != null) {
                specification = specification.and(buildSpecification(criteria.getRelatedProductId(),
                    root -> root.join(Model_.relatedProduct, JoinType.LEFT).get(Products_.id)));
            }
            if (criteria.getLocationId() != null) {
                specification = specification.and(buildSpecification(criteria.getLocationId(),
                    root -> root.join(Model_.location, JoinType.LEFT).get(Location_.id)));
            }
        }
        return specification;
    }
}

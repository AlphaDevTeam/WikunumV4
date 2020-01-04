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

import com.alphadevs.sales.domain.ConfigurationItems;
import com.alphadevs.sales.domain.*; // for static metamodels
import com.alphadevs.sales.repository.ConfigurationItemsRepository;
import com.alphadevs.sales.service.dto.ConfigurationItemsCriteria;

/**
 * Service for executing complex queries for {@link ConfigurationItems} entities in the database.
 * The main input is a {@link ConfigurationItemsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ConfigurationItems} or a {@link Page} of {@link ConfigurationItems} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ConfigurationItemsQueryService extends QueryService<ConfigurationItems> {

    private final Logger log = LoggerFactory.getLogger(ConfigurationItemsQueryService.class);

    private final ConfigurationItemsRepository configurationItemsRepository;

    public ConfigurationItemsQueryService(ConfigurationItemsRepository configurationItemsRepository) {
        this.configurationItemsRepository = configurationItemsRepository;
    }

    /**
     * Return a {@link List} of {@link ConfigurationItems} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ConfigurationItems> findByCriteria(ConfigurationItemsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ConfigurationItems> specification = createSpecification(criteria);
        return configurationItemsRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ConfigurationItems} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ConfigurationItems> findByCriteria(ConfigurationItemsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ConfigurationItems> specification = createSpecification(criteria);
        return configurationItemsRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ConfigurationItemsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ConfigurationItems> specification = createSpecification(criteria);
        return configurationItemsRepository.count(specification);
    }

    /**
     * Function to convert {@link ConfigurationItemsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ConfigurationItems> createSpecification(ConfigurationItemsCriteria criteria) {
        Specification<ConfigurationItems> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ConfigurationItems_.id));
            }
            if (criteria.getConfigCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getConfigCode(), ConfigurationItems_.configCode));
            }
            if (criteria.getConfigDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getConfigDescription(), ConfigurationItems_.configDescription));
            }
            if (criteria.getConfigEnabled() != null) {
                specification = specification.and(buildSpecification(criteria.getConfigEnabled(), ConfigurationItems_.configEnabled));
            }
            if (criteria.getConfigParamter() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getConfigParamter(), ConfigurationItems_.configParamter));
            }
            if (criteria.getLocationId() != null) {
                specification = specification.and(buildSpecification(criteria.getLocationId(),
                    root -> root.join(ConfigurationItems_.locations, JoinType.LEFT).get(Location_.id)));
            }
        }
        return specification;
    }
}

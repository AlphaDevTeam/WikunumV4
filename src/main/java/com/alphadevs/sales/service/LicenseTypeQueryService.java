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

import com.alphadevs.sales.domain.LicenseType;
import com.alphadevs.sales.domain.*; // for static metamodels
import com.alphadevs.sales.repository.LicenseTypeRepository;
import com.alphadevs.sales.service.dto.LicenseTypeCriteria;

/**
 * Service for executing complex queries for {@link LicenseType} entities in the database.
 * The main input is a {@link LicenseTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LicenseType} or a {@link Page} of {@link LicenseType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LicenseTypeQueryService extends QueryService<LicenseType> {

    private final Logger log = LoggerFactory.getLogger(LicenseTypeQueryService.class);

    private final LicenseTypeRepository licenseTypeRepository;

    public LicenseTypeQueryService(LicenseTypeRepository licenseTypeRepository) {
        this.licenseTypeRepository = licenseTypeRepository;
    }

    /**
     * Return a {@link List} of {@link LicenseType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LicenseType> findByCriteria(LicenseTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<LicenseType> specification = createSpecification(criteria);
        return licenseTypeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link LicenseType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LicenseType> findByCriteria(LicenseTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LicenseType> specification = createSpecification(criteria);
        return licenseTypeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LicenseTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<LicenseType> specification = createSpecification(criteria);
        return licenseTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link LicenseTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LicenseType> createSpecification(LicenseTypeCriteria criteria) {
        Specification<LicenseType> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), LicenseType_.id));
            }
            if (criteria.getLicenseTypeCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLicenseTypeCode(), LicenseType_.licenseTypeCode));
            }
            if (criteria.getLicenseTypeName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLicenseTypeName(), LicenseType_.licenseTypeName));
            }
            if (criteria.getValidityDays() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValidityDays(), LicenseType_.validityDays));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), LicenseType_.isActive));
            }
        }
        return specification;
    }
}

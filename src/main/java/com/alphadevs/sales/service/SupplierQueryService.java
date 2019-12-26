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

import com.alphadevs.sales.domain.Supplier;
import com.alphadevs.sales.domain.*; // for static metamodels
import com.alphadevs.sales.repository.SupplierRepository;
import com.alphadevs.sales.service.dto.SupplierCriteria;

/**
 * Service for executing complex queries for {@link Supplier} entities in the database.
 * The main input is a {@link SupplierCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Supplier} or a {@link Page} of {@link Supplier} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SupplierQueryService extends QueryService<Supplier> {

    private final Logger log = LoggerFactory.getLogger(SupplierQueryService.class);

    private final SupplierRepository supplierRepository;

    public SupplierQueryService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    /**
     * Return a {@link List} of {@link Supplier} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Supplier> findByCriteria(SupplierCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Supplier> specification = createSpecification(criteria);
        return supplierRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Supplier} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Supplier> findByCriteria(SupplierCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Supplier> specification = createSpecification(criteria);
        return supplierRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SupplierCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Supplier> specification = createSpecification(criteria);
        return supplierRepository.count(specification);
    }

    /**
     * Function to convert {@link SupplierCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Supplier> createSpecification(SupplierCriteria criteria) {
        Specification<Supplier> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Supplier_.id));
            }
            if (criteria.getSupplierCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSupplierCode(), Supplier_.supplierCode));
            }
            if (criteria.getSupplierName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSupplierName(), Supplier_.supplierName));
            }
            if (criteria.getSupplierCreditLimit() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSupplierCreditLimit(), Supplier_.supplierCreditLimit));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), Supplier_.isActive));
            }
            if (criteria.getRating() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRating(), Supplier_.rating));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), Supplier_.phone));
            }
            if (criteria.getAddressLine1() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddressLine1(), Supplier_.addressLine1));
            }
            if (criteria.getAddressLine2() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddressLine2(), Supplier_.addressLine2));
            }
            if (criteria.getCity() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCity(), Supplier_.city));
            }
            if (criteria.getCountry() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCountry(), Supplier_.country));
            }
            if (criteria.getHistoryId() != null) {
                specification = specification.and(buildSpecification(criteria.getHistoryId(),
                    root -> root.join(Supplier_.history, JoinType.LEFT).get(DocumentHistory_.id)));
            }
            if (criteria.getLocationId() != null) {
                specification = specification.and(buildSpecification(criteria.getLocationId(),
                    root -> root.join(Supplier_.location, JoinType.LEFT).get(Location_.id)));
            }
        }
        return specification;
    }
}

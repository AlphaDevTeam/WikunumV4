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

import com.alphadevs.sales.domain.PaymentTypes;
import com.alphadevs.sales.domain.*; // for static metamodels
import com.alphadevs.sales.repository.PaymentTypesRepository;
import com.alphadevs.sales.service.dto.PaymentTypesCriteria;

/**
 * Service for executing complex queries for {@link PaymentTypes} entities in the database.
 * The main input is a {@link PaymentTypesCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PaymentTypes} or a {@link Page} of {@link PaymentTypes} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PaymentTypesQueryService extends QueryService<PaymentTypes> {

    private final Logger log = LoggerFactory.getLogger(PaymentTypesQueryService.class);

    private final PaymentTypesRepository paymentTypesRepository;

    public PaymentTypesQueryService(PaymentTypesRepository paymentTypesRepository) {
        this.paymentTypesRepository = paymentTypesRepository;
    }

    /**
     * Return a {@link List} of {@link PaymentTypes} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PaymentTypes> findByCriteria(PaymentTypesCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PaymentTypes> specification = createSpecification(criteria);
        return paymentTypesRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link PaymentTypes} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PaymentTypes> findByCriteria(PaymentTypesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PaymentTypes> specification = createSpecification(criteria);
        return paymentTypesRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PaymentTypesCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PaymentTypes> specification = createSpecification(criteria);
        return paymentTypesRepository.count(specification);
    }

    /**
     * Function to convert {@link PaymentTypesCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PaymentTypes> createSpecification(PaymentTypesCriteria criteria) {
        Specification<PaymentTypes> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PaymentTypes_.id));
            }
            if (criteria.getPaymentTypesCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPaymentTypesCode(), PaymentTypes_.paymentTypesCode));
            }
            if (criteria.getPaymentTypes() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPaymentTypes(), PaymentTypes_.paymentTypes));
            }
            if (criteria.getPaymentTypesChargePer() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPaymentTypesChargePer(), PaymentTypes_.paymentTypesChargePer));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), PaymentTypes_.isActive));
            }
            if (criteria.getLocationId() != null) {
                specification = specification.and(buildSpecification(criteria.getLocationId(),
                    root -> root.join(PaymentTypes_.location, JoinType.LEFT).get(Location_.id)));
            }
        }
        return specification;
    }
}

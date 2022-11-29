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

import com.alphadevs.sales.domain.StorageBin;
import com.alphadevs.sales.domain.*; // for static metamodels
import com.alphadevs.sales.repository.StorageBinRepository;
import com.alphadevs.sales.service.dto.StorageBinCriteria;

/**
 * Service for executing complex queries for {@link StorageBin} entities in the database.
 * The main input is a {@link StorageBinCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link StorageBin} or a {@link Page} of {@link StorageBin} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class StorageBinQueryService extends QueryService<StorageBin> {

    private final Logger log = LoggerFactory.getLogger(StorageBinQueryService.class);

    private final StorageBinRepository storageBinRepository;

    public StorageBinQueryService(StorageBinRepository storageBinRepository) {
        this.storageBinRepository = storageBinRepository;
    }

    /**
     * Return a {@link List} of {@link StorageBin} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<StorageBin> findByCriteria(StorageBinCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<StorageBin> specification = createSpecification(criteria);
        return storageBinRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link StorageBin} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<StorageBin> findByCriteria(StorageBinCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<StorageBin> specification = createSpecification(criteria);
        return storageBinRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(StorageBinCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<StorageBin> specification = createSpecification(criteria);
        return storageBinRepository.count(specification);
    }

    /**
     * Function to convert {@link StorageBinCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<StorageBin> createSpecification(StorageBinCriteria criteria) {
        Specification<StorageBin> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), StorageBin_.id));
            }
            if (criteria.getBinNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBinNumber(), StorageBin_.binNumber));
            }
            if (criteria.getBinDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBinDescription(), StorageBin_.binDescription));
            }
        }
        return specification;
    }
}

package com.alphadevs.sales.service;

import com.alphadevs.sales.domain.Location;
import com.alphadevs.sales.repository.LocationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Location}.
 */
@Service
@Transactional
public class LocationService {

    private final Logger log = LoggerFactory.getLogger(LocationService.class);

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    /**
     * Save a location.
     *
     * @param location the entity to save.
     * @return the persisted entity.
     */
    public Location save(Location location) {
        log.debug("Request to save Location : {}", location);
        return locationRepository.save(location);
    }

    /**
     * Get all the locations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Location> findAll(Pageable pageable) {
        log.debug("Request to get all Locations");
        return locationRepository.findAll(pageable);
    }

    /**
     * Get all the locations with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Location> findAllWithEagerRelationships(Pageable pageable) {
        return locationRepository.findAllWithEagerRelationships(pageable);
    }
    

    /**
     * Get one location by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Location> findOne(Long id) {
        log.debug("Request to get Location : {}", id);
        return locationRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the location by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Location : {}", id);
        locationRepository.deleteById(id);
    }
}

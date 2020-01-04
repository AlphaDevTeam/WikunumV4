package com.alphadevs.sales.service;

import com.alphadevs.sales.domain.LicenseType;
import com.alphadevs.sales.repository.LicenseTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link LicenseType}.
 */
@Service
@Transactional
public class LicenseTypeService {

    private final Logger log = LoggerFactory.getLogger(LicenseTypeService.class);

    private final LicenseTypeRepository licenseTypeRepository;

    public LicenseTypeService(LicenseTypeRepository licenseTypeRepository) {
        this.licenseTypeRepository = licenseTypeRepository;
    }

    /**
     * Save a licenseType.
     *
     * @param licenseType the entity to save.
     * @return the persisted entity.
     */
    public LicenseType save(LicenseType licenseType) {
        log.debug("Request to save LicenseType : {}", licenseType);
        return licenseTypeRepository.save(licenseType);
    }

    /**
     * Get all the licenseTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<LicenseType> findAll(Pageable pageable) {
        log.debug("Request to get all LicenseTypes");
        return licenseTypeRepository.findAll(pageable);
    }


    /**
     * Get one licenseType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LicenseType> findOne(Long id) {
        log.debug("Request to get LicenseType : {}", id);
        return licenseTypeRepository.findById(id);
    }

    /**
     * Delete the licenseType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete LicenseType : {}", id);
        licenseTypeRepository.deleteById(id);
    }
}

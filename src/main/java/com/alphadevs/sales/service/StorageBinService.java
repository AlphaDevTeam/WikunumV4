package com.alphadevs.sales.service;

import com.alphadevs.sales.domain.StorageBin;
import com.alphadevs.sales.repository.StorageBinRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link StorageBin}.
 */
@Service
@Transactional
public class StorageBinService {

    private final Logger log = LoggerFactory.getLogger(StorageBinService.class);

    private final StorageBinRepository storageBinRepository;

    public StorageBinService(StorageBinRepository storageBinRepository) {
        this.storageBinRepository = storageBinRepository;
    }

    /**
     * Save a storageBin.
     *
     * @param storageBin the entity to save.
     * @return the persisted entity.
     */
    public StorageBin save(StorageBin storageBin) {
        log.debug("Request to save StorageBin : {}", storageBin);
        return storageBinRepository.save(storageBin);
    }

    /**
     * Get all the storageBins.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<StorageBin> findAll(Pageable pageable) {
        log.debug("Request to get all StorageBins");
        return storageBinRepository.findAll(pageable);
    }


    /**
     * Get one storageBin by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<StorageBin> findOne(Long id) {
        log.debug("Request to get StorageBin : {}", id);
        return storageBinRepository.findById(id);
    }

    /**
     * Delete the storageBin by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete StorageBin : {}", id);
        storageBinRepository.deleteById(id);
    }
}

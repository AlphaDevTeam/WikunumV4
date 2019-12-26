package com.alphadevs.sales.service;

import com.alphadevs.sales.domain.ConfigurationItems;
import com.alphadevs.sales.repository.ConfigurationItemsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ConfigurationItems}.
 */
@Service
@Transactional
public class ConfigurationItemsService {

    private final Logger log = LoggerFactory.getLogger(ConfigurationItemsService.class);

    private final ConfigurationItemsRepository configurationItemsRepository;

    public ConfigurationItemsService(ConfigurationItemsRepository configurationItemsRepository) {
        this.configurationItemsRepository = configurationItemsRepository;
    }

    /**
     * Save a configurationItems.
     *
     * @param configurationItems the entity to save.
     * @return the persisted entity.
     */
    public ConfigurationItems save(ConfigurationItems configurationItems) {
        log.debug("Request to save ConfigurationItems : {}", configurationItems);
        return configurationItemsRepository.save(configurationItems);
    }

    /**
     * Get all the configurationItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ConfigurationItems> findAll(Pageable pageable) {
        log.debug("Request to get all ConfigurationItems");
        return configurationItemsRepository.findAll(pageable);
    }


    /**
     * Get one configurationItems by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ConfigurationItems> findOne(Long id) {
        log.debug("Request to get ConfigurationItems : {}", id);
        return configurationItemsRepository.findById(id);
    }

    /**
     * Delete the configurationItems by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ConfigurationItems : {}", id);
        configurationItemsRepository.deleteById(id);
    }
}

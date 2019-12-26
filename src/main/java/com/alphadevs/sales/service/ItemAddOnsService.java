package com.alphadevs.sales.service;

import com.alphadevs.sales.domain.ItemAddOns;
import com.alphadevs.sales.repository.ItemAddOnsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ItemAddOns}.
 */
@Service
@Transactional
public class ItemAddOnsService {

    private final Logger log = LoggerFactory.getLogger(ItemAddOnsService.class);

    private final ItemAddOnsRepository itemAddOnsRepository;

    public ItemAddOnsService(ItemAddOnsRepository itemAddOnsRepository) {
        this.itemAddOnsRepository = itemAddOnsRepository;
    }

    /**
     * Save a itemAddOns.
     *
     * @param itemAddOns the entity to save.
     * @return the persisted entity.
     */
    public ItemAddOns save(ItemAddOns itemAddOns) {
        log.debug("Request to save ItemAddOns : {}", itemAddOns);
        return itemAddOnsRepository.save(itemAddOns);
    }

    /**
     * Get all the itemAddOns.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ItemAddOns> findAll(Pageable pageable) {
        log.debug("Request to get all ItemAddOns");
        return itemAddOnsRepository.findAll(pageable);
    }


    /**
     * Get one itemAddOns by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ItemAddOns> findOne(Long id) {
        log.debug("Request to get ItemAddOns : {}", id);
        return itemAddOnsRepository.findById(id);
    }

    /**
     * Delete the itemAddOns by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ItemAddOns : {}", id);
        itemAddOnsRepository.deleteById(id);
    }
}

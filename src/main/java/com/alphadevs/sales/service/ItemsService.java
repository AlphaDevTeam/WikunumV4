package com.alphadevs.sales.service;

import com.alphadevs.sales.domain.Items;
import com.alphadevs.sales.repository.ItemsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Items}.
 */
@Service
@Transactional
public class ItemsService {

    private final Logger log = LoggerFactory.getLogger(ItemsService.class);

    private final ItemsRepository itemsRepository;

    public ItemsService(ItemsRepository itemsRepository) {
        this.itemsRepository = itemsRepository;
    }

    /**
     * Save a items.
     *
     * @param items the entity to save.
     * @return the persisted entity.
     */
    public Items save(Items items) {
        log.debug("Request to save Items : {}", items);
        return itemsRepository.save(items);
    }

    /**
     * Get all the items.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Items> findAll(Pageable pageable) {
        log.debug("Request to get all Items");
        return itemsRepository.findAll(pageable);
    }

    /**
     * Get all the items with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Items> findAllWithEagerRelationships(Pageable pageable) {
        return itemsRepository.findAllWithEagerRelationships(pageable);
    }
    

    /**
     * Get one items by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Items> findOne(Long id) {
        log.debug("Request to get Items : {}", id);
        return itemsRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the items by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Items : {}", id);
        itemsRepository.deleteById(id);
    }
}

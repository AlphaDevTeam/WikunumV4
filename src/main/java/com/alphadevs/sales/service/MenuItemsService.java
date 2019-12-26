package com.alphadevs.sales.service;

import com.alphadevs.sales.domain.MenuItems;
import com.alphadevs.sales.repository.MenuItemsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link MenuItems}.
 */
@Service
@Transactional
public class MenuItemsService {

    private final Logger log = LoggerFactory.getLogger(MenuItemsService.class);

    private final MenuItemsRepository menuItemsRepository;

    public MenuItemsService(MenuItemsRepository menuItemsRepository) {
        this.menuItemsRepository = menuItemsRepository;
    }

    /**
     * Save a menuItems.
     *
     * @param menuItems the entity to save.
     * @return the persisted entity.
     */
    public MenuItems save(MenuItems menuItems) {
        log.debug("Request to save MenuItems : {}", menuItems);
        return menuItemsRepository.save(menuItems);
    }

    /**
     * Get all the menuItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MenuItems> findAll(Pageable pageable) {
        log.debug("Request to get all MenuItems");
        return menuItemsRepository.findAll(pageable);
    }


    /**
     * Get one menuItems by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MenuItems> findOne(Long id) {
        log.debug("Request to get MenuItems : {}", id);
        return menuItemsRepository.findById(id);
    }

    /**
     * Delete the menuItems by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete MenuItems : {}", id);
        menuItemsRepository.deleteById(id);
    }
}

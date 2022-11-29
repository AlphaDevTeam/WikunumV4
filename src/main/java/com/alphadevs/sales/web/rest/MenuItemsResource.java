package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.domain.MenuItems;
import com.alphadevs.sales.service.MenuItemsService;
import com.alphadevs.sales.service.UserService;
import com.alphadevs.sales.web.rest.errors.BadRequestAlertException;
import com.alphadevs.sales.service.dto.MenuItemsCriteria;
import com.alphadevs.sales.service.MenuItemsQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.alphadevs.sales.domain.MenuItems}.
 */
@RestController
@RequestMapping("/api")
public class MenuItemsResource {

    private final Logger log = LoggerFactory.getLogger(MenuItemsResource.class);

    private static final String ENTITY_NAME = "menuItems";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MenuItemsService menuItemsService;
    private final UserService userService;

    private final MenuItemsQueryService menuItemsQueryService;

    public MenuItemsResource(MenuItemsService menuItemsService, UserService userService, MenuItemsQueryService menuItemsQueryService) {
        this.menuItemsService = menuItemsService;
        this.userService = userService;
        this.menuItemsQueryService = menuItemsQueryService;
    }

    /**
     * {@code POST  /menu-items} : Create a new menuItems.
     *
     * @param menuItems the menuItems to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new menuItems, or with status {@code 400 (Bad Request)} if the menuItems has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/menu-items")
    public ResponseEntity<MenuItems> createMenuItems(@Valid @RequestBody MenuItems menuItems) throws URISyntaxException {
        log.debug("REST request to save MenuItems : {}", menuItems);
        if (menuItems.getId() != null) {
            throw new BadRequestAlertException("A new menuItems cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MenuItems result = menuItemsService.save(menuItems);
        return ResponseEntity.created(new URI("/api/menu-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /menu-items} : Updates an existing menuItems.
     *
     * @param menuItems the menuItems to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated menuItems,
     * or with status {@code 400 (Bad Request)} if the menuItems is not valid,
     * or with status {@code 500 (Internal Server Error)} if the menuItems couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/menu-items")
    public ResponseEntity<MenuItems> updateMenuItems(@Valid @RequestBody MenuItems menuItems) throws URISyntaxException {
        log.debug("REST request to update MenuItems : {}", menuItems);
        if (menuItems.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MenuItems result = menuItemsService.save(menuItems);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, menuItems.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /menu-items} : get all the menuItems.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of menuItems in body.
     */
    @GetMapping("/menu-items")
    public ResponseEntity<List<MenuItems>> getAllMenuItems(MenuItemsCriteria criteria, Pageable pageable) {
        log.debug("REST request to get MenuItems by criteria: {}", criteria);
        Page<MenuItems> page = menuItemsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /menu-items/count} : count all the menuItems.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/menu-items/count")
    public ResponseEntity<Long> countMenuItems(MenuItemsCriteria criteria) {
        log.debug("REST request to count MenuItems by criteria: {}", criteria);
        return ResponseEntity.ok().body(menuItemsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /menu-items/:id} : get the "id" menuItems.
     *
     * @param id the id of the menuItems to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the menuItems, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/menu-items/{id}")
    public ResponseEntity<MenuItems> getMenuItems(@PathVariable Long id) {
        log.debug("REST request to get MenuItems : {}", id);
        Optional<MenuItems> menuItems = menuItemsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(menuItems);
    }

    /**
     * {@code DELETE  /menu-items/:id} : delete the "id" menuItems.
     *
     * @param id the id of the menuItems to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/menu-items/{id}")
    public ResponseEntity<Void> deleteMenuItems(@PathVariable Long id) {
        log.debug("REST request to delete MenuItems : {}", id);
        menuItemsService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code GET  /menu-items/current-user} : get all the menuItems related to logged in user.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of menuItems in body.
     */
    @GetMapping("/menu-items/current-user")
    public ResponseEntity<List<MenuItems>> getAllUserMenuItems(MenuItemsCriteria criteria, Pageable pageable) {

        log.debug("REST request to get MenuItems by user : {}");
        Page<MenuItems> page = new PageImpl<>(userService.getUserMenu());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}

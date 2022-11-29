package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.WikunumApp;
import com.alphadevs.sales.domain.MenuItems;
import com.alphadevs.sales.domain.UserPermissions;
import com.alphadevs.sales.repository.MenuItemsRepository;
import com.alphadevs.sales.service.MenuItemsService;
import com.alphadevs.sales.web.rest.errors.ExceptionTranslator;
import com.alphadevs.sales.service.MenuItemsQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static com.alphadevs.sales.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link MenuItemsResource} REST controller.
 */
@SpringBootTest(classes = WikunumApp.class)
public class MenuItemsResourceIT {

    private static final String DEFAULT_MENU_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MENU_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MENU_URL = "AAAAAAAAAA";
    private static final String UPDATED_MENU_URL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    @Autowired
    private MenuItemsRepository menuItemsRepository;

    @Autowired
    private MenuItemsService menuItemsService;

    @Autowired
    private MenuItemsQueryService menuItemsQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restMenuItemsMockMvc;

    private MenuItems menuItems;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MenuItemsResource menuItemsResource = new MenuItemsResource(menuItemsService, userService, menuItemsQueryService);
        this.restMenuItemsMockMvc = MockMvcBuilders.standaloneSetup(menuItemsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MenuItems createEntity(EntityManager em) {
        MenuItems menuItems = new MenuItems()
            .menuName(DEFAULT_MENU_NAME)
            .menuURL(DEFAULT_MENU_URL)
            .isActive(DEFAULT_IS_ACTIVE);
        return menuItems;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MenuItems createUpdatedEntity(EntityManager em) {
        MenuItems menuItems = new MenuItems()
            .menuName(UPDATED_MENU_NAME)
            .menuURL(UPDATED_MENU_URL)
            .isActive(UPDATED_IS_ACTIVE);
        return menuItems;
    }

    @BeforeEach
    public void initTest() {
        menuItems = createEntity(em);
    }

    @Test
    @Transactional
    public void createMenuItems() throws Exception {
        int databaseSizeBeforeCreate = menuItemsRepository.findAll().size();

        // Create the MenuItems
        restMenuItemsMockMvc.perform(post("/api/menu-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(menuItems)))
            .andExpect(status().isCreated());

        // Validate the MenuItems in the database
        List<MenuItems> menuItemsList = menuItemsRepository.findAll();
        assertThat(menuItemsList).hasSize(databaseSizeBeforeCreate + 1);
        MenuItems testMenuItems = menuItemsList.get(menuItemsList.size() - 1);
        assertThat(testMenuItems.getMenuName()).isEqualTo(DEFAULT_MENU_NAME);
        assertThat(testMenuItems.getMenuURL()).isEqualTo(DEFAULT_MENU_URL);
        assertThat(testMenuItems.isIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void createMenuItemsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = menuItemsRepository.findAll().size();

        // Create the MenuItems with an existing ID
        menuItems.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMenuItemsMockMvc.perform(post("/api/menu-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(menuItems)))
            .andExpect(status().isBadRequest());

        // Validate the MenuItems in the database
        List<MenuItems> menuItemsList = menuItemsRepository.findAll();
        assertThat(menuItemsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkMenuNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = menuItemsRepository.findAll().size();
        // set the field null
        menuItems.setMenuName(null);

        // Create the MenuItems, which fails.

        restMenuItemsMockMvc.perform(post("/api/menu-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(menuItems)))
            .andExpect(status().isBadRequest());

        List<MenuItems> menuItemsList = menuItemsRepository.findAll();
        assertThat(menuItemsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMenuURLIsRequired() throws Exception {
        int databaseSizeBeforeTest = menuItemsRepository.findAll().size();
        // set the field null
        menuItems.setMenuURL(null);

        // Create the MenuItems, which fails.

        restMenuItemsMockMvc.perform(post("/api/menu-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(menuItems)))
            .andExpect(status().isBadRequest());

        List<MenuItems> menuItemsList = menuItemsRepository.findAll();
        assertThat(menuItemsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMenuItems() throws Exception {
        // Initialize the database
        menuItemsRepository.saveAndFlush(menuItems);

        // Get all the menuItemsList
        restMenuItemsMockMvc.perform(get("/api/menu-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(menuItems.getId().intValue())))
            .andExpect(jsonPath("$.[*].menuName").value(hasItem(DEFAULT_MENU_NAME)))
            .andExpect(jsonPath("$.[*].menuURL").value(hasItem(DEFAULT_MENU_URL)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    public void getMenuItems() throws Exception {
        // Initialize the database
        menuItemsRepository.saveAndFlush(menuItems);

        // Get the menuItems
        restMenuItemsMockMvc.perform(get("/api/menu-items/{id}", menuItems.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(menuItems.getId().intValue()))
            .andExpect(jsonPath("$.menuName").value(DEFAULT_MENU_NAME))
            .andExpect(jsonPath("$.menuURL").value(DEFAULT_MENU_URL))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }


    @Test
    @Transactional
    public void getMenuItemsByIdFiltering() throws Exception {
        // Initialize the database
        menuItemsRepository.saveAndFlush(menuItems);

        Long id = menuItems.getId();

        defaultMenuItemsShouldBeFound("id.equals=" + id);
        defaultMenuItemsShouldNotBeFound("id.notEquals=" + id);

        defaultMenuItemsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMenuItemsShouldNotBeFound("id.greaterThan=" + id);

        defaultMenuItemsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMenuItemsShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllMenuItemsByMenuNameIsEqualToSomething() throws Exception {
        // Initialize the database
        menuItemsRepository.saveAndFlush(menuItems);

        // Get all the menuItemsList where menuName equals to DEFAULT_MENU_NAME
        defaultMenuItemsShouldBeFound("menuName.equals=" + DEFAULT_MENU_NAME);

        // Get all the menuItemsList where menuName equals to UPDATED_MENU_NAME
        defaultMenuItemsShouldNotBeFound("menuName.equals=" + UPDATED_MENU_NAME);
    }

    @Test
    @Transactional
    public void getAllMenuItemsByMenuNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        menuItemsRepository.saveAndFlush(menuItems);

        // Get all the menuItemsList where menuName not equals to DEFAULT_MENU_NAME
        defaultMenuItemsShouldNotBeFound("menuName.notEquals=" + DEFAULT_MENU_NAME);

        // Get all the menuItemsList where menuName not equals to UPDATED_MENU_NAME
        defaultMenuItemsShouldBeFound("menuName.notEquals=" + UPDATED_MENU_NAME);
    }

    @Test
    @Transactional
    public void getAllMenuItemsByMenuNameIsInShouldWork() throws Exception {
        // Initialize the database
        menuItemsRepository.saveAndFlush(menuItems);

        // Get all the menuItemsList where menuName in DEFAULT_MENU_NAME or UPDATED_MENU_NAME
        defaultMenuItemsShouldBeFound("menuName.in=" + DEFAULT_MENU_NAME + "," + UPDATED_MENU_NAME);

        // Get all the menuItemsList where menuName equals to UPDATED_MENU_NAME
        defaultMenuItemsShouldNotBeFound("menuName.in=" + UPDATED_MENU_NAME);
    }

    @Test
    @Transactional
    public void getAllMenuItemsByMenuNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        menuItemsRepository.saveAndFlush(menuItems);

        // Get all the menuItemsList where menuName is not null
        defaultMenuItemsShouldBeFound("menuName.specified=true");

        // Get all the menuItemsList where menuName is null
        defaultMenuItemsShouldNotBeFound("menuName.specified=false");
    }
                @Test
    @Transactional
    public void getAllMenuItemsByMenuNameContainsSomething() throws Exception {
        // Initialize the database
        menuItemsRepository.saveAndFlush(menuItems);

        // Get all the menuItemsList where menuName contains DEFAULT_MENU_NAME
        defaultMenuItemsShouldBeFound("menuName.contains=" + DEFAULT_MENU_NAME);

        // Get all the menuItemsList where menuName contains UPDATED_MENU_NAME
        defaultMenuItemsShouldNotBeFound("menuName.contains=" + UPDATED_MENU_NAME);
    }

    @Test
    @Transactional
    public void getAllMenuItemsByMenuNameNotContainsSomething() throws Exception {
        // Initialize the database
        menuItemsRepository.saveAndFlush(menuItems);

        // Get all the menuItemsList where menuName does not contain DEFAULT_MENU_NAME
        defaultMenuItemsShouldNotBeFound("menuName.doesNotContain=" + DEFAULT_MENU_NAME);

        // Get all the menuItemsList where menuName does not contain UPDATED_MENU_NAME
        defaultMenuItemsShouldBeFound("menuName.doesNotContain=" + UPDATED_MENU_NAME);
    }


    @Test
    @Transactional
    public void getAllMenuItemsByMenuURLIsEqualToSomething() throws Exception {
        // Initialize the database
        menuItemsRepository.saveAndFlush(menuItems);

        // Get all the menuItemsList where menuURL equals to DEFAULT_MENU_URL
        defaultMenuItemsShouldBeFound("menuURL.equals=" + DEFAULT_MENU_URL);

        // Get all the menuItemsList where menuURL equals to UPDATED_MENU_URL
        defaultMenuItemsShouldNotBeFound("menuURL.equals=" + UPDATED_MENU_URL);
    }

    @Test
    @Transactional
    public void getAllMenuItemsByMenuURLIsNotEqualToSomething() throws Exception {
        // Initialize the database
        menuItemsRepository.saveAndFlush(menuItems);

        // Get all the menuItemsList where menuURL not equals to DEFAULT_MENU_URL
        defaultMenuItemsShouldNotBeFound("menuURL.notEquals=" + DEFAULT_MENU_URL);

        // Get all the menuItemsList where menuURL not equals to UPDATED_MENU_URL
        defaultMenuItemsShouldBeFound("menuURL.notEquals=" + UPDATED_MENU_URL);
    }

    @Test
    @Transactional
    public void getAllMenuItemsByMenuURLIsInShouldWork() throws Exception {
        // Initialize the database
        menuItemsRepository.saveAndFlush(menuItems);

        // Get all the menuItemsList where menuURL in DEFAULT_MENU_URL or UPDATED_MENU_URL
        defaultMenuItemsShouldBeFound("menuURL.in=" + DEFAULT_MENU_URL + "," + UPDATED_MENU_URL);

        // Get all the menuItemsList where menuURL equals to UPDATED_MENU_URL
        defaultMenuItemsShouldNotBeFound("menuURL.in=" + UPDATED_MENU_URL);
    }

    @Test
    @Transactional
    public void getAllMenuItemsByMenuURLIsNullOrNotNull() throws Exception {
        // Initialize the database
        menuItemsRepository.saveAndFlush(menuItems);

        // Get all the menuItemsList where menuURL is not null
        defaultMenuItemsShouldBeFound("menuURL.specified=true");

        // Get all the menuItemsList where menuURL is null
        defaultMenuItemsShouldNotBeFound("menuURL.specified=false");
    }
                @Test
    @Transactional
    public void getAllMenuItemsByMenuURLContainsSomething() throws Exception {
        // Initialize the database
        menuItemsRepository.saveAndFlush(menuItems);

        // Get all the menuItemsList where menuURL contains DEFAULT_MENU_URL
        defaultMenuItemsShouldBeFound("menuURL.contains=" + DEFAULT_MENU_URL);

        // Get all the menuItemsList where menuURL contains UPDATED_MENU_URL
        defaultMenuItemsShouldNotBeFound("menuURL.contains=" + UPDATED_MENU_URL);
    }

    @Test
    @Transactional
    public void getAllMenuItemsByMenuURLNotContainsSomething() throws Exception {
        // Initialize the database
        menuItemsRepository.saveAndFlush(menuItems);

        // Get all the menuItemsList where menuURL does not contain DEFAULT_MENU_URL
        defaultMenuItemsShouldNotBeFound("menuURL.doesNotContain=" + DEFAULT_MENU_URL);

        // Get all the menuItemsList where menuURL does not contain UPDATED_MENU_URL
        defaultMenuItemsShouldBeFound("menuURL.doesNotContain=" + UPDATED_MENU_URL);
    }


    @Test
    @Transactional
    public void getAllMenuItemsByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        menuItemsRepository.saveAndFlush(menuItems);

        // Get all the menuItemsList where isActive equals to DEFAULT_IS_ACTIVE
        defaultMenuItemsShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the menuItemsList where isActive equals to UPDATED_IS_ACTIVE
        defaultMenuItemsShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllMenuItemsByIsActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        menuItemsRepository.saveAndFlush(menuItems);

        // Get all the menuItemsList where isActive not equals to DEFAULT_IS_ACTIVE
        defaultMenuItemsShouldNotBeFound("isActive.notEquals=" + DEFAULT_IS_ACTIVE);

        // Get all the menuItemsList where isActive not equals to UPDATED_IS_ACTIVE
        defaultMenuItemsShouldBeFound("isActive.notEquals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllMenuItemsByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        menuItemsRepository.saveAndFlush(menuItems);

        // Get all the menuItemsList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultMenuItemsShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the menuItemsList where isActive equals to UPDATED_IS_ACTIVE
        defaultMenuItemsShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllMenuItemsByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        menuItemsRepository.saveAndFlush(menuItems);

        // Get all the menuItemsList where isActive is not null
        defaultMenuItemsShouldBeFound("isActive.specified=true");

        // Get all the menuItemsList where isActive is null
        defaultMenuItemsShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    public void getAllMenuItemsByUserPermissionIsEqualToSomething() throws Exception {
        // Initialize the database
        menuItemsRepository.saveAndFlush(menuItems);
        UserPermissions userPermission = UserPermissionsResourceIT.createEntity(em);
        em.persist(userPermission);
        em.flush();
        menuItems.addUserPermission(userPermission);
        menuItemsRepository.saveAndFlush(menuItems);
        Long userPermissionId = userPermission.getId();

        // Get all the menuItemsList where userPermission equals to userPermissionId
        defaultMenuItemsShouldBeFound("userPermissionId.equals=" + userPermissionId);

        // Get all the menuItemsList where userPermission equals to userPermissionId + 1
        defaultMenuItemsShouldNotBeFound("userPermissionId.equals=" + (userPermissionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMenuItemsShouldBeFound(String filter) throws Exception {
        restMenuItemsMockMvc.perform(get("/api/menu-items?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(menuItems.getId().intValue())))
            .andExpect(jsonPath("$.[*].menuName").value(hasItem(DEFAULT_MENU_NAME)))
            .andExpect(jsonPath("$.[*].menuURL").value(hasItem(DEFAULT_MENU_URL)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restMenuItemsMockMvc.perform(get("/api/menu-items/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMenuItemsShouldNotBeFound(String filter) throws Exception {
        restMenuItemsMockMvc.perform(get("/api/menu-items?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMenuItemsMockMvc.perform(get("/api/menu-items/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingMenuItems() throws Exception {
        // Get the menuItems
        restMenuItemsMockMvc.perform(get("/api/menu-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMenuItems() throws Exception {
        // Initialize the database
        menuItemsService.save(menuItems);

        int databaseSizeBeforeUpdate = menuItemsRepository.findAll().size();

        // Update the menuItems
        MenuItems updatedMenuItems = menuItemsRepository.findById(menuItems.getId()).get();
        // Disconnect from session so that the updates on updatedMenuItems are not directly saved in db
        em.detach(updatedMenuItems);
        updatedMenuItems
            .menuName(UPDATED_MENU_NAME)
            .menuURL(UPDATED_MENU_URL)
            .isActive(UPDATED_IS_ACTIVE);

        restMenuItemsMockMvc.perform(put("/api/menu-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMenuItems)))
            .andExpect(status().isOk());

        // Validate the MenuItems in the database
        List<MenuItems> menuItemsList = menuItemsRepository.findAll();
        assertThat(menuItemsList).hasSize(databaseSizeBeforeUpdate);
        MenuItems testMenuItems = menuItemsList.get(menuItemsList.size() - 1);
        assertThat(testMenuItems.getMenuName()).isEqualTo(UPDATED_MENU_NAME);
        assertThat(testMenuItems.getMenuURL()).isEqualTo(UPDATED_MENU_URL);
        assertThat(testMenuItems.isIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void updateNonExistingMenuItems() throws Exception {
        int databaseSizeBeforeUpdate = menuItemsRepository.findAll().size();

        // Create the MenuItems

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMenuItemsMockMvc.perform(put("/api/menu-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(menuItems)))
            .andExpect(status().isBadRequest());

        // Validate the MenuItems in the database
        List<MenuItems> menuItemsList = menuItemsRepository.findAll();
        assertThat(menuItemsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMenuItems() throws Exception {
        // Initialize the database
        menuItemsService.save(menuItems);

        int databaseSizeBeforeDelete = menuItemsRepository.findAll().size();

        // Delete the menuItems
        restMenuItemsMockMvc.perform(delete("/api/menu-items/{id}", menuItems.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MenuItems> menuItemsList = menuItemsRepository.findAll();
        assertThat(menuItemsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.WikunumApp;
import com.alphadevs.sales.domain.UserPermissions;
import com.alphadevs.sales.domain.MenuItems;
import com.alphadevs.sales.domain.ExUser;
import com.alphadevs.sales.domain.UserGroup;
import com.alphadevs.sales.repository.UserPermissionsRepository;
import com.alphadevs.sales.service.UserPermissionsService;
import com.alphadevs.sales.web.rest.errors.ExceptionTranslator;
import com.alphadevs.sales.service.dto.UserPermissionsCriteria;
import com.alphadevs.sales.service.UserPermissionsQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static com.alphadevs.sales.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link UserPermissionsResource} REST controller.
 */
@SpringBootTest(classes = WikunumApp.class)
public class UserPermissionsResourceIT {

    private static final String DEFAULT_USER_PERM_KEY = "AAAAAAAAAA";
    private static final String UPDATED_USER_PERM_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_USER_PERM_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_USER_PERM_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    @Autowired
    private UserPermissionsRepository userPermissionsRepository;

    @Mock
    private UserPermissionsRepository userPermissionsRepositoryMock;

    @Mock
    private UserPermissionsService userPermissionsServiceMock;

    @Autowired
    private UserPermissionsService userPermissionsService;

    @Autowired
    private UserPermissionsQueryService userPermissionsQueryService;

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

    private MockMvc restUserPermissionsMockMvc;

    private UserPermissions userPermissions;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UserPermissionsResource userPermissionsResource = new UserPermissionsResource(userPermissionsService, userPermissionsQueryService);
        this.restUserPermissionsMockMvc = MockMvcBuilders.standaloneSetup(userPermissionsResource)
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
    public static UserPermissions createEntity(EntityManager em) {
        UserPermissions userPermissions = new UserPermissions()
            .userPermKey(DEFAULT_USER_PERM_KEY)
            .userPermDescription(DEFAULT_USER_PERM_DESCRIPTION)
            .isActive(DEFAULT_IS_ACTIVE);
        // Add required entity
        MenuItems menuItems;
        if (TestUtil.findAll(em, MenuItems.class).isEmpty()) {
            menuItems = MenuItemsResourceIT.createEntity(em);
            em.persist(menuItems);
            em.flush();
        } else {
            menuItems = TestUtil.findAll(em, MenuItems.class).get(0);
        }
        userPermissions.getMenuItems().add(menuItems);
        return userPermissions;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserPermissions createUpdatedEntity(EntityManager em) {
        UserPermissions userPermissions = new UserPermissions()
            .userPermKey(UPDATED_USER_PERM_KEY)
            .userPermDescription(UPDATED_USER_PERM_DESCRIPTION)
            .isActive(UPDATED_IS_ACTIVE);
        // Add required entity
        MenuItems menuItems;
        if (TestUtil.findAll(em, MenuItems.class).isEmpty()) {
            menuItems = MenuItemsResourceIT.createUpdatedEntity(em);
            em.persist(menuItems);
            em.flush();
        } else {
            menuItems = TestUtil.findAll(em, MenuItems.class).get(0);
        }
        userPermissions.getMenuItems().add(menuItems);
        return userPermissions;
    }

    @BeforeEach
    public void initTest() {
        userPermissions = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserPermissions() throws Exception {
        int databaseSizeBeforeCreate = userPermissionsRepository.findAll().size();

        // Create the UserPermissions
        restUserPermissionsMockMvc.perform(post("/api/user-permissions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userPermissions)))
            .andExpect(status().isCreated());

        // Validate the UserPermissions in the database
        List<UserPermissions> userPermissionsList = userPermissionsRepository.findAll();
        assertThat(userPermissionsList).hasSize(databaseSizeBeforeCreate + 1);
        UserPermissions testUserPermissions = userPermissionsList.get(userPermissionsList.size() - 1);
        assertThat(testUserPermissions.getUserPermKey()).isEqualTo(DEFAULT_USER_PERM_KEY);
        assertThat(testUserPermissions.getUserPermDescription()).isEqualTo(DEFAULT_USER_PERM_DESCRIPTION);
        assertThat(testUserPermissions.isIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void createUserPermissionsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userPermissionsRepository.findAll().size();

        // Create the UserPermissions with an existing ID
        userPermissions.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserPermissionsMockMvc.perform(post("/api/user-permissions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userPermissions)))
            .andExpect(status().isBadRequest());

        // Validate the UserPermissions in the database
        List<UserPermissions> userPermissionsList = userPermissionsRepository.findAll();
        assertThat(userPermissionsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllUserPermissions() throws Exception {
        // Initialize the database
        userPermissionsRepository.saveAndFlush(userPermissions);

        // Get all the userPermissionsList
        restUserPermissionsMockMvc.perform(get("/api/user-permissions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userPermissions.getId().intValue())))
            .andExpect(jsonPath("$.[*].userPermKey").value(hasItem(DEFAULT_USER_PERM_KEY)))
            .andExpect(jsonPath("$.[*].userPermDescription").value(hasItem(DEFAULT_USER_PERM_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllUserPermissionsWithEagerRelationshipsIsEnabled() throws Exception {
        UserPermissionsResource userPermissionsResource = new UserPermissionsResource(userPermissionsServiceMock, userPermissionsQueryService);
        when(userPermissionsServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restUserPermissionsMockMvc = MockMvcBuilders.standaloneSetup(userPermissionsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restUserPermissionsMockMvc.perform(get("/api/user-permissions?eagerload=true"))
        .andExpect(status().isOk());

        verify(userPermissionsServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllUserPermissionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        UserPermissionsResource userPermissionsResource = new UserPermissionsResource(userPermissionsServiceMock, userPermissionsQueryService);
            when(userPermissionsServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restUserPermissionsMockMvc = MockMvcBuilders.standaloneSetup(userPermissionsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restUserPermissionsMockMvc.perform(get("/api/user-permissions?eagerload=true"))
        .andExpect(status().isOk());

            verify(userPermissionsServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getUserPermissions() throws Exception {
        // Initialize the database
        userPermissionsRepository.saveAndFlush(userPermissions);

        // Get the userPermissions
        restUserPermissionsMockMvc.perform(get("/api/user-permissions/{id}", userPermissions.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userPermissions.getId().intValue()))
            .andExpect(jsonPath("$.userPermKey").value(DEFAULT_USER_PERM_KEY))
            .andExpect(jsonPath("$.userPermDescription").value(DEFAULT_USER_PERM_DESCRIPTION))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }


    @Test
    @Transactional
    public void getUserPermissionsByIdFiltering() throws Exception {
        // Initialize the database
        userPermissionsRepository.saveAndFlush(userPermissions);

        Long id = userPermissions.getId();

        defaultUserPermissionsShouldBeFound("id.equals=" + id);
        defaultUserPermissionsShouldNotBeFound("id.notEquals=" + id);

        defaultUserPermissionsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUserPermissionsShouldNotBeFound("id.greaterThan=" + id);

        defaultUserPermissionsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUserPermissionsShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllUserPermissionsByUserPermKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        userPermissionsRepository.saveAndFlush(userPermissions);

        // Get all the userPermissionsList where userPermKey equals to DEFAULT_USER_PERM_KEY
        defaultUserPermissionsShouldBeFound("userPermKey.equals=" + DEFAULT_USER_PERM_KEY);

        // Get all the userPermissionsList where userPermKey equals to UPDATED_USER_PERM_KEY
        defaultUserPermissionsShouldNotBeFound("userPermKey.equals=" + UPDATED_USER_PERM_KEY);
    }

    @Test
    @Transactional
    public void getAllUserPermissionsByUserPermKeyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        userPermissionsRepository.saveAndFlush(userPermissions);

        // Get all the userPermissionsList where userPermKey not equals to DEFAULT_USER_PERM_KEY
        defaultUserPermissionsShouldNotBeFound("userPermKey.notEquals=" + DEFAULT_USER_PERM_KEY);

        // Get all the userPermissionsList where userPermKey not equals to UPDATED_USER_PERM_KEY
        defaultUserPermissionsShouldBeFound("userPermKey.notEquals=" + UPDATED_USER_PERM_KEY);
    }

    @Test
    @Transactional
    public void getAllUserPermissionsByUserPermKeyIsInShouldWork() throws Exception {
        // Initialize the database
        userPermissionsRepository.saveAndFlush(userPermissions);

        // Get all the userPermissionsList where userPermKey in DEFAULT_USER_PERM_KEY or UPDATED_USER_PERM_KEY
        defaultUserPermissionsShouldBeFound("userPermKey.in=" + DEFAULT_USER_PERM_KEY + "," + UPDATED_USER_PERM_KEY);

        // Get all the userPermissionsList where userPermKey equals to UPDATED_USER_PERM_KEY
        defaultUserPermissionsShouldNotBeFound("userPermKey.in=" + UPDATED_USER_PERM_KEY);
    }

    @Test
    @Transactional
    public void getAllUserPermissionsByUserPermKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        userPermissionsRepository.saveAndFlush(userPermissions);

        // Get all the userPermissionsList where userPermKey is not null
        defaultUserPermissionsShouldBeFound("userPermKey.specified=true");

        // Get all the userPermissionsList where userPermKey is null
        defaultUserPermissionsShouldNotBeFound("userPermKey.specified=false");
    }
                @Test
    @Transactional
    public void getAllUserPermissionsByUserPermKeyContainsSomething() throws Exception {
        // Initialize the database
        userPermissionsRepository.saveAndFlush(userPermissions);

        // Get all the userPermissionsList where userPermKey contains DEFAULT_USER_PERM_KEY
        defaultUserPermissionsShouldBeFound("userPermKey.contains=" + DEFAULT_USER_PERM_KEY);

        // Get all the userPermissionsList where userPermKey contains UPDATED_USER_PERM_KEY
        defaultUserPermissionsShouldNotBeFound("userPermKey.contains=" + UPDATED_USER_PERM_KEY);
    }

    @Test
    @Transactional
    public void getAllUserPermissionsByUserPermKeyNotContainsSomething() throws Exception {
        // Initialize the database
        userPermissionsRepository.saveAndFlush(userPermissions);

        // Get all the userPermissionsList where userPermKey does not contain DEFAULT_USER_PERM_KEY
        defaultUserPermissionsShouldNotBeFound("userPermKey.doesNotContain=" + DEFAULT_USER_PERM_KEY);

        // Get all the userPermissionsList where userPermKey does not contain UPDATED_USER_PERM_KEY
        defaultUserPermissionsShouldBeFound("userPermKey.doesNotContain=" + UPDATED_USER_PERM_KEY);
    }


    @Test
    @Transactional
    public void getAllUserPermissionsByUserPermDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        userPermissionsRepository.saveAndFlush(userPermissions);

        // Get all the userPermissionsList where userPermDescription equals to DEFAULT_USER_PERM_DESCRIPTION
        defaultUserPermissionsShouldBeFound("userPermDescription.equals=" + DEFAULT_USER_PERM_DESCRIPTION);

        // Get all the userPermissionsList where userPermDescription equals to UPDATED_USER_PERM_DESCRIPTION
        defaultUserPermissionsShouldNotBeFound("userPermDescription.equals=" + UPDATED_USER_PERM_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllUserPermissionsByUserPermDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        userPermissionsRepository.saveAndFlush(userPermissions);

        // Get all the userPermissionsList where userPermDescription not equals to DEFAULT_USER_PERM_DESCRIPTION
        defaultUserPermissionsShouldNotBeFound("userPermDescription.notEquals=" + DEFAULT_USER_PERM_DESCRIPTION);

        // Get all the userPermissionsList where userPermDescription not equals to UPDATED_USER_PERM_DESCRIPTION
        defaultUserPermissionsShouldBeFound("userPermDescription.notEquals=" + UPDATED_USER_PERM_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllUserPermissionsByUserPermDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        userPermissionsRepository.saveAndFlush(userPermissions);

        // Get all the userPermissionsList where userPermDescription in DEFAULT_USER_PERM_DESCRIPTION or UPDATED_USER_PERM_DESCRIPTION
        defaultUserPermissionsShouldBeFound("userPermDescription.in=" + DEFAULT_USER_PERM_DESCRIPTION + "," + UPDATED_USER_PERM_DESCRIPTION);

        // Get all the userPermissionsList where userPermDescription equals to UPDATED_USER_PERM_DESCRIPTION
        defaultUserPermissionsShouldNotBeFound("userPermDescription.in=" + UPDATED_USER_PERM_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllUserPermissionsByUserPermDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        userPermissionsRepository.saveAndFlush(userPermissions);

        // Get all the userPermissionsList where userPermDescription is not null
        defaultUserPermissionsShouldBeFound("userPermDescription.specified=true");

        // Get all the userPermissionsList where userPermDescription is null
        defaultUserPermissionsShouldNotBeFound("userPermDescription.specified=false");
    }
                @Test
    @Transactional
    public void getAllUserPermissionsByUserPermDescriptionContainsSomething() throws Exception {
        // Initialize the database
        userPermissionsRepository.saveAndFlush(userPermissions);

        // Get all the userPermissionsList where userPermDescription contains DEFAULT_USER_PERM_DESCRIPTION
        defaultUserPermissionsShouldBeFound("userPermDescription.contains=" + DEFAULT_USER_PERM_DESCRIPTION);

        // Get all the userPermissionsList where userPermDescription contains UPDATED_USER_PERM_DESCRIPTION
        defaultUserPermissionsShouldNotBeFound("userPermDescription.contains=" + UPDATED_USER_PERM_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllUserPermissionsByUserPermDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        userPermissionsRepository.saveAndFlush(userPermissions);

        // Get all the userPermissionsList where userPermDescription does not contain DEFAULT_USER_PERM_DESCRIPTION
        defaultUserPermissionsShouldNotBeFound("userPermDescription.doesNotContain=" + DEFAULT_USER_PERM_DESCRIPTION);

        // Get all the userPermissionsList where userPermDescription does not contain UPDATED_USER_PERM_DESCRIPTION
        defaultUserPermissionsShouldBeFound("userPermDescription.doesNotContain=" + UPDATED_USER_PERM_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllUserPermissionsByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        userPermissionsRepository.saveAndFlush(userPermissions);

        // Get all the userPermissionsList where isActive equals to DEFAULT_IS_ACTIVE
        defaultUserPermissionsShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the userPermissionsList where isActive equals to UPDATED_IS_ACTIVE
        defaultUserPermissionsShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllUserPermissionsByIsActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        userPermissionsRepository.saveAndFlush(userPermissions);

        // Get all the userPermissionsList where isActive not equals to DEFAULT_IS_ACTIVE
        defaultUserPermissionsShouldNotBeFound("isActive.notEquals=" + DEFAULT_IS_ACTIVE);

        // Get all the userPermissionsList where isActive not equals to UPDATED_IS_ACTIVE
        defaultUserPermissionsShouldBeFound("isActive.notEquals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllUserPermissionsByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        userPermissionsRepository.saveAndFlush(userPermissions);

        // Get all the userPermissionsList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultUserPermissionsShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the userPermissionsList where isActive equals to UPDATED_IS_ACTIVE
        defaultUserPermissionsShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllUserPermissionsByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        userPermissionsRepository.saveAndFlush(userPermissions);

        // Get all the userPermissionsList where isActive is not null
        defaultUserPermissionsShouldBeFound("isActive.specified=true");

        // Get all the userPermissionsList where isActive is null
        defaultUserPermissionsShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserPermissionsByMenuItemsIsEqualToSomething() throws Exception {
        // Get already existing entity
        MenuItems menuItems = userPermissions.getMenuItems();
        userPermissionsRepository.saveAndFlush(userPermissions);
        Long menuItemsId = menuItems.getId();

        // Get all the userPermissionsList where menuItems equals to menuItemsId
        defaultUserPermissionsShouldBeFound("menuItemsId.equals=" + menuItemsId);

        // Get all the userPermissionsList where menuItems equals to menuItemsId + 1
        defaultUserPermissionsShouldNotBeFound("menuItemsId.equals=" + (menuItemsId + 1));
    }


    @Test
    @Transactional
    public void getAllUserPermissionsByUsersIsEqualToSomething() throws Exception {
        // Initialize the database
        userPermissionsRepository.saveAndFlush(userPermissions);
        ExUser users = ExUserResourceIT.createEntity(em);
        em.persist(users);
        em.flush();
        userPermissions.addUsers(users);
        userPermissionsRepository.saveAndFlush(userPermissions);
        Long usersId = users.getId();

        // Get all the userPermissionsList where users equals to usersId
        defaultUserPermissionsShouldBeFound("usersId.equals=" + usersId);

        // Get all the userPermissionsList where users equals to usersId + 1
        defaultUserPermissionsShouldNotBeFound("usersId.equals=" + (usersId + 1));
    }


    @Test
    @Transactional
    public void getAllUserPermissionsByUserGroupIsEqualToSomething() throws Exception {
        // Initialize the database
        userPermissionsRepository.saveAndFlush(userPermissions);
        UserGroup userGroup = UserGroupResourceIT.createEntity(em);
        em.persist(userGroup);
        em.flush();
        userPermissions.addUserGroup(userGroup);
        userPermissionsRepository.saveAndFlush(userPermissions);
        Long userGroupId = userGroup.getId();

        // Get all the userPermissionsList where userGroup equals to userGroupId
        defaultUserPermissionsShouldBeFound("userGroupId.equals=" + userGroupId);

        // Get all the userPermissionsList where userGroup equals to userGroupId + 1
        defaultUserPermissionsShouldNotBeFound("userGroupId.equals=" + (userGroupId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUserPermissionsShouldBeFound(String filter) throws Exception {
        restUserPermissionsMockMvc.perform(get("/api/user-permissions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userPermissions.getId().intValue())))
            .andExpect(jsonPath("$.[*].userPermKey").value(hasItem(DEFAULT_USER_PERM_KEY)))
            .andExpect(jsonPath("$.[*].userPermDescription").value(hasItem(DEFAULT_USER_PERM_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restUserPermissionsMockMvc.perform(get("/api/user-permissions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUserPermissionsShouldNotBeFound(String filter) throws Exception {
        restUserPermissionsMockMvc.perform(get("/api/user-permissions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUserPermissionsMockMvc.perform(get("/api/user-permissions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingUserPermissions() throws Exception {
        // Get the userPermissions
        restUserPermissionsMockMvc.perform(get("/api/user-permissions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserPermissions() throws Exception {
        // Initialize the database
        userPermissionsService.save(userPermissions);

        int databaseSizeBeforeUpdate = userPermissionsRepository.findAll().size();

        // Update the userPermissions
        UserPermissions updatedUserPermissions = userPermissionsRepository.findById(userPermissions.getId()).get();
        // Disconnect from session so that the updates on updatedUserPermissions are not directly saved in db
        em.detach(updatedUserPermissions);
        updatedUserPermissions
            .userPermKey(UPDATED_USER_PERM_KEY)
            .userPermDescription(UPDATED_USER_PERM_DESCRIPTION)
            .isActive(UPDATED_IS_ACTIVE);

        restUserPermissionsMockMvc.perform(put("/api/user-permissions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUserPermissions)))
            .andExpect(status().isOk());

        // Validate the UserPermissions in the database
        List<UserPermissions> userPermissionsList = userPermissionsRepository.findAll();
        assertThat(userPermissionsList).hasSize(databaseSizeBeforeUpdate);
        UserPermissions testUserPermissions = userPermissionsList.get(userPermissionsList.size() - 1);
        assertThat(testUserPermissions.getUserPermKey()).isEqualTo(UPDATED_USER_PERM_KEY);
        assertThat(testUserPermissions.getUserPermDescription()).isEqualTo(UPDATED_USER_PERM_DESCRIPTION);
        assertThat(testUserPermissions.isIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void updateNonExistingUserPermissions() throws Exception {
        int databaseSizeBeforeUpdate = userPermissionsRepository.findAll().size();

        // Create the UserPermissions

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserPermissionsMockMvc.perform(put("/api/user-permissions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userPermissions)))
            .andExpect(status().isBadRequest());

        // Validate the UserPermissions in the database
        List<UserPermissions> userPermissionsList = userPermissionsRepository.findAll();
        assertThat(userPermissionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteUserPermissions() throws Exception {
        // Initialize the database
        userPermissionsService.save(userPermissions);

        int databaseSizeBeforeDelete = userPermissionsRepository.findAll().size();

        // Delete the userPermissions
        restUserPermissionsMockMvc.perform(delete("/api/user-permissions/{id}", userPermissions.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserPermissions> userPermissionsList = userPermissionsRepository.findAll();
        assertThat(userPermissionsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.WikunumApp;
import com.alphadevs.sales.domain.Location;
import com.alphadevs.sales.domain.DocumentHistory;
import com.alphadevs.sales.domain.Company;
import com.alphadevs.sales.domain.ConfigurationItems;
import com.alphadevs.sales.domain.ExUser;
import com.alphadevs.sales.repository.LocationRepository;
import com.alphadevs.sales.service.LocationService;
import com.alphadevs.sales.web.rest.errors.ExceptionTranslator;
import com.alphadevs.sales.service.dto.LocationCriteria;
import com.alphadevs.sales.service.LocationQueryService;

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
 * Integration tests for the {@link LocationResource} REST controller.
 */
@SpringBootTest(classes = WikunumApp.class)
public class LocationResourceIT {

    private static final String DEFAULT_LOCATION_CODE = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    @Autowired
    private LocationRepository locationRepository;

    @Mock
    private LocationRepository locationRepositoryMock;

    @Mock
    private LocationService locationServiceMock;

    @Autowired
    private LocationService locationService;

    @Autowired
    private LocationQueryService locationQueryService;

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

    private MockMvc restLocationMockMvc;

    private Location location;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LocationResource locationResource = new LocationResource(locationService, locationQueryService);
        this.restLocationMockMvc = MockMvcBuilders.standaloneSetup(locationResource)
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
    public static Location createEntity(EntityManager em) {
        Location location = new Location()
            .locationCode(DEFAULT_LOCATION_CODE)
            .locationName(DEFAULT_LOCATION_NAME)
            .isActive(DEFAULT_IS_ACTIVE);
        // Add required entity
        Company company;
        if (TestUtil.findAll(em, Company.class).isEmpty()) {
            company = CompanyResourceIT.createEntity(em);
            em.persist(company);
            em.flush();
        } else {
            company = TestUtil.findAll(em, Company.class).get(0);
        }
        location.setCompany(company);
        // Add required entity
        ExUser exUser;
        if (TestUtil.findAll(em, ExUser.class).isEmpty()) {
            exUser = ExUserResourceIT.createEntity(em);
            em.persist(exUser);
            em.flush();
        } else {
            exUser = TestUtil.findAll(em, ExUser.class).get(0);
        }
        location.getUsers().add(exUser);
        return location;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Location createUpdatedEntity(EntityManager em) {
        Location location = new Location()
            .locationCode(UPDATED_LOCATION_CODE)
            .locationName(UPDATED_LOCATION_NAME)
            .isActive(UPDATED_IS_ACTIVE);
        // Add required entity
        Company company;
        if (TestUtil.findAll(em, Company.class).isEmpty()) {
            company = CompanyResourceIT.createUpdatedEntity(em);
            em.persist(company);
            em.flush();
        } else {
            company = TestUtil.findAll(em, Company.class).get(0);
        }
        location.setCompany(company);
        // Add required entity
        ExUser exUser;
        if (TestUtil.findAll(em, ExUser.class).isEmpty()) {
            exUser = ExUserResourceIT.createUpdatedEntity(em);
            em.persist(exUser);
            em.flush();
        } else {
            exUser = TestUtil.findAll(em, ExUser.class).get(0);
        }
        location.getUsers().add(exUser);
        return location;
    }

    @BeforeEach
    public void initTest() {
        location = createEntity(em);
    }

    @Test
    @Transactional
    public void createLocation() throws Exception {
        int databaseSizeBeforeCreate = locationRepository.findAll().size();

        // Create the Location
        restLocationMockMvc.perform(post("/api/locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(location)))
            .andExpect(status().isCreated());

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeCreate + 1);
        Location testLocation = locationList.get(locationList.size() - 1);
        assertThat(testLocation.getLocationCode()).isEqualTo(DEFAULT_LOCATION_CODE);
        assertThat(testLocation.getLocationName()).isEqualTo(DEFAULT_LOCATION_NAME);
        assertThat(testLocation.isIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void createLocationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = locationRepository.findAll().size();

        // Create the Location with an existing ID
        location.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLocationMockMvc.perform(post("/api/locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(location)))
            .andExpect(status().isBadRequest());

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkLocationCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = locationRepository.findAll().size();
        // set the field null
        location.setLocationCode(null);

        // Create the Location, which fails.

        restLocationMockMvc.perform(post("/api/locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(location)))
            .andExpect(status().isBadRequest());

        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLocationNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = locationRepository.findAll().size();
        // set the field null
        location.setLocationName(null);

        // Create the Location, which fails.

        restLocationMockMvc.perform(post("/api/locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(location)))
            .andExpect(status().isBadRequest());

        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLocations() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList
        restLocationMockMvc.perform(get("/api/locations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(location.getId().intValue())))
            .andExpect(jsonPath("$.[*].locationCode").value(hasItem(DEFAULT_LOCATION_CODE)))
            .andExpect(jsonPath("$.[*].locationName").value(hasItem(DEFAULT_LOCATION_NAME)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllLocationsWithEagerRelationshipsIsEnabled() throws Exception {
        LocationResource locationResource = new LocationResource(locationServiceMock, locationQueryService);
        when(locationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restLocationMockMvc = MockMvcBuilders.standaloneSetup(locationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restLocationMockMvc.perform(get("/api/locations?eagerload=true"))
        .andExpect(status().isOk());

        verify(locationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllLocationsWithEagerRelationshipsIsNotEnabled() throws Exception {
        LocationResource locationResource = new LocationResource(locationServiceMock, locationQueryService);
            when(locationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restLocationMockMvc = MockMvcBuilders.standaloneSetup(locationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restLocationMockMvc.perform(get("/api/locations?eagerload=true"))
        .andExpect(status().isOk());

            verify(locationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getLocation() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get the location
        restLocationMockMvc.perform(get("/api/locations/{id}", location.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(location.getId().intValue()))
            .andExpect(jsonPath("$.locationCode").value(DEFAULT_LOCATION_CODE))
            .andExpect(jsonPath("$.locationName").value(DEFAULT_LOCATION_NAME))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }


    @Test
    @Transactional
    public void getLocationsByIdFiltering() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        Long id = location.getId();

        defaultLocationShouldBeFound("id.equals=" + id);
        defaultLocationShouldNotBeFound("id.notEquals=" + id);

        defaultLocationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLocationShouldNotBeFound("id.greaterThan=" + id);

        defaultLocationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLocationShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllLocationsByLocationCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where locationCode equals to DEFAULT_LOCATION_CODE
        defaultLocationShouldBeFound("locationCode.equals=" + DEFAULT_LOCATION_CODE);

        // Get all the locationList where locationCode equals to UPDATED_LOCATION_CODE
        defaultLocationShouldNotBeFound("locationCode.equals=" + UPDATED_LOCATION_CODE);
    }

    @Test
    @Transactional
    public void getAllLocationsByLocationCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where locationCode not equals to DEFAULT_LOCATION_CODE
        defaultLocationShouldNotBeFound("locationCode.notEquals=" + DEFAULT_LOCATION_CODE);

        // Get all the locationList where locationCode not equals to UPDATED_LOCATION_CODE
        defaultLocationShouldBeFound("locationCode.notEquals=" + UPDATED_LOCATION_CODE);
    }

    @Test
    @Transactional
    public void getAllLocationsByLocationCodeIsInShouldWork() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where locationCode in DEFAULT_LOCATION_CODE or UPDATED_LOCATION_CODE
        defaultLocationShouldBeFound("locationCode.in=" + DEFAULT_LOCATION_CODE + "," + UPDATED_LOCATION_CODE);

        // Get all the locationList where locationCode equals to UPDATED_LOCATION_CODE
        defaultLocationShouldNotBeFound("locationCode.in=" + UPDATED_LOCATION_CODE);
    }

    @Test
    @Transactional
    public void getAllLocationsByLocationCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where locationCode is not null
        defaultLocationShouldBeFound("locationCode.specified=true");

        // Get all the locationList where locationCode is null
        defaultLocationShouldNotBeFound("locationCode.specified=false");
    }
                @Test
    @Transactional
    public void getAllLocationsByLocationCodeContainsSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where locationCode contains DEFAULT_LOCATION_CODE
        defaultLocationShouldBeFound("locationCode.contains=" + DEFAULT_LOCATION_CODE);

        // Get all the locationList where locationCode contains UPDATED_LOCATION_CODE
        defaultLocationShouldNotBeFound("locationCode.contains=" + UPDATED_LOCATION_CODE);
    }

    @Test
    @Transactional
    public void getAllLocationsByLocationCodeNotContainsSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where locationCode does not contain DEFAULT_LOCATION_CODE
        defaultLocationShouldNotBeFound("locationCode.doesNotContain=" + DEFAULT_LOCATION_CODE);

        // Get all the locationList where locationCode does not contain UPDATED_LOCATION_CODE
        defaultLocationShouldBeFound("locationCode.doesNotContain=" + UPDATED_LOCATION_CODE);
    }


    @Test
    @Transactional
    public void getAllLocationsByLocationNameIsEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where locationName equals to DEFAULT_LOCATION_NAME
        defaultLocationShouldBeFound("locationName.equals=" + DEFAULT_LOCATION_NAME);

        // Get all the locationList where locationName equals to UPDATED_LOCATION_NAME
        defaultLocationShouldNotBeFound("locationName.equals=" + UPDATED_LOCATION_NAME);
    }

    @Test
    @Transactional
    public void getAllLocationsByLocationNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where locationName not equals to DEFAULT_LOCATION_NAME
        defaultLocationShouldNotBeFound("locationName.notEquals=" + DEFAULT_LOCATION_NAME);

        // Get all the locationList where locationName not equals to UPDATED_LOCATION_NAME
        defaultLocationShouldBeFound("locationName.notEquals=" + UPDATED_LOCATION_NAME);
    }

    @Test
    @Transactional
    public void getAllLocationsByLocationNameIsInShouldWork() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where locationName in DEFAULT_LOCATION_NAME or UPDATED_LOCATION_NAME
        defaultLocationShouldBeFound("locationName.in=" + DEFAULT_LOCATION_NAME + "," + UPDATED_LOCATION_NAME);

        // Get all the locationList where locationName equals to UPDATED_LOCATION_NAME
        defaultLocationShouldNotBeFound("locationName.in=" + UPDATED_LOCATION_NAME);
    }

    @Test
    @Transactional
    public void getAllLocationsByLocationNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where locationName is not null
        defaultLocationShouldBeFound("locationName.specified=true");

        // Get all the locationList where locationName is null
        defaultLocationShouldNotBeFound("locationName.specified=false");
    }
                @Test
    @Transactional
    public void getAllLocationsByLocationNameContainsSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where locationName contains DEFAULT_LOCATION_NAME
        defaultLocationShouldBeFound("locationName.contains=" + DEFAULT_LOCATION_NAME);

        // Get all the locationList where locationName contains UPDATED_LOCATION_NAME
        defaultLocationShouldNotBeFound("locationName.contains=" + UPDATED_LOCATION_NAME);
    }

    @Test
    @Transactional
    public void getAllLocationsByLocationNameNotContainsSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where locationName does not contain DEFAULT_LOCATION_NAME
        defaultLocationShouldNotBeFound("locationName.doesNotContain=" + DEFAULT_LOCATION_NAME);

        // Get all the locationList where locationName does not contain UPDATED_LOCATION_NAME
        defaultLocationShouldBeFound("locationName.doesNotContain=" + UPDATED_LOCATION_NAME);
    }


    @Test
    @Transactional
    public void getAllLocationsByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where isActive equals to DEFAULT_IS_ACTIVE
        defaultLocationShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the locationList where isActive equals to UPDATED_IS_ACTIVE
        defaultLocationShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllLocationsByIsActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where isActive not equals to DEFAULT_IS_ACTIVE
        defaultLocationShouldNotBeFound("isActive.notEquals=" + DEFAULT_IS_ACTIVE);

        // Get all the locationList where isActive not equals to UPDATED_IS_ACTIVE
        defaultLocationShouldBeFound("isActive.notEquals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllLocationsByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultLocationShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the locationList where isActive equals to UPDATED_IS_ACTIVE
        defaultLocationShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllLocationsByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where isActive is not null
        defaultLocationShouldBeFound("isActive.specified=true");

        // Get all the locationList where isActive is null
        defaultLocationShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    public void getAllLocationsByHistoryIsEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);
        DocumentHistory history = DocumentHistoryResourceIT.createEntity(em);
        em.persist(history);
        em.flush();
        location.setHistory(history);
        locationRepository.saveAndFlush(location);
        Long historyId = history.getId();

        // Get all the locationList where history equals to historyId
        defaultLocationShouldBeFound("historyId.equals=" + historyId);

        // Get all the locationList where history equals to historyId + 1
        defaultLocationShouldNotBeFound("historyId.equals=" + (historyId + 1));
    }


    @Test
    @Transactional
    public void getAllLocationsByCompanyIsEqualToSomething() throws Exception {
        // Get already existing entity
        Company company = location.getCompany();
        locationRepository.saveAndFlush(location);
        Long companyId = company.getId();

        // Get all the locationList where company equals to companyId
        defaultLocationShouldBeFound("companyId.equals=" + companyId);

        // Get all the locationList where company equals to companyId + 1
        defaultLocationShouldNotBeFound("companyId.equals=" + (companyId + 1));
    }


    @Test
    @Transactional
    public void getAllLocationsByConfigitemsIsEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);
        ConfigurationItems configitems = ConfigurationItemsResourceIT.createEntity(em);
        em.persist(configitems);
        em.flush();
        location.addConfigitems(configitems);
        locationRepository.saveAndFlush(location);
        Long configitemsId = configitems.getId();

        // Get all the locationList where configitems equals to configitemsId
        defaultLocationShouldBeFound("configitemsId.equals=" + configitemsId);

        // Get all the locationList where configitems equals to configitemsId + 1
        defaultLocationShouldNotBeFound("configitemsId.equals=" + (configitemsId + 1));
    }


    @Test
    @Transactional
    public void getAllLocationsByUsersIsEqualToSomething() throws Exception {
        // Get already existing entity
        ExUser users = location.getUsers();
        locationRepository.saveAndFlush(location);
        Long usersId = users.getId();

        // Get all the locationList where users equals to usersId
        defaultLocationShouldBeFound("usersId.equals=" + usersId);

        // Get all the locationList where users equals to usersId + 1
        defaultLocationShouldNotBeFound("usersId.equals=" + (usersId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLocationShouldBeFound(String filter) throws Exception {
        restLocationMockMvc.perform(get("/api/locations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(location.getId().intValue())))
            .andExpect(jsonPath("$.[*].locationCode").value(hasItem(DEFAULT_LOCATION_CODE)))
            .andExpect(jsonPath("$.[*].locationName").value(hasItem(DEFAULT_LOCATION_NAME)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restLocationMockMvc.perform(get("/api/locations/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLocationShouldNotBeFound(String filter) throws Exception {
        restLocationMockMvc.perform(get("/api/locations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLocationMockMvc.perform(get("/api/locations/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingLocation() throws Exception {
        // Get the location
        restLocationMockMvc.perform(get("/api/locations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLocation() throws Exception {
        // Initialize the database
        locationService.save(location);

        int databaseSizeBeforeUpdate = locationRepository.findAll().size();

        // Update the location
        Location updatedLocation = locationRepository.findById(location.getId()).get();
        // Disconnect from session so that the updates on updatedLocation are not directly saved in db
        em.detach(updatedLocation);
        updatedLocation
            .locationCode(UPDATED_LOCATION_CODE)
            .locationName(UPDATED_LOCATION_NAME)
            .isActive(UPDATED_IS_ACTIVE);

        restLocationMockMvc.perform(put("/api/locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedLocation)))
            .andExpect(status().isOk());

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeUpdate);
        Location testLocation = locationList.get(locationList.size() - 1);
        assertThat(testLocation.getLocationCode()).isEqualTo(UPDATED_LOCATION_CODE);
        assertThat(testLocation.getLocationName()).isEqualTo(UPDATED_LOCATION_NAME);
        assertThat(testLocation.isIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void updateNonExistingLocation() throws Exception {
        int databaseSizeBeforeUpdate = locationRepository.findAll().size();

        // Create the Location

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocationMockMvc.perform(put("/api/locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(location)))
            .andExpect(status().isBadRequest());

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteLocation() throws Exception {
        // Initialize the database
        locationService.save(location);

        int databaseSizeBeforeDelete = locationRepository.findAll().size();

        // Delete the location
        restLocationMockMvc.perform(delete("/api/locations/{id}", location.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

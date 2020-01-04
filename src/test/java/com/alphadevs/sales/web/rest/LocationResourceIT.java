package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.WikunumApp;
import com.alphadevs.sales.domain.Location;
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
import org.springframework.util.Base64Utils;
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

    private static final String DEFAULT_EMAIL = "jE-7@_y\"?kE.Ho)8}";
    private static final String UPDATED_EMAIL = "[='em[@.T.m";

    private static final Double DEFAULT_RATING = 1D;
    private static final Double UPDATED_RATING = 2D;
    private static final Double SMALLER_RATING = 1D - 1D;

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_LINE_1 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_LINE_1 = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_LINE_2 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_LINE_2 = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_VAT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_VAT_NUMBER = "BBBBBBBBBB";

    private static final Double DEFAULT_VAT_PERC = 1D;
    private static final Double UPDATED_VAT_PERC = 2D;
    private static final Double SMALLER_VAT_PERC = 1D - 1D;

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
            .isActive(DEFAULT_IS_ACTIVE)
            .email(DEFAULT_EMAIL)
            .rating(DEFAULT_RATING)
            .phone(DEFAULT_PHONE)
            .addressLine1(DEFAULT_ADDRESS_LINE_1)
            .addressLine2(DEFAULT_ADDRESS_LINE_2)
            .city(DEFAULT_CITY)
            .country(DEFAULT_COUNTRY)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .vatNumber(DEFAULT_VAT_NUMBER)
            .vatPerc(DEFAULT_VAT_PERC);
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
            .isActive(UPDATED_IS_ACTIVE)
            .email(UPDATED_EMAIL)
            .rating(UPDATED_RATING)
            .phone(UPDATED_PHONE)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .vatNumber(UPDATED_VAT_NUMBER)
            .vatPerc(UPDATED_VAT_PERC);
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
        assertThat(testLocation.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testLocation.getRating()).isEqualTo(DEFAULT_RATING);
        assertThat(testLocation.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testLocation.getAddressLine1()).isEqualTo(DEFAULT_ADDRESS_LINE_1);
        assertThat(testLocation.getAddressLine2()).isEqualTo(DEFAULT_ADDRESS_LINE_2);
        assertThat(testLocation.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testLocation.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testLocation.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testLocation.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testLocation.getVatNumber()).isEqualTo(DEFAULT_VAT_NUMBER);
        assertThat(testLocation.getVatPerc()).isEqualTo(DEFAULT_VAT_PERC);
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
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = locationRepository.findAll().size();
        // set the field null
        location.setEmail(null);

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
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING.doubleValue())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].addressLine1").value(hasItem(DEFAULT_ADDRESS_LINE_1)))
            .andExpect(jsonPath("$.[*].addressLine2").value(hasItem(DEFAULT_ADDRESS_LINE_2)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].vatNumber").value(hasItem(DEFAULT_VAT_NUMBER)))
            .andExpect(jsonPath("$.[*].vatPerc").value(hasItem(DEFAULT_VAT_PERC.doubleValue())));
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
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING.doubleValue()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.addressLine1").value(DEFAULT_ADDRESS_LINE_1))
            .andExpect(jsonPath("$.addressLine2").value(DEFAULT_ADDRESS_LINE_2))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.vatNumber").value(DEFAULT_VAT_NUMBER))
            .andExpect(jsonPath("$.vatPerc").value(DEFAULT_VAT_PERC.doubleValue()));
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
    public void getAllLocationsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where email equals to DEFAULT_EMAIL
        defaultLocationShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the locationList where email equals to UPDATED_EMAIL
        defaultLocationShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllLocationsByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where email not equals to DEFAULT_EMAIL
        defaultLocationShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the locationList where email not equals to UPDATED_EMAIL
        defaultLocationShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllLocationsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultLocationShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the locationList where email equals to UPDATED_EMAIL
        defaultLocationShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllLocationsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where email is not null
        defaultLocationShouldBeFound("email.specified=true");

        // Get all the locationList where email is null
        defaultLocationShouldNotBeFound("email.specified=false");
    }
                @Test
    @Transactional
    public void getAllLocationsByEmailContainsSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where email contains DEFAULT_EMAIL
        defaultLocationShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the locationList where email contains UPDATED_EMAIL
        defaultLocationShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllLocationsByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where email does not contain DEFAULT_EMAIL
        defaultLocationShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the locationList where email does not contain UPDATED_EMAIL
        defaultLocationShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }


    @Test
    @Transactional
    public void getAllLocationsByRatingIsEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where rating equals to DEFAULT_RATING
        defaultLocationShouldBeFound("rating.equals=" + DEFAULT_RATING);

        // Get all the locationList where rating equals to UPDATED_RATING
        defaultLocationShouldNotBeFound("rating.equals=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    public void getAllLocationsByRatingIsNotEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where rating not equals to DEFAULT_RATING
        defaultLocationShouldNotBeFound("rating.notEquals=" + DEFAULT_RATING);

        // Get all the locationList where rating not equals to UPDATED_RATING
        defaultLocationShouldBeFound("rating.notEquals=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    public void getAllLocationsByRatingIsInShouldWork() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where rating in DEFAULT_RATING or UPDATED_RATING
        defaultLocationShouldBeFound("rating.in=" + DEFAULT_RATING + "," + UPDATED_RATING);

        // Get all the locationList where rating equals to UPDATED_RATING
        defaultLocationShouldNotBeFound("rating.in=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    public void getAllLocationsByRatingIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where rating is not null
        defaultLocationShouldBeFound("rating.specified=true");

        // Get all the locationList where rating is null
        defaultLocationShouldNotBeFound("rating.specified=false");
    }

    @Test
    @Transactional
    public void getAllLocationsByRatingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where rating is greater than or equal to DEFAULT_RATING
        defaultLocationShouldBeFound("rating.greaterThanOrEqual=" + DEFAULT_RATING);

        // Get all the locationList where rating is greater than or equal to UPDATED_RATING
        defaultLocationShouldNotBeFound("rating.greaterThanOrEqual=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    public void getAllLocationsByRatingIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where rating is less than or equal to DEFAULT_RATING
        defaultLocationShouldBeFound("rating.lessThanOrEqual=" + DEFAULT_RATING);

        // Get all the locationList where rating is less than or equal to SMALLER_RATING
        defaultLocationShouldNotBeFound("rating.lessThanOrEqual=" + SMALLER_RATING);
    }

    @Test
    @Transactional
    public void getAllLocationsByRatingIsLessThanSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where rating is less than DEFAULT_RATING
        defaultLocationShouldNotBeFound("rating.lessThan=" + DEFAULT_RATING);

        // Get all the locationList where rating is less than UPDATED_RATING
        defaultLocationShouldBeFound("rating.lessThan=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    public void getAllLocationsByRatingIsGreaterThanSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where rating is greater than DEFAULT_RATING
        defaultLocationShouldNotBeFound("rating.greaterThan=" + DEFAULT_RATING);

        // Get all the locationList where rating is greater than SMALLER_RATING
        defaultLocationShouldBeFound("rating.greaterThan=" + SMALLER_RATING);
    }


    @Test
    @Transactional
    public void getAllLocationsByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where phone equals to DEFAULT_PHONE
        defaultLocationShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the locationList where phone equals to UPDATED_PHONE
        defaultLocationShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllLocationsByPhoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where phone not equals to DEFAULT_PHONE
        defaultLocationShouldNotBeFound("phone.notEquals=" + DEFAULT_PHONE);

        // Get all the locationList where phone not equals to UPDATED_PHONE
        defaultLocationShouldBeFound("phone.notEquals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllLocationsByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultLocationShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the locationList where phone equals to UPDATED_PHONE
        defaultLocationShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllLocationsByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where phone is not null
        defaultLocationShouldBeFound("phone.specified=true");

        // Get all the locationList where phone is null
        defaultLocationShouldNotBeFound("phone.specified=false");
    }
                @Test
    @Transactional
    public void getAllLocationsByPhoneContainsSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where phone contains DEFAULT_PHONE
        defaultLocationShouldBeFound("phone.contains=" + DEFAULT_PHONE);

        // Get all the locationList where phone contains UPDATED_PHONE
        defaultLocationShouldNotBeFound("phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllLocationsByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where phone does not contain DEFAULT_PHONE
        defaultLocationShouldNotBeFound("phone.doesNotContain=" + DEFAULT_PHONE);

        // Get all the locationList where phone does not contain UPDATED_PHONE
        defaultLocationShouldBeFound("phone.doesNotContain=" + UPDATED_PHONE);
    }


    @Test
    @Transactional
    public void getAllLocationsByAddressLine1IsEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where addressLine1 equals to DEFAULT_ADDRESS_LINE_1
        defaultLocationShouldBeFound("addressLine1.equals=" + DEFAULT_ADDRESS_LINE_1);

        // Get all the locationList where addressLine1 equals to UPDATED_ADDRESS_LINE_1
        defaultLocationShouldNotBeFound("addressLine1.equals=" + UPDATED_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    public void getAllLocationsByAddressLine1IsNotEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where addressLine1 not equals to DEFAULT_ADDRESS_LINE_1
        defaultLocationShouldNotBeFound("addressLine1.notEquals=" + DEFAULT_ADDRESS_LINE_1);

        // Get all the locationList where addressLine1 not equals to UPDATED_ADDRESS_LINE_1
        defaultLocationShouldBeFound("addressLine1.notEquals=" + UPDATED_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    public void getAllLocationsByAddressLine1IsInShouldWork() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where addressLine1 in DEFAULT_ADDRESS_LINE_1 or UPDATED_ADDRESS_LINE_1
        defaultLocationShouldBeFound("addressLine1.in=" + DEFAULT_ADDRESS_LINE_1 + "," + UPDATED_ADDRESS_LINE_1);

        // Get all the locationList where addressLine1 equals to UPDATED_ADDRESS_LINE_1
        defaultLocationShouldNotBeFound("addressLine1.in=" + UPDATED_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    public void getAllLocationsByAddressLine1IsNullOrNotNull() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where addressLine1 is not null
        defaultLocationShouldBeFound("addressLine1.specified=true");

        // Get all the locationList where addressLine1 is null
        defaultLocationShouldNotBeFound("addressLine1.specified=false");
    }
                @Test
    @Transactional
    public void getAllLocationsByAddressLine1ContainsSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where addressLine1 contains DEFAULT_ADDRESS_LINE_1
        defaultLocationShouldBeFound("addressLine1.contains=" + DEFAULT_ADDRESS_LINE_1);

        // Get all the locationList where addressLine1 contains UPDATED_ADDRESS_LINE_1
        defaultLocationShouldNotBeFound("addressLine1.contains=" + UPDATED_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    public void getAllLocationsByAddressLine1NotContainsSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where addressLine1 does not contain DEFAULT_ADDRESS_LINE_1
        defaultLocationShouldNotBeFound("addressLine1.doesNotContain=" + DEFAULT_ADDRESS_LINE_1);

        // Get all the locationList where addressLine1 does not contain UPDATED_ADDRESS_LINE_1
        defaultLocationShouldBeFound("addressLine1.doesNotContain=" + UPDATED_ADDRESS_LINE_1);
    }


    @Test
    @Transactional
    public void getAllLocationsByAddressLine2IsEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where addressLine2 equals to DEFAULT_ADDRESS_LINE_2
        defaultLocationShouldBeFound("addressLine2.equals=" + DEFAULT_ADDRESS_LINE_2);

        // Get all the locationList where addressLine2 equals to UPDATED_ADDRESS_LINE_2
        defaultLocationShouldNotBeFound("addressLine2.equals=" + UPDATED_ADDRESS_LINE_2);
    }

    @Test
    @Transactional
    public void getAllLocationsByAddressLine2IsNotEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where addressLine2 not equals to DEFAULT_ADDRESS_LINE_2
        defaultLocationShouldNotBeFound("addressLine2.notEquals=" + DEFAULT_ADDRESS_LINE_2);

        // Get all the locationList where addressLine2 not equals to UPDATED_ADDRESS_LINE_2
        defaultLocationShouldBeFound("addressLine2.notEquals=" + UPDATED_ADDRESS_LINE_2);
    }

    @Test
    @Transactional
    public void getAllLocationsByAddressLine2IsInShouldWork() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where addressLine2 in DEFAULT_ADDRESS_LINE_2 or UPDATED_ADDRESS_LINE_2
        defaultLocationShouldBeFound("addressLine2.in=" + DEFAULT_ADDRESS_LINE_2 + "," + UPDATED_ADDRESS_LINE_2);

        // Get all the locationList where addressLine2 equals to UPDATED_ADDRESS_LINE_2
        defaultLocationShouldNotBeFound("addressLine2.in=" + UPDATED_ADDRESS_LINE_2);
    }

    @Test
    @Transactional
    public void getAllLocationsByAddressLine2IsNullOrNotNull() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where addressLine2 is not null
        defaultLocationShouldBeFound("addressLine2.specified=true");

        // Get all the locationList where addressLine2 is null
        defaultLocationShouldNotBeFound("addressLine2.specified=false");
    }
                @Test
    @Transactional
    public void getAllLocationsByAddressLine2ContainsSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where addressLine2 contains DEFAULT_ADDRESS_LINE_2
        defaultLocationShouldBeFound("addressLine2.contains=" + DEFAULT_ADDRESS_LINE_2);

        // Get all the locationList where addressLine2 contains UPDATED_ADDRESS_LINE_2
        defaultLocationShouldNotBeFound("addressLine2.contains=" + UPDATED_ADDRESS_LINE_2);
    }

    @Test
    @Transactional
    public void getAllLocationsByAddressLine2NotContainsSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where addressLine2 does not contain DEFAULT_ADDRESS_LINE_2
        defaultLocationShouldNotBeFound("addressLine2.doesNotContain=" + DEFAULT_ADDRESS_LINE_2);

        // Get all the locationList where addressLine2 does not contain UPDATED_ADDRESS_LINE_2
        defaultLocationShouldBeFound("addressLine2.doesNotContain=" + UPDATED_ADDRESS_LINE_2);
    }


    @Test
    @Transactional
    public void getAllLocationsByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where city equals to DEFAULT_CITY
        defaultLocationShouldBeFound("city.equals=" + DEFAULT_CITY);

        // Get all the locationList where city equals to UPDATED_CITY
        defaultLocationShouldNotBeFound("city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllLocationsByCityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where city not equals to DEFAULT_CITY
        defaultLocationShouldNotBeFound("city.notEquals=" + DEFAULT_CITY);

        // Get all the locationList where city not equals to UPDATED_CITY
        defaultLocationShouldBeFound("city.notEquals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllLocationsByCityIsInShouldWork() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where city in DEFAULT_CITY or UPDATED_CITY
        defaultLocationShouldBeFound("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY);

        // Get all the locationList where city equals to UPDATED_CITY
        defaultLocationShouldNotBeFound("city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllLocationsByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where city is not null
        defaultLocationShouldBeFound("city.specified=true");

        // Get all the locationList where city is null
        defaultLocationShouldNotBeFound("city.specified=false");
    }
                @Test
    @Transactional
    public void getAllLocationsByCityContainsSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where city contains DEFAULT_CITY
        defaultLocationShouldBeFound("city.contains=" + DEFAULT_CITY);

        // Get all the locationList where city contains UPDATED_CITY
        defaultLocationShouldNotBeFound("city.contains=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllLocationsByCityNotContainsSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where city does not contain DEFAULT_CITY
        defaultLocationShouldNotBeFound("city.doesNotContain=" + DEFAULT_CITY);

        // Get all the locationList where city does not contain UPDATED_CITY
        defaultLocationShouldBeFound("city.doesNotContain=" + UPDATED_CITY);
    }


    @Test
    @Transactional
    public void getAllLocationsByCountryIsEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where country equals to DEFAULT_COUNTRY
        defaultLocationShouldBeFound("country.equals=" + DEFAULT_COUNTRY);

        // Get all the locationList where country equals to UPDATED_COUNTRY
        defaultLocationShouldNotBeFound("country.equals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllLocationsByCountryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where country not equals to DEFAULT_COUNTRY
        defaultLocationShouldNotBeFound("country.notEquals=" + DEFAULT_COUNTRY);

        // Get all the locationList where country not equals to UPDATED_COUNTRY
        defaultLocationShouldBeFound("country.notEquals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllLocationsByCountryIsInShouldWork() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where country in DEFAULT_COUNTRY or UPDATED_COUNTRY
        defaultLocationShouldBeFound("country.in=" + DEFAULT_COUNTRY + "," + UPDATED_COUNTRY);

        // Get all the locationList where country equals to UPDATED_COUNTRY
        defaultLocationShouldNotBeFound("country.in=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllLocationsByCountryIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where country is not null
        defaultLocationShouldBeFound("country.specified=true");

        // Get all the locationList where country is null
        defaultLocationShouldNotBeFound("country.specified=false");
    }
                @Test
    @Transactional
    public void getAllLocationsByCountryContainsSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where country contains DEFAULT_COUNTRY
        defaultLocationShouldBeFound("country.contains=" + DEFAULT_COUNTRY);

        // Get all the locationList where country contains UPDATED_COUNTRY
        defaultLocationShouldNotBeFound("country.contains=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllLocationsByCountryNotContainsSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where country does not contain DEFAULT_COUNTRY
        defaultLocationShouldNotBeFound("country.doesNotContain=" + DEFAULT_COUNTRY);

        // Get all the locationList where country does not contain UPDATED_COUNTRY
        defaultLocationShouldBeFound("country.doesNotContain=" + UPDATED_COUNTRY);
    }


    @Test
    @Transactional
    public void getAllLocationsByVatNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where vatNumber equals to DEFAULT_VAT_NUMBER
        defaultLocationShouldBeFound("vatNumber.equals=" + DEFAULT_VAT_NUMBER);

        // Get all the locationList where vatNumber equals to UPDATED_VAT_NUMBER
        defaultLocationShouldNotBeFound("vatNumber.equals=" + UPDATED_VAT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllLocationsByVatNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where vatNumber not equals to DEFAULT_VAT_NUMBER
        defaultLocationShouldNotBeFound("vatNumber.notEquals=" + DEFAULT_VAT_NUMBER);

        // Get all the locationList where vatNumber not equals to UPDATED_VAT_NUMBER
        defaultLocationShouldBeFound("vatNumber.notEquals=" + UPDATED_VAT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllLocationsByVatNumberIsInShouldWork() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where vatNumber in DEFAULT_VAT_NUMBER or UPDATED_VAT_NUMBER
        defaultLocationShouldBeFound("vatNumber.in=" + DEFAULT_VAT_NUMBER + "," + UPDATED_VAT_NUMBER);

        // Get all the locationList where vatNumber equals to UPDATED_VAT_NUMBER
        defaultLocationShouldNotBeFound("vatNumber.in=" + UPDATED_VAT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllLocationsByVatNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where vatNumber is not null
        defaultLocationShouldBeFound("vatNumber.specified=true");

        // Get all the locationList where vatNumber is null
        defaultLocationShouldNotBeFound("vatNumber.specified=false");
    }
                @Test
    @Transactional
    public void getAllLocationsByVatNumberContainsSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where vatNumber contains DEFAULT_VAT_NUMBER
        defaultLocationShouldBeFound("vatNumber.contains=" + DEFAULT_VAT_NUMBER);

        // Get all the locationList where vatNumber contains UPDATED_VAT_NUMBER
        defaultLocationShouldNotBeFound("vatNumber.contains=" + UPDATED_VAT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllLocationsByVatNumberNotContainsSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where vatNumber does not contain DEFAULT_VAT_NUMBER
        defaultLocationShouldNotBeFound("vatNumber.doesNotContain=" + DEFAULT_VAT_NUMBER);

        // Get all the locationList where vatNumber does not contain UPDATED_VAT_NUMBER
        defaultLocationShouldBeFound("vatNumber.doesNotContain=" + UPDATED_VAT_NUMBER);
    }


    @Test
    @Transactional
    public void getAllLocationsByVatPercIsEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where vatPerc equals to DEFAULT_VAT_PERC
        defaultLocationShouldBeFound("vatPerc.equals=" + DEFAULT_VAT_PERC);

        // Get all the locationList where vatPerc equals to UPDATED_VAT_PERC
        defaultLocationShouldNotBeFound("vatPerc.equals=" + UPDATED_VAT_PERC);
    }

    @Test
    @Transactional
    public void getAllLocationsByVatPercIsNotEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where vatPerc not equals to DEFAULT_VAT_PERC
        defaultLocationShouldNotBeFound("vatPerc.notEquals=" + DEFAULT_VAT_PERC);

        // Get all the locationList where vatPerc not equals to UPDATED_VAT_PERC
        defaultLocationShouldBeFound("vatPerc.notEquals=" + UPDATED_VAT_PERC);
    }

    @Test
    @Transactional
    public void getAllLocationsByVatPercIsInShouldWork() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where vatPerc in DEFAULT_VAT_PERC or UPDATED_VAT_PERC
        defaultLocationShouldBeFound("vatPerc.in=" + DEFAULT_VAT_PERC + "," + UPDATED_VAT_PERC);

        // Get all the locationList where vatPerc equals to UPDATED_VAT_PERC
        defaultLocationShouldNotBeFound("vatPerc.in=" + UPDATED_VAT_PERC);
    }

    @Test
    @Transactional
    public void getAllLocationsByVatPercIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where vatPerc is not null
        defaultLocationShouldBeFound("vatPerc.specified=true");

        // Get all the locationList where vatPerc is null
        defaultLocationShouldNotBeFound("vatPerc.specified=false");
    }

    @Test
    @Transactional
    public void getAllLocationsByVatPercIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where vatPerc is greater than or equal to DEFAULT_VAT_PERC
        defaultLocationShouldBeFound("vatPerc.greaterThanOrEqual=" + DEFAULT_VAT_PERC);

        // Get all the locationList where vatPerc is greater than or equal to UPDATED_VAT_PERC
        defaultLocationShouldNotBeFound("vatPerc.greaterThanOrEqual=" + UPDATED_VAT_PERC);
    }

    @Test
    @Transactional
    public void getAllLocationsByVatPercIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where vatPerc is less than or equal to DEFAULT_VAT_PERC
        defaultLocationShouldBeFound("vatPerc.lessThanOrEqual=" + DEFAULT_VAT_PERC);

        // Get all the locationList where vatPerc is less than or equal to SMALLER_VAT_PERC
        defaultLocationShouldNotBeFound("vatPerc.lessThanOrEqual=" + SMALLER_VAT_PERC);
    }

    @Test
    @Transactional
    public void getAllLocationsByVatPercIsLessThanSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where vatPerc is less than DEFAULT_VAT_PERC
        defaultLocationShouldNotBeFound("vatPerc.lessThan=" + DEFAULT_VAT_PERC);

        // Get all the locationList where vatPerc is less than UPDATED_VAT_PERC
        defaultLocationShouldBeFound("vatPerc.lessThan=" + UPDATED_VAT_PERC);
    }

    @Test
    @Transactional
    public void getAllLocationsByVatPercIsGreaterThanSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where vatPerc is greater than DEFAULT_VAT_PERC
        defaultLocationShouldNotBeFound("vatPerc.greaterThan=" + DEFAULT_VAT_PERC);

        // Get all the locationList where vatPerc is greater than SMALLER_VAT_PERC
        defaultLocationShouldBeFound("vatPerc.greaterThan=" + SMALLER_VAT_PERC);
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
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING.doubleValue())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].addressLine1").value(hasItem(DEFAULT_ADDRESS_LINE_1)))
            .andExpect(jsonPath("$.[*].addressLine2").value(hasItem(DEFAULT_ADDRESS_LINE_2)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].vatNumber").value(hasItem(DEFAULT_VAT_NUMBER)))
            .andExpect(jsonPath("$.[*].vatPerc").value(hasItem(DEFAULT_VAT_PERC.doubleValue())));

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
            .isActive(UPDATED_IS_ACTIVE)
            .email(UPDATED_EMAIL)
            .rating(UPDATED_RATING)
            .phone(UPDATED_PHONE)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .vatNumber(UPDATED_VAT_NUMBER)
            .vatPerc(UPDATED_VAT_PERC);

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
        assertThat(testLocation.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testLocation.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testLocation.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testLocation.getAddressLine1()).isEqualTo(UPDATED_ADDRESS_LINE_1);
        assertThat(testLocation.getAddressLine2()).isEqualTo(UPDATED_ADDRESS_LINE_2);
        assertThat(testLocation.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testLocation.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testLocation.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testLocation.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testLocation.getVatNumber()).isEqualTo(UPDATED_VAT_NUMBER);
        assertThat(testLocation.getVatPerc()).isEqualTo(UPDATED_VAT_PERC);
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

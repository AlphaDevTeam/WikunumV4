package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.WikunumApp;
import com.alphadevs.sales.domain.ExUser;
import com.alphadevs.sales.domain.User;
import com.alphadevs.sales.domain.DocumentHistory;
import com.alphadevs.sales.domain.Company;
import com.alphadevs.sales.domain.Location;
import com.alphadevs.sales.domain.UserGroup;
import com.alphadevs.sales.domain.UserPermissions;
import com.alphadevs.sales.repository.ExUserRepository;
import com.alphadevs.sales.service.ExUserService;
import com.alphadevs.sales.web.rest.errors.ExceptionTranslator;
import com.alphadevs.sales.service.dto.ExUserCriteria;
import com.alphadevs.sales.service.ExUserQueryService;

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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.alphadevs.sales.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ExUserResource} REST controller.
 */
@SpringBootTest(classes = WikunumApp.class)
public class ExUserResourceIT {

    private static final String DEFAULT_USER_KEY = "AAAAAAAAAA";
    private static final String UPDATED_USER_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_LOGIN = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "#1?<.@|7C.HfpWS";
    private static final String UPDATED_EMAIL = "eP8~@Qz\\24.73t";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

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

    private static final BigDecimal DEFAULT_USER_LIMIT = new BigDecimal(1);
    private static final BigDecimal UPDATED_USER_LIMIT = new BigDecimal(2);
    private static final BigDecimal SMALLER_USER_LIMIT = new BigDecimal(1 - 1);

    private static final Double DEFAULT_CREDIT_SCORE = 1D;
    private static final Double UPDATED_CREDIT_SCORE = 2D;
    private static final Double SMALLER_CREDIT_SCORE = 1D - 1D;

    @Autowired
    private ExUserRepository exUserRepository;

    @Mock
    private ExUserRepository exUserRepositoryMock;

    @Mock
    private ExUserService exUserServiceMock;

    @Autowired
    private ExUserService exUserService;

    @Autowired
    private ExUserQueryService exUserQueryService;

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

    private MockMvc restExUserMockMvc;

    private ExUser exUser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ExUserResource exUserResource = new ExUserResource(exUserService, exUserQueryService);
        this.restExUserMockMvc = MockMvcBuilders.standaloneSetup(exUserResource)
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
    public static ExUser createEntity(EntityManager em) {
        ExUser exUser = new ExUser()
            .userKey(DEFAULT_USER_KEY)
            .login(DEFAULT_LOGIN)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL)
            .isActive(DEFAULT_IS_ACTIVE)
            .phone(DEFAULT_PHONE)
            .addressLine1(DEFAULT_ADDRESS_LINE_1)
            .addressLine2(DEFAULT_ADDRESS_LINE_2)
            .city(DEFAULT_CITY)
            .country(DEFAULT_COUNTRY)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .userLimit(DEFAULT_USER_LIMIT)
            .creditScore(DEFAULT_CREDIT_SCORE);
        // Add required entity
        Company company;
        if (TestUtil.findAll(em, Company.class).isEmpty()) {
            company = CompanyResourceIT.createEntity(em);
            em.persist(company);
            em.flush();
        } else {
            company = TestUtil.findAll(em, Company.class).get(0);
        }
        exUser.setCompany(company);
        // Add required entity
        Location location;
        if (TestUtil.findAll(em, Location.class).isEmpty()) {
            location = LocationResourceIT.createEntity(em);
            em.persist(location);
            em.flush();
        } else {
            location = TestUtil.findAll(em, Location.class).get(0);
        }
        exUser.getLocations().add(location);
        return exUser;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExUser createUpdatedEntity(EntityManager em) {
        ExUser exUser = new ExUser()
            .userKey(UPDATED_USER_KEY)
            .login(UPDATED_LOGIN)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .isActive(UPDATED_IS_ACTIVE)
            .phone(UPDATED_PHONE)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .userLimit(UPDATED_USER_LIMIT)
            .creditScore(UPDATED_CREDIT_SCORE);
        // Add required entity
        Company company;
        if (TestUtil.findAll(em, Company.class).isEmpty()) {
            company = CompanyResourceIT.createUpdatedEntity(em);
            em.persist(company);
            em.flush();
        } else {
            company = TestUtil.findAll(em, Company.class).get(0);
        }
        exUser.setCompany(company);
        // Add required entity
        Location location;
        if (TestUtil.findAll(em, Location.class).isEmpty()) {
            location = LocationResourceIT.createUpdatedEntity(em);
            em.persist(location);
            em.flush();
        } else {
            location = TestUtil.findAll(em, Location.class).get(0);
        }
        exUser.getLocations().add(location);
        return exUser;
    }

    @BeforeEach
    public void initTest() {
        exUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createExUser() throws Exception {
        int databaseSizeBeforeCreate = exUserRepository.findAll().size();

        // Create the ExUser
        restExUserMockMvc.perform(post("/api/ex-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(exUser)))
            .andExpect(status().isCreated());

        // Validate the ExUser in the database
        List<ExUser> exUserList = exUserRepository.findAll();
        assertThat(exUserList).hasSize(databaseSizeBeforeCreate + 1);
        ExUser testExUser = exUserList.get(exUserList.size() - 1);
        assertThat(testExUser.getUserKey()).isEqualTo(DEFAULT_USER_KEY);
        assertThat(testExUser.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(testExUser.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testExUser.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testExUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testExUser.isIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testExUser.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testExUser.getAddressLine1()).isEqualTo(DEFAULT_ADDRESS_LINE_1);
        assertThat(testExUser.getAddressLine2()).isEqualTo(DEFAULT_ADDRESS_LINE_2);
        assertThat(testExUser.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testExUser.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testExUser.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testExUser.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testExUser.getUserLimit()).isEqualTo(DEFAULT_USER_LIMIT);
        assertThat(testExUser.getCreditScore()).isEqualTo(DEFAULT_CREDIT_SCORE);
    }

    @Test
    @Transactional
    public void createExUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = exUserRepository.findAll().size();

        // Create the ExUser with an existing ID
        exUser.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restExUserMockMvc.perform(post("/api/ex-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(exUser)))
            .andExpect(status().isBadRequest());

        // Validate the ExUser in the database
        List<ExUser> exUserList = exUserRepository.findAll();
        assertThat(exUserList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkUserKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = exUserRepository.findAll().size();
        // set the field null
        exUser.setUserKey(null);

        // Create the ExUser, which fails.

        restExUserMockMvc.perform(post("/api/ex-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(exUser)))
            .andExpect(status().isBadRequest());

        List<ExUser> exUserList = exUserRepository.findAll();
        assertThat(exUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLoginIsRequired() throws Exception {
        int databaseSizeBeforeTest = exUserRepository.findAll().size();
        // set the field null
        exUser.setLogin(null);

        // Create the ExUser, which fails.

        restExUserMockMvc.perform(post("/api/ex-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(exUser)))
            .andExpect(status().isBadRequest());

        List<ExUser> exUserList = exUserRepository.findAll();
        assertThat(exUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = exUserRepository.findAll().size();
        // set the field null
        exUser.setFirstName(null);

        // Create the ExUser, which fails.

        restExUserMockMvc.perform(post("/api/ex-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(exUser)))
            .andExpect(status().isBadRequest());

        List<ExUser> exUserList = exUserRepository.findAll();
        assertThat(exUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = exUserRepository.findAll().size();
        // set the field null
        exUser.setEmail(null);

        // Create the ExUser, which fails.

        restExUserMockMvc.perform(post("/api/ex-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(exUser)))
            .andExpect(status().isBadRequest());

        List<ExUser> exUserList = exUserRepository.findAll();
        assertThat(exUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllExUsers() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList
        restExUserMockMvc.perform(get("/api/ex-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].userKey").value(hasItem(DEFAULT_USER_KEY)))
            .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].addressLine1").value(hasItem(DEFAULT_ADDRESS_LINE_1)))
            .andExpect(jsonPath("$.[*].addressLine2").value(hasItem(DEFAULT_ADDRESS_LINE_2)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].userLimit").value(hasItem(DEFAULT_USER_LIMIT.intValue())))
            .andExpect(jsonPath("$.[*].creditScore").value(hasItem(DEFAULT_CREDIT_SCORE.doubleValue())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllExUsersWithEagerRelationshipsIsEnabled() throws Exception {
        ExUserResource exUserResource = new ExUserResource(exUserServiceMock, exUserQueryService);
        when(exUserServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restExUserMockMvc = MockMvcBuilders.standaloneSetup(exUserResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restExUserMockMvc.perform(get("/api/ex-users?eagerload=true"))
        .andExpect(status().isOk());

        verify(exUserServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllExUsersWithEagerRelationshipsIsNotEnabled() throws Exception {
        ExUserResource exUserResource = new ExUserResource(exUserServiceMock, exUserQueryService);
            when(exUserServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restExUserMockMvc = MockMvcBuilders.standaloneSetup(exUserResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restExUserMockMvc.perform(get("/api/ex-users?eagerload=true"))
        .andExpect(status().isOk());

            verify(exUserServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getExUser() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get the exUser
        restExUserMockMvc.perform(get("/api/ex-users/{id}", exUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(exUser.getId().intValue()))
            .andExpect(jsonPath("$.userKey").value(DEFAULT_USER_KEY))
            .andExpect(jsonPath("$.login").value(DEFAULT_LOGIN))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.addressLine1").value(DEFAULT_ADDRESS_LINE_1))
            .andExpect(jsonPath("$.addressLine2").value(DEFAULT_ADDRESS_LINE_2))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.userLimit").value(DEFAULT_USER_LIMIT.intValue()))
            .andExpect(jsonPath("$.creditScore").value(DEFAULT_CREDIT_SCORE.doubleValue()));
    }


    @Test
    @Transactional
    public void getExUsersByIdFiltering() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        Long id = exUser.getId();

        defaultExUserShouldBeFound("id.equals=" + id);
        defaultExUserShouldNotBeFound("id.notEquals=" + id);

        defaultExUserShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultExUserShouldNotBeFound("id.greaterThan=" + id);

        defaultExUserShouldBeFound("id.lessThanOrEqual=" + id);
        defaultExUserShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllExUsersByUserKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where userKey equals to DEFAULT_USER_KEY
        defaultExUserShouldBeFound("userKey.equals=" + DEFAULT_USER_KEY);

        // Get all the exUserList where userKey equals to UPDATED_USER_KEY
        defaultExUserShouldNotBeFound("userKey.equals=" + UPDATED_USER_KEY);
    }

    @Test
    @Transactional
    public void getAllExUsersByUserKeyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where userKey not equals to DEFAULT_USER_KEY
        defaultExUserShouldNotBeFound("userKey.notEquals=" + DEFAULT_USER_KEY);

        // Get all the exUserList where userKey not equals to UPDATED_USER_KEY
        defaultExUserShouldBeFound("userKey.notEquals=" + UPDATED_USER_KEY);
    }

    @Test
    @Transactional
    public void getAllExUsersByUserKeyIsInShouldWork() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where userKey in DEFAULT_USER_KEY or UPDATED_USER_KEY
        defaultExUserShouldBeFound("userKey.in=" + DEFAULT_USER_KEY + "," + UPDATED_USER_KEY);

        // Get all the exUserList where userKey equals to UPDATED_USER_KEY
        defaultExUserShouldNotBeFound("userKey.in=" + UPDATED_USER_KEY);
    }

    @Test
    @Transactional
    public void getAllExUsersByUserKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where userKey is not null
        defaultExUserShouldBeFound("userKey.specified=true");

        // Get all the exUserList where userKey is null
        defaultExUserShouldNotBeFound("userKey.specified=false");
    }
                @Test
    @Transactional
    public void getAllExUsersByUserKeyContainsSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where userKey contains DEFAULT_USER_KEY
        defaultExUserShouldBeFound("userKey.contains=" + DEFAULT_USER_KEY);

        // Get all the exUserList where userKey contains UPDATED_USER_KEY
        defaultExUserShouldNotBeFound("userKey.contains=" + UPDATED_USER_KEY);
    }

    @Test
    @Transactional
    public void getAllExUsersByUserKeyNotContainsSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where userKey does not contain DEFAULT_USER_KEY
        defaultExUserShouldNotBeFound("userKey.doesNotContain=" + DEFAULT_USER_KEY);

        // Get all the exUserList where userKey does not contain UPDATED_USER_KEY
        defaultExUserShouldBeFound("userKey.doesNotContain=" + UPDATED_USER_KEY);
    }


    @Test
    @Transactional
    public void getAllExUsersByLoginIsEqualToSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where login equals to DEFAULT_LOGIN
        defaultExUserShouldBeFound("login.equals=" + DEFAULT_LOGIN);

        // Get all the exUserList where login equals to UPDATED_LOGIN
        defaultExUserShouldNotBeFound("login.equals=" + UPDATED_LOGIN);
    }

    @Test
    @Transactional
    public void getAllExUsersByLoginIsNotEqualToSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where login not equals to DEFAULT_LOGIN
        defaultExUserShouldNotBeFound("login.notEquals=" + DEFAULT_LOGIN);

        // Get all the exUserList where login not equals to UPDATED_LOGIN
        defaultExUserShouldBeFound("login.notEquals=" + UPDATED_LOGIN);
    }

    @Test
    @Transactional
    public void getAllExUsersByLoginIsInShouldWork() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where login in DEFAULT_LOGIN or UPDATED_LOGIN
        defaultExUserShouldBeFound("login.in=" + DEFAULT_LOGIN + "," + UPDATED_LOGIN);

        // Get all the exUserList where login equals to UPDATED_LOGIN
        defaultExUserShouldNotBeFound("login.in=" + UPDATED_LOGIN);
    }

    @Test
    @Transactional
    public void getAllExUsersByLoginIsNullOrNotNull() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where login is not null
        defaultExUserShouldBeFound("login.specified=true");

        // Get all the exUserList where login is null
        defaultExUserShouldNotBeFound("login.specified=false");
    }
                @Test
    @Transactional
    public void getAllExUsersByLoginContainsSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where login contains DEFAULT_LOGIN
        defaultExUserShouldBeFound("login.contains=" + DEFAULT_LOGIN);

        // Get all the exUserList where login contains UPDATED_LOGIN
        defaultExUserShouldNotBeFound("login.contains=" + UPDATED_LOGIN);
    }

    @Test
    @Transactional
    public void getAllExUsersByLoginNotContainsSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where login does not contain DEFAULT_LOGIN
        defaultExUserShouldNotBeFound("login.doesNotContain=" + DEFAULT_LOGIN);

        // Get all the exUserList where login does not contain UPDATED_LOGIN
        defaultExUserShouldBeFound("login.doesNotContain=" + UPDATED_LOGIN);
    }


    @Test
    @Transactional
    public void getAllExUsersByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where firstName equals to DEFAULT_FIRST_NAME
        defaultExUserShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the exUserList where firstName equals to UPDATED_FIRST_NAME
        defaultExUserShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllExUsersByFirstNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where firstName not equals to DEFAULT_FIRST_NAME
        defaultExUserShouldNotBeFound("firstName.notEquals=" + DEFAULT_FIRST_NAME);

        // Get all the exUserList where firstName not equals to UPDATED_FIRST_NAME
        defaultExUserShouldBeFound("firstName.notEquals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllExUsersByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultExUserShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the exUserList where firstName equals to UPDATED_FIRST_NAME
        defaultExUserShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllExUsersByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where firstName is not null
        defaultExUserShouldBeFound("firstName.specified=true");

        // Get all the exUserList where firstName is null
        defaultExUserShouldNotBeFound("firstName.specified=false");
    }
                @Test
    @Transactional
    public void getAllExUsersByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where firstName contains DEFAULT_FIRST_NAME
        defaultExUserShouldBeFound("firstName.contains=" + DEFAULT_FIRST_NAME);

        // Get all the exUserList where firstName contains UPDATED_FIRST_NAME
        defaultExUserShouldNotBeFound("firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllExUsersByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where firstName does not contain DEFAULT_FIRST_NAME
        defaultExUserShouldNotBeFound("firstName.doesNotContain=" + DEFAULT_FIRST_NAME);

        // Get all the exUserList where firstName does not contain UPDATED_FIRST_NAME
        defaultExUserShouldBeFound("firstName.doesNotContain=" + UPDATED_FIRST_NAME);
    }


    @Test
    @Transactional
    public void getAllExUsersByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where lastName equals to DEFAULT_LAST_NAME
        defaultExUserShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the exUserList where lastName equals to UPDATED_LAST_NAME
        defaultExUserShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllExUsersByLastNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where lastName not equals to DEFAULT_LAST_NAME
        defaultExUserShouldNotBeFound("lastName.notEquals=" + DEFAULT_LAST_NAME);

        // Get all the exUserList where lastName not equals to UPDATED_LAST_NAME
        defaultExUserShouldBeFound("lastName.notEquals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllExUsersByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultExUserShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the exUserList where lastName equals to UPDATED_LAST_NAME
        defaultExUserShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllExUsersByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where lastName is not null
        defaultExUserShouldBeFound("lastName.specified=true");

        // Get all the exUserList where lastName is null
        defaultExUserShouldNotBeFound("lastName.specified=false");
    }
                @Test
    @Transactional
    public void getAllExUsersByLastNameContainsSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where lastName contains DEFAULT_LAST_NAME
        defaultExUserShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the exUserList where lastName contains UPDATED_LAST_NAME
        defaultExUserShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllExUsersByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where lastName does not contain DEFAULT_LAST_NAME
        defaultExUserShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the exUserList where lastName does not contain UPDATED_LAST_NAME
        defaultExUserShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }


    @Test
    @Transactional
    public void getAllExUsersByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where email equals to DEFAULT_EMAIL
        defaultExUserShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the exUserList where email equals to UPDATED_EMAIL
        defaultExUserShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllExUsersByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where email not equals to DEFAULT_EMAIL
        defaultExUserShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the exUserList where email not equals to UPDATED_EMAIL
        defaultExUserShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllExUsersByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultExUserShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the exUserList where email equals to UPDATED_EMAIL
        defaultExUserShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllExUsersByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where email is not null
        defaultExUserShouldBeFound("email.specified=true");

        // Get all the exUserList where email is null
        defaultExUserShouldNotBeFound("email.specified=false");
    }
                @Test
    @Transactional
    public void getAllExUsersByEmailContainsSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where email contains DEFAULT_EMAIL
        defaultExUserShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the exUserList where email contains UPDATED_EMAIL
        defaultExUserShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllExUsersByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where email does not contain DEFAULT_EMAIL
        defaultExUserShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the exUserList where email does not contain UPDATED_EMAIL
        defaultExUserShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }


    @Test
    @Transactional
    public void getAllExUsersByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where isActive equals to DEFAULT_IS_ACTIVE
        defaultExUserShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the exUserList where isActive equals to UPDATED_IS_ACTIVE
        defaultExUserShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllExUsersByIsActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where isActive not equals to DEFAULT_IS_ACTIVE
        defaultExUserShouldNotBeFound("isActive.notEquals=" + DEFAULT_IS_ACTIVE);

        // Get all the exUserList where isActive not equals to UPDATED_IS_ACTIVE
        defaultExUserShouldBeFound("isActive.notEquals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllExUsersByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultExUserShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the exUserList where isActive equals to UPDATED_IS_ACTIVE
        defaultExUserShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllExUsersByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where isActive is not null
        defaultExUserShouldBeFound("isActive.specified=true");

        // Get all the exUserList where isActive is null
        defaultExUserShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    public void getAllExUsersByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where phone equals to DEFAULT_PHONE
        defaultExUserShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the exUserList where phone equals to UPDATED_PHONE
        defaultExUserShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllExUsersByPhoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where phone not equals to DEFAULT_PHONE
        defaultExUserShouldNotBeFound("phone.notEquals=" + DEFAULT_PHONE);

        // Get all the exUserList where phone not equals to UPDATED_PHONE
        defaultExUserShouldBeFound("phone.notEquals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllExUsersByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultExUserShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the exUserList where phone equals to UPDATED_PHONE
        defaultExUserShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllExUsersByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where phone is not null
        defaultExUserShouldBeFound("phone.specified=true");

        // Get all the exUserList where phone is null
        defaultExUserShouldNotBeFound("phone.specified=false");
    }
                @Test
    @Transactional
    public void getAllExUsersByPhoneContainsSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where phone contains DEFAULT_PHONE
        defaultExUserShouldBeFound("phone.contains=" + DEFAULT_PHONE);

        // Get all the exUserList where phone contains UPDATED_PHONE
        defaultExUserShouldNotBeFound("phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllExUsersByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where phone does not contain DEFAULT_PHONE
        defaultExUserShouldNotBeFound("phone.doesNotContain=" + DEFAULT_PHONE);

        // Get all the exUserList where phone does not contain UPDATED_PHONE
        defaultExUserShouldBeFound("phone.doesNotContain=" + UPDATED_PHONE);
    }


    @Test
    @Transactional
    public void getAllExUsersByAddressLine1IsEqualToSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where addressLine1 equals to DEFAULT_ADDRESS_LINE_1
        defaultExUserShouldBeFound("addressLine1.equals=" + DEFAULT_ADDRESS_LINE_1);

        // Get all the exUserList where addressLine1 equals to UPDATED_ADDRESS_LINE_1
        defaultExUserShouldNotBeFound("addressLine1.equals=" + UPDATED_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    public void getAllExUsersByAddressLine1IsNotEqualToSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where addressLine1 not equals to DEFAULT_ADDRESS_LINE_1
        defaultExUserShouldNotBeFound("addressLine1.notEquals=" + DEFAULT_ADDRESS_LINE_1);

        // Get all the exUserList where addressLine1 not equals to UPDATED_ADDRESS_LINE_1
        defaultExUserShouldBeFound("addressLine1.notEquals=" + UPDATED_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    public void getAllExUsersByAddressLine1IsInShouldWork() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where addressLine1 in DEFAULT_ADDRESS_LINE_1 or UPDATED_ADDRESS_LINE_1
        defaultExUserShouldBeFound("addressLine1.in=" + DEFAULT_ADDRESS_LINE_1 + "," + UPDATED_ADDRESS_LINE_1);

        // Get all the exUserList where addressLine1 equals to UPDATED_ADDRESS_LINE_1
        defaultExUserShouldNotBeFound("addressLine1.in=" + UPDATED_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    public void getAllExUsersByAddressLine1IsNullOrNotNull() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where addressLine1 is not null
        defaultExUserShouldBeFound("addressLine1.specified=true");

        // Get all the exUserList where addressLine1 is null
        defaultExUserShouldNotBeFound("addressLine1.specified=false");
    }
                @Test
    @Transactional
    public void getAllExUsersByAddressLine1ContainsSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where addressLine1 contains DEFAULT_ADDRESS_LINE_1
        defaultExUserShouldBeFound("addressLine1.contains=" + DEFAULT_ADDRESS_LINE_1);

        // Get all the exUserList where addressLine1 contains UPDATED_ADDRESS_LINE_1
        defaultExUserShouldNotBeFound("addressLine1.contains=" + UPDATED_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    public void getAllExUsersByAddressLine1NotContainsSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where addressLine1 does not contain DEFAULT_ADDRESS_LINE_1
        defaultExUserShouldNotBeFound("addressLine1.doesNotContain=" + DEFAULT_ADDRESS_LINE_1);

        // Get all the exUserList where addressLine1 does not contain UPDATED_ADDRESS_LINE_1
        defaultExUserShouldBeFound("addressLine1.doesNotContain=" + UPDATED_ADDRESS_LINE_1);
    }


    @Test
    @Transactional
    public void getAllExUsersByAddressLine2IsEqualToSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where addressLine2 equals to DEFAULT_ADDRESS_LINE_2
        defaultExUserShouldBeFound("addressLine2.equals=" + DEFAULT_ADDRESS_LINE_2);

        // Get all the exUserList where addressLine2 equals to UPDATED_ADDRESS_LINE_2
        defaultExUserShouldNotBeFound("addressLine2.equals=" + UPDATED_ADDRESS_LINE_2);
    }

    @Test
    @Transactional
    public void getAllExUsersByAddressLine2IsNotEqualToSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where addressLine2 not equals to DEFAULT_ADDRESS_LINE_2
        defaultExUserShouldNotBeFound("addressLine2.notEquals=" + DEFAULT_ADDRESS_LINE_2);

        // Get all the exUserList where addressLine2 not equals to UPDATED_ADDRESS_LINE_2
        defaultExUserShouldBeFound("addressLine2.notEquals=" + UPDATED_ADDRESS_LINE_2);
    }

    @Test
    @Transactional
    public void getAllExUsersByAddressLine2IsInShouldWork() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where addressLine2 in DEFAULT_ADDRESS_LINE_2 or UPDATED_ADDRESS_LINE_2
        defaultExUserShouldBeFound("addressLine2.in=" + DEFAULT_ADDRESS_LINE_2 + "," + UPDATED_ADDRESS_LINE_2);

        // Get all the exUserList where addressLine2 equals to UPDATED_ADDRESS_LINE_2
        defaultExUserShouldNotBeFound("addressLine2.in=" + UPDATED_ADDRESS_LINE_2);
    }

    @Test
    @Transactional
    public void getAllExUsersByAddressLine2IsNullOrNotNull() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where addressLine2 is not null
        defaultExUserShouldBeFound("addressLine2.specified=true");

        // Get all the exUserList where addressLine2 is null
        defaultExUserShouldNotBeFound("addressLine2.specified=false");
    }
                @Test
    @Transactional
    public void getAllExUsersByAddressLine2ContainsSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where addressLine2 contains DEFAULT_ADDRESS_LINE_2
        defaultExUserShouldBeFound("addressLine2.contains=" + DEFAULT_ADDRESS_LINE_2);

        // Get all the exUserList where addressLine2 contains UPDATED_ADDRESS_LINE_2
        defaultExUserShouldNotBeFound("addressLine2.contains=" + UPDATED_ADDRESS_LINE_2);
    }

    @Test
    @Transactional
    public void getAllExUsersByAddressLine2NotContainsSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where addressLine2 does not contain DEFAULT_ADDRESS_LINE_2
        defaultExUserShouldNotBeFound("addressLine2.doesNotContain=" + DEFAULT_ADDRESS_LINE_2);

        // Get all the exUserList where addressLine2 does not contain UPDATED_ADDRESS_LINE_2
        defaultExUserShouldBeFound("addressLine2.doesNotContain=" + UPDATED_ADDRESS_LINE_2);
    }


    @Test
    @Transactional
    public void getAllExUsersByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where city equals to DEFAULT_CITY
        defaultExUserShouldBeFound("city.equals=" + DEFAULT_CITY);

        // Get all the exUserList where city equals to UPDATED_CITY
        defaultExUserShouldNotBeFound("city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllExUsersByCityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where city not equals to DEFAULT_CITY
        defaultExUserShouldNotBeFound("city.notEquals=" + DEFAULT_CITY);

        // Get all the exUserList where city not equals to UPDATED_CITY
        defaultExUserShouldBeFound("city.notEquals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllExUsersByCityIsInShouldWork() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where city in DEFAULT_CITY or UPDATED_CITY
        defaultExUserShouldBeFound("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY);

        // Get all the exUserList where city equals to UPDATED_CITY
        defaultExUserShouldNotBeFound("city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllExUsersByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where city is not null
        defaultExUserShouldBeFound("city.specified=true");

        // Get all the exUserList where city is null
        defaultExUserShouldNotBeFound("city.specified=false");
    }
                @Test
    @Transactional
    public void getAllExUsersByCityContainsSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where city contains DEFAULT_CITY
        defaultExUserShouldBeFound("city.contains=" + DEFAULT_CITY);

        // Get all the exUserList where city contains UPDATED_CITY
        defaultExUserShouldNotBeFound("city.contains=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllExUsersByCityNotContainsSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where city does not contain DEFAULT_CITY
        defaultExUserShouldNotBeFound("city.doesNotContain=" + DEFAULT_CITY);

        // Get all the exUserList where city does not contain UPDATED_CITY
        defaultExUserShouldBeFound("city.doesNotContain=" + UPDATED_CITY);
    }


    @Test
    @Transactional
    public void getAllExUsersByCountryIsEqualToSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where country equals to DEFAULT_COUNTRY
        defaultExUserShouldBeFound("country.equals=" + DEFAULT_COUNTRY);

        // Get all the exUserList where country equals to UPDATED_COUNTRY
        defaultExUserShouldNotBeFound("country.equals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllExUsersByCountryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where country not equals to DEFAULT_COUNTRY
        defaultExUserShouldNotBeFound("country.notEquals=" + DEFAULT_COUNTRY);

        // Get all the exUserList where country not equals to UPDATED_COUNTRY
        defaultExUserShouldBeFound("country.notEquals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllExUsersByCountryIsInShouldWork() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where country in DEFAULT_COUNTRY or UPDATED_COUNTRY
        defaultExUserShouldBeFound("country.in=" + DEFAULT_COUNTRY + "," + UPDATED_COUNTRY);

        // Get all the exUserList where country equals to UPDATED_COUNTRY
        defaultExUserShouldNotBeFound("country.in=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllExUsersByCountryIsNullOrNotNull() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where country is not null
        defaultExUserShouldBeFound("country.specified=true");

        // Get all the exUserList where country is null
        defaultExUserShouldNotBeFound("country.specified=false");
    }
                @Test
    @Transactional
    public void getAllExUsersByCountryContainsSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where country contains DEFAULT_COUNTRY
        defaultExUserShouldBeFound("country.contains=" + DEFAULT_COUNTRY);

        // Get all the exUserList where country contains UPDATED_COUNTRY
        defaultExUserShouldNotBeFound("country.contains=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllExUsersByCountryNotContainsSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where country does not contain DEFAULT_COUNTRY
        defaultExUserShouldNotBeFound("country.doesNotContain=" + DEFAULT_COUNTRY);

        // Get all the exUserList where country does not contain UPDATED_COUNTRY
        defaultExUserShouldBeFound("country.doesNotContain=" + UPDATED_COUNTRY);
    }


    @Test
    @Transactional
    public void getAllExUsersByUserLimitIsEqualToSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where userLimit equals to DEFAULT_USER_LIMIT
        defaultExUserShouldBeFound("userLimit.equals=" + DEFAULT_USER_LIMIT);

        // Get all the exUserList where userLimit equals to UPDATED_USER_LIMIT
        defaultExUserShouldNotBeFound("userLimit.equals=" + UPDATED_USER_LIMIT);
    }

    @Test
    @Transactional
    public void getAllExUsersByUserLimitIsNotEqualToSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where userLimit not equals to DEFAULT_USER_LIMIT
        defaultExUserShouldNotBeFound("userLimit.notEquals=" + DEFAULT_USER_LIMIT);

        // Get all the exUserList where userLimit not equals to UPDATED_USER_LIMIT
        defaultExUserShouldBeFound("userLimit.notEquals=" + UPDATED_USER_LIMIT);
    }

    @Test
    @Transactional
    public void getAllExUsersByUserLimitIsInShouldWork() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where userLimit in DEFAULT_USER_LIMIT or UPDATED_USER_LIMIT
        defaultExUserShouldBeFound("userLimit.in=" + DEFAULT_USER_LIMIT + "," + UPDATED_USER_LIMIT);

        // Get all the exUserList where userLimit equals to UPDATED_USER_LIMIT
        defaultExUserShouldNotBeFound("userLimit.in=" + UPDATED_USER_LIMIT);
    }

    @Test
    @Transactional
    public void getAllExUsersByUserLimitIsNullOrNotNull() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where userLimit is not null
        defaultExUserShouldBeFound("userLimit.specified=true");

        // Get all the exUserList where userLimit is null
        defaultExUserShouldNotBeFound("userLimit.specified=false");
    }

    @Test
    @Transactional
    public void getAllExUsersByUserLimitIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where userLimit is greater than or equal to DEFAULT_USER_LIMIT
        defaultExUserShouldBeFound("userLimit.greaterThanOrEqual=" + DEFAULT_USER_LIMIT);

        // Get all the exUserList where userLimit is greater than or equal to UPDATED_USER_LIMIT
        defaultExUserShouldNotBeFound("userLimit.greaterThanOrEqual=" + UPDATED_USER_LIMIT);
    }

    @Test
    @Transactional
    public void getAllExUsersByUserLimitIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where userLimit is less than or equal to DEFAULT_USER_LIMIT
        defaultExUserShouldBeFound("userLimit.lessThanOrEqual=" + DEFAULT_USER_LIMIT);

        // Get all the exUserList where userLimit is less than or equal to SMALLER_USER_LIMIT
        defaultExUserShouldNotBeFound("userLimit.lessThanOrEqual=" + SMALLER_USER_LIMIT);
    }

    @Test
    @Transactional
    public void getAllExUsersByUserLimitIsLessThanSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where userLimit is less than DEFAULT_USER_LIMIT
        defaultExUserShouldNotBeFound("userLimit.lessThan=" + DEFAULT_USER_LIMIT);

        // Get all the exUserList where userLimit is less than UPDATED_USER_LIMIT
        defaultExUserShouldBeFound("userLimit.lessThan=" + UPDATED_USER_LIMIT);
    }

    @Test
    @Transactional
    public void getAllExUsersByUserLimitIsGreaterThanSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where userLimit is greater than DEFAULT_USER_LIMIT
        defaultExUserShouldNotBeFound("userLimit.greaterThan=" + DEFAULT_USER_LIMIT);

        // Get all the exUserList where userLimit is greater than SMALLER_USER_LIMIT
        defaultExUserShouldBeFound("userLimit.greaterThan=" + SMALLER_USER_LIMIT);
    }


    @Test
    @Transactional
    public void getAllExUsersByCreditScoreIsEqualToSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where creditScore equals to DEFAULT_CREDIT_SCORE
        defaultExUserShouldBeFound("creditScore.equals=" + DEFAULT_CREDIT_SCORE);

        // Get all the exUserList where creditScore equals to UPDATED_CREDIT_SCORE
        defaultExUserShouldNotBeFound("creditScore.equals=" + UPDATED_CREDIT_SCORE);
    }

    @Test
    @Transactional
    public void getAllExUsersByCreditScoreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where creditScore not equals to DEFAULT_CREDIT_SCORE
        defaultExUserShouldNotBeFound("creditScore.notEquals=" + DEFAULT_CREDIT_SCORE);

        // Get all the exUserList where creditScore not equals to UPDATED_CREDIT_SCORE
        defaultExUserShouldBeFound("creditScore.notEquals=" + UPDATED_CREDIT_SCORE);
    }

    @Test
    @Transactional
    public void getAllExUsersByCreditScoreIsInShouldWork() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where creditScore in DEFAULT_CREDIT_SCORE or UPDATED_CREDIT_SCORE
        defaultExUserShouldBeFound("creditScore.in=" + DEFAULT_CREDIT_SCORE + "," + UPDATED_CREDIT_SCORE);

        // Get all the exUserList where creditScore equals to UPDATED_CREDIT_SCORE
        defaultExUserShouldNotBeFound("creditScore.in=" + UPDATED_CREDIT_SCORE);
    }

    @Test
    @Transactional
    public void getAllExUsersByCreditScoreIsNullOrNotNull() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where creditScore is not null
        defaultExUserShouldBeFound("creditScore.specified=true");

        // Get all the exUserList where creditScore is null
        defaultExUserShouldNotBeFound("creditScore.specified=false");
    }

    @Test
    @Transactional
    public void getAllExUsersByCreditScoreIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where creditScore is greater than or equal to DEFAULT_CREDIT_SCORE
        defaultExUserShouldBeFound("creditScore.greaterThanOrEqual=" + DEFAULT_CREDIT_SCORE);

        // Get all the exUserList where creditScore is greater than or equal to UPDATED_CREDIT_SCORE
        defaultExUserShouldNotBeFound("creditScore.greaterThanOrEqual=" + UPDATED_CREDIT_SCORE);
    }

    @Test
    @Transactional
    public void getAllExUsersByCreditScoreIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where creditScore is less than or equal to DEFAULT_CREDIT_SCORE
        defaultExUserShouldBeFound("creditScore.lessThanOrEqual=" + DEFAULT_CREDIT_SCORE);

        // Get all the exUserList where creditScore is less than or equal to SMALLER_CREDIT_SCORE
        defaultExUserShouldNotBeFound("creditScore.lessThanOrEqual=" + SMALLER_CREDIT_SCORE);
    }

    @Test
    @Transactional
    public void getAllExUsersByCreditScoreIsLessThanSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where creditScore is less than DEFAULT_CREDIT_SCORE
        defaultExUserShouldNotBeFound("creditScore.lessThan=" + DEFAULT_CREDIT_SCORE);

        // Get all the exUserList where creditScore is less than UPDATED_CREDIT_SCORE
        defaultExUserShouldBeFound("creditScore.lessThan=" + UPDATED_CREDIT_SCORE);
    }

    @Test
    @Transactional
    public void getAllExUsersByCreditScoreIsGreaterThanSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where creditScore is greater than DEFAULT_CREDIT_SCORE
        defaultExUserShouldNotBeFound("creditScore.greaterThan=" + DEFAULT_CREDIT_SCORE);

        // Get all the exUserList where creditScore is greater than SMALLER_CREDIT_SCORE
        defaultExUserShouldBeFound("creditScore.greaterThan=" + SMALLER_CREDIT_SCORE);
    }


    @Test
    @Transactional
    public void getAllExUsersByRelatedUserIsEqualToSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);
        User relatedUser = UserResourceIT.createEntity(em);
        em.persist(relatedUser);
        em.flush();
        exUser.setRelatedUser(relatedUser);
        exUserRepository.saveAndFlush(exUser);
        Long relatedUserId = relatedUser.getId();

        // Get all the exUserList where relatedUser equals to relatedUserId
        defaultExUserShouldBeFound("relatedUserId.equals=" + relatedUserId);

        // Get all the exUserList where relatedUser equals to relatedUserId + 1
        defaultExUserShouldNotBeFound("relatedUserId.equals=" + (relatedUserId + 1));
    }


    @Test
    @Transactional
    public void getAllExUsersByHistoryIsEqualToSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);
        DocumentHistory history = DocumentHistoryResourceIT.createEntity(em);
        em.persist(history);
        em.flush();
        exUser.setHistory(history);
        exUserRepository.saveAndFlush(exUser);
        Long historyId = history.getId();

        // Get all the exUserList where history equals to historyId
        defaultExUserShouldBeFound("historyId.equals=" + historyId);

        // Get all the exUserList where history equals to historyId + 1
        defaultExUserShouldNotBeFound("historyId.equals=" + (historyId + 1));
    }


    @Test
    @Transactional
    public void getAllExUsersByCompanyIsEqualToSomething() throws Exception {
        // Get already existing entity
        Company company = exUser.getCompany();
        exUserRepository.saveAndFlush(exUser);
        Long companyId = company.getId();

        // Get all the exUserList where company equals to companyId
        defaultExUserShouldBeFound("companyId.equals=" + companyId);

        // Get all the exUserList where company equals to companyId + 1
        defaultExUserShouldNotBeFound("companyId.equals=" + (companyId + 1));
    }


    @Test
    @Transactional
    public void getAllExUsersByLocationsIsEqualToSomething() throws Exception {
        // Get already existing entity
        Location locations = exUser.getLocations();
        exUserRepository.saveAndFlush(exUser);
        Long locationsId = locations.getId();

        // Get all the exUserList where locations equals to locationsId
        defaultExUserShouldBeFound("locationsId.equals=" + locationsId);

        // Get all the exUserList where locations equals to locationsId + 1
        defaultExUserShouldNotBeFound("locationsId.equals=" + (locationsId + 1));
    }


    @Test
    @Transactional
    public void getAllExUsersByUserGroupsIsEqualToSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);
        UserGroup userGroups = UserGroupResourceIT.createEntity(em);
        em.persist(userGroups);
        em.flush();
        exUser.addUserGroups(userGroups);
        exUserRepository.saveAndFlush(exUser);
        Long userGroupsId = userGroups.getId();

        // Get all the exUserList where userGroups equals to userGroupsId
        defaultExUserShouldBeFound("userGroupsId.equals=" + userGroupsId);

        // Get all the exUserList where userGroups equals to userGroupsId + 1
        defaultExUserShouldNotBeFound("userGroupsId.equals=" + (userGroupsId + 1));
    }


    @Test
    @Transactional
    public void getAllExUsersByUserPermissionsIsEqualToSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);
        UserPermissions userPermissions = UserPermissionsResourceIT.createEntity(em);
        em.persist(userPermissions);
        em.flush();
        exUser.addUserPermissions(userPermissions);
        exUserRepository.saveAndFlush(exUser);
        Long userPermissionsId = userPermissions.getId();

        // Get all the exUserList where userPermissions equals to userPermissionsId
        defaultExUserShouldBeFound("userPermissionsId.equals=" + userPermissionsId);

        // Get all the exUserList where userPermissions equals to userPermissionsId + 1
        defaultExUserShouldNotBeFound("userPermissionsId.equals=" + (userPermissionsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultExUserShouldBeFound(String filter) throws Exception {
        restExUserMockMvc.perform(get("/api/ex-users?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].userKey").value(hasItem(DEFAULT_USER_KEY)))
            .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].addressLine1").value(hasItem(DEFAULT_ADDRESS_LINE_1)))
            .andExpect(jsonPath("$.[*].addressLine2").value(hasItem(DEFAULT_ADDRESS_LINE_2)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].userLimit").value(hasItem(DEFAULT_USER_LIMIT.intValue())))
            .andExpect(jsonPath("$.[*].creditScore").value(hasItem(DEFAULT_CREDIT_SCORE.doubleValue())));

        // Check, that the count call also returns 1
        restExUserMockMvc.perform(get("/api/ex-users/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultExUserShouldNotBeFound(String filter) throws Exception {
        restExUserMockMvc.perform(get("/api/ex-users?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restExUserMockMvc.perform(get("/api/ex-users/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingExUser() throws Exception {
        // Get the exUser
        restExUserMockMvc.perform(get("/api/ex-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExUser() throws Exception {
        // Initialize the database
        exUserService.save(exUser);

        int databaseSizeBeforeUpdate = exUserRepository.findAll().size();

        // Update the exUser
        ExUser updatedExUser = exUserRepository.findById(exUser.getId()).get();
        // Disconnect from session so that the updates on updatedExUser are not directly saved in db
        em.detach(updatedExUser);
        updatedExUser
            .userKey(UPDATED_USER_KEY)
            .login(UPDATED_LOGIN)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .isActive(UPDATED_IS_ACTIVE)
            .phone(UPDATED_PHONE)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .userLimit(UPDATED_USER_LIMIT)
            .creditScore(UPDATED_CREDIT_SCORE);

        restExUserMockMvc.perform(put("/api/ex-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedExUser)))
            .andExpect(status().isOk());

        // Validate the ExUser in the database
        List<ExUser> exUserList = exUserRepository.findAll();
        assertThat(exUserList).hasSize(databaseSizeBeforeUpdate);
        ExUser testExUser = exUserList.get(exUserList.size() - 1);
        assertThat(testExUser.getUserKey()).isEqualTo(UPDATED_USER_KEY);
        assertThat(testExUser.getLogin()).isEqualTo(UPDATED_LOGIN);
        assertThat(testExUser.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testExUser.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testExUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testExUser.isIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testExUser.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testExUser.getAddressLine1()).isEqualTo(UPDATED_ADDRESS_LINE_1);
        assertThat(testExUser.getAddressLine2()).isEqualTo(UPDATED_ADDRESS_LINE_2);
        assertThat(testExUser.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testExUser.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testExUser.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testExUser.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testExUser.getUserLimit()).isEqualTo(UPDATED_USER_LIMIT);
        assertThat(testExUser.getCreditScore()).isEqualTo(UPDATED_CREDIT_SCORE);
    }

    @Test
    @Transactional
    public void updateNonExistingExUser() throws Exception {
        int databaseSizeBeforeUpdate = exUserRepository.findAll().size();

        // Create the ExUser

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExUserMockMvc.perform(put("/api/ex-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(exUser)))
            .andExpect(status().isBadRequest());

        // Validate the ExUser in the database
        List<ExUser> exUserList = exUserRepository.findAll();
        assertThat(exUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteExUser() throws Exception {
        // Initialize the database
        exUserService.save(exUser);

        int databaseSizeBeforeDelete = exUserRepository.findAll().size();

        // Delete the exUser
        restExUserMockMvc.perform(delete("/api/ex-users/{id}", exUser.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ExUser> exUserList = exUserRepository.findAll();
        assertThat(exUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

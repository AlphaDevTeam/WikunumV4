package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.WikunumApp;
import com.alphadevs.sales.domain.ItemAddOns;
import com.alphadevs.sales.domain.Location;
import com.alphadevs.sales.domain.Items;
import com.alphadevs.sales.repository.ItemAddOnsRepository;
import com.alphadevs.sales.service.ItemAddOnsService;
import com.alphadevs.sales.web.rest.errors.ExceptionTranslator;
import com.alphadevs.sales.service.dto.ItemAddOnsCriteria;
import com.alphadevs.sales.service.ItemAddOnsQueryService;

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
import org.springframework.util.Base64Utils;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;

import static com.alphadevs.sales.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ItemAddOnsResource} REST controller.
 */
@SpringBootTest(classes = WikunumApp.class)
public class ItemAddOnsResourceIT {

    private static final String DEFAULT_ADDON_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ADDON_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_ADDON_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ADDON_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDON_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_ADDON_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Boolean DEFAULT_ALLOW_SUBSTRACT = false;
    private static final Boolean UPDATED_ALLOW_SUBSTRACT = true;

    private static final BigDecimal DEFAULT_ADDON_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_ADDON_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_ADDON_PRICE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_SUBSTRACT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_SUBSTRACT_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_SUBSTRACT_PRICE = new BigDecimal(1 - 1);

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    @Autowired
    private ItemAddOnsRepository itemAddOnsRepository;

    @Autowired
    private ItemAddOnsService itemAddOnsService;

    @Autowired
    private ItemAddOnsQueryService itemAddOnsQueryService;

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

    private MockMvc restItemAddOnsMockMvc;

    private ItemAddOns itemAddOns;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ItemAddOnsResource itemAddOnsResource = new ItemAddOnsResource(itemAddOnsService, itemAddOnsQueryService);
        this.restItemAddOnsMockMvc = MockMvcBuilders.standaloneSetup(itemAddOnsResource)
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
    public static ItemAddOns createEntity(EntityManager em) {
        ItemAddOns itemAddOns = new ItemAddOns()
            .addonCode(DEFAULT_ADDON_CODE)
            .addonName(DEFAULT_ADDON_NAME)
            .addonDescription(DEFAULT_ADDON_DESCRIPTION)
            .isActive(DEFAULT_IS_ACTIVE)
            .allowSubstract(DEFAULT_ALLOW_SUBSTRACT)
            .addonPrice(DEFAULT_ADDON_PRICE)
            .substractPrice(DEFAULT_SUBSTRACT_PRICE)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE);
        // Add required entity
        Location location;
        if (TestUtil.findAll(em, Location.class).isEmpty()) {
            location = LocationResourceIT.createEntity(em);
            em.persist(location);
            em.flush();
        } else {
            location = TestUtil.findAll(em, Location.class).get(0);
        }
        itemAddOns.setLocation(location);
        return itemAddOns;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemAddOns createUpdatedEntity(EntityManager em) {
        ItemAddOns itemAddOns = new ItemAddOns()
            .addonCode(UPDATED_ADDON_CODE)
            .addonName(UPDATED_ADDON_NAME)
            .addonDescription(UPDATED_ADDON_DESCRIPTION)
            .isActive(UPDATED_IS_ACTIVE)
            .allowSubstract(UPDATED_ALLOW_SUBSTRACT)
            .addonPrice(UPDATED_ADDON_PRICE)
            .substractPrice(UPDATED_SUBSTRACT_PRICE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        // Add required entity
        Location location;
        if (TestUtil.findAll(em, Location.class).isEmpty()) {
            location = LocationResourceIT.createUpdatedEntity(em);
            em.persist(location);
            em.flush();
        } else {
            location = TestUtil.findAll(em, Location.class).get(0);
        }
        itemAddOns.setLocation(location);
        return itemAddOns;
    }

    @BeforeEach
    public void initTest() {
        itemAddOns = createEntity(em);
    }

    @Test
    @Transactional
    public void createItemAddOns() throws Exception {
        int databaseSizeBeforeCreate = itemAddOnsRepository.findAll().size();

        // Create the ItemAddOns
        restItemAddOnsMockMvc.perform(post("/api/item-add-ons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itemAddOns)))
            .andExpect(status().isCreated());

        // Validate the ItemAddOns in the database
        List<ItemAddOns> itemAddOnsList = itemAddOnsRepository.findAll();
        assertThat(itemAddOnsList).hasSize(databaseSizeBeforeCreate + 1);
        ItemAddOns testItemAddOns = itemAddOnsList.get(itemAddOnsList.size() - 1);
        assertThat(testItemAddOns.getAddonCode()).isEqualTo(DEFAULT_ADDON_CODE);
        assertThat(testItemAddOns.getAddonName()).isEqualTo(DEFAULT_ADDON_NAME);
        assertThat(testItemAddOns.getAddonDescription()).isEqualTo(DEFAULT_ADDON_DESCRIPTION);
        assertThat(testItemAddOns.isIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testItemAddOns.isAllowSubstract()).isEqualTo(DEFAULT_ALLOW_SUBSTRACT);
        assertThat(testItemAddOns.getAddonPrice()).isEqualTo(DEFAULT_ADDON_PRICE);
        assertThat(testItemAddOns.getSubstractPrice()).isEqualTo(DEFAULT_SUBSTRACT_PRICE);
        assertThat(testItemAddOns.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testItemAddOns.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createItemAddOnsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = itemAddOnsRepository.findAll().size();

        // Create the ItemAddOns with an existing ID
        itemAddOns.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restItemAddOnsMockMvc.perform(post("/api/item-add-ons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itemAddOns)))
            .andExpect(status().isBadRequest());

        // Validate the ItemAddOns in the database
        List<ItemAddOns> itemAddOnsList = itemAddOnsRepository.findAll();
        assertThat(itemAddOnsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkAddonCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemAddOnsRepository.findAll().size();
        // set the field null
        itemAddOns.setAddonCode(null);

        // Create the ItemAddOns, which fails.

        restItemAddOnsMockMvc.perform(post("/api/item-add-ons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itemAddOns)))
            .andExpect(status().isBadRequest());

        List<ItemAddOns> itemAddOnsList = itemAddOnsRepository.findAll();
        assertThat(itemAddOnsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAddonNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemAddOnsRepository.findAll().size();
        // set the field null
        itemAddOns.setAddonName(null);

        // Create the ItemAddOns, which fails.

        restItemAddOnsMockMvc.perform(post("/api/item-add-ons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itemAddOns)))
            .andExpect(status().isBadRequest());

        List<ItemAddOns> itemAddOnsList = itemAddOnsRepository.findAll();
        assertThat(itemAddOnsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllItemAddOns() throws Exception {
        // Initialize the database
        itemAddOnsRepository.saveAndFlush(itemAddOns);

        // Get all the itemAddOnsList
        restItemAddOnsMockMvc.perform(get("/api/item-add-ons?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itemAddOns.getId().intValue())))
            .andExpect(jsonPath("$.[*].addonCode").value(hasItem(DEFAULT_ADDON_CODE)))
            .andExpect(jsonPath("$.[*].addonName").value(hasItem(DEFAULT_ADDON_NAME)))
            .andExpect(jsonPath("$.[*].addonDescription").value(hasItem(DEFAULT_ADDON_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].allowSubstract").value(hasItem(DEFAULT_ALLOW_SUBSTRACT.booleanValue())))
            .andExpect(jsonPath("$.[*].addonPrice").value(hasItem(DEFAULT_ADDON_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].substractPrice").value(hasItem(DEFAULT_SUBSTRACT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));
    }
    
    @Test
    @Transactional
    public void getItemAddOns() throws Exception {
        // Initialize the database
        itemAddOnsRepository.saveAndFlush(itemAddOns);

        // Get the itemAddOns
        restItemAddOnsMockMvc.perform(get("/api/item-add-ons/{id}", itemAddOns.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(itemAddOns.getId().intValue()))
            .andExpect(jsonPath("$.addonCode").value(DEFAULT_ADDON_CODE))
            .andExpect(jsonPath("$.addonName").value(DEFAULT_ADDON_NAME))
            .andExpect(jsonPath("$.addonDescription").value(DEFAULT_ADDON_DESCRIPTION))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.allowSubstract").value(DEFAULT_ALLOW_SUBSTRACT.booleanValue()))
            .andExpect(jsonPath("$.addonPrice").value(DEFAULT_ADDON_PRICE.intValue()))
            .andExpect(jsonPath("$.substractPrice").value(DEFAULT_SUBSTRACT_PRICE.intValue()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)));
    }


    @Test
    @Transactional
    public void getItemAddOnsByIdFiltering() throws Exception {
        // Initialize the database
        itemAddOnsRepository.saveAndFlush(itemAddOns);

        Long id = itemAddOns.getId();

        defaultItemAddOnsShouldBeFound("id.equals=" + id);
        defaultItemAddOnsShouldNotBeFound("id.notEquals=" + id);

        defaultItemAddOnsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultItemAddOnsShouldNotBeFound("id.greaterThan=" + id);

        defaultItemAddOnsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultItemAddOnsShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllItemAddOnsByAddonCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        itemAddOnsRepository.saveAndFlush(itemAddOns);

        // Get all the itemAddOnsList where addonCode equals to DEFAULT_ADDON_CODE
        defaultItemAddOnsShouldBeFound("addonCode.equals=" + DEFAULT_ADDON_CODE);

        // Get all the itemAddOnsList where addonCode equals to UPDATED_ADDON_CODE
        defaultItemAddOnsShouldNotBeFound("addonCode.equals=" + UPDATED_ADDON_CODE);
    }

    @Test
    @Transactional
    public void getAllItemAddOnsByAddonCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        itemAddOnsRepository.saveAndFlush(itemAddOns);

        // Get all the itemAddOnsList where addonCode not equals to DEFAULT_ADDON_CODE
        defaultItemAddOnsShouldNotBeFound("addonCode.notEquals=" + DEFAULT_ADDON_CODE);

        // Get all the itemAddOnsList where addonCode not equals to UPDATED_ADDON_CODE
        defaultItemAddOnsShouldBeFound("addonCode.notEquals=" + UPDATED_ADDON_CODE);
    }

    @Test
    @Transactional
    public void getAllItemAddOnsByAddonCodeIsInShouldWork() throws Exception {
        // Initialize the database
        itemAddOnsRepository.saveAndFlush(itemAddOns);

        // Get all the itemAddOnsList where addonCode in DEFAULT_ADDON_CODE or UPDATED_ADDON_CODE
        defaultItemAddOnsShouldBeFound("addonCode.in=" + DEFAULT_ADDON_CODE + "," + UPDATED_ADDON_CODE);

        // Get all the itemAddOnsList where addonCode equals to UPDATED_ADDON_CODE
        defaultItemAddOnsShouldNotBeFound("addonCode.in=" + UPDATED_ADDON_CODE);
    }

    @Test
    @Transactional
    public void getAllItemAddOnsByAddonCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemAddOnsRepository.saveAndFlush(itemAddOns);

        // Get all the itemAddOnsList where addonCode is not null
        defaultItemAddOnsShouldBeFound("addonCode.specified=true");

        // Get all the itemAddOnsList where addonCode is null
        defaultItemAddOnsShouldNotBeFound("addonCode.specified=false");
    }
                @Test
    @Transactional
    public void getAllItemAddOnsByAddonCodeContainsSomething() throws Exception {
        // Initialize the database
        itemAddOnsRepository.saveAndFlush(itemAddOns);

        // Get all the itemAddOnsList where addonCode contains DEFAULT_ADDON_CODE
        defaultItemAddOnsShouldBeFound("addonCode.contains=" + DEFAULT_ADDON_CODE);

        // Get all the itemAddOnsList where addonCode contains UPDATED_ADDON_CODE
        defaultItemAddOnsShouldNotBeFound("addonCode.contains=" + UPDATED_ADDON_CODE);
    }

    @Test
    @Transactional
    public void getAllItemAddOnsByAddonCodeNotContainsSomething() throws Exception {
        // Initialize the database
        itemAddOnsRepository.saveAndFlush(itemAddOns);

        // Get all the itemAddOnsList where addonCode does not contain DEFAULT_ADDON_CODE
        defaultItemAddOnsShouldNotBeFound("addonCode.doesNotContain=" + DEFAULT_ADDON_CODE);

        // Get all the itemAddOnsList where addonCode does not contain UPDATED_ADDON_CODE
        defaultItemAddOnsShouldBeFound("addonCode.doesNotContain=" + UPDATED_ADDON_CODE);
    }


    @Test
    @Transactional
    public void getAllItemAddOnsByAddonNameIsEqualToSomething() throws Exception {
        // Initialize the database
        itemAddOnsRepository.saveAndFlush(itemAddOns);

        // Get all the itemAddOnsList where addonName equals to DEFAULT_ADDON_NAME
        defaultItemAddOnsShouldBeFound("addonName.equals=" + DEFAULT_ADDON_NAME);

        // Get all the itemAddOnsList where addonName equals to UPDATED_ADDON_NAME
        defaultItemAddOnsShouldNotBeFound("addonName.equals=" + UPDATED_ADDON_NAME);
    }

    @Test
    @Transactional
    public void getAllItemAddOnsByAddonNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        itemAddOnsRepository.saveAndFlush(itemAddOns);

        // Get all the itemAddOnsList where addonName not equals to DEFAULT_ADDON_NAME
        defaultItemAddOnsShouldNotBeFound("addonName.notEquals=" + DEFAULT_ADDON_NAME);

        // Get all the itemAddOnsList where addonName not equals to UPDATED_ADDON_NAME
        defaultItemAddOnsShouldBeFound("addonName.notEquals=" + UPDATED_ADDON_NAME);
    }

    @Test
    @Transactional
    public void getAllItemAddOnsByAddonNameIsInShouldWork() throws Exception {
        // Initialize the database
        itemAddOnsRepository.saveAndFlush(itemAddOns);

        // Get all the itemAddOnsList where addonName in DEFAULT_ADDON_NAME or UPDATED_ADDON_NAME
        defaultItemAddOnsShouldBeFound("addonName.in=" + DEFAULT_ADDON_NAME + "," + UPDATED_ADDON_NAME);

        // Get all the itemAddOnsList where addonName equals to UPDATED_ADDON_NAME
        defaultItemAddOnsShouldNotBeFound("addonName.in=" + UPDATED_ADDON_NAME);
    }

    @Test
    @Transactional
    public void getAllItemAddOnsByAddonNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemAddOnsRepository.saveAndFlush(itemAddOns);

        // Get all the itemAddOnsList where addonName is not null
        defaultItemAddOnsShouldBeFound("addonName.specified=true");

        // Get all the itemAddOnsList where addonName is null
        defaultItemAddOnsShouldNotBeFound("addonName.specified=false");
    }
                @Test
    @Transactional
    public void getAllItemAddOnsByAddonNameContainsSomething() throws Exception {
        // Initialize the database
        itemAddOnsRepository.saveAndFlush(itemAddOns);

        // Get all the itemAddOnsList where addonName contains DEFAULT_ADDON_NAME
        defaultItemAddOnsShouldBeFound("addonName.contains=" + DEFAULT_ADDON_NAME);

        // Get all the itemAddOnsList where addonName contains UPDATED_ADDON_NAME
        defaultItemAddOnsShouldNotBeFound("addonName.contains=" + UPDATED_ADDON_NAME);
    }

    @Test
    @Transactional
    public void getAllItemAddOnsByAddonNameNotContainsSomething() throws Exception {
        // Initialize the database
        itemAddOnsRepository.saveAndFlush(itemAddOns);

        // Get all the itemAddOnsList where addonName does not contain DEFAULT_ADDON_NAME
        defaultItemAddOnsShouldNotBeFound("addonName.doesNotContain=" + DEFAULT_ADDON_NAME);

        // Get all the itemAddOnsList where addonName does not contain UPDATED_ADDON_NAME
        defaultItemAddOnsShouldBeFound("addonName.doesNotContain=" + UPDATED_ADDON_NAME);
    }


    @Test
    @Transactional
    public void getAllItemAddOnsByAddonDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        itemAddOnsRepository.saveAndFlush(itemAddOns);

        // Get all the itemAddOnsList where addonDescription equals to DEFAULT_ADDON_DESCRIPTION
        defaultItemAddOnsShouldBeFound("addonDescription.equals=" + DEFAULT_ADDON_DESCRIPTION);

        // Get all the itemAddOnsList where addonDescription equals to UPDATED_ADDON_DESCRIPTION
        defaultItemAddOnsShouldNotBeFound("addonDescription.equals=" + UPDATED_ADDON_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllItemAddOnsByAddonDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        itemAddOnsRepository.saveAndFlush(itemAddOns);

        // Get all the itemAddOnsList where addonDescription not equals to DEFAULT_ADDON_DESCRIPTION
        defaultItemAddOnsShouldNotBeFound("addonDescription.notEquals=" + DEFAULT_ADDON_DESCRIPTION);

        // Get all the itemAddOnsList where addonDescription not equals to UPDATED_ADDON_DESCRIPTION
        defaultItemAddOnsShouldBeFound("addonDescription.notEquals=" + UPDATED_ADDON_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllItemAddOnsByAddonDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        itemAddOnsRepository.saveAndFlush(itemAddOns);

        // Get all the itemAddOnsList where addonDescription in DEFAULT_ADDON_DESCRIPTION or UPDATED_ADDON_DESCRIPTION
        defaultItemAddOnsShouldBeFound("addonDescription.in=" + DEFAULT_ADDON_DESCRIPTION + "," + UPDATED_ADDON_DESCRIPTION);

        // Get all the itemAddOnsList where addonDescription equals to UPDATED_ADDON_DESCRIPTION
        defaultItemAddOnsShouldNotBeFound("addonDescription.in=" + UPDATED_ADDON_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllItemAddOnsByAddonDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemAddOnsRepository.saveAndFlush(itemAddOns);

        // Get all the itemAddOnsList where addonDescription is not null
        defaultItemAddOnsShouldBeFound("addonDescription.specified=true");

        // Get all the itemAddOnsList where addonDescription is null
        defaultItemAddOnsShouldNotBeFound("addonDescription.specified=false");
    }
                @Test
    @Transactional
    public void getAllItemAddOnsByAddonDescriptionContainsSomething() throws Exception {
        // Initialize the database
        itemAddOnsRepository.saveAndFlush(itemAddOns);

        // Get all the itemAddOnsList where addonDescription contains DEFAULT_ADDON_DESCRIPTION
        defaultItemAddOnsShouldBeFound("addonDescription.contains=" + DEFAULT_ADDON_DESCRIPTION);

        // Get all the itemAddOnsList where addonDescription contains UPDATED_ADDON_DESCRIPTION
        defaultItemAddOnsShouldNotBeFound("addonDescription.contains=" + UPDATED_ADDON_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllItemAddOnsByAddonDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        itemAddOnsRepository.saveAndFlush(itemAddOns);

        // Get all the itemAddOnsList where addonDescription does not contain DEFAULT_ADDON_DESCRIPTION
        defaultItemAddOnsShouldNotBeFound("addonDescription.doesNotContain=" + DEFAULT_ADDON_DESCRIPTION);

        // Get all the itemAddOnsList where addonDescription does not contain UPDATED_ADDON_DESCRIPTION
        defaultItemAddOnsShouldBeFound("addonDescription.doesNotContain=" + UPDATED_ADDON_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllItemAddOnsByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        itemAddOnsRepository.saveAndFlush(itemAddOns);

        // Get all the itemAddOnsList where isActive equals to DEFAULT_IS_ACTIVE
        defaultItemAddOnsShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the itemAddOnsList where isActive equals to UPDATED_IS_ACTIVE
        defaultItemAddOnsShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllItemAddOnsByIsActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        itemAddOnsRepository.saveAndFlush(itemAddOns);

        // Get all the itemAddOnsList where isActive not equals to DEFAULT_IS_ACTIVE
        defaultItemAddOnsShouldNotBeFound("isActive.notEquals=" + DEFAULT_IS_ACTIVE);

        // Get all the itemAddOnsList where isActive not equals to UPDATED_IS_ACTIVE
        defaultItemAddOnsShouldBeFound("isActive.notEquals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllItemAddOnsByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        itemAddOnsRepository.saveAndFlush(itemAddOns);

        // Get all the itemAddOnsList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultItemAddOnsShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the itemAddOnsList where isActive equals to UPDATED_IS_ACTIVE
        defaultItemAddOnsShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllItemAddOnsByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemAddOnsRepository.saveAndFlush(itemAddOns);

        // Get all the itemAddOnsList where isActive is not null
        defaultItemAddOnsShouldBeFound("isActive.specified=true");

        // Get all the itemAddOnsList where isActive is null
        defaultItemAddOnsShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    public void getAllItemAddOnsByAllowSubstractIsEqualToSomething() throws Exception {
        // Initialize the database
        itemAddOnsRepository.saveAndFlush(itemAddOns);

        // Get all the itemAddOnsList where allowSubstract equals to DEFAULT_ALLOW_SUBSTRACT
        defaultItemAddOnsShouldBeFound("allowSubstract.equals=" + DEFAULT_ALLOW_SUBSTRACT);

        // Get all the itemAddOnsList where allowSubstract equals to UPDATED_ALLOW_SUBSTRACT
        defaultItemAddOnsShouldNotBeFound("allowSubstract.equals=" + UPDATED_ALLOW_SUBSTRACT);
    }

    @Test
    @Transactional
    public void getAllItemAddOnsByAllowSubstractIsNotEqualToSomething() throws Exception {
        // Initialize the database
        itemAddOnsRepository.saveAndFlush(itemAddOns);

        // Get all the itemAddOnsList where allowSubstract not equals to DEFAULT_ALLOW_SUBSTRACT
        defaultItemAddOnsShouldNotBeFound("allowSubstract.notEquals=" + DEFAULT_ALLOW_SUBSTRACT);

        // Get all the itemAddOnsList where allowSubstract not equals to UPDATED_ALLOW_SUBSTRACT
        defaultItemAddOnsShouldBeFound("allowSubstract.notEquals=" + UPDATED_ALLOW_SUBSTRACT);
    }

    @Test
    @Transactional
    public void getAllItemAddOnsByAllowSubstractIsInShouldWork() throws Exception {
        // Initialize the database
        itemAddOnsRepository.saveAndFlush(itemAddOns);

        // Get all the itemAddOnsList where allowSubstract in DEFAULT_ALLOW_SUBSTRACT or UPDATED_ALLOW_SUBSTRACT
        defaultItemAddOnsShouldBeFound("allowSubstract.in=" + DEFAULT_ALLOW_SUBSTRACT + "," + UPDATED_ALLOW_SUBSTRACT);

        // Get all the itemAddOnsList where allowSubstract equals to UPDATED_ALLOW_SUBSTRACT
        defaultItemAddOnsShouldNotBeFound("allowSubstract.in=" + UPDATED_ALLOW_SUBSTRACT);
    }

    @Test
    @Transactional
    public void getAllItemAddOnsByAllowSubstractIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemAddOnsRepository.saveAndFlush(itemAddOns);

        // Get all the itemAddOnsList where allowSubstract is not null
        defaultItemAddOnsShouldBeFound("allowSubstract.specified=true");

        // Get all the itemAddOnsList where allowSubstract is null
        defaultItemAddOnsShouldNotBeFound("allowSubstract.specified=false");
    }

    @Test
    @Transactional
    public void getAllItemAddOnsByAddonPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        itemAddOnsRepository.saveAndFlush(itemAddOns);

        // Get all the itemAddOnsList where addonPrice equals to DEFAULT_ADDON_PRICE
        defaultItemAddOnsShouldBeFound("addonPrice.equals=" + DEFAULT_ADDON_PRICE);

        // Get all the itemAddOnsList where addonPrice equals to UPDATED_ADDON_PRICE
        defaultItemAddOnsShouldNotBeFound("addonPrice.equals=" + UPDATED_ADDON_PRICE);
    }

    @Test
    @Transactional
    public void getAllItemAddOnsByAddonPriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        itemAddOnsRepository.saveAndFlush(itemAddOns);

        // Get all the itemAddOnsList where addonPrice not equals to DEFAULT_ADDON_PRICE
        defaultItemAddOnsShouldNotBeFound("addonPrice.notEquals=" + DEFAULT_ADDON_PRICE);

        // Get all the itemAddOnsList where addonPrice not equals to UPDATED_ADDON_PRICE
        defaultItemAddOnsShouldBeFound("addonPrice.notEquals=" + UPDATED_ADDON_PRICE);
    }

    @Test
    @Transactional
    public void getAllItemAddOnsByAddonPriceIsInShouldWork() throws Exception {
        // Initialize the database
        itemAddOnsRepository.saveAndFlush(itemAddOns);

        // Get all the itemAddOnsList where addonPrice in DEFAULT_ADDON_PRICE or UPDATED_ADDON_PRICE
        defaultItemAddOnsShouldBeFound("addonPrice.in=" + DEFAULT_ADDON_PRICE + "," + UPDATED_ADDON_PRICE);

        // Get all the itemAddOnsList where addonPrice equals to UPDATED_ADDON_PRICE
        defaultItemAddOnsShouldNotBeFound("addonPrice.in=" + UPDATED_ADDON_PRICE);
    }

    @Test
    @Transactional
    public void getAllItemAddOnsByAddonPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemAddOnsRepository.saveAndFlush(itemAddOns);

        // Get all the itemAddOnsList where addonPrice is not null
        defaultItemAddOnsShouldBeFound("addonPrice.specified=true");

        // Get all the itemAddOnsList where addonPrice is null
        defaultItemAddOnsShouldNotBeFound("addonPrice.specified=false");
    }

    @Test
    @Transactional
    public void getAllItemAddOnsByAddonPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemAddOnsRepository.saveAndFlush(itemAddOns);

        // Get all the itemAddOnsList where addonPrice is greater than or equal to DEFAULT_ADDON_PRICE
        defaultItemAddOnsShouldBeFound("addonPrice.greaterThanOrEqual=" + DEFAULT_ADDON_PRICE);

        // Get all the itemAddOnsList where addonPrice is greater than or equal to UPDATED_ADDON_PRICE
        defaultItemAddOnsShouldNotBeFound("addonPrice.greaterThanOrEqual=" + UPDATED_ADDON_PRICE);
    }

    @Test
    @Transactional
    public void getAllItemAddOnsByAddonPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemAddOnsRepository.saveAndFlush(itemAddOns);

        // Get all the itemAddOnsList where addonPrice is less than or equal to DEFAULT_ADDON_PRICE
        defaultItemAddOnsShouldBeFound("addonPrice.lessThanOrEqual=" + DEFAULT_ADDON_PRICE);

        // Get all the itemAddOnsList where addonPrice is less than or equal to SMALLER_ADDON_PRICE
        defaultItemAddOnsShouldNotBeFound("addonPrice.lessThanOrEqual=" + SMALLER_ADDON_PRICE);
    }

    @Test
    @Transactional
    public void getAllItemAddOnsByAddonPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        itemAddOnsRepository.saveAndFlush(itemAddOns);

        // Get all the itemAddOnsList where addonPrice is less than DEFAULT_ADDON_PRICE
        defaultItemAddOnsShouldNotBeFound("addonPrice.lessThan=" + DEFAULT_ADDON_PRICE);

        // Get all the itemAddOnsList where addonPrice is less than UPDATED_ADDON_PRICE
        defaultItemAddOnsShouldBeFound("addonPrice.lessThan=" + UPDATED_ADDON_PRICE);
    }

    @Test
    @Transactional
    public void getAllItemAddOnsByAddonPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        itemAddOnsRepository.saveAndFlush(itemAddOns);

        // Get all the itemAddOnsList where addonPrice is greater than DEFAULT_ADDON_PRICE
        defaultItemAddOnsShouldNotBeFound("addonPrice.greaterThan=" + DEFAULT_ADDON_PRICE);

        // Get all the itemAddOnsList where addonPrice is greater than SMALLER_ADDON_PRICE
        defaultItemAddOnsShouldBeFound("addonPrice.greaterThan=" + SMALLER_ADDON_PRICE);
    }


    @Test
    @Transactional
    public void getAllItemAddOnsBySubstractPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        itemAddOnsRepository.saveAndFlush(itemAddOns);

        // Get all the itemAddOnsList where substractPrice equals to DEFAULT_SUBSTRACT_PRICE
        defaultItemAddOnsShouldBeFound("substractPrice.equals=" + DEFAULT_SUBSTRACT_PRICE);

        // Get all the itemAddOnsList where substractPrice equals to UPDATED_SUBSTRACT_PRICE
        defaultItemAddOnsShouldNotBeFound("substractPrice.equals=" + UPDATED_SUBSTRACT_PRICE);
    }

    @Test
    @Transactional
    public void getAllItemAddOnsBySubstractPriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        itemAddOnsRepository.saveAndFlush(itemAddOns);

        // Get all the itemAddOnsList where substractPrice not equals to DEFAULT_SUBSTRACT_PRICE
        defaultItemAddOnsShouldNotBeFound("substractPrice.notEquals=" + DEFAULT_SUBSTRACT_PRICE);

        // Get all the itemAddOnsList where substractPrice not equals to UPDATED_SUBSTRACT_PRICE
        defaultItemAddOnsShouldBeFound("substractPrice.notEquals=" + UPDATED_SUBSTRACT_PRICE);
    }

    @Test
    @Transactional
    public void getAllItemAddOnsBySubstractPriceIsInShouldWork() throws Exception {
        // Initialize the database
        itemAddOnsRepository.saveAndFlush(itemAddOns);

        // Get all the itemAddOnsList where substractPrice in DEFAULT_SUBSTRACT_PRICE or UPDATED_SUBSTRACT_PRICE
        defaultItemAddOnsShouldBeFound("substractPrice.in=" + DEFAULT_SUBSTRACT_PRICE + "," + UPDATED_SUBSTRACT_PRICE);

        // Get all the itemAddOnsList where substractPrice equals to UPDATED_SUBSTRACT_PRICE
        defaultItemAddOnsShouldNotBeFound("substractPrice.in=" + UPDATED_SUBSTRACT_PRICE);
    }

    @Test
    @Transactional
    public void getAllItemAddOnsBySubstractPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemAddOnsRepository.saveAndFlush(itemAddOns);

        // Get all the itemAddOnsList where substractPrice is not null
        defaultItemAddOnsShouldBeFound("substractPrice.specified=true");

        // Get all the itemAddOnsList where substractPrice is null
        defaultItemAddOnsShouldNotBeFound("substractPrice.specified=false");
    }

    @Test
    @Transactional
    public void getAllItemAddOnsBySubstractPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemAddOnsRepository.saveAndFlush(itemAddOns);

        // Get all the itemAddOnsList where substractPrice is greater than or equal to DEFAULT_SUBSTRACT_PRICE
        defaultItemAddOnsShouldBeFound("substractPrice.greaterThanOrEqual=" + DEFAULT_SUBSTRACT_PRICE);

        // Get all the itemAddOnsList where substractPrice is greater than or equal to UPDATED_SUBSTRACT_PRICE
        defaultItemAddOnsShouldNotBeFound("substractPrice.greaterThanOrEqual=" + UPDATED_SUBSTRACT_PRICE);
    }

    @Test
    @Transactional
    public void getAllItemAddOnsBySubstractPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemAddOnsRepository.saveAndFlush(itemAddOns);

        // Get all the itemAddOnsList where substractPrice is less than or equal to DEFAULT_SUBSTRACT_PRICE
        defaultItemAddOnsShouldBeFound("substractPrice.lessThanOrEqual=" + DEFAULT_SUBSTRACT_PRICE);

        // Get all the itemAddOnsList where substractPrice is less than or equal to SMALLER_SUBSTRACT_PRICE
        defaultItemAddOnsShouldNotBeFound("substractPrice.lessThanOrEqual=" + SMALLER_SUBSTRACT_PRICE);
    }

    @Test
    @Transactional
    public void getAllItemAddOnsBySubstractPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        itemAddOnsRepository.saveAndFlush(itemAddOns);

        // Get all the itemAddOnsList where substractPrice is less than DEFAULT_SUBSTRACT_PRICE
        defaultItemAddOnsShouldNotBeFound("substractPrice.lessThan=" + DEFAULT_SUBSTRACT_PRICE);

        // Get all the itemAddOnsList where substractPrice is less than UPDATED_SUBSTRACT_PRICE
        defaultItemAddOnsShouldBeFound("substractPrice.lessThan=" + UPDATED_SUBSTRACT_PRICE);
    }

    @Test
    @Transactional
    public void getAllItemAddOnsBySubstractPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        itemAddOnsRepository.saveAndFlush(itemAddOns);

        // Get all the itemAddOnsList where substractPrice is greater than DEFAULT_SUBSTRACT_PRICE
        defaultItemAddOnsShouldNotBeFound("substractPrice.greaterThan=" + DEFAULT_SUBSTRACT_PRICE);

        // Get all the itemAddOnsList where substractPrice is greater than SMALLER_SUBSTRACT_PRICE
        defaultItemAddOnsShouldBeFound("substractPrice.greaterThan=" + SMALLER_SUBSTRACT_PRICE);
    }


    @Test
    @Transactional
    public void getAllItemAddOnsByLocationIsEqualToSomething() throws Exception {
        // Get already existing entity
        Location location = itemAddOns.getLocation();
        itemAddOnsRepository.saveAndFlush(itemAddOns);
        Long locationId = location.getId();

        // Get all the itemAddOnsList where location equals to locationId
        defaultItemAddOnsShouldBeFound("locationId.equals=" + locationId);

        // Get all the itemAddOnsList where location equals to locationId + 1
        defaultItemAddOnsShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }


    @Test
    @Transactional
    public void getAllItemAddOnsByItemsIsEqualToSomething() throws Exception {
        // Initialize the database
        itemAddOnsRepository.saveAndFlush(itemAddOns);
        Items items = ItemsResourceIT.createEntity(em);
        em.persist(items);
        em.flush();
        itemAddOns.addItems(items);
        itemAddOnsRepository.saveAndFlush(itemAddOns);
        Long itemsId = items.getId();

        // Get all the itemAddOnsList where items equals to itemsId
        defaultItemAddOnsShouldBeFound("itemsId.equals=" + itemsId);

        // Get all the itemAddOnsList where items equals to itemsId + 1
        defaultItemAddOnsShouldNotBeFound("itemsId.equals=" + (itemsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultItemAddOnsShouldBeFound(String filter) throws Exception {
        restItemAddOnsMockMvc.perform(get("/api/item-add-ons?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itemAddOns.getId().intValue())))
            .andExpect(jsonPath("$.[*].addonCode").value(hasItem(DEFAULT_ADDON_CODE)))
            .andExpect(jsonPath("$.[*].addonName").value(hasItem(DEFAULT_ADDON_NAME)))
            .andExpect(jsonPath("$.[*].addonDescription").value(hasItem(DEFAULT_ADDON_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].allowSubstract").value(hasItem(DEFAULT_ALLOW_SUBSTRACT.booleanValue())))
            .andExpect(jsonPath("$.[*].addonPrice").value(hasItem(DEFAULT_ADDON_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].substractPrice").value(hasItem(DEFAULT_SUBSTRACT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));

        // Check, that the count call also returns 1
        restItemAddOnsMockMvc.perform(get("/api/item-add-ons/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultItemAddOnsShouldNotBeFound(String filter) throws Exception {
        restItemAddOnsMockMvc.perform(get("/api/item-add-ons?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restItemAddOnsMockMvc.perform(get("/api/item-add-ons/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingItemAddOns() throws Exception {
        // Get the itemAddOns
        restItemAddOnsMockMvc.perform(get("/api/item-add-ons/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateItemAddOns() throws Exception {
        // Initialize the database
        itemAddOnsService.save(itemAddOns);

        int databaseSizeBeforeUpdate = itemAddOnsRepository.findAll().size();

        // Update the itemAddOns
        ItemAddOns updatedItemAddOns = itemAddOnsRepository.findById(itemAddOns.getId()).get();
        // Disconnect from session so that the updates on updatedItemAddOns are not directly saved in db
        em.detach(updatedItemAddOns);
        updatedItemAddOns
            .addonCode(UPDATED_ADDON_CODE)
            .addonName(UPDATED_ADDON_NAME)
            .addonDescription(UPDATED_ADDON_DESCRIPTION)
            .isActive(UPDATED_IS_ACTIVE)
            .allowSubstract(UPDATED_ALLOW_SUBSTRACT)
            .addonPrice(UPDATED_ADDON_PRICE)
            .substractPrice(UPDATED_SUBSTRACT_PRICE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restItemAddOnsMockMvc.perform(put("/api/item-add-ons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedItemAddOns)))
            .andExpect(status().isOk());

        // Validate the ItemAddOns in the database
        List<ItemAddOns> itemAddOnsList = itemAddOnsRepository.findAll();
        assertThat(itemAddOnsList).hasSize(databaseSizeBeforeUpdate);
        ItemAddOns testItemAddOns = itemAddOnsList.get(itemAddOnsList.size() - 1);
        assertThat(testItemAddOns.getAddonCode()).isEqualTo(UPDATED_ADDON_CODE);
        assertThat(testItemAddOns.getAddonName()).isEqualTo(UPDATED_ADDON_NAME);
        assertThat(testItemAddOns.getAddonDescription()).isEqualTo(UPDATED_ADDON_DESCRIPTION);
        assertThat(testItemAddOns.isIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testItemAddOns.isAllowSubstract()).isEqualTo(UPDATED_ALLOW_SUBSTRACT);
        assertThat(testItemAddOns.getAddonPrice()).isEqualTo(UPDATED_ADDON_PRICE);
        assertThat(testItemAddOns.getSubstractPrice()).isEqualTo(UPDATED_SUBSTRACT_PRICE);
        assertThat(testItemAddOns.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testItemAddOns.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingItemAddOns() throws Exception {
        int databaseSizeBeforeUpdate = itemAddOnsRepository.findAll().size();

        // Create the ItemAddOns

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemAddOnsMockMvc.perform(put("/api/item-add-ons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itemAddOns)))
            .andExpect(status().isBadRequest());

        // Validate the ItemAddOns in the database
        List<ItemAddOns> itemAddOnsList = itemAddOnsRepository.findAll();
        assertThat(itemAddOnsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteItemAddOns() throws Exception {
        // Initialize the database
        itemAddOnsService.save(itemAddOns);

        int databaseSizeBeforeDelete = itemAddOnsRepository.findAll().size();

        // Delete the itemAddOns
        restItemAddOnsMockMvc.perform(delete("/api/item-add-ons/{id}", itemAddOns.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ItemAddOns> itemAddOnsList = itemAddOnsRepository.findAll();
        assertThat(itemAddOnsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

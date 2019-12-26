package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.WikunumApp;
import com.alphadevs.sales.domain.Items;
import com.alphadevs.sales.domain.DocumentHistory;
import com.alphadevs.sales.domain.Model;
import com.alphadevs.sales.domain.Products;
import com.alphadevs.sales.domain.Location;
import com.alphadevs.sales.domain.UnitOfMeasure;
import com.alphadevs.sales.domain.Currency;
import com.alphadevs.sales.domain.ItemAddOns;
import com.alphadevs.sales.repository.ItemsRepository;
import com.alphadevs.sales.service.ItemsService;
import com.alphadevs.sales.web.rest.errors.ExceptionTranslator;
import com.alphadevs.sales.service.dto.ItemsCriteria;
import com.alphadevs.sales.service.ItemsQueryService;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static com.alphadevs.sales.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ItemsResource} REST controller.
 */
@SpringBootTest(classes = WikunumApp.class)
public class ItemsResourceIT {

    private static final String DEFAULT_ITEM_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ITEM_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_ITEM_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ITEM_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ITEM_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_ITEM_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_ITEM_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_ITEM_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_ITEM_PRICE = new BigDecimal(1 - 1);

    private static final String DEFAULT_ITEM_SERIAL = "AAAAAAAAAA";
    private static final String UPDATED_ITEM_SERIAL = "BBBBBBBBBB";

    private static final String DEFAULT_ITEM_SUPPLIER_SERIAL = "AAAAAAAAAA";
    private static final String UPDATED_ITEM_SUPPLIER_SERIAL = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_ITEM_PROMOTIONAL_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_ITEM_PROMOTIONAL_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_ITEM_PROMOTIONAL_PRICE = new BigDecimal(1 - 1);

    private static final Double DEFAULT_ITEM_PROMOTIONAL_PERCENTAGE = 1D;
    private static final Double UPDATED_ITEM_PROMOTIONAL_PERCENTAGE = 2D;
    private static final Double SMALLER_ITEM_PROMOTIONAL_PERCENTAGE = 1D - 1D;

    private static final BigDecimal DEFAULT_ITEM_COST = new BigDecimal(1);
    private static final BigDecimal UPDATED_ITEM_COST = new BigDecimal(2);
    private static final BigDecimal SMALLER_ITEM_COST = new BigDecimal(1 - 1);

    private static final LocalDate DEFAULT_ORIGINAL_STOCK_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ORIGINAL_STOCK_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_ORIGINAL_STOCK_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_MODIFIED_STOCK_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_MODIFIED_STOCK_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_MODIFIED_STOCK_DATE = LocalDate.ofEpochDay(-1L);

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    @Autowired
    private ItemsRepository itemsRepository;

    @Mock
    private ItemsRepository itemsRepositoryMock;

    @Mock
    private ItemsService itemsServiceMock;

    @Autowired
    private ItemsService itemsService;

    @Autowired
    private ItemsQueryService itemsQueryService;

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

    private MockMvc restItemsMockMvc;

    private Items items;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ItemsResource itemsResource = new ItemsResource(itemsService, itemsQueryService);
        this.restItemsMockMvc = MockMvcBuilders.standaloneSetup(itemsResource)
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
    public static Items createEntity(EntityManager em) {
        Items items = new Items()
            .itemCode(DEFAULT_ITEM_CODE)
            .itemName(DEFAULT_ITEM_NAME)
            .itemDescription(DEFAULT_ITEM_DESCRIPTION)
            .itemPrice(DEFAULT_ITEM_PRICE)
            .itemSerial(DEFAULT_ITEM_SERIAL)
            .itemSupplierSerial(DEFAULT_ITEM_SUPPLIER_SERIAL)
            .itemPromotionalPrice(DEFAULT_ITEM_PROMOTIONAL_PRICE)
            .itemPromotionalPercentage(DEFAULT_ITEM_PROMOTIONAL_PERCENTAGE)
            .itemCost(DEFAULT_ITEM_COST)
            .originalStockDate(DEFAULT_ORIGINAL_STOCK_DATE)
            .modifiedStockDate(DEFAULT_MODIFIED_STOCK_DATE)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE);
        // Add required entity
        Model model;
        if (TestUtil.findAll(em, Model.class).isEmpty()) {
            model = ModelResourceIT.createEntity(em);
            em.persist(model);
            em.flush();
        } else {
            model = TestUtil.findAll(em, Model.class).get(0);
        }
        items.setRelatedModel(model);
        // Add required entity
        Products products;
        if (TestUtil.findAll(em, Products.class).isEmpty()) {
            products = ProductsResourceIT.createEntity(em);
            em.persist(products);
            em.flush();
        } else {
            products = TestUtil.findAll(em, Products.class).get(0);
        }
        items.setRelatedProduct(products);
        // Add required entity
        Location location;
        if (TestUtil.findAll(em, Location.class).isEmpty()) {
            location = LocationResourceIT.createEntity(em);
            em.persist(location);
            em.flush();
        } else {
            location = TestUtil.findAll(em, Location.class).get(0);
        }
        items.setLocation(location);
        // Add required entity
        UnitOfMeasure unitOfMeasure;
        if (TestUtil.findAll(em, UnitOfMeasure.class).isEmpty()) {
            unitOfMeasure = UnitOfMeasureResourceIT.createEntity(em);
            em.persist(unitOfMeasure);
            em.flush();
        } else {
            unitOfMeasure = TestUtil.findAll(em, UnitOfMeasure.class).get(0);
        }
        items.setUnitOfMeasure(unitOfMeasure);
        // Add required entity
        Currency currency;
        if (TestUtil.findAll(em, Currency.class).isEmpty()) {
            currency = CurrencyResourceIT.createEntity(em);
            em.persist(currency);
            em.flush();
        } else {
            currency = TestUtil.findAll(em, Currency.class).get(0);
        }
        items.setCurrency(currency);
        return items;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Items createUpdatedEntity(EntityManager em) {
        Items items = new Items()
            .itemCode(UPDATED_ITEM_CODE)
            .itemName(UPDATED_ITEM_NAME)
            .itemDescription(UPDATED_ITEM_DESCRIPTION)
            .itemPrice(UPDATED_ITEM_PRICE)
            .itemSerial(UPDATED_ITEM_SERIAL)
            .itemSupplierSerial(UPDATED_ITEM_SUPPLIER_SERIAL)
            .itemPromotionalPrice(UPDATED_ITEM_PROMOTIONAL_PRICE)
            .itemPromotionalPercentage(UPDATED_ITEM_PROMOTIONAL_PERCENTAGE)
            .itemCost(UPDATED_ITEM_COST)
            .originalStockDate(UPDATED_ORIGINAL_STOCK_DATE)
            .modifiedStockDate(UPDATED_MODIFIED_STOCK_DATE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        // Add required entity
        Model model;
        if (TestUtil.findAll(em, Model.class).isEmpty()) {
            model = ModelResourceIT.createUpdatedEntity(em);
            em.persist(model);
            em.flush();
        } else {
            model = TestUtil.findAll(em, Model.class).get(0);
        }
        items.setRelatedModel(model);
        // Add required entity
        Products products;
        if (TestUtil.findAll(em, Products.class).isEmpty()) {
            products = ProductsResourceIT.createUpdatedEntity(em);
            em.persist(products);
            em.flush();
        } else {
            products = TestUtil.findAll(em, Products.class).get(0);
        }
        items.setRelatedProduct(products);
        // Add required entity
        Location location;
        if (TestUtil.findAll(em, Location.class).isEmpty()) {
            location = LocationResourceIT.createUpdatedEntity(em);
            em.persist(location);
            em.flush();
        } else {
            location = TestUtil.findAll(em, Location.class).get(0);
        }
        items.setLocation(location);
        // Add required entity
        UnitOfMeasure unitOfMeasure;
        if (TestUtil.findAll(em, UnitOfMeasure.class).isEmpty()) {
            unitOfMeasure = UnitOfMeasureResourceIT.createUpdatedEntity(em);
            em.persist(unitOfMeasure);
            em.flush();
        } else {
            unitOfMeasure = TestUtil.findAll(em, UnitOfMeasure.class).get(0);
        }
        items.setUnitOfMeasure(unitOfMeasure);
        // Add required entity
        Currency currency;
        if (TestUtil.findAll(em, Currency.class).isEmpty()) {
            currency = CurrencyResourceIT.createUpdatedEntity(em);
            em.persist(currency);
            em.flush();
        } else {
            currency = TestUtil.findAll(em, Currency.class).get(0);
        }
        items.setCurrency(currency);
        return items;
    }

    @BeforeEach
    public void initTest() {
        items = createEntity(em);
    }

    @Test
    @Transactional
    public void createItems() throws Exception {
        int databaseSizeBeforeCreate = itemsRepository.findAll().size();

        // Create the Items
        restItemsMockMvc.perform(post("/api/items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(items)))
            .andExpect(status().isCreated());

        // Validate the Items in the database
        List<Items> itemsList = itemsRepository.findAll();
        assertThat(itemsList).hasSize(databaseSizeBeforeCreate + 1);
        Items testItems = itemsList.get(itemsList.size() - 1);
        assertThat(testItems.getItemCode()).isEqualTo(DEFAULT_ITEM_CODE);
        assertThat(testItems.getItemName()).isEqualTo(DEFAULT_ITEM_NAME);
        assertThat(testItems.getItemDescription()).isEqualTo(DEFAULT_ITEM_DESCRIPTION);
        assertThat(testItems.getItemPrice()).isEqualTo(DEFAULT_ITEM_PRICE);
        assertThat(testItems.getItemSerial()).isEqualTo(DEFAULT_ITEM_SERIAL);
        assertThat(testItems.getItemSupplierSerial()).isEqualTo(DEFAULT_ITEM_SUPPLIER_SERIAL);
        assertThat(testItems.getItemPromotionalPrice()).isEqualTo(DEFAULT_ITEM_PROMOTIONAL_PRICE);
        assertThat(testItems.getItemPromotionalPercentage()).isEqualTo(DEFAULT_ITEM_PROMOTIONAL_PERCENTAGE);
        assertThat(testItems.getItemCost()).isEqualTo(DEFAULT_ITEM_COST);
        assertThat(testItems.getOriginalStockDate()).isEqualTo(DEFAULT_ORIGINAL_STOCK_DATE);
        assertThat(testItems.getModifiedStockDate()).isEqualTo(DEFAULT_MODIFIED_STOCK_DATE);
        assertThat(testItems.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testItems.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createItemsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = itemsRepository.findAll().size();

        // Create the Items with an existing ID
        items.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restItemsMockMvc.perform(post("/api/items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(items)))
            .andExpect(status().isBadRequest());

        // Validate the Items in the database
        List<Items> itemsList = itemsRepository.findAll();
        assertThat(itemsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkItemCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemsRepository.findAll().size();
        // set the field null
        items.setItemCode(null);

        // Create the Items, which fails.

        restItemsMockMvc.perform(post("/api/items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(items)))
            .andExpect(status().isBadRequest());

        List<Items> itemsList = itemsRepository.findAll();
        assertThat(itemsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkItemNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemsRepository.findAll().size();
        // set the field null
        items.setItemName(null);

        // Create the Items, which fails.

        restItemsMockMvc.perform(post("/api/items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(items)))
            .andExpect(status().isBadRequest());

        List<Items> itemsList = itemsRepository.findAll();
        assertThat(itemsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkItemCostIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemsRepository.findAll().size();
        // set the field null
        items.setItemCost(null);

        // Create the Items, which fails.

        restItemsMockMvc.perform(post("/api/items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(items)))
            .andExpect(status().isBadRequest());

        List<Items> itemsList = itemsRepository.findAll();
        assertThat(itemsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOriginalStockDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemsRepository.findAll().size();
        // set the field null
        items.setOriginalStockDate(null);

        // Create the Items, which fails.

        restItemsMockMvc.perform(post("/api/items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(items)))
            .andExpect(status().isBadRequest());

        List<Items> itemsList = itemsRepository.findAll();
        assertThat(itemsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkModifiedStockDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemsRepository.findAll().size();
        // set the field null
        items.setModifiedStockDate(null);

        // Create the Items, which fails.

        restItemsMockMvc.perform(post("/api/items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(items)))
            .andExpect(status().isBadRequest());

        List<Items> itemsList = itemsRepository.findAll();
        assertThat(itemsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllItems() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList
        restItemsMockMvc.perform(get("/api/items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(items.getId().intValue())))
            .andExpect(jsonPath("$.[*].itemCode").value(hasItem(DEFAULT_ITEM_CODE)))
            .andExpect(jsonPath("$.[*].itemName").value(hasItem(DEFAULT_ITEM_NAME)))
            .andExpect(jsonPath("$.[*].itemDescription").value(hasItem(DEFAULT_ITEM_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].itemPrice").value(hasItem(DEFAULT_ITEM_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].itemSerial").value(hasItem(DEFAULT_ITEM_SERIAL)))
            .andExpect(jsonPath("$.[*].itemSupplierSerial").value(hasItem(DEFAULT_ITEM_SUPPLIER_SERIAL)))
            .andExpect(jsonPath("$.[*].itemPromotionalPrice").value(hasItem(DEFAULT_ITEM_PROMOTIONAL_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].itemPromotionalPercentage").value(hasItem(DEFAULT_ITEM_PROMOTIONAL_PERCENTAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].itemCost").value(hasItem(DEFAULT_ITEM_COST.intValue())))
            .andExpect(jsonPath("$.[*].originalStockDate").value(hasItem(DEFAULT_ORIGINAL_STOCK_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifiedStockDate").value(hasItem(DEFAULT_MODIFIED_STOCK_DATE.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllItemsWithEagerRelationshipsIsEnabled() throws Exception {
        ItemsResource itemsResource = new ItemsResource(itemsServiceMock, itemsQueryService);
        when(itemsServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restItemsMockMvc = MockMvcBuilders.standaloneSetup(itemsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restItemsMockMvc.perform(get("/api/items?eagerload=true"))
        .andExpect(status().isOk());

        verify(itemsServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllItemsWithEagerRelationshipsIsNotEnabled() throws Exception {
        ItemsResource itemsResource = new ItemsResource(itemsServiceMock, itemsQueryService);
            when(itemsServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restItemsMockMvc = MockMvcBuilders.standaloneSetup(itemsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restItemsMockMvc.perform(get("/api/items?eagerload=true"))
        .andExpect(status().isOk());

            verify(itemsServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getItems() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get the items
        restItemsMockMvc.perform(get("/api/items/{id}", items.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(items.getId().intValue()))
            .andExpect(jsonPath("$.itemCode").value(DEFAULT_ITEM_CODE))
            .andExpect(jsonPath("$.itemName").value(DEFAULT_ITEM_NAME))
            .andExpect(jsonPath("$.itemDescription").value(DEFAULT_ITEM_DESCRIPTION))
            .andExpect(jsonPath("$.itemPrice").value(DEFAULT_ITEM_PRICE.intValue()))
            .andExpect(jsonPath("$.itemSerial").value(DEFAULT_ITEM_SERIAL))
            .andExpect(jsonPath("$.itemSupplierSerial").value(DEFAULT_ITEM_SUPPLIER_SERIAL))
            .andExpect(jsonPath("$.itemPromotionalPrice").value(DEFAULT_ITEM_PROMOTIONAL_PRICE.intValue()))
            .andExpect(jsonPath("$.itemPromotionalPercentage").value(DEFAULT_ITEM_PROMOTIONAL_PERCENTAGE.doubleValue()))
            .andExpect(jsonPath("$.itemCost").value(DEFAULT_ITEM_COST.intValue()))
            .andExpect(jsonPath("$.originalStockDate").value(DEFAULT_ORIGINAL_STOCK_DATE.toString()))
            .andExpect(jsonPath("$.modifiedStockDate").value(DEFAULT_MODIFIED_STOCK_DATE.toString()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)));
    }


    @Test
    @Transactional
    public void getItemsByIdFiltering() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        Long id = items.getId();

        defaultItemsShouldBeFound("id.equals=" + id);
        defaultItemsShouldNotBeFound("id.notEquals=" + id);

        defaultItemsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultItemsShouldNotBeFound("id.greaterThan=" + id);

        defaultItemsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultItemsShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllItemsByItemCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemCode equals to DEFAULT_ITEM_CODE
        defaultItemsShouldBeFound("itemCode.equals=" + DEFAULT_ITEM_CODE);

        // Get all the itemsList where itemCode equals to UPDATED_ITEM_CODE
        defaultItemsShouldNotBeFound("itemCode.equals=" + UPDATED_ITEM_CODE);
    }

    @Test
    @Transactional
    public void getAllItemsByItemCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemCode not equals to DEFAULT_ITEM_CODE
        defaultItemsShouldNotBeFound("itemCode.notEquals=" + DEFAULT_ITEM_CODE);

        // Get all the itemsList where itemCode not equals to UPDATED_ITEM_CODE
        defaultItemsShouldBeFound("itemCode.notEquals=" + UPDATED_ITEM_CODE);
    }

    @Test
    @Transactional
    public void getAllItemsByItemCodeIsInShouldWork() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemCode in DEFAULT_ITEM_CODE or UPDATED_ITEM_CODE
        defaultItemsShouldBeFound("itemCode.in=" + DEFAULT_ITEM_CODE + "," + UPDATED_ITEM_CODE);

        // Get all the itemsList where itemCode equals to UPDATED_ITEM_CODE
        defaultItemsShouldNotBeFound("itemCode.in=" + UPDATED_ITEM_CODE);
    }

    @Test
    @Transactional
    public void getAllItemsByItemCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemCode is not null
        defaultItemsShouldBeFound("itemCode.specified=true");

        // Get all the itemsList where itemCode is null
        defaultItemsShouldNotBeFound("itemCode.specified=false");
    }
                @Test
    @Transactional
    public void getAllItemsByItemCodeContainsSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemCode contains DEFAULT_ITEM_CODE
        defaultItemsShouldBeFound("itemCode.contains=" + DEFAULT_ITEM_CODE);

        // Get all the itemsList where itemCode contains UPDATED_ITEM_CODE
        defaultItemsShouldNotBeFound("itemCode.contains=" + UPDATED_ITEM_CODE);
    }

    @Test
    @Transactional
    public void getAllItemsByItemCodeNotContainsSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemCode does not contain DEFAULT_ITEM_CODE
        defaultItemsShouldNotBeFound("itemCode.doesNotContain=" + DEFAULT_ITEM_CODE);

        // Get all the itemsList where itemCode does not contain UPDATED_ITEM_CODE
        defaultItemsShouldBeFound("itemCode.doesNotContain=" + UPDATED_ITEM_CODE);
    }


    @Test
    @Transactional
    public void getAllItemsByItemNameIsEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemName equals to DEFAULT_ITEM_NAME
        defaultItemsShouldBeFound("itemName.equals=" + DEFAULT_ITEM_NAME);

        // Get all the itemsList where itemName equals to UPDATED_ITEM_NAME
        defaultItemsShouldNotBeFound("itemName.equals=" + UPDATED_ITEM_NAME);
    }

    @Test
    @Transactional
    public void getAllItemsByItemNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemName not equals to DEFAULT_ITEM_NAME
        defaultItemsShouldNotBeFound("itemName.notEquals=" + DEFAULT_ITEM_NAME);

        // Get all the itemsList where itemName not equals to UPDATED_ITEM_NAME
        defaultItemsShouldBeFound("itemName.notEquals=" + UPDATED_ITEM_NAME);
    }

    @Test
    @Transactional
    public void getAllItemsByItemNameIsInShouldWork() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemName in DEFAULT_ITEM_NAME or UPDATED_ITEM_NAME
        defaultItemsShouldBeFound("itemName.in=" + DEFAULT_ITEM_NAME + "," + UPDATED_ITEM_NAME);

        // Get all the itemsList where itemName equals to UPDATED_ITEM_NAME
        defaultItemsShouldNotBeFound("itemName.in=" + UPDATED_ITEM_NAME);
    }

    @Test
    @Transactional
    public void getAllItemsByItemNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemName is not null
        defaultItemsShouldBeFound("itemName.specified=true");

        // Get all the itemsList where itemName is null
        defaultItemsShouldNotBeFound("itemName.specified=false");
    }
                @Test
    @Transactional
    public void getAllItemsByItemNameContainsSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemName contains DEFAULT_ITEM_NAME
        defaultItemsShouldBeFound("itemName.contains=" + DEFAULT_ITEM_NAME);

        // Get all the itemsList where itemName contains UPDATED_ITEM_NAME
        defaultItemsShouldNotBeFound("itemName.contains=" + UPDATED_ITEM_NAME);
    }

    @Test
    @Transactional
    public void getAllItemsByItemNameNotContainsSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemName does not contain DEFAULT_ITEM_NAME
        defaultItemsShouldNotBeFound("itemName.doesNotContain=" + DEFAULT_ITEM_NAME);

        // Get all the itemsList where itemName does not contain UPDATED_ITEM_NAME
        defaultItemsShouldBeFound("itemName.doesNotContain=" + UPDATED_ITEM_NAME);
    }


    @Test
    @Transactional
    public void getAllItemsByItemDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemDescription equals to DEFAULT_ITEM_DESCRIPTION
        defaultItemsShouldBeFound("itemDescription.equals=" + DEFAULT_ITEM_DESCRIPTION);

        // Get all the itemsList where itemDescription equals to UPDATED_ITEM_DESCRIPTION
        defaultItemsShouldNotBeFound("itemDescription.equals=" + UPDATED_ITEM_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllItemsByItemDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemDescription not equals to DEFAULT_ITEM_DESCRIPTION
        defaultItemsShouldNotBeFound("itemDescription.notEquals=" + DEFAULT_ITEM_DESCRIPTION);

        // Get all the itemsList where itemDescription not equals to UPDATED_ITEM_DESCRIPTION
        defaultItemsShouldBeFound("itemDescription.notEquals=" + UPDATED_ITEM_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllItemsByItemDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemDescription in DEFAULT_ITEM_DESCRIPTION or UPDATED_ITEM_DESCRIPTION
        defaultItemsShouldBeFound("itemDescription.in=" + DEFAULT_ITEM_DESCRIPTION + "," + UPDATED_ITEM_DESCRIPTION);

        // Get all the itemsList where itemDescription equals to UPDATED_ITEM_DESCRIPTION
        defaultItemsShouldNotBeFound("itemDescription.in=" + UPDATED_ITEM_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllItemsByItemDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemDescription is not null
        defaultItemsShouldBeFound("itemDescription.specified=true");

        // Get all the itemsList where itemDescription is null
        defaultItemsShouldNotBeFound("itemDescription.specified=false");
    }
                @Test
    @Transactional
    public void getAllItemsByItemDescriptionContainsSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemDescription contains DEFAULT_ITEM_DESCRIPTION
        defaultItemsShouldBeFound("itemDescription.contains=" + DEFAULT_ITEM_DESCRIPTION);

        // Get all the itemsList where itemDescription contains UPDATED_ITEM_DESCRIPTION
        defaultItemsShouldNotBeFound("itemDescription.contains=" + UPDATED_ITEM_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllItemsByItemDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemDescription does not contain DEFAULT_ITEM_DESCRIPTION
        defaultItemsShouldNotBeFound("itemDescription.doesNotContain=" + DEFAULT_ITEM_DESCRIPTION);

        // Get all the itemsList where itemDescription does not contain UPDATED_ITEM_DESCRIPTION
        defaultItemsShouldBeFound("itemDescription.doesNotContain=" + UPDATED_ITEM_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllItemsByItemPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemPrice equals to DEFAULT_ITEM_PRICE
        defaultItemsShouldBeFound("itemPrice.equals=" + DEFAULT_ITEM_PRICE);

        // Get all the itemsList where itemPrice equals to UPDATED_ITEM_PRICE
        defaultItemsShouldNotBeFound("itemPrice.equals=" + UPDATED_ITEM_PRICE);
    }

    @Test
    @Transactional
    public void getAllItemsByItemPriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemPrice not equals to DEFAULT_ITEM_PRICE
        defaultItemsShouldNotBeFound("itemPrice.notEquals=" + DEFAULT_ITEM_PRICE);

        // Get all the itemsList where itemPrice not equals to UPDATED_ITEM_PRICE
        defaultItemsShouldBeFound("itemPrice.notEquals=" + UPDATED_ITEM_PRICE);
    }

    @Test
    @Transactional
    public void getAllItemsByItemPriceIsInShouldWork() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemPrice in DEFAULT_ITEM_PRICE or UPDATED_ITEM_PRICE
        defaultItemsShouldBeFound("itemPrice.in=" + DEFAULT_ITEM_PRICE + "," + UPDATED_ITEM_PRICE);

        // Get all the itemsList where itemPrice equals to UPDATED_ITEM_PRICE
        defaultItemsShouldNotBeFound("itemPrice.in=" + UPDATED_ITEM_PRICE);
    }

    @Test
    @Transactional
    public void getAllItemsByItemPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemPrice is not null
        defaultItemsShouldBeFound("itemPrice.specified=true");

        // Get all the itemsList where itemPrice is null
        defaultItemsShouldNotBeFound("itemPrice.specified=false");
    }

    @Test
    @Transactional
    public void getAllItemsByItemPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemPrice is greater than or equal to DEFAULT_ITEM_PRICE
        defaultItemsShouldBeFound("itemPrice.greaterThanOrEqual=" + DEFAULT_ITEM_PRICE);

        // Get all the itemsList where itemPrice is greater than or equal to UPDATED_ITEM_PRICE
        defaultItemsShouldNotBeFound("itemPrice.greaterThanOrEqual=" + UPDATED_ITEM_PRICE);
    }

    @Test
    @Transactional
    public void getAllItemsByItemPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemPrice is less than or equal to DEFAULT_ITEM_PRICE
        defaultItemsShouldBeFound("itemPrice.lessThanOrEqual=" + DEFAULT_ITEM_PRICE);

        // Get all the itemsList where itemPrice is less than or equal to SMALLER_ITEM_PRICE
        defaultItemsShouldNotBeFound("itemPrice.lessThanOrEqual=" + SMALLER_ITEM_PRICE);
    }

    @Test
    @Transactional
    public void getAllItemsByItemPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemPrice is less than DEFAULT_ITEM_PRICE
        defaultItemsShouldNotBeFound("itemPrice.lessThan=" + DEFAULT_ITEM_PRICE);

        // Get all the itemsList where itemPrice is less than UPDATED_ITEM_PRICE
        defaultItemsShouldBeFound("itemPrice.lessThan=" + UPDATED_ITEM_PRICE);
    }

    @Test
    @Transactional
    public void getAllItemsByItemPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemPrice is greater than DEFAULT_ITEM_PRICE
        defaultItemsShouldNotBeFound("itemPrice.greaterThan=" + DEFAULT_ITEM_PRICE);

        // Get all the itemsList where itemPrice is greater than SMALLER_ITEM_PRICE
        defaultItemsShouldBeFound("itemPrice.greaterThan=" + SMALLER_ITEM_PRICE);
    }


    @Test
    @Transactional
    public void getAllItemsByItemSerialIsEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemSerial equals to DEFAULT_ITEM_SERIAL
        defaultItemsShouldBeFound("itemSerial.equals=" + DEFAULT_ITEM_SERIAL);

        // Get all the itemsList where itemSerial equals to UPDATED_ITEM_SERIAL
        defaultItemsShouldNotBeFound("itemSerial.equals=" + UPDATED_ITEM_SERIAL);
    }

    @Test
    @Transactional
    public void getAllItemsByItemSerialIsNotEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemSerial not equals to DEFAULT_ITEM_SERIAL
        defaultItemsShouldNotBeFound("itemSerial.notEquals=" + DEFAULT_ITEM_SERIAL);

        // Get all the itemsList where itemSerial not equals to UPDATED_ITEM_SERIAL
        defaultItemsShouldBeFound("itemSerial.notEquals=" + UPDATED_ITEM_SERIAL);
    }

    @Test
    @Transactional
    public void getAllItemsByItemSerialIsInShouldWork() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemSerial in DEFAULT_ITEM_SERIAL or UPDATED_ITEM_SERIAL
        defaultItemsShouldBeFound("itemSerial.in=" + DEFAULT_ITEM_SERIAL + "," + UPDATED_ITEM_SERIAL);

        // Get all the itemsList where itemSerial equals to UPDATED_ITEM_SERIAL
        defaultItemsShouldNotBeFound("itemSerial.in=" + UPDATED_ITEM_SERIAL);
    }

    @Test
    @Transactional
    public void getAllItemsByItemSerialIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemSerial is not null
        defaultItemsShouldBeFound("itemSerial.specified=true");

        // Get all the itemsList where itemSerial is null
        defaultItemsShouldNotBeFound("itemSerial.specified=false");
    }
                @Test
    @Transactional
    public void getAllItemsByItemSerialContainsSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemSerial contains DEFAULT_ITEM_SERIAL
        defaultItemsShouldBeFound("itemSerial.contains=" + DEFAULT_ITEM_SERIAL);

        // Get all the itemsList where itemSerial contains UPDATED_ITEM_SERIAL
        defaultItemsShouldNotBeFound("itemSerial.contains=" + UPDATED_ITEM_SERIAL);
    }

    @Test
    @Transactional
    public void getAllItemsByItemSerialNotContainsSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemSerial does not contain DEFAULT_ITEM_SERIAL
        defaultItemsShouldNotBeFound("itemSerial.doesNotContain=" + DEFAULT_ITEM_SERIAL);

        // Get all the itemsList where itemSerial does not contain UPDATED_ITEM_SERIAL
        defaultItemsShouldBeFound("itemSerial.doesNotContain=" + UPDATED_ITEM_SERIAL);
    }


    @Test
    @Transactional
    public void getAllItemsByItemSupplierSerialIsEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemSupplierSerial equals to DEFAULT_ITEM_SUPPLIER_SERIAL
        defaultItemsShouldBeFound("itemSupplierSerial.equals=" + DEFAULT_ITEM_SUPPLIER_SERIAL);

        // Get all the itemsList where itemSupplierSerial equals to UPDATED_ITEM_SUPPLIER_SERIAL
        defaultItemsShouldNotBeFound("itemSupplierSerial.equals=" + UPDATED_ITEM_SUPPLIER_SERIAL);
    }

    @Test
    @Transactional
    public void getAllItemsByItemSupplierSerialIsNotEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemSupplierSerial not equals to DEFAULT_ITEM_SUPPLIER_SERIAL
        defaultItemsShouldNotBeFound("itemSupplierSerial.notEquals=" + DEFAULT_ITEM_SUPPLIER_SERIAL);

        // Get all the itemsList where itemSupplierSerial not equals to UPDATED_ITEM_SUPPLIER_SERIAL
        defaultItemsShouldBeFound("itemSupplierSerial.notEquals=" + UPDATED_ITEM_SUPPLIER_SERIAL);
    }

    @Test
    @Transactional
    public void getAllItemsByItemSupplierSerialIsInShouldWork() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemSupplierSerial in DEFAULT_ITEM_SUPPLIER_SERIAL or UPDATED_ITEM_SUPPLIER_SERIAL
        defaultItemsShouldBeFound("itemSupplierSerial.in=" + DEFAULT_ITEM_SUPPLIER_SERIAL + "," + UPDATED_ITEM_SUPPLIER_SERIAL);

        // Get all the itemsList where itemSupplierSerial equals to UPDATED_ITEM_SUPPLIER_SERIAL
        defaultItemsShouldNotBeFound("itemSupplierSerial.in=" + UPDATED_ITEM_SUPPLIER_SERIAL);
    }

    @Test
    @Transactional
    public void getAllItemsByItemSupplierSerialIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemSupplierSerial is not null
        defaultItemsShouldBeFound("itemSupplierSerial.specified=true");

        // Get all the itemsList where itemSupplierSerial is null
        defaultItemsShouldNotBeFound("itemSupplierSerial.specified=false");
    }
                @Test
    @Transactional
    public void getAllItemsByItemSupplierSerialContainsSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemSupplierSerial contains DEFAULT_ITEM_SUPPLIER_SERIAL
        defaultItemsShouldBeFound("itemSupplierSerial.contains=" + DEFAULT_ITEM_SUPPLIER_SERIAL);

        // Get all the itemsList where itemSupplierSerial contains UPDATED_ITEM_SUPPLIER_SERIAL
        defaultItemsShouldNotBeFound("itemSupplierSerial.contains=" + UPDATED_ITEM_SUPPLIER_SERIAL);
    }

    @Test
    @Transactional
    public void getAllItemsByItemSupplierSerialNotContainsSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemSupplierSerial does not contain DEFAULT_ITEM_SUPPLIER_SERIAL
        defaultItemsShouldNotBeFound("itemSupplierSerial.doesNotContain=" + DEFAULT_ITEM_SUPPLIER_SERIAL);

        // Get all the itemsList where itemSupplierSerial does not contain UPDATED_ITEM_SUPPLIER_SERIAL
        defaultItemsShouldBeFound("itemSupplierSerial.doesNotContain=" + UPDATED_ITEM_SUPPLIER_SERIAL);
    }


    @Test
    @Transactional
    public void getAllItemsByItemPromotionalPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemPromotionalPrice equals to DEFAULT_ITEM_PROMOTIONAL_PRICE
        defaultItemsShouldBeFound("itemPromotionalPrice.equals=" + DEFAULT_ITEM_PROMOTIONAL_PRICE);

        // Get all the itemsList where itemPromotionalPrice equals to UPDATED_ITEM_PROMOTIONAL_PRICE
        defaultItemsShouldNotBeFound("itemPromotionalPrice.equals=" + UPDATED_ITEM_PROMOTIONAL_PRICE);
    }

    @Test
    @Transactional
    public void getAllItemsByItemPromotionalPriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemPromotionalPrice not equals to DEFAULT_ITEM_PROMOTIONAL_PRICE
        defaultItemsShouldNotBeFound("itemPromotionalPrice.notEquals=" + DEFAULT_ITEM_PROMOTIONAL_PRICE);

        // Get all the itemsList where itemPromotionalPrice not equals to UPDATED_ITEM_PROMOTIONAL_PRICE
        defaultItemsShouldBeFound("itemPromotionalPrice.notEquals=" + UPDATED_ITEM_PROMOTIONAL_PRICE);
    }

    @Test
    @Transactional
    public void getAllItemsByItemPromotionalPriceIsInShouldWork() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemPromotionalPrice in DEFAULT_ITEM_PROMOTIONAL_PRICE or UPDATED_ITEM_PROMOTIONAL_PRICE
        defaultItemsShouldBeFound("itemPromotionalPrice.in=" + DEFAULT_ITEM_PROMOTIONAL_PRICE + "," + UPDATED_ITEM_PROMOTIONAL_PRICE);

        // Get all the itemsList where itemPromotionalPrice equals to UPDATED_ITEM_PROMOTIONAL_PRICE
        defaultItemsShouldNotBeFound("itemPromotionalPrice.in=" + UPDATED_ITEM_PROMOTIONAL_PRICE);
    }

    @Test
    @Transactional
    public void getAllItemsByItemPromotionalPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemPromotionalPrice is not null
        defaultItemsShouldBeFound("itemPromotionalPrice.specified=true");

        // Get all the itemsList where itemPromotionalPrice is null
        defaultItemsShouldNotBeFound("itemPromotionalPrice.specified=false");
    }

    @Test
    @Transactional
    public void getAllItemsByItemPromotionalPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemPromotionalPrice is greater than or equal to DEFAULT_ITEM_PROMOTIONAL_PRICE
        defaultItemsShouldBeFound("itemPromotionalPrice.greaterThanOrEqual=" + DEFAULT_ITEM_PROMOTIONAL_PRICE);

        // Get all the itemsList where itemPromotionalPrice is greater than or equal to UPDATED_ITEM_PROMOTIONAL_PRICE
        defaultItemsShouldNotBeFound("itemPromotionalPrice.greaterThanOrEqual=" + UPDATED_ITEM_PROMOTIONAL_PRICE);
    }

    @Test
    @Transactional
    public void getAllItemsByItemPromotionalPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemPromotionalPrice is less than or equal to DEFAULT_ITEM_PROMOTIONAL_PRICE
        defaultItemsShouldBeFound("itemPromotionalPrice.lessThanOrEqual=" + DEFAULT_ITEM_PROMOTIONAL_PRICE);

        // Get all the itemsList where itemPromotionalPrice is less than or equal to SMALLER_ITEM_PROMOTIONAL_PRICE
        defaultItemsShouldNotBeFound("itemPromotionalPrice.lessThanOrEqual=" + SMALLER_ITEM_PROMOTIONAL_PRICE);
    }

    @Test
    @Transactional
    public void getAllItemsByItemPromotionalPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemPromotionalPrice is less than DEFAULT_ITEM_PROMOTIONAL_PRICE
        defaultItemsShouldNotBeFound("itemPromotionalPrice.lessThan=" + DEFAULT_ITEM_PROMOTIONAL_PRICE);

        // Get all the itemsList where itemPromotionalPrice is less than UPDATED_ITEM_PROMOTIONAL_PRICE
        defaultItemsShouldBeFound("itemPromotionalPrice.lessThan=" + UPDATED_ITEM_PROMOTIONAL_PRICE);
    }

    @Test
    @Transactional
    public void getAllItemsByItemPromotionalPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemPromotionalPrice is greater than DEFAULT_ITEM_PROMOTIONAL_PRICE
        defaultItemsShouldNotBeFound("itemPromotionalPrice.greaterThan=" + DEFAULT_ITEM_PROMOTIONAL_PRICE);

        // Get all the itemsList where itemPromotionalPrice is greater than SMALLER_ITEM_PROMOTIONAL_PRICE
        defaultItemsShouldBeFound("itemPromotionalPrice.greaterThan=" + SMALLER_ITEM_PROMOTIONAL_PRICE);
    }


    @Test
    @Transactional
    public void getAllItemsByItemPromotionalPercentageIsEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemPromotionalPercentage equals to DEFAULT_ITEM_PROMOTIONAL_PERCENTAGE
        defaultItemsShouldBeFound("itemPromotionalPercentage.equals=" + DEFAULT_ITEM_PROMOTIONAL_PERCENTAGE);

        // Get all the itemsList where itemPromotionalPercentage equals to UPDATED_ITEM_PROMOTIONAL_PERCENTAGE
        defaultItemsShouldNotBeFound("itemPromotionalPercentage.equals=" + UPDATED_ITEM_PROMOTIONAL_PERCENTAGE);
    }

    @Test
    @Transactional
    public void getAllItemsByItemPromotionalPercentageIsNotEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemPromotionalPercentage not equals to DEFAULT_ITEM_PROMOTIONAL_PERCENTAGE
        defaultItemsShouldNotBeFound("itemPromotionalPercentage.notEquals=" + DEFAULT_ITEM_PROMOTIONAL_PERCENTAGE);

        // Get all the itemsList where itemPromotionalPercentage not equals to UPDATED_ITEM_PROMOTIONAL_PERCENTAGE
        defaultItemsShouldBeFound("itemPromotionalPercentage.notEquals=" + UPDATED_ITEM_PROMOTIONAL_PERCENTAGE);
    }

    @Test
    @Transactional
    public void getAllItemsByItemPromotionalPercentageIsInShouldWork() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemPromotionalPercentage in DEFAULT_ITEM_PROMOTIONAL_PERCENTAGE or UPDATED_ITEM_PROMOTIONAL_PERCENTAGE
        defaultItemsShouldBeFound("itemPromotionalPercentage.in=" + DEFAULT_ITEM_PROMOTIONAL_PERCENTAGE + "," + UPDATED_ITEM_PROMOTIONAL_PERCENTAGE);

        // Get all the itemsList where itemPromotionalPercentage equals to UPDATED_ITEM_PROMOTIONAL_PERCENTAGE
        defaultItemsShouldNotBeFound("itemPromotionalPercentage.in=" + UPDATED_ITEM_PROMOTIONAL_PERCENTAGE);
    }

    @Test
    @Transactional
    public void getAllItemsByItemPromotionalPercentageIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemPromotionalPercentage is not null
        defaultItemsShouldBeFound("itemPromotionalPercentage.specified=true");

        // Get all the itemsList where itemPromotionalPercentage is null
        defaultItemsShouldNotBeFound("itemPromotionalPercentage.specified=false");
    }

    @Test
    @Transactional
    public void getAllItemsByItemPromotionalPercentageIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemPromotionalPercentage is greater than or equal to DEFAULT_ITEM_PROMOTIONAL_PERCENTAGE
        defaultItemsShouldBeFound("itemPromotionalPercentage.greaterThanOrEqual=" + DEFAULT_ITEM_PROMOTIONAL_PERCENTAGE);

        // Get all the itemsList where itemPromotionalPercentage is greater than or equal to UPDATED_ITEM_PROMOTIONAL_PERCENTAGE
        defaultItemsShouldNotBeFound("itemPromotionalPercentage.greaterThanOrEqual=" + UPDATED_ITEM_PROMOTIONAL_PERCENTAGE);
    }

    @Test
    @Transactional
    public void getAllItemsByItemPromotionalPercentageIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemPromotionalPercentage is less than or equal to DEFAULT_ITEM_PROMOTIONAL_PERCENTAGE
        defaultItemsShouldBeFound("itemPromotionalPercentage.lessThanOrEqual=" + DEFAULT_ITEM_PROMOTIONAL_PERCENTAGE);

        // Get all the itemsList where itemPromotionalPercentage is less than or equal to SMALLER_ITEM_PROMOTIONAL_PERCENTAGE
        defaultItemsShouldNotBeFound("itemPromotionalPercentage.lessThanOrEqual=" + SMALLER_ITEM_PROMOTIONAL_PERCENTAGE);
    }

    @Test
    @Transactional
    public void getAllItemsByItemPromotionalPercentageIsLessThanSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemPromotionalPercentage is less than DEFAULT_ITEM_PROMOTIONAL_PERCENTAGE
        defaultItemsShouldNotBeFound("itemPromotionalPercentage.lessThan=" + DEFAULT_ITEM_PROMOTIONAL_PERCENTAGE);

        // Get all the itemsList where itemPromotionalPercentage is less than UPDATED_ITEM_PROMOTIONAL_PERCENTAGE
        defaultItemsShouldBeFound("itemPromotionalPercentage.lessThan=" + UPDATED_ITEM_PROMOTIONAL_PERCENTAGE);
    }

    @Test
    @Transactional
    public void getAllItemsByItemPromotionalPercentageIsGreaterThanSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemPromotionalPercentage is greater than DEFAULT_ITEM_PROMOTIONAL_PERCENTAGE
        defaultItemsShouldNotBeFound("itemPromotionalPercentage.greaterThan=" + DEFAULT_ITEM_PROMOTIONAL_PERCENTAGE);

        // Get all the itemsList where itemPromotionalPercentage is greater than SMALLER_ITEM_PROMOTIONAL_PERCENTAGE
        defaultItemsShouldBeFound("itemPromotionalPercentage.greaterThan=" + SMALLER_ITEM_PROMOTIONAL_PERCENTAGE);
    }


    @Test
    @Transactional
    public void getAllItemsByItemCostIsEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemCost equals to DEFAULT_ITEM_COST
        defaultItemsShouldBeFound("itemCost.equals=" + DEFAULT_ITEM_COST);

        // Get all the itemsList where itemCost equals to UPDATED_ITEM_COST
        defaultItemsShouldNotBeFound("itemCost.equals=" + UPDATED_ITEM_COST);
    }

    @Test
    @Transactional
    public void getAllItemsByItemCostIsNotEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemCost not equals to DEFAULT_ITEM_COST
        defaultItemsShouldNotBeFound("itemCost.notEquals=" + DEFAULT_ITEM_COST);

        // Get all the itemsList where itemCost not equals to UPDATED_ITEM_COST
        defaultItemsShouldBeFound("itemCost.notEquals=" + UPDATED_ITEM_COST);
    }

    @Test
    @Transactional
    public void getAllItemsByItemCostIsInShouldWork() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemCost in DEFAULT_ITEM_COST or UPDATED_ITEM_COST
        defaultItemsShouldBeFound("itemCost.in=" + DEFAULT_ITEM_COST + "," + UPDATED_ITEM_COST);

        // Get all the itemsList where itemCost equals to UPDATED_ITEM_COST
        defaultItemsShouldNotBeFound("itemCost.in=" + UPDATED_ITEM_COST);
    }

    @Test
    @Transactional
    public void getAllItemsByItemCostIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemCost is not null
        defaultItemsShouldBeFound("itemCost.specified=true");

        // Get all the itemsList where itemCost is null
        defaultItemsShouldNotBeFound("itemCost.specified=false");
    }

    @Test
    @Transactional
    public void getAllItemsByItemCostIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemCost is greater than or equal to DEFAULT_ITEM_COST
        defaultItemsShouldBeFound("itemCost.greaterThanOrEqual=" + DEFAULT_ITEM_COST);

        // Get all the itemsList where itemCost is greater than or equal to UPDATED_ITEM_COST
        defaultItemsShouldNotBeFound("itemCost.greaterThanOrEqual=" + UPDATED_ITEM_COST);
    }

    @Test
    @Transactional
    public void getAllItemsByItemCostIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemCost is less than or equal to DEFAULT_ITEM_COST
        defaultItemsShouldBeFound("itemCost.lessThanOrEqual=" + DEFAULT_ITEM_COST);

        // Get all the itemsList where itemCost is less than or equal to SMALLER_ITEM_COST
        defaultItemsShouldNotBeFound("itemCost.lessThanOrEqual=" + SMALLER_ITEM_COST);
    }

    @Test
    @Transactional
    public void getAllItemsByItemCostIsLessThanSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemCost is less than DEFAULT_ITEM_COST
        defaultItemsShouldNotBeFound("itemCost.lessThan=" + DEFAULT_ITEM_COST);

        // Get all the itemsList where itemCost is less than UPDATED_ITEM_COST
        defaultItemsShouldBeFound("itemCost.lessThan=" + UPDATED_ITEM_COST);
    }

    @Test
    @Transactional
    public void getAllItemsByItemCostIsGreaterThanSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemCost is greater than DEFAULT_ITEM_COST
        defaultItemsShouldNotBeFound("itemCost.greaterThan=" + DEFAULT_ITEM_COST);

        // Get all the itemsList where itemCost is greater than SMALLER_ITEM_COST
        defaultItemsShouldBeFound("itemCost.greaterThan=" + SMALLER_ITEM_COST);
    }


    @Test
    @Transactional
    public void getAllItemsByOriginalStockDateIsEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where originalStockDate equals to DEFAULT_ORIGINAL_STOCK_DATE
        defaultItemsShouldBeFound("originalStockDate.equals=" + DEFAULT_ORIGINAL_STOCK_DATE);

        // Get all the itemsList where originalStockDate equals to UPDATED_ORIGINAL_STOCK_DATE
        defaultItemsShouldNotBeFound("originalStockDate.equals=" + UPDATED_ORIGINAL_STOCK_DATE);
    }

    @Test
    @Transactional
    public void getAllItemsByOriginalStockDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where originalStockDate not equals to DEFAULT_ORIGINAL_STOCK_DATE
        defaultItemsShouldNotBeFound("originalStockDate.notEquals=" + DEFAULT_ORIGINAL_STOCK_DATE);

        // Get all the itemsList where originalStockDate not equals to UPDATED_ORIGINAL_STOCK_DATE
        defaultItemsShouldBeFound("originalStockDate.notEquals=" + UPDATED_ORIGINAL_STOCK_DATE);
    }

    @Test
    @Transactional
    public void getAllItemsByOriginalStockDateIsInShouldWork() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where originalStockDate in DEFAULT_ORIGINAL_STOCK_DATE or UPDATED_ORIGINAL_STOCK_DATE
        defaultItemsShouldBeFound("originalStockDate.in=" + DEFAULT_ORIGINAL_STOCK_DATE + "," + UPDATED_ORIGINAL_STOCK_DATE);

        // Get all the itemsList where originalStockDate equals to UPDATED_ORIGINAL_STOCK_DATE
        defaultItemsShouldNotBeFound("originalStockDate.in=" + UPDATED_ORIGINAL_STOCK_DATE);
    }

    @Test
    @Transactional
    public void getAllItemsByOriginalStockDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where originalStockDate is not null
        defaultItemsShouldBeFound("originalStockDate.specified=true");

        // Get all the itemsList where originalStockDate is null
        defaultItemsShouldNotBeFound("originalStockDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllItemsByOriginalStockDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where originalStockDate is greater than or equal to DEFAULT_ORIGINAL_STOCK_DATE
        defaultItemsShouldBeFound("originalStockDate.greaterThanOrEqual=" + DEFAULT_ORIGINAL_STOCK_DATE);

        // Get all the itemsList where originalStockDate is greater than or equal to UPDATED_ORIGINAL_STOCK_DATE
        defaultItemsShouldNotBeFound("originalStockDate.greaterThanOrEqual=" + UPDATED_ORIGINAL_STOCK_DATE);
    }

    @Test
    @Transactional
    public void getAllItemsByOriginalStockDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where originalStockDate is less than or equal to DEFAULT_ORIGINAL_STOCK_DATE
        defaultItemsShouldBeFound("originalStockDate.lessThanOrEqual=" + DEFAULT_ORIGINAL_STOCK_DATE);

        // Get all the itemsList where originalStockDate is less than or equal to SMALLER_ORIGINAL_STOCK_DATE
        defaultItemsShouldNotBeFound("originalStockDate.lessThanOrEqual=" + SMALLER_ORIGINAL_STOCK_DATE);
    }

    @Test
    @Transactional
    public void getAllItemsByOriginalStockDateIsLessThanSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where originalStockDate is less than DEFAULT_ORIGINAL_STOCK_DATE
        defaultItemsShouldNotBeFound("originalStockDate.lessThan=" + DEFAULT_ORIGINAL_STOCK_DATE);

        // Get all the itemsList where originalStockDate is less than UPDATED_ORIGINAL_STOCK_DATE
        defaultItemsShouldBeFound("originalStockDate.lessThan=" + UPDATED_ORIGINAL_STOCK_DATE);
    }

    @Test
    @Transactional
    public void getAllItemsByOriginalStockDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where originalStockDate is greater than DEFAULT_ORIGINAL_STOCK_DATE
        defaultItemsShouldNotBeFound("originalStockDate.greaterThan=" + DEFAULT_ORIGINAL_STOCK_DATE);

        // Get all the itemsList where originalStockDate is greater than SMALLER_ORIGINAL_STOCK_DATE
        defaultItemsShouldBeFound("originalStockDate.greaterThan=" + SMALLER_ORIGINAL_STOCK_DATE);
    }


    @Test
    @Transactional
    public void getAllItemsByModifiedStockDateIsEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where modifiedStockDate equals to DEFAULT_MODIFIED_STOCK_DATE
        defaultItemsShouldBeFound("modifiedStockDate.equals=" + DEFAULT_MODIFIED_STOCK_DATE);

        // Get all the itemsList where modifiedStockDate equals to UPDATED_MODIFIED_STOCK_DATE
        defaultItemsShouldNotBeFound("modifiedStockDate.equals=" + UPDATED_MODIFIED_STOCK_DATE);
    }

    @Test
    @Transactional
    public void getAllItemsByModifiedStockDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where modifiedStockDate not equals to DEFAULT_MODIFIED_STOCK_DATE
        defaultItemsShouldNotBeFound("modifiedStockDate.notEquals=" + DEFAULT_MODIFIED_STOCK_DATE);

        // Get all the itemsList where modifiedStockDate not equals to UPDATED_MODIFIED_STOCK_DATE
        defaultItemsShouldBeFound("modifiedStockDate.notEquals=" + UPDATED_MODIFIED_STOCK_DATE);
    }

    @Test
    @Transactional
    public void getAllItemsByModifiedStockDateIsInShouldWork() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where modifiedStockDate in DEFAULT_MODIFIED_STOCK_DATE or UPDATED_MODIFIED_STOCK_DATE
        defaultItemsShouldBeFound("modifiedStockDate.in=" + DEFAULT_MODIFIED_STOCK_DATE + "," + UPDATED_MODIFIED_STOCK_DATE);

        // Get all the itemsList where modifiedStockDate equals to UPDATED_MODIFIED_STOCK_DATE
        defaultItemsShouldNotBeFound("modifiedStockDate.in=" + UPDATED_MODIFIED_STOCK_DATE);
    }

    @Test
    @Transactional
    public void getAllItemsByModifiedStockDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where modifiedStockDate is not null
        defaultItemsShouldBeFound("modifiedStockDate.specified=true");

        // Get all the itemsList where modifiedStockDate is null
        defaultItemsShouldNotBeFound("modifiedStockDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllItemsByModifiedStockDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where modifiedStockDate is greater than or equal to DEFAULT_MODIFIED_STOCK_DATE
        defaultItemsShouldBeFound("modifiedStockDate.greaterThanOrEqual=" + DEFAULT_MODIFIED_STOCK_DATE);

        // Get all the itemsList where modifiedStockDate is greater than or equal to UPDATED_MODIFIED_STOCK_DATE
        defaultItemsShouldNotBeFound("modifiedStockDate.greaterThanOrEqual=" + UPDATED_MODIFIED_STOCK_DATE);
    }

    @Test
    @Transactional
    public void getAllItemsByModifiedStockDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where modifiedStockDate is less than or equal to DEFAULT_MODIFIED_STOCK_DATE
        defaultItemsShouldBeFound("modifiedStockDate.lessThanOrEqual=" + DEFAULT_MODIFIED_STOCK_DATE);

        // Get all the itemsList where modifiedStockDate is less than or equal to SMALLER_MODIFIED_STOCK_DATE
        defaultItemsShouldNotBeFound("modifiedStockDate.lessThanOrEqual=" + SMALLER_MODIFIED_STOCK_DATE);
    }

    @Test
    @Transactional
    public void getAllItemsByModifiedStockDateIsLessThanSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where modifiedStockDate is less than DEFAULT_MODIFIED_STOCK_DATE
        defaultItemsShouldNotBeFound("modifiedStockDate.lessThan=" + DEFAULT_MODIFIED_STOCK_DATE);

        // Get all the itemsList where modifiedStockDate is less than UPDATED_MODIFIED_STOCK_DATE
        defaultItemsShouldBeFound("modifiedStockDate.lessThan=" + UPDATED_MODIFIED_STOCK_DATE);
    }

    @Test
    @Transactional
    public void getAllItemsByModifiedStockDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where modifiedStockDate is greater than DEFAULT_MODIFIED_STOCK_DATE
        defaultItemsShouldNotBeFound("modifiedStockDate.greaterThan=" + DEFAULT_MODIFIED_STOCK_DATE);

        // Get all the itemsList where modifiedStockDate is greater than SMALLER_MODIFIED_STOCK_DATE
        defaultItemsShouldBeFound("modifiedStockDate.greaterThan=" + SMALLER_MODIFIED_STOCK_DATE);
    }


    @Test
    @Transactional
    public void getAllItemsByHistoryIsEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);
        DocumentHistory history = DocumentHistoryResourceIT.createEntity(em);
        em.persist(history);
        em.flush();
        items.setHistory(history);
        itemsRepository.saveAndFlush(items);
        Long historyId = history.getId();

        // Get all the itemsList where history equals to historyId
        defaultItemsShouldBeFound("historyId.equals=" + historyId);

        // Get all the itemsList where history equals to historyId + 1
        defaultItemsShouldNotBeFound("historyId.equals=" + (historyId + 1));
    }


    @Test
    @Transactional
    public void getAllItemsByRelatedModelIsEqualToSomething() throws Exception {
        // Get already existing entity
        Model relatedModel = items.getRelatedModel();
        itemsRepository.saveAndFlush(items);
        Long relatedModelId = relatedModel.getId();

        // Get all the itemsList where relatedModel equals to relatedModelId
        defaultItemsShouldBeFound("relatedModelId.equals=" + relatedModelId);

        // Get all the itemsList where relatedModel equals to relatedModelId + 1
        defaultItemsShouldNotBeFound("relatedModelId.equals=" + (relatedModelId + 1));
    }


    @Test
    @Transactional
    public void getAllItemsByRelatedProductIsEqualToSomething() throws Exception {
        // Get already existing entity
        Products relatedProduct = items.getRelatedProduct();
        itemsRepository.saveAndFlush(items);
        Long relatedProductId = relatedProduct.getId();

        // Get all the itemsList where relatedProduct equals to relatedProductId
        defaultItemsShouldBeFound("relatedProductId.equals=" + relatedProductId);

        // Get all the itemsList where relatedProduct equals to relatedProductId + 1
        defaultItemsShouldNotBeFound("relatedProductId.equals=" + (relatedProductId + 1));
    }


    @Test
    @Transactional
    public void getAllItemsByLocationIsEqualToSomething() throws Exception {
        // Get already existing entity
        Location location = items.getLocation();
        itemsRepository.saveAndFlush(items);
        Long locationId = location.getId();

        // Get all the itemsList where location equals to locationId
        defaultItemsShouldBeFound("locationId.equals=" + locationId);

        // Get all the itemsList where location equals to locationId + 1
        defaultItemsShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }


    @Test
    @Transactional
    public void getAllItemsByUnitOfMeasureIsEqualToSomething() throws Exception {
        // Get already existing entity
        UnitOfMeasure unitOfMeasure = items.getUnitOfMeasure();
        itemsRepository.saveAndFlush(items);
        Long unitOfMeasureId = unitOfMeasure.getId();

        // Get all the itemsList where unitOfMeasure equals to unitOfMeasureId
        defaultItemsShouldBeFound("unitOfMeasureId.equals=" + unitOfMeasureId);

        // Get all the itemsList where unitOfMeasure equals to unitOfMeasureId + 1
        defaultItemsShouldNotBeFound("unitOfMeasureId.equals=" + (unitOfMeasureId + 1));
    }


    @Test
    @Transactional
    public void getAllItemsByCurrencyIsEqualToSomething() throws Exception {
        // Get already existing entity
        Currency currency = items.getCurrency();
        itemsRepository.saveAndFlush(items);
        Long currencyId = currency.getId();

        // Get all the itemsList where currency equals to currencyId
        defaultItemsShouldBeFound("currencyId.equals=" + currencyId);

        // Get all the itemsList where currency equals to currencyId + 1
        defaultItemsShouldNotBeFound("currencyId.equals=" + (currencyId + 1));
    }


    @Test
    @Transactional
    public void getAllItemsByAddonsIsEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);
        ItemAddOns addons = ItemAddOnsResourceIT.createEntity(em);
        em.persist(addons);
        em.flush();
        items.addAddons(addons);
        itemsRepository.saveAndFlush(items);
        Long addonsId = addons.getId();

        // Get all the itemsList where addons equals to addonsId
        defaultItemsShouldBeFound("addonsId.equals=" + addonsId);

        // Get all the itemsList where addons equals to addonsId + 1
        defaultItemsShouldNotBeFound("addonsId.equals=" + (addonsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultItemsShouldBeFound(String filter) throws Exception {
        restItemsMockMvc.perform(get("/api/items?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(items.getId().intValue())))
            .andExpect(jsonPath("$.[*].itemCode").value(hasItem(DEFAULT_ITEM_CODE)))
            .andExpect(jsonPath("$.[*].itemName").value(hasItem(DEFAULT_ITEM_NAME)))
            .andExpect(jsonPath("$.[*].itemDescription").value(hasItem(DEFAULT_ITEM_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].itemPrice").value(hasItem(DEFAULT_ITEM_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].itemSerial").value(hasItem(DEFAULT_ITEM_SERIAL)))
            .andExpect(jsonPath("$.[*].itemSupplierSerial").value(hasItem(DEFAULT_ITEM_SUPPLIER_SERIAL)))
            .andExpect(jsonPath("$.[*].itemPromotionalPrice").value(hasItem(DEFAULT_ITEM_PROMOTIONAL_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].itemPromotionalPercentage").value(hasItem(DEFAULT_ITEM_PROMOTIONAL_PERCENTAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].itemCost").value(hasItem(DEFAULT_ITEM_COST.intValue())))
            .andExpect(jsonPath("$.[*].originalStockDate").value(hasItem(DEFAULT_ORIGINAL_STOCK_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifiedStockDate").value(hasItem(DEFAULT_MODIFIED_STOCK_DATE.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));

        // Check, that the count call also returns 1
        restItemsMockMvc.perform(get("/api/items/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultItemsShouldNotBeFound(String filter) throws Exception {
        restItemsMockMvc.perform(get("/api/items?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restItemsMockMvc.perform(get("/api/items/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingItems() throws Exception {
        // Get the items
        restItemsMockMvc.perform(get("/api/items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateItems() throws Exception {
        // Initialize the database
        itemsService.save(items);

        int databaseSizeBeforeUpdate = itemsRepository.findAll().size();

        // Update the items
        Items updatedItems = itemsRepository.findById(items.getId()).get();
        // Disconnect from session so that the updates on updatedItems are not directly saved in db
        em.detach(updatedItems);
        updatedItems
            .itemCode(UPDATED_ITEM_CODE)
            .itemName(UPDATED_ITEM_NAME)
            .itemDescription(UPDATED_ITEM_DESCRIPTION)
            .itemPrice(UPDATED_ITEM_PRICE)
            .itemSerial(UPDATED_ITEM_SERIAL)
            .itemSupplierSerial(UPDATED_ITEM_SUPPLIER_SERIAL)
            .itemPromotionalPrice(UPDATED_ITEM_PROMOTIONAL_PRICE)
            .itemPromotionalPercentage(UPDATED_ITEM_PROMOTIONAL_PERCENTAGE)
            .itemCost(UPDATED_ITEM_COST)
            .originalStockDate(UPDATED_ORIGINAL_STOCK_DATE)
            .modifiedStockDate(UPDATED_MODIFIED_STOCK_DATE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restItemsMockMvc.perform(put("/api/items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedItems)))
            .andExpect(status().isOk());

        // Validate the Items in the database
        List<Items> itemsList = itemsRepository.findAll();
        assertThat(itemsList).hasSize(databaseSizeBeforeUpdate);
        Items testItems = itemsList.get(itemsList.size() - 1);
        assertThat(testItems.getItemCode()).isEqualTo(UPDATED_ITEM_CODE);
        assertThat(testItems.getItemName()).isEqualTo(UPDATED_ITEM_NAME);
        assertThat(testItems.getItemDescription()).isEqualTo(UPDATED_ITEM_DESCRIPTION);
        assertThat(testItems.getItemPrice()).isEqualTo(UPDATED_ITEM_PRICE);
        assertThat(testItems.getItemSerial()).isEqualTo(UPDATED_ITEM_SERIAL);
        assertThat(testItems.getItemSupplierSerial()).isEqualTo(UPDATED_ITEM_SUPPLIER_SERIAL);
        assertThat(testItems.getItemPromotionalPrice()).isEqualTo(UPDATED_ITEM_PROMOTIONAL_PRICE);
        assertThat(testItems.getItemPromotionalPercentage()).isEqualTo(UPDATED_ITEM_PROMOTIONAL_PERCENTAGE);
        assertThat(testItems.getItemCost()).isEqualTo(UPDATED_ITEM_COST);
        assertThat(testItems.getOriginalStockDate()).isEqualTo(UPDATED_ORIGINAL_STOCK_DATE);
        assertThat(testItems.getModifiedStockDate()).isEqualTo(UPDATED_MODIFIED_STOCK_DATE);
        assertThat(testItems.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testItems.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingItems() throws Exception {
        int databaseSizeBeforeUpdate = itemsRepository.findAll().size();

        // Create the Items

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemsMockMvc.perform(put("/api/items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(items)))
            .andExpect(status().isBadRequest());

        // Validate the Items in the database
        List<Items> itemsList = itemsRepository.findAll();
        assertThat(itemsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteItems() throws Exception {
        // Initialize the database
        itemsService.save(items);

        int databaseSizeBeforeDelete = itemsRepository.findAll().size();

        // Delete the items
        restItemsMockMvc.perform(delete("/api/items/{id}", items.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Items> itemsList = itemsRepository.findAll();
        assertThat(itemsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

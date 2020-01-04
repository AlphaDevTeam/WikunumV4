package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.WikunumApp;
import com.alphadevs.sales.domain.StockTransfer;
import com.alphadevs.sales.domain.Items;
import com.alphadevs.sales.domain.Location;
import com.alphadevs.sales.repository.StockTransferRepository;
import com.alphadevs.sales.service.StockTransferService;
import com.alphadevs.sales.web.rest.errors.ExceptionTranslator;
import com.alphadevs.sales.service.dto.StockTransferCriteria;
import com.alphadevs.sales.service.StockTransferQueryService;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.alphadevs.sales.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link StockTransferResource} REST controller.
 */
@SpringBootTest(classes = WikunumApp.class)
public class StockTransferResourceIT {

    private static final String DEFAULT_TRANSACTION_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_TRANSACTION_NUMBER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_TRANSACTION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TRANSACTION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_TRANSACTION_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_TRANSACTION_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_TRANSACTION_DESCRIPTION = "BBBBBBBBBB";

    private static final Double DEFAULT_TRANSACTION_QTY = 1D;
    private static final Double UPDATED_TRANSACTION_QTY = 2D;
    private static final Double SMALLER_TRANSACTION_QTY = 1D - 1D;

    @Autowired
    private StockTransferRepository stockTransferRepository;

    @Autowired
    private StockTransferService stockTransferService;

    @Autowired
    private StockTransferQueryService stockTransferQueryService;

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

    private MockMvc restStockTransferMockMvc;

    private StockTransfer stockTransfer;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final StockTransferResource stockTransferResource = new StockTransferResource(stockTransferService, stockTransferQueryService);
        this.restStockTransferMockMvc = MockMvcBuilders.standaloneSetup(stockTransferResource)
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
    public static StockTransfer createEntity(EntityManager em) {
        StockTransfer stockTransfer = new StockTransfer()
            .transactionNumber(DEFAULT_TRANSACTION_NUMBER)
            .transactionDate(DEFAULT_TRANSACTION_DATE)
            .transactionDescription(DEFAULT_TRANSACTION_DESCRIPTION)
            .transactionQty(DEFAULT_TRANSACTION_QTY);
        // Add required entity
        Items items;
        if (TestUtil.findAll(em, Items.class).isEmpty()) {
            items = ItemsResourceIT.createEntity(em);
            em.persist(items);
            em.flush();
        } else {
            items = TestUtil.findAll(em, Items.class).get(0);
        }
        stockTransfer.setItem(items);
        // Add required entity
        Location location;
        if (TestUtil.findAll(em, Location.class).isEmpty()) {
            location = LocationResourceIT.createEntity(em);
            em.persist(location);
            em.flush();
        } else {
            location = TestUtil.findAll(em, Location.class).get(0);
        }
        stockTransfer.setLocationFrom(location);
        // Add required entity
        stockTransfer.setLocationTo(location);
        return stockTransfer;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StockTransfer createUpdatedEntity(EntityManager em) {
        StockTransfer stockTransfer = new StockTransfer()
            .transactionNumber(UPDATED_TRANSACTION_NUMBER)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .transactionDescription(UPDATED_TRANSACTION_DESCRIPTION)
            .transactionQty(UPDATED_TRANSACTION_QTY);
        // Add required entity
        Items items;
        if (TestUtil.findAll(em, Items.class).isEmpty()) {
            items = ItemsResourceIT.createUpdatedEntity(em);
            em.persist(items);
            em.flush();
        } else {
            items = TestUtil.findAll(em, Items.class).get(0);
        }
        stockTransfer.setItem(items);
        // Add required entity
        Location location;
        if (TestUtil.findAll(em, Location.class).isEmpty()) {
            location = LocationResourceIT.createUpdatedEntity(em);
            em.persist(location);
            em.flush();
        } else {
            location = TestUtil.findAll(em, Location.class).get(0);
        }
        stockTransfer.setLocationFrom(location);
        // Add required entity
        stockTransfer.setLocationTo(location);
        return stockTransfer;
    }

    @BeforeEach
    public void initTest() {
        stockTransfer = createEntity(em);
    }

    @Test
    @Transactional
    public void createStockTransfer() throws Exception {
        int databaseSizeBeforeCreate = stockTransferRepository.findAll().size();

        // Create the StockTransfer
        restStockTransferMockMvc.perform(post("/api/stock-transfers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockTransfer)))
            .andExpect(status().isCreated());

        // Validate the StockTransfer in the database
        List<StockTransfer> stockTransferList = stockTransferRepository.findAll();
        assertThat(stockTransferList).hasSize(databaseSizeBeforeCreate + 1);
        StockTransfer testStockTransfer = stockTransferList.get(stockTransferList.size() - 1);
        assertThat(testStockTransfer.getTransactionNumber()).isEqualTo(DEFAULT_TRANSACTION_NUMBER);
        assertThat(testStockTransfer.getTransactionDate()).isEqualTo(DEFAULT_TRANSACTION_DATE);
        assertThat(testStockTransfer.getTransactionDescription()).isEqualTo(DEFAULT_TRANSACTION_DESCRIPTION);
        assertThat(testStockTransfer.getTransactionQty()).isEqualTo(DEFAULT_TRANSACTION_QTY);
    }

    @Test
    @Transactional
    public void createStockTransferWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = stockTransferRepository.findAll().size();

        // Create the StockTransfer with an existing ID
        stockTransfer.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStockTransferMockMvc.perform(post("/api/stock-transfers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockTransfer)))
            .andExpect(status().isBadRequest());

        // Validate the StockTransfer in the database
        List<StockTransfer> stockTransferList = stockTransferRepository.findAll();
        assertThat(stockTransferList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTransactionNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockTransferRepository.findAll().size();
        // set the field null
        stockTransfer.setTransactionNumber(null);

        // Create the StockTransfer, which fails.

        restStockTransferMockMvc.perform(post("/api/stock-transfers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockTransfer)))
            .andExpect(status().isBadRequest());

        List<StockTransfer> stockTransferList = stockTransferRepository.findAll();
        assertThat(stockTransferList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTransactionDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockTransferRepository.findAll().size();
        // set the field null
        stockTransfer.setTransactionDate(null);

        // Create the StockTransfer, which fails.

        restStockTransferMockMvc.perform(post("/api/stock-transfers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockTransfer)))
            .andExpect(status().isBadRequest());

        List<StockTransfer> stockTransferList = stockTransferRepository.findAll();
        assertThat(stockTransferList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTransactionDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockTransferRepository.findAll().size();
        // set the field null
        stockTransfer.setTransactionDescription(null);

        // Create the StockTransfer, which fails.

        restStockTransferMockMvc.perform(post("/api/stock-transfers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockTransfer)))
            .andExpect(status().isBadRequest());

        List<StockTransfer> stockTransferList = stockTransferRepository.findAll();
        assertThat(stockTransferList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTransactionQtyIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockTransferRepository.findAll().size();
        // set the field null
        stockTransfer.setTransactionQty(null);

        // Create the StockTransfer, which fails.

        restStockTransferMockMvc.perform(post("/api/stock-transfers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockTransfer)))
            .andExpect(status().isBadRequest());

        List<StockTransfer> stockTransferList = stockTransferRepository.findAll();
        assertThat(stockTransferList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStockTransfers() throws Exception {
        // Initialize the database
        stockTransferRepository.saveAndFlush(stockTransfer);

        // Get all the stockTransferList
        restStockTransferMockMvc.perform(get("/api/stock-transfers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockTransfer.getId().intValue())))
            .andExpect(jsonPath("$.[*].transactionNumber").value(hasItem(DEFAULT_TRANSACTION_NUMBER)))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].transactionDescription").value(hasItem(DEFAULT_TRANSACTION_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].transactionQty").value(hasItem(DEFAULT_TRANSACTION_QTY.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getStockTransfer() throws Exception {
        // Initialize the database
        stockTransferRepository.saveAndFlush(stockTransfer);

        // Get the stockTransfer
        restStockTransferMockMvc.perform(get("/api/stock-transfers/{id}", stockTransfer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(stockTransfer.getId().intValue()))
            .andExpect(jsonPath("$.transactionNumber").value(DEFAULT_TRANSACTION_NUMBER))
            .andExpect(jsonPath("$.transactionDate").value(DEFAULT_TRANSACTION_DATE.toString()))
            .andExpect(jsonPath("$.transactionDescription").value(DEFAULT_TRANSACTION_DESCRIPTION))
            .andExpect(jsonPath("$.transactionQty").value(DEFAULT_TRANSACTION_QTY.doubleValue()));
    }


    @Test
    @Transactional
    public void getStockTransfersByIdFiltering() throws Exception {
        // Initialize the database
        stockTransferRepository.saveAndFlush(stockTransfer);

        Long id = stockTransfer.getId();

        defaultStockTransferShouldBeFound("id.equals=" + id);
        defaultStockTransferShouldNotBeFound("id.notEquals=" + id);

        defaultStockTransferShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultStockTransferShouldNotBeFound("id.greaterThan=" + id);

        defaultStockTransferShouldBeFound("id.lessThanOrEqual=" + id);
        defaultStockTransferShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllStockTransfersByTransactionNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        stockTransferRepository.saveAndFlush(stockTransfer);

        // Get all the stockTransferList where transactionNumber equals to DEFAULT_TRANSACTION_NUMBER
        defaultStockTransferShouldBeFound("transactionNumber.equals=" + DEFAULT_TRANSACTION_NUMBER);

        // Get all the stockTransferList where transactionNumber equals to UPDATED_TRANSACTION_NUMBER
        defaultStockTransferShouldNotBeFound("transactionNumber.equals=" + UPDATED_TRANSACTION_NUMBER);
    }

    @Test
    @Transactional
    public void getAllStockTransfersByTransactionNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        stockTransferRepository.saveAndFlush(stockTransfer);

        // Get all the stockTransferList where transactionNumber not equals to DEFAULT_TRANSACTION_NUMBER
        defaultStockTransferShouldNotBeFound("transactionNumber.notEquals=" + DEFAULT_TRANSACTION_NUMBER);

        // Get all the stockTransferList where transactionNumber not equals to UPDATED_TRANSACTION_NUMBER
        defaultStockTransferShouldBeFound("transactionNumber.notEquals=" + UPDATED_TRANSACTION_NUMBER);
    }

    @Test
    @Transactional
    public void getAllStockTransfersByTransactionNumberIsInShouldWork() throws Exception {
        // Initialize the database
        stockTransferRepository.saveAndFlush(stockTransfer);

        // Get all the stockTransferList where transactionNumber in DEFAULT_TRANSACTION_NUMBER or UPDATED_TRANSACTION_NUMBER
        defaultStockTransferShouldBeFound("transactionNumber.in=" + DEFAULT_TRANSACTION_NUMBER + "," + UPDATED_TRANSACTION_NUMBER);

        // Get all the stockTransferList where transactionNumber equals to UPDATED_TRANSACTION_NUMBER
        defaultStockTransferShouldNotBeFound("transactionNumber.in=" + UPDATED_TRANSACTION_NUMBER);
    }

    @Test
    @Transactional
    public void getAllStockTransfersByTransactionNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockTransferRepository.saveAndFlush(stockTransfer);

        // Get all the stockTransferList where transactionNumber is not null
        defaultStockTransferShouldBeFound("transactionNumber.specified=true");

        // Get all the stockTransferList where transactionNumber is null
        defaultStockTransferShouldNotBeFound("transactionNumber.specified=false");
    }
                @Test
    @Transactional
    public void getAllStockTransfersByTransactionNumberContainsSomething() throws Exception {
        // Initialize the database
        stockTransferRepository.saveAndFlush(stockTransfer);

        // Get all the stockTransferList where transactionNumber contains DEFAULT_TRANSACTION_NUMBER
        defaultStockTransferShouldBeFound("transactionNumber.contains=" + DEFAULT_TRANSACTION_NUMBER);

        // Get all the stockTransferList where transactionNumber contains UPDATED_TRANSACTION_NUMBER
        defaultStockTransferShouldNotBeFound("transactionNumber.contains=" + UPDATED_TRANSACTION_NUMBER);
    }

    @Test
    @Transactional
    public void getAllStockTransfersByTransactionNumberNotContainsSomething() throws Exception {
        // Initialize the database
        stockTransferRepository.saveAndFlush(stockTransfer);

        // Get all the stockTransferList where transactionNumber does not contain DEFAULT_TRANSACTION_NUMBER
        defaultStockTransferShouldNotBeFound("transactionNumber.doesNotContain=" + DEFAULT_TRANSACTION_NUMBER);

        // Get all the stockTransferList where transactionNumber does not contain UPDATED_TRANSACTION_NUMBER
        defaultStockTransferShouldBeFound("transactionNumber.doesNotContain=" + UPDATED_TRANSACTION_NUMBER);
    }


    @Test
    @Transactional
    public void getAllStockTransfersByTransactionDateIsEqualToSomething() throws Exception {
        // Initialize the database
        stockTransferRepository.saveAndFlush(stockTransfer);

        // Get all the stockTransferList where transactionDate equals to DEFAULT_TRANSACTION_DATE
        defaultStockTransferShouldBeFound("transactionDate.equals=" + DEFAULT_TRANSACTION_DATE);

        // Get all the stockTransferList where transactionDate equals to UPDATED_TRANSACTION_DATE
        defaultStockTransferShouldNotBeFound("transactionDate.equals=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllStockTransfersByTransactionDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        stockTransferRepository.saveAndFlush(stockTransfer);

        // Get all the stockTransferList where transactionDate not equals to DEFAULT_TRANSACTION_DATE
        defaultStockTransferShouldNotBeFound("transactionDate.notEquals=" + DEFAULT_TRANSACTION_DATE);

        // Get all the stockTransferList where transactionDate not equals to UPDATED_TRANSACTION_DATE
        defaultStockTransferShouldBeFound("transactionDate.notEquals=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllStockTransfersByTransactionDateIsInShouldWork() throws Exception {
        // Initialize the database
        stockTransferRepository.saveAndFlush(stockTransfer);

        // Get all the stockTransferList where transactionDate in DEFAULT_TRANSACTION_DATE or UPDATED_TRANSACTION_DATE
        defaultStockTransferShouldBeFound("transactionDate.in=" + DEFAULT_TRANSACTION_DATE + "," + UPDATED_TRANSACTION_DATE);

        // Get all the stockTransferList where transactionDate equals to UPDATED_TRANSACTION_DATE
        defaultStockTransferShouldNotBeFound("transactionDate.in=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllStockTransfersByTransactionDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockTransferRepository.saveAndFlush(stockTransfer);

        // Get all the stockTransferList where transactionDate is not null
        defaultStockTransferShouldBeFound("transactionDate.specified=true");

        // Get all the stockTransferList where transactionDate is null
        defaultStockTransferShouldNotBeFound("transactionDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllStockTransfersByTransactionDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        stockTransferRepository.saveAndFlush(stockTransfer);

        // Get all the stockTransferList where transactionDate is greater than or equal to DEFAULT_TRANSACTION_DATE
        defaultStockTransferShouldBeFound("transactionDate.greaterThanOrEqual=" + DEFAULT_TRANSACTION_DATE);

        // Get all the stockTransferList where transactionDate is greater than or equal to UPDATED_TRANSACTION_DATE
        defaultStockTransferShouldNotBeFound("transactionDate.greaterThanOrEqual=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllStockTransfersByTransactionDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        stockTransferRepository.saveAndFlush(stockTransfer);

        // Get all the stockTransferList where transactionDate is less than or equal to DEFAULT_TRANSACTION_DATE
        defaultStockTransferShouldBeFound("transactionDate.lessThanOrEqual=" + DEFAULT_TRANSACTION_DATE);

        // Get all the stockTransferList where transactionDate is less than or equal to SMALLER_TRANSACTION_DATE
        defaultStockTransferShouldNotBeFound("transactionDate.lessThanOrEqual=" + SMALLER_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllStockTransfersByTransactionDateIsLessThanSomething() throws Exception {
        // Initialize the database
        stockTransferRepository.saveAndFlush(stockTransfer);

        // Get all the stockTransferList where transactionDate is less than DEFAULT_TRANSACTION_DATE
        defaultStockTransferShouldNotBeFound("transactionDate.lessThan=" + DEFAULT_TRANSACTION_DATE);

        // Get all the stockTransferList where transactionDate is less than UPDATED_TRANSACTION_DATE
        defaultStockTransferShouldBeFound("transactionDate.lessThan=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllStockTransfersByTransactionDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        stockTransferRepository.saveAndFlush(stockTransfer);

        // Get all the stockTransferList where transactionDate is greater than DEFAULT_TRANSACTION_DATE
        defaultStockTransferShouldNotBeFound("transactionDate.greaterThan=" + DEFAULT_TRANSACTION_DATE);

        // Get all the stockTransferList where transactionDate is greater than SMALLER_TRANSACTION_DATE
        defaultStockTransferShouldBeFound("transactionDate.greaterThan=" + SMALLER_TRANSACTION_DATE);
    }


    @Test
    @Transactional
    public void getAllStockTransfersByTransactionDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        stockTransferRepository.saveAndFlush(stockTransfer);

        // Get all the stockTransferList where transactionDescription equals to DEFAULT_TRANSACTION_DESCRIPTION
        defaultStockTransferShouldBeFound("transactionDescription.equals=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the stockTransferList where transactionDescription equals to UPDATED_TRANSACTION_DESCRIPTION
        defaultStockTransferShouldNotBeFound("transactionDescription.equals=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllStockTransfersByTransactionDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        stockTransferRepository.saveAndFlush(stockTransfer);

        // Get all the stockTransferList where transactionDescription not equals to DEFAULT_TRANSACTION_DESCRIPTION
        defaultStockTransferShouldNotBeFound("transactionDescription.notEquals=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the stockTransferList where transactionDescription not equals to UPDATED_TRANSACTION_DESCRIPTION
        defaultStockTransferShouldBeFound("transactionDescription.notEquals=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllStockTransfersByTransactionDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        stockTransferRepository.saveAndFlush(stockTransfer);

        // Get all the stockTransferList where transactionDescription in DEFAULT_TRANSACTION_DESCRIPTION or UPDATED_TRANSACTION_DESCRIPTION
        defaultStockTransferShouldBeFound("transactionDescription.in=" + DEFAULT_TRANSACTION_DESCRIPTION + "," + UPDATED_TRANSACTION_DESCRIPTION);

        // Get all the stockTransferList where transactionDescription equals to UPDATED_TRANSACTION_DESCRIPTION
        defaultStockTransferShouldNotBeFound("transactionDescription.in=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllStockTransfersByTransactionDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockTransferRepository.saveAndFlush(stockTransfer);

        // Get all the stockTransferList where transactionDescription is not null
        defaultStockTransferShouldBeFound("transactionDescription.specified=true");

        // Get all the stockTransferList where transactionDescription is null
        defaultStockTransferShouldNotBeFound("transactionDescription.specified=false");
    }
                @Test
    @Transactional
    public void getAllStockTransfersByTransactionDescriptionContainsSomething() throws Exception {
        // Initialize the database
        stockTransferRepository.saveAndFlush(stockTransfer);

        // Get all the stockTransferList where transactionDescription contains DEFAULT_TRANSACTION_DESCRIPTION
        defaultStockTransferShouldBeFound("transactionDescription.contains=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the stockTransferList where transactionDescription contains UPDATED_TRANSACTION_DESCRIPTION
        defaultStockTransferShouldNotBeFound("transactionDescription.contains=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllStockTransfersByTransactionDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        stockTransferRepository.saveAndFlush(stockTransfer);

        // Get all the stockTransferList where transactionDescription does not contain DEFAULT_TRANSACTION_DESCRIPTION
        defaultStockTransferShouldNotBeFound("transactionDescription.doesNotContain=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the stockTransferList where transactionDescription does not contain UPDATED_TRANSACTION_DESCRIPTION
        defaultStockTransferShouldBeFound("transactionDescription.doesNotContain=" + UPDATED_TRANSACTION_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllStockTransfersByTransactionQtyIsEqualToSomething() throws Exception {
        // Initialize the database
        stockTransferRepository.saveAndFlush(stockTransfer);

        // Get all the stockTransferList where transactionQty equals to DEFAULT_TRANSACTION_QTY
        defaultStockTransferShouldBeFound("transactionQty.equals=" + DEFAULT_TRANSACTION_QTY);

        // Get all the stockTransferList where transactionQty equals to UPDATED_TRANSACTION_QTY
        defaultStockTransferShouldNotBeFound("transactionQty.equals=" + UPDATED_TRANSACTION_QTY);
    }

    @Test
    @Transactional
    public void getAllStockTransfersByTransactionQtyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        stockTransferRepository.saveAndFlush(stockTransfer);

        // Get all the stockTransferList where transactionQty not equals to DEFAULT_TRANSACTION_QTY
        defaultStockTransferShouldNotBeFound("transactionQty.notEquals=" + DEFAULT_TRANSACTION_QTY);

        // Get all the stockTransferList where transactionQty not equals to UPDATED_TRANSACTION_QTY
        defaultStockTransferShouldBeFound("transactionQty.notEquals=" + UPDATED_TRANSACTION_QTY);
    }

    @Test
    @Transactional
    public void getAllStockTransfersByTransactionQtyIsInShouldWork() throws Exception {
        // Initialize the database
        stockTransferRepository.saveAndFlush(stockTransfer);

        // Get all the stockTransferList where transactionQty in DEFAULT_TRANSACTION_QTY or UPDATED_TRANSACTION_QTY
        defaultStockTransferShouldBeFound("transactionQty.in=" + DEFAULT_TRANSACTION_QTY + "," + UPDATED_TRANSACTION_QTY);

        // Get all the stockTransferList where transactionQty equals to UPDATED_TRANSACTION_QTY
        defaultStockTransferShouldNotBeFound("transactionQty.in=" + UPDATED_TRANSACTION_QTY);
    }

    @Test
    @Transactional
    public void getAllStockTransfersByTransactionQtyIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockTransferRepository.saveAndFlush(stockTransfer);

        // Get all the stockTransferList where transactionQty is not null
        defaultStockTransferShouldBeFound("transactionQty.specified=true");

        // Get all the stockTransferList where transactionQty is null
        defaultStockTransferShouldNotBeFound("transactionQty.specified=false");
    }

    @Test
    @Transactional
    public void getAllStockTransfersByTransactionQtyIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        stockTransferRepository.saveAndFlush(stockTransfer);

        // Get all the stockTransferList where transactionQty is greater than or equal to DEFAULT_TRANSACTION_QTY
        defaultStockTransferShouldBeFound("transactionQty.greaterThanOrEqual=" + DEFAULT_TRANSACTION_QTY);

        // Get all the stockTransferList where transactionQty is greater than or equal to UPDATED_TRANSACTION_QTY
        defaultStockTransferShouldNotBeFound("transactionQty.greaterThanOrEqual=" + UPDATED_TRANSACTION_QTY);
    }

    @Test
    @Transactional
    public void getAllStockTransfersByTransactionQtyIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        stockTransferRepository.saveAndFlush(stockTransfer);

        // Get all the stockTransferList where transactionQty is less than or equal to DEFAULT_TRANSACTION_QTY
        defaultStockTransferShouldBeFound("transactionQty.lessThanOrEqual=" + DEFAULT_TRANSACTION_QTY);

        // Get all the stockTransferList where transactionQty is less than or equal to SMALLER_TRANSACTION_QTY
        defaultStockTransferShouldNotBeFound("transactionQty.lessThanOrEqual=" + SMALLER_TRANSACTION_QTY);
    }

    @Test
    @Transactional
    public void getAllStockTransfersByTransactionQtyIsLessThanSomething() throws Exception {
        // Initialize the database
        stockTransferRepository.saveAndFlush(stockTransfer);

        // Get all the stockTransferList where transactionQty is less than DEFAULT_TRANSACTION_QTY
        defaultStockTransferShouldNotBeFound("transactionQty.lessThan=" + DEFAULT_TRANSACTION_QTY);

        // Get all the stockTransferList where transactionQty is less than UPDATED_TRANSACTION_QTY
        defaultStockTransferShouldBeFound("transactionQty.lessThan=" + UPDATED_TRANSACTION_QTY);
    }

    @Test
    @Transactional
    public void getAllStockTransfersByTransactionQtyIsGreaterThanSomething() throws Exception {
        // Initialize the database
        stockTransferRepository.saveAndFlush(stockTransfer);

        // Get all the stockTransferList where transactionQty is greater than DEFAULT_TRANSACTION_QTY
        defaultStockTransferShouldNotBeFound("transactionQty.greaterThan=" + DEFAULT_TRANSACTION_QTY);

        // Get all the stockTransferList where transactionQty is greater than SMALLER_TRANSACTION_QTY
        defaultStockTransferShouldBeFound("transactionQty.greaterThan=" + SMALLER_TRANSACTION_QTY);
    }


    @Test
    @Transactional
    public void getAllStockTransfersByItemIsEqualToSomething() throws Exception {
        // Get already existing entity
        Items item = stockTransfer.getItem();
        stockTransferRepository.saveAndFlush(stockTransfer);
        Long itemId = item.getId();

        // Get all the stockTransferList where item equals to itemId
        defaultStockTransferShouldBeFound("itemId.equals=" + itemId);

        // Get all the stockTransferList where item equals to itemId + 1
        defaultStockTransferShouldNotBeFound("itemId.equals=" + (itemId + 1));
    }


    @Test
    @Transactional
    public void getAllStockTransfersByLocationFromIsEqualToSomething() throws Exception {
        // Get already existing entity
        Location locationFrom = stockTransfer.getLocationFrom();
        stockTransferRepository.saveAndFlush(stockTransfer);
        Long locationFromId = locationFrom.getId();

        // Get all the stockTransferList where locationFrom equals to locationFromId
        defaultStockTransferShouldBeFound("locationFromId.equals=" + locationFromId);

        // Get all the stockTransferList where locationFrom equals to locationFromId + 1
        defaultStockTransferShouldNotBeFound("locationFromId.equals=" + (locationFromId + 1));
    }


    @Test
    @Transactional
    public void getAllStockTransfersByLocationToIsEqualToSomething() throws Exception {
        // Get already existing entity
        Location locationTo = stockTransfer.getLocationTo();
        stockTransferRepository.saveAndFlush(stockTransfer);
        Long locationToId = locationTo.getId();

        // Get all the stockTransferList where locationTo equals to locationToId
        defaultStockTransferShouldBeFound("locationToId.equals=" + locationToId);

        // Get all the stockTransferList where locationTo equals to locationToId + 1
        defaultStockTransferShouldNotBeFound("locationToId.equals=" + (locationToId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultStockTransferShouldBeFound(String filter) throws Exception {
        restStockTransferMockMvc.perform(get("/api/stock-transfers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockTransfer.getId().intValue())))
            .andExpect(jsonPath("$.[*].transactionNumber").value(hasItem(DEFAULT_TRANSACTION_NUMBER)))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].transactionDescription").value(hasItem(DEFAULT_TRANSACTION_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].transactionQty").value(hasItem(DEFAULT_TRANSACTION_QTY.doubleValue())));

        // Check, that the count call also returns 1
        restStockTransferMockMvc.perform(get("/api/stock-transfers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultStockTransferShouldNotBeFound(String filter) throws Exception {
        restStockTransferMockMvc.perform(get("/api/stock-transfers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStockTransferMockMvc.perform(get("/api/stock-transfers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingStockTransfer() throws Exception {
        // Get the stockTransfer
        restStockTransferMockMvc.perform(get("/api/stock-transfers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStockTransfer() throws Exception {
        // Initialize the database
        stockTransferService.save(stockTransfer);

        int databaseSizeBeforeUpdate = stockTransferRepository.findAll().size();

        // Update the stockTransfer
        StockTransfer updatedStockTransfer = stockTransferRepository.findById(stockTransfer.getId()).get();
        // Disconnect from session so that the updates on updatedStockTransfer are not directly saved in db
        em.detach(updatedStockTransfer);
        updatedStockTransfer
            .transactionNumber(UPDATED_TRANSACTION_NUMBER)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .transactionDescription(UPDATED_TRANSACTION_DESCRIPTION)
            .transactionQty(UPDATED_TRANSACTION_QTY);

        restStockTransferMockMvc.perform(put("/api/stock-transfers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedStockTransfer)))
            .andExpect(status().isOk());

        // Validate the StockTransfer in the database
        List<StockTransfer> stockTransferList = stockTransferRepository.findAll();
        assertThat(stockTransferList).hasSize(databaseSizeBeforeUpdate);
        StockTransfer testStockTransfer = stockTransferList.get(stockTransferList.size() - 1);
        assertThat(testStockTransfer.getTransactionNumber()).isEqualTo(UPDATED_TRANSACTION_NUMBER);
        assertThat(testStockTransfer.getTransactionDate()).isEqualTo(UPDATED_TRANSACTION_DATE);
        assertThat(testStockTransfer.getTransactionDescription()).isEqualTo(UPDATED_TRANSACTION_DESCRIPTION);
        assertThat(testStockTransfer.getTransactionQty()).isEqualTo(UPDATED_TRANSACTION_QTY);
    }

    @Test
    @Transactional
    public void updateNonExistingStockTransfer() throws Exception {
        int databaseSizeBeforeUpdate = stockTransferRepository.findAll().size();

        // Create the StockTransfer

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStockTransferMockMvc.perform(put("/api/stock-transfers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockTransfer)))
            .andExpect(status().isBadRequest());

        // Validate the StockTransfer in the database
        List<StockTransfer> stockTransferList = stockTransferRepository.findAll();
        assertThat(stockTransferList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteStockTransfer() throws Exception {
        // Initialize the database
        stockTransferService.save(stockTransfer);

        int databaseSizeBeforeDelete = stockTransferRepository.findAll().size();

        // Delete the stockTransfer
        restStockTransferMockMvc.perform(delete("/api/stock-transfers/{id}", stockTransfer.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<StockTransfer> stockTransferList = stockTransferRepository.findAll();
        assertThat(stockTransferList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

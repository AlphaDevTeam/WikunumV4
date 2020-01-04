package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.WikunumApp;
import com.alphadevs.sales.domain.GoodsReceipt;
import com.alphadevs.sales.domain.GoodsReceiptDetails;
import com.alphadevs.sales.domain.PurchaseOrder;
import com.alphadevs.sales.domain.Supplier;
import com.alphadevs.sales.domain.Location;
import com.alphadevs.sales.domain.TransactionType;
import com.alphadevs.sales.repository.GoodsReceiptRepository;
import com.alphadevs.sales.service.GoodsReceiptService;
import com.alphadevs.sales.web.rest.errors.ExceptionTranslator;
import com.alphadevs.sales.service.dto.GoodsReceiptCriteria;
import com.alphadevs.sales.service.GoodsReceiptQueryService;

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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.alphadevs.sales.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link GoodsReceiptResource} REST controller.
 */
@SpringBootTest(classes = WikunumApp.class)
public class GoodsReceiptResourceIT {

    private static final String DEFAULT_GRN_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_GRN_NUMBER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_GRN_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_GRN_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_GRN_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_PO_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PO_NUMBER = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_GRN_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_GRN_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_GRN_AMOUNT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_CASH_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_CASH_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_CASH_AMOUNT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_CARD_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_CARD_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_CARD_AMOUNT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_DUE_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_DUE_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_DUE_AMOUNT = new BigDecimal(1 - 1);

    @Autowired
    private GoodsReceiptRepository goodsReceiptRepository;

    @Autowired
    private GoodsReceiptService goodsReceiptService;

    @Autowired
    private GoodsReceiptQueryService goodsReceiptQueryService;

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

    private MockMvc restGoodsReceiptMockMvc;

    private GoodsReceipt goodsReceipt;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GoodsReceiptResource goodsReceiptResource = new GoodsReceiptResource(goodsReceiptService, goodsReceiptQueryService);
        this.restGoodsReceiptMockMvc = MockMvcBuilders.standaloneSetup(goodsReceiptResource)
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
    public static GoodsReceipt createEntity(EntityManager em) {
        GoodsReceipt goodsReceipt = new GoodsReceipt()
            .grnNumber(DEFAULT_GRN_NUMBER)
            .grnDate(DEFAULT_GRN_DATE)
            .poNumber(DEFAULT_PO_NUMBER)
            .grnAmount(DEFAULT_GRN_AMOUNT)
            .cashAmount(DEFAULT_CASH_AMOUNT)
            .cardAmount(DEFAULT_CARD_AMOUNT)
            .dueAmount(DEFAULT_DUE_AMOUNT);
        // Add required entity
        GoodsReceiptDetails goodsReceiptDetails;
        if (TestUtil.findAll(em, GoodsReceiptDetails.class).isEmpty()) {
            goodsReceiptDetails = GoodsReceiptDetailsResourceIT.createEntity(em);
            em.persist(goodsReceiptDetails);
            em.flush();
        } else {
            goodsReceiptDetails = TestUtil.findAll(em, GoodsReceiptDetails.class).get(0);
        }
        goodsReceipt.getDetails().add(goodsReceiptDetails);
        // Add required entity
        PurchaseOrder purchaseOrder;
        if (TestUtil.findAll(em, PurchaseOrder.class).isEmpty()) {
            purchaseOrder = PurchaseOrderResourceIT.createEntity(em);
            em.persist(purchaseOrder);
            em.flush();
        } else {
            purchaseOrder = TestUtil.findAll(em, PurchaseOrder.class).get(0);
        }
        goodsReceipt.getLinkedPOs().add(purchaseOrder);
        // Add required entity
        Supplier supplier;
        if (TestUtil.findAll(em, Supplier.class).isEmpty()) {
            supplier = SupplierResourceIT.createEntity(em);
            em.persist(supplier);
            em.flush();
        } else {
            supplier = TestUtil.findAll(em, Supplier.class).get(0);
        }
        goodsReceipt.setSupplier(supplier);
        // Add required entity
        Location location;
        if (TestUtil.findAll(em, Location.class).isEmpty()) {
            location = LocationResourceIT.createEntity(em);
            em.persist(location);
            em.flush();
        } else {
            location = TestUtil.findAll(em, Location.class).get(0);
        }
        goodsReceipt.setLocation(location);
        // Add required entity
        TransactionType transactionType;
        if (TestUtil.findAll(em, TransactionType.class).isEmpty()) {
            transactionType = TransactionTypeResourceIT.createEntity(em);
            em.persist(transactionType);
            em.flush();
        } else {
            transactionType = TestUtil.findAll(em, TransactionType.class).get(0);
        }
        goodsReceipt.setTransactionType(transactionType);
        return goodsReceipt;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GoodsReceipt createUpdatedEntity(EntityManager em) {
        GoodsReceipt goodsReceipt = new GoodsReceipt()
            .grnNumber(UPDATED_GRN_NUMBER)
            .grnDate(UPDATED_GRN_DATE)
            .poNumber(UPDATED_PO_NUMBER)
            .grnAmount(UPDATED_GRN_AMOUNT)
            .cashAmount(UPDATED_CASH_AMOUNT)
            .cardAmount(UPDATED_CARD_AMOUNT)
            .dueAmount(UPDATED_DUE_AMOUNT);
        // Add required entity
        GoodsReceiptDetails goodsReceiptDetails;
        if (TestUtil.findAll(em, GoodsReceiptDetails.class).isEmpty()) {
            goodsReceiptDetails = GoodsReceiptDetailsResourceIT.createUpdatedEntity(em);
            em.persist(goodsReceiptDetails);
            em.flush();
        } else {
            goodsReceiptDetails = TestUtil.findAll(em, GoodsReceiptDetails.class).get(0);
        }
        goodsReceipt.getDetails().add(goodsReceiptDetails);
        // Add required entity
        PurchaseOrder purchaseOrder;
        if (TestUtil.findAll(em, PurchaseOrder.class).isEmpty()) {
            purchaseOrder = PurchaseOrderResourceIT.createUpdatedEntity(em);
            em.persist(purchaseOrder);
            em.flush();
        } else {
            purchaseOrder = TestUtil.findAll(em, PurchaseOrder.class).get(0);
        }
        goodsReceipt.getLinkedPOs().add(purchaseOrder);
        // Add required entity
        Supplier supplier;
        if (TestUtil.findAll(em, Supplier.class).isEmpty()) {
            supplier = SupplierResourceIT.createUpdatedEntity(em);
            em.persist(supplier);
            em.flush();
        } else {
            supplier = TestUtil.findAll(em, Supplier.class).get(0);
        }
        goodsReceipt.setSupplier(supplier);
        // Add required entity
        Location location;
        if (TestUtil.findAll(em, Location.class).isEmpty()) {
            location = LocationResourceIT.createUpdatedEntity(em);
            em.persist(location);
            em.flush();
        } else {
            location = TestUtil.findAll(em, Location.class).get(0);
        }
        goodsReceipt.setLocation(location);
        // Add required entity
        TransactionType transactionType;
        if (TestUtil.findAll(em, TransactionType.class).isEmpty()) {
            transactionType = TransactionTypeResourceIT.createUpdatedEntity(em);
            em.persist(transactionType);
            em.flush();
        } else {
            transactionType = TestUtil.findAll(em, TransactionType.class).get(0);
        }
        goodsReceipt.setTransactionType(transactionType);
        return goodsReceipt;
    }

    @BeforeEach
    public void initTest() {
        goodsReceipt = createEntity(em);
    }

    @Test
    @Transactional
    public void createGoodsReceipt() throws Exception {
        int databaseSizeBeforeCreate = goodsReceiptRepository.findAll().size();

        // Create the GoodsReceipt
        restGoodsReceiptMockMvc.perform(post("/api/goods-receipts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(goodsReceipt)))
            .andExpect(status().isCreated());

        // Validate the GoodsReceipt in the database
        List<GoodsReceipt> goodsReceiptList = goodsReceiptRepository.findAll();
        assertThat(goodsReceiptList).hasSize(databaseSizeBeforeCreate + 1);
        GoodsReceipt testGoodsReceipt = goodsReceiptList.get(goodsReceiptList.size() - 1);
        assertThat(testGoodsReceipt.getGrnNumber()).isEqualTo(DEFAULT_GRN_NUMBER);
        assertThat(testGoodsReceipt.getGrnDate()).isEqualTo(DEFAULT_GRN_DATE);
        assertThat(testGoodsReceipt.getPoNumber()).isEqualTo(DEFAULT_PO_NUMBER);
        assertThat(testGoodsReceipt.getGrnAmount()).isEqualTo(DEFAULT_GRN_AMOUNT);
        assertThat(testGoodsReceipt.getCashAmount()).isEqualTo(DEFAULT_CASH_AMOUNT);
        assertThat(testGoodsReceipt.getCardAmount()).isEqualTo(DEFAULT_CARD_AMOUNT);
        assertThat(testGoodsReceipt.getDueAmount()).isEqualTo(DEFAULT_DUE_AMOUNT);
    }

    @Test
    @Transactional
    public void createGoodsReceiptWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = goodsReceiptRepository.findAll().size();

        // Create the GoodsReceipt with an existing ID
        goodsReceipt.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGoodsReceiptMockMvc.perform(post("/api/goods-receipts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(goodsReceipt)))
            .andExpect(status().isBadRequest());

        // Validate the GoodsReceipt in the database
        List<GoodsReceipt> goodsReceiptList = goodsReceiptRepository.findAll();
        assertThat(goodsReceiptList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkGrnNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = goodsReceiptRepository.findAll().size();
        // set the field null
        goodsReceipt.setGrnNumber(null);

        // Create the GoodsReceipt, which fails.

        restGoodsReceiptMockMvc.perform(post("/api/goods-receipts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(goodsReceipt)))
            .andExpect(status().isBadRequest());

        List<GoodsReceipt> goodsReceiptList = goodsReceiptRepository.findAll();
        assertThat(goodsReceiptList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkGrnDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = goodsReceiptRepository.findAll().size();
        // set the field null
        goodsReceipt.setGrnDate(null);

        // Create the GoodsReceipt, which fails.

        restGoodsReceiptMockMvc.perform(post("/api/goods-receipts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(goodsReceipt)))
            .andExpect(status().isBadRequest());

        List<GoodsReceipt> goodsReceiptList = goodsReceiptRepository.findAll();
        assertThat(goodsReceiptList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkGrnAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = goodsReceiptRepository.findAll().size();
        // set the field null
        goodsReceipt.setGrnAmount(null);

        // Create the GoodsReceipt, which fails.

        restGoodsReceiptMockMvc.perform(post("/api/goods-receipts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(goodsReceipt)))
            .andExpect(status().isBadRequest());

        List<GoodsReceipt> goodsReceiptList = goodsReceiptRepository.findAll();
        assertThat(goodsReceiptList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGoodsReceipts() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList
        restGoodsReceiptMockMvc.perform(get("/api/goods-receipts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(goodsReceipt.getId().intValue())))
            .andExpect(jsonPath("$.[*].grnNumber").value(hasItem(DEFAULT_GRN_NUMBER)))
            .andExpect(jsonPath("$.[*].grnDate").value(hasItem(DEFAULT_GRN_DATE.toString())))
            .andExpect(jsonPath("$.[*].poNumber").value(hasItem(DEFAULT_PO_NUMBER)))
            .andExpect(jsonPath("$.[*].grnAmount").value(hasItem(DEFAULT_GRN_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].cashAmount").value(hasItem(DEFAULT_CASH_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].cardAmount").value(hasItem(DEFAULT_CARD_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].dueAmount").value(hasItem(DEFAULT_DUE_AMOUNT.intValue())));
    }
    
    @Test
    @Transactional
    public void getGoodsReceipt() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get the goodsReceipt
        restGoodsReceiptMockMvc.perform(get("/api/goods-receipts/{id}", goodsReceipt.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(goodsReceipt.getId().intValue()))
            .andExpect(jsonPath("$.grnNumber").value(DEFAULT_GRN_NUMBER))
            .andExpect(jsonPath("$.grnDate").value(DEFAULT_GRN_DATE.toString()))
            .andExpect(jsonPath("$.poNumber").value(DEFAULT_PO_NUMBER))
            .andExpect(jsonPath("$.grnAmount").value(DEFAULT_GRN_AMOUNT.intValue()))
            .andExpect(jsonPath("$.cashAmount").value(DEFAULT_CASH_AMOUNT.intValue()))
            .andExpect(jsonPath("$.cardAmount").value(DEFAULT_CARD_AMOUNT.intValue()))
            .andExpect(jsonPath("$.dueAmount").value(DEFAULT_DUE_AMOUNT.intValue()));
    }


    @Test
    @Transactional
    public void getGoodsReceiptsByIdFiltering() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        Long id = goodsReceipt.getId();

        defaultGoodsReceiptShouldBeFound("id.equals=" + id);
        defaultGoodsReceiptShouldNotBeFound("id.notEquals=" + id);

        defaultGoodsReceiptShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultGoodsReceiptShouldNotBeFound("id.greaterThan=" + id);

        defaultGoodsReceiptShouldBeFound("id.lessThanOrEqual=" + id);
        defaultGoodsReceiptShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllGoodsReceiptsByGrnNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where grnNumber equals to DEFAULT_GRN_NUMBER
        defaultGoodsReceiptShouldBeFound("grnNumber.equals=" + DEFAULT_GRN_NUMBER);

        // Get all the goodsReceiptList where grnNumber equals to UPDATED_GRN_NUMBER
        defaultGoodsReceiptShouldNotBeFound("grnNumber.equals=" + UPDATED_GRN_NUMBER);
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptsByGrnNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where grnNumber not equals to DEFAULT_GRN_NUMBER
        defaultGoodsReceiptShouldNotBeFound("grnNumber.notEquals=" + DEFAULT_GRN_NUMBER);

        // Get all the goodsReceiptList where grnNumber not equals to UPDATED_GRN_NUMBER
        defaultGoodsReceiptShouldBeFound("grnNumber.notEquals=" + UPDATED_GRN_NUMBER);
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptsByGrnNumberIsInShouldWork() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where grnNumber in DEFAULT_GRN_NUMBER or UPDATED_GRN_NUMBER
        defaultGoodsReceiptShouldBeFound("grnNumber.in=" + DEFAULT_GRN_NUMBER + "," + UPDATED_GRN_NUMBER);

        // Get all the goodsReceiptList where grnNumber equals to UPDATED_GRN_NUMBER
        defaultGoodsReceiptShouldNotBeFound("grnNumber.in=" + UPDATED_GRN_NUMBER);
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptsByGrnNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where grnNumber is not null
        defaultGoodsReceiptShouldBeFound("grnNumber.specified=true");

        // Get all the goodsReceiptList where grnNumber is null
        defaultGoodsReceiptShouldNotBeFound("grnNumber.specified=false");
    }
                @Test
    @Transactional
    public void getAllGoodsReceiptsByGrnNumberContainsSomething() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where grnNumber contains DEFAULT_GRN_NUMBER
        defaultGoodsReceiptShouldBeFound("grnNumber.contains=" + DEFAULT_GRN_NUMBER);

        // Get all the goodsReceiptList where grnNumber contains UPDATED_GRN_NUMBER
        defaultGoodsReceiptShouldNotBeFound("grnNumber.contains=" + UPDATED_GRN_NUMBER);
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptsByGrnNumberNotContainsSomething() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where grnNumber does not contain DEFAULT_GRN_NUMBER
        defaultGoodsReceiptShouldNotBeFound("grnNumber.doesNotContain=" + DEFAULT_GRN_NUMBER);

        // Get all the goodsReceiptList where grnNumber does not contain UPDATED_GRN_NUMBER
        defaultGoodsReceiptShouldBeFound("grnNumber.doesNotContain=" + UPDATED_GRN_NUMBER);
    }


    @Test
    @Transactional
    public void getAllGoodsReceiptsByGrnDateIsEqualToSomething() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where grnDate equals to DEFAULT_GRN_DATE
        defaultGoodsReceiptShouldBeFound("grnDate.equals=" + DEFAULT_GRN_DATE);

        // Get all the goodsReceiptList where grnDate equals to UPDATED_GRN_DATE
        defaultGoodsReceiptShouldNotBeFound("grnDate.equals=" + UPDATED_GRN_DATE);
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptsByGrnDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where grnDate not equals to DEFAULT_GRN_DATE
        defaultGoodsReceiptShouldNotBeFound("grnDate.notEquals=" + DEFAULT_GRN_DATE);

        // Get all the goodsReceiptList where grnDate not equals to UPDATED_GRN_DATE
        defaultGoodsReceiptShouldBeFound("grnDate.notEquals=" + UPDATED_GRN_DATE);
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptsByGrnDateIsInShouldWork() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where grnDate in DEFAULT_GRN_DATE or UPDATED_GRN_DATE
        defaultGoodsReceiptShouldBeFound("grnDate.in=" + DEFAULT_GRN_DATE + "," + UPDATED_GRN_DATE);

        // Get all the goodsReceiptList where grnDate equals to UPDATED_GRN_DATE
        defaultGoodsReceiptShouldNotBeFound("grnDate.in=" + UPDATED_GRN_DATE);
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptsByGrnDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where grnDate is not null
        defaultGoodsReceiptShouldBeFound("grnDate.specified=true");

        // Get all the goodsReceiptList where grnDate is null
        defaultGoodsReceiptShouldNotBeFound("grnDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptsByGrnDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where grnDate is greater than or equal to DEFAULT_GRN_DATE
        defaultGoodsReceiptShouldBeFound("grnDate.greaterThanOrEqual=" + DEFAULT_GRN_DATE);

        // Get all the goodsReceiptList where grnDate is greater than or equal to UPDATED_GRN_DATE
        defaultGoodsReceiptShouldNotBeFound("grnDate.greaterThanOrEqual=" + UPDATED_GRN_DATE);
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptsByGrnDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where grnDate is less than or equal to DEFAULT_GRN_DATE
        defaultGoodsReceiptShouldBeFound("grnDate.lessThanOrEqual=" + DEFAULT_GRN_DATE);

        // Get all the goodsReceiptList where grnDate is less than or equal to SMALLER_GRN_DATE
        defaultGoodsReceiptShouldNotBeFound("grnDate.lessThanOrEqual=" + SMALLER_GRN_DATE);
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptsByGrnDateIsLessThanSomething() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where grnDate is less than DEFAULT_GRN_DATE
        defaultGoodsReceiptShouldNotBeFound("grnDate.lessThan=" + DEFAULT_GRN_DATE);

        // Get all the goodsReceiptList where grnDate is less than UPDATED_GRN_DATE
        defaultGoodsReceiptShouldBeFound("grnDate.lessThan=" + UPDATED_GRN_DATE);
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptsByGrnDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where grnDate is greater than DEFAULT_GRN_DATE
        defaultGoodsReceiptShouldNotBeFound("grnDate.greaterThan=" + DEFAULT_GRN_DATE);

        // Get all the goodsReceiptList where grnDate is greater than SMALLER_GRN_DATE
        defaultGoodsReceiptShouldBeFound("grnDate.greaterThan=" + SMALLER_GRN_DATE);
    }


    @Test
    @Transactional
    public void getAllGoodsReceiptsByPoNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where poNumber equals to DEFAULT_PO_NUMBER
        defaultGoodsReceiptShouldBeFound("poNumber.equals=" + DEFAULT_PO_NUMBER);

        // Get all the goodsReceiptList where poNumber equals to UPDATED_PO_NUMBER
        defaultGoodsReceiptShouldNotBeFound("poNumber.equals=" + UPDATED_PO_NUMBER);
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptsByPoNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where poNumber not equals to DEFAULT_PO_NUMBER
        defaultGoodsReceiptShouldNotBeFound("poNumber.notEquals=" + DEFAULT_PO_NUMBER);

        // Get all the goodsReceiptList where poNumber not equals to UPDATED_PO_NUMBER
        defaultGoodsReceiptShouldBeFound("poNumber.notEquals=" + UPDATED_PO_NUMBER);
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptsByPoNumberIsInShouldWork() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where poNumber in DEFAULT_PO_NUMBER or UPDATED_PO_NUMBER
        defaultGoodsReceiptShouldBeFound("poNumber.in=" + DEFAULT_PO_NUMBER + "," + UPDATED_PO_NUMBER);

        // Get all the goodsReceiptList where poNumber equals to UPDATED_PO_NUMBER
        defaultGoodsReceiptShouldNotBeFound("poNumber.in=" + UPDATED_PO_NUMBER);
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptsByPoNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where poNumber is not null
        defaultGoodsReceiptShouldBeFound("poNumber.specified=true");

        // Get all the goodsReceiptList where poNumber is null
        defaultGoodsReceiptShouldNotBeFound("poNumber.specified=false");
    }
                @Test
    @Transactional
    public void getAllGoodsReceiptsByPoNumberContainsSomething() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where poNumber contains DEFAULT_PO_NUMBER
        defaultGoodsReceiptShouldBeFound("poNumber.contains=" + DEFAULT_PO_NUMBER);

        // Get all the goodsReceiptList where poNumber contains UPDATED_PO_NUMBER
        defaultGoodsReceiptShouldNotBeFound("poNumber.contains=" + UPDATED_PO_NUMBER);
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptsByPoNumberNotContainsSomething() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where poNumber does not contain DEFAULT_PO_NUMBER
        defaultGoodsReceiptShouldNotBeFound("poNumber.doesNotContain=" + DEFAULT_PO_NUMBER);

        // Get all the goodsReceiptList where poNumber does not contain UPDATED_PO_NUMBER
        defaultGoodsReceiptShouldBeFound("poNumber.doesNotContain=" + UPDATED_PO_NUMBER);
    }


    @Test
    @Transactional
    public void getAllGoodsReceiptsByGrnAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where grnAmount equals to DEFAULT_GRN_AMOUNT
        defaultGoodsReceiptShouldBeFound("grnAmount.equals=" + DEFAULT_GRN_AMOUNT);

        // Get all the goodsReceiptList where grnAmount equals to UPDATED_GRN_AMOUNT
        defaultGoodsReceiptShouldNotBeFound("grnAmount.equals=" + UPDATED_GRN_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptsByGrnAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where grnAmount not equals to DEFAULT_GRN_AMOUNT
        defaultGoodsReceiptShouldNotBeFound("grnAmount.notEquals=" + DEFAULT_GRN_AMOUNT);

        // Get all the goodsReceiptList where grnAmount not equals to UPDATED_GRN_AMOUNT
        defaultGoodsReceiptShouldBeFound("grnAmount.notEquals=" + UPDATED_GRN_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptsByGrnAmountIsInShouldWork() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where grnAmount in DEFAULT_GRN_AMOUNT or UPDATED_GRN_AMOUNT
        defaultGoodsReceiptShouldBeFound("grnAmount.in=" + DEFAULT_GRN_AMOUNT + "," + UPDATED_GRN_AMOUNT);

        // Get all the goodsReceiptList where grnAmount equals to UPDATED_GRN_AMOUNT
        defaultGoodsReceiptShouldNotBeFound("grnAmount.in=" + UPDATED_GRN_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptsByGrnAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where grnAmount is not null
        defaultGoodsReceiptShouldBeFound("grnAmount.specified=true");

        // Get all the goodsReceiptList where grnAmount is null
        defaultGoodsReceiptShouldNotBeFound("grnAmount.specified=false");
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptsByGrnAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where grnAmount is greater than or equal to DEFAULT_GRN_AMOUNT
        defaultGoodsReceiptShouldBeFound("grnAmount.greaterThanOrEqual=" + DEFAULT_GRN_AMOUNT);

        // Get all the goodsReceiptList where grnAmount is greater than or equal to UPDATED_GRN_AMOUNT
        defaultGoodsReceiptShouldNotBeFound("grnAmount.greaterThanOrEqual=" + UPDATED_GRN_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptsByGrnAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where grnAmount is less than or equal to DEFAULT_GRN_AMOUNT
        defaultGoodsReceiptShouldBeFound("grnAmount.lessThanOrEqual=" + DEFAULT_GRN_AMOUNT);

        // Get all the goodsReceiptList where grnAmount is less than or equal to SMALLER_GRN_AMOUNT
        defaultGoodsReceiptShouldNotBeFound("grnAmount.lessThanOrEqual=" + SMALLER_GRN_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptsByGrnAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where grnAmount is less than DEFAULT_GRN_AMOUNT
        defaultGoodsReceiptShouldNotBeFound("grnAmount.lessThan=" + DEFAULT_GRN_AMOUNT);

        // Get all the goodsReceiptList where grnAmount is less than UPDATED_GRN_AMOUNT
        defaultGoodsReceiptShouldBeFound("grnAmount.lessThan=" + UPDATED_GRN_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptsByGrnAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where grnAmount is greater than DEFAULT_GRN_AMOUNT
        defaultGoodsReceiptShouldNotBeFound("grnAmount.greaterThan=" + DEFAULT_GRN_AMOUNT);

        // Get all the goodsReceiptList where grnAmount is greater than SMALLER_GRN_AMOUNT
        defaultGoodsReceiptShouldBeFound("grnAmount.greaterThan=" + SMALLER_GRN_AMOUNT);
    }


    @Test
    @Transactional
    public void getAllGoodsReceiptsByCashAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where cashAmount equals to DEFAULT_CASH_AMOUNT
        defaultGoodsReceiptShouldBeFound("cashAmount.equals=" + DEFAULT_CASH_AMOUNT);

        // Get all the goodsReceiptList where cashAmount equals to UPDATED_CASH_AMOUNT
        defaultGoodsReceiptShouldNotBeFound("cashAmount.equals=" + UPDATED_CASH_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptsByCashAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where cashAmount not equals to DEFAULT_CASH_AMOUNT
        defaultGoodsReceiptShouldNotBeFound("cashAmount.notEquals=" + DEFAULT_CASH_AMOUNT);

        // Get all the goodsReceiptList where cashAmount not equals to UPDATED_CASH_AMOUNT
        defaultGoodsReceiptShouldBeFound("cashAmount.notEquals=" + UPDATED_CASH_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptsByCashAmountIsInShouldWork() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where cashAmount in DEFAULT_CASH_AMOUNT or UPDATED_CASH_AMOUNT
        defaultGoodsReceiptShouldBeFound("cashAmount.in=" + DEFAULT_CASH_AMOUNT + "," + UPDATED_CASH_AMOUNT);

        // Get all the goodsReceiptList where cashAmount equals to UPDATED_CASH_AMOUNT
        defaultGoodsReceiptShouldNotBeFound("cashAmount.in=" + UPDATED_CASH_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptsByCashAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where cashAmount is not null
        defaultGoodsReceiptShouldBeFound("cashAmount.specified=true");

        // Get all the goodsReceiptList where cashAmount is null
        defaultGoodsReceiptShouldNotBeFound("cashAmount.specified=false");
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptsByCashAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where cashAmount is greater than or equal to DEFAULT_CASH_AMOUNT
        defaultGoodsReceiptShouldBeFound("cashAmount.greaterThanOrEqual=" + DEFAULT_CASH_AMOUNT);

        // Get all the goodsReceiptList where cashAmount is greater than or equal to UPDATED_CASH_AMOUNT
        defaultGoodsReceiptShouldNotBeFound("cashAmount.greaterThanOrEqual=" + UPDATED_CASH_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptsByCashAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where cashAmount is less than or equal to DEFAULT_CASH_AMOUNT
        defaultGoodsReceiptShouldBeFound("cashAmount.lessThanOrEqual=" + DEFAULT_CASH_AMOUNT);

        // Get all the goodsReceiptList where cashAmount is less than or equal to SMALLER_CASH_AMOUNT
        defaultGoodsReceiptShouldNotBeFound("cashAmount.lessThanOrEqual=" + SMALLER_CASH_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptsByCashAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where cashAmount is less than DEFAULT_CASH_AMOUNT
        defaultGoodsReceiptShouldNotBeFound("cashAmount.lessThan=" + DEFAULT_CASH_AMOUNT);

        // Get all the goodsReceiptList where cashAmount is less than UPDATED_CASH_AMOUNT
        defaultGoodsReceiptShouldBeFound("cashAmount.lessThan=" + UPDATED_CASH_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptsByCashAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where cashAmount is greater than DEFAULT_CASH_AMOUNT
        defaultGoodsReceiptShouldNotBeFound("cashAmount.greaterThan=" + DEFAULT_CASH_AMOUNT);

        // Get all the goodsReceiptList where cashAmount is greater than SMALLER_CASH_AMOUNT
        defaultGoodsReceiptShouldBeFound("cashAmount.greaterThan=" + SMALLER_CASH_AMOUNT);
    }


    @Test
    @Transactional
    public void getAllGoodsReceiptsByCardAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where cardAmount equals to DEFAULT_CARD_AMOUNT
        defaultGoodsReceiptShouldBeFound("cardAmount.equals=" + DEFAULT_CARD_AMOUNT);

        // Get all the goodsReceiptList where cardAmount equals to UPDATED_CARD_AMOUNT
        defaultGoodsReceiptShouldNotBeFound("cardAmount.equals=" + UPDATED_CARD_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptsByCardAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where cardAmount not equals to DEFAULT_CARD_AMOUNT
        defaultGoodsReceiptShouldNotBeFound("cardAmount.notEquals=" + DEFAULT_CARD_AMOUNT);

        // Get all the goodsReceiptList where cardAmount not equals to UPDATED_CARD_AMOUNT
        defaultGoodsReceiptShouldBeFound("cardAmount.notEquals=" + UPDATED_CARD_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptsByCardAmountIsInShouldWork() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where cardAmount in DEFAULT_CARD_AMOUNT or UPDATED_CARD_AMOUNT
        defaultGoodsReceiptShouldBeFound("cardAmount.in=" + DEFAULT_CARD_AMOUNT + "," + UPDATED_CARD_AMOUNT);

        // Get all the goodsReceiptList where cardAmount equals to UPDATED_CARD_AMOUNT
        defaultGoodsReceiptShouldNotBeFound("cardAmount.in=" + UPDATED_CARD_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptsByCardAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where cardAmount is not null
        defaultGoodsReceiptShouldBeFound("cardAmount.specified=true");

        // Get all the goodsReceiptList where cardAmount is null
        defaultGoodsReceiptShouldNotBeFound("cardAmount.specified=false");
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptsByCardAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where cardAmount is greater than or equal to DEFAULT_CARD_AMOUNT
        defaultGoodsReceiptShouldBeFound("cardAmount.greaterThanOrEqual=" + DEFAULT_CARD_AMOUNT);

        // Get all the goodsReceiptList where cardAmount is greater than or equal to UPDATED_CARD_AMOUNT
        defaultGoodsReceiptShouldNotBeFound("cardAmount.greaterThanOrEqual=" + UPDATED_CARD_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptsByCardAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where cardAmount is less than or equal to DEFAULT_CARD_AMOUNT
        defaultGoodsReceiptShouldBeFound("cardAmount.lessThanOrEqual=" + DEFAULT_CARD_AMOUNT);

        // Get all the goodsReceiptList where cardAmount is less than or equal to SMALLER_CARD_AMOUNT
        defaultGoodsReceiptShouldNotBeFound("cardAmount.lessThanOrEqual=" + SMALLER_CARD_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptsByCardAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where cardAmount is less than DEFAULT_CARD_AMOUNT
        defaultGoodsReceiptShouldNotBeFound("cardAmount.lessThan=" + DEFAULT_CARD_AMOUNT);

        // Get all the goodsReceiptList where cardAmount is less than UPDATED_CARD_AMOUNT
        defaultGoodsReceiptShouldBeFound("cardAmount.lessThan=" + UPDATED_CARD_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptsByCardAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where cardAmount is greater than DEFAULT_CARD_AMOUNT
        defaultGoodsReceiptShouldNotBeFound("cardAmount.greaterThan=" + DEFAULT_CARD_AMOUNT);

        // Get all the goodsReceiptList where cardAmount is greater than SMALLER_CARD_AMOUNT
        defaultGoodsReceiptShouldBeFound("cardAmount.greaterThan=" + SMALLER_CARD_AMOUNT);
    }


    @Test
    @Transactional
    public void getAllGoodsReceiptsByDueAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where dueAmount equals to DEFAULT_DUE_AMOUNT
        defaultGoodsReceiptShouldBeFound("dueAmount.equals=" + DEFAULT_DUE_AMOUNT);

        // Get all the goodsReceiptList where dueAmount equals to UPDATED_DUE_AMOUNT
        defaultGoodsReceiptShouldNotBeFound("dueAmount.equals=" + UPDATED_DUE_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptsByDueAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where dueAmount not equals to DEFAULT_DUE_AMOUNT
        defaultGoodsReceiptShouldNotBeFound("dueAmount.notEquals=" + DEFAULT_DUE_AMOUNT);

        // Get all the goodsReceiptList where dueAmount not equals to UPDATED_DUE_AMOUNT
        defaultGoodsReceiptShouldBeFound("dueAmount.notEquals=" + UPDATED_DUE_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptsByDueAmountIsInShouldWork() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where dueAmount in DEFAULT_DUE_AMOUNT or UPDATED_DUE_AMOUNT
        defaultGoodsReceiptShouldBeFound("dueAmount.in=" + DEFAULT_DUE_AMOUNT + "," + UPDATED_DUE_AMOUNT);

        // Get all the goodsReceiptList where dueAmount equals to UPDATED_DUE_AMOUNT
        defaultGoodsReceiptShouldNotBeFound("dueAmount.in=" + UPDATED_DUE_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptsByDueAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where dueAmount is not null
        defaultGoodsReceiptShouldBeFound("dueAmount.specified=true");

        // Get all the goodsReceiptList where dueAmount is null
        defaultGoodsReceiptShouldNotBeFound("dueAmount.specified=false");
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptsByDueAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where dueAmount is greater than or equal to DEFAULT_DUE_AMOUNT
        defaultGoodsReceiptShouldBeFound("dueAmount.greaterThanOrEqual=" + DEFAULT_DUE_AMOUNT);

        // Get all the goodsReceiptList where dueAmount is greater than or equal to UPDATED_DUE_AMOUNT
        defaultGoodsReceiptShouldNotBeFound("dueAmount.greaterThanOrEqual=" + UPDATED_DUE_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptsByDueAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where dueAmount is less than or equal to DEFAULT_DUE_AMOUNT
        defaultGoodsReceiptShouldBeFound("dueAmount.lessThanOrEqual=" + DEFAULT_DUE_AMOUNT);

        // Get all the goodsReceiptList where dueAmount is less than or equal to SMALLER_DUE_AMOUNT
        defaultGoodsReceiptShouldNotBeFound("dueAmount.lessThanOrEqual=" + SMALLER_DUE_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptsByDueAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where dueAmount is less than DEFAULT_DUE_AMOUNT
        defaultGoodsReceiptShouldNotBeFound("dueAmount.lessThan=" + DEFAULT_DUE_AMOUNT);

        // Get all the goodsReceiptList where dueAmount is less than UPDATED_DUE_AMOUNT
        defaultGoodsReceiptShouldBeFound("dueAmount.lessThan=" + UPDATED_DUE_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptsByDueAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where dueAmount is greater than DEFAULT_DUE_AMOUNT
        defaultGoodsReceiptShouldNotBeFound("dueAmount.greaterThan=" + DEFAULT_DUE_AMOUNT);

        // Get all the goodsReceiptList where dueAmount is greater than SMALLER_DUE_AMOUNT
        defaultGoodsReceiptShouldBeFound("dueAmount.greaterThan=" + SMALLER_DUE_AMOUNT);
    }


    @Test
    @Transactional
    public void getAllGoodsReceiptsByDetailsIsEqualToSomething() throws Exception {
        // Get already existing entity
        GoodsReceiptDetails details = goodsReceipt.getDetails();
        goodsReceiptRepository.saveAndFlush(goodsReceipt);
        Long detailsId = details.getId();

        // Get all the goodsReceiptList where details equals to detailsId
        defaultGoodsReceiptShouldBeFound("detailsId.equals=" + detailsId);

        // Get all the goodsReceiptList where details equals to detailsId + 1
        defaultGoodsReceiptShouldNotBeFound("detailsId.equals=" + (detailsId + 1));
    }


    @Test
    @Transactional
    public void getAllGoodsReceiptsByLinkedPOsIsEqualToSomething() throws Exception {
        // Get already existing entity
        PurchaseOrder linkedPOs = goodsReceipt.getLinkedPOs();
        goodsReceiptRepository.saveAndFlush(goodsReceipt);
        Long linkedPOsId = linkedPOs.getId();

        // Get all the goodsReceiptList where linkedPOs equals to linkedPOsId
        defaultGoodsReceiptShouldBeFound("linkedPOsId.equals=" + linkedPOsId);

        // Get all the goodsReceiptList where linkedPOs equals to linkedPOsId + 1
        defaultGoodsReceiptShouldNotBeFound("linkedPOsId.equals=" + (linkedPOsId + 1));
    }


    @Test
    @Transactional
    public void getAllGoodsReceiptsBySupplierIsEqualToSomething() throws Exception {
        // Get already existing entity
        Supplier supplier = goodsReceipt.getSupplier();
        goodsReceiptRepository.saveAndFlush(goodsReceipt);
        Long supplierId = supplier.getId();

        // Get all the goodsReceiptList where supplier equals to supplierId
        defaultGoodsReceiptShouldBeFound("supplierId.equals=" + supplierId);

        // Get all the goodsReceiptList where supplier equals to supplierId + 1
        defaultGoodsReceiptShouldNotBeFound("supplierId.equals=" + (supplierId + 1));
    }


    @Test
    @Transactional
    public void getAllGoodsReceiptsByLocationIsEqualToSomething() throws Exception {
        // Get already existing entity
        Location location = goodsReceipt.getLocation();
        goodsReceiptRepository.saveAndFlush(goodsReceipt);
        Long locationId = location.getId();

        // Get all the goodsReceiptList where location equals to locationId
        defaultGoodsReceiptShouldBeFound("locationId.equals=" + locationId);

        // Get all the goodsReceiptList where location equals to locationId + 1
        defaultGoodsReceiptShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }


    @Test
    @Transactional
    public void getAllGoodsReceiptsByTransactionTypeIsEqualToSomething() throws Exception {
        // Get already existing entity
        TransactionType transactionType = goodsReceipt.getTransactionType();
        goodsReceiptRepository.saveAndFlush(goodsReceipt);
        Long transactionTypeId = transactionType.getId();

        // Get all the goodsReceiptList where transactionType equals to transactionTypeId
        defaultGoodsReceiptShouldBeFound("transactionTypeId.equals=" + transactionTypeId);

        // Get all the goodsReceiptList where transactionType equals to transactionTypeId + 1
        defaultGoodsReceiptShouldNotBeFound("transactionTypeId.equals=" + (transactionTypeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGoodsReceiptShouldBeFound(String filter) throws Exception {
        restGoodsReceiptMockMvc.perform(get("/api/goods-receipts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(goodsReceipt.getId().intValue())))
            .andExpect(jsonPath("$.[*].grnNumber").value(hasItem(DEFAULT_GRN_NUMBER)))
            .andExpect(jsonPath("$.[*].grnDate").value(hasItem(DEFAULT_GRN_DATE.toString())))
            .andExpect(jsonPath("$.[*].poNumber").value(hasItem(DEFAULT_PO_NUMBER)))
            .andExpect(jsonPath("$.[*].grnAmount").value(hasItem(DEFAULT_GRN_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].cashAmount").value(hasItem(DEFAULT_CASH_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].cardAmount").value(hasItem(DEFAULT_CARD_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].dueAmount").value(hasItem(DEFAULT_DUE_AMOUNT.intValue())));

        // Check, that the count call also returns 1
        restGoodsReceiptMockMvc.perform(get("/api/goods-receipts/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGoodsReceiptShouldNotBeFound(String filter) throws Exception {
        restGoodsReceiptMockMvc.perform(get("/api/goods-receipts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGoodsReceiptMockMvc.perform(get("/api/goods-receipts/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingGoodsReceipt() throws Exception {
        // Get the goodsReceipt
        restGoodsReceiptMockMvc.perform(get("/api/goods-receipts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGoodsReceipt() throws Exception {
        // Initialize the database
        goodsReceiptService.save(goodsReceipt);

        int databaseSizeBeforeUpdate = goodsReceiptRepository.findAll().size();

        // Update the goodsReceipt
        GoodsReceipt updatedGoodsReceipt = goodsReceiptRepository.findById(goodsReceipt.getId()).get();
        // Disconnect from session so that the updates on updatedGoodsReceipt are not directly saved in db
        em.detach(updatedGoodsReceipt);
        updatedGoodsReceipt
            .grnNumber(UPDATED_GRN_NUMBER)
            .grnDate(UPDATED_GRN_DATE)
            .poNumber(UPDATED_PO_NUMBER)
            .grnAmount(UPDATED_GRN_AMOUNT)
            .cashAmount(UPDATED_CASH_AMOUNT)
            .cardAmount(UPDATED_CARD_AMOUNT)
            .dueAmount(UPDATED_DUE_AMOUNT);

        restGoodsReceiptMockMvc.perform(put("/api/goods-receipts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedGoodsReceipt)))
            .andExpect(status().isOk());

        // Validate the GoodsReceipt in the database
        List<GoodsReceipt> goodsReceiptList = goodsReceiptRepository.findAll();
        assertThat(goodsReceiptList).hasSize(databaseSizeBeforeUpdate);
        GoodsReceipt testGoodsReceipt = goodsReceiptList.get(goodsReceiptList.size() - 1);
        assertThat(testGoodsReceipt.getGrnNumber()).isEqualTo(UPDATED_GRN_NUMBER);
        assertThat(testGoodsReceipt.getGrnDate()).isEqualTo(UPDATED_GRN_DATE);
        assertThat(testGoodsReceipt.getPoNumber()).isEqualTo(UPDATED_PO_NUMBER);
        assertThat(testGoodsReceipt.getGrnAmount()).isEqualTo(UPDATED_GRN_AMOUNT);
        assertThat(testGoodsReceipt.getCashAmount()).isEqualTo(UPDATED_CASH_AMOUNT);
        assertThat(testGoodsReceipt.getCardAmount()).isEqualTo(UPDATED_CARD_AMOUNT);
        assertThat(testGoodsReceipt.getDueAmount()).isEqualTo(UPDATED_DUE_AMOUNT);
    }

    @Test
    @Transactional
    public void updateNonExistingGoodsReceipt() throws Exception {
        int databaseSizeBeforeUpdate = goodsReceiptRepository.findAll().size();

        // Create the GoodsReceipt

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGoodsReceiptMockMvc.perform(put("/api/goods-receipts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(goodsReceipt)))
            .andExpect(status().isBadRequest());

        // Validate the GoodsReceipt in the database
        List<GoodsReceipt> goodsReceiptList = goodsReceiptRepository.findAll();
        assertThat(goodsReceiptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteGoodsReceipt() throws Exception {
        // Initialize the database
        goodsReceiptService.save(goodsReceipt);

        int databaseSizeBeforeDelete = goodsReceiptRepository.findAll().size();

        // Delete the goodsReceipt
        restGoodsReceiptMockMvc.perform(delete("/api/goods-receipts/{id}", goodsReceipt.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GoodsReceipt> goodsReceiptList = goodsReceiptRepository.findAll();
        assertThat(goodsReceiptList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

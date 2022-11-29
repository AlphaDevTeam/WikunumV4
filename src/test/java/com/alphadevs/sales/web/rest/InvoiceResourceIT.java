package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.WikunumApp;
import com.alphadevs.sales.domain.Invoice;
import com.alphadevs.sales.domain.InvoiceDetails;
import com.alphadevs.sales.domain.Customer;
import com.alphadevs.sales.domain.TransactionType;
import com.alphadevs.sales.domain.Location;
import com.alphadevs.sales.repository.InvoiceRepository;
import com.alphadevs.sales.service.InvoiceService;
import com.alphadevs.sales.web.rest.errors.ExceptionTranslator;
import com.alphadevs.sales.service.dto.InvoiceCriteria;
import com.alphadevs.sales.service.InvoiceQueryService;

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
 * Integration tests for the {@link InvoiceResource} REST controller.
 */
@SpringBootTest(classes = WikunumApp.class)
public class InvoiceResourceIT {

    private static final String DEFAULT_INV_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_INV_NUMBER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_INV_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_INV_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_INV_DATE = LocalDate.ofEpochDay(-1L);

    private static final BigDecimal DEFAULT_INV_TOTAL_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_INV_TOTAL_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_INV_TOTAL_AMOUNT = new BigDecimal(1 - 1);

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
    private InvoiceRepository invoiceRepository;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private InvoiceQueryService invoiceQueryService;

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

    private MockMvc restInvoiceMockMvc;

    private Invoice invoice;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InvoiceResource invoiceResource = new InvoiceResource(invoiceService, invoiceQueryService);
        this.restInvoiceMockMvc = MockMvcBuilders.standaloneSetup(invoiceResource)
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
    public static Invoice createEntity(EntityManager em) {
        Invoice invoice = new Invoice()
            .invNumber(DEFAULT_INV_NUMBER)
            .invDate(DEFAULT_INV_DATE)
            .invTotalAmount(DEFAULT_INV_TOTAL_AMOUNT)
            .cashAmount(DEFAULT_CASH_AMOUNT)
            .cardAmount(DEFAULT_CARD_AMOUNT)
            .dueAmount(DEFAULT_DUE_AMOUNT);
        // Add required entity
        InvoiceDetails invoiceDetails;
        if (TestUtil.findAll(em, InvoiceDetails.class).isEmpty()) {
            invoiceDetails = InvoiceDetailsResourceIT.createEntity(em);
            em.persist(invoiceDetails);
            em.flush();
        } else {
            invoiceDetails = TestUtil.findAll(em, InvoiceDetails.class).get(0);
        }
        invoice.getDetails().add(invoiceDetails);
        // Add required entity
        Customer customer;
        if (TestUtil.findAll(em, Customer.class).isEmpty()) {
            customer = CustomerResourceIT.createEntity(em);
            em.persist(customer);
            em.flush();
        } else {
            customer = TestUtil.findAll(em, Customer.class).get(0);
        }
        invoice.setCustomer(customer);
        // Add required entity
        TransactionType transactionType;
        if (TestUtil.findAll(em, TransactionType.class).isEmpty()) {
            transactionType = TransactionTypeResourceIT.createEntity(em);
            em.persist(transactionType);
            em.flush();
        } else {
            transactionType = TestUtil.findAll(em, TransactionType.class).get(0);
        }
        invoice.setTransactionType(transactionType);
        // Add required entity
        Location location;
        if (TestUtil.findAll(em, Location.class).isEmpty()) {
            location = LocationResourceIT.createEntity(em);
            em.persist(location);
            em.flush();
        } else {
            location = TestUtil.findAll(em, Location.class).get(0);
        }
        invoice.setLocation(location);
        return invoice;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Invoice createUpdatedEntity(EntityManager em) {
        Invoice invoice = new Invoice()
            .invNumber(UPDATED_INV_NUMBER)
            .invDate(UPDATED_INV_DATE)
            .invTotalAmount(UPDATED_INV_TOTAL_AMOUNT)
            .cashAmount(UPDATED_CASH_AMOUNT)
            .cardAmount(UPDATED_CARD_AMOUNT)
            .dueAmount(UPDATED_DUE_AMOUNT);
        // Add required entity
        InvoiceDetails invoiceDetails;
        if (TestUtil.findAll(em, InvoiceDetails.class).isEmpty()) {
            invoiceDetails = InvoiceDetailsResourceIT.createUpdatedEntity(em);
            em.persist(invoiceDetails);
            em.flush();
        } else {
            invoiceDetails = TestUtil.findAll(em, InvoiceDetails.class).get(0);
        }
        invoice.getDetails().add(invoiceDetails);
        // Add required entity
        Customer customer;
        if (TestUtil.findAll(em, Customer.class).isEmpty()) {
            customer = CustomerResourceIT.createUpdatedEntity(em);
            em.persist(customer);
            em.flush();
        } else {
            customer = TestUtil.findAll(em, Customer.class).get(0);
        }
        invoice.setCustomer(customer);
        // Add required entity
        TransactionType transactionType;
        if (TestUtil.findAll(em, TransactionType.class).isEmpty()) {
            transactionType = TransactionTypeResourceIT.createUpdatedEntity(em);
            em.persist(transactionType);
            em.flush();
        } else {
            transactionType = TestUtil.findAll(em, TransactionType.class).get(0);
        }
        invoice.setTransactionType(transactionType);
        // Add required entity
        Location location;
        if (TestUtil.findAll(em, Location.class).isEmpty()) {
            location = LocationResourceIT.createUpdatedEntity(em);
            em.persist(location);
            em.flush();
        } else {
            location = TestUtil.findAll(em, Location.class).get(0);
        }
        invoice.setLocation(location);
        return invoice;
    }

    @BeforeEach
    public void initTest() {
        invoice = createEntity(em);
    }

    @Test
    @Transactional
    public void createInvoice() throws Exception {
        int databaseSizeBeforeCreate = invoiceRepository.findAll().size();

        // Create the Invoice
        restInvoiceMockMvc.perform(post("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoice)))
            .andExpect(status().isCreated());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeCreate + 1);
        Invoice testInvoice = invoiceList.get(invoiceList.size() - 1);
        assertThat(testInvoice.getInvNumber()).isEqualTo(DEFAULT_INV_NUMBER);
        assertThat(testInvoice.getInvDate()).isEqualTo(DEFAULT_INV_DATE);
        assertThat(testInvoice.getInvTotalAmount()).isEqualTo(DEFAULT_INV_TOTAL_AMOUNT);
        assertThat(testInvoice.getCashAmount()).isEqualTo(DEFAULT_CASH_AMOUNT);
        assertThat(testInvoice.getCardAmount()).isEqualTo(DEFAULT_CARD_AMOUNT);
        assertThat(testInvoice.getDueAmount()).isEqualTo(DEFAULT_DUE_AMOUNT);
    }

    @Test
    @Transactional
    public void createInvoiceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = invoiceRepository.findAll().size();

        // Create the Invoice with an existing ID
        invoice.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvoiceMockMvc.perform(post("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoice)))
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkInvNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceRepository.findAll().size();
        // set the field null
        invoice.setInvNumber(null);

        // Create the Invoice, which fails.

        restInvoiceMockMvc.perform(post("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoice)))
            .andExpect(status().isBadRequest());

        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkInvDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceRepository.findAll().size();
        // set the field null
        invoice.setInvDate(null);

        // Create the Invoice, which fails.

        restInvoiceMockMvc.perform(post("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoice)))
            .andExpect(status().isBadRequest());

        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkInvTotalAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceRepository.findAll().size();
        // set the field null
        invoice.setInvTotalAmount(null);

        // Create the Invoice, which fails.

        restInvoiceMockMvc.perform(post("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoice)))
            .andExpect(status().isBadRequest());

        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInvoices() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList
        restInvoiceMockMvc.perform(get("/api/invoices?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoice.getId().intValue())))
            .andExpect(jsonPath("$.[*].invNumber").value(hasItem(DEFAULT_INV_NUMBER)))
            .andExpect(jsonPath("$.[*].invDate").value(hasItem(DEFAULT_INV_DATE.toString())))
            .andExpect(jsonPath("$.[*].invTotalAmount").value(hasItem(DEFAULT_INV_TOTAL_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].cashAmount").value(hasItem(DEFAULT_CASH_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].cardAmount").value(hasItem(DEFAULT_CARD_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].dueAmount").value(hasItem(DEFAULT_DUE_AMOUNT.intValue())));
    }
    
    @Test
    @Transactional
    public void getInvoice() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get the invoice
        restInvoiceMockMvc.perform(get("/api/invoices/{id}", invoice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(invoice.getId().intValue()))
            .andExpect(jsonPath("$.invNumber").value(DEFAULT_INV_NUMBER))
            .andExpect(jsonPath("$.invDate").value(DEFAULT_INV_DATE.toString()))
            .andExpect(jsonPath("$.invTotalAmount").value(DEFAULT_INV_TOTAL_AMOUNT.intValue()))
            .andExpect(jsonPath("$.cashAmount").value(DEFAULT_CASH_AMOUNT.intValue()))
            .andExpect(jsonPath("$.cardAmount").value(DEFAULT_CARD_AMOUNT.intValue()))
            .andExpect(jsonPath("$.dueAmount").value(DEFAULT_DUE_AMOUNT.intValue()));
    }


    @Test
    @Transactional
    public void getInvoicesByIdFiltering() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        Long id = invoice.getId();

        defaultInvoiceShouldBeFound("id.equals=" + id);
        defaultInvoiceShouldNotBeFound("id.notEquals=" + id);

        defaultInvoiceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultInvoiceShouldNotBeFound("id.greaterThan=" + id);

        defaultInvoiceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultInvoiceShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllInvoicesByInvNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invNumber equals to DEFAULT_INV_NUMBER
        defaultInvoiceShouldBeFound("invNumber.equals=" + DEFAULT_INV_NUMBER);

        // Get all the invoiceList where invNumber equals to UPDATED_INV_NUMBER
        defaultInvoiceShouldNotBeFound("invNumber.equals=" + UPDATED_INV_NUMBER);
    }

    @Test
    @Transactional
    public void getAllInvoicesByInvNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invNumber not equals to DEFAULT_INV_NUMBER
        defaultInvoiceShouldNotBeFound("invNumber.notEquals=" + DEFAULT_INV_NUMBER);

        // Get all the invoiceList where invNumber not equals to UPDATED_INV_NUMBER
        defaultInvoiceShouldBeFound("invNumber.notEquals=" + UPDATED_INV_NUMBER);
    }

    @Test
    @Transactional
    public void getAllInvoicesByInvNumberIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invNumber in DEFAULT_INV_NUMBER or UPDATED_INV_NUMBER
        defaultInvoiceShouldBeFound("invNumber.in=" + DEFAULT_INV_NUMBER + "," + UPDATED_INV_NUMBER);

        // Get all the invoiceList where invNumber equals to UPDATED_INV_NUMBER
        defaultInvoiceShouldNotBeFound("invNumber.in=" + UPDATED_INV_NUMBER);
    }

    @Test
    @Transactional
    public void getAllInvoicesByInvNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invNumber is not null
        defaultInvoiceShouldBeFound("invNumber.specified=true");

        // Get all the invoiceList where invNumber is null
        defaultInvoiceShouldNotBeFound("invNumber.specified=false");
    }
                @Test
    @Transactional
    public void getAllInvoicesByInvNumberContainsSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invNumber contains DEFAULT_INV_NUMBER
        defaultInvoiceShouldBeFound("invNumber.contains=" + DEFAULT_INV_NUMBER);

        // Get all the invoiceList where invNumber contains UPDATED_INV_NUMBER
        defaultInvoiceShouldNotBeFound("invNumber.contains=" + UPDATED_INV_NUMBER);
    }

    @Test
    @Transactional
    public void getAllInvoicesByInvNumberNotContainsSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invNumber does not contain DEFAULT_INV_NUMBER
        defaultInvoiceShouldNotBeFound("invNumber.doesNotContain=" + DEFAULT_INV_NUMBER);

        // Get all the invoiceList where invNumber does not contain UPDATED_INV_NUMBER
        defaultInvoiceShouldBeFound("invNumber.doesNotContain=" + UPDATED_INV_NUMBER);
    }


    @Test
    @Transactional
    public void getAllInvoicesByInvDateIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invDate equals to DEFAULT_INV_DATE
        defaultInvoiceShouldBeFound("invDate.equals=" + DEFAULT_INV_DATE);

        // Get all the invoiceList where invDate equals to UPDATED_INV_DATE
        defaultInvoiceShouldNotBeFound("invDate.equals=" + UPDATED_INV_DATE);
    }

    @Test
    @Transactional
    public void getAllInvoicesByInvDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invDate not equals to DEFAULT_INV_DATE
        defaultInvoiceShouldNotBeFound("invDate.notEquals=" + DEFAULT_INV_DATE);

        // Get all the invoiceList where invDate not equals to UPDATED_INV_DATE
        defaultInvoiceShouldBeFound("invDate.notEquals=" + UPDATED_INV_DATE);
    }

    @Test
    @Transactional
    public void getAllInvoicesByInvDateIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invDate in DEFAULT_INV_DATE or UPDATED_INV_DATE
        defaultInvoiceShouldBeFound("invDate.in=" + DEFAULT_INV_DATE + "," + UPDATED_INV_DATE);

        // Get all the invoiceList where invDate equals to UPDATED_INV_DATE
        defaultInvoiceShouldNotBeFound("invDate.in=" + UPDATED_INV_DATE);
    }

    @Test
    @Transactional
    public void getAllInvoicesByInvDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invDate is not null
        defaultInvoiceShouldBeFound("invDate.specified=true");

        // Get all the invoiceList where invDate is null
        defaultInvoiceShouldNotBeFound("invDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvoicesByInvDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invDate is greater than or equal to DEFAULT_INV_DATE
        defaultInvoiceShouldBeFound("invDate.greaterThanOrEqual=" + DEFAULT_INV_DATE);

        // Get all the invoiceList where invDate is greater than or equal to UPDATED_INV_DATE
        defaultInvoiceShouldNotBeFound("invDate.greaterThanOrEqual=" + UPDATED_INV_DATE);
    }

    @Test
    @Transactional
    public void getAllInvoicesByInvDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invDate is less than or equal to DEFAULT_INV_DATE
        defaultInvoiceShouldBeFound("invDate.lessThanOrEqual=" + DEFAULT_INV_DATE);

        // Get all the invoiceList where invDate is less than or equal to SMALLER_INV_DATE
        defaultInvoiceShouldNotBeFound("invDate.lessThanOrEqual=" + SMALLER_INV_DATE);
    }

    @Test
    @Transactional
    public void getAllInvoicesByInvDateIsLessThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invDate is less than DEFAULT_INV_DATE
        defaultInvoiceShouldNotBeFound("invDate.lessThan=" + DEFAULT_INV_DATE);

        // Get all the invoiceList where invDate is less than UPDATED_INV_DATE
        defaultInvoiceShouldBeFound("invDate.lessThan=" + UPDATED_INV_DATE);
    }

    @Test
    @Transactional
    public void getAllInvoicesByInvDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invDate is greater than DEFAULT_INV_DATE
        defaultInvoiceShouldNotBeFound("invDate.greaterThan=" + DEFAULT_INV_DATE);

        // Get all the invoiceList where invDate is greater than SMALLER_INV_DATE
        defaultInvoiceShouldBeFound("invDate.greaterThan=" + SMALLER_INV_DATE);
    }


    @Test
    @Transactional
    public void getAllInvoicesByInvTotalAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invTotalAmount equals to DEFAULT_INV_TOTAL_AMOUNT
        defaultInvoiceShouldBeFound("invTotalAmount.equals=" + DEFAULT_INV_TOTAL_AMOUNT);

        // Get all the invoiceList where invTotalAmount equals to UPDATED_INV_TOTAL_AMOUNT
        defaultInvoiceShouldNotBeFound("invTotalAmount.equals=" + UPDATED_INV_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllInvoicesByInvTotalAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invTotalAmount not equals to DEFAULT_INV_TOTAL_AMOUNT
        defaultInvoiceShouldNotBeFound("invTotalAmount.notEquals=" + DEFAULT_INV_TOTAL_AMOUNT);

        // Get all the invoiceList where invTotalAmount not equals to UPDATED_INV_TOTAL_AMOUNT
        defaultInvoiceShouldBeFound("invTotalAmount.notEquals=" + UPDATED_INV_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllInvoicesByInvTotalAmountIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invTotalAmount in DEFAULT_INV_TOTAL_AMOUNT or UPDATED_INV_TOTAL_AMOUNT
        defaultInvoiceShouldBeFound("invTotalAmount.in=" + DEFAULT_INV_TOTAL_AMOUNT + "," + UPDATED_INV_TOTAL_AMOUNT);

        // Get all the invoiceList where invTotalAmount equals to UPDATED_INV_TOTAL_AMOUNT
        defaultInvoiceShouldNotBeFound("invTotalAmount.in=" + UPDATED_INV_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllInvoicesByInvTotalAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invTotalAmount is not null
        defaultInvoiceShouldBeFound("invTotalAmount.specified=true");

        // Get all the invoiceList where invTotalAmount is null
        defaultInvoiceShouldNotBeFound("invTotalAmount.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvoicesByInvTotalAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invTotalAmount is greater than or equal to DEFAULT_INV_TOTAL_AMOUNT
        defaultInvoiceShouldBeFound("invTotalAmount.greaterThanOrEqual=" + DEFAULT_INV_TOTAL_AMOUNT);

        // Get all the invoiceList where invTotalAmount is greater than or equal to UPDATED_INV_TOTAL_AMOUNT
        defaultInvoiceShouldNotBeFound("invTotalAmount.greaterThanOrEqual=" + UPDATED_INV_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllInvoicesByInvTotalAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invTotalAmount is less than or equal to DEFAULT_INV_TOTAL_AMOUNT
        defaultInvoiceShouldBeFound("invTotalAmount.lessThanOrEqual=" + DEFAULT_INV_TOTAL_AMOUNT);

        // Get all the invoiceList where invTotalAmount is less than or equal to SMALLER_INV_TOTAL_AMOUNT
        defaultInvoiceShouldNotBeFound("invTotalAmount.lessThanOrEqual=" + SMALLER_INV_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllInvoicesByInvTotalAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invTotalAmount is less than DEFAULT_INV_TOTAL_AMOUNT
        defaultInvoiceShouldNotBeFound("invTotalAmount.lessThan=" + DEFAULT_INV_TOTAL_AMOUNT);

        // Get all the invoiceList where invTotalAmount is less than UPDATED_INV_TOTAL_AMOUNT
        defaultInvoiceShouldBeFound("invTotalAmount.lessThan=" + UPDATED_INV_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllInvoicesByInvTotalAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invTotalAmount is greater than DEFAULT_INV_TOTAL_AMOUNT
        defaultInvoiceShouldNotBeFound("invTotalAmount.greaterThan=" + DEFAULT_INV_TOTAL_AMOUNT);

        // Get all the invoiceList where invTotalAmount is greater than SMALLER_INV_TOTAL_AMOUNT
        defaultInvoiceShouldBeFound("invTotalAmount.greaterThan=" + SMALLER_INV_TOTAL_AMOUNT);
    }


    @Test
    @Transactional
    public void getAllInvoicesByCashAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where cashAmount equals to DEFAULT_CASH_AMOUNT
        defaultInvoiceShouldBeFound("cashAmount.equals=" + DEFAULT_CASH_AMOUNT);

        // Get all the invoiceList where cashAmount equals to UPDATED_CASH_AMOUNT
        defaultInvoiceShouldNotBeFound("cashAmount.equals=" + UPDATED_CASH_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllInvoicesByCashAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where cashAmount not equals to DEFAULT_CASH_AMOUNT
        defaultInvoiceShouldNotBeFound("cashAmount.notEquals=" + DEFAULT_CASH_AMOUNT);

        // Get all the invoiceList where cashAmount not equals to UPDATED_CASH_AMOUNT
        defaultInvoiceShouldBeFound("cashAmount.notEquals=" + UPDATED_CASH_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllInvoicesByCashAmountIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where cashAmount in DEFAULT_CASH_AMOUNT or UPDATED_CASH_AMOUNT
        defaultInvoiceShouldBeFound("cashAmount.in=" + DEFAULT_CASH_AMOUNT + "," + UPDATED_CASH_AMOUNT);

        // Get all the invoiceList where cashAmount equals to UPDATED_CASH_AMOUNT
        defaultInvoiceShouldNotBeFound("cashAmount.in=" + UPDATED_CASH_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllInvoicesByCashAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where cashAmount is not null
        defaultInvoiceShouldBeFound("cashAmount.specified=true");

        // Get all the invoiceList where cashAmount is null
        defaultInvoiceShouldNotBeFound("cashAmount.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvoicesByCashAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where cashAmount is greater than or equal to DEFAULT_CASH_AMOUNT
        defaultInvoiceShouldBeFound("cashAmount.greaterThanOrEqual=" + DEFAULT_CASH_AMOUNT);

        // Get all the invoiceList where cashAmount is greater than or equal to UPDATED_CASH_AMOUNT
        defaultInvoiceShouldNotBeFound("cashAmount.greaterThanOrEqual=" + UPDATED_CASH_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllInvoicesByCashAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where cashAmount is less than or equal to DEFAULT_CASH_AMOUNT
        defaultInvoiceShouldBeFound("cashAmount.lessThanOrEqual=" + DEFAULT_CASH_AMOUNT);

        // Get all the invoiceList where cashAmount is less than or equal to SMALLER_CASH_AMOUNT
        defaultInvoiceShouldNotBeFound("cashAmount.lessThanOrEqual=" + SMALLER_CASH_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllInvoicesByCashAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where cashAmount is less than DEFAULT_CASH_AMOUNT
        defaultInvoiceShouldNotBeFound("cashAmount.lessThan=" + DEFAULT_CASH_AMOUNT);

        // Get all the invoiceList where cashAmount is less than UPDATED_CASH_AMOUNT
        defaultInvoiceShouldBeFound("cashAmount.lessThan=" + UPDATED_CASH_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllInvoicesByCashAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where cashAmount is greater than DEFAULT_CASH_AMOUNT
        defaultInvoiceShouldNotBeFound("cashAmount.greaterThan=" + DEFAULT_CASH_AMOUNT);

        // Get all the invoiceList where cashAmount is greater than SMALLER_CASH_AMOUNT
        defaultInvoiceShouldBeFound("cashAmount.greaterThan=" + SMALLER_CASH_AMOUNT);
    }


    @Test
    @Transactional
    public void getAllInvoicesByCardAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where cardAmount equals to DEFAULT_CARD_AMOUNT
        defaultInvoiceShouldBeFound("cardAmount.equals=" + DEFAULT_CARD_AMOUNT);

        // Get all the invoiceList where cardAmount equals to UPDATED_CARD_AMOUNT
        defaultInvoiceShouldNotBeFound("cardAmount.equals=" + UPDATED_CARD_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllInvoicesByCardAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where cardAmount not equals to DEFAULT_CARD_AMOUNT
        defaultInvoiceShouldNotBeFound("cardAmount.notEquals=" + DEFAULT_CARD_AMOUNT);

        // Get all the invoiceList where cardAmount not equals to UPDATED_CARD_AMOUNT
        defaultInvoiceShouldBeFound("cardAmount.notEquals=" + UPDATED_CARD_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllInvoicesByCardAmountIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where cardAmount in DEFAULT_CARD_AMOUNT or UPDATED_CARD_AMOUNT
        defaultInvoiceShouldBeFound("cardAmount.in=" + DEFAULT_CARD_AMOUNT + "," + UPDATED_CARD_AMOUNT);

        // Get all the invoiceList where cardAmount equals to UPDATED_CARD_AMOUNT
        defaultInvoiceShouldNotBeFound("cardAmount.in=" + UPDATED_CARD_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllInvoicesByCardAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where cardAmount is not null
        defaultInvoiceShouldBeFound("cardAmount.specified=true");

        // Get all the invoiceList where cardAmount is null
        defaultInvoiceShouldNotBeFound("cardAmount.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvoicesByCardAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where cardAmount is greater than or equal to DEFAULT_CARD_AMOUNT
        defaultInvoiceShouldBeFound("cardAmount.greaterThanOrEqual=" + DEFAULT_CARD_AMOUNT);

        // Get all the invoiceList where cardAmount is greater than or equal to UPDATED_CARD_AMOUNT
        defaultInvoiceShouldNotBeFound("cardAmount.greaterThanOrEqual=" + UPDATED_CARD_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllInvoicesByCardAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where cardAmount is less than or equal to DEFAULT_CARD_AMOUNT
        defaultInvoiceShouldBeFound("cardAmount.lessThanOrEqual=" + DEFAULT_CARD_AMOUNT);

        // Get all the invoiceList where cardAmount is less than or equal to SMALLER_CARD_AMOUNT
        defaultInvoiceShouldNotBeFound("cardAmount.lessThanOrEqual=" + SMALLER_CARD_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllInvoicesByCardAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where cardAmount is less than DEFAULT_CARD_AMOUNT
        defaultInvoiceShouldNotBeFound("cardAmount.lessThan=" + DEFAULT_CARD_AMOUNT);

        // Get all the invoiceList where cardAmount is less than UPDATED_CARD_AMOUNT
        defaultInvoiceShouldBeFound("cardAmount.lessThan=" + UPDATED_CARD_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllInvoicesByCardAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where cardAmount is greater than DEFAULT_CARD_AMOUNT
        defaultInvoiceShouldNotBeFound("cardAmount.greaterThan=" + DEFAULT_CARD_AMOUNT);

        // Get all the invoiceList where cardAmount is greater than SMALLER_CARD_AMOUNT
        defaultInvoiceShouldBeFound("cardAmount.greaterThan=" + SMALLER_CARD_AMOUNT);
    }


    @Test
    @Transactional
    public void getAllInvoicesByDueAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where dueAmount equals to DEFAULT_DUE_AMOUNT
        defaultInvoiceShouldBeFound("dueAmount.equals=" + DEFAULT_DUE_AMOUNT);

        // Get all the invoiceList where dueAmount equals to UPDATED_DUE_AMOUNT
        defaultInvoiceShouldNotBeFound("dueAmount.equals=" + UPDATED_DUE_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllInvoicesByDueAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where dueAmount not equals to DEFAULT_DUE_AMOUNT
        defaultInvoiceShouldNotBeFound("dueAmount.notEquals=" + DEFAULT_DUE_AMOUNT);

        // Get all the invoiceList where dueAmount not equals to UPDATED_DUE_AMOUNT
        defaultInvoiceShouldBeFound("dueAmount.notEquals=" + UPDATED_DUE_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllInvoicesByDueAmountIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where dueAmount in DEFAULT_DUE_AMOUNT or UPDATED_DUE_AMOUNT
        defaultInvoiceShouldBeFound("dueAmount.in=" + DEFAULT_DUE_AMOUNT + "," + UPDATED_DUE_AMOUNT);

        // Get all the invoiceList where dueAmount equals to UPDATED_DUE_AMOUNT
        defaultInvoiceShouldNotBeFound("dueAmount.in=" + UPDATED_DUE_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllInvoicesByDueAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where dueAmount is not null
        defaultInvoiceShouldBeFound("dueAmount.specified=true");

        // Get all the invoiceList where dueAmount is null
        defaultInvoiceShouldNotBeFound("dueAmount.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvoicesByDueAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where dueAmount is greater than or equal to DEFAULT_DUE_AMOUNT
        defaultInvoiceShouldBeFound("dueAmount.greaterThanOrEqual=" + DEFAULT_DUE_AMOUNT);

        // Get all the invoiceList where dueAmount is greater than or equal to UPDATED_DUE_AMOUNT
        defaultInvoiceShouldNotBeFound("dueAmount.greaterThanOrEqual=" + UPDATED_DUE_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllInvoicesByDueAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where dueAmount is less than or equal to DEFAULT_DUE_AMOUNT
        defaultInvoiceShouldBeFound("dueAmount.lessThanOrEqual=" + DEFAULT_DUE_AMOUNT);

        // Get all the invoiceList where dueAmount is less than or equal to SMALLER_DUE_AMOUNT
        defaultInvoiceShouldNotBeFound("dueAmount.lessThanOrEqual=" + SMALLER_DUE_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllInvoicesByDueAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where dueAmount is less than DEFAULT_DUE_AMOUNT
        defaultInvoiceShouldNotBeFound("dueAmount.lessThan=" + DEFAULT_DUE_AMOUNT);

        // Get all the invoiceList where dueAmount is less than UPDATED_DUE_AMOUNT
        defaultInvoiceShouldBeFound("dueAmount.lessThan=" + UPDATED_DUE_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllInvoicesByDueAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where dueAmount is greater than DEFAULT_DUE_AMOUNT
        defaultInvoiceShouldNotBeFound("dueAmount.greaterThan=" + DEFAULT_DUE_AMOUNT);

        // Get all the invoiceList where dueAmount is greater than SMALLER_DUE_AMOUNT
        defaultInvoiceShouldBeFound("dueAmount.greaterThan=" + SMALLER_DUE_AMOUNT);
    }


    @Test
    @Transactional
    public void getAllInvoicesByDetailsIsEqualToSomething() throws Exception {
        // Get already existing entity
        InvoiceDetails details = invoice.getDetails();
        invoiceRepository.saveAndFlush(invoice);
        Long detailsId = details.getId();

        // Get all the invoiceList where details equals to detailsId
        defaultInvoiceShouldBeFound("detailsId.equals=" + detailsId);

        // Get all the invoiceList where details equals to detailsId + 1
        defaultInvoiceShouldNotBeFound("detailsId.equals=" + (detailsId + 1));
    }


    @Test
    @Transactional
    public void getAllInvoicesByCustomerIsEqualToSomething() throws Exception {
        // Get already existing entity
        Customer customer = invoice.getCustomer();
        invoiceRepository.saveAndFlush(invoice);
        Long customerId = customer.getId();

        // Get all the invoiceList where customer equals to customerId
        defaultInvoiceShouldBeFound("customerId.equals=" + customerId);

        // Get all the invoiceList where customer equals to customerId + 1
        defaultInvoiceShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }


    @Test
    @Transactional
    public void getAllInvoicesByTransactionTypeIsEqualToSomething() throws Exception {
        // Get already existing entity
        TransactionType transactionType = invoice.getTransactionType();
        invoiceRepository.saveAndFlush(invoice);
        Long transactionTypeId = transactionType.getId();

        // Get all the invoiceList where transactionType equals to transactionTypeId
        defaultInvoiceShouldBeFound("transactionTypeId.equals=" + transactionTypeId);

        // Get all the invoiceList where transactionType equals to transactionTypeId + 1
        defaultInvoiceShouldNotBeFound("transactionTypeId.equals=" + (transactionTypeId + 1));
    }


    @Test
    @Transactional
    public void getAllInvoicesByLocationIsEqualToSomething() throws Exception {
        // Get already existing entity
        Location location = invoice.getLocation();
        invoiceRepository.saveAndFlush(invoice);
        Long locationId = location.getId();

        // Get all the invoiceList where location equals to locationId
        defaultInvoiceShouldBeFound("locationId.equals=" + locationId);

        // Get all the invoiceList where location equals to locationId + 1
        defaultInvoiceShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInvoiceShouldBeFound(String filter) throws Exception {
        restInvoiceMockMvc.perform(get("/api/invoices?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoice.getId().intValue())))
            .andExpect(jsonPath("$.[*].invNumber").value(hasItem(DEFAULT_INV_NUMBER)))
            .andExpect(jsonPath("$.[*].invDate").value(hasItem(DEFAULT_INV_DATE.toString())))
            .andExpect(jsonPath("$.[*].invTotalAmount").value(hasItem(DEFAULT_INV_TOTAL_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].cashAmount").value(hasItem(DEFAULT_CASH_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].cardAmount").value(hasItem(DEFAULT_CARD_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].dueAmount").value(hasItem(DEFAULT_DUE_AMOUNT.intValue())));

        // Check, that the count call also returns 1
        restInvoiceMockMvc.perform(get("/api/invoices/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInvoiceShouldNotBeFound(String filter) throws Exception {
        restInvoiceMockMvc.perform(get("/api/invoices?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInvoiceMockMvc.perform(get("/api/invoices/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingInvoice() throws Exception {
        // Get the invoice
        restInvoiceMockMvc.perform(get("/api/invoices/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInvoice() throws Exception {
        // Initialize the database
        invoiceService.save(invoice);

        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();

        // Update the invoice
        Invoice updatedInvoice = invoiceRepository.findById(invoice.getId()).get();
        // Disconnect from session so that the updates on updatedInvoice are not directly saved in db
        em.detach(updatedInvoice);
        updatedInvoice
            .invNumber(UPDATED_INV_NUMBER)
            .invDate(UPDATED_INV_DATE)
            .invTotalAmount(UPDATED_INV_TOTAL_AMOUNT)
            .cashAmount(UPDATED_CASH_AMOUNT)
            .cardAmount(UPDATED_CARD_AMOUNT)
            .dueAmount(UPDATED_DUE_AMOUNT);

        restInvoiceMockMvc.perform(put("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedInvoice)))
            .andExpect(status().isOk());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
        Invoice testInvoice = invoiceList.get(invoiceList.size() - 1);
        assertThat(testInvoice.getInvNumber()).isEqualTo(UPDATED_INV_NUMBER);
        assertThat(testInvoice.getInvDate()).isEqualTo(UPDATED_INV_DATE);
        assertThat(testInvoice.getInvTotalAmount()).isEqualTo(UPDATED_INV_TOTAL_AMOUNT);
        assertThat(testInvoice.getCashAmount()).isEqualTo(UPDATED_CASH_AMOUNT);
        assertThat(testInvoice.getCardAmount()).isEqualTo(UPDATED_CARD_AMOUNT);
        assertThat(testInvoice.getDueAmount()).isEqualTo(UPDATED_DUE_AMOUNT);
    }

    @Test
    @Transactional
    public void updateNonExistingInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();

        // Create the Invoice

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceMockMvc.perform(put("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoice)))
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteInvoice() throws Exception {
        // Initialize the database
        invoiceService.save(invoice);

        int databaseSizeBeforeDelete = invoiceRepository.findAll().size();

        // Delete the invoice
        restInvoiceMockMvc.perform(delete("/api/invoices/{id}", invoice.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

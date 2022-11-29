package com.alphadevs.sales.web.rest;

import com.alphadevs.sales.WikunumApp;
import com.alphadevs.sales.domain.Products;
import com.alphadevs.sales.domain.DocumentHistory;
import com.alphadevs.sales.domain.Location;
import com.alphadevs.sales.repository.ProductsRepository;
import com.alphadevs.sales.service.ProductsService;
import com.alphadevs.sales.web.rest.errors.ExceptionTranslator;
import com.alphadevs.sales.service.dto.ProductsCriteria;
import com.alphadevs.sales.service.ProductsQueryService;

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
 * Integration tests for the {@link ProductsResource} REST controller.
 */
@SpringBootTest(classes = WikunumApp.class)
public class ProductsResourceIT {

    private static final String DEFAULT_PRODUCT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_NAME = "BBBBBBBBBB";

    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private ProductsService productsService;

    @Autowired
    private ProductsQueryService productsQueryService;

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

    private MockMvc restProductsMockMvc;

    private Products products;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProductsResource productsResource = new ProductsResource(productsService, productsQueryService);
        this.restProductsMockMvc = MockMvcBuilders.standaloneSetup(productsResource)
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
    public static Products createEntity(EntityManager em) {
        Products products = new Products()
            .productCode(DEFAULT_PRODUCT_CODE)
            .productName(DEFAULT_PRODUCT_NAME);
        // Add required entity
        Location location;
        if (TestUtil.findAll(em, Location.class).isEmpty()) {
            location = LocationResourceIT.createEntity(em);
            em.persist(location);
            em.flush();
        } else {
            location = TestUtil.findAll(em, Location.class).get(0);
        }
        products.setLocation(location);
        return products;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Products createUpdatedEntity(EntityManager em) {
        Products products = new Products()
            .productCode(UPDATED_PRODUCT_CODE)
            .productName(UPDATED_PRODUCT_NAME);
        // Add required entity
        Location location;
        if (TestUtil.findAll(em, Location.class).isEmpty()) {
            location = LocationResourceIT.createUpdatedEntity(em);
            em.persist(location);
            em.flush();
        } else {
            location = TestUtil.findAll(em, Location.class).get(0);
        }
        products.setLocation(location);
        return products;
    }

    @BeforeEach
    public void initTest() {
        products = createEntity(em);
    }

    @Test
    @Transactional
    public void createProducts() throws Exception {
        int databaseSizeBeforeCreate = productsRepository.findAll().size();

        // Create the Products
        restProductsMockMvc.perform(post("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(products)))
            .andExpect(status().isCreated());

        // Validate the Products in the database
        List<Products> productsList = productsRepository.findAll();
        assertThat(productsList).hasSize(databaseSizeBeforeCreate + 1);
        Products testProducts = productsList.get(productsList.size() - 1);
        assertThat(testProducts.getProductCode()).isEqualTo(DEFAULT_PRODUCT_CODE);
        assertThat(testProducts.getProductName()).isEqualTo(DEFAULT_PRODUCT_NAME);
    }

    @Test
    @Transactional
    public void createProductsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = productsRepository.findAll().size();

        // Create the Products with an existing ID
        products.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductsMockMvc.perform(post("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(products)))
            .andExpect(status().isBadRequest());

        // Validate the Products in the database
        List<Products> productsList = productsRepository.findAll();
        assertThat(productsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkProductCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = productsRepository.findAll().size();
        // set the field null
        products.setProductCode(null);

        // Create the Products, which fails.

        restProductsMockMvc.perform(post("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(products)))
            .andExpect(status().isBadRequest());

        List<Products> productsList = productsRepository.findAll();
        assertThat(productsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProductNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = productsRepository.findAll().size();
        // set the field null
        products.setProductName(null);

        // Create the Products, which fails.

        restProductsMockMvc.perform(post("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(products)))
            .andExpect(status().isBadRequest());

        List<Products> productsList = productsRepository.findAll();
        assertThat(productsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProducts() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList
        restProductsMockMvc.perform(get("/api/products?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(products.getId().intValue())))
            .andExpect(jsonPath("$.[*].productCode").value(hasItem(DEFAULT_PRODUCT_CODE)))
            .andExpect(jsonPath("$.[*].productName").value(hasItem(DEFAULT_PRODUCT_NAME)));
    }
    
    @Test
    @Transactional
    public void getProducts() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get the products
        restProductsMockMvc.perform(get("/api/products/{id}", products.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(products.getId().intValue()))
            .andExpect(jsonPath("$.productCode").value(DEFAULT_PRODUCT_CODE))
            .andExpect(jsonPath("$.productName").value(DEFAULT_PRODUCT_NAME));
    }


    @Test
    @Transactional
    public void getProductsByIdFiltering() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        Long id = products.getId();

        defaultProductsShouldBeFound("id.equals=" + id);
        defaultProductsShouldNotBeFound("id.notEquals=" + id);

        defaultProductsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProductsShouldNotBeFound("id.greaterThan=" + id);

        defaultProductsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProductsShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllProductsByProductCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where productCode equals to DEFAULT_PRODUCT_CODE
        defaultProductsShouldBeFound("productCode.equals=" + DEFAULT_PRODUCT_CODE);

        // Get all the productsList where productCode equals to UPDATED_PRODUCT_CODE
        defaultProductsShouldNotBeFound("productCode.equals=" + UPDATED_PRODUCT_CODE);
    }

    @Test
    @Transactional
    public void getAllProductsByProductCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where productCode not equals to DEFAULT_PRODUCT_CODE
        defaultProductsShouldNotBeFound("productCode.notEquals=" + DEFAULT_PRODUCT_CODE);

        // Get all the productsList where productCode not equals to UPDATED_PRODUCT_CODE
        defaultProductsShouldBeFound("productCode.notEquals=" + UPDATED_PRODUCT_CODE);
    }

    @Test
    @Transactional
    public void getAllProductsByProductCodeIsInShouldWork() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where productCode in DEFAULT_PRODUCT_CODE or UPDATED_PRODUCT_CODE
        defaultProductsShouldBeFound("productCode.in=" + DEFAULT_PRODUCT_CODE + "," + UPDATED_PRODUCT_CODE);

        // Get all the productsList where productCode equals to UPDATED_PRODUCT_CODE
        defaultProductsShouldNotBeFound("productCode.in=" + UPDATED_PRODUCT_CODE);
    }

    @Test
    @Transactional
    public void getAllProductsByProductCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where productCode is not null
        defaultProductsShouldBeFound("productCode.specified=true");

        // Get all the productsList where productCode is null
        defaultProductsShouldNotBeFound("productCode.specified=false");
    }
                @Test
    @Transactional
    public void getAllProductsByProductCodeContainsSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where productCode contains DEFAULT_PRODUCT_CODE
        defaultProductsShouldBeFound("productCode.contains=" + DEFAULT_PRODUCT_CODE);

        // Get all the productsList where productCode contains UPDATED_PRODUCT_CODE
        defaultProductsShouldNotBeFound("productCode.contains=" + UPDATED_PRODUCT_CODE);
    }

    @Test
    @Transactional
    public void getAllProductsByProductCodeNotContainsSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where productCode does not contain DEFAULT_PRODUCT_CODE
        defaultProductsShouldNotBeFound("productCode.doesNotContain=" + DEFAULT_PRODUCT_CODE);

        // Get all the productsList where productCode does not contain UPDATED_PRODUCT_CODE
        defaultProductsShouldBeFound("productCode.doesNotContain=" + UPDATED_PRODUCT_CODE);
    }


    @Test
    @Transactional
    public void getAllProductsByProductNameIsEqualToSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where productName equals to DEFAULT_PRODUCT_NAME
        defaultProductsShouldBeFound("productName.equals=" + DEFAULT_PRODUCT_NAME);

        // Get all the productsList where productName equals to UPDATED_PRODUCT_NAME
        defaultProductsShouldNotBeFound("productName.equals=" + UPDATED_PRODUCT_NAME);
    }

    @Test
    @Transactional
    public void getAllProductsByProductNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where productName not equals to DEFAULT_PRODUCT_NAME
        defaultProductsShouldNotBeFound("productName.notEquals=" + DEFAULT_PRODUCT_NAME);

        // Get all the productsList where productName not equals to UPDATED_PRODUCT_NAME
        defaultProductsShouldBeFound("productName.notEquals=" + UPDATED_PRODUCT_NAME);
    }

    @Test
    @Transactional
    public void getAllProductsByProductNameIsInShouldWork() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where productName in DEFAULT_PRODUCT_NAME or UPDATED_PRODUCT_NAME
        defaultProductsShouldBeFound("productName.in=" + DEFAULT_PRODUCT_NAME + "," + UPDATED_PRODUCT_NAME);

        // Get all the productsList where productName equals to UPDATED_PRODUCT_NAME
        defaultProductsShouldNotBeFound("productName.in=" + UPDATED_PRODUCT_NAME);
    }

    @Test
    @Transactional
    public void getAllProductsByProductNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where productName is not null
        defaultProductsShouldBeFound("productName.specified=true");

        // Get all the productsList where productName is null
        defaultProductsShouldNotBeFound("productName.specified=false");
    }
                @Test
    @Transactional
    public void getAllProductsByProductNameContainsSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where productName contains DEFAULT_PRODUCT_NAME
        defaultProductsShouldBeFound("productName.contains=" + DEFAULT_PRODUCT_NAME);

        // Get all the productsList where productName contains UPDATED_PRODUCT_NAME
        defaultProductsShouldNotBeFound("productName.contains=" + UPDATED_PRODUCT_NAME);
    }

    @Test
    @Transactional
    public void getAllProductsByProductNameNotContainsSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList where productName does not contain DEFAULT_PRODUCT_NAME
        defaultProductsShouldNotBeFound("productName.doesNotContain=" + DEFAULT_PRODUCT_NAME);

        // Get all the productsList where productName does not contain UPDATED_PRODUCT_NAME
        defaultProductsShouldBeFound("productName.doesNotContain=" + UPDATED_PRODUCT_NAME);
    }


    @Test
    @Transactional
    public void getAllProductsByHistoryIsEqualToSomething() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);
        DocumentHistory history = DocumentHistoryResourceIT.createEntity(em);
        em.persist(history);
        em.flush();
        products.setHistory(history);
        productsRepository.saveAndFlush(products);
        Long historyId = history.getId();

        // Get all the productsList where history equals to historyId
        defaultProductsShouldBeFound("historyId.equals=" + historyId);

        // Get all the productsList where history equals to historyId + 1
        defaultProductsShouldNotBeFound("historyId.equals=" + (historyId + 1));
    }


    @Test
    @Transactional
    public void getAllProductsByLocationIsEqualToSomething() throws Exception {
        // Get already existing entity
        Location location = products.getLocation();
        productsRepository.saveAndFlush(products);
        Long locationId = location.getId();

        // Get all the productsList where location equals to locationId
        defaultProductsShouldBeFound("locationId.equals=" + locationId);

        // Get all the productsList where location equals to locationId + 1
        defaultProductsShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductsShouldBeFound(String filter) throws Exception {
        restProductsMockMvc.perform(get("/api/products?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(products.getId().intValue())))
            .andExpect(jsonPath("$.[*].productCode").value(hasItem(DEFAULT_PRODUCT_CODE)))
            .andExpect(jsonPath("$.[*].productName").value(hasItem(DEFAULT_PRODUCT_NAME)));

        // Check, that the count call also returns 1
        restProductsMockMvc.perform(get("/api/products/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductsShouldNotBeFound(String filter) throws Exception {
        restProductsMockMvc.perform(get("/api/products?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductsMockMvc.perform(get("/api/products/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingProducts() throws Exception {
        // Get the products
        restProductsMockMvc.perform(get("/api/products/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProducts() throws Exception {
        // Initialize the database
        productsService.save(products);

        int databaseSizeBeforeUpdate = productsRepository.findAll().size();

        // Update the products
        Products updatedProducts = productsRepository.findById(products.getId()).get();
        // Disconnect from session so that the updates on updatedProducts are not directly saved in db
        em.detach(updatedProducts);
        updatedProducts
            .productCode(UPDATED_PRODUCT_CODE)
            .productName(UPDATED_PRODUCT_NAME);

        restProductsMockMvc.perform(put("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProducts)))
            .andExpect(status().isOk());

        // Validate the Products in the database
        List<Products> productsList = productsRepository.findAll();
        assertThat(productsList).hasSize(databaseSizeBeforeUpdate);
        Products testProducts = productsList.get(productsList.size() - 1);
        assertThat(testProducts.getProductCode()).isEqualTo(UPDATED_PRODUCT_CODE);
        assertThat(testProducts.getProductName()).isEqualTo(UPDATED_PRODUCT_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingProducts() throws Exception {
        int databaseSizeBeforeUpdate = productsRepository.findAll().size();

        // Create the Products

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductsMockMvc.perform(put("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(products)))
            .andExpect(status().isBadRequest());

        // Validate the Products in the database
        List<Products> productsList = productsRepository.findAll();
        assertThat(productsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProducts() throws Exception {
        // Initialize the database
        productsService.save(products);

        int databaseSizeBeforeDelete = productsRepository.findAll().size();

        // Delete the products
        restProductsMockMvc.perform(delete("/api/products/{id}", products.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Products> productsList = productsRepository.findAll();
        assertThat(productsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

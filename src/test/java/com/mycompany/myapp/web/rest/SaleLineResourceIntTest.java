package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Application;
import com.mycompany.myapp.domain.SaleLine;
import com.mycompany.myapp.repository.SaleLineRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the SaleLineResource REST controller.
 *
 * @see SaleLineResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class SaleLineResourceIntTest {


    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_TAX = new BigDecimal(1);
    private static final BigDecimal UPDATED_TAX = new BigDecimal(2);

    @Inject
    private SaleLineRepository saleLineRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSaleLineMockMvc;

    private SaleLine saleLine;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SaleLineResource saleLineResource = new SaleLineResource();
        ReflectionTestUtils.setField(saleLineResource, "saleLineRepository", saleLineRepository);
        this.restSaleLineMockMvc = MockMvcBuilders.standaloneSetup(saleLineResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        saleLine = new SaleLine();
        saleLine.setQuantity(DEFAULT_QUANTITY);
        saleLine.setPrice(DEFAULT_PRICE);
        saleLine.setTax(DEFAULT_TAX);
    }

    @Test
    @Transactional
    public void createSaleLine() throws Exception {
        int databaseSizeBeforeCreate = saleLineRepository.findAll().size();

        // Create the SaleLine

        restSaleLineMockMvc.perform(post("/api/saleLines")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(saleLine)))
                .andExpect(status().isCreated());

        // Validate the SaleLine in the database
        List<SaleLine> saleLines = saleLineRepository.findAll();
        assertThat(saleLines).hasSize(databaseSizeBeforeCreate + 1);
        SaleLine testSaleLine = saleLines.get(saleLines.size() - 1);
        assertThat(testSaleLine.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testSaleLine.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testSaleLine.getTax()).isEqualTo(DEFAULT_TAX);
    }

    @Test
    @Transactional
    public void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = saleLineRepository.findAll().size();
        // set the field null
        saleLine.setQuantity(null);

        // Create the SaleLine, which fails.

        restSaleLineMockMvc.perform(post("/api/saleLines")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(saleLine)))
                .andExpect(status().isBadRequest());

        List<SaleLine> saleLines = saleLineRepository.findAll();
        assertThat(saleLines).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = saleLineRepository.findAll().size();
        // set the field null
        saleLine.setPrice(null);

        // Create the SaleLine, which fails.

        restSaleLineMockMvc.perform(post("/api/saleLines")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(saleLine)))
                .andExpect(status().isBadRequest());

        List<SaleLine> saleLines = saleLineRepository.findAll();
        assertThat(saleLines).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSaleLines() throws Exception {
        // Initialize the database
        saleLineRepository.saveAndFlush(saleLine);

        // Get all the saleLines
        restSaleLineMockMvc.perform(get("/api/saleLines?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(saleLine.getId().intValue())))
                .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
                .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
                .andExpect(jsonPath("$.[*].tax").value(hasItem(DEFAULT_TAX.intValue())));
    }

    @Test
    @Transactional
    public void getSaleLine() throws Exception {
        // Initialize the database
        saleLineRepository.saveAndFlush(saleLine);

        // Get the saleLine
        restSaleLineMockMvc.perform(get("/api/saleLines/{id}", saleLine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(saleLine.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()))
            .andExpect(jsonPath("$.tax").value(DEFAULT_TAX.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSaleLine() throws Exception {
        // Get the saleLine
        restSaleLineMockMvc.perform(get("/api/saleLines/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSaleLine() throws Exception {
        // Initialize the database
        saleLineRepository.saveAndFlush(saleLine);

		int databaseSizeBeforeUpdate = saleLineRepository.findAll().size();

        // Update the saleLine
        saleLine.setQuantity(UPDATED_QUANTITY);
        saleLine.setPrice(UPDATED_PRICE);
        saleLine.setTax(UPDATED_TAX);

        restSaleLineMockMvc.perform(put("/api/saleLines")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(saleLine)))
                .andExpect(status().isOk());

        // Validate the SaleLine in the database
        List<SaleLine> saleLines = saleLineRepository.findAll();
        assertThat(saleLines).hasSize(databaseSizeBeforeUpdate);
        SaleLine testSaleLine = saleLines.get(saleLines.size() - 1);
        assertThat(testSaleLine.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testSaleLine.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testSaleLine.getTax()).isEqualTo(UPDATED_TAX);
    }

    @Test
    @Transactional
    public void deleteSaleLine() throws Exception {
        // Initialize the database
        saleLineRepository.saveAndFlush(saleLine);

		int databaseSizeBeforeDelete = saleLineRepository.findAll().size();

        // Get the saleLine
        restSaleLineMockMvc.perform(delete("/api/saleLines/{id}", saleLine.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<SaleLine> saleLines = saleLineRepository.findAll();
        assertThat(saleLines).hasSize(databaseSizeBeforeDelete - 1);
    }
}

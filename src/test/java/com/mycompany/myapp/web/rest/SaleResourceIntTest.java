package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Application;
import com.mycompany.myapp.domain.Sale;
import com.mycompany.myapp.repository.SaleRepository;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the SaleResource REST controller.
 *
 * @see SaleResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class SaleResourceIntTest {


    private static final LocalDate DEFAULT_FECHA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_COUNTRY = "AAAAA";
    private static final String UPDATED_COUNTRY = "BBBBB";

    private static final BigDecimal DEFAULT_RATE = new BigDecimal(1);
    private static final BigDecimal UPDATED_RATE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_SUB_TOTAL = new BigDecimal(1);
    private static final BigDecimal UPDATED_SUB_TOTAL = new BigDecimal(2);

    private static final BigDecimal DEFAULT_DISCOUNTS = new BigDecimal(1);
    private static final BigDecimal UPDATED_DISCOUNTS = new BigDecimal(2);

    private static final BigDecimal DEFAULT_TAXES = new BigDecimal(1);
    private static final BigDecimal UPDATED_TAXES = new BigDecimal(2);

    private static final BigDecimal DEFAULT_TOTAL = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL = new BigDecimal(2);

    private static final BigDecimal DEFAULT_TOTAL_PAIED = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_PAIED = new BigDecimal(2);

    private static final LocalDate DEFAULT_PRINT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PRINT_DATE = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private SaleRepository saleRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSaleMockMvc;

    private Sale sale;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SaleResource saleResource = new SaleResource();
        ReflectionTestUtils.setField(saleResource, "saleRepository", saleRepository);
        this.restSaleMockMvc = MockMvcBuilders.standaloneSetup(saleResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        sale = new Sale();
        sale.setFecha(DEFAULT_FECHA);
        sale.setCountry(DEFAULT_COUNTRY);
        sale.setRate(DEFAULT_RATE);
        sale.setSubTotal(DEFAULT_SUB_TOTAL);
        sale.setDiscounts(DEFAULT_DISCOUNTS);
        sale.setTaxes(DEFAULT_TAXES);
        sale.setTotal(DEFAULT_TOTAL);
        sale.setTotalPaied(DEFAULT_TOTAL_PAIED);
        sale.setPrintDate(DEFAULT_PRINT_DATE);
    }

    @Test
    @Transactional
    public void createSale() throws Exception {
        int databaseSizeBeforeCreate = saleRepository.findAll().size();

        // Create the Sale

        restSaleMockMvc.perform(post("/api/sales")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(sale)))
                .andExpect(status().isCreated());

        // Validate the Sale in the database
        List<Sale> sales = saleRepository.findAll();
        assertThat(sales).hasSize(databaseSizeBeforeCreate + 1);
        Sale testSale = sales.get(sales.size() - 1);
        assertThat(testSale.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testSale.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testSale.getRate()).isEqualTo(DEFAULT_RATE);
        assertThat(testSale.getSubTotal()).isEqualTo(DEFAULT_SUB_TOTAL);
        assertThat(testSale.getDiscounts()).isEqualTo(DEFAULT_DISCOUNTS);
        assertThat(testSale.getTaxes()).isEqualTo(DEFAULT_TAXES);
        assertThat(testSale.getTotal()).isEqualTo(DEFAULT_TOTAL);
        assertThat(testSale.getTotalPaied()).isEqualTo(DEFAULT_TOTAL_PAIED);
        assertThat(testSale.getPrintDate()).isEqualTo(DEFAULT_PRINT_DATE);
    }

    @Test
    @Transactional
    public void checkFechaIsRequired() throws Exception {
        int databaseSizeBeforeTest = saleRepository.findAll().size();
        // set the field null
        sale.setFecha(null);

        // Create the Sale, which fails.

        restSaleMockMvc.perform(post("/api/sales")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(sale)))
                .andExpect(status().isBadRequest());

        List<Sale> sales = saleRepository.findAll();
        assertThat(sales).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCountryIsRequired() throws Exception {
        int databaseSizeBeforeTest = saleRepository.findAll().size();
        // set the field null
        sale.setCountry(null);

        // Create the Sale, which fails.

        restSaleMockMvc.perform(post("/api/sales")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(sale)))
                .andExpect(status().isBadRequest());

        List<Sale> sales = saleRepository.findAll();
        assertThat(sales).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSales() throws Exception {
        // Initialize the database
        saleRepository.saveAndFlush(sale);

        // Get all the sales
        restSaleMockMvc.perform(get("/api/sales?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(sale.getId().intValue())))
                .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
                .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())))
                .andExpect(jsonPath("$.[*].rate").value(hasItem(DEFAULT_RATE.intValue())))
                .andExpect(jsonPath("$.[*].subTotal").value(hasItem(DEFAULT_SUB_TOTAL.intValue())))
                .andExpect(jsonPath("$.[*].discounts").value(hasItem(DEFAULT_DISCOUNTS.intValue())))
                .andExpect(jsonPath("$.[*].taxes").value(hasItem(DEFAULT_TAXES.intValue())))
                .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.intValue())))
                .andExpect(jsonPath("$.[*].totalPaied").value(hasItem(DEFAULT_TOTAL_PAIED.intValue())))
                .andExpect(jsonPath("$.[*].printDate").value(hasItem(DEFAULT_PRINT_DATE.toString())));
    }

    @Test
    @Transactional
    public void getSale() throws Exception {
        // Initialize the database
        saleRepository.saveAndFlush(sale);

        // Get the sale
        restSaleMockMvc.perform(get("/api/sales/{id}", sale.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(sale.getId().intValue()))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY.toString()))
            .andExpect(jsonPath("$.rate").value(DEFAULT_RATE.intValue()))
            .andExpect(jsonPath("$.subTotal").value(DEFAULT_SUB_TOTAL.intValue()))
            .andExpect(jsonPath("$.discounts").value(DEFAULT_DISCOUNTS.intValue()))
            .andExpect(jsonPath("$.taxes").value(DEFAULT_TAXES.intValue()))
            .andExpect(jsonPath("$.total").value(DEFAULT_TOTAL.intValue()))
            .andExpect(jsonPath("$.totalPaied").value(DEFAULT_TOTAL_PAIED.intValue()))
            .andExpect(jsonPath("$.printDate").value(DEFAULT_PRINT_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSale() throws Exception {
        // Get the sale
        restSaleMockMvc.perform(get("/api/sales/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSale() throws Exception {
        // Initialize the database
        saleRepository.saveAndFlush(sale);

		int databaseSizeBeforeUpdate = saleRepository.findAll().size();

        // Update the sale
        sale.setFecha(UPDATED_FECHA);
        sale.setCountry(UPDATED_COUNTRY);
        sale.setRate(UPDATED_RATE);
        sale.setSubTotal(UPDATED_SUB_TOTAL);
        sale.setDiscounts(UPDATED_DISCOUNTS);
        sale.setTaxes(UPDATED_TAXES);
        sale.setTotal(UPDATED_TOTAL);
        sale.setTotalPaied(UPDATED_TOTAL_PAIED);
        sale.setPrintDate(UPDATED_PRINT_DATE);

        restSaleMockMvc.perform(put("/api/sales")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(sale)))
                .andExpect(status().isOk());

        // Validate the Sale in the database
        List<Sale> sales = saleRepository.findAll();
        assertThat(sales).hasSize(databaseSizeBeforeUpdate);
        Sale testSale = sales.get(sales.size() - 1);
        assertThat(testSale.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testSale.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testSale.getRate()).isEqualTo(UPDATED_RATE);
        assertThat(testSale.getSubTotal()).isEqualTo(UPDATED_SUB_TOTAL);
        assertThat(testSale.getDiscounts()).isEqualTo(UPDATED_DISCOUNTS);
        assertThat(testSale.getTaxes()).isEqualTo(UPDATED_TAXES);
        assertThat(testSale.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testSale.getTotalPaied()).isEqualTo(UPDATED_TOTAL_PAIED);
        assertThat(testSale.getPrintDate()).isEqualTo(UPDATED_PRINT_DATE);
    }

    @Test
    @Transactional
    public void deleteSale() throws Exception {
        // Initialize the database
        saleRepository.saveAndFlush(sale);

		int databaseSizeBeforeDelete = saleRepository.findAll().size();

        // Get the sale
        restSaleMockMvc.perform(delete("/api/sales/{id}", sale.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Sale> sales = saleRepository.findAll();
        assertThat(sales).hasSize(databaseSizeBeforeDelete - 1);
    }
}

package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Application;
import com.mycompany.myapp.domain.DiscountSale;
import com.mycompany.myapp.repository.DiscountSaleRepository;

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
 * Test class for the DiscountSaleResource REST controller.
 *
 * @see DiscountSaleResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class DiscountSaleResourceIntTest {


    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    @Inject
    private DiscountSaleRepository discountSaleRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restDiscountSaleMockMvc;

    private DiscountSale discountSale;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DiscountSaleResource discountSaleResource = new DiscountSaleResource();
        ReflectionTestUtils.setField(discountSaleResource, "discountSaleRepository", discountSaleRepository);
        this.restDiscountSaleMockMvc = MockMvcBuilders.standaloneSetup(discountSaleResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        discountSale = new DiscountSale();
        discountSale.setAmount(DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    public void createDiscountSale() throws Exception {
        int databaseSizeBeforeCreate = discountSaleRepository.findAll().size();

        // Create the DiscountSale

        restDiscountSaleMockMvc.perform(post("/api/discountSales")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(discountSale)))
                .andExpect(status().isCreated());

        // Validate the DiscountSale in the database
        List<DiscountSale> discountSales = discountSaleRepository.findAll();
        assertThat(discountSales).hasSize(databaseSizeBeforeCreate + 1);
        DiscountSale testDiscountSale = discountSales.get(discountSales.size() - 1);
        assertThat(testDiscountSale.getAmount()).isEqualTo(DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = discountSaleRepository.findAll().size();
        // set the field null
        discountSale.setAmount(null);

        // Create the DiscountSale, which fails.

        restDiscountSaleMockMvc.perform(post("/api/discountSales")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(discountSale)))
                .andExpect(status().isBadRequest());

        List<DiscountSale> discountSales = discountSaleRepository.findAll();
        assertThat(discountSales).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDiscountSales() throws Exception {
        // Initialize the database
        discountSaleRepository.saveAndFlush(discountSale);

        // Get all the discountSales
        restDiscountSaleMockMvc.perform(get("/api/discountSales?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(discountSale.getId().intValue())))
                .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())));
    }

    @Test
    @Transactional
    public void getDiscountSale() throws Exception {
        // Initialize the database
        discountSaleRepository.saveAndFlush(discountSale);

        // Get the discountSale
        restDiscountSaleMockMvc.perform(get("/api/discountSales/{id}", discountSale.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(discountSale.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingDiscountSale() throws Exception {
        // Get the discountSale
        restDiscountSaleMockMvc.perform(get("/api/discountSales/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDiscountSale() throws Exception {
        // Initialize the database
        discountSaleRepository.saveAndFlush(discountSale);

		int databaseSizeBeforeUpdate = discountSaleRepository.findAll().size();

        // Update the discountSale
        discountSale.setAmount(UPDATED_AMOUNT);

        restDiscountSaleMockMvc.perform(put("/api/discountSales")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(discountSale)))
                .andExpect(status().isOk());

        // Validate the DiscountSale in the database
        List<DiscountSale> discountSales = discountSaleRepository.findAll();
        assertThat(discountSales).hasSize(databaseSizeBeforeUpdate);
        DiscountSale testDiscountSale = discountSales.get(discountSales.size() - 1);
        assertThat(testDiscountSale.getAmount()).isEqualTo(UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void deleteDiscountSale() throws Exception {
        // Initialize the database
        discountSaleRepository.saveAndFlush(discountSale);

		int databaseSizeBeforeDelete = discountSaleRepository.findAll().size();

        // Get the discountSale
        restDiscountSaleMockMvc.perform(delete("/api/discountSales/{id}", discountSale.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<DiscountSale> discountSales = discountSaleRepository.findAll();
        assertThat(discountSales).hasSize(databaseSizeBeforeDelete - 1);
    }
}

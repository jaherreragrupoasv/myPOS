package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Application;
import com.mycompany.myapp.domain.DiscountSaleLine;
import com.mycompany.myapp.repository.DiscountSaleLineRepository;

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
 * Test class for the DiscountSaleLineResource REST controller.
 *
 * @see DiscountSaleLineResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class DiscountSaleLineResourceIntTest {


    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    @Inject
    private DiscountSaleLineRepository discountSaleLineRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restDiscountSaleLineMockMvc;

    private DiscountSaleLine discountSaleLine;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DiscountSaleLineResource discountSaleLineResource = new DiscountSaleLineResource();
        ReflectionTestUtils.setField(discountSaleLineResource, "discountSaleLineRepository", discountSaleLineRepository);
        this.restDiscountSaleLineMockMvc = MockMvcBuilders.standaloneSetup(discountSaleLineResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        discountSaleLine = new DiscountSaleLine();
        discountSaleLine.setAmount(DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    public void createDiscountSaleLine() throws Exception {
        int databaseSizeBeforeCreate = discountSaleLineRepository.findAll().size();

        // Create the DiscountSaleLine

        restDiscountSaleLineMockMvc.perform(post("/api/discountSaleLines")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(discountSaleLine)))
                .andExpect(status().isCreated());

        // Validate the DiscountSaleLine in the database
        List<DiscountSaleLine> discountSaleLines = discountSaleLineRepository.findAll();
        assertThat(discountSaleLines).hasSize(databaseSizeBeforeCreate + 1);
        DiscountSaleLine testDiscountSaleLine = discountSaleLines.get(discountSaleLines.size() - 1);
        assertThat(testDiscountSaleLine.getAmount()).isEqualTo(DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = discountSaleLineRepository.findAll().size();
        // set the field null
        discountSaleLine.setAmount(null);

        // Create the DiscountSaleLine, which fails.

        restDiscountSaleLineMockMvc.perform(post("/api/discountSaleLines")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(discountSaleLine)))
                .andExpect(status().isBadRequest());

        List<DiscountSaleLine> discountSaleLines = discountSaleLineRepository.findAll();
        assertThat(discountSaleLines).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDiscountSaleLines() throws Exception {
        // Initialize the database
        discountSaleLineRepository.saveAndFlush(discountSaleLine);

        // Get all the discountSaleLines
        restDiscountSaleLineMockMvc.perform(get("/api/discountSaleLines?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(discountSaleLine.getId().intValue())))
                .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())));
    }

    @Test
    @Transactional
    public void getDiscountSaleLine() throws Exception {
        // Initialize the database
        discountSaleLineRepository.saveAndFlush(discountSaleLine);

        // Get the discountSaleLine
        restDiscountSaleLineMockMvc.perform(get("/api/discountSaleLines/{id}", discountSaleLine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(discountSaleLine.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingDiscountSaleLine() throws Exception {
        // Get the discountSaleLine
        restDiscountSaleLineMockMvc.perform(get("/api/discountSaleLines/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDiscountSaleLine() throws Exception {
        // Initialize the database
        discountSaleLineRepository.saveAndFlush(discountSaleLine);

		int databaseSizeBeforeUpdate = discountSaleLineRepository.findAll().size();

        // Update the discountSaleLine
        discountSaleLine.setAmount(UPDATED_AMOUNT);

        restDiscountSaleLineMockMvc.perform(put("/api/discountSaleLines")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(discountSaleLine)))
                .andExpect(status().isOk());

        // Validate the DiscountSaleLine in the database
        List<DiscountSaleLine> discountSaleLines = discountSaleLineRepository.findAll();
        assertThat(discountSaleLines).hasSize(databaseSizeBeforeUpdate);
        DiscountSaleLine testDiscountSaleLine = discountSaleLines.get(discountSaleLines.size() - 1);
        assertThat(testDiscountSaleLine.getAmount()).isEqualTo(UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void deleteDiscountSaleLine() throws Exception {
        // Initialize the database
        discountSaleLineRepository.saveAndFlush(discountSaleLine);

		int databaseSizeBeforeDelete = discountSaleLineRepository.findAll().size();

        // Get the discountSaleLine
        restDiscountSaleLineMockMvc.perform(delete("/api/discountSaleLines/{id}", discountSaleLine.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<DiscountSaleLine> discountSaleLines = discountSaleLineRepository.findAll();
        assertThat(discountSaleLines).hasSize(databaseSizeBeforeDelete - 1);
    }
}

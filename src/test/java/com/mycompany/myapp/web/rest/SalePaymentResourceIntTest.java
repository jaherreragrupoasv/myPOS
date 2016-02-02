package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Application;
import com.mycompany.myapp.domain.SalePayment;
import com.mycompany.myapp.repository.SalePaymentRepository;

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
 * Test class for the SalePaymentResource REST controller.
 *
 * @see SalePaymentResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class SalePaymentResourceIntTest {

    private static final String DEFAULT_CREDIT_CARD = "AAAAA";
    private static final String UPDATED_CREDIT_CARD = "BBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    @Inject
    private SalePaymentRepository salePaymentRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSalePaymentMockMvc;

    private SalePayment salePayment;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SalePaymentResource salePaymentResource = new SalePaymentResource();
        ReflectionTestUtils.setField(salePaymentResource, "salePaymentRepository", salePaymentRepository);
        this.restSalePaymentMockMvc = MockMvcBuilders.standaloneSetup(salePaymentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        salePayment = new SalePayment();
        salePayment.setCreditCard(DEFAULT_CREDIT_CARD);
        salePayment.setDate(DEFAULT_DATE);
        salePayment.setAmount(DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    public void createSalePayment() throws Exception {
        int databaseSizeBeforeCreate = salePaymentRepository.findAll().size();

        // Create the SalePayment

        restSalePaymentMockMvc.perform(post("/api/salePayments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(salePayment)))
                .andExpect(status().isCreated());

        // Validate the SalePayment in the database
        List<SalePayment> salePayments = salePaymentRepository.findAll();
        assertThat(salePayments).hasSize(databaseSizeBeforeCreate + 1);
        SalePayment testSalePayment = salePayments.get(salePayments.size() - 1);
        assertThat(testSalePayment.getCreditCard()).isEqualTo(DEFAULT_CREDIT_CARD);
        assertThat(testSalePayment.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testSalePayment.getAmount()).isEqualTo(DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = salePaymentRepository.findAll().size();
        // set the field null
        salePayment.setAmount(null);

        // Create the SalePayment, which fails.

        restSalePaymentMockMvc.perform(post("/api/salePayments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(salePayment)))
                .andExpect(status().isBadRequest());

        List<SalePayment> salePayments = salePaymentRepository.findAll();
        assertThat(salePayments).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSalePayments() throws Exception {
        // Initialize the database
        salePaymentRepository.saveAndFlush(salePayment);

        // Get all the salePayments
        restSalePaymentMockMvc.perform(get("/api/salePayments?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(salePayment.getId().intValue())))
                .andExpect(jsonPath("$.[*].creditCard").value(hasItem(DEFAULT_CREDIT_CARD.toString())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
                .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())));
    }

    @Test
    @Transactional
    public void getSalePayment() throws Exception {
        // Initialize the database
        salePaymentRepository.saveAndFlush(salePayment);

        // Get the salePayment
        restSalePaymentMockMvc.perform(get("/api/salePayments/{id}", salePayment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(salePayment.getId().intValue()))
            .andExpect(jsonPath("$.creditCard").value(DEFAULT_CREDIT_CARD.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSalePayment() throws Exception {
        // Get the salePayment
        restSalePaymentMockMvc.perform(get("/api/salePayments/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSalePayment() throws Exception {
        // Initialize the database
        salePaymentRepository.saveAndFlush(salePayment);

		int databaseSizeBeforeUpdate = salePaymentRepository.findAll().size();

        // Update the salePayment
        salePayment.setCreditCard(UPDATED_CREDIT_CARD);
        salePayment.setDate(UPDATED_DATE);
        salePayment.setAmount(UPDATED_AMOUNT);

        restSalePaymentMockMvc.perform(put("/api/salePayments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(salePayment)))
                .andExpect(status().isOk());

        // Validate the SalePayment in the database
        List<SalePayment> salePayments = salePaymentRepository.findAll();
        assertThat(salePayments).hasSize(databaseSizeBeforeUpdate);
        SalePayment testSalePayment = salePayments.get(salePayments.size() - 1);
        assertThat(testSalePayment.getCreditCard()).isEqualTo(UPDATED_CREDIT_CARD);
        assertThat(testSalePayment.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testSalePayment.getAmount()).isEqualTo(UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void deleteSalePayment() throws Exception {
        // Initialize the database
        salePaymentRepository.saveAndFlush(salePayment);

		int databaseSizeBeforeDelete = salePaymentRepository.findAll().size();

        // Get the salePayment
        restSalePaymentMockMvc.perform(delete("/api/salePayments/{id}", salePayment.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<SalePayment> salePayments = salePaymentRepository.findAll();
        assertThat(salePayments).hasSize(databaseSizeBeforeDelete - 1);
    }
}

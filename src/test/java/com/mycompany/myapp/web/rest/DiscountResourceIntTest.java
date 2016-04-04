package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Application;
import com.mycompany.myapp.domain.Discount;
import com.mycompany.myapp.repository.DiscountRepository;

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

import com.mycompany.myapp.domain.enumeration.DiscountType;

/**
 * Test class for the DiscountResource REST controller.
 *
 * @see DiscountResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class DiscountResourceIntTest {

    private static final String DEFAULT_DISCOUNT_NAME = "AAAAA";
    private static final String UPDATED_DISCOUNT_NAME = "BBBBB";

    private static final LocalDate DEFAULT_FROM_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FROM_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_TO_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TO_DATE = LocalDate.now(ZoneId.systemDefault());
    
    private static final DiscountType DEFAULT_TYPE = DiscountType.PORCENTAJE;
    private static final DiscountType UPDATED_TYPE = DiscountType.CANTIDAD;

    private static final BigDecimal DEFAULT_VALUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALUE = new BigDecimal(2);

    @Inject
    private DiscountRepository discountRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restDiscountMockMvc;

    private Discount discount;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DiscountResource discountResource = new DiscountResource();
        ReflectionTestUtils.setField(discountResource, "discountRepository", discountRepository);
        this.restDiscountMockMvc = MockMvcBuilders.standaloneSetup(discountResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        discount = new Discount();
        discount.setDiscountName(DEFAULT_DISCOUNT_NAME);
        discount.setFromDate(DEFAULT_FROM_DATE);
        discount.setToDate(DEFAULT_TO_DATE);
        discount.setType(DEFAULT_TYPE);
        discount.setValue(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void createDiscount() throws Exception {
        int databaseSizeBeforeCreate = discountRepository.findAll().size();

        // Create the Discount

        restDiscountMockMvc.perform(post("/api/discounts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(discount)))
                .andExpect(status().isCreated());

        // Validate the Discount in the database
        List<Discount> discounts = discountRepository.findAll();
        assertThat(discounts).hasSize(databaseSizeBeforeCreate + 1);
        Discount testDiscount = discounts.get(discounts.size() - 1);
        assertThat(testDiscount.getDiscountName()).isEqualTo(DEFAULT_DISCOUNT_NAME);
        assertThat(testDiscount.getFromDate()).isEqualTo(DEFAULT_FROM_DATE);
        assertThat(testDiscount.getToDate()).isEqualTo(DEFAULT_TO_DATE);
        assertThat(testDiscount.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testDiscount.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void checkDiscountNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = discountRepository.findAll().size();
        // set the field null
        discount.setDiscountName(null);

        // Create the Discount, which fails.

        restDiscountMockMvc.perform(post("/api/discounts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(discount)))
                .andExpect(status().isBadRequest());

        List<Discount> discounts = discountRepository.findAll();
        assertThat(discounts).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDiscounts() throws Exception {
        // Initialize the database
        discountRepository.saveAndFlush(discount);

        // Get all the discounts
        restDiscountMockMvc.perform(get("/api/discounts?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(discount.getId().intValue())))
                .andExpect(jsonPath("$.[*].discountName").value(hasItem(DEFAULT_DISCOUNT_NAME.toString())))
                .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
                .andExpect(jsonPath("$.[*].toDate").value(hasItem(DEFAULT_TO_DATE.toString())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
                .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.intValue())));
    }

    @Test
    @Transactional
    public void getDiscount() throws Exception {
        // Initialize the database
        discountRepository.saveAndFlush(discount);

        // Get the discount
        restDiscountMockMvc.perform(get("/api/discounts/{id}", discount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(discount.getId().intValue()))
            .andExpect(jsonPath("$.discountName").value(DEFAULT_DISCOUNT_NAME.toString()))
            .andExpect(jsonPath("$.fromDate").value(DEFAULT_FROM_DATE.toString()))
            .andExpect(jsonPath("$.toDate").value(DEFAULT_TO_DATE.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingDiscount() throws Exception {
        // Get the discount
        restDiscountMockMvc.perform(get("/api/discounts/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDiscount() throws Exception {
        // Initialize the database
        discountRepository.saveAndFlush(discount);

		int databaseSizeBeforeUpdate = discountRepository.findAll().size();

        // Update the discount
        discount.setDiscountName(UPDATED_DISCOUNT_NAME);
        discount.setFromDate(UPDATED_FROM_DATE);
        discount.setToDate(UPDATED_TO_DATE);
        discount.setType(UPDATED_TYPE);
        discount.setValue(UPDATED_VALUE);

        restDiscountMockMvc.perform(put("/api/discounts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(discount)))
                .andExpect(status().isOk());

        // Validate the Discount in the database
        List<Discount> discounts = discountRepository.findAll();
        assertThat(discounts).hasSize(databaseSizeBeforeUpdate);
        Discount testDiscount = discounts.get(discounts.size() - 1);
        assertThat(testDiscount.getDiscountName()).isEqualTo(UPDATED_DISCOUNT_NAME);
        assertThat(testDiscount.getFromDate()).isEqualTo(UPDATED_FROM_DATE);
        assertThat(testDiscount.getToDate()).isEqualTo(UPDATED_TO_DATE);
        assertThat(testDiscount.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testDiscount.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void deleteDiscount() throws Exception {
        // Initialize the database
        discountRepository.saveAndFlush(discount);

		int databaseSizeBeforeDelete = discountRepository.findAll().size();

        // Get the discount
        restDiscountMockMvc.perform(delete("/api/discounts/{id}", discount.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Discount> discounts = discountRepository.findAll();
        assertThat(discounts).hasSize(databaseSizeBeforeDelete - 1);
    }
}

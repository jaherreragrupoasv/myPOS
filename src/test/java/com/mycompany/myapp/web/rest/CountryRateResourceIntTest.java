package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Application;
import com.mycompany.myapp.domain.CountryRate;
import com.mycompany.myapp.repository.CountryRateRepository;

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
 * Test class for the CountryRateResource REST controller.
 *
 * @see CountryRateResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class CountryRateResourceIntTest {

    private static final String DEFAULT_COUNTRY = "ESPAÑA";
    private static final String UPDATED_COUNTRY = "FRANCIA";

    private static final BigDecimal DEFAULT_RATE = new BigDecimal(0.5);
    private static final BigDecimal UPDATED_RATE = new BigDecimal(0.5);

    @Inject
    private CountryRateRepository countryRateRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCountryRateMockMvc;

    private CountryRate countryRate;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CountryRateResource countryRateResource = new CountryRateResource();
        ReflectionTestUtils.setField(countryRateResource, "countryRateRepository", countryRateRepository);
        this.restCountryRateMockMvc = MockMvcBuilders.standaloneSetup(countryRateResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        countryRate = new CountryRate();
        countryRate.setCountry(DEFAULT_COUNTRY);
        countryRate.setRate(DEFAULT_RATE);
    }

    @Test
    @Transactional
    public void createCountryRate() throws Exception {
        int databaseSizeBeforeCreate = countryRateRepository.findAll().size();

        // Create the CountryRate

        restCountryRateMockMvc.perform(post("/api/countryRates")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(countryRate)))
                .andExpect(status().isCreated());

        // Validate the CountryRate in the database
        List<CountryRate> countryRates = countryRateRepository.findAll();
        assertThat(countryRates).hasSize(databaseSizeBeforeCreate + 1);
        CountryRate testCountryRate = countryRates.get(countryRates.size() - 1);
        assertThat(testCountryRate.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testCountryRate.getRate()).isEqualTo(DEFAULT_RATE);
    }

    @Test
    @Transactional
    public void checkCountryIsRequired() throws Exception {
        int databaseSizeBeforeTest = countryRateRepository.findAll().size();
        // set the field null
        countryRate.setCountry(null);

        // Create the CountryRate, which fails.

        restCountryRateMockMvc.perform(post("/api/countryRates")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(countryRate)))
                .andExpect(status().isBadRequest());

        List<CountryRate> countryRates = countryRateRepository.findAll();
        assertThat(countryRates).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCountryRates() throws Exception {
        // Initialize the database
        countryRateRepository.saveAndFlush(countryRate);

        // Get all the countryRates
        restCountryRateMockMvc.perform(get("/api/countryRates?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(countryRate.getId().intValue())))
                .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())))
                .andExpect(jsonPath("$.[*].rate").value(hasItem(DEFAULT_RATE.toString())));
    }

    @Test
    @Transactional
    public void getCountryRate() throws Exception {
        // Initialize the database
        countryRateRepository.saveAndFlush(countryRate);

        // Get the countryRate
        restCountryRateMockMvc.perform(get("/api/countryRates/{id}", countryRate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(countryRate.getId().intValue()))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY.toString()))
            .andExpect(jsonPath("$.rate").value(DEFAULT_RATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCountryRate() throws Exception {
        // Get the countryRate
        restCountryRateMockMvc.perform(get("/api/countryRates/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCountryRate() throws Exception {
        // Initialize the database
        countryRateRepository.saveAndFlush(countryRate);

		int databaseSizeBeforeUpdate = countryRateRepository.findAll().size();

        // Update the countryRate
        countryRate.setCountry(UPDATED_COUNTRY);
        countryRate.setRate(UPDATED_RATE);

        restCountryRateMockMvc.perform(put("/api/countryRates")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(countryRate)))
                .andExpect(status().isOk());

        // Validate the CountryRate in the database
        List<CountryRate> countryRates = countryRateRepository.findAll();
        assertThat(countryRates).hasSize(databaseSizeBeforeUpdate);
        CountryRate testCountryRate = countryRates.get(countryRates.size() - 1);
        assertThat(testCountryRate.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testCountryRate.getRate()).isEqualTo(UPDATED_RATE);
    }

    @Test
    @Transactional
    public void deleteCountryRate() throws Exception {
        // Initialize the database
        countryRateRepository.saveAndFlush(countryRate);

		int databaseSizeBeforeDelete = countryRateRepository.findAll().size();

        // Get the countryRate
        restCountryRateMockMvc.perform(delete("/api/countryRates/{id}", countryRate.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<CountryRate> countryRates = countryRateRepository.findAll();
        assertThat(countryRates).hasSize(databaseSizeBeforeDelete - 1);
    }
}

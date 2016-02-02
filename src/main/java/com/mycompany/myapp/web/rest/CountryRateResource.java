package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.CountryRate;
import com.mycompany.myapp.repository.CountryRateRepository;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing CountryRate.
 */
@RestController
@RequestMapping("/api")
public class CountryRateResource {

    private final Logger log = LoggerFactory.getLogger(CountryRateResource.class);
        
    @Inject
    private CountryRateRepository countryRateRepository;
    
    /**
     * POST  /countryRates -> Create a new countryRate.
     */
    @RequestMapping(value = "/countryRates",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CountryRate> createCountryRate(@Valid @RequestBody CountryRate countryRate) throws URISyntaxException {
        log.debug("REST request to save CountryRate : {}", countryRate);
        if (countryRate.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("countryRate", "idexists", "A new countryRate cannot already have an ID")).body(null);
        }
        CountryRate result = countryRateRepository.save(countryRate);
        return ResponseEntity.created(new URI("/api/countryRates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("countryRate", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /countryRates -> Updates an existing countryRate.
     */
    @RequestMapping(value = "/countryRates",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CountryRate> updateCountryRate(@Valid @RequestBody CountryRate countryRate) throws URISyntaxException {
        log.debug("REST request to update CountryRate : {}", countryRate);
        if (countryRate.getId() == null) {
            return createCountryRate(countryRate);
        }
        CountryRate result = countryRateRepository.save(countryRate);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("countryRate", countryRate.getId().toString()))
            .body(result);
    }

    /**
     * GET  /countryRates -> get all the countryRates.
     */
    @RequestMapping(value = "/countryRates",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<CountryRate> getAllCountryRates() {
        log.debug("REST request to get all CountryRates");
        return countryRateRepository.findAll();
            }

    /**
     * GET  /countryRates/:id -> get the "id" countryRate.
     */
    @RequestMapping(value = "/countryRates/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CountryRate> getCountryRate(@PathVariable Long id) {
        log.debug("REST request to get CountryRate : {}", id);
        CountryRate countryRate = countryRateRepository.findOne(id);
        return Optional.ofNullable(countryRate)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /countryRates/:id -> delete the "id" countryRate.
     */
    @RequestMapping(value = "/countryRates/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCountryRate(@PathVariable Long id) {
        log.debug("REST request to delete CountryRate : {}", id);
        countryRateRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("countryRate", id.toString())).build();
    }
}

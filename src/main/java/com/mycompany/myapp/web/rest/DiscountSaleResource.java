package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.DiscountSale;
import com.mycompany.myapp.repository.DiscountSaleRepository;
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
 * REST controller for managing DiscountSale.
 */
@RestController
@RequestMapping("/api")
public class DiscountSaleResource {

    private final Logger log = LoggerFactory.getLogger(DiscountSaleResource.class);
        
    @Inject
    private DiscountSaleRepository discountSaleRepository;
    
    /**
     * POST  /discountSales -> Create a new discountSale.
     */
    @RequestMapping(value = "/discountSales",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DiscountSale> createDiscountSale(@Valid @RequestBody DiscountSale discountSale) throws URISyntaxException {
        log.debug("REST request to save DiscountSale : {}", discountSale);
        if (discountSale.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("discountSale", "idexists", "A new discountSale cannot already have an ID")).body(null);
        }
        DiscountSale result = discountSaleRepository.save(discountSale);
        return ResponseEntity.created(new URI("/api/discountSales/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("discountSale", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /discountSales -> Updates an existing discountSale.
     */
    @RequestMapping(value = "/discountSales",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DiscountSale> updateDiscountSale(@Valid @RequestBody DiscountSale discountSale) throws URISyntaxException {
        log.debug("REST request to update DiscountSale : {}", discountSale);
        if (discountSale.getId() == null) {
            return createDiscountSale(discountSale);
        }
        DiscountSale result = discountSaleRepository.save(discountSale);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("discountSale", discountSale.getId().toString()))
            .body(result);
    }

    /**
     * GET  /discountSales -> get all the discountSales.
     */
    @RequestMapping(value = "/discountSales",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<DiscountSale> getAllDiscountSales() {
        log.debug("REST request to get all DiscountSales");
        return discountSaleRepository.findAll();
            }

    /**
     * GET  /discountSales/:id -> get the "id" discountSale.
     */
    @RequestMapping(value = "/discountSales/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DiscountSale> getDiscountSale(@PathVariable Long id) {
        log.debug("REST request to get DiscountSale : {}", id);
        DiscountSale discountSale = discountSaleRepository.findOne(id);
        return Optional.ofNullable(discountSale)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /discountSales/:id -> delete the "id" discountSale.
     */
    @RequestMapping(value = "/discountSales/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDiscountSale(@PathVariable Long id) {
        log.debug("REST request to delete DiscountSale : {}", id);
        discountSaleRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("discountSale", id.toString())).build();
    }
}

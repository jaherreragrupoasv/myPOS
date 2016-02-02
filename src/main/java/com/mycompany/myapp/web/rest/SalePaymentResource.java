package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.SalePayment;
import com.mycompany.myapp.repository.SalePaymentRepository;
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
 * REST controller for managing SalePayment.
 */
@RestController
@RequestMapping("/api")
public class SalePaymentResource {

    private final Logger log = LoggerFactory.getLogger(SalePaymentResource.class);
        
    @Inject
    private SalePaymentRepository salePaymentRepository;
    
    /**
     * POST  /salePayments -> Create a new salePayment.
     */
    @RequestMapping(value = "/salePayments",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SalePayment> createSalePayment(@Valid @RequestBody SalePayment salePayment) throws URISyntaxException {
        log.debug("REST request to save SalePayment : {}", salePayment);
        if (salePayment.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("salePayment", "idexists", "A new salePayment cannot already have an ID")).body(null);
        }
        SalePayment result = salePaymentRepository.save(salePayment);
        return ResponseEntity.created(new URI("/api/salePayments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("salePayment", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /salePayments -> Updates an existing salePayment.
     */
    @RequestMapping(value = "/salePayments",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SalePayment> updateSalePayment(@Valid @RequestBody SalePayment salePayment) throws URISyntaxException {
        log.debug("REST request to update SalePayment : {}", salePayment);
        if (salePayment.getId() == null) {
            return createSalePayment(salePayment);
        }
        SalePayment result = salePaymentRepository.save(salePayment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("salePayment", salePayment.getId().toString()))
            .body(result);
    }

    /**
     * GET  /salePayments -> get all the salePayments.
     */
    @RequestMapping(value = "/salePayments",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<SalePayment> getAllSalePayments() {
        log.debug("REST request to get all SalePayments");
        return salePaymentRepository.findAll();
            }

    /**
     * GET  /salePayments/:id -> get the "id" salePayment.
     */
    @RequestMapping(value = "/salePayments/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SalePayment> getSalePayment(@PathVariable Long id) {
        log.debug("REST request to get SalePayment : {}", id);
        SalePayment salePayment = salePaymentRepository.findOne(id);
        return Optional.ofNullable(salePayment)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /salePayments/:id -> delete the "id" salePayment.
     */
    @RequestMapping(value = "/salePayments/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSalePayment(@PathVariable Long id) {
        log.debug("REST request to delete SalePayment : {}", id);
        salePaymentRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("salePayment", id.toString())).build();
    }
}

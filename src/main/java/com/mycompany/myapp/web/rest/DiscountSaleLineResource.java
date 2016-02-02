package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.DiscountSaleLine;
import com.mycompany.myapp.repository.DiscountSaleLineRepository;
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
 * REST controller for managing DiscountSaleLine.
 */
@RestController
@RequestMapping("/api")
public class DiscountSaleLineResource {

    private final Logger log = LoggerFactory.getLogger(DiscountSaleLineResource.class);
        
    @Inject
    private DiscountSaleLineRepository discountSaleLineRepository;
    
    /**
     * POST  /discountSaleLines -> Create a new discountSaleLine.
     */
    @RequestMapping(value = "/discountSaleLines",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DiscountSaleLine> createDiscountSaleLine(@Valid @RequestBody DiscountSaleLine discountSaleLine) throws URISyntaxException {
        log.debug("REST request to save DiscountSaleLine : {}", discountSaleLine);
        if (discountSaleLine.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("discountSaleLine", "idexists", "A new discountSaleLine cannot already have an ID")).body(null);
        }
        DiscountSaleLine result = discountSaleLineRepository.save(discountSaleLine);
        return ResponseEntity.created(new URI("/api/discountSaleLines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("discountSaleLine", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /discountSaleLines -> Updates an existing discountSaleLine.
     */
    @RequestMapping(value = "/discountSaleLines",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DiscountSaleLine> updateDiscountSaleLine(@Valid @RequestBody DiscountSaleLine discountSaleLine) throws URISyntaxException {
        log.debug("REST request to update DiscountSaleLine : {}", discountSaleLine);
        if (discountSaleLine.getId() == null) {
            return createDiscountSaleLine(discountSaleLine);
        }
        DiscountSaleLine result = discountSaleLineRepository.save(discountSaleLine);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("discountSaleLine", discountSaleLine.getId().toString()))
            .body(result);
    }

    /**
     * GET  /discountSaleLines -> get all the discountSaleLines.
     */
    @RequestMapping(value = "/discountSaleLines",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<DiscountSaleLine> getAllDiscountSaleLines() {
        log.debug("REST request to get all DiscountSaleLines");
        return discountSaleLineRepository.findAll();
            }

    /**
     * GET  /discountSaleLines/:id -> get the "id" discountSaleLine.
     */
    @RequestMapping(value = "/discountSaleLines/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DiscountSaleLine> getDiscountSaleLine(@PathVariable Long id) {
        log.debug("REST request to get DiscountSaleLine : {}", id);
        DiscountSaleLine discountSaleLine = discountSaleLineRepository.findOne(id);
        return Optional.ofNullable(discountSaleLine)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /discountSaleLines/:id -> delete the "id" discountSaleLine.
     */
    @RequestMapping(value = "/discountSaleLines/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDiscountSaleLine(@PathVariable Long id) {
        log.debug("REST request to delete DiscountSaleLine : {}", id);
        discountSaleLineRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("discountSaleLine", id.toString())).build();
    }
}

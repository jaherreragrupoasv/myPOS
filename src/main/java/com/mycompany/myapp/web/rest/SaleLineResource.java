package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.SaleLine;
import com.mycompany.myapp.repository.SaleLineRepository;
import com.mycompany.myapp.service.SaleLineService;
import com.mycompany.myapp.service.SaleService;
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
 * REST controller for managing SaleLine.
 */
@RestController
@RequestMapping("/api")
public class SaleLineResource {

    private final Logger log = LoggerFactory.getLogger(SaleLineResource.class);

    @Inject
    private SaleLineRepository saleLineRepository;

    @Inject
    private SaleLineService saleLineService;

    /**
     * POST  /saleLines -> Create a new saleLine.
     */
    @RequestMapping(value = "/saleLines",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SaleLine> createSaleLine(@Valid @RequestBody SaleLine saleLine) throws URISyntaxException {
        log.debug("REST request to save SaleLine : {}", saleLine);
        if (saleLine.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("saleLine", "idexists", "A new saleLine cannot already have an ID")).body(null);
        }

        SaleLine result = saleLineRepository.save(saleLine);

//      Save line and update total
//        SaleLine result = saleLineService.saveLine(saleLine);

        return ResponseEntity.created(new URI("/api/saleLines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("saleLine", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /saleLines -> Updates an existing saleLine.
     */
    @RequestMapping(value = "/saleLines",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SaleLine> updateSaleLine(@Valid @RequestBody SaleLine saleLine) throws URISyntaxException {
        log.debug("REST request to update SaleLine : {}", saleLine);
        if (saleLine.getId() == null) {
            return createSaleLine(saleLine);
        }

        SaleLine result = saleLineRepository.save(saleLine);

//      Save line and update total
//        SaleLine result = saleLineService.saveLine(saleLine);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("saleLine", saleLine.getId().toString()))
            .body(result);
    }

    /**
     * GET  /saleLines -> get all the saleLines.
     */
    @RequestMapping(value = "/saleLines",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<SaleLine> getAllSaleLines() {
        log.debug("REST request to get all SaleLines");
        return saleLineRepository.findAll();
    }

    /**
     * GET  /saleLines/:id -> get the "id" saleLine.
     */
    @RequestMapping(value = "/saleLines/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SaleLine> getSaleLine(@PathVariable Long id) {
        log.debug("REST request to get SaleLine : {}", id);
        SaleLine saleLine = saleLineRepository.findOne(id);
        return Optional.ofNullable(saleLine)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /saleLines/:id -> delete the "id" saleLine.
     */
    @RequestMapping(value = "/saleLines/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSaleLine(@PathVariable Long id) {
        log.debug("REST request to delete SaleLine : {}", id);
        saleLineRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("saleLine", id.toString())).build();
    }
}

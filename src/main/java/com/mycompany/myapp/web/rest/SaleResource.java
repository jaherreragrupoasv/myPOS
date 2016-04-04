package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Sale;
import com.mycompany.myapp.domain.SaleLine;
import com.mycompany.myapp.repository.SaleLineRepository;
import com.mycompany.myapp.repository.SaleRepository;
import com.mycompany.myapp.service.SaleService;
import com.mycompany.myapp.service.patterns.ITotalCalculator;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * REST controller for managing Sale.
 */
@RestController
@Transactional
@RequestMapping("/api")
public class SaleResource {

    private final Logger log = LoggerFactory.getLogger(SaleResource.class);

    @Inject
    private SaleRepository saleRepository;

    @Inject
    private SaleService saleService;

    /**
     * POST  /sales -> Create a new sale.
     */
    @RequestMapping(value = "/sales",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Sale> createSale(@Valid @RequestBody Sale sale) throws URISyntaxException {
        log.debug("REST request to save Sale : {}", sale);
        if (sale.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("sale", "idexists", "A new sale cannot already have an ID")).body(null);
        }

        sale.setFecha(LocalDate.now());
        Sale result = saleService.saveSale(sale);

        return ResponseEntity.created(new URI("/api/sales/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("sale", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sales -> Updates an existing sale.
     */
    @RequestMapping(value = "/sales",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional()
    public ResponseEntity<Sale> updateSale(@Valid @RequestBody Sale sale) throws URISyntaxException {
        log.debug("REST request to update Sale : {}", sale);
        if (sale.getId() == null) {
            return createSale(sale);
        }
        Sale result = saleRepository.save(sale);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("sale", sale.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sales -> get all the sales.
     */
    @RequestMapping(value = "/sales",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
//    @Transactional
    public List<Sale> getAllSales() {
        log.debug("REST request to get all Sales");
        return saleRepository.findAll();
            }

    /**
     * GET  /sales/:id -> get the "id" sale.
     */
    @RequestMapping(value = "/sales/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional
    public ResponseEntity<Sale> getSale(@PathVariable Long id) {
        log.debug("REST request to get Sale : {}", id);
        Sale sale = saleRepository.findOne(id);

        List<SaleLine> saleLines = saleRepository.getSaleLines(sale.getId());

        sale.setSaleLines(saleLines);

        return Optional.ofNullable(sale)
                .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /sales/:id -> delete the "id" sale.
     */
    @RequestMapping(value = "/sales/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSale(@PathVariable Long id) {
        log.debug("REST request to delete Sale : {}", id);
        saleRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("sale", id.toString())).build();
    }

    /**
     * GET  /sales/salelines/:id -> get all lines of the the sale.
     */

    @RequestMapping(value = "/sales/saleLines/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional
    public List<SaleLine> getAllLines(@PathVariable Long id) {
        log.debug("REST request to get all Lines of Sale : {}", id);
        return saleRepository.getSaleLines(id);
    }

    /**
     * POST  /sales/print/:id -> print the sale.
     */

    @RequestMapping(value = "/sales/print/{id}",
        method = RequestMethod.GET)
    @Timed
    @Transactional
    public void print(@PathVariable Long id) {
        log.debug("REST request to print Sale : {}", id);

        try {
            saleService.printSale(id);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}

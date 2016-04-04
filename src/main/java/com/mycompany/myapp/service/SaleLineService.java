package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Discount;
import com.mycompany.myapp.domain.SaleLine;
import com.mycompany.myapp.repository.SaleLineRepository;
import com.mycompany.myapp.service.patterns.ITotalCalculator;
import com.mycompany.myapp.service.patterns.SalesCurrencyConversionDecorator;
import com.mycompany.myapp.service.patterns.SalesTaxDecorator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.List;

/**
 * Service class for managing salelines.
 */
@Service
@Transactional
public class SaleLineService {

    private final Logger log = LoggerFactory.getLogger(SaleLineService.class);

    @Inject
    private SaleLineRepository saleLineRepository;

    @Autowired
    private ITotalCalculator saleService;

    @Transactional(rollbackFor=Exception.class)
    public SaleLine saveLine(SaleLine saleLine) {

        try {
            //1.- Save de line.
            saleLineRepository.save(saleLine);

//            UTILIZANDO UN @PROCEDURE NO FUNCIONA CUANDO RECUPERA UNA COLECCION
//            List<Discount> cadena = saleLineRepository.discountsToApply(saleLine.getSale().getId());

//            List<Discount> discounts = saleLineRepository.getDiscountsToApply(saleLine.getSale_id(), LocalDate.now(), LocalDate.now());

            //2.- Calculate total of sale.
//            CalculatorService calculatorService = new CalculatorService();

//            log.debug("Saving line {} and updating price of sale.", saleLine.getSale_id());

        } catch (Exception e) {
            saleLine = null;
        }
        return saleLine;
    }
}

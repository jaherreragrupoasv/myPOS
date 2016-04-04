package com.mycompany.myapp.service.patterns;

import com.mycompany.myapp.domain.Sale;
import com.mycompany.myapp.domain.SaleLine;
import com.mycompany.myapp.repository.SaleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

@Component("SaleBaseDecorator")
@Transactional
public class SaleBaseDecorator implements ITotalCalculator {

    private final Logger log = LoggerFactory.getLogger(SaleBaseDecorator.class);

    @Inject
    private SaleRepository saleRepository;

    public void calculateTotal(Sale sale) {
        log.debug("Updating taxes, discounts and total in sale {}", sale.getId());

        //1.- Calculate subTotal.
        sale.CalculateTotal();
    }

}

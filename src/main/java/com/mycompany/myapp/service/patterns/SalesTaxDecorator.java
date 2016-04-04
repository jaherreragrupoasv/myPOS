package com.mycompany.myapp.service.patterns;

import com.mycompany.myapp.domain.Sale;
import com.mycompany.myapp.domain.SaleLine;
import com.mycompany.myapp.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by jaherrera on 08/01/2016.
 */

@Component
@Primary
@Transactional
public class SalesTaxDecorator implements ITotalCalculator {

    BigDecimal taxRate;

    @Inject
    private SaleRepository saleRepository;

    @Autowired
    @Qualifier("SalesCurrencyConversionDecorator")
    private ITotalCalculator saleCurrencyConversionDecorator;

    @Autowired
    @Qualifier("SaleBaseDecorator")
    private ITotalCalculator saleBaseDecorator;

    @Autowired
    @Qualifier("SalesDiscountsDecorator")
    private ITotalCalculator SalesDiscountsDecorator;

    public void calculateTotal(Sale sale) {

        saleBaseDecorator.calculateTotal(sale);

        saleCurrencyConversionDecorator.calculateTotal(sale);

        SalesDiscountsDecorator.calculateTotal(sale);

        BigDecimal taxes = new BigDecimal(0);

        // Muestra cada hijo en este nodo
        for (SaleLine c : sale.getSaleLines())
        {
            taxes = taxes.add(c.CalculateTaxes());
        }

        sale.setTaxes(taxes);

//        Calculo total
        sale.setTotal(sale.getSubTotal().add(sale.getTaxes()).subtract(sale.getDiscounts()));

        saleRepository.save(sale);
    }
}

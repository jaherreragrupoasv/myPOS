package com.mycompany.myapp.service.patterns;

import com.mycompany.myapp.domain.CountryRate;
import com.mycompany.myapp.domain.Sale;
import com.mycompany.myapp.repository.CountryRateRepository;
import com.mycompany.myapp.repository.SaleRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.math.BigDecimal;

/**
 * Created by jaherrera on 08/01/2016.
 */


@Component("SalesCurrencyConversionDecorator")
@Transactional
public class SalesCurrencyConversionDecorator implements ITotalCalculator {

    @Inject
    private CountryRateRepository countryRateRepository;

    public void calculateTotal(Sale sale) {

        // Use Web Service to get conversion rate.

        CountryRate countryRate = countryRateRepository.findByCountry(sale.getCountry());

        BigDecimal rate = countryRate.getRate();

        sale.setRate(rate);
    }
}

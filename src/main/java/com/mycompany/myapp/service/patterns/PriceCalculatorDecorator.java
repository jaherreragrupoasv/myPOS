package com.mycompany.myapp.service.patterns;

import com.mycompany.myapp.domain.Sale;

/**
 * Created by jaherrera on 08/01/2016.
 */
public abstract class PriceCalculatorDecorator implements ITotalCalculator {

    protected ITotalCalculator priceCalculator;

    public PriceCalculatorDecorator(ITotalCalculator pc) {
        this.priceCalculator = pc;
    }

    public void calculateTotal(Sale sale) {priceCalculator.calculateTotal(sale);
    }
}


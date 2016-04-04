package com.mycompany.myapp.service.patterns;

import com.mycompany.myapp.domain.Sale;

/**
 * Created by jaherrera on 08/01/2016.
 */


public interface ITotalCalculator {
    void calculateTotal(Sale sale);
}

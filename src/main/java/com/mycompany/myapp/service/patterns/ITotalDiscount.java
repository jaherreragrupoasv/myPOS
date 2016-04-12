package com.mycompany.myapp.service.patterns;

import com.mycompany.myapp.domain.Discount;

import java.math.BigDecimal;

/**
 * Created by jaherrera on 10/04/2016.
 */
public interface ITotalDiscount {

    public BigDecimal getDiscount(Long saleID, Discount discount);

}

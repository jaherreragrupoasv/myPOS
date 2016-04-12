package com.mycompany.myapp.service.patterns;

import com.mycompany.myapp.domain.Discount;

import java.math.BigDecimal;

/**
 * Created by jaherrera on 10/04/2016.
 */
public class DiscountContext {

    private ITotalDiscount discountContext = null;

    public BigDecimal getDiscount(Long saleID, Discount discount) {
        return discountContext.getDiscount(saleID,discount);
    }

    public ITotalDiscount getDiscount() {
        return discountContext;
    }
    public void setDiscount(ITotalDiscount discount) {
        this.discountContext = discount;
    }

}

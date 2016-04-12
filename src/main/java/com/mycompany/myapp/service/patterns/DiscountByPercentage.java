package com.mycompany.myapp.service.patterns;

import com.mycompany.myapp.domain.Discount;
import com.mycompany.myapp.repository.DiscountRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.math.BigDecimal;

/**
 * Created by jaherrera on 10/04/2016.
 */

@Component("DiscountByPercentage")
@Transactional
public class DiscountByPercentage implements ITotalDiscount {

    @Inject
    private DiscountRepository discountRepository;

    @Override
    public BigDecimal getDiscount(Long saleID, Discount discount) {

        //we get the total bought in that category or product.
        BigDecimal total = discountRepository.getBaseOfDiscount(saleID, discount.getId());

        if (total == null) {total = new BigDecimal(0);}

        return total.multiply(discount.getValue().multiply(new BigDecimal(0.01)));
    }
}


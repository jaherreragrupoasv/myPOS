package com.mycompany.myapp.service.patterns;

import com.mycompany.myapp.domain.Article;
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

@Component("DiscountByQuantity")
@Transactional
public class DiscountByQuantity implements ITotalDiscount {

    @Inject
    private DiscountRepository discountRepository;

    @Override
    public BigDecimal getDiscount(Long saleID, Discount discount) {

        BigDecimal discounts = new BigDecimal(0);

        //Getting the units that we have bought
        BigDecimal numberOfItems = discountRepository.getNumberOfItems(saleID, discount.getId());

        if (numberOfItems == null) {numberOfItems = new BigDecimal(0);}

        BigDecimal numberOfItemsToDiscount = numberOfItems.subtract(discount.getValue());

        if (numberOfItemsToDiscount.intValue() > 0) {
            Article article = discount.getArticle();

            discounts = article.getPrice().multiply(numberOfItemsToDiscount);
        }

        return discounts;
    }
}


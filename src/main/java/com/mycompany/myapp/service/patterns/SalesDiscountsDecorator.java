package com.mycompany.myapp.service.patterns;

import com.mycompany.myapp.domain.Article;
import com.mycompany.myapp.domain.Discount;
import com.mycompany.myapp.domain.Sale;
import com.mycompany.myapp.domain.SaleLine;
import com.mycompany.myapp.domain.enumeration.DiscountType;
import com.mycompany.myapp.repository.ArticleRepository;
import com.mycompany.myapp.repository.DiscountRepository;
import com.mycompany.myapp.repository.SaleLineRepository;
import com.mycompany.myapp.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by jaherrera on 08/01/2016.
 */

@Component("SalesDiscountsDecorator")
@Transactional
public class SalesDiscountsDecorator implements ITotalCalculator {

    BigDecimal discounts;

    @Inject
    private SaleLineRepository saleLineRepository;

    @Inject
    private DiscountRepository discountRepository;

    @Inject
    private ArticleRepository articleRepository;

    public void calculateTotal(Sale sale) {

        BigDecimal discounts = new BigDecimal(0);

//        Traigo los descuentos aplicables
        List<Discount> discountsToApply = saleLineRepository.getDiscountsToApply(sale.getId(), LocalDate.now(), LocalDate.now());

        // Para cada descuento ...
        for (Discount d : discountsToApply) {
            if (d.getType() == DiscountType.CANTIDAD) {

//                Traemos las unidades compradas de ese producto
                BigDecimal numberOfItems = discountRepository.getNumberOfItems(sale.getId(), d.getId());

                if (numberOfItems == null) {numberOfItems = new BigDecimal(0);}

                BigDecimal numberOfItemsToDiscount = numberOfItems.subtract(d.getValue());

                if (numberOfItemsToDiscount.intValue() > 0) {
                    Article article = d.getArticle();

                    discounts = discounts.add(article.getPrice().multiply(numberOfItemsToDiscount));
                }

            } else if (d.getType() == DiscountType.PORCENTAJE) {

//                Traemos el total de compra de esa categoria o producto o total
                BigDecimal total = discountRepository.getBaseOfDiscount(sale.getId(), d.getId());

                if (total == null) {total = new BigDecimal(0);}

                discounts = discounts.add(total.multiply(d.getValue().multiply(new BigDecimal(0.01))));
            }
        }

        sale.setDiscounts(discounts);
    }
}

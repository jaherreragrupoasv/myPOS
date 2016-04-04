package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Discount;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Spring Data JPA repository for the Discount entity.
 */
public interface DiscountRepository extends JpaRepository<Discount,Long> {


    String sql = "SELECT SUM(s.quantity) " +
        "FROM Discount d, SaleLine s WHERE " +
        "s.sale_id = :sale_id AND " +
        "d.id = :id AND " +
        "d.article.id = s.article.id ";

    @Query(value = sql)
    BigDecimal getNumberOfItems(@Param("sale_id") Long sale_id, @Param("id") Long id);

    String sql2 = "SELECT SUM(s.quantity * s.price) " +
        "FROM Discount d, SaleLine s WHERE " +
        "s.sale_id = :sale_id AND " +
        "d.id = :id AND " +
        "(" +
        "  (d.category.id = s.article.category.id) " +
        "OR " +
        "  (d.article.id = s.article.id) " +
        "OR " +
        "  (d.article.id IS NULL AND d.category.id IS NULL) " +
        ")";

    @Query(value = sql2)
    BigDecimal getBaseOfDiscount(@Param("sale_id") Long sale_id, @Param("id") Long id);

}

package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Discount;
import com.mycompany.myapp.domain.SaleLine;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * Spring Data JPA repository for the SaleLine entity.
 */

public interface SaleLineRepository extends JpaRepository<SaleLine,Long> {

    @Procedure(procedureName = "discountsToApply")
    List<Discount> discountsToApply(Long idSale);


    String sql = "SELECT DISTINCT d FROM Discount d, SaleLine s WHERE " +
                 "s.sale_id = :id AND " +
                 "(d.toDate IS NULL OR d.toDate >= :toDate) AND " +
                 "(d.fromDate IS NULL OR d.fromDate <= :fromDate) AND " +
                 "(" +
                 "  (d.category.id = s.article.category.id) " +
                 "OR " +
                 "  (d.article.id = s.article.id) " +
                 "OR " +
                 "  (d.article.id IS NULL AND d.category.id IS NULL) " +
                 ")";

    @Query(sql)
    List<Discount> getDiscountsToApply(@Param("id") Long id, @Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);


    List<SaleLine> findByarticle_id(Long id);

}

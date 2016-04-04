package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Sale;

import com.mycompany.myapp.domain.SaleLine;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Sale entity.
 */
public interface SaleRepository extends JpaRepository<Sale,Long> {

    @Query("SELECT saleline FROM SaleLine saleline where saleline.sale_id = :id")
    List<SaleLine> getSaleLines(@Param("id") Long id);

//    @Query("UPDATE sale SET sale.totalPaied = (SELECT totalPaied = SUM(amount) FROM SalePayment sp WHERE sp.sale.id = :id) FROM Sale sale where sale.id = :id")
//    BigDecimal updateSalePaied(@Param("id") Long id);

    @Query(value = "SELECT SUM(amount) FROM SalePayment sp WHERE sp.sale_id = :id")
    BigDecimal totalSalePaied(@Param("id") Long id);

}

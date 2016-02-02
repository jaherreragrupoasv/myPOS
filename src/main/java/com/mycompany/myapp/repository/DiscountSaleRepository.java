package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.DiscountSale;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the DiscountSale entity.
 */
public interface DiscountSaleRepository extends JpaRepository<DiscountSale,Long> {

}

package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.DiscountSaleLine;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the DiscountSaleLine entity.
 */
public interface DiscountSaleLineRepository extends JpaRepository<DiscountSaleLine,Long> {

}

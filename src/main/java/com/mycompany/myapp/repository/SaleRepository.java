package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Sale;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Sale entity.
 */
public interface SaleRepository extends JpaRepository<Sale,Long> {

}

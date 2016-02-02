package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.SaleLine;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the SaleLine entity.
 */
public interface SaleLineRepository extends JpaRepository<SaleLine,Long> {

}

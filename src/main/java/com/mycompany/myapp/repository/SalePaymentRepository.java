package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.SalePayment;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the SalePayment entity.
 */
public interface SalePaymentRepository extends JpaRepository<SalePayment,Long> {

}

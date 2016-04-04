package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.CountryRate;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the CountryRate entity.
 */
public interface CountryRateRepository extends JpaRepository<CountryRate,Long> {

    CountryRate findByCountry(String country);

}

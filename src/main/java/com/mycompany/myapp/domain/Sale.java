package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.LocalDate;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Sale.
 */
@Entity
@Table(name = "sale")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Sale implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @NotNull
    @Size(max = 10)
    @Column(name = "country", length = 10, nullable = false)
    private String country;

    @Column(name = "rate", precision=10, scale=2)
    private BigDecimal rate;

    @Column(name = "sub_total", precision=10, scale=2)
    private BigDecimal subTotal;

    @Column(name = "discounts", precision=10, scale=2)
    private BigDecimal discounts;

    @Column(name = "taxes", precision=10, scale=2)
    private BigDecimal taxes;

    @Column(name = "total", precision=10, scale=2)
    private BigDecimal total;

    @Column(name = "total_paied", precision=10, scale=2)
    private BigDecimal totalPaied;

    @Column(name = "print_date")
    private LocalDate printDate;

    @OneToMany(mappedBy = "sale")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<SaleLine> saleLines = new HashSet<>();

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public BigDecimal getDiscounts() {
        return discounts;
    }

    public void setDiscounts(BigDecimal discounts) {
        this.discounts = discounts;
    }

    public BigDecimal getTaxes() {
        return taxes;
    }

    public void setTaxes(BigDecimal taxes) {
        this.taxes = taxes;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getTotalPaied() {
        return totalPaied;
    }

    public void setTotalPaied(BigDecimal totalPaied) {
        this.totalPaied = totalPaied;
    }

    public LocalDate getPrintDate() {
        return printDate;
    }

    public void setPrintDate(LocalDate printDate) {
        this.printDate = printDate;
    }

    public Set<SaleLine> getSaleLines() {
        return saleLines;
    }

    public void setSaleLines(Set<SaleLine> saleLines) {
        this.saleLines = saleLines;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Sale sale = (Sale) o;
        return Objects.equals(id, sale.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Sale{" +
            "id=" + id +
            ", fecha='" + fecha + "'" +
            ", country='" + country + "'" +
            ", rate='" + rate + "'" +
            ", subTotal='" + subTotal + "'" +
            ", discounts='" + discounts + "'" +
            ", taxes='" + taxes + "'" +
            ", total='" + total + "'" +
            ", totalPaied='" + totalPaied + "'" +
            ", printDate='" + printDate + "'" +
            '}';
    }
}

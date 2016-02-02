package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DiscountSaleLine.
 */
@Entity
@Table(name = "discount_sale_line")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DiscountSaleLine implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "amount", precision=10, scale=2, nullable = false)
    private BigDecimal amount;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @ManyToOne
    @JoinColumn(name = "saleLine_id")
    private SaleLine saleline;

    public void setSale(SaleLine saleline) {
        this.saleline = saleline;
    }

    @ManyToOne
    @JoinColumn(name = "discount_id")
    private Discount discount;

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DiscountSaleLine discountSaleLine = (DiscountSaleLine) o;
        return Objects.equals(id, discountSaleLine.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "DiscountSaleLine{" +
            "id=" + id +
            ", amount='" + amount + "'" +
            '}';
    }
}

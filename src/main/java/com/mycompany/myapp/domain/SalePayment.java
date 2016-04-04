package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mycompany.myapp.domain.enumeration.PaymentType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.LocalDate;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A SalePayment.
 */
@Entity
@Table(name = "sale_payment")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SalePayment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private PaymentType type;

    @Size(max = 20)
    @Column(name = "credit_card", length = 20)
    private String creditCard;

    @NotNull
    @Column(name = "amount", precision=10, scale=2, nullable = false)
    private BigDecimal amount;


    @NotNull
    @JsonProperty
    protected Long sale_id;

    @Column(name = "sale_id")
    public Long getSale_id() {
        return sale_id;
    }

    public void setSale_id(Long id) {
        this.sale_id = id;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PaymentType getType() {
        return type;
    }

    public void setType(PaymentType type) {
        this.type = type;
    }

    public String getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SalePayment salePayment = (SalePayment) o;
        return Objects.equals(id, salePayment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SalePayment{" +
            "id=" + id +
            ", type='" + type + "'" +
            ", creditCard='" + creditCard + "'" +
            ", amount='" + amount + "'" +
            '}';
    }
}

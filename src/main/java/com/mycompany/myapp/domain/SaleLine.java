package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mycompany.myapp.domain.patterns.CostItem;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A SaleLine.
 */
@Entity
@Table(name = "sale_line")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SaleLine implements Serializable, CostItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @NotNull
    @Column(name = "price", precision=10, scale=2, nullable = false)
    private BigDecimal price;

    @Column(name = "tax", precision=10, scale=2)
    private BigDecimal tax;

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

//    @ManyToOne
//    @JsonBackReference
//    @JoinColumn(name = "sale_id")
//    private Sale sale;
//
//    public Sale getSale() {
//        return sale;
//    }
//
//    public void setSale(Sale sale) {
//        this.sale = sale;
//    }

    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;

    public void setArticle(Article article) {
        this.article = article;
    }

    public Article getArticle() {
        return article;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal CalculateTotal() {
        return (price.multiply(new BigDecimal(quantity)));
    }

    public BigDecimal CalculateTaxes() {
        return (price.multiply(new BigDecimal(quantity)).multiply(tax).multiply(new BigDecimal(0.01)));
    }

    public void show() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SaleLine saleLine = (SaleLine) o;
        return Objects.equals(id, saleLine.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SaleLine{" +
            "id=" + id +
            ", quantity='" + quantity + "'" +
            ", price='" + price + "'" +
            ", tax='" + tax + "'" +
            '}';
    }
}

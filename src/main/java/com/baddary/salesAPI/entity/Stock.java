package com.baddary.salesAPI.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "stock", indexes = @Index(name = "idx_product_expire", columnList = "product_id, expire"))
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
    @Column(nullable = false)
    private LocalDate expireDate;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal quantitySU;
    @Column(nullable = false)
    private String batch;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal priceSU;

    public Stock() {
    }
    public BigDecimal getPriceSU() {
        return priceSU;
    }

    public void setPriceSU(BigDecimal priceSU) {
        if (priceSU == null) {
            throw new IllegalArgumentException("priceSU cannot be null");
        }
        if (priceSU.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("priceSU must be greater than zero");
        }
        this.priceSU = priceSU;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public LocalDate getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDate expire) {
        this.expireDate = expire;
    }

    public BigDecimal getQuantitySU() {
        return quantitySU;
    }

    public void setQuantitySU(BigDecimal quantitySU) {
        if (quantitySU == null) {
            throw new IllegalArgumentException("quantitySU cannot be null");
        }
        if (quantitySU.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("quantitySU must be greater than or equal to zero");
        }
        this.quantitySU = quantitySU;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Stock other = (Stock) obj;
        return id != null && id.equals(other.id);
    }
}

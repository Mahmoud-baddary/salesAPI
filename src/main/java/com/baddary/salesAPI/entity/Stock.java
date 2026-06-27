package com.baddary.salesAPI.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "stock", indexes = @Index(name = "idx_product_expire", columnList = "productId, expire"))
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    private LocalDate expire;
    private double quantitySU;
    private String batch;
    private double priceSU;

    public Stock() {
    }

    public double getPriceSU() {
        return priceSU;
    }

    public void setPriceSU(double priceSU) {
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

    public LocalDate getExpire() {
        return expire;
    }

    public void setExpire(LocalDate expire) {
        this.expire = expire;
    }

    public double getQuantitySU() {
        return quantitySU;
    }

    public void setQuantitySU(double quantitySU) {
        this.quantitySU = quantitySU;
    }
}

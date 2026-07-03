package com.baddary.salesAPI.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;

public class StockDTO {
    private Long id;
    @NotNull(message = "product id is required")
    private Long productId;
    @NotNull(message = "expire date is required")
    private LocalDate expire;
    @PositiveOrZero(message = "quantitySU must be equal or greater than zero")
    private double quantitySU;
    @NotBlank(message = "batch is required")
    private String batch;
    @Positive(message = "priceSU must be greater than zero")
    private BigDecimal priceSU;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
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

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public BigDecimal getPriceSU() {
        return priceSU;
    }

    public void setPriceSU(BigDecimal priceSU) {
        this.priceSU = priceSU;
    }
}

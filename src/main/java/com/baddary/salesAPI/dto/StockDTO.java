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
    private LocalDate expireDate;
    @NotNull(message = "quantitySU is required")
    @PositiveOrZero(message = "quantitySU must be equal or greater than zero")
    private BigDecimal quantitySU;
    @NotBlank(message = "batch is required")
    private String batch;
    @NotNull(message = "priceSU is required")
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

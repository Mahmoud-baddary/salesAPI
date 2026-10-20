package com.baddary.salesAPI.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public class OrderProductDTO {
    @NotNull(message = "product id is required")
    private Long productId;
    @NotBlank(message = "batch is required")
    private String batch;
    @NotBlank(message = "unit is required")
    private String unit;
    private String note;
    @Positive(message = "quantity must be greater than zero")
    private BigDecimal quantity;
    @Positive(message = "price must be positive number")
    private BigDecimal price;
    private BigDecimal discount = BigDecimal.ZERO;
    @NotNull(message = "expire is required")
    private LocalDate expireDate;
    private Long orderId;
    private ProductSummaryDTO productSummaryDTO;
    @Positive (message = "Quantity of smallest unit must be a positive number")
    private BigDecimal quantitySU;
    @Positive (message = "Price of smallest unit must be a positive number")
    private BigDecimal priceSU;
    

    public Long getOrderId() {
        return orderId;
    }
    

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
    

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount != null ? discount : BigDecimal.ZERO;
    }

    public LocalDate getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDate expireDate) {
        this.expireDate = expireDate;
    }

    public ProductSummaryDTO getProductSummaryDTO() {
        return productSummaryDTO;
    }

    public void setProductSummaryDTO(ProductSummaryDTO productSummaryDTO) {
        this.productSummaryDTO = productSummaryDTO;
    }

    public BigDecimal getQuantitySU() {
        return quantitySU;
    }

    public void setQuantitySU(BigDecimal quantitySU) {
        this.quantitySU = quantitySU;
    }


    public BigDecimal getPriceSU() {
        return priceSU;
    }


    public void setPriceSU(BigDecimal priceSU) {
        this.priceSU = priceSU;
    }
}

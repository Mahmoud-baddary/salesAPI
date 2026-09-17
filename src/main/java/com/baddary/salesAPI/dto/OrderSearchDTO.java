package com.baddary.salesAPI.dto;

import java.time.LocalDate;
import com.baddary.salesAPI.enums.OrderType;

import jakarta.validation.constraints.NotNull;

public class OrderSearchDTO {
    private Long orderId;
    private Long customerId;
    private Long userId;
    @NotNull(message = "From Date is required")
    private LocalDate fromDate;
    @NotNull(message = "To Date is required")
    private LocalDate toDate;
    private OrderType orderType;
    private String productBarcode;

    public String getProductBarcode() {
        return productBarcode;
    }

    public void setProductBarcode(String productBarcode) {
        this.productBarcode = productBarcode;
    }

    public OrderSearchDTO() {
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

}

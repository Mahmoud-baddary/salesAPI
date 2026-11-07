package com.baddary.salesAPI.dto;

import java.time.LocalDate;
import com.baddary.salesAPI.enums.OrderType;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

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
    @PositiveOrZero(message = "Page number cannot be negative")
    private int page = 0; // Default to the first page

    @Positive(message = "Page size must be greater than zero")
    private int size = 20; // Default to 20 items per page

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

    @AssertTrue(message = "To Date must be after From Date")
    public boolean isDateRangeValid() {
        if (fromDate == null || toDate == null)
            return true;
        return !toDate.isBefore(fromDate);
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

}

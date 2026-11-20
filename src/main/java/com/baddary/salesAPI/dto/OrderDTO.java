package com.baddary.salesAPI.dto;

import com.baddary.salesAPI.enums.OrderType;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

public class OrderDTO {
    private Long id;
    @NotNull(message = "date is required")
    private LocalDate date;
    @NotNull(message = "time is required")
    private LocalTime time;
    @NotNull(message = "order type is required")
    private OrderType orderType;
    @NotNull(message = "paid money is required and must be zero or positive")
    @PositiveOrZero(message = "paid money is required and must be zero or positive")
    private BigDecimal paidMoney;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal totalPrice;
    private BigDecimal discount = BigDecimal.ZERO;
    @NotNull(message = "Customer id is required")
    private Long customerId;
    private Long userId;
    private Long originalOrderId; 
    @Valid
    private final Set<OrderProductDTO> orderProductDTOSet = new HashSet<>();

    private String customerName;
    private String userName;

    public Long getId() {
        return id;
    }

    public void setOrderProductDTOSet(Set<OrderProductDTO> orderProductDTOSet) {
        this.orderProductDTOSet.clear();
        if (orderProductDTOSet != null) {
            this.orderProductDTOSet.addAll(orderProductDTOSet);
        }
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public BigDecimal getPaidMoney() {
        return paidMoney;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount != null ? discount : BigDecimal.ZERO;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getUserId() {
        return userId;
    }

    
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    

    public Set<OrderProductDTO> getOrderProductDTOSet() {
        return orderProductDTOSet;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        if (totalPrice == null || totalPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("totalPrice must be greater than zero");
        }
        this.totalPrice = totalPrice;
    }

    

    public void setPaidMoney(BigDecimal paidMoney) {
        this.paidMoney = paidMoney;
    }

    public Long getOriginalOrderId() {
        return originalOrderId;
    }

    public void setOriginalOrderId(Long originalOrderId) {
        this.originalOrderId = originalOrderId;
    }
}

package com.baddary.salesAPI.dto;

import com.baddary.salesAPI.enums.OrderType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    @NotNull(message = "paid money is required")
    private BigDecimal paidMoney;
    private BigDecimal discount = BigDecimal.ZERO;
    @NotNull(message = "Customer id is required")
    private Long customerId;
    @NotNull(message = "User id is required")
    private Long userId;
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

    public void setPaymentType(BigDecimal paidMoney) {
        this.paidMoney = paidMoney;
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

    // public double getTotalPrice() {
    // return orderProductDTOSet.stream()
    // .mapToDouble(item -> (item.getQuantity() * item.getPrice()) * (1 -
    // item.getDiscount() / 100.0))
    // .sum() * (1 - discount / 100.0);
    // }
    public BigDecimal calculateTotalPrice() {
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (OrderProductDTO item : orderProductDTOSet) {
            BigDecimal quantity = BigDecimal.valueOf(item.getQuantity());
            BigDecimal price = item.getPrice();
            BigDecimal itemDiscount = item.getDiscount();
            totalPrice = totalPrice.add((quantity.multiply(price).multiply(
                    BigDecimal.ONE.subtract(itemDiscount.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP)))));
        }
        return totalPrice
                .multiply(BigDecimal.ONE
                        .subtract(this.discount.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP)))
                .setScale(2, RoundingMode.HALF_UP);
    }
}

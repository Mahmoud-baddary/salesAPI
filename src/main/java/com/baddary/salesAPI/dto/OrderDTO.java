package com.baddary.salesAPI.dto;


import com.baddary.salesAPI.enums.OrderType;
import com.baddary.salesAPI.enums.PaymentType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "payment type is required")
    private PaymentType paymentType;
    private double discount;
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

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
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
}

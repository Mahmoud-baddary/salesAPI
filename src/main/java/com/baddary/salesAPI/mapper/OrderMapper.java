package com.baddary.salesAPI.mapper;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.baddary.salesAPI.dto.OrderDTO;
import com.baddary.salesAPI.dto.OrderProductDTO;
import com.baddary.salesAPI.entity.Customer;
import com.baddary.salesAPI.entity.Order;
import com.baddary.salesAPI.entity.User;

public class OrderMapper {
    private OrderMapper() {
    }

    public static Order toEntity(OrderDTO dto, User user, Customer customer, Order originalOrder) {
        Order entity = new Order();
        entity.setCustomer(customer);
        entity.setOriginalOrder(originalOrder);
        entity.setDate(dto.getDate());
        entity.setTime(dto.getTime());
        entity.setDiscount(dto.getDiscount());
        entity.setUser(user);
        entity.setPaidMoney(dto.getPaidMoney());
        entity.setOrderType(dto.getOrderType());
        entity.setTotalPrice(calculateTotalPrice(dto));
        return entity;
    }

    public static OrderDTO toDTO(Order entity) {
        OrderDTO dto = new OrderDTO();
        dto.setId(entity.getId());
        dto.setDate(entity.getDate());
        dto.setTime(entity.getTime());
        dto.setOrderType(entity.getOrderType());
        dto.setDiscount(entity.getDiscount());
        dto.setPaidMoney(entity.getPaidMoney());
        dto.setTotalPrice(entity.getTotalPrice());
        if (entity.getCustomer() != null) {
            dto.setCustomerId(entity.getCustomer().getId());
            dto.setCustomerName(entity.getCustomer().getName());
        }
        if (entity.getUser() != null) {
            dto.setUserId(entity.getUser().getId());
            dto.setUserName(entity.getUser().getName());
        }
        if (entity.getOriginalOrder() != null) {
            dto.setOriginalOrderId(entity.getOriginalOrder().getId());
        }
        dto.getOrderProductDTOSet()
                .addAll(entity.getOrderProductSet().stream().map(OrderProductMapper::toDTO).toList());

        return dto;
    }
    public static OrderDTO toSummaryDTO(Order entity) {
        OrderDTO dto = new OrderDTO();
        dto.setId(entity.getId());
        dto.setDate(entity.getDate());
        dto.setTime(entity.getTime());
        dto.setOrderType(entity.getOrderType());
        dto.setDiscount(entity.getDiscount());
        dto.setPaidMoney(entity.getPaidMoney());
        dto.setTotalPrice(entity.getTotalPrice());
        if (entity.getCustomer() != null) {
            dto.setCustomerId(entity.getCustomer().getId());
            dto.setCustomerName(entity.getCustomer().getName());
        }
        if (entity.getUser() != null) {
            dto.setUserId(entity.getUser().getId());
            dto.setUserName(entity.getUser().getName());
        }
        if (entity.getOriginalOrder() != null) {
            dto.setOriginalOrderId(entity.getOriginalOrder().getId());
        }
        return dto;
    }
    private static BigDecimal calculateTotalPrice(OrderDTO orderDTO) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (OrderProductDTO item : orderDTO.getOrderProductDTOSet()) {
            BigDecimal quantity = item.getQuantity();
            BigDecimal price = item.getPrice();
            BigDecimal itemDiscount = item.getDiscount();
            totalPrice = totalPrice.add((quantity.multiply(price).multiply(
                    BigDecimal.ONE.subtract(itemDiscount.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP)))));
        }
        return totalPrice
                .multiply(BigDecimal.ONE
                        .subtract(orderDTO.getDiscount().divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP)))
                .setScale(2, RoundingMode.HALF_UP);
    }
}

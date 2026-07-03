package com.baddary.salesAPI.mapper;


import com.baddary.salesAPI.dto.OrderDTO;
import com.baddary.salesAPI.entity.Customer;
import com.baddary.salesAPI.entity.Order;
import com.baddary.salesAPI.entity.User;

public class OrderMapper {
    private OrderMapper(){}
    public static Order toEntity (OrderDTO dto, User user, Customer customer){
        Order entity = new Order();
        entity.setCustomer(customer);
        entity.setDate(dto.getDate());
        entity.setTime(dto.getTime());
        entity.setDiscount(dto.getDiscount());
        entity.setUser(user);
        entity.setPaidMoney(dto.getPaidMoney());
        entity.setOrderType(dto.getOrderType());
        return entity;
    }

    public static OrderDTO toDTO(Order entity){
        OrderDTO dto = new OrderDTO();
        dto.setId(entity.getId());
        dto.setDate(entity.getDate());
        dto.setTime(entity.getTime());
        dto.setOrderType(entity.getOrderType());
        dto.setDiscount(entity.getDiscount());
        dto.setPaymentType(entity.getPaidMoney());
        dto.setCustomerId(entity.getCustomer().getId());
        dto.setUserId(entity.getUser().getId());
        dto.setCustomerName(entity.getCustomer().getName());
        dto.setUserName(entity.getUser().getName());
        dto.getOrderProductDTOSet().addAll(entity.getOrderProductSet().stream().map(OrderProductMapper::toDTO).toList());

        return dto;
    }

}

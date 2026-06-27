package com.baddary.salesAPI.mapper;


import com.baddary.salesAPI.dto.OrderProductDTO;
import com.baddary.salesAPI.entity.Order;
import com.baddary.salesAPI.entity.OrderProduct;
import com.baddary.salesAPI.entity.Product;

public class OrderProductMapper {
    private OrderProductMapper(){}

    public static OrderProduct toEntity(OrderProductDTO dto, Product p, Order order){
        OrderProduct entity = new OrderProduct();
        entity.setProduct(p);
        entity.setOrder(order);
        entity.setUnit(dto.getUnit());
        entity.setPrice(dto.getPrice());
        entity.setQuantity(dto.getQuantity());
        entity.setExpireDate(dto.getExpireDate());
        entity.setNote(dto.getNote());
        entity.setDiscount(dto.getDiscount());
        entity.setBatch(dto.getBatch());
        entity.setQuantitySU();
        entity.setPriceSU();
        return entity;
    }
    public static OrderProductDTO toDTO(OrderProduct entity){
        OrderProductDTO dto = new OrderProductDTO();
        dto.setProductId(entity.getProduct().getId());
        dto.setOrderId(entity.getOrder().getId());
        dto.setUnit(entity.getUnit());
        dto.setPrice(entity.getPrice());
        dto.setQuantity(entity.getQuantity());
        dto.setExpireDate(entity.getExpireDate());
        dto.setNote(entity.getNote());
        dto.setDiscount(entity.getDiscount());
        dto.setBatch(entity.getBatch());
        dto.setProductName(entity.getProduct().getName());
        return dto;

    }
}

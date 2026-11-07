package com.baddary.salesAPI.mapper;


import java.math.BigDecimal;

import com.baddary.salesAPI.dto.OrderProductDTO;
import com.baddary.salesAPI.entity.Order;
import com.baddary.salesAPI.entity.OrderProduct;
import com.baddary.salesAPI.entity.Product;

public class OrderProductMapper {
    private OrderProductMapper() {
    }

    public static OrderProduct toEntity(OrderProductDTO dto, Product p, Order order) {
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
        entity.setQuantitySU(calculateQuantitSU(p, dto.getQuantity(), dto.getUnit()));
        entity.setPriceSU(calculatePriceSU(p, dto.getPrice(), dto.getUnit()));
        return entity;
    }

    public static OrderProductDTO toDTO(OrderProduct entity) {
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
        dto.setProductSummaryDTO(ProductMapper.toSummaryDTO(entity.getProduct()));
        dto.setPriceSU(entity.getPriceSU());
        dto.setQuantitySU(entity.getQuantitySU());
        return dto;

    }

    private static BigDecimal calculateQuantitSU(Product product, BigDecimal quantity, String unit) {
        if (unit.equals(product.getSmallestUnit())) {
            return quantity;
        } else if (unit.equals(product.getMediumUnit())) {
            return quantity.multiply((new BigDecimal(product.getSmallestUnitAmount()).divide(new BigDecimal(product.getMediumUnitAmount()))));
        } else if (unit.equals(product.getGreatestUnit())) {
            return quantity.multiply(new BigDecimal(product.getSmallestUnitAmount()));
        } else {
            throw new IllegalArgumentException("Invalid unit: " + unit);
        }
    }

    private static BigDecimal calculatePriceSU(Product product, BigDecimal price, String unit) {
        if (unit.equals(product.getSmallestUnit())) {
            return price;
        } else if (unit.equals(product.getMediumUnit())) {
            return price.divide((new BigDecimal(product.getSmallestUnitAmount()).divide(new BigDecimal(product.getMediumUnitAmount()))));
        } else if (unit.equals(product.getGreatestUnit())) {
            return price.divide(new BigDecimal(product.getSmallestUnitAmount()));
        } else {
            throw new IllegalArgumentException("Invalid unit: " + unit);
        }
    }
}

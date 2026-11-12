package com.baddary.salesAPI.mapper;

import java.math.BigDecimal;
import java.math.RoundingMode;

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
        entity.setQuantitySU(calculateQuantitySU(p, dto.getQuantity(), dto.getUnit()));
        entity.setPriceSU(calculatePriceSU(p, dto.getPrice(), dto.getUnit()));
        return entity;
    }

    public static OrderProductDTO toDTO(OrderProduct entity) {
        OrderProductDTO dto = new OrderProductDTO();
        if (entity.getProduct() != null) {
            dto.setProductId(entity.getProduct().getId());
            dto.setProductSummaryDTO(ProductMapper.toSummaryDTO(entity.getProduct()));

        }
        if (entity.getOrder() != null) {
            dto.setOrderId(entity.getOrder().getId());
        }
        dto.setUnit(entity.getUnit());
        dto.setPrice(entity.getPrice());
        dto.setQuantity(entity.getQuantity());
        dto.setExpireDate(entity.getExpireDate());
        dto.setNote(entity.getNote());
        dto.setDiscount(entity.getDiscount());
        dto.setBatch(entity.getBatch());
        dto.setPriceSU(entity.getPriceSU());
        dto.setQuantitySU(entity.getQuantitySU());
        return dto;

    }

    private static BigDecimal calculateQuantitySU(Product product, BigDecimal quantity, String unit) {
        if(unit == null || quantity == null || product == null){
            throw new IllegalArgumentException("Unit, quantity, and product must not be null");
        }
        BigDecimal result;
        if (unit.equals(product.getSmallestUnit())) {
            result = quantity;
        } else if (unit.equals(product.getMediumUnit())) {
            result = quantity.multiply(
                (new BigDecimal(product.getSmallestUnitAmount())
                    .divide(new BigDecimal(product.getMediumUnitAmount()), 10, RoundingMode.HALF_UP))
                );
        } else if (unit.equals(product.getGreatestUnit())) {
            result = quantity.multiply(new BigDecimal(product.getSmallestUnitAmount()));
        } else {
            throw new IllegalArgumentException("Invalid unit: " + unit);
        }
        return result.setScale(2, RoundingMode.HALF_UP);
    }

    private static BigDecimal calculatePriceSU(Product product, BigDecimal price, String unit) {
        if (unit == null || price == null || product == null) {
            throw new IllegalArgumentException("Unit, price, and product must not be null");
        }
        BigDecimal result;
        if (unit.equals(product.getSmallestUnit())) {
            result = price;
        } else if (unit.equals(product.getMediumUnit())) {
            result = price.divide((new BigDecimal(product.getSmallestUnitAmount())
                    .divide(new BigDecimal(product.getMediumUnitAmount()), 4, RoundingMode.HALF_UP)), 10, RoundingMode.HALF_UP);
        } else if (unit.equals(product.getGreatestUnit())) {
            result = price.divide(new BigDecimal(product.getSmallestUnitAmount()), 4, RoundingMode.HALF_UP);
        } else {
            throw new IllegalArgumentException("Invalid unit: " + unit);
        }
        return result.setScale(2, RoundingMode.HALF_UP);
    }
}

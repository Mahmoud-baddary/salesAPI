package com.baddary.salesAPI.mapper;

import com.baddary.salesAPI.dto.StockDTO;
import com.baddary.salesAPI.entity.Product;
import com.baddary.salesAPI.entity.Stock;

public class StockMapper {
    private StockMapper(){}

    public static Stock toEntity(StockDTO dto, Product product){
        Stock entity = new Stock();
        entity.setBatch(dto.getBatch());
        entity.setPriceSU(dto.getPriceSU());
        entity.setQuantitySU(dto.getQuantitySU());
        entity.setId(dto.getId());
        entity.setProduct(product);
        entity.setExpireDate(dto.getExpireDate());
        return entity;
    }

    public static StockDTO toDTO(Stock entity){
        StockDTO dto = new StockDTO();
        dto.setBatch(entity.getBatch());
        dto.setPriceSU(entity.getPriceSU());
        dto.setQuantitySU(entity.getQuantitySU());
        dto.setId(entity.getId());
        dto.setExpireDate(entity.getExpireDate());
        if(entity.getProduct() != null) {
            dto.setProductId(entity.getProduct().getId());
        }
        return dto;
    }
}

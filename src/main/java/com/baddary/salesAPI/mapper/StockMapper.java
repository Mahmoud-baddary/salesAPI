package com.baddary.salesAPI.mapper;

import com.baddary.salesAPI.dto.StockDTO;
import com.baddary.salesAPI.entity.Stock;

public class StockMapper {
    private StockMapper(){}

    public static Stock toEntity(StockDTO dto){
        Stock entity = new Stock();
        entity.setBatch(dto.getBatch());
        entity.setPriceSU(dto.getPriceSU());
        entity.setQuantitySU(dto.getQuantitySU());
        entity.setId(dto.getId());
        entity.setExpire(dto.getExpire());
        return entity;
    }

    public static StockDTO toDTO(Stock entity){
        StockDTO dto = new StockDTO();
        dto.setBatch(entity.getBatch());
        dto.setPriceSU(entity.getPriceSU());
        dto.setQuantitySU(entity.getQuantitySU());
        dto.setId(entity.getId());
        dto.setExpire(entity.getExpire());
        dto.setProductId(entity.getProduct().getId());
        return dto;
    }
}

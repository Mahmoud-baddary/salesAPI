package com.baddary.salesAPI.mapper;

import com.baddary.salesAPI.dto.ProductDTO;
import com.baddary.salesAPI.entity.Barcode;
import com.baddary.salesAPI.entity.Product;

public class ProductMapper {
    private ProductMapper() {
    }

    public static ProductDTO toDTO(Product entity) {
        ProductDTO dto = new ProductDTO();
        dto.setName(entity.getName());
        dto.setId(entity.getId());
        dto.setSmallestUnitAmount(entity.getSmallestUnitAmount());
        dto.setMediumUnitAmount(entity.getMediumUnitAmount());
        dto.setSmallestUnit(entity.getSmallestUnit());
        dto.setMediumUnit(entity.getMediumUnit());
        dto.setGreatestUnit(entity.getGreatestUnit());
        entity.getBarcodes().stream().map(BarcodeMapper::toDTO).forEach(dto.getBarcodeDTOSet()::add);
        return dto;
    }

    public static Product toEntity(ProductDTO dto) {
        Product entity = new Product();
        entity.setName(dto.getName());
        entity.setSmallestUnitAmount(dto.getSmallestUnitAmount());
        entity.setMediumUnitAmount(dto.getMediumUnitAmount());
        entity.setGreatestUnit(dto.getGreatestUnit());
        entity.setMediumUnit(dto.getMediumUnit());
        entity.setSmallestUnit(dto.getSmallestUnit());
        dto.getBarcodeDTOSet().forEach(barcodeDTO->{
            Barcode barcode = BarcodeMapper.toEntity(barcodeDTO);
            entity.addBarcode(barcode);
        });
        return entity;
    }


    public static void updateEntity(Product entity, ProductDTO dto) {
        entity.setName(dto.getName());
        entity.setSmallestUnitAmount(dto.getSmallestUnitAmount());
        entity.setMediumUnitAmount(dto.getMediumUnitAmount());
        entity.setMediumUnit(dto.getMediumUnit());
        entity.setSmallestUnit(dto.getSmallestUnit());
        dto.getBarcodeDTOSet().forEach(barcodeDTO->{
            Barcode barcode = BarcodeMapper.toEntity(barcodeDTO);
            entity.addBarcode(barcode);
        });
    }
}

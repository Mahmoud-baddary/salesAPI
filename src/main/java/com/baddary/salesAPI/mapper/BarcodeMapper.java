package com.baddary.salesAPI.mapper;

import com.baddary.salesAPI.dto.BarcodeDTO;
import com.baddary.salesAPI.entity.Barcode;

public class BarcodeMapper {
    private BarcodeMapper(){}


    public static BarcodeDTO toDTO(Barcode entity){
        BarcodeDTO dto = new BarcodeDTO();
        dto.setBarcodeTxt(entity.getBarcodeTxt());
        dto.setProductId(entity.getProduct().getId());
        return dto;
    }
    public static Barcode toEntity(BarcodeDTO dto){
        Barcode entity = new Barcode();
        entity.setBarcodeTxt(dto.getBarcodeTxt());
        return entity;
    }

}

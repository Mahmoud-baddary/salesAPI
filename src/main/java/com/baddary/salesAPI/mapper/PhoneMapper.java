package com.baddary.salesAPI.mapper;

import com.baddary.salesAPI.dto.PhoneDTO;
import com.baddary.salesAPI.entity.Phone;

public class PhoneMapper {
    private PhoneMapper(){

    }

    public static Phone toEntity(PhoneDTO dto){
        Phone phone = new Phone();
        phone.setPhoneNum(dto.getPhoneNum());
        return phone;
    }
    public static PhoneDTO toDTO(Phone entity){
        PhoneDTO dto = new PhoneDTO();
        dto.setId(entity.getId());
        dto.setPhoneNum(entity.getPhoneNum());
        dto.setCustomerId(entity.getCustomer().getId());
        return dto;
    }

}

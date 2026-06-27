package com.baddary.salesAPI.mapper;

import com.baddary.salesAPI.dto.CustomerDTO;
import com.baddary.salesAPI.entity.Customer;
import com.baddary.salesAPI.entity.Phone;

public class CustomerMapper {
    private CustomerMapper() {
    }


    public static CustomerDTO toDTO(Customer entity) {
        CustomerDTO dto = new CustomerDTO();
        dto.setAddress(entity.getAddress());
        dto.setEmail(entity.getEmail());
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setId(entity.getId());
        dto.getPhoneDTOSet().addAll(entity.getPhones().stream().map(PhoneMapper::toDTO).toList());
        return dto;
    }


    public static Customer toEntity(CustomerDTO dto) {
        Customer entity = new Customer();
        entity.setAddress(dto.getAddress());
        entity.setEmail(dto.getEmail());
        entity.setName(dto.getName());
        entity.setId(dto.getId());
        dto.getPhoneDTOSet().forEach(phoneDTO->{
            Phone phone = PhoneMapper.toEntity(phoneDTO);
            entity.addPhone(phone);
        });
        return entity;
    }

    public static void updateEntity(Customer entity, CustomerDTO dto) {
        entity.setAddress(dto.getAddress());
        entity.setEmail(dto.getEmail());
        entity.setName(dto.getName());
        dto.getPhoneDTOSet().forEach(phoneDTO->{
            Phone phone = PhoneMapper.toEntity(phoneDTO);
            entity.addPhone(phone);
        });
    }

}

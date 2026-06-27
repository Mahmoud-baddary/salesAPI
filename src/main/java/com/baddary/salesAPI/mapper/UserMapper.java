package com.baddary.salesAPI.mapper;

import com.baddary.salesAPI.dto.UserDTO;
import com.baddary.salesAPI.entity.User;

public class UserMapper {
    private UserMapper(){
    }
    public static User toEntity(UserDTO dto){
        User entity = new User();
        entity.setId(dto.getId());
        entity.setPassword(dto.getPassword());
        entity.setName(dto.getName());
        entity.setRole(dto.getRole());
        return entity;
    }
    public static UserDTO toDTO(User entity){
        UserDTO dto = new UserDTO();
        dto.setId(entity.getId());
        dto.setPassword(entity.getPassword());
        dto.setName(entity.getName());
        dto.setRole(entity.getRole());
        return dto;
    }
}

package com.baddary.salesAPI.service;

import com.baddary.salesAPI.dto.UserDTO;
import com.baddary.salesAPI.entity.User;
import com.baddary.salesAPI.mapper.UserMapper;
import com.baddary.salesAPI.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<UserDTO> findByNameIgnoreCase(String name) {
        if (name.isBlank()) throw new RuntimeException("Name is required");
        return userRepository.findByNameIgnoreCase(name).map(UserMapper::toDTO);
    }

    public Long count() {
        return userRepository.count();
    }

    public UserDTO addUser(UserDTO userDTO) {
        if (userDTO.getName().isBlank() || userDTO.getPassword().isBlank())
            throw new RuntimeException("name or password is empty");
        User saved = userRepository.save(UserMapper.toEntity(userDTO));
        return UserMapper.toDTO(saved);
    }
}

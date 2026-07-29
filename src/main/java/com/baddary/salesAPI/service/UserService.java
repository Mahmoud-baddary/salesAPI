package com.baddary.salesAPI.service;

import com.baddary.salesAPI.dto.UserDTO;
import com.baddary.salesAPI.entity.CashRegister;
import com.baddary.salesAPI.entity.User;
import com.baddary.salesAPI.mapper.UserMapper;
import com.baddary.salesAPI.repository.CashRegisterRepository;
import com.baddary.salesAPI.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final CashRegisterRepository cashRepository;
    public UserService(UserRepository userRepository, CashRegisterRepository cashRepository) {
        this.userRepository = userRepository;
        this.cashRepository = cashRepository;
    }

    public Optional<UserDTO> findByNameIgnoreCase(String name) {
        if (name.isBlank())
            throw new RuntimeException("Name is required");
        return userRepository.findByNameIgnoreCase(name).map(UserMapper::toDTO);
    }

    public Long count() {
        return userRepository.count();
    }

    @Transactional
    public UserDTO addUser(UserDTO userDTO) {
        if (userDTO.getName().isBlank() || userDTO.getPassword().isBlank())
            throw new RuntimeException("name or password is empty");
        User saved = userRepository.save(UserMapper.toEntity(userDTO));
        CashRegister cashRegister = new CashRegister();
        cashRegister.setCurrentAmount(BigDecimal.ZERO);
        cashRegister.setUser(saved);
        cashRepository.save(cashRegister);
        return UserMapper.toDTO(saved);
    }
}

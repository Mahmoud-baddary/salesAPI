package com.baddary.salesAPI.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.baddary.salesAPI.entity.CashRegister;

public interface CashRegisterRepository extends JpaRepository<CashRegister, Long> {

    Optional<CashRegister> findByUserId(Long userId);
    
}

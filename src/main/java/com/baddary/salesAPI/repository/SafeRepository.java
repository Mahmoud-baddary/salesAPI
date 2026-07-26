package com.baddary.salesAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.baddary.salesAPI.entity.Safe;

public interface SafeRepository extends JpaRepository<Safe, Long> {
    
}

package com.baddary.salesAPI.repository;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.baddary.salesAPI.entity.Safe;

public interface SafeRepository extends JpaRepository<Safe, Long> {
    @Query("""
            SELECT s.totalAmount FROM Safe s
            """)
    Optional<BigDecimal> totalAmount();
}

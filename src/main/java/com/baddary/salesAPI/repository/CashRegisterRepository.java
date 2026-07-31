package com.baddary.salesAPI.repository;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.baddary.salesAPI.entity.CashRegister;

public interface CashRegisterRepository extends JpaRepository<CashRegister, Long> {

    Optional<CashRegister> findByUserId(Long userId);

    @Query("""
            SELECT c.currentAmount FROM CashRegister c WHERE c.user.id = :userId
            """)
    Optional<BigDecimal> currentAmount(@Param("userId") Long userId);

}

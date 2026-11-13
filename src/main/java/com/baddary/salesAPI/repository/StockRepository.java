package com.baddary.salesAPI.repository;

import com.baddary.salesAPI.entity.Stock;

import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {
        @Query("""
                        SELECT s FROM Stock s join fetch s.product p
                        where p.id = :productId and s.expireDate = :expireDate and s.batch = :batch
                        """)

        Optional<Stock> findByProductIdAndExpireAndBatch(@Param("productId") Long productId, @Param("expireDate") LocalDate expireDate, @Param("batch") String batch);

        List<Stock> findByProductId(@Param("productId") Long productId);

        @Query("""
                        SELECT Distinct s FROM Stock s join fetch s.product p
                        where lower(p.name) like lower(concat('%', :name, '%'))
                        """)
        List<Stock> findByProductName(@Param("name") String name);

        @Lock(LockModeType.PESSIMISTIC_WRITE)
        @Query("SELECT s FROM Stock s join fetch s.product p WHERE p.id = :productId AND s.expireDate = :expireDate AND s.batch = :batch")
        Optional<Stock> findStockForUpdate(@Param("productId") Long productId,
                        @Param("expireDate") LocalDate expireDate,
                        @Param("batch") String batch);
}

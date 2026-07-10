package com.baddary.salesAPI.repository;

import com.baddary.salesAPI.entity.Stock;

import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StockRepository extends CrudRepository<Stock, Long> {
    Optional<Stock> findByProductIdAndExpire(Long productId, LocalDate expire);
    List<Stock> findByProductId(Long productId);
    @Query("""
            SELECT s FROM Stock s join fetch s.product p 
            where lower(p.name) like lower(concat('%', :name, '%'))
            """)
    List<Stock> findByProductName(String name);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Stock s WHERE s.product.id = :productId AND s.expire = :expire")
    Optional<Stock> findStockForUpdate(@Param("productId") Long productId,
                                       @Param("expire") LocalDate expire);
}

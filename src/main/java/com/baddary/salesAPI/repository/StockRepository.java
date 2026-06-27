package com.baddary.salesAPI.repository;

import com.baddary.salesAPI.entity.Stock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

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
}

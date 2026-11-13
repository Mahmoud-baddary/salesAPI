package com.baddary.salesAPI.repository;

import com.baddary.salesAPI.entity.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p from Product p left join fetch p.barcodes where lower(p.name) = lower(:name)")
    Optional<Product> findByName(@Param("name") String name);

    @Query("Select Distinct p from Product p left join fetch p.barcodes")
    List<Product> findAll();

    @Query("""
                select distinct p
                from Product p
                left join fetch p.barcodes
                where lower(p.name) like lower(concat('%', :name, '%'))
            """)
    List<Product> findByNameIgnoreCase(@Param("name") String name);
    @Query("Select p from Product p left join fetch p.barcodes where p.id = :id")
    Optional<Product> findById(@Param("id") Long id);
}

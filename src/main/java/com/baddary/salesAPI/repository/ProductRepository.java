package com.baddary.salesAPI.repository;

import com.baddary.salesAPI.entity.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends CrudRepository<Product, Long> {

    List<Product> findByNameContainingIgnoreCase(String name);

    @Query("SELECT p from Product p left join fetch p.barcodes where p.name = :name")
    Optional<Product> findByName(String name);

    @Query("Select p from Product p left join fetch p.barcodes")
    List<Product> findAll();

    @Query("""
                select p
                from Product p
                left join fetch p.barcodes
                where lower(p.name) like lower(concat('%', :name, '%'))
            """)
    List<Product> findByNameIgnoreCase(String name);
    @Query("Select p from Product p left join fetch p.barcodes where p.id = :id")
    Optional<Product> findById(Long id);
}

package com.baddary.salesAPI.service;

import com.baddary.salesAPI.dto.ProductDTO;
import com.baddary.salesAPI.entity.Product;
import com.baddary.salesAPI.exception.ResourceNotFoundException;
import com.baddary.salesAPI.mapper.ProductMapper;
import com.baddary.salesAPI.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final EntityManager entityManager;

    public ProductService(ProductRepository productRepository, EntityManager entityManager) {
        this.productRepository = productRepository;
        this.entityManager = entityManager;
    }

    public List<ProductDTO> searchByName(String name) {
        List<Product> products = productRepository.findByNameIgnoreCase(name);
        return products.stream().map(ProductMapper::toDTO).toList();
    }

    @Transactional
    public ProductDTO updateProduct(long id, ProductDTO dto) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        Product productToUpdate = optionalProduct.orElseThrow(()->new ResourceNotFoundException("Product is not found"));
        productToUpdate.getBarcodes().clear();
        entityManager.flush();
        ProductMapper.updateEntity(productToUpdate, dto);
        Product saved = productRepository.save(productToUpdate);
        return ProductMapper.toDTO(saved);
    }

    public ProductDTO addProduct(ProductDTO dto) {
        Product entity = ProductMapper.toEntity(dto);
        Product saved = productRepository.save(entity);
        return ProductMapper.toDTO(saved);
    }

    public List<ProductDTO> findAll() {
        List<Product> all = productRepository.findAll();
        return all.stream().map(ProductMapper::toDTO).toList();
    }

    public Optional<ProductDTO> findById(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        return optionalProduct.map(ProductMapper::toDTO);
    }

    public Optional<ProductDTO> findByName(String name) {
        Optional<Product> product = productRepository.findByName(name);
        return product.map(ProductMapper::toDTO);
    }
}

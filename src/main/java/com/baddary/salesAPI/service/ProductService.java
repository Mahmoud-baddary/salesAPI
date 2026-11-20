package com.baddary.salesAPI.service;

import com.baddary.salesAPI.dto.ProductDTO;
import com.baddary.salesAPI.entity.Product;
import com.baddary.salesAPI.exception.ResourceNotFoundException;
import com.baddary.salesAPI.mapper.ProductMapper;
import com.baddary.salesAPI.repository.ProductRepository;
import jakarta.persistence.EntityManager;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final EntityManager entityManager;

    public ProductService(ProductRepository productRepository, EntityManager entityManager) {
        this.productRepository = productRepository;
        this.entityManager = entityManager;
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> searchByName(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<Product> productPage = productRepository.findByNameIgnoreCase(name, pageable);
        return productPage.map(ProductMapper::toSummaryDTO);
    }

    @Transactional(rollbackFor = Exception.class)
    public ProductDTO updateProduct(Long id, ProductDTO dto) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        Product productToUpdate = optionalProduct
                .orElseThrow(() -> new ResourceNotFoundException("Product is not found"));
        productToUpdate.getBarcodes().clear();
        entityManager.flush();
        ProductMapper.updateEntity(productToUpdate, dto);
        return ProductMapper.toDTO(productToUpdate);
    }

    @Transactional(rollbackFor = Exception.class)
    public ProductDTO addProduct(ProductDTO dto) {
        Product entity = ProductMapper.toEntity(dto);
        Product saved = productRepository.save(entity);
        return ProductMapper.toDTO(saved);
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<Product> productPage = productRepository.findAll(pageable);
        return productPage.map(ProductMapper::toSummaryDTO);
    }

    @Transactional(readOnly = true)
    public Optional<ProductDTO> findById(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        return optionalProduct.map(ProductMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Optional<ProductDTO> findByName(String name) {
        Optional<Product> product = productRepository.findByName(name);
        return product.map(ProductMapper::toDTO);
    }
}

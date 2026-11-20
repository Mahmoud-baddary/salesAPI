package com.baddary.salesAPI.controller;

import com.baddary.salesAPI.dto.ProductDTO;
import com.baddary.salesAPI.exception.ResourceNotFoundException;
import com.baddary.salesAPI.service.ProductService;
import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ProductDTO>> searchByName(@RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(productService.searchByName(name, page, size));
    }

    @PreAuthorize("hasRole('ADMIN')") // Only admins can access this endpoint
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable long id, @RequestBody @Valid ProductDTO dto) {
        ProductDTO productDTO = productService.updateProduct(id, dto);
        return ResponseEntity.status(200).body(productDTO);
    }

    @PreAuthorize("hasRole('ADMIN')") // Only admins can access this endpoint
    @PostMapping
    public ResponseEntity<ProductDTO> addProduct(@RequestBody @Valid ProductDTO dto) {
        ProductDTO productDTO = productService.addProduct(dto);
        return ResponseEntity.status(201).body(productDTO);
    }

    @GetMapping
    public ResponseEntity<Page<ProductDTO>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(productService.findAll(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> findById(@PathVariable long id) {
        ProductDTO product = productService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product ID : ", id));
        return ResponseEntity.ok(product);
    }

    @GetMapping("/by-name")
    public ResponseEntity<ProductDTO> findByName(@RequestParam String name) {
        ProductDTO product = productService.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Product Name : ", name));
        return ResponseEntity.ok(product);
    }

}

package com.baddary.salesAPI.controller;

import com.baddary.salesAPI.dto.ProductDTO;
import com.baddary.salesAPI.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/search")
    public List<ProductDTO> searchByName(@RequestParam String name) {
        return productService.searchByName(name);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable long id, @RequestBody @Valid ProductDTO dto) {
        ProductDTO productDTO = productService.updateProduct(id, dto);
        return ResponseEntity.status(200).body(productDTO);
    }

    @PostMapping
    public ResponseEntity<ProductDTO> addProduct(@RequestBody @Valid ProductDTO dto) {
        ProductDTO productDTO = productService.addProduct(dto);
        return ResponseEntity.status(201).body(productDTO);
    }

    @GetMapping
    public List<ProductDTO> findAll() {
        return productService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> findById(@PathVariable long id) {
        return productService.findById(id)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-name")
    public ResponseEntity<ProductDTO> findByName(@RequestParam String name) {
        return productService.findByName(name)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

}

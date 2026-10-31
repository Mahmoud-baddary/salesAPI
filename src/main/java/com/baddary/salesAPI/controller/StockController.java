package com.baddary.salesAPI.controller;

import com.baddary.salesAPI.dto.StockDTO;
import com.baddary.salesAPI.exception.ResourceNotFoundException;
import com.baddary.salesAPI.service.StockService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/stocks")
public class StockController {
    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping("/id/{productId}")
    public ResponseEntity<List<StockDTO>> findStocks(@PathVariable long productId) {
        return ResponseEntity.ok(stockService.findStocks(productId));
    }

    @GetMapping("/by-product-expire")
    public ResponseEntity<StockDTO> findStock(@RequestParam long productId, @RequestParam LocalDate expire) {
        StockDTO stockDTO = stockService.findStock(productId, expire)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Stock not found for Product ID: " + productId + " and Expire Date: " + expire));
        return ResponseEntity.ok(stockDTO);
    }

    @GetMapping("/by-product-name")
    public ResponseEntity<List<StockDTO>> findStocksByProductName(@RequestParam String productName) {
        return ResponseEntity.ok(stockService.findStocks(productName));
    }
}

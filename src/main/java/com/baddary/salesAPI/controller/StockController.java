package com.baddary.salesAPI.controller;

import com.baddary.salesAPI.dto.StockDTO;
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

    @GetMapping("/{productId}")
    public List<StockDTO> findStocks(@PathVariable long productId) {
        return stockService.findStocks(productId);
    }

    @GetMapping("/by-product-expire")
    public ResponseEntity<StockDTO> findStock(@RequestParam long productId, @RequestParam LocalDate expire) {
        return stockService.findStock(productId, expire)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/by-product-name")
    public List<StockDTO> findStocks(@RequestParam String productName){
        return stockService.findStocks(productName);
    }
}

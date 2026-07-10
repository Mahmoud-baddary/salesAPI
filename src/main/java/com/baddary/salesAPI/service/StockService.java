package com.baddary.salesAPI.service;

import com.baddary.salesAPI.dto.StockDTO;
import com.baddary.salesAPI.entity.Product;
import com.baddary.salesAPI.entity.Stock;
import com.baddary.salesAPI.mapper.StockMapper;
import com.baddary.salesAPI.repository.OrderRepository;
import com.baddary.salesAPI.repository.ProductRepository;
import com.baddary.salesAPI.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class StockService {
    private final StockRepository stockRepository;
    private final ProductRepository productRepository;

    public StockService(StockRepository stockRepository, OrderRepository orderRepository,
            ProductRepository productRepository) {
        this.stockRepository = stockRepository;
        this.productRepository = productRepository;
    }

    public void increaseStock(Long productId, LocalDate expire, String batch, double quantitySU, BigDecimal priceSU) {
        Product product = productRepository.findById(productId).orElseThrow();
        Stock stock = stockRepository.findByProductIdAndExpire(productId, expire)
                .orElseGet(() -> {
                    Stock s = new Stock();
                    s.setProduct(product);
                    s.setExpire(expire);
                    s.setBatch(batch);
                    s.setQuantitySU(0);
                    s.setPriceSU(priceSU);
                    return s;
                });
        stock.setQuantitySU(stock.getQuantitySU() + quantitySU);
        stockRepository.save(stock);
    }

    @Transactional
    public void decreaseStock(Long productId, LocalDate expire, double quantitySU) {
        Stock stock = stockRepository.findStockForUpdate(productId, expire)
                .orElseThrow(() -> new RuntimeException("Stock not found"));
        // 2. Check if we have enough stock (now guaranteed to be the latest value)
        if (stock.getQuantitySU() < quantitySU) {
            throw new RuntimeException(
                    "Insufficient stock! Available: " + stock.getQuantitySU() + ", requested: " + quantitySU);
        }
        // 3. Decrease the quantity
        double newQuantity = stock.getQuantitySU() - quantitySU;
        stock.setQuantitySU(newQuantity);

        // 4. Save or delete
        if (newQuantity == 0) {
            stockRepository.delete(stock); 
        } else {
            stockRepository.save(stock);
        }
    }

    public List<StockDTO> findStocks(Long productId) {
        return stockRepository.findByProductId(productId).stream().map(StockMapper::toDTO).toList();
    }

    public Optional<StockDTO> findStock(Long productId, LocalDate expire) {
        return stockRepository.findByProductIdAndExpire(productId, expire).map(StockMapper::toDTO);
    }

    public List<StockDTO> findStocks(String productName) {
        return stockRepository.findByProductName(productName.trim()).stream().map(StockMapper::toDTO).toList();
    }

}

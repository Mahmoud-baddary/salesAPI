package com.baddary.salesAPI.dto;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderDTOTest {

    @Test
    void shouldCalculateTotalPriceCorrectly() {
        // 1. ARRANGE (Set up the data)
        OrderDTO order = new OrderDTO();
        
        // Order discount = 10%
        order.setDiscount(new BigDecimal("10.00"));

        // Item 1: 2 items at 50.00 each, with 0% discount
        OrderProductDTO item1 = new OrderProductDTO();
        item1.setQuantity(new BigDecimal("2.0"));
        item1.setPrice(new BigDecimal("50.00"));
        item1.setDiscount(new BigDecimal("0.00"));
        order.getOrderProductDTOSet().add(item1);

        // Item 2: 1 item at 100.00, with 0% discount
        OrderProductDTO item2 = new OrderProductDTO();
        item2.setQuantity(new BigDecimal("1.0"));
        item2.setPrice(new BigDecimal("100.00"));
        item2.setDiscount(new BigDecimal("0.00"));
        order.getOrderProductDTOSet().add(item2);

        // 2. ACT (Call the method we want to test)
        BigDecimal total = order.calculateTotalPrice();

        // 3. ASSERT (Check if the result matches our expectation)
        // Math: (2 * 50) + (1 * 100) = 200.00
        // Apply 10% order discount: 200.00 - 20.00 = 180.00
        assertEquals(new BigDecimal("180.00"), total);
    }
}
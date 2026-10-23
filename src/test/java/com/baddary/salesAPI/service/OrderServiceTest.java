package com.baddary.salesAPI.service;

import com.baddary.salesAPI.dto.OrderDTO;
import com.baddary.salesAPI.dto.OrderProductDTO;
import com.baddary.salesAPI.entity.Order;
import com.baddary.salesAPI.entity.OrderProduct;
import com.baddary.salesAPI.entity.Product;
import com.baddary.salesAPI.enums.OrderType;
import com.baddary.salesAPI.repository.CustomerRepository;
import com.baddary.salesAPI.repository.OrderRepository;
import com.baddary.salesAPI.repository.ProductRepository;
import com.baddary.salesAPI.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private StockService stockService;
    @Mock
    private CashService cashService;
    @Mock
    private CustomerService customerService;

    @InjectMocks
    private OrderService orderService;

    @Test
    void shouldThrowExceptionWhenReturningMoreThanOriginalQuantity() {
        // 1. ARRANGE (Set up the fake database)

        // --- Setup the original order ---
        Order originalOrder = new Order();
        originalOrder.setId(1L);

        Product product = new Product();
        product.setId(100L); // Fake product ID

        OrderProduct originalItem = new OrderProduct();
        originalItem.setProduct(product);
        originalItem.setExpireDate(LocalDate.of(2026, 1, 1)); // Fake expire date
        originalItem.setBatch("BATCH-A"); // Fake batch
        originalItem.setQuantitySU(new BigDecimal("5.0")); // Bought 5 units
        originalOrder.addOrderProduct(originalItem);

        // --- Setup the return order (asking to return 6) ---
        OrderDTO returnDTO = new OrderDTO();
        returnDTO.setOriginalOrderId(1L);
        returnDTO.setOrderType(OrderType.SALE_RETURN);

        OrderProductDTO returnItem = new OrderProductDTO();
        returnItem.setProductId(100L);
        returnItem.setExpireDate(LocalDate.of(2026, 1, 1));
        returnItem.setBatch("BATCH-A");
        returnItem.setQuantitySU(new BigDecimal("6.0")); // Trying to return 6 units
        returnDTO.getOrderProductDTOSet().add(returnItem);

        // --- Tell the fake repository what to do ---
        when(orderRepository.findById(1L)).thenReturn(Optional.of(originalOrder));

        // ✅ THE FIX: 5 parameters, BigDecimal return type
        when(orderRepository.sumReturnedQuantitySU(
                any(), any(), any(), any(), any())).thenReturn(BigDecimal.ZERO); // Assume 0 units have been returned so
                                                                                 // far

        // 2. ACT & 3. ASSERT
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderService.addOrder(returnDTO);
        });

        System.out.println("✅ Test passed! Caught expected exception: " + exception.getMessage());
    }
}
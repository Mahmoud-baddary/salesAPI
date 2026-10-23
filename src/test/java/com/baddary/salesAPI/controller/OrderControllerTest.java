package com.baddary.salesAPI.controller;

import com.baddary.salesAPI.dto.OrderDTO;
import com.baddary.salesAPI.service.OrderService;
import com.baddary.salesAPI.util.JwtUtil; // Import JwtUtil
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import java.math.BigDecimal;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(addFilters = false) // ✅ THIS BYPASSES THE JWT FILTER
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
    @MockitoBean
    private OrderService orderService;

    // ✅ Add this so the JwtAuthenticationFilter can be created
    @MockitoBean
    private JwtUtil jwtUtil;

    @Test
    void shouldReturnCreatedStatusWhenAddingOrder() throws Exception {
        // 1. ARRANGE
        OrderDTO requestDTO = new OrderDTO();
        requestDTO.setCustomerId(1L);
        requestDTO.setUserId(1L);
        requestDTO.setPaidMoney(new BigDecimal("100.00"));
        requestDTO.setDate(java.time.LocalDate.now());
        requestDTO.setTime(java.time.LocalTime.now());
        requestDTO.setOrderType(com.baddary.salesAPI.enums.OrderType.SALE);

        OrderDTO responseDTO = new OrderDTO();
        responseDTO.setId(99L);
        responseDTO.setCustomerId(1L);

        when(orderService.addOrder(any(OrderDTO.class))).thenReturn(responseDTO);

        // 2. ACT & 3. ASSERT
        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(99))
                .andExpect(jsonPath("$.customerId").value(1));
    }
}
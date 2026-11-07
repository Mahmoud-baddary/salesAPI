package com.baddary.salesAPI.controller;

import com.baddary.salesAPI.dto.OrderDTO;
import com.baddary.salesAPI.dto.OrderSearchDTO;
import com.baddary.salesAPI.exception.ResourceNotFoundException;
import com.baddary.salesAPI.service.OrderService;
import com.baddary.salesAPI.service.UserService;
import jakarta.validation.Valid;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final UserService userService; // Add UserService

    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<OrderDTO> addOrder(@RequestBody @Valid OrderDTO orderDTO) {
        orderDTO.setUserId(getCurrentUserId());
        
        OrderDTO dto = orderService.addOrder(orderDTO);
        return ResponseEntity.status(201).body(dto);
    }

    @PostMapping("/search")
    public ResponseEntity<Page<OrderDTO>> searchOrders(@RequestBody @Valid OrderSearchDTO dto) {
        Page<OrderDTO> orderPage = orderService.searchOrders(dto);
        return ResponseEntity.ok(orderPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> findById(@PathVariable long id) {
        OrderDTO order = orderService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order ID : ", id));
        return ResponseEntity.ok(order);
    }

    // Helper method (same as in CashController)
    private Long getCurrentUserId() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.findByNameIgnoreCase(username)
                .orElseThrow(() -> new ResourceNotFoundException("User Name : ", username))
                .getId();
    }
}
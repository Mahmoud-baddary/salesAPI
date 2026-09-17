package com.baddary.salesAPI.controller;

import com.baddary.salesAPI.dto.OrderDTO;
import com.baddary.salesAPI.dto.OrderSearchDTO;

import com.baddary.salesAPI.service.OrderService;
import jakarta.validation.Valid;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderDTO> addOrder(@RequestBody @Valid OrderDTO orderDTO) {
        OrderDTO dto = orderService.addOrder(orderDTO);
        return ResponseEntity.status(201).body(dto);
    }

    @PostMapping("/search")
    public ResponseEntity<List<OrderDTO>> searchOrders(@RequestBody @Valid OrderSearchDTO dto) {

        List<OrderDTO> orders = orderService.searchOrders(dto);
        return ResponseEntity.ok(orders);
    }

    

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> findById(@PathVariable long id) {
        return orderService.findById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

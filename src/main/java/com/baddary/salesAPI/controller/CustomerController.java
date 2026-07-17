package com.baddary.salesAPI.controller;

import com.baddary.salesAPI.dto.CustomerDTO;
import com.baddary.salesAPI.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<CustomerDTO> findAll() {
        return customerService.findAll();
    }

    @GetMapping("/search")
    public List<CustomerDTO> searchByName(@RequestParam String name) {
        return customerService.searchByName(name);
    }

    @GetMapping("/search-by-balance")
    public List<CustomerDTO> searchByNameAndBalanceStatus(
            @RequestParam(required = false) String name,
            @RequestParam String balanceStatus) {
        return customerService.searchByNameAndBalanceStatus(name, balanceStatus);
    }

    @PostMapping
    public ResponseEntity<CustomerDTO> addCustomer(@RequestBody @Valid CustomerDTO dto) {
        CustomerDTO customerDTO = customerService.addCustomer(dto);
        return ResponseEntity.status(201).body(customerDTO);
    }

    @GetMapping("/count")
    public long count() {
        return customerService.count();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable long id, @RequestBody @Valid CustomerDTO dto) {
        CustomerDTO customerDTO = customerService.updateCustomer(id, dto);
        return ResponseEntity.status(200).body(customerDTO);
    }

    @PatchMapping("/{id}/settle")
    public ResponseEntity<CustomerDTO> settleBalance(
            @PathVariable long id,
            @RequestBody Map<String, BigDecimal> payload) {
        BigDecimal amount = payload.get("amount");
        if (amount == null) {
            throw new IllegalArgumentException("amount is required");
        }
        CustomerDTO updated = customerService.settleCustomerBalance(id, amount);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/names")
    public List<String> findAllNames() {
        return customerService.findAllNames();
    }

    @GetMapping("/by-name")
    public ResponseEntity<CustomerDTO> findByName(@RequestParam String name) {
        return customerService.findByName(name)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> findById(@PathVariable long id) {
        return customerService.findById(id)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-phone")
    public ResponseEntity<CustomerDTO> findByPhone(@RequestParam String phoneNum) {
        return customerService.findByPhone(phoneNum)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}

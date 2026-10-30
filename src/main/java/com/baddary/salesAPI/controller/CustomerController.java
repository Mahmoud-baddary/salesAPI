package com.baddary.salesAPI.controller;

import com.baddary.salesAPI.dto.CustomerDTO;
import com.baddary.salesAPI.exception.ResourceNotFoundException;
import com.baddary.salesAPI.service.CustomerService;
import com.baddary.salesAPI.service.UserService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService customerService;
    private final UserService userService; // ADD THIS

    public CustomerController(CustomerService customerService, UserService userService) {
        this.customerService = customerService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> findAll() {
        return ResponseEntity.ok(customerService.findAll());
    }

    @GetMapping("/search")
    public ResponseEntity<List<CustomerDTO>> searchByName(@RequestParam String name) {
        return ResponseEntity.ok(customerService.searchByName(name));
    }

    @GetMapping("/search-by-balance")
    public ResponseEntity<List<CustomerDTO>> searchByNameAndBalanceStatus(
            @RequestParam(required = false) String name,
            @RequestParam String balanceStatus) {
        return ResponseEntity.ok(customerService.searchByNameAndBalanceStatus(name, balanceStatus));
    }

    @PostMapping
    public ResponseEntity<CustomerDTO> addCustomer(@RequestBody @Valid CustomerDTO dto) {
        CustomerDTO customerDTO = customerService.addCustomer(dto);
        return ResponseEntity.status(201).body(customerDTO);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(customerService.count());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable long id, @RequestBody @Valid CustomerDTO dto) {
        CustomerDTO customerDTO = customerService.updateCustomer(id, dto);
        return ResponseEntity.status(200).body(customerDTO);
    }

    @PostMapping("/{customerId}/settle")
    public ResponseEntity<CustomerDTO> settleBalance(
            @PathVariable long customerId,
            @RequestBody BigDecimal amount) {

        CustomerDTO updated = customerService.settleCustomerBalance(customerId, getLoggedInUserId(),
                amount);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/names")
    public ResponseEntity<List<String>> findAllNames() {
        return ResponseEntity.ok(customerService.findAllNames());
    }

    @GetMapping("/by-name")
    public ResponseEntity<CustomerDTO> findByName(@RequestParam String name) {
        return customerService.findByName(name)
                .map(ResponseEntity::ok).orElseThrow(() -> new ResourceNotFoundException("Customer with name", name));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> findById(@PathVariable long id) {
        return customerService.findById(id)
                .map(ResponseEntity::ok).orElseThrow(() -> new ResourceNotFoundException("Customer with ID", id));
    }

    @GetMapping("/by-phone")
    public ResponseEntity<CustomerDTO> findByPhone(@RequestParam String phoneNum) {
        return customerService.findByPhone(phoneNum)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Customer with phone number", phoneNum));
    }

    // Add this at the bottom of your CustomerController class
    private Long getLoggedInUserId() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.findByNameIgnoreCase(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", username))
                .getId();
    }
}

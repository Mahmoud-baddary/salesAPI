package com.baddary.salesAPI.controller;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baddary.salesAPI.dto.CashDTO;
import com.baddary.salesAPI.exception.ResourceNotFoundException;
import com.baddary.salesAPI.service.CashService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/cash")
public class CashController {
    private final CashService cashService;

    public CashController(CashService cashService) {
        this.cashService = cashService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/deposit")
    public ResponseEntity<?> depositCashToSafe(@RequestBody @Valid CashDTO cashDTO) {
        cashService.depositCashToSafe(cashDTO.getUserId(), cashDTO.getAmount());
        return ResponseEntity.ok(Map.of("message", "Cash deposited to safe successfully"));
    }

    @PostMapping("/receive")
    public ResponseEntity<?> receiveCashFromSafe(@RequestBody @Valid CashDTO cashDTO) {
        cashService.receiveCashFromSafe(cashDTO.getUserId(), cashDTO.getAmount());
        return ResponseEntity.ok(Map.of("message", "Cash received successfully"));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<BigDecimal> currentAmount(@PathVariable Long userId) {
        BigDecimal amount = cashService.currentAmount(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cash register for user", userId));
        return ResponseEntity.ok(amount);
    }

}

package com.baddary.salesAPI.controller;

import java.math.BigDecimal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baddary.salesAPI.service.CashService;

@RestController
@RequestMapping("/cash")
public class CashController {
    private final CashService cashService;

    public CashController(CashService cashService) {
        this.cashService = cashService;
    }

    @PostMapping("/deposit/{userId}")
    public ResponseEntity<String> depositAllCash(@PathVariable Long userId) {
        cashService.depositAllCashToSafe(userId);
        return ResponseEntity.ok("All cash deposited to safe successfully");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<BigDecimal> currentAmount(@PathVariable Long userId) {
        return cashService.currentAmount(userId).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}

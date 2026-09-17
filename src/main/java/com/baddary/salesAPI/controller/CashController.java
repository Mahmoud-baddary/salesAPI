package com.baddary.salesAPI.controller;

import java.math.BigDecimal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baddary.salesAPI.dto.CashDTO;
import com.baddary.salesAPI.service.CashService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/cash")
public class CashController {
    private final CashService cashService;

    public CashController(CashService cashService) {
        this.cashService = cashService;
    }

    @PostMapping("/deposit")
    public ResponseEntity<String> depositCashToSafe(@RequestBody @Valid CashDTO cashDTO) {
        cashService.depositCashToSafe(cashDTO.getUserId(), cashDTO.getAmount());
        return ResponseEntity.ok("Cash deposited to safe successfully");
    }

    @PostMapping("/receive")
    public ResponseEntity<String> receiveCashFromSafe(@RequestBody @Valid CashDTO cashDTO) {
        cashService.receiveCashFromSafe(cashDTO.getUserId(), cashDTO.getAmount());
        return ResponseEntity.ok("Cash received successfully");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<BigDecimal> currentAmount(@PathVariable Long userId) {
        return cashService.currentAmount(userId).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}

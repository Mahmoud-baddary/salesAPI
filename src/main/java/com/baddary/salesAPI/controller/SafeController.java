package com.baddary.salesAPI.controller;

import java.math.BigDecimal;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baddary.salesAPI.service.SafeService;

@RestController
@RequestMapping("/safe")
public class SafeController {
    private final SafeService safeService;

    public SafeController(SafeService safeService) {
        this.safeService = safeService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/total")
    public ResponseEntity<BigDecimal> totalAmount() {
        return safeService.totalAmount().map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

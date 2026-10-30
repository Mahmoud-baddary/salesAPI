package com.baddary.salesAPI.controller;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.baddary.salesAPI.exception.ResourceNotFoundException;
import com.baddary.salesAPI.service.CashService;
import com.baddary.salesAPI.service.UserService; // Need this to find the user


@RestController
@RequestMapping("/cash")
public class CashController {
    private final CashService cashService;
    private final UserService userService; // Add this

    public CashController(CashService cashService, UserService userService) {
        this.cashService = cashService;
        this.userService = userService;
    }

    @PostMapping("/deposit")
    public ResponseEntity<Map<String, String>> depositCashToSafe(@RequestBody BigDecimal amount) {
        // ✅ Get the real user from the token
        Long realUserId = getCurrentUserId();

        cashService.depositCashToSafe(realUserId, amount);
        
        return ResponseEntity.ok(Map.of("message", "Cash deposited to safe successfully"));
    }

    @PostMapping("/receive")
    public ResponseEntity<Map<String, String>> receiveCashFromSafe(@RequestBody BigDecimal amount) {
        // ✅ Get the real user from the token
        Long realUserId = getCurrentUserId();

        cashService.receiveCashFromSafe(realUserId, amount);
        
        return ResponseEntity.ok(Map.of("message", "Cash received successfully"));
    }
    @PreAuthorize("hasRole('ADMIN')") // Only admins can access this endpoint
    @GetMapping("/{userId}")
    public ResponseEntity<BigDecimal> currentAmount(@PathVariable Long userId) {
        BigDecimal amount = cashService.currentAmount(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cash Register for user", userId));
        return ResponseEntity.ok(amount);
    }
    @GetMapping("/my")
    public ResponseEntity<BigDecimal> myCurrentAmount() {
        Long userId = getCurrentUserId();
        BigDecimal amount = cashService.currentAmount(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cash Register for user", userId));
        return ResponseEntity.ok(amount);
    }
    // Helper method to avoid repeating the same 3 lines of code
    private Long getCurrentUserId() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.findByNameIgnoreCase(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", username))
                .getId();
    }
}
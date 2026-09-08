package com.baddary.salesAPI.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CashDTO {
    @NotNull(message = "User id is required")
    private Long userId;
    @Positive(message = "Amount must be positve")
    private BigDecimal amount;
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    
}

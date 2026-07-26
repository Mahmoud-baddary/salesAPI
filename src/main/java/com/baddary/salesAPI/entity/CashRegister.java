package com.baddary.salesAPI.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "cash_register")
public class CashRegister {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @Column(precision = 10, scale = 2)
    private BigDecimal currentAmount = BigDecimal.ZERO;

    @Version
    private Long version;

    
    
    public CashRegister() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(BigDecimal currentAmount) {
        this.currentAmount = currentAmount;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    
}
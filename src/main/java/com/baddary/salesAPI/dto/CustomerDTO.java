package com.baddary.salesAPI.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class CustomerDTO {
    private Long id;
    @NotBlank(message = "Customer name is required")
    private String name;
    private String email, address;
    @Valid
    private final Set<PhoneDTO> phoneDTOSet = new HashSet<>();
    private BigDecimal balance = BigDecimal.ZERO;

    public Long getId() {
        return id;
    }

    public void setPhoneDTOSet(Set<PhoneDTO> phoneDTOSet) {
        this.phoneDTOSet.clear();
        if (phoneDTOSet != null) {
            this.phoneDTOSet.addAll(phoneDTOSet);
        }
    }

    

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<PhoneDTO> getPhoneDTOSet() {
        return phoneDTOSet;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal amountOwed) {
        this.balance = amountOwed != null ? amountOwed : BigDecimal.ZERO;
    }

   
}

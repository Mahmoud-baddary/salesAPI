package com.baddary.salesAPI.dto;

import jakarta.validation.constraints.NotBlank;

public class PhoneDTO {
    private Long id;
    @NotBlank(message = "phone number is required")
    private String phoneNum;
    private Long customerId;

    public PhoneDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}

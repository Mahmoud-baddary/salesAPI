package com.baddary.salesAPI.dto;

import jakarta.validation.constraints.NotBlank;

public class BarcodeDTO {

    @NotBlank(message = "Barcode text is required")
    private String barcodeTxt;
    private Long productId;

    public BarcodeDTO() {
    }

    public String getBarcodeTxt() {
        return barcodeTxt;
    }

    public void setBarcodeTxt(String barcodeTxt) {
        this.barcodeTxt = barcodeTxt;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}

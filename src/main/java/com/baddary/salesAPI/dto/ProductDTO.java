package com.baddary.salesAPI.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.util.HashSet;
import java.util.Set;

public class ProductDTO {
    private Long id;
    @NotBlank(message = "product name is required")
    private String name;
    @NotBlank(message = "greatest unit is required")
    private String greatestUnit;
    @NotBlank(message = "medium unit is required")
    private String mediumUnit;
    @NotBlank(message = "smallest unit is required")
    private String smallestUnit;
    @Positive(message = "medium unit amount is required")
    private int mediumUnitAmount;
    @Positive(message = "smallest unit amount is required")
    private int smallestUnitAmount;

    @Valid
    private final Set<BarcodeDTO> barcodeDTOSet = new HashSet<>();

    public ProductDTO() {
    }

    public void setBarcodeDTOSet(Set<BarcodeDTO> barcodeDTOSet){
        this.barcodeDTOSet.clear();
        if(barcodeDTOSet != null){
            this.barcodeDTOSet.addAll(barcodeDTOSet);
        }
    }

    public Long getId() {
        return id;
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

    public String getGreatestUnit() {
        return greatestUnit;
    }

    public void setGreatestUnit(String greatestUnit) {
        this.greatestUnit = greatestUnit;
    }

    public String getMediumUnit() {
        return mediumUnit;
    }

    public void setMediumUnit(String mediumUnit) {
        this.mediumUnit = mediumUnit;
    }

    public String getSmallestUnit() {
        return smallestUnit;
    }

    public void setSmallestUnit(String smallestUnit) {
        this.smallestUnit = smallestUnit;
    }

    public int getMediumUnitAmount() {
        return mediumUnitAmount;
    }

    public void setMediumUnitAmount(int mediumUnitAmount) {
        this.mediumUnitAmount = mediumUnitAmount;
    }

    public int getSmallestUnitAmount() {
        return smallestUnitAmount;
    }

    public void setSmallestUnitAmount(int smallestUnitAmount) {
        this.smallestUnitAmount = smallestUnitAmount;
    }


    public Set<BarcodeDTO> getBarcodeDTOSet() {
        return barcodeDTOSet;
    }

}

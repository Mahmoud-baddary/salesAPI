package com.baddary.salesAPI.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "products", indexes = @Index(name = "idx_productName", columnList = "name"))
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    private String greatestUnit;
    @Column(nullable = false)
    private String mediumUnit;
    @Column(nullable = false)
    private String smallestUnit;
    @Column(nullable = false)
    private int mediumUnitAmount;
    @Column(nullable = false)
    private int SmallestUnitAmount;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderProduct> orderProductSet = new HashSet<>();
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Barcode> barcodes = new HashSet<>();

    public Product() {
    }




    public void setId(Long id) {
        this.id = id;
    }

    public int getMediumUnitAmount() {
        return mediumUnitAmount;
    }

    public void setMediumUnitAmount(int mediumUnitAmount) {
        this.mediumUnitAmount = mediumUnitAmount;
    }

    public int getSmallestUnitAmount() {
        return SmallestUnitAmount;
    }

    public void setSmallestUnitAmount(int smallestUnitAmount) {
        this.SmallestUnitAmount = smallestUnitAmount;
    }

    public Set<Barcode> getBarcodes() {
        return barcodes;
    }

    public Set<OrderProduct> getOrderProductSet() {
        return orderProductSet;
    }

    public Long getId() {
        return id;
    }

    public void addBarcode(Barcode barcode) {
        barcodes.add(barcode);
        barcode.setProduct(this);
    }

    public void removeBarcode(Barcode barcode){
        barcodes.remove(barcode);
        barcode.setProduct(null);
    }

   



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.toUpperCase().strip();
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

    @Override
    public String toString() {
        return this.name;
    }
}

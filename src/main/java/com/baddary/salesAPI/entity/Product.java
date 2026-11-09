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
    private int smallestUnitAmount;
    @OneToMany(mappedBy = "product", orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<OrderProduct> orderProductSet = new HashSet<>();
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Barcode> barcodes = new HashSet<>();
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Stock> stocks = new HashSet<>();

    public Product() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getMediumUnitAmount() {
        return mediumUnitAmount;
    }

    public void setMediumUnitAmount(int mediumUnitAmount) {
        if (mediumUnitAmount <= 0) {
            throw new IllegalArgumentException("mediumUnitAmount must be greater than zero");
        }
        this.mediumUnitAmount = mediumUnitAmount;
    }

    public int getSmallestUnitAmount() {
        return smallestUnitAmount;
    }

    public void setSmallestUnitAmount(int smallestUnitAmount) {
        if (smallestUnitAmount <= 0) {
            throw new IllegalArgumentException("smallestUnitAmount must be greater than zero");
        }                       
        this.smallestUnitAmount = smallestUnitAmount;
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

    public void removeBarcode(Barcode barcode) {
        barcodes.remove(barcode);
        barcode.setProduct(null);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Product name cannot be null or blank");
        }
        this.name = name.toUpperCase().strip();
    }

    public String getGreatestUnit() {
        return greatestUnit;
    }

    public void setGreatestUnit(String greatestUnit) {
        if (greatestUnit == null || greatestUnit.isBlank()) {
            throw new IllegalArgumentException("Greatest unit cannot be null or blank");
        }
        this.greatestUnit = greatestUnit.strip();
    }

    public String getMediumUnit() {
        return mediumUnit;
    }

    public void setMediumUnit(String mediumUnit) {
        if (mediumUnit == null || mediumUnit.isBlank()) {
            throw new IllegalArgumentException("Medium unit cannot be null or blank");
        }
        this.mediumUnit = mediumUnit.strip();
    }

    public String getSmallestUnit() {
        return smallestUnit;
    }

    public void setSmallestUnit(String smallestUnit) {
        if (smallestUnit == null || smallestUnit.isBlank()) {
            throw new IllegalArgumentException("Smallest unit cannot be null or blank");
        }
        this.smallestUnit = smallestUnit.strip();
    }

    public Set<Stock> getStocks() {
        return stocks;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Product product = (Product) o;
        return id != null && id.equals(product.id);
    }
}

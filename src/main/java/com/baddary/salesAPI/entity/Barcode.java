package com.baddary.salesAPI.entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "barcodes", indexes = @Index(name = "idx_barcode", columnList = "barcode_txt"))
public class Barcode {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String barcodeTxt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;


    public Barcode(){

    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBarcodeTxt() {
        return barcodeTxt;
    }

    public void setBarcodeTxt(String barcodeTxt) {
        this.barcodeTxt = barcodeTxt;
    }


    @Override
    public String toString() {
        return "Barcode{" +
                "id=" + id +
                ", barcodeTxt='" + barcodeTxt + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Barcode barcode1)) return false;
        return Objects.equals(barcodeTxt, barcode1.barcodeTxt);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(barcodeTxt);
    }
}

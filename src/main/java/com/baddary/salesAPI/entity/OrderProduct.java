package com.baddary.salesAPI.entity;


import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "orders_products")
public class OrderProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    private String batch;
    private String unit;
    @Column(nullable = false)
    private double quantity;
    @Column(nullable = false)
    private double quantitySU;
    @Column(nullable = false)
    private double price;
    private double discount;
    private String note;
    private LocalDate expireDate;
    private double priceSU;

    public OrderProduct(){

    }

    public double getPriceSU() {
        return priceSU;
    }

    public void setPriceSU() {
        this.priceSU = calculatePriceSU(this.price);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public double getQuantitySU() {
        return quantitySU;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantitySU() {
        this.quantitySU = calculateQuantitySU(this.quantity);
    }

    public void setQuantity(double quantity)
    {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalDate getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDate expireDate) {
        this.expireDate = expireDate;
    }

    private double calculateQuantitySU(double quantity){
        String unit = this.getUnit();
        if(unit.equalsIgnoreCase(this.product.getGreatestUnit())){
            return quantity * this.product.getSmallestUnitAmount();
        } else if (unit.equalsIgnoreCase(this.product.getMediumUnit())) {
            return quantity * ((double) this.product.getSmallestUnitAmount() / this.product.getMediumUnitAmount());
        }else{
            return quantity;
        }
    }
    private double calculatePriceSU(double price){
        double result = 0;
        if (this.unit.equalsIgnoreCase(this.product.getGreatestUnit())){
            result = price / this.product.getSmallestUnitAmount();
        }else if (this.unit.equalsIgnoreCase(this.product.getMediumUnit())){
            result = price / ((double) this.product.getSmallestUnitAmount() /this.product.getMediumUnitAmount());
        }else{
            result = price;
        }
        return result;
    }

    @Override
    public String toString() {
        return "OrderProduct{" +
                "id=" + id +
                ", product=" + product +
                ", order=" + order +
                ", batch='" + batch + '\'' +
                ", unit='" + unit + '\'' +
                ", quantity=" + quantity +
                ", quantitySU=" + quantitySU +
                ", price=" + price +
                ", discount=" + discount +
                ", note='" + note + '\'' +
                ", expireDate=" + expireDate +
                ", priceSU=" + priceSU +
                '}';
    }
}

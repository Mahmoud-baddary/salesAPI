package com.baddary.salesAPI.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
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
    @Column(nullable = false)
    private String batch;
    @Column(nullable = false)
    private String unit;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal quantity;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal quantitySU;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(precision = 10, scale = 2)
    private BigDecimal discount = BigDecimal.ZERO;

    private String note;
    @Column(nullable = false)
    private LocalDate expireDate;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal priceSU;

    public OrderProduct() {

    }

    public BigDecimal getPriceSU() {
        return priceSU;
    }

    

    public void setPriceSU(BigDecimal priceSU) {
        this.priceSU = priceSU;
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

    public BigDecimal getQuantitySU() {
        return quantitySU;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantitySU(BigDecimal quantitySU) {
        this.quantitySU = quantitySU;
        
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price ;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount != null ? discount : BigDecimal.ZERO;
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

    // private BigDecimal calculateQuantitySU(BigDecimal quantity) {
    //     String unit = this.getUnit();
    //     if (unit.equalsIgnoreCase(this.product.getGreatestUnit())) {
    //         return quantity.multiply( BigDecimal.valueOf(this.product.getSmallestUnitAmount()));
    //     } else if (unit.equalsIgnoreCase(this.product.getMediumUnit())) {
    //         return quantity.multiply(BigDecimal.valueOf((double) this.product.getSmallestUnitAmount() / this.product.getMediumUnitAmount()));
    //     } else {
    //         return quantity;
    //     }
    // }

    // private BigDecimal calculatePriceSU(BigDecimal price) {
    //     if (price == null || unit == null || product == null)
    //         return null;
    //     BigDecimal result;
    //     if (this.unit.equalsIgnoreCase(this.product.getGreatestUnit())) {
    //         result = price.divide(BigDecimal.valueOf(this.product.getSmallestUnitAmount()), 2, RoundingMode.HALF_UP);
    //     } else if (this.unit.equalsIgnoreCase(this.product.getMediumUnit())) {
    //         double divisor = (double) this.product.getSmallestUnitAmount() / this.product.getMediumUnitAmount();
    //         result = price.divide(BigDecimal.valueOf(divisor), 2, RoundingMode.HALF_UP);
    //     } else {
    //         result = price;
    //     }
    //     return result;
    // }

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

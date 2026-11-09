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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
    @ManyToOne(fetch = FetchType.LAZY)
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
        if (priceSU == null) {
            throw new IllegalArgumentException("priceSU cannot be null");
        }
        if (priceSU.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("priceSU cannot be negative or zero");
        }
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
        if (quantitySU == null) {
            throw new IllegalArgumentException("quantitySU cannot be null");
        }
        if (quantitySU.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("quantitySU cannot be negative or zero");
        }
        this.quantitySU = quantitySU;

    }

    public void setQuantity(BigDecimal quantity) {
        if (quantity == null) {
            throw new IllegalArgumentException("quantity cannot be null");
        }
        if (quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("quantity cannot be negative or zero");
        }
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        if (price == null) {
            throw new IllegalArgumentException("price cannot be null");
        }
        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("price cannot be negative or zero");
        }
        this.price = price;
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

    @Override
    public boolean equals(Object o) {
        // 1. If it's the exact same memory address, it's equal
        if (this == o)
            return true;

        // 2. If the other object is null, or not the same class, it's not equal
        if (o == null || getClass() != o.getClass())
            return false;

        // 3. Cast the other object
        OrderProduct orderProduct = (OrderProduct) o;

        // 4. If this ID is null, it's a new object. It can't be equal to anything else.
        // 5. If both have IDs, compare them.
        return id != null && id.equals(orderProduct.id);
    }

    @Override
    public int hashCode() {
        // Always return the same number for this class
        return getClass().hashCode();
    }
}

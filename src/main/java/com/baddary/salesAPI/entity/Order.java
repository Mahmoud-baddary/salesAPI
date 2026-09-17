package com.baddary.salesAPI.entity;

import com.baddary.salesAPI.enums.OrderType;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime time;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderType orderType;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal paidMoney;  

    @Column(precision = 10, scale = 2)
    private BigDecimal discount = BigDecimal.ZERO;    

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "original_order_id")
    private Order originalOrder;   // null for normal orders, set for returns

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<OrderProduct> orderProductSet = new HashSet<>();

    

    public Order() { }


    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public LocalTime getTime() { return time; }
    public void setTime(LocalTime time) { this.time = time; }

    public OrderType getOrderType() { return orderType; }
    public void setOrderType(OrderType orderType) { this.orderType = orderType; }

    public BigDecimal getPaidMoney() { return paidMoney; }
    public void setPaidMoney(BigDecimal paidMoney) { this.paidMoney = paidMoney; }

    public BigDecimal getDiscount() { return discount; }
    public void setDiscount(BigDecimal discount) { 
        this.discount = discount != null ? discount : BigDecimal.ZERO; 
    }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    

    public void addOrderProduct(OrderProduct orderProduct) {
        this.orderProductSet.add(orderProduct);
        orderProduct.setOrder(this);
    }

    public void removeOrderProduct(OrderProduct orderProduct) {
        this.orderProductSet.remove(orderProduct);
        orderProduct.setOrder(null);
    }

    public Set<OrderProduct> getOrderProductSet() {
        return orderProductSet;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", date=" + date +
                ", time=" + time +
                ", orderType=" + orderType +
                ", paidMoney=" + paidMoney +
                ", discount=" + discount +
                ", customer=" + customer +
                ", user=" + user +
                ", orderProductSet=" + orderProductSet +
                '}';
    }

    public Order getOriginalOrder() {
        return originalOrder;
    }


    public void setOriginalOrder(Order originalOrder) {
        this.originalOrder = originalOrder;
    }
}
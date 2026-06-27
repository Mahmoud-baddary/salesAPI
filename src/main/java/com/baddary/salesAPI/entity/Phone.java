package com.baddary.salesAPI.entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "phones", indexes = @Index(name = "idx_phoneNum", columnList = "phoneNum"))
public class Phone {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    @Column(nullable = false, unique = true)
    private String phoneNum;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public Phone(String phoneNum) {
        this.phoneNum = phoneNum.strip();
    }
    public Phone(){

    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Customer getCustomer() {
        return customer;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Phone{" +
                "id=" + id +
                ", phoneNum='" + phoneNum + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Phone phone = (Phone) o;
        return Objects.equals(phoneNum, phone.phoneNum);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(phoneNum);
    }
}

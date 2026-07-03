package com.baddary.salesAPI.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String email;
    private String address;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("phoneNum ASC")
    private Set<Phone> phones = new HashSet<>();

    // New monetary fields
    @Column(precision = 10, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    
    public Customer() { }

    // Getters and Setters (including the new ones)

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name.toUpperCase().strip(); }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Set<Phone> getPhones() { return phones; }
    public void setPhones(Set<Phone> phones) { this.phones = phones; } // optional setter

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal amountOwed) {
        this.balance = amountOwed != null ? amountOwed : BigDecimal.ZERO;
    }


    public void addPhone(Phone phone) {
        this.phones.add(phone);
        phone.setCustomer(this);
    }

    public void removePhone(Phone phone) {
        this.phones.remove(phone);
        phone.setCustomer(null);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", phones=" + phones +
                ", amountOwed=" + balance +
                '}';
    }
}
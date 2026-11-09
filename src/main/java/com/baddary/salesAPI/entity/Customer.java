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

    @Version
    private long version;

    @Column(nullable = false, unique = true)
    private String name;

    private String email;
    private String address;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
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
    public void setName(String name) { this.name = name != null ? name.toUpperCase().strip() : null; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Set<Phone> getPhones() { return phones; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) {
        this.balance = balance != null ? balance : BigDecimal.ZERO;
    }


    public void addPhone(Phone phone) {
        this.phones.add(phone);
        phone.setCustomer(this);
    }

    public void removePhone(Phone phone) {
        this.phones.remove(phone);
        phone.setCustomer(null);
    }

    

    public Long getVersion() {
        return version;
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
        Customer customer = (Customer) o;

        // 4. If this ID is null, it's a new object. It can't be equal to anything else.
        // 5. If both have IDs, compare them.
        return id != null && id.equals(customer.id);
    }

    @Override
    public int hashCode() {
        // Always return the same number for this class
        return getClass().hashCode();
    }

}
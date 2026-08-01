package com.baddary.salesAPI.config;

import com.baddary.salesAPI.entity.Customer;
import com.baddary.salesAPI.entity.Phone;
import com.baddary.salesAPI.entity.Safe;
import com.baddary.salesAPI.repository.CustomerRepository;
import com.baddary.salesAPI.repository.SafeRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataInitializer {

    private final SafeRepository safeRepository;
    private final CustomerRepository customerRepository;

    public DataInitializer(SafeRepository safeRepository, CustomerRepository customerRepository) {
        this.safeRepository = safeRepository;
        this.customerRepository = customerRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initializeSafe() {
        // Check if Safe table is empty
        if (safeRepository.count() == 0) {
            Safe safe = new Safe();
            safe.setTotalAmount(BigDecimal.ZERO);
            safeRepository.save(safe);
            System.out.println("Safe row created with balance 0.00");
        } else {
            System.out.println("ℹSafe already exists.");
        }
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initializeGuest() {
        // Check if Customer table is empty
        if (customerRepository.count() == 0) {
            Customer customer = new Customer();
            customer.setName("guest");
            customer.addPhone(new Phone("0000"));
            customerRepository.save(customer);
            System.out.println("Guest was created now");
        } else {
            System.out.println("guest already exists.");
        }
    }
}
package com.baddary.salesAPI.service;

import com.baddary.salesAPI.dto.CustomerDTO;
import com.baddary.salesAPI.entity.Customer;
import com.baddary.salesAPI.mapper.CustomerMapper;
import com.baddary.salesAPI.repository.CustomerRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final EntityManager entityManager;


    public CustomerService(CustomerRepository customerRepository, EntityManager entityManager) {
        this.customerRepository = customerRepository;
        this.entityManager = entityManager;
    }

    public List<CustomerDTO> findAll() {
        List<Customer> customers = customerRepository.findAll();
        List<CustomerDTO> dtos = customers.stream().map(CustomerMapper::toDTO).toList();
        return dtos;
    }

    public List<CustomerDTO> searchByName(String name) {
        List<Customer> customers = customerRepository.searchByName(name);
        List<CustomerDTO> dtos = customers.stream().map(CustomerMapper::toDTO).toList();
        return dtos;
    }

    public CustomerDTO addCustomer(CustomerDTO dto) {
        Customer entity = CustomerMapper.toEntity(dto);
        Customer saved = customerRepository.save(entity);
        return CustomerMapper.toDTO(saved);
    }

    public Long count() {
        return customerRepository.count();
    }

    @Transactional
    public CustomerDTO updateCustomer(Long id, CustomerDTO dto) {
        Optional<Customer> toUpdateOptional = customerRepository.findById(id);
        Customer toUpdate = toUpdateOptional.orElseThrow(
                ()->new RuntimeException("Customer is not found")
        );
        toUpdate.getPhones().clear();
        entityManager.flush();
        CustomerMapper.updateEntity(toUpdate, dto);
        Customer saved = customerRepository.save(toUpdate);
        return CustomerMapper.toDTO(saved);

    }

    public CustomerDTO updateCustomerBalance(Long id, BigDecimal balance){
        Optional<Customer> toUpdateOptional = customerRepository.findById(id);
        Customer toUpdate = toUpdateOptional.orElseThrow(
                ()->new RuntimeException("Customer is not found")
        );
        toUpdate.setBalance(balance);
        Customer saved = customerRepository.save(toUpdate);
        return CustomerMapper.toDTO(saved);
    }

    public List<String> findAllNames() {
        return customerRepository.findNameBy();
    }

    public Optional<CustomerDTO> findByName(String name) {
        Optional<Customer> customer = customerRepository.findByName(name);
        return customer.map(CustomerMapper::toDTO);
    }

    public Optional<CustomerDTO> findById(Long customerId) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        return optionalCustomer.map(CustomerMapper::toDTO);
    }

    public Optional<CustomerDTO> findByPhone(String phoneNum) {
        Optional<Customer> optionalCustomer = customerRepository.findByPhone(phoneNum);
        return optionalCustomer.map(CustomerMapper::toDTO);
    }

    public List<CustomerDTO> searchByNameAndBalanceStatus(String name, String balanceStatus){
        List<Customer> customers = customerRepository.searchByNameAndBalanceStatus(name, balanceStatus);
        return customers.stream().map(CustomerMapper::toDTO).toList();
    }
}

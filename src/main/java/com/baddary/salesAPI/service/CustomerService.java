package com.baddary.salesAPI.service;

import com.baddary.salesAPI.dto.CustomerDTO;
import com.baddary.salesAPI.dto.OrderDTO;
import com.baddary.salesAPI.entity.Customer;
import com.baddary.salesAPI.enums.OrderType;
import com.baddary.salesAPI.exception.ResourceNotFoundException;
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
    private final CashService cashService;


    public CustomerService(CustomerRepository customerRepository, EntityManager entityManager, CashService cashService) {
        this.customerRepository = customerRepository;
        this.entityManager = entityManager;
        this.cashService = cashService;
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
                ()->new ResourceNotFoundException("Customer is not found")
        );
        toUpdate.getPhones().clear();
        entityManager.flush();
        CustomerMapper.updateEntity(toUpdate, dto);
        Customer saved = customerRepository.save(toUpdate);
        return CustomerMapper.toDTO(saved);

    }
    public void updateCustomerBalance(Customer customer, OrderDTO orderDTO, BigDecimal totalPrice) {
        BigDecimal netChange = totalPrice.subtract(orderDTO.getPaidMoney());
        // if paid money is greater that totalprice then throw error
        if (netChange.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("paid money must not be greater than order price");
        }
        if (netChange.compareTo(BigDecimal.ZERO) != 0) {
            if (orderDTO.getOrderType() == OrderType.SALE || orderDTO.getOrderType() == OrderType.BUY_RETURN) {
                customer.setBalance(customer.getBalance().add(netChange));
            } else {
                customer.setBalance(customer.getBalance().subtract(netChange));
            }
            customerRepository.save(customer);
        }
    }
    @Transactional
    public CustomerDTO settleCustomerBalance(Long customerId, Long userId, BigDecimal amount){
        Optional<Customer> toUpdateOptional = customerRepository.findById(customerId);
        Customer toUpdate = toUpdateOptional.orElseThrow(
                ()->new ResourceNotFoundException("Customer is not found")
        );
        BigDecimal balance = toUpdate.getBalance();
        if(amount.compareTo(balance.abs()) > 0){
            throw new RuntimeException("amount must not be greater than balance");
        }
        if (balance.compareTo(BigDecimal.ZERO) < 0) { // DESERVE
            balance = balance.add(amount);
            cashService.decreaseUserCash(userId, amount);
            
        }else if (balance.compareTo(BigDecimal.ZERO) > 0) { // OWE
            balance = balance.subtract(amount);
            cashService.increaseUserCash(userId, amount);
        }
        toUpdate.setBalance(balance);
        Customer saved = customerRepository.save(toUpdate);
        return CustomerMapper.toDTO(saved);
    }

    // public CustomerDTO settleCustomerBalance(Long customerId, Long userId, BigDecimal amount){
    //     int retries = 3;
    //     while (retries > 0) {
    //         try {
    //             return doSettleCustomerBalance(customerId, userId, amount);
    //         } catch (OptimisticLockException | OptimisticLockingFailureException e) {
    //             retries--;
    //             if (retries == 0) {
    //                 throw new RuntimeException("Customer balance was updated by another transaction. Please try again.",
    //                         e);
    //             }
    //         }
    //     }
    //     throw new RuntimeException("Unexpected error");
    // }

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

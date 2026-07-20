package com.baddary.salesAPI.service;

import com.baddary.salesAPI.dto.OrderDTO;
import com.baddary.salesAPI.dto.OrderProductDTO;
import com.baddary.salesAPI.entity.*;
import com.baddary.salesAPI.enums.OrderType;
import com.baddary.salesAPI.mapper.OrderMapper;
import com.baddary.salesAPI.mapper.OrderProductMapper;
import com.baddary.salesAPI.repository.CustomerRepository;
import com.baddary.salesAPI.repository.OrderRepository;
import com.baddary.salesAPI.repository.ProductRepository;
import com.baddary.salesAPI.repository.UserRepository;
import com.baddary.salesAPI.specification.OrderSpecifications;

import jakarta.persistence.OptimisticLockException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final StockService stockService;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository, StockService stockService,
            CustomerRepository customerRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.stockService = stockService;
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public OrderDTO addOrder(OrderDTO orderDTO) {
        int retries = 3;
        while (retries > 0) {
            try {
                return doAddOrder(orderDTO);
            } catch (OptimisticLockException | OptimisticLockingFailureException e) {
                retries--;
                if (retries == 0) {
                    throw new RuntimeException("Customer balance was updated by another transaction. Please try again.",
                            e);
                }
                // Retry with fresh data (the loop will re-fetch)
            }
        }
        throw new RuntimeException("Unexpected error");
    }

    @Transactional
    private OrderDTO doAddOrder(OrderDTO orderDTO) {
        Customer customer = customerRepository.findById(orderDTO.getCustomerId()).orElseThrow();
        User user = userRepository.findById(orderDTO.getUserId()).orElseThrow();
        Order orderEntity = OrderMapper.toEntity(orderDTO, user, customer);
        for (OrderProductDTO orderProductDTO : orderDTO.getOrderProductDTOSet()) {
            Product product = productRepository.findById(orderProductDTO.getProductId()).orElseThrow();
            OrderProduct opEntity = OrderProductMapper.toEntity(orderProductDTO, product, orderEntity);
            orderEntity.addOrderProduct(opEntity);
        }
        Order saved = orderRepository.save(orderEntity);
        if (orderEntity.getOrderType() == OrderType.BUY) {
            for (OrderProduct op : orderEntity.getOrderProductSet()) {
                stockService.increaseStock(op.getProduct().getId(),
                        op.getExpireDate(), op.getBatch(), op.getQuantitySU(), op.getPriceSU());
            }
        } else {
            for (OrderProduct op : orderEntity.getOrderProductSet()) {
                stockService.decreaseStock(op.getProduct().getId(),
                        op.getExpireDate(), op.getQuantitySU());
            }
        }
        // Update customer balance (this is where @Version is checked)
        BigDecimal totalPrice = orderDTO.calculateTotalPrice();
        updateCustomerBalance(customer, orderDTO, totalPrice);
        return OrderMapper.toDTO(saved);

    }

    private void updateCustomerBalance(Customer customer, OrderDTO orderDTO, BigDecimal totalPrice) {
        BigDecimal netChange = totalPrice.subtract(orderDTO.getPaidMoney());
        System.out.println("final price : " + totalPrice);
        System.out.println("net change : " + netChange);
        // if paid money is greater that totalprice then throw error
        if (netChange.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("paid money must not be greater than order price");
        }
        if (netChange.compareTo(BigDecimal.ZERO) != 0) {
            if (orderDTO.getOrderType() == OrderType.SALE) {
                customer.setBalance(customer.getBalance().add(netChange));
            } else {
                customer.setBalance(customer.getBalance().subtract(netChange));
            }
            customerRepository.save(customer);
        }
    }

    public List<OrderDTO> searchOrders(String customerName, String productName,
            String userName, LocalDate fromDate,
            LocalDate toDate, OrderType orderType) {
        Specification<Order> spec = Specification
                .where(OrderSpecifications.customerNameContains(customerName))
                .and(OrderSpecifications.productNameContains(productName))
                .and(OrderSpecifications.userNameContains(userName))
                .and(OrderSpecifications.dateBetween(fromDate, toDate))
                .and(OrderSpecifications.orderTypeEquals(orderType));
        List<Order> orders = orderRepository.findAll(spec);
        ;

        return orders.stream().map(OrderMapper::toDTO).toList();
    }

    public List<OrderDTO> searchOrders(Long customerId, LocalDate fromDate,
            LocalDate toDate, OrderType orderType) {
        
        Specification<Order> spec = Specification
        .where(OrderSpecifications.customerIdEqual(customerId))
        .and(OrderSpecifications.dateBetween(fromDate, toDate))
        .and(OrderSpecifications.orderTypeEquals(orderType));
        List<Order> orders = orderRepository.findAll(spec);
        return orders.stream().map(OrderMapper::toDTO).toList();
    }

    public Optional<OrderDTO> findById(Long id) {
        return orderRepository.findById(id).map(OrderMapper::toDTO);
    }

}

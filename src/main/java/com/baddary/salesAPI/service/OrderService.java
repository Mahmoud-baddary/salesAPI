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
    private final CustomerService customerService;
    private final CashService cashService;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository, StockService stockService,
            CustomerRepository customerRepository, UserRepository userRepository, CustomerService customerService,
            CashService cashService) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.stockService = stockService;
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
        this.customerService = customerService;
        this.cashService = cashService;
    }


    @Transactional
    public OrderDTO addOrder(OrderDTO orderDTO) {
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
            // buy means increase stock and decrease cash
            for (OrderProduct op : orderEntity.getOrderProductSet()) {
                stockService.increaseStock(op.getProduct().getId(),
                        op.getExpireDate(), op.getBatch(), op.getQuantitySU(), op.getPriceSU());
            }
            // update user cash
            this.cashService.decreaseUserCash(orderDTO.getUserId(), orderDTO.getPaidMoney());
        } else { // sell increase cash and decrease the stock
            for (OrderProduct op : orderEntity.getOrderProductSet()) {
                stockService.decreaseStock(op.getProduct().getId(),
                        op.getExpireDate(), op.getQuantitySU());
            }
            // update user cash
            this.cashService.increaseUserCash(orderDTO.getUserId(), orderDTO.getPaidMoney());
        }
        // Update customer balance
        BigDecimal totalPrice = orderDTO.calculateTotalPrice();
        customerService.updateCustomerBalance(customer, orderDTO, totalPrice);

        return OrderMapper.toDTO(saved);

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

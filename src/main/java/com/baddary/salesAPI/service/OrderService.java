package com.baddary.salesAPI.service;

import com.baddary.salesAPI.dto.OrderDTO;
import com.baddary.salesAPI.dto.OrderProductDTO;
import com.baddary.salesAPI.entity.*;
import com.baddary.salesAPI.enums.OrderType;
import com.baddary.salesAPI.enums.PaymentType;
import com.baddary.salesAPI.mapper.OrderMapper;
import com.baddary.salesAPI.mapper.OrderProductMapper;
import com.baddary.salesAPI.repository.CustomerRepository;
import com.baddary.salesAPI.repository.OrderRepository;
import com.baddary.salesAPI.repository.ProductRepository;
import com.baddary.salesAPI.repository.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
        

        // update customer money oweing
        BigDecimal totalPrice = orderDTO.getTotalPrice();
        if (totalPrice.compareTo(orderDTO.getPaidMoney()) == 1) {
            BigDecimal netChange = totalPrice.subtract(orderDTO.getPaidMoney());
            if (orderDTO.getOrderType() == OrderType.SALE) {
                customer.setBalance(customer.getBalance().add(netChange));
            }else{
                customer.setBalance(customer.getBalance().subtract(netChange));
            }
            
            customerRepository.save(customer);
        }

        return OrderMapper.toDTO(saved);

    }

    public List<OrderDTO> searchOrders(String customerName, String productName,
            String userName, LocalDate fromDate,
            LocalDate toDate, OrderType orderType) {
        List<Order> orders = orderRepository.searchOrders(
                customerName, productName, userName, fromDate, toDate, orderType);
        return orders.stream().map(OrderMapper::toDTO).toList();
    }

    public Optional<OrderDTO> findById(Long id){
        return orderRepository.findById(id).map(OrderMapper::toDTO);
    }

}

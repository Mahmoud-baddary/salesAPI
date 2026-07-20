package com.baddary.salesAPI.repository;

import com.baddary.salesAPI.entity.Order;
import com.baddary.salesAPI.enums.OrderType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

        @Query("SELECT DISTINCT o FROM Order o " +
                        "LEFT JOIN o.orderProductSet op " +
                        "LEFT JOIN op.product p " +
                        "LEFT JOIN o.customer c " +
                        "LEFT JOIN o.user u " +
                        "WHERE (:customerName IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', CAST(:customerName AS string), '%'))) "
                        +
                        "AND (:productName IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', CAST(:productName AS string), '%'))) "
                        +
                        "AND (:userName IS NULL OR LOWER(u.name) LIKE LOWER(CONCAT('%', CAST(:userName AS string), '%'))) "
                        +
                        "AND (:fromDate IS NULL OR o.date >= CAST(:fromDate AS date)) " +
                        "AND (:toDate IS NULL OR o.date <= CAST(:toDate AS date)) " +
                        "AND (:orderType IS NULL OR o.orderType = :orderType) ")
        List<Order> searchOrders(@Param("customerName") String customerName,
                        @Param("productName") String productName,
                        @Param("userName") String userName,
                        @Param("fromDate") LocalDate fromDate,
                        @Param("toDate") LocalDate toDate,
                        @Param("orderType") OrderType orderType);

        @Query("SELECT DISTINCT o FROM Order o " +
                        "LEFT JOIN o.orderProductSet op " +
                        "LEFT JOIN o.customer c " +
                        "WHERE (c.id = :customerId) "
                        +
                        "AND (o.date >= :fromDate) " +
                        "AND (o.date <= :toDate) " +
                        "AND (:orderType IS NULL OR o.orderType = :orderType) ")
        List<Order> searchOrders(@Param("customerId") Long customerId,
                        @Param("fromDate") LocalDate fromDate,
                        @Param("toDate") LocalDate toDate,
                        @Param("orderType") OrderType orderType);

        @Query("""
                        SELECT o from Order o
                        left join fetch o.customer
                        left join fetch o.user
                        left join fetch o.orderProductSet op
                        left join fetch op.product where o.id = :id
                                """)
        Optional<Order> findById(@Param("id") Long id);

}

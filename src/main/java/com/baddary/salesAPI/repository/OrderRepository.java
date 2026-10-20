package com.baddary.salesAPI.repository;

import com.baddary.salesAPI.entity.Order;
import com.baddary.salesAPI.enums.OrderType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
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
                        left join fetch op.product p
                        where o.id = :id
                                """)
        Optional<Order> findById(@Param("id") Long id);

        @Query(value = """
                        SELECT
                            o.id               AS order_id,
                            o.date             AS order_date,
                            o.time             AS order_time,
                            o.order_type       AS order_type,
                            o.discount         AS order_discount,
                            o.paid_money       AS order_paid_money,

                            c.id               AS customer_id,
                            c.name             AS customer_name,


                            u.id               AS user_id,
                            u.name             AS user_name,

                            op.id              AS op_id,
                            op.quantity        AS op_quantity,
                            op.quantity_su     AS op_quantity_su,
                            op.price           AS op_price,
                            op.price_su        AS op_price_su,
                            op.discount        AS op_discount,
                            op.unit            AS op_unit,
                            op.batch           AS op_batch,
                            op.expire_date     AS op_expire_date,
                            op.note            AS op_note,

                            p.id               AS product_id,
                            p.name             AS product_name,
                            p.greatest_unit    AS product_greatest_unit,
                            p.medium_unit      AS product_medium_unit,
                            p.smallest_unit    AS product_smallest_unit,

                            s.id               AS stock_id,
                            s.quantity_su      AS stock_quantity_su,
                            s.price_su         AS stock_price_su,
                            s.batch            AS stock_batch,
                            s.expire_date      AS stock_expire_date
                        FROM orders o
                        LEFT JOIN customers       c  ON c.id = o.customer_id
                        LEFT JOIN users           u  ON u.id = o.user_id
                        LEFT JOIN orders_products op ON op.order_id = o.id
                        LEFT JOIN products        p  ON p.id = op.product_id
                        LEFT JOIN stock           s  ON s.product_id = op.product_id
                                                     AND s.expire_date = op.expire_date
                                                     AND s.batch = op.batch
                        WHERE o.id = :id
                        """, nativeQuery = true)
        List<Object[]> findOrderDetailsRaw(@Param("id") Long id);

        @Query("""
                            SELECT COALESCE(SUM(op.quantitySU), 0)
                            FROM Order o
                            JOIN o.orderProductSet op
                            WHERE o.originalOrder.id = :originalOrderId                              AND op.product.id = :productId
                              AND op.expireDate = :expireDate
                              AND op.batch = :batch
                              AND o.orderType = :returnType
                        """)
        BigDecimal sumReturnedQuantitySU(@Param("originalOrderId") Long originalOrderId,
                        @Param("productId") Long productId,
                        @Param("expireDate") LocalDate expireDate,
                        @Param("batch") String batch,
                        @Param("returnType") OrderType returnType);

        @Override
        @EntityGraph(attributePaths = {
                        "customer",
                        "user",
                        "orderProductSet",
                        "orderProductSet.product"
        })
        List<Order> findAll(Specification<Order> spec);
}

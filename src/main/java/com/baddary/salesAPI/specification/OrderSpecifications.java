package com.baddary.salesAPI.specification;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import com.baddary.salesAPI.enums.OrderType;

import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Join;
import com.baddary.salesAPI.entity.*;

public class OrderSpecifications {
    public static Specification<Order> customerNameContains(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isBlank()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("customer").get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<Order> productNameContains(String productName) {
        return (root, query, cb) -> {
            if (productName == null || productName.isBlank()) {
                return cb.conjunction();
            }
            var join = root.join("orderProductSet");
            return cb.like(cb.lower(join.get("product").get("name")),
                    "%" + productName.toLowerCase() + "%");
        };
    }

    public static Specification<Order> productBarcodeEquals(String barcode) {
        return (root, query, cb) -> {
            if (barcode == null || barcode.isBlank()) {
                return cb.conjunction();
            }
            Join<Order, OrderProduct> orderProductJoin = root
                    .join("orderProductSet", JoinType.LEFT);
            Join<OrderProduct, Product> productJoin = orderProductJoin
                    .join("product", JoinType.LEFT);
            Join<Product, Barcode> barcodeJoin = productJoin
                    .join("barcodes", JoinType.LEFT);
            return cb.equal(
                    cb.lower(barcodeJoin.get("barcodeTxt")),
                    barcode.toLowerCase().trim());

        };
    }

    public static Specification<Order> userNameContains(String userName) {
        return (root, query, cb) -> {
            if (userName == null || userName.isBlank()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("user").get("name")), "%" + userName.toLowerCase() + "%");
        };
    }

    public static Specification<Order> dateBetween(LocalDate from, LocalDate to) {
        return (root, query, cb) -> {
            if (from == null && to == null) {
                return cb.conjunction();
            }
            if (from != null && to != null) {
                return cb.between(root.get("date"), from, to);
            }
            if (from != null) {
                return cb.greaterThanOrEqualTo(root.get("date"), from);
            }
            // to != null
            return cb.greaterThanOrEqualTo(root.get("date"), to);

        };

    }

    public static Specification<Order> orderTypeEquals(OrderType orderType) {
        return (root, query, cb) -> {
            if (orderType == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("orderType"), orderType);
        };
    }

    public static Specification<Order> customerIdEqual(Long customerId) {
        return (root, query, cb) -> {
            if (customerId == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("customer").get("id"), customerId);
        };
    }

    public static Specification<Order> userIdEquals(Long userId) {
        return (root, query, cb) -> {
            if (userId == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("user").get("id"), userId);
        };
    }

    public static Specification<Order> orderIdEqual(Long orderId) {
        return (root, query, cb) -> {
            if (orderId == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("id"), orderId);
        };
    }
}

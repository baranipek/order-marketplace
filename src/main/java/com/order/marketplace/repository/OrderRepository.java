package com.order.marketplace.repository;

import com.order.marketplace.domain.Order;

import java.util.List;

public interface OrderRepository {
    Integer save(Order order);

    void cancel(Integer orderId);

    List<Order> findOrders();
}

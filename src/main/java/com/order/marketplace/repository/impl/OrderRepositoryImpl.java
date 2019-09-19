package com.order.marketplace.repository.impl;

import com.order.marketplace.domain.Order;
import com.order.marketplace.repository.OrderRepository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class OrderRepositoryImpl implements OrderRepository {

    private final AtomicInteger orderIdCounter;

    private final Map<Integer, Order> orderMap;

    public OrderRepositoryImpl(final Map<Integer, Order> orderMap) {
        this.orderIdCounter = new AtomicInteger(orderMap.size());
        this.orderMap = orderMap;
    }

    @Override
    public Integer save(final Order order) {
        Integer orderId = orderIdCounter.incrementAndGet();
        orderMap.putIfAbsent(orderId, Order.builder().userId(order.getUserId())
                .orderType(order.getOrderType()).quantity(order.getQuantity())
                .pricePerKilogram(order.getPricePerKilogram()).build());
        return orderId;
    }

    @Override
    public void cancel(final Integer orderId) {
        orderMap.keySet().removeIf(key -> key.equals(orderId));
    }

    @Override
    public List<Order> findOrders() {
        return orderMap.values().stream().map(order ->
                Order.builder().orderType(order.getOrderType()).quantity(order.getQuantity())
                        .userId(order.getUserId()).pricePerKilogram(order.getPricePerKilogram()).build())
                .collect(Collectors.toList());

    }
}

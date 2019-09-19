package com.order.marketplace.repository.impl;

import com.order.marketplace.domain.Order;
import com.order.marketplace.enumeration.OrderType;
import com.order.marketplace.repository.OrderRepository;
import lombok.var;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class OrderRepositoryImplTest {

    private OrderRepository orderRepository;
    private Map<Integer, Order> orderMap;

    @BeforeEach
    void setUp() {
        orderMap = new ConcurrentHashMap<>();
        orderRepository = new OrderRepositoryImpl(orderMap);
    }

    @Test
    @DisplayName("Add order to repository")
    void orderRequest_SaveOrder_OrderSavedSuccessfully() {
        //given
        final Order order = Order.builder().orderType(OrderType.BUY).pricePerKilogram(10).
                quantity(3).userId("userId1").build();

        // when
        Integer orderId = orderRepository.save(order);

        // then
        Order expectedOrder = orderMap.get(1);
        assertEquals(orderId, Integer.valueOf(1));
        assertEquals(order, expectedOrder);
    }

    @Test
    @DisplayName("Add order to repository")
    void cancelOrderRequest_CancelOrder_OrderCanceledSuccessfully() {
        //given
        final Order order = Order.builder().orderType(OrderType.BUY).pricePerKilogram(10).
                quantity(3).userId("userId1").build();
        Integer orderId = orderRepository.save(order);

        // when
        orderRepository.cancel(orderId);

        // then
       assertNull(orderMap.get(orderId));
    }
}
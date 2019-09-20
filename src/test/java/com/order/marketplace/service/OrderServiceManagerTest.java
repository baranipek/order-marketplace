package com.order.marketplace.service;

import com.order.marketplace.domain.Order;
import com.order.marketplace.domain.OrderResult;
import com.order.marketplace.enumeration.OrderType;
import com.order.marketplace.repository.impl.OrderRepositoryImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class OrderServiceManagerTest {

    @Mock
    private OrderRepositoryImpl orderRepository;

    @InjectMocks
    private OrderServiceManager orderServiceManager;

    @Test
    @DisplayName("Save an order to resource")
    void orderRequest_SaveOrder_OrderSavedSuccessfully() {
        // given
        final Order order = Order.builder().userId("user1")
                .quantity(3)
                .pricePerKilogram(123).orderType(OrderType.BUY)
                .build();

        Integer orderId = 12312;
        when(orderRepository.save(order)).thenReturn(orderId);

        // when
        Integer orderIdExpected = orderServiceManager.saveOrder(order);

        // then
        assertEquals(orderIdExpected, Integer.valueOf(orderId));
        verify(orderRepository).save(order);
    }

    @Test
    @DisplayName("Cancel order")
    void cancelOrderRequest_CancelOrder_OrderCanceledSuccessfully() {
        // given
        final Order order = Order.builder().userId("user1")
                .quantity(3)
                .pricePerKilogram(123).orderType(OrderType.BUY)
                .build();

        // when
        Integer orderId = 12312;
        when(orderRepository.save(order)).thenReturn(orderId);

        Integer orderIdExpected = orderServiceManager.saveOrder(order);
        orderServiceManager.cancelOrder(orderIdExpected);

        // then
        verify(orderRepository).cancel(orderId);
    }

    @Test
    @DisplayName("Display buy orders for unique price in order of highest price")
    void buyerHighestPrices_GetOrders_BuyerHighestPricesFirst() {

        when(orderRepository.findOrders()).thenReturn(Arrays.asList(Order.builder().userId("user4").orderType(OrderType.BUY).
                        pricePerKilogram(100).quantity(3).build(),Order.builder().userId("user3").orderType(OrderType.BUY).
                        pricePerKilogram(50).quantity(2).build(),Order.builder().userId("user2").orderType(OrderType.BUY).
                        pricePerKilogram(100).quantity(2).build(),Order.builder().userId("user1").
                        orderType(OrderType.SELL).pricePerKilogram(20).quantity(2).build()));


        OrderResult orderResult  = orderServiceManager.getOrders();

        assertEquals(orderResult.getBuyerOrders().size(),2);
        assertEquals(orderResult.getSellerOrders().size(),1);
        assertEquals(orderResult.getBuyerOrders().get(0).getQuantity(),Double.valueOf(5.0));
        assertEquals(orderResult.getBuyerOrders().get(0).getPricePerKilogram(),100L);

    }

    @Test
    @DisplayName("Display sell orders for unique price in order of lowest price")
     void sellerLowestPrice_GetOrders_SellerLowestPricesFirst() {

        when(orderRepository.findOrders()).thenReturn(Arrays.asList(Order.builder().userId("user4").orderType(OrderType.SELL).
                pricePerKilogram(100).quantity(3).build(),Order.builder().userId("user3").orderType(OrderType.SELL).
                pricePerKilogram(50).quantity(2).build(),Order.builder().userId("user2").orderType(OrderType.SELL).
                pricePerKilogram(100).quantity(2).build(),Order.builder().userId("user1").
                orderType(OrderType.SELL).pricePerKilogram(20).quantity(2).build()));


        OrderResult orderResult  = orderServiceManager.getOrders();

        assertEquals(orderResult.getSellerOrders().size(),3);
        assertEquals(orderResult.getSellerOrders().get(0).getQuantity(),Double.valueOf(2.0));
        assertEquals(orderResult.getSellerOrders().get(0).getPricePerKilogram(),20L);

    }

}
package com.order.marketplace.service;

import com.order.marketplace.domain.Order;
import com.order.marketplace.domain.OrderResult;
import com.order.marketplace.domain.OrderSummary;
import com.order.marketplace.enumeration.OrderType;
import com.order.marketplace.repository.OrderRepository;
import lombok.var;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.order.marketplace.enumeration.OrderType.BUY;
import static com.order.marketplace.enumeration.OrderType.SELL;
import static java.util.Collections.emptyList;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.*;

public class OrderServiceManager {
    private final OrderRepository repository;

    public OrderServiceManager(OrderRepository repository) {
        this.repository = repository;
    }

    private static Function<Map.Entry<Long, List<Order>>, OrderSummary> orderSummaryFunction(final OrderType orderType) {
        return entry -> OrderSummary.builder().pricePerKilogram(entry.getKey()).quantity(
                entry.getValue().stream()
                        .map(Order::getQuantity)
                        .reduce(Double::sum).orElseGet(() -> (double) 0L)).type(orderType)
                .build();
    }

    private static List<OrderSummary> decorateOrderSummaryList(final Map<OrderType, List<Order>> orders
            , final OrderType orderType
            , final Comparator<OrderSummary> orderDisplayComparator) {
        return orders.getOrDefault(orderType, emptyList()).stream()
                .collect(groupingBy(Order::getPricePerKilogram, toList()))
                .entrySet().stream()
                .map(orderSummaryFunction(orderType))
                .sorted(orderDisplayComparator)
                .collect(Collectors.toList());
    }

    public OrderResult getOrders() {
        final var orders = repository.findOrders().parallelStream()
                .collect(groupingByConcurrent(Order::getOrderType));

        return OrderResult.builder().buyerOrders(decorateOrderSummaryList(orders, BUY
                , comparing(OrderSummary::getPricePerKilogram).reversed())).
                sellerOrders(decorateOrderSummaryList(orders, SELL
                        , comparing(OrderSummary::getPricePerKilogram))).build();
    }

    public Integer saveOrder(final Order order) { return repository.save(order); }

    public void cancelOrder(final Integer orderId) {
        repository.cancel(orderId);
    }

}

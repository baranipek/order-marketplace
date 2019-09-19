package com.order.marketplace.domain;

import com.order.marketplace.enumeration.OrderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@AllArgsConstructor
@Builder
@Data
final public class Order {
    private final String userId;
    private final double quantity;
    private final long pricePerKilogram;
    private final OrderType orderType;
}

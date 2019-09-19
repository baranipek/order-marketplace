package com.order.marketplace.domain;

import com.order.marketplace.enumeration.OrderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class OrderSummary {
    private final Double quantity;
    private final long pricePerKilogram;
    private final OrderType type;
}

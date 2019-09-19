package com.order.marketplace.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class OrderResult {
    private List<OrderSummary> buyerOrders;
    private List<OrderSummary> sellerOrders;
}

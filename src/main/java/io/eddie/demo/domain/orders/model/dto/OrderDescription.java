package io.eddie.demo.domain.orders.model.dto;

import io.eddie.demo.domain.orders.model.entity.Orders;
import io.eddie.demo.domain.orders.model.vo.OrderState;

import java.time.LocalDateTime;
import java.util.List;

public record OrderDescription(
        String orderCode,
        OrderState orderStatus,
        List<OrderItemDescription> orderItems,
        LocalDateTime orderedAt,
        Long totalPrice
) {
}

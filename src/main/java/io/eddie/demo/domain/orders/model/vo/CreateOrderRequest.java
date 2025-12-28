package io.eddie.demo.domain.orders.model.vo;

import java.util.List;

public record CreateOrderRequest(
        List<String> cartItemCodes
) {
}

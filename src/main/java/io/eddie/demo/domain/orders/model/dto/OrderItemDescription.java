package io.eddie.demo.domain.orders.model.dto;

public record OrderItemDescription(
        String productCode,
        String productName,
        Long productPrice,
        Integer quantity) {
}

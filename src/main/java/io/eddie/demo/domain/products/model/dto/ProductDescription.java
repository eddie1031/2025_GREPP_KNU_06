package io.eddie.demo.domain.products.model.dto;

import java.time.LocalDateTime;

public record ProductDescription(
        String code,
        String sellerCode,
        String name,
        String description,
        Long price,
        LocalDateTime createdAt
) {
}

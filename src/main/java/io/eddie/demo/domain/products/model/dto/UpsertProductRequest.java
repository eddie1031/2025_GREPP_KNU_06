package io.eddie.demo.domain.products.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record UpsertProductRequest(
        @NotBlank(message = "상품명은 반드시 입력되어야 합니다.")
        String name,
        @NotBlank(message = "설명은 공란일 수 없습니다.")
        String description,
        @Min(value = 1000L, message = "최소한 1,000원 이상은 입력하여 주시기 바랍니다.")
        Long price
) {
}

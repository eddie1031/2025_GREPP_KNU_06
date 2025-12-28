package io.eddie.demo.domain.carts.model.vo;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCartItemRequest(
        @NotBlank(message = "상품 코드는 반드시 입력되어야 합니다.")
        String productCode,

        @NotBlank(message = "상품명은 반드시 입력되어야 합니다.")
        String productName,

        @NotNull(message = "상품 가격은 반드시 입력되어야 합니다.")
        Long productPrice,

        @Min(value = 1, message = "최소한 한 개 이상을 추가해주시기 바랍니다.")
        Integer quantity
) {
}

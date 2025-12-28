package io.eddie.demo.domain.payments.model.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record DepositPaymentRequest(
        @NotBlank
        String orderCode,

        @Positive
        Long amount
) {
}

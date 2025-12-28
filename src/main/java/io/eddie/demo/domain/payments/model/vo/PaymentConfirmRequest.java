package io.eddie.demo.domain.payments.model.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PaymentConfirmRequest(

        @NotBlank
        String orderCode,

        @NotNull
        Boolean useDeposit,

        Long depositAmount,

        @Positive
        Long amount,

        String paymentKey

) {
}

package io.eddie.demo.domain.payments.model.vo;

public record PaymentDescription(
        String orderCode,
        String paymentCode
) {
}

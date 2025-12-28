package io.eddie.demo.domain.payments.model.vo;

public record TossPaymentsRequest(
        String paymentKey,
        String orderId,
        String amount
) {
}

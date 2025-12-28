package io.eddie.demo.domain.payments.model.dto;

import io.eddie.demo.domain.payments.model.vo.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentRequest {

    private String accountCode;
    private PaymentType paymentType;
    private String paymentKey;
    private String orderCode;
    private Long amount;

    public PaymentRequest(String accountCode, PaymentType paymentType, String orderCode, Long amount) {
        this.accountCode = accountCode;
        this.paymentType = paymentType;
        this.orderCode = orderCode;
        this.amount = amount;
    }

}

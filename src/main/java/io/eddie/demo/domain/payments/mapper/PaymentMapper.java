package io.eddie.demo.domain.payments.mapper;

import io.eddie.demo.domain.payments.model.entity.Payment;
import io.eddie.demo.domain.payments.model.vo.PaymentDescription;

public class PaymentMapper {

    public static PaymentDescription toDescription(Payment payment) {
        return new PaymentDescription(
                payment.getOrderCode(),
                payment.getCode()
        );
    }

}

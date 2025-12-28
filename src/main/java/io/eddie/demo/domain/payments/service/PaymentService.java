package io.eddie.demo.domain.payments.service;

import io.eddie.demo.domain.payments.model.entity.Payment;
import io.eddie.demo.domain.payments.model.vo.PaymentConfirmRequest;
import io.eddie.demo.domain.payments.model.dto.PaymentRequest;

public interface PaymentService {

    Payment process(String accountCode, PaymentConfirmRequest request);

    Payment pay(PaymentRequest request);

    Payment refund(String accountCode, PaymentRequest request);

    Payment confirmPayment(PaymentRequest request) throws Exception;

}

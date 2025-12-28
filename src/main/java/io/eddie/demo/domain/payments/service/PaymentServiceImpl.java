package io.eddie.demo.domain.payments.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.eddie.demo.domain.deposits.model.entity.Deposit;
import io.eddie.demo.domain.deposits.model.entity.DepositHistory;
import io.eddie.demo.domain.deposits.service.DepositService;
import io.eddie.demo.domain.orders.model.entity.Orders;
import io.eddie.demo.domain.orders.service.OrderService;
import io.eddie.demo.domain.payments.model.dto.PaymentRequest;
import io.eddie.demo.domain.payments.model.entity.Payment;
import io.eddie.demo.domain.payments.model.vo.*;
import io.eddie.demo.domain.payments.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final ObjectMapper om;

    private final OrderService orderService;
    private final DepositService depositService;
    private final PaymentRepository paymentRepository;

    @Value("${custom.payments.toss.secrets}")
    private String tossPaymentSecrets;

    @Value("${custom.payments.toss.confirm-url}")
    private String tossPaymentConfirmUrl;

    @Override
    @Transactional
    public Payment process(String accountCode, PaymentConfirmRequest request) {

        if (request.useDeposit()) {

            Payment depositPayment = pay(getPaymentRequest(accountCode, PaymentType.DEPOSIT, request));

            if (request.amount() > 0) {
                return pay(getPaymentRequest(accountCode, PaymentType.TOSS_PAYMENT, request));
            }

            return depositPayment;

        }

        return pay(getPaymentRequest(accountCode, PaymentType.TOSS_PAYMENT, request));

    }

    private PaymentRequest getPaymentRequest(String accountCode, PaymentType type, PaymentConfirmRequest request) {
        return new PaymentRequest(accountCode, type, request.paymentKey(), request.orderCode(), request.amount());
    }

    @Override
    @Transactional
    public Payment pay(PaymentRequest request) {

        Payment payment;

        if (request.getPaymentType() == PaymentType.DEPOSIT) {
            payment = handleDepositPayment(request.getAccountCode(), request.getOrderCode(), request.getAmount());
        } else if (request.getPaymentType() == PaymentType.TOSS_PAYMENT) {
            payment = handleTossPayment(request.getAccountCode(), request.getOrderCode(), request.getPaymentKey(), request.getAmount());
        } else {
            throw new UnsupportedOperationException("결제 형식이 잘못되었습니다.");
        }

        return payment;

    }

    private Payment handleDepositPayment(String accountCode, String orderCode, Long amount) {

        Deposit deposit = depositService.getByAccountCode(accountCode);

        if (amount > deposit.getBalance()) {
            throw new IllegalStateException("잔액이 충분하지 않습니다.");
        }

        DepositHistory history = depositService.withdraw(accountCode, DepositHistory.DepositHistoryType.PAYMENT_INTERNAL, amount);

        Payment payment = Payment.builder()
                .accountCode(accountCode)
                .orderCode(orderCode)
                .amount(amount)
                .depositHistoryCode(history.getCode())
                .build();

        payment.setPaymentStatus(PaymentStatus.PAYMENT_COMPLETED);

        orderService.completeOrder(accountCode, orderCode);

        return paymentRepository.save(payment);

    }

    private Payment handleTossPayment(String accountCode, String orderCode, String paymentKey, Long amount) {

        boolean result = processTossPayment(new TossPaymentsRequest(paymentKey, orderCode, amount.toString()));

        if (!result)
            throw new IllegalStateException("토스페이먼츠 결제에 실패하였습니다.");

        depositService.charge(accountCode, DepositHistory.DepositHistoryType.CHARGE_TOSS, amount);
        DepositHistory history = depositService.withdraw(accountCode, DepositHistory.DepositHistoryType.PAYMENT_TOSS, amount);

        Payment payment = Payment.builder()
                .accountCode(accountCode)
                .orderCode(orderCode)
                .amount(amount)
                .depositHistoryCode(history.getCode())
                .build();

        payment.setPaymentStatus(PaymentStatus.PAYMENT_COMPLETED);

        orderService.completeOrder(accountCode, orderCode);

        return paymentRepository.save(payment);

    }

    @Override
    @Transactional
    public Payment refund(String accountCode, PaymentRequest request) {

        Payment payment = paymentRepository.findByOrderCode(request.getOrderCode())
                .orElseThrow(() -> new IllegalArgumentException("결제 내역을 찾을 수 없습니다."));

        if (!payment.canRevoke())
            throw new IllegalStateException("환불할 수 없는 상태입니다.");

        Orders order = orderService.getOrder(accountCode, request.getOrderCode());

        depositService.refund(accountCode, DepositHistory.DepositHistoryType.REFUND_INTERNAL, order.getTotalPrice());

        payment.setPaymentStatus(PaymentStatus.PAYMENT_REFUNDED);

        return payment;
    }

    @Override
    @Transactional
    public Payment confirmPayment(PaymentRequest request) {
        throw new UnsupportedOperationException("");
    }

    private boolean processTossPayment(TossPaymentsRequest request) {

        try {
            String authorization = "Basic " + Base64.getEncoder()
                    .encodeToString((tossPaymentSecrets + ":").getBytes(StandardCharsets.UTF_8));

            Map<String, Object> requestMap = om.convertValue(request, new TypeReference<>() {
            });

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(tossPaymentConfirmUrl))
                    .header("Authorization", authorization)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofByteArray(om.writeValueAsBytes(requestMap)))
                    .build();

            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == HttpStatus.OK.value()) {
                return true;
            } else {
                log.error("토스페이먼츠 결제 수행 과정에서 오류가 발생하였습니다. 다시 시도하여 주시기 바랍니다. 응답코드 : {}", response.statusCode());
                log.info("response.body() = {}", response.body());
                return false;
            }

        } catch (Exception e) {
            log.error("토스페이먼츠 결제 수행 과정에서 오류가 발생하였습니다.");
            return false;
        }


    }

}

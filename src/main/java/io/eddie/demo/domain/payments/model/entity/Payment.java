package io.eddie.demo.domain.payments.model.entity;

import io.eddie.demo.common.model.persistence.BaseEntity;
import io.eddie.demo.domain.payments.model.vo.PaymentStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Payment extends BaseEntity {

    private Long amount;

    private String accountCode;

    private String orderCode;

    private String depositHistoryCode;

    private LocalDateTime paidAt;

    @Setter
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus = PaymentStatus.PAYMENT_ACCEPTED;

    /**
     * 지불이 되었고, 결제 완료일 기준 3일 이내에만 환불 가능
     */
    public boolean canRevoke() {
        return this.paymentStatus == PaymentStatus.PAYMENT_COMPLETED
                &&
                this.getPaidAt().plusDays(3).isAfter(LocalDateTime.now());
    }


    public void complete(String depositHistoryCode) {
        this.paidAt = LocalDateTime.now();
        this.paymentStatus = PaymentStatus.PAYMENT_COMPLETED;
        this.depositHistoryCode = depositHistoryCode;
    }

    public void refund(String depositHistoryCode) {
        this.paymentStatus = PaymentStatus.PAYMENT_REFUNDED;
        this.depositHistoryCode = depositHistoryCode;
    }

    public void cancel() {
        this.paymentStatus = PaymentStatus.PAYMENT_CANCELLED;
    }

    @Builder
    public Payment(Long amount, String accountCode, String orderCode, String depositHistoryCode) {
        this.amount = amount;
        this.accountCode = accountCode;
        this.orderCode = orderCode;
        this.depositHistoryCode = depositHistoryCode;
    }

}

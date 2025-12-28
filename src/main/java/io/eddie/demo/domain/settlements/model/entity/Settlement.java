package io.eddie.demo.domain.settlements.model.entity;

import io.eddie.demo.common.model.persistence.BaseEntity;
import io.eddie.demo.domain.settlements.model.vo.SettlementStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Settlement extends BaseEntity {

    private String buyerCode;
    private String sellerCode;

    private String orderItemCode;

    @Enumerated(EnumType.STRING)
    private SettlementStatus settlementStatus = SettlementStatus.SETTLEMENT_CREATED;

    private Double settlementRate;

    private LocalDateTime settlementDate;

    private Long totalAmount;

    // 내가 정산할 비용
    private Long settlementAmount;

    // 고객에게 정산할 비용
    private Long settlementBalance;

    @Builder
    public Settlement(String buyerCode, String sellerCode, String orderItemCode, Double settlementRate, LocalDateTime settlementDate, Long totalAmount) {
        this.buyerCode = buyerCode;
        this.sellerCode = sellerCode;
        this.orderItemCode = orderItemCode;
        this.settlementRate = settlementRate;
        this.settlementDate = settlementDate;
        this.totalAmount = totalAmount;
    }

    public void process() {
        this.settlementStatus = SettlementStatus.SETTLEMENT_PROCESSING;
    }

    public void done() {
        this.settlementStatus = SettlementStatus.SETTLEMENT_SUCCESS;
        this.settlementDate = LocalDateTime.now();
    }

    public void fail() {
        this.settlementStatus = SettlementStatus.SETTLEMENT_FAILED;
    }

    private long calculateSettlementAmount() {
        return (long) (this.totalAmount - calculateSettlementBalance());
    }

    private long calculateSettlementBalance() {
        return (long) (this.totalAmount * this.settlementRate);
    }

    public void applySettlementPolicy() {
        this.settlementBalance = calculateSettlementBalance();
        this.settlementAmount = calculateSettlementAmount();
    }

}

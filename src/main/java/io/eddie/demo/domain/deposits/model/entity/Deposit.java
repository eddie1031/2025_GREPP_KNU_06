package io.eddie.demo.domain.deposits.model.entity;

import io.eddie.demo.common.model.persistence.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Deposit extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String accountCode;

    private Long balance = 0L;

    @Builder
    public Deposit(String accountCode) {
        this.accountCode = accountCode;
    }

    public void charge(Long amount) {
        if (amount <= 0) throw new IllegalArgumentException("금액은 0보다 커야 합니다.");
        this.balance += amount;
    }

    public void withdraw(Long amount) {
        if (amount <= 0) throw new IllegalArgumentException("금액은 0보다 커야 합니다.");
        if (this.balance < amount) throw new IllegalStateException("잔액 부족");
        this.balance -= amount;
    }

}

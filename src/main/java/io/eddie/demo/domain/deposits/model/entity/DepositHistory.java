package io.eddie.demo.domain.deposits.model.entity;

import io.eddie.demo.common.model.persistence.BaseEntity;
import io.eddie.demo.domain.accounts.model.entity.Account;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class DepositHistory extends BaseEntity {

    private String accountCode;

    @ManyToOne
    @JoinColumn(name = "deposit_id")
    private Deposit deposit;

    private Long amount;

    @Enumerated(EnumType.STRING)
    private DepositHistoryType type;

    public enum DepositHistoryType {

        CHARGE_TRANSFER {
            @Override
            public void apply(Deposit deposit, Long amount) {
                deposit.charge(amount);
            }
        },
        CHARGE_TOSS {
            @Override
            public void apply(Deposit deposit, Long amount) {
                deposit.charge(amount);
            }
        },

        PAYMENT_TOSS {
            @Override
            public void apply(Deposit deposit, Long amount) {
                deposit.withdraw(amount);
            }
        },
        PAYMENT_INTERNAL {
            @Override
            public void apply(Deposit deposit, Long amount) {
                deposit.withdraw(amount);
            }
        },

        REFUND_INTERNAL {
            @Override
            public void apply(Deposit deposit, Long amount) {
                deposit.charge(amount);
            }
        },
        REFUND_TOSS {
            @Override
            public void apply(Deposit deposit, Long amount) {
                deposit.charge(amount);
            }
        },

        SETTLEMENT {
            @Override
            public void apply(Deposit deposit, Long amount) {
                deposit.charge(amount);
            }
        };

        public abstract void apply(Deposit deposit, Long amount);

    }

    @Builder
    public DepositHistory(String accountCode, Deposit deposit, Long amount, DepositHistoryType type) {
        this.accountCode = accountCode;
        this.deposit = deposit;
        this.amount = amount;
        this.type = type;
    }

}

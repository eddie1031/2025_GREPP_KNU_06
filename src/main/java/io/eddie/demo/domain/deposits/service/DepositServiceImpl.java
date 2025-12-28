package io.eddie.demo.domain.deposits.service;

import io.eddie.demo.domain.accounts.model.entity.Account;
import io.eddie.demo.domain.deposits.model.entity.Deposit;
import io.eddie.demo.domain.deposits.model.entity.DepositHistory;
import io.eddie.demo.domain.deposits.repository.DepositHistoryRepository;
import io.eddie.demo.domain.deposits.repository.DepositRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DepositServiceImpl implements DepositService {

    private final DepositRepository depositRepository;
    private final DepositHistoryRepository depositHistoryRepository;

    @Override
    @Transactional
    public Deposit save(String accountCode) {

        Deposit deposit = Deposit.builder()
                .accountCode(accountCode)
                .build();

        deposit = depositRepository.save(deposit);

        return deposit;

    }

    @Override
    @Transactional(readOnly = true)
    public Deposit getByAccountCode(String accountCode) {
        return depositRepository.findByAccountCode(accountCode)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 예치금 코드 입니다."));
    }

    @Override
    public boolean hasEnoughBalance(String depositCode, Long amount) {
        return depositRepository.hasEnoughBalance(depositCode, amount);
    }

    @Override
    @Transactional
    public DepositHistory charge(String accountCode, DepositHistory.DepositHistoryType type, Long amount) {
        return applyHistory(accountCode, type, amount);
    }

    @Override
    @Transactional
    public DepositHistory withdraw(String accountCode, DepositHistory.DepositHistoryType type, Long amount) {
        return applyHistory(accountCode, type, amount);
    }

    @Override
    @Transactional
    public DepositHistory refund(String accountCode, DepositHistory.DepositHistoryType type, Long amount) {
        return applyHistory(accountCode, type, amount);
    }

    private DepositHistory applyHistory(String accountCode, DepositHistory.DepositHistoryType type, Long amount) {

        Deposit deposit = getByAccountCode(accountCode);

        type.apply(deposit, amount);

        DepositHistory history = DepositHistory.builder()
                .type(type)
                .deposit(deposit)
                .accountCode(accountCode)
                .amount(amount)
                .build();

        history = depositHistoryRepository.save(history);

        return history;

    }

}

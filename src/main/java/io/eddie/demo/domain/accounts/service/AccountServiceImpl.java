package io.eddie.demo.domain.accounts.service;

import io.eddie.demo.common.model.event.AccountCreatedEvent;
import io.eddie.demo.domain.accounts.model.dto.CreateAccountRequest;
import io.eddie.demo.domain.accounts.model.entity.Account;
import io.eddie.demo.domain.accounts.repository.AccountRepository;
import io.eddie.demo.domain.carts.model.entity.Cart;
import io.eddie.demo.domain.carts.service.CartService;
import io.eddie.demo.domain.deposits.model.entity.Deposit;
import io.eddie.demo.domain.deposits.service.DepositService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    @Value("${custom.kafka.topic.account.event}")
    private String accountEventTopic;

    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;

    private final CartService cartService;
    private final DepositService depositService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public Account create(CreateAccountRequest request) {

        Account account = Account.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .email(request.email())
                .build();

        AccountCreatedEvent accountCreatedEvent = new AccountCreatedEvent(account.getCode());

        kafkaTemplate.send(accountEventTopic, accountCreatedEvent);

        Cart cart = cartService.save(account.getCode());
        account.setCartCode(cart.getCode());

        Deposit deposit = depositService.save(account.getCode());
        account.setDepositCode(deposit.getCode());

        return accountRepository.save(account);

    }

    @Override
    @Transactional(readOnly = true)
    public Account getByUsername(String username) {
        return accountRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원은 존재하지 않습니다."));
    }

    @Override
    @Transactional(readOnly = true)
    public Account getByAccountCode(String accountCode) {
        return accountRepository.findByCode(accountCode)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원은 존재하지 않습니다."));
    }

}

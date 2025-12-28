package io.eddie.demo.domain.accounts.service;

import io.eddie.demo.domain.accounts.model.dto.CreateAccountRequest;
import io.eddie.demo.domain.accounts.model.entity.Account;

import java.util.Optional;

public interface AccountService {

    Account create(CreateAccountRequest request);
    Account getByUsername(String username);
    Account getByAccountCode(String accountCode);

}

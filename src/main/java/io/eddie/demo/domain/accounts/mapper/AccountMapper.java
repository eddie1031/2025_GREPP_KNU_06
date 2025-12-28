package io.eddie.demo.domain.accounts.mapper;

import io.eddie.demo.domain.accounts.model.dto.AccountDescription;
import io.eddie.demo.domain.accounts.model.dto.AccountDetail;
import io.eddie.demo.domain.accounts.model.entity.Account;

public class AccountMapper {

    public static AccountDescription toDescription(Account account) {
        return new AccountDescription(account.getCode(), account.getUsername());
    }

    public static AccountDetail toDetail(Account account) {
        return new AccountDetail(account.getUsername(), account.getEmail());
    }

}

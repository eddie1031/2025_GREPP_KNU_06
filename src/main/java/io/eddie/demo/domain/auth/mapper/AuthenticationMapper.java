package io.eddie.demo.domain.auth.mapper;

import io.eddie.demo.domain.auth.model.dto.AuthenticationDetails;
import io.eddie.demo.domain.accounts.model.entity.Account;

public class AuthenticationMapper {

    public static AuthenticationDetails toDetails(Account account) {
        return new AuthenticationDetails(account.getCode(), account.getUsername(), account.getPassword(), account.getRoles());
    }

}

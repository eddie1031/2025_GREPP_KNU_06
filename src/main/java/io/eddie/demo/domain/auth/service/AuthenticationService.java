package io.eddie.demo.domain.auth.service;

import io.eddie.demo.domain.auth.mapper.AuthenticationMapper;
import io.eddie.demo.domain.accounts.model.entity.Account;
import io.eddie.demo.domain.accounts.service.AccountService;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements UserDetailsService {

    private final AccountService accountService;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        Account account = accountService.getByUsername(username);

        return AuthenticationMapper.toDetails(account);

    }

}

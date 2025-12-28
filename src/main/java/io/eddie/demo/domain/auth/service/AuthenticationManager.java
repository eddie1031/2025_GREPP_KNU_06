package io.eddie.demo.domain.auth.service;

import io.eddie.demo.domain.auth.model.dto.AuthenticationDetails;
import io.eddie.demo.domain.auth.model.vo.TokenBody;

public interface AuthenticationManager {

    AuthenticationDetails loadAuthenticationByCode(String accountCode);

    boolean validateToken(String token);
    TokenBody parseToken(String token);
    String issueToken(String code);


}

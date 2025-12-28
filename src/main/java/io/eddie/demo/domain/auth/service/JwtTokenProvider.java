package io.eddie.demo.domain.auth.service;

import io.eddie.demo.domain.auth.model.vo.TokenBody;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Slf4j
@Service
public class JwtTokenProvider implements TokenProvider {

    @Value("${custom.jwt.secrets.app-key}")
    private String appKey;

    @Override
    public String issue(Long validateTime, Map<String, Object> claims) {
        JwtBuilder builder = Jwts.builder()
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + validateTime))
                .signWith(getSecretKey(), Jwts.SIG.HS256);

        for (Map.Entry<String, Object> claimEntry : claims.entrySet()) {
            builder.claim(claimEntry.getKey(), claimEntry.getValue());
        }

        return builder.compact();
    }

    @Override
    public boolean validate(String token) {
        try {
            getClaims(token);
            return true;
        } catch ( JwtException e ) {
            log.info("Invalid JWT Token was detected: {}  msg : {}", token ,e.getMessage());
            System.out.println("Invalid Token signature");
        } catch ( IllegalStateException e ) {
            log.info("JWT claims String is empty: {}  msg : {}", token ,e.getMessage());
        } catch ( Exception e ) {
            log.error("an error raised from validating token : {}  msg : {}", token ,e.getMessage());
        }

        return false;
    }

    @Override
    public Map<String, Object> getClaims(String token) {

        Jws<Claims> parsed = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token);

        return parsed.getPayload();
    }

    @Override
    public TokenBody parse(String token) {
        return new TokenBody(
                getClaims(token).get("accountCode").toString()
        );
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(appKey.getBytes());
    }
}

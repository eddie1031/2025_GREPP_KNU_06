package io.eddie.demo.domain.auth.config;

import io.eddie.demo.domain.auth.model.dto.AuthenticationDetails;
import io.eddie.demo.domain.auth.model.vo.TokenBody;
import io.eddie.demo.domain.auth.service.AuthenticationManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationManager application;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
    ) throws ServletException, IOException {

        String token = resolveToken(request);

        if ( token != null && application.validateToken(token) ) {
            TokenBody tokenBody = application.parseToken(token);

            AuthenticationDetails authenticationDetails = application.loadAuthenticationByCode(tokenBody.accountCode());

            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(authenticationDetails, token, authenticationDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);

    }

    private String resolveToken(HttpServletRequest request) {

        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;

    }

}

package io.eddie.demo.domain.auth.config;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final TokenAuthenticationFilter tokenAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            AuthenticationSuccessHandler successHandler,
            AuthenticationFailureHandler failureHandler
    ) throws Exception {

        return http

                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())

                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                )

                .formLogin(f -> f.successHandler(successHandler)
                        .failureHandler(failureHandler))

//                .sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

//                .exceptionHandling(ex -> ex
//                        .authenticationEntryPoint((request, response, authException) -> {
//                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "인증에 실패하였습니다.");
//                        })
//                )

                .authorizeHttpRequests(
                        auth -> auth.requestMatchers("/favicon.ico")
                                                                .permitAll()
                                                            .requestMatchers(CorsUtils::isPreFlightRequest)
                                                                .permitAll()
                                                            .requestMatchers("/view/**")
                                                                .permitAll()
                                                            .requestMatchers("/h2-console/**")
                                                                .permitAll()
                                                            .requestMatchers(HttpMethod.POST, "/accounts")
                                                                .permitAll()
                                                            .requestMatchers(HttpMethod.GET, "/products/**")
                                                                .permitAll()
                                                            .anyRequest()
                                                                .authenticated()
                )

                .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                .build();

    }

}

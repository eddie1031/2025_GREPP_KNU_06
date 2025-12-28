package io.eddie.demo.domain.auth.model.dto;

public record SignInRequest(
        String username,
        String password
) {
}

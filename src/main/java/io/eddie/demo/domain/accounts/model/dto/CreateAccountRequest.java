package io.eddie.demo.domain.accounts.model.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateAccountRequest(
        @NotBlank
        String username,
        @NotBlank
        String password,
        @NotBlank
        String email
) {
}

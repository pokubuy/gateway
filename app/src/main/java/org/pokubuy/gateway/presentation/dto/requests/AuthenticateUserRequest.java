package org.pokubuy.gateway.presentation.dto.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthenticateUserRequest(
    @NotBlank
    @Email
    String email,

    @NotBlank
    @Size(min = 8)
    String password
) {}

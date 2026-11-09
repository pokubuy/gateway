package org.pokubuy.gateway.presentation.responses;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RenewTokenResponse(
    @NotBlank
    @Pattern(
        regexp = "^[A-Za-z0-9_-]+\\.[A-Za-z0-9_-]+\\.[A-Za-z0-9_-]+$",
        message = "Not a JWT-token"
    )
    String accessToken
) {}

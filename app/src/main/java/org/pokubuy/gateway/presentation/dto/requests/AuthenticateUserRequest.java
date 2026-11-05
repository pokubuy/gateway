package org.pokubuy.gateway.presentation.dto.requests;

public record AuthenticateUserRequest(
    String email,
    String password
) {}

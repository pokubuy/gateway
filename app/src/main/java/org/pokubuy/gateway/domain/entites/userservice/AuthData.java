package org.pokubuy.gateway.domain.entites.userservice;

public record AuthData(
    String accessToken,
    String refreshToken
) {}

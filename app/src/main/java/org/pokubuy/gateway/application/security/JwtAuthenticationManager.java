package org.pokubuy.gateway.application.security;

import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public final class JwtAuthenticationManager implements AuthenticationManager {

    @Override
    public @NonNull Authentication authenticate(
        @NonNull Authentication authentication
    ) throws AuthenticationException {
        return authentication;
    }
}

package org.pokubuy.gateway.application.security;

import org.jspecify.annotations.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@Component
public final class JwtAuthenticationConverter implements Converter<Jwt, Mono<JwtAuthenticationToken>> {

    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter =
        new JwtGrantedAuthoritiesConverter();

    @Override
    public Mono<JwtAuthenticationToken> convert(@NonNull Jwt jwt) {
        return Mono.fromCallable(() -> new JwtAuthenticationToken(
            jwt, extractResourceRoles(jwt), getPrincipalClaimName(jwt))
        );
    }

    private String getPrincipalClaimName(Jwt jwt) {
        String claimName = JwtClaimNames.SUB;
        return jwt.getClaim(claimName);
    }

    private List<SimpleGrantedAuthority> extractResourceRoles(Jwt jwt) {
        if (!(jwt.getClaim("realm_access") instanceof Map<?, ?> realmAccess)) {
            return List.of();
        }
        if (!(realmAccess.get("roles") instanceof List<?> strRoles)) {
            return List.of();
        }
        return strRoles.stream()
            .filter(String.class::isInstance)
            .map(String.class::cast)
            .map(strRole -> new SimpleGrantedAuthority("ROLE_" + strRole.toUpperCase(Locale.ROOT)))
            .toList();
    }
}

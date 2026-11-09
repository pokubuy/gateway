package org.pokubuy.gateway.application.configuration;

import lombok.RequiredArgsConstructor;
import org.pokubuy.gateway.application.security.JwtAuthenticationConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http) {
        http
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .authorizeExchange(exchanges -> exchanges
                .pathMatchers("/v3/api-docs/**", "/swagger-ui/**").permitAll()
                .pathMatchers("/users/login", "/users/renew").permitAll()
                .pathMatchers(HttpMethod.POST, "/users/clients", "/users/sellers").permitAll()
                .pathMatchers(HttpMethod.POST, "/users/moderators", "/users/support").hasRole("MODERATOR")
                .pathMatchers(HttpMethod.GET, "/users").hasAnyRole("MODERATOR", "SUPPORT")
                .anyExchange().denyAll()
            )
            .oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwtSpec ->
                    jwtSpec.jwtAuthenticationConverter(new JwtAuthenticationConverter())
                )
            );
        return http.build();
    }
}

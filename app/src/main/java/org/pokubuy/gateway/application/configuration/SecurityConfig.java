package org.pokubuy.gateway.application.configuration;

import lombok.RequiredArgsConstructor;
import org.pokubuy.gateway.application.security.JwtAuthenticationConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${security.cors.allowed-origins}")
    private List<String> allowedOrigins;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        var config = new CorsConfiguration();
        config.setAllowedOrigins(allowedOrigins);
        config.setAllowedMethods(List.of("*"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        config.setAllowPrivateNetwork(true);

        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public SecurityWebFilterChain filterChain(
        ServerHttpSecurity http,
        CorsConfigurationSource corsConfigurationSource
    ) {
        http
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .authorizeExchange(exchanges -> exchanges
                .pathMatchers("/v3/api-docs/**", "/swagger-ui/**").permitAll()
                .pathMatchers("/users/login", "/users/renew").permitAll()
                .pathMatchers(HttpMethod.POST, "/users/clients", "/users/sellers").permitAll()
                .pathMatchers(HttpMethod.POST, "/users/moderators", "/users/support").hasRole("MODERATOR")
                .pathMatchers(HttpMethod.GET, "/users").hasAnyRole("MODERATOR", "SUPPORT")
                .pathMatchers("/client/**").authenticated()
                .pathMatchers("/seller/**").hasAnyRole("SELLER", "MODERATOR", "SUPPORT")
                .pathMatchers("/orders/**").authenticated()
                .pathMatchers("/catalog/listings/**").permitAll()
                .pathMatchers("/sellers/*/name").permitAll()
                .pathMatchers("/sellers/**").hasAnyRole("SELLER", "MODERATOR", "SUPPORT")
                .pathMatchers(HttpMethod.GET, "/categories/**").permitAll()
                .anyExchange().denyAll()
            )
            .oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwtSpec ->
                    jwtSpec.jwtAuthenticationConverter(new JwtAuthenticationConverter())
                )
            )
            .cors(cors -> cors.configurationSource(corsConfigurationSource));
        return http.build();
    }
}

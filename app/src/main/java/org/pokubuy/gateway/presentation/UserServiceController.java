package org.pokubuy.gateway.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.pokubuy.gateway.presentation.dto.requests.AuthenticateUserRequest;
import org.pokubuy.gateway.presentation.dto.requests.CreateClientRequest;
import org.pokubuy.gateway.presentation.dto.requests.CreateSellerRequest;
import org.pokubuy.gateway.presentation.dto.requests.RenewTokenRequest;
import org.pokubuy.gateway.presentation.responses.RenewTokenResponse;
import org.pokubuy.userservice.api.enitites.AuthData;
import org.pokubuy.userservice.api.enitites.enums.UserRole;
import org.pokubuy.userservice.api.services.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserServiceController {

    private final UserService userService;

    @PostMapping("/clients")
    public Mono<AuthData> createClient(
        @Valid @RequestBody CreateClientRequest request
    ) {
        return userService.createUser(
            request.username(),
            request.email(),
            request.password(),
            List.of(UserRole.CLIENT)
        );
    }

    @PostMapping("/sellers")
    public Mono<AuthData> createSeller(
        @Valid @RequestBody CreateSellerRequest request
    ) {
        return userService.createUser(
            request.username(),
            request.email(),
            request.password(),
            List.of(UserRole.SELLER)
        );
    }

    @PostMapping("/login")
    public Mono<AuthData> authenticateUser(
        @Valid @RequestBody AuthenticateUserRequest request
    ) {
        return userService.authenticateUser(
            request.email(),
            request.password()
        );
    }

    @PostMapping("/renew")
    public Mono<RenewTokenResponse> renewToken(
        @Valid @RequestBody RenewTokenRequest renewTokenRequest
    ) {
        return userService.renewToken(renewTokenRequest.refreshToken())
            .map(RenewTokenResponse::new);
    }
}

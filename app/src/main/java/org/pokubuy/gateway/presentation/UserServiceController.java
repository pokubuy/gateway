package org.pokubuy.gateway.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.pokubuy.gateway.presentation.dto.requests.AuthenticateUserRequest;
import org.pokubuy.gateway.presentation.dto.requests.CreateUserRequest;
import org.pokubuy.gateway.presentation.dto.requests.RenewTokenRequest;
import org.pokubuy.gateway.presentation.responses.RenewTokenResponse;
import org.pokubuy.userservice.api.enitites.AuthData;
import org.pokubuy.userservice.api.enitites.UserInfo;
import org.pokubuy.userservice.api.enitites.enums.UserRole;
import org.pokubuy.userservice.api.services.UserService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserServiceController {

    private final UserService userService;

    @PostMapping("/clients")
    public Mono<AuthData> createClient(
        @Valid @RequestBody CreateUserRequest request
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
        @Valid @RequestBody CreateUserRequest request
    ) {
        return userService.createUser(
            request.username(),
            request.email(),
            request.password(),
            List.of(UserRole.SELLER)
        );
    }

    @PostMapping("/support")
    public Mono<AuthData> createSupport(
        @Valid @RequestBody CreateUserRequest request
    ) {
        return userService.createUser(
            request.username(),
            request.email(),
            request.password(),
            List.of(UserRole.SUPPORT)
        );
    }

    @PostMapping("/moderators")
    public Mono<AuthData> createModerator(
        @Valid @RequestBody CreateUserRequest request
    ) {
        return userService.createUser(
            request.username(),
            request.email(),
            request.password(),
            List.of(UserRole.MODERATOR)
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

    @GetMapping("/{id}")
    public Mono<UserInfo> getUser(@PathVariable UUID id) {
        return userService.getUser(id);
    }
}

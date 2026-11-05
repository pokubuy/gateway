package org.pokubuy.gateway.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.pokubuy.gateway.domain.entites.userservice.AuthData;
import org.pokubuy.gateway.domain.services.UserService;
import org.pokubuy.gateway.presentation.dto.requests.AuthenticateUserRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public final class UserServiceController {

    private final UserService userService;

    @PostMapping("/login")
    public Mono<AuthData> authenticateUser(
        @Valid @RequestBody AuthenticateUserRequest request
    ) {
        return userService.authenticateUser(
            request.email(),
            request.password()
        );
    }
}

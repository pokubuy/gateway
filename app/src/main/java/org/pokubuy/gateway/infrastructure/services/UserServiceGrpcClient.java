package org.pokubuy.gateway.infrastructure.services;

import lombok.RequiredArgsConstructor;
import org.pokubuy.gateway.domain.entites.userservice.AuthData;
import org.pokubuy.gateway.domain.services.UserService;
import org.pokubuy.userservice.grpc.AuthenticateUserRequest;
import org.pokubuy.userservice.grpc.ReactorUserServiceGrpc;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserServiceGrpcClient implements UserService {

    private final ReactorUserServiceGrpc.ReactorUserServiceStub userService;

    pu

    @Override
    public Mono<AuthData> authenticateUser(String email, String password) {
        return userService.authenticateUser(
            AuthenticateUserRequest.newBuilder()
                .setEmail(email)
                .setPassword(password)
                .build()
        ).map(message -> new AuthData(message.getAccessToken(), message.getRefreshToken()));
    }
}

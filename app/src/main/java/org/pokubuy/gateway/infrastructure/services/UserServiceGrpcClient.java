package org.pokubuy.gateway.infrastructure.services;

import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import org.pokubuy.gateway.infrastructure.exceptions.GrpcClientException;
import org.pokubuy.userservice.api.enitites.AuthData;
import org.pokubuy.userservice.api.enitites.UserInfo;
import org.pokubuy.userservice.api.enitites.enums.UserRole;
import org.pokubuy.userservice.api.mappers.AuthDataMapper;
import org.pokubuy.userservice.api.mappers.UserInfoMapper;
import org.pokubuy.userservice.api.mappers.UserRoleMapper;
import org.pokubuy.userservice.api.services.UserService;
import org.pokubuy.userservice.grpc.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceGrpcClient implements UserService {

    private final static String SERVICE_NAME = "pokubuy-user-service";

    private final ReactorUserServiceGrpc.ReactorUserServiceStub grpcUserService;
    private final UserRoleMapper userRoleMapper;
    private final UserInfoMapper userInfoMapper;
    private final AuthDataMapper authDataMapper;

    @Override
    public Mono<AuthData> createUser(String username, String email, String password, List<UserRole> roles) {
        return grpcUserService.createUser(
                CreateUserRequest.newBuilder()
                    .setUsername(username)
                    .setEmail(email)
                    .setPassword(password)
                    .addAllRoles(roles.stream().map(userRoleMapper::toGrpc).toList())
                    .build()
            ).map(authDataMapper::toDomain)
            .onErrorMap(this::mapException);
    }

    @Override
    public Mono<AuthData> authenticateUser(String email, String password) {
        return grpcUserService.authenticateUser(
                AuthenticateUserRequest.newBuilder()
                    .setEmail(email)
                    .setPassword(password)
                    .build()
            ).map(authDataMapper::toDomain)
            .onErrorMap(this::mapException);
    }

    @Override
    public Mono<String> renewToken(String refreshToken) {
        return grpcUserService.renewToken(
                RenewTokenRequest.newBuilder()
                    .setRefreshToken(refreshToken)
                    .build()
            ).map(RenewTokenResponse::getAccessToken)
            .onErrorMap(this::mapException);
    }

    @Override
    public Mono<UserInfo> getUser(UUID id) {
        return grpcUserService.getUser(
                GetUserRequest.newBuilder()
                    .setId(id.toString())
                    .build()
            ).map(userInfoMapper::toDomain)
            .onErrorMap(this::mapException);
    }

    private Throwable mapException(Throwable ex) {
        if (ex instanceof StatusRuntimeException statusRuntimeException) {
            return new GrpcClientException(SERVICE_NAME, statusRuntimeException);
        }
        return ex;
    }
}

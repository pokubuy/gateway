package org.pokubuy.gateway.domain.services;

import org.pokubuy.gateway.domain.entites.userservice.AuthData;
import reactor.core.publisher.Mono;

public interface UserService {

    Mono<AuthData> authenticateUser(String email, String password);
}

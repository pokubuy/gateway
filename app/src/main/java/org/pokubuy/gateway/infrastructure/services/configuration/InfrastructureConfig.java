package org.pokubuy.gateway.infrastructure.services.configuration;

import org.pokubuy.userservice.api.mappers.AuthDataMapper;
import org.pokubuy.userservice.api.mappers.UserInfoMapper;
import org.pokubuy.userservice.api.mappers.UserRoleMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfrastructureConfig {

    @Bean
    public UserRoleMapper userRoleMapper() {
        return new UserRoleMapper();
    }

    @Bean
    public UserInfoMapper userInfoMapper(UserRoleMapper userRoleMapper) {
        return new UserInfoMapper(userRoleMapper);
    }

    @Bean
    public AuthDataMapper authDataMapper() {
        return new AuthDataMapper();
    }
}

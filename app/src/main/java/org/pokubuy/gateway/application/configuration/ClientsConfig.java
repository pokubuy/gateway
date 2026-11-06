package org.pokubuy.gateway.application.configuration;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.RequiredArgsConstructor;
import org.pokubuy.gateway.application.configuration.configurationproperties.ClientsConfigurationProperties;
import org.pokubuy.userservice.grpc.ReactorUserServiceGrpc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ClientsConfig {

    private final ClientsConfigurationProperties configurationProperties;

    @Bean
    public ReactorUserServiceGrpc.ReactorUserServiceStub userServiceStub() {
        var userServiceInfo = configurationProperties.getUserService();
        ManagedChannel channel = ManagedChannelBuilder.forAddress(userServiceInfo.getHost(), userServiceInfo.getPort())
            .usePlaintext()
            .build();
        return ReactorUserServiceGrpc.newReactorStub(channel);
    }
}

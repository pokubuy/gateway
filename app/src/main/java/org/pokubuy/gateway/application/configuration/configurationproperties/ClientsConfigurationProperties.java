package org.pokubuy.gateway.application.configuration.configurationproperties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("clients")
@Component
@Data
public final class ClientsConfigurationProperties {

    private GrpcClientInfo userService;

    @Data
    public static class GrpcClientInfo {

        private String host;
        private int port;
    }
}

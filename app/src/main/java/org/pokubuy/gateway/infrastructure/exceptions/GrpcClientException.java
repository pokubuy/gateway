package org.pokubuy.gateway.infrastructure.exceptions;

import io.grpc.StatusRuntimeException;
import lombok.Getter;

@Getter
public final class GrpcClientException extends RuntimeException {

    private final String serviceName;
    private final StatusRuntimeException statusRuntimeException;

    public GrpcClientException(
        String serviceName,
        StatusRuntimeException statusRuntimeException
    ) {
        this.serviceName = serviceName;
        this.statusRuntimeException = statusRuntimeException;
        var msg = statusRuntimeException.getMessage();
        super(msg.substring(msg.indexOf(':') + 2));
    }
}

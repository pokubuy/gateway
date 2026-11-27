package org.pokubuy.gateway.infrastructure.exceptions;

import lombok.Getter;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Objects;

public final class HttpWebClientException extends RuntimeException {

    @Getter
    private final String serviceName;

    @Getter
    private final @Nullable HttpStatusCode statusCode;

    @Getter
    private final @Nullable String detail;

    public HttpWebClientException(String serviceName, Throwable cause) {
        super(cause);
        this.serviceName = serviceName;
        if (cause instanceof WebClientResponseException ex) {
            statusCode = ex.getStatusCode();
            detail = Objects.requireNonNull(ex.getResponseBodyAs(BackendErrorResponse.class)).detail();
        } else {
            statusCode = null;
            detail = null;
        }
    }
}

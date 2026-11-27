package org.pokubuy.gateway.application.exceptions;

import io.grpc.Status;
import lombok.extern.slf4j.Slf4j;
import org.pokubuy.gateway.infrastructure.exceptions.GrpcClientException;
import org.pokubuy.gateway.infrastructure.exceptions.HttpWebClientException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Mono;

import java.time.Instant;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @Value("${spring.application.name}")
    private String serviceName;

    @ExceptionHandler(GrpcClientException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleGrpcClientException(
        GrpcClientException ex
    ) {
        var status = mapHttpStatus(ex);
        if (status.is5xxServerError()) {
            log.error("Unknown error", ex);
        }
        return monoFromErrorResponse(
            mapHttpStatus(ex),
            new ErrorResponse(ex.getServiceName(), ex.getMessage(), Instant.now())
        );
    }

    private static HttpStatus mapHttpStatus(GrpcClientException ex) {
        var status = ex.getStatusRuntimeException().getStatus().getCode();
        return getHttpStatus(status);
    }

    private static HttpStatus getHttpStatus(Status.Code status) {
        return switch (status) {
            case INVALID_ARGUMENT, OUT_OF_RANGE -> HttpStatus.BAD_REQUEST;
            case DEADLINE_EXCEEDED -> HttpStatus.GATEWAY_TIMEOUT;
            case NOT_FOUND -> HttpStatus.NOT_FOUND;
            case ALREADY_EXISTS -> HttpStatus.CONFLICT;
            case PERMISSION_DENIED -> HttpStatus.FORBIDDEN;
            case RESOURCE_EXHAUSTED -> HttpStatus.TOO_MANY_REQUESTS;
            case FAILED_PRECONDITION -> HttpStatus.PRECONDITION_FAILED;
            case UNAVAILABLE -> HttpStatus.SERVICE_UNAVAILABLE;
            case UNAUTHENTICATED -> HttpStatus.UNAUTHORIZED;
            case UNIMPLEMENTED -> HttpStatus.NOT_IMPLEMENTED;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }

    @ExceptionHandler(HttpWebClientException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleHttpWebClientException(
        HttpWebClientException ex
    ) {
        var code = ex.getStatusCode();
        var message = ex.getMessage();
        if (code == null) {
            code = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        if (ex.getDetail() != null) {
            message = ex.getDetail();
        } else {
            message = "Internal server error";
            log.error("Unknown error", ex);
        }
        return Mono.just(ResponseEntity.status(code).body(new ErrorResponse(
            ex.getServiceName(),
            message,
            Instant.now()
        )));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ErrorResponse>> handleException(Exception ex) {
        log.error("Unknown error", ex);
        return monoFromException(HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }

    private Mono<ResponseEntity<ErrorResponse>> monoFromException(HttpStatus status, Exception ex) {
        return monoFromErrorResponse(status, new ErrorResponse(serviceName, ex.getMessage(), Instant.now()));
    }

    private Mono<ResponseEntity<ErrorResponse>> monoFromErrorResponse(
        HttpStatus status,
        ErrorResponse errorResponse
    ) {
        return Mono.just(ResponseEntity.status(status).body(errorResponse));
    }
}

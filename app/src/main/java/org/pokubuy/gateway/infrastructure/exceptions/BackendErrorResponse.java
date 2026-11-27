package org.pokubuy.gateway.infrastructure.exceptions;

import org.springframework.http.HttpStatusCode;

public record BackendErrorResponse(
    String detail,
    HttpStatusCode status,
    String title,
    String type
) {}

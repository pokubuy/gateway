package org.pokubuy.gateway.application.exceptions;

import java.time.Instant;

public record ErrorResponse(
    String serviceName,
    String message,
    Instant timestamp
) {}

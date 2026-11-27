package org.pokubuy.gateway.infrastructure.services.dto.listing;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ListingSummaryResponse(
    UUID id,
    String title,
    BigDecimal price,
    int executionDays,
    ListingStatus status,
    Instant publishedAt
) {}
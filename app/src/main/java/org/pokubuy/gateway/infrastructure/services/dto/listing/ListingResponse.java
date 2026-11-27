package org.pokubuy.gateway.infrastructure.services.dto.listing;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ListingResponse(
    UUID id,
    UUID sellerId,
    UUID categoryId,
    String title,
    String description,
    BigDecimal price,
    String currency,
    int executionDays,
    int quantityAvailable,
    ListingStatus status,
    String rejectionReason,
    Instant publishedAt,
    Instant createdAt,
    Instant updatedAt
) {}

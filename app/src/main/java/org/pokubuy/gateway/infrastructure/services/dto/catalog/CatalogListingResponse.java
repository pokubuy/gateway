package org.pokubuy.gateway.infrastructure.services.dto.catalog;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record CatalogListingResponse(
    UUID id,
    UUID sellerId,
    UUID categoryId,
    String categoryName,
    String title,
    String description,
    BigDecimal price,
    String currency,
    int executionDays,
    int quantityAvailable,
    Instant publishedAt
) {}
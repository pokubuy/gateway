package org.pokubuy.gateway.infrastructure.services.dto.orders;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record OrderResponse(
    UUID id,
    UUID listingId,
    UUID buyerId,
    UUID sellerId,
    OrderStatus status,
    BigDecimal totalAmount,
    Instant createdAt
) {}

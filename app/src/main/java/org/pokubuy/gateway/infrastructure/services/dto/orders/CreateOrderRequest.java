package org.pokubuy.gateway.infrastructure.services.dto.orders;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateOrderRequest(@NotNull UUID listingId) {}

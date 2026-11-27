package org.pokubuy.gateway.presentation.responses;

import java.util.UUID;

public record GetSellerNameResponse(
    UUID sellerId,
    String name
) {}

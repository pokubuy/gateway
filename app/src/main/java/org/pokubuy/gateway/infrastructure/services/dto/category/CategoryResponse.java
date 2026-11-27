package org.pokubuy.gateway.infrastructure.services.dto.category;

import java.util.UUID;

public record CategoryResponse(
    UUID id,
    UUID parentId,
    String name
) {}

package org.pokubuy.gateway.infrastructure.services.dto.common;

public record PageData(
    int size,
    int number,
    int totalElements,
    int totalPages
) {}

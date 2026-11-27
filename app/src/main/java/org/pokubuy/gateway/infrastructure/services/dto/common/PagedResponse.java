package org.pokubuy.gateway.infrastructure.services.dto.common;

import java.util.List;

public record PagedResponse<T>(List<T> content, PageData page) {}

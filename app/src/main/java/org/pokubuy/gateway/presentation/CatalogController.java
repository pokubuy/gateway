package org.pokubuy.gateway.presentation;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.pokubuy.gateway.infrastructure.services.CatalogServiceWebClient;
import org.pokubuy.gateway.infrastructure.services.dto.catalog.CatalogListingResponse;
import org.pokubuy.gateway.infrastructure.services.dto.common.PagedResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/catalog/listings")
@RequiredArgsConstructor
public class CatalogController {

    private final CatalogServiceWebClient webClient;

    @GetMapping
    public Mono<PagedResponse<CatalogListingResponse>> search(
        @RequestParam(required = false) @Nullable UUID categoryId,
        @RequestParam(required = false) @Nullable BigDecimal minPrice,
        @RequestParam(required = false) @Nullable BigDecimal maxPrice,
        @RequestParam(required = false) @Nullable Integer minExecutionDays,
        @RequestParam(required = false) @Nullable Integer maxExecutionDays,
        @RequestParam(required = false) @Nullable String q,
        @RequestParam(required = false) @Nullable Integer page,
        @RequestParam(required = false) @Nullable Integer size,
        @RequestParam(required = false) @Nullable String sort
    ) {
        return webClient.search(
            categoryId,
            minPrice,
            maxPrice,
            minExecutionDays,
            maxExecutionDays,
            q,
            page,
            size,
            sort
        );
    }

    @GetMapping("/{catalogId}")
    public Mono<CatalogListingResponse> getById(@PathVariable @NotNull UUID catalogId) {
        return webClient.getById(catalogId);
    }
}

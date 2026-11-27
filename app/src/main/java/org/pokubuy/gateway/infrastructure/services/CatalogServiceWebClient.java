package org.pokubuy.gateway.infrastructure.services;

import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.pokubuy.gateway.infrastructure.exceptions.HttpWebClientException;
import org.pokubuy.gateway.infrastructure.services.dto.catalog.CatalogListingResponse;
import org.pokubuy.gateway.infrastructure.services.dto.common.PagedResponse;
import org.pokubuy.gateway.infrastructure.services.utils.HttpWebClientUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public final class CatalogServiceWebClient {

    private final static String SERVICE_NAME = "pokubuy-backend/listing-service";

    private final WebClient webClient;
    @Value("${clients.backend.url}")
    private String baseUrl;

    @PostConstruct
    public void init() {
        this.baseUrl = this.baseUrl + "/api/v1/catalog/listings";
    }

    @GetMapping
    public Mono<PagedResponse<CatalogListingResponse>> search(
        @Nullable UUID categoryId,
        @Nullable BigDecimal minPrice,
        @Nullable BigDecimal maxPrice,
        @Nullable Integer minExecutionDays,
        @Nullable Integer maxExecutionDays,
        @Nullable String q,
        @Nullable Integer page,
        @Nullable Integer size,
        @Nullable String sort
    ) {
        URI uri = UriComponentsBuilder.fromUriString(this.baseUrl)
            .queryParamIfPresent("categoryId", Optional.ofNullable(categoryId))
            .queryParamIfPresent("minPrice", Optional.ofNullable(minPrice))
            .queryParamIfPresent("maxPrice", Optional.ofNullable(maxPrice))
            .queryParamIfPresent("minExecutionDays", Optional.ofNullable(minExecutionDays))
            .queryParamIfPresent("maxExecutionDays", Optional.ofNullable(maxExecutionDays))
            .queryParamIfPresent("q", Optional.ofNullable(q))
            .queryParamIfPresent("page", Optional.ofNullable(page))
            .queryParamIfPresent("size", Optional.ofNullable(size))
            .queryParamIfPresent("sort", Optional.ofNullable(sort))
            .build()
            .encode()
            .toUri();

        return webClient.get().uri(uri)
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<PagedResponse<CatalogListingResponse>>() {})
            .onErrorMap(this::mapException);
    }

    public Mono<CatalogListingResponse> getById(UUID catalogId) {
        return webClient.get().uri(baseUrl + "/{catalogId}", catalogId)
            .retrieve()
            .bodyToMono(CatalogListingResponse.class)
            .onErrorMap(this::mapException);
    }

    private HttpWebClientException mapException(Throwable ex) {
        return HttpWebClientUtils.mapException(SERVICE_NAME, ex);
    }
}
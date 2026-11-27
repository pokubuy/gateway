package org.pokubuy.gateway.infrastructure.services;

import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.pokubuy.gateway.infrastructure.exceptions.HttpWebClientException;
import org.pokubuy.gateway.infrastructure.services.dto.category.CategoryResponse;
import org.pokubuy.gateway.infrastructure.services.dto.common.PagedResponse;
import org.pokubuy.gateway.infrastructure.services.utils.HttpWebClientUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public final class CategoryServiceWebClient {

    private final static String SERVICE_NAME = "pokubuy-backend/category-service";

    private final WebClient webClient;
    @Value("${clients.backend.url}")
    private String baseUrl;

    @PostConstruct
    public void init() {
        this.baseUrl = this.baseUrl + "/api/v1/categories";
    }

    public Mono<PagedResponse<CategoryResponse>> getCategories(
        @Nullable Integer page,
        @Nullable Integer size,
        @Nullable String sort
    ) {
        URI uri = UriComponentsBuilder.fromUriString(this.baseUrl)
            .queryParamIfPresent("page", Optional.ofNullable(page))
            .queryParamIfPresent("size", Optional.ofNullable(size))
            .queryParamIfPresent("sort", Optional.ofNullable(sort))
            .build()
            .encode()
            .toUri();

        return webClient.get().uri(uri)
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<PagedResponse<CategoryResponse>>() {})
            .onErrorMap(this::mapException);
    }

    public Mono<CategoryResponse> getCategory(UUID categoryId) {
        return webClient.get().uri(baseUrl + "/{categoryId}", categoryId)
            .retrieve()
            .bodyToMono(CategoryResponse.class)
            .onErrorMap(this::mapException);
    }

    private HttpWebClientException mapException(Throwable ex) {
        return HttpWebClientUtils.mapException(SERVICE_NAME, ex);
    }
}
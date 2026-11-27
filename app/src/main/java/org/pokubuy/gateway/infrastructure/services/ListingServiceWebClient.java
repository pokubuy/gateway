package org.pokubuy.gateway.infrastructure.services;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.pokubuy.gateway.infrastructure.exceptions.HttpWebClientException;
import org.pokubuy.gateway.infrastructure.services.dto.listing.CreateListingRequest;
import org.pokubuy.gateway.infrastructure.services.dto.listing.ListingResponse;
import org.pokubuy.gateway.infrastructure.services.dto.listing.ListingSummaryResponse;
import org.pokubuy.gateway.infrastructure.services.dto.listing.UpdateListingRequest;
import org.pokubuy.gateway.infrastructure.services.utils.HttpWebClientUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public final class ListingServiceWebClient {

    private final static String SERVICE_NAME = "pokubuy-backend/listing-service";

    private final WebClient webClient;
    @Value("${clients.backend.url}")
    private String baseUrl;

    @PostConstruct
    public void init() {
        this.baseUrl = this.baseUrl + "/api/v1/seller/{sellerId}/listings";
    }

    public Mono<ListingResponse> create(UUID sellerId, CreateListingRequest request) {
        return webClient.post()
            .uri(this.baseUrl, sellerId)
            .bodyValue(request)
            .retrieve()
            .bodyToMono(ListingResponse.class)
            .onErrorMap(this::mapException);
    }

    public Flux<ListingSummaryResponse> list(UUID sellerId) {
        return webClient.get()
            .uri(this.baseUrl, sellerId)
            .retrieve()
            .bodyToFlux(ListingSummaryResponse.class)
            .onErrorMap(this::mapException);
    }

    public Mono<ListingResponse> get(UUID sellerId, UUID listingId) {
        return webClient.get()
            .uri(this.baseUrl + "/{id}", sellerId, listingId)
            .retrieve()
            .bodyToMono(ListingResponse.class)
            .onErrorMap(this::mapException);
    }

    public Mono<ListingResponse> update(
        UUID sellerId,
        UUID listingId,
        UpdateListingRequest request
    ) {
        return webClient.put()
            .uri(this.baseUrl + "/{id}", sellerId, listingId)
            .bodyValue(request)
            .retrieve()
            .bodyToMono(ListingResponse.class)
            .onErrorMap(this::mapException);
    }

    public Mono<ListingResponse> submit(UUID sellerId, UUID listingId) {
        return webClient.post()
            .uri(this.baseUrl + "/{id}/submit", sellerId, listingId)
            .retrieve()
            .bodyToMono(ListingResponse.class)
            .onErrorMap(this::mapException);
    }

    public Mono<ListingResponse> pause(UUID sellerId, UUID listingId) {
        return webClient.post()
            .uri(this.baseUrl + "/{id}/pause", sellerId, listingId)
            .retrieve()
            .bodyToMono(ListingResponse.class)
            .onErrorMap(this::mapException);
    }

    public Mono<ListingResponse> resume(UUID sellerId, UUID listingId) {
        return webClient.post()
            .uri(this.baseUrl + "/{id}/resume", sellerId, listingId)
            .retrieve()
            .bodyToMono(ListingResponse.class)
            .onErrorMap(this::mapException);
    }

    public Mono<Void> archive(UUID sellerId, UUID listingId) {
        return webClient.post()
            .uri(this.baseUrl + "/{id}/archive", sellerId, listingId)
            .retrieve()
            .toBodilessEntity()
            .onErrorMap(this::mapException)
            .then();
    }

    private HttpWebClientException mapException(Throwable ex) {
        return HttpWebClientUtils.mapException(SERVICE_NAME, ex);
    }
}

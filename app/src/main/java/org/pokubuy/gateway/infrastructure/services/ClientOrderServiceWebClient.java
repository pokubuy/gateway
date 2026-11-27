package org.pokubuy.gateway.infrastructure.services;

import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.pokubuy.gateway.infrastructure.exceptions.HttpWebClientException;
import org.pokubuy.gateway.infrastructure.services.dto.common.PagedResponse;
import org.pokubuy.gateway.infrastructure.services.dto.orders.CreateOrderRequest;
import org.pokubuy.gateway.infrastructure.services.dto.orders.OrderResponse;
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
public final class ClientOrderServiceWebClient {

    private final static String SERVICE_NAME = "pokubuy-backend/order-service";

    private final WebClient webClient;
    @Value("${clients.backend.url}")
    private String baseUrl;

    @PostConstruct
    public void init() {
        this.baseUrl = this.baseUrl + "/api/v1/user/{clientId}/orders";
    }

    public Mono<OrderResponse> create(UUID clientId, CreateOrderRequest request) {
        return webClient.post().uri(baseUrl, clientId)
            .bodyValue(request)
            .retrieve()
            .bodyToMono(OrderResponse.class)
            .onErrorMap(this::mapException);
    }

    public Mono<OrderResponse> pay(UUID clientId, UUID orderId) {
        return webClient.post().uri(baseUrl + "/{orderId}/pay", clientId, orderId)
            .retrieve()
            .bodyToMono(OrderResponse.class)
            .onErrorMap(this::mapException);
    }

    public Mono<OrderResponse> confirm(UUID clientId, UUID orderId) {
        return webClient.post().uri(baseUrl + "/{orderId}/confirm", clientId, orderId)
            .retrieve()
            .bodyToMono(OrderResponse.class)
            .onErrorMap(this::mapException);
    }

    public Mono<PagedResponse<OrderResponse>> getClientOrders(
        UUID clientId,
        @Nullable Integer page,
        @Nullable Integer size,
        @Nullable String sort
    ) {
        URI uri = UriComponentsBuilder.fromUriString(this.baseUrl)
            .queryParamIfPresent("page", Optional.ofNullable(page))
            .queryParamIfPresent("size", Optional.ofNullable(size))
            .queryParamIfPresent("sort", Optional.ofNullable(sort))
            .buildAndExpand(clientId)
            .encode()
            .toUri();

        return webClient.get()
            .uri(uri)
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<PagedResponse<OrderResponse>>() {})
            .onErrorMap(this::mapException);
    }

    private HttpWebClientException mapException(Throwable ex) {
        return HttpWebClientUtils.mapException(SERVICE_NAME, ex);
    }
}

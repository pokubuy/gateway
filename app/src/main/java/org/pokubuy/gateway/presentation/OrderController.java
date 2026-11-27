package org.pokubuy.gateway.presentation;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.pokubuy.gateway.infrastructure.services.ClientOrderServiceWebClient;
import org.pokubuy.gateway.infrastructure.services.SellerOrderServiceWebClient;
import org.pokubuy.gateway.infrastructure.services.dto.common.PagedResponse;
import org.pokubuy.gateway.infrastructure.services.dto.orders.CreateOrderRequest;
import org.pokubuy.gateway.infrastructure.services.dto.orders.OrderResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.pokubuy.gateway.presentation.utils.AuthUtils.extractUserId;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final ClientOrderServiceWebClient clientWebClient;
    private final SellerOrderServiceWebClient sellerWebClient;

    @PostMapping
    public Mono<OrderResponse> create(
        @AuthenticationPrincipal Jwt jwt,
        @Valid @RequestBody CreateOrderRequest request
    ) {
        return clientWebClient.create(extractUserId(jwt), request);
    }

    @PostMapping("/{orderId}/pay")
    public Mono<OrderResponse> pay(
        @AuthenticationPrincipal Jwt jwt,
        @PathVariable @NotNull UUID orderId
    ) {
        return clientWebClient.pay(extractUserId(jwt), orderId);
    }

    @PostMapping("/{orderId}/confirm")
    public Mono<OrderResponse> confirm(
        @AuthenticationPrincipal Jwt jwt,
        @PathVariable UUID orderId
    ) {
        return clientWebClient.confirm(extractUserId(jwt), orderId);
    }

    @PostMapping("/{orderId}/accept")
    @PreAuthorize("hasAnyRole('SELLER', 'ADMIN', 'SUPPORT')")
    public Mono<OrderResponse> accept(
        @AuthenticationPrincipal Jwt jwt,
        @PathVariable UUID orderId
    ) {
        return sellerWebClient.accept(extractUserId(jwt), orderId);
    }

    @PostMapping("/{orderId}/complete")
    @PreAuthorize("hasAnyRole('SELLER', 'ADMIN', 'SUPPORT')")
    public Mono<OrderResponse> complete(
        @AuthenticationPrincipal Jwt jwt,
        @PathVariable UUID orderId
    ) {
        return sellerWebClient.complete(extractUserId(jwt), orderId);
    }

    @GetMapping("/outgoing")
    public Mono<PagedResponse<OrderResponse>> getClientOrders(
        @AuthenticationPrincipal Jwt jwt,
        @RequestParam(required = false) @Nullable Integer page,
        @RequestParam(required = false) @Nullable Integer size,
        @RequestParam(required = false) @Nullable String sort
    ) {
        return clientWebClient.getClientOrders(extractUserId(jwt), page, size, sort);
    }

    @GetMapping("/incoming")
    @PreAuthorize("hasRole('SELLER')")
    public Mono<PagedResponse<OrderResponse>> getSellerOrders(
        @AuthenticationPrincipal Jwt jwt,
        @RequestParam(required = false) @Nullable Integer page,
        @RequestParam(required = false) @Nullable Integer size,
        @RequestParam(required = false) @Nullable String sort
    ) {
        return sellerWebClient.getSellerOrders(extractUserId(jwt), page, size, sort);
    }
}

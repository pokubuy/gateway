package org.pokubuy.gateway.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.pokubuy.gateway.infrastructure.services.ListingServiceWebClient;
import org.pokubuy.gateway.infrastructure.services.dto.listing.CreateListingRequest;
import org.pokubuy.gateway.infrastructure.services.dto.listing.ListingResponse;
import org.pokubuy.gateway.infrastructure.services.dto.listing.ListingSummaryResponse;
import org.pokubuy.gateway.infrastructure.services.dto.listing.UpdateListingRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.pokubuy.gateway.presentation.utils.AuthUtils.extractUserId;

@RestController
@RequestMapping("/seller/listings")
@RequiredArgsConstructor
public class SellerListingController {

    private final ListingServiceWebClient listingService;

    @PostMapping
    public Mono<ListingResponse> create(
        @AuthenticationPrincipal Jwt jwt,
        CreateListingRequest request
    ) {
        return listingService.create(extractUserId(jwt), request);
    }

    @GetMapping
    public Flux<ListingSummaryResponse> list(@AuthenticationPrincipal Jwt jwt) {
        return listingService.list(extractUserId(jwt));
    }

    @GetMapping("/{listingId}")
    public Mono<ListingResponse> get(
        @AuthenticationPrincipal Jwt jwt,
        @PathVariable UUID listingId
    ) {
        return listingService.get(extractUserId(jwt), listingId);
    }

    @PutMapping("/{listingId}")
    public Mono<ListingResponse> update(
        @AuthenticationPrincipal Jwt jwt,
        @PathVariable UUID listingId,
        @Valid @RequestBody UpdateListingRequest request
    ) {
        return listingService.update(extractUserId(jwt), listingId, request);
    }

    @PostMapping("/{listingId}/submit")
    public Mono<ListingResponse> submit(
        @AuthenticationPrincipal Jwt jwt,
        @PathVariable UUID listingId
    ) {
        return listingService.submit(extractUserId(jwt), listingId);
    }

    @PostMapping("/{listingId}/pause")
    public Mono<ListingResponse> pause(
        @AuthenticationPrincipal Jwt jwt,
        @PathVariable UUID listingId
    ) {
        return listingService.pause(extractUserId(jwt), listingId);
    }

    @PostMapping("/{listingId}/resume")
    public Mono<ListingResponse> resume(
        @AuthenticationPrincipal Jwt jwt,
        @PathVariable UUID listingId
    ) {
        return listingService.resume(extractUserId(jwt), listingId);
    }

    @PostMapping("/{listingId}/archive")
    public Mono<Void> archive(
        @AuthenticationPrincipal Jwt jwt,
        @PathVariable UUID listingId
    ) {
        return listingService.archive(extractUserId(jwt), listingId);
    }
}

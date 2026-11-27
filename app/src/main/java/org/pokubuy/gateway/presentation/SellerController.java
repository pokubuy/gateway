package org.pokubuy.gateway.presentation;

import lombok.RequiredArgsConstructor;
import org.pokubuy.gateway.presentation.responses.GetSellerNameResponse;
import org.pokubuy.userservice.api.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/sellers")
@RequiredArgsConstructor
public class SellerController {

    private final UserService userService;

    @GetMapping("/{sellerId}/name")
    public Mono<GetSellerNameResponse> getSellerUsername(@PathVariable UUID sellerId) {
        return userService.getUser(sellerId)
            .map(seller -> new GetSellerNameResponse(sellerId, seller.username()));
    }
}

package org.pokubuy.gateway.presentation;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.pokubuy.gateway.infrastructure.services.CategoryServiceWebClient;
import org.pokubuy.gateway.infrastructure.services.dto.category.CategoryResponse;
import org.pokubuy.gateway.infrastructure.services.dto.common.PagedResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryServiceWebClient webClient;

    @GetMapping
    public Mono<PagedResponse<CategoryResponse>> getCategories(
        @RequestParam(required = false) @Nullable Integer page,
        @RequestParam(required = false) @Nullable Integer size,
        @RequestParam(required = false) @Nullable String sort
    ) {

        return webClient.getCategories(page, size, sort);
    }

    @GetMapping("/{categoryId}")
    public Mono<CategoryResponse> getCategory(@PathVariable UUID categoryId) {
        return webClient.getCategory(categoryId);
    }
}

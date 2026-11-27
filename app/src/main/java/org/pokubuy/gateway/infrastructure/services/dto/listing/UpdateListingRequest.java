package org.pokubuy.gateway.infrastructure.services.dto.listing;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.UUID;

public record UpdateListingRequest(
    @NotNull UUID categoryId,
    @NotBlank @Size(max = 200) String title,
    @NotBlank @Size(max = 10_000) String description,
    @NotNull @DecimalMin(value = "0.01") @Digits(integer = 10, fraction = 2) BigDecimal price,
    @Min(1) @Max(365) int executionDays,
    @Min(0) @Max(999_999) int quantityAvailable
) {}
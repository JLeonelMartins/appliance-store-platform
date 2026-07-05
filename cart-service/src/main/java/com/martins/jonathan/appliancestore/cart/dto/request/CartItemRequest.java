package com.martins.jonathan.appliancestore.cart.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CartItemRequest(

        @Schema(
                description = "Identifier of the product to add",
                example = "2")
        @NotNull(message = "Product id is required")
        @Positive(message = "Product id must be greater than zero")
        Long productId,

        @Schema(
                description = "Number of product units",
                example = "2")
        @NotNull(message = "Quantity is required")
        @Positive(message = "Quantity must be greater than zero")
        Integer quantity

) {
}
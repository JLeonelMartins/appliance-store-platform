package com.martins.jonathan.appliancestore.sales.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record SaleRequest(

        @Schema(
                description = "Identifier of the cart that will be converted into a sale",
                example = "1"
        )
        @NotNull(message = "Cart id is required")
        @Positive(message = "Cart id must be greater than zero")
        Long cartId

) {
}
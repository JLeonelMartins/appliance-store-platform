package com.martins.jonathan.appliancestore.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductRequest(

        @Schema(description = "Unique product code", example = "TV-001")
        @NotBlank(message = "Product code is required")
        @Size(max = 50)
        String code,

        @Schema(description = "Product name", example = "Smart TV 55 4K")
        @NotBlank(message = "Product name is required")
        @Size(max = 120)
        String name,

        @Schema(description = "Product brand", example = "Samsung")
        @NotBlank(message = "Product brand is required")
        @Size(max = 80)
        String brand,

        @Schema(description = "Individual product price", example = "850000.00")
        @NotNull(message = "Product price is required")
        @Positive(message = "Product price must be greater than zero")
        @Digits(
                integer = 10,
                fraction = 2,
                message = "Product price must have up to 10 integer digits and 2 decimal places"
        )
        BigDecimal price

) {
}

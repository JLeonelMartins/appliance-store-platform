package com.martins.jonathan.appliancestore.sales.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SaleResponse(

        @Schema(
                description = "Unique identifier of the sale",
                example = "1"
        )
        Long id,

        @Schema(
                description = "Identifier of the cart associated with the sale",
                example = "3"
        )
        Long cartId,

        @Schema(
                description = "Date and time when the sale was registered",
                example = "2026-06-30T15:30:00"
        )
        LocalDateTime saleDate,

        @Schema(
                description = "Final sale total obtained from the cart",
                example = "1700000.00"
        )
        BigDecimal total

) {
}
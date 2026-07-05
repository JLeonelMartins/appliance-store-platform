package com.martins.jonathan.appliancestore.sales.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record SaleRequest(

        @NotNull(message = "Cart id is required")
        @Positive(message = "Cart id must be greater than zero")
        Long cartId,

        @NotBlank(message = "Customer email is required")
        @Email(message = "Customer email must be valid")
        String customerEmail

) {
}
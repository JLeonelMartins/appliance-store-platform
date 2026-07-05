package com.martins.jonathan.appliancestore.cart.product.dto;

import java.math.BigDecimal;

public record ProductClientResponse(
        Long id,
        String code,
        String name,
        String brand,
        BigDecimal price
) {
}

package com.martins.jonathan.appliancestore.product.dto;

import java.math.BigDecimal;

public record ProductResponse(

        Long id,
        String code,
        String name,
        String brand,
        BigDecimal price

) {
}

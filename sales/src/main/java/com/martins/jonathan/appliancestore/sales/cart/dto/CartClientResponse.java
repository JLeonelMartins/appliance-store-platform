package com.martins.jonathan.appliancestore.sales.cart.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CartClientResponse(
        Long id,
        BigDecimal total
) {
}
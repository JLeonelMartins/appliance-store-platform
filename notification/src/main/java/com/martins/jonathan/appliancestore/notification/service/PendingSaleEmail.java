package com.martins.jonathan.appliancestore.notification.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PendingSaleEmail(
        String recipient,
        Long saleId,
        Long cartId,
        BigDecimal total,
        LocalDateTime saleDate
) {
}
package com.martins.jonathan.appliancestore.notification.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "sale_notifications", uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_sale_notification_event_id",
                        columnNames = "event_id"
                )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SaleNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id", nullable = false, updatable = false,length = 36)
    private String eventId;

    @Column(name = "sale_id", nullable = false)
    private Long saleId;

    @Column(name = "cart_id", nullable = false)
    private Long cartId;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal total;

    @Column(name = "sale_date", nullable = false)
    private LocalDateTime saleDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private NotificationStatus status;

    @Column(nullable = false)
    private Integer attempts;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Column(name = "last_error", length = 1000)
    private String lastError;

    @Column(nullable = false, length = 255)
    private String recipient;

    public SaleNotification(
            String eventId,
            Long saleId,
            Long cartId,
            BigDecimal total,
            LocalDateTime saleDate,
            String recipient
    ) {
        this.eventId = eventId;
        this.saleId = saleId;
        this.cartId = cartId;
        this.total = total;
        this.saleDate = saleDate;
        this.recipient = recipient;
        this.status = NotificationStatus.PENDING;
        this.attempts = 0;
        this.createdAt = LocalDateTime.now();
    }
    public void registerAttempt() {
        this.attempts++;
        this.status = NotificationStatus.PENDING;
    }

    public void markAsSent() {
        this.status = NotificationStatus.SENT;
        this.sentAt = LocalDateTime.now();
        this.lastError = null;
    }

    public void markAsFailed(String errorMessage) {
        this.status = NotificationStatus.FAILED;
        this.lastError = limitErrorMessage(errorMessage);
    }

    private String limitErrorMessage(String errorMessage) {

        if (errorMessage == null || errorMessage.isBlank()) {
            return "Unknown email delivery error";
        }

        return errorMessage.substring(0, Math.min(errorMessage.length(), 1000));
    }
}
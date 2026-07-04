package com.martins.jonathan.appliancestore.notification.service;

import com.martins.jonathan.appliancestore.notification.event.SaleCreatedEvent;
import com.martins.jonathan.appliancestore.notification.model.NotificationStatus;
import com.martins.jonathan.appliancestore.notification.model.SaleNotification;
import com.martins.jonathan.appliancestore.notification.repository.ISaleNotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SaleNotificationService {

    private final ISaleNotificationRepository notificationRepository;

    public SaleNotificationService(
            ISaleNotificationRepository notificationRepository
    ) {
        this.notificationRepository = notificationRepository;
    }

    @Transactional
    public NotificationRegistrationResult registerOrFind(String eventId, SaleCreatedEvent event) {

        return notificationRepository
                .findByEventId(eventId)
                .map(existingNotification ->
                        new NotificationRegistrationResult(
                                existingNotification.getId(),
                                existingNotification.getStatus()
                                        != NotificationStatus.SENT
                        )
                )
                .orElseGet(() -> createNotification(eventId, event));
    }

    private NotificationRegistrationResult createNotification(String eventId, SaleCreatedEvent event) {

        SaleNotification notification = new SaleNotification(
                        eventId,
                        event.saleId(),
                        event.cartId(),
                        event.total(),
                        event.saleDate(),
                        event.customerEmail()
                );

        SaleNotification savedNotification = notificationRepository.save(notification);

        return new NotificationRegistrationResult(savedNotification.getId(),true);
    }

    @Transactional
    public PendingSaleEmail prepareAttempt(Long notificationId) {

        SaleNotification notification = findNotification(notificationId);

        notification.registerAttempt();

        return new PendingSaleEmail(
                notification.getRecipient(),
                notification.getSaleId(),
                notification.getCartId(),
                notification.getTotal(),
                notification.getSaleDate()
        );
    }

    @Transactional
    public void markAsSent(Long notificationId) {

        SaleNotification notification =  findNotification(notificationId);

        notification.markAsSent();
    }

    @Transactional
    public void markAsFailed(Long notificationId, String errorMessage) {

        SaleNotification notification = findNotification(notificationId);

        notification.markAsFailed(errorMessage);
    }

    private SaleNotification findNotification(Long notificationId) {
        return notificationRepository
                .findById(notificationId)
                .orElseThrow(() ->
                        new IllegalStateException("Notification not found with id: " + notificationId)
                );
    }
}
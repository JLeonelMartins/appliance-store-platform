package com.martins.jonathan.appliancestore.notification.service;

import com.martins.jonathan.appliancestore.notification.email.EmailGateway;
import com.martins.jonathan.appliancestore.notification.email.EmailMessage;
import org.springframework.stereotype.Service;

@Service
public class SaleEmailProcessor {

    private final SaleNotificationService notificationService;
    private final EmailGateway emailGateway;

    public SaleEmailProcessor(SaleNotificationService notificationService, EmailGateway emailGateway) {
        this.notificationService = notificationService;
        this.emailGateway = emailGateway;
    }

    public void process(Long notificationId) {

        PendingSaleEmail pendingEmail = notificationService.prepareAttempt(notificationId);

        EmailMessage emailMessage =
                new EmailMessage(
                        pendingEmail.recipient(),
                        "Sale confirmation #"
                                + pendingEmail.saleId(),
                        buildBody(pendingEmail)
                );

        try {
            emailGateway.send(emailMessage);

            notificationService.markAsSent(notificationId);

        } catch (RuntimeException exception) {

            notificationService.markAsFailed(notificationId, exception.getMessage());

            throw exception;
        }
    }

    private String buildBody(PendingSaleEmail pendingEmail) {

        return """
                Thank you for your purchase.

                Sale ID: %d
                Cart ID: %d
                Total: %s
                Date: %s

                Appliance Store
                """.formatted(
                pendingEmail.saleId(),
                pendingEmail.cartId(),
                pendingEmail.total(),
                pendingEmail.saleDate()
        );
    }
}
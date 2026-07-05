package com.martins.jonathan.appliancestore.notification.email;

public record EmailMessage(
        String recipient,
        String subject,
        String body
) {
}
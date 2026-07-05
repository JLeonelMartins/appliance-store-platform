package com.martins.jonathan.appliancestore.notification.service;

public record NotificationRegistrationResult(
        Long notificationId,
        boolean shouldProcess
) {
}
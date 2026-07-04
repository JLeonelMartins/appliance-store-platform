package com.martins.jonathan.appliancestore.notification.email;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.notification.email")
public record NotificationEmailProperties(String from) {
}
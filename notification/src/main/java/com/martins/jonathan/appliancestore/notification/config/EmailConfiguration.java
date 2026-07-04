package com.martins.jonathan.appliancestore.notification.config;

import com.martins.jonathan.appliancestore.notification.email.NotificationEmailProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(NotificationEmailProperties.class)
public class EmailConfiguration {
}
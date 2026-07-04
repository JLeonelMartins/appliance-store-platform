package com.martins.jonathan.appliancestore.notification.kafka;

import com.martins.jonathan.appliancestore.notification.event.SaleCreatedEvent;
import com.martins.jonathan.appliancestore.notification.service.NotificationRegistrationResult;
import com.martins.jonathan.appliancestore.notification.service.SaleEmailProcessor;
import com.martins.jonathan.appliancestore.notification.service.SaleNotificationService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class SaleCreatedConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SaleCreatedConsumer.class);

    private final SaleNotificationService notificationService;
    private final SaleEmailProcessor emailProcessor;

    public SaleCreatedConsumer(SaleNotificationService notificationService, SaleEmailProcessor emailProcessor) {
        this.notificationService = notificationService;
        this.emailProcessor = emailProcessor;
    }

    @KafkaListener(topics = "${app.kafka.topics.sale-created}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(ConsumerRecord<String, SaleCreatedEvent> record) {

        SaleCreatedEvent event = record.value();

        String eventId = extractEventId(record);

        LOGGER.info(
                "SaleCreatedEvent received. eventId={}, saleId={}, cartId={}, customerEmail={}",
                eventId,
                event.saleId(),
                event.cartId(),
                event.customerEmail()
        );

        NotificationRegistrationResult result = notificationService.registerOrFind(
                        eventId,
                        event
                );

        if (!result.shouldProcess()) {

            LOGGER.info(
                    "Notification already sent. eventId={}, saleId={}",
                    eventId,
                    event.saleId()
            );

            return;
        }

        emailProcessor.process(result.notificationId()
        );

        LOGGER.info(
                "Sale email sent. eventId={}, saleId={}, customerEmail={}",
                eventId,
                event.saleId(),
                event.customerEmail()
        );
    }

    private String extractEventId(ConsumerRecord<String, SaleCreatedEvent> record){

        Header eventIdHeader = record.headers().lastHeader("id");

        if (eventIdHeader == null) {
            throw new IllegalStateException(
                    "Kafka message does not contain Debezium outbox event id header"
            );
        }

        return new String(eventIdHeader.value(), StandardCharsets.UTF_8
        );
    }
}
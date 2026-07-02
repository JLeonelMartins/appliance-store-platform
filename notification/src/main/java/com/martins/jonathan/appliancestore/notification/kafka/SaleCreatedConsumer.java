package com.martins.jonathan.appliancestore.notification.kafka;

import com.martins.jonathan.appliancestore.notification.event.SaleCreatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class SaleCreatedConsumer {

    @KafkaListener(topics = "sale-created")
    public void consume(SaleCreatedEvent event) {

        System.out.println(
                """

                =====================================
                NEW SALE NOTIFICATION
                Sale ID: %d
                Cart ID: %d
                Total: %s
                Date: %s
                =====================================

                """.formatted(
                        event.saleId(),
                        event.cartId(),
                        event.total(),
                        event.saleDate()
                )
        );
    }
}
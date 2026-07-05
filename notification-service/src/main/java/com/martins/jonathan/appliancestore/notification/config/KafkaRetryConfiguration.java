package com.martins.jonathan.appliancestore.notification.config;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaRetryConfiguration {

    private static final long RETRY_INTERVAL_IN_MILLISECONDS = 5000L;

    private static final long MAX_RETRY_ATTEMPTS = 3L;

    @Bean
    public DefaultErrorHandler defaultErrorHandler(KafkaTemplate<Object, Object> kafkaTemplate) {

        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(kafkaTemplate,
                this::resolveDeadLetterTopic
                );

        FixedBackOff fixedBackOff = new FixedBackOff(RETRY_INTERVAL_IN_MILLISECONDS,  MAX_RETRY_ATTEMPTS);

        return new DefaultErrorHandler(recoverer, fixedBackOff);
    }

    private TopicPartition resolveDeadLetterTopic(
            ConsumerRecord<?, ?> record,
            Exception exception
    ) {

        return new TopicPartition(
                record.topic() + ".DLT",
                record.partition()
        );
    }
}
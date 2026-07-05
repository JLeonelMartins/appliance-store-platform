package com.martins.jonathan.appliancestore.sales.outbox.service;

import com.martins.jonathan.appliancestore.sales.event.SaleCreatedEvent;
import com.martins.jonathan.appliancestore.sales.exception.OutboxSerializationException;
import com.martins.jonathan.appliancestore.sales.outbox.model.OutboxEvent;
import com.martins.jonathan.appliancestore.sales.outbox.repository.IOutboxEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.json.JsonMapper;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class OutboxEventService {

    private static final String AGGREGATE_TYPE = "sale";
    private static final String EVENT_TYPE = "SaleCreated";

    private final IOutboxEventRepository outboxEventRepository;
    private final JsonMapper jsonMapper;

    public OutboxEventService(IOutboxEventRepository outboxEventRepository, JsonMapper jsonMapper) {
        this.outboxEventRepository = outboxEventRepository;
        this.jsonMapper = jsonMapper;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void save(SaleCreatedEvent event) {

        String payload = serialize(event);

        OutboxEvent outboxEvent = new OutboxEvent(
                UUID.randomUUID().toString(),
                AGGREGATE_TYPE,
                event.saleId().toString(),
                EVENT_TYPE,
                payload,
                LocalDateTime.now()
        );

        outboxEventRepository.save(outboxEvent);
    }

    private String serialize(SaleCreatedEvent event) {

        try {
            return jsonMapper.writeValueAsString(event);

        } catch (Exception exception) {
            throw new OutboxSerializationException(exception);
        }
    }
}
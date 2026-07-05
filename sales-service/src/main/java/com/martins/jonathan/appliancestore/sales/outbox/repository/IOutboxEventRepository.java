package com.martins.jonathan.appliancestore.sales.outbox.repository;

import com.martins.jonathan.appliancestore.sales.outbox.model.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IOutboxEventRepository extends JpaRepository<OutboxEvent, String> {
}

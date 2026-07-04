package com.martins.jonathan.appliancestore.notification.repository;

import com.martins.jonathan.appliancestore.notification.model.SaleNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ISaleNotificationRepository
        extends JpaRepository<SaleNotification, Long> {

    boolean existsByEventId(String eventId);

    Optional<SaleNotification> findByEventId(
            String eventId
    );
}
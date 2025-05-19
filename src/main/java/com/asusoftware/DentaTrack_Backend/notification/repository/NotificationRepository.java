package com.asusoftware.DentaTrack_Backend.notification.repository;

import com.asusoftware.DentaTrack_Backend.notification.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    List<Notification> findByClinicIdOrderByTimestampDesc(UUID clinicId);

    List<Notification> findByUserIdAndReadFalseOrderByTimestampDesc(UUID userId);
}

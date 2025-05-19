package com.asusoftware.DentaTrack_Backend.notification.service;

import com.asusoftware.DentaTrack_Backend.notification.model.Notification;
import com.asusoftware.DentaTrack_Backend.notification.model.NotificationMessage;
import com.asusoftware.DentaTrack_Backend.notification.model.NotificationType;
import com.asusoftware.DentaTrack_Backend.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final SimpMessagingTemplate messagingTemplate;

    private final NotificationRepository notificationRepository;

    public void notifyClinic(UUID clinicId, String title, String message, NotificationType type) {
        Notification notification = Notification.builder()
                .id(UUID.randomUUID())
                .clinicId(clinicId)
                .title(title)
                .message(message)
                .type(type)
                .timestamp(LocalDateTime.now())
                .read(false)
                .build();

        // 1. Salvăm în DB
        notificationRepository.save(notification);

        // 2. Trimitem prin WebSocket
        NotificationMessage wsMessage = new NotificationMessage(
                title,
                message,
                type.name().toLowerCase(),
                notification.getTimestamp()
        );
        sendNotificationToClinic(clinicId, wsMessage);
    }

    public void sendNotificationToClinic(UUID clinicId, NotificationMessage message) {
        messagingTemplate.convertAndSend("/topic/clinic/" + clinicId, message);
    }
}

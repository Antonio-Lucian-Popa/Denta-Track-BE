package com.asusoftware.DentaTrack_Backend.notification;

import com.asusoftware.DentaTrack_Backend.appointment.model.Appointment;
import com.asusoftware.DentaTrack_Backend.appointment.repository.AppointmentRepository;
import com.asusoftware.DentaTrack_Backend.notification.model.NotificationMessage;
import com.asusoftware.DentaTrack_Backend.notification.model.NotificationType;
import com.asusoftware.DentaTrack_Backend.notification.service.NotificationService;
import com.asusoftware.DentaTrack_Backend.product.model.Product;
import com.asusoftware.DentaTrack_Backend.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationScheduler {

    private final ProductRepository productRepository;
    private final AppointmentRepository appointmentRepository;
    private final NotificationService notificationService;

    @Scheduled(fixedRate = 1800000) // la 30 de minute
    public void checkAndSendScheduledNotifications() {
        LocalDate limitDate = LocalDate.now().plusDays(30);
        List<Product> expiringSoon = productRepository.findExpiringInNextDays(limitDate);
        for (Product product : expiringSoon) {
            notificationService.sendNotificationToClinic(
                    product.getClinicId(),
                    new NotificationMessage(
                            "Product Expiring Soon",
                            product.getName() + " expires in less than 30 days.",
                            NotificationType.WARNING.name(),
                            LocalDateTime.now()
                    )
            );
        }

        List<Appointment> upcoming = appointmentRepository.findAppointmentsInNextHour();
        for (Appointment appointment : upcoming) {
            notificationService.sendNotificationToClinic(
                    appointment.getClinicId(),
                    new NotificationMessage(
                            "Appointment Reminder",
                            "Upcoming with " + appointment.getPatientName() + " at " +
                                    appointment.getDateTime().toLocalTime(),
                            NotificationType.INFO.name(),
                            LocalDateTime.now()
                    )
            );
        }
    }
}


package com.asusoftware.DentaTrack_Backend.appointment.model.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AppointmentDto {
    private UUID id;
    private UUID clinicId;
    private UUID userId;
    private LocalDateTime dateTime;
    private int durationMinutes;
    private String patientName;
    private String patientPhone;
    private String reason;
    private String status;
}

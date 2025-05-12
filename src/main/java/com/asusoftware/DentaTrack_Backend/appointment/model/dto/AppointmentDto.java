package com.asusoftware.DentaTrack_Backend.appointment.model.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AppointmentDto {
    private UUID id;
    private LocalDateTime dateTime;
    private Integer durationMinutes;
    private String patientName;
    private String reason;
    private String status;
    private UUID clinicId;
    private UUID doctorId;
}

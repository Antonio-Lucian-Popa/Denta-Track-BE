package com.asusoftware.DentaTrack_Backend.appointment.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAppointmentDto {
    private UUID clinicId;
    private LocalDateTime dateTime;
    private int durationMinutes;
    private String patientName;
    private String patientPhone;
    private String reason;
}


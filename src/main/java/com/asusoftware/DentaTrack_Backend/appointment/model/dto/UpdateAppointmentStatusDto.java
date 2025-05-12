package com.asusoftware.DentaTrack_Backend.appointment.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAppointmentStatusDto {
    private String status; // SCHEDULED / COMPLETED / CANCELED
}

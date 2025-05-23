package com.asusoftware.DentaTrack_Backend.appointment.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes = 30;

    @Column(name = "patient_name", nullable = false)
    private String patientName;

    @Column(name = "patient_phone", nullable = false)
    private String patientPhone;

    private String reason;

    @Column(nullable = false)
    private String status = "SCHEDULED";

    @Column(name = "clinic_id", nullable = false)
    private UUID clinicId;

    @Column(name = "user_id", nullable = false)
    private UUID userId; // Doctorul la care este programat pacientul.
}


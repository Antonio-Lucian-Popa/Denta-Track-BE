package com.asusoftware.DentaTrack_Backend.clinic.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
@Entity
@Table(name = "clinic_owners", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"clinic_id", "user_id"})
})
public class ClinicOwner {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "clinic_id", nullable = false)
    private UUID clinicId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}


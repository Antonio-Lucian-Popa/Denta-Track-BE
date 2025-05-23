package com.asusoftware.DentaTrack_Backend.invitation.model;

import com.asusoftware.DentaTrack_Backend.user.model.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "invitations")
public class Invitation {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(name = "clinic_id", nullable = false)
    private UUID clinicId;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Column(name = "doctor_id")
    private UUID doctorId;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    private Boolean used = false;

    @Column(name = "employee_email", nullable = false)
    private String employeeEmail;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}


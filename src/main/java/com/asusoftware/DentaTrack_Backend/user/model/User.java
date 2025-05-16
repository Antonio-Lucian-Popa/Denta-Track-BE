package com.asusoftware.DentaTrack_Backend.user.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Email
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "keycloak_id", nullable = false, unique = true)
    private UUID keycloakId;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private UserRole role; // ADMIN, DOCTOR, ASSISTANT

    @Column(name = "doctor_id")
    private UUID doctorId; // doar dacă userul este asistent

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}


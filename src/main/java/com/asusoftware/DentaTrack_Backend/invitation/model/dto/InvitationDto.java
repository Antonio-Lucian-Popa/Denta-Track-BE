package com.asusoftware.DentaTrack_Backend.invitation.model.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class InvitationDto {
    private UUID id;
    private String token;
    private UUID clinicId;
    private String role;
    private UUID doctorId;
    private LocalDateTime expiresAt;
    private Boolean used;
    private LocalDateTime createdAt;
}

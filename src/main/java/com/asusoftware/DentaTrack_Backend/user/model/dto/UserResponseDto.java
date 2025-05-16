package com.asusoftware.DentaTrack_Backend.user.model.dto;

import com.asusoftware.DentaTrack_Backend.user.model.UserRole;
import lombok.Data;

import java.util.UUID;

@Data
public class UserResponseDto {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private UserRole role;
    private UUID clinicId;
    private UUID doctorId;
    private UUID keycloakId;
}


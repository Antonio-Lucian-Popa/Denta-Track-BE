package com.asusoftware.DentaTrack_Backend.user.model.dto;

import com.asusoftware.DentaTrack_Backend.user.model.UserRole;
import lombok.Data;

import java.util.UUID;

@Data
public class UserDto {
    private UUID id;
    private String username;
    private String email;
    private UserRole role;
    private UUID clinicId;
    private UUID doctorId;
}

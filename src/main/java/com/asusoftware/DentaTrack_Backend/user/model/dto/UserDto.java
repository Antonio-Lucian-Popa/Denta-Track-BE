package com.asusoftware.DentaTrack_Backend.user.model.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class UserDto {
    private UUID id;
    private String username;
    private String role;
    private UUID clinicId;
    private UUID doctorId;
}

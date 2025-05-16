package com.asusoftware.DentaTrack_Backend.user.model.dto;

import com.asusoftware.DentaTrack_Backend.user.model.UserRole;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UserRegisterDto {
    private String email;
    private String firstName;
    private String lastName;
    private UserRole role;
    private UUID clinicId;
    private UUID doctorId; // poate fi null
    private String password;
}
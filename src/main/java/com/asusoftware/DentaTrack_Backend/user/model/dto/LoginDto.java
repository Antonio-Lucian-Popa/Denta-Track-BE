package com.asusoftware.DentaTrack_Backend.user.model.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginDto {
    private String email;
    private String password;
}
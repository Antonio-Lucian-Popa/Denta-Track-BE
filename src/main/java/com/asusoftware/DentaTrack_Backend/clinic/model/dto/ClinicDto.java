package com.asusoftware.DentaTrack_Backend.clinic.model.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ClinicDto {
    private UUID id;
    private String name;
    private String address;
    private LocalDateTime createdAt;
}


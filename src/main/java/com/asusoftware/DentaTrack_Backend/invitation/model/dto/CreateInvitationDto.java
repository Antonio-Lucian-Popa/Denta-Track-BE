package com.asusoftware.DentaTrack_Backend.invitation.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateInvitationDto {
    private String email; // email of the user to invite
    private UUID clinicId;
    private String role;      // DOCTOR / ASSISTANT
    private UUID doctorId;    // doar pentru asistente
}

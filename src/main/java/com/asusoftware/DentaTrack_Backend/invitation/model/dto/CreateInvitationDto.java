package com.asusoftware.DentaTrack_Backend.invitation.model.dto;

import com.asusoftware.DentaTrack_Backend.user.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateInvitationDto {
    private String employeeEmail; // email of the user to invite
    private UUID clinicId;
    private UserRole role;      // DOCTOR / ASSISTANT
    private UUID doctorId;    // doar pentru asistente
}

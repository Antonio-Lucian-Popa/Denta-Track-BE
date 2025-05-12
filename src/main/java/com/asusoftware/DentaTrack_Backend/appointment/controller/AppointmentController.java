package com.asusoftware.DentaTrack_Backend.appointment.controller;

import com.asusoftware.DentaTrack_Backend.appointment.model.dto.AppointmentDto;
import com.asusoftware.DentaTrack_Backend.appointment.model.dto.CreateAppointmentDto;
import com.asusoftware.DentaTrack_Backend.appointment.model.dto.UpdateAppointmentStatusDto;
import com.asusoftware.DentaTrack_Backend.appointment.service.AppointmentService;
import com.asusoftware.DentaTrack_Backend.user.model.User;
import com.asusoftware.DentaTrack_Backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final UserService userService;

    /**
     * Creează o programare nouă pentru un pacient.
     */
    @PostMapping
    public ResponseEntity<AppointmentDto> createAppointment(@AuthenticationPrincipal Jwt principal,
                                                            @RequestBody CreateAppointmentDto dto) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        User user = userService.getByKeycloakId(keycloakId);

        AppointmentDto response = appointmentService.createAppointment(user.getId(), dto);
        return ResponseEntity.ok(response);
    }

    /**
     * Returnează programările dintr-o clinică.
     */
    @GetMapping("/clinic/{clinicId}")
    public ResponseEntity<List<AppointmentDto>> getByClinic(@AuthenticationPrincipal Jwt principal,
                                                            @PathVariable UUID clinicId) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        User user = userService.getByKeycloakId(keycloakId);

        List<AppointmentDto> list = appointmentService.getAppointmentsByClinic(clinicId, user.getId());
        return ResponseEntity.ok(list);
    }

    /**
     * Returnează programările unui medic.
     */
    @GetMapping("/doctor")
    public ResponseEntity<List<AppointmentDto>> getByDoctor(@AuthenticationPrincipal Jwt principal) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        User user = userService.getByKeycloakId(keycloakId);

        List<AppointmentDto> list = appointmentService.getAppointmentsByDoctor(user.getId());
        return ResponseEntity.ok(list);
    }

    /**
     * Actualizează statusul unei programări.
     */
    @PatchMapping("/{appointmentId}/status")
    public ResponseEntity<AppointmentDto> updateStatus(@AuthenticationPrincipal Jwt principal,
                                                       @PathVariable UUID appointmentId,
                                                       @RequestBody UpdateAppointmentStatusDto dto) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        User user = userService.getByKeycloakId(keycloakId);

        AppointmentDto updated = appointmentService.updateStatus(appointmentId, dto, user.getId());
        return ResponseEntity.ok(updated);
    }
}
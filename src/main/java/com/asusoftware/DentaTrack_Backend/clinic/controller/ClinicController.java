package com.asusoftware.DentaTrack_Backend.clinic.controller;

import com.asusoftware.DentaTrack_Backend.clinic.model.dto.ClinicDto;
import com.asusoftware.DentaTrack_Backend.clinic.model.dto.CreateClinicDto;
import com.asusoftware.DentaTrack_Backend.clinic.service.ClinicService;
import com.asusoftware.DentaTrack_Backend.user.model.User;
import com.asusoftware.DentaTrack_Backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/clinics")
@RequiredArgsConstructor
public class ClinicController {

    private final ClinicService clinicService;
    private final UserService userService;

    /**
     * Creează o clinică nouă și o asociază userului logat.
     */
    @PostMapping
    public ResponseEntity<ClinicDto> createClinic(@AuthenticationPrincipal Jwt principal,
                                                  @RequestBody CreateClinicDto dto) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        User user = userService.getByKeycloakId(keycloakId);

        ClinicDto clinic = clinicService.createClinic(user.getId(), dto);
        return ResponseEntity.ok(clinic);
    }

    /**
     * Returnează toate clinicile unde userul logat este owner.
     */
    @GetMapping
    public ResponseEntity<List<ClinicDto>> getMyClinics(@AuthenticationPrincipal Jwt principal) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        User user = userService.getByKeycloakId(keycloakId);

        List<ClinicDto> clinics = clinicService.getClinicsForUser(user.getId());
        return ResponseEntity.ok(clinics);
    }

    /**
     * Returnează o clinică după ID (doar dacă userul este owner).
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClinicDto> getClinicById(@AuthenticationPrincipal Jwt principal,
                                                   @PathVariable UUID id) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        User user = userService.getByKeycloakId(keycloakId);

        if (!clinicService.isUserOwnerOfClinic(user.getId(), id)) {
            return ResponseEntity.status(403).build(); // Forbidden
        }

        ClinicDto clinic = clinicService.getById(id);
        return ResponseEntity.ok(clinic);
    }
}

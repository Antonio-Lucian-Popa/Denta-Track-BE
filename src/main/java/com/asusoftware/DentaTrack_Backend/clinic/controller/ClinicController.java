package com.asusoftware.DentaTrack_Backend.clinic.controller;

import com.asusoftware.DentaTrack_Backend.clinic.model.dto.ClinicDto;
import com.asusoftware.DentaTrack_Backend.clinic.model.dto.CreateClinicDto;
import com.asusoftware.DentaTrack_Backend.clinic.service.ClinicService;
import com.asusoftware.DentaTrack_Backend.user.model.User;
import com.asusoftware.DentaTrack_Backend.user.model.dto.UserDto;
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

    /**
     * Returnează toți utilizatorii (doctori și asistente) dintr-o clinică.
     */
    @GetMapping("/{id}/staff")
    public ResponseEntity<List<UserDto>> getClinicStaff(@AuthenticationPrincipal Jwt principal,
                                                        @PathVariable UUID id) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        User currentUser = userService.getByKeycloakId(keycloakId);

        if (!clinicService.isUserOwnerOfClinic(currentUser.getId(), id)) {
            return ResponseEntity.status(403).build();
        }

        List<UserDto> staff = clinicService.getUsersInClinic(id);
        return ResponseEntity.ok(staff);
    }

    @DeleteMapping("/{clinicId}/users/{userId}")
    public ResponseEntity<Void> removeUserFromClinic(@AuthenticationPrincipal Jwt principal,
                                                     @PathVariable UUID clinicId,
                                                     @PathVariable UUID userId) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        User currentUser = userService.getByKeycloakId(keycloakId);

        // Doar ownerul clinicii are voie
        if (!clinicService.isUserOwnerOfClinic(currentUser.getId(), clinicId)) {
            return ResponseEntity.status(403).build();
        }

        clinicService.removeUserFromClinic(clinicId, userId);
        return ResponseEntity.noContent().build();
    }


}

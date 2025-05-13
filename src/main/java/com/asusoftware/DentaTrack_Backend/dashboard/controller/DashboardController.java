package com.asusoftware.DentaTrack_Backend.dashboard.controller;

import com.asusoftware.DentaTrack_Backend.clinic.service.ClinicService;
import com.asusoftware.DentaTrack_Backend.dashboard.model.dto.DashboardDto;
import com.asusoftware.DentaTrack_Backend.dashboard.service.DashboardService;
import com.asusoftware.DentaTrack_Backend.user.model.User;
import com.asusoftware.DentaTrack_Backend.user.service.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/dashboard")
@AllArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;
    private final UserService userService;
    private final ClinicService clinicService;

    @GetMapping("/clinic/{clinicId}")
    public ResponseEntity<DashboardDto> getDashboard(@AuthenticationPrincipal Jwt principal,
                                                     @PathVariable UUID clinicId) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        User user = userService.getByKeycloakId(keycloakId);

        if (!clinicService.isUserInClinic(user.getId(), clinicId)) {
            return ResponseEntity.status(403).build();
        }

        DashboardDto data = dashboardService.getDashboard(clinicId);
        return ResponseEntity.ok(data);
    }
}

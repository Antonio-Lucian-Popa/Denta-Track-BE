package com.asusoftware.DentaTrack_Backend.user.controller;

import com.asusoftware.DentaTrack_Backend.user.model.User;
import com.asusoftware.DentaTrack_Backend.user.model.dto.LoginDto;
import com.asusoftware.DentaTrack_Backend.user.model.dto.UserRegisterDto;
import com.asusoftware.DentaTrack_Backend.user.model.dto.UserResponseDto;
import com.asusoftware.DentaTrack_Backend.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.AccessTokenResponse;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ModelMapper mapper;

    /**
     * Înregistrare standard – folosită de doctorul care creează clinică mai târziu.
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@RequestBody @Valid UserRegisterDto dto) {
        UserResponseDto response = userService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Înregistrare pe bază de invitație (link primit pe email).
     */
    @PostMapping("/register/invite")
    public ResponseEntity<UserResponseDto> registerWithInvite(
            @RequestParam("token") String inviteToken,
            @RequestBody @Valid UserRegisterDto dto) {

        UserResponseDto response = userService.registerWithInvite(dto, inviteToken);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Login user (prin Keycloak).
     */
    @PostMapping("/login")
    public ResponseEntity<AccessTokenResponse> login(@RequestBody LoginDto dto) {
        return ResponseEntity.ok(userService.login(dto));
    }

    /**
     * Obține datele userului logat (necesită token JWT).
     */
    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getCurrentUser(@AuthenticationPrincipal Jwt principal) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        User user = userService.getByKeycloakId(keycloakId);
        return ResponseEntity.ok(mapper.map(user, UserResponseDto.class));
    }

    /**
     * Șterge userul curent (opțional).
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteCurrentUser(@AuthenticationPrincipal Jwt principal) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        userService.deleteByKeycloakId(keycloakId);
        return ResponseEntity.noContent().build();
    }

    /**
     * (Opțional) Returnează toate clinicile unde userul este owner.
     */
    @GetMapping("/my-clinics")
    public ResponseEntity<List<UUID>> getClinicIds(@AuthenticationPrincipal Jwt principal) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        User user = userService.getByKeycloakId(keycloakId);
        List<UUID> clinicIds = userService.getClinicIdsWhereUserIsOwner(user.getId());
        return ResponseEntity.ok(clinicIds);
    }
}

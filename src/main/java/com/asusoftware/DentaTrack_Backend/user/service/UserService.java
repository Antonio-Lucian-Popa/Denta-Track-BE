package com.asusoftware.DentaTrack_Backend.user.service;

import com.asusoftware.DentaTrack_Backend.config.KeycloakService;
import com.asusoftware.DentaTrack_Backend.exception.InvalidTokenException;
import com.asusoftware.DentaTrack_Backend.exception.UserNotFoundException;
import com.asusoftware.DentaTrack_Backend.invitation.model.Invitation;
import com.asusoftware.DentaTrack_Backend.invitation.repository.InvitationRepository;
import com.asusoftware.DentaTrack_Backend.user.model.User;
import com.asusoftware.DentaTrack_Backend.user.model.dto.LoginDto;
import com.asusoftware.DentaTrack_Backend.user.model.dto.UserRegisterDto;
import com.asusoftware.DentaTrack_Backend.user.model.dto.UserResponseDto;
import com.asusoftware.DentaTrack_Backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.AccessTokenResponse;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final KeycloakService keycloakService;
    private final InvitationRepository invitationRepository;
    private final ModelMapper mapper;

    /**
     * Înregistrare standard (fără clinică).
     * Se folosește pentru doctorul care creează ulterior clinica.
     */
    @Transactional
    public UserResponseDto register(UserRegisterDto dto) {
        String keycloakId = keycloakService.createKeycloakUser(dto);

        User user = User.builder()
                .id(UUID.randomUUID())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .keycloakId(UUID.fromString(keycloakId))
                .role(dto.getRole()) // ex: DOCTOR
                .doctorId(dto.getDoctorId()) // null dacă nu e asistent
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);

        return mapper.map(user, UserResponseDto.class);
    }

    /**
     * Înregistrare pe baza unei invitații — clinică, rol și doctorId vin din token.
     */
    @Transactional
    public UserResponseDto registerWithInvite(UserRegisterDto dto, String token) {
        Invitation invitation = invitationRepository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Invitația nu este validă"));

        if (invitation.getUsed()) {
            throw new InvalidTokenException("Invitația a fost deja folosită.");
        }

        if (invitation.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new InvalidTokenException("Invitația a expirat.");
        }

        String keycloakId = keycloakService.createKeycloakUser(dto);

        User user = User.builder()
                .id(UUID.randomUUID())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .keycloakId(UUID.fromString(keycloakId))
                .role(invitation.getRole())
                .doctorId(invitation.getDoctorId())
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);

        invitation.setUsed(true);
        invitationRepository.save(invitation);

        return mapper.map(user, UserResponseDto.class);
    }

    /**
     * Login utilizator prin Keycloak.
     */
    public AccessTokenResponse login(LoginDto dto) {
        return keycloakService.loginUser(dto);
    }

    /**
     * Returnează userul autenticat după keycloakId.
     */
    public User getByKeycloakId(UUID keycloakId) {
        return userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new UserNotFoundException("User cu keycloakId " + keycloakId + " nu a fost găsit"));
    }

    /**
     * Ștergere cont după keycloakId (soft delete sau hard).
     */
    @Transactional
    public void deleteByKeycloakId(UUID keycloakId) {
        userRepository.findByKeycloakId(keycloakId)
                .ifPresent(userRepository::delete);
    }

    /**
     * Returnează toți utilizatorii (doctori sau asistente) dintr-o clinică.
     */
    public List<UserResponseDto> getUsersByClinic(UUID clinicId) {
        List<User> users = userRepository.findAllUsersByClinic(clinicId);
        return users.stream()
                .map(user -> mapper.map(user, UserResponseDto.class))
                .collect(Collectors.toList());
    }

    /**
     * Obține toate clinicile unde userul este owner (pentru dashboard, permisiuni, etc).
     */
    public List<UUID> getClinicIdsWhereUserIsOwner(UUID userId) {
        return userRepository.findClinicIdsWhereUserIsOwner(userId);
    }

}



package com.asusoftware.DentaTrack_Backend.invitation.service;

import com.asusoftware.DentaTrack_Backend.clinic.model.Clinic;
import com.asusoftware.DentaTrack_Backend.clinic.model.dto.ClinicDto;
import com.asusoftware.DentaTrack_Backend.clinic.service.ClinicService;
import com.asusoftware.DentaTrack_Backend.exception.InvalidTokenException;
import com.asusoftware.DentaTrack_Backend.invitation.model.Invitation;
import com.asusoftware.DentaTrack_Backend.invitation.model.dto.CreateInvitationDto;
import com.asusoftware.DentaTrack_Backend.invitation.model.dto.InvitationDto;
import com.asusoftware.DentaTrack_Backend.invitation.repository.InvitationRepository;
import com.asusoftware.DentaTrack_Backend.mail.service.MailService;
import com.asusoftware.DentaTrack_Backend.user.model.User;
import com.asusoftware.DentaTrack_Backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import jakarta.ws.rs.NotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvitationService {

    private final InvitationRepository invitationRepository;
    private final UserService userService;
    private final MailService mailService;
    private final ClinicService clinicService;
    private final ModelMapper mapper;

    /**
     * Generează o invitație unică pentru o clinică și un rol.
     */
    @Transactional
    public InvitationDto generateInvitation(CreateInvitationDto dto, String baseUrl) {
        String token = UUID.randomUUID().toString();

        // trbeuie sa luam clinicId si doctorId
        User doctor = userService.getById(dto.getDoctorId());
        ClinicDto clinic = clinicService.getById(dto.getClinicId());

        Invitation invitation = Invitation.builder()
                .token(token)
                .clinicId(dto.getClinicId())
                .role(dto.getRole())
                .doctorId(dto.getDoctorId())
                .employeeEmail(dto.getEmployeeEmail())
                .expiresAt(LocalDateTime.now().plusDays(4)) // valabil 7 zile
                .used(false)
                .createdAt(LocalDateTime.now())
                .build();

        invitationRepository.save(invitation);

        // Trebuie sa trimitem email la user cu token(adica invitatia)
        // 2. Trimiți mail
        String link = "http://localhost:5173/dentatrack-fe" + "/login?token=" + invitation.getToken();
        mailService.sendInvitationEmail(dto.getEmployeeEmail(), link, clinic.getName(), dto.getRole().toString(), doctor.getFirstName() + " " + doctor.getLastName());

        return mapper.map(invitation, InvitationDto.class);
    }

    /**
     * Validează tokenul și returnează datele legate de invitație.
     */
    public InvitationDto validateToken(String token) {
        Invitation invitation = invitationRepository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Invitație invalidă"));

        if (invitation.getUsed()) {
            throw new InvalidTokenException("Invitația a fost deja folosită");
        }

        if (invitation.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new InvalidTokenException("Invitația a expirat");
        }

        return mapper.map(invitation, InvitationDto.class);
    }

    /**
     * Marchează invitația ca folosită după înregistrare.
     */
    @Transactional
    public void markAsUsed(String token) {
        Invitation invitation = invitationRepository.findByToken(token)
                .orElseThrow(() -> new NotFoundException("Invitație inexistentă"));

        invitation.setUsed(true);
        invitationRepository.save(invitation);
    }

    /**
     * Opțional: returnează invitațiile active pentru o clinică.
     */
    public List<InvitationDto> getActiveInvitations(UUID clinicId) {
        return invitationRepository.findActiveInvitationsByClinic(clinicId).stream()
                .map(i -> mapper.map(i, InvitationDto.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteInvitation(UUID invitationId) {
        invitationRepository.deleteById(invitationId);
    }

    public boolean canDeleteInvitation(UUID invitationId, UUID userId) {
        return invitationRepository.findById(invitationId)
                .map(inv -> clinicService.isUserOwnerOfClinic(userId, inv.getClinicId()))
                .orElse(false);
    }

}
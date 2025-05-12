package com.asusoftware.DentaTrack_Backend.invitation.service;

import com.asusoftware.DentaTrack_Backend.exception.InvalidTokenException;
import com.asusoftware.DentaTrack_Backend.invitation.model.Invitation;
import com.asusoftware.DentaTrack_Backend.invitation.model.dto.CreateInvitationDto;
import com.asusoftware.DentaTrack_Backend.invitation.model.dto.InvitationDto;
import com.asusoftware.DentaTrack_Backend.invitation.repository.InvitationRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.ws.rs.NotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvitationService {

    private final InvitationRepository invitationRepository;
    private final ModelMapper mapper;

    /**
     * Generează o invitație unică pentru o clinică și un rol.
     */
    @Transactional
    public InvitationDto generateInvitation(CreateInvitationDto dto) {
        String token = UUID.randomUUID().toString();

        Invitation invitation = Invitation.builder()
                .id(UUID.randomUUID())
                .token(token)
                .clinicId(dto.getClinicId())
                .role(dto.getRole())
                .doctorId(dto.getDoctorId())
                .expiresAt(LocalDateTime.now().plusDays(7)) // valabil 7 zile
                .used(false)
                .createdAt(LocalDateTime.now())
                .build();

        invitationRepository.save(invitation);

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
}
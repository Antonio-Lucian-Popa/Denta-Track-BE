package com.asusoftware.DentaTrack_Backend.invitation.repository;

import com.asusoftware.DentaTrack_Backend.invitation.model.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, UUID> {

    /**
     * Caută o invitație după tokenul său (UUID sau string generat securizat).
     */
    Optional<Invitation> findByToken(String token);

    /**
     * Verifică dacă o invitație există deja pentru un anumit clinic + rol + doctor
     * (poate fi util dacă vrei să eviți trimiterea multiplă).
     */
    boolean existsByClinicIdAndRoleAndDoctorId(UUID clinicId, String role, UUID doctorId);

    /**
     * Returnează toate invitațiile active (nefolosite și neexpirate) pentru o clinică.
     */
    @Query("SELECT i FROM Invitation i WHERE i.clinicId = :clinicId AND i.used = false AND i.expiresAt > CURRENT_TIMESTAMP")
    List<Invitation> findActiveInvitationsByClinic(@Param("clinicId") UUID clinicId);
}

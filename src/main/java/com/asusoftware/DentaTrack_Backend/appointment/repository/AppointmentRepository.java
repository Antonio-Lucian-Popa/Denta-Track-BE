package com.asusoftware.DentaTrack_Backend.appointment.repository;

import com.asusoftware.DentaTrack_Backend.appointment.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    /**
     * Returnează toate programările dintr-o clinică.
     */
    List<Appointment> findByClinicId(UUID clinicId);

    /**
     * Returnează toate programările făcute de un anumit utilizator (medic).
     */
    List<Appointment> findByUserId(UUID userId);

    int countByClinicIdAndDateTimeBetween(UUID clinicId, LocalDateTime start, LocalDateTime end);

    int countByClinicIdAndStatusAndDateTimeBetween(UUID clinicId, String status, LocalDateTime start, LocalDateTime end);

    @Modifying
    @Query("UPDATE Appointment a SET a.userId = NULL WHERE a.userId = :userId AND a.clinicId = :clinicId")
    void clearClinicUser(@Param("userId") UUID userId, @Param("clinicId") UUID clinicId);

}


package com.asusoftware.DentaTrack_Backend.clinic.repository;

import com.asusoftware.DentaTrack_Backend.clinic.model.ClinicStaff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClinicStaffRepository extends JpaRepository<ClinicStaff, UUID> {
    boolean existsByClinicIdAndUserId(UUID clinicId, UUID userId);
    List<ClinicStaff> findAllByUserId(UUID userId);
    Optional<ClinicStaff> findByClinicIdAndUserId(UUID clinicId, UUID userId);
}

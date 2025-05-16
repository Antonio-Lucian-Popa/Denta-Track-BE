package com.asusoftware.DentaTrack_Backend.clinic.repository;

import com.asusoftware.DentaTrack_Backend.clinic.model.ClinicStaff;
import com.asusoftware.DentaTrack_Backend.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClinicStaffRepository extends JpaRepository<ClinicStaff, UUID> {
    boolean existsByClinicIdAndUserId(UUID clinicId, UUID userId);
    List<ClinicStaff> findAllByUserId(UUID userId);
    Optional<ClinicStaff> findByClinicIdAndUserId(UUID clinicId, UUID userId);
    void deleteByClinicIdAndUserId(UUID clinicId, UUID userId);
    @Query("SELECT u FROM User u WHERE u.id IN (SELECT cs.userId FROM ClinicStaff cs WHERE cs.clinicId = :clinicId)")
    List<User> findUsersByClinicViaClinicStaff(UUID clinicId);

    @Query("SELECT cs.clinicId FROM ClinicStaff cs WHERE cs.userId = :userId")
    List<UUID> findClinicIdsByUserId(UUID userId);
}

package com.asusoftware.DentaTrack_Backend.clinic.repository;

import com.asusoftware.DentaTrack_Backend.clinic.model.ClinicOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ClinicOwnerRepository extends JpaRepository<ClinicOwner, UUID> {

    List<ClinicOwner> findByUserId(UUID userId);

    boolean existsByClinicIdAndUserId(UUID clinicId, UUID userId);

    @Query("SELECT co.clinicId FROM ClinicOwner co WHERE co.userId = :userId")
    List<UUID> findClinicIdsByUserId(@Param("userId") UUID userId);

}



package com.asusoftware.DentaTrack_Backend.clinic.repository;

import com.asusoftware.DentaTrack_Backend.clinic.model.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClinicRepository extends JpaRepository<Clinic, UUID> {
}

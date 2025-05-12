package com.asusoftware.DentaTrack_Backend.clinic.service;

import com.asusoftware.DentaTrack_Backend.clinic.model.Clinic;
import com.asusoftware.DentaTrack_Backend.clinic.model.ClinicOwner;
import com.asusoftware.DentaTrack_Backend.clinic.model.dto.ClinicDto;
import com.asusoftware.DentaTrack_Backend.clinic.model.dto.CreateClinicDto;
import com.asusoftware.DentaTrack_Backend.clinic.repository.ClinicOwnerRepository;
import com.asusoftware.DentaTrack_Backend.clinic.repository.ClinicRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClinicService {

    private final ClinicRepository clinicRepository;
    private final ClinicOwnerRepository clinicOwnerRepository;
    private final ModelMapper mapper;

    @Transactional
    public ClinicDto createClinic(UUID userId, CreateClinicDto dto) {
        // 1. Creează clinica
        Clinic clinic = new Clinic();
        clinic.setId(UUID.randomUUID());
        clinic.setName(dto.getName());
        clinic.setAddress(dto.getAddress());
        clinic.setCreatedAt(LocalDateTime.now());
        clinicRepository.save(clinic);

        // 2. Salvează relația de ownership
        ClinicOwner owner = new ClinicOwner();
        owner.setId(UUID.randomUUID());
        owner.setClinicId(clinic.getId());
        owner.setUserId(userId);
        owner.setCreatedAt(LocalDateTime.now());
        clinicOwnerRepository.save(owner);

        // 3. Returnează DTO
        return mapper.map(clinic, ClinicDto.class);
    }
}


package com.asusoftware.DentaTrack_Backend.clinic.service;

import com.asusoftware.DentaTrack_Backend.appointment.repository.AppointmentRepository;
import com.asusoftware.DentaTrack_Backend.clinic.model.Clinic;
import com.asusoftware.DentaTrack_Backend.clinic.model.ClinicOwner;
import com.asusoftware.DentaTrack_Backend.clinic.model.dto.ClinicDto;
import com.asusoftware.DentaTrack_Backend.clinic.model.dto.CreateClinicDto;
import com.asusoftware.DentaTrack_Backend.clinic.repository.ClinicOwnerRepository;
import com.asusoftware.DentaTrack_Backend.clinic.repository.ClinicRepository;
import com.asusoftware.DentaTrack_Backend.invitation.repository.InvitationRepository;
import com.asusoftware.DentaTrack_Backend.product.model.Product;
import com.asusoftware.DentaTrack_Backend.product.repository.ProductRepository;
import com.asusoftware.DentaTrack_Backend.user.model.User;
import com.asusoftware.DentaTrack_Backend.user.model.dto.UserDto;
import com.asusoftware.DentaTrack_Backend.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
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
public class ClinicService {

    private final ClinicRepository clinicRepository;
    private final ProductRepository productRepository;
    private final ClinicOwnerRepository clinicOwnerRepository;
    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;
    private final ModelMapper mapper;
    private final InvitationRepository invitationRepository;

    /**
     * Creează o clinică și o asociază cu utilizatorul logat (owner).
     */
    @Transactional
    public ClinicDto createClinic(UUID userId, CreateClinicDto dto) {
        // Verifică dacă userul există
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Userul nu a fost găsit"));

        // Creează clinica
        Clinic clinic = Clinic.builder()
                .name(dto.getName())
                .address(dto.getAddress())
                .createdAt(LocalDateTime.now())
                .build();

        clinicRepository.save(clinic);

        // Adaugă userul ca owner în clinic_owners
        ClinicOwner owner = ClinicOwner.builder()
                .clinicId(clinic.getId())
                .userId(user.getId())
                .createdAt(LocalDateTime.now())
                .build();

        clinicOwnerRepository.save(owner);

        return mapper.map(clinic, ClinicDto.class);
    }

    /**
     * Returnează toate clinicile unde userul este owner.
     */
    public List<ClinicDto> getClinicsForUser(UUID userId) {
        List<UUID> clinicIds = clinicOwnerRepository.findClinicIdsByUserId(userId);
        List<Clinic> clinics = clinicRepository.findAllById(clinicIds);

        return clinics.stream()
                .map(clinic -> mapper.map(clinic, ClinicDto.class))
                .collect(Collectors.toList());
    }

    /**
     * Returnează o clinică după ID.
     */
    public ClinicDto getById(UUID clinicId) {
        Clinic clinic = clinicRepository.findById(clinicId)
                .orElseThrow(() -> new NotFoundException("Clinica nu a fost găsită"));
        return mapper.map(clinic, ClinicDto.class);
    }

    /**
     * Verifică dacă un user este owner într-o clinică.
     */
    public boolean isUserOwnerOfClinic(UUID userId, UUID clinicId) {
        return clinicOwnerRepository.existsByClinicIdAndUserId(clinicId, userId);
    }

    public boolean isUserInClinic(UUID userId, UUID clinicId) {
        // Owner sau doctor/asistent care are produse înregistrate în clinică
        return isUserOwnerOfClinic(userId, clinicId)
                || productRepository.existsByClinicIdAndUserId(clinicId, userId);
    }

    public List<UserDto> getUsersInClinic(UUID clinicId) {
        List<User> users = userRepository.findUsersByClinic(clinicId);
        return users.stream().map((user) -> mapper.map(user, UserDto.class)).toList();
    }


    public void removeUserFromClinic(UUID clinicId, UUID userId) {
        // Șterge legătura clinică -> user din produsele și programările unde apare
        productRepository.clearClinicUser(userId, clinicId);
        appointmentRepository.clearClinicUser(userId, clinicId);
        // Dacă vrei, poți șterge și invitațiile vechi (opțional)
        invitationRepository.deleteByClinicIdAndDoctorId(clinicId, userId);
    }



}


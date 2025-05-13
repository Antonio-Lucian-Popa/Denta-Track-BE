package com.asusoftware.DentaTrack_Backend.appointment.service;

import com.asusoftware.DentaTrack_Backend.appointment.model.Appointment;
import com.asusoftware.DentaTrack_Backend.appointment.model.dto.AppointmentDto;
import com.asusoftware.DentaTrack_Backend.appointment.model.dto.CreateAppointmentDto;
import com.asusoftware.DentaTrack_Backend.appointment.model.dto.UpdateAppointmentStatusDto;
import com.asusoftware.DentaTrack_Backend.appointment.repository.AppointmentRepository;
import com.asusoftware.DentaTrack_Backend.clinic.service.ClinicService;
import com.asusoftware.DentaTrack_Backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final ClinicService clinicService;
    private final UserService userService;
    private final ModelMapper mapper;

    /**
     * Creează o programare nouă pentru pacient.
     */
    @Transactional
    public AppointmentDto createAppointment(UUID userId, CreateAppointmentDto dto) {
        // verifică dacă userul are dreptul să creeze în acea clinică
        if (!clinicService.isUserInClinic(userId, dto.getClinicId())) {
            throw new SecurityException("Acces interzis la această clinică");
        }

        Appointment appointment = Appointment.builder()
                .clinicId(dto.getClinicId())
                .userId(userId)
                .dateTime(dto.getDateTime())
                .durationMinutes(dto.getDurationMinutes())
                .patientName(dto.getPatientName())
                .patientPhone(dto.getPatientPhone())
                .reason(dto.getReason())
                .status("SCHEDULED")
                .build();

        appointmentRepository.save(appointment);
        return mapper.map(appointment, AppointmentDto.class);
    }

    /**
     * Returnează toate programările dintr-o clinică.
     */
    public List<AppointmentDto> getAppointmentsByClinic(UUID clinicId, UUID requesterId) {
        if (!clinicService.isUserInClinic(requesterId, clinicId)) {
            throw new SecurityException("Acces interzis la această clinică");
        }

        return appointmentRepository.findByClinicId(clinicId).stream()
                .map(a -> mapper.map(a, AppointmentDto.class))
                .collect(Collectors.toList());
    }

    /**
     * Returnează toate programările unui doctor.
     */
    public List<AppointmentDto> getAppointmentsByDoctor(UUID doctorId) {
        return appointmentRepository.findByUserId(doctorId).stream()
                .map(a -> mapper.map(a, AppointmentDto.class))
                .collect(Collectors.toList());
    }

    /**
     * Update status programare (ex: completat / anulat).
     */
    @Transactional
    public AppointmentDto updateStatus(UUID appointmentId, UpdateAppointmentStatusDto dto, UUID requesterId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new NotFoundException("Programarea nu a fost găsită"));

        if (!clinicService.isUserInClinic(requesterId, appointment.getClinicId())) {
            throw new SecurityException("Nu ai voie să modifici această programare");
        }

        appointment.setStatus(dto.getStatus()); // ex: COMPLETED, CANCELED
        appointmentRepository.save(appointment);

        return mapper.map(appointment, AppointmentDto.class);
    }
}

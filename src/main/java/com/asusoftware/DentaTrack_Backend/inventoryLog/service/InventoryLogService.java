package com.asusoftware.DentaTrack_Backend.inventoryLog.service;

import com.asusoftware.DentaTrack_Backend.inventoryLog.model.InventoryLog;
import com.asusoftware.DentaTrack_Backend.inventoryLog.model.dto.InventoryLogDto;
import com.asusoftware.DentaTrack_Backend.inventoryLog.repository.InventoryLogRepository;
import com.asusoftware.DentaTrack_Backend.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryLogService {

    private final InventoryLogRepository logRepository;
    private final ProductRepository productRepository;
    private final ModelMapper mapper;

    public List<InventoryLogDto> getLogsByProduct(UUID productId) {
        return logRepository.findByProductId(productId).stream()
                .map(log -> mapper.map(log, InventoryLogDto.class))
                .collect(Collectors.toList());
    }

    public List<InventoryLogDto> getLogsByUser(UUID userId) {
        return logRepository.findByUserId(userId).stream()
                .map(log -> mapper.map(log, InventoryLogDto.class))
                .collect(Collectors.toList());
    }

    public List<InventoryLogDto> getLogsByClinic(UUID clinicId) {
        return logRepository.findByClinicId(clinicId).stream()
                .map(log -> mapper.map(log, InventoryLogDto.class))
                .collect(Collectors.toList());
    }

    public List<InventoryLogDto> getLogsBetween(LocalDateTime start, LocalDateTime end) {
        return logRepository.findByTimestampBetween(start, end).stream()
                .map(log -> mapper.map(log, InventoryLogDto.class))
                .collect(Collectors.toList());
    }

    public List<InventoryLog> getRawLogsByClinic(UUID clinicId) {
        return logRepository.findByClinicId(clinicId);
    }

    public List<InventoryLog> getLogsByClinicBetween(UUID clinicId, LocalDateTime start, LocalDateTime end) {
        return logRepository.findByClinicIdAndTimestampBetween(clinicId, start, end);
    }


}

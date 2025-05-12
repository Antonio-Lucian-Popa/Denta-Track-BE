package com.asusoftware.DentaTrack_Backend.inventoryLog.controller;

import com.asusoftware.DentaTrack_Backend.inventoryLog.model.dto.InventoryLogDto;
import com.asusoftware.DentaTrack_Backend.inventoryLog.service.InventoryLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/inventory-logs")
@RequiredArgsConstructor
public class InventoryLogController {

    private final InventoryLogService inventoryLogService;

    @GetMapping("/product/{productId}")
    public List<InventoryLogDto> getByProduct(@PathVariable UUID productId) {
        return inventoryLogService.getLogsByProduct(productId);
    }

    @GetMapping("/user/{userId}")
    public List<InventoryLogDto> getByUser(@PathVariable UUID userId) {
        return inventoryLogService.getLogsByUser(userId);
    }

    @GetMapping("/clinic/{clinicId}")
    public List<InventoryLogDto> getByClinic(@PathVariable UUID clinicId) {
        return inventoryLogService.getLogsByClinic(clinicId);
    }

    @GetMapping("/between")
    public List<InventoryLogDto> getBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return inventoryLogService.getLogsBetween(start, end);
    }
}

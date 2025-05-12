package com.asusoftware.DentaTrack_Backend.inventoryLog.controller;

import com.asusoftware.DentaTrack_Backend.inventoryLog.model.InventoryLog;
import com.asusoftware.DentaTrack_Backend.inventoryLog.model.dto.InventoryLogDto;
import com.asusoftware.DentaTrack_Backend.inventoryLog.service.InventoryExportService;
import com.asusoftware.DentaTrack_Backend.inventoryLog.service.InventoryLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/inventory-logs")
@RequiredArgsConstructor
public class InventoryLogController {

    private final InventoryLogService inventoryLogService;
    private final InventoryExportService inventoryExportService;

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

    @GetMapping("/clinic/{clinicId}/export")
    public ResponseEntity<byte[]> exportLogsAsExcel(@PathVariable UUID clinicId) {
        try {
            List<InventoryLog> logs = inventoryLogService.getRawLogsByClinic(clinicId);
            InputStream excel = inventoryExportService.exportToExcel(logs);

            byte[] fileBytes = excel.readAllBytes();
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=inventory_logs.xlsx")
                    .header("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                    .body(fileBytes);

        } catch (IOException e) {
            // Poți loga eroarea și returna o eroare 500
            return ResponseEntity.internalServerError().build();
        }
    }


}

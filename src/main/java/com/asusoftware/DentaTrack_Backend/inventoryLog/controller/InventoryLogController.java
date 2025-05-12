package com.asusoftware.DentaTrack_Backend.inventoryLog.controller;

import com.asusoftware.DentaTrack_Backend.inventoryLog.model.InventoryLog;
import com.asusoftware.DentaTrack_Backend.inventoryLog.model.dto.InventoryLogDto;
import com.asusoftware.DentaTrack_Backend.inventoryLog.service.InventoryExportService;
import com.asusoftware.DentaTrack_Backend.inventoryLog.service.InventoryLogService;
import com.asusoftware.DentaTrack_Backend.product.model.Product;
import com.asusoftware.DentaTrack_Backend.product.service.ProductService;
import com.asusoftware.DentaTrack_Backend.user.model.User;
import com.asusoftware.DentaTrack_Backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/inventory-logs")
@RequiredArgsConstructor
public class InventoryLogController {

    private final InventoryLogService inventoryLogService;
    private final InventoryExportService inventoryExportService;
    private final UserService userService;
    private final ProductService productService;

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
    public ResponseEntity<byte[]> exportLogsAsExcel(
            @PathVariable UUID clinicId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        try {
            // 1. Obține logurile în funcție de filtrare
            List<InventoryLog> logs = (start != null && end != null)
                    ? inventoryLogService.getLogsByClinicBetween(clinicId, start, end)
                    : inventoryLogService.getRawLogsByClinic(clinicId);

            // 2. Construiește map-urile cu numele utilizatorilor și produselor
            List<User> users = userService.getUsersEntityByClinic(clinicId);
            Map<UUID, String> userNames = users.stream()
                    .collect(Collectors.toMap(User::getId, u -> u.getFirstName() + " " + u.getLastName()));

            List<Product> products = productService.getProductsByClinicRaw(clinicId);
            Map<UUID, String> productNames = products.stream()
                    .collect(Collectors.toMap(Product::getId, Product::getName));

            // 3. Generează Excel
            InputStream excel = inventoryExportService.exportToExcel(logs, userNames, productNames);
            byte[] fileBytes = excel.readAllBytes();

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=inventory_logs.xlsx")
                    .header("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                    .body(fileBytes);

        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}

package com.asusoftware.DentaTrack_Backend.inventoryLog.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryLogDto {
    private UUID id;
    private UUID productId;
    private String actionType;
    private int quantity;
    private String reason;
    private UUID userId;
    private String userNameOfAction;
    private LocalDateTime timestamp;
}

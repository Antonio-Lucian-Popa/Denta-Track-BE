package com.asusoftware.DentaTrack_Backend.product.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private UUID id;
    private String name;
    private String category;
    private String unit;
    private int quantity;
    private int lowStockThreshold;
    private LocalDate expirationDate;
    private UUID clinicId;
    private UUID userId;
}

package com.asusoftware.DentaTrack_Backend.product.model.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ProductDto {
    private UUID id;
    private String name;
    private String category;
    private String unit;
    private Integer quantity;
    private Integer lowStockThreshold;
    private LocalDate expirationDate;
    private UUID clinicId;
    private UUID userId;
    private LocalDateTime createdAt;
}

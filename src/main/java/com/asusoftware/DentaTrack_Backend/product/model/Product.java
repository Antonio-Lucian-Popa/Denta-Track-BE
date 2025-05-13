package com.asusoftware.DentaTrack_Backend.product.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;

    private String category;
    private String unit;

    private Integer quantity = 0;

    @Column(name = "low_stock_threshold")
    private Integer lowStockThreshold;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @Column(name = "clinic_id", nullable = false)
    private UUID clinicId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}


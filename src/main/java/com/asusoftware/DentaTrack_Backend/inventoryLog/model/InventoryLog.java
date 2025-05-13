package com.asusoftware.DentaTrack_Backend.inventoryLog.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "inventory_log")
public class InventoryLog {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Column(name = "action_type", nullable = false)
    private String actionType; // IN / OUT

    @Column(nullable = false)
    private Integer quantity;

    private String reason;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "clinic_id", nullable = false)
    private UUID clinicId;

    private LocalDateTime timestamp = LocalDateTime.now();

}


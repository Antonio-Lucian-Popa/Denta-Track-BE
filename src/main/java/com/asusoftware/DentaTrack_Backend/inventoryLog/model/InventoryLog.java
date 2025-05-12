package com.asusoftware.DentaTrack_Backend.inventoryLog.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

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

    private LocalDateTime timestamp = LocalDateTime.now();

}


package com.asusoftware.DentaTrack_Backend.product.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStockDto {
    private String actionType; // IN / OUT
    private int quantity;
    private String reason;
}


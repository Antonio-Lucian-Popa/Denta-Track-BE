package com.asusoftware.DentaTrack_Backend.dashboard.model.dto;

import com.asusoftware.DentaTrack_Backend.product.model.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDto {
    private int totalAppointments;
    private int completedAppointments;
    private int canceledAppointments;
    private int lowStockCount;
    private int expiredCount;
    private int consumptionLogsThisMonth;

    private List<Product> lowStockProducts;
    private List<Product> expiredProducts;
}


package com.asusoftware.DentaTrack_Backend.dashboard.service;

import com.asusoftware.DentaTrack_Backend.appointment.repository.AppointmentRepository;
import com.asusoftware.DentaTrack_Backend.dashboard.model.dto.DashboardDto;
import com.asusoftware.DentaTrack_Backend.inventoryLog.repository.InventoryLogRepository;
import com.asusoftware.DentaTrack_Backend.product.model.Product;
import com.asusoftware.DentaTrack_Backend.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final AppointmentRepository appointmentRepository;
    private final ProductRepository productRepository;
    private final InventoryLogRepository inventoryLogRepository;

    public DashboardDto getDashboard(UUID clinicId) {
        LocalDateTime startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime endOfMonth = startOfMonth.plusMonths(1).minusSeconds(1);

        int totalAppointments = appointmentRepository.countByClinicIdAndDateTimeBetween(clinicId, startOfMonth, endOfMonth);
        int completedAppointments = appointmentRepository.countByClinicIdAndStatusAndDateTimeBetween(clinicId, "COMPLETED", startOfMonth, endOfMonth);
        int canceledAppointments = appointmentRepository.countByClinicIdAndStatusAndDateTimeBetween(clinicId, "CANCELED", startOfMonth, endOfMonth);

        List<Product> lowStockProducts = productRepository.findByClinicIdAndQuantityLessThanEqual(clinicId, 5); // sau threshold
        List<Product> expiredProducts = productRepository.findByClinicIdAndExpirationDateBefore(clinicId, LocalDate.now());

        int logsOut = inventoryLogRepository.countByClinicIdAndActionTypeAndTimestampBetween(clinicId, "OUT", startOfMonth, endOfMonth);

        return DashboardDto.builder()
                .totalAppointments(totalAppointments)
                .completedAppointments(completedAppointments)
                .canceledAppointments(canceledAppointments)
                .lowStockCount(lowStockProducts.size())
                .expiredCount(expiredProducts.size())
                .consumptionLogsThisMonth(logsOut)
                .lowStockProducts(lowStockProducts)
                .expiredProducts(expiredProducts)
                .build();
    }
}


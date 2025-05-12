package com.asusoftware.DentaTrack_Backend.inventoryLog.service;

import com.asusoftware.DentaTrack_Backend.inventoryLog.model.InventoryLog;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InventoryExportService {

    public InputStream exportToExcel(List<InventoryLog> logs,
                                     Map<UUID, String> userNames,
                                     Map<UUID, String> productNames) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Inventory Logs");

            Row header = sheet.createRow(0);
            String[] columns = {"Produs", "Acțiune", "Cantitate", "Motiv", "Utilizator", "Data"};

            for (int i = 0; i < columns.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(columns[i]);
            }

            int rowIdx = 1;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            for (InventoryLog log : logs) {
                Row row = sheet.createRow(rowIdx++);

                String productName = productNames.getOrDefault(log.getProductId(), "Necunoscut");
                String userFullName = userNames.getOrDefault(log.getUserId(), "Necunoscut");

                row.createCell(0).setCellValue(productName);
                row.createCell(1).setCellValue(log.getActionType());
                row.createCell(2).setCellValue(log.getQuantity());
                row.createCell(3).setCellValue(log.getReason());
                row.createCell(4).setCellValue(userFullName);
                row.createCell(5).setCellValue(log.getTimestamp().format(formatter));
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        } catch (Exception e) {
            throw new RuntimeException("Nu s-a putut genera fișierul Excel", e);
        }
    }


}

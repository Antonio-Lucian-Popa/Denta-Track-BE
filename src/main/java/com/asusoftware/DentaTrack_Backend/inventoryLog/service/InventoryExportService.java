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

@Service
@RequiredArgsConstructor
public class InventoryExportService {

    public InputStream exportToExcel(List<InventoryLog> logs) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Inventory Logs");

            Row header = sheet.createRow(0);
            String[] columns = {"Product ID", "Actiune", "Cantitate", "Reason", "User ID", "Timestamp"};

            for (int i = 0; i < columns.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(columns[i]);
            }

            int rowIdx = 1;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            for (InventoryLog log : logs) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(log.getProductId().toString());
                row.createCell(1).setCellValue(log.getActionType());
                row.createCell(2).setCellValue(log.getQuantity());
                row.createCell(3).setCellValue(log.getReason());
                row.createCell(4).setCellValue(log.getUserId().toString());
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

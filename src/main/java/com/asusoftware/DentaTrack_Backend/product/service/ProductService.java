package com.asusoftware.DentaTrack_Backend.product.service;

import com.asusoftware.DentaTrack_Backend.inventoryLog.model.InventoryLog;
import com.asusoftware.DentaTrack_Backend.inventoryLog.repository.InventoryLogRepository;
import com.asusoftware.DentaTrack_Backend.product.model.Product;
import com.asusoftware.DentaTrack_Backend.product.model.dto.CreateProductDto;
import com.asusoftware.DentaTrack_Backend.product.model.dto.ProductDto;
import com.asusoftware.DentaTrack_Backend.product.model.dto.UpdateStockDto;
import com.asusoftware.DentaTrack_Backend.product.repository.ProductRepository;
import com.asusoftware.DentaTrack_Backend.user.model.User;
import com.asusoftware.DentaTrack_Backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.NotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final InventoryLogRepository logRepository;
    private final UserService userService;
    private final ModelMapper mapper;

    /**
     * Adaugă un produs nou în clinică.
     */
    @Transactional
    public ProductDto addProduct(UUID userId, CreateProductDto dto) {
        User user = userService.getById(userId);

        Product product = Product.builder()
                .id(UUID.randomUUID())
                .name(dto.getName())
                .category(dto.getCategory())
                .unit(dto.getUnit())
                .quantity(dto.getQuantity())
                .lowStockThreshold(dto.getLowStockThreshold())
                .expirationDate(dto.getExpirationDate())
                .clinicId(dto.getClinicId())
                .userId(userId)
                .createdAt(LocalDateTime.now())
                .build();

        productRepository.save(product);

        logRepository.save(InventoryLog.builder()
                .id(UUID.randomUUID())
                .productId(product.getId())
                .actionType("IN")
                .quantity(dto.getQuantity())
                .reason("Adăugare inițială")
                .userId(userId)
                .timestamp(LocalDateTime.now())
                .build());

        return mapper.map(product, ProductDto.class);
    }

    /**
     * Listează toate produsele dintr-o clinică.
     */
    public List<ProductDto> getProductsByClinic(UUID clinicId) {
        return productRepository.findByClinicId(clinicId).stream()
                .map(p -> mapper.map(p, ProductDto.class))
                .collect(Collectors.toList());
    }

    /**
     * Update stoc produs (consum / aprovizionare).
     */
    @Transactional
    public ProductDto updateStock(UUID productId, UpdateStockDto dto, UUID userId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Produsul nu a fost găsit"));

        int newQuantity = product.getQuantity();

        if ("IN".equalsIgnoreCase(dto.getActionType())) {
            newQuantity += dto.getQuantity();
        } else if ("OUT".equalsIgnoreCase(dto.getActionType())) {
            newQuantity -= dto.getQuantity();
            if (newQuantity < 0) newQuantity = 0;
        } else {
            throw new IllegalArgumentException("Tip de acțiune invalid (IN / OUT)");
        }

        product.setQuantity(newQuantity);
        productRepository.save(product);

        logRepository.save(InventoryLog.builder()
                .id(UUID.randomUUID())
                .productId(product.getId())
                .actionType(dto.getActionType())
                .quantity(dto.getQuantity())
                .reason(dto.getReason())
                .userId(userId)
                .timestamp(LocalDateTime.now())
                .build());

        return mapper.map(product, ProductDto.class);
    }

    /**
     * Returnează produsele care sunt sub pragul minim de stoc.
     */
    public List<ProductDto> getLowStockProducts(UUID clinicId) {
        return productRepository.findByClinicId(clinicId).stream()
                .filter(p -> p.getQuantity() <= p.getLowStockThreshold())
                .map(p -> mapper.map(p, ProductDto.class))
                .collect(Collectors.toList());
    }

    public Product getProductById(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Produsul cu ID-ul " + productId + " nu a fost găsit"));
    }
}

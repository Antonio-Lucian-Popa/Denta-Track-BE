package com.asusoftware.DentaTrack_Backend.product.controller;

import com.asusoftware.DentaTrack_Backend.product.model.dto.CreateProductDto;
import com.asusoftware.DentaTrack_Backend.product.model.dto.ProductDto;
import com.asusoftware.DentaTrack_Backend.product.model.dto.UpdateStockDto;
import com.asusoftware.DentaTrack_Backend.product.service.ProductService;
import com.asusoftware.DentaTrack_Backend.user.model.User;
import com.asusoftware.DentaTrack_Backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final UserService userService;

    /**
     * Adaugă un nou produs în clinică.
     */
    @PostMapping
    public ResponseEntity<ProductDto> addProduct(@AuthenticationPrincipal Jwt principal,
                                                 @RequestBody CreateProductDto dto) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        User user = userService.getByKeycloakId(keycloakId);

        ProductDto product = productService.addProduct(user.getId(), dto);
        return ResponseEntity.ok(product);
    }

    /**
     * Listează toate produsele dintr-o clinică.
     */
    @GetMapping("/clinic/{clinicId}")
    public ResponseEntity<List<ProductDto>> getProductsByClinic(@PathVariable UUID clinicId) {
        List<ProductDto> products = productService.getProductsByClinic(clinicId);
        return ResponseEntity.ok(products);
    }

    /**
     * Actualizează stocul unui produs (IN / OUT).
     */
    @PostMapping("/{productId}/stock")
    public ResponseEntity<ProductDto> updateStock(@AuthenticationPrincipal Jwt principal,
                                                  @PathVariable UUID productId,
                                                  @RequestBody UpdateStockDto dto) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        User user = userService.getByKeycloakId(keycloakId);

        ProductDto updated = productService.updateStock(productId, dto, user.getId());
        return ResponseEntity.ok(updated);
    }

    /**
     * Returnează produsele sub pragul minim de stoc pentru alertă.
     */
    @GetMapping("/clinic/{clinicId}/low-stock")
    public ResponseEntity<List<ProductDto>> getLowStock(@PathVariable UUID clinicId) {
        List<ProductDto> lowStock = productService.getLowStockProducts(clinicId);
        return ResponseEntity.ok(lowStock);
    }
}
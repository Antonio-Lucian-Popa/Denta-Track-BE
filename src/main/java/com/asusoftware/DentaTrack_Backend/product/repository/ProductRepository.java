package com.asusoftware.DentaTrack_Backend.product.repository;

import com.asusoftware.DentaTrack_Backend.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    /**
     * Returnează toate produsele asociate unei clinici.
     */
    List<Product> findByClinicId(UUID clinicId);

    /**
     * Returnează produsele dintr-o clinică aflate sub pragul minim (opțional dacă vrei direct în repo).
     */
    List<Product> findByClinicIdAndQuantityLessThanEqual(UUID clinicId, int threshold);

    /**
     * Verifică dacă un utilizator are produse înregistrate în clinică
     * (folosit pentru a determina dacă face parte din clinică).
     */
    boolean existsByClinicIdAndUserId(UUID clinicId, UUID userId);

    List<Product> findByClinicIdAndExpirationDateBefore(UUID clinicId, LocalDate date);

    @Modifying
    @Query("UPDATE Product p SET p.userId = NULL WHERE p.userId = :userId AND p.clinicId = :clinicId")
    void clearClinicUser(@Param("userId") UUID userId, @Param("clinicId") UUID clinicId);

    @Query("SELECT p FROM Product p WHERE p.expirationDate IS NOT NULL AND p.expirationDate <= :date")
    List<Product> findExpiringInNextDays(@Param("date") LocalDate date);


}

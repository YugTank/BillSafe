package com.billsafe.billsafe.purchase.repository;

import com.billsafe.billsafe.auth.entity.User;
import com.billsafe.billsafe.purchase.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, UUID>, JpaSpecificationExecutor<Purchase> {

    List<Purchase> getByUser(User user);

    Optional<Purchase> getByIdAndUser(UUID id, User user);

    long countByUser(User user);

    long countByUserAndWarrantyExpiryBefore(User user, LocalDate date);

    long countByUserAndWarrantyExpiryAfter(User user, LocalDate date);

    List<Purchase> findTop5ByUserOrderByPurchaseDateDesc(User user);

    @Query("SELECT COUNT(p) FROM Purchase p Where p.user=:user AND p.warrantyExpiry BETWEEN :today AND :endDate")
    long countExpiringSoon(@Param("user") User user, @Param("today") LocalDate today, @Param("endDate") LocalDate endDate);
}

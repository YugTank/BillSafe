package com.billsafe.billsafe.purchase.entity;

import com.billsafe.billsafe.auth.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "purchases", schema = "purchase")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Purchase {

    @Id
    @UuidGenerator
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name="product_name", nullable = false)
    private String productName;

    @Column(nullable = false)
    private String category;

    private String brand;
    private String store;

    @Column(name="purchase_date",nullable = false)
    private LocalDate purchaseDate;
    @Column(name="warranty_month",nullable = false)
    private Integer warrantyMonth;
    @Column(name="warranty_expiry",nullable = false)
    private LocalDate warrantyExpiry;

    @Column(nullable = false)
    private BigDecimal price;
    private String notes;

    @Column(name="created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name="updated_at", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}

package com.billsafe.billsafe.purchase.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseResponse {

    private UUID id;
    private String productName;
    private String category;
    private String brand;
    private LocalDate purchaseDate;
    private LocalDate warrantyExpiry;
    private String store;
    private BigDecimal price;
    private String notes;
}

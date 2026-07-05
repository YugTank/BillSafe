package com.billsafe.billsafe.purchase.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreatePurchaseRequest {

    @NotBlank
    private String productName;

    @NotBlank
    private String category;
    private String brand;

    @NotNull
    private LocalDate purchaseDate;
    @NotNull
    @Positive
    private Integer warrantyMonths;

    private String store;
    @NotNull
    @Positive
    private BigDecimal price;

    private String notes;
}

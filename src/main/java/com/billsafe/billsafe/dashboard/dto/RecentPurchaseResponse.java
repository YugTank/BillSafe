package com.billsafe.billsafe.dashboard.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecentPurchaseResponse {

    private UUID id;
    private String productName;
    private LocalDate purchaseDate;
    private LocalDate warrantyExpiry;
}

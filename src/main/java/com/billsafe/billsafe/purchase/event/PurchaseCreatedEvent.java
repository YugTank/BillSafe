package com.billsafe.billsafe.purchase.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseCreatedEvent {
    private UUID purchaseId;
    private UUID userId;
    private String email;
    private String productName;
    private LocalDate warrantyExpiry;
}

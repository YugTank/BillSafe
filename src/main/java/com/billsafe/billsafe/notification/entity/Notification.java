package com.billsafe.billsafe.notification.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "notifications", schema = "notification")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID purchaseId;

    private UUID userId;

    private String email;
    private String productName;

    @Enumerated(EnumType.STRING)
    private NotificationStatus status;

    private LocalDate reminderDate;

    @Column(nullable = false)
    private int retryCount=0;

}

package com.billsafe.billsafe.notification.consumer;

import com.billsafe.billsafe.notification.service.NotificationService;
import com.billsafe.billsafe.purchase.event.PurchaseCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PurchaseEventConsumer {

    private final NotificationService notificationService;

    @KafkaListener(topics = "purchase-created", groupId = "notification-service")
    public void consume(PurchaseCreatedEvent purchaseCreatedEvent) {
        notificationService.createNotification(purchaseCreatedEvent);
    }
}

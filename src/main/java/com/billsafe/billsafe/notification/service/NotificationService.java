package com.billsafe.billsafe.notification.service;

import com.billsafe.billsafe.notification.entity.Notification;
import com.billsafe.billsafe.notification.entity.NotificationStatus;
import com.billsafe.billsafe.notification.repository.NotificationRepository;
import com.billsafe.billsafe.purchase.entity.Purchase;
import com.billsafe.billsafe.purchase.event.PurchaseCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public void createNotification(PurchaseCreatedEvent event){
        Notification notification = new Notification();
        notification.setPurchaseId(event.getPurchaseId());
        notification.setUserId(event.getUserId());
        notification.setEmail(event.getEmail());
        notification.setProductName(event.getProductName());
        notification.setReminderDate(event.getWarrantyExpiry().minusDays(7));

        notification.setStatus(NotificationStatus.PENDING);
        notificationRepository.save(notification);

    }
}

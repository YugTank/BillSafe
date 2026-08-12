package com.billsafe.billsafe.notification.scheduler;

import com.billsafe.billsafe.notification.entity.Notification;
import com.billsafe.billsafe.notification.entity.NotificationStatus;
import com.billsafe.billsafe.notification.repository.NotificationRepository;
import com.billsafe.billsafe.notification.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationScheduler {

    private final NotificationRepository notificationRepository;
    private final EmailService emailService;

    public void processNotifications() {
        LocalDate today = LocalDate.now();

        List<Notification> notifications=notificationRepository.findByStatusAndReminderDateLessThanEqual(NotificationStatus.PENDING, today);

        for(Notification notification:notifications){
            try{
                emailService.sendWarrantyReminder(notification.getEmail(), notification.getProductName());
                notification.setStatus(NotificationStatus.SENT);
                notificationRepository.save(notification);
            }
            catch (Exception e){
                notification.setStatus(NotificationStatus.FAILED);
                notificationRepository.save(notification);

                System.out.println("Failed to send notification: "+notification.getId());
            }
        }
    }

}

package com.billsafe.billsafe.notification.repository;

import com.billsafe.billsafe.notification.entity.Notification;
import com.billsafe.billsafe.notification.entity.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    List<Notification> findByStatusAndReminderDateLessThanEqual(NotificationStatus status, LocalDate date);
}

package com.billsafe.billsafe.notification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    public void sendWarrantyReminder(String email, String productName){
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom("yugtank05@gmail.com");
        mailMessage.setTo(email);
        mailMessage.setSubject("Billsafe warranty Reminder");

        mailMessage.setText( "Your warranty for " + productName +
                " is expiring soon.\n\n" +
                "Please check your BillSafe account for more details.");

        javaMailSender.send(mailMessage);
    }
}

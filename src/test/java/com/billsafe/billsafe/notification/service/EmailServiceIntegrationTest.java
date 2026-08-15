package com.billsafe.billsafe.notification.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest(classes = {EmailService.class, MailSenderAutoConfiguration.class})
@Import(EmailService.class)
class EmailServiceIntegrationTest {

    @Autowired
    private EmailService emailService;

    @Test
    void sendWarrantyReminder() {
        String email = "muquanta36@gmail.com";
        String productName = "Test Product";

        emailService.sendWarrantyReminder(email, productName);
    }
}

package com.billsafe.billsafe.notification.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailService emailService;

    @Test
    void sendWarrantyReminder() {
        String email = "muquanta36@gmail.com";
        String productName = "Test Product";

        emailService.sendWarrantyReminder(email, productName);

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(javaMailSender).send(messageCaptor.capture());

        SimpleMailMessage capturedMessage = messageCaptor.getValue();
        assertEquals(email, capturedMessage.getTo()[0]);
        assertEquals("Billsafe warranty Reminder", capturedMessage.getSubject());
        assertEquals("Your warranty for Test Product is expiring soon.\n\nPlease check your BillSafe account for more details.", capturedMessage.getText());
    }
}
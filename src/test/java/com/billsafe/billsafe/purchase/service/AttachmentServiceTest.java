package com.billsafe.billsafe.purchase.service;

import com.billsafe.billsafe.auth.entity.User;
import com.billsafe.billsafe.common.exception.AttachmentNotFoundException;
import com.billsafe.billsafe.common.exception.PurchaseNotFoundException;
import com.billsafe.billsafe.common.security.CurrentUserService;
import com.billsafe.billsafe.purchase.entity.Attachment;
import com.billsafe.billsafe.purchase.entity.AttachmentType;
import com.billsafe.billsafe.purchase.entity.Purchase;
import com.billsafe.billsafe.purchase.repository.AttachmentRepository;
import com.billsafe.billsafe.purchase.repository.PurchaseRepository;
import com.billsafe.billsafe.storage.StorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AttachmentServiceTest {

    @Mock
    private AttachmentRepository attachmentRepository;

    @Mock
    private PurchaseRepository purchaseRepository;

    @Mock
    private CurrentUserService currentUserService;

    @Mock
    private StorageService storageService;

    @Mock
    private MultipartFile multipartFile;

    @Mock
    private Resource resource;

    @InjectMocks
    private AttachmentService attachmentService;

    private User user;
    private Purchase purchase;
    private Attachment attachment;
    private UUID purchaseId;
    private UUID attachmentId;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail("test@example.com");

        purchase = new Purchase();
        purchase.setId(UUID.randomUUID());
        purchase.setUser(user);

        attachment = new Attachment();
        attachment.setId(UUID.randomUUID());
        attachment.setPurchase(purchase);
        attachment.setFileName("test-file.pdf");
        attachment.setFileType(AttachmentType.BILL);
        attachment.setFilePath("bills/test-file.pdf");

        purchaseId = purchase.getId();
        attachmentId = attachment.getId();
    }

    @Test
    void uploadAttachment_Success() {
        // Arrange
        when(currentUserService.getCurrentUser()).thenReturn(user);
        when(purchaseRepository.getByIdAndUser(purchaseId, user)).thenReturn(Optional.of(purchase));
        when(multipartFile.getOriginalFilename()).thenReturn("test-file.pdf");
        when(storageService.upload(any(MultipartFile.class), eq("bills"))).thenReturn("bills/test-file.pdf");
        when(attachmentRepository.save(any(Attachment.class))).thenReturn(attachment);

        // Act
        attachmentService.uploadAttachment(purchaseId, multipartFile, AttachmentType.BILL);

        // Assert
        verify(currentUserService).getCurrentUser();
        verify(purchaseRepository).getByIdAndUser(purchaseId, user);
        verify(storageService).upload(multipartFile, "bills");
        verify(attachmentRepository).save(any(Attachment.class));
    }

    @Test
    void uploadAttachment_WarrantyFolder() {
        // Arrange
        when(currentUserService.getCurrentUser()).thenReturn(user);
        when(purchaseRepository.getByIdAndUser(purchaseId, user)).thenReturn(Optional.of(purchase));
        when(multipartFile.getOriginalFilename()).thenReturn("warranty.pdf");
        when(storageService.upload(any(MultipartFile.class), eq("warranty"))).thenReturn("warranty/warranty.pdf");
        when(attachmentRepository.save(any(Attachment.class))).thenReturn(attachment);

        // Act
        attachmentService.uploadAttachment(purchaseId, multipartFile, AttachmentType.WARRANTY_CARD);

        // Assert
        verify(storageService).upload(multipartFile, "warranty");
    }

    @Test
    void uploadAttachment_PurchaseNotFound() {
        // Arrange
        when(currentUserService.getCurrentUser()).thenReturn(user);
        when(purchaseRepository.getByIdAndUser(purchaseId, user)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PurchaseNotFoundException.class, () -> 
            attachmentService.uploadAttachment(purchaseId, multipartFile, AttachmentType.BILL)
        );

        verify(storageService, never()).upload(any(), any());
        verify(attachmentRepository, never()).save(any());
    }

    @Test
    void getAttachments_Success() {
        // Arrange
        when(purchaseRepository.findById(purchaseId)).thenReturn(Optional.of(purchase));
        when(attachmentRepository.findByPurchase(purchase)).thenReturn(java.util.List.of(attachment));

        // Act
        var responses = attachmentService.getAttachments(purchaseId);

        // Assert
        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(attachment.getId(), responses.get(0).getId());
        assertEquals(attachment.getFileName(), responses.get(0).getFileName());
        assertEquals(attachment.getFileType(), responses.get(0).getFileType());
    }

    @Test
    void getAttachments_PurchaseNotFound() {
        // Arrange
        when(purchaseRepository.findById(purchaseId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PurchaseNotFoundException.class, () -> 
            attachmentService.getAttachments(purchaseId)
        );
    }

    @Test
    void downloadAttachment_Success() {
        // Arrange
        when(currentUserService.getCurrentUser()).thenReturn(user);
        when(attachmentRepository.findByIdAndPurchase_User(attachmentId, user)).thenReturn(Optional.of(attachment));
        when(storageService.download("bills/test-file.pdf")).thenReturn(resource);

        // Act
        Resource result = attachmentService.downloadAttachment(attachmentId, purchaseId);

        // Assert
        assertNotNull(result);
        verify(currentUserService).getCurrentUser();
        verify(attachmentRepository).findByIdAndPurchase_User(attachmentId, user);
        verify(storageService).download("bills/test-file.pdf");
    }

    @Test
    void downloadAttachment_AttachmentNotFound() {
        // Arrange
        when(currentUserService.getCurrentUser()).thenReturn(user);
        when(attachmentRepository.findByIdAndPurchase_User(attachmentId, user)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AttachmentNotFoundException.class, () -> 
            attachmentService.downloadAttachment(attachmentId, purchaseId)
        );

        verify(storageService, never()).download(any());
    }
}

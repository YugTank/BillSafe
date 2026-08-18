package com.billsafe.billsafe.purchase.controller;

import com.billsafe.billsafe.purchase.dto.AttachmentResponse;
import com.billsafe.billsafe.purchase.dto.AttachmentUploadResponse;
import com.billsafe.billsafe.purchase.entity.AttachmentType;
import com.billsafe.billsafe.purchase.service.AttachmentService;
import org.springframework.core.io.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class AttachmentController {

    private final AttachmentService attachmentService;

    @PostMapping("/{purchaseId}/attachments")
    public AttachmentUploadResponse uploadAttachment(@PathVariable UUID purchaseId, @RequestParam("file") MultipartFile file, @RequestParam("type") AttachmentType attachmentType){
         return attachmentService.uploadAttachment(purchaseId,file,attachmentType);
    }

    @GetMapping("/{purchaseId}/attachments")
    public List<AttachmentResponse> getAttachments(@PathVariable UUID purchaseId){
        return attachmentService.getAttachments(purchaseId);
    }


    @GetMapping("/{purchaseId}/attachments/{attachmentId}/download")
    public ResponseEntity<Resource> download(@PathVariable UUID purchaseId, @PathVariable UUID attachmentId){
        Resource resource=attachmentService.downloadAttachment(purchaseId, attachmentId);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\""+attachmentService.findAttachmentById(attachmentId).getFileName()+ "\"")
                .body(resource);
    }

    @DeleteMapping("/{purchaseId}/attachments/{attachmentId}")
    public ResponseEntity<Void> deleteAttachment(@PathVariable UUID purchaseId, @PathVariable UUID attachmentId){
        attachmentService.deleteAttachment(purchaseId, attachmentId);
        return ResponseEntity.noContent().build();
    }
}

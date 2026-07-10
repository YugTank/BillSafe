package com.billsafe.billsafe.purchase.controller;

import com.billsafe.billsafe.purchase.dto.AttachmentResponse;
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
    public void uploadAttachment(@PathVariable UUID purchaseId, @RequestParam("file") MultipartFile file, @RequestParam("type") AttachmentType attachmentType){
         attachmentService.uploadAttachment(purchaseId,file,attachmentType);
    }

    @GetMapping("/{purchaseId}/attachments")
    public List<AttachmentResponse> getAttachments(@PathVariable UUID purchaseId){
        return attachmentService.getAttachments(purchaseId);
    }


    @GetMapping("/{attachmentId}/download")
    public ResponseEntity<Resource> download(@PathVariable UUID attachmentId){
        Resource resource=attachmentService.downloadAttachment(attachmentId);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\""+resource.getFilename()+ "\"")
                .body(resource);
    }
}

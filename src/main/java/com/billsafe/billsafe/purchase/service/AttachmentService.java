package com.billsafe.billsafe.purchase.service;

import com.billsafe.billsafe.auth.entity.User;
import com.billsafe.billsafe.common.exception.AttachmentNotFoundException;
import com.billsafe.billsafe.common.exception.FileStorageException;
import com.billsafe.billsafe.common.exception.PurchaseNotFoundException;
import com.billsafe.billsafe.common.security.CurrentUserService;
import com.billsafe.billsafe.purchase.dto.AttachmentResponse;
import com.billsafe.billsafe.purchase.entity.Attachment;
import com.billsafe.billsafe.purchase.entity.AttachmentType;
import com.billsafe.billsafe.purchase.entity.Purchase;
import com.billsafe.billsafe.purchase.repository.AttachmentRepository;
import com.billsafe.billsafe.purchase.repository.PurchaseRepository;
import com.billsafe.billsafe.storage.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final PurchaseRepository purchaseRepository;
    private final CurrentUserService currentUserService;
    private final StorageService storageService;

    public void uploadAttachment(UUID purchaseId, MultipartFile file, AttachmentType attachmentType){
        User user=currentUserService.getCurrentUser();
        Purchase purchase=purchaseRepository.getByIdAndUser(purchaseId, user).orElseThrow(()->new PurchaseNotFoundException("Purchase not found"));

        String folder;
        if(attachmentType==AttachmentType.BILL){
            folder="bills";
        }else{
            folder="warranty";
        }
        String path=storageService.upload(file, folder);

        Attachment attachment=Attachment.builder()
                .purchase(purchase)
                .fileName(file.getOriginalFilename())
                .fileType(attachmentType)
                .filePath(path)
                .build();

        attachment=attachmentRepository.save(attachment);
    }

    public List<AttachmentResponse> getAttachments(UUID purchaseId){
        Purchase purchase=purchaseRepository.findById(purchaseId).orElseThrow(()->new PurchaseNotFoundException("Purchase not found"));

        List<Attachment> attachments=attachmentRepository.findByPurchase(purchase);

        return attachments.stream().map(attachment->new AttachmentResponse(
                attachment.getId(),
                attachment.getFileName(),
                attachment.getFileType(),
                attachment.getUploadedAt()
        )).toList();
    }

    public Resource downloadAttachment(UUID attachmentId){
        User user=currentUserService.getCurrentUser();
        Attachment attachment=attachmentRepository.findByIdAndPurchase_User(attachmentId,user).orElseThrow(()->new AttachmentNotFoundException("Attachment not found"));

        try{
            Path path= Paths.get(attachment.getFilePath());
            return new UrlResource(path.toUri());
        }
        catch (MalformedURLException e){
            throw new FileStorageException("Failed to download attachment",e);
        }
    }
}

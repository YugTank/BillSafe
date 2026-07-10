package com.billsafe.billsafe.purchase.dto;

import com.billsafe.billsafe.purchase.entity.AttachmentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AttachmentResponse {

    private UUID id;
    private String fileName;
    private AttachmentType fileType;
    LocalDateTime uploadedAt;
}

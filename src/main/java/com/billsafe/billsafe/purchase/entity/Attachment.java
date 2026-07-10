package com.billsafe.billsafe.purchase.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="attachments", schema = "purchase")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Attachment {

    @Id
    @UuidGenerator
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_id", nullable = false)
    private Purchase purchase;

    @Column(name="file_name",nullable = false)
    private String fileName;

    @Enumerated(EnumType.STRING)
    @Column(name="file_type",nullable = false)
    private AttachmentType fileType;

    @Column(name="file_path",nullable = false)
    private String filePath;

    @Column(name="uploaded_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime uploadedAt;
}

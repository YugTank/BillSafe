package com.billsafe.billsafe.purchase.repository;

import com.billsafe.billsafe.auth.entity.User;
import com.billsafe.billsafe.purchase.entity.Attachment;
import com.billsafe.billsafe.purchase.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, UUID> {

    List<Attachment> findByPurchase(Purchase purchase);

    Optional<Attachment> findByIdAndPurchase_User(UUID id, User user);
}

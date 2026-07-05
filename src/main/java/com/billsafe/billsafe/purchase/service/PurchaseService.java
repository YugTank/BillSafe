package com.billsafe.billsafe.purchase.service;

import com.billsafe.billsafe.auth.entity.User;
import com.billsafe.billsafe.auth.security.CustomUserDetails;
import com.billsafe.billsafe.common.security.CurrentUserService;
import com.billsafe.billsafe.purchase.PurchaseRepository;
import com.billsafe.billsafe.purchase.dto.CreatePurchaseRequest;
import com.billsafe.billsafe.purchase.dto.PurchaseResponse;
import com.billsafe.billsafe.purchase.entity.Purchase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final CurrentUserService currentUserService;

    public PurchaseResponse createPurchase(CreatePurchaseRequest createPurchaseRequest) {
       User user= currentUserService.getCurrentUser();

        LocalDate warrantyExpiry=createPurchaseRequest.getPurchaseDate().plusMonths(createPurchaseRequest.getWarrantyMonths());
        Purchase purchase=Purchase.builder()
                .user(user)
                .productName(createPurchaseRequest.getProductName())
                .category(createPurchaseRequest.getCategory())
                .brand(createPurchaseRequest.getBrand())
                .purchaseDate(createPurchaseRequest.getPurchaseDate())
                .warrantyMonth(createPurchaseRequest.getWarrantyMonths())
                .warrantyExpiry(warrantyExpiry)
                .price(createPurchaseRequest.getPrice())
                .notes(createPurchaseRequest.getNotes())
                .build();

        purchase=purchaseRepository.save(purchase);
        return toResponse(purchase);
    }

    private PurchaseResponse toResponse(Purchase purchase){
        return new PurchaseResponse(
                purchase.getId(),
                purchase.getProductName(),
                purchase.getCategory(),
                purchase.getBrand(),
                purchase.getPurchaseDate(),
                purchase.getWarrantyExpiry(),
                purchase.getStore(),
                purchase.getPrice(),
                purchase.getNotes()
        );
    }
}

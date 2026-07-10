package com.billsafe.billsafe.purchase.service;

import com.billsafe.billsafe.auth.entity.User;
import com.billsafe.billsafe.common.exception.PurchaseNotFoundException;
import com.billsafe.billsafe.common.security.CurrentUserService;
import com.billsafe.billsafe.purchase.dto.UpdatePurchaseRequest;
import com.billsafe.billsafe.purchase.repository.PurchaseRepository;
import com.billsafe.billsafe.purchase.dto.CreatePurchaseRequest;
import com.billsafe.billsafe.purchase.dto.PurchaseResponse;
import com.billsafe.billsafe.purchase.entity.Purchase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
                .store(createPurchaseRequest.getStore())
                .purchaseDate(createPurchaseRequest.getPurchaseDate())
                .warrantyMonth(createPurchaseRequest.getWarrantyMonths())
                .warrantyExpiry(warrantyExpiry)
                .price(createPurchaseRequest.getPrice())
                .notes(createPurchaseRequest.getNotes())
                .build();

        purchase=purchaseRepository.save(purchase);
        return toResponse(purchase);
    }

    public List<PurchaseResponse> getMyPurchase(){
        User user=currentUserService.getCurrentUser();
        List<Purchase> purchases=purchaseRepository.getByUser(user);
        return purchases.stream().map(this::toResponse).toList();
    }

    public Optional<PurchaseResponse> getPurchaseById(UUID id){
        User user=currentUserService.getCurrentUser();
        Optional<Purchase> purchase=purchaseRepository.getByIdAndUser(id, user);
        return purchase.map(this::toResponse);
    }

    public PurchaseResponse updateById(UUID id, UpdatePurchaseRequest request){
        User user=currentUserService.getCurrentUser();
        Purchase purchase=purchaseRepository.getByIdAndUser(id, user).orElseThrow(()->new PurchaseNotFoundException("Purchase not found"));

            purchase.setProductName(request.getProductName());
            purchase.setCategory(request.getCategory());
            purchase.setBrand(request.getBrand());
            purchase.setStore(request.getStore());
            purchase.setPurchaseDate(request.getPurchaseDate());
            purchase.setWarrantyMonth(request.getWarrantyMonths());
            purchase.setWarrantyExpiry(request.getPurchaseDate().plusMonths(request.getWarrantyMonths()));
            purchase.setPrice(request.getPrice());
            purchase.setNotes(request.getNotes());
            purchase=purchaseRepository.save(purchase);

            return toResponse(purchase);
    }

    public void deleteById(UUID id){
        User user=currentUserService.getCurrentUser();
        Purchase purchase=purchaseRepository.getByIdAndUser(id, user).orElseThrow(()->new PurchaseNotFoundException("Purchase not found"));
            purchaseRepository.delete(purchase);
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

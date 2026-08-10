package com.billsafe.billsafe.purchase.controller;

import com.billsafe.billsafe.purchase.dto.CreatePurchaseRequest;
import com.billsafe.billsafe.purchase.dto.PurchaseFilterRequest;
import com.billsafe.billsafe.purchase.dto.PurchaseResponse;
import com.billsafe.billsafe.purchase.dto.UpdatePurchaseRequest;
import com.billsafe.billsafe.purchase.service.PurchaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/purchases")
public class PurchaseController {
    private final PurchaseService purchaseService;

    @PostMapping
    public PurchaseResponse createPurchase(@Valid @RequestBody CreatePurchaseRequest createPurchaseRequest) {
        return purchaseService.createPurchase(createPurchaseRequest);
    }

//    @GetMapping
//    public List<PurchaseResponse> getMyPurchase() {
//        return purchaseService.getMyPurchase();
//    }

    @GetMapping("/{id}")
    public Optional<PurchaseResponse> getPurchaseById(@PathVariable UUID id) {
        return purchaseService.getPurchaseById(id);
    }

    @PutMapping("/{id}")
    public PurchaseResponse updateById(@PathVariable UUID id, @Valid @RequestBody UpdatePurchaseRequest request) {
        return purchaseService.updateById(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable UUID id) {
        purchaseService.deleteById(id);
    }

    @GetMapping
    public ResponseEntity<Page<PurchaseResponse>> searchPurchase(PurchaseFilterRequest request, Pageable pageable){
        return ResponseEntity.ok(purchaseService.searchPurchase(request, pageable));
    }
}

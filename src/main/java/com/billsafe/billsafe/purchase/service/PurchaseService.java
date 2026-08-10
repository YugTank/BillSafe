package com.billsafe.billsafe.purchase.service;

import com.billsafe.billsafe.auth.entity.User;
import com.billsafe.billsafe.common.cache.RedisCacheService;
import com.billsafe.billsafe.common.util.CacheKeys;
import com.billsafe.billsafe.common.exception.PurchaseNotFoundException;
import com.billsafe.billsafe.common.security.CurrentUserService;
import com.billsafe.billsafe.purchase.dto.PurchaseFilterRequest;
import com.billsafe.billsafe.purchase.dto.UpdatePurchaseRequest;
import com.billsafe.billsafe.purchase.event.PurchaseCreatedEvent;
import com.billsafe.billsafe.purchase.producer.PurchaseEventProducer;
import com.billsafe.billsafe.purchase.repository.PurchaseRepository;
import com.billsafe.billsafe.purchase.dto.CreatePurchaseRequest;
import com.billsafe.billsafe.purchase.dto.PurchaseResponse;
import com.billsafe.billsafe.purchase.entity.Purchase;
import com.billsafe.billsafe.purchase.specification.PurchaseSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final CurrentUserService currentUserService;
    private final RedisCacheService redisCacheService;
    private final PurchaseEventProducer purchaseEventProducer;

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
        redisCacheService.evict(CacheKeys.dashboard(user.getId()));

        PurchaseCreatedEvent event=new PurchaseCreatedEvent(purchase.getId(), user.getId(), user.getEmail(), createPurchaseRequest.getProductName(), warrantyExpiry);
        purchaseEventProducer.purchaseCreated(event);
        return toResponse(purchase);
    }

    public List<PurchaseResponse> getMyPurchase(){
        User user=currentUserService.getCurrentUser();
        List<Purchase> purchases=purchaseRepository.getByUser(user);
        return purchases.stream().map(this::toResponse).toList();
    }

    public Optional<PurchaseResponse> getPurchaseById(UUID id){

        String cacheKey = CacheKeys.purchase(id);
        PurchaseResponse cachedPurchase = redisCacheService.get(cacheKey, PurchaseResponse.class);
        if(cachedPurchase!=null){
            log.info("Cache HIT: {}",id);
            return Optional.of(cachedPurchase);
        }

        log.info("Cache MISS: {}",id);
        User user=currentUserService.getCurrentUser();
        Optional<Purchase> purchase=purchaseRepository.getByIdAndUser(id, user);
        Optional<PurchaseResponse> response = purchase.map(this::toResponse);
        response.ifPresent(r -> redisCacheService.put(cacheKey, r, java.time.Duration.ofMinutes(10)));
        return response;
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

            String cacheKey = CacheKeys.purchase(id);
            redisCacheService.put(cacheKey, toResponse(purchase), java.time.Duration.ofMinutes(10));
            redisCacheService.evict(CacheKeys.dashboard(user.getId()));
            return toResponse(purchase);
    }

    public void deleteById(UUID id){
        User user=currentUserService.getCurrentUser();
        Purchase purchase=purchaseRepository.getByIdAndUser(id, user).orElseThrow(()->new PurchaseNotFoundException("Purchase not found"));
            purchaseRepository.delete(purchase);
            redisCacheService.evict(CacheKeys.purchase(id));
                redisCacheService.evict(CacheKeys.dashboard(user.getId()));
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

    public Page<PurchaseResponse> searchPurchase(PurchaseFilterRequest purchaseFilterRequest, Pageable pageable){
        User user=currentUserService.getCurrentUser();

        Specification<Purchase> specification=PurchaseSpecification.belongsToUser(user)
                .and(PurchaseSpecification.hasCategory(purchaseFilterRequest.getCategory()))
                .and(PurchaseSpecification.hasBrand(purchaseFilterRequest.getBrand()));

        Page<Purchase> purchases=purchaseRepository.findAll(specification, pageable);
        return purchases.map(this::toResponse);
    }
}

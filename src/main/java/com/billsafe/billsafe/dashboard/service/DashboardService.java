package com.billsafe.billsafe.dashboard.service;

import com.billsafe.billsafe.auth.entity.User;
import com.billsafe.billsafe.common.cache.RedisCacheService;
import com.billsafe.billsafe.common.security.CurrentUserService;
import com.billsafe.billsafe.common.util.CacheKeys;
import com.billsafe.billsafe.dashboard.dto.DashboardResponse;
import com.billsafe.billsafe.dashboard.dto.RecentPurchaseResponse;
import com.billsafe.billsafe.purchase.entity.Purchase;
import com.billsafe.billsafe.purchase.repository.PurchaseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardService {

    private final PurchaseRepository purchaseRepository;
    private final CurrentUserService currentUserService;
    private final RedisCacheService redisCacheService;

    public DashboardResponse getDashboard(){
        String key= CacheKeys.dashboard(currentUserService.getCurrentUser().getId());

        DashboardResponse cached=redisCacheService.get(key, DashboardResponse.class);

        if(cached!=null){
            log.info("Dashboard Cache HIT: {}",key);
            return cached;
        }

        log.info("Dashboard Cache MISS: {}",key);
        User user=currentUserService.getCurrentUser();
        LocalDate today=LocalDate.now();

        long totalPurchases=purchaseRepository.countByUser(user);

        long activeWarranties=purchaseRepository.countByUserAndWarrantyExpiryAfter(user, today);

        long expiredWarranties=purchaseRepository.countByUserAndWarrantyExpiryBefore(user, today);

        long expiringSoon=purchaseRepository.countExpiringSoon(user, today, today.plusDays(30));

        List<RecentPurchaseResponse> recentPurchases=purchaseRepository.findTop5ByUserOrderByPurchaseDateDesc(user)
                .stream()
                .map(this::toRecentPurchaseResponse)
                .toList();

        DashboardResponse dashboardResponse=DashboardResponse.builder()
                .totalPurchases(totalPurchases)
                .activeWarranties(activeWarranties)
                .expiredWarranties(expiredWarranties)
                .expiringSoon(expiringSoon)
                .recentPurchases(recentPurchases)
                .build();

        redisCacheService.put(key, dashboardResponse, Duration.ofMinutes(5));
        return dashboardResponse;
    }

    private RecentPurchaseResponse toRecentPurchaseResponse(Purchase purchase) {

        return RecentPurchaseResponse.builder()
                .id(purchase.getId())
                .productName(purchase.getProductName())
                .purchaseDate(purchase.getPurchaseDate())
                .warrantyExpiry(purchase.getWarrantyExpiry())
                .build();
    }
}

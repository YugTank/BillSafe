package com.billsafe.billsafe.dashboard.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DashboardResponse {

    private Long totalPurchases;
    private Long activeWarranties;
    private Long expiredWarranties;
    private Long expiringSoon;
    private List<RecentPurchaseResponse> recentPurchases;
}

package com.billsafe.billsafe.common.util;

import java.util.UUID;

public final class CacheKeys {

    private static final String PURCHASE_PREFIX = "purchase:";

    private CacheKeys() {
    }

    public static String purchase(UUID id) {
        return PURCHASE_PREFIX + id;
    }

    public static String dashboard(UUID userId){
        return "dashboard:user:"+userId;
    }
}

package com.pos.backend.security;

public class TenantContext {

    private static final ThreadLocal<Long> currentShopId = new ThreadLocal<>();

    public static void setShopId(Long shopId) {
        currentShopId.set(shopId);
    }

    public static Long getShopId() {
        return currentShopId.get();
    }

    public static void clear() {
        currentShopId.remove();
    }
}
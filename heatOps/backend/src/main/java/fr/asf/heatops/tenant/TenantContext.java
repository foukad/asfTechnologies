package fr.asf.heatops.tenant;

import java.util.UUID;

public class TenantContext {

    private static final ThreadLocal<UUID> CURRENT_TENANT = new ThreadLocal<>();

    public static void setTenant(UUID tenantId) {
        CURRENT_TENANT.set(tenantId);
    }

    public static UUID getTenant() {
        return CURRENT_TENANT.get();
    }

    public static void clear() {
        CURRENT_TENANT.remove();
    }
}

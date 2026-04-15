package fr.asf.heatops.tenant;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.LocalDateTime;
import java.util.UUID;

public class TenantEntityListener {

@PrePersist public void prePersist(BaseTenantEntity entity) {
    LocalDateTime now = LocalDateTime.now();
entity.setCreatedAt(now);
entity.setUpdatedAt(now);
// Injection automatique du tenant
if (entity.getCompanyId() == null) {
    entity.setCompanyId(TenantContext.getTenant());
}
entity.setActive(true);
}

@PreUpdate public void preUpdate(BaseTenantEntity entity) {
entity.setUpdatedAt(LocalDateTime.now()); }
}

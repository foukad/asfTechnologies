package fr.asf.heatops.tenant;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public abstract class TenantAwareService<T extends BaseTenantEntity, ID> {

    protected abstract JpaRepository<T, ID> getRepository();

    public T create(T entity) {
        // companyId sera injecté par l’EntityListener
        return getRepository().save(entity);
    }

    public List<T> findAll() {
        return getRepository().findAll(); // filtré automatiquement
    }

    public T findById(ID id) {
        return getRepository().findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));
    }
}

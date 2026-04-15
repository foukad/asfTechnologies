package fr.asf.heatops.tenant;

import jakarta.persistence.EntityManager;
import org.hibernate.CallbackException;
import org.hibernate.Interceptor;
import org.hibernate.Session;
import org.hibernate.type.Type;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.UUID;

@Component
public class TenantHibernateInterceptor implements Interceptor {

    private final ObjectProvider<EntityManager> entityManagerProvider;

    public TenantHibernateInterceptor(ObjectProvider<EntityManager> entityManagerProvider) {
        this.entityManagerProvider = entityManagerProvider;
    }

    @Override
    public boolean onLoad(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) throws CallbackException {
        applyTenantFilter();
        return false;
    }

    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) throws CallbackException {
        return false;
    }


    public void applyTenantFilter() {
        UUID tenant = TenantContext.getTenant();
        if (tenant != null) {
            EntityManager entityManager = entityManagerProvider.getIfAvailable();
            if (entityManager != null) {
                Session session = entityManager.unwrap(Session.class);
                session.enableFilter("tenantFilter")
                        .setParameter("tenantId", tenant);

                session.enableFilter("activeFilter");
            }
        }
    }
}

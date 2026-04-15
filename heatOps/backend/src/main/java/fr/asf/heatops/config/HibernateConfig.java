package fr.asf.heatops.config;

import fr.asf.heatops.tenant.TenantHibernateInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class HibernateConfig {

    private final TenantHibernateInterceptor interceptor;

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer() {
        return props -> {
            // Passer l'instance directement plutôt que le nom de la classe
            props.put("hibernate.session_factory.interceptor", interceptor);
        };
    }
}

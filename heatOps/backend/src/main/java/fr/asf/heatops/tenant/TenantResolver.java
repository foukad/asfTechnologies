package fr.asf.heatops.tenant;


import fr.asf.heatops.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TenantResolver {

    private final JwtService jwtService;

    public UUID resolveTenant(HttpServletRequest request) {

        // 1. Priorité : JWT
        String auth = request.getHeader("Authorization");
        if (auth != null && auth.startsWith("Bearer ")) {
            String token = auth.substring(7);
            String tenantId = jwtService.extractTenant(token);
            if (tenantId != null && !tenantId.isBlank()) {
                return UUID.fromString(tenantId);
            }
        }

        // 2. Option : header X-Tenant-ID (utile pour tests, batchs, admin)
        String headerTenant = request.getHeader("X-Tenant-ID");
        if (headerTenant != null && !headerTenant.isBlank()) {
            return UUID.fromString(headerTenant);
        }

        // 3. Option : sous-domaine (client1.monsite.com)
        // Exemple :
        // String host = request.getServerName();
        // extraire tenant depuis host si besoin

        // 4. Aucun tenant trouvé
        return null;
    }
}



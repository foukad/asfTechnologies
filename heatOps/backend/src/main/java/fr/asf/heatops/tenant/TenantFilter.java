package fr.asf.heatops.tenant;

import fr.asf.heatops.security.JwtService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TenantFilter extends OncePerRequestFilter {

    private final TenantResolver tenantResolver;
    private final TenantHibernateInterceptor interceptor;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Endpoints publics
        String path = request.getServletPath();
        if (path.startsWith("/v3/api-docs")
                || path.startsWith("/swagger-ui")
                || path.equals("/swagger-ui.html")
                || path.startsWith("/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        UUID tenant = tenantResolver.resolveTenant(request);

        if (tenant != null) {
            TenantContext.setTenant(tenant);
            interceptor.applyTenantFilter();
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }
}

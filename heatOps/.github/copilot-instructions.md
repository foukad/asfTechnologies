# HeatOps – Copilot Instructions

## Vue d'ensemble

Application de gestion d'interventions terrain pour techniciens CVC (chauffage, ventilation, climatisation).  
Architecture : **Spring Boot 3.4.3 / Java 17** (backend) + **Next.js 16 / React 19 / TypeScript** (frontend) + stack observabilité Docker (OTEL → Jaeger + Prometheus).

```
backend/          → API REST Spring Boot, PostgreSQL, Flyway, MapStruct
heatops-frontend/ → Next.js App Router, Tailwind CSS, Radix UI
observability/    → docker-compose-digma.yml (otel-collector, jaeger, prometheus)
test/             → Collections Postman d'intégration
```

## Architecture multi-tenant (pattern central)

**Toutes les entités métier héritent de `BaseTenantEntity`** (`backend/src/main/java/fr/asf/heatops/tenant/`).  
Ne jamais créer d'entité sans en hériter.

Le tenant (`companyId: UUID`) est isolé par deux filtres Hibernate déclarés sur `BaseTenantEntity` :
- `tenantFilter` → `company_id = :tenantId` (filtre par company)
- `activeFilter` → `active = true` (soft delete)

**Chaîne d'exécution par requête :**
1. `TenantFilter` (servlet filter) → extrait le tenant via `TenantResolver` (JWT claim `tenant` > header `X-Tenant-ID`)
2. `TenantContext.setTenant(uuid)` → stocke dans `ThreadLocal`
3. `TenantHibernateInterceptor.applyTenantFilter()` → active les filtres Hibernate sur la session
4. `TenantEntityListener` (@PrePersist) → injecte `companyId` depuis `TenantContext` automatiquement
5. `finally` → `TenantContext.clear()`

Endpoints publics (exemptés du filtre) : `/auth/**`, `/v3/api-docs/**`, `/swagger-ui/**`.

## Soft delete

Ne jamais appeler `repository.deleteById()` sur les entités tenant. Utiliser `entity.deactivate()` puis `save()` :
```java
client.deactivate(); // active = false
clientRepository.save(client);
```
`activeFilter` masque automatiquement les entités inactives dans toutes les requêtes.

## JWT et authentification

Le JWT contient deux claims importants : `sub` (email) et `tenant` (companyId UUID).  
`AuthService.register()` réutilise une `Company` existante (recherche case-insensitive par nom) ou en crée une nouvelle.  
`JwtService` utilise JJWT 0.12.x — utiliser `Jwts.builder()` et `Jwts.parser().verifyWith(...)`.

## Frontend : auth et appels API

- **Auth** : JWT et tenantId stockés dans deux cookies (`token`, `tenantId`). Hook `useAuth` les lit ; le tenant est extrait du payload JWT décodé en base64 côté client.
- **Appels API** : passer **uniquement** par `lib/api.ts` → injecte automatiquement `Authorization: Bearer` et `X-Tenant-ID`. Ne jamais appeler `fetch` directement.
- **Middleware** `middleware.ts` : protège `/clients`, `/equipments`, `/technicians`, `/interventions`, `/dashboard`. Variable d'env requise : `NEXT_PUBLIC_API_URL`.

## Domaine métier

```
Company → User (role ADMIN)
Client → Equipment → Intervention ← Technician
```
`Intervention` porte `InterventionStatus` : `PLANNED | IN_PROGRESS | DONE`.  
`Intervention` référence `client`, `equipment` et `technician` (tous optionnels sauf client).

## Conventions de code backend

- **MapStruct** (`componentModel = "spring"`) pour toutes les conversions entité ↔ DTO — voir `mapper/`.
- **Lombok** `@RequiredArgsConstructor` pour l'injection de dépendances (pas de `@Autowired`).
- Les repositories n'exposent **pas** de méthodes `findAllByCompanyId` — le filtrage est géré par Hibernate.
- Controllers annotés `@Tag`, `@Operation`, `@ApiResponse` (SpringDoc) — maintenir la documentation OpenAPI.
- Swagger UI disponible sur `http://localhost:8080/swagger-ui.html`.

## Migrations de base de données

Ajouter un fichier `V{N+1}__description.sql` dans `backend/src/main/resources/db/migration/`.  
`ddl-auto: validate` → Flyway est la seule source de vérité du schéma. Ne jamais modifier une migration existante.  
Schéma actuel : `company`, `app_user` (réservé PostgreSQL → **pas** `user`), `client`, `equipment`, `technician`, `intervention`.

## Commandes de développement

```bash
# Backend (Java 17 requis)
cd backend && ./mvnw spring-boot:run

# Frontend
cd heatops-frontend && npm run dev          # http://localhost:3000

# Observabilité (Jaeger UI: http://localhost:16686, Prometheus: http://localhost:9090)
docker compose --file observability/docker-compose-digma.yml --project-name heatops-obs up -d
```
Base de données PostgreSQL attendue sur `localhost:5432/heatops` (user: `postgres`, password: `password`).

## Tests

Collections Postman dans `test/` (intégration globale) et `backend/src/test/resources/postman/` (soft delete).  
Environnement local : `HeatOps-Local.postman_environment.json`.

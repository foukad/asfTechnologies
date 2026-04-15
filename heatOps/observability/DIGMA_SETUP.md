# Installation et Configuration de l'Observabilité (OpenTelemetry + Jaeger + Prometheus + Grafana)

## Qu'est-ce que cette stack d'observabilité ?

Cette solution d'observabilité complète vous permet de :
- **Tracer** toutes vos requêtes avec Jaeger (traces distribuées)
- **Monitorer** les performances avec Prometheus (métriques)
- **Visualiser** toutes les données avec Grafana (dashboards)
- Détecter automatiquement les problèmes de performance
- Identifier les erreurs et exceptions en temps réel
- Analyser le comportement de votre code durant le développement

## Installation effectuée

✅ **Backend (Spring Boot)** :
- Dépendances OpenTelemetry ajoutées au `pom.xml`
- Configuration dans `application.yaml`
- Instrumentation automatique du code Spring Boot
- Export vers Jaeger (OTLP) et Prometheus

✅ **Stack d'observabilité (Docker Compose)** :
- **Jaeger** (port 16686) - Traces distribuées et UI
- **Prometheus** (port 9090) - Collecte de métriques
- **Grafana** (port 3001) - Visualisation et dashboards

## Démarrage de la stack

### 1. Lancer les conteneurs

```bash
# Depuis le répertoire racine du projet
docker-compose -f docker-compose-digma.yml up -d
```

### 2. Vérifier que les conteneurs sont actifs

```bash
docker-compose -f docker-compose-digma.yml ps
```

Vous devriez voir 3 conteneurs en cours d'exécution :
- `digma-jaeger` - Backend de traces
- `digma-prometheus` - Collecte de métriques
- `digma-grafana` - Interface de visualisation

### 3. Accéder aux interfaces web

Ouvrez votre navigateur et accédez à :

- **Jaeger UI** (Traces) : http://localhost:16686
- **Prometheus** (Métriques) : http://localhost:9090
- **Grafana** (Dashboards) : http://localhost:3001
  - Username: `admin`
  - Password: `admin`

### 4. Démarrer votre application backend

```bash
cd backend
./mvnw spring-boot:run
```

L'application enverra automatiquement :
- Les traces vers Jaeger (port 4317)
- Les métriques vers Prometheus (scraping sur port 8080/actuator/prometheus)

## Utilisation

### Jaeger - Visualiser les traces

1. **Accéder à Jaeger UI** : http://localhost:16686
2. **Sélectionner le service** : `heatops-backend`
3. **Cliquer sur "Find Traces"**
4. Vous verrez toutes les requêtes HTTP, les appels de base de données, etc.

### Grafana - Créer des dashboards

1. **Accéder à Grafana** : http://localhost:3001 (admin/admin)

2. **Ajouter Prometheus comme source de données** :
   - Allez dans Configuration → Data Sources
   - Cliquez sur "Add data source"
   - Sélectionnez "Prometheus"
   - URL: `http://prometheus:9090`
   - Cliquez sur "Save & Test"

3. **Ajouter Jaeger comme source de données** :
   - Configuration → Data Sources
   - Cliquez sur "Add data source"
   - Sélectionnez "Jaeger"
   - URL: `http://jaeger:16686`
   - Cliquez sur "Save & Test"

4. **Importer des dashboards** :
   - Allez dans Dashboards → Import
   - ID du dashboard Spring Boot : `11378`
   - Sélectionnez Prometheus comme source de données

### Prometheus - Requêtes métriques

Accédez à http://localhost:9090 et essayez ces requêtes :

```promql
# Taux de requêtes HTTP
rate(http_server_requests_seconds_count[5m])

# Temps de réponse moyen
rate(http_server_requests_seconds_sum[5m]) / rate(http_server_requests_seconds_count[5m])

# Utilisation de la JVM
jvm_memory_used_bytes

# Connexions à la base de données
hikaricp_connections_active
```

### Fonctionnalités disponibles

- **Distributed Tracing** : Visualisez le flux complet de vos requêtes (Jaeger)
- **Performance Analysis** : Détection des endpoints lents (Jaeger + Prometheus)
- **Error Detection** : Identification des exceptions et erreurs (Jaeger)
- **Database Queries** : Analyse des requêtes SQL (Jaeger traces)
- **Metrics Monitoring** : CPU, mémoire, latence, throughput (Prometheus)
- **Custom Dashboards** : Créez vos propres visualisations (Grafana)

## Arrêter la stack

```bash
docker-compose -f docker-compose-digma.yml down
```

Pour supprimer également les données :
```bash
docker-compose -f docker-compose-digma.yml down -v
```

## Configuration avancée

### Modifier le taux d'échantillonnage

Dans `application.yaml`, vous pouvez ajuster :

```yaml
management:
  tracing:
    sampling:
      probability: 1.0  # 1.0 = 100%, 0.1 = 10%
```

### Personnaliser les exports OTLP

```yaml
otel:
  exporter:
    otlp:
      endpoint: http://localhost:4317
      timeout: 10s
```

## Dépannage

### Les traces n'apparaissent pas dans Jaeger

1. Vérifiez que les conteneurs sont actifs :
   ```bash
   docker-compose -f docker-compose-digma.yml ps
   ```

2. Vérifiez que votre application se connecte bien :
   ```bash
   curl http://localhost:8080/actuator/health
   ```

3. Vérifiez les logs de Jaeger :
   ```bash
   docker-compose -f docker-compose-digma.yml logs jaeger
   ```

4. Testez manuellement l'endpoint OTLP :
   ```bash
   nc -zv localhost 4317
   ```

### Prometheus ne scrape pas les métriques

1. Vérifiez que l'endpoint est accessible :
   ```bash
   curl http://localhost:8080/actuator/prometheus
   ```

2. Vérifiez les targets dans Prometheus :
   - Ouvrez http://localhost:9090/targets
   - Le target `spring-boot` doit être "UP"

3. Si "host.docker.internal" ne fonctionne pas (Linux), modifiez `prometheus.yml` :
   ```yaml
   - targets: ['172.17.0.1:8080']  # IP du bridge Docker
   ```

### Port déjà utilisé

Si un port est déjà utilisé, modifiez `docker-compose-digma.yml` :
```yaml
ports:
  - "16687:16686"  # Change le port externe de Jaeger UI
```

## Ressources

- Documentation Jaeger : https://www.jaegertracing.io/docs/
- Documentation Prometheus : https://prometheus.io/docs/
- Documentation Grafana : https://grafana.com/docs/
- OpenTelemetry : https://opentelemetry.io/docs/

## Version installée

- **Jaeger** : v1.53 (all-in-one avec support OTLP)
- **Prometheus** : v2.48.1
- **Grafana** : v10.2.3
- **OpenTelemetry** : v1.44.1
- **Spring Boot OpenTelemetry Starter** : v2.10.0

---

**Note** : Toutes les données sont stockées localement. Aucune donnée n'est envoyée à des serveurs externes.

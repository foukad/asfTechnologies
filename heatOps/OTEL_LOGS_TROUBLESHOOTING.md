# 🔍 Guide de Dépannage - Erreur OTLP Logs

## Erreur originale

```
Failed to export logs. Server responded with UNIMPLEMENTED. 
This usually means that your collector is not configured with an otlp receiver 
in the "pipelines" section of the configuration.

Error: unknown service opentelemetry.proto.collector.logs.v1.LogsService
```

## Causes possibles

| Cause | Symptôme | Solution |
|-------|----------|----------|
| **Collecteur OTLP absent** | Logs ne sont pas reçus | Ajouter le service `otel-collector` au docker-compose |
| **Pipeline logs non configuré** | Service non géré | Ajouter la section `logs` dans `service.pipelines` |
| **Endpoint incorrect** | Connection refused | Vérifier l'endpoint dans `application.yaml` |
| **Port non exposé** | Communication impossible | Vérifier les ports exposés du collecteur |

## ✅ Étapes de résolution appliquées

### 1. Ajout du collecteur OpenTelemetry

**Fichier:** `docker-compose-digma.yml`

```yaml
otel-collector:
  image: otel/opentelemetry-collector-contrib:0.93.0
  container_name: digma-otel-collector
  command: ["--config=/etc/otel-collector-config.yaml"]
  ports:
    - "4317:4317"   # OTLP gRPC
    - "4318:4318"   # OTLP HTTP
    - "8888:8888"   # Prometheus metrics
  volumes:
    - ./otel-collector-config.yaml:/etc/otel-collector-config.yaml
  depends_on:
    - jaeger
  networks:
    - observability
```

### 2. Configuration du collecteur

**Fichier:** `otel-collector-config.yaml`

```yaml
receivers:
  otlp:
    protocols:
      grpc:
        endpoint: 0.0.0.0:4317
      http:
        endpoint: 0.0.0.0:4318

processors:
  batch:
    timeout: 10s
    send_batch_size: 1024

exporters:
  jaeger:
    endpoint: jaeger:14250
    tls:
      insecure: true
  prometheus:
    endpoint: 0.0.0.0:8888

service:
  pipelines:
    traces:
      receivers: [otlp]
      processors: [batch]
      exporters: [jaeger]
    metrics:
      receivers: [otlp]
      processors: [batch]
      exporters: [prometheus]
    logs:                    # ← IMPORTANT: Pipeline pour les logs
      receivers: [otlp]
      processors: [batch]
      exporters: [jaeger]
```

### 3. Configuration de l'application

**Fichier:** `backend/src/main/resources/application.yaml`

```yaml
otel:
  service:
    name: heatops-backend
  logs:
    exporter: otlp      # ← Les logs sont bien activés
  exporter:
    otlp:
      endpoint: http://localhost:4317  # ← Pointe vers le collecteur
      protocol: grpc
```

## 🧪 Test de la solution

### Étape 1 : Arrêter les services
```bash
./stop-digma.sh
```

### Étape 2 : Nettoyer les images
```bash
docker-compose -f docker-compose-digma.yml down -v
```

### Étape 3 : Lancer la stack mise à jour
```bash
./start-digma.sh
```

### Étape 4 : Vérifier les logs du collecteur
```bash
docker logs digma-otel-collector
```

**Vous devriez voir:**
```
Traces exporter registered
Logs exporter registered
Metrics exporter registered
```

### Étape 5 : Lancer l'application
```bash
cd backend
./mvnw spring-boot:run
```

### Étape 6 : Vérifier les logs de l'application
```bash
# Les logs ne devraient plus contenir d'erreurs OTLP
grep -i "otlp\|export" logs.txt
```

### Étape 7 : Vérifier dans Jaeger
1. Accédez à http://localhost:16686
2. Cherchez le service `heatops-backend`
3. Les traces doivent inclure les logs

## 📊 Architecture finale

```
┌──────────────────────────┐
│  Application Backend     │
│  (Spring Boot + OpenTel) │
└────────────┬─────────────┘
             │
    OTLP gRPC (port 4317)
             │
             ▼
┌──────────────────────────┐
│  OTEL Collector          │
│  ├─ Receiver OTLP gRPC   │
│  ├─ Receiver OTLP HTTP   │
│  ├─ Processor: Batch     │
│  ├─ Exporter: Jaeger     │
│  └─ Exporter: Prometheus │
└──────────────────────────┘
         │          │
  gRPC   │          │ Prometheus
  port   │          │ port 8888
 14250   │          │
         ▼          ▼
    ┌────────┐  ┌──────────┐
    │ Jaeger │  │Prometheus│
    │ :16686 │  │ :9090    │
    └────────┘  └──────────┘
         │          │
         └────┬─────┘
              │
              ▼
         ┌─────────┐
         │ Grafana │
         │ :3001   │
         └─────────┘
```

## 🔍 Diagnostic avancé

### Vérifier les conteneurs
```bash
docker ps | grep digma
```

### Vérifier la connectivité
```bash
# Depuis le conteneur de l'application
docker exec -it <app-container> curl http://otel-collector:4317
```

### Vérifier les metrics
```bash
curl http://localhost:8888/metrics | head -20
```

## 💾 Fichiers modifiés

- ✅ `docker-compose-digma.yml` - Ajout du service collecteur
- ✅ `otel-collector-config.yaml` - Configuration du collecteur (nouveau)
- ✅ `prometheus.yml` - Ajout du scrape du collecteur
- ✅ `backend/src/main/resources/application.yaml` - Existait déjà

## ✅ Vérification finale

```bash
# 1. Vérifier que tous les conteneurs sont en cours d'exécution
docker ps | grep digma

# 2. Vérifier que le collecteur reçoit les données
docker logs digma-otel-collector 2>&1 | grep -i "traces\|logs\|metrics"

# 3. Vérifier que Jaeger reçoit les traces
curl http://localhost:16686/api/services
```

Le pipeline logs devrait maintenant fonctionner correctement ! 🎉

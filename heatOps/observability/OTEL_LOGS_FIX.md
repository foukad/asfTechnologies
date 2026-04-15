# 🔧 Correction du Service OTLP Logs

## Problème identifié

L'erreur `unknown service opentelemetry.proto.collector.logs.v1.LogsService` signifiait que le collecteur n'était pas configuré pour recevoir les logs OTLP.

## Solution apportée

### 1. **Ajout du collecteur OpenTelemetry**

Un service `otel-collector` a été ajouté au `docker-compose-digma.yml` qui :
- Reçoit les données OTLP (traces, métriques, logs) sur les ports `4317` (gRPC) et `4318` (HTTP)
- Traite les données avec un processeur batch
- Exporte vers Jaeger et Prometheus

### 2. **Configuration du collecteur** (`otel-collector-config.yaml`)

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
    logs:
      receivers: [otlp]
      processors: [batch]
      exporters: [jaeger]
```

### 3. **Architecture mise à jour**

```
┌─────────────────┐
│   Application   │
│   (HeatOps)     │
└────────┬────────┘
         │ OTLP (traces, métriques, logs)
         ▼
┌──────────────────────┐
│ OTEL Collector       │
│ :4317 (gRPC)         │
│ :4318 (HTTP)         │
└──────┬───────────┬───┘
       │           │
       ▼           ▼
   ┌────────┐  ┌──────────┐
   │ Jaeger │  │Prometheus│
   │ UI     │  │          │
   │:16686  │  │ :9090    │
   └────────┘  └──────────┘
```

## 🚀 Comment tester

```bash
# 1. Arrêter la stack actuelle
./stop-digma.sh

# 2. Lancer la nouvelle stack
./start-digma.sh

# 3. Lancer l'application
cd backend
./mvnw spring-boot:run

# 4. Les logs devraient être correctement exportés
```

## ✅ Points clés

- ✅ Le collecteur gère maintenant les 3 pipelines : traces, métriques, logs
- ✅ Les logs OTLP sont exportés vers Jaeger
- ✅ Les métriques OTLP sont exportées vers Prometheus
- ✅ Les traces continuent d'être exportées vers Jaeger
- ✅ Ports OTLP supprimés de Jaeger (maintenant gérés par le collecteur)


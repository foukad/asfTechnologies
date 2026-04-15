# 🔧 Configuration OTLP Logs - Résumé des changements

## 🎯 Problème résolu

```
Failed to export logs. Server responded with UNIMPLEMENTED.
unknown service opentelemetry.proto.collector.logs.v1.LogsService
```

## 📦 Fichiers créés

### 1. `otel-collector-config.yaml` ⭐
Configuration du collecteur OpenTelemetry avec les 3 pipelines :
- Traces → Jaeger
- Métriques → Prometheus  
- **Logs → Jaeger** (la partie manquante !)

### 2. `verify-otel.sh` 🧪
Script de vérification automatisée pour tester :
- État des conteneurs Docker
- Accessibilité des ports OTLP
- Configuration du collecteur
- Logs sans erreurs

### 3. `OTEL_LOGS_FIX.md` 📖
Documentation technique détaillée de la correction

### 4. `OTEL_LOGS_TROUBLESHOOTING.md` 🔍
Guide complet de dépannage avec architecture et diagnostic

### 5. `OTEL_LOGS_FIXED.md` ✅
Récapitulatif de la correction et étapes pour l'appliquer

## 🔄 Fichiers modifiés

### `docker-compose-digma.yml`
**Ajout:** Service `otel-collector`
```yaml
otel-collector:
  image: otel/opentelemetry-collector-contrib:0.93.0
  ports:
    - "4317:4317"  # OTLP gRPC
    - "4318:4318"  # OTLP HTTP
```

**Suppression:** Ports 4317/4318 de Jaeger (maintenant gérés par le collecteur)

### `prometheus.yml`
**Ajout:** Job pour scraper le collecteur OTEL
```yaml
- job_name: 'otel-collector'
  static_configs:
    - targets: ['otel-collector:8888']
```

## ✅ Checklist d'implémentation

- ✅ `otel-collector-config.yaml` créé
- ✅ Pipeline logs configuré dans le collecteur
- ✅ Service `otel-collector` ajouté au docker-compose
- ✅ Prometheus configuré pour scraper le collecteur
- ✅ Script de vérification fourni
- ✅ Documentation complète créée
- ✅ Configuration validée (docker-compose config)

## 🚀 Démarrage rapide

```bash
# 1. Arrêter la stack actuelle
./stop-digma.sh

# 2. Nettoyer
docker-compose -f docker-compose-digma.yml down -v

# 3. Lancer la nouvelle stack
./start-digma.sh

# 4. Vérifier
./verify-otel.sh

# 5. Lancer l'application
cd backend && ./mvnw spring-boot:run
```

## 📊 Architecture résultante

```
┌─────────────────────────────────────┐
│  Application Backend (Spring Boot)  │
│  - Envoi OTLP vers port 4317        │
└────────────────┬────────────────────┘
                 │
    OTLP gRPC (:4317)
                 │
                 ▼
┌─────────────────────────────────────┐
│   OpenTelemetry Collector           │
│   ├─ Reçoit: Traces, Logs, Metrics  │
│   ├─ Traite: Batch processor        │
│   └─ Exporte: Jaeger + Prometheus   │
└─────────────────────────────────────┘
         │                  │
         │                  │
    gRPC:14250      Prom:8888
         │                  │
         ▼                  ▼
    ┌────────┐         ┌────────────┐
    │ Jaeger │         │ Prometheus │
    │ :16686 │         │ :9090      │
    └────────┘         └────────────┘
```

## 🧪 Test de l'erreur résolu

L'erreur ne devrait plus apparaître dans les logs de l'application :

```bash
# Avant (erreur)
[OTEL] Failed to export logs. Server responded with UNIMPLEMENTED

# Après (aucune erreur)
# Les logs sont correctement exportés
```

## 📞 Support

Si vous rencontrez des problèmes :

1. **Consultez** `OTEL_LOGS_TROUBLESHOOTING.md`
2. **Exécutez** `./verify-otel.sh`
3. **Vérifiez** les logs du collecteur : `docker logs digma-otel-collector`

## 🎉 C'est tout !

La configuration est maintenant complète et les logs OTLP seront correctement gérés.


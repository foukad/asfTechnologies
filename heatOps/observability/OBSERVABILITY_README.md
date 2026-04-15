# 📊 Stack d'Observabilité - HeatOps

## Qu'est-ce qui est installé ?

Une stack complète d'observabilité basée sur OpenTelemetry avec :
- **Jaeger** : Traces distribuées
- **Prometheus** : Métriques
- **Grafana** : Visualisation

## 🚀 Démarrage Rapide

```bash
# 1. Lancer la stack
./start-digma.sh

# 2. Ouvrir les interfaces
# Jaeger:     http://localhost:16686
# Prometheus: http://localhost:9090
# Grafana:    http://localhost:3001 (admin/admin)

# 3. Démarrer l'application
cd backend
./mvnw spring-boot:run

# 4. Voir les traces dans Jaeger !
```

## 📚 Documentation

- [Guide de démarrage rapide](DIGMA_QUICKSTART.md)
- [Documentation complète](DIGMA_SETUP.md)

## 🛑 Arrêt

```bash
./stop-digma.sh
```

---

**Note** : Le port de Grafana est 3001 (et non 3000) pour éviter le conflit avec le frontend Next.js.

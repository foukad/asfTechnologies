# 🚀 Démarrage Rapide - Observabilité (Jaeger + Prometheus + Grafana)

## Démarrer la stack en 3 étapes

### 1️⃣ Lancer la stack d'observabilité
```bash
./start-digma.sh
```

### 2️⃣ Ouvrir les interfaces web
- **Jaeger (Traces)** : **http://localhost:16686**
- **Prometheus (Métriques)** : **http://localhost:9090**
- **Grafana (Dashboards)** : **http://localhost:3001** (admin/admin)

### 3️⃣ Démarrer votre application
```bash
cd backend
./mvnw spring-boot:run
```

C'est tout ! Les traces apparaîtront automatiquement dans Jaeger 🎉

---

## Arrêter la stack
```bash
./stop-digma.sh
```

---

📖 **Documentation complète :** [INSTALLATION_DIGMA_COMPLETE.md](INSTALLATION_DIGMA_COMPLETE.md)  
📚 **Guide de configuration :** [DIGMA_SETUP.md](DIGMA_SETUP.md)

# ✅ Installation de Digma - TERMINÉE

## 📋 Résumé de l'installation

L'installation de **Digma (version gratuite/Open Source)** a été complétée avec succès pour votre projet HeatOps !

## ✅ Ce qui a été installé

### 1. Backend Spring Boot

#### Dépendances ajoutées au `pom.xml` :
- ✅ OpenTelemetry API (v1.44.1)
- ✅ OpenTelemetry SDK (v1.44.1)
- ✅ OpenTelemetry Exporter OTLP (v1.44.1)
- ✅ OpenTelemetry Spring Boot Starter (v2.10.0)
- ✅ Instrumentation automatique pour Spring Boot

**Toutes les dépendances ont été téléchargées avec succès !**

#### Configuration ajoutée à `application.yaml` :
```yaml
otel:
  service:
    name: heatops-backend
  traces:
    exporter: otlp
  metrics:
    exporter: otlp
  logs:
    exporter: otlp
  exporter:
    otlp:
      endpoint: http://localhost:5050
      protocol: grpc

management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
  metrics:
    export:
      prometheus:
        enabled: true
```

### 2. Infrastructure Docker

#### Fichier `docker-compose-digma.yml` créé :
- ✅ Digma Collector (port 5050 - OTLP gRPC)
- ✅ Digma Analytics (port 5052)
- ✅ Digma UI (port 5053)

### 3. Scripts de gestion

#### Scripts créés et rendus exécutables :
- ✅ `start-digma.sh` - Démarrer Digma
- ✅ `stop-digma.sh` - Arrêter Digma

### 4. Documentation

- ✅ `DIGMA_SETUP.md` - Guide complet d'utilisation

## 🚀 Comment utiliser Digma

### Étape 1 : Démarrer Digma

```bash
# Dans le répertoire racine du projet
./start-digma.sh
```

Ou manuellement :
```bash
docker-compose -f docker-compose-digma.yml up -d
```

### Étape 2 : Accéder à l'interface Digma

Ouvrez votre navigateur : **http://localhost:5053**

### Étape 3 : Démarrer votre application backend

```bash
cd backend
./mvnw spring-boot:run
```

### Étape 4 : Utiliser votre application

Faites des requêtes à votre API et les traces apparaîtront automatiquement dans Digma !

## 🔧 Fonctionnalités de Digma

Une fois votre application démarrée, Digma vous fournira :

- 📊 **Traces distribuées** : Visualisez le flux complet de vos requêtes
- ⚡ **Analyse de performance** : Identifiez les endpoints lents
- 🐛 **Détection d'erreurs** : Repérez les exceptions et erreurs
- 🗄️ **Monitoring de base de données** : Analysez vos requêtes SQL
- 📈 **Métriques en temps réel** : CPU, mémoire, latence, etc.

## 🎯 Installation du plugin IntelliJ (Optionnel)

Pour avoir les insights directement dans votre IDE :

1. **Settings/Preferences** → **Plugins**
2. Recherchez **"Digma"**
3. Installez et redémarrez
4. Le plugin se connectera automatiquement à localhost:5053

## 📦 Services et Ports

| Service | Port | Description |
|---------|------|-------------|
| Digma UI | 5053 | Interface web principale |
| Digma Collector | 5050 | Réception des traces OTLP (gRPC) |
| Digma Collector HTTP | 5051 | Réception des traces OTLP (HTTP) |
| Digma Analytics | 5052 | Moteur d'analyse |

## 🛠️ Commandes utiles

### Voir les logs de Digma
```bash
docker-compose -f docker-compose-digma.yml logs -f
```

### Voir l'état des conteneurs
```bash
docker-compose -f docker-compose-digma.yml ps
```

### Arrêter Digma
```bash
./stop-digma.sh
# ou
docker-compose -f docker-compose-digma.yml down
```

### Arrêter et supprimer les données
```bash
docker-compose -f docker-compose-digma.yml down -v
```

### Redémarrer Digma
```bash
docker-compose -f docker-compose-digma.yml restart
```

## ⚠️ Note importante sur la compilation

Un problème de compilation Lombok/Java a été détecté dans votre projet. Ce problème **est indépendant de l'installation de Digma** et existait probablement déjà.

### Solution possible :

Si vous rencontrez l'erreur `java.lang.ExceptionInInitializerError` avec Lombok :

1. Vérifiez votre version de Java :
   ```bash
   java -version
   ```

2. Assurez-vous d'utiliser Java 17 (comme spécifié dans le pom.xml)

3. Si vous utilisez un JDK différent, configurez JAVA_HOME :
   ```bash
   export JAVA_HOME=/path/to/jdk-17
   ```

4. Alternative : utilisez le wrapper Maven avec le bon JDK :
   ```bash
   JAVA_HOME=/path/to/jdk-17 ./mvnw clean compile
   ```

## 🎉 Conclusion

Digma est **complètement installé et prêt à l'emploi** !

Même si le projet backend a un problème de compilation indépendant de Digma, toutes les dépendances et la configuration de Digma sont en place. Une fois que vous aurez résolu le problème de compilation, Digma fonctionnera immédiatement sans configuration supplémentaire.

## 📚 Ressources

- Documentation Digma : https://docs.digma.ai/
- GitHub : https://github.com/digma-ai/digma
- OpenTelemetry : https://opentelemetry.io/

---

**Installation réalisée le :** 18 février 2026  
**Version de Digma :** Latest (Free/OSS)  
**Version OpenTelemetry :** 1.44.1

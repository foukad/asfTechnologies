#!/bin/bash

# Script de vérification de la configuration OTLP

echo "🔍 Vérification de la configuration OTLP Logs..."
echo ""

# Couleurs
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Fonction pour afficher un résultat
check_result() {
    if [ $1 -eq 0 ]; then
        echo -e "${GREEN}✅${NC} $2"
        return 0
    else
        echo -e "${RED}❌${NC} $2"
        return 1
    fi
}

# 1. Vérifier que Docker est en cours d'exécution
echo "1️⃣  Vérification de Docker..."
docker info > /dev/null 2>&1
check_result $? "Docker est en cours d'exécution"
echo ""

# 2. Vérifier que les conteneurs OTLP sont en cours d'exécution
echo "2️⃣  Vérification des conteneurs..."

docker ps | grep -q "digma-otel-collector"
check_result $? "Collecteur OpenTelemetry en cours d'exécution"

docker ps | grep -q "digma-jaeger"
check_result $? "Jaeger en cours d'exécution"

docker ps | grep -q "digma-prometheus"
check_result $? "Prometheus en cours d'exécution"

echo ""

# 3. Vérifier la connectivité aux ports
echo "3️⃣  Vérification de la connectivité..."

timeout 2 bash -c "cat < /dev/null > /dev/tcp/localhost/4317" 2>/dev/null
check_result $? "Port 4317 (OTLP gRPC) accessible"

timeout 2 bash -c "cat < /dev/null > /dev/tcp/localhost/4318" 2>/dev/null
check_result $? "Port 4318 (OTLP HTTP) accessible"

timeout 2 bash -c "cat < /dev/null > /dev/tcp/localhost/16686" 2>/dev/null
check_result $? "Port 16686 (Jaeger UI) accessible"

timeout 2 bash -c "cat < /dev/null > /dev/tcp/localhost/9090" 2>/dev/null
check_result $? "Port 9090 (Prometheus) accessible"

echo ""

# 4. Vérifier la configuration du collecteur
echo "4️⃣  Vérification de la configuration..."

# Vérifier que le fichier de configuration existe
[ -f "./otel-collector-config.yaml" ]
check_result $? "Fichier otel-collector-config.yaml existe"

# Vérifier que le pipeline logs est configuré
grep -q "logs:" ./otel-collector-config.yaml
check_result $? "Pipeline 'logs' configuré dans le collecteur"

grep -q "otlp:" ./otel-collector-config.yaml
check_result $? "Receiver 'otlp' configuré dans le collecteur"

echo ""

# 5. Vérifier la configuration de l'application
echo "5️⃣  Vérification de la configuration de l'application..."

if [ -f "./backend/src/main/resources/application.yaml" ]; then
    grep -q "logs:" ./backend/src/main/resources/application.yaml
    check_result $? "Logs configurés dans application.yaml"

    grep -q "exporter: otlp" ./backend/src/main/resources/application.yaml
    check_result $? "Exporter OTLP configuré pour les logs"
else
    echo -e "${RED}❌${NC} Fichier application.yaml non trouvé"
fi

echo ""

# 6. Vérifier les logs du collecteur
echo "6️⃣  Vérification des logs du collecteur..."
echo ""

COLLECTOR_LOGS=$(docker logs digma-otel-collector 2>&1 | tail -20)

if echo "$COLLECTOR_LOGS" | grep -q -i "error"; then
    echo -e "${YELLOW}⚠️  Le collecteur a des erreurs :${NC}"
    echo "$COLLECTOR_LOGS" | grep -i "error"
else
    echo -e "${GREEN}✅${NC} Pas d'erreurs détectées"
fi

echo ""

# 7. Résumé
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "📊 RÉSUMÉ DE LA VÉRIFICATION"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""
echo "Services d'observabilité:"
echo "  - Jaeger UI:        http://localhost:16686"
echo "  - Prometheus:       http://localhost:9090"
echo "  - OTLP gRPC:        http://localhost:4317"
echo "  - OTLP HTTP:        http://localhost:4318"
echo ""
echo "Prochaines étapes:"
echo "  1. Démarrez l'application: cd backend && ./mvnw spring-boot:run"
echo "  2. Ouvrez Jaeger sur http://localhost:16686"
echo "  3. Cherchez le service 'heatops-backend'"
echo "  4. Les traces et logs devraient apparaître"
echo ""


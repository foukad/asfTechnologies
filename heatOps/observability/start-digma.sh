#!/bin/bash

# Script de démarrage de la stack d'observabilité (Jaeger + Prometheus + Grafana)

echo "🚀 Démarrage de la stack d'observabilité..."
echo ""

# Vérifier que Docker est installé et en cours d'exécution
if ! command -v docker &> /dev/null; then
    echo "❌ Docker n'est pas installé. Veuillez installer Docker pour continuer."
    exit 1
fi

if ! docker info &> /dev/null; then
    echo "❌ Docker n'est pas en cours d'exécution. Veuillez démarrer Docker Desktop."
    exit 1
fi

echo "✅ Docker est en cours d'exécution"
echo ""

# Démarrer les conteneurs
echo "📦 Démarrage des conteneurs (Jaeger, Prometheus, Grafana)..."
docker-compose -f docker-compose-digma.yml up -d

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ Stack d'observabilité démarrée avec succès!"
    echo ""
    echo "📊 Services disponibles:"
    echo "   - Jaeger UI:        http://localhost:16686 (Traces distribuées)"
    echo "   - Prometheus:       http://localhost:9090  (Métriques)"
    echo "   - Grafana:          http://localhost:3001  (Dashboards - admin/admin)"
    echo ""
    echo "💡 Conseils:"
    echo "   1. Ouvrez Jaeger sur http://localhost:16686"
    echo "   2. Ouvrez Grafana sur http://localhost:3001 (admin/admin)"
    echo "   3. Démarrez votre application backend avec: cd backend && ./mvnw spring-boot:run"
    echo "   4. Les traces apparaîtront automatiquement dans Jaeger!"
    echo ""
    echo "🛑 Pour arrêter, utilisez: ./stop-digma.sh"
    echo ""

    # Attendre que les services soient prêts
    echo "⏳ Attente du démarrage des services (15 secondes)..."
    sleep 15

    # Vérifier que les services sont en cours d'exécution
    echo ""
    echo "📋 État des conteneurs:"
    docker-compose -f docker-compose-digma.yml ps

else
    echo ""
    echo "❌ Erreur lors du démarrage de la stack d'observabilité"
    echo "Vérifiez les logs avec: docker-compose -f docker-compose-digma.yml logs"
    exit 1
fi

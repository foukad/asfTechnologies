#!/bin/bash

# Script d'arrêt de la stack d'observabilité

echo "🛑 Arrêt de la stack d'observabilité (Jaeger, Prometheus, Grafana)..."
echo ""

docker-compose -f docker-compose-digma.yml down

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ Stack d'observabilité arrêtée avec succès!"
    echo ""
    echo "💡 Pour supprimer également les données, utilisez:"
    echo "   docker-compose -f docker-compose-digma.yml down -v"
    echo ""
else
    echo ""
    echo "❌ Erreur lors de l'arrêt"
    exit 1
fi

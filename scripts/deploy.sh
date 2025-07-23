#!/bin/bash

# Deployment script for production environments
set -e

echo "🚀 Starting production deployment..."

# Load environment variables
if [ -f .env.prod ]; then
    export $(cat .env.prod | xargs)
fi

# Default values
REGISTRY=${REGISTRY:-ghcr.io}
IMAGE_NAME=${IMAGE_NAME:-fuzzy-search}
IMAGE_TAG=${IMAGE_TAG:-latest}
ENVIRONMENT=${ENVIRONMENT:-production}

echo "📦 Pulling latest Docker image..."
docker pull ${REGISTRY}/${IMAGE_NAME}:${IMAGE_TAG}

echo "🛑 Stopping existing containers..."
docker compose -f docker-compose.prod.yml down --remove-orphans

echo "🗑️ Cleaning up old containers and images..."
docker system prune -f

echo "🎯 Starting new containers..."
docker compose -f docker-compose.prod.yml up -d

echo "⏳ Waiting for services to be healthy..."
sleep 30

# Health check
echo "🔍 Performing health check..."
for i in {1..10}; do
    if curl -f http://localhost:8080/q/health > /dev/null 2>&1; then
        echo "✅ Application is healthy!"
        break
    else
        echo "⏳ Waiting for application to be ready... ($i/10)"
        sleep 10
    fi

    if [ $i -eq 10 ]; then
        echo "❌ Health check failed!"
        docker compose -f docker-compose.prod.yml logs app
        exit 1
    fi
done

echo "🎉 Deployment completed successfully!"
echo "📱 Application is available at: http://localhost:8080"

# Optional: Run smoke tests
if [ "$RUN_SMOKE_TESTS" = "true" ]; then
    echo "🧪 Running smoke tests..."
    ./scripts/smoke-tests.sh
fi

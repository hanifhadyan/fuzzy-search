#!/bin/bash

# Change to scripts directory to access docker-compose.yml
cd "$(dirname "$0")"

echo "Stopping application and cleaning up..."

# Stop Docker containers
echo "Stopping PostgreSQL container..."
docker compose down

# Clean up volumes if requested
if [ "$1" = "--clean-volumes" ]; then
    echo "Removing Docker volumes..."
    docker compose down -v
    docker volume prune -f
fi

echo "Cleanup completed!"

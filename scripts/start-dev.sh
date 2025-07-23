#!/bin/bash

# Change to scripts directory to access docker-compose.yml
cd "$(dirname "$0")"

# Load environment variables from .env file
if [ -f ../.env ]; then
    export $(cat ../.env | xargs)
fi

# Start Docker containers using docker compose
echo "Starting PostgreSQL container with docker compose..."
docker compose up -d postgres

# Wait for PostgreSQL to be ready
echo "Waiting for PostgreSQL to be ready..."
sleep 10

# Check if PostgreSQL is ready
until docker compose exec postgres pg_isready -h localhost -p 5440 -U $DB_USER; do
  echo "PostgreSQL is not ready yet. Waiting..."
  sleep 2
done

echo "PostgreSQL is ready!"

# Go back to project root and run the application
cd ..
echo "Starting Quarkus application..."
./gradlew quarkusDev

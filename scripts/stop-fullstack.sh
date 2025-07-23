#!/bin/bash

# Change to scripts directory to access docker-compose.yml
cd "$(dirname "$0")"

echo "🛑 Stopping Full Stack Fuzzy Search Application..."

# Go back to project root to access PID files
cd ..

# Stop backend if PID file exists
if [ -f backend.pid ]; then
    BACKEND_PID=$(cat backend.pid)
    if kill -0 $BACKEND_PID 2>/dev/null; then
        echo "🔧 Stopping backend server (PID: $BACKEND_PID)..."
        kill $BACKEND_PID
    fi
    rm -f backend.pid
fi

# Stop frontend if PID file exists
if [ -f frontend.pid ]; then
    FRONTEND_PID=$(cat frontend.pid)
    if kill -0 $FRONTEND_PID 2>/dev/null; then
        echo "⚛️ Stopping frontend server (PID: $FRONTEND_PID)..."
        kill $FRONTEND_PID
    fi
    rm -f frontend.pid
fi

# Stop any remaining processes on ports 3000 and 8080
echo "🔍 Checking for remaining processes..."
lsof -ti:3000 | xargs kill -9 2>/dev/null || true
lsof -ti:8080 | xargs kill -9 2>/dev/null || true

# Go back to scripts directory and stop Docker containers
cd scripts
echo "📊 Stopping PostgreSQL container..."
docker compose down

# Clean up log files if requested
if [ "$1" = "--clean-logs" ]; then
    echo "🧹 Cleaning up log files..."
    cd ..
    rm -f backend.log frontend.log
    cd scripts
fi

# Clean up volumes if requested
if [ "$1" = "--clean-volumes" ]; then
    echo "🧹 Removing Docker volumes..."
    docker compose down -v
    docker volume prune -f
fi

echo "✅ Full stack application stopped successfully!"

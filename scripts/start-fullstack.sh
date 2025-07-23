#!/bin/bash

# Change to scripts directory to access docker-compose.yml
cd "$(dirname "$0")"

# Load environment variables from .env file
if [ -f ../.env ]; then
    export $(cat ../.env | xargs)
fi

echo "🚀 Starting Full Stack Fuzzy Search Application..."

# Start PostgreSQL with docker compose
echo "📊 Starting PostgreSQL database..."
docker compose up -d postgres

# Wait for PostgreSQL to be ready
echo "⏳ Waiting for PostgreSQL to be ready..."
sleep 10

# Check if PostgreSQL is ready
until docker compose exec postgres pg_isready -h localhost -p 5440 -U $DB_USER; do
  echo "PostgreSQL is not ready yet. Waiting..."
  sleep 2
done

echo "✅ PostgreSQL is ready!"

# Go back to project root
cd ..

# Install frontend dependencies if node_modules doesn't exist
if [ ! -d "frontend/node_modules" ]; then
    echo "📦 Installing frontend dependencies..."
    cd frontend
    npm install
    cd ..
fi

# Start both backend and frontend concurrently
echo "🎯 Starting backend and frontend servers..."

# Start backend in background
echo "🔧 Starting Quarkus backend on port 8080..."
./gradlew quarkusDev > backend.log 2>&1 &
BACKEND_PID=$!

# Wait a bit for backend to start
sleep 5

# Start frontend in background
echo "⚛️ Starting React frontend on port 3000..."
cd frontend
npm start > ../frontend.log 2>&1 &
FRONTEND_PID=$!
cd ..

echo "🎉 Full stack application is starting..."
echo "📱 Frontend: http://localhost:3000"
echo "🔧 Backend API: http://localhost:8080"
echo "📊 Database: PostgreSQL on port 5440"
echo ""
echo "📋 Logs:"
echo "   Backend: tail -f backend.log"
echo "   Frontend: tail -f frontend.log"
echo ""
echo "🛑 To stop the application, run: ./scripts/stop-fullstack.sh"

# Save PIDs for cleanup
echo $BACKEND_PID > backend.pid
echo $FRONTEND_PID > frontend.pid

# Keep script running
wait

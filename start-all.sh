#!/bin/bash

# 🚀 START ALL SERVICES - ONE COMMAND

echo "╔════════════════════════════════════════════════════════════════╗"
echo "║                                                                ║"
echo "║   🚀 SAMANA - Starting All Services                           ║"
echo "║                                                                ║"
echo "╚════════════════════════════════════════════════════════════════╝"
echo ""

# Colors
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

# Get directory
SAMANA_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SAMANA_DIR"

# Check if already running
if lsof -i :8080 > /dev/null 2>&1; then
  echo -e "${YELLOW}⚠️  Backend already running on port 8080${NC}"
else
  echo -e "${BLUE}1️⃣ Starting Backend (port 8080)...${NC}"

  # Set environment variables
  export SPRING_PROFILES_ACTIVE=neon
  export JWT_SECRET="your-secret-key-min-32-chars-long"

  # Check if NEON_DATABASE_URL is set
  if [ -z "$NEON_DATABASE_URL" ]; then
    echo -e "${YELLOW}⚠️  NEON_DATABASE_URL not set. Using local PostgreSQL...${NC}"
    export SPRING_PROFILES_ACTIVE=postgres
    export DB_USERNAME=postgres
    export DB_PASSWORD=postgres
  fi

  # Start backend
  cd backend/samana-backend
  nohup java -jar target/samana-backend-0.0.1-SNAPSHOT.jar > backend.log 2>&1 &
  BACKEND_PID=$!
  echo -e "${GREEN}   Backend PID: $BACKEND_PID${NC}"
  cd "$SAMANA_DIR"

  # Wait for backend to start
  sleep 3
fi

# Check if frontend already running
if lsof -i :3000 > /dev/null 2>&1; then
  echo -e "${YELLOW}⚠️  Frontend already running on port 3000${NC}"
else
  echo -e "${BLUE}2️⃣ Starting Frontend (port 3000)...${NC}"
  nohup python3 -m http.server 3000 > frontend.log 2>&1 &
  FRONTEND_PID=$!
  echo -e "${GREEN}   Frontend PID: $FRONTEND_PID${NC}"
  sleep 2
fi

echo ""
echo -e "${GREEN}═══════════════════════════════════════════════════════════════${NC}"
echo -e "${GREEN}✅ ALL SERVICES STARTED!${NC}"
echo -e "${GREEN}═══════════════════════════════════════════════════════════════${NC}"
echo ""

# Verify services
echo -e "${BLUE}Testing connections...${NC}"
echo ""

# Test backend
if curl -s http://localhost:8080/api/experiences > /dev/null 2>&1; then
  echo -e "${GREEN}✅ Backend: http://localhost:8080${NC}"
else
  echo -e "${YELLOW}⏳ Backend: Still starting (may take 10-15 seconds)...${NC}"
fi

# Test frontend
if curl -s http://localhost:3000/ > /dev/null 2>&1; then
  echo -e "${GREEN}✅ Frontend: http://localhost:3000${NC}"
else
  echo -e "${YELLOW}❌ Frontend: Connection failed${NC}"
fi

echo ""
echo -e "${GREEN}═══════════════════════════════════════════════════════════════${NC}"
echo ""
echo "🌐 OPEN IN BROWSER:"
echo -e "   ${BLUE}http://localhost:3000${NC}"
echo ""
echo "📊 LOGS:"
echo -e "   Backend: ${BLUE}tail -f backend/samana-backend/backend.log${NC}"
echo -e "   Frontend: ${BLUE}tail -f frontend.log${NC}"
echo ""
echo "🧪 RUN TESTS:"
echo -e "   ${BLUE}./test-local.sh${NC}"
echo ""
echo "🛑 STOP ALL:"
echo -e "   ${BLUE}./stop-local.sh${NC}"
echo ""
echo -e "${GREEN}═══════════════════════════════════════════════════════════════${NC}"

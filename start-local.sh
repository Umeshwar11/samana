#!/bin/bash

# 🚀 SAMANA LOCAL DEPLOYMENT STARTUP SCRIPT
# Starts backend, frontend, and provides testing commands

set -e

echo "╔════════════════════════════════════════════════════════════════╗"
echo "║                                                                ║"
echo "║   🚀 SAMANA PLATFORM - LOCAL DEPLOYMENT                       ║"
echo "║                                                                ║"
echo "╚════════════════════════════════════════════════════════════════╝"
echo ""

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Check prerequisites
echo -e "${BLUE}📋 Checking prerequisites...${NC}"

# Check Java
if ! command -v java &> /dev/null; then
    echo -e "${RED}❌ Java not found. Please install Java 21${NC}"
    exit 1
fi

# Check Maven
if ! command -v mvn &> /dev/null; then
    echo -e "${RED}❌ Maven not found. Please install Maven${NC}"
    exit 1
fi

# Check PostgreSQL
if ! command -v psql &> /dev/null; then
    echo -e "${RED}❌ PostgreSQL client not found${NC}"
    exit 1
fi

echo -e "${GREEN}✅ All prerequisites found${NC}"
echo ""

# Database setup
echo -e "${BLUE}🗄️ Setting up database...${NC}"

# Check if PostgreSQL is running
if ! pg_isready -h localhost -U postgres &> /dev/null; then
    echo -e "${YELLOW}Starting PostgreSQL...${NC}"
    if [[ "$OSTYPE" == "darwin"* ]]; then
        brew services start postgresql
    elif [[ "$OSTYPE" == "linux-gnu"* ]]; then
        sudo systemctl start postgresql
    fi
    sleep 2
fi

# Create database and user if they don't exist
PSQL_CMD="psql -U postgres -h localhost"

# Check if database exists
if ! $PSQL_CMD -lqt | cut -d \| -f 1 | grep -qw samana_db; then
    echo -e "${YELLOW}Creating database and user...${NC}"
    $PSQL_CMD -c "CREATE DATABASE samana_db;" 2>/dev/null || true
    $PSQL_CMD -c "CREATE USER samana_user WITH PASSWORD 'samana_password';" 2>/dev/null || true
    $PSQL_CMD -c "GRANT ALL PRIVILEGES ON DATABASE samana_db TO samana_user;" 2>/dev/null || true
    echo -e "${GREEN}✅ Database ready${NC}"
else
    echo -e "${GREEN}✅ Database already exists${NC}"
fi

echo ""

# Backend setup
echo -e "${BLUE}🔧 Building backend...${NC}"
cd backend/samana-backend

# Set environment variables
export DB_USERNAME=samana_user
export DB_PASSWORD=samana_password
export SPRING_PROFILES_ACTIVE=postgres
export JWT_SECRET=your-secret-key-min-32-chars-long
export PORT=8080

# Clean and build
mvn clean package -DskipTests -q

if [ ! -f "target/samana-backend-0.0.1-SNAPSHOT.jar" ]; then
    echo -e "${RED}❌ Backend build failed${NC}"
    exit 1
fi

echo -e "${GREEN}✅ Backend built successfully${NC}"
echo ""

# Return to root
cd - > /dev/null

# Create .env file for easy reference
cat > .env << 'ENVEOF'
export DB_USERNAME=samana_user
export DB_PASSWORD=samana_password
export SPRING_PROFILES_ACTIVE=postgres
export JWT_SECRET=your-secret-key-min-32-chars-long
export PORT=8080
ENVEOF

echo -e "${BLUE}📝 Environment file created (.env)${NC}"
echo ""

# Display startup instructions
echo -e "${BLUE}═══════════════════════════════════════════════════════════════${NC}"
echo -e "${GREEN}✅ LOCAL SETUP COMPLETE!${NC}"
echo -e "${BLUE}═══════════════════════════════════════════════════════════════${NC}"
echo ""

echo -e "${YELLOW}🚀 STARTING SERVICES:${NC}"
echo ""

# Start backend in background
echo -e "${BLUE}1️⃣  Starting Backend on port 8080...${NC}"
cd backend/samana-backend
source ../../.env

# Kill previous instances if any
pkill -f "java.*samana-backend" 2>/dev/null || true
sleep 1

java -jar target/samana-backend-0.0.1-SNAPSHOT.jar > backend.log 2>&1 &
BACKEND_PID=$!
echo -e "${GREEN}   Backend PID: $BACKEND_PID${NC}"
echo -e "${GREEN}   Logs: $(pwd)/backend.log${NC}"

# Wait for backend to start
sleep 5

if ! ps -p $BACKEND_PID > /dev/null; then
    echo -e "${RED}❌ Backend failed to start${NC}"
    tail -50 backend.log
    exit 1
fi

# Test backend
if curl -s http://localhost:8080/api/experiences > /dev/null 2>&1; then
    echo -e "${GREEN}   ✅ Backend is running${NC}"
else
    echo -e "${YELLOW}   ⚠️  Backend may still be starting...${NC}"
fi

cd - > /dev/null
echo ""

# Start frontend
echo -e "${BLUE}2️⃣  Starting Frontend on port 3000...${NC}"

# Kill previous instances if any
pkill -f "python.*http.server" 2>/dev/null || true
pkill -f "npx.*http-server" 2>/dev/null || true
sleep 1

if command -v python3 &> /dev/null; then
    cd $(pwd)
    python3 -m http.server 3000 > frontend.log 2>&1 &
    FRONTEND_PID=$!
    echo -e "${GREEN}   Frontend PID: $FRONTEND_PID${NC}"
    echo -e "${GREEN}   Logs: $(pwd)/frontend.log${NC}"
else
    echo -e "${YELLOW}   Using npx http-server${NC}"
    npx http-server -p 3000 > frontend.log 2>&1 &
    FRONTEND_PID=$!
    echo -e "${GREEN}   Frontend PID: $FRONTEND_PID${NC}"
fi

cd - > /dev/null
echo ""

# Display access information
echo -e "${BLUE}═══════════════════════════════════════════════════════════════${NC}"
echo -e "${GREEN}🎉 ALL SERVICES STARTED!${NC}"
echo -e "${BLUE}═══════════════════════════════════════════════════════════════${NC}"
echo ""

echo -e "${YELLOW}📱 ACCESS URLS:${NC}"
echo -e "   Frontend:  ${BLUE}http://localhost:3000${NC}"
echo -e "   Backend:   ${BLUE}http://localhost:8080${NC}"
echo ""

echo -e "${YELLOW}🧪 QUICK TESTS:${NC}"
cat << 'TESTSEOF'
   # Test backend
   curl http://localhost:8080/api/experiences
   curl http://localhost:8080/api/experiences/filter-options/states
   curl http://localhost:8080/api/calendar/festival-states

   # Test API integration
   curl "http://localhost:8080/api/experiences/search?title=Holi"

TESTSEOF

echo ""

echo -e "${YELLOW}📋 AVAILABLE PAGES:${NC}"
cat << 'PAGESEOF'
   http://localhost:3000/                    - Home
   http://localhost:3000/explore.html         - Search & Explore
   http://localhost:3000/calendar.html        - Festival Calendar (if exists)
   http://localhost:3000/experience.html?id=1 - Experience Details
   http://localhost:3000/bookings.html        - My Bookings
   http://localhost:3000/login.html           - Login

PAGESEOF

echo ""

echo -e "${YELLOW}🛑 STOP SERVICES:${NC}"
cat << 'STOPEOF'
   # Stop backend
   kill BACKEND_PID (or use Ctrl+C in backend terminal)

   # Stop frontend
   kill FRONTEND_PID (or use Ctrl+C in frontend terminal)

   # Or run:
   ./stop-local.sh

STOPEOF

echo ""

echo -e "${YELLOW}📊 MONITORING:${NC}"
cat << 'MONITOREOF'
   # Watch backend logs
   tail -f backend/samana-backend/backend.log

   # Watch frontend logs
   tail -f frontend.log

MONITOREOF

echo ""

echo -e "${BLUE}═══════════════════════════════════════════════════════════════${NC}"
echo -e "${GREEN}✅ Ready for testing and development!${NC}"
echo -e "${BLUE}═══════════════════════════════════════════════════════════════${NC}"
echo ""

# Keep script running
wait

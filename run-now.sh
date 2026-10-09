#!/bin/bash

# 🚀 ONE-COMMAND SAMANA STARTUP
# Just run: ./run-now.sh

echo "╔════════════════════════════════════════════════════════════════╗"
echo "║                                                                ║"
echo "║   🚀 SAMANA PLATFORM - QUICK START                            ║"
echo "║                                                                ║"
echo "╚════════════════════════════════════════════════════════════════╝"
echo ""

# Colors
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

echo -e "${BLUE}ℹ️  IMPORTANT: You need to open 3 separate terminals!${NC}"
echo ""
echo -e "${YELLOW}This script will display commands for each terminal.${NC}"
echo ""

# Ask which terminal user is running
echo "Which terminal are you running this in?"
echo "1) Terminal 1 (Backend)"
echo "2) Terminal 2 (Frontend)"
echo "3) Terminal 3 (Tests)"
echo ""
read -p "Enter choice (1-3): " choice

case $choice in
  1)
    echo ""
    echo -e "${GREEN}═══════════════════════════════════════════════════════════════${NC}"
    echo -e "${GREEN}TERMINAL 1: BACKEND${NC}"
    echo -e "${GREEN}═══════════════════════════════════════════════════════════════${NC}"
    echo ""
    echo "Running Backend on port 8080..."
    echo ""

    cd backend/samana-backend

    # Check if JAR exists
    if [ ! -f "target/samana-backend-0.0.1-SNAPSHOT.jar" ]; then
      echo -e "${YELLOW}JAR file not found. Building...${NC}"
      mvn clean package -DskipTests -q
    fi

    # Set environment variables
    export SPRING_PROFILES_ACTIVE=neon
    export JWT_SECRET="your-secret-key-min-32-chars-long-change-this"

    # Check if NEON_DATABASE_URL is set
    if [ -z "$NEON_DATABASE_URL" ]; then
      echo -e "${YELLOW}⚠️  NEON_DATABASE_URL not set!${NC}"
      echo ""
      echo "Please set it first:"
      echo "export NEON_DATABASE_URL=\"postgresql://user:pass@ep-xyz.us-east-1.neon.tech/samana_db\""
      echo ""
      echo "Or use local PostgreSQL:"
      echo "export SPRING_PROFILES_ACTIVE=postgres"
      echo "export DB_USERNAME=postgres"
      echo "export DB_PASSWORD=postgres"
      echo ""
      exit 1
    fi

    echo -e "${GREEN}✅ Starting Backend...${NC}"
    java -jar target/samana-backend-0.0.1-SNAPSHOT.jar

    ;;

  2)
    echo ""
    echo -e "${GREEN}═══════════════════════════════════════════════════════════════${NC}"
    echo -e "${GREEN}TERMINAL 2: FRONTEND${NC}"
    echo -e "${GREEN}═══════════════════════════════════════════════════════════════${NC}"
    echo ""
    echo "Running Frontend on port 3000..."
    echo ""

    cd /Users/sudhir_ahirkar/Applications/samana/samana

    echo -e "${GREEN}✅ Starting Frontend...${NC}"
    python3 -m http.server 3000

    ;;

  3)
    echo ""
    echo -e "${GREEN}═══════════════════════════════════════════════════════════════${NC}"
    echo -e "${GREEN}TERMINAL 3: TESTS${NC}"
    echo -e "${GREEN}═══════════════════════════════════════════════════════════════${NC}"
    echo ""
    echo "Running tests in 5 seconds..."
    echo "Make sure Terminal 1 and 2 are running first!"
    echo ""

    sleep 5

    cd /Users/sudhir_ahirkar/Applications/samana/samana

    echo -e "${GREEN}✅ Running Tests...${NC}"
    ./test-local.sh

    ;;

  *)
    echo -e "${YELLOW}Invalid choice. Please enter 1, 2, or 3.${NC}"
    exit 1
    ;;
esac

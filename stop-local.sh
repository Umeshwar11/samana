#!/bin/bash

# 🛑 SAMANA LOCAL DEPLOYMENT STOP SCRIPT

echo "╔════════════════════════════════════════════════════════════════╗"
echo "║                                                                ║"
echo "║   🛑 SAMANA PLATFORM - STOPPING LOCAL SERVICES               ║"
echo "║                                                                ║"
echo "╚════════════════════════════════════════════════════════════════╝"
echo ""

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

echo -e "${BLUE}Stopping services...${NC}"
echo ""

# Stop backend
echo -e "${YELLOW}Stopping Backend...${NC}"
pkill -f "java.*samana-backend" 2>/dev/null && echo -e "${GREEN}✅ Backend stopped${NC}" || echo -e "${YELLOW}ℹ️  Backend not running${NC}"

# Stop frontend
echo -e "${YELLOW}Stopping Frontend...${NC}"
pkill -f "python.*http.server.*3000" 2>/dev/null && echo -e "${GREEN}✅ Frontend stopped${NC}" || echo -e "${YELLOW}ℹ️  Frontend not running${NC}"
pkill -f "npx.*http-server" 2>/dev/null && echo -e "${GREEN}✅ Frontend stopped${NC}" || true

echo ""
echo -e "${BLUE}═══════════════════════════════════════════════════════════════${NC}"
echo -e "${GREEN}✅ ALL SERVICES STOPPED${NC}"
echo -e "${BLUE}═══════════════════════════════════════════════════════════════${NC}"
echo ""

echo -e "${YELLOW}💡 To check if services are still running:${NC}"
echo "   lsof -i :8080  (Backend port)"
echo "   lsof -i :3000  (Frontend port)"

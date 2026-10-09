#!/bin/bash

# 🧪 SAMANA LOCAL TESTING SCRIPT
# Validates all 3 critical features

set -e

echo "╔════════════════════════════════════════════════════════════════╗"
echo "║                                                                ║"
echo "║   🧪 SAMANA PLATFORM - LOCAL VALIDATION TESTS                 ║"
echo "║                                                                ║"
echo "╚════════════════════════════════════════════════════════════════╝"
echo ""

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

# URLs
BACKEND_URL="http://localhost:8080/api"
FRONTEND_URL="http://localhost:3000"

# Counters
TESTS_PASSED=0
TESTS_FAILED=0

# Test function
test_endpoint() {
    local name=$1
    local method=$2
    local endpoint=$3
    local expected_status=$4

    echo -n "Testing: $name... "

    if [ "$method" = "GET" ]; then
        response=$(curl -s -w "\n%{http_code}" "$BACKEND_URL$endpoint")
    elif [ "$method" = "POST" ]; then
        response=$(curl -s -w "\n%{http_code}" -X POST "$BACKEND_URL$endpoint" \
            -H "Content-Type: application/json" \
            -d "$5")
    fi

    status=$(echo "$response" | tail -n 1)
    body=$(echo "$response" | head -n -1)

    if [ "$status" = "$expected_status" ]; then
        echo -e "${GREEN}✅ PASS (HTTP $status)${NC}"
        ((TESTS_PASSED++))
        return 0
    else
        echo -e "${RED}❌ FAIL (Expected $expected_status, got $status)${NC}"
        echo "   Response: $body" | head -c 100
        ((TESTS_FAILED++))
        return 1
    fi
}

# Check backend is running
echo -e "${BLUE}Checking if backend is running...${NC}"
if ! curl -s -f "$BACKEND_URL/experiences" > /dev/null 2>&1; then
    echo -e "${RED}❌ Backend is not running on port 8080${NC}"
    echo "   Please run: ./start-local.sh"
    exit 1
fi
echo -e "${GREEN}✅ Backend is running${NC}"
echo ""

# =====================================================
# FEATURE 1: EXPERIENCE PACKAGES TESTS
# =====================================================
echo -e "${BLUE}═══════════════════════════════════════════════════════════════${NC}"
echo -e "${BLUE}📦 FEATURE 1: EXPERIENCE PACKAGES${NC}"
echo -e "${BLUE}═══════════════════════════════════════════════════════════════${NC}"
echo ""

test_endpoint "Get experience packages" "GET" "/experiences/1/packages" "200"
test_endpoint "Get all packages" "GET" "/packages" "200"

echo ""

# =====================================================
# FEATURE 2: SEARCH & FILTERS TESTS
# =====================================================
echo -e "${BLUE}═══════════════════════════════════════════════════════════════${NC}"
echo -e "${BLUE}🔍 FEATURE 2: SEARCH & FILTERS${NC}"
echo -e "${BLUE}═══════════════════════════════════════════════════════════════${NC}"
echo ""

echo -e "${YELLOW}Filter Options:${NC}"
test_endpoint "Get all states" "GET" "/experiences/filter-options/states" "200"
test_endpoint "Get all locations" "GET" "/experiences/filter-options/locations" "200"

echo ""
echo -e "${YELLOW}Search Queries:${NC}"
test_endpoint "Search all experiences" "GET" "/experiences/search" "200"
test_endpoint "Search by title" "GET" "/experiences/search?title=Holi" "200"
test_endpoint "Filter by state" "GET" "/experiences/filter/state?state=Rajasthan" "200"
test_endpoint "Filter by price range" "GET" "/experiences/filter/price?minPrice=1000&maxPrice=5000" "200"
test_endpoint "Filter by duration" "GET" "/experiences/filter/duration?minDuration=1&maxDuration=7" "200"
test_endpoint "Filter by guests" "GET" "/experiences/filter/guests?minGuests=4" "200"
test_endpoint "Quick search" "GET" "/experiences/quick-search?q=Holi" "200"

echo ""

# =====================================================
# FEATURE 3: FESTIVAL CALENDAR TESTS
# =====================================================
echo -e "${BLUE}═══════════════════════════════════════════════════════════════${NC}"
echo -e "${BLUE}📅 FEATURE 3: FESTIVAL CALENDAR${NC}"
echo -e "${BLUE}═══════════════════════════════════════════════════════════════${NC}"
echo ""

echo -e "${YELLOW}Festival States & Dates:${NC}"
test_endpoint "Get festival states" "GET" "/calendar/festival-states" "200"
test_endpoint "Get festivals for month 3" "GET" "/calendar/festivals/month/3" "200"
test_endpoint "Get festivals for month 1, year 2026" "GET" "/calendar/festivals/month/1/year/2026" "200"
test_endpoint "Get festivals by state" "GET" "/calendar/festivals/state/Rajasthan" "200"
test_endpoint "Get festival statistics" "GET" "/calendar/statistics" "200"
test_endpoint "Get upcoming festivals" "GET" "/calendar/festivals/upcoming" "200"

echo ""

# =====================================================
# FRONTEND TESTS
# =====================================================
echo -e "${BLUE}═══════════════════════════════════════════════════════════════${NC}"
echo -e "${BLUE}🎨 FRONTEND PAGES${NC}"
echo -e "${BLUE}═══════════════════════════════════════════════════════════════${NC}"
echo ""

test_frontend_page() {
    local name=$1
    local path=$2

    echo -n "Testing: $name... "
    if curl -s -f "$FRONTEND_URL/$path" > /dev/null 2>&1; then
        echo -e "${GREEN}✅ PASS${NC}"
        ((TESTS_PASSED++))
    else
        echo -e "${RED}❌ FAIL (Page not found)${NC}"
        ((TESTS_FAILED++))
    fi
}

test_frontend_page "Home page" "index.html"
test_frontend_page "Explore page" "explore.html"
test_frontend_page "Experience details" "experience.html"
test_frontend_page "Bookings page" "bookings.html"
test_frontend_page "Login page" "login.html"

echo ""

# =====================================================
# RESULTS SUMMARY
# =====================================================
echo -e "${BLUE}═══════════════════════════════════════════════════════════════${NC}"
echo -e "${BLUE}📊 TEST RESULTS${NC}"
echo -e "${BLUE}═══════════════════════════════════════════════════════════════${NC}"
echo ""

TOTAL_TESTS=$((TESTS_PASSED + TESTS_FAILED))
SUCCESS_RATE=$((TESTS_PASSED * 100 / TOTAL_TESTS))

echo -e "Total Tests:  $TOTAL_TESTS"
echo -e "${GREEN}Passed:       $TESTS_PASSED${NC}"
if [ $TESTS_FAILED -gt 0 ]; then
    echo -e "${RED}Failed:       $TESTS_FAILED${NC}"
else
    echo -e "${GREEN}Failed:       $TESTS_FAILED${NC}"
fi
echo -e "Success Rate: ${GREEN}${SUCCESS_RATE}%${NC}"
echo ""

if [ $TESTS_FAILED -eq 0 ]; then
    echo -e "${GREEN}✅ ALL TESTS PASSED!${NC}"
    echo ""
    echo -e "${BLUE}═══════════════════════════════════════════════════════════════${NC}"
    echo -e "${GREEN}🎉 READY FOR GCP DEPLOYMENT!${NC}"
    echo -e "${BLUE}═══════════════════════════════════════════════════════════════${NC}"
    exit 0
else
    echo -e "${RED}❌ SOME TESTS FAILED${NC}"
    echo ""
    echo -e "${YELLOW}Please check:${NC}"
    echo "  1. Backend is running: ./start-local.sh"
    echo "  2. Database is set up correctly"
    echo "  3. All environment variables are set"
    echo "  4. Check backend logs for errors"
    exit 1
fi

#!/bin/bash

##############################################################################
# SAMANA Local Development Setup Script
# Automates database, backend, and frontend setup
##############################################################################

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuration
PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
DB_NAME="samana_db"
DB_USER="samana_user"
DB_PASSWORD="samana_password"
DB_HOST="localhost"
DB_PORT="5432"
BACKEND_PORT="8080"
FRONTEND_PORT="3000"
JWT_SECRET="samana-local-dev-secret-key-minimum-32-chars-required-for-hs256-algorithm"

# Helper functions
print_header() {
    echo -e "\n${BLUE}════════════════════════════════════════${NC}"
    echo -e "${BLUE}$1${NC}"
    echo -e "${BLUE}════════════════════════════════════════${NC}\n"
}

print_success() {
    echo -e "${GREEN}✓ $1${NC}"
}

print_error() {
    echo -e "${RED}✗ $1${NC}"
}

print_info() {
    echo -e "${YELLOW}ℹ $1${NC}"
}

check_command() {
    if ! command -v $1 &> /dev/null; then
        print_error "$1 is not installed"
        echo "  Install: $2"
        exit 1
    fi
    print_success "$1 is installed"
}

# ============================================================================
# START SETUP
# ============================================================================

print_header "SAMANA Local Development Setup"

echo "Project Root: $PROJECT_ROOT"
echo "Database: $DB_NAME"
echo "Backend Port: $BACKEND_PORT"
echo "Frontend Port: $FRONTEND_PORT"

# ============================================================================
# 1. Check Prerequisites
# ============================================================================

print_header "1. Checking Prerequisites"

check_command "java" "https://www.oracle.com/java/technologies/downloads/"
java -version | head -n 1

check_command "mvn" "https://maven.apache.org/download.cgi"
mvn -v | head -n 1

check_command "psql" "https://www.postgresql.org/download/"
psql --version

# ============================================================================
# 2. Database Setup
# ============================================================================

print_header "2. Setting Up Database"

print_info "Checking PostgreSQL connection..."
if psql -U postgres -c "SELECT 1" &> /dev/null; then
    print_success "PostgreSQL is running"
else
    print_error "PostgreSQL is not running"
    echo "Start PostgreSQL and run this script again"
    exit 1
fi

print_info "Creating database '$DB_NAME'..."
psql -U postgres -tc "SELECT 1 FROM pg_database WHERE datname = '$DB_NAME'" | grep -q 1 && \
    print_success "Database '$DB_NAME' already exists" || \
    psql -U postgres -c "CREATE DATABASE $DB_NAME" && \
    print_success "Database created"

print_info "Creating user '$DB_USER'..."
psql -U postgres -tc "SELECT 1 FROM pg_user WHERE usename = '$DB_USER'" | grep -q 1 && \
    print_success "User '$DB_USER' already exists" || \
    psql -U postgres -c "CREATE USER $DB_USER WITH PASSWORD '$DB_PASSWORD'" && \
    print_success "User created"

print_info "Granting privileges..."
psql -U postgres -d $DB_NAME -c "GRANT ALL PRIVILEGES ON DATABASE $DB_NAME TO $DB_USER" 2>/dev/null || true
psql -U postgres -d $DB_NAME -c "GRANT ALL ON SCHEMA public TO $DB_USER" 2>/dev/null || true
print_success "Privileges granted"

print_info "Testing database connection..."
if psql -U $DB_USER -d $DB_NAME -h $DB_HOST -c "SELECT 1" &> /dev/null; then
    print_success "Database connection successful"
else
    print_error "Cannot connect to database"
    exit 1
fi

# ============================================================================
# 3. Backend Environment Setup
# ============================================================================

print_header "3. Setting Up Backend Environment"

BACKEND_DIR="$PROJECT_ROOT/backend/samana-backend"

if [ ! -d "$BACKEND_DIR" ]; then
    print_error "Backend directory not found: $BACKEND_DIR"
    exit 1
fi

# Create .env file for backend
ENV_FILE="$BACKEND_DIR/.env"
if [ ! -f "$ENV_FILE" ]; then
    print_info "Creating backend .env file..."
    cat > "$ENV_FILE" << EOF
# Database Configuration
DB_USERNAME=$DB_USER
DB_PASSWORD=$DB_PASSWORD
SPRING_PROFILES_ACTIVE=postgres

# JWT Configuration
JWT_SECRET=$JWT_SECRET

# Server Configuration
PORT=$BACKEND_PORT
EOF
    print_success "Created $ENV_FILE"
else
    print_success "Backend .env file already exists"
fi

# ============================================================================
# 4. Frontend Configuration
# ============================================================================

print_header "4. Updating Frontend Configuration"

FRONTEND_DIR="$PROJECT_ROOT/frontend"
SCRIPT_FILE="$FRONTEND_DIR/js/script.js"

if [ ! -f "$SCRIPT_FILE" ]; then
    print_error "Frontend script file not found: $SCRIPT_FILE"
    exit 1
fi

print_info "Checking if frontend uses local API..."
if grep -q "localhost:8080" "$SCRIPT_FILE"; then
    print_success "Frontend already configured for local"
else
    print_info "Updating frontend API URLs to localhost..."
    # Backup original
    cp "$SCRIPT_FILE" "$SCRIPT_FILE.backup"
    print_success "Created backup: $SCRIPT_FILE.backup"

    # Update API URLs
    if [[ "$OSTYPE" == "darwin"* ]]; then
        # macOS
        sed -i '' 's|https://samana-backend-[^/]*/api|http://localhost:8080/api|g' "$SCRIPT_FILE"
        sed -i '' 's|https://samana-backend-[^/]*|http://localhost:8080|g' "$SCRIPT_FILE"
    else
        # Linux
        sed -i 's|https://samana-backend-[^/]*/api|http://localhost:8080/api|g' "$SCRIPT_FILE"
        sed -i 's|https://samana-backend-[^/]*|http://localhost:8080|g' "$SCRIPT_FILE"
    fi
    print_success "Frontend API URLs updated"
fi

# ============================================================================
# 5. Build Backend
# ============================================================================

print_header "5. Building Backend (This may take a few minutes)"

cd "$BACKEND_DIR"

if [ ! -f "pom.xml" ]; then
    print_error "pom.xml not found in $BACKEND_DIR"
    exit 1
fi

print_info "Running: mvn clean install"
if mvn clean install -q; then
    print_success "Backend build successful"
else
    print_error "Backend build failed"
    print_info "Try running manually: cd $BACKEND_DIR && mvn clean install"
    exit 1
fi

# ============================================================================
# 6. Summary
# ============================================================================

print_header "Setup Complete! 🎉"

cat << EOF
${GREEN}All components are ready for local testing!${NC}

${BLUE}Database:${NC}
  • Host: $DB_HOST
  • Port: $DB_PORT
  • Database: $DB_NAME
  • User: $DB_USER
  • Password: $DB_PASSWORD

${BLUE}Backend:${NC}
  • Location: $BACKEND_DIR
  • Port: $BACKEND_PORT
  • Status: Ready to run
  • Command: cd $BACKEND_DIR && source .env && mvn spring-boot:run

${BLUE}Frontend:${NC}
  • Location: $FRONTEND_DIR
  • Port: $FRONTEND_PORT
  • Status: Ready to serve
  • Command: cd $FRONTEND_DIR && python3 -m http.server $FRONTEND_PORT

${YELLOW}Next Steps:${NC}

1. ${BLUE}Start Backend${NC} (in Terminal 1):
   cd $BACKEND_DIR
   source .env
   mvn spring-boot:run

2. ${BLUE}Start Frontend${NC} (in Terminal 2):
   cd $FRONTEND_DIR
   python3 -m http.server $FRONTEND_PORT

3. ${BLUE}Open Browser${NC}:
   http://localhost:$FRONTEND_PORT

4. ${BLUE}Test the Application${NC}:
   • Navigate to login page
   • Check browser console (F12) for errors
   • Try to register and login

${YELLOW}Useful Commands:${NC}

# Test Backend
curl http://localhost:$BACKEND_PORT/api/categories

# Connect to Database
psql -U $DB_USER -d $DB_NAME -h $DB_HOST

# View Backend Logs
tail -f $BACKEND_DIR/nohup.out

# Reset Database
psql -U $DB_USER -d $DB_NAME -c "DROP SCHEMA public CASCADE; CREATE SCHEMA public;"

${GREEN}For detailed troubleshooting, see LOCAL_SETUP_GUIDE.md${NC}
EOF

print_header "Happy Coding! 🚀"

# 🚀 Local Development Setup Guide - SAMANA

**Last Updated:** October 8, 2026  
**Status:** Ready for Local Testing

---

## 📋 Prerequisites

Before starting, ensure you have installed:

### Required Software:
- **Java 21+** - Download from [Oracle JDK](https://www.oracle.com/java/technologies/downloads/)
- **Maven 3.8+** - [Maven Downloads](https://maven.apache.org/download.cgi)
- **PostgreSQL 14+** - [PostgreSQL Downloads](https://www.postgresql.org/download/)
- **Node.js 18+ (Optional)** - For frontend serving with live reload

### Verify Installation:
```bash
java -version          # Should show Java 21
mvn -v                 # Should show Maven 3.8+
psql --version         # Should show PostgreSQL 14+
```

---

## 🛠️ Step 1: Database Setup (PostgreSQL)

### 1.1 Start PostgreSQL Server

**macOS (Homebrew):**
```bash
brew services start postgresql@14
# or
postgres -D /usr/local/var/postgres
```

**Linux (Ubuntu):**
```bash
sudo systemctl start postgresql
# or
sudo service postgresql start
```

**Windows (Command Prompt):**
```cmd
net start PostgreSQL-x64-14
```

### 1.2 Create Database & User

```bash
# Connect to PostgreSQL
psql -U postgres

# In psql shell:
CREATE DATABASE samana_db;
CREATE USER samana_user WITH PASSWORD 'samana_password';
ALTER ROLE samana_user SET client_encoding TO 'utf8';
ALTER ROLE samana_user SET default_transaction_isolation TO 'read committed';
ALTER ROLE samana_user SET default_transaction_deferrable TO on;
ALTER ROLE samana_user SET timezone TO 'UTC';
GRANT ALL PRIVILEGES ON DATABASE samana_db TO samana_user;
\c samana_db
GRANT ALL ON SCHEMA public TO samana_user;
\q
```

### 1.3 Verify Database Connection

```bash
psql -U samana_user -d samana_db -h localhost -W
# Password: samana_password

# In psql, test:
SELECT 1;  # Should return 1
\q
```

---

## 🔧 Step 2: Backend Setup

### 2.1 Navigate to Backend Directory

```bash
cd /Users/sudhir_ahirkar/Applications/samana/samana/backend/samana-backend
```

### 2.2 Set Environment Variables

Create a `.env` file in the backend root (or export to terminal):

```bash
# Database
export DB_USERNAME=samana_user
export DB_PASSWORD=samana_password
export SPRING_PROFILES_ACTIVE=postgres

# JWT Secret (generate a secure one!)
export JWT_SECRET=your-secret-key-here-minimum-32-characters-long-for-hs256

# Server Port
export PORT=8080
```

### 2.3 Load Environment Variables (Optional)

Create `.env` file:
```bash
cat > .env << 'EOF'
DB_USERNAME=samana_user
DB_PASSWORD=samana_password
SPRING_PROFILES_ACTIVE=postgres
JWT_SECRET=samana-local-dev-secret-key-minimum-32-chars-required-for-hs256-algorithm-minimum-32-chars
PORT=8080
EOF
```

Load environment:
```bash
source .env
```

### 2.4 Build Backend

```bash
# Clean build
mvn clean install

# This will:
# ✓ Download dependencies
# ✓ Compile Java code
# ✓ Run tests (if any)
# ✓ Package JAR file
```

**First build takes 5-10 minutes** (downloading all Maven dependencies)

### 2.5 Run Backend Server

```bash
# Option 1: Using Maven directly
mvn spring-boot:run

# Option 2: Using JAR file
mvn clean package -DskipTests
java -Dspring.profiles.active=postgres \
     -Dserver.port=8080 \
     -DJWT_SECRET="samana-local-dev-secret-key" \
     -jar target/samana-backend-0.0.1-SNAPSHOT.jar

# Option 3: Using Environment Variables
export JWT_SECRET=samana-local-dev-secret-key-minimum-32-chars-required
mvn spring-boot:run
```

### Expected Output:
```
Started SamanaBackendApplication in 4.521 seconds (JVM running for 4.887)
Server is running on port: 8080
```

### 2.6 Test Backend Health

In a new terminal:
```bash
# Check if backend is running
curl http://localhost:8080/api/health

# You should get a response (might be 404 if endpoint doesn't exist)
# But if you can reach it, backend is running!
```

---

## 💻 Step 3: Frontend Setup

### 3.1 Navigate to Frontend Directory

```bash
cd /Users/sudhir_ahirkar/Applications/samana/samana/frontend
```

### 3.2 Update API Configuration (Important!)

Open `frontend/js/script.js` and update the API URL:

```javascript
// CHANGE FROM (Cloud Run):
const API_BASE_URL = "https://samana-backend-82716904797.asia-south1.run.app/api";
const BACKEND_URL = "https://samana-backend-82716904797.asia-south1.run.app";

// TO (Local):
const API_BASE_URL = "http://localhost:8080/api";
const BACKEND_URL = "http://localhost:8080";
```

**Also update these files:**
- `frontend/js/host.js` - Line 1
- `frontend/js/host-profile.js` - Line 5
- `frontend/js/experience.js` - Lines 11, 19
- `frontend/js/explore.js` - Line 10
- `frontend/js/location.js` - (search for API_URL)
- Other JS files with API URLs

### 3.3 Option A: Simple HTTP Server (Recommended for Testing)

Using Python (built-in on macOS/Linux):

```bash
# Python 3
python3 -m http.server 3000

# Python 2 (deprecated)
python -m SimpleHTTPServer 3000
```

Using Node.js:
```bash
# Install simple-http-server globally (if not installed)
npm install -g http-server

# Run server
http-server -p 3000 -c-1
# -p 3000: Use port 3000
# -c-1: Disable caching
```

**Then access:** `http://localhost:3000`

### 3.4 Option B: Live Development Server (Best for Development)

Using Node.js with live reload:

```bash
# Install live-server globally
npm install -g live-server

# Run with auto-reload
live-server --port=3000 --no-browser
```

Open browser: `http://localhost:3000`

### 3.5 Option C: Using VS Code Live Server (Easy)

1. Install "Live Server" extension in VS Code
2. Right-click on `index.html` → "Open with Live Server"
3. Server starts on `http://127.0.0.1:5500`

---

## 🔌 Step 4: Configure API URLs for Local Testing

### Create Local Configuration File

Create `frontend/js/config.js`:

```javascript
// API Configuration
const API_CONFIG = {
  // Local development
  development: {
    apiUrl: 'http://localhost:8080/api',
    backendUrl: 'http://localhost:8080'
  },
  // Production
  production: {
    apiUrl: 'https://samana-backend-82716904797.asia-south1.run.app/api',
    backendUrl: 'https://samana-backend-82716904797.asia-south1.run.app'
  }
};

// Detect environment
const isDev = window.location.hostname === 'localhost' || 
              window.location.hostname === '127.0.0.1';

// Set API URLs
window.API_BASE_URL = isDev ? 
  API_CONFIG.development.apiUrl : 
  API_CONFIG.production.apiUrl;

window.BACKEND_URL = isDev ? 
  API_CONFIG.development.backendUrl : 
  API_CONFIG.production.backendUrl;
```

Add to HTML files before script.js:
```html
<script src="js/config.js"></script>
```

---

## 🧪 Step 5: Testing the Setup

### 5.1 Backend Health Check

```bash
# Test API endpoint
curl -X GET http://localhost:8080/api/categories

# Expected: JSON response or 200 status
```

### 5.2 Frontend Functionality Test

1. Open `http://localhost:3000` in browser
2. Navigate to Login page
3. Check console for errors (F12 → Console tab)
4. Try to login with test credentials

### 5.3 Check Network Requests

1. Open Developer Tools (F12)
2. Go to Network tab
3. Perform an action (login, search, etc.)
4. Check requests going to `http://localhost:8080/api/...`

---

## 🔄 Step 6: Running Both Services

### Terminal Setup (Recommended)

**Terminal 1 - Backend:**
```bash
cd backend/samana-backend
source .env  # Load environment variables
mvn spring-boot:run
```

**Terminal 2 - Frontend:**
```bash
cd frontend
live-server --port=3000 --no-browser
# or
python3 -m http.server 3000
```

**Terminal 3 - Database (if needed):**
```bash
# Monitor PostgreSQL
psql -U samana_user -d samana_db

# Or use pgAdmin for GUI
```

### Quick Start Script

Create `run-local.sh`:

```bash
#!/bin/bash

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

PROJECT_ROOT="/Users/sudhir_ahirkar/Applications/samana/samana"

echo -e "${GREEN}=== SAMANA Local Development Setup ===${NC}\n"

# Check Java
echo -e "${YELLOW}Checking Java...${NC}"
if ! command -v java &> /dev/null; then
    echo -e "${RED}Java not installed!${NC}"
    exit 1
fi
java -version

# Check Maven
echo -e "${YELLOW}Checking Maven...${NC}"
if ! command -v mvn &> /dev/null; then
    echo -e "${RED}Maven not installed!${NC}"
    exit 1
fi
mvn -v

# Check PostgreSQL
echo -e "${YELLOW}Checking PostgreSQL...${NC}"
if ! command -v psql &> /dev/null; then
    echo -e "${RED}PostgreSQL not installed!${NC}"
    exit 1
fi
psql --version

# Start Backend
echo -e "${GREEN}Starting Backend...${NC}"
cd "$PROJECT_ROOT/backend/samana-backend"
export SPRING_PROFILES_ACTIVE=postgres
export DB_USERNAME=samana_user
export DB_PASSWORD=samana_password
export JWT_SECRET=samana-local-dev-secret-key-minimum-32-chars-required
mvn spring-boot:run &
BACKEND_PID=$!
echo -e "${GREEN}Backend PID: $BACKEND_PID${NC}"

# Wait for backend to start
sleep 5

# Start Frontend
echo -e "${GREEN}Starting Frontend...${NC}"
cd "$PROJECT_ROOT/frontend"
python3 -m http.server 3000 &
FRONTEND_PID=$!
echo -e "${GREEN}Frontend PID: $FRONTEND_PID${NC}"

echo -e "${GREEN}=== All Services Running ===${NC}"
echo -e "Backend:  http://localhost:8080"
echo -e "Frontend: http://localhost:3000"
echo -e "Database: localhost:5432 (samana_db)"
echo -e "\nPress Ctrl+C to stop all services"

# Wait for Ctrl+C
wait

# Cleanup
kill $BACKEND_PID $FRONTEND_PID 2>/dev/null
echo -e "${YELLOW}Services stopped${NC}"
```

Run with:
```bash
chmod +x run-local.sh
./run-local.sh
```

---

## 🐛 Troubleshooting

### Backend Won't Start

**Error: "Connection refused" (PostgreSQL)**
```bash
# Verify PostgreSQL is running
psql -U postgres -c "SELECT version();"

# If not running, start it
brew services start postgresql@14  # macOS
sudo systemctl start postgresql     # Linux
```

**Error: "JWT_SECRET not set"**
```bash
# Set the environment variable
export JWT_SECRET=samana-local-dev-secret-key-minimum-32-chars-required-for-hs256

# Or add to .env file
echo "JWT_SECRET=samana-local-dev-secret-key-minimum-32-chars-required" >> .env
source .env
```

**Error: "Port 8080 already in use"**
```bash
# Kill process using port 8080
lsof -i :8080
kill -9 <PID>

# Or use different port
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=9090"
```

### Frontend Can't Connect to Backend

**Error: "CORS error" or "Failed to fetch"**
```
Solution: Ensure backend is running and check:
1. Backend is on http://localhost:8080
2. Frontend API URL is updated in config
3. Open DevTools → Network tab to see actual requests
```

**Error: "API_BASE_URL undefined"**
```
Solution: Ensure config.js is loaded before script.js
In HTML: <script src="js/config.js"></script> (before script.js)
```

**Error: "401 Unauthorized on all requests"**
```
Solution: JWT token not being sent or expired
1. Login again to get fresh token
2. Check localStorage in DevTools (F12 → Application → LocalStorage)
3. Token should be stored at: localhost:3000
```

### PostgreSQL Issues

**Can't connect to database**
```bash
# Test connection
psql -U samana_user -d samana_db -h localhost -W
# Password: samana_password

# If connection fails, check PostgreSQL logs
# macOS: tail -f /usr/local/var/log/postgres.log
```

**Database tables not created**
```
Spring will auto-create tables (spring.jpa.hibernate.ddl-auto=update)
Check backend logs for: "Hibernate: CREATE TABLE..."
```

---

## 📊 Testing Checklist

After setup, verify:

- [ ] Backend server running on port 8080
- [ ] Frontend server running on port 3000
- [ ] PostgreSQL database `samana_db` created
- [ ] Can access `http://localhost:3000` in browser
- [ ] No errors in browser console (F12)
- [ ] API URLs in JavaScript files point to `localhost:8080`
- [ ] Can register a new user
- [ ] Can login with created credentials
- [ ] Form validation working (toast notifications appear)
- [ ] Images load from server
- [ ] Can navigate between pages without errors

---

## 🚀 Running Tests

### Backend Tests

```bash
cd backend/samana-backend

# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=UserServiceTest

# Skip tests during build
mvn clean package -DskipTests
```

### Frontend Testing

Open DevTools (F12) and check:

```javascript
// Test toast notifications
toast.success("Test notification");

// Test form validation
new FormValidator('loginForm');

// Test API calls
fetch('http://localhost:8080/api/categories')
  .then(r => r.json())
  .then(d => console.log(d));
```

---

## 📝 Important Notes

1. **JWT_SECRET:** Must be at least 32 characters for HS256 algorithm
2. **Database:** Auto-created schema via Hibernate (check logs)
3. **API URLs:** Must be updated in ALL JS files for local testing
4. **CORS:** Backend allows localhost via Spring configuration
5. **Images:** Stored on server, served via `/uploads/` endpoint

---

## 🔐 Security for Local Development

**DO NOT use these settings in production:**

```properties
# Local dev (unsafe)
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
server.error.include-message=always
spring.security.debug=true  # Logs all security events

# These are safe for debugging locally
# But disable in production
```

---

## 🌐 Switching Between Local and Production

### Before Deploying to GCP

1. Update API URLs back to production:
```javascript
// In frontend/js/script.js
const API_BASE_URL = "https://samana-backend-xxxx.run.app/api";
const BACKEND_URL = "https://samana-backend-xxxx.run.app";
```

2. Update environment-specific config:
```bash
# In backend
export SPRING_PROFILES_ACTIVE=cloud
export JWT_SECRET=<production-secret>
export DB_USERNAME=<cloud-db-user>
export DB_PASSWORD=<cloud-db-password>
```

3. Test in production environment:
```bash
mvn clean package -DskipTests
java -jar target/samana-backend-0.0.1-SNAPSHOT.jar
```

---

## 📞 Quick Reference Commands

```bash
# Start all services
cd backend/samana-backend && source .env && mvn spring-boot:run &
cd frontend && python3 -m http.server 3000 &

# Check if services are running
curl http://localhost:8080/api/categories
curl http://localhost:3000

# Stop services
pkill -f "mvn spring-boot:run"
pkill -f "http.server"

# View logs
tail -f backend/samana-backend/nohup.out

# Reset database
psql -U samana_user -d samana_db -c "DROP SCHEMA public CASCADE; CREATE SCHEMA public;"
```

---

## ✅ Success Indicators

You'll know everything is working when:

1. ✅ Backend starts with message: "Started SamanaBackendApplication"
2. ✅ Frontend loads at `http://localhost:3000` without errors
3. ✅ Browser console shows no CORS errors
4. ✅ Login form works and redirects to dashboard
5. ✅ Toast notifications appear when submitting forms
6. ✅ Network requests show 200 status codes
7. ✅ Database records are created/updated on actions

---

**Ready to test locally! 🎉**

For GCP deployment after testing, see `DEPLOY_GCP.md` (coming soon)

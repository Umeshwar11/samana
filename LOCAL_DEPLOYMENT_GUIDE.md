# 🚀 LOCAL DEPLOYMENT & TESTING GUIDE

**Status:** Ready for Local Setup  
**Estimated Time:** 30-45 minutes  
**Next Step:** GCP Deployment

---

## 📋 PRE-REQUISITES

### Required Software
- ✅ Java 21 JDK (Spring Boot requirement)
- ✅ Maven 3.8+
- ✅ PostgreSQL 14+
- ✅ Node.js 18+ (for frontend)
- ✅ Git

### Verify Installation
```bash
java -version          # Should show Java 21
mvn -version          # Should show Maven 3.8+
psql --version        # Should show PostgreSQL 14+
node --version        # Should show Node 18+
```

---

## 🗄️ STEP 1: DATABASE SETUP

### 1.1 Start PostgreSQL
```bash
# macOS (if using Homebrew)
brew services start postgresql

# Verify it's running
psql --version
```

### 1.2 Create Database & User
```bash
# Connect to PostgreSQL
psql -U postgres

# Inside psql:
CREATE DATABASE samana_db;
CREATE USER samana_user WITH PASSWORD 'samana_password';
GRANT ALL PRIVILEGES ON DATABASE samana_db TO samana_user;
\q
```

### 1.3 Verify Database
```bash
psql -U samana_user -d samana_db -h localhost
\dt  # List tables (should be empty at first)
\q
```

---

## 🔧 STEP 2: BACKEND SETUP & LOCAL BUILD

### 2.1 Navigate to Backend
```bash
cd /Users/sudhir_ahirkar/Applications/samana/samana/backend/samana-backend
```

### 2.2 Set Environment Variables
```bash
# Create .env file in backend root
cat > .env << 'EOF'
export DB_USERNAME=samana_user
export DB_PASSWORD=samana_password
export SPRING_PROFILES_ACTIVE=postgres
export JWT_SECRET=your-secret-key-min-32-chars-long
export PORT=8080
EOF

# Load environment variables
source .env
```

### 2.3 Clean & Build Backend
```bash
# Clean previous builds
mvn clean

# Run all tests
mvn test

# Build the application
mvn clean package -DskipTests

# Output should be in target/samana-backend-0.0.1-SNAPSHOT.jar
ls -lh target/*.jar
```

### 2.4 Start Backend Locally
```bash
# Option 1: Using Maven
mvn spring-boot:run

# Option 2: Using JAR (runs on port 8080)
java -jar target/samana-backend-0.0.1-SNAPSHOT.jar

# Expected output:
# Started SamanaApplication in X.XXX seconds (JVM running for X.XXX)
# Tomcat started on port 8080 (http)
```

### 2.5 Verify Backend is Running
```bash
# In a new terminal:
curl http://localhost:8080/api/experiences

# Should return:
# {"total":0,"experiences":[]}
# Or test specific endpoints:
curl http://localhost:8080/api/calendar/festival-states
curl http://localhost:8080/api/experiences/filter-options/states
```

---

## 🎨 STEP 3: FRONTEND SETUP & LOCAL BUILD

### 3.1 Navigate to Frontend
```bash
cd /Users/sudhir_ahirkar/Applications/samana/samana/frontend
# or
cd /Users/sudhir_ahirkar/Applications/samana/samana
# Check where frontend files are
ls -la
```

### 3.2 Start Frontend Locally
```bash
# If using Python (simple HTTP server)
cd frontend
python3 -m http.server 3000

# Or if using Node.js
npx http-server -p 3000

# Or if it's a React app
npm install
npm start
```

### 3.3 Access Frontend
```
http://localhost:3000
```

### 3.4 Verify Frontend Configuration
The frontend should be configured to use the backend at:
```javascript
// In frontend JS files, update API_BASE_URL if needed:
const API_BASE_URL = 'http://localhost:8080/api';
```

---

## 🧪 STEP 4: VALIDATION TESTING

### 4.1 Test Backend Endpoints

#### Test Search & Filters
```bash
# Get all filter options (states)
curl http://localhost:8080/api/experiences/filter-options/states

# Search experiences
curl "http://localhost:8080/api/experiences/search?title=Holi&state=Uttar%20Pradesh"

# Quick search
curl "http://localhost:8080/api/experiences/quick-search?q=Holi"

# Filter by price
curl "http://localhost:8080/api/experiences/filter/price?minPrice=1000&maxPrice=5000"

# Filter by duration
curl "http://localhost:8080/api/experiences/filter/duration?minDuration=1&maxDuration=7"
```

#### Test Experience Packages
```bash
# Get packages for an experience (if experience ID 1 exists)
curl http://localhost:8080/api/experiences/1/packages

# Create a new package
curl -X POST http://localhost:8080/api/packages \
  -H "Content-Type: application/json" \
  -d '{
    "experienceId": 1,
    "name": "3-Day Experience",
    "description": "A 3-day festival experience",
    "price": 5000,
    "duration": 3,
    "maxGuests": 8,
    "included": "Accommodation, Meals, Guide",
    "notIncluded": "Travel, Personal Items"
  }'
```

#### Test Festival Calendar
```bash
# Get festivals by month
curl http://localhost:8080/api/calendar/festivals/month/3

# Get festivals by state and month
curl "http://localhost:8080/api/calendar/festivals/state/Rajasthan/month/3"

# Get festival states
curl http://localhost:8080/api/calendar/festival-states

# Get upcoming festivals
curl http://localhost:8080/api/calendar/festivals/upcoming

# Create available date
curl -X POST http://localhost:8080/api/calendar/available-dates \
  -H "Content-Type: application/json" \
  -d '{
    "experienceId": 1,
    "date": "2026-03-25",
    "totalSlots": 10,
    "isFestivalDate": true,
    "festivalName": "Holi"
  }'
```

### 4.2 Test Frontend UI

#### Search & Filters
1. Go to http://localhost:3000/explore.html
2. Try searching for a festival
3. Apply filters (state, location, price, duration)
4. Verify results update

#### Experience Packages
1. Go to http://localhost:3000/experience.html?id=1
2. Scroll to packages section
3. Select different packages
4. Verify price updates

#### Festival Calendar
1. Go to http://localhost:3000/calendar.html
2. Click month navigation buttons
3. Try filtering by state
4. Click to view experience details

### 4.3 Check Logs for Errors

#### Backend Logs
```bash
# While backend is running, check for:
# - No "ERROR" messages
# - Database connected successfully
# - All tables created

# Look for:
# Hibernate: ALTER TABLE experiences ADD COLUMN...
# Tables created: experience_packages, available_dates
```

#### Frontend Logs
```bash
# In browser console (F12):
# - No red error messages
# - API calls should return 200 status
# - Check Network tab for API responses
```

---

## 📊 STEP 5: COMPREHENSIVE TEST CHECKLIST

### Backend Tests
- [ ] Backend starts on port 8080
- [ ] Database connects successfully
- [ ] All tables created automatically
- [ ] GET /api/experiences returns 200
- [ ] GET /api/experiences/filter-options/states returns states
- [ ] GET /api/experiences/search works with filters
- [ ] POST /api/packages creates package
- [ ] GET /api/calendar/festivals/month/3 returns festivals
- [ ] POST /api/calendar/available-dates creates date
- [ ] All error responses have proper status codes

### Frontend Tests
- [ ] Frontend loads on port 3000
- [ ] Page layout is responsive
- [ ] Search bar works
- [ ] Filters load and apply correctly
- [ ] Package cards display properly
- [ ] Calendar navigates between months
- [ ] All links work (no 404s)
- [ ] API calls reach backend successfully
- [ ] Error messages display properly

### Integration Tests
- [ ] Create experience → Add packages → View on frontend
- [ ] Add available dates → Calendar shows festival
- [ ] Search → Click result → See packages
- [ ] Filter experiences → Book package flow works
- [ ] No console errors (F12)
- [ ] No CORS errors

---

## 🐛 TROUBLESHOOTING

### Issue: Database Connection Refused
```
Error: Connection to localhost:5432 refused

Solution:
1. Check PostgreSQL is running: brew services list
2. Start it: brew services start postgresql
3. Verify credentials in application.properties
4. Check port 5432 is open: lsof -i :5432
```

### Issue: Port 8080 Already in Use
```
Error: Address already in use: bind

Solution:
# Find what's using port 8080
lsof -i :8080

# Kill the process
kill -9 <PID>

# Or use a different port
PORT=8081 mvn spring-boot:run
```

### Issue: Frontend API Calls Failing
```
Error: CORS error or 404

Solution:
1. Verify backend is running on 8080
2. Check API_BASE_URL in frontend code
3. Check @CrossOrigin annotations on controllers
4. Look at Network tab in browser DevTools
5. Check backend logs for actual errors
```

### Issue: Tables Not Created
```
Error: Table not found

Solution:
1. Check spring.jpa.hibernate.ddl-auto=update
2. Restart backend (drop data and recreate)
3. Check database user has permissions
4. Look for Hibernate errors in logs
```

### Issue: Tests Failing
```
Error: Unit tests failing locally

Solution:
1. Run: mvn clean test
2. Check test output for specific failure
3. Verify database is running
4. Check @TestPropertySource in test files
5. Look at test logs in target/surefire-reports/
```

---

## ✅ LOCAL VALIDATION CHECKLIST

Before proceeding to GCP, verify:

### Backend
- [ ] Compiles without errors
- [ ] All 60+ tests pass (mvn test)
- [ ] Runs on localhost:8080
- [ ] Database connections work
- [ ] All 26+ endpoints respond correctly
- [ ] Error handling works
- [ ] Logging is visible
- [ ] No security warnings

### Frontend
- [ ] Loads on localhost:3000
- [ ] All pages accessible
- [ ] API integration works
- [ ] Search functionality works
- [ ] Filters apply correctly
- [ ] Package selection works
- [ ] Calendar displays properly
- [ ] Mobile responsive (test on F12)

### Integration
- [ ] User can complete full flow:
  1. Search for experience
  2. Apply filters
  3. View experience details
  4. Select package
  5. Navigate calendar
- [ ] All API responses valid
- [ ] No console errors
- [ ] No CORS issues
- [ ] Performance acceptable

---

## 📱 DEVICE TESTING CHECKLIST

### Desktop (Chrome)
- [ ] Page loads
- [ ] All features work
- [ ] Performance good

### Mobile (DevTools Emulation)
- [ ] iPhone SE (375px)
- [ ] iPhone 12 (390px)
- [ ] iPad (768px)
- [ ] Touch targets large enough
- [ ] Text readable

---

## 🚀 NEXT STEPS: GCP DEPLOYMENT

Once local validation is complete:

1. **Build Docker image** for backend
2. **Deploy to Cloud Run** (backend)
3. **Deploy to Cloud Storage/CDN** (frontend)
4. **Configure Cloud SQL** (PostgreSQL)
5. **Setup firewall rules** and SSL
6. **Verify on GCP** endpoints work
7. **Monitor logs** and metrics
8. **Test production flow** end-to-end

---

## 📝 COMMANDS REFERENCE

### Quick Start (All-in-One)
```bash
# Terminal 1 - Backend
cd backend/samana-backend
source .env
mvn clean test && mvn spring-boot:run

# Terminal 2 - Frontend
cd frontend
python3 -m http.server 3000

# Terminal 3 - Testing
curl http://localhost:8080/api/experiences
# Open browser to http://localhost:3000
```

### Troubleshooting Commands
```bash
# Check database
psql -U samana_user -d samana_db -h localhost

# Check ports
lsof -i :8080
lsof -i :3000

# Kill processes
kill -9 <PID>

# View logs
tail -f backend/samana-backend/logs/*.log

# Run specific tests
mvn test -Dtest=ExperienceSearchServiceTest
```

---

## 📞 SUPPORT

**Issues?** Check:
1. Backend logs (console output)
2. Frontend console (F12)
3. Database connection (psql)
4. Port availability (lsof)
5. Error responses (curl output)

**Next:** Once validation complete, proceed to GCP_DEPLOYMENT.md

---

**Last Updated:** October 9, 2026  
**Status:** Ready for Local Testing  
**Estimated Completion:** 45 minutes

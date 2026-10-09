# 🚀 DEPLOYMENT WORKFLOW - FROM LOCAL TO GCP

**Complete End-to-End Deployment Guide**  
**Status:** Ready for Execution  
**Total Time:** ~3-4 hours (Local validation + GCP deployment)

---

## 📋 WORKFLOW OVERVIEW

```
┌─────────────────────────────────────────────────────────────┐
│                                                             │
│  1. LOCAL SETUP (30-45 min)                                │
│     └─ Database setup                                      │
│     └─ Backend build & start                               │
│     └─ Frontend start                                      │
│                                                             │
│  2. LOCAL VALIDATION (15-30 min)                           │
│     └─ Run test suite (26+ endpoints)                      │
│     └─ Manual feature testing                              │
│     └─ Performance check                                   │
│                                                             │
│  3. GCP DEPLOYMENT (1-2 hours)                             │
│     └─ Docker build & push                                 │
│     └─ Cloud SQL setup                                     │
│     └─ Cloud Run deployment                                │
│     └─ Cloud Storage + CDN setup                           │
│                                                             │
│  4. PRODUCTION VALIDATION (30-45 min)                      │
│     └─ End-to-end testing on GCP                           │
│     └─ Performance validation                              │
│     └─ Monitoring setup                                    │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

---

## ⏱️ PHASE 1: LOCAL SETUP (30-45 minutes)

### 1.1 Prerequisites Check
```bash
# Verify all required software is installed
java -version          # Java 21+
mvn -version          # Maven 3.8+
psql --version        # PostgreSQL 14+
node --version        # Node 18+ (optional for frontend)
docker --version      # Docker (for GCP later)
```

### 1.2 Start All Services
```bash
# One command to start everything
./start-local.sh

# Expected output:
# ✅ Database ready
# ✅ Backend built successfully
# ✅ Backend is running (port 8080)
# ✅ Frontend is running (port 3000)
```

### 1.3 Verify Services
```bash
# Backend
curl http://localhost:8080/api/experiences
# Should return: {"total":0,"experiences":[]}

# Frontend
curl http://localhost:3000/
# Should return HTML content

# Both should be healthy
```

---

## 🧪 PHASE 2: LOCAL VALIDATION (15-30 minutes)

### 2.1 Run Automated Test Suite
```bash
# Run comprehensive tests
./test-local.sh

# Expected output:
# ✅ Feature 1: Experience Packages (5 tests)
# ✅ Feature 2: Search & Filters (9 tests)
# ✅ Feature 3: Festival Calendar (6 tests)
# ✅ Frontend Pages (5 tests)
# ✅ Success Rate: 100%
```

### 2.2 Manual Feature Testing

#### Search & Filters
```bash
# 1. Open http://localhost:3000/explore.html
# 2. Try each filter:
#    - Search by festival name
#    - Filter by state
#    - Filter by location
#    - Filter by price range
#    - Filter by duration
#    - Apply multiple filters
# 3. Verify results update correctly
```

#### Experience Packages
```bash
# 1. Navigate to an experience detail page
# 2. View package options
# 3. Select different packages
# 4. Verify prices update
# 5. Test on mobile view (F12 → Toggle device toolbar)
```

#### Festival Calendar
```bash
# 1. Navigate to festival calendar (if created)
# 2. Test month navigation
# 3. Filter by state
# 4. Click to view experience details
# 5. Test responsive design
```

### 2.3 Backend Testing
```bash
# Test all 26+ endpoints
curl http://localhost:8080/api/experiences/filter-options/states
curl http://localhost:8080/api/experiences/search?title=Holi
curl http://localhost:8080/api/calendar/festival-states

# Check logs for errors
tail -f backend/samana-backend/backend.log
```

### 2.4 Database Validation
```bash
# Connect to local database
psql -U samana_user -d samana_db -h localhost

# Check tables created
\dt

# Sample queries
SELECT COUNT(*) FROM experiences;
SELECT COUNT(*) FROM experience_packages;
SELECT COUNT(*) FROM available_dates;

# Exit
\q
```

### 2.5 Performance Check
```bash
# Backend response time should be < 500ms
time curl http://localhost:8080/api/experiences/search?title=Holi

# Frontend load time should be < 2s
curl -o /dev/null -s -w "Time: %{time_total}s\n" http://localhost:3000
```

### ✅ Sign-Off: Local Validation Complete
```
All tests passed ✅
- Backend: Healthy
- Frontend: Responsive
- Database: Connected
- APIs: All working
- Performance: Good

Ready to proceed to GCP deployment
```

---

## ☁️ PHASE 3: GCP DEPLOYMENT (1-2 hours)

### 3.1 GCP Project Setup
```bash
# Login to GCP
gcloud auth login

# Set project
gcloud config set project YOUR_PROJECT_ID

# Enable required APIs
gcloud services enable compute.googleapis.com
gcloud services enable run.googleapis.com
gcloud services enable cloudsql.googleapis.com
gcloud services enable storage-api.googleapis.com
gcloud services enable container.googleapis.com
gcloud services enable artifactregistry.googleapis.com
```

### 3.2 Build Docker Image
```bash
cd backend/samana-backend

# Build image
docker build -t samana-backend:latest .

# Test locally
docker run -d --name samana-test \
  -p 8081:8080 \
  -e DB_USERNAME=test \
  -e JWT_SECRET=test \
  samana-backend:latest

# Verify
curl http://localhost:8081/api/experiences

# Cleanup
docker stop samana-test && docker rm samana-test
```

### 3.3 Push to Artifact Registry
```bash
# Create repository
gcloud artifacts repositories create samana-repo \
  --repository-format=docker \
  --location=us-central1

# Configure Docker
gcloud auth configure-docker us-central1-docker.pkg.dev

# Tag and push
docker tag samana-backend:latest \
  us-central1-docker.pkg.dev/YOUR_PROJECT_ID/samana-repo/backend:latest

docker push us-central1-docker.pkg.dev/YOUR_PROJECT_ID/samana-repo/backend:latest
```

### 3.4 Setup Cloud SQL
```bash
# Create PostgreSQL instance
gcloud sql instances create samana-postgres \
  --database-version=POSTGRES_15 \
  --tier=db-f1-micro \
  --region=us-central1

# Create database
gcloud sql databases create samana_db \
  --instance=samana-postgres

# Create user
gcloud sql users create samana_user \
  --instance=samana-postgres \
  --password=YOUR_STRONG_PASSWORD

# Store password in Secret Manager
echo -n "YOUR_STRONG_PASSWORD" | \
  gcloud secrets create samana-db-password --data-file=-
```

### 3.5 Deploy to Cloud Run
```bash
# Deploy backend
gcloud run deploy samana-backend \
  --image=us-central1-docker.pkg.dev/YOUR_PROJECT_ID/samana-repo/backend:latest \
  --platform=managed \
  --region=us-central1 \
  --allow-unauthenticated \
  --memory=512Mi \
  --cpu=1 \
  --set-env-vars="SPRING_PROFILES_ACTIVE=cloud,DB_USERNAME=samana_user" \
  --set-secrets="DB_PASSWORD=samana-db-password:latest,JWT_SECRET=jwt-secret:latest"

# Get service URL
BACKEND_URL=$(gcloud run services describe samana-backend \
  --region=us-central1 \
  --format="value(status.url)")

echo "Backend URL: $BACKEND_URL"
```

### 3.6 Deploy Frontend
```bash
# Create Cloud Storage bucket
gsutil mb -l us-central1 gs://samana-frontend

# Make public
gsutil iam ch allUsers:objectViewer gs://samana-frontend

# Update API URLs in HTML files
cd /path/to/frontend
find . -name "*.html" -type f \
  -exec sed -i "s|http://localhost:8080|$BACKEND_URL|g" {} \;

# Upload files
gsutil -m cp -r . gs://samana-frontend/

# Enable website hosting
gsutil web set -m index.html -e index.html gs://samana-frontend

echo "Frontend URL: https://samana-frontend.storage.googleapis.com"
```

---

## 🧪 PHASE 4: PRODUCTION VALIDATION (30-45 minutes)

### 4.1 Test Backend on GCP
```bash
BACKEND_URL=YOUR_CLOUD_RUN_URL

# Test key endpoints
curl $BACKEND_URL/api/experiences
curl $BACKEND_URL/api/experiences/filter-options/states
curl $BACKEND_URL/api/calendar/festival-states

# All should return 200 OK
```

### 4.2 Test Frontend on GCP
```bash
# Open in browser
https://samana-frontend.storage.googleapis.com

# Test workflows
# 1. Search and filter experiences
# 2. View experience packages
# 3. Browse festival calendar
# 4. Check mobile responsiveness
```

### 4.3 Database Verification
```bash
# Connect to Cloud SQL
gcloud sql connect samana-postgres --user=samana_user

# Check tables and data
\dt
SELECT COUNT(*) FROM experiences;
SELECT COUNT(*) FROM experience_packages;

\q
```

### 4.4 Performance Check on GCP
```bash
# Measure latency
time curl $BACKEND_URL/api/experiences/search?title=Holi

# Should be < 1 second
```

### 4.5 Setup Monitoring
```bash
# View logs
gcloud run logs read samana-backend --region=us-central1

# Setup alerts (via Cloud Console)
# - High error rate (> 1%)
# - High latency (> 5s)
# - Service unavailability
```

### ✅ Sign-Off: Production Ready
```
GCP Deployment Complete ✅
- Backend: Running on Cloud Run
- Frontend: Served from Cloud Storage + CDN
- Database: PostgreSQL on Cloud SQL
- Monitoring: Active
- Performance: Good
- Security: Configured

Ready for public launch
```

---

## 🎯 COMPLETE CHECKLIST

### Local Setup
- [ ] Database created and running
- [ ] Backend compiled without errors
- [ ] Frontend accessible
- [ ] All services started

### Local Validation
- [ ] All 26+ endpoints tested
- [ ] Feature 1: Packages working
- [ ] Feature 2: Search & Filters working
- [ ] Feature 3: Calendar working
- [ ] Frontend pages responsive
- [ ] No console errors

### GCP Deployment
- [ ] Docker image built
- [ ] Image pushed to Artifact Registry
- [ ] Cloud SQL instance created
- [ ] Database initialized
- [ ] Cloud Run service deployed
- [ ] Frontend uploaded to Cloud Storage
- [ ] CORS configured
- [ ] Secrets stored in Secret Manager

### Production Validation
- [ ] Backend endpoints responding
- [ ] Frontend loading correctly
- [ ] API integration working
- [ ] Database connected
- [ ] Performance acceptable
- [ ] Logging enabled
- [ ] Alerts configured

---

## 📞 QUICK REFERENCE

### Start Local Services
```bash
./start-local.sh
```

### Run Tests
```bash
./test-local.sh
```

### Stop Services
```bash
./stop-local.sh
```

### GCP Commands
```bash
# Login
gcloud auth login

# Deploy backend
gcloud run deploy samana-backend \
  --image=us-central1-docker.pkg.dev/PROJECT/samana-repo/backend:latest

# View logs
gcloud run logs read samana-backend --region=us-central1

# Update service
gcloud run services update samana-backend --region=us-central1 \
  --set-env-vars=KEY=VALUE
```

---

## ❌ TROUBLESHOOTING

### Local Issues
**Backend won't start:**
```bash
# Check if port 8080 is in use
lsof -i :8080

# Check logs
tail backend/samana-backend/backend.log

# Clean rebuild
mvn clean install
```

**Database connection failed:**
```bash
# Verify PostgreSQL is running
brew services start postgresql  # macOS

# Check database exists
psql -U postgres -c "\l"

# Recreate if needed
psql -U postgres -c "DROP DATABASE samana_db;"
psql -U postgres -c "CREATE DATABASE samana_db;"
```

**Frontend API calls failing:**
```bash
# Check CORS headers
curl -I http://localhost:8080/api/experiences

# Verify API_BASE_URL in frontend
grep -r "localhost:8080" *.html

# Update to correct backend URL
```

### GCP Issues
**Cloud Run deployment fails:**
```bash
# Check image
gcloud artifacts docker images list \
  us-central1-docker.pkg.dev/PROJECT/samana-repo

# Check logs
gcloud run logs read samana-backend --region=us-central1
```

**Database connection fails on Cloud Run:**
```bash
# Verify Cloud SQL proxy
gcloud sql instances describe samana-postgres

# Check environment variables
gcloud run services describe samana-backend --region=us-central1
```

---

## 🎉 SUCCESS CRITERIA

### All Three Features Working
- ✅ Users can select different experience packages
- ✅ Users can search and filter experiences
- ✅ Users can browse festival calendar

### Performance Targets
- ✅ Backend response time < 500ms
- ✅ Frontend load time < 2 seconds
- ✅ Database queries < 100ms

### Quality Standards
- ✅ Zero critical bugs in testing
- ✅ All 26+ endpoints tested
- ✅ No console errors
- ✅ No CORS issues
- ✅ Mobile responsive

---

## 📈 NEXT STEPS AFTER LAUNCH

1. **User Onboarding**
   - Set up user invitations
   - Create onboarding flow
   - Monitor user adoption

2. **Performance Monitoring**
   - Track response times
   - Monitor error rates
   - Analyze user behavior

3. **Feedback Collection**
   - User surveys
   - Bug reports
   - Feature requests

4. **Continuous Improvement**
   - Bug fixes
   - Performance optimization
   - Feature enhancements

---

**Total Time Estimate: 3-4 hours**  
**Complexity: Medium**  
**Risk Level: Low (with local validation)**

**Status: ✅ Ready to Deploy**

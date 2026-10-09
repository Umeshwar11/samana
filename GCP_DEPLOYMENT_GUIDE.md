# ☁️ GCP DEPLOYMENT GUIDE - SAMANA PLATFORM

**Environment:** Google Cloud Platform (GCP)  
**Services:** Cloud Run (Backend), Cloud Storage (Frontend), Cloud SQL (Database)  
**Status:** Ready for Production Deployment  

---

## 📋 PRE-REQUISITES

### 1. GCP Account & Project Setup
```bash
# Login to GCP
gcloud auth login

# Set your project
gcloud config set project samana-project  # Replace with your project ID

# Enable required APIs
gcloud services enable compute.googleapis.com
gcloud services enable run.googleapis.com
gcloud services enable cloudsql.googleapis.com
gcloud services enable storage-api.googleapis.com
gcloud services enable container.googleapis.com
gcloud services enable artifactregistry.googleapis.com
```

### 2. Required Tools
- ✅ Google Cloud SDK
- ✅ Docker
- ✅ gcloud CLI

### 3. Verify Installation
```bash
gcloud --version
docker --version
```

---

## 🐳 STEP 1: BUILD BACKEND DOCKER IMAGE

### 1.1 Create Dockerfile
```bash
cd /Users/sudhir_ahirkar/Applications/samana/samana/backend/samana-backend
cat > Dockerfile << 'EOF'
# Build stage
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /build/target/*.jar app.jar

EXPOSE 8080
ENV SPRING_PROFILES_ACTIVE=cloud
ENV PORT=8080

CMD ["java", "-jar", "app.jar"]
EOF

cat Dockerfile
```

### 1.2 Build Image Locally (Test)
```bash
docker build -t samana-backend:latest .
docker images | grep samana
```

### 1.3 Test Docker Image Locally
```bash
# Run with environment variables
docker run -d \
  --name samana-test \
  -p 8080:8080 \
  -e DB_USERNAME=test \
  -e DB_PASSWORD=test \
  -e JWT_SECRET=test-secret-key \
  samana-backend:latest

# Check logs
docker logs samana-test

# Stop and remove
docker stop samana-test
docker rm samana-test
```

### 1.4 Push to Artifact Registry

#### Option A: Google Artifact Registry
```bash
# Setup Artifact Registry
gcloud artifacts repositories create samana-repo \
  --repository-format=docker \
  --location=us-central1

# Configure Docker authentication
gcloud auth configure-docker us-central1-docker.pkg.dev

# Tag image
docker tag samana-backend:latest \
  us-central1-docker.pkg.dev/samana-project/samana-repo/backend:latest

# Push to registry
docker push us-central1-docker.pkg.dev/samana-project/samana-repo/backend:latest

# Verify
gcloud artifacts docker images list us-central1-docker.pkg.dev/samana-project/samana-repo
```

---

## 🗄️ STEP 2: SETUP CLOUD SQL DATABASE

### 2.1 Create Cloud SQL PostgreSQL Instance
```bash
gcloud sql instances create samana-postgres \
  --database-version=POSTGRES_15 \
  --tier=db-f1-micro \
  --region=us-central1 \
  --allocated-ip-range-name=google-managed-services-vpc \
  --network=default
```

### 2.2 Wait for Instance Creation
```bash
# Check status
gcloud sql instances describe samana-postgres

# Should show: status: RUNNABLE
```

### 2.3 Create Database
```bash
gcloud sql databases create samana_db \
  --instance=samana-postgres
```

### 2.4 Create Database User
```bash
gcloud sql users create samana_user \
  --instance=samana-postgres \
  --password=CHANGE_ME_STRONG_PASSWORD
```

### 2.5 Get Connection Details
```bash
# Get instance IP
gcloud sql instances describe samana-postgres --format="value(ipAddresses[0].ipAddress)"

# Get connection name
gcloud sql instances describe samana-postgres --format="value(connectionName)"
```

### 2.6 Update Application Properties for Cloud
```bash
cd /Users/sudhir_ahirkar/Applications/samana/samana/backend/samana-backend
cat > src/main/resources/application-cloud.properties << 'EOF'
spring.application.name=samana-backend
spring.profiles.active=cloud

# Cloud SQL PostgreSQL via Cloud SQL Proxy
spring.datasource.url=jdbc:postgresql://cloud-sql-proxy/samana_db
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=org.postgresql.Driver

# Use Cloud SQL socket for Cloud Run
spring.cloud.gcp.sql.database-name=samana_db

spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=false

server.port=${PORT:8080}

# JWT
jwt.secret=${JWT_SECRET}

# Logging
logging.level.root=INFO
logging.level.com.samana=DEBUG
EOF

cat src/main/resources/application-cloud.properties
```

---

## ☁️ STEP 3: DEPLOY BACKEND TO CLOUD RUN

### 3.1 Create Cloud Run Service
```bash
# Build and deploy directly
gcloud run deploy samana-backend \
  --source=/Users/sudhir_ahirkar/Applications/samana/samana/backend/samana-backend \
  --platform=managed \
  --region=us-central1 \
  --allow-unauthenticated \
  --memory=512Mi \
  --cpu=1 \
  --timeout=300s \
  --set-env-vars="SPRING_PROFILES_ACTIVE=cloud,JWT_SECRET=your-jwt-secret-key" \
  --set-secrets="DB_PASSWORD=samana-db-password:latest" \
  --build-config-file=Dockerfile
```

### 3.2 Add Database Credentials
```bash
# Store password in Secret Manager
echo -n "CHANGE_ME_STRONG_PASSWORD" | gcloud secrets create samana-db-password --data-file=-

# Update Cloud Run service
gcloud run services update samana-backend \
  --region=us-central1 \
  --set-env-vars="DB_USERNAME=samana_user"
```

### 3.3 Get Backend Service URL
```bash
# Get Cloud Run URL
gcloud run services describe samana-backend \
  --region=us-central1 \
  --format="value(status.url)"

# Example output: https://samana-backend-abc123.a.run.app
```

### 3.4 Test Cloud Run Backend
```bash
# Get service URL
BACKEND_URL=$(gcloud run services describe samana-backend \
  --region=us-central1 \
  --format="value(status.url)")

echo "Testing backend at: $BACKEND_URL"

# Test endpoint
curl $BACKEND_URL/api/experiences
curl $BACKEND_URL/api/calendar/festival-states
curl $BACKEND_URL/api/experiences/filter-options/states
```

---

## 📱 STEP 4: DEPLOY FRONTEND TO CLOUD STORAGE + CDN

### 4.1 Create Cloud Storage Bucket
```bash
# Create bucket for frontend
gsutil mb -l us-central1 gs://samana-frontend

# Make it public
gsutil iam ch allUsers:objectViewer gs://samana-frontend
```

### 4.2 Build Frontend (if needed)
```bash
cd /Users/sudhir_ahirkar/Applications/samana/samana/frontend

# For static HTML sites (no build needed)
# Files are already ready to upload

# Update API_BASE_URL in all HTML files
# Replace: http://localhost:8080 with actual Cloud Run URL
```

### 4.3 Upload Frontend Files
```bash
# Get backend URL
BACKEND_URL=$(gcloud run services describe samana-backend \
  --region=us-central1 \
  --format="value(status.url)")

# Update API endpoints in HTML files
find . -name "*.html" -type f -exec sed -i "s|http://localhost:8080|$BACKEND_URL|g" {} \;

# Upload all files
gsutil -m cp -r . gs://samana-frontend/

# Make index.html the default
gsutil web set -m index.html -e index.html gs://samana-frontend
```

### 4.4 Get Frontend URL
```bash
# Frontend URL
echo "http://samana-frontend.storage.googleapis.com"

# Or use Cloud CDN:
# Create a load balancer pointing to the bucket (optional for better performance)
```

---

## 🔗 STEP 5: CONFIGURE NETWORKING & CORS

### 5.1 Update CORS in Backend
```bash
# Backend automatically has CORS configured for Cloud Run
# In ExperienceSearchController, ExperiencePackageController, FestivalCalendarController:
# @CrossOrigin(origins = {"http://localhost:3000", "https://samana-frontend.storage.googleapis.com"})

# Deploy again with CORS settings
gcloud run deploy samana-backend \
  --region=us-central1 \
  --update-env-vars="ALLOWED_ORIGINS=https://samana-frontend.storage.googleapis.com"
```

### 5.2 Test CORS
```bash
# Test from frontend
curl -H "Origin: https://samana-frontend.storage.googleapis.com" \
  -H "Access-Control-Request-Method: GET" \
  -H "Access-Control-Request-Headers: content-type" \
  $BACKEND_URL/api/experiences \
  -v
```

---

## ✅ STEP 6: VALIDATION & TESTING

### 6.1 Backend Endpoint Testing
```bash
BACKEND_URL=$(gcloud run services describe samana-backend \
  --region=us-central1 \
  --format="value(status.url)")

# Test all critical endpoints
echo "Testing Search & Filters..."
curl $BACKEND_URL/api/experiences/filter-options/states
curl "$BACKEND_URL/api/experiences/search?title=Holi"

echo "Testing Packages..."
curl $BACKEND_URL/api/experiences/1/packages

echo "Testing Calendar..."
curl $BACKEND_URL/api/calendar/festival-states
curl $BACKEND_URL/api/calendar/festivals/month/3
```

### 6.2 Frontend Loading
```bash
FRONTEND_URL="https://samana-frontend.storage.googleapis.com"

# Test in browser
# $FRONTEND_URL/index.html
# $FRONTEND_URL/explore.html
# $FRONTEND_URL/experience.html
# $FRONTEND_URL/calendar.html
```

### 6.3 API Integration Test
```bash
# From frontend, verify API calls work
# Check browser Network tab for:
# - 200 status codes
# - Proper CORS headers
# - Data returned correctly
```

### 6.4 Database Connectivity
```bash
# Connect to Cloud SQL to verify
gcloud sql connect samana-postgres \
  --user=samana_user

# Inside psql:
\dt  # List tables (should show created tables)
SELECT COUNT(*) FROM experiences;
\q
```

---

## 🔐 STEP 7: SECURITY & SSL

### 7.1 Setup Custom Domain (Optional)
```bash
# Map custom domain to Cloud Run
gcloud run domain-mappings create \
  --domain=api.samana.com \
  --service=samana-backend \
  --region=us-central1
```

### 7.2 Configure SSL Certificate
```bash
# Cloud Run automatically handles SSL for *.run.app URLs
# For custom domains:
# gcloud compute ssl-certificates create samana-cert \
#   --domains=api.samana.com
```

### 7.3 Update Firewall Rules
```bash
# Allow Cloud Run to access Cloud SQL
gcloud compute instances list

# Firewall rules are auto-managed by Cloud Run + Cloud SQL
```

---

## 📊 STEP 8: MONITORING & LOGGING

### 8.1 View Logs
```bash
# Backend logs
gcloud run logs read samana-backend --region=us-central1

# Continuous logging
gcloud run logs read samana-backend --region=us-central1 --follow
```

### 8.2 Setup Cloud Monitoring
```bash
# View metrics in Cloud Console
# Go to: Cloud Run > samana-backend > Metrics

# Key metrics to monitor:
# - Request count
# - Request latency
# - Error rate
# - Memory usage
# - CPU usage
```

### 8.3 Setup Alerts
```bash
# Configure alerts for:
# - High error rate (> 1%)
# - High latency (> 5s)
# - Service unavailability
# Via: Cloud Monitoring > Alerting > Create Policy
```

---

## 📈 STEP 9: PERFORMANCE OPTIMIZATION

### 9.1 Enable Cloud CDN
```bash
# Already enabled for Cloud Storage

# For backend (if load balancer created):
gcloud compute backend-services update samana-backend \
  --enable-cdn \
  --cache-mode=CACHE_ALL_STATIC
```

### 9.2 Auto-scaling
```bash
# Cloud Run auto-scales automatically
# Configure in Cloud Run settings:
# - Min instances: 1
# - Max instances: 100
# - CPU throttling: enabled
```

### 9.3 Database Optimization
```bash
# Cloud SQL auto-backups enabled
# Check performance insights in Cloud SQL console
```

---

## 🧪 STEP 10: END-TO-END TESTING

### Complete User Flow
```
1. Open https://samana-frontend.storage.googleapis.com
2. Search for experiences
3. Apply filters (state, price, duration)
4. View experience details
5. Check available packages
6. View festival calendar
7. Verify all API calls succeed
8. Check browser console for errors
```

### Test Scenarios
- [ ] Search returns results
- [ ] Filters reduce results correctly
- [ ] Packages display with pricing
- [ ] Calendar navigates between months
- [ ] Festival availability shown
- [ ] Click to book works
- [ ] All pages load < 2 seconds
- [ ] Mobile responsive
- [ ] No CORS errors
- [ ] No JavaScript errors

---

## 📋 PRE-LAUNCH CHECKLIST

### Backend
- [ ] Cloud Run service deployed
- [ ] Environment variables set
- [ ] Database connected and initialized
- [ ] All tables created
- [ ] 26+ endpoints tested
- [ ] Logs showing no errors
- [ ] Performance acceptable

### Frontend
- [ ] Files uploaded to Cloud Storage
- [ ] Accessible via public URL
- [ ] API_BASE_URL updated to Cloud Run URL
- [ ] All pages loading correctly
- [ ] Responsive on mobile
- [ ] Console clean (no errors)

### Security
- [ ] Database user has limited permissions
- [ ] JWT secret configured
- [ ] CORS properly configured
- [ ] No sensitive data in logs
- [ ] SSL enabled
- [ ] IP whitelist if needed

### Performance
- [ ] Backend response time < 1s
- [ ] Frontend load time < 2s
- [ ] Database queries optimized
- [ ] CDN enabled
- [ ] Caching configured

### Monitoring
- [ ] Logs configured
- [ ] Alerts setup
- [ ] Metrics visible
- [ ] Backup automated
- [ ] Error tracking enabled

---

## 🚀 ROLLBACK PROCEDURE

### If Something Goes Wrong
```bash
# Stop current deployment
gcloud run services delete samana-backend --region=us-central1

# Rollback to previous version
gcloud run deploy samana-backend \
  --region=us-central1 \
  --image=us-central1-docker.pkg.dev/samana-project/samana-repo/backend:previous

# Restore database from backup
# Via: Cloud SQL > Backups > Restore
```

---

## 📝 COMMANDS REFERENCE

### Quick Deployment Commands
```bash
# View all services
gcloud run services list

# View service details
gcloud run services describe samana-backend --region=us-central1

# View logs
gcloud run logs read samana-backend --region=us-central1

# Update service
gcloud run deploy samana-backend --region=us-central1 --image=IMAGE_URL

# Delete service
gcloud run services delete samana-backend --region=us-central1

# View Cloud SQL instances
gcloud sql instances list

# Connect to database
gcloud sql connect samana-postgres --user=samana_user

# View Cloud Storage buckets
gsutil ls -b

# Upload files
gsutil -m cp -r ./frontend/* gs://samana-frontend/
```

---

## 📞 TROUBLESHOOTING

### Issue: Backend fails to deploy
```
Solution:
1. Check Docker image builds: docker build .
2. Verify environment variables are set
3. Check project permissions: gcloud auth list
4. View deployment logs: gcloud run deploy samana-backend --log-http-requests
```

### Issue: Database connection fails
```
Solution:
1. Verify Cloud SQL instance is running
2. Check network connectivity
3. Verify database credentials
4. Check Cloud SQL Proxy is configured
```

### Issue: CORS errors on frontend
```
Solution:
1. Add origin to @CrossOrigin in controllers
2. Redeploy backend
3. Check Access-Control headers in response
4. Verify frontend URL matches CORS configuration
```

### Issue: Frontend loads but no data
```
Solution:
1. Check browser Network tab for API calls
2. Verify API_BASE_URL in frontend code
3. Check backend logs for errors
4. Test API directly with curl
5. Check CORS configuration
```

---

## ✅ DEPLOYMENT COMPLETE CHECKLIST

- [ ] Backend deployed to Cloud Run
- [ ] Database initialized with tables
- [ ] Frontend uploaded to Cloud Storage
- [ ] API integration tested
- [ ] CORS configured
- [ ] Logging enabled
- [ ] Monitoring setup
- [ ] Performance validated
- [ ] Security review complete
- [ ] End-to-end flow tested

---

## 🎉 PRODUCTION READY

Once all checkpoints pass:
- ✅ Backend: Production (Cloud Run)
- ✅ Frontend: Production (Cloud Storage + CDN)
- ✅ Database: Production (Cloud SQL)
- ✅ Monitoring: Active
- ✅ Backups: Automated

---

**Deployment Status:** Ready for Launch  
**Next Step:** Post-Launch Monitoring & User Onboarding

---

**Last Updated:** October 9, 2026  
**Deployment Date:** [Your Date]  
**Status:** Ready for Production

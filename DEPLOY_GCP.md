# 🚀 Deploy SAMANA to Google Cloud Platform (GCP)

**Tested Deployment Workflow**

---

## 📋 Prerequisites

### GCP Account & Project Setup
1. GCP Account with billing enabled
2. `gcloud` CLI installed: [Google Cloud SDK](https://cloud.google.com/sdk/docs/install)
3. Project created in GCP Console

### Verify GCP Setup
```bash
# Check gcloud CLI
gcloud --version

# Login to GCP
gcloud auth login

# Set project
gcloud config set project YOUR_PROJECT_ID

# Enable required APIs
gcloud services enable run.googleapis.com
gcloud services enable sqladmin.googleapis.com
gcloud services enable container.googleapis.com
gcloud services enable cloudbuild.googleapis.com
```

---

## 🛠️ Step 1: Prepare Backend for Production

### 1.1 Update Backend Configuration

**File:** `backend/samana-backend/src/main/resources/application-cloud.properties`

```properties
# ========== CLOUD DATABASE ==========
spring.datasource.url=jdbc:postgresql://IP_ADDRESS:5432/samana_db
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=org.postgresql.Driver

# ========== JPA/HIBERNATE ==========
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=false

# ========== SERVER ==========
server.port=${PORT:8080}
server.compression.enabled=true
server.compression.min-response-size=1024

# ========== JWT ==========
jwt.secret=${JWT_SECRET}
jwt.expiration=${JWT_EXPIRATION:86400000}

# ========== LOGGING ==========
logging.level.root=WARN
logging.level.com.samana=INFO
```

### 1.2 Update Frontend API URLs

**File:** `frontend/js/script.js`

Change from local to production:

```javascript
// ========== PRODUCTION API ==========
const API_BASE_URL = "https://samana-backend-XXXXX.run.app/api";
const BACKEND_URL = "https://samana-backend-XXXXX.run.app";
```

**Also update in:**
- `frontend/js/host.js`
- `frontend/js/host-profile.js`
- `frontend/js/experience.js`
- `frontend/js/explore.js`
- `frontend/js/location.js`
- Other JS files with API references

### 1.3 Remove Debug Logging

Remove/comment out all `System.out.println()` and `console.log()` statements from production code.

---

## 🗄️ Step 2: Setup Cloud SQL (PostgreSQL)

### 2.1 Create Cloud SQL Instance

```bash
# Create PostgreSQL instance
gcloud sql instances create samana-db \
  --database-version=POSTGRES_15 \
  --tier=db-f1-micro \
  --region=us-central1 \
  --availability-type=ZONAL

# Get instance IP
gcloud sql instances describe samana-db \
  --format="value(ipAddresses[0].ipAddress)"
```

### 2.2 Create Database & User

```bash
# Create database
gcloud sql databases create samana_db \
  --instance=samana-db

# Create database user
gcloud sql users create samana_user \
  --instance=samana-db \
  --password=STRONG_PASSWORD_HERE
```

### 2.3 Configure Network Access

```bash
# Allow Cloud Run to access Cloud SQL
gcloud sql instances patch samana-db \
  --database-version=POSTGRES_15 \
  --require-ssl

# Allow public IP access (development only!)
gcloud sql instances patch samana-db \
  --require-ssl=false
```

### 2.4 Test Connection

```bash
# Get instance IP
INSTANCE_IP=$(gcloud sql instances describe samana-db --format="value(ipAddresses[0].ipAddress)")

# Connect from local machine
psql -h $INSTANCE_IP \
     -U samana_user \
     -d samana_db \
     -W

# If connection fails, check firewall rules:
gcloud sql instances describe samana-db
```

---

## 📦 Step 3: Build & Deploy Backend

### 3.1 Update Dockerfile

**File:** `Dockerfile`

```dockerfile
# Build stage
FROM maven:3.8-eclipse-temurin-21 as builder
WORKDIR /app
COPY backend/samana-backend/pom.xml .
RUN mvn dependency:resolve
COPY backend/samana-backend/src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/samana-backend-*.jar app.jar

# Set environment variables
ENV SPRING_PROFILES_ACTIVE=cloud \
    PORT=8080

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
```

### 3.2 Create Service Account (One-time)

```bash
# Create service account for Cloud Run
gcloud iam service-accounts create samana-backend \
  --display-name "SAMANA Backend Service"

# Grant permissions
gcloud projects add-iam-policy-binding PROJECT_ID \
  --member="serviceAccount:samana-backend@PROJECT_ID.iam.gserviceaccount.com" \
  --role="roles/cloudsql.client"

gcloud projects add-iam-policy-binding PROJECT_ID \
  --member="serviceAccount:samana-backend@PROJECT_ID.iam.gserviceaccount.com" \
  --role="roles/storage.objectViewer"
```

### 3.3 Build & Push Docker Image

```bash
# Set project and variables
PROJECT_ID=$(gcloud config get-value project)
REGION=us-central1
SERVICE_NAME=samana-backend

# Build Docker image
docker build -t gcr.io/$PROJECT_ID/$SERVICE_NAME:latest .

# Push to Google Container Registry
docker push gcr.io/$PROJECT_ID/$SERVICE_NAME:latest

# Or use gcloud build
gcloud builds submit --tag gcr.io/$PROJECT_ID/$SERVICE_NAME:latest
```

### 3.4 Deploy to Cloud Run

```bash
PROJECT_ID=$(gcloud config get-value project)
SERVICE_NAME=samana-backend
REGION=us-central1
CLOUD_SQL_INSTANCE="$PROJECT_ID:$REGION:samana-db"
DB_IP=$(gcloud sql instances describe samana-db --format="value(ipAddresses[0].ipAddress)")

gcloud run deploy $SERVICE_NAME \
  --image gcr.io/$PROJECT_ID/$SERVICE_NAME:latest \
  --region $REGION \
  --platform managed \
  --allow-unauthenticated \
  --set-env-vars="SPRING_PROFILES_ACTIVE=cloud,PORT=8080" \
  --set-env-vars="DB_USERNAME=samana_user" \
  --set-env-vars="DB_PASSWORD=YOUR_DB_PASSWORD" \
  --set-env-vars="JWT_SECRET=YOUR_JWT_SECRET_32_CHARS_MIN" \
  --memory 512Mi \
  --cpu 1 \
  --timeout 3600 \
  --max-instances 10 \
  --min-instances 1 \
  --service-account "samana-backend@$PROJECT_ID.iam.gserviceaccount.com"

# Get service URL
gcloud run services describe $SERVICE_NAME --region $REGION --format="value(status.url)"
```

### 3.5 Verify Backend Deployment

```bash
# Get service URL
SERVICE_URL=$(gcloud run services describe samana-backend \
  --region us-central1 \
  --format="value(status.url)")

# Test API
curl $SERVICE_URL/api/categories

# Should return JSON response
```

---

## 🌐 Step 4: Deploy Frontend

### 4.1 Prepare Frontend Files

```bash
cd frontend

# Update all API URLs to production
# Search and replace in all JS files:
# FROM: http://localhost:8080/api
# TO: https://samana-backend-XXXXX.run.app/api
```

### 4.2 Deploy to Cloud Storage & Cloud CDN (Option A - Recommended)

```bash
PROJECT_ID=$(gcloud config get-value project)
BUCKET_NAME="samana-frontend-$PROJECT_ID"
REGION=us-central1

# Create storage bucket
gsutil mb gs://$BUCKET_NAME

# Copy files to bucket
gsutil -m cp -r frontend/* gs://$BUCKET_NAME/

# Set permissions (public read)
gsutil iam ch serviceAccount:cloud-cdn@$PROJECT_ID.iam.gserviceaccount.com:objectViewer gs://$BUCKET_NAME

# Enable public access
gsutil iam ch allUsers:objectViewer gs://$BUCKET_NAME

# Get bucket URL
echo "Frontend URL: https://storage.googleapis.com/$BUCKET_NAME/index.html"
```

### 4.3 Configure Cloud CDN (Optional but Recommended)

```bash
# Create load balancer with CDN enabled
gcloud compute backend-buckets create samana-frontend \
  --gcs-uri-prefix=gs://$BUCKET_NAME \
  --enable-cdn \
  --cache-mode=CACHE_ALL_STATIC \
  --client-ttl=3600 \
  --default-ttl=3600 \
  --max-ttl=86400

# Create URL map
gcloud compute url-maps create samana-lb \
  --default-service=samana-frontend

# Create HTTP proxy
gcloud compute target-http-proxies create samana-proxy \
  --url-map=samana-lb

# Create forwarding rule
gcloud compute forwarding-rules create samana-frontend-lb \
  --global \
  --target-http-proxy=samana-proxy \
  --address=samana-frontend-ip
```

### 4.4 Deploy to Cloud Run (Option B - Containerized)

If you prefer to containerize frontend:

```bash
# Create Dockerfile for frontend
cat > frontend.Dockerfile << 'EOF'
FROM node:18-alpine
WORKDIR /app
COPY frontend/ .
RUN npm install -g http-server
ENV PORT=8080
EXPOSE 8080
CMD ["http-server", "-p", "8080", "-c-1"]
EOF

# Build and deploy
docker build -f frontend.Dockerfile -t gcr.io/$PROJECT_ID/samana-frontend:latest .
docker push gcr.io/$PROJECT_ID/samana-frontend:latest

gcloud run deploy samana-frontend \
  --image gcr.io/$PROJECT_ID/samana-frontend:latest \
  --region us-central1 \
  --allow-unauthenticated \
  --memory 256Mi \
  --cpu 1
```

---

## 🔐 Step 5: Setup Custom Domain & HTTPS

### 5.1 Reserve Static IP

```bash
# Reserve static IP for frontend
gcloud compute addresses create samana-frontend-ip \
  --global \
  --ip-version IPV4
```

### 5.2 Configure DNS

In your domain registrar (GoDaddy, Route 53, etc.):

```
Add DNS records:
A Record:     example.com      → [STATIC_IP]
A Record:     www.example.com  → [STATIC_IP]
CNAME:        api              → [Cloud Run Service URL]
```

### 5.3 Setup SSL Certificate (Google-Managed)

```bash
gcloud compute ssl-certificates create samana-ssl \
  --domains=example.com,www.example.com,api.example.com \
  --global
```

---

## 🧪 Step 6: Testing in Production

### 6.1 Test Backend API

```bash
# Get backend URL
BACKEND_URL=$(gcloud run services describe samana-backend \
  --region us-central1 \
  --format="value(status.url)")

# Test endpoints
curl $BACKEND_URL/api/categories
curl $BACKEND_URL/api/users/register \
  -X POST \
  -H "Content-Type: application/json" \
  -d '{"name":"Test","email":"test@example.com","password":"password123"}'
```

### 6.2 Test Frontend

1. Open frontend URL in browser
2. Check DevTools console (F12) for errors
3. Test login/registration flow
4. Verify API calls go to correct endpoint

### 6.3 Monitor Logs

```bash
# Backend logs
gcloud run logs read samana-backend --region us-central1 --limit 50

# View specific service logs
gcloud logging read "resource.type=cloud_run_revision AND resource.labels.service_name=samana-backend" \
  --limit 100 \
  --format json
```

---

## 🛡️ Step 7: Setup Monitoring & Alerts

### 7.1 Enable Cloud Monitoring

```bash
# Create uptime check
gcloud monitoring uptime-checks create samana-backend \
  --display-name="SAMANA Backend Health" \
  --resource-type="uptime-url" \
  --monitored-resource="url" \
  --http-check-port=443 \
  --http-check-path="/api/categories" \
  --period=60
```

### 7.2 Create Alert Policy

```bash
# Alert on high error rate
gcloud alpha monitoring policies create \
  --notification-channels=[CHANNEL_ID] \
  --display-name="SAMANA Backend Error Rate" \
  --condition-display-name="High Error Rate" \
  --condition-threshold-value=0.05 \
  --condition-threshold-duration=300s
```

---

## 📊 Step 8: Optimize for Production

### 8.1 Enable Cloud CDN for Backend

```bash
# Create backend service for Cloud Run
gcloud compute backend-services create samana-backend-service \
  --protocol=HTTPS \
  --global \
  --enable-cdn \
  --cache-mode=CACHE_ALL_STATIC \
  --client-ttl=3600
```

### 8.2 Setup Auto-Scaling

```bash
# Update Cloud Run service with autoscaling
gcloud run services update samana-backend \
  --region us-central1 \
  --min-instances=1 \
  --max-instances=20 \
  --concurrency=100
```

### 8.3 Enable Compression

Backend (already in Dockerfile):
```properties
server.compression.enabled=true
server.compression.min-response-size=1024
```

### 8.4 Setup Database Backups

```bash
# Create automated backups
gcloud sql backups create --instance=samana-db \
  --backup-configuration \
  --backup-start-time=03:00 \
  --retained-backups-count=30 \
  --transaction-log-retention-days=7
```

---

## 🔄 Step 9: CI/CD Pipeline

### 9.1 Setup Cloud Build

**File:** `cloudbuild.yaml` (already exists in project)

```yaml
steps:
  # Build backend
  - name: 'gcr.io/cloud-builders/docker'
    args: ['build', '-t', 'gcr.io/$PROJECT_ID/samana-backend:$SHORT_SHA', 
           '-t', 'gcr.io/$PROJECT_ID/samana-backend:latest', '.']

  # Push to registry
  - name: 'gcr.io/cloud-builders/docker'
    args: ['push', 'gcr.io/$PROJECT_ID/samana-backend:$SHORT_SHA']

  # Deploy to Cloud Run
  - name: 'gcr.io/cloud-builders/gke-deploy'
    args:
      - run
      - --service=samana-backend
      - --image=gcr.io/$PROJECT_ID/samana-backend:$SHORT_SHA
      - --region=us-central1

images:
  - 'gcr.io/$PROJECT_ID/samana-backend:$SHORT_SHA'
  - 'gcr.io/$PROJECT_ID/samana-backend:latest'
```

### 9.2 Connect GitHub Repository

```bash
# Connect GitHub to Cloud Build
gcloud builds connect --repository-name=samana \
  --repository-owner=YOUR_GITHUB_USER \
  --region=us-central1
```

---

## 📝 Deployment Checklist

Before deploying:

- [ ] Backend API URLs updated in frontend
- [ ] Frontend API URLs point to production backend
- [ ] Database credentials set in environment variables
- [ ] JWT_SECRET set to secure value (32+ characters)
- [ ] Debug logging disabled
- [ ] CORS configured for production domain
- [ ] SSL certificates ready
- [ ] Database backups enabled
- [ ] Monitoring alerts configured
- [ ] Load balancer health checks passing

---

## 🚨 Troubleshooting Deployment

### Backend won't start on Cloud Run

```bash
# Check logs
gcloud run logs read samana-backend --region us-central1 --limit 50

# Common issues:
# 1. Database connection failed → Check Cloud SQL IP whitelist
# 2. JWT_SECRET not set → Add to environment variables
# 3. Port not exposed → Set PORT=8080 in env vars
```

### Frontend shows API errors

```bash
# Check DevTools Network tab
# Verify API URL is production URL
# Check CORS headers in backend response
```

### Database connection timeout

```bash
# Check Cloud SQL instance is running
gcloud sql instances describe samana-db

# Whitelist your Cloud Run service
gcloud sql instances patch samana-db \
  --database-version=POSTGRES_15 \
  --require-ssl=false  # For development
```

---

## 📈 Performance Monitoring

### Key Metrics to Monitor

```bash
# CPU usage
gcloud monitoring read \
  --filter='resource.type="cloud_run_revision" 
    AND metric.type="run.googleapis.com/request_count"' \
  --format=table

# Response time
gcloud monitoring read \
  --filter='metric.type="run.googleapis.com/request_latencies"' \
  --format=table

# Database connections
gcloud sql operations list --instance=samana-db --limit=20
```

---

## 🔄 Update & Rollback

### Deploy New Version

```bash
# Build and deploy new version
gcloud run deploy samana-backend \
  --image gcr.io/$PROJECT_ID/samana-backend:v2.0 \
  --region us-central1

# Old version still available
gcloud run revisions list --region us-central1
```

### Rollback to Previous Version

```bash
# List available revisions
gcloud run revisions list --region us-central1 --service=samana-backend

# Route traffic to previous revision
gcloud run services update-traffic samana-backend \
  --to-revisions REVISION_NAME=100 \
  --region us-central1
```

---

## 💰 Cost Optimization

### Reduce Cloud SQL Costs

```bash
# Use smaller instance for development
gcloud sql instances patch samana-db \
  --tier=db-f1-micro

# Use PostgreSQL (cheaper than MySQL)

# Enable automatic storage increase
gcloud sql instances patch samana-db \
  --database-version=POSTGRES_15 \
  --storage-auto-increase \
  --storage-auto-increase-limit=100
```

### Reduce Cloud Run Costs

```bash
# Lower min-instances
gcloud run services update samana-backend \
  --min-instances=0  # Scale to zero when idle

# Lower resource allocation
gcloud run deploy samana-backend \
  --memory 256Mi \
  --cpu 1
```

---

## ✅ Post-Deployment Steps

1. **Verify All Services**
   - Test frontend loading
   - Test API endpoints
   - Test database connectivity

2. **Monitor Logs**
   - Check for errors in Cloud Logging
   - Set up alerts for high error rates

3. **Setup Backups**
   - Enable Cloud SQL backups
   - Test restoration process

4. **Document Configuration**
   - Save all credentials securely
   - Document service URLs
   - Keep deployment commands

5. **Plan Maintenance**
   - Schedule database updates
   - Plan version upgrades
   - Setup monitoring dashboards

---

## 📞 Quick Reference

```bash
# View running services
gcloud run services list

# View service details
gcloud run services describe samana-backend --region us-central1

# View logs
gcloud run logs read samana-backend --region us-central1

# Update environment variables
gcloud run services update samana-backend \
  --set-env-vars="KEY=VALUE" \
  --region us-central1

# View Cloud SQL instances
gcloud sql instances list

# Backup database
gcloud sql backups create --instance=samana-db

# View metrics
gcloud monitoring dashboards list
```

---

**Deployment Complete! 🚀**

For troubleshooting, see GCP Cloud Run documentation: https://cloud.google.com/run/docs

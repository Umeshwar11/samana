#!/bin/bash

# GCP Deployment Script for SAMANA Application

# Configuration
GCP_PROJECT_ID="project-c3260d5b-68c2-4c8f-b0d"
GCP_REGION="asia-south1"
IMAGE_NAME="samana-backend"
IMAGE_TAG="latest"
SERVICE_NAME="samana-backend"
DB_INSTANCE="samana-postgres"
DB_NAME="samana_db"
DB_USER="samana_user"

echo "================================"
echo "SAMANA - GCP Cloud Run Deployment"
echo "================================"
echo ""
echo "Configuration:"
echo "  Project ID: $GCP_PROJECT_ID"
echo "  Region: $GCP_REGION"
echo "  Service: $SERVICE_NAME"
echo ""

# Step 1: Set GCP Project
echo "[1/8] Setting GCP Project..."
gcloud config set project $GCP_PROJECT_ID

# Step 2: Create Cloud SQL Instance (if not exists)
echo "[2/8] Checking Cloud SQL instance..."
if ! gcloud sql instances describe $DB_INSTANCE > /dev/null 2>&1; then
  echo "Creating Cloud SQL PostgreSQL instance..."
  gcloud sql instances create $DB_INSTANCE \
    --database-version=POSTGRES_15 \
    --tier=db-f1-micro \
    --region=$GCP_REGION \
    --backup \
    --enable-public-ip \
    --authorized-networks=0.0.0.0/0
else
  echo "Cloud SQL instance already exists"
  # Ensure public IP is enabled
  gcloud sql instances patch $DB_INSTANCE --enable-public-ip 2>/dev/null || true
fi

# Step 3: Create Database
echo "[3/8] Creating database..."
gcloud sql databases create $DB_NAME \
  --instance=$DB_INSTANCE \
  --charset=UTF8 2>/dev/null || echo "Database already exists"

# Step 4: Create Database User
echo "[4/8] Creating database user..."
gcloud sql users create $DB_USER \
  --instance=$DB_INSTANCE \
  --password=SamanaDB@2024Secure 2>/dev/null || echo "User already exists"

# Step 5: Create Secrets
echo "[5/8] Setting up secrets..."
echo -n "samana-development-secret-key-2024-secure-enough-for-production-123456789" | \
  gcloud secrets create jwt-secret --data-file=- --replication-policy="automatic" 2>/dev/null || \
  echo "JWT secret already exists"

echo -n "SamanaDB@2024Secure" | \
  gcloud secrets create db-password --data-file=- --replication-policy="automatic" 2>/dev/null || \
  echo "DB password secret already exists"

# Grant Cloud Run service account access to secrets
PROJECT_NUMBER=$(gcloud projects describe $GCP_PROJECT_ID --format='value(projectNumber)')
SERVICE_ACCOUNT="$PROJECT_NUMBER-compute@developer.gserviceaccount.com"

gcloud secrets add-iam-policy-binding jwt-secret \
  --member=serviceAccount:$SERVICE_ACCOUNT \
  --role=roles/secretmanager.secretAccessor 2>/dev/null

gcloud secrets add-iam-policy-binding db-password \
  --member=serviceAccount:$SERVICE_ACCOUNT \
  --role=roles/secretmanager.secretAccessor 2>/dev/null

# Step 6: Configure Docker
echo "[6/8] Configuring Docker for GCP..."
gcloud auth configure-docker gcr.io

# Step 7: Build and Push Docker Image
echo "[7/8] Building and pushing Docker image..."
docker build --platform linux/amd64 -t gcr.io/$GCP_PROJECT_ID/$IMAGE_NAME:$IMAGE_TAG .
docker push gcr.io/$GCP_PROJECT_ID/$IMAGE_NAME:$IMAGE_TAG

# Step 8: Deploy to Cloud Run
echo "[8/8] Deploying to Cloud Run..."
CLOUD_SQL_CONNECTION="$GCP_PROJECT_ID:$GCP_REGION:$DB_INSTANCE"

gcloud run deploy $SERVICE_NAME \
  --image=gcr.io/$GCP_PROJECT_ID/$IMAGE_NAME:$IMAGE_TAG \
  --platform=managed \
  --region=$GCP_REGION \
  --memory=512Mi \
  --cpu=1 \
  --timeout=300 \
  --max-instances=100 \
  --port=8080 \
  --allow-unauthenticated \
  --set-env-vars="SPRING_PROFILES_ACTIVE=cloud,DB_DRIVER=org.postgresql.Driver" \
  --set-secrets="JWT_SECRET=jwt-secret:latest,DB_PASSWORD=db-password:latest" \
  --add-cloudsql-instances=$CLOUD_SQL_CONNECTION

echo ""
echo "================================"
echo "Deployment Complete!"
echo "================================"
echo ""

# Get Service URL
SERVICE_URL=$(gcloud run services describe $SERVICE_NAME --region=$GCP_REGION --format='value(status.url)')
echo "Service URL: $SERVICE_URL"
echo ""
echo "Test the API:"
echo "  curl $SERVICE_URL/api/categories"
echo ""
echo "View Logs:"
echo "  gcloud run services logs read $SERVICE_NAME --region=$GCP_REGION --limit=50"
echo ""

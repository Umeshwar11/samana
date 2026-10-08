#!/bin/bash

# GCP Deployment Script for SAMANA Application with Neon PostgreSQL

# Configuration
GCP_PROJECT_ID="project-c3260d5b-68c2-4c8f-b0d"
GCP_REGION="asia-south1"
BACKEND_IMAGE_NAME="samana-backend"
FRONTEND_IMAGE_NAME="samana-frontend"
IMAGE_TAG="latest"
BACKEND_SERVICE_NAME="samana-backend"
FRONTEND_SERVICE_NAME="samana-frontend"
NEON_DB_URL="${NEON_DATABASE_URL}"
JWT_SECRET="samana-development-secret-key-2024-secure-enough-for-production-123456789"

echo "================================"
echo "SAMANA - GCP Cloud Run Deployment"
echo "================================"
echo ""
echo "Configuration:"
echo "  Project ID: $GCP_PROJECT_ID"
echo "  Region: $GCP_REGION"
echo "  Backend Service: $BACKEND_SERVICE_NAME"
echo "  Frontend Service: $FRONTEND_SERVICE_NAME"
echo ""

# Step 1: Set GCP Project
echo "[1/6] Setting GCP Project..."
gcloud config set project $GCP_PROJECT_ID

# Step 2: Configure Docker
echo "[2/6] Configuring Docker for GCP..."
gcloud auth configure-docker gcr.io

# Step 3: Build and Push Docker Images
echo "[3/7] Building and pushing Docker images..."
if ! docker info > /dev/null 2>&1; then
  echo "ERROR: Docker daemon is not running. Please start Docker and try again."
  exit 1
fi

echo "  Building backend image..."
docker build --platform linux/amd64 -t gcr.io/$GCP_PROJECT_ID/$BACKEND_IMAGE_NAME:$IMAGE_TAG backend/
docker push gcr.io/$GCP_PROJECT_ID/$BACKEND_IMAGE_NAME:$IMAGE_TAG

echo "  Building frontend image..."
docker build --platform linux/amd64 -t gcr.io/$GCP_PROJECT_ID/$FRONTEND_IMAGE_NAME:$IMAGE_TAG frontend/
docker push gcr.io/$GCP_PROJECT_ID/$FRONTEND_IMAGE_NAME:$IMAGE_TAG

# Step 4: Create/Update Secrets
echo "[4/7] Setting up secrets..."
echo -n "$JWT_SECRET" | \
  gcloud secrets create jwt-secret --data-file=- --replication-policy="automatic" 2>/dev/null || \
  gcloud secrets versions add jwt-secret --data-file=- <<< "$JWT_SECRET" 2>/dev/null || \
  echo "JWT secret configured"

# Grant Cloud Run service account access to secrets
PROJECT_NUMBER=$(gcloud projects describe $GCP_PROJECT_ID --format='value(projectNumber)')
SERVICE_ACCOUNT="$PROJECT_NUMBER-compute@developer.gserviceaccount.com"

gcloud secrets add-iam-policy-binding jwt-secret \
  --member=serviceAccount:$SERVICE_ACCOUNT \
  --role=roles/secretmanager.secretAccessor 2>/dev/null || true

# Step 5: Deploy Backend to Cloud Run
echo "[5/7] Deploying backend to Cloud Run..."

gcloud run deploy $BACKEND_SERVICE_NAME \
  --image=gcr.io/$GCP_PROJECT_ID/$BACKEND_IMAGE_NAME:$IMAGE_TAG \
  --platform=managed \
  --region=$GCP_REGION \
  --memory=512Mi \
  --cpu=1 \
  --timeout=300 \
  --max-instances=100 \
  --port=8080 \
  --allow-unauthenticated \
  --set-env-vars="SPRING_PROFILES_ACTIVE=cloud,DATABASE_URL=${NEON_DATABASE_URL}" \
  --set-secrets="JWT_SECRET=jwt-secret:latest"

# Step 6: Deploy Frontend to Cloud Run
echo "[6/7] Deploying frontend to Cloud Run..."

gcloud run deploy $FRONTEND_SERVICE_NAME \
  --image=gcr.io/$GCP_PROJECT_ID/$FRONTEND_IMAGE_NAME:$IMAGE_TAG \
  --platform=managed \
  --region=$GCP_REGION \
  --memory=256Mi \
  --cpu=1 \
  --timeout=60 \
  --concurrency=10 \
  --max-instances=50 \
  --port=8080 \
  --allow-unauthenticated

# Step 7: Get Service URLs
echo "[7/7] Retrieving service details..."
BACKEND_URL=$(gcloud run services describe $BACKEND_SERVICE_NAME --region=$GCP_REGION --format='value(status.url)')
FRONTEND_URL=$(gcloud run services describe $FRONTEND_SERVICE_NAME --region=$GCP_REGION --format='value(status.url)')

echo ""
echo "================================"
echo "Deployment Complete!"
echo "================================"
echo ""
echo "Backend URL: $BACKEND_URL"
echo "Frontend URL: $FRONTEND_URL"
echo ""
echo "Test the API:"
echo "  curl $BACKEND_URL/api/categories"
echo ""
echo "View Logs:"
echo "  Backend:  gcloud run services logs read $BACKEND_SERVICE_NAME --region=$GCP_REGION --limit=50"
echo "  Frontend: gcloud run services logs read $FRONTEND_SERVICE_NAME --region=$GCP_REGION --limit=50"
echo ""

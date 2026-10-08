#!/bin/bash

# GCP Deployment Script for SAMANA Application with Neon PostgreSQL

# Configuration
GCP_PROJECT_ID="project-c3260d5b-68c2-4c8f-b0d"
GCP_REGION="asia-south1"
IMAGE_NAME="samana-backend"
IMAGE_TAG="latest"
SERVICE_NAME="samana-backend"
NEON_DB_URL="${NEON_DATABASE_URL}"
JWT_SECRET="samana-development-secret-key-2024-secure-enough-for-production-123456789"

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
echo "[1/6] Setting GCP Project..."
gcloud config set project $GCP_PROJECT_ID

# Step 2: Configure Docker
echo "[2/6] Configuring Docker for GCP..."
gcloud auth configure-docker gcr.io

# Step 3: Build and Push Docker Image
echo "[3/6] Building and pushing Docker image..."
docker build --platform linux/amd64 -t gcr.io/$GCP_PROJECT_ID/$IMAGE_NAME:$IMAGE_TAG .
docker push gcr.io/$GCP_PROJECT_ID/$IMAGE_NAME:$IMAGE_TAG

# Step 4: Create/Update Secrets
echo "[4/6] Setting up secrets..."
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

# Step 5: Deploy to Cloud Run
echo "[5/6] Deploying to Cloud Run..."

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
  --set-env-vars="SPRING_PROFILES_ACTIVE=cloud" \
  --set-secrets="JWT_SECRET=jwt-secret:latest" \
  --update-secrets="DATABASE_URL=${NEON_DATABASE_URL}"

# Step 6: Get Service URL
echo "[6/6] Retrieving service details..."
SERVICE_URL=$(gcloud run services describe $SERVICE_NAME --region=$GCP_REGION --format='value(status.url)')

echo ""
echo "================================"
echo "Deployment Complete!"
echo "================================"
echo ""
echo "Service URL: $SERVICE_URL"
echo ""
echo "Test the API:"
echo "  curl $SERVICE_URL/api/categories"
echo ""
echo "View Logs:"
echo "  gcloud run services logs read $SERVICE_NAME --region=$GCP_REGION --limit=50"
echo ""

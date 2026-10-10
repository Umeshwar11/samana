# SAMANA - Cultural Experience Platform

A comprehensive platform connecting travelers with authentic cultural experiences hosted by local communities across India. Discover traditions, festivals, food, crafts, and unforgettable cultural moments.

## 🌍 Live Deployment

- **Frontend**: https://samana-frontend-82716904797.asia-south1.run.app
- **Backend API**: https://samana-backend-hmuq4onkta-el.a.run.app
- **API Docs**: https://samana-backend-hmuq4onkta-el.a.run.app/api/categories

---

## 📋 Table of Contents

1. [Project Structure](#project-structure)
2. [Prerequisites](#prerequisites)
3. [Local Development Setup](#local-development-setup)
4. [GCP Cloud Deployment](#gcp-cloud-deployment)
5. [Environment Configuration](#environment-configuration)
6. [Testing](#testing)
7. [Troubleshooting](#troubleshooting)

---

## 📁 Project Structure

```
samana/
├── backend/
│   └── samana-backend/          # Spring Boot backend application
│       ├── src/
│       │   ├── main/java/com/samana/
│       │   │   ├── controller/  # REST API endpoints
│       │   │   ├── service/     # Business logic
│       │   │   ├── repository/  # JPA repositories
│       │   │   ├── model/       # Entity models
│       │   │   ├── config/      # Spring configuration (CORS, Security, etc.)
│       │   │   └── jwt/         # JWT authentication
│       │   └── resources/
│       │       ├── application.properties
│       │       ├── application-neon.properties    # Neon PostgreSQL config
│       │       ├── application-postgres.properties # Local PostgreSQL config
│       │       └── application-cloud.properties    # GCP config
│       └── pom.xml
├── frontend/
│   ├── index.html
│   ├── css/
│   │   ├── style.css
│   │   └── responsive.css
│   ├── js/
│   │   ├── script.js            # Main app logic
│   │   ├── explore.js           # Experiences page
│   │   ├── host.js              # Host management
│   │   └── toast.js             # Notifications
│   ├── images/
│   ├── Dockerfile               # Frontend container image
│   └── nginx.conf               # Nginx configuration with CORS
├── Dockerfile                   # Backend container image (Maven build)
├── deploy.sh                    # GCP deployment script
├── start-all.sh                 # Local development startup
├── stop-local.sh                # Local development shutdown
└── README.md                    # This file
```

---

## 🔧 Prerequisites

### For Local Development
- **Java 21** (JDK)
- **Maven 3.9+**
- **Node.js** (optional, for frontend development)
- **PostgreSQL 15+** (for local database)
- **Docker** (optional, for containerized local setup)
- **Git**

### For GCP Deployment
- **Google Cloud Account** with active project
- **GCP APIs Enabled**:
  - Cloud Run
  - Artifact Registry
  - Cloud SQL (optional)
- **gcloud CLI** installed and configured
- **Docker** installed and running
- **Neon PostgreSQL Account** (free tier available at https://neon.tech)

---

## 🏠 Local Development Setup

### Step 1: Clone the Repository

```bash
git clone https://github.com/sudhir-ahirkar/samana.git
cd samana
```

### Step 2: Set Up PostgreSQL Database

#### Option A: Using Neon PostgreSQL (Cloud-based, Recommended)

1. **Create a Neon Account**
   - Go to https://neon.tech
   - Sign up with GitHub or email
   - Create a new project

2. **Get Connection String**
   - In Neon dashboard, click "Connection String"
   - Copy the full connection string (format: `postgresql://user:password@host/database?sslmode=require`)

3. **Set Environment Variable**
   ```bash
   export NEON_DATABASE_URL="postgresql://your_user:your_password@ep-xyz.us-east-2.aws.neon.tech/samana?sslmode=require"
   export DB_USERNAME="your_user"
   export DB_PASSWORD="your_password"
   ```

#### Option B: Using Local PostgreSQL

1. **Install PostgreSQL**
   ```bash
   # macOS with Homebrew
   brew install postgresql@15
   brew services start postgresql@15

   # Ubuntu/Debian
   sudo apt-get install postgresql postgresql-contrib
   sudo systemctl start postgresql
   ```

2. **Create Database and User**
   ```bash
   psql -U postgres

   # In psql shell:
   CREATE DATABASE samana_db;
   CREATE USER samana_user WITH PASSWORD 'samana_password';
   ALTER ROLE samana_user SET client_encoding TO 'utf8';
   GRANT ALL PRIVILEGES ON DATABASE samana_db TO samana_user;
   \q
   ```

3. **Set Environment Variables**
   ```bash
   export DB_USERNAME=samana_user
   export DB_PASSWORD=samana_password
   export SPRING_PROFILES_ACTIVE=postgres
   ```

### Step 3: Set Up Backend

```bash
cd backend/samana-backend

# Set required environment variables
export SPRING_PROFILES_ACTIVE=neon  # or 'postgres' for local DB
export JWT_SECRET="your-secret-key-min-32-chars-long"
export PORT=8080

# Build the application
mvn clean package -DskipTests

# Run the application
java -jar target/samana-backend-0.0.1-SNAPSHOT.jar
```

**Backend will start at**: http://localhost:8080

### Step 4: Set Up Frontend

```bash
# Navigate to frontend directory
cd frontend

# Start a simple HTTP server
python3 -m http.server 3000

# Or use Node.js if installed
npx http-server -p 3000
```

**Frontend will be available at**: http://localhost:3000

### Step 5: Quick Start (All-in-One)

Use the provided script to start everything:

```bash
cd samana
./start-all.sh
```

This will:
- ✅ Check if services are already running
- ✅ Start backend on port 8080
- ✅ Start frontend on port 3000
- ✅ Verify both services are responding

**Verify Local Setup**:
```bash
# Test Backend API
curl http://localhost:8080/api/categories

# Test Frontend
open http://localhost:3000
```

---

## ☁️ GCP Cloud Deployment

### Prerequisites

1. **GCP Project Created**
2. **gcloud CLI Installed and Authenticated**
   ```bash
   gcloud auth login
   gcloud config set project YOUR_PROJECT_ID
   ```

3. **Required APIs Enabled**
   ```bash
   gcloud services enable run.googleapis.com
   gcloud services enable container.googleapis.com
   gcloud services enable artifactregistry.googleapis.com
   ```

4. **Neon PostgreSQL URL** (see local setup section)

### Step 1: Configure Deployment Script

Edit `deploy.sh` and update:

```bash
GCP_PROJECT_ID="your-project-id"
GCP_REGION="asia-south1"  # or your preferred region
BACKEND_SERVICE_NAME="samana-backend"
FRONTEND_SERVICE_NAME="samana-frontend"
JWT_SECRET="your-32-character-secret-key"
```

### Step 2: Deploy to GCP

```bash
# Set Neon database URL
export NEON_DATABASE_URL="postgresql://user:password@host/database?sslmode=require"

# Run deployment script
cd samana
./deploy.sh
```

The script will:
1. ✅ Set GCP project
2. ✅ Configure Docker authentication
3. ✅ Build backend Docker image
4. ✅ Build frontend Docker image
5. ✅ Push images to Google Artifact Registry
6. ✅ Deploy backend to Cloud Run
7. ✅ Deploy frontend to Cloud Run
8. ✅ Configure environment variables and secrets

### Step 3: Verify GCP Deployment

```bash
# Check backend service
gcloud run services describe samana-backend --region=asia-south1

# Check frontend service
gcloud run services describe samana-frontend --region=asia-south1

# Get service URLs
gcloud run services describe samana-backend --region=asia-south1 --format='value(status.url)'
gcloud run services describe samana-frontend --region=asia-south1 --format='value(status.url)'
```

### Step 4: View Logs

```bash
# Backend logs
gcloud run services logs read samana-backend --region=asia-south1 --limit=50

# Frontend logs
gcloud run services logs read samana-frontend --region=asia-south1 --limit=50

# Stream logs in real-time
gcloud run services logs read samana-backend --region=asia-south1 --follow
```

---

## 🔐 Environment Configuration

### Local Development (.env)

```bash
# Database Configuration
SPRING_PROFILES_ACTIVE=neon  # or 'postgres' for local
DB_USERNAME=samana_user
DB_PASSWORD=samana_password
NEON_DATABASE_URL=postgresql://user:password@host/database

# Server Configuration
PORT=8080
JWT_SECRET=your-secret-key-min-32-chars-long

# Frontend
FRONTEND_URL=http://localhost:3000
```

### GCP Cloud Configuration

Set via `deploy.sh`:

```bash
# Backend Environment Variables
SPRING_PROFILES_ACTIVE=neon
NEON_DATABASE_URL=${NEON_DATABASE_URL}
JWT_SECRET=your-secret-key-min-32-chars-long
PORT=8080

# CORS Configuration (automatic via nginx)
# Allowed origins: *.run.app, localhost:*
```

---

## 🧪 Testing

### Local Testing

```bash
# Test Backend API Endpoints
curl http://localhost:8080/api/categories
curl http://localhost:8080/api/experiences
curl http://localhost:8080/api/locations

# Test User Registration
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User",
    "email": "test@example.com",
    "password": "testpass123"
  }'

# Test User Login
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "testpass123"
  }'

# Run Backend Tests
cd backend/samana-backend
mvn test
```

### GCP Testing

```bash
# Get Backend URL
BACKEND_URL=$(gcloud run services describe samana-backend --region=asia-south1 --format='value(status.url)')

# Test API
curl $BACKEND_URL/api/categories
curl $BACKEND_URL/api/experiences

# Get Frontend URL
FRONTEND_URL=$(gcloud run services describe samana-frontend --region=asia-south1 --format='value(status.url)')

# Open in browser
open $FRONTEND_URL
```

---

## 🐛 Troubleshooting

### Local Development Issues

#### Backend fails to start with database connection error

```bash
# Check if PostgreSQL is running
pg_isready -h localhost -p 5432

# For Neon, verify connection string
psql $NEON_DATABASE_URL

# Check if port 8080 is already in use
lsof -i :8080

# Kill process using port 8080
kill -9 <PID>
```

#### Frontend not loading

```bash
# Check if Python HTTP server is running
lsof -i :3000

# Restart frontend server
python3 -m http.server 3000

# Clear browser cache
# Press Cmd+Shift+R (macOS) or Ctrl+Shift+R (Linux/Windows)
```

#### CORS errors

- **Issue**: "Access to fetch at 'http://localhost:8080/api/...' has been blocked by CORS policy"
- **Solution**: Verify CORS configuration in `backend/samana-backend/src/main/java/com/samana/config/SecurityConfig.java`
- **Allowed origins**: `http://localhost:3000`, `http://127.0.0.1:3000`

### GCP Deployment Issues

#### Backend service fails to start (Health check timeout)

```bash
# Check service logs
gcloud run services logs read samana-backend --region=asia-south1

# Common causes:
# 1. Database connection issue - verify NEON_DATABASE_URL
# 2. Incorrect Spring profile - check SPRING_PROFILES_ACTIVE
# 3. Insufficient memory - increase Cloud Run memory

# Increase timeout and memory
gcloud run deploy samana-backend \
  --timeout=300 \
  --memory=512Mi
```

#### "Driver org.postgresql.Driver claims to not accept jdbcUrl"

- **Cause**: JDBC URL format is incorrect
- **Solution**: Ensure URL starts with `jdbc:postgresql://`
- **Check**: `application-neon.properties` line 7

#### 403 Forbidden when pushing to GCP

```bash
# Authenticate with Docker
gcloud auth configure-docker gcr.io

# Verify GCP project
gcloud config list --format='value(core.project)'

# Re-run deployment
./deploy.sh
```

#### Frontend CSS/JS files return 404

- **Cause**: Nginx not serving static files correctly
- **Solution**: Verify `nginx.conf` configuration
- **Check**: Files exist in `/usr/share/nginx/html/`

### Common Error Messages

| Error | Solution |
|-------|----------|
| `Connection refused` | PostgreSQL not running. Run `brew services start postgresql@15` |
| `Port already in use` | Kill process: `lsof -i :PORT` then `kill -9 PID` |
| `Invalid JDBC URL` | Check `application-neon.properties` for correct format |
| `CORS policy error` | Verify frontend URL in backend CORS config |
| `Health check timeout` | Increase timeout in Cloud Run settings or check database connection |

---

## 📚 Database Schema

### Key Tables

#### Users
```sql
CREATE TABLE users (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(255),
  email VARCHAR(255) UNIQUE,
  password VARCHAR(255),
  role VARCHAR(50),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### Experiences
```sql
CREATE TABLE experiences (
  id BIGSERIAL PRIMARY KEY,
  title VARCHAR(255),
  description TEXT,
  location VARCHAR(255),
  price DECIMAL(10, 2),
  duration INT,
  max_guests INT,
  host_id BIGINT REFERENCES users(id),
  category_id BIGINT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### Bookings
```sql
CREATE TABLE bookings (
  id BIGSERIAL PRIMARY KEY,
  user_id BIGINT REFERENCES users(id),
  experience_id BIGINT REFERENCES experiences(id),
  booking_date DATE,
  status VARCHAR(50),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

---

## 🚀 Deployment Checklist

### Pre-Deployment
- [ ] All environment variables set correctly
- [ ] Database is running and accessible
- [ ] Backend builds successfully (`mvn clean package`)
- [ ] Frontend loads without errors
- [ ] All APIs return 200 responses
- [ ] CORS is configured correctly
- [ ] JWT secret is secure and 32+ characters

### Post-Deployment (GCP)
- [ ] Backend service shows "Ready" status
- [ ] Frontend service shows "Ready" status
- [ ] API endpoints respond correctly
- [ ] Frontend loads from GCP URL
- [ ] CORS headers are present in responses
- [ ] Logs show no errors or warnings
- [ ] Database connection successful

---

## 📞 Support & Documentation

### API Documentation
- Base URL: `https://samana-backend-hmuq4onkta-el.a.run.app`
- Endpoints: `/api/categories`, `/api/experiences`, `/api/users`, etc.

### Database
- **Provider**: Neon PostgreSQL (https://neon.tech)
- **Free tier**: 3 projects, auto-scaling
- **Connection**: Via NEON_DATABASE_URL environment variable

### Cloud Platform
- **Provider**: Google Cloud Run
- **Region**: asia-south1 (India)
- **Pricing**: ~$0.40/million requests

---

## 📝 License

This project is part of the SAMANA cultural experience platform.

---

## 👨‍💻 Development Team

- **Frontend**: React/Vanilla JS, Bootstrap, Nginx
- **Backend**: Spring Boot 3.5, Spring Data JPA, Spring Security
- **Database**: PostgreSQL 15+ (Neon)
- **Deployment**: Google Cloud Run, Docker
- **CI/CD**: Manual via `deploy.sh`

---

## 🎯 Next Steps

1. **Local Development**
   ```bash
   ./start-all.sh
   # App runs on http://localhost:3000
   ```

2. **Push to GitHub**
   ```bash
   git add .
   git commit -m "Your changes"
   git push origin main
   ```

3. **Deploy to GCP**
   ```bash
   export NEON_DATABASE_URL="your-neon-url"
   ./deploy.sh
   ```

---

**Last Updated**: October 11, 2026  
**Status**: ✅ Production Ready

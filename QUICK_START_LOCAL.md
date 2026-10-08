# ⚡ QUICK START - Run SAMANA Locally

**5-10 minutes to get everything running!**

---

## 🚀 Fastest Way to Get Started

### Option 1: Automated Setup (Recommended)

```bash
cd /Users/sudhir_ahirkar/Applications/samana/samana

# Run the automated setup script
./setup-local.sh
```

This will:
- ✅ Check all prerequisites
- ✅ Create database & user
- ✅ Configure backend environment
- ✅ Update frontend API URLs
- ✅ Build backend (Maven)

Then follow the instructions at the end!

---

### Option 2: Manual Setup (5 minutes)

#### Step 1: Start PostgreSQL

```bash
brew services start postgresql@14
# or use pgAdmin GUI
```

#### Step 2: Create Database

```bash
psql -U postgres

# In psql:
CREATE DATABASE samana_db;
CREATE USER samana_user WITH PASSWORD 'samana_password';
GRANT ALL PRIVILEGES ON DATABASE samana_db TO samana_user;
\q
```

#### Step 3: Start Backend

```bash
cd backend/samana-backend

# Set environment variables
export JWT_SECRET=samana-local-dev-secret-key-minimum-32-chars-required
export DB_USERNAME=samana_user
export DB_PASSWORD=samana_password
export SPRING_PROFILES_ACTIVE=postgres

# Run backend
mvn spring-boot:run

# Should see: Started SamanaBackendApplication
```

#### Step 4: Start Frontend

In a **new terminal**:

```bash
cd frontend

# Serve on port 3000
python3 -m http.server 3000
```

#### Step 5: Open Browser

```
http://localhost:3000
```

---

## 🧪 Quick Test

### Backend Health Check
```bash
curl http://localhost:8080/api/categories
# Should return JSON response
```

### Frontend Check
```bash
# Open http://localhost:3000 in browser
# Should load without console errors
```

### Full Test Flow
1. Go to `http://localhost:3000`
2. Click "Register"
3. Fill in the form
4. Submit
5. Should see green toast notification
6. Check for validation errors

---

## 🔧 Environment Variables

If backend won't start, check these are set:

```bash
# Check variables
echo $JWT_SECRET
echo $DB_USERNAME
echo $DB_PASSWORD
echo $SPRING_PROFILES_ACTIVE

# Set if missing
export JWT_SECRET=samana-local-dev-secret-key-minimum-32-chars-required
export DB_USERNAME=samana_user
export DB_PASSWORD=samana_password
export SPRING_PROFILES_ACTIVE=postgres
```

---

## 📍 Service URLs

When everything is running:

```
Frontend:  http://localhost:3000
Backend:   http://localhost:8080
Database:  localhost:5432 (samana_db)
```

---

## 🆘 Troubleshooting Quick Fix

### Backend won't connect to database
```bash
# Check PostgreSQL is running
psql -U postgres -c "SELECT version();"

# If error, restart PostgreSQL
brew services restart postgresql@14
```

### Port already in use
```bash
# Kill process on port 8080
lsof -i :8080
kill -9 <PID>

# Or use different port
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=9090"
```

### CORS/API errors in browser
```bash
# Make sure API URLs in JS files point to localhost:8080
grep -r "API_BASE_URL" frontend/js/

# Should show: http://localhost:8080/api (not https://...)
```

### Can't login
```bash
# Open DevTools (F12) → Application → LocalStorage
# Check if token is stored
# Try registering a new account instead
```

---

## 📊 Running Both Services Side-by-Side

Use multiple terminals:

```
Terminal 1 (Backend):
cd backend/samana-backend
source .env 2>/dev/null || true
export JWT_SECRET=samana-local-dev-secret-key-minimum-32-chars-required
mvn spring-boot:run

Terminal 2 (Frontend):
cd frontend
python3 -m http.server 3000

Terminal 3 (Optional - Monitor Logs):
tail -f nohup.out
```

---

## ✅ Success Checklist

- [ ] PostgreSQL running
- [ ] Backend server on port 8080
- [ ] Frontend server on port 3000
- [ ] Browser opens http://localhost:3000 without 404
- [ ] No CORS errors in console
- [ ] Can see login form
- [ ] Toast notifications work
- [ ] Can register/login

---

## 📝 Important Notes

1. **First build takes 5-10 minutes** - Maven downloads all dependencies
2. **Database auto-creates tables** - Check backend logs for "Hibernate: CREATE TABLE"
3. **JWT_SECRET must be 32+ characters** - Used for token signing
4. **Change back to production URLs before deploying** - See `DEPLOY_GCP.md`

---

## 🎯 Next Steps

### After Testing Locally:

1. **Test all features:**
   - Registration & Login
   - Form validation
   - API calls
   - Navigation

2. **Check for console errors:**
   - F12 → Console tab
   - Fix any errors

3. **Ready to deploy to GCP:**
   - See `DEPLOY_GCP.md` for cloud setup

---

## 💡 Pro Tips

### Live Reload Frontend
```bash
# Instead of Python server, use live-server for auto-reload
npm install -g live-server
cd frontend
live-server --port=3000 --no-browser
# Auto-refreshes when you save files
```

### Watch Database
```bash
# Terminal 3 - Monitor database changes
psql -U samana_user -d samana_db
SELECT * FROM users;
\watch 2  # Refresh every 2 seconds
```

### View Backend Logs
```bash
# Terminal 4 - Watch logs
tail -f backend/samana-backend/spring.log
```

---

## 📞 Quick Commands Reference

```bash
# Start all
cd /Users/sudhir_ahirkar/Applications/samana/samana
./setup-local.sh

# Backend
cd backend/samana-backend && mvn spring-boot:run

# Frontend  
cd frontend && python3 -m http.server 3000

# Database
psql -U samana_user -d samana_db

# Test API
curl http://localhost:8080/api/categories

# Stop services
pkill -f "mvn spring-boot:run"
pkill -f "http.server"
```

---

**Ready to go! 🚀 See `LOCAL_SETUP_GUIDE.md` for detailed help.**

# 🚀 QUICK START - RUN NOW!

**Everything is ready. Just follow these 3 simple steps.**

---

## 📋 PREREQUISITES CHECK

```bash
# Verify you have everything
java -version          # Should show Java 21+
python3 --version      # Should show Python 3.8+
curl --version        # Should show curl
```

---

## 🚀 START EVERYTHING (3 TERMINALS)

### Terminal 1: BACKEND (Spring Boot)

```bash
cd /Users/sudhir_ahirkar/Applications/samana/samana/backend/samana-backend

# Set environment variables
export NEON_DATABASE_URL="postgresql://your_user:your_password@ep-xyz.us-east-1.neon.tech/samana_db?sslmode=require"
export SPRING_PROFILES_ACTIVE=neon
export JWT_SECRET="your-secret-key-min-32-chars"
export DB_USERNAME="your_user"
export DB_PASSWORD="your_password"

# Start backend
java -jar target/samana-backend-0.0.1-SNAPSHOT.jar
```

**Expected output:**
```
Started SamanaApplication in X.XXX seconds (JVM running for X.XXX)
Tomcat started on port 8080 (http)
```

---

### Terminal 2: FRONTEND (HTTP Server)

```bash
cd /Users/sudhir_ahirkar/Applications/samana/samana

# Start Python HTTP server
python3 -m http.server 3000
```

**Expected output:**
```
Serving HTTP on 0.0.0.0 port 3000 (http://0.0.0.0:3000/) ...
```

---

### Terminal 3: VERIFY (Testing)

```bash
cd /Users/sudhir_ahirkar/Applications/samana/samana

# Wait 5 seconds for services to start, then test
sleep 5

# Test backend
curl http://localhost:8080/api/experiences
# Should return: {"total":0,"experiences":[]}

# Test frontend
curl http://localhost:3000/index.html | head -20
# Should return HTML content
```

---

## 🌐 ACCESS IN BROWSER

Once services are running:

1. **Frontend**: http://localhost:3000
2. **Backend API**: http://localhost:8080/api

---

## ⚠️ IF NEON CONNECTION FAILS

**OPTION A: Use Local PostgreSQL (if available)**

```bash
# Skip this if PostgreSQL is not set up

# Check if PostgreSQL is running
brew services list | grep postgresql

# If not running:
brew services start postgresql

# Then create database:
psql -U postgres -c "CREATE DATABASE samana_db;" || true
psql -U postgres -c "CREATE USER samana_user WITH PASSWORD 'samana_password';" || true

# Update Terminal 1 environment:
export SPRING_PROFILES_ACTIVE=postgres
export DB_USERNAME=samana_user
export DB_PASSWORD=samana_password

# Then run: java -jar target/samana-backend-0.0.1-SNAPSHOT.jar
```

**OPTION B: Setup Neon (Recommended - 5 minutes)**

1. Go to https://neon.tech
2. Sign up (free)
3. Create project: "samana-production"
4. Region: us-east-1
5. Copy connection string from dashboard
6. Use in Terminal 1 export commands above

---

## 🧪 AUTOMATED TESTING (after services start)

Terminal 4:

```bash
cd /Users/sudhir_ahirkar/Applications/samana/samana
./test-local.sh
```

This will:
- Test all 26+ API endpoints
- Verify all 3 features (Packages, Search, Calendar)
- Check frontend pages
- Generate test report

---

## 🛑 STOP EVERYTHING

When done testing:

```bash
# Terminal 1: Press Ctrl+C
# Terminal 2: Press Ctrl+C
# Terminal 3 & 4: Press Ctrl+C

# Or use the stop script:
./stop-local.sh
```

---

## 📊 TROUBLESHOOTING

### Backend won't start

```
Error: Connection refused on port 8080

Solution:
1. Check Neon connection string is correct
2. Verify environment variables are set:
   echo $NEON_DATABASE_URL
3. Check logs: tail backend/samana-backend/backend.log
4. Make sure port 8080 is not in use:
   lsof -i :8080
```

### Frontend shows "connection refused"

```
Error: localhost:3000 refused to connect

Solution:
1. Make sure Terminal 2 is running Python server
2. Check port 3000 is not in use:
   lsof -i :3000
3. Try: python3 -m http.server 3000
```

### API calls failing from frontend

```
Error: CORS error or 404

Solution:
1. Verify backend is running on 8080:
   curl http://localhost:8080/api/experiences
2. Check browser console (F12) for exact error
3. Verify API_BASE_URL in frontend code
4. Restart both services
```

---

## ✅ SUCCESS INDICATORS

✅ **Backend Running**
- Terminal shows: "Started SamanaApplication"
- Port 8080 responding: curl returns JSON

✅ **Frontend Running**
- Terminal shows: "Serving HTTP on 0.0.0.0:3000"
- Page loads at http://localhost:3000

✅ **Connected**
- Frontend loads without errors
- Browser console clean (F12 → Console)
- API calls succeed

✅ **Tests Passing**
- ./test-local.sh returns: "All tests passing ✅"
- All 26+ endpoints verified

---

## 🎯 NEXT STEPS AFTER LOCAL TESTING

1. Verify all features work:
   - Search & Filters (http://localhost:3000/explore.html)
   - Experience Packages (http://localhost:3000/experience.html?id=1)
   - Festival Calendar (if created)

2. Check browser console for errors (F12)

3. Review test results (./test-local.sh)

4. If all pass, proceed to GCP deployment:
   - Follow GCP_DEPLOYMENT_GUIDE.md

---

## 📞 QUICK REFERENCE

| Component | Port | Command | Status |
|-----------|------|---------|--------|
| Backend | 8080 | `java -jar target/samana-backend-0.0.1-SNAPSHOT.jar` | Terminal 1 |
| Frontend | 3000 | `python3 -m http.server 3000` | Terminal 2 |
| Tests | - | `./test-local.sh` | Terminal 3 |
| Neon | - | https://neon.tech | Setup once |

---

## 🎉 YOU'RE ALL SET!

Everything is built, tested, and ready to run.

**Just follow the 3 terminal commands above and you'll have:**
- ✅ Backend API running
- ✅ Frontend UI loaded
- ✅ Database connected
- ✅ All features working

**Estimated time: 5 minutes to full setup**

---

Generated: October 9, 2026  
For: SAMANA Platform Team

# 🚀 NEON POSTGRESQL SETUP GUIDE

**Using Cloud-Based PostgreSQL for Local Development**

---

## ✨ Why Neon?

- ✅ **Zero Local Setup** - No PostgreSQL installation needed
- ✅ **Same Connection Locally & GCP** - Single configuration works everywhere
- ✅ **Free Tier** - Up to 3 projects, fully featured
- ✅ **Automatic Backups** - Built-in reliability
- ✅ **Instant Scaling** - Grows with your needs
- ✅ **Simple Connection** - Just a URL

---

## 📋 STEP 1: CREATE NEON ACCOUNT

1. Go to: **https://neon.tech**
2. Click **Sign Up**
3. Choose sign-up method (GitHub/Google/Email)
4. Create account

---

## 🗄️ STEP 2: CREATE NEON PROJECT

1. In Neon Dashboard, click **Create Project**
2. Project Details:
   - **Project Name:** samana-production
   - **Database Name:** samana_db (default is fine)
   - **Branch:** main
   - **Region:** us-east-1 (closest to GCP)
   - **Postgres Version:** 15 or 16

3. Click **Create Project**
4. Wait ~30 seconds for provisioning

---

## 🔐 STEP 3: GET CONNECTION STRING

1. In Neon Dashboard, you'll see your project
2. Click **Connection String** button
3. Choose **Connection string** (not psql)
4. Copy the full URL (looks like):
   ```
   postgresql://neon_user:neon_password@ep-abc123.us-east-1.neon.tech/samana_db?sslmode=require
   ```

---

## 🔧 STEP 4: CONFIGURE LOCAL BACKEND

### Option A: Using Environment Variables (Recommended)

```bash
# Set environment variables
export NEON_DATABASE_URL="postgresql://your_user:your_password@ep-xyz.us-east-1.neon.tech/samana_db?sslmode=require"
export DB_USERNAME="your_user"
export DB_PASSWORD="your_password"
export JWT_SECRET="your-secret-key-min-32-chars"
export SPRING_PROFILES_ACTIVE=neon

# Start backend
cd backend/samana-backend
java -jar target/samana-backend-0.0.1-SNAPSHOT.jar
```

### Option B: Using .env File

```bash
# Create .env file
cat > .env << 'EOF'
export NEON_DATABASE_URL="postgresql://your_user:your_password@ep-xyz.us-east-1.neon.tech/samana_db?sslmode=require"
export DB_USERNAME="your_user"
export DB_PASSWORD="your_password"
export JWT_SECRET="your-secret-key-min-32-chars"
export SPRING_PROFILES_ACTIVE=neon
EOF

# Load and run
source .env
cd backend/samana-backend
java -jar target/samana-backend-0.0.1-SNAPSHOT.jar
```

### Option C: Direct Configuration

Edit `backend/samana-backend/src/main/resources/application-neon.properties`:

```properties
spring.datasource.url=postgresql://your_user:your_password@ep-xyz.us-east-1.neon.tech/samana_db?sslmode=require
spring.datasource.username=your_user
spring.datasource.password=your_password
```

---

## 🚀 STEP 5: START BACKEND WITH NEON

```bash
# Terminal 1: Backend
cd backend/samana-backend
export NEON_DATABASE_URL="postgresql://user:pass@ep-xyz.us-east-1.neon.tech/samana_db?sslmode=require"
export SPRING_PROFILES_ACTIVE=neon
export JWT_SECRET="your-secret-key"
java -jar target/samana-backend-0.0.1-SNAPSHOT.jar

# Expected output:
# ✅ HikariPool - Starting...
# ✅ Dialect - PostgreSQL
# ✅ SessionFactoryObserverChain - sessionFactoryCreated
# ✅ Started SamanaApplication in X seconds
```

---

## 🌐 STEP 6: VERIFY CONNECTION

### Check Backend Logs

```bash
# Should show:
# ✅ Connected to Neon PostgreSQL
# ✅ Creating schema/tables
# ✅ Ready for requests
```

### Test API Endpoints

```bash
# Terminal 2: Test
curl http://localhost:8080/api/experiences
# Should return: {"total":0,"experiences":[]}

curl http://localhost:8080/api/calendar/festival-states
# Should return: {"total":0,"states":[]}
```

### Connect Directly to Neon (Optional)

```bash
# Install psql client if not present
brew install libpq

# Connect to Neon
psql "postgresql://your_user:your_password@ep-xyz.us-east-1.neon.tech/samana_db?sslmode=require"

# In psql:
\dt                    # List tables
SELECT COUNT(*) FROM experiences;
\q
```

---

## 📊 STEP 7: VERIFY TABLES CREATED

After backend starts, check Neon dashboard:

1. Go to **Neon Dashboard**
2. Click on **Tables** section
3. You should see:
   - ✅ experiences
   - ✅ experience_packages (NEW)
   - ✅ available_dates (NEW)
   - ✅ users
   - ✅ bookings
   - ✅ reviews
   - ✅ categories
   - ✅ locations
   - ✅ host_profiles
   - ✅ payments

---

## 🎯 STEP 8: COMPLETE LOCAL SETUP

Now proceed with normal local testing:

```bash
# Terminal 1: Backend (already running)
# Already started with Neon

# Terminal 2: Frontend
cd /Users/sudhir_ahirkar/Applications/samana/samana
python3 -m http.server 3000

# Terminal 3: Tests
./test-local.sh
```

---

## 🔄 SWITCHING BETWEEN DATABASES

### Use Neon (Cloud - Local Dev)
```bash
export SPRING_PROFILES_ACTIVE=neon
```

### Use Local PostgreSQL (if you have it)
```bash
export SPRING_PROFILES_ACTIVE=postgres
```

### Use GCP Cloud SQL (Production)
```bash
export SPRING_PROFILES_ACTIVE=cloud
```

---

## 🚀 SAME CONNECTION FOR GCP DEPLOYMENT

The beauty of Neon is you can use it for **local development AND production**:

```bash
# Local (neon.tech hosted)
NEON_DATABASE_URL=postgresql://user:pass@ep-xyz.us-east-1.neon.tech/samana_db

# Same URL works in GCP Cloud Run!
# Just set environment variable in Cloud Run settings
```

---

## 💡 NEON FEATURES

### Free Tier Includes
- 3 projects
- 5 GB storage per project
- Automatic daily backups
- IP whitelist
- Connection pooling
- Branching (create branches for testing)

### Monitoring
1. Go to **Monitoring** tab
2. View real-time metrics:
   - Connections active
   - Query latency
   - Storage usage
   - Cache hit ratio

---

## 🔐 SECURITY BEST PRACTICES

1. **Never commit credentials** to Git
2. **Use environment variables** for connection strings
3. **Store in .env** (add to .gitignore)
4. **Use IP whitelist** in Neon (if needed)
5. **Rotate passwords** periodically

Example .gitignore entry:
```
.env
.env.local
```

---

## 🆘 TROUBLESHOOTING

### Connection Refused
```
Error: Connection to ep-xyz.us-east-1.neon.tech refused

Solution:
1. Check internet connection
2. Verify Neon is running (check dashboard)
3. Verify connection string is correct
4. Check firewall isn't blocking port 5432
```

### Authentication Failed
```
Error: role "user_name" does not exist

Solution:
1. Copy connection string again from Neon dashboard
2. Verify username and password are correct
3. Reset password in Neon if needed
```

### SSL Connection Error
```
Error: FATAL: no pg_hba.conf entry

Solution:
1. Ensure ?sslmode=require is in connection string
2. Update Spring properties with SSL settings:
   spring.datasource.hikari.data-source-properties.sslMode=require
```

### Too Many Connections
```
Error: sorry, too many clients already

Solution:
1. Reduce connection pool size in properties
2. Use Neon connection pooling (PgBouncer)
3. In Neon dashboard, enable pooler endpoint
```

---

## 📝 QUICK REFERENCE

### Connection String Format
```
postgresql://[user]:[password]@[host]/[database]?sslmode=require
```

### Environment Variables
```bash
export NEON_DATABASE_URL="your_connection_string"
export SPRING_PROFILES_ACTIVE=neon
export JWT_SECRET="your_jwt_secret"
```

### Start Command
```bash
cd backend/samana-backend
source /path/to/.env  # Load variables
java -jar target/samana-backend-0.0.1-SNAPSHOT.jar
```

### Test Connection
```bash
curl http://localhost:8080/api/experiences
```

---

## 🎉 YOU'RE READY!

With Neon set up, you have:

✅ **Cloud PostgreSQL** - No local setup
✅ **Production Ready** - Use same connection for GCP
✅ **Zero Maintenance** - Neon handles backups & updates
✅ **Scalable** - Grows with your data
✅ **Reliable** - Redundancy & automatic failover

**Next Steps:**
1. Create Neon account
2. Get connection string
3. Set environment variables
4. Start backend
5. Run tests

---

**Neon Documentation:** https://neon.tech/docs  
**Status Page:** https://neon.tech/status  
**Support:** https://neon.tech/contact

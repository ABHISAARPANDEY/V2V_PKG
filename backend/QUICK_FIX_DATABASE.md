# Quick Fix: Database Connection Error

## 🚨 Error: "Can't reach database server at `localhost:5432`"

**Problem**: PostgreSQL is not running.

## ✅ Quick Fix (3 steps):

### 1. Start PostgreSQL with Docker:

**Try this (newer Docker - uses space):**
```bash
cd backend
docker compose up -d postgres
```

**If that doesn't work, try (older Docker - uses hyphen):**
```bash
docker-compose up -d postgres
```

**If Docker not installed:** Install Docker Desktop or use local PostgreSQL (see below).

### 2. Make sure .env file exists:
Create `backend/.env` with:
```env
DATABASE_URL="postgresql://v2v_user:v2v_password@localhost:5432/v2v_db?schema=public"
JWT_SECRET="dev-jwt-secret-change-in-production"
PORT=3001
CORS_ORIGINS="http://localhost:3000,http://localhost:5173"
```

### 3. Run migrations:
```bash
npm run prisma:generate
npm run prisma:migrate
```

### 4. Start backend:
```bash
npm run start:dev
```

Should now work! ✅

## 📋 Full Setup (First Time):

```bash
# 1. Start database (try "docker compose" first, then "docker-compose" if needed)
docker compose up -d postgres

# 2. Wait 5 seconds, then generate Prisma client
npm run prisma:generate

# 3. Run migrations (creates tables)
npm run prisma:migrate

# 4. Seed database (creates admin user)
npm run prisma:seed

# 5. Start backend
npm run start:dev
```

See `START_DATABASE.md` for detailed instructions.


# Start Database - Quick Fix

## ❌ Error: "Can't reach database server at `localhost:5432`"

This means PostgreSQL is not running. Here's how to fix it:

## ✅ Solution: Start PostgreSQL with Docker

### Step 1: Start PostgreSQL

**Try this first (newer Docker versions):**
```bash
cd backend
docker compose up -d postgres
```

**If that doesn't work, try (older Docker versions):**
```bash
docker-compose up -d postgres
```

**If Docker is not installed**, see "Alternative" section below or install Docker Desktop.

Wait a few seconds for PostgreSQL to start.

### Step 2: Verify Database is Running

```bash
# Check if container is running
docker ps
```

You should see `v2v-postgres` in the list.

### Step 3: Create .env File (if not exists)

```bash
# Create .env file
```

Add this content to `.env`:
```env
DATABASE_URL="postgresql://v2v_user:v2v_password@localhost:5432/v2v_db?schema=public"
JWT_SECRET="dev-jwt-secret-change-in-production"
PORT=3001
CORS_ORIGINS="http://localhost:3000,http://localhost:5173"
```

### Step 4: Run Migrations

```bash
npm run prisma:generate
npm run prisma:migrate
```

### Step 5: Start Backend Again

```bash
npm run start:dev
```

Should now see: `✅ Database connected`

## 🔍 Alternative: Check if PostgreSQL is Already Running

If you have PostgreSQL installed locally:

1. **Check if it's running:**
   - Windows: Check Services (services.msc) for PostgreSQL
   - Mac/Linux: `sudo systemctl status postgresql`

2. **Update .env with your local credentials:**
   ```env
   DATABASE_URL="postgresql://postgres:YOUR_PASSWORD@localhost:5432/v2v_db?schema=public"
   ```

3. **Create database:**
   ```sql
   CREATE DATABASE v2v_db;
   ```

## 🆘 Troubleshooting

### Docker not installed?
- Install Docker Desktop: https://www.docker.com/products/docker-desktop
- Or use local PostgreSQL (see alternative above)

### "Port 5432 already in use"
- Another PostgreSQL instance is running
- Stop it or change port in docker-compose.yml

### Still can't connect?
- Check `.env` file has correct DATABASE_URL
- Verify Docker container is running: `docker ps`
- Check logs: `docker-compose logs postgres`


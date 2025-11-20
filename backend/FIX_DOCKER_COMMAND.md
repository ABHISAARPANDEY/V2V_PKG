# Fix: docker-compose Command Not Found

## Problem
`docker-compose` command not recognized. This can mean:
1. Docker is not installed
2. Using newer Docker version (uses `docker compose` instead)

## ✅ Solution 1: Use New Docker Compose Syntax

Newer Docker versions use `docker compose` (space) instead of `docker-compose` (hyphen):

```bash
docker compose up -d postgres
```

Try this first!

## ✅ Solution 2: Install Docker Desktop

If Docker is not installed:

1. **Download Docker Desktop:**
   - Windows: https://www.docker.com/products/docker-desktop
   - Install and restart your computer

2. **Start Docker Desktop** (should be running in system tray)

3. **Then try:**
   ```bash
   docker compose up -d postgres
   ```

## ✅ Solution 3: Use Local PostgreSQL (No Docker)

If you don't want to use Docker, install PostgreSQL locally:

### Windows:
1. Download PostgreSQL: https://www.postgresql.org/download/windows/
2. Install with default settings
3. Remember the password you set during installation

### Update .env file:
```env
DATABASE_URL="postgresql://postgres:YOUR_PASSWORD@localhost:5432/v2v_db?schema=public"
JWT_SECRET="dev-jwt-secret-change-in-production"
PORT=3001
CORS_ORIGINS="http://localhost:3000,http://localhost:5173"
```

### Create database:
1. Open pgAdmin or psql
2. Run: `CREATE DATABASE v2v_db;`

### Then run migrations:
```bash
npm run prisma:generate
npm run prisma:migrate
npm run prisma:seed
npm run start:dev
```

## 🔍 Check Docker Version

To check if Docker is installed and which version:

```bash
docker --version
docker compose version
```

If these work, use `docker compose` (space) instead of `docker-compose` (hyphen).



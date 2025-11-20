# Environment Variables Setup Guide

## 📝 What to Put in `.env` File

### Option 1: Using Docker (Easiest - Recommended)

If you're using Docker Compose for PostgreSQL, use these credentials:

```env
DATABASE_URL="postgresql://v2v_user:v2v_password@localhost:5432/v2v_db?schema=public"
JWT_SECRET="dev-jwt-secret-change-in-production"
PORT=3001
CORS_ORIGINS="http://localhost:3000,http://localhost:5173"
```

**Explanation:**
- **DATABASE_URL**: Uses the credentials from `docker-compose.yml`
  - Username: `v2v_user`
  - Password: `v2v_password`
  - Database: `v2v_db`
  - Host: `localhost` (or `postgres` if running inside Docker)
  - Port: `5432`

### Option 2: Using Local PostgreSQL

If you have PostgreSQL installed locally:

```env
DATABASE_URL="postgresql://YOUR_POSTGRES_USER:YOUR_POSTGRES_PASSWORD@localhost:5432/v2v_db?schema=public"
JWT_SECRET="dev-jwt-secret-change-in-production"
PORT=3001
CORS_ORIGINS="http://localhost:3000,http://localhost:5173"
```

**Replace:**
- `YOUR_POSTGRES_USER` - Your PostgreSQL username (usually `postgres`)
- `YOUR_POSTGRES_PASSWORD` - Your PostgreSQL password

**Example:**
```env
DATABASE_URL="postgresql://postgres:mypassword123@localhost:5432/v2v_db?schema=public"
```

## 🔧 Step-by-Step Setup

### 1. Create .env File

```bash
cd backend
cp .env.example .env
```

### 2. Edit .env File

Open `.env` in a text editor and fill in the values:

#### For Docker Setup:
```env
# Database (matches docker-compose.yml)
DATABASE_URL="postgresql://v2v_user:v2v_password@localhost:5432/v2v_db?schema=public"

# JWT Secret (use any random string for development)
JWT_SECRET="dev-jwt-secret-change-in-production"

# Server Port
PORT=3001

# CORS Origins (comma-separated)
CORS_ORIGINS="http://localhost:3000,http://localhost:5173"
```

#### For Local PostgreSQL:
```env
# Database (replace with your PostgreSQL credentials)
DATABASE_URL="postgresql://postgres:yourpassword@localhost:5432/v2v_db?schema=public"

# JWT Secret
JWT_SECRET="dev-jwt-secret-change-in-production"

# Server Port
PORT=3001

# CORS Origins
CORS_ORIGINS="http://localhost:3000,http://localhost:5173"
```

## 📋 Field Explanations

### DATABASE_URL
Format: `postgresql://USERNAME:PASSWORD@HOST:PORT/DATABASE_NAME?schema=public`

- **USERNAME**: PostgreSQL username
- **PASSWORD**: PostgreSQL password
- **HOST**: `localhost` (or `postgres` if using Docker network)
- **PORT**: `5432` (default PostgreSQL port)
- **DATABASE_NAME**: `v2v_db` (or any name you prefer)

### JWT_SECRET
- Any random string for development
- Use a strong, random string in production
- Example: `"my-super-secret-jwt-key-12345"`

### PORT
- Port for the backend API
- Default: `3001`
- Change if port is already in use

### CORS_ORIGINS
- Comma-separated list of allowed origins
- For development, you can use `"*"` or leave as is
- Already configured to allow all origins in code

## ✅ Quick Setup (Docker)

1. **Create .env file:**
   ```bash
   cd backend
   cp .env.example .env
   ```

2. **Edit .env - Use these exact values for Docker:**
   ```env
   DATABASE_URL="postgresql://v2v_user:v2v_password@localhost:5432/v2v_db?schema=public"
   JWT_SECRET="dev-jwt-secret-change-in-production"
   PORT=3001
   CORS_ORIGINS="http://localhost:3000,http://localhost:5173"
   ```

3. **Start Docker:**
   ```bash
   docker-compose up -d postgres
   ```

4. **Run migrations:**
   ```bash
   npm run prisma:generate
   npm run prisma:migrate
   ```

5. **Start backend:**
   ```bash
   npm run start:dev
   ```

## ✅ Quick Setup (Local PostgreSQL)

1. **Create .env file:**
   ```bash
   cd backend
   cp .env.example .env
   ```

2. **Edit .env - Replace with your PostgreSQL credentials:**
   ```env
   DATABASE_URL="postgresql://postgres:YOUR_PASSWORD@localhost:5432/v2v_db?schema=public"
   JWT_SECRET="dev-jwt-secret-change-in-production"
   PORT=3001
   CORS_ORIGINS="http://localhost:3000,http://localhost:5173"
   ```

3. **Create database:**
   ```sql
   CREATE DATABASE v2v_db;
   ```

4. **Run migrations:**
   ```bash
   npm run prisma:generate
   npm run prisma:migrate
   ```

5. **Start backend:**
   ```bash
   npm run start:dev
   ```

## 🆘 Troubleshooting

### "Database connection failed"
- Check PostgreSQL is running
- Verify username/password in DATABASE_URL
- Make sure database exists (for local PostgreSQL)

### "Port 3001 already in use"
- Change `PORT=3002` in .env
- Or stop the process using port 3001

### "Invalid DATABASE_URL format"
- Make sure there are quotes around the URL
- Check for typos in username/password
- Verify format: `postgresql://user:pass@host:port/db?schema=public`


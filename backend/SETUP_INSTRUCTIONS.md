# Backend Setup Instructions

## ⚠️ "nest is not recognized" Error

This error means **dependencies are not installed**. Follow these steps:

## Step-by-Step Setup

### 1. Install Dependencies

```bash
cd backend
npm install
```

This will install all required packages including NestJS CLI. Wait for it to complete (may take 2-5 minutes).

### 2. Set Up Environment Variables

```bash
# Copy the example env file
cp .env.example .env
```

Then edit `.env` file with your database credentials:

```env
DATABASE_URL="postgresql://user:password@localhost:5432/v2v_db?schema=public"
JWT_SECRET="your-super-secret-jwt-key-change-in-production"
PORT=3001
CORS_ORIGINS="http://localhost:3000,http://localhost:5173"
```

### 3. Set Up Database

**Option A: Using Docker (Recommended for quick setup)**

```bash
# Start PostgreSQL
docker-compose up -d postgres

# Wait a few seconds for database to start, then run migrations
npm run prisma:generate
npm run prisma:migrate
```

**Option B: Using Local PostgreSQL**

1. Install PostgreSQL if not already installed
2. Create a database named `v2v_db`
3. Update `DATABASE_URL` in `.env` with your credentials
4. Run:
```bash
npm run prisma:generate
npm run prisma:migrate
```

### 4. Seed Database (Create Admin User)

```bash
npm run prisma:seed
```

This creates:
- **Admin user**: `admin@v2v.com` / `admin123`
- **Test user**: `user@v2v.com` / `user123`

### 5. Start the Backend

```bash
npm run start:dev
```

You should see:
```
🚀 Backend API running on: http://localhost:3001
✅ Database connected
```

## Common Issues

### "nest is not recognized"
- **Solution**: Run `npm install` first
- Make sure you're in the `backend` directory

### "Cannot find module '@nestjs/...'"
- **Solution**: Run `npm install` again
- Delete `node_modules` folder and `package-lock.json`, then `npm install`

### "Database connection failed"
- **Solution**: 
  - Check PostgreSQL is running
  - Verify `DATABASE_URL` in `.env` is correct
  - For Docker: `docker-compose up -d postgres`

### "Port 3001 already in use"
- **Solution**: 
  - Change `PORT` in `.env` to a different port (e.g., 3002)
  - Or stop the process using port 3001

## Quick Start (All Steps)

```bash
# 1. Install dependencies
cd backend
npm install

# 2. Set up environment (edit .env file)
cp .env.example .env
# Edit .env with your database credentials

# 3. Start database (Docker)
docker-compose up -d postgres

# 4. Generate Prisma client and run migrations
npm run prisma:generate
npm run prisma:migrate

# 5. Seed database (create admin user)
npm run prisma:seed

# 6. Start backend
npm run start:dev
```

## Verify It's Working

1. **Check backend is running:**
   - Open browser: `http://localhost:3001`
   - Should see: "Cannot GET /" (this is normal)

2. **Test API endpoint:**
   - Open: `http://localhost:3001/auth/login`
   - Should see validation error (means API is working)

3. **Check logs:**
   - Should see: `🚀 Backend API running on: http://localhost:3001`
   - Should see: `✅ Database connected`

## Next Steps

Once backend is running:
1. Start admin web panel: `cd admin-web && npm install && npm run dev`
2. Start Android app in Android Studio
3. Login to admin panel with: `admin@v2v.com` / `admin123`


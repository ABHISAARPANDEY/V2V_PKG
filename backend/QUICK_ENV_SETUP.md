# Quick .env Setup

## 📝 What to Put in `.env` File

### For Docker Setup (Easiest):

```env
DATABASE_URL="postgresql://v2v_user:v2v_password@localhost:5432/v2v_db?schema=public"
JWT_SECRET="dev-jwt-secret-change-in-production"
PORT=3001
CORS_ORIGINS="http://localhost:3000,http://localhost:5173"
```

**These credentials match the Docker Compose setup.**

### For Local PostgreSQL:

```env
DATABASE_URL="postgresql://postgres:YOUR_PASSWORD@localhost:5432/v2v_db?schema=public"
JWT_SECRET="dev-jwt-secret-change-in-production"
PORT=3001
CORS_ORIGINS="http://localhost:3000,http://localhost:5173"
```

**Replace `YOUR_PASSWORD` with your actual PostgreSQL password.**

## 🚀 Quick Steps

1. **Create .env file:**
   ```bash
   cd backend
   cp .env.example .env
   ```

2. **Edit .env file** - Copy one of the configurations above

3. **If using Docker, start PostgreSQL:**
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

That's it! See `ENV_SETUP_GUIDE.md` for more details.


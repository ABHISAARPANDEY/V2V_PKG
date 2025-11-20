# Setup Without Docker (Local PostgreSQL)

If you don't have Docker or prefer local PostgreSQL:

## Step 1: Install PostgreSQL

### Windows:
1. Download: https://www.postgresql.org/download/windows/
2. Run installer
3. **Remember the password** you set for `postgres` user
4. Complete installation

### Mac:
```bash
brew install postgresql@16
brew services start postgresql@16
```

### Linux (Ubuntu/Debian):
```bash
sudo apt update
sudo apt install postgresql postgresql-contrib
sudo systemctl start postgresql
```

## Step 2: Create Database

### Using pgAdmin (GUI):
1. Open pgAdmin
2. Connect to PostgreSQL server
3. Right-click "Databases" → Create → Database
4. Name: `v2v_db`
5. Click Save

### Using psql (Command Line):
```bash
# Windows (in Command Prompt as Administrator)
psql -U postgres

# Mac/Linux
sudo -u postgres psql
```

Then in psql:
```sql
CREATE DATABASE v2v_db;
\q
```

## Step 3: Create .env File

Create `backend/.env` with:
```env
DATABASE_URL="postgresql://postgres:YOUR_PASSWORD@localhost:5432/v2v_db?schema=public"
JWT_SECRET="dev-jwt-secret-change-in-production"
PORT=3001
CORS_ORIGINS="http://localhost:3000,http://localhost:5173"
```

**Replace `YOUR_PASSWORD` with the password you set during PostgreSQL installation.**

## Step 4: Run Setup

```bash
cd backend

# Generate Prisma client
npm run prisma:generate

# Run migrations (creates tables)
npm run prisma:migrate

# Seed database (creates admin user)
npm run prisma:seed

# Start backend
npm run start:dev
```

## ✅ Verify It Works

You should see:
```
✅ Database connected
🚀 Backend API running on: http://localhost:3001
```

## 🆘 Troubleshooting

### "Password authentication failed"
- Check password in `.env` matches PostgreSQL password
- Try resetting PostgreSQL password

### "Database v2v_db does not exist"
- Create it: `CREATE DATABASE v2v_db;`

### "Connection refused"
- Make sure PostgreSQL service is running
- Windows: Check Services (services.msc)
- Mac/Linux: `sudo systemctl status postgresql`



# Quick Setup - Create Admin User

## ⚠️ Login Failed? You need to create the admin user first!

### Step 1: Make sure backend is running

```bash
cd backend
npm run start:dev
```

The backend should be running on `http://localhost:3001`

### Step 2: Create the admin user

Open a **new terminal** (keep backend running) and run:

```bash
cd backend
npm run prisma:seed
```

You should see output like:
```
🌱 Seeding database...
✅ Admin user created: admin@v2v.com
📋 Default Credentials:
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
👤 ADMIN:
   Email:    admin@v2v.com
   Password: admin123
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
✅ Seeding completed!
```

### Step 3: Try logging in again

Go back to the admin panel (`http://localhost:3000/login`) and login with:
- **Email**: `admin@v2v.com`
- **Password**: `admin123`

## 🔍 Troubleshooting

**"Cannot connect to backend" error?**
- Make sure backend is running on port 3001
- Check `http://localhost:3001` in browser - should show "Cannot GET /" (this is normal)

**"Database connection failed" error?**
- Make sure PostgreSQL is running
- Check your `.env` file has correct `DATABASE_URL`
- Run migrations: `npm run prisma:migrate`

**Still can't login?**
- Check backend logs for errors
- Verify the user was created: `npm run prisma:studio` and check the `users` table
- Try running seed script again: `npm run prisma:seed`


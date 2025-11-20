# Default Admin Credentials

## 🔐 Admin Panel Login

After running the seed script, use these credentials to login to the admin panel:

```
Email:    admin@v2v.com
Password: admin123
```

## 🚀 How to Create Default Admin User

### Option 1: Run Seed Script (Recommended)

```bash
cd backend
npm run prisma:seed
```

This will create:
- **Admin user**: `admin@v2v.com` / `admin123`
- **Test user**: `user@v2v.com` / `user123` (for mobile app testing)

### Option 2: Create Manually via API

You can also create an admin user by registering via the API and then updating the role in the database:

```bash
# 1. Register a user
curl -X POST http://localhost:3001/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Admin",
    "email": "admin@v2v.com",
    "password": "admin123"
  }'

# 2. Update role to ADMIN in database
# Use Prisma Studio or direct SQL:
# UPDATE users SET role = 'ADMIN' WHERE email = 'admin@v2v.com';
```

### Option 3: Use Prisma Studio

```bash
cd backend
npm run prisma:studio
```

Then manually create a user with:
- Email: `admin@v2v.com`
- Password: (hash it using argon2 or use the seed script)
- Role: `ADMIN`

## 📝 Test User Credentials

For testing the mobile app:

```
Email:    user@v2v.com
Password: user123
```

## ⚠️ Security Note

**Change these default credentials in production!**

These are default credentials for development/testing only. In production:
1. Change the admin password immediately
2. Use strong, unique passwords
3. Consider implementing additional security measures

## 🔄 Reset Admin Password

If you need to reset the admin password, run the seed script again:

```bash
npm run prisma:seed
```

This will update the admin password back to `admin123`.


# Test API Endpoints

## ✅ "Cannot GET /" is CORRECT!

When you see "Cannot GET /" in your browser, it means:
- ✅ Backend is running
- ✅ Backend is accessible from your phone
- ✅ Connection is working!

The root path `/` doesn't exist, so NestJS shows this message. This is **normal and expected**.

## 🧪 Test Actual API Endpoints

### From Phone Browser:

1. **Test Health Check** (if you add one):
   ```
   http://192.168.105.232:3001/health
   ```

2. **Test Register Endpoint** (will show validation error, but confirms it's working):
   ```
   http://192.168.105.232:3001/auth/register
   ```
   Should show validation error (means endpoint exists!)

### From Computer (using curl or Postman):

**Test Registration:**
```bash
curl -X POST http://localhost:3001/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User",
    "email": "test@example.com",
    "password": "password123"
  }'
```

**Test Login:**
```bash
curl -X POST http://localhost:3001/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@v2v.com",
    "password": "admin123"
  }'
```

## ✅ Your Setup is Correct!

Since you can access `http://192.168.105.232:3001` from your phone, the connection is working. The "Bad Request" error in the app is likely due to:

1. **Validation errors** - Check the error message in the app (should now show details)
2. **Missing fields** - Make sure all fields are filled
3. **Invalid format** - Email must be valid, password at least 6 chars

## 🔍 Next Steps

1. **Try registering again in the app**
2. **Check the error message** - It should now show the actual problem
3. **Check Logcat** in Android Studio for detailed logs
4. **Check backend logs** for incoming requests

The connection is working! The issue is likely with the request data or validation.


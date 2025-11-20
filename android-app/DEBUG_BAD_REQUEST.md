# Debug: Bad Request Error

## ✅ Connection is Working!

You confirmed the backend is accessible (you see the health check JSON), so the connection is fine.

## 🔍 Why "Bad Request"?

The "Bad Request" error is usually due to **validation errors**. I've improved the error handling to show you the exact problem.

## 📋 Check These Things

### 1. Registration Form Fields

Make sure you're entering:
- **Name**: At least 2 characters (e.g., "John")
- **Email**: Valid email format (e.g., "test@example.com")
- **Password**: At least 6 characters (e.g., "password123")

### 2. Check the Error Message

After the fix, the app should show a **detailed error message** instead of just "Bad Request". It will tell you:
- "name must be longer than or equal to 2 characters"
- "email must be an email"
- "password must be longer than or equal to 6 characters"

### 3. Check Logcat

In Android Studio:
1. Open **Logcat** (bottom panel)
2. Filter by your app package: `com.v2v.app`
3. Look for error messages when you try to register
4. You should see the actual backend error response

### 4. Check Backend Logs

In your backend terminal, you should see the incoming request. Look for:
- The request body being received
- Any validation errors

## 🔧 What I Fixed

1. **Better Error Parsing**: Now extracts validation error messages from backend
2. **Improved Exception Handling**: Backend now returns clearer error messages
3. **Better Error Display**: App will show the actual validation error

## ✅ Next Steps

1. **Restart Backend** (to load the new error handling):
   ```bash
   # Stop backend (Ctrl+C)
   # Restart:
   npm run start:dev
   ```

2. **Rebuild App**:
   - Build → Clean Project
   - Build → Rebuild Project

3. **Try Registering Again**:
   - The error message should now be **much more specific**
   - It will tell you exactly what's wrong

4. **Check Logcat**:
   - Look for the detailed error message
   - It should show the validation errors

## 🧪 Test Registration Manually

To verify the endpoint works, test from your computer:

```bash
curl -X POST http://localhost:3001/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User",
    "email": "test@example.com",
    "password": "password123"
  }'
```

If this works, the issue is with the app's request format. If it fails, check the error message.

The improved error handling will now show you **exactly** what's wrong!


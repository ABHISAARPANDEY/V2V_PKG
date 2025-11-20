# Fix: Bad Request Error on Registration

## Problem
Getting "Bad Request" error when trying to register. This is usually due to:
1. Validation errors (missing/invalid fields)
2. Network connectivity issues
3. Backend not running or not accessible

## ✅ Fixes Applied

1. **Improved Error Handling**: Now shows actual error messages from backend
2. **Better Network Error Messages**: More descriptive connection errors

## 🔍 Verify Your Setup

### 1. Check IP Address is Correct

Your current IP: `192.168.105.232`

**Verify it matches your computer:**
- **Windows**: Open CMD → `ipconfig` → Find "IPv4 Address"
- **Mac/Linux**: Open Terminal → `ifconfig` → Find your network IP

**If different, update `ApiClient.kt` line 13:**
```kotlin
private const val BASE_URL = "http://YOUR_ACTUAL_IP:3001/"
```

### 2. Check Backend is Running

```bash
cd backend
npm run start:dev
```

Should see:
```
🚀 Backend API running on: http://localhost:3001
✅ Database connected
```

### 3. Test Backend from Browser

Open: `http://192.168.105.232:3001`

- If you see "Cannot GET /" → Backend is accessible ✅
- If connection fails → Backend not accessible or firewall blocking ❌

### 4. Check Windows Firewall

- Allow Node.js through Windows Firewall
- Or temporarily disable firewall to test

### 5. Verify Same Network

- Phone and computer must be on **same WiFi network**
- Check WiFi name matches on both devices

## 📋 Registration Requirements

Make sure you're entering:
- **Name**: At least 2 characters
- **Email**: Valid email format (e.g., `user@example.com`)
- **Password**: At least 6 characters

## 🆘 Common Issues

### "Network error: Unable to connect"
- Backend not running
- Wrong IP address
- Firewall blocking
- Not on same network

### "Bad Request" with validation errors
- Check error message - it will show what's wrong
- Name too short (< 2 chars)
- Invalid email format
- Password too short (< 6 chars)

### "User with this email already exists"
- Email is already registered
- Try different email or login instead

## 🔧 Next Steps

1. **Rebuild app** after changes:
   - Build → Clean Project
   - Build → Rebuild Project

2. **Check Logcat** in Android Studio for detailed error messages

3. **Check backend logs** for incoming requests and errors

The improved error handling will now show you the actual error message from the backend, making it easier to debug!


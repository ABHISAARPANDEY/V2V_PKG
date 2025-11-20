# Troubleshooting: Failed to Connect to Backend

## Error: "Failed to connect to /10.0.2.2:3001"

This error means the Android app cannot reach the backend API. Follow these steps:

## ✅ Step 1: Verify Backend is Running

**Check if backend is running:**
```bash
cd backend
npm run start:dev
```

You should see:
```
🚀 Backend API running on: http://localhost:3001
✅ Database connected
```

**Test backend in browser:**
- Open: `http://localhost:3001`
- Should show: "Cannot GET /" (this is normal - means backend is running)

## ✅ Step 2: Check Emulator vs Physical Device

### For Android Emulator:
- Use: `http://10.0.2.2:3001/`
- `10.0.2.2` is the emulator's special IP that maps to your computer's `localhost`

### For Physical Device:
- **Find your computer's IP address:**
  - **Windows**: Open CMD → type `ipconfig` → find "IPv4 Address"
  - **Mac/Linux**: Open Terminal → type `ifconfig` → find your network IP
  - Example: `192.168.1.100`

- **Update ApiClient.kt:**
  ```kotlin
  private const val BASE_URL = "http://192.168.1.100:3001/"
  ```
  (Replace with your actual IP)

## ✅ Step 3: Check Firewall

**Windows Firewall:**
1. Open Windows Defender Firewall
2. Click "Allow an app or feature"
3. Make sure Node.js is allowed for Private networks
4. Or temporarily disable firewall to test

**Mac/Linux:**
- Check firewall settings allow connections on port 3001

## ✅ Step 4: Verify Network Security Config

Make sure `network_security_config.xml` exists and is referenced in `AndroidManifest.xml`:
- File: `app/src/main/res/xml/network_security_config.xml`
- Manifest should have: `android:networkSecurityConfig="@xml/network_security_config"`

## ✅ Step 5: Test Connection Manually

**From Android device/emulator:**
1. Open browser in emulator/device
2. Navigate to: `http://10.0.2.2:3001` (or your IP)
3. If you see "Cannot GET /" - connection works!
4. If connection fails - backend not accessible

## ✅ Step 6: Check Backend CORS Settings

Make sure backend allows connections from your device:

**In `backend/src/main.ts`:**
```typescript
app.enableCors({
  origin: process.env.CORS_ORIGINS?.split(',') || ['http://localhost:3000', 'http://localhost:5173'],
  credentials: true,
});
```

Add your device's origin if needed, or use `origin: true` for development.

## ✅ Step 7: Common Issues

### Issue: "Connection refused"
- **Solution**: Backend not running or wrong port
- Check: `npm run start:dev` in backend folder

### Issue: "Network unreachable"
- **Solution**: Wrong IP address or device not on same network
- Check: IP address matches your computer's actual IP

### Issue: "Timeout"
- **Solution**: Firewall blocking or backend not responding
- Check: Backend logs for errors

### Issue: Works in browser but not in app
- **Solution**: Network security config missing
- Check: `network_security_config.xml` exists and is referenced

## 🔧 Quick Fix Checklist

- [ ] Backend is running (`npm run start:dev`)
- [ ] Backend accessible at `http://localhost:3001` in browser
- [ ] Using correct IP (`10.0.2.2` for emulator, your IP for physical device)
- [ ] Firewall allows connections on port 3001
- [ ] Network security config is set up
- [ ] Device/emulator and computer on same network (for physical device)
- [ ] Rebuilt app after changing API URL

## 📱 Testing Steps

1. **Start backend:**
   ```bash
   cd backend
   npm run start:dev
   ```

2. **Verify backend:**
   - Open browser: `http://localhost:3001`
   - Should see "Cannot GET /" (means it's working)

3. **Update API URL if needed:**
   - Edit: `app/src/main/java/com/v2v/app/data/api/ApiClient.kt`
   - Use `10.0.2.2` for emulator
   - Use your computer's IP for physical device

4. **Rebuild app:**
   - Build → Clean Project
   - Build → Rebuild Project

5. **Run app and test connection**

## 🆘 Still Not Working?

1. **Check Logcat** in Android Studio for detailed error messages
2. **Check backend logs** for incoming requests
3. **Try using physical device** instead of emulator (or vice versa)
4. **Verify database is connected** (backend should show "✅ Database connected")


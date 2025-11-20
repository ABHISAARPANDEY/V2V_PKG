# Verify Your IP Address

## Current IP in ApiClient.kt

Your current IP is set to: **`192.168.105.232`**

## ✅ How to Verify It's Correct

### Windows:
1. Open **Command Prompt** (CMD)
2. Type: `ipconfig`
3. Look for **"IPv4 Address"** under your active network adapter
4. It should match: `192.168.105.232`

### Mac/Linux:
1. Open **Terminal**
2. Type: `ifconfig` or `ip addr`
3. Look for your network interface (usually `en0` or `wlan0`)
4. Find the **inet** address - should match: `192.168.105.232`

## 🔧 If IP is Different

If your actual IP is different, update `ApiClient.kt` line 13:

```kotlin
private const val BASE_URL = "http://YOUR_ACTUAL_IP:3001/"
```

Then:
1. **Rebuild app**: Build → Clean Project → Rebuild Project
2. **Run again**

## ✅ Test Connection

1. **Make sure backend is running:**
   ```bash
   cd backend
   npm run start:dev
   ```

2. **Test from phone browser:**
   - Open browser on your phone
   - Go to: `http://192.168.105.232:3001`
   - If you see "Cannot GET /" → Connection works! ✅
   - If connection fails → Check IP or firewall

3. **Check Windows Firewall:**
   - Allow Node.js through firewall
   - Or temporarily disable to test

## 📋 Requirements

- ✅ Phone and computer on **same WiFi network**
- ✅ Backend running on port 3001
- ✅ IP address matches your computer's IP
- ✅ Firewall allows connections on port 3001


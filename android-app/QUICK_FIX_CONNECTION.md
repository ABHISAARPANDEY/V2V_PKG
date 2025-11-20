# Quick Fix: Connection Failed

## 🚨 "Failed to connect to /10.0.2.2:3001"

### Immediate Steps:

1. **Check Backend is Running:**
   ```bash
   cd backend
   npm run start:dev
   ```
   Should show: `🚀 Backend API running on: http://localhost:3001`

2. **Test Backend in Browser:**
   - Open: `http://localhost:3001`
   - If you see "Cannot GET /" → Backend is working ✅
   - If connection fails → Backend not running ❌

3. **For Physical Device (Not Emulator):**
   - Find your IP: `ipconfig` (Windows) or `ifconfig` (Mac/Linux)
   - Update `ApiClient.kt` line 10:
     ```kotlin
     private const val BASE_URL = "http://YOUR_IP:3001/"
     ```
   - Example: `http://192.168.1.100:3001/`

4. **Rebuild App:**
   - Build → Clean Project
   - Build → Rebuild Project
   - Run again

### Still Not Working?

- Check Windows Firewall allows Node.js
- Make sure device and computer on same WiFi (for physical device)
- See `TROUBLESHOOTING_CONNECTION.md` for detailed steps


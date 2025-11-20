# Quick Start Guide - Android Studio

## 🚀 Fast Setup (5 minutes)

### 1. Open Project
- Launch Android Studio
- **File → Open** → Select `android-app` folder
- Wait for Gradle sync (first time: 5-10 minutes)

### 2. Configure API URL
**For Physical Device:**
- Open: `app/src/main/java/com/v2v/app/data/api/ApiClient.kt`
- Line 10: Change `http://10.0.2.2:3001/` to `http://YOUR_COMPUTER_IP:3001/`
- Find your IP: Windows (`ipconfig`) or Mac/Linux (`ifconfig`)

**For Emulator:**
- Keep default: `http://10.0.2.2:3001/` (already correct)

### 3. Connect Device
- **Physical**: Enable USB Debugging → Connect via USB
- **Emulator**: Tools → Device Manager → Create/Start emulator

### 4. Run
- Click **▶️ Run** button (or Shift+F10)
- Grant all permissions when prompted

### 5. Test
1. Register account
2. Register vehicle
3. BLE starts automatically
4. Check status shows "Active"

## ⚠️ Common Issues

**Gradle Sync Failed?**
→ File → Invalidate Caches → Invalidate and Restart

**Can't Connect to Backend?**
→ Check backend is running on port 3001
→ Check firewall allows connections
→ Verify IP address is correct

**BLE Not Working?**
→ Use physical device (emulators have limited BLE support)
→ Grant all Bluetooth and Location permissions

**Build Errors?**
→ Build → Clean Project → Rebuild Project

## 📱 Testing Checklist

- [ ] App launches without crashing
- [ ] Can register new user
- [ ] Can login
- [ ] Can register vehicle
- [ ] Broadcasting shows "Active"
- [ ] Scanning shows "Active"
- [ ] Detections appear (if unregistered devices nearby)

## 🔗 Full Documentation

See `ANDROID_STUDIO_SETUP.md` for detailed instructions.


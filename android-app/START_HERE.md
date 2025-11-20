# 🚀 START HERE - Android Studio Setup

## Quick Steps (5 minutes)

### 1️⃣ Open Project
1. Open **Android Studio**
2. Click **"Open"** or **File → Open**
3. Navigate to and select the **`android-app`** folder
4. Click **OK**
5. **Wait for Gradle sync** (5-10 minutes first time) ⏳

### 2️⃣ Configure API URL

**IMPORTANT:** Update the backend URL based on your device:

#### For Physical Android Device:
1. Find your computer's IP address:
   - **Windows**: Open CMD → type `ipconfig` → find "IPv4 Address"
   - **Mac/Linux**: Open Terminal → type `ifconfig` → find your network IP
   - Example: `192.168.1.100`

2. Edit the file:
   - Open: `app/src/main/java/com/v2v/app/data/api/ApiClient.kt`
   - Line 10: Change `http://10.0.2.2:3001/` 
   - To: `http://YOUR_IP:3001/` (e.g., `http://192.168.1.100:3001/`)

#### For Android Emulator:
- Keep the default: `http://10.0.2.2:3001/` (already correct ✅)

### 3️⃣ Connect Device

**Physical Device:**
1. Enable Developer Options:
   - Settings → About Phone → Tap "Build Number" 7 times
   - Settings → Developer Options → Enable "USB Debugging"
2. Connect via USB cable
3. Allow USB debugging when prompted on phone

**OR Emulator:**
1. Tools → Device Manager
2. Create/Start an emulator (API 34 recommended)

### 4️⃣ Run the App

1. Make sure your device/emulator is selected in the dropdown (top toolbar)
2. Click the **green ▶️ Run button** (or press `Shift + F10`)
3. Wait for build to complete (2-5 minutes first time)
4. App will install and launch automatically

### 5️⃣ Grant Permissions

When app launches, **grant ALL permissions**:
- ✅ Location
- ✅ Bluetooth
- ✅ Nearby Devices (Android 12+)

## ✅ Testing Checklist

After running:
- [ ] App launches without crashing
- [ ] Can see login/register screen
- [ ] Can register a new account
- [ ] Can login
- [ ] Can register a vehicle
- [ ] Status shows "Broadcasting: Active"
- [ ] Status shows "Scanning: Active"

## ⚠️ Troubleshooting

**Gradle Sync Failed?**
→ File → Invalidate Caches → Invalidate and Restart

**Can't Connect to Backend?**
→ Make sure backend is running: `http://localhost:3001`
→ Check your IP address is correct
→ Check Windows Firewall allows connections

**BLE Not Working?**
→ Use a **physical device** (emulators have limited BLE)
→ Grant all permissions
→ Enable Bluetooth on device

**Build Errors?**
→ Build → Clean Project
→ Build → Rebuild Project

## 📚 More Help

- **Detailed Guide**: See `ANDROID_STUDIO_SETUP.md`
- **Quick Reference**: See `QUICK_START.md`
- **Backend Setup**: See `backend/README.md`
- **Admin Panel**: See `admin-web/README.md`

## 🎯 What to Do Next

1. **Start Backend**: 
   ```bash
   cd backend
   npm install
   npm run prisma:migrate
   npm run start:dev
   ```

2. **Start Admin Panel** (optional):
   ```bash
   cd admin-web
   npm install
   npm run dev
   ```

3. **Run Android App** (follow steps above)

4. **Test the System**:
   - Register user in app
   - Register vehicle
   - Check admin panel for detections
   - Test with multiple devices

---

**Need help?** Check the Logcat window in Android Studio for error messages!


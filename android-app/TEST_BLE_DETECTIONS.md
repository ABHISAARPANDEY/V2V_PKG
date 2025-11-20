# Testing BLE and Unregistered Detections

## ✅ Does the App Use Bluetooth?

**Yes!** The app uses Bluetooth Low Energy (BLE) for:
1. **Advertising** - Broadcasts your vehicle's UUID to nearby devices
2. **Scanning** - Detects other BLE devices nearby
3. **Detection** - Identifies unregistered vehicles

## 🔍 How BLE Works in This App

### When You Register a Vehicle:
1. Backend generates a unique `vehicleUuid` for your vehicle
2. App stores this UUID locally
3. App automatically starts:
   - **BLE Advertising** - Broadcasts your vehicle UUID
   - **BLE Scanning** - Scans for other devices
   - **Location Tracking** - Gets GPS location (optional)

### Detection Logic:
- App scans for BLE devices with the V2V service UUID
- If a device is found:
  - **Has matching UUID format** → Registered vehicle (ignored)
  - **No matching UUID** → Unregistered vehicle (detected!)
- After 3+ detections within 3 seconds → Confirmed detection
- Detection is sent to backend with location

## 📱 How to Test Unregistered Detections

### Option 1: Use Two Devices (Recommended)

**Device 1 (Registered Vehicle):**
1. Register a user account
2. Register a vehicle
3. App will automatically start BLE advertising and scanning
4. Status should show "Broadcasting: Active" and "Scanning: Active"

**Device 2 (Unregistered - Any BLE Device):**
- Use another phone with Bluetooth enabled
- Or use any BLE device (smartwatch, fitness tracker, etc.)
- The registered device should detect it as "unregistered"

### Option 2: Use One Device + External BLE Device

1. Register and start the app on your phone
2. Enable Bluetooth on another device nearby:
   - Another phone
   - Smartwatch
   - Fitness tracker
   - Any BLE-enabled device
3. Your app should detect it as unregistered

### Option 3: Test with BLE Scanner App

1. Install a BLE scanner app on another phone (e.g., "nRF Connect" or "BLE Scanner")
2. Start broadcasting from the scanner app
3. Your V2V app should detect it

## ⚠️ Important Notes

### BLE Requirements:
- **Physical Device Required** - Emulators don't support BLE properly
- **Bluetooth Must Be Enabled** - On both devices
- **Location Permission** - Required for BLE scanning on Android 6.0+
- **Same Area** - Devices must be within BLE range (~10-30 meters)

### Detection Threshold:
- Requires **3+ detections** within **3 seconds** to confirm
- This prevents false positives from brief encounters

## 🔧 Check if BLE is Working

### In the App:
1. After registering a vehicle, check the **Status** section:
   - "Broadcasting: Active" ✅
   - "Scanning: Active" ✅
2. If both show "Active", BLE is working!

### In Logcat:
Look for:
- `"BLE advertising started successfully"`
- `"BLE scanning started"`
- `"Confirmed unregistered detection"`

## 📊 View Detections

### In the App:
- Detections appear in the **"Unregistered Detections"** section
- Shows: Identifier, time, RSSI (signal strength)

### In Admin Panel:
1. Login: `admin@v2v.com` / `admin123`
2. Go to Dashboard
3. See all detections with details
4. View on map (if location available)

## 🧪 Step-by-Step Testing Guide

### Setup:
1. ✅ Backend running
2. ✅ Database seeded (admin user exists)
3. ✅ App installed on physical device
4. ✅ User registered and logged in
5. ✅ Vehicle registered

### Test:
1. **Check Status** - Should show Broadcasting and Scanning as Active
2. **Enable Bluetooth** on another device nearby
3. **Wait 5-10 seconds** - App scans periodically
4. **Check Detections** - Should appear in "Unregistered Detections" section
5. **Check Admin Panel** - Should see detection in dashboard

## 🆘 Troubleshooting

### "Broadcasting: Inactive" or "Scanning: Inactive"
- Check Bluetooth is enabled on device
- Grant all permissions (Bluetooth, Location)
- Restart the app
- Check Logcat for errors

### No Detections Appearing
- Make sure another BLE device is nearby and Bluetooth is on
- Wait longer (scanning happens periodically)
- Check Logcat for scan results
- Verify detection threshold (needs 3+ detections)

### Detections Not Sending to Backend
- Check backend is running
- Check network connection
- Check Logcat for API errors
- Verify device is registered with backend

## 📝 What Happens When Detection Occurs

1. **App detects** unregistered BLE device
2. **Confirms** after 3+ sightings (within 3 seconds)
3. **Gets location** (if permission granted)
4. **Sends to backend** via API
5. **Shows in app** - "Unregistered Detections" section
6. **Shows in admin panel** - Dashboard and detection list

Ready to test? Say "start" when you're ready!


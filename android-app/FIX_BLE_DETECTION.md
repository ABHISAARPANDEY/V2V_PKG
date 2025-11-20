# Fixed: BLE Not Detecting Unregistered Devices

## Problem
Device A (with app) was not detecting Device B (without app) even though both had Bluetooth enabled.

## Root Cause
The BLE scanner was **filtering** to only scan for devices with the V2V service UUID. Since Device B doesn't have the app, it doesn't broadcast that UUID, so it was being filtered out.

## ✅ Solution Applied

Changed the scanner to:
1. **Scan ALL BLE devices** (removed the service UUID filter)
2. **Check each device** to see if it has the V2V service UUID
3. **Classify devices**:
   - Has V2V service + registered UUID → Registered vehicle (ignore)
   - Has V2V service + unregistered UUID → Unregistered V2V device (detect)
   - No V2V service → Unregistered regular BLE device (detect)

## What Changed

**Before:**
- Only scanned devices with V2V service UUID
- Missed devices without the app

**After:**
- Scans all BLE devices
- Identifies which are registered vs unregistered
- Detects any BLE device without the V2V app

## 🔧 Next Steps

1. **Rebuild the app:**
   - Build → Clean Project
   - Build → Rebuild Project

2. **Reinstall on Device A:**
   - Uninstall old version
   - Install new build

3. **Test again:**
   - Device A: App installed, vehicle registered
   - Device B: No app, just Bluetooth enabled
   - Device A should now detect Device B!

## 📱 Testing Steps

1. **Device A (with app):**
   - Open app
   - Login
   - Register vehicle (if not already)
   - Check Status: Should show "Broadcasting: Active" and "Scanning: Active"

2. **Device B (without app):**
   - Just enable Bluetooth
   - Keep it nearby Device A (within 10-30 meters)

3. **Wait 5-10 seconds:**
   - Device A scans periodically
   - Should detect Device B as unregistered

4. **Check Detections:**
   - In Device A app: "Unregistered Detections" section
   - Should show Device B's Bluetooth address
   - After 3+ detections, it will be confirmed

5. **Check Admin Panel:**
   - Login: `admin@v2v.com` / `admin123`
   - Dashboard should show the detection

## ⚠️ Important Notes

- **Detection requires 3+ sightings** within 3 seconds to confirm
- Devices must be **within BLE range** (~10-30 meters)
- **Bluetooth must be enabled** on both devices
- **Location permission** required on Device A (for BLE scanning)

## 🆘 Still Not Detecting?

1. **Check Logcat** in Android Studio:
   - Look for "BLE scanning started"
   - Look for "onScanResult" messages
   - Should see Device B's address being scanned

2. **Check Status in App:**
   - Both Broadcasting and Scanning should be "Active"
   - If not, check permissions

3. **Move devices closer:**
   - BLE range is limited
   - Try within 5-10 meters

4. **Wait longer:**
   - Scanning happens periodically
   - May take 10-30 seconds to detect

The fix should now allow Device A to detect any BLE device, including Device B without the app!


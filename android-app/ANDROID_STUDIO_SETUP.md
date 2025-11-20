# Android Studio Setup Guide for V2V App

## Prerequisites

1. **Android Studio** - Download and install the latest version (Hedgehog or later)
   - Download from: https://developer.android.com/studio
   - Make sure to install Android SDK 34 (Android 14)

2. **Physical Android Device** (Recommended)
   - BLE (Bluetooth Low Energy) requires physical hardware
   - Device should run Android 8.0 (API 26) or higher
   - Enable Developer Options and USB Debugging on your device

   OR

   **Android Emulator** (Limited BLE support)
   - Some emulators support BLE, but physical device is recommended

3. **Backend API Running**
   - Make sure the backend is running on `http://localhost:3001`
   - Or update the API URL in the app (see Configuration section)

## Step-by-Step Setup Instructions

### 1. Open Project in Android Studio

1. Launch **Android Studio**
2. Click **"Open"** or **"File → Open"**
3. Navigate to the `android-app` folder in this project
4. Select the `android-app` folder and click **"OK"**
5. Android Studio will start indexing and syncing Gradle files (this may take a few minutes)

### 2. Wait for Gradle Sync

- Android Studio will automatically sync Gradle dependencies
- You'll see a progress bar at the bottom
- Wait until it completes (may take 5-10 minutes on first open)
- If you see errors, click **"Sync Now"** in the notification bar

### 3. Install Required SDK Components

1. Go to **Tools → SDK Manager** (or click the SDK Manager icon in toolbar)
2. In the **SDK Platforms** tab:
   - Check **Android 14.0 (API 34)** - should be installed
   - Check **Android 8.0 (API 26)** or higher
3. In the **SDK Tools** tab, ensure these are checked:
   - ✅ Android SDK Build-Tools
   - ✅ Android SDK Platform-Tools
   - ✅ Android SDK Command-line Tools
   - ✅ Google Play services
4. Click **"Apply"** and wait for installation

### 4. Configure API URL

**Important:** Update the backend API URL based on your setup:

#### For Android Emulator:
The default URL `http://10.0.2.2:3001/` should work (this is the emulator's alias for localhost).

#### For Physical Device:
You need to use your computer's IP address:

1. Find your computer's IP address:
   - **Windows**: Open Command Prompt, type `ipconfig`, look for "IPv4 Address"
   - **Mac/Linux**: Open Terminal, type `ifconfig` or `ip addr`, look for your network interface IP
   - Example: `192.168.1.100`

2. Update the API URL:
   - Open: `app/src/main/java/com/v2v/app/data/api/ApiClient.kt`
   - Find line 10: `private const val BASE_URL = "http://10.0.2.2:3001/"`
   - Change to: `private const val BASE_URL = "http://YOUR_IP_ADDRESS:3001/"`
   - Example: `private const val BASE_URL = "http://192.168.1.100:3001/"`

3. **Important:** Make sure your backend allows connections from your device:
   - In `backend/src/main.ts`, check CORS settings
   - Ensure your firewall allows connections on port 3001

### 5. Connect Your Device

#### Physical Device:
1. Enable **Developer Options** on your Android device:
   - Go to **Settings → About Phone**
   - Tap **Build Number** 7 times
   - Go back to **Settings → Developer Options**
   - Enable **USB Debugging**

2. Connect device via USB
3. On your device, when prompted, allow USB debugging
4. In Android Studio, you should see your device in the device dropdown (top toolbar)

#### Emulator:
1. Go to **Tools → Device Manager**
2. Click **"Create Device"**
3. Select a device (e.g., Pixel 5)
4. Select a system image (API 34 recommended)
5. Click **"Finish"** and wait for emulator to start

### 6. Build and Run

1. Make sure your device/emulator is selected in the device dropdown (top toolbar)
2. Click the **green "Run"** button (▶️) or press **Shift + F10**
3. Android Studio will:
   - Build the app (first build may take 2-5 minutes)
   - Install the app on your device
   - Launch the app

### 7. Grant Permissions

When the app launches, it will request permissions:
- **Location** - Required for BLE scanning
- **Bluetooth** - Required for BLE advertising and scanning
- **Nearby Devices** (Android 12+) - Required for BLE

**Grant all permissions** when prompted.

## Troubleshooting

### Gradle Sync Failed
- **Solution**: 
  - Go to **File → Invalidate Caches → Invalidate and Restart**
  - Or check your internet connection (Gradle downloads dependencies)

### "SDK location not found"
- **Solution**: 
  - Go to **File → Project Structure → SDK Location**
  - Set Android SDK location (usually `C:\Users\YourName\AppData\Local\Android\Sdk` on Windows)

### Build Errors
- **Solution**:
  - Click **"Sync Project with Gradle Files"** (elephant icon in toolbar)
  - Clean project: **Build → Clean Project**
  - Rebuild: **Build → Rebuild Project**

### App Crashes on Launch
- **Check Logcat** (bottom panel in Android Studio)
- Look for red error messages
- Common issues:
  - Missing permissions
  - API URL not accessible
  - Backend not running

### BLE Not Working
- **Physical Device**: Make sure Bluetooth is enabled
- **Emulator**: Some emulators don't support BLE properly - use a physical device
- **Permissions**: Ensure all Bluetooth and Location permissions are granted

### Cannot Connect to Backend
- **Check backend is running**: Open `http://localhost:3001` in browser
- **Check IP address**: Make sure you're using the correct IP for physical devices
- **Check firewall**: Windows Firewall may block connections
- **Check CORS**: Backend must allow your device's origin

### "Execution failed for task ':app:mergeDebugResources'"
- **Solution**: 
  - Go to **File → Invalidate Caches → Invalidate and Restart**
  - Delete `.gradle` folder in project root
  - Rebuild project

## Project Structure Overview

```
android-app/
├── app/
│   ├── src/main/
│   │   ├── java/com/v2v/app/
│   │   │   ├── data/          # API clients, models, repositories
│   │   │   ├── ble/           # BLE advertising and scanning
│   │   │   ├── viewmodel/     # ViewModels for UI state
│   │   │   └── ui/            # Compose screens and navigation
│   │   ├── res/               # Resources (strings, themes)
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts       # App dependencies
├── build.gradle.kts           # Project-level config
└── settings.gradle.kts        # Project settings
```

## Testing the App

1. **Register a new user**:
   - Open app → Click "Register"
   - Fill in name, email, password
   - Submit

2. **Register a vehicle**:
   - After login, you'll see vehicle registration form
   - Fill in vehicle details (all optional)
   - Click "Register Vehicle"

3. **Start BLE services**:
   - Once vehicle is registered, BLE advertising and scanning start automatically
   - You should see "Broadcasting: Active" and "Scanning: Active"

4. **Test detection**:
   - Use another device with the app (or any BLE device)
   - The app should detect unregistered devices
   - Detections appear in the "Unregistered Detections" section

## Next Steps

- Check the admin web panel to see detections: `http://localhost:3000`
- Review detection details and update status
- Test with multiple devices to see detection system in action

## Need Help?

- Check Android Studio's **Logcat** for detailed error messages
- Review the main `README.md` for system overview
- Check backend logs for API errors
- Ensure all three components (backend, admin web, Android app) are running


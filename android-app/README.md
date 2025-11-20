# V2V Android App

Android mobile application for the V2V Bluetooth Vehicle Registration & Unregistered Detection System.

## Tech Stack

- **Kotlin** - Programming language
- **Jetpack Compose** - Modern UI toolkit
- **Android BLE APIs** - Bluetooth Low Energy advertising and scanning
- **Retrofit** - HTTP client for API calls
- **Coroutines & Flow** - Asynchronous programming
- **ViewModel** - UI-related data holder
- **Navigation Compose** - Navigation between screens

## Features

- User authentication (register/login)
- Vehicle registration
- Bluetooth LE advertising (broadcast vehicle UUID)
- Bluetooth LE scanning (detect nearby devices)
- Unregistered vehicle detection
- Location tracking (optional, for geotagging detections)
- Real-time detection reporting to backend

## Setup

### Prerequisites

- Android Studio Hedgehog or later
- Android SDK 26+ (Android 8.0+)
- Physical Android device with Bluetooth LE support (recommended) or emulator

### Configuration

1. Open the project in Android Studio
2. Update the API base URL in `ApiClient.kt`:
   - For emulator: `http://10.0.2.2:3001/` (default)
   - For physical device: Use your computer's IP address, e.g., `http://192.168.1.100:3001/`

3. Build and run the app

### Permissions

The app requires the following permissions:
- Bluetooth (scan, advertise, connect)
- Location (required for BLE scanning on Android 6.0+)

These are requested at runtime.

## Project Structure

```
app/src/main/java/com/v2v/app/
├── data/
│   ├── api/          # API client and services
│   ├── model/        # Data models
│   └── repository/   # Data repositories
├── ble/              # BLE advertising and scanning
├── viewmodel/        # ViewModels for UI state
└── ui/
    ├── screens/      # Compose screens
    ├── navigation/   # Navigation setup
    └── theme/        # App theme
```

## BLE Implementation

- **BleAdvertiser**: Broadcasts vehicle UUID via BLE advertising
- **BleScanner**: Scans for nearby BLE devices
- **DetectionManager**: Manages unregistered detection logic (confirmation threshold, deduplication)

## Notes

- The app uses a custom BLE service UUID for V2V communication
- Vehicle UUID is embedded in BLE service data
- Unregistered detections require 3+ sightings within 3 seconds to be confirmed
- Location is optional but recommended for better detection tracking


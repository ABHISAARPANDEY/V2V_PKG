# Fixed: Network Security Policy Error

## Problem
Android 9+ (API 28+) blocks cleartext HTTP traffic by default for security. When trying to connect to `http://10.0.2.2:3001` (emulator localhost), you get:

**Error:**
```
Communication to 10.0.2.2 not permitted by the network security policy
```

## Solution Applied
Created a network security configuration file that allows cleartext HTTP traffic for:
- `localhost`
- `10.0.2.2` (Android emulator's localhost alias)
- `127.0.0.1`
- Local network IPs (for physical devices)

## What Changed

1. **Created** `app/src/main/res/xml/network_security_config.xml`:
   - Allows cleartext HTTP for localhost and emulator
   - Allows cleartext for local network (for physical devices)

2. **Updated** `AndroidManifest.xml`:
   - Added `android:networkSecurityConfig="@xml/network_security_config"` to the `<application>` tag

## Important Notes

⚠️ **Security Warning**: This configuration allows cleartext HTTP traffic, which is **only safe for development**. 

For production:
- Use HTTPS instead of HTTP
- Remove or restrict the network security config
- Use proper SSL certificates

## Next Steps

1. **Rebuild the app**:
   - Build → Clean Project
   - Build → Rebuild Project

2. **Run the app** - the network security error should be resolved

3. **For Physical Devices**: 
   - Update `ApiClient.kt` to use your computer's IP address instead of `10.0.2.2`
   - Example: `http://192.168.1.100:3001/`
   - The network security config will allow this

The app should now be able to connect to the backend API!


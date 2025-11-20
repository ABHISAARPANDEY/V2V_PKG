# Fixed: Experimental Material API Warning

## Problem
Material3's `TopAppBar` is marked as experimental and requires an opt-in annotation.

**Error:**
```
This material API is experimental and is likely to change or to be removed in the future.
```

## Solution Applied
Added the `@OptIn(ExperimentalMaterial3Api::class)` annotation to the `MainScreen` composable function.

## What Changed
1. Added import: `import androidx.compose.material3.ExperimentalMaterial3Api`
2. Updated `@OptIn` annotation to include both:
   - `ExperimentalPermissionsApi::class` (already present)
   - `ExperimentalMaterial3Api::class` (newly added)

Changed from:
```kotlin
@OptIn(ExperimentalPermissionsApi::class)
```

To:
```kotlin
@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
```

## Next Steps
1. **Sync Gradle** (if needed)
2. **Clean and Rebuild**:
   - Build → Clean Project
   - Build → Rebuild Project
3. **Run the app** - the compilation error should be resolved

The app should now compile successfully without experimental API warnings!


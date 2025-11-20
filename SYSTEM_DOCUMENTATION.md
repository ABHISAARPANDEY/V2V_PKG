# V2V System - Complete Documentation

## Table of Contents
1. [System Overview](#system-overview)
2. [System Architecture](#system-architecture)
3. [Database Schema](#database-schema)
4. [Android App - User Workflow](#android-app---user-workflow)
5. [Admin Web Panel - User Workflow](#admin-web-panel---user-workflow)
6. [Technical Components](#technical-components)
7. [API Endpoints](#api-endpoints)

---

## System Overview

**V2V (Vehicle-to-Vehicle)** is a Bluetooth Low Energy (BLE) based system designed to help identify and track unregistered vehicles. The system consists of three main components:

1. **Android Mobile App** - Used by vehicle owners to register their vehicles and detect unregistered vehicles nearby
2. **Admin Web Panel** - Used by administrators to review and manage vehicle detections
3. **Backend API** - The central server that stores all data and coordinates between the mobile app and admin panel

### How It Works (Simple Explanation)

Imagine you're driving your car with the V2V app running on your phone. The app does two things:
- **Broadcasts** your vehicle's identity using Bluetooth (like a digital license plate)
- **Scans** for other nearby Bluetooth devices

When the app finds a Bluetooth device that doesn't belong to a registered vehicle, it reports this to the server. Administrators can then review these reports through the web panel to identify potentially unregistered vehicles.

---

## System Architecture

### High-Level Architecture Diagram

```
┌─────────────────┐         ┌─────────────────┐
│   Android App   │         │  Admin Web App  │
│  (Kotlin/Compose)│         │   (Next.js)     │
└────────┬────────┘         └────────┬────────┘
         │                            │
         │  HTTP/REST API             │
         │  (JWT Authentication)      │
         │                            │
         └────────────┬───────────────┘
                      │
              ┌───────▼────────┐
              │  Backend API    │
              │   (NestJS)      │
              └───────┬─────────┘
                      │
              ┌───────▼────────┐
              │   PostgreSQL    │
              │    Database     │
              └─────────────────┘
```

### Component Details

#### 1. Android Mobile App
- **Technology**: Kotlin with Jetpack Compose
- **Purpose**: Vehicle registration and BLE detection
- **Key Features**:
  - User authentication (login/register)
  - Vehicle registration
  - BLE advertising (broadcasts vehicle UUID)
  - BLE scanning (detects nearby devices)
  - Automatic detection reporting

#### 2. Admin Web Panel
- **Technology**: Next.js (React) with TypeScript
- **Purpose**: Administrative oversight and detection management
- **Key Features**:
  - Admin login
  - Dashboard with statistics
  - Detection list and filtering
  - Detection detail view
  - Status management (NEW, UNDER_REVIEW, CONFIRMED, IGNORED)

#### 3. Backend API
- **Technology**: NestJS (Node.js) with TypeScript
- **Purpose**: Central data management and API server
- **Key Features**:
  - RESTful API
  - JWT authentication
  - PostgreSQL database with Prisma ORM
  - User, Vehicle, Device, and Detection management
  - Admin endpoints

#### 4. Database
- **Technology**: PostgreSQL
- **Purpose**: Persistent data storage
- **Managed By**: Prisma ORM

---

## Database Schema

### Entity Relationship Diagram

```
┌─────────────┐
│    User     │
│─────────────│
│ id (PK)     │
│ name        │
│ email       │
│ passwordHash│
│ role        │
└──────┬──────┘
       │
       │ 1:N
       │
       ├──────────────────┬──────────────────┐
       │                  │                  │
       ▼                  ▼                  ▼
┌─────────────┐   ┌─────────────┐   ┌─────────────┐
│  Vehicle    │   │   Device    │   │AdminAction  │
│─────────────│   │─────────────│   │    Log      │
│ id (PK)     │   │ id (PK)     │   │─────────────│
│ userId (FK) │   │ userId (FK) │   │ id (PK)     │
│ registration│   │ vehicleId   │   │ adminUserId │
│ make        │   │ platform    │   │ detectionId │
│ model       │   │ identifier  │   │ action      │
│ year        │   │ lastSeenAt  │   │ notes       │
│ vehicleUuid │   └──────┬──────┘   └─────────────┘
└──────┬──────┘          │
       │                 │
       │ 1:N             │ 1:N
       │                 │
       ▼                 ▼
┌─────────────────────────────┐
│ UnregisteredDetection       │
│─────────────────────────────│
│ id (PK)                     │
│ detectedByVehicleId (FK)     │
│ detectedByDeviceId (FK)     │
│ unregisteredIdentifier      │
│ rssi                        │
│ latitude                    │
│ longitude                   │
│ detectedAt                  │
│ status                      │
└─────────────────────────────┘
```

### Detailed Schema

#### User Table
Stores user account information.

| Field | Type | Description |
|-------|------|-------------|
| id | UUID | Unique identifier |
| name | String | User's full name |
| email | String | Email address (unique) |
| passwordHash | String | Hashed password |
| role | Enum | USER or ADMIN |
| createdAt | DateTime | Account creation timestamp |
| updatedAt | DateTime | Last update timestamp |

**Relationships**:
- One User can have many Vehicles
- One User can have many Devices
- One User (admin) can have many AdminActionLogs

#### Vehicle Table
Stores registered vehicle information.

| Field | Type | Description |
|-------|------|-------------|
| id | UUID | Unique identifier |
| userId | UUID (FK) | Owner of the vehicle |
| registrationNumber | String? | Vehicle registration/license plate |
| make | String? | Vehicle manufacturer |
| model | String? | Vehicle model |
| year | Integer? | Manufacturing year |
| vehicleUuid | String | Unique UUID for BLE broadcasting (unique) |
| createdAt | DateTime | Registration timestamp |
| updatedAt | DateTime | Last update timestamp |

**Relationships**:
- Belongs to one User
- Can have many Devices
- Can detect many UnregisteredDetections

#### Device Table
Stores physical device (phone) information.

| Field | Type | Description |
|-------|------|-------------|
| id | UUID | Unique identifier |
| userId | UUID (FK) | Owner of the device |
| vehicleId | UUID (FK)? | Associated vehicle (optional) |
| devicePlatform | Enum | ANDROID or IOS |
| deviceIdentifier | String | Unique device identifier (unique) |
| lastSeenAt | DateTime? | Last heartbeat timestamp |
| createdAt | DateTime | Registration timestamp |
| updatedAt | DateTime | Last update timestamp |

**Relationships**:
- Belongs to one User
- Can belong to one Vehicle (optional)
- Can detect many UnregisteredDetections

#### UnregisteredDetection Table
Stores reports of detected unregistered vehicles.

| Field | Type | Description |
|-------|------|-------------|
| id | UUID | Unique identifier |
| detectedByVehicleId | UUID (FK) | Vehicle that made the detection |
| detectedByDeviceId | UUID (FK) | Device that made the detection |
| unregisteredIdentifier | String | Identifier of unregistered device (indexed) |
| rssi | Integer? | Signal strength |
| latitude | Float? | GPS latitude |
| longitude | Float? | GPS longitude |
| detectedAt | DateTime | Detection timestamp (indexed) |
| status | Enum | NEW, UNDER_REVIEW, CONFIRMED, IGNORED (indexed) |
| createdAt | DateTime | Record creation timestamp |
| updatedAt | DateTime | Last update timestamp |

**Relationships**:
- Belongs to one Vehicle (detector)
- Belongs to one Device (detector)
- Can have many AdminActionLogs

**Status Values**:
- **NEW**: Just detected, not yet reviewed
- **UNDER_REVIEW**: Admin is investigating
- **CONFIRMED**: Confirmed as unregistered vehicle
- **IGNORED**: False positive or not relevant

#### AdminActionLog Table
Stores administrative actions on detections.

| Field | Type | Description |
|-------|------|-------------|
| id | UUID | Unique identifier |
| adminUserId | UUID (FK) | Admin who performed the action |
| detectionId | UUID (FK) | Detection being acted upon |
| action | String | Description of the action |
| notes | String? | Optional notes |
| createdAt | DateTime | Action timestamp (indexed) |

**Relationships**:
- Belongs to one User (admin)
- Belongs to one UnregisteredDetection

### Enums

**UserRole**:
- `USER` - Regular vehicle owner
- `ADMIN` - Administrator with access to web panel

**DevicePlatform**:
- `ANDROID` - Android device
- `IOS` - iOS device (future support)

**DetectionStatus**:
- `NEW` - Newly detected
- `UNDER_REVIEW` - Being reviewed
- `CONFIRMED` - Confirmed unregistered
- `IGNORED` - Ignored/false positive

---

## Android App - User Workflow

### Initial Setup Flow

```
1. User opens app
   ↓
2. App checks if user is logged in
   ↓
3a. If NOT logged in → Login Screen
3b. If logged in → Main Screen
```

### Registration Flow

```
Login Screen
   ↓
User clicks "Register" or "Don't have account?"
   ↓
Register Screen
   ↓
User enters:
  - Name
  - Email
  - Password
   ↓
User clicks "Register"
   ↓
App sends registration request to backend
   ↓
Backend creates user account (role: USER)
   ↓
Backend returns JWT token
   ↓
App stores token
   ↓
Navigate to Main Screen
```

### Login Flow

```
Login Screen
   ↓
User enters:
  - Email
  - Password
   ↓
User clicks "Login"
   ↓
App sends login request to backend
   ↓
Backend validates credentials
   ↓
Backend returns JWT token + user info
   ↓
App stores token
   ↓
Navigate to Main Screen
```

### Main Screen - First Time User

```
Main Screen
   ↓
App checks if user has registered vehicle
   ↓
If NO vehicle registered:
   ↓
Vehicle Registration Section appears
   ↓
User enters:
  - Registration Number (optional)
  - Make (optional)
  - Model (optional)
  - Year (optional)
   ↓
User clicks "Register Vehicle"
   ↓
App generates unique Vehicle UUID
   ↓
App sends vehicle data to backend
   ↓
Backend creates vehicle record
   ↓
App stores vehicle info locally
   ↓
Vehicle Registration Section disappears
   ↓
Status Section appears
```

### Main Screen - Active User

```
Main Screen
   ↓
App loads user's vehicle from backend
   ↓
Status Section displays:
  - Vehicle registration number (if available)
  - Vehicle UUID
  - Advertising status (ON/OFF)
  - Scanning status (ON/OFF)
   ↓
App automatically:
  1. Registers device with backend (if not already registered)
  2. Starts BLE advertising (broadcasts vehicle UUID)
  3. Starts BLE scanning (detects nearby devices)
```

### BLE Advertising (Broadcasting)

```
User has registered vehicle
   ↓
App requests Bluetooth permissions
   ↓
User grants permissions
   ↓
App starts BLE advertising
   ↓
App embeds Vehicle UUID in BLE service data
   ↓
Service UUID: 0000FEAA-0000-1000-8000-00805F9B34FB
   ↓
Phone continuously broadcasts:
  - Service UUID
  - Vehicle UUID (in service data)
   ↓
Other V2V apps can detect this broadcast
```

**What this means**: Your phone is constantly broadcasting your vehicle's identity via Bluetooth, like a digital license plate that other V2V apps can "see."

### BLE Scanning (Detection)

```
App starts BLE scanning
   ↓
App scans for ALL nearby Bluetooth devices
   ↓
For each detected device:
   ↓
App checks:
  1. Does device have V2V service UUID?
     - YES → Extract Vehicle UUID from service data
     - NO → Treat as unregistered device
   ↓
2. If has V2V service:
     - Is Vehicle UUID in registered vehicles list?
       - YES → Skip (registered vehicle)
       - NO → Report as unregistered
   ↓
3. If no V2V service:
     - Report as unregistered device
   ↓
App collects detection data:
  - Device address (identifier)
  - Signal strength (RSSI)
  - GPS location (if available)
  - Timestamp
   ↓
App sends detection to backend
   ↓
Backend creates UnregisteredDetection record
   ↓
Detection appears in Detections Section
```

**What this means**: Your app is constantly looking for nearby Bluetooth devices. If it finds one that doesn't belong to a registered vehicle, it reports it to the server.

### Detection Reporting Flow

```
BLE Scanner detects unregistered device
   ↓
App collects:
  - Unregistered identifier (device address)
  - RSSI (signal strength)
  - GPS coordinates (if location permission granted)
  - Current timestamp
   ↓
App sends POST /detections/unregistered to backend
   ↓
Backend:
  1. Validates user is authenticated
  2. Finds user's current vehicle
  3. Finds user's current device
  4. Creates UnregisteredDetection record
  5. Sets status to NEW
   ↓
Backend returns detection ID
   ↓
App displays detection in Detections Section
```

### Detections Section

The app displays a list of recent unregistered detections made by the user's device:

- **Unregistered Identifier**: The Bluetooth address of the detected device
- **Detected At**: When the detection occurred
- **Location**: GPS coordinates (if available)
- **RSSI**: Signal strength (indicates proximity)

### Logout Flow

```
User clicks "Logout" button
   ↓
App:
  1. Stops BLE advertising
  2. Stops BLE scanning
  3. Clears stored authentication token
  4. Clears local vehicle data
   ↓
Navigate to Login Screen
```

---

## Admin Web Panel - User Workflow

### Login Flow

```
User opens admin panel (http://localhost:3000)
   ↓
Login Page appears
   ↓
User enters:
  - Email (must be ADMIN role)
  - Password
   ↓
User clicks "Sign in"
   ↓
App sends login request to backend
   ↓
Backend validates:
  1. Credentials are correct
  2. User role is ADMIN
   ↓
Backend returns JWT token
   ↓
App stores token in browser localStorage
   ↓
Navigate to Dashboard
```

**Important**: Only users with ADMIN role can access the web panel. Regular users (USER role) will see an "Access denied" error.

### Dashboard Overview

The dashboard displays key statistics:

**Overview Cards**:
- **Total Vehicles**: Number of registered vehicles in the system
- **Detections Today**: Number of detections made today
- **Detections This Week**: Number of detections made this week
- **New Detections**: Number of detections with NEW status

**Status Breakdown**:
- **New**: Detections not yet reviewed
- **Under Review**: Detections being investigated
- **Confirmed**: Confirmed unregistered vehicles
- **Ignored**: False positives or irrelevant detections

**Recent Detections Table**:
Shows the 10 most recent detections with:
- Identifier (truncated)
- Detection timestamp
- Location (GPS coordinates)
- Detected by (vehicle/user)
- Status badge
- "View" link to details

### Viewing Detection Details

```
Dashboard
   ↓
User clicks "View" on a detection
   ↓
Detection Detail Page loads
   ↓
Page displays:
  - Detection Information:
    * Unregistered Identifier (full)
    * Detection timestamp
    * RSSI (signal strength)
    * GPS coordinates (with Google Maps link)
  - Detected By:
    * Vehicle registration number
    * Vehicle make/model
    * User name and email
    * Device ID
  - Admin Actions:
    * History of all admin actions on this detection
  - Update Status Panel:
    * Current status
    * Status dropdown (NEW, UNDER_REVIEW, CONFIRMED, IGNORED)
    * Notes text area
    * Update button
```

### Updating Detection Status

```
Detection Detail Page
   ↓
Admin reviews detection information
   ↓
Admin selects new status from dropdown:
  - NEW → Keep as new
  - UNDER_REVIEW → Mark as being reviewed
  - CONFIRMED → Confirm as unregistered vehicle
  - IGNORED → Mark as false positive/irrelevant
   ↓
Admin optionally adds notes
   ↓
Admin clicks "Update Status"
   ↓
App sends PATCH /detections/unregistered/:id to backend
   ↓
Backend:
  1. Validates admin is authenticated
  2. Validates admin has ADMIN role
  3. Updates detection status
  4. Creates AdminActionLog entry
   ↓
Backend returns updated detection
   ↓
Page refreshes with new status
   ↓
Success message appears
```

### Admin Action Logging

Every time an admin updates a detection status, the system automatically:
1. Creates an AdminActionLog entry
2. Records:
   - Which admin performed the action
   - What action was taken
   - Any notes provided
   - Timestamp

This creates a complete audit trail of all administrative actions.

### Logout Flow

```
User clicks "Logout" button
   ↓
App:
  1. Removes authentication token from localStorage
  2. Clears any cached data
   ↓
Navigate to Login Page
```

---

## Technical Components

### Backend API Structure

```
backend/
├── src/
│   ├── main.ts                 # Application entry point
│   ├── app.module.ts           # Root module
│   ├── modules/
│   │   ├── auth/               # Authentication module
│   │   │   ├── auth.controller.ts
│   │   │   ├── auth.service.ts
│   │   │   ├── guards/         # JWT guards
│   │   │   ├── strategies/     # JWT strategy
│   │   │   └── dto/            # Data transfer objects
│   │   ├── users/              # User management
│   │   ├── vehicles/           # Vehicle management
│   │   ├── devices/            # Device management
│   │   ├── detections/         # Detection management
│   │   └── admin/              # Admin endpoints
│   ├── database/
│   │   ├── database.module.ts
│   │   └── prisma.service.ts   # Prisma client
│   └── common/
│       └── filters/            # Exception filters
└── prisma/
    ├── schema.prisma           # Database schema
    └── migrations/             # Database migrations
```

### Android App Structure

```
android-app/app/src/main/java/com/v2v/app/
├── MainActivity.kt              # App entry point
├── ui/
│   ├── navigation/
│   │   └── V2VNavigation.kt    # Navigation setup
│   ├── screens/
│   │   ├── LoginScreen.kt
│   │   ├── RegisterScreen.kt
│   │   └── MainScreen.kt
│   └── theme/                  # UI theme
├── viewmodel/                   # ViewModels (state management)
│   ├── AuthViewModel.kt
│   ├── MainViewModel.kt
│   └── VehicleViewModel.kt
├── data/
│   └── api/
│       ├── ApiClient.kt         # Retrofit client
│       ├── ApiService.kt        # API interface
│       └── TokenManager.kt      # Token storage
└── ble/
    ├── BleScanner.kt            # BLE scanning logic
    └── BleAdvertiser.kt         # BLE advertising logic
```

### Admin Web Panel Structure

```
admin-web/app/
├── page.tsx                     # Root (redirects to dashboard)
├── layout.tsx                   # Root layout
├── login/
│   └── page.tsx                 # Login page
├── dashboard/
│   └── page.tsx                 # Dashboard page
├── detections/
│   └── [id]/
│       └── page.tsx             # Detection detail page
└── lib/
    └── api.ts                   # API client functions
```

### BLE Implementation Details

**Service UUID**: `0000FEAA-0000-1000-8000-00805F9B34FB`

This is a custom UUID used to identify V2V app broadcasts. When a vehicle is registered, its unique Vehicle UUID is embedded in the BLE service data.

**Advertising Format**:
- Service UUID: `0000FEAA-0000-1000-8000-00805F9B34FB`
- Service Data: First 16 bytes of Vehicle UUID (hex encoded)

**Scanning Logic**:
1. Scan for ALL BLE devices (not filtered)
2. For each device:
   - Check if it has V2V service UUID
   - If yes: Extract Vehicle UUID from service data
     - Check if UUID is in registered vehicles list
     - If not registered → Report as unregistered
   - If no: Report as unregistered device

**Detection Threshold**:
- Currently, any detection is immediately reported
- Future enhancement: Require 3+ sightings within 3 seconds for confirmation

---

## API Endpoints

### Authentication Endpoints

#### POST /auth/register
Register a new user account.

**Request Body**:
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "securepassword"
}
```

**Response**:
```json
{
  "accessToken": "jwt_token_here",
  "user": {
    "id": "uuid",
    "name": "John Doe",
    "email": "john@example.com",
    "role": "USER"
  }
}
```

#### POST /auth/login
Login with email and password.

**Request Body**:
```json
{
  "email": "john@example.com",
  "password": "securepassword"
}
```

**Response**:
```json
{
  "accessToken": "jwt_token_here",
  "user": {
    "id": "uuid",
    "name": "John Doe",
    "email": "john@example.com",
    "role": "USER"
  }
}
```

#### GET /auth/me
Get current user profile (requires authentication).

**Headers**:
```
Authorization: Bearer <jwt_token>
```

**Response**:
```json
{
  "id": "uuid",
  "name": "John Doe",
  "email": "john@example.com",
  "role": "USER"
}
```

### Vehicle Endpoints

#### POST /vehicles
Create a new vehicle (requires authentication).

**Headers**:
```
Authorization: Bearer <jwt_token>
```

**Request Body**:
```json
{
  "registrationNumber": "ABC-1234",
  "make": "Toyota",
  "model": "Camry",
  "year": 2020
}
```

**Response**:
```json
{
  "id": "uuid",
  "userId": "uuid",
  "registrationNumber": "ABC-1234",
  "make": "Toyota",
  "model": "Camry",
  "year": 2020,
  "vehicleUuid": "generated-uuid",
  "createdAt": "2024-01-01T00:00:00Z"
}
```

#### GET /vehicles/my
Get all vehicles for current user (requires authentication).

**Headers**:
```
Authorization: Bearer <jwt_token>
```

**Response**:
```json
[
  {
    "id": "uuid",
    "registrationNumber": "ABC-1234",
    "make": "Toyota",
    "model": "Camry",
    "year": 2020,
    "vehicleUuid": "generated-uuid"
  }
]
```

### Device Endpoints

#### POST /devices/register
Register a device (requires authentication).

**Headers**:
```
Authorization: Bearer <jwt_token>
```

**Request Body**:
```json
{
  "devicePlatform": "ANDROID",
  "deviceIdentifier": "unique_device_id",
  "vehicleId": "vehicle_uuid"
}
```

**Response**:
```json
{
  "id": "uuid",
  "userId": "uuid",
  "vehicleId": "uuid",
  "devicePlatform": "ANDROID",
  "deviceIdentifier": "unique_device_id",
  "lastSeenAt": null
}
```

#### PATCH /devices/:id/heartbeat
Update device heartbeat (requires authentication).

**Headers**:
```
Authorization: Bearer <jwt_token>
```

**Response**:
```json
{
  "id": "uuid",
  "lastSeenAt": "2024-01-01T00:00:00Z"
}
```

### Detection Endpoints

#### POST /detections/unregistered
Report an unregistered detection (requires authentication).

**Headers**:
```
Authorization: Bearer <jwt_token>
```

**Request Body**:
```json
{
  "unregisteredIdentifier": "device_address",
  "rssi": -65,
  "latitude": 40.7128,
  "longitude": -74.0060
}
```

**Response**:
```json
{
  "id": "uuid",
  "unregisteredIdentifier": "device_address",
  "rssi": -65,
  "latitude": 40.7128,
  "longitude": -74.0060,
  "detectedAt": "2024-01-01T00:00:00Z",
  "status": "NEW"
}
```

#### GET /detections/unregistered
Get all unregistered detections (requires ADMIN role).

**Headers**:
```
Authorization: Bearer <admin_jwt_token>
```

**Query Parameters**:
- `status` (optional): Filter by status (NEW, UNDER_REVIEW, CONFIRMED, IGNORED)
- `startDate` (optional): Filter by start date (ISO 8601)
- `endDate` (optional): Filter by end date (ISO 8601)
- `limit` (optional): Number of results (default: 10)
- `offset` (optional): Pagination offset

**Response**:
```json
{
  "data": [
    {
      "id": "uuid",
      "unregisteredIdentifier": "device_address",
      "detectedAt": "2024-01-01T00:00:00Z",
      "status": "NEW",
      "detectedByVehicle": {
        "id": "uuid",
        "registrationNumber": "ABC-1234",
        "user": {
          "name": "John Doe",
          "email": "john@example.com"
        }
      }
    }
  ],
  "total": 100
}
```

#### GET /detections/unregistered/:id
Get a specific detection (requires ADMIN role).

**Headers**:
```
Authorization: Bearer <admin_jwt_token>
```

**Response**:
```json
{
  "id": "uuid",
  "unregisteredIdentifier": "device_address",
  "rssi": -65,
  "latitude": 40.7128,
  "longitude": -74.0060,
  "detectedAt": "2024-01-01T00:00:00Z",
  "status": "NEW",
  "detectedByVehicle": {
    "id": "uuid",
    "registrationNumber": "ABC-1234",
    "make": "Toyota",
    "model": "Camry",
    "user": {
      "name": "John Doe",
      "email": "john@example.com"
    }
  },
  "detectedByDevice": {
    "id": "uuid",
    "deviceIdentifier": "device_id"
  },
  "adminActions": []
}
```

#### PATCH /detections/unregistered/:id
Update detection status (requires ADMIN role).

**Headers**:
```
Authorization: Bearer <admin_jwt_token>
```

**Request Body**:
```json
{
  "status": "CONFIRMED",
  "notes": "Confirmed as unregistered vehicle"
}
```

**Response**:
```json
{
  "id": "uuid",
  "status": "CONFIRMED",
  "adminActions": [
    {
      "id": "uuid",
      "action": "Status updated to CONFIRMED",
      "notes": "Confirmed as unregistered vehicle",
      "createdAt": "2024-01-01T00:00:00Z",
      "adminUser": {
        "name": "Admin User",
        "email": "admin@example.com"
      }
    }
  ]
}
```

### Admin Endpoints

#### GET /admin/overview
Get dashboard overview statistics (requires ADMIN role).

**Headers**:
```
Authorization: Bearer <admin_jwt_token>
```

**Response**:
```json
{
  "totalVehicles": 150,
  "totalDetections": 500,
  "detectionsToday": 25,
  "detectionsThisWeek": 120,
  "statusBreakdown": {
    "new": 45,
    "underReview": 12,
    "confirmed": 380,
    "ignored": 63
  }
}
```

---

## Security Considerations

### Authentication
- All API endpoints (except `/auth/register` and `/auth/login`) require JWT authentication
- JWT tokens are stored:
  - Android App: In secure storage (TokenManager)
  - Admin Web: In browser localStorage
- Tokens expire after a set period (configured in backend)

### Authorization
- Regular users (USER role) can only:
  - Manage their own vehicles
  - Register their devices
  - Report detections
- Administrators (ADMIN role) can additionally:
  - View all detections
  - Update detection statuses
  - View dashboard statistics

### Data Privacy
- User passwords are hashed (never stored in plain text)
- GPS coordinates are optional and only stored if user grants location permission
- Device identifiers are used for tracking but don't expose personal information

---

## Environment Setup

### Backend Environment Variables

Create a `.env` file in the `backend/` directory:

```env
DATABASE_URL="postgresql://user:password@localhost:5432/v2v_db"
JWT_SECRET="your-secret-key-here"
PORT=3001
CORS_ORIGINS="http://localhost:3000,http://localhost:5173"
```

### Admin Web Environment Variables

Create a `.env.local` file in the `admin-web/` directory:

```env
NEXT_PUBLIC_API_URL=http://localhost:3001
```

### Android App Configuration

Update the `BASE_URL` in `ApiClient.kt`:
- For Android Emulator: Use `http://10.0.2.2:3001`
- For Physical Device: Use your computer's IP address (e.g., `http://192.168.1.100:3001`)

---

## Common Workflows Summary

### New User Journey

1. **Download and open Android app**
2. **Register account** (name, email, password)
3. **Login** with credentials
4. **Register vehicle** (optional details)
5. **Grant permissions** (Bluetooth, Location)
6. **App automatically starts**:
   - Broadcasting vehicle identity via BLE
   - Scanning for nearby devices
7. **Detections are automatically reported** to backend

### Admin Review Journey

1. **Open admin web panel**
2. **Login** with admin credentials
3. **View dashboard** for overview statistics
4. **Click on detection** to view details
5. **Review information**:
   - Unregistered identifier
   - Detection location
   - Who detected it
   - Signal strength
6. **Update status**:
   - Mark as UNDER_REVIEW if investigating
   - Mark as CONFIRMED if verified
   - Mark as IGNORED if false positive
7. **Add notes** for future reference

---

## Troubleshooting

### Android App Issues

**Bluetooth not working**:
- Ensure Bluetooth is enabled on device
- Grant all required permissions (Bluetooth, Location)
- Restart the app

**Cannot connect to backend**:
- Verify backend is running on correct port
- Check BASE_URL in ApiClient.kt matches your setup
- Ensure device and computer are on same WiFi network
- Check Windows Firewall allows connections on port 3001

**No detections appearing**:
- Ensure BLE scanning is active (check status in app)
- Verify other Bluetooth devices are nearby
- Check location permission is granted (required for BLE on Android)

### Admin Web Panel Issues

**Cannot login**:
- Verify user has ADMIN role in database
- Check backend is running
- Verify API URL in .env.local

**No detections showing**:
- Check if detections exist in database
- Verify filters aren't hiding results
- Check browser console for errors

### Backend Issues

**Database connection errors**:
- Verify PostgreSQL is running
- Check DATABASE_URL in .env file
- Run migrations: `npm run prisma:migrate`

**Authentication errors**:
- Verify JWT_SECRET is set in .env
- Check token expiration settings
- Ensure tokens are being sent in Authorization header

---

## Future Enhancements

Potential improvements for the system:

1. **Detection Confirmation**: Require multiple sightings before reporting
2. **Cryptographic Signatures**: Sign BLE broadcasts for security
3. **Privacy Enhancements**: Encrypt vehicle UUIDs
4. **iOS Support**: Add iOS app version
5. **Real-time Updates**: WebSocket support for live detection updates
6. **Advanced Filtering**: More sophisticated detection filtering
7. **Analytics Dashboard**: More detailed statistics and charts
8. **Mobile Admin App**: Admin panel for mobile devices

---

## Conclusion

This documentation provides a comprehensive overview of the V2V system, including:
- System architecture and components
- Complete database schema
- Detailed user workflows for both Android app and Admin web panel
- API endpoint documentation
- Technical implementation details

The system is designed to be scalable, secure, and user-friendly, enabling efficient detection and management of unregistered vehicles through Bluetooth Low Energy technology.


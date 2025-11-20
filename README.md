# V2V Bluetooth Vehicle Registration & Unregistered Detection System (MVP)

A full-stack system for vehicle registration and unregistered vehicle detection using Bluetooth Low Energy (BLE).

## Project Structure

```
V2VV/
├── backend/          # NestJS + TypeScript + PostgreSQL backend API
├── admin-web/        # Next.js admin web panel
└── android-app/      # Android mobile app (Kotlin + Jetpack Compose)
```

## System Overview

### Components

1. **Backend API** (NestJS)
   - RESTful API with JWT authentication
   - PostgreSQL database with Prisma ORM
   - User, Vehicle, Device, and Detection management
   - Admin endpoints for oversight

2. **Admin Web Panel** (Next.js)
   - Dashboard with statistics
   - Detection list and detail views
   - Status management for detections
   - Admin authentication

3. **Android Mobile App** (Kotlin)
   - User registration and login
   - Vehicle registration
   - BLE advertising (broadcast vehicle UUID)
   - BLE scanning (detect nearby devices)
   - Unregistered vehicle detection and reporting

## Quick Start

### Backend

```bash
cd backend
npm install
cp .env.example .env  # Edit with your database credentials
npm run prisma:generate
npm run prisma:migrate
npm run start:dev
```

Backend runs on `http://localhost:3001`

### Admin Web Panel

```bash
cd admin-web
npm install
# Create .env.local with NEXT_PUBLIC_API_URL=http://localhost:3001
npm run dev
```

Admin panel runs on `http://localhost:3000`

### Android App

1. Open `android-app` in Android Studio
2. Update API URL in `ApiClient.kt` if needed
3. Build and run on a physical device (BLE requires hardware)

## Docker Setup

```bash
cd backend
docker-compose up -d
```

This starts PostgreSQL and the backend API.

## Database Schema

- **User**: User accounts (USER/ADMIN roles)
- **Vehicle**: Registered vehicles with unique UUIDs
- **Device**: Physical devices (phones) linked to users/vehicles
- **UnregisteredDetection**: Detections of unregistered vehicles
- **AdminActionLog**: Admin actions on detections

## API Endpoints

### Authentication
- `POST /auth/register` - Register new user
- `POST /auth/login` - Login
- `GET /auth/me` - Get current user

### Vehicles
- `POST /vehicles` - Create vehicle
- `GET /vehicles/my` - Get user's vehicles

### Devices
- `POST /devices/register` - Register device
- `PATCH /devices/:id/heartbeat` - Update heartbeat

### Detections
- `POST /detections/unregistered` - Report unregistered detection
- `GET /detections/unregistered` - List detections (admin)
- `PATCH /detections/unregistered/:id` - Update status (admin)

### Admin
- `GET /admin/overview` - Dashboard statistics

## BLE Implementation

- Uses custom service UUID: `0000FEAA-0000-1000-8000-00805F9B34FB`
- Vehicle UUID embedded in BLE service data
- Detection requires 3+ sightings within 3 seconds for confirmation
- Unregistered devices are those not broadcasting the expected service UUID/data format

## Environment Variables

### Backend (.env)
```
DATABASE_URL="postgresql://user:password@localhost:5432/v2v_db"
JWT_SECRET="your-secret-key"
PORT=3001
CORS_ORIGINS="http://localhost:3000,http://localhost:5173"
```

### Admin Web (.env.local)
```
NEXT_PUBLIC_API_URL=http://localhost:3001
```

## Development Notes

- This is an MVP - code is structured for future enhancements (cryptographic signatures, better privacy)
- BLE implementation is abstracted for easy refactoring
- Backend uses Prisma migrations for database schema
- All components use TypeScript/Kotlin for type safety

## License

UNLICENSED - Private project


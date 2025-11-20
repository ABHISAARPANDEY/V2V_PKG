# V2V Backend API

Backend API for the V2V Bluetooth Vehicle Registration & Unregistered Detection System.

## Tech Stack

- **NestJS** - Progressive Node.js framework
- **TypeScript** - Type-safe JavaScript
- **Prisma** - Next-generation ORM
- **PostgreSQL** - Relational database
- **JWT** - Authentication
- **Argon2** - Password hashing

## Setup

### Prerequisites

- Node.js 20+
- PostgreSQL 16+ (or use Docker)
- npm or yarn

### Installation

**⚠️ IMPORTANT: Run `npm install` first!**

1. Install dependencies (REQUIRED - fixes "nest is not recognized" error):
```bash
npm install
```
Wait for installation to complete (2-5 minutes).

2. Set up environment variables:
```bash
cp .env.example .env
# Edit .env with your database credentials
```

3. Set up database:
```bash
# Generate Prisma client
npm run prisma:generate

# Run migrations
npm run prisma:migrate

# Seed database with default admin user
npm run prisma:seed
```

**Default Admin Credentials:**
- Email: `admin@v2v.com`
- Password: `admin123`

See `DEFAULT_CREDENTIALS.md` for more details.
See `SETUP_INSTRUCTIONS.md` for detailed setup guide.

4. Start the server:
```bash
# Development
npm run start:dev

# Production
npm run build
npm run start:prod
```

### Docker Setup

```bash
# Start PostgreSQL and backend
docker-compose up -d

# Run migrations (in backend container or locally)
npm run prisma:migrate
```

## API Endpoints

### Authentication
- `POST /auth/register` - Register new user
- `POST /auth/login` - Login user
- `GET /auth/me` - Get current user (protected)

### Vehicles
- `POST /vehicles` - Create vehicle (protected)
- `GET /vehicles/my` - Get user's vehicles (protected)

### Devices
- `POST /devices/register` - Register device (protected)
- `PATCH /devices/:id/heartbeat` - Update device heartbeat (protected)

### Detections
- `POST /detections/unregistered` - Report unregistered detection (protected)
- `GET /detections/unregistered` - List detections (admin only)
- `GET /detections/unregistered/:id` - Get detection details (admin only)
- `PATCH /detections/unregistered/:id` - Update detection status (admin only)

### Admin
- `GET /admin/overview` - Get dashboard statistics (admin only)

## Database Schema

See `prisma/schema.prisma` for the complete database schema.

## Development

```bash
# Watch mode
npm run start:dev

# Generate Prisma client
npm run prisma:generate

# Open Prisma Studio
npm run prisma:studio

# Run migrations
npm run prisma:migrate
```


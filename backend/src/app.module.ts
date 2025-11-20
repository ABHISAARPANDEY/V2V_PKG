import { Module } from '@nestjs/common';
import { ConfigModule } from '@nestjs/config';
import { AuthModule } from './modules/auth/auth.module';
import { UsersModule } from './modules/users/users.module';
import { VehiclesModule } from './modules/vehicles/vehicles.module';
import { DevicesModule } from './modules/devices/devices.module';
import { DetectionsModule } from './modules/detections/detections.module';
import { AdminModule } from './modules/admin/admin.module';
import { DatabaseModule } from './database/database.module';
import { HealthController } from './health.controller';

@Module({
  imports: [
    ConfigModule.forRoot({
      isGlobal: true,
      envFilePath: '.env',
    }),
    DatabaseModule,
    AuthModule,
    UsersModule,
    VehiclesModule,
    DevicesModule,
    DetectionsModule,
    AdminModule,
  ],
  controllers: [HealthController],
})
export class AppModule {}


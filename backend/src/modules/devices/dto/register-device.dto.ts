import { IsString, IsEnum, IsOptional, IsUUID } from 'class-validator';
import { DevicePlatform } from '@prisma/client';

export class RegisterDeviceDto {
  @IsString()
  deviceIdentifier: string;

  @IsEnum(DevicePlatform)
  platform: DevicePlatform;

  @IsUUID()
  @IsOptional()
  vehicleId?: string;
}


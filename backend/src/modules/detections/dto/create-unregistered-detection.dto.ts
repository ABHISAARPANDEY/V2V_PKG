import { IsString, IsUUID, IsInt, IsOptional, IsNumber, IsDateString, Min, Max } from 'class-validator';

export class CreateUnregisteredDetectionDto {
  @IsUUID()
  detectedByVehicleId: string;

  @IsUUID()
  detectedByDeviceId: string;

  @IsString()
  unregisteredIdentifier: string;

  @IsInt()
  @IsOptional()
  rssi?: number;

  @IsNumber()
  @IsOptional()
  @Min(-90)
  @Max(90)
  latitude?: number;

  @IsNumber()
  @IsOptional()
  @Min(-180)
  @Max(180)
  longitude?: number;

  @IsDateString()
  @IsOptional()
  detectedAt?: string;
}


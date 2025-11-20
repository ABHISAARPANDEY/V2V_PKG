import { IsEnum, IsOptional, IsString } from 'class-validator';
import { DetectionStatus } from '@prisma/client';

export class UpdateDetectionStatusDto {
  @IsEnum(DetectionStatus)
  status: DetectionStatus;

  @IsString()
  @IsOptional()
  notes?: string;
}


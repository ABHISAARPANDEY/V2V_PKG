import { IsString, IsOptional, IsInt, Min, Max } from 'class-validator';

export class CreateVehicleDto {
  @IsString()
  @IsOptional()
  registrationNumber?: string;

  @IsString()
  @IsOptional()
  make?: string;

  @IsString()
  @IsOptional()
  model?: string;

  @IsInt()
  @Min(1900)
  @Max(2100)
  @IsOptional()
  year?: number;
}


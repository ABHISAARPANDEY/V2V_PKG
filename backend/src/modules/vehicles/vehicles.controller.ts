import { Controller, Post, Get, Body, UseGuards, Request, Param } from '@nestjs/common';
import { VehiclesService } from './vehicles.service';
import { CreateVehicleDto } from './dto/create-vehicle.dto';
import { JwtAuthGuard } from '../auth/guards/jwt-auth.guard';

@Controller('vehicles')
@UseGuards(JwtAuthGuard)
export class VehiclesController {
  constructor(private readonly vehiclesService: VehiclesService) {}

  @Post()
  async create(@Request() req, @Body() createVehicleDto: CreateVehicleDto) {
    return this.vehiclesService.create(req.user.userId, createVehicleDto);
  }

  @Get('my')
  async findMyVehicles(@Request() req) {
    return this.vehiclesService.findMyVehicles(req.user.userId);
  }

  @Get(':id')
  async findOne(@Request() req, @Param('id') id: string) {
    return this.vehiclesService.findOne(id, req.user.userId);
  }
}


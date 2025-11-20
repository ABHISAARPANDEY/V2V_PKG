import { Controller, Post, Patch, Body, UseGuards, Request, Param } from '@nestjs/common';
import { DevicesService } from './devices.service';
import { RegisterDeviceDto } from './dto/register-device.dto';
import { JwtAuthGuard } from '../auth/guards/jwt-auth.guard';

@Controller('devices')
@UseGuards(JwtAuthGuard)
export class DevicesController {
  constructor(private readonly devicesService: DevicesService) {}

  @Post('register')
  async register(@Request() req, @Body() registerDeviceDto: RegisterDeviceDto) {
    return this.devicesService.register(req.user.userId, registerDeviceDto);
  }

  @Patch(':id/heartbeat')
  async heartbeat(@Request() req, @Param('id') id: string) {
    return this.devicesService.heartbeat(id, req.user.userId);
  }
}


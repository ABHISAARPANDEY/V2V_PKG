import { Controller, Post, Get, Patch, Body, UseGuards, Request, Param, Query } from '@nestjs/common';
import { DetectionsService } from './detections.service';
import { CreateUnregisteredDetectionDto } from './dto/create-unregistered-detection.dto';
import { UpdateDetectionStatusDto } from './dto/update-detection-status.dto';
import { JwtAuthGuard } from '../auth/guards/jwt-auth.guard';
import { RolesGuard } from '../auth/guards/roles.guard';
import { Roles } from '../auth/decorators/roles.decorator';
import { UserRole, DetectionStatus } from '@prisma/client';

@Controller('detections')
export class DetectionsController {
  constructor(private readonly detectionsService: DetectionsService) {}

  @Post('unregistered')
  @UseGuards(JwtAuthGuard)
  async createUnregistered(@Request() req, @Body() createDto: CreateUnregisteredDetectionDto) {
    return this.detectionsService.createUnregistered(req.user.userId, createDto);
  }

  @Get('unregistered')
  @UseGuards(JwtAuthGuard, RolesGuard)
  @Roles(UserRole.ADMIN)
  async findAllUnregistered(
    @Query('status') status?: DetectionStatus,
    @Query('startDate') startDate?: string,
    @Query('endDate') endDate?: string,
    @Query('limit') limit?: string,
    @Query('offset') offset?: string,
  ) {
    return this.detectionsService.findAll({
      status: status as DetectionStatus | undefined,
      startDate: startDate ? new Date(startDate) : undefined,
      endDate: endDate ? new Date(endDate) : undefined,
      limit: limit ? parseInt(limit, 10) : undefined,
      offset: offset ? parseInt(offset, 10) : undefined,
    });
  }

  @Get('unregistered/:id')
  @UseGuards(JwtAuthGuard, RolesGuard)
  @Roles(UserRole.ADMIN)
  async findOne(@Param('id') id: string) {
    return this.detectionsService.findOne(id);
  }

  @Patch('unregistered/:id')
  @UseGuards(JwtAuthGuard, RolesGuard)
  @Roles(UserRole.ADMIN)
  async updateStatus(@Request() req, @Param('id') id: string, @Body() updateDto: UpdateDetectionStatusDto) {
    return this.detectionsService.updateStatus(id, req.user.userId, updateDto);
  }
}


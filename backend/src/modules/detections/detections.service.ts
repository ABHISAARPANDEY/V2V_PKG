import { Injectable, NotFoundException, ForbiddenException, BadRequestException } from '@nestjs/common';
import { PrismaService } from '../../database/prisma.service';
import { CreateUnregisteredDetectionDto } from './dto/create-unregistered-detection.dto';
import { UpdateDetectionStatusDto } from './dto/update-detection-status.dto';
import { DetectionStatus } from '@prisma/client';

@Injectable()
export class DetectionsService {
  constructor(private prisma: PrismaService) {}

  async createUnregistered(userId: string, createDto: CreateUnregisteredDetectionDto) {
    const { detectedByVehicleId, detectedByDeviceId, unregisteredIdentifier, rssi, latitude, longitude, detectedAt } = createDto;

    // Verify vehicle belongs to user
    const vehicle = await this.prisma.vehicle.findUnique({
      where: { id: detectedByVehicleId },
    });

    if (!vehicle) {
      throw new NotFoundException('Vehicle not found');
    }

    if (vehicle.userId !== userId) {
      throw new ForbiddenException('Vehicle does not belong to you');
    }

    // Verify device belongs to user
    const device = await this.prisma.device.findUnique({
      where: { id: detectedByDeviceId },
    });

    if (!device) {
      throw new NotFoundException('Device not found');
    }

    if (device.userId !== userId) {
      throw new ForbiddenException('Device does not belong to you');
    }

    // Create detection
    const detection = await this.prisma.unregisteredDetection.create({
      data: {
        detectedByVehicleId,
        detectedByDeviceId,
        unregisteredIdentifier,
        rssi: rssi || null,
        latitude: latitude || null,
        longitude: longitude || null,
        detectedAt: detectedAt ? new Date(detectedAt) : new Date(),
        status: 'NEW',
      },
      include: {
        detectedByVehicle: {
          include: {
            user: {
              select: {
                id: true,
                name: true,
                email: true,
              },
            },
          },
        },
        detectedByDevice: true,
      },
    });

    return detection;
  }

  async findAll(filters?: {
    status?: DetectionStatus;
    startDate?: Date;
    endDate?: Date;
    limit?: number;
    offset?: number;
  }) {
    const where: any = {};

    if (filters?.status) {
      where.status = filters.status;
    }

    if (filters?.startDate || filters?.endDate) {
      where.detectedAt = {};
      if (filters.startDate) {
        where.detectedAt.gte = filters.startDate;
      }
      if (filters.endDate) {
        where.detectedAt.lte = filters.endDate;
      }
    }

    const [detections, total] = await Promise.all([
      this.prisma.unregisteredDetection.findMany({
        where,
        include: {
          detectedByVehicle: {
            include: {
              user: {
                select: {
                  id: true,
                  name: true,
                  email: true,
                },
              },
            },
          },
          detectedByDevice: true,
          adminActions: {
            include: {
              adminUser: {
                select: {
                  id: true,
                  name: true,
                  email: true,
                },
              },
            },
            orderBy: {
              createdAt: 'desc',
            },
          },
        },
        orderBy: {
          detectedAt: 'desc',
        },
        take: filters?.limit || 50,
        skip: filters?.offset || 0,
      }),
      this.prisma.unregisteredDetection.count({ where }),
    ]);

    return {
      data: detections,
      total,
      limit: filters?.limit || 50,
      offset: filters?.offset || 0,
    };
  }

  async findOne(id: string) {
    const detection = await this.prisma.unregisteredDetection.findUnique({
      where: { id },
      include: {
        detectedByVehicle: {
          include: {
            user: {
              select: {
                id: true,
                name: true,
                email: true,
              },
            },
          },
        },
        detectedByDevice: true,
        adminActions: {
          include: {
            adminUser: {
              select: {
                id: true,
                name: true,
                email: true,
              },
            },
          },
          orderBy: {
            createdAt: 'desc',
          },
        },
      },
    });

    if (!detection) {
      throw new NotFoundException('Detection not found');
    }

    return detection;
  }

  async updateStatus(id: string, adminUserId: string, updateDto: UpdateDetectionStatusDto) {
    const detection = await this.prisma.unregisteredDetection.findUnique({
      where: { id },
    });

    if (!detection) {
      throw new NotFoundException('Detection not found');
    }

    // Update detection status
    const updated = await this.prisma.unregisteredDetection.update({
      where: { id },
      data: {
        status: updateDto.status,
      },
    });

    // Log admin action
    await this.prisma.adminActionLog.create({
      data: {
        adminUserId,
        detectionId: id,
        action: `STATUS_UPDATED_TO_${updateDto.status}`,
        notes: updateDto.notes || null,
      },
    });

    return updated;
  }
}


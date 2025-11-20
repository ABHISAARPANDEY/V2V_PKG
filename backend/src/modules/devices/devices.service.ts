import { Injectable, NotFoundException, ForbiddenException } from '@nestjs/common';
import { PrismaService } from '../../database/prisma.service';
import { RegisterDeviceDto } from './dto/register-device.dto';

@Injectable()
export class DevicesService {
  constructor(private prisma: PrismaService) {}

  async register(userId: string, registerDeviceDto: RegisterDeviceDto) {
    const { deviceIdentifier, platform, vehicleId } = registerDeviceDto;

    // Verify vehicle belongs to user if provided
    if (vehicleId) {
      const vehicle = await this.prisma.vehicle.findUnique({
        where: { id: vehicleId },
      });

      if (!vehicle) {
        throw new NotFoundException('Vehicle not found');
      }

      if (vehicle.userId !== userId) {
        throw new ForbiddenException('Vehicle does not belong to you');
      }
    }

    // Check if device already exists
    const existingDevice = await this.prisma.device.findUnique({
      where: { deviceIdentifier },
    });

    if (existingDevice) {
      // Update if exists
      return this.prisma.device.update({
        where: { id: existingDevice.id },
        data: {
          userId,
          vehicleId: vehicleId || null,
          lastSeenAt: new Date(),
        },
        include: {
          vehicle: true,
          user: {
            select: {
              id: true,
              name: true,
              email: true,
            },
          },
        },
      });
    }

    // Create new device
    return this.prisma.device.create({
      data: {
        userId,
        vehicleId: vehicleId || null,
        devicePlatform: platform,
        deviceIdentifier,
        lastSeenAt: new Date(),
      },
      include: {
        vehicle: true,
        user: {
          select: {
            id: true,
            name: true,
            email: true,
          },
        },
      },
    });
  }

  async heartbeat(deviceId: string, userId: string) {
    const device = await this.prisma.device.findUnique({
      where: { id: deviceId },
    });

    if (!device) {
      throw new NotFoundException('Device not found');
    }

    if (device.userId !== userId) {
      throw new ForbiddenException('Device does not belong to you');
    }

    return this.prisma.device.update({
      where: { id: deviceId },
      data: {
        lastSeenAt: new Date(),
      },
    });
  }

  async findOne(deviceId: string, userId?: string) {
    const device = await this.prisma.device.findUnique({
      where: { id: deviceId },
      include: {
        vehicle: true,
        user: {
          select: {
            id: true,
            name: true,
            email: true,
          },
        },
      },
    });

    if (!device) {
      throw new NotFoundException('Device not found');
    }

    if (userId && device.userId !== userId) {
      throw new ForbiddenException('Device does not belong to you');
    }

    return device;
  }
}


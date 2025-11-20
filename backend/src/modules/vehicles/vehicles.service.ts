import { Injectable, ForbiddenException, NotFoundException } from '@nestjs/common';
import { PrismaService } from '../../database/prisma.service';
import { CreateVehicleDto } from './dto/create-vehicle.dto';
import { randomUUID } from 'crypto';

@Injectable()
export class VehiclesService {
  constructor(private prisma: PrismaService) {}

  async create(userId: string, createVehicleDto: CreateVehicleDto) {
    // Generate vehicle UUID (server-side for security)
    const vehicleUuid = randomUUID();

    const vehicle = await this.prisma.vehicle.create({
      data: {
        userId,
        vehicleUuid,
        ...createVehicleDto,
      },
      include: {
        user: {
          select: {
            id: true,
            name: true,
            email: true,
          },
        },
      },
    });

    return vehicle;
  }

  async findMyVehicles(userId: string) {
    return this.prisma.vehicle.findMany({
      where: { userId },
      include: {
        user: {
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
    });
  }

  async findOne(id: string, userId?: string) {
    const vehicle = await this.prisma.vehicle.findUnique({
      where: { id },
      include: {
        user: {
          select: {
            id: true,
            name: true,
            email: true,
          },
        },
      },
    });

    if (!vehicle) {
      throw new NotFoundException('Vehicle not found');
    }

    // If userId provided, check ownership
    if (userId && vehicle.userId !== userId) {
      throw new ForbiddenException('You do not have access to this vehicle');
    }

    return vehicle;
  }

  async findByVehicleUuid(vehicleUuid: string) {
    return this.prisma.vehicle.findUnique({
      where: { vehicleUuid },
    });
  }
}


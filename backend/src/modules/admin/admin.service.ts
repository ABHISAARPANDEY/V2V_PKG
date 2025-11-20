import { Injectable } from '@nestjs/common';
import { PrismaService } from '../../database/prisma.service';

@Injectable()
export class AdminService {
  constructor(private prisma: PrismaService) {}

  async getOverview() {
    const now = new Date();
    const todayStart = new Date(now.setHours(0, 0, 0, 0));
    const weekStart = new Date(now.setDate(now.getDate() - 7));

    const [
      totalVehicles,
      totalDetections,
      detectionsToday,
      detectionsThisWeek,
      newDetections,
      underReviewDetections,
      confirmedDetections,
      ignoredDetections,
    ] = await Promise.all([
      this.prisma.vehicle.count(),
      this.prisma.unregisteredDetection.count(),
      this.prisma.unregisteredDetection.count({
        where: {
          detectedAt: {
            gte: todayStart,
          },
        },
      }),
      this.prisma.unregisteredDetection.count({
        where: {
          detectedAt: {
            gte: weekStart,
          },
        },
      }),
      this.prisma.unregisteredDetection.count({
        where: { status: 'NEW' },
      }),
      this.prisma.unregisteredDetection.count({
        where: { status: 'UNDER_REVIEW' },
      }),
      this.prisma.unregisteredDetection.count({
        where: { status: 'CONFIRMED' },
      }),
      this.prisma.unregisteredDetection.count({
        where: { status: 'IGNORED' },
      }),
    ]);

    return {
      totalVehicles,
      totalDetections,
      detectionsToday,
      detectionsThisWeek,
      statusBreakdown: {
        new: newDetections,
        underReview: underReviewDetections,
        confirmed: confirmedDetections,
        ignored: ignoredDetections,
      },
    };
  }
}


import { PrismaClient } from '@prisma/client';
import * as argon2 from 'argon2';

const prisma = new PrismaClient();

async function main() {
  console.log('🌱 Seeding database...');

  // Create default admin user
  const adminEmail = 'admin@v2v.com';
  const adminPassword = 'admin123';
  const adminPasswordHash = await argon2.hash(adminPassword);

  // Check if admin already exists
  const existingAdmin = await prisma.user.findUnique({
    where: { email: adminEmail },
  });

  if (existingAdmin) {
    console.log('✅ Admin user already exists');
    // Update password in case it changed
    await prisma.user.update({
      where: { email: adminEmail },
      data: { passwordHash: adminPasswordHash },
    });
    console.log('✅ Admin password updated');
  } else {
    // Create new admin user
    const admin = await prisma.user.create({
      data: {
        name: 'Admin User',
        email: adminEmail,
        passwordHash: adminPasswordHash,
        role: 'ADMIN',
      },
    });
    console.log('✅ Admin user created:', admin.email);
  }

  // Create a test regular user (optional)
  const testUserEmail = 'user@v2v.com';
  const testUserPassword = 'user123';
  const testUserPasswordHash = await argon2.hash(testUserPassword);

  const existingUser = await prisma.user.findUnique({
    where: { email: testUserEmail },
  });

  if (!existingUser) {
    const testUser = await prisma.user.create({
      data: {
        name: 'Test User',
        email: testUserEmail,
        passwordHash: testUserPasswordHash,
        role: 'USER',
      },
    });
    console.log('✅ Test user created:', testUser.email);
  } else {
    console.log('✅ Test user already exists');
  }

  console.log('\n📋 Default Credentials:');
  console.log('━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━');
  console.log('👤 ADMIN:');
  console.log('   Email:    admin@v2v.com');
  console.log('   Password: admin123');
  console.log('━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━');
  console.log('👤 USER (for mobile app testing):');
  console.log('   Email:    user@v2v.com');
  console.log('   Password: user123');
  console.log('━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━');
  console.log('\n✅ Seeding completed!');
}

main()
  .catch((e) => {
    console.error('❌ Error seeding database:', e);
    process.exit(1);
  })
  .finally(async () => {
    await prisma.$disconnect();
  });


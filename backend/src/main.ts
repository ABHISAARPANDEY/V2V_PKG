import { NestFactory } from '@nestjs/core';
import { ValidationPipe, BadRequestException } from '@nestjs/common';
import { AppModule } from './app.module';
import { HttpExceptionFilter } from './common/filters/http-exception.filter';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);

  // Enable CORS for admin panel and mobile app
  app.enableCors({
    origin: true, // Allow all origins for development (restrict in production)
    credentials: true,
  });

  // Global exception filter for better error messages
  app.useGlobalFilters(new HttpExceptionFilter());

  // Global validation pipe
  app.useGlobalPipes(
    new ValidationPipe({
      whitelist: true,
      forbidNonWhitelisted: true,
      transform: true,
      exceptionFactory: (errors) => {
        const messages = errors.map(error => {
          return Object.values(error.constraints || {}).join(', ');
        });
        return new BadRequestException(messages.length > 0 ? messages : 'Validation failed');
      },
    }),
  );

  const port = process.env.PORT || 3001;
  await app.listen(port);
  console.log(`🚀 Backend API running on: http://localhost:${port}`);
}

bootstrap();


# Docker Deployment Guide

## Overview

This project includes a multi-stage Dockerfile optimized for production deployment of the Soccer League application, which combines a Kotlin/Spring Boot backend with a React frontend.

## Files

- `Dockerfile` - Multi-stage build configuration
- `docker-compose.yml` - Complete application stack with MongoDB
- `.dockerignore` - Optimized build context

## Quick Start

### Using Docker Compose (Recommended)

```bash
# Start the complete application stack
docker-compose up -d

# View logs
docker-compose logs -f soccer-league

# Stop the stack
docker-compose down
```

The application will be available at http://localhost:8080

### Using Docker Build Only

```bash
# Build the image
docker build -t soccer-league:latest .

# Run with in-memory database (development)
docker run -p 8080:8080 soccer-league:latest

# Run with external MongoDB
docker run -p 8080:8080 \
  -e MONGODB_URI=mongodb://your-mongo-host:27017/soccer-league \
  soccer-league:latest
```

## Architecture

### Multi-Stage Build Process

1. **Build Stage** (`maven:3.9-eclipse-temurin-21-alpine`)
   - Downloads dependencies (cached layer)
   - Builds frontend via frontend-maven-plugin (Node.js 20.18.0, npm 10.8.2)
   - Compiles Kotlin/Spring Boot application
   - Creates executable JAR

2. **Production Stage** (`eclipse-temurin:21-jre-alpine`)
   - Minimal JRE-only image
   - Non-root user for security
   - Health checks enabled
   - Optimized for size and security

### Security Features

- Non-root user (`appuser:appgroup`)
- Minimal attack surface (JRE-only, Alpine Linux)
- Health checks for container orchestration
- Proper file ownership and permissions

## Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `MONGODB_URI` | `mongodb://mongo:27017/soccer-league` | MongoDB connection string |
| `START_MONTH` | `10` | Season start month (October) |
| `START_DAY` | `1` | Season start day |
| `SPRING_PROFILES_ACTIVE` | `prod` | Spring Boot profile |

## Health Checks

The container includes health checks that monitor the Spring Boot Actuator health endpoint:

- **Interval**: 30 seconds
- **Timeout**: 3 seconds
- **Start Period**: 40 seconds (application startup time)
- **Retries**: 3

Health check endpoint: `http://localhost:8080/actuator/health`

## MongoDB Integration

The `docker-compose.yml` includes:

- MongoDB 7 (jammy variant)
- Persistent data storage via Docker volumes
- Health checks for MongoDB
- Automatic database initialization

## Build Optimization

The Dockerfile includes several optimizations:

1. **Layer Caching**: Dependencies downloaded in separate layer
2. **Multi-stage**: Build artifacts not included in final image
3. **Security**: Non-root user, minimal base image
4. **Size**: Alpine Linux base, JRE-only runtime

## Development vs Production

### Development
```bash
# Use in-memory database
docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=dev \
  soccer-league:latest
```

### Production
```bash
# Use docker-compose for full stack
docker-compose up -d
```

## Monitoring

Container logs can be accessed via:

```bash
# Application logs
docker-compose logs -f soccer-league

# MongoDB logs
docker-compose logs -f mongo

# All services
docker-compose logs -f
```

## Scaling

The containerized application supports horizontal scaling:

```bash
# Scale application instances
docker-compose up -d --scale soccer-league=3

# Use a load balancer (nginx, traefik, etc.) for traffic distribution
```

## Troubleshooting

### Health Check Failures
```bash
# Check application health manually
docker exec <container-id> wget -qO- http://localhost:8080/actuator/health

# View detailed logs
docker-compose logs soccer-league
```

### Build Issues
```bash
# Clean build without cache
docker build --no-cache -t soccer-league:latest .

# Check build context size
docker build --progress=plain -t soccer-league:latest .
```

### MongoDB Connection Issues
```bash
# Verify MongoDB is running
docker-compose ps mongo

# Check MongoDB logs
docker-compose logs mongo

# Test MongoDB connection
docker exec -it <mongo-container> mongosh soccer-league
```
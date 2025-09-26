# Build stage
FROM maven:3.9-eclipse-temurin-21 as build
WORKDIR /app

# Copy dependency files first for better layer caching
COPY pom.xml ./
COPY src/main/frontend/package*.json ./src/main/frontend/
RUN mvn dependency:go-offline -B && \
    mvn com.github.eirslett:frontend-maven-plugin:install-node-and-npm -B

# Copy source code
COPY src/ ./src/

# Set Maven options and build (includes frontend via frontend-maven-plugin)
ENV MAVEN_OPTS="-Dorg.slf4j.simpleLogger.log.org.apache.maven.cli.transfer.Slf4jMavenTransferListener=warn -Djansi.force=true"
RUN mvn clean package -B -Dstyle.color=always -DskipTests

# Production stage
FROM eclipse-temurin:21-jre

# Create non-root user for security
RUN groupadd -g 1001 appgroup && \
    useradd -u 1001 -g appgroup -s /bin/bash -m appuser

# Install wget for health checks
RUN apt-get update && apt-get install -y --no-install-recommends wget && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copy JAR with proper ownership
COPY --from=build --chown=appuser:appgroup /app/target/soccer-league.jar ./soccer-league.jar

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

USER appuser
EXPOSE 8080

CMD ["java", "-jar", "soccer-league.jar"]

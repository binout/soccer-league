#!/bin/bash

echo "🧹 Cleaning up ports before integration tests..."

# Kill any process using port 8080 (Spring Boot HTTP)
echo "Checking port 8080..."
lsof -ti :8080 | xargs -r kill -9 2>/dev/null || echo "Port 8080 is free"

# Kill any process using port 9001 (JMX default)
echo "Checking port 9001..."  
lsof -ti :9001 | xargs -r kill -9 2>/dev/null || echo "Port 9001 is free"

# Kill any process using port 9999 (JMX configured)
echo "Checking port 9999..."
lsof -ti :9999 | xargs -r kill -9 2>/dev/null || echo "Port 9999 is free"

echo "✅ Port cleanup completed"
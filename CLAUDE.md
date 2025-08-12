# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Soccer League is a web application for managing soccer match planning between friends. Built with a Kotlin/Spring Boot backend and React frontend, it follows Domain-Driven Design principles with clean architecture layers.

## Build and Development Commands

### Backend (Maven)
- **Build application**: `mvn clean package`
- **Run tests**: `mvn test`
- **Run application**: `mvn spring-boot:run`
- **Run single test**: `mvn test -Dtest=ClassName#methodName`

### Frontend (npm in src/main/frontend)
- **Development watch mode**: `npm run watch` (rebuilds on file changes)
- **Development build**: `npm run dev-build`
- **Production build**: `npm run prod-build`
- **Inject test data**: `npm run inject-data`

### Development Workflow
1. Run backend from IDE (IntelliJ recommended)
2. Run `npm run watch` in frontend directory for hot reload
3. Frontend builds to `target/classes/public` for Spring Boot to serve

## Architecture

### Domain Layer (`src/main/kotlin/io/github/binout/soccer/domain`)
Core business logic organized by aggregates:
- **Season**: Manages friendly and league matches, calculates current season based on START_MONTH/START_DAY
- **Player**: Player management and statistics
- **MatchDate**: Date planning for friendly and league matches
- **DomainEvents**: Event-driven architecture support

### Application Layer (`src/main/kotlin/io/github/binout/soccer/application`)
Application services that orchestrate domain objects:
- `SeasonService`: Season management operations
- `PlayerService`: Player operations
- `MatchDateService`: Match date scheduling

### Infrastructure Layer (`src/main/kotlin/io/github/binout/soccer/infrastructure`)
- **Persistence**: MongoDB integration with in-memory fallback for development
- **Mail**: SendGrid integration for notifications
- **Templates**: Freemarker for email templates

### Interface Layer (`src/main/kotlin/io/github/binout/soccer/interfaces/rest`)
REST API controllers following resource-based design:
- `SeasonsResource`: Season CRUD and statistics
- `PlayersResource`: Player management
- `FriendlyMatchDateResource`/`LeagueMatchDateResource`: Match scheduling

### Frontend (`src/main/frontend/src`)
React application with:
- `Application.js`: Main app component
- `Season.js`: Season management
- `Players.js`: Player management
- `Agenda.js`/`PlayersAgenda.js`: Match scheduling
- `Statistics.js`: Statistics display

## Environment Variables

- `MONGODB_URI`: MongoDB connection string (uses in-memory if not set)
- `SENDGRID_API_KEY`: SendGrid API key for email notifications
- `START_MONTH`: Season start month (default: 10 for October)
- `START_DAY`: Season start day of month (default: 1)

## Key Implementation Details

- **Season Logic**: Seasons span across calendar years (e.g., "2024-2025") starting from configurable month/day
- **Repository Pattern**: Domain repositories with MongoDB implementations
- **Event-Driven**: Uses domain events for cross-cutting concerns
- **Test Strategy**: Unit tests with JUnit 5, AssertJ, and Mockito; in-memory repositories for testing

## Task Master AI Instructions
**Import Task Master's development workflow commands and guidelines, treat as if import is in the main CLAUDE.md file.**
@./.taskmaster/CLAUDE.md

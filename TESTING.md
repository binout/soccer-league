# Integration Testing Guide

This project includes comprehensive integration tests using Playwright for end-to-end testing of the React frontend and Spring Boot backend.

## Test Setup

The integration tests are located in `src/main/frontend/tests/` and include:

- **application.spec.ts**: Page load validation, navigation testing, console error detection
- **api-integration.spec.ts**: API call validation, error handling, route interception  
- **user-workflows.spec.ts**: Complete user journeys, responsive testing, mobile viewport

## Running Tests

### Via NPM (from frontend directory)

```bash
cd src/main/frontend
npm run test              # Run all tests headless
npm run test:ui           # Interactive UI mode for debugging
npm run test:headed       # Run with browser visible
```

### Via Maven (from project root)

```bash
# Run full integration test suite (builds app, starts server, runs tests, stops server)
mvn verify -Pintegration-tests

# Test individual phases:
mvn pre-integration-test -Pintegration-tests    # Start server & install browsers  
mvn integration-test -Pintegration-tests        # Run tests (requires server running)
mvn post-integration-test -Pintegration-tests   # Stop server
```

**Maven Integration Lifecycle:**
1. **initialize**: Cleanup any existing processes on ports 8080, 9001, 9999
2. **pre-integration-test**: Starts Spring Boot server on port 8080 (JMX on 9999) + installs Playwright browsers
3. **integration-test**: Runs Playwright tests against running server
4. **post-integration-test**: Stops Spring Boot server

## Troubleshooting

### Port Conflicts
If you encounter `Port already in use: 9001` errors:

```bash
# Manual cleanup (if automated cleanup fails)
./scripts/cleanup-ports.sh

# Or manually kill processes:
lsof -ti :8080 | xargs kill -9  # Kill Spring Boot HTTP server
lsof -ti :9001 | xargs kill -9  # Kill JMX default port
lsof -ti :9999 | xargs kill -9  # Kill JMX configured port
```

## Test Configuration

- **Base URL**: `http://localhost:8080` (Spring Boot server)
- **Browsers**: Chrome, Firefox, Safari/Webkit
- **Server**: Automatically starts Spring Boot before tests
- **Reports**: Generated in `src/main/frontend/playwright-report/`

## Test Coverage

### Frontend Integration
- ✅ Page loads without JavaScript errors
- ✅ Navigation between all major pages (Season, Agenda, Players, Statistics)
- ✅ Mobile responsive layout testing
- ✅ Console error detection and reporting

### API Integration  
- ✅ API endpoint availability and response validation
- ✅ Error handling for failed API calls
- ✅ Route interception and request verification
- ✅ Frontend-backend communication testing

### User Workflows
- ✅ Player management workflows (add/view players)
- ✅ Match scheduling workflows (friendly/league matches)
- ✅ Season management navigation
- ✅ Statistics viewing and interaction
- ✅ Cross-device compatibility testing

## Development

Tests are designed to be:
- **Robust**: Handle dynamic content and async operations
- **Cross-browser**: Work consistently across Chrome, Firefox, Safari
- **Maintainable**: Clear test structure with good error reporting
- **Fast**: Parallelized execution with efficient wait strategies

The tests automatically start the Spring Boot server before execution and will wait for it to be ready before running the test suite.
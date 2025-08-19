import { test, expect } from '@playwright/test';

test.describe('Basic Setup Validation', () => {

  test('should connect to Spring Boot server', async ({ request }) => {
    // Verify server is running and responds
    const response = await request.get('/rest/seasons/current');
    expect(response.ok()).toBeTruthy();
    expect(response.status()).toBe(200);
    
    const contentType = response.headers()['content-type'];
    expect(contentType).toContain('application/json');
  });

  test('should load main page successfully', async ({ page }) => {
    await page.goto('/');
    
    // Wait for page to fully load
    await page.waitForLoadState('networkidle');
    
    // Verify page loaded without errors (actual title is "PES 5")
    await expect(page).toHaveTitle(/PES 5/);
    
    // Verify basic page structure is present
    await expect(page.locator('body')).toBeVisible();
  });

  test('should handle API endpoints correctly', async ({ page }) => {
    let apiError = false;
    
    // Listen for any network errors
    page.on('response', response => {
      if (!response.ok() && response.url().includes('/rest/')) {
        apiError = true;
      }
    });
    
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    
    // Verify no API errors occurred during page load
    expect(apiError).toBeFalsy();
  });
});
import { test, expect } from '@playwright/test';

test.describe('API Integration Tests', () => {

  test('should load current season from API', async ({ page }) => {
    // Intercept API calls to verify they're being made
    let currentSeasonCalled = false;
    await page.route('**/rest/seasons/current', route => {
      currentSeasonCalled = true;
      route.continue();
    });

    await page.goto('/');
    await page.waitForLoadState('networkidle');
    
    // Verify API call was made
    expect(currentSeasonCalled).toBe(true);
    
    // Check that season data is displayed
    await expect(page.locator('text=Season')).toBeVisible();
  });

  test('should load players from API', async ({ page }) => {
    let playersApiCalled = false;
    await page.route('**/rest/players-stats', route => {
      playersApiCalled = true;
      route.continue();
    });

    await page.goto('/');
    await page.click('a[href="/players"]');
    await page.waitForLoadState('networkidle');
    
    // Verify API call was made
    expect(playersApiCalled).toBe(true);
  });

  test('should load match dates from API', async ({ page }) => {
    let matchDatesApiCalled = false;
    await page.route('**/rest/match-dates/friendly/next', route => {
      matchDatesApiCalled = true;
      route.continue();
    });

    await page.goto('/');
    await page.click('a[href="/agenda"]');
    await page.waitForLoadState('networkidle');
    
    // Verify API call was made
    expect(matchDatesApiCalled).toBe(true);
  });

  test('should handle API error gracefully', async ({ page }) => {
    // Mock API to return error
    await page.route('**/rest/seasons/current', route => {
      route.fulfill({
        status: 500,
        contentType: 'application/json',
        body: JSON.stringify({ error: 'Internal Server Error' })
      });
    });

    await page.goto('/');
    await page.waitForLoadState('networkidle');
    
    // Application should still load without crashing
    await expect(page.locator('body')).toBeVisible();
  });
});
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
    
    // Check that season data is displayed (target the heading, not the navigation link)
    await expect(page.locator('h2:has-text("Season")')).toBeVisible();
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

  test('should correctly display player icons based on API data structure', async ({ page }) => {
    // Mock players-stats API with correct field names
    await page.route('**/rest/players-stats', route => {
      route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: JSON.stringify([
          {
            name: "Test League GK",
            email: "test.league.gk@test.com", 
            isPlayerLeague: true,
            isGoalkeeper: true,
            nbSeasons: 1,
            nbMatches: 5
          },
          {
            name: "Test League Player",
            email: "test.league@test.com",
            isPlayerLeague: true, 
            isGoalkeeper: false,
            nbSeasons: 1,
            nbMatches: 3
          },
          {
            name: "Test Regular GK",
            email: "test.gk@test.com",
            isPlayerLeague: false,
            isGoalkeeper: true, 
            nbSeasons: 1,
            nbMatches: 2
          },
          {
            name: "Test Regular Player",
            email: "test.regular@test.com",
            isPlayerLeague: false,
            isGoalkeeper: false,
            nbSeasons: 1, 
            nbMatches: 1
          }
        ])
      });
    });

    await page.goto('/');
    await page.click('a[href="/players"]');
    await page.waitForLoadState('networkidle');

    // Verify API structure mapping works correctly
    // League goalkeeper should show both icons ⭐ 🥅
    await expect(page.locator('span', { hasText: 'Test League GK ⭐' })).toBeVisible();
    await expect(page.locator('span', { hasText: 'Test League GK ⭐ 🥅' })).toBeVisible();
    
    // League player should show only star ⭐
    await expect(page.locator('span', { hasText: 'Test League Player ⭐' })).toBeVisible();
    
    // Regular goalkeeper should show only goalkeeper icon 🥅
    await expect(page.locator('span', { hasText: 'Test Regular GK 🥅' })).toBeVisible();
    
    // Regular player should show no icons
    const regularPlayer = page.locator('span', { hasText: 'Test Regular Player' });
    await expect(regularPlayer).toBeVisible();
    await expect(regularPlayer).not.toContainText('⭐');
    await expect(regularPlayer).not.toContainText('🥅');

    // Verify league player count is correct
    await expect(page.locator('h3')).toContainText('2 League Players');
  });
});
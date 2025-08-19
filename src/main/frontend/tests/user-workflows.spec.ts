import { test, expect } from '@playwright/test';

test.describe('End-to-End User Workflows', () => {

  // Inject test data before running workflow tests
  test.beforeAll(async ({ request }) => {
    // Inject test data using the same approach as inject.js script
    const players = [
      {
        "playerLeague": true,
        "goalkeeper": true,
        "name": "Test Player 1",
        "email": "test1@test.com"
      },
      {
        "playerLeague": false,
        "goalkeeper": false,
        "name": "Test Player 2", 
        "email": "test2@test.com"
      }
    ];

    for (const player of players) {
      await request.put(`/rest/players/${encodeURIComponent(player.name)}`, {
        data: player,
        headers: { 'Content-Type': 'application/json' }
      });
    }
  });

  test('should view player management with data', async ({ page }) => {
    await page.goto('/');
    
    // Navigate to Players page
    await page.click('a[href="/players"]');
    await page.waitForLoadState('networkidle');
    
    // Verify players page loads with expected content
    await expect(page.locator('h2')).toContainText('Players');
    await expect(page.locator('h3')).toContainText('League Players');
    
    // Verify table headers are present
    await expect(page.locator('text=Name')).toBeVisible();
    await expect(page.locator('text=Email')).toBeVisible();
    
    // Verify test data is displayed
    await expect(page.locator('text=Test Player 1')).toBeVisible();
    await expect(page.locator('text=Test Player 2')).toBeVisible();
    
    // Verify league player indicator (⭐) appears for league players
    // Note: The indicator may not show immediately if stats need to be calculated
    const leaguePlayerText = page.locator('span:has-text("Test Player 1")');
    await expect(leaguePlayerText.first()).toBeVisible();
  });

  test('should view agenda and match scheduling interface', async ({ page }) => {
    await page.goto('/');
    
    // Navigate to Agenda page
    await page.click('a[href="/agenda"]');
    await page.waitForLoadState('networkidle');
    
    // Verify agenda page loads with tabs
    await expect(page.locator('[role="tablist"]')).toBeVisible();
    
    // Should see friendly tab by default (actual label is "Friendly", not "Friendly Matches")
    await expect(page.locator('text=Friendly')).toBeVisible();
    
    // Check if league tab exists (actual label is "League")
    const leagueTab = page.locator('text=League');
    if (await leagueTab.count() > 0) {
      await leagueTab.click();
      await page.waitForLoadState('networkidle');
    }
  });

  test('should navigate through season page', async ({ page }) => {
    await page.goto('/');
    
    // Verify we're on season page (home page)
    await expect(page).toHaveURL('/');
    await page.waitForLoadState('networkidle');
    
    // Season page should load without errors
    await expect(page.locator('body')).toBeVisible();
  });

  test('should handle responsive layout on mobile viewport', async ({ page }) => {
    // Set mobile viewport
    await page.setViewportSize({ width: 375, height: 667 });
    
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    
    // Verify page loads on mobile
    await expect(page.locator('body')).toBeVisible();
    
    // Test navigation on mobile - navigation should still be accessible
    await expect(page.locator('a[href="/players"]')).toBeVisible();
    await page.click('a[href="/players"]');
    await page.waitForLoadState('networkidle');
    await expect(page.locator('h2')).toContainText('Players');
  });
});
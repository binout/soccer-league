import { test, expect } from '@playwright/test';

test.describe('End-to-End User Workflows', () => {

  // Inject test data before running workflow tests
  test.beforeAll(async ({ request }) => {
    // Inject test data using the same approach as inject.js script
    const players = [
      {
        "isPlayerLeague": true,
        "isGoalkeeper": true,
        "name": "League Goalkeeper",
        "email": "league.gk@test.com"
      },
      {
        "isPlayerLeague": true,
        "isGoalkeeper": false,
        "name": "League Player",
        "email": "league@test.com"
      },
      {
        "isPlayerLeague": false,
        "isGoalkeeper": true,
        "name": "Regular Goalkeeper",
        "email": "gk@test.com"
      },
      {
        "isPlayerLeague": false,
        "isGoalkeeper": false,
        "name": "Regular Player",
        "email": "regular@test.com"
      }
    ];

    for (const player of players) {
      await request.put(`/rest/players/${encodeURIComponent(player.name)}`, {
        data: player,
        headers: { 'Content-Type': 'application/json' }
      });
    }
  });

  test('should view player management with correct icons', async ({ page }) => {
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
    
    // Verify test data is displayed (using more specific selectors)
    await expect(page.locator('span:has-text("League Goalkeeper")')).toBeVisible();
    await expect(page.locator('span:has-text("League Player ⭐")')).toBeVisible();
    await expect(page.locator('span:has-text("Regular Goalkeeper")')).toBeVisible();
    await expect(page.locator('span:has-text("Regular Player")')).toBeVisible();
    
    // Verify player icons are displayed correctly
    // League Goalkeeper should have both ⭐ and 🥅 (note: goalkeeper icon has leading space)
    const leagueGk = page.locator('span').filter({ hasText: 'League Goalkeeper' });
    await expect(leagueGk).toContainText('⭐');
    await expect(leagueGk).toContainText('🥅');
    
    // League Player should have only ⭐
    const leaguePlayer = page.locator('span').filter({ hasText: 'League Player' }).first();
    await expect(leaguePlayer).toContainText('⭐');
    await expect(leaguePlayer).not.toContainText('🥅');
    
    // Regular Goalkeeper should have only 🥅
    const regularGk = page.locator('span').filter({ hasText: 'Regular Goalkeeper' });
    await expect(regularGk).toContainText('🥅');
    await expect(regularGk).not.toContainText('⭐');
    
    // Regular Player should have no icons
    const regularPlayer = page.locator('span').filter({ hasText: 'Regular Player' }).first();
    await expect(regularPlayer).not.toContainText('⭐');
    await expect(regularPlayer).not.toContainText('🥅');
    
    // Verify league player count is correct (should show "2 League Players")
    await expect(page.locator('h3')).toContainText('2 League Players');
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
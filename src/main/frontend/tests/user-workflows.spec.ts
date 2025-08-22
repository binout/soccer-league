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
    
    // Verify players page loads with expected content (h1 for main heading, h5 for subtitle)
    await expect(page.locator('h1')).toContainText('Players');
    await expect(page.locator('h5')).toContainText('League Players');
    
    // Verify table headers are present
    await expect(page.locator('text=Name')).toBeVisible();
    await expect(page.locator('text=Email')).toBeVisible();
    
    // Verify test data is displayed with new MUI Table design
    // Use more specific selectors to avoid multiple matches
    await expect(page.locator('tbody tr').filter({ hasText: 'League Goalkeeper' })).toBeVisible();
    await expect(page.locator('tbody tr').filter({ hasText: 'League Player' })).toBeVisible();
    await expect(page.locator('tbody tr').filter({ hasText: 'Regular Goalkeeper' })).toBeVisible();
    await expect(page.locator('tbody tr').filter({ hasText: 'Regular Player' })).toBeVisible();
    
    // Verify player icons are displayed as MUI Chips
    // League Goalkeeper should have both chips visible (star and goalkeeper)
    const leagueGkRow = page.locator('tr').filter({ hasText: 'League Goalkeeper' });
    await expect(leagueGkRow.locator('.MuiChip-label:has-text("⭐")')).toBeVisible(); // Star chip
    await expect(leagueGkRow.locator('.MuiChip-label:has-text("🥅")')).toBeVisible(); // Goalkeeper chip
    
    // League Player should have only star chip
    const leaguePlayerRow = page.locator('tr').filter({ hasText: 'League Player' });
    await expect(leaguePlayerRow.locator('.MuiChip-label:has-text("⭐")')).toBeVisible(); // Star chip
    await expect(leaguePlayerRow.locator('.MuiChip-label:has-text("🥅")')).toHaveCount(0); // No goalkeeper chip
    
    // Regular Goalkeeper should have only goalkeeper chip
    const regularGkRow = page.locator('tr').filter({ hasText: 'Regular Goalkeeper' });
    await expect(regularGkRow.locator('.MuiChip-label:has-text("🥅")')).toBeVisible(); // Goalkeeper chip
    await expect(regularGkRow.locator('.MuiChip-label:has-text("⭐")')).toHaveCount(0); // No star chip
    
    // Regular Player should have no chips
    const regularPlayerRow = page.locator('tr').filter({ hasText: 'Regular Player' });
    await expect(regularPlayerRow.locator('.MuiChip-label')).toHaveCount(0); // No chips
    
    // Verify league player count is correct (changed from h3 to h5)
    await expect(page.locator('h5')).toContainText('2 League Players');
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
    
    // Test mobile navigation - hamburger menu should be visible
    await expect(page.locator('button[aria-label="Toggle navigation menu"]')).toBeVisible();
    
    // Click hamburger menu to open mobile navigation
    await page.click('button[aria-label="Toggle navigation menu"]');
    
    // Wait for menu to open and navigation links to be accessible
    await expect(page.locator('a[href="/players"]')).toBeVisible();
    await page.click('a[href="/players"]');
    await page.waitForLoadState('networkidle');
    await expect(page.locator('h1')).toContainText('Players');
  });
});
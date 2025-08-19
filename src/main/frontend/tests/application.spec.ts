import { test, expect } from '@playwright/test';

test.describe('Application Load and Navigation Tests', () => {
  
  test('should load the main page without JavaScript errors', async ({ page }) => {
    // Listen for console errors
    const consoleMessages: string[] = [];
    page.on('console', msg => {
      if (msg.type() === 'error') {
        consoleMessages.push(msg.text());
      }
    });

    // Navigate to the main page
    await page.goto('/');
    
    // Wait for page to load
    await page.waitForLoadState('networkidle');
    
    // Check that page loaded successfully (actual title is "PES 5")
    await expect(page).toHaveTitle(/PES 5/);
    
    // Verify no console errors
    expect(consoleMessages).toHaveLength(0);
  });

  test('should navigate to Season page', async ({ page }) => {
    await page.goto('/');
    
    // Click on Season navigation link (Home page)
    await page.click('a[href="/"]');
    
    // Verify we're on the season page (by checking URL)
    await expect(page).toHaveURL('/');
  });

  test('should navigate to Agenda page', async ({ page }) => {
    await page.goto('/');
    
    // Click on Agenda navigation link  
    await page.click('a[href="/agenda"]');
    
    // Verify we're on the agenda page
    await expect(page).toHaveURL('/agenda');
  });

  test('should navigate to Players page', async ({ page }) => {
    await page.goto('/');
    
    // Click on Players navigation link
    await page.click('a[href="/players"]');
    
    // Verify we're on the players page and content is loaded
    await expect(page).toHaveURL('/players');
    await expect(page.locator('h2')).toContainText('Players');
  });
});
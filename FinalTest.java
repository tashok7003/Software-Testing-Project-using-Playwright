package Playwright;

import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitForSelectorState;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;

public class FinalTest {

    private static List<TestResult> testResults = new ArrayList<>();

    public static void main(String[] args) {
        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        BrowserContext context = browser.newContext();
        Page page = context.newPage();

        // Start tracing before the tests
        context.tracing().start(new Tracing.StartOptions()
            .setScreenshots(true)
            .setSnapshots(true)
            .setSources(true));

        try {
            runTest(() -> loginTest(page), "Login Test", "Navigate to login page, enter credentials, and log in.");
            //runTest(() -> detectAndInteractWithMenus(page), "Menu Interaction Test", "Detect and interact with menu items.");
            runTest(() -> addToCartTest(page), "Add to Cart Test", "Add an item to the cart.");
            runTest(() -> checkoutTest(page), "Checkout Test", "Complete the checkout process.");
            System.out.println("All tests completed.");
        } catch (Exception e) {
            System.err.println("Test suite failed: " + e.getMessage());
        } finally {
            // Stop tracing after the tests
            context.tracing().stop(new Tracing.StopOptions().setPath(Paths.get("trace.zip")));
            browser.close();
            playwright.close();
            exportTestResultsToExcel();
        }
    }

    public static void runTest(Testable testable, String testName, String processDescription) {
        Instant start = Instant.now();
        try {
            testable.run();
            Instant end = Instant.now();
            Duration duration = Duration.between(start, end);
            testResults.add(new TestResult(testName, "Passed", duration, processDescription, ""));
            System.out.println(testName + ": Passed");
        } catch (Exception e) {
            Instant end = Instant.now();
            Duration duration = Duration.between(start, end);
            String failureReason = e.getMessage();
            testResults.add(new TestResult(testName, "Failed", duration, processDescription, failureReason));
            System.out.println(testName + ": Failed");
        }
    }

    public static void loginTest(Page page) throws InterruptedException {
        Instant start = Instant.now();
        page.navigate("https://www.saucedemo.com/");
        page.getByPlaceholder("Username").fill("standard_user");
        page.getByPlaceholder("Password").fill("secret_sauce");
        page.locator("[data-test=login-button]").click();

        // Wait for navigation to complete
        page.waitForURL("https://www.saucedemo.com/inventory.html");
        Thread.sleep(1000);  // Slow down for visibility
        if (!page.url().equals("https://www.saucedemo.com/inventory.html")) {
            throw new RuntimeException("Login test failed!");
        }
        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        testResults.add(new TestResult("Login Test - Fill credentials", "Passed", duration, "Filled in username and password, clicked login.", ""));
    }

    public static void detectAndInteractWithMenus(Page page) throws InterruptedException {
        Instant start = Instant.now();
        // Example of detecting and interacting with a menu
        Locator menuButton = page.locator("#react-burger-menu-btn");
        menuButton.click();

        // Ensure the menu is visible
        page.locator(".bm-menu").waitFor();

        Locator menuItems = page.locator("nav a.bm-item.menu-item");
        int itemCount = menuItems.count();
        testResults.add(new TestResult("Menu Interaction - Found items", "Passed", Duration.between(start, Instant.now()), "Detected menu items.", "Found " + itemCount + " menu items."));
        
        for (int i = 0; i < itemCount; i++) {
            Locator menuItem = menuItems.nth(i);
            String itemText = menuItem.innerText();
            System.out.println("Interacting with menu item: " + itemText);

            try {
                // Make sure the element is in the viewport before clicking
                menuItem.scrollIntoViewIfNeeded();
                menuItem.click();

                // Wait for a short duration to observe the interaction
                page.waitForTimeout(1000);

                // Navigate back if necessary
                if (itemText.equals("All Items")) {
                    page.navigate("https://www.saucedemo.com/inventory.html");
                } else if (itemText.equals("About")) {
                    page.navigate("https://www.saucedemo.com/");
                } else if (itemText.equals("Logout")) {
                    page.navigate("https://www.saucedemo.com/");
                    loginTest(page);
                }
                testResults.add(new TestResult("Menu Interaction - " + itemText, "Passed", Duration.between(start, Instant.now()), "Interacted with menu item: " + itemText, ""));
            } catch (Exception e) {
                testResults.add(new TestResult("Menu Interaction - " + itemText, "Failed", Duration.between(start, Instant.now()), "Attempted to interact with menu item: " + itemText, e.getMessage()));
            }
        }
    }

    public static void addToCartTest(Page page) throws InterruptedException {
        Instant start = Instant.now();
        page.navigate("https://www.saucedemo.com/inventory.html");
        page.locator("[data-test=\"add-to-cart-sauce-labs-backpack\"]").click();
        Locator cartBadge = page.locator(".shopping_cart_badge");

        // Ensure the cart badge becomes visible
        cartBadge.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        Thread.sleep(1000);  // Slow down for visibility
        if (!cartBadge.isVisible()) {
            throw new RuntimeException("Add to cart test failed!");
        }
        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        testResults.add(new TestResult("Add to Cart Test - Add item", "Passed", duration, "Added item to cart.", ""));
    }

    public static void checkoutTest(Page page) throws InterruptedException {
        Instant start = Instant.now();
        page.locator(".shopping_cart_badge").click();
        page.locator("[data-test=\"checkout\"]").click();
        page.getByPlaceholder("First Name").fill("Peetla");
        page.getByPlaceholder("Last Name").fill("Srihari");
        page.locator("[data-test=\"postalCode\"]").fill("560083");
        page.locator("[data-test=\"continue\"]").click();

        // Wait for navigation to complete
        page.waitForURL("https://www.saucedemo.com/checkout-step-two.html");
        Thread.sleep(1000);  // Slow down for visibility
        if (!page.url().equals("https://www.saucedemo.com/checkout-step-two.html")) {
            throw new RuntimeException("Checkout test failed at step one!");
        }
        page.locator("[data-test='finish']").click();
        testResults.add(new TestResult("Checkout Test - Fill information", "Passed", Duration.between(start, Instant.now()), "Filled in personal information for checkout.", ""));

        // Wait for navigation to complete
        page.waitForURL("https://www.saucedemo.com/checkout-complete.html");
        Thread.sleep(1000);  // Slow down for visibility
        if (!page.url().equals("https://www.saucedemo.com/checkout-complete.html")) {
            throw new RuntimeException("Checkout test failed at step two!");
        }
        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        testResults.add(new TestResult("Checkout Test - Complete purchase", "Passed", duration, "Completed purchase successfully.", ""));
    }

    public static void exportTestResultsToExcel() {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Test Results");

        String[] headers = {"Test Case", "Status", "Duration (ms)", "Process Description", "Reason/Details"};
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        int rowNum = 1;
        for (TestResult result : testResults) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(result.testCase);
            row.createCell(1).setCellValue(result.status);
            row.createCell(2).setCellValue(result.duration.toMillis());
            row.createCell(3).setCellValue(result.processDescription);
            row.createCell(4).setCellValue(result.details);
        }

        try (FileOutputStream fileOut = new FileOutputStream("final_test_results.xlsx")) {
            workbook.write(fileOut);
            System.out.println("Test results exported.");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to export test results: " + e.getMessage());
        }
    }

    public interface Testable {
        void run() throws Exception;
    }

    public static class TestResult {
        String testCase;
        String status;
        Duration duration;
        String processDescription;
        String details;

        TestResult(String testCase, String status, Duration duration, String processDescription, String details) {
            this.testCase = testCase;
            this.status = status;
            this.duration = duration;
            this.processDescription = processDescription;
            this.details = details;
        }
    }
}

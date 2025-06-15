package testCases;


import org.openqa.selenium.By; // Import By class
import org.openqa.selenium.support.ui.ExpectedConditions; // Import ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait; // Import WebDriverWait
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObjects.DiaryPage;
import pageObjects.SummaryPage;
import utilities.ExtentReportManager;

import java.time.Duration; // Import Duration

import java.util.HashSet;
import java.util.Set;

public class SummaryPageTest extends BaseTest {

    @Test(priority = 3, description = "Verifies summary statistics on the Summary page")
    public void verifySummaryData() {
        test = ExtentReportManager.getReport().createTest("Summary Page Data Verification");
        logger.info("Starting verifySummaryData test.");

        // Switch to the Summary page tab
        getDriver().switchTo().window(summaryPageWindowHandle);

        // *** ADD THIS EXPLICIT WAIT HERE ***
        // Wait for a critical element on the Summary page to be visible before interacting.
        // The 'good' element itself is a good candidate, or any other element that signifies
        // the page is ready.
        try {
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(15)); // Increased wait time
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody/tr[1]/td[3]")));
            logger.info("Successfully waited for 'good' element to be visible on Summary Page.");
        } catch (org.openqa.selenium.TimeoutException e) {
            logger.error("Timeout waiting for 'good' element on Summary Page: " + e.getMessage());
            Assert.fail("Summary page did not load or 'good' element not visible within timeout.");
        }
        // **********************************

        SummaryPage summary = new SummaryPage(getDriver());

        Assert.assertEquals(summary.getGoodCount(), 6, "Good dreams count mismatch.");
        logger.info("Good dreams count verified: " + summary.getGoodCount());

        Assert.assertEquals(summary.getBadCount(), 4, "Bad dreams count mismatch.");
        logger.info("Bad dreams count verified: " + summary.getBadCount());

        Assert.assertEquals(summary.getTotalCount(), 10, "Total dreams count mismatch.");
        logger.info("Total dreams count verified: " + summary.getTotalCount());

        Assert.assertEquals(summary.getRecurringCount(), 2, "Recurring dreams count mismatch.");
        logger.info("Recurring dreams count verified: " + summary.getRecurringCount());

        // --- Bonus: Verify Recurring Dreams Logic (from dreams-diary.html) ---
        logger.info("Verifying recurring dreams logic from Diary page data.");
        // Switch to Diary page to get dream names
        getDriver().switchTo().window(diaryPageWindowHandle);

        // Add a wait here too, as you're switching back to a previously loaded tab
        try {
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
            // Wait for a known element on the Diary page, e.g., the table rows
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table//tbody//tr")));
            logger.info("Successfully waited for Diary page table rows to be visible.");
        } catch (org.openqa.selenium.TimeoutException e) {
            logger.error("Timeout waiting for Diary page elements: " + e.getMessage());
            Assert.fail("Diary page elements not visible after switching back.");
        }

        DiaryPage diary = new DiaryPage(getDriver());

        Set<String> allDreamNames = new HashSet<>();
        Set<String> recurringDreamNames = new HashSet<>();

        for (int i = 1; i <= diary.getRowCount(); i++) {
            String dreamName = diary.getDreamName(i);
            if (!allDreamNames.add(dreamName)) { // If add returns false, it's a duplicate (recurring)
                recurringDreamNames.add(dreamName);
            }
        }

        Set<String> expectedRecurring = new HashSet<>();
        expectedRecurring.add("Flying over mountains");
        expectedRecurring.add("Lost in maze");

        Assert.assertEquals(recurringDreamNames.size(), expectedRecurring.size(), "Count of actual recurring dreams does not match expected.");
        Assert.assertTrue(recurringDreamNames.containsAll(expectedRecurring) && expectedRecurring.containsAll(recurringDreamNames),
                "Actual recurring dreams do not match expected recurring dreams.");

        logger.info("Verified recurring dreams: " + recurringDreamNames);
        // Switch back to summary page if needed for further actions (optional)
        // If there are no more assertions on the SummaryPage, no need to switch back.
        // getDriver().switchTo().window(summaryPageWindowHandle);
        logger.info("Finished verifySummaryData test.");
    }
}
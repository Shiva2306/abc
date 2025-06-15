




package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;
import pageObjects.DiaryPage; // Corrected from DairyPage
import utilities.ExtentReportManager;

import java.util.HashSet;
import java.util.Set;

public class DiaryPageTest extends BaseTest {

    @Test(priority = 2, description = "Validates data on the Dream Diary page")
    public void verifyDiaryPageData() {
        test = ExtentReportManager.getReport().createTest("Dream Diary Page Data Validation");
        logger.info("Starting verifyDiaryPageData test.");

        // Switch to the Diary page tab
        getDriver().switchTo().window(diaryPageWindowHandle);
        DiaryPage diary = new DiaryPage(getDriver());

        Assert.assertEquals(diary.getRowCount(), 10, "Row count mismatch on Diary page.");
        logger.info("Verified 10 rows are present in the Diary table.");

        Assert.assertTrue(diary.allRowsHaveData(), "Not all rows are fully filled on Diary page.");
        logger.info("All rows have complete data on Diary page.");

        Assert.assertTrue(diary.onlyGoodOrBadDreams(), "Invalid dream types found on Diary page.");
        logger.info("All dream types are Good or Bad only on Diary page.");

        logger.info("Finished verifyDiaryPageData test.");
    }
}